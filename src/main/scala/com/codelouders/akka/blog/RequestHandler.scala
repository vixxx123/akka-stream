package com.codelouders.akka.blog

import akka.NotUsed
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source}

// Type of sink should be probably something else. I dont know what you want to send downsream so it is what it is for now
class RequestHandler(sink: Sink[String, NotUsed])(implicit am: ActorMaterializer) {

  import akka.http.scaladsl.model._
  import akka.http.scaladsl.server.Directives._

  val route =
    path("hello") {
      get {
        // this might be body from request or msg you create based on request params etc
        Source.single("YO").runWith(sink)
        complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>Say hello to akka-http</h1>"))
      }
    }
}
