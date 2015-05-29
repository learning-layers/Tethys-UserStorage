package de.dbis.acis.cloud.TethysUserStorage.util;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

/**
 * This is the Cross-Origin Resource Sharing (CORS) class.
 * It enables the usage of Cross-Origin-Requests for browsers and webclients.
 * We explicitely need this for the swagger frontend.
 * 
 * @author Gordon Lawrenz <lawrenz@dbis.rwth-aachen.de>
 */
@Provider
public class CORSFilter implements ContainerResponseFilter {
	
    /**
     * Contains all valid methods for http requests to this server 
     */
    public static String VALID_METHODS = "DELETE, HEAD, GET, OPTIONS, POST, PUT, COPY";

    /* (non-Javadoc)
     * @see javax.ws.rs.container.ContainerResponseFilter#filter(javax.ws.rs.container.ContainerRequestContext, javax.ws.rs.container.ContainerResponseContext)
     */
    @Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
		responseContext.getHeaders().add("Access-Control-Allow-Origin", "*");
		responseContext.getHeaders().add("Access-Control-Allow-Methods",VALID_METHODS);
		responseContext.getHeaders().add("Access-Control-Allow-Headers","Accept, Content-Type, Origin, X-Auth-Token");
	}
	
}