/**
 * Copyright (C) 2014 The Android Open Source Project
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
package android.bluetooth.client.map;


class BluetoothMasRequestSetPath extends android.bluetooth.client.map.BluetoothMasRequest {
    enum SetPathDir {

        ROOT,
        UP,
        DOWN;}

    android.bluetooth.client.map.BluetoothMasRequestSetPath.SetPathDir mDir;

    java.lang.String mName;

    public BluetoothMasRequestSetPath(java.lang.String name) {
        mDir = android.bluetooth.client.map.BluetoothMasRequestSetPath.SetPathDir.DOWN;
        mName = name;
        mHeaderSet.setHeader(HeaderSet.NAME, name);
    }

    public BluetoothMasRequestSetPath(boolean goRoot) {
        mHeaderSet.setEmptyNameHeader();
        if (goRoot) {
            mDir = android.bluetooth.client.map.BluetoothMasRequestSetPath.SetPathDir.ROOT;
        } else {
            mDir = android.bluetooth.client.map.BluetoothMasRequestSetPath.SetPathDir.UP;
        }
    }

    @java.lang.Override
    public void execute(javax.obex.ClientSession session) {
        javax.obex.HeaderSet hs = null;
        try {
            switch (mDir) {
                case ROOT :
                case DOWN :
                    hs = session.setPath(mHeaderSet, false, false);
                    break;
                case UP :
                    hs = session.setPath(mHeaderSet, true, false);
                    break;
            }
            mResponseCode = hs.getResponseCode();
        } catch (java.io.IOException e) {
            mResponseCode = javax.obex.ResponseCodes.OBEX_HTTP_INTERNAL_ERROR;
        }
    }
}

