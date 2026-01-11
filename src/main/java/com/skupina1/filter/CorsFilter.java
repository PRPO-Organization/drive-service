package com.skupina1.filter;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;

public class CorsFilter implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext request, ContainerResponseContext response){
        response.getHeaders().add(
                "Access-Control-Allow-Origin", "http://localhost:4200"
        );
        response.getHeaders().add(
                "Access-Control-Allow-Headers",
                "origin, content-type, accept, authorization"
        );
        response.getHeaders().add(
                "Access-Control-Allow-Methods",
                "GET, POST, PUT, DELETE, OPTIONS"
        );
        response.getHeaders().add(
                "Access-Control-Allow-Credentials", "true"
        );
    }
}
