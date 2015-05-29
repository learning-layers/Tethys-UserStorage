package de.dbis.acis.cloud.TethysUserStorage.services.proxy.openstack.swift.v1;

import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import de.dbis.acis.cloud.TethysUserStorage.util.COPY;

/**
 * JAX-RS annotated Interface for Openstack Swift. We will use it to generate Proxies.
 * 
 * All annotations are from the specification you can find under http://developer.openstack.org/api-ref-objectstorage-v1.html
 * This class reference to the first section "Object Storage API v1 (SUPPORTED)"
 * 
 * @author Gordon Lawrenz <lawrenz@dbis.rwth-aachen.de>
 */
@Path("/v1")
public interface SwiftP {

	// Accounts
	
	/**
	 * @param accountName
	 * @param Token
	 * @return
	 */
	@GET
	@Path("/AUTH_{account}")
	public Response getAccDetailsAndListContainers(
			@PathParam("account") String accountName,
			@HeaderParam("X-Auth-Token") String Token
			);
	
	/**
	 * @param accountName
	 * @param Token
	 * @return
	 */
	@POST
	@Path("/AUTH_{account}")
	public Response updateMetadata(
			@PathParam("account") String accountName,
			@HeaderParam("X-Auth-Token") String Token
			);
	
	/**
	 * @param accountName
	 * @param Token
	 * @return
	 */
	@HEAD
	@Path("/AUTH_{account}")
	public Response getAccMetadata(
			@PathParam("account") String accountName,
			@HeaderParam("X-Auth-Token") String Token
			);
	
	// Containers
	
	/**
	 * @param accountName
	 * @param container
	 * @param Token
	 * @return
	 */
	@GET
	@Path("/AUTH_{account}/{container}")
	public Response getContainerDetailsAndListObjects(
			@PathParam("account") String accountName,
			@PathParam("container") String container,
			@HeaderParam("X-Auth-Token") String Token
			);
	
	/**
	 * @param accountName
	 * @param container
	 * @param Token
	 * @return
	 */
	@PUT
	@Path("/AUTH_{account}/{container}")
	public Response createContainer(
			@PathParam("account") String accountName,
			@PathParam("container") String container,
			@HeaderParam("X-Auth-Token") String Token
			);
	
	/**
	 * @param accountName
	 * @param container
	 * @param Token
	 * @return
	 */
	@POST
	@Path("/AUTH_{account}/{container}")
	public Response udateMetadata(
			@PathParam("account") String accountName,
			@PathParam("container") String container,
			@HeaderParam("X-Auth-Token") String Token
			);
	
	/**
	 * @param accountName
	 * @param container
	 * @param Token
	 * @return
	 */
	@DELETE
	@Path("/AUTH_{account}/{container}")
	public Response deleteMetadata(
			@PathParam("account") String accountName,
			@PathParam("container") String container,
			@HeaderParam("X-Auth-Token") String Token
			);
	
	/**
	 * @param accountName
	 * @param container
	 * @param Token
	 * @return
	 */
	@HEAD
	@Path("/AUTH_{account}/{container}")
	public Response getContainerMetadata(
			@PathParam("account") String accountName,
			@PathParam("container") String container,
			@HeaderParam("X-Auth-Token") String Token
			);
	
	//Objects
	
	/**
	 * @param accountName
	 * @param container
	 * @param pathToObject
	 * @param Token
	 * @return
	 */
	@GET
	@Path("/AUTH_{account}/{container}/{object}")
	public Response getObjectContentAndMetadata(
			@PathParam("account") String accountName,
			@PathParam("container") String container,
			@PathParam("object") String pathToObject,
			@HeaderParam("X-Auth-Token") String Token
			);
	
	/**
	 * @param accountName
	 * @param container
	 * @param pathToObject
	 * @param Token
	 * @param inputStream
	 * @return
	 */
	@PUT
	@Path("/AUTH_{account}/{container}/{object}")
	@Consumes( { MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_FORM_URLENCODED } )
	public Response createOrReplaceObject(
			@PathParam("account") String accountName,
			@PathParam("container") String container,
			@PathParam("object") String pathToObject,
			@HeaderParam("X-Auth-Token") String Token,
			InputStream inputStream
			);
	
	/**
	 * @param accountName
	 * @param container
	 * @param pathToObject
	 * @param Token
	 * @param newDestination
	 * @return
	 */
	@COPY
	@Path("/AUTH_{account}/{container}/{object}")
	public Response copyObject(
			@PathParam("account") String accountName,
			@PathParam("container") String container,
			@PathParam("object") String pathToObject,
			@HeaderParam("X-Auth-Token") String Token,
			@HeaderParam("Destination") String newDestination
			);
	
	/**
	 * @param accountName
	 * @param container
	 * @param pathToObject
	 * @param Token
	 * @return
	 */
	@POST
	@Path("/AUTH_{account}/{container}/{object}")
	public Response udateMetadata(
			@PathParam("account") String accountName,
			@PathParam("container") String container,
			@PathParam("object") String pathToObject,
			@HeaderParam("X-Auth-Token") String Token
			);
	
	/**
	 * @param accountName
	 * @param container
	 * @param pathToObject
	 * @param Token
	 * @return
	 */
	@DELETE
	@Path("/AUTH_{account}/{container}/{object}")
	public Response deleteObject(
			@PathParam("account") String accountName,
			@PathParam("container") String container,
			@PathParam("object") String pathToObject,
			@HeaderParam("X-Auth-Token") String Token
			);
	
	/**
	 * @param accountName
	 * @param container
	 * @param pathToObject
	 * @param Token
	 * @return
	 */
	@HEAD
	@Path("/AUTH_{account}/{container}/{object}")
	public Response getObjectMetadata(
			@PathParam("account") String accountName,
			@PathParam("container") String container,
			@PathParam("object") String pathToObject,
			@HeaderParam("X-Auth-Token") String Token
			);
	
}
