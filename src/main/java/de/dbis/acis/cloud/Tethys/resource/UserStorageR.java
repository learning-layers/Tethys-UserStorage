package de.dbis.acis.cloud.Tethys.resource;

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

import de.dbis.acis.cloud.Tethys.entity.LDAP.LDAPUserInfo;
import de.dbis.acis.cloud.Tethys.services.interfaces.StorageSI;
import de.dbis.acis.cloud.Tethys.services.proxy.oidc.OidcP;


/**
 * This Resource matches the URL /Tethys/users/{sub}
 * 
 * @author Gordon Lawrenz <lawrenz@dbis.rwth-aachen.de> *
 */
@Path("/users/storage")
@Api(value="/users/storage", description = "User storage ")
public class UserStorageR {
	
	private StorageSI storageService;
	private OidcP oidcP;
	
	/**
	 * @param storageService
	 * @param oidcP
	 */
	@Inject
	public UserStorageR(StorageSI storageService, OidcP oidcP) {
		this.storageService = storageService;
		this.oidcP = oidcP;
	}
	
	/**
	 * @param accessToken
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
	 * @param accessToken
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
	public Response getUserRoot(@HeaderParam("Authorization") String accessToken, @PathParam("path") final String path) {
		return getFiles(accessToken, path);
	}
	
	/**
	 * @param accessToken
	 * @param path
	 * @return
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	@GET
	@Path("/{path : .+}")
	@ApiOperation(value="Get a list of all files in a sub-storage or a file with specified path")
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
	 * @param accessToken
	 * @param path
	 * @return
	 * @throws IOException 
	 */
	@DELETE
	@Path("/{path : .+}")
	@ApiOperation(value="deletes files or dir")
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
	
	
	@SuppressWarnings("unused")
	private boolean checkPath(String Path){
		
		//TODO checks path
		
		return true;
	}

}