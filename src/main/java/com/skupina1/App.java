package com.skupina1;

import com.skupina1.resource.RideResource;
import jakarta.enterprise.inject.se.SeContainer;
import jakarta.enterprise.inject.se.SeContainerInitializer;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.net.URI;

public class App {
    public static final String BASE_URI = "http://0.0.0.0:8080/";

    public static void main(String[] args) {

        ResourceConfig rc = new ResourceConfig().packages("com.skupina1.resource");

        HttpServer server = GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);

        try {
            server.start();
            System.out.println("Notification service running at " + BASE_URI);
            Thread.currentThread().join(); // Keep main thread alive
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            server.shutdown();
        }
    }
}
