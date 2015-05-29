package de.dbis.acis.cloud.TethysUserStorage.util;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
//import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.mysql.jdbc.AbandonedConnectionCleanupThread;

/**
 * This class is here to prevent a memory leak, because tomcat can't stop them jdbc-thread(mysqlconnection).
 * 
 * @author Gordon Lawrenz <lawrenz@dbis.rwth-aachen.de>
 */
@WebListener
public class ContainerContextClosedHandler implements ServletContextListener {
    //private static final Logger logger = Logger.getLogger(ContainerContextClosedHandler.class.getName());

	/* (non-Javadoc)
	 * Initializes the Container Context.
	 * 
     * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
     */
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        // nothing to do here
    	System.out.println("Initialize Container Context");
    }

    /* (non-Javadoc)
     * Clean up at destroy
     * 
     * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
     */
    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    	System.out.println("Destroy Container Context");
        Enumeration<Driver> drivers = DriverManager.getDrivers();     

        Driver driver = null;

        // clear drivers
        while(drivers.hasMoreElements()) {
            try {
                driver = drivers.nextElement();
                DriverManager.deregisterDriver(driver);

            } catch (SQLException ex) {
                // deregistration failed, might want to do something, log at the very least
            }
        }

        // MySQL driver leaves around a thread. This static method cleans it up.
        try {
            AbandonedConnectionCleanupThread.shutdown();
        } catch (InterruptedException e) {
            // again failure, not much you can do
        }
    }

}
