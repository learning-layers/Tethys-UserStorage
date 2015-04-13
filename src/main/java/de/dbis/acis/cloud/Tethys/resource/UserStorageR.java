package de.dbis.acis.cloud.Tethys.resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.StreamingOutput;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import de.dbis.acis.cloud.Tethys.client.OpenstackClient;
import de.dbis.acis.cloud.Tethys.entity.LDAP.LDAPUserInfo;
import de.dbis.acis.cloud.Tethys.services.interfaces.StorageSI;


/**
 * This Resource matches the URL /Tethys/users/{sub}
 * 
 * @author Gordon Lawrenz <lawrenz@dbis.rwth-aachen.de> *
 */
@Path("/users/storage")
@Api(value="/users/storage", description = "User storage ")
public class UserStorageR {
	
	@Inject
	private StorageSI storageService;
	
	LDAPUserInfo ldapUser = null;
	
	final static boolean __debug = false;
	
	/**
	 * @param path
	 * @param is
	 * @return
	 * @throws IOException
	 */
	@POST
	@Path("/{path : .+}")
	@Consumes( { MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_FORM_URLENCODED } )
	@ApiOperation(value="posts file/substorage with specified pat")
	@ApiResponses( {
		@ApiResponse(code = 200, message = "OK")
	} )
	public Response postFileOrDir(@HeaderParam("Authorization") String accessToken, @PathParam("path") String path, InputStream is) throws IOException {

		if(__debug){
			if(accessToken == null || accessToken.isEmpty()) {
				return Response.status(Status.UNAUTHORIZED).build();
			}
			ldapUser = OpenstackClient.verifyAccessToken2(accessToken);
		} else {
			ldapUser = testUser();
		}
		if(storageService.checkPathExists(ldapUser.getName() + "/" + path)) {
			return Response.status(Status.CONFLICT).build();
		}
		if(is.available()==0){
			storageService.createDir(ldapUser.getName() + "/" + path);
		} else {
			storageService.createFile(is, ldapUser.getName() + "/" + path);
		}
		
		return Response.status(Status.CREATED).build();
		
	}
	
	/**
	 * @param path
	 * @return
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	@GET
	@Path("/{path : .*}")
	@ApiOperation(value="Get a list of all files in a sub-storage or a file with specified path")
	@ApiResponses( {
		@ApiResponse(code = 200, message = "OK")
	} )
	public Response getFiles(@HeaderParam("Authorization") String accessToken, @PathParam("path") final String path) throws ClassNotFoundException, IOException{

		StreamingOutput so = null;
		if(__debug){
			if(accessToken == null || accessToken.isEmpty()) {
				return Response.status(Status.UNAUTHORIZED).build();
			}
			ldapUser = OpenstackClient.verifyAccessToken2(accessToken);
		} else {
			ldapUser = testUser();
		}
		//check if file/dir exists
		if(!storageService.checkPathExists(ldapUser.getName() + "/" + path)) {
			return Response.status(Status.NOT_FOUND).build();
		}
		//go further and read dir/file
		so = new StreamingOutput(){

			@Override
			public void write(OutputStream os) throws IOException,
					WebApplicationException {
				storageService.getContent(os, ldapUser.getName() + "/" + path);	
			}
		};
		return Response.ok(so).build();
	}
	
	/**
	 * @param path
	 * @param is
	 * @return
	 * @throws IOException
	 */
	@PUT
	@Path("/{path : .+}")
	@Consumes( { MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_FORM_URLENCODED } )
	@ApiOperation(value="puts file/substorage with specified pat")
	@ApiResponses( {
		@ApiResponse(code = 200, message = "OK")
	} )
	public Response putFile(@HeaderParam("Authorization") String accessToken, @PathParam("path") String path, InputStream is) {
		

		if(__debug){
			if(accessToken == null || accessToken.isEmpty()) {
				return Response.status(Status.UNAUTHORIZED).build();
			}
			ldapUser = OpenstackClient.verifyAccessToken2(accessToken);
		} else {
			ldapUser = testUser();
		}
		
		//TODO check if dir with name
		storageService.overwriteFile(is, ldapUser.getName() + "/" + path);
		
		return Response.ok().build();
		
	}
	
	
	/**
	 * @param accessToken
	 * @param path
	 * @return
	 */
	@DELETE
	@Path("/{path : .+}")
	@ApiOperation(value="deletes files or dir")
	@ApiResponses( {
		@ApiResponse(code = 200, message = "OK")
	} )
	public Response deleteFileOrDir(@HeaderParam("Authorization") String accessToken, @PathParam("path") String path){
		
		if(__debug){
			if(accessToken == null || accessToken.isEmpty()) {
				return Response.status(Status.UNAUTHORIZED).build();
			}
			ldapUser = OpenstackClient.verifyAccessToken2(accessToken);
		} else {
			ldapUser = testUser();
		}
		
		if(!storageService.checkPathExists(ldapUser.getName() + "/" + path)) {
			return Response.status(Status.NOT_FOUND).build();
		}
		storageService.delete(ldapUser.getName() + "/" + path);
		
		return Response.ok().build();
	}
	
	
	@SuppressWarnings("unused")
	private boolean checkPath(String Path){
		
		//TODO checks path
		
		return true;
	}
	
	private LDAPUserInfo testUser(){
		LDAPUserInfo test = new LDAPUserInfo();
		test.setName("test");
		return test;
	}
}