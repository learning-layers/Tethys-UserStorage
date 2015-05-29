package de.dbis.acis.cloud.TethysUserStorage;

import java.util.ResourceBundle;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.wordnik.swagger.config.ConfigFactory;
import com.wordnik.swagger.config.ScannerFactory;
import com.wordnik.swagger.config.SwaggerConfig;
import com.wordnik.swagger.jaxrs.config.DefaultJaxrsScanner;
import com.wordnik.swagger.jaxrs.reader.DefaultJaxrsApiReader;
import com.wordnik.swagger.reader.ClassReaders;

/**
 * This WebListener configures the Swagger part of this Web Application.
 * Swagger helps exploring our API easily via a dynamic web frontend.
 * 
 * @author Gordon Lawrenz <lawrenz@dbis.rwth-aachen.de>
 */
@WebListener
public class SwaggerJaxrsListener implements ServletContextListener {

	//Gets the swagger configuration from the swagger.properties
	ResourceBundle swaggerResourceBundle = ResourceBundle.getBundle("swagger");

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		System.out.println("Swagger starts...");
		SwaggerConfig swaggerConfig = new SwaggerConfig();
		ConfigFactory.setConfig(swaggerConfig);
		swaggerConfig.setBasePath(
				"http://"+swaggerResourceBundle.getString("swaggerWebApplicationHost")
				+":"		+swaggerResourceBundle.getString("swaggerWebApplicationPort")
				+"/"		+swaggerResourceBundle.getString("swaggerWebApplicationName")
		);
		swaggerConfig.setApiVersion("1.0.0");
		ScannerFactory.setScanner(new DefaultJaxrsScanner());
		ClassReaders.setReader(new DefaultJaxrsApiReader());
		System.out.println("Swagger started!");
		System.out.println("");
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		System.out.println("Swagger destroyed");
	}

}
