/**
 * Copyright (C) 2011 The Android Open Source Project
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
package android.net.wifi.p2p;


/**
 * A class representing a Wi-Fi p2p provisional discovery request/response
 * See {@link #WifiP2pProvDiscEvent} for supported types
 *
 * @unknown 
 */
public class WifiP2pProvDiscEvent {
    private static final java.lang.String TAG = "WifiP2pProvDiscEvent";

    public static final int PBC_REQ = 1;

    public static final int PBC_RSP = 2;

    public static final int ENTER_PIN = 3;

    public static final int SHOW_PIN = 4;

    /* One of PBC_REQ, PBC_RSP, ENTER_PIN or SHOW_PIN */
    public int event;

    public android.net.wifi.p2p.WifiP2pDevice device;

    /* Valid when event = SHOW_PIN */
    public java.lang.String pin;

    public WifiP2pProvDiscEvent() {
        device = new android.net.wifi.p2p.WifiP2pDevice();
    }

    /**
     *
     *
     * @param string
     * 		formats supported include
     * 		
     * 		P2P-PROV-DISC-PBC-REQ 42:fc:89:e1:e2:27
     * 		P2P-PROV-DISC-PBC-RESP 02:12:47:f2:5a:36
     * 		P2P-PROV-DISC-ENTER-PIN 42:fc:89:e1:e2:27
     * 		P2P-PROV-DISC-SHOW-PIN 42:fc:89:e1:e2:27 44490607
     * 		
     * 		Note: The events formats can be looked up in the wpa_supplicant code
     * @unknown 
     */
    public WifiP2pProvDiscEvent(java.lang.String string) throws java.lang.IllegalArgumentException {
        java.lang.String[] tokens = string.split(" ");
        if (tokens.length < 2) {
            throw new java.lang.IllegalArgumentException("Malformed event " + string);
        }
        if (tokens[0].endsWith("PBC-REQ"))
            event = android.net.wifi.p2p.WifiP2pProvDiscEvent.PBC_REQ;
        else
            if (tokens[0].endsWith("PBC-RESP"))
                event = android.net.wifi.p2p.WifiP2pProvDiscEvent.PBC_RSP;
            else
                if (tokens[0].endsWith("ENTER-PIN"))
                    event = android.net.wifi.p2p.WifiP2pProvDiscEvent.ENTER_PIN;
                else
                    if (tokens[0].endsWith("SHOW-PIN"))
                        event = android.net.wifi.p2p.WifiP2pProvDiscEvent.SHOW_PIN;
                    else
                        throw new java.lang.IllegalArgumentException("Malformed event " + string);




        device = new android.net.wifi.p2p.WifiP2pDevice();
        device.deviceAddress = tokens[1];
        if (event == android.net.wifi.p2p.WifiP2pProvDiscEvent.SHOW_PIN) {
            pin = tokens[2];
        }
    }

    public java.lang.String toString() {
        java.lang.StringBuffer sbuf = new java.lang.StringBuffer();
        sbuf.append(device);
        sbuf.append("\n event: ").append(event);
        sbuf.append("\n pin: ").append(pin);
        return sbuf.toString();
    }
}

