/*
 * The MIT License
 *
 * Copyright 2015 randalkamradt.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package net.kamradtfamily.servertrackimpl;

import java.util.Date;
import java.util.Random;
import net.kamradtfamily.servertrack.spi.AverageLoadValues;
import net.kamradtfamily.servertrack.spi.LoadValue;
import net.kamradtfamily.servertrack.spi.ServerTrack;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author randalkamradt
 */
public class ConcurrentMemoryServerTrackTest {
    Random r = new Random(new Date().getTime());
    public ConcurrentMemoryServerTrackTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of record and getLoad method, of class ConncurentMemoryServerTrack.
     */
    @Test
    public void testSimpleRecordAndGetLoad() {
        System.out.println("simple record and getLoad");
        String serverName = "testserver1";
        double cpuLoad = r.nextDouble()+1; // prevent zero load to help find first load in buckets
        double ramLoad = r.nextDouble()+1;
        ConncurentMemoryServerTrack instance = new ConncurentMemoryServerTrack();
        LoadValue ret = instance.record(serverName, new LoadValue(cpuLoad, ramLoad), new Date().getTime());
        assertEquals(cpuLoad, ret.getCpuLoad(), 0.0);
        assertEquals(ramLoad, ret.getRamLoad(), 0.0);
        AverageLoadValues alv = instance.getLoad(serverName, new Date().getTime());
        assertEquals(serverName, alv.getServerName());
        assertNotNull(alv.getByHour());
        assertNotNull(alv.getByMinute());
        assertEquals(ServerTrack.HOUR_BUCKETS, alv.getByHour().size());
        assertEquals(ServerTrack.MINUTE_BUCKETS, alv.getByMinute().size());
        // because minute and hour buckets are not aligned, it will always land in first bucket (unless it takes a minute to proccess).
        LoadValue lv = alv.getByHour().get(0); 
        assertEquals(cpuLoad, lv.getCpuLoad(), 0.0);
        assertEquals(ramLoad, lv.getRamLoad(), 0.0);
        lv = alv.getByMinute().get(0);
        assertEquals(cpuLoad, lv.getCpuLoad(), 0.0);
        assertEquals(ramLoad, lv.getRamLoad(), 0.0);
        
    }
    /**
     * Test of record and getLoad method, with two servers.
     */
    @Test
    public void testTwoServerRecordAndGetLoad() {
        System.out.println("record and getLoad with two servers");
        ConncurentMemoryServerTrack instance = new ConncurentMemoryServerTrack();
        // record data for first server
        String serverName1 = "testserver1";
        double cpuLoad1 = r.nextDouble()+1; // prevent zero load to help find first load in buckets
        double ramLoad1 = r.nextDouble()+1;
        instance.record(serverName1, new LoadValue(cpuLoad1, ramLoad1), new Date().getTime());
        // record data for second server
        String serverName2 = "testserver2";
        double cpuLoad2 = r.nextDouble()+1; // prevent zero load to help find first load in buckets
        double ramLoad2 = r.nextDouble()+1;
        instance.record(serverName2, new LoadValue(cpuLoad2, ramLoad2), new Date().getTime());
        // check first server results
        AverageLoadValues alv = instance.getLoad(serverName1, new Date().getTime());
        assertEquals(serverName1, alv.getServerName());
        assertNotNull(alv.getByHour());
        assertNotNull(alv.getByMinute());
        assertEquals(ServerTrack.HOUR_BUCKETS, alv.getByHour().size());
        assertEquals(ServerTrack.MINUTE_BUCKETS, alv.getByMinute().size());
        LoadValue lv = alv.getByHour().get(0);
        System.out.println("cpu server1: " + lv.getCpuLoad() + " ram server1: " + lv.getRamLoad());
        assertEquals(cpuLoad1, lv.getCpuLoad(), 0.0);
        assertEquals(ramLoad1, lv.getRamLoad(), 0.0);
        
        // check second server results
        alv = instance.getLoad(serverName2, new Date().getTime());
        assertEquals(serverName2, alv.getServerName());
        assertNotNull(alv.getByHour());
        assertNotNull(alv.getByMinute());
        assertEquals(ServerTrack.HOUR_BUCKETS, alv.getByHour().size());
        assertEquals(ServerTrack.MINUTE_BUCKETS, alv.getByMinute().size());
        lv = alv.getByHour().get(0);
        System.out.println("cpu server2: " + lv.getCpuLoad() + " ram server2: " + lv.getRamLoad());
        assertEquals(cpuLoad2, lv.getCpuLoad(), 0.0);
        assertEquals(ramLoad2, lv.getRamLoad(), 0.0);
    }

}
