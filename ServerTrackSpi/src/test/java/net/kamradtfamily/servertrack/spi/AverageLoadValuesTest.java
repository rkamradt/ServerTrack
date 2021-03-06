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

import java.util.List;
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
public class AverageLoadValuesTest {
    final String serverName = "testserver";
    AverageLoadValues instance;
    
    public AverageLoadValuesTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        instance = new AverageLoadValues(serverName);
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getAverageLoadByMinute method, of class AverageLoadValues.
     */
    @Test
    public void testGetByMinute() {
        System.out.println("getAverageLoadByMinute");
        List<LoadValue> result = instance.getByMinute();
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    /**
     * Test of getAverageLoadByHour method, of class AverageLoadValues.
     */
    @Test
    public void testGetByHour() {
        System.out.println("getAverageLoadByHour");
        List<LoadValue> result = instance.getByHour();
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    /**
     * Test of getServerName method, of class AverageLoadValues.
     */
    @Test
    public void testGetServerName() {
        System.out.println("getServerName");
        String expResult = serverName;
        String result = instance.getServerName();
        assertEquals(expResult, result);
    }
    
}
