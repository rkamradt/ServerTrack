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

@Path("/servertrack")
public class ServerTrackRest {

    @GET
    @Path("/{server}")
    @Produces("application/json")
    public Response getLoad(@PathParam("server") String server) {
        ServerTrack serverTrack = ServerTrackSingleton.getTheServerTrack();
        AverageLoadValues value = serverTrack.getLoad(server, new Date().getTime());
        return Response.ok().entity(value).build();
    }

    @POST
    @Produces("application/json")
    @Consumes("application/json")
    @Path("/{server}")
    public Response record(@PathParam("server") String server, LoadValue input) {
        ServerTrack serverTrack = ServerTrackSingleton.getTheServerTrack();
        LoadValue lv = serverTrack.record(server, input); 
        return Response.ok().entity(lv).build();
    }
}

