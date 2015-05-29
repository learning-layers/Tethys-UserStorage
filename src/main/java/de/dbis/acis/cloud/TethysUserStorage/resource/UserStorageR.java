package de.dbis.acis.cloud.TethysUserStorage.resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileAlreadyExistsException;

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

import de.dbis.acis.cloud.TethysUserStorage.entity.LDAP.LDAPUserInfo;
import de.dbis.acis.cloud.TethysUserStorage.services.interfaces.StorageSI;
import de.dbis.acis.cloud.TethysUserStorage.services.proxy.oidc.OidcP;


/**
 * This Resource matches the URL .../users/storage
 * 
 * @author Gordon Lawrenz <lawrenz@dbis.rwth-aachen.de> *
 */
@Path("/users/storage")
@Api(value="/users/storage", description = "User storage resource.")
public class UserStorageR {
	
	private StorageSI storageService;
	private OidcP oidcP;
	
	/**
	 * Constructor of the UserStorageResource
	 * Uses Dependency Injection
	 * 
	 * @param storageService
	 * @param oidcP
	 */
	@Inject
	public UserStorageR(StorageSI storageService, OidcP oidcP) {
		this.storageService = storageService;
		this.oidcP = oidcP;
	}
	
	/**
	 * Creates a file or a directory with given path
	 * 
	 * @param accessToken
	 * @param path
	 * @param is
	 * @return
	 * @throws IOException
	 */
	@POST
	@Path("/{path : .+}")
	@Consumes( { MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_FORM_URLENCODED } )
	@ApiOperation(value="posts file or directory with given path.")
	@ApiResponses( {
		@ApiResponse(code = 200, message = "OK")
	} )
	public Response postFileOrDir(@HeaderParam("Authorization") String accessToken, @PathParam("path") String path, InputStream is) throws IOException {

		if(accessToken == null || accessToken.isEmpty()) {
			return Response.status(Status.UNAUTHORIZED).build();
		}
		
		Response proxyResponse = oidcP.verifyAccessToken(accessToken);
		if(proxyResponse.getStatusInfo()!=Status.OK) {
			return proxyResponse;
		}
		
		LDAPUserInfo user = (LDAPUserInfo) proxyResponse.getEntity();

		if(!storageService.checkPathExists(user.getName(), path)) {
			return Response.status(Status.CONFLICT).build();
		}
		if(is.available()==0){
			storageService.createDir(user.getName(), path);
		} else {
			try {
				storageService.createFile(is, user.getName(), path);
			} catch (FileAlreadyExistsException e) {
				// file already exists! return 409
				e.printStackTrace();
				return Response.status(Status.CONFLICT).build();
			}
		}
		
		return Response.status(Status.CREATED).build();
		
	}
	
	/**
	 * Get a list of all files or directories in the root of the container of a user.
	 * 
	 * @param accessToken
	 * @return
	 */
	@GET
	@Path("/")
	@ApiOperation(value="Get a list of all files or directories in the root of the container of a user.")
	@ApiResponses( {
		@ApiResponse(code = 200, message = "OK")
	} )
	public Response getUserRoot(@HeaderParam("Authorization") String accessToken) {
		StreamingOutput so = null;
		
		if(accessToken == null || accessToken.isEmpty()) {
			return Response.status(Status.UNAUTHORIZED).build();
		}
		
		Response proxyResponse = oidcP.verifyAccessToken(accessToken);
		if(proxyResponse.getStatusInfo()!=Status.OK) {
			return proxyResponse;
		}
		
		final LDAPUserInfo user = (LDAPUserInfo) proxyResponse.getEntity();
		
		//go further and read dir/file
		so = new StreamingOutput(){

			@Override
			public void write(OutputStream os) throws IOException,
					WebApplicationException {
				storageService.getContent(os, user.getName(), "");	
			}
		};
		
		//TODO doesn't look for file so can't get 404
		return Response.ok(so).build();
	}
	
