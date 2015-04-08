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

import java.io.Serializable;
import java.util.Date;


/**
 *
 * A representation of load for a particular time
 * 
 * @author randalkamradt
 * @see ServerTrack
 * @since 1.0
 */
public class LoadValue implements Serializable {

    private long timestamp;
    private double cpuLoad;
    private double ramLoad;
    /**
     * Serialization version.
     */
    private static final long serialVersionUID = 1L;
    /**
     * empty constructor for use by JSON marshaller
     */
    public LoadValue() {
        
    }

    /**
     *
     * Create a load value with the cpu load, ram load and create a timestamp of
     * now
     *
     * @param cpuLoad the cpu load
     * @param ramLoad the ram load
     */
    public LoadValue(double cpuLoad, double ramLoad) {
        this(cpuLoad, ramLoad, new Date().getTime());
    }

     /**
     *
     * Create a load value with the cpu load, ram load for a particular timestamp
     * 
     * This is used when restructuring timestamps for normalized buckets.
     *
     * @param cpuLoad the cpu load
     * @param ramLoad the ram load
     * @param timestamp
     */
    public LoadValue(double cpuLoad, double ramLoad, long timestamp) {
        this.timestamp = timestamp;
        this.cpuLoad = cpuLoad;
        this.ramLoad = ramLoad;
    }

    /**
     * 
     * @return the timestamp
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * @return the cpuLoad
     */
    public double getCpuLoad() {
        return cpuLoad;
    }

    /**
     * @return the ramLoad
     */
    public double getRamLoad() {
        return ramLoad;
    }
    /**
     * 
     * check if the timestamp is in the specified range, start being exclusive
     * and end being inclusive
     * 
     * @param start
     * @param end
     * @return true if the timestamp is in range
     */
    public boolean inRange(long start, long end) {
        return timestamp > start && timestamp <= end;
    }

    /**
     * @param timestamp the timestamp to set
     */
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * @param cpuLoad the cpuLoad to set
     */
    public void setCpuLoad(double cpuLoad) {
        this.cpuLoad = cpuLoad;
    }

    /**
     * @param ramLoad the ramLoad to set
     */
    public void setRamLoad(double ramLoad) {
        this.ramLoad = ramLoad;
    }

}
