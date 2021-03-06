package com.twitter.finagle.http.javaapi;

import java.net.InetAddress;
import java.net.InetSocketAddress;

import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.jboss.netty.handler.codec.http.HttpVersion;

import com.twitter.finagle.Service;
import com.twitter.finagle.builder.ServerBuilder;
import com.twitter.finagle.http.Http;
import com.twitter.util.Future;

public final class HttpServerTest {

  private HttpServerTest() { }

  private static void runServer() {
    Service<HttpRequest, HttpResponse> service =
      new Service<HttpRequest, HttpResponse>() {
        public Future<HttpResponse> apply(HttpRequest request) {
          HttpResponse httpResponse = new DefaultHttpResponse(
            HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
          // Respond right away.
          httpResponse.setContent(ChannelBuffers.wrappedBuffer("yo".getBytes()));

          Future<HttpResponse> future = Future.value(httpResponse);
          return future;
        }
      };

    ServerBuilder.safeBuild(
      service,
      ServerBuilder.get()
      .codec(Http.get())
      .bindTo(new InetSocketAddress(InetAddress.getLoopbackAddress(), 0))
      .name("HttpServer"));
  }

  public static void main(String[] args) {
    runServer();
  }
}
