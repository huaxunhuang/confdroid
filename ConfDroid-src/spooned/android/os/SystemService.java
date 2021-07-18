/**
 * Copyright (C) 2006 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package android.os;


/**
 * Controls and utilities for low-level {@code init} services.
 *
 * @unknown 
 */
public class SystemService {
    private static java.util.HashMap<java.lang.String, android.os.SystemService.State> sStates = com.google.android.collect.Maps.newHashMap();

    /**
     * State of a known {@code init} service.
     */
    public enum State {

        RUNNING("running"),
        STOPPING("stopping"),
        STOPPED("stopped"),
        RESTARTING("restarting");
        State(java.lang.String state) {
            android.os.SystemService.sStates.put(state, this);
        }
    }

    private static java.lang.Object sPropertyLock = new java.lang.Object();

    static {
        android.os.SystemProperties.addChangeCallback(new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                synchronized(android.os.SystemService.sPropertyLock) {
                    android.os.SystemService.sPropertyLock.notifyAll();
                }
            }
        });
    }

    /**
     * Request that the init daemon start a named service.
     */
    public static void start(java.lang.String name) {
        android.os.SystemProperties.set("ctl.start", name);
    }

    /**
     * Request that the init daemon stop a named service.
     */
    public static void stop(java.lang.String name) {
        android.os.SystemProperties.set("ctl.stop", name);
    }

    /**
     * Request that the init daemon restart a named service.
     */
    public static void restart(java.lang.String name) {
        android.os.SystemProperties.set("ctl.restart", name);
    }

    /**
     * Return current state of given service.
     */
    public static android.os.SystemService.State getState(java.lang.String service) {
        final java.lang.String rawState = android.os.SystemProperties.get("init.svc." + service);
        final android.os.SystemService.State state = android.os.SystemService.sStates.get(rawState);
        if (state != null) {
            return state;
        } else {
            return android.os.SystemService.State.STOPPED;
        }
    }

    /**
     * Check if given service is {@link State#STOPPED}.
     */
    public static boolean isStopped(java.lang.String service) {
        return android.os.SystemService.State.STOPPED.equals(android.os.SystemService.getState(service));
    }

    /**
     * Check if given service is {@link State#RUNNING}.
     */
    public static boolean isRunning(java.lang.String service) {
        return android.os.SystemService.State.RUNNING.equals(android.os.SystemService.getState(service));
    }

    /**
     * Wait until given service has entered specific state.
     */
    public static void waitForState(java.lang.String service, android.os.SystemService.State state, long timeoutMillis) throws java.util.concurrent.TimeoutException {
        final long endMillis = android.os.SystemClock.elapsedRealtime() + timeoutMillis;
        while (true) {
            synchronized(android.os.SystemService.sPropertyLock) {
                final android.os.SystemService.State currentState = android.os.SystemService.getState(service);
                if (state.equals(currentState)) {
                    return;
                }
                if (android.os.SystemClock.elapsedRealtime() >= endMillis) {
                    throw new java.util.concurrent.TimeoutException((((((("Service " + service) + " currently ") + currentState) + "; waited ") + timeoutMillis) + "ms for ") + state);
                }
                try {
                    android.os.SystemService.sPropertyLock.wait(timeoutMillis);
                } catch (java.lang.InterruptedException e) {
                }
            }
        } 
    }

    /**
     * Wait until any of given services enters {@link State#STOPPED}.
     */
    public static void waitForAnyStopped(java.lang.String... services) {
        while (true) {
            synchronized(android.os.SystemService.sPropertyLock) {
                for (java.lang.String service : services) {
                    if (android.os.SystemService.State.STOPPED.equals(android.os.SystemService.getState(service))) {
                        return;
                    }
                }
                try {
                    android.os.SystemService.sPropertyLock.wait();
                } catch (java.lang.InterruptedException e) {
                }
            }
        } 
    }
}