	/**
	 * Get or a file a list of all files or directories in a directory in the container of a user found under given path
	 * @param accessToken
	 * @param path
	 * @return
	 * @throws ClassNotFoundException
	 */
	@GET
	@Path("/{path : .+}")
	@ApiOperation(value="Gets a file or a list of all files or directories in a directory in the container of a user found under given path.")
	@ApiResponses( {
		@ApiResponse(code = 200, message = "OK")
	} )
	public Response getFiles(@HeaderParam("Authorization") String accessToken, @PathParam("path") final String path) {

		StreamingOutput so = null;
		
		if(accessToken == null || accessToken.isEmpty()) {
			return Response.status(Status.UNAUTHORIZED).build();
		}
		
		Response proxyResponse = oidcP.verifyAccessToken(accessToken);
		if(proxyResponse.getStatusInfo()!=Status.OK) {
			return proxyResponse;
		}
		
		final LDAPUserInfo user = (LDAPUserInfo) proxyResponse.getEntity();
		
		//check if file/dir exists
		if(!storageService.checkPathExists(user.getName(), path)) {
			return Response.status(Status.NOT_FOUND).build();
		}
		//go further and read dir/file
		so = new StreamingOutput(){

			@Override
			public void write(OutputStream os) throws IOException,
					WebApplicationException {
				storageService.getContent(os, user.getName(), path);	
			}
		};
		
		//TODO doesn't look for file so can't get 404
		return Response.ok(so).build();
	}
	
	/**
	 * Creates and/or overwrites a file with given path.
	 * 
	 * @param path
	 * @param is
	 * @return
	 * @throws IOException
	 */
	@PUT
	@Path("/{path : .+}")
	@Consumes( { MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_FORM_URLENCODED } )
	@ApiOperation(value="Puts file with given path.")
	@ApiResponses( {
		@ApiResponse(code = 200, message = "OK")
	} )
	public Response putFile(@HeaderParam("Authorization") String accessToken, @PathParam("path") String path, InputStream is) throws IOException {

		if(accessToken == null || accessToken.isEmpty()) {
			return Response.status(Status.UNAUTHORIZED).build();
		}
		
		Response proxyResponse = oidcP.verifyAccessToken(accessToken);
		if(proxyResponse.getStatusInfo()!=Status.OK) {
			return proxyResponse;
		}
		
		LDAPUserInfo user = (LDAPUserInfo) proxyResponse.getEntity();
		
		//TODO check if dir with name
		storageService.overwriteFile(is, user.getName(), path);
		
		return Response.ok().build();
		
	}
	
	
	/**
	 * Deletes a file found under given path.
	 * 
	 * @param accessToken
	 * @param path
	 * @return
	 * @throws IOException 
	 */
	@DELETE
	@Path("/{path : .+}")
	@ApiOperation(value="Deletes a file found under given path.")
	@ApiResponses( {
		@ApiResponse(code = 200, message = "OK")
	} )
	public Response deleteFileOrDir(@HeaderParam("Authorization") String accessToken, @PathParam("path") String path) throws IOException{
		
		if(accessToken == null || accessToken.isEmpty()) {
			return Response.status(Status.UNAUTHORIZED).build();
		}
		
		Response proxyResponse = oidcP.verifyAccessToken(accessToken);
		if(proxyResponse.getStatusInfo()!=Status.OK) {
			return proxyResponse;
		}
		
		LDAPUserInfo user = (LDAPUserInfo) proxyResponse.getEntity();
		
		if(!storageService.checkPathExists(user.getName(), path)) {
			return Response.status(Status.NOT_FOUND).build();
		}
		storageService.delete(user.getName(), path);
		
		return Response.ok().build();
	}
	
	
	/**
	 * Should verify if the specified path is consistent. Maybe we will do this later with annotations!?
	 * 
	 * @param Path
	 * @return
	 */
	@SuppressWarnings("unused")
	private boolean checkPath(String Path){
		
		//TODO checks path
		
		return true;
	}

}