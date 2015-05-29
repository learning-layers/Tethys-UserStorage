package de.dbis.acis.cloud.TethysUserStorage.services.proxy.oidc;

import java.util.ResourceBundle;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import de.dbis.acis.cloud.TethysUserStorage.entity.LDAP.LDAPUserInfo;

/**
 * Interface to mock proxies for the OpenID Connect API. This class just creates a default user.
 * 
 * @author Gordon Lawrenz <lawrenz@dbis.rwth-aachen.de>
 */
public class MockOidcP implements OidcP {

	/**
	 * Gets configuration from properties file.
	 */
	ResourceBundle configRB = ResourceBundle.getBundle("config");
	
	/**
	 * Constructor of the MockOidcProxy
	 */
	public MockOidcP() {
		System.out.println("create MockOidcP");
	}
	
	/* (non-Javadoc)
	 * @see de.dbis.acis.cloud.TethysUserStorage.services.proxy.oidc.OidcP#verifyAccessToken(java.lang.String)
	 */
	@Override
	public Response verifyAccessToken(String accessToken) {
		if(configRB.getString("authToken").equals(accessToken)) {
			LDAPUserInfo test = new LDAPUserInfo();
			test.setName("test");
			return Response.status(Status.OK).entity(test).header("Content-Type", "application/json").build();
		} else {
			return Response.status(Status.UNAUTHORIZED).build();
		}
	}
}
