package com.codelouders.akka.blog

import akka.NotUsed
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.{ActorMaterializer, Attributes}
import akka.stream.scaladsl.{Flow, MergeHub, Sink}

object RequestMergerer extends App{

  implicit val ac = ActorSystem()
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = ac.dispatcher


  val runningStreamAsSink =
    MergeHub
      .source(256)
      .via(commonForAllRequestsLogic())
      .to(Sink  .ignore)
    .run()

  val routingHandler = new RequestHandler(runningStreamAsSink)
  val bindingFuture = Http().bindAndHandle(routingHandler.route, "localhost", 8080)



  def commonForAllRequestsLogic(): Flow[String, String, NotUsed] =
    Flow[String]
      .log("flow-logic")
      .addAttributes(Attributes.logLevels(
        onElement = Attributes.LogLevels.Info))

}
