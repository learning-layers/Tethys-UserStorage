package de.dbis.acis.cloud.TethysUserStorage;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;

import de.dbis.acis.cloud.TethysUserStorage.util.InjectionBinder;

/**
 * Deployment class for a JAX-RS 2.0 (Jersey) application with Servlet 3.0.
 * 
 * @author Gordon Lawrenz <lawrenz@dbis.rwth-aachen.de>
 */
@ApplicationPath("/*")
public class WebApplication extends ResourceConfig {

	/**
	 * Constructor of our WebApplication.
	 */
	public WebApplication() {
		System.out.println("TethysUserStorage starts...");
		packages("de.dbis.acis.cloud.TethysUserStorage.resource");
		packages("de.dbis.acis.cloud.TethysUserStorage.services.storage");
		register(new InjectionBinder());
		register(de.dbis.acis.cloud.TethysUserStorage.util.GsonMessageBodyHandler.class);
		register(de.dbis.acis.cloud.TethysUserStorage.util.CORSFilter.class);
		register(de.dbis.acis.cloud.TethysUserStorage.util.ContainerContextClosedHandler.class);
		packages("com.wordnik.swagger.jaxrs.json");
	
		register(com.wordnik.swagger.jersey.listing.ApiListingResource.class);
		register(com.wordnik.swagger.jersey.listing.JerseyApiDeclarationProvider.class);
		register(com.wordnik.swagger.jersey.listing.ApiListingResourceJSON.class);
		register(com.wordnik.swagger.jersey.listing.JerseyResourceListingProvider.class);
		
		register(new LoggingFilter());
		property(ServerProperties.TRACING, "ALL");
		System.out.println("TethysUserStorage started!");
		System.out.println("");
	}
	
}


