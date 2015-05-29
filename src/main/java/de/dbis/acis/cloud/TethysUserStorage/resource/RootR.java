package de.dbis.acis.cloud.TethysUserStorage.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.wordnik.swagger.annotations.*;


/**
 * This Resource matches the Root URI
 * 
 * @author Gordon Lawrenz <lawrenz@dbis.rwth-aachen.de>
 */
@Path("/")
@Api("")
public class RootR {

    /**
     * returns "Gives a description and a link where you can find more information about this API."
     * 
     * @return "Gives a description and a link where you can find more information about this API."
     */
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getRoot() {
		return "This is the Tethys User Storage API\n";
	}
	
}