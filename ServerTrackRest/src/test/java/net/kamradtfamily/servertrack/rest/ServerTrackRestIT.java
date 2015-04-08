package net.kamradtfamily.servertrack.rest;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.ws.rs.core.Response;
import net.kamradtfamily.servertrack.spi.AverageLoadValues;
import net.kamradtfamily.servertrack.spi.LoadValue;

import org.apache.cxf.jaxrs.client.WebClient;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.MappingJsonFactory;
import org.junit.BeforeClass;
import org.junit.Test;

public class ServerTrackRestIT {
    private static String endpointUrl;
    Random rand = new Random(new Date().getTime());

    @BeforeClass
    public static void beforeClass() {
        endpointUrl = System.getProperty("service.url");
    }
        /**
     * 
     * test POST methods
     * 
     * @throws Exception 
     */
    @Test
    public void testPost() throws Exception {
        List<Object> providers = new ArrayList<>();
        providers.add(new org.codehaus.jackson.jaxrs.JacksonJsonProvider());
        double cpuLoad = rand.nextDouble()+1;
        double ramLoad = rand.nextDouble()+1;
        LoadValue inputBean = new LoadValue(cpuLoad, ramLoad);
        WebClient client = WebClient.create(endpointUrl + "/servertrack/posttest", providers);
        Response r = client.accept("application/json")
            .type("application/json")
            .post(inputBean);
        assertEquals(Response.Status.OK.getStatusCode(), r.getStatus());
        MappingJsonFactory factory = new MappingJsonFactory();
        JsonParser parser = factory.createJsonParser((InputStream)r.getEntity());
        LoadValue output = parser.readValueAs(LoadValue.class);
        assertEquals(cpuLoad, output.getCpuLoad(),0.0);
        assertEquals(ramLoad, output.getRamLoad(),0.0);
        assertTrue(output.getTimestamp() != 0);
    }
    /**
     * 
     * test POST and GET methods
     * 
     * @throws Exception 
     */
    @Test
    public void testPostAndGet() throws Exception {
        List<Object> providers = new ArrayList<>();
        providers.add(new org.codehaus.jackson.jaxrs.JacksonJsonProvider());
        final String serverName = "testserver";
        double cpuLoad = rand.nextDouble()+1;
        double ramLoad = rand.nextDouble()+1;
        LoadValue inputBean = new LoadValue(cpuLoad, ramLoad);
        WebClient client = WebClient.create(endpointUrl + "/servertrack/" + serverName, providers);
        Response r = client.accept("application/json")
            .type("application/json")
            .post(inputBean);
        assertEquals(Response.Status.OK.getStatusCode(), r.getStatus());
        MappingJsonFactory factory = new MappingJsonFactory();
        JsonParser parser = factory.createJsonParser((InputStream)r.getEntity());
        LoadValue output = parser.readValueAs(LoadValue.class);
        assertEquals(cpuLoad, output.getCpuLoad(),0.0);
        assertEquals(ramLoad, output.getRamLoad(),0.0);
        assertTrue(output.getTimestamp() != 0);
        client = WebClient.create(endpointUrl + "/servertrack/" + serverName);
        r = client.accept("application/json").get();
        assertEquals(Response.Status.OK.getStatusCode(), r.getStatus());
        parser = factory.createJsonParser((InputStream)r.getEntity());
        AverageLoadValues alv = parser.readValueAs(AverageLoadValues.class);
        int firstIndex = 0; // check in first and second buckets in case of border condition
        if(alv.getByHour().get(0).getCpuLoad() == 0.0) {
            firstIndex++;
        }
        LoadValue lv = alv.getByHour().get(firstIndex);
        assertEquals(cpuLoad, lv.getCpuLoad(), 0.0);
        assertEquals(ramLoad, lv.getRamLoad(), 0.0);
        
        firstIndex = 0; // check in first and second buckets in case of border condition
        if(alv.getByHour().get(0).getCpuLoad() == 0.0) {
            firstIndex++;
        }
        lv = alv.getByHour().get(firstIndex);
        assertEquals(cpuLoad, lv.getCpuLoad(), 0.0);
        assertEquals(ramLoad, lv.getRamLoad(), 0.0);
    }

}
