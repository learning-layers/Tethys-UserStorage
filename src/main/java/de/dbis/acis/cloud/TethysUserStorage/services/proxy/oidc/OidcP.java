package de.dbis.acis.cloud.TethysUserStorage.services.proxy.oidc;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
public interface OidcP {

	/**
	 * @param accessToken
	 * @return
	 */
	@GET
	@Path("/o/oauth2/userinfo")
	@Produces(MediaType.APPLICATION_JSON)
	public Response verifyAccessToken(@HeaderParam("Authorization") String accessToken);
	
}
