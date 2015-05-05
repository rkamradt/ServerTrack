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

import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import net.kamradtfamily.servertrack.spi.AverageLoadValues;
import net.kamradtfamily.servertrack.spi.LoadValue;
import net.kamradtfamily.servertrack.spi.ServerTrack;

/**
 *
 * An implementation of ServerTrack that uses a simple in-memory scheme to store values
 * This implementation is thread-safe by using the ConcurrentHashMap, and ConcurrentLinkedQueue
 * 
 * @author randalkamradt
 * @see ServerTrack
 * @since 1.0
 */
public class ConcurentMemoryServerTrack implements ServerTrack {
    /**
     * number of milliseconds in an hour
     */
    public static final long HOUR_OFFSET = 1000*60*60;
    /**
     * number of milliseconds in a minute
     */
    public static final long MINUTE_OFFSET = 1000*60;
    /**
     * The map of server names and load values. This map is relatively static as it
     * is only modified when a new server is encountered, so the choice of concurrency type 
     * should have minimal impact.
     */
    private final Map<String, Queue<LoadValue>> loadValuesMap = new ConcurrentHashMap<>(); 
    /**
     * 
     * Add a record to the store
     * 
     * @param serverName the name of the server
     * @param input the input values in the form of a LoadValue object
     * @param timestamp the timestamp in milliseconds
     * @return 
     */
    @Override
    public LoadValue record(final String serverName, final LoadValue input, final long timestamp) {
        Queue<LoadValue> loadValues = loadValuesMap.get(serverName);
        if(loadValues == null) {
            loadValues = new ConcurrentLinkedQueue<>(); // use a deque since we are adding to the tail and removing from the head
            loadValuesMap.put(serverName, loadValues);
        }
        LoadValue loadValue = new LoadValue(input.getCpuLoad(), input.getRamLoad(), timestamp);
        loadValues.add(loadValue); 
        final long minTime = timestamp - ServerTrack.HOUR_BUCKETS*HOUR_OFFSET;
        while(loadValues.peek().getTimestamp() < minTime) { // we should be safe that there is one item on the queue, so it should never return null
            loadValues.poll(); // remove
        }
        return loadValue;
    }
    /**
     * 
     * Get an average in hourly and minute buckets for a server
     * 
     * @param serverName the name of the server
     * @param end the timestamp (usually now) to start the buckets
     * @return 
     */
    @Override
    public AverageLoadValues getLoad(final String serverName, final long end) {
        final AverageLoadValues ret = new AverageLoadValues(serverName);
        averageBuckets(end, loadValuesMap.get(serverName), BucketDuration.Hour, ret.getByHour());
        averageBuckets(end, loadValuesMap.get(serverName), BucketDuration.Minute, ret.getByMinute());
        return ret;
    }
    /**
     * An hour and minute enumeration to help in the averageBuckets method
     */
    private enum BucketDuration { Hour, Minute };
    /**
     * A helper method to fill the buckets
     * 
     * TODO: is there a more efficient way to pull the data from the lists? The 
     * current strategy iterates through the queue 84 times. How well does a 
     * Queue perform as a Stream? Need to investigate
     * 
     * Question: are the time buckets aligned with actual minutes and hours? If
     * so significant work will need to be done here (and in the tests to avoid 
     * border conditions). I choose not-aligned as that gives a consistent average
     * through all buckets, including the first and last which might otherwise be
     * truncated.
     * 
     * @param end the timestamp to start with (usually now)
     * @param lvs the load values to iterate through
     * @param bucketDuration Hour or Minute
     * @param averageLoad 
     */
    private void averageBuckets(final long end, final Queue<LoadValue> lvs, final BucketDuration bucketDuration, final List<LoadValue> averageLoad) {
        final int bucketNumber = bucketDuration == BucketDuration.Hour ? ServerTrack.HOUR_BUCKETS : ServerTrack.MINUTE_BUCKETS;
        long localEnd = end;
        for(int i = 0; i < bucketNumber; i++) {
            final long fend = localEnd;
            final long fstart = bucketDuration == BucketDuration.Hour ? -HOUR_OFFSET : -MINUTE_OFFSET;
            double cpu = lvs.stream().
                    filter(l -> l.inRange(fstart, fend)).
                    mapToDouble(l -> l.getCpuLoad()).
                    average().
                    orElse(0.0);
            double ram = lvs.stream().
                    filter(l -> l.inRange(fstart, fend)).
                    mapToDouble(l -> l.getRamLoad()).
                    average().
                    orElse(0.0);
            averageLoad.add(new LoadValue(cpu, ram, localEnd));
            localEnd = fstart;
        }
    }

}
