package net.kamradtfamily.servertrack.rest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.Date;
import net.kamradtfamily.servertrack.spi.AverageLoadValues;
import net.kamradtfamily.servertrack.spi.LoadValue;
import net.kamradtfamily.servertrack.spi.ServerTrack;

/**
 * A RESTful Webservice object. Two entry points POST and GET. Both take a
 * servername as the last-leg of the path. POST creates a timestamped load 
 * entry, and GET will return a list of load entries in hourly and minute buckets.
 * 
 * @author randalkamradt
 * @since 1.0
 */
@Path("/servertrack")
public class ServerTrackRest {

    /**
     * Get the list of load entries for this server
     * @param server the server name
     * @return the JSON document with the lists of averaged loads.
     */
    @GET
    @Path("/{server}")
    @Produces("application/json")
    public Response getLoad(@PathParam("server") String server) {
        ServerTrack serverTrack = ServerTrackSingleton.getTheServerTrack();
        AverageLoadValues value = serverTrack.getLoad(server, new Date().getTime());
        return Response.ok().entity(value).build();
    }

    /**
     *
     * Add a single load record to the store.
     * 
     * @param server the name of the server
     * @param input a JSON object that has the cpu and ram load.
     * @return a JSON object that has the cpu and ram load, and a timestamp.
     */
    @POST
    @Produces("application/json")
    @Consumes("application/json")
    @Path("/{server}")
    public Response record(@PathParam("server") String server, LoadValue input) {
        ServerTrack serverTrack = ServerTrackSingleton.getTheServerTrack();
        LoadValue lv = serverTrack.record(server, input, new Date().getTime()); 
        return Response.ok().entity(lv).build();
    }
}

