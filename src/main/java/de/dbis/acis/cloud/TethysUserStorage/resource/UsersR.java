package de.dbis.acis.cloud.TethysUserStorage.resource;

import java.io.IOException;
import java.util.ResourceBundle;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import de.dbis.acis.cloud.TethysUserStorage.entity.LDAP.LDAPUserInfo;
import de.dbis.acis.cloud.TethysUserStorage.services.interfaces.StorageSI;
import de.dbis.acis.cloud.TethysUserStorage.services.proxy.oidc.OidcP;

/**
 * This SubResource matches the URL /i5Cloud/users
 * 
 * @author Gordon Lawrenz <lawrenz@dbis.rwth-aachen.de>
 */
@Path("/users")
@Api(value="/users", description = "Operations about users", position = 3)
public class UsersR {

	private ResourceBundle configRB = ResourceBundle.getBundle("config");
	private StorageSI storageService;
	private OidcP oidcP;
	
	/**
	 * @param storageService
	 * @param oidcP
	 */
	@Inject
	public UsersR(StorageSI storageService, OidcP oidcP) {
		this.storageService = storageService;
		this.oidcP = oidcP;
	}
	
	/**
	 * @param accessToken
	 * @return
	 */
	@GET
	@Path("/oauth")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Method in Testing - Verify LDAP Access")
	@ApiResponses( {
		@ApiResponse(code = 200, message = "OK")
	} )
	public Response verifyAccessToken(@HeaderParam("Authorization") String accessToken) {
		
		
		if(accessToken == null || accessToken.isEmpty()) {
			return Response.status(Status.UNAUTHORIZED).build();
		}
		
		Response proxyResponse = oidcP.verifyAccessToken(accessToken);
		if(proxyResponse.getStatusInfo()!=Status.OK) {
			return proxyResponse;
		}
		
		LDAPUserInfo user = (LDAPUserInfo) proxyResponse.getEntity();
		return Response.status(Status.OK).entity(user).build();
		
	}
	
	@POST
	@Path("/{userName}")
	@ApiOperation(value="Creates User with LDAP User Data")
	@ApiResponses( {
		@ApiResponse(code = 201, message = "Created"),
		@ApiResponse(code = 202, message = "Accepted")
	} )
	public Response createStorage(@PathParam("userName") String userName, @HeaderParam("X-Admin-Token") String adminToken) throws IOException{
		
		if(adminToken == null || !adminToken.equals(configRB.getString("adminToken"))) {
			return Response.status(Status.UNAUTHORIZED).build();
		}
		
		storageService.createDir(userName, null);
		
		return Response.status(Status.CREATED).build();
	}
}