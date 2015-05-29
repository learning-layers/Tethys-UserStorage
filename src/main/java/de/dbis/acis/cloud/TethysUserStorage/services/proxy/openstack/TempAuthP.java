package de.dbis.acis.cloud.TethysUserStorage.services.proxy.openstack;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Interface to create proxies for openstacks tempauth API
 * 
 * @author Gordon Lawrenz <lawrenz@dbis.rwth-aachen.de>
 */
@Path("/")
public interface TempAuthP {
	
	/**
	 * @param userName
	 * @param key
	 * @return
	 */
	@GET
	@Path("/auth/v1.0")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getToken(@HeaderParam("X-Auth-User") String userName, @HeaderParam("X-Auth-Key") String key);
	
}
