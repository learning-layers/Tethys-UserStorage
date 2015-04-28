package de.dbis.acis.cloud.Tethys.services.proxy.oidc;

import java.util.ResourceBundle;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import de.dbis.acis.cloud.Tethys.entity.LDAP.LDAPUserInfo;

public class MockOidcP implements OidcP {

	ResourceBundle configRB = ResourceBundle.getBundle("config");
	
	public MockOidcP() {
		System.out.println("create MockOidcP");
	}
	
	@Override
	public Response verifyAccessToken(String accessToken) {
		if(configRB.getString("authPW").equals(accessToken)) {
			LDAPUserInfo test = new LDAPUserInfo();
			test.setName("test");
			return Response.status(Status.OK).entity(test).build();
		} else {
			return Response.status(Status.UNAUTHORIZED).header("Content-Type", "application/json").build();
		}
	}
}
