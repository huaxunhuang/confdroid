/**
 * Copyright (C) 2015 The Android Open Source Project
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
package android.nfc.cardemulation;


/**
 * <p>HostNfcFService is a convenience {@link Service} class that can be
 * extended to emulate an NFC-F card inside an Android service component.
 *
 * <h3>NFC Protocols</h3>
 * <p>Cards emulated by this class are based on the NFC-Forum NFC-F
 * protocol (based on the JIS-X 6319-4 specification.)</p>
 *
 * <h3>System Code and NFCID2 registration</h3>
 * <p>A {@link HostNfcFService HostNfcFService service} can register
 * exactly one System Code and one NFCID2. For details about the use of
 * System Code and NFCID2, see the NFC Forum Digital specification.</p>
 * <p>To statically register a System Code and NFCID2 with the service, a {@link #SERVICE_META_DATA}
 * entry must be included in the declaration of the service.
 *
 * <p>All {@link HostNfcFService HostNfcFService} declarations in the manifest must require the
 * {@link android.Manifest.permission#BIND_NFC_SERVICE} permission
 * in their &lt;service&gt; tag, to ensure that only the platform can bind to your service.</p>
 *
 * <p>An example of a HostNfcFService manifest declaration is shown below:
 *
 * <pre> &lt;service android:name=".MyHostNfcFService" android:exported="true" android:permission="android.permission.BIND_NFC_SERVICE"&gt;
 *     &lt;intent-filter&gt;
 *         &lt;action android:name="android.nfc.cardemulation.action.HOST_NFCF_SERVICE"/&gt;
 *     &lt;/intent-filter&gt;
 *     &lt;meta-data android:name="android.nfc.cardemulation.host_nfcf_service" android:resource="@xml/nfcfservice"/&gt;
 * &lt;/service&gt;</pre>
 *
 * This meta-data tag points to an nfcfservice.xml file.
 * An example of this file with a System Code and NFCID2 declaration is shown below:
 * <pre>
 * &lt;host-nfcf-service xmlns:android="http://schemas.android.com/apk/res/android"
 *           android:description="@string/servicedesc"&gt;
 *       &lt;system-code-filter android:name="4000"/&gt;
 *       &lt;nfcid2-filter android:name="02FE000000000000"/&gt;
 * &lt;/host-nfcf-service&gt;
 * </pre>
 *
 * <p>The {@link android.R.styleable#HostNfcFService &lt;host-nfcf-service&gt;} is required
 * to contain a
 * {@link android.R.styleable#HostApduService_description &lt;android:description&gt;}
 * attribute that contains a user-friendly description of the service that may be shown in UI.
 * <p>The {@link android.R.styleable#HostNfcFService &lt;host-nfcf-service&gt;} must
 * contain:
 * <ul>
 * <li>Exactly one {@link android.R.styleable#SystemCodeFilter &lt;system-code-filter&gt;} tag.</li>
 * <li>Exactly one {@link android.R.styleable#Nfcid2Filter &lt;nfcid2-filter&gt;} tag.</li>
 * </ul>
 * </p>
 *
 * <p>Alternatively, the System Code and NFCID2 can be dynamically registererd for a service
 * by using the {@link NfcFCardEmulation#registerSystemCodeForService(ComponentName, String)} and
 * {@link NfcFCardEmulation#setNfcid2ForService(ComponentName, String)} methods.
 * </p>
 *
 * <h3>Service selection</h3>
 * <p>When a remote NFC devices wants to communicate with your service, it
 * sends a SENSF_REQ command to the NFC controller, requesting a System Code.
 * If a {@link NfcFCardEmulation NfcFCardEmulation service} has registered
 * this system code and has been enabled by the foreground application, the
 * NFC controller will respond with the NFCID2 that is registered for this service.
 * The reader can then continue data exchange with this service by using the NFCID2.</p>
 *
 * <h3>Data exchange</h3>
 * <p>After service selection, all frames addressed to the NFCID2 of this service will
 * be sent through {@link #processNfcFPacket(byte[], Bundle)}, until the NFC link is
 * broken.<p>
 *
 * <p>When the NFC link is broken, {@link #onDeactivated(int)} will be called.</p>
 */
public abstract class HostNfcFService extends android.app.Service {
    /**
     * The {@link Intent} action that must be declared as handled by the service.
     */
    @android.annotation.SdkConstant(android.annotation.SdkConstant.SdkConstantType.SERVICE_ACTION)
    public static final java.lang.String SERVICE_INTERFACE = "android.nfc.cardemulation.action.HOST_NFCF_SERVICE";

    /**
     * The name of the meta-data element that contains
     * more information about this service.
     */
    public static final java.lang.String SERVICE_META_DATA = "android.nfc.cardemulation.host_nfcf_service";

    /**
     * Reason for {@link #onDeactivated(int)}.
     * Indicates deactivation was due to the NFC link
     * being lost.
     */
    public static final int DEACTIVATION_LINK_LOSS = 0;

    static final java.lang.String TAG = "NfcFService";

    /**
     * MSG_COMMAND_PACKET is sent by NfcService when
     * a NFC-F command packet has been received.
     *
     * @unknown 
     */
    public static final int MSG_COMMAND_PACKET = 0;

    /**
     * MSG_RESPONSE_PACKET is sent to NfcService to send
     * a response packet back to the remote device.
     *
     * @unknown 
     */
    public static final int MSG_RESPONSE_PACKET = 1;

    /**
     * MSG_DEACTIVATED is sent by NfcService when
     * the current session is finished; because
     * the NFC link was deactivated.
     *
     * @unknown 
     */
    public static final int MSG_DEACTIVATED = 2;

    /**
     *
     *
     * @unknown 
     */
    public static final java.lang.String KEY_DATA = "data";

    /**
     *
     *
     * @unknown 
     */
    public static final java.lang.String KEY_MESSENGER = "messenger";

    /**
     * Messenger interface to NfcService for sending responses.
     * Only accessed on main thread by the message handler.
     *
     * @unknown 
     */
    android.os.Messenger mNfcService = null;

    final android.os.Messenger mMessenger = new android.os.Messenger(new android.nfc.cardemulation.HostNfcFService.MsgHandler());

    final class MsgHandler extends android.os.Handler {
        @java.lang.Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case android.nfc.cardemulation.HostNfcFService.MSG_COMMAND_PACKET :
                    android.os.Bundle dataBundle = msg.getData();
                    if (dataBundle == null) {
                        return;
                    }
                    if (mNfcService == null)
                        mNfcService = msg.replyTo;

                    byte[] packet = dataBundle.getByteArray(android.nfc.cardemulation.HostNfcFService.KEY_DATA);
                    if (packet != null) {
                        byte[] responsePacket = processNfcFPacket(packet, null);
                        if (responsePacket != null) {
                            if (mNfcService == null) {
                                android.util.Log.e(android.nfc.cardemulation.HostNfcFService.TAG, "Response not sent; service was deactivated.");
                                return;
                            }
                            android.os.Message responseMsg = android.os.Message.obtain(null, android.nfc.cardemulation.HostNfcFService.MSG_RESPONSE_PACKET);
                            android.os.Bundle responseBundle = new android.os.Bundle();
                            responseBundle.putByteArray(android.nfc.cardemulation.HostNfcFService.KEY_DATA, responsePacket);
                            responseMsg.setData(responseBundle);
                            responseMsg.replyTo = android.nfc.cardemulation.HostNfcFService.this.mMessenger;
                            try {
                                mNfcService.send(responseMsg);
                            } catch (android.os.RemoteException e) {
                                android.util.Log.e("TAG", "Response not sent; RemoteException calling into " + "NfcService.");
                            }
                        }
                    } else {
                        android.util.Log.e(android.nfc.cardemulation.HostNfcFService.TAG, "Received MSG_COMMAND_PACKET without data.");
                    }
                    break;
                case android.nfc.cardemulation.HostNfcFService.MSG_RESPONSE_PACKET :
                    if (mNfcService == null) {
                        android.util.Log.e(android.nfc.cardemulation.HostNfcFService.TAG, "Response not sent; service was deactivated.");
                        return;
                    }
                    try {
                        msg.replyTo = android.nfc.cardemulation.HostNfcFService.this.mMessenger;
                        mNfcService.send(msg);
                    } catch (android.os.RemoteException e) {
                        android.util.Log.e(android.nfc.cardemulation.HostNfcFService.TAG, "RemoteException calling into NfcService.");
                    }
                    break;
                case android.nfc.cardemulation.HostNfcFService.MSG_DEACTIVATED :
                    // Make sure we won't call into NfcService again
                    mNfcService = null;
                    onDeactivated(msg.arg1);
                    break;
                default :
                    super.handleMessage(msg);
            }
        }
    }

    @java.lang.Override
    public final android.os.IBinder onBind(android.content.Intent intent) {
        return mMessenger.getBinder();
    }

    /**
     * Sends a response packet back to the remote device.
     *
     * <p>Note: this method may be called from any thread and will not block.
     *
     * @param responsePacket
     * 		A byte-array containing the response packet.
     */
    public final void sendResponsePacket(byte[] responsePacket) {
        android.os.Message responseMsg = android.os.Message.obtain(null, android.nfc.cardemulation.HostNfcFService.MSG_RESPONSE_PACKET);
        android.os.Bundle dataBundle = new android.os.Bundle();
        dataBundle.putByteArray(android.nfc.cardemulation.HostNfcFService.KEY_DATA, responsePacket);
        responseMsg.setData(dataBundle);
        try {
            mMessenger.send(responseMsg);
        } catch (android.os.RemoteException e) {
            android.util.Log.e("TAG", "Local messenger has died.");
        }
    }

    /**
     * <p>This method will be called when a NFC-F packet has been received
     * from a remote device. A response packet can be provided directly
     * by returning a byte-array in this method. Note that in general
     * response packets must be sent as quickly as possible, given the fact
     * that the user is likely holding his device over an NFC reader
     * when this method is called.
     *
     * <p class="note">This method is running on the main thread of your application.
     * If you cannot return a response packet immediately, return null
     * and use the {@link #sendResponsePacket(byte[])} method later.
     *
     * @param commandPacket
     * 		The NFC-F packet that was received from the remote device
     * @param extras
     * 		A bundle containing extra data. May be null.
     * @return a byte-array containing the response packet, or null if no
    response packet can be sent at this point.
     */
    public abstract byte[] processNfcFPacket(byte[] commandPacket, android.os.Bundle extras);

    /**
     * This method will be called in following possible scenarios:
     * <li>The NFC link has been lost
     *
     * @param reason
     * 		{@link #DEACTIVATION_LINK_LOSS}
     */
    public abstract void onDeactivated(int reason);
}

