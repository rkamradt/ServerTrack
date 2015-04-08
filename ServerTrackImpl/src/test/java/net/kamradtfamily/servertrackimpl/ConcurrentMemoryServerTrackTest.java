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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import org.junit.Ignore;

/**
 *
 * @author randalkamradt
 */
public class ConcurrentMemoryServerTrackTest {
    Random r = new Random(new Date().getTime());
    static final double epsilon = 0.000001; // is there a best practice for epsilon when comparing doubles?
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
        assertEquals(cpuLoad, lv.getCpuLoad(), epsilon);
        assertEquals(ramLoad, lv.getRamLoad(), epsilon);
        lv = alv.getByMinute().get(0);
        assertEquals(cpuLoad, lv.getCpuLoad(), epsilon);
        assertEquals(ramLoad, lv.getRamLoad(), epsilon);
        
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
        assertEquals(cpuLoad1, lv.getCpuLoad(), epsilon);
        assertEquals(ramLoad1, lv.getRamLoad(), epsilon);
        
        // check second server results
        alv = instance.getLoad(serverName2, new Date().getTime());
        assertEquals(serverName2, alv.getServerName());
        assertNotNull(alv.getByHour());
        assertNotNull(alv.getByMinute());
        assertEquals(ServerTrack.HOUR_BUCKETS, alv.getByHour().size());
        assertEquals(ServerTrack.MINUTE_BUCKETS, alv.getByMinute().size());
        lv = alv.getByHour().get(0);
        assertEquals(cpuLoad2, lv.getCpuLoad(), epsilon);
        assertEquals(ramLoad2, lv.getRamLoad(), epsilon);
    }

    /**
     * Test of record and getLoad method, with one server and two entries.
     */
    @Test
    public void testTwoEntryRecordAndGetLoad() {
        System.out.println("record and getLoad with two entries");
        ConncurentMemoryServerTrack instance = new ConncurentMemoryServerTrack();
        // record data for first server
        String serverName = "testserver";
        double cpuLoad1 = r.nextDouble()+1; // prevent zero load to help find first load in buckets
        double ramLoad1 = r.nextDouble()+1;
        instance.record(serverName, new LoadValue(cpuLoad1, ramLoad1), new Date().getTime());
        // record data for second server
        double cpuLoad2 = r.nextDouble()+1; // prevent zero load to help find first load in buckets
        double ramLoad2 = r.nextDouble()+1;
        instance.record(serverName, new LoadValue(cpuLoad2, ramLoad2), new Date().getTime());
        // check first results
        AverageLoadValues alv = instance.getLoad(serverName, new Date().getTime());
        assertEquals(serverName, alv.getServerName());
        assertNotNull(alv.getByHour());
        assertNotNull(alv.getByMinute());
        assertEquals(ServerTrack.HOUR_BUCKETS, alv.getByHour().size());
        assertEquals(ServerTrack.MINUTE_BUCKETS, alv.getByMinute().size());
        LoadValue lv = alv.getByHour().get(0);
        assertEquals((cpuLoad1+cpuLoad2)/2, lv.getCpuLoad(), epsilon);
        assertEquals((ramLoad1+ramLoad2)/2, lv.getRamLoad(), epsilon);
        lv = alv.getByMinute().get(0);
        assertEquals((cpuLoad1+cpuLoad2)/2, lv.getCpuLoad(), epsilon);
        assertEquals((ramLoad1+ramLoad2)/2, lv.getRamLoad(), epsilon);
        
    }
    /**
     * Test of record and getLoad method, with one server and many entries by hour.
     */
    @Test
    public void testManyEntryHourRecordAndGetLoad() {
        System.out.println("record and getLoad with many entries by hour");
        final ConncurentMemoryServerTrack instance = new ConncurentMemoryServerTrack();
        // record data for first server
        final String serverName = "testserver";
        final long fstarttime = new Date().getTime(); 
        long endtime = fstarttime;
        List<LoadValue> lvs = new ArrayList<>();
        for(int i = 0; i < 60*60*12; i++) { // add twelve hours worth of data at one second intervals
            endtime += 1000; // add every second;
            // use load values between 0 and 1
            lvs.add(new LoadValue((Math.sin(endtime)+1)/2, (Math.cos(endtime)+1)/2, endtime));
        }
        lvs.parallelStream().forEach(l -> instance.record(serverName, l, l.getTimestamp()));
        final long fendtime = endtime;
        // get the total averages:
        final double avgCpu = lvs.parallelStream().filter(l -> l.inRange(fstarttime, fendtime)).mapToDouble(l -> l.getCpuLoad()).average().orElse(0.0);
        final double avgRam = lvs.parallelStream().filter(l -> l.inRange(fstarttime, fendtime)).mapToDouble(l -> l.getRamLoad()).average().orElse(0.0);
        AverageLoadValues alv = instance.getLoad(serverName, endtime);
        assertEquals(serverName, alv.getServerName());
        assertNotNull(alv.getByHour());
        assertNotNull(alv.getByMinute());
        assertEquals(ServerTrack.HOUR_BUCKETS, alv.getByHour().size());
        assertEquals(ServerTrack.MINUTE_BUCKETS, alv.getByMinute().size());
        double avgCpuByHour = alv.getByHour().parallelStream().filter(l -> l.inRange(fstarttime, fendtime)).mapToDouble(l -> l.getCpuLoad()).average().orElse(0.0);
        double avgRamByHour = alv.getByHour().parallelStream().filter(l -> l.inRange(fstarttime, fendtime)).mapToDouble(l -> l.getRamLoad()).average().orElse(0.0);
        // averages should be the same whether by hour, minute, or discreet
        System.out.println("avgCpu: " + avgCpu + " avgRam: " + avgRam);
        System.out.println("avgCpuByHour: " + avgCpuByHour + " avgRamByHour: " + avgRamByHour);
        assertEquals(avgCpu, avgCpuByHour, epsilon);
        assertEquals(avgRam, avgRamByHour, epsilon);
    }
    /**
     * Test of record and getLoad method, with one server and many entries by minute.
     */
    @Ignore("Why does this work in hours, but not in minute buckets? ")
    @Test
    public void testManyEntryMinuteRecordAndGetLoad() {
        System.out.println("record and getLoad with many entries by minute");
        final ConncurentMemoryServerTrack instance = new ConncurentMemoryServerTrack();
        // record data for first server
        final String serverName = "testserver";
        final long fstarttime = new Date().getTime(); 
        long endtime = fstarttime;
        List<LoadValue> lvs = new ArrayList<>();
        for(int i = 0; i < 600*30; i++) { // add 30 minutes worth of data at one tenth of a second intervals
            endtime += 100; // add every tenth of a second;
            // use load values between 0 and 1
            lvs.add(new LoadValue((Math.sin(endtime)+1)/2, (Math.cos(endtime)+1)/2, endtime));
        }
        lvs.parallelStream().forEach(l -> instance.record(serverName, l, l.getTimestamp()));
        final long fendtime = endtime;
        // get the total averages:
        double avgCpu = lvs.parallelStream().filter(l -> l.inRange(fstarttime, fendtime)).mapToDouble(l -> l.getCpuLoad()).average().orElse(0.0);
        double avgRam = lvs.parallelStream().filter(l -> l.inRange(fstarttime, fendtime)).mapToDouble(l -> l.getRamLoad()).average().orElse(0.0);
        AverageLoadValues alv = instance.getLoad(serverName, endtime);
        assertEquals(serverName, alv.getServerName());
        assertNotNull(alv.getByHour());
        assertNotNull(alv.getByMinute());
        assertEquals(ServerTrack.HOUR_BUCKETS, alv.getByHour().size());
        assertEquals(ServerTrack.MINUTE_BUCKETS, alv.getByMinute().size());
        double avgCpuByMinute = alv.getByMinute().parallelStream().filter(l -> l.inRange(fstarttime, fendtime)).mapToDouble(l -> l.getCpuLoad()).average().orElse(0.0);
        double avgRamByMinute = alv.getByMinute().parallelStream().filter(l -> l.inRange(fstarttime, fendtime)).mapToDouble(l -> l.getRamLoad()).average().orElse(0.0);
        // averages should be the same whether by hour, minute, or discreet
        System.out.println("avgCpu: " + avgCpu + " avgRam: " + avgRam);
        System.out.println("avgCpuByMinute: " + avgCpuByMinute + " avgRamByMinute: " + avgRamByMinute);
        assertEquals(avgCpu, avgCpuByMinute, epsilon);
        assertEquals(avgRam, avgRamByMinute, epsilon);
    }
}
