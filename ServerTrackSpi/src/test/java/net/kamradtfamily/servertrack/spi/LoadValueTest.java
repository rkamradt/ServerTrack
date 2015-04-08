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
package net.kamradtfamily.servertrack.spi;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Random;
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
public class LoadValueTest {
    Random r = new Random(new Date().getTime());
    
    public LoadValueTest() {
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
     * Test of creating a bean with specific values and now timestamp
     */
    @Test
    public void testCreateLoadNow() {
        System.out.println("addLoad");
        double cpuLoad = r.nextDouble();
        double ramLoad = r.nextDouble();
        LoadValue instance = new LoadValue(cpuLoad, ramLoad);
        assertEquals(cpuLoad, instance.getCpuLoad(), 0.0);
        assertEquals(ramLoad, instance.getRamLoad(), 0.0);
        assertNotNull(instance.getTimestamp()); // no test is made to ensure now
    }
    /**
     * Test of creating a bean with specific values and specific timestamp
     */
    @Test
    public void testCreateLoadSpecificTime() {
        System.out.println("addLoad");
        double cpuLoad = r.nextDouble();
        double ramLoad = r.nextDouble();
        long timestamp = new Date().getTime(); // just use now
        LoadValue instance = new LoadValue(cpuLoad, ramLoad, timestamp);
        assertEquals(cpuLoad, instance.getCpuLoad(), 0.0);
        assertEquals(ramLoad, instance.getRamLoad(), 0.0);
        assertEquals(timestamp, instance.getTimestamp());
    }

}
