package de.dbis.acis.cloud.Tethys.resource;


import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.client.ClientResponse;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import de.dbis.acis.cloud.Tethys.client.OpenstackClient;
import de.dbis.acis.cloud.Tethys.entity.LDAP.LDAPUserInfo;

/**
 * This SubResource matches the URL /i5Cloud/users
 * 
 * @author Gordon Lawrenz <lawrenz@dbis.rwth-aachen.de>
 */
@Path("/users")
@Api(value="/users", description = "Operations about users", position = 3)
public class UsersR {

	/**
	 * @param accessToken
	 * @return
	 */
	@Path("/verifyAccessToken")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Method in Testing - Verify LDAP Access")
	@ApiResponses( {
		@ApiResponse(code = 200, message = "OK")
	} )
	public Response verifyAccessToken(@HeaderParam("Authorization") String accessToken) {
		
		ResponseBuilder r = null;
		
		if(accessToken == null || accessToken.isEmpty()) {
			return Response.status(Status.CONFLICT).build();
		}
		
		ClientResponse response = OpenstackClient.verifyAccessToken(accessToken);
		
		if(response.getStatusInfo()==Status.OK) {
			r = Response.status(Status.OK).entity(response.readEntity(LDAPUserInfo.class));
		} //else if(response.getStatusInfo()==Status.UNAUTHORIZED) {
			//r = Response.status(Status.UNAUTHORIZED).entity("TEST");
		 else {
			r = Response.status(Status.INTERNAL_SERVER_ERROR);
		}
		
		return r.build();
	}
	
}