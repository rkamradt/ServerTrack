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
    private final static String servername = "testserver";
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
        System.out.println("record");
        String serverName = "";
        double cpuLoad = r.nextDouble()+1; // prevent zero load to help find first load in buckets
        double ramLoad = r.nextDouble()+1;
        ConncurentMemoryServerTrack instance = new ConncurentMemoryServerTrack();
        LoadValue ret = instance.record(serverName, new LoadValue(cpuLoad, ramLoad));
        assertEquals(cpuLoad, ret.getCpuLoad(), 0.0);
        assertEquals(ramLoad, ret.getRamLoad(), 0.0);
        AverageLoadValues alv = instance.getLoad(serverName, new Date().getTime());
        assertEquals(serverName, alv.getServerName());
        assertNotNull(alv.getByHour());
        assertNotNull(alv.getByMinute());
        assertEquals(ServerTrack.HOUR_BUCKETS, alv.getByHour().size());
        assertEquals(ServerTrack.MINUTE_BUCKETS, alv.getByMinute().size());
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
