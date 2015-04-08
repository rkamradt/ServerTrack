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

/**
 * In this project, two web API endpoints are necessary. They are:
 * 
 * 1. Record load for a given server
 * This should take a:
 *                 • server name (string)
 *                 • CPU load (double)
 *                 • RAM load (double)
 * And apply the values to an in-memory model used to provide the data in endpoint #2. 
 * 
 * 2. Display loads for a given server
 * This should return data (if it has any) for the given server:
 *                 • A list of the average load values for the last 60 minutes broken down by minute
 *                 • A list of the average load values for the last 24 hours broken down by hour
 * 
 * Question for requirement source, can the number of minutes and hours be modified? consider this
 * possibility for increase opportunity for code-reuse.
 *
 * @author randalkamradt
 * @since 1.0
 */
public interface ServerTrack {
    /**
     * The number of buckets returned from getLoad in the hourly buckets. 
     */
    public static final int HOUR_BUCKETS = 24;
    /**
     * The number of buckets return from getLoad in the minute buckets.
     */
    public static final int MINUTE_BUCKETS = 60;
    /**
     * 
     * Record the cpu load and ram load of a server
     * 
     * @param serverName the name of the server
     * @param loadValue the loads
     * @param timestamp
     * @return An AverageLoadValues bean with the averages filled in
     */
    LoadValue record(String serverName, LoadValue loadValue, long timestamp);
    /**
     * 
     * get the average load in minute and hour buckets for a server
     * 
     * @param serverName the name of the server
     * @param end the end time for the buckets (highest value)
     * @return An AverageLoadValues bean with the averages filled in
     */
    AverageLoadValues getLoad(String serverName, long end);
    
}
