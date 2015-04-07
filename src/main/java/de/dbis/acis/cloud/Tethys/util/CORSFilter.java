package de.dbis.acis.cloud.Tethys.util;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

@Provider
public class CORSFilter implements ContainerResponseFilter {
	

    public static String VALID_METHODS = "DELETE, HEAD, GET, OPTIONS, POST, PUT";


    @Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
		responseContext.getHeaders().add("Access-Control-Allow-Origin", "*");
		responseContext.getHeaders().add("Access-Control-Allow-Methods",VALID_METHODS);
		responseContext.getHeaders().add("Access-Control-Allow-Headers","Accept, Content-Type, Origin, X-Auth-Token");
		
	}
	
}