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
package android.bluetooth;


/**
 * Wrapper class for default implementation of IBluetoothGattCallback.
 *
 * @unknown 
 */
public class BluetoothGattCallbackWrapper extends android.bluetooth.IBluetoothGattCallback.Stub {
    @java.lang.Override
    public void onClientRegistered(int status, int clientIf) throws android.os.RemoteException {
    }

    @java.lang.Override
    public void onClientConnectionState(int status, int clientIf, boolean connected, java.lang.String address) throws android.os.RemoteException {
    }

    @java.lang.Override
    public void onScanResult(android.bluetooth.le.ScanResult scanResult) throws android.os.RemoteException {
    }

    @java.lang.Override
    public void onBatchScanResults(java.util.List<android.bluetooth.le.ScanResult> batchResults) throws android.os.RemoteException {
    }

    @java.lang.Override
    public void onSearchComplete(java.lang.String address, java.util.List<android.bluetooth.BluetoothGattService> services, int status) throws android.os.RemoteException {
    }

    @java.lang.Override
    public void onCharacteristicRead(java.lang.String address, int status, int handle, byte[] value) throws android.os.RemoteException {
    }

    @java.lang.Override
    public void onCharacteristicWrite(java.lang.String address, int status, int handle) throws android.os.RemoteException {
    }

    @java.lang.Override
    public void onExecuteWrite(java.lang.String address, int status) throws android.os.RemoteException {
    }

    @java.lang.Override
    public void onDescriptorRead(java.lang.String address, int status, int handle, byte[] value) throws android.os.RemoteException {
    }

    @java.lang.Override
    public void onDescriptorWrite(java.lang.String address, int status, int handle) throws android.os.RemoteException {
    }

    @java.lang.Override
    public void onNotify(java.lang.String address, int handle, byte[] value) throws android.os.RemoteException {
    }

    @java.lang.Override
    public void onReadRemoteRssi(java.lang.String address, int rssi, int status) throws android.os.RemoteException {
    }

    @java.lang.Override
    public void onMultiAdvertiseCallback(int status, boolean isStart, android.bluetooth.le.AdvertiseSettings advertiseSettings) throws android.os.RemoteException {
    }

    @java.lang.Override
    public void onConfigureMTU(java.lang.String address, int mtu, int status) throws android.os.RemoteException {
    }

    @java.lang.Override
    public void onFoundOrLost(boolean onFound, android.bluetooth.le.ScanResult scanResult) throws android.os.RemoteException {
    }

    @java.lang.Override
    public void onScanManagerErrorCallback(int errorCode) throws android.os.RemoteException {
    }
}

