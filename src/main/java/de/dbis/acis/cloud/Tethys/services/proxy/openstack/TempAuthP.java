package de.dbis.acis.cloud.Tethys.services.proxy.openstack;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;


@Path("/")
public interface TempAuthP {

	
	/**
	 * 
	 * @return
	 */
	@GET
	@Path("/auth/v1.0")
	public Response getToken();
	
}
