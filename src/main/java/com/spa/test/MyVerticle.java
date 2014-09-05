package com.spa.test;

import org.vertx.java.core.Handler;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.eventbus.EventBus;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.core.sockjs.SockJSServer;
import org.vertx.java.platform.Verticle;

public class MyVerticle extends Verticle{
	
	public void start() {
		final EventBus eb = vertx.eventBus();
        // logger.info("Starting beautifier");
        HttpServer server = vertx.createHttpServer();
        server.requestHandler(new Handler<HttpServerRequest>() {			
			@Override
			public void handle(final HttpServerRequest request) {
				String reqPath = request.path();
				//String fileName = request.path();
				 if (reqPath.equals("/")) {
		             //fileName = "index.html";
		             request.response().sendFile("./web/index.html");
		         } else if (reqPath.equals("/test")) {
		        	 request.bodyHandler(new Handler<Buffer>() {					
		        		 @Override
						 public void handle(Buffer event) {
		        			 eb.send("navigation", new JsonObject().putString("path", request.path()));
						 }
					 });
		         } else {
		        	 request.response().sendFile("./web/" + reqPath);
		         }
				
				
			}
        });
        JsonArray inboundPermitted = new JsonArray();
        JsonObject addrss1 = new JsonObject().putString("address", "navigation");
        inboundPermitted.add(addrss1);
        
        JsonArray outboundPermitted = new JsonArray();
        
        SockJSServer sockJSServer = vertx.createSockJSServer(server);
        sockJSServer.bridge(new JsonObject().putString("prefix", "/eventbus"), inboundPermitted,
                outboundPermitted);
        server.listen(8080, "localhost");
        
        // deploying SPA verticle
        container.deployVerticle("web/js/spa-test.js");
	}
	
}
