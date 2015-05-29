package de.dbis.acis.cloud.TethysUserStorage.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.wordnik.swagger.annotations.*;

/**
 * This Resource matches the URL .../
 * 
 * @author Gordon Lawrenz <lawrenz@dbis.rwth-aachen.de>
 */
@Path("/")
@Api(value="", description = "Root Resource", position = 0)
public class RootR {

    /**
     * Returns information about this API
     * 
     * @return
     */
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@ApiOperation(value="Returns information about this API.")
	@ApiResponses( {
		@ApiResponse(code = 200, message = "OK")
	} )
	public String getRoot() {
		return "This is the Tethys User Storage API\n";
	}
	
}