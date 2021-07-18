package android.hardware.hdmi;


/**
 * Parent for classes of various HDMI-CEC device type used to access
 * the HDMI control system service. Contains methods and data used in common.
 *
 * @unknown 
 */
@android.annotation.SystemApi
public abstract class HdmiClient {
    private static final java.lang.String TAG = "HdmiClient";

    /* package */
    final android.hardware.hdmi.IHdmiControlService mService;

    private android.hardware.hdmi.IHdmiVendorCommandListener mIHdmiVendorCommandListener;

    /* package */
    abstract int getDeviceType();

    /* package */
    HdmiClient(android.hardware.hdmi.IHdmiControlService service) {
        mService = service;
    }

    /**
     * Returns the active source information.
     *
     * @return {@link HdmiDeviceInfo} object that describes the active source
    or active routing path
     */
    public android.hardware.hdmi.HdmiDeviceInfo getActiveSource() {
        try {
            return mService.getActiveSource();
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.hardware.hdmi.HdmiClient.TAG, "getActiveSource threw exception ", e);
        }
        return null;
    }

    /**
     * Sends a key event to other logical device.
     *
     * @param keyCode
     * 		key code to send. Defined in {@link android.view.KeyEvent}.
     * @param isPressed
     * 		true if this is key press event
     */
    public void sendKeyEvent(int keyCode, boolean isPressed) {
        try {
            mService.sendKeyEvent(getDeviceType(), keyCode, isPressed);
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.hardware.hdmi.HdmiClient.TAG, "sendKeyEvent threw exception ", e);
        }
    }

    /**
     * Sends vendor-specific command.
     *
     * @param targetAddress
     * 		address of the target device
     * @param params
     * 		vendor-specific parameter. For &lt;Vendor Command With ID&gt; do not
     * 		include the first 3 bytes (vendor ID).
     * @param hasVendorId
     * 		{@code true} if the command type will be &lt;Vendor Command With ID&gt;.
     * 		{@code false} if the command will be &lt;Vendor Command&gt;
     */
    public void sendVendorCommand(int targetAddress, byte[] params, boolean hasVendorId) {
        try {
            mService.sendVendorCommand(getDeviceType(), targetAddress, params, hasVendorId);
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.hardware.hdmi.HdmiClient.TAG, "failed to send vendor command: ", e);
        }
    }

    /**
     * Sets a listener used to receive incoming vendor-specific command.
     *
     * @param listener
     * 		listener object
     */
    public void setVendorCommandListener(@android.annotation.NonNull
    android.hardware.hdmi.HdmiControlManager.VendorCommandListener listener) {
        if (listener == null) {
            throw new java.lang.IllegalArgumentException("listener cannot be null");
        }
        if (mIHdmiVendorCommandListener != null) {
            throw new java.lang.IllegalStateException("listener was already set");
        }
        try {
            android.hardware.hdmi.IHdmiVendorCommandListener wrappedListener = android.hardware.hdmi.HdmiClient.getListenerWrapper(listener);
            mService.addVendorCommandListener(wrappedListener, getDeviceType());
            mIHdmiVendorCommandListener = wrappedListener;
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.hardware.hdmi.HdmiClient.TAG, "failed to set vendor command listener: ", e);
        }
    }

    private static android.hardware.hdmi.IHdmiVendorCommandListener getListenerWrapper(final android.hardware.hdmi.HdmiControlManager.VendorCommandListener listener) {
        return new android.hardware.hdmi.IHdmiVendorCommandListener.Stub() {
            @java.lang.Override
            public void onReceived(int srcAddress, int destAddress, byte[] params, boolean hasVendorId) {
                listener.onReceived(srcAddress, destAddress, params, hasVendorId);
            }

            @java.lang.Override
            public void onControlStateChanged(boolean enabled, int reason) {
                listener.onControlStateChanged(enabled, reason);
            }
        };
    }
}

