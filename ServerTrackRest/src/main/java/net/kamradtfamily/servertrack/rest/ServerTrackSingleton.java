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
package net.kamradtfamily.servertrack.rest;

import net.kamradtfamily.servertrack.spi.ServerTrack;
import net.kamradtfamily.servertrackimpl.ConncurentMemoryServerTrack;

/**
 *
 * A Singleton that represents the API. This *should* be loaded as part of the
 * webapp initialization logic, especially if it will need to take parameters.
 * But it should work fine as is, and will be initialized on the first request. 
 * Because it is initialized on a request thread, the creation of the singleton
 * must be synchronized.
 * 
 * @author randalkamradt
 * @since 1.0
 */
public class ServerTrackSingleton {
    static ServerTrack theServerTrack;
    static ServerTrack getTheServerTrack() {
        if(theServerTrack == null) {
            synchronized(ServerTrackSingleton.class) {  // ensure that only one thread creates the singleton
                if(theServerTrack == null) { // double check in case another thread snuck in and set it.
                    theServerTrack = new ConncurentMemoryServerTrack();
                }
            }
        }
        return theServerTrack;
    }
    
}
