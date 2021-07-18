package android.nfc;


/**
 *
 *
 * @unknown 
 */
public class ApduList implements android.os.Parcelable {
    private java.util.ArrayList<byte[]> commands = new java.util.ArrayList<byte[]>();

    public ApduList() {
    }

    public void add(byte[] command) {
        commands.add(command);
    }

    public java.util.List<byte[]> get() {
        return commands;
    }

    public static final android.os.Parcelable.Creator<android.nfc.ApduList> CREATOR = new android.os.Parcelable.Creator<android.nfc.ApduList>() {
        @java.lang.Override
        public android.nfc.ApduList createFromParcel(android.os.Parcel in) {
            return new android.nfc.ApduList(in);
        }

        @java.lang.Override
        public android.nfc.ApduList[] newArray(int size) {
            return new android.nfc.ApduList[size];
        }
    };

    private ApduList(android.os.Parcel in) {
        int count = in.readInt();
        for (int i = 0; i < count; i++) {
            int length = in.readInt();
            byte[] cmd = new byte[length];
            in.readByteArray(cmd);
            commands.add(cmd);
        }
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeInt(commands.size());
        for (byte[] cmd : commands) {
            dest.writeInt(cmd.length);
            dest.writeByteArray(cmd);
        }
    }
}

