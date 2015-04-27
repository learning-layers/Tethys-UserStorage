package de.dbis.acis.cloud.Tethys;

import java.util.ResourceBundle;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.client.proxy.WebResourceFactory;

import de.dbis.acis.cloud.Tethys.services.LocalStorageS;
import de.dbis.acis.cloud.Tethys.services.SwiftStorageS;
import de.dbis.acis.cloud.Tethys.services.interfaces.StorageSI;
import de.dbis.acis.cloud.Tethys.services.proxy.openstack.TempAuthP;
import de.dbis.acis.cloud.Tethys.services.proxy.openstack.swift.v1.SwiftP;
import de.dbis.acis.cloud.Tethys.util.GsonMessageBodyHandler;

/**
 * HK2 Binding class. Which binds classes for Dependency Injection(DI).
 * See "HK2", "JSR-330", "DI" and "CDI" for more Information.
 * 
 * @author Gordon Lawrenz <lawrenz@dbis.rwth-aachen.de>
 */
public class TethysBinder extends AbstractBinder {
	
	ResourceBundle configRB = ResourceBundle.getBundle("config");
	ResourceBundle openstackRB = null;

	@Override
	protected void configure() {
		String storageVariable = configRB.getString("storageType");
		
		chooseStorage(storageVariable);
	}

	protected void chooseStorage(String storageVariable)  {
		switch(storageVariable){
			case "Local":
				System.out.println("Storage Service: Local Storage");
				bind(new LocalStorageS(configRB.getString("storageRootPath"))).to(StorageSI.class);
				break;
				
			case "Swift":
				System.out.println("Storage Service: Swift");
				
				openstackRB = ResourceBundle.getBundle("openstack");
				String swiftURL = openstackRB.getString("swiftURL");
				
				WebTarget oidcWebTarget = ClientBuilder.newClient().target(openstackRB.getString("swiftURL")).register(GsonMessageBodyHandler.class);
				bind(WebResourceFactory.newResource(TempAuthP.class, oidcWebTarget)).to(TempAuthP.class);
				
				WebTarget tempAuthWebTarget = ClientBuilder.newClient().target(swiftURL).register(GsonMessageBodyHandler.class);
				WebTarget swiftWebTarget = ClientBuilder.newClient().target(swiftURL).register(GsonMessageBodyHandler.class);
				
				bind(WebResourceFactory.newResource(TempAuthP.class, tempAuthWebTarget)).to(TempAuthP.class);
				bind(WebResourceFactory.newResource(SwiftP.class, swiftWebTarget )).to(SwiftP.class);
				
				bind(SwiftStorageS.class).to(StorageSI.class);
				break;
				
			default:
				System.out.println("Storage Service: "+storageVariable+" doesn't exist. Wrong Configuration!");
				throw new IllegalArgumentException("Storage Service: "+storageVariable+" doesn't exist. Wrong Configuration!");
		}
	}
	
}