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
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Simple Bean for containing the AverageLoadValues. No calculations are done
 * here. I could not make this a read-only bean since it needs to be serialized
 * into JSON
 * 
 * @author randalkamradt
 * @see ServerTrack
 * @since 1.0
 */
public class AverageLoadValues implements Serializable {
    private static final long serialVersionUID = 1L;
    private String serverName;
    private List<LoadValue> byHour = new ArrayList<>();
    private List<LoadValue> byMinute = new ArrayList<>();
    /**
     * empty constructor for use by JSON marshaller
     */
    public AverageLoadValues() {
        
    }
    /**
     * Create an AverageLoadValue bean for a server
     * @param serverName the name of the server
     */
    public AverageLoadValues(String serverName) {
        this.serverName = serverName;
    }
    /**
     * Get the server name
     * @return the server name
     */
    public String getServerName() {
        return serverName;
    }

    /**
     * Set the server name
     * @param serverName the serverName to set
     */
    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    /**
     * get the hour buckets
     * @return the byHour
     */
    public List<LoadValue> getByHour() {
        return byHour;
    }

    /**
     * set the hour buckets
     * @param byHour the byHour to set
     */
    public void setByHour(List<LoadValue> byHour) {
        this.byHour = byHour;
    }

    /**
     * get the minute buckets
     * @return the byMinute
     */
    public List<LoadValue> getByMinute() {
        return byMinute;
    }

    /**
     * set the minute buckets
     * @param byMinute the byMinute to set
     */
    public void setByMinute(List<LoadValue> byMinute) {
        this.byMinute = byMinute;
    }
}
