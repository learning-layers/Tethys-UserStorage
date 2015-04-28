package de.dbis.acis.cloud.Tethys;

import java.util.ResourceBundle;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.client.proxy.WebResourceFactory;

import de.dbis.acis.cloud.Tethys.services.interfaces.StorageSI;
import de.dbis.acis.cloud.Tethys.services.proxy.oidc.MockOidcP;
import de.dbis.acis.cloud.Tethys.services.proxy.oidc.OidcP;
import de.dbis.acis.cloud.Tethys.services.proxy.openstack.TempAuthP;
import de.dbis.acis.cloud.Tethys.services.proxy.openstack.swift.v1.SwiftP;
import de.dbis.acis.cloud.Tethys.services.storage.LocalStorageS;
import de.dbis.acis.cloud.Tethys.services.storage.SwiftStorageS;
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
	ResourceBundle oidcRB = ResourceBundle.getBundle("oidc");

	/* (non-Javadoc)
	 * @see org.glassfish.hk2.utilities.binding.AbstractBinder#configure()
	 */
	@Override
	protected void configure() {
		String storageVariable = configRB.getString("storageType");
		String authVariable = configRB.getString("authType");
		System.out.println(storageVariable);
		System.out.println(authVariable);
		
		chooseStorage(storageVariable);
		chooseAuth(authVariable);
		
	}
	
	/**
	 * @param authVariable
	 */
	protected void chooseAuth(String authVariable){
		switch(authVariable){
			case "OIDC":
				System.out.println("create OidcP");
				WebTarget oidcWebTarget = ClientBuilder.newClient().target(oidcRB.getString("oidcURL")).register(GsonMessageBodyHandler.class);
				bind(WebResourceFactory.newResource(OidcP.class, oidcWebTarget)).to(OidcP.class);
				break;
				
			case "MockOIDC":
				bind(MockOidcP.class).to(OidcP.class);
				break;
				
			default:
				System.out.println("Auth Service: " + authVariable + " doesn't exist. Wrong Configuration!");
				throw new IllegalArgumentException("Auth Service: " + authVariable + " doesn't exist. Wrong Configuration!");
		}
	}

	/**
	 * @param storageVariable
	 */
	protected void chooseStorage(String storageVariable)  {
		switch(storageVariable){
			case "Local":
				bind(LocalStorageS.class).to(StorageSI.class);
				break;
				
			case "Swift":
				openstackRB = ResourceBundle.getBundle("openstack");
				String swiftURL = openstackRB.getString("swiftURL");
				
				WebTarget tempAuthWebTarget = ClientBuilder.newClient().target(swiftURL).register(GsonMessageBodyHandler.class);
				WebTarget swiftWebTarget = ClientBuilder.newClient().target(swiftURL).register(GsonMessageBodyHandler.class);
				
				bind(WebResourceFactory.newResource(TempAuthP.class, tempAuthWebTarget)).to(TempAuthP.class);
				bind(WebResourceFactory.newResource(SwiftP.class, swiftWebTarget)).to(SwiftP.class);
				
				bind(SwiftStorageS.class).to(StorageSI.class);
				break;
				
			default:
				System.out.println("Storage Service: " + storageVariable + " doesn't exist. Wrong Configuration!");
				throw new IllegalArgumentException("Storage Service: " + storageVariable + " doesn't exist. Wrong Configuration!");
		}
	}
	
}