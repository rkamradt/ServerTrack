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
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.kamradtfamily.servertrack.spi.AverageLoadValues;
import net.kamradtfamily.servertrack.spi.LoadValue;
import net.kamradtfamily.servertrack.spi.ServerTrack;

/**
 *
 * An implementation of ServerTrack that uses a simple in-memory scheme to store values
 * 
 * @author randalkamradt
 * @see ServerTrack
 * @since 1.0
 */
public class ConncurentMemoryServerTrack implements ServerTrack {
    public static final long HOUR_OFFSET = 1000*60*60;
    public static final long MINUTE_OFFSET = 1000*60;
    /**
     * The map of server names and load values. This map is relatively static as it
     * is only modified when a new server is encountered, so the choice of concurrency type 
     * should have minimal impact.
     */
    private final Map<String, List<LoadValue>> loadValuesMap = new ConcurrentHashMap<>(); 
    @Override
    public LoadValue record(final String serverName, LoadValue input) {
        List<LoadValue> loadValues = loadValuesMap.get(serverName);
        if(loadValues == null) {
            loadValues = new ArrayList<>(); // should this be array list?
            loadValuesMap.put(serverName, loadValues);
        }
        LoadValue loadValue = new LoadValue(input.getCpuLoad(), input.getRamLoad());
        loadValues.add(loadValue); // todo synchronize this call
        // TODO remove load values that have expired (past the 24 hour point
        return loadValue;
    }

    @Override
    public AverageLoadValues getLoad(final String serverName, final long end) {
        final AverageLoadValues ret = new AverageLoadValues(serverName);
        averageBuckets(end, ret, BucketDuration.Hour, ServerTrack.HOUR_BUCKETS, ret.getByHour());
        averageBuckets(end, ret, BucketDuration.Minute, ServerTrack.MINUTE_BUCKETS, ret.getByMinute());
        return ret;
    }
    private enum BucketDuration { Hour, Minute };
    private void averageBuckets(final long end, final AverageLoadValues ret, final BucketDuration bucketDuration, final int bucketNumber, final List<LoadValue> averageLoad) {
        List<LoadValue> lvs = loadValuesMap.get(ret.getServerName());
        long localEnd = end;
        for(int i = 0; i < bucketNumber; i++) {
            final long fend = localEnd;
            final long fstart = bucketDuration == BucketDuration.Hour ? HOUR_OFFSET : MINUTE_OFFSET;
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
