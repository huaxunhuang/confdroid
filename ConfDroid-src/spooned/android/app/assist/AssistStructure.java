package android.app.assist;


/**
 * Assist data automatically created by the platform's implementation
 * of {@link android.app.Activity#onProvideAssistData}.
 */
public class AssistStructure implements android.os.Parcelable {
    static final java.lang.String TAG = "AssistStructure";

    static final boolean DEBUG_PARCEL = false;

    static final boolean DEBUG_PARCEL_CHILDREN = false;

    static final boolean DEBUG_PARCEL_TREE = false;

    static final int VALIDATE_WINDOW_TOKEN = 0x11111111;

    static final int VALIDATE_VIEW_TOKEN = 0x22222222;

    boolean mHaveData;

    android.content.ComponentName mActivityComponent;

    final java.util.ArrayList<android.app.assist.AssistStructure.WindowNode> mWindowNodes = new java.util.ArrayList<>();

    final java.util.ArrayList<android.app.assist.AssistStructure.ViewNodeBuilder> mPendingAsyncChildren = new java.util.ArrayList<>();

    android.app.assist.AssistStructure.SendChannel mSendChannel;

    android.os.IBinder mReceiveChannel;

    android.graphics.Rect mTmpRect = new android.graphics.Rect();

    static final int TRANSACTION_XFER = android.os.Binder.FIRST_CALL_TRANSACTION + 1;

    static final java.lang.String DESCRIPTOR = "android.app.AssistStructure";

    static final class SendChannel extends android.os.Binder {
        volatile android.app.assist.AssistStructure mAssistStructure;

        SendChannel(android.app.assist.AssistStructure as) {
            mAssistStructure = as;
        }

        @java.lang.Override
        protected boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException {
            if (code == android.app.assist.AssistStructure.TRANSACTION_XFER) {
                android.app.assist.AssistStructure as = mAssistStructure;
                if (as == null) {
                    return true;
                }
                data.enforceInterface(android.app.assist.AssistStructure.DESCRIPTOR);
                android.os.IBinder token = data.readStrongBinder();
                if (android.app.assist.AssistStructure.DEBUG_PARCEL)
                    android.util.Log.d(android.app.assist.AssistStructure.TAG, (("Request for data on " + as) + " using token ") + token);

                if (token != null) {
                    if (android.app.assist.AssistStructure.DEBUG_PARCEL)
                        android.util.Log.d(android.app.assist.AssistStructure.TAG, "Resuming partial write of " + token);

                    if (token instanceof android.app.assist.AssistStructure.ParcelTransferWriter) {
                        android.app.assist.AssistStructure.ParcelTransferWriter xfer = ((android.app.assist.AssistStructure.ParcelTransferWriter) (token));
                        xfer.writeToParcel(as, reply);
                        return true;
                    }
                    android.util.Log.w(android.app.assist.AssistStructure.TAG, "Caller supplied bad token type: " + token);
                    // Don't write anything; this is the end of the data.
                    return true;
                }
                // long start = SystemClock.uptimeMillis();
                android.app.assist.AssistStructure.ParcelTransferWriter xfer = new android.app.assist.AssistStructure.ParcelTransferWriter(as, reply);
                xfer.writeToParcel(as, reply);
                // Log.i(TAG, "Time to parcel: " + (SystemClock.uptimeMillis()-start) + "ms");
                return true;
            } else {
                return super.onTransact(code, data, reply, flags);
            }
        }
    }

    static final class ViewStackEntry {
        android.app.assist.AssistStructure.ViewNode node;

        int curChild;

        int numChildren;
    }

    static final class ParcelTransferWriter extends android.os.Binder {
        final boolean mWriteStructure;

        int mCurWindow;

        int mNumWindows;

        final java.util.ArrayList<android.app.assist.AssistStructure.ViewStackEntry> mViewStack = new java.util.ArrayList<>();

        android.app.assist.AssistStructure.ViewStackEntry mCurViewStackEntry;

        int mCurViewStackPos;

        int mNumWrittenWindows;

        int mNumWrittenViews;

        final float[] mTmpMatrix = new float[9];

        ParcelTransferWriter(android.app.assist.AssistStructure as, android.os.Parcel out) {
            mWriteStructure = as.waitForReady();
            android.content.ComponentName.writeToParcel(as.mActivityComponent, out);
            mNumWindows = as.mWindowNodes.size();
            if (mWriteStructure && (mNumWindows > 0)) {
                out.writeInt(mNumWindows);
            } else {
                out.writeInt(0);
            }
        }

        void writeToParcel(android.app.assist.AssistStructure as, android.os.Parcel out) {
            int start = out.dataPosition();
            mNumWrittenWindows = 0;
            mNumWrittenViews = 0;
            boolean more = writeToParcelInner(as, out);
            android.util.Log.i(android.app.assist.AssistStructure.TAG, ((((((("Flattened " + (more ? "partial" : "final")) + " assist data: ") + (out.dataPosition() - start)) + " bytes, containing ") + mNumWrittenWindows) + " windows, ") + mNumWrittenViews) + " views");
        }

        boolean writeToParcelInner(android.app.assist.AssistStructure as, android.os.Parcel out) {
            if (mNumWindows == 0) {
                return false;
            }
            if (android.app.assist.AssistStructure.DEBUG_PARCEL)
                android.util.Log.d(android.app.assist.AssistStructure.TAG, "Creating PooledStringWriter @ " + out.dataPosition());

            android.os.PooledStringWriter pwriter = new android.os.PooledStringWriter(out);
            while (writeNextEntryToParcel(as, out, pwriter)) {
                // If the parcel is above the IPC limit, then we are getting too
                // large for a single IPC so stop here and let the caller come back when it
                // is ready for more.
                if (out.dataSize() > android.os.IBinder.MAX_IPC_SIZE) {
                    if (android.app.assist.AssistStructure.DEBUG_PARCEL)
                        android.util.Log.d(android.app.assist.AssistStructure.TAG, ((("Assist data size is " + out.dataSize()) + " @ pos ") + out.dataPosition()) + "; returning partial result");

                    out.writeInt(0);
                    out.writeStrongBinder(this);
                    if (android.app.assist.AssistStructure.DEBUG_PARCEL)
                        android.util.Log.d(android.app.assist.AssistStructure.TAG, (("Finishing PooledStringWriter @ " + out.dataPosition()) + ", size ") + pwriter.getStringCount());

                    pwriter.finish();
                    return true;
                }
            } 
            if (android.app.assist.AssistStructure.DEBUG_PARCEL)
                android.util.Log.d(android.app.assist.AssistStructure.TAG, (("Finishing PooledStringWriter @ " + out.dataPosition()) + ", size ") + pwriter.getStringCount());

            pwriter.finish();
            mViewStack.clear();
            return false;
        }

        void pushViewStackEntry(android.app.assist.AssistStructure.ViewNode node, int pos) {
            android.app.assist.AssistStructure.ViewStackEntry entry;
            if (pos >= mViewStack.size()) {
                entry = new android.app.assist.AssistStructure.ViewStackEntry();
                mViewStack.add(entry);
                if (android.app.assist.AssistStructure.DEBUG_PARCEL_TREE)
                    android.util.Log.d(android.app.assist.AssistStructure.TAG, (("New stack entry at " + pos) + ": ") + entry);

            } else {
                entry = mViewStack.get(pos);
                if (android.app.assist.AssistStructure.DEBUG_PARCEL_TREE)
                    android.util.Log.d(android.app.assist.AssistStructure.TAG, (("Existing stack entry at " + pos) + ": ") + entry);

            }
            entry.node = node;
            entry.numChildren = node.getChildCount();
            entry.curChild = 0;
            mCurViewStackEntry = entry;
        }

        void writeView(android.app.assist.AssistStructure.ViewNode child, android.os.Parcel out, android.os.PooledStringWriter pwriter, int levelAdj) {
            if (android.app.assist.AssistStructure.DEBUG_PARCEL)
                android.util.Log.d(android.app.assist.AssistStructure.TAG, (((((("write view: at " + out.dataPosition()) + ", windows=") + mNumWrittenWindows) + ", views=") + mNumWrittenViews) + ", level=") + (mCurViewStackPos + levelAdj));

            out.writeInt(android.app.assist.AssistStructure.VALIDATE_VIEW_TOKEN);
            int flags = child.writeSelfToParcel(out, pwriter, mTmpMatrix);
            mNumWrittenViews++;
            // If the child has children, push it on the stack to write them next.
            if ((flags & android.app.assist.AssistStructure.ViewNode.FLAGS_HAS_CHILDREN) != 0) {
                if (android.app.assist.AssistStructure.DEBUG_PARCEL_TREE || android.app.assist.AssistStructure.DEBUG_PARCEL_CHILDREN)
                    android.util.Log.d(android.app.assist.AssistStructure.TAG, (((("Preparing to write " + child.mChildren.length) + " children: @ #") + mNumWrittenViews) + ", level ") + (mCurViewStackPos + levelAdj));

                out.writeInt(child.mChildren.length);
                int pos = ++mCurViewStackPos;
                pushViewStackEntry(child, pos);
            }
        }

        boolean writeNextEntryToParcel(android.app.assist.AssistStructure as, android.os.Parcel out, android.os.PooledStringWriter pwriter) {
            // Write next view node if appropriate.
            if (mCurViewStackEntry != null) {
                if (mCurViewStackEntry.curChild < mCurViewStackEntry.numChildren) {
                    // Write the next child in the current view.
                    if (android.app.assist.AssistStructure.DEBUG_PARCEL_TREE)
                        android.util.Log.d(android.app.assist.AssistStructure.TAG, (("Writing child #" + mCurViewStackEntry.curChild) + " in ") + mCurViewStackEntry.node);

                    android.app.assist.AssistStructure.ViewNode child = mCurViewStackEntry.node.mChildren[mCurViewStackEntry.curChild];
                    mCurViewStackEntry.curChild++;
                    writeView(child, out, pwriter, 1);
                    return true;
                }
                // We are done writing children of the current view; pop off the stack.
                do {
                    int pos = --mCurViewStackPos;
                    if (android.app.assist.AssistStructure.DEBUG_PARCEL_TREE)
                        android.util.Log.d(android.app.assist.AssistStructure.TAG, (("Done with " + mCurViewStackEntry.node) + "; popping up to ") + pos);

                    if (pos < 0) {
                        // Reached the last view; step to next window.
                        if (android.app.assist.AssistStructure.DEBUG_PARCEL_TREE)
                            android.util.Log.d(android.app.assist.AssistStructure.TAG, "Done with view hierarchy!");

                        mCurViewStackEntry = null;
                        break;
                    }
                    mCurViewStackEntry = mViewStack.get(pos);
                } while (mCurViewStackEntry.curChild >= mCurViewStackEntry.numChildren );
                return true;
            }
            // Write the next window if appropriate.
            int pos = mCurWindow;
            if (pos < mNumWindows) {
                android.app.assist.AssistStructure.WindowNode win = as.mWindowNodes.get(pos);
                mCurWindow++;
                if (android.app.assist.AssistStructure.DEBUG_PARCEL)
                    android.util.Log.d(android.app.assist.AssistStructure.TAG, (((((("write window #" + pos) + ": at ") + out.dataPosition()) + ", windows=") + mNumWrittenWindows) + ", views=") + mNumWrittenViews);

                out.writeInt(android.app.assist.AssistStructure.VALIDATE_WINDOW_TOKEN);
                win.writeSelfToParcel(out, pwriter, mTmpMatrix);
                mNumWrittenWindows++;
                android.app.assist.AssistStructure.ViewNode root = win.mRoot;
                mCurViewStackPos = 0;
                if (android.app.assist.AssistStructure.DEBUG_PARCEL_TREE)
                    android.util.Log.d(android.app.assist.AssistStructure.TAG, "Writing initial root view " + root);

                writeView(root, out, pwriter, 0);
                return true;
            }
            return false;
        }
    }

    final class ParcelTransferReader {
        final float[] mTmpMatrix = new float[9];

        android.os.PooledStringReader mStringReader;

        int mNumReadWindows;

        int mNumReadViews;

        private final android.os.IBinder mChannel;

        private android.os.IBinder mTransferToken;

        private android.os.Parcel mCurParcel;

        ParcelTransferReader(android.os.IBinder channel) {
            mChannel = channel;
        }

        void go() {
            fetchData();
            mActivityComponent = android.content.ComponentName.readFromParcel(mCurParcel);
            final int N = mCurParcel.readInt();
            if (N > 0) {
                if (android.app.assist.AssistStructure.DEBUG_PARCEL)
                    android.util.Log.d(android.app.assist.AssistStructure.TAG, "Creating PooledStringReader @ " + mCurParcel.dataPosition());

                mStringReader = new android.os.PooledStringReader(mCurParcel);
                if (android.app.assist.AssistStructure.DEBUG_PARCEL)
                    android.util.Log.d(android.app.assist.AssistStructure.TAG, "PooledStringReader size = " + mStringReader.getStringCount());

                for (int i = 0; i < N; i++) {
                    mWindowNodes.add(new android.app.assist.AssistStructure.WindowNode(this));
                }
            }
            if (android.app.assist.AssistStructure.DEBUG_PARCEL)
                android.util.Log.d(android.app.assist.AssistStructure.TAG, (((((("Finished reading: at " + mCurParcel.dataPosition()) + ", avail=") + mCurParcel.dataAvail()) + ", windows=") + mNumReadWindows) + ", views=") + mNumReadViews);

        }

        android.os.Parcel readParcel(int validateToken, int level) {
            if (android.app.assist.AssistStructure.DEBUG_PARCEL)
                android.util.Log.d(android.app.assist.AssistStructure.TAG, (((((((("readParcel: at " + mCurParcel.dataPosition()) + ", avail=") + mCurParcel.dataAvail()) + ", windows=") + mNumReadWindows) + ", views=") + mNumReadViews) + ", level=") + level);

            int token = mCurParcel.readInt();
            if (token != 0) {
                if (token != validateToken) {
                    throw new android.os.BadParcelableException((("Got token " + java.lang.Integer.toHexString(token)) + ", expected token ") + java.lang.Integer.toHexString(validateToken));
                }
                return mCurParcel;
            }
            // We have run out of partial data, need to read another batch.
            mTransferToken = mCurParcel.readStrongBinder();
            if (mTransferToken == null) {
                throw new java.lang.IllegalStateException("Reached end of partial data without transfer token");
            }
            if (android.app.assist.AssistStructure.DEBUG_PARCEL)
                android.util.Log.d(android.app.assist.AssistStructure.TAG, (("Ran out of partial data at " + mCurParcel.dataPosition()) + ", token ") + mTransferToken);

            fetchData();
            if (android.app.assist.AssistStructure.DEBUG_PARCEL)
                android.util.Log.d(android.app.assist.AssistStructure.TAG, "Creating PooledStringReader @ " + mCurParcel.dataPosition());

            mStringReader = new android.os.PooledStringReader(mCurParcel);
            if (android.app.assist.AssistStructure.DEBUG_PARCEL)
                android.util.Log.d(android.app.assist.AssistStructure.TAG, "PooledStringReader size = " + mStringReader.getStringCount());

            if (android.app.assist.AssistStructure.DEBUG_PARCEL)
                android.util.Log.d(android.app.assist.AssistStructure.TAG, (((((("readParcel: at " + mCurParcel.dataPosition()) + ", avail=") + mCurParcel.dataAvail()) + ", windows=") + mNumReadWindows) + ", views=") + mNumReadViews);

            mCurParcel.readInt();
            return mCurParcel;
        }

        private void fetchData() {
            android.os.Parcel data = android.os.Parcel.obtain();
            data.writeInterfaceToken(android.app.assist.AssistStructure.DESCRIPTOR);
            data.writeStrongBinder(mTransferToken);
            if (android.app.assist.AssistStructure.DEBUG_PARCEL)
                android.util.Log.d(android.app.assist.AssistStructure.TAG, "Requesting data with token " + mTransferToken);

            if (mCurParcel != null) {
                mCurParcel.recycle();
            }
            mCurParcel = android.os.Parcel.obtain();
            try {
                mChannel.transact(android.app.assist.AssistStructure.TRANSACTION_XFER, data, mCurParcel, 0);
            } catch (android.os.RemoteException e) {
                android.util.Log.w(android.app.assist.AssistStructure.TAG, "Failure reading AssistStructure data", e);
                throw new java.lang.IllegalStateException("Failure reading AssistStructure data: " + e);
            }
            data.recycle();
            mNumReadWindows = mNumReadViews = 0;
        }
    }

    static final class ViewNodeText {
        java.lang.CharSequence mText;

        float mTextSize;

        int mTextStyle;

        int mTextColor = android.app.assist.AssistStructure.ViewNode.TEXT_COLOR_UNDEFINED;

        int mTextBackgroundColor = android.app.assist.AssistStructure.ViewNode.TEXT_COLOR_UNDEFINED;

        int mTextSelectionStart;

        int mTextSelectionEnd;

        int[] mLineCharOffsets;

        int[] mLineBaselines;

        java.lang.String mHint;

        ViewNodeText() {
        }

        boolean isSimple() {
            return (((((mTextBackgroundColor == android.app.assist.AssistStructure.ViewNode.TEXT_COLOR_UNDEFINED) && (mTextSelectionStart == 0)) && (mTextSelectionEnd == 0)) && (mLineCharOffsets == null)) && (mLineBaselines == null)) && (mHint == null);
        }

        ViewNodeText(android.os.Parcel in, boolean simple) {
            mText = android.text.TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
            mTextSize = in.readFloat();
            mTextStyle = in.readInt();
            mTextColor = in.readInt();
            if (!simple) {
                mTextBackgroundColor = in.readInt();
                mTextSelectionStart = in.readInt();
                mTextSelectionEnd = in.readInt();
                mLineCharOffsets = in.createIntArray();
                mLineBaselines = in.createIntArray();
                mHint = in.readString();
            }
        }

        void writeToParcel(android.os.Parcel out, boolean simple) {
            android.text.TextUtils.writeToParcel(mText, out, 0);
            out.writeFloat(mTextSize);
            out.writeInt(mTextStyle);
            out.writeInt(mTextColor);
            if (!simple) {
                out.writeInt(mTextBackgroundColor);
                out.writeInt(mTextSelectionStart);
                out.writeInt(mTextSelectionEnd);
                out.writeIntArray(mLineCharOffsets);
                out.writeIntArray(mLineBaselines);
                out.writeString(mHint);
            }
        }
    }

    /**
     * Describes a window in the assist data.
     */
    public static class WindowNode {
        final int mX;

        final int mY;

        final int mWidth;

        final int mHeight;

        final java.lang.CharSequence mTitle;

        final int mDisplayId;

        final android.app.assist.AssistStructure.ViewNode mRoot;

        WindowNode(android.app.assist.AssistStructure assist, android.view.ViewRootImpl root) {
            android.view.View view = root.getView();
            android.graphics.Rect rect = new android.graphics.Rect();
            view.getBoundsOnScreen(rect);
            mX = rect.left - view.getLeft();
            mY = rect.top - view.getTop();
            mWidth = rect.width();
            mHeight = rect.height();
            mTitle = root.getTitle();
            mDisplayId = root.getDisplayId();
            mRoot = new android.app.assist.AssistStructure.ViewNode();
            android.app.assist.AssistStructure.ViewNodeBuilder builder = new android.app.assist.AssistStructure.ViewNodeBuilder(assist, mRoot, false);
            if ((root.getWindowFlags() & android.view.WindowManager.LayoutParams.FLAG_SECURE) != 0) {
                // This is a secure window, so it doesn't want a screenshot, and that
                // means we should also not copy out its view hierarchy.
                view.onProvideStructure(builder);
                builder.setAssistBlocked(true);
                return;
            }
            view.dispatchProvideStructure(builder);
        }

        WindowNode(android.app.assist.AssistStructure.ParcelTransferReader reader) {
            android.os.Parcel in = reader.readParcel(android.app.assist.AssistStructure.VALIDATE_WINDOW_TOKEN, 0);
            reader.mNumReadWindows++;
            mX = in.readInt();
            mY = in.readInt();
            mWidth = in.readInt();
            mHeight = in.readInt();
            mTitle = android.text.TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
            mDisplayId = in.readInt();
            mRoot = new android.app.assist.AssistStructure.ViewNode(reader, 0);
        }

        void writeSelfToParcel(android.os.Parcel out, android.os.PooledStringWriter pwriter, float[] tmpMatrix) {
            out.writeInt(mX);
            out.writeInt(mY);
            out.writeInt(mWidth);
            out.writeInt(mHeight);
            android.text.TextUtils.writeToParcel(mTitle, out, 0);
            out.writeInt(mDisplayId);
        }

        /**
         * Returns the left edge of the window, in pixels, relative to the left
         * edge of the screen.
         */
        public int getLeft() {
            return mX;
        }

        /**
         * Returns the top edge of the window, in pixels, relative to the top
         * edge of the screen.
         */
        public int getTop() {
            return mY;
        }

        /**
         * Returns the total width of the window in pixels.
         */
        public int getWidth() {
            return mWidth;
        }

        /**
         * Returns the total height of the window in pixels.
         */
        public int getHeight() {
            return mHeight;
        }

        /**
         * Returns the title associated with the window, if it has one.
         */
        public java.lang.CharSequence getTitle() {
            return mTitle;
        }

        /**
         * Returns the ID of the display this window is on, for use with
         * {@link android.hardware.display.DisplayManager#getDisplay DisplayManager.getDisplay()}.
         */
        public int getDisplayId() {
            return mDisplayId;
        }

        /**
         * Returns the {@link ViewNode} containing the root content of the window.
         */
        public android.app.assist.AssistStructure.ViewNode getRootViewNode() {
            return mRoot;
        }
    }

    /**
     * Describes a single view in the assist data.
     */
    public static class ViewNode {
        /**
         * Magic value for text color that has not been defined, which is very unlikely
         * to be confused with a real text color.
         */
        public static final int TEXT_COLOR_UNDEFINED = 1;

        public static final int TEXT_STYLE_BOLD = 1 << 0;

        public static final int TEXT_STYLE_ITALIC = 1 << 1;

        public static final int TEXT_STYLE_UNDERLINE = 1 << 2;

        public static final int TEXT_STYLE_STRIKE_THRU = 1 << 3;

        int mId = android.view.View.NO_ID;

        java.lang.String mIdPackage;

        java.lang.String mIdType;

        java.lang.String mIdEntry;

        int mX;

        int mY;

        int mScrollX;

        int mScrollY;

        int mWidth;

        int mHeight;

        android.graphics.Matrix mMatrix;

        float mElevation;

        float mAlpha = 1.0F;

        static final int FLAGS_DISABLED = 0x1;

        static final int FLAGS_VISIBILITY_MASK = (android.view.View.VISIBLE | android.view.View.INVISIBLE) | android.view.View.GONE;

        static final int FLAGS_FOCUSABLE = 0x10;

        static final int FLAGS_FOCUSED = 0x20;

        static final int FLAGS_SELECTED = 0x40;

        static final int FLAGS_ASSIST_BLOCKED = 0x80;

        static final int FLAGS_CHECKABLE = 0x100;

        static final int FLAGS_CHECKED = 0x200;

        static final int FLAGS_CLICKABLE = 0x400;

        static final int FLAGS_LONG_CLICKABLE = 0x800;

        static final int FLAGS_ACCESSIBILITY_FOCUSED = 0x1000;

        static final int FLAGS_ACTIVATED = 0x2000;

        static final int FLAGS_CONTEXT_CLICKABLE = 0x4000;

        static final int FLAGS_HAS_MATRIX = 0x40000000;

        static final int FLAGS_HAS_ALPHA = 0x20000000;

        static final int FLAGS_HAS_ELEVATION = 0x10000000;

        static final int FLAGS_HAS_SCROLL = 0x8000000;

        static final int FLAGS_HAS_LARGE_COORDS = 0x4000000;

        static final int FLAGS_HAS_CONTENT_DESCRIPTION = 0x2000000;

        static final int FLAGS_HAS_TEXT = 0x1000000;

        static final int FLAGS_HAS_COMPLEX_TEXT = 0x800000;

        static final int FLAGS_HAS_EXTRAS = 0x400000;

        static final int FLAGS_HAS_ID = 0x200000;

        static final int FLAGS_HAS_CHILDREN = 0x100000;

        static final int FLAGS_ALL_CONTROL = 0xfff00000;

        int mFlags;

        java.lang.String mClassName;

        java.lang.CharSequence mContentDescription;

        android.app.assist.AssistStructure.ViewNodeText mText;

        android.os.Bundle mExtras;

        android.app.assist.AssistStructure.ViewNode[] mChildren;

        ViewNode() {
        }

        ViewNode(android.app.assist.AssistStructure.ParcelTransferReader reader, int nestingLevel) {
            final android.os.Parcel in = reader.readParcel(android.app.assist.AssistStructure.VALIDATE_VIEW_TOKEN, nestingLevel);
            reader.mNumReadViews++;
            final android.os.PooledStringReader preader = reader.mStringReader;
            mClassName = preader.readString();
            mFlags = in.readInt();
            final int flags = mFlags;
            if ((flags & android.app.assist.AssistStructure.ViewNode.FLAGS_HAS_ID) != 0) {
                mId = in.readInt();
                if (mId != 0) {
                    mIdEntry = preader.readString();
                    if (mIdEntry != null) {
                        mIdType = preader.readString();
                        mIdPackage = preader.readString();
                    }
                }
            }
            if ((flags & android.app.assist.AssistStructure.ViewNode.FLAGS_HAS_LARGE_COORDS) != 0) {
                mX = in.readInt();
                mY = in.readInt();
                mWidth = in.readInt();
                mHeight = in.readInt();
            } else {
                int val = in.readInt();
                mX = val & 0x7fff;
                mY = (val >> 16) & 0x7fff;
                val = in.readInt();
                mWidth = val & 0x7fff;
                mHeight = (val >> 16) & 0x7fff;
            }
            if ((flags & android.app.assist.AssistStructure.ViewNode.FLAGS_HAS_SCROLL) != 0) {
                mScrollX = in.readInt();
                mScrollY = in.readInt();
            }
            if ((flags & android.app.assist.AssistStructure.ViewNode.FLAGS_HAS_MATRIX) != 0) {
                mMatrix = new android.graphics.Matrix();
                in.readFloatArray(reader.mTmpMatrix);
                mMatrix.setValues(reader.mTmpMatrix);
            }
            if ((flags & android.app.assist.AssistStructure.ViewNode.FLAGS_HAS_ELEVATION) != 0) {
                mElevation = in.readFloat();
            }
            if ((flags & android.app.assist.AssistStructure.ViewNode.FLAGS_HAS_ALPHA) != 0) {
                mAlpha = in.readFloat();
            }
            if ((flags & android.app.assist.AssistStructure.ViewNode.FLAGS_HAS_CONTENT_DESCRIPTION) != 0) {
                mContentDescription = android.text.TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
            }
            if ((flags & android.app.assist.AssistStructure.ViewNode.FLAGS_HAS_TEXT) != 0) {
                mText = new android.app.assist.AssistStructure.ViewNodeText(in, (flags & android.app.assist.AssistStructure.ViewNode.FLAGS_HAS_COMPLEX_TEXT) == 0);
            }
            if ((flags & android.app.assist.AssistStructure.ViewNode.FLAGS_HAS_EXTRAS) != 0) {
                mExtras = in.readBundle();
            }
            if ((flags & android.app.assist.AssistStructure.ViewNode.FLAGS_HAS_CHILDREN) != 0) {
                final int NCHILDREN = in.readInt();
                if (android.app.assist.AssistStructure.DEBUG_PARCEL_TREE || android.app.assist.AssistStructure.DEBUG_PARCEL_CHILDREN)
                    android.util.Log.d(android.app.assist.AssistStructure.TAG, (((("Preparing to read " + NCHILDREN) + " children: @ #") + reader.mNumReadViews) + ", level ") + nestingLevel);

                mChildren = new android.app.assist.AssistStructure.ViewNode[NCHILDREN];
                for (int i = 0; i < NCHILDREN; i++) {
                    mChildren[i] = new android.app.assist.AssistStructure.ViewNode(reader, nestingLevel + 1);
                }
            }
        }

        int writeSelfToParcel(android.os.Parcel out, android.os.PooledStringWriter pwriter, float[] tmpMatrix) {
            int flags = mFlags & (~android.app.assist.AssistStructure.ViewNode.FLAGS_ALL_CONTROL);
            if (mId != android.view.View.NO_ID) {
                flags |= android.app.assist.AssistStructure.ViewNode.FLAGS_HAS_ID;
            }
            if ((((mX & (~0x7fff)) != 0) || ((mY & (~0x7fff)) != 0)) || (((mWidth & (~0x7fff)) != 0) | ((mHeight & (~0x7fff)) != 0))) {
                flags |= android.app.assist.AssistStructure.ViewNode.FLAGS_HAS_LARGE_COORDS;
            }
            if ((mScrollX != 0) || (mScrollY != 0)) {
                flags |= android.app.assist.AssistStructure.ViewNode.FLAGS_HAS_SCROLL;
            }
            if (mMatrix != null) {
                flags |= android.app.assist.AssistStructure.ViewNode.FLAGS_HAS_MATRIX;
            }
            if (mElevation != 0) {
                flags |= android.app.assist.AssistStructure.ViewNode.FLAGS_HAS_ELEVATION;
            }
            if (mAlpha != 1.0F) {
                flags |= android.app.assist.AssistStructure.ViewNode.FLAGS_HAS_ALPHA;
            }
            if (mContentDescription != null) {
                flags |= android.app.assist.AssistStructure.ViewNode.FLAGS_HAS_CONTENT_DESCRIPTION;
            }
            if (mText != null) {
                flags |= android.app.assist.AssistStructure.ViewNode.FLAGS_HAS_TEXT;
                if (!mText.isSimple()) {
                    flags |= android.app.assist.AssistStructure.ViewNode.FLAGS_HAS_COMPLEX_TEXT;
                }
            }
            if (mExtras != null) {
                flags |= android.app.assist.AssistStructure.ViewNode.FLAGS_HAS_EXTRAS;
            }
            if (mChildren != null) {
                flags |= android.app.assist.AssistStructure.ViewNode.FLAGS_HAS_CHILDREN;
            }
            pwriter.writeString(mClassName);
            out.writeInt(flags);
            if ((flags & android.app.assist.AssistStructure.ViewNode.FLAGS_HAS_ID) != 0) {
                out.writeInt(mId);
                if (mId != 0) {
                    pwriter.writeString(mIdEntry);
                    if (mIdEntry != null) {
                        pwriter.writeString(mIdType);
                        pwriter.writeString(mIdPackage);
                    }
                }
            }
            if ((flags & android.app.assist.AssistStructure.ViewNode.FLAGS_HAS_LARGE_COORDS) != 0) {
                out.writeInt(mX);
                out.writeInt(mY);
                out.writeInt(mWidth);
                out.writeInt(mHeight);
            } else {
                out.writeInt((mY << 16) | mX);
                out.writeInt((mHeight << 16) | mWidth);
            }
            if ((flags & android.app.assist.AssistStructure.ViewNode.FLAGS_HAS_SCROLL) != 0) {
                out.writeInt(mScrollX);
                out.writeInt(mScrollY);
            }
            if ((flags & android.app.assist.AssistStructure.ViewNode.FLAGS_HAS_MATRIX) != 0) {
                mMatrix.getValues(tmpMatrix);
                out.writeFloatArray(tmpMatrix);
            }
            if ((flags & android.app.assist.AssistStructure.ViewNode.FLAGS_HAS_ELEVATION) != 0) {
                out.writeFloat(mElevation);
            }
            if ((flags & android.app.assist.AssistStructure.ViewNode.FLAGS_HAS_ALPHA) != 0) {
                out.writeFloat(mAlpha);
            }
            if ((flags & android.app.assist.AssistStructure.ViewNode.FLAGS_HAS_CONTENT_DESCRIPTION) != 0) {
                android.text.TextUtils.writeToParcel(mContentDescription, out, 0);
            }
            if ((flags & android.app.assist.AssistStructure.ViewNode.FLAGS_HAS_TEXT) != 0) {
                mText.writeToParcel(out, (flags & android.app.assist.AssistStructure.ViewNode.FLAGS_HAS_COMPLEX_TEXT) == 0);
            }
            if ((flags & android.app.assist.AssistStructure.ViewNode.FLAGS_HAS_EXTRAS) != 0) {
                out.writeBundle(mExtras);
            }
            return flags;
        }

        /**
         * Returns the ID associated with this view, as per {@link View#getId() View.getId()}.
         */
        public int getId() {
            return mId;
        }

        /**
         * If {@link #getId()} is a resource identifier, this is the package name of that
         * identifier.  See {@link android.view.ViewStructure#setId ViewStructure.setId}
         * for more information.
         */
        public java.lang.String getIdPackage() {
            return mIdPackage;
        }

        /**
         * If {@link #getId()} is a resource identifier, this is the type name of that
         * identifier.  See {@link android.view.ViewStructure#setId ViewStructure.setId}
         * for more information.
         */
        public java.lang.String getIdType() {
            return mIdType;
        }

        /**
         * If {@link #getId()} is a resource identifier, this is the entry name of that
         * identifier.  See {@link android.view.ViewStructure#setId ViewStructure.setId}
         * for more information.
         */
        public java.lang.String getIdEntry() {
            return mIdEntry;
        }

        /**
         * Returns the left edge of this view, in pixels, relative to the left edge of its parent.
         */
        public int getLeft() {
            return mX;
        }

        /**
         * Returns the top edge of this view, in pixels, relative to the top edge of its parent.
         */
        public int getTop() {
            return mY;
        }

        /**
         * Returns the current X scroll offset of this view, as per
         * {@link android.view.View#getScrollX() View.getScrollX()}.
         */
        public int getScrollX() {
            return mScrollX;
        }

        /**
         * Returns the current Y scroll offset of this view, as per
         * {@link android.view.View#getScrollX() View.getScrollY()}.
         */
        public int getScrollY() {
            return mScrollY;
        }

        /**
         * Returns the width of this view, in pixels.
         */
        public int getWidth() {
            return mWidth;
        }

        /**
         * Returns the height of this view, in pixels.
         */
        public int getHeight() {
            return mHeight;
        }

        /**
         * Returns the transformation that has been applied to this view, such as a translation
         * or scaling.  The returned Matrix object is owned by ViewNode; do not modify it.
         * Returns null if there is no transformation applied to the view.
         */
        public android.graphics.Matrix getTransformation() {
            return mMatrix;
        }

        /**
         * Returns the visual elevation of the view, used for shadowing and other visual
         * characterstics, as set by {@link ViewStructure#setElevation
         * ViewStructure.setElevation(float)}.
         */
        public float getElevation() {
            return mElevation;
        }

        /**
         * Returns the alpha transformation of the view, used to reduce the overall opacity
         * of the view's contents, as set by {@link ViewStructure#setAlpha
         * ViewStructure.setAlpha(float)}.
         */
        public float getAlpha() {
            return mAlpha;
        }

        /**
         * Returns the visibility mode of this view, as per
         * {@link android.view.View#getVisibility() View.getVisibility()}.
         */
        public int getVisibility() {
            return mFlags & android.app.assist.AssistStructure.ViewNode.FLAGS_VISIBILITY_MASK;
        }

        /**
         * Returns true if assist data has been blocked starting at this node in the hierarchy.
         */
        public boolean isAssistBlocked() {
            return (mFlags & android.app.assist.AssistStructure.ViewNode.FLAGS_ASSIST_BLOCKED) != 0;
        }

        /**
         * Returns true if this node is in an enabled state.
         */
        public boolean isEnabled() {
            return (mFlags & android.app.assist.AssistStructure.ViewNode.FLAGS_DISABLED) == 0;
        }

        /**
         * Returns true if this node is clickable by the user.
         */
        public boolean isClickable() {
            return (mFlags & android.app.assist.AssistStructure.ViewNode.FLAGS_CLICKABLE) != 0;
        }

        /**
         * Returns true if this node can take input focus.
         */
        public boolean isFocusable() {
            return (mFlags & android.app.assist.AssistStructure.ViewNode.FLAGS_FOCUSABLE) != 0;
        }

        /**
         * Returns true if this node currently had input focus at the time that the
         * structure was collected.
         */
        public boolean isFocused() {
            return (mFlags & android.app.assist.AssistStructure.ViewNode.FLAGS_FOCUSED) != 0;
        }

        /**
         * Returns true if this node currently had accessibility focus at the time that the
         * structure was collected.
         */
        public boolean isAccessibilityFocused() {
            return (mFlags & android.app.assist.AssistStructure.ViewNode.FLAGS_ACCESSIBILITY_FOCUSED) != 0;
        }

        /**
         * Returns true if this node represents something that is checkable by the user.
         */
        public boolean isCheckable() {
            return (mFlags & android.app.assist.AssistStructure.ViewNode.FLAGS_CHECKABLE) != 0;
        }

        /**
         * Returns true if this node is currently in a checked state.
         */
        public boolean isChecked() {
            return (mFlags & android.app.assist.AssistStructure.ViewNode.FLAGS_CHECKED) != 0;
        }

        /**
         * Returns true if this node has currently been selected by the user.
         */
        public boolean isSelected() {
            return (mFlags & android.app.assist.AssistStructure.ViewNode.FLAGS_SELECTED) != 0;
        }

        /**
         * Returns true if this node has currently been activated by the user.
         */
        public boolean isActivated() {
            return (mFlags & android.app.assist.AssistStructure.ViewNode.FLAGS_ACTIVATED) != 0;
        }

        /**
         * Returns true if this node is something the user can perform a long click/press on.
         */
        public boolean isLongClickable() {
            return (mFlags & android.app.assist.AssistStructure.ViewNode.FLAGS_LONG_CLICKABLE) != 0;
        }

        /**
         * Returns true if this node is something the user can perform a context click on.
         */
        public boolean isContextClickable() {
            return (mFlags & android.app.assist.AssistStructure.ViewNode.FLAGS_CONTEXT_CLICKABLE) != 0;
        }

        /**
         * Returns the class name of the node's implementation, indicating its behavior.
         * For example, a button will report "android.widget.Button" meaning it behaves
         * like a {@link android.widget.Button}.
         */
        public java.lang.String getClassName() {
            return mClassName;
        }

        /**
         * Returns any content description associated with the node, which semantically describes
         * its purpose for accessibility and other uses.
         */
        public java.lang.CharSequence getContentDescription() {
            return mContentDescription;
        }

        /**
         * Returns any text associated with the node that is displayed to the user, or null
         * if there is none.
         */
        public java.lang.CharSequence getText() {
            return mText != null ? mText.mText : null;
        }

        /**
         * If {@link #getText()} is non-null, this is where the current selection starts.
         */
        public int getTextSelectionStart() {
            return mText != null ? mText.mTextSelectionStart : -1;
        }

        /**
         * If {@link #getText()} is non-null, this is where the current selection starts.
         * If there is no selection, returns the same value as {@link #getTextSelectionStart()},
         * indicating the cursor position.
         */
        public int getTextSelectionEnd() {
            return mText != null ? mText.mTextSelectionEnd : -1;
        }

        /**
         * If {@link #getText()} is non-null, this is the main text color associated with it.
         * If there is no text color, {@link #TEXT_COLOR_UNDEFINED} is returned.
         * Note that the text may also contain style spans that modify the color of specific
         * parts of the text.
         */
        public int getTextColor() {
            return mText != null ? mText.mTextColor : android.app.assist.AssistStructure.ViewNode.TEXT_COLOR_UNDEFINED;
        }

        /**
         * If {@link #getText()} is non-null, this is the main text background color associated
         * with it.
         * If there is no text background color, {@link #TEXT_COLOR_UNDEFINED} is returned.
         * Note that the text may also contain style spans that modify the color of specific
         * parts of the text.
         */
        public int getTextBackgroundColor() {
            return mText != null ? mText.mTextBackgroundColor : android.app.assist.AssistStructure.ViewNode.TEXT_COLOR_UNDEFINED;
        }

        /**
         * If {@link #getText()} is non-null, this is the main text size (in pixels) associated
         * with it.
         * Note that the text may also contain style spans that modify the size of specific
         * parts of the text.
         */
        public float getTextSize() {
            return mText != null ? mText.mTextSize : 0;
        }

        /**
         * If {@link #getText()} is non-null, this is the main text style associated
         * with it, containing a bit mask of {@link #TEXT_STYLE_BOLD},
         * {@link #TEXT_STYLE_BOLD}, {@link #TEXT_STYLE_STRIKE_THRU}, and/or
         * {@link #TEXT_STYLE_UNDERLINE}.
         * Note that the text may also contain style spans that modify the style of specific
         * parts of the text.
         */
        public int getTextStyle() {
            return mText != null ? mText.mTextStyle : 0;
        }

        /**
         * Return per-line offsets into the text returned by {@link #getText()}.  Each entry
         * in the array is a formatted line of text, and the value it contains is the offset
         * into the text string where that line starts.  May return null if there is no line
         * information.
         */
        public int[] getTextLineCharOffsets() {
            return mText != null ? mText.mLineCharOffsets : null;
        }

        /**
         * Return per-line baselines into the text returned by {@link #getText()}.  Each entry
         * in the array is a formatted line of text, and the value it contains is the baseline
         * where that text appears in the view.  May return null if there is no line
         * information.
         */
        public int[] getTextLineBaselines() {
            return mText != null ? mText.mLineBaselines : null;
        }

        /**
         * Return additional hint text associated with the node; this is typically used with
         * a node that takes user input, describing to the user what the input means.
         */
        public java.lang.String getHint() {
            return mText != null ? mText.mHint : null;
        }

        /**
         * Return a Bundle containing optional vendor-specific extension information.
         */
        public android.os.Bundle getExtras() {
            return mExtras;
        }

        /**
         * Return the number of children this node has.
         */
        public int getChildCount() {
            return mChildren != null ? mChildren.length : 0;
        }

        /**
         * Return a child of this node, given an index value from 0 to
         * {@link #getChildCount()}-1.
         */
        public android.app.assist.AssistStructure.ViewNode getChildAt(int index) {
            return mChildren[index];
        }
    }

    static class ViewNodeBuilder extends android.view.ViewStructure {
        final android.app.assist.AssistStructure mAssist;

        final android.app.assist.AssistStructure.ViewNode mNode;

        final boolean mAsync;

        ViewNodeBuilder(android.app.assist.AssistStructure assist, android.app.assist.AssistStructure.ViewNode node, boolean async) {
            mAssist = assist;
            mNode = node;
            mAsync = async;
        }

        @java.lang.Override
        public void setId(int id, java.lang.String packageName, java.lang.String typeName, java.lang.String entryName) {
            mNode.mId = id;
            mNode.mIdPackage = packageName;
            mNode.mIdType = typeName;
            mNode.mIdEntry = entryName;
        }

        @java.lang.Override
        public void setDimens(int left, int top, int scrollX, int scrollY, int width, int height) {
            mNode.mX = left;
            mNode.mY = top;
            mNode.mScrollX = scrollX;
            mNode.mScrollY = scrollY;
            mNode.mWidth = width;
            mNode.mHeight = height;
        }

        @java.lang.Override
        public void setTransformation(android.graphics.Matrix matrix) {
            if (matrix == null) {
                mNode.mMatrix = null;
            } else {
                mNode.mMatrix = new android.graphics.Matrix(matrix);
            }
        }

        @java.lang.Override
        public void setElevation(float elevation) {
            mNode.mElevation = elevation;
        }

        @java.lang.Override
        public void setAlpha(float alpha) {
            mNode.mAlpha = alpha;
        }

        @java.lang.Override
        public void setVisibility(int visibility) {
            mNode.mFlags = (mNode.mFlags & (~android.app.assist.AssistStructure.ViewNode.FLAGS_VISIBILITY_MASK)) | visibility;
        }

        @java.lang.Override
        public void setAssistBlocked(boolean state) {
            mNode.mFlags = (mNode.mFlags & (~android.app.assist.AssistStructure.ViewNode.FLAGS_ASSIST_BLOCKED)) | (state ? android.app.assist.AssistStructure.ViewNode.FLAGS_ASSIST_BLOCKED : 0);
        }

        @java.lang.Override
        public void setEnabled(boolean state) {
            mNode.mFlags = (mNode.mFlags & (~android.app.assist.AssistStructure.ViewNode.FLAGS_DISABLED)) | (state ? 0 : android.app.assist.AssistStructure.ViewNode.FLAGS_DISABLED);
        }

        @java.lang.Override
        public void setClickable(boolean state) {
            mNode.mFlags = (mNode.mFlags & (~android.app.assist.AssistStructure.ViewNode.FLAGS_CLICKABLE)) | (state ? android.app.assist.AssistStructure.ViewNode.FLAGS_CLICKABLE : 0);
        }

        @java.lang.Override
        public void setLongClickable(boolean state) {
            mNode.mFlags = (mNode.mFlags & (~android.app.assist.AssistStructure.ViewNode.FLAGS_LONG_CLICKABLE)) | (state ? android.app.assist.AssistStructure.ViewNode.FLAGS_LONG_CLICKABLE : 0);
        }

        @java.lang.Override
        public void setContextClickable(boolean state) {
            mNode.mFlags = (mNode.mFlags & (~android.app.assist.AssistStructure.ViewNode.FLAGS_CONTEXT_CLICKABLE)) | (state ? android.app.assist.AssistStructure.ViewNode.FLAGS_CONTEXT_CLICKABLE : 0);
        }

        @java.lang.Override
        public void setFocusable(boolean state) {
            mNode.mFlags = (mNode.mFlags & (~android.app.assist.AssistStructure.ViewNode.FLAGS_FOCUSABLE)) | (state ? android.app.assist.AssistStructure.ViewNode.FLAGS_FOCUSABLE : 0);
        }

        @java.lang.Override
        public void setFocused(boolean state) {
            mNode.mFlags = (mNode.mFlags & (~android.app.assist.AssistStructure.ViewNode.FLAGS_FOCUSED)) | (state ? android.app.assist.AssistStructure.ViewNode.FLAGS_FOCUSED : 0);
        }

        @java.lang.Override
        public void setAccessibilityFocused(boolean state) {
            mNode.mFlags = (mNode.mFlags & (~android.app.assist.AssistStructure.ViewNode.FLAGS_ACCESSIBILITY_FOCUSED)) | (state ? android.app.assist.AssistStructure.ViewNode.FLAGS_ACCESSIBILITY_FOCUSED : 0);
        }

        @java.lang.Override
        public void setCheckable(boolean state) {
            mNode.mFlags = (mNode.mFlags & (~android.app.assist.AssistStructure.ViewNode.FLAGS_CHECKABLE)) | (state ? android.app.assist.AssistStructure.ViewNode.FLAGS_CHECKABLE : 0);
        }

        @java.lang.Override
        public void setChecked(boolean state) {
            mNode.mFlags = (mNode.mFlags & (~android.app.assist.AssistStructure.ViewNode.FLAGS_CHECKED)) | (state ? android.app.assist.AssistStructure.ViewNode.FLAGS_CHECKED : 0);
        }

        @java.lang.Override
        public void setSelected(boolean state) {
            mNode.mFlags = (mNode.mFlags & (~android.app.assist.AssistStructure.ViewNode.FLAGS_SELECTED)) | (state ? android.app.assist.AssistStructure.ViewNode.FLAGS_SELECTED : 0);
        }

        @java.lang.Override
        public void setActivated(boolean state) {
            mNode.mFlags = (mNode.mFlags & (~android.app.assist.AssistStructure.ViewNode.FLAGS_ACTIVATED)) | (state ? android.app.assist.AssistStructure.ViewNode.FLAGS_ACTIVATED : 0);
        }

        @java.lang.Override
        public void setClassName(java.lang.String className) {
            mNode.mClassName = className;
        }

        @java.lang.Override
        public void setContentDescription(java.lang.CharSequence contentDescription) {
            mNode.mContentDescription = contentDescription;
        }

        private final android.app.assist.AssistStructure.ViewNodeText getNodeText() {
            if (mNode.mText != null) {
                return mNode.mText;
            }
            mNode.mText = new android.app.assist.AssistStructure.ViewNodeText();
            return mNode.mText;
        }

        @java.lang.Override
        public void setText(java.lang.CharSequence text) {
            android.app.assist.AssistStructure.ViewNodeText t = getNodeText();
            t.mText = text;
            t.mTextSelectionStart = t.mTextSelectionEnd = -1;
        }

        @java.lang.Override
        public void setText(java.lang.CharSequence text, int selectionStart, int selectionEnd) {
            android.app.assist.AssistStructure.ViewNodeText t = getNodeText();
            t.mText = text;
            t.mTextSelectionStart = selectionStart;
            t.mTextSelectionEnd = selectionEnd;
        }

        @java.lang.Override
        public void setTextStyle(float size, int fgColor, int bgColor, int style) {
            android.app.assist.AssistStructure.ViewNodeText t = getNodeText();
            t.mTextColor = fgColor;
            t.mTextBackgroundColor = bgColor;
            t.mTextSize = size;
            t.mTextStyle = style;
        }

        @java.lang.Override
        public void setTextLines(int[] charOffsets, int[] baselines) {
            android.app.assist.AssistStructure.ViewNodeText t = getNodeText();
            t.mLineCharOffsets = charOffsets;
            t.mLineBaselines = baselines;
        }

        @java.lang.Override
        public void setHint(java.lang.CharSequence hint) {
            getNodeText().mHint = (hint != null) ? hint.toString() : null;
        }

        @java.lang.Override
        public java.lang.CharSequence getText() {
            return mNode.mText != null ? mNode.mText.mText : null;
        }

        @java.lang.Override
        public int getTextSelectionStart() {
            return mNode.mText != null ? mNode.mText.mTextSelectionStart : -1;
        }

        @java.lang.Override
        public int getTextSelectionEnd() {
            return mNode.mText != null ? mNode.mText.mTextSelectionEnd : -1;
        }

        @java.lang.Override
        public java.lang.CharSequence getHint() {
            return mNode.mText != null ? mNode.mText.mHint : null;
        }

        @java.lang.Override
        public android.os.Bundle getExtras() {
            if (mNode.mExtras != null) {
                return mNode.mExtras;
            }
            mNode.mExtras = new android.os.Bundle();
            return mNode.mExtras;
        }

        @java.lang.Override
        public boolean hasExtras() {
            return mNode.mExtras != null;
        }

        @java.lang.Override
        public void setChildCount(int num) {
            mNode.mChildren = new android.app.assist.AssistStructure.ViewNode[num];
        }

        @java.lang.Override
        public int addChildCount(int num) {
            if (mNode.mChildren == null) {
                setChildCount(num);
                return 0;
            }
            final int start = mNode.mChildren.length;
            android.app.assist.AssistStructure.ViewNode[] newArray = new android.app.assist.AssistStructure.ViewNode[start + num];
            java.lang.System.arraycopy(mNode.mChildren, 0, newArray, 0, start);
            mNode.mChildren = newArray;
            return start;
        }

        @java.lang.Override
        public int getChildCount() {
            return mNode.mChildren != null ? mNode.mChildren.length : 0;
        }

        @java.lang.Override
        public android.view.ViewStructure newChild(int index) {
            android.app.assist.AssistStructure.ViewNode node = new android.app.assist.AssistStructure.ViewNode();
            mNode.mChildren[index] = node;
            return new android.app.assist.AssistStructure.ViewNodeBuilder(mAssist, node, false);
        }

        @java.lang.Override
        public android.view.ViewStructure asyncNewChild(int index) {
            synchronized(mAssist) {
                android.app.assist.AssistStructure.ViewNode node = new android.app.assist.AssistStructure.ViewNode();
                mNode.mChildren[index] = node;
                android.app.assist.AssistStructure.ViewNodeBuilder builder = new android.app.assist.AssistStructure.ViewNodeBuilder(mAssist, node, true);
                mAssist.mPendingAsyncChildren.add(builder);
                return builder;
            }
        }

        @java.lang.Override
        public void asyncCommit() {
            synchronized(mAssist) {
                if (!mAsync) {
                    throw new java.lang.IllegalStateException(("Child " + this) + " was not created with ViewStructure.asyncNewChild");
                }
                if (!mAssist.mPendingAsyncChildren.remove(this)) {
                    throw new java.lang.IllegalStateException(("Child " + this) + " already committed");
                }
                mAssist.notifyAll();
            }
        }

        @java.lang.Override
        public android.graphics.Rect getTempRect() {
            return mAssist.mTmpRect;
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public AssistStructure(android.app.Activity activity) {
        mHaveData = true;
        mActivityComponent = activity.getComponentName();
        java.util.ArrayList<android.view.ViewRootImpl> views = android.view.WindowManagerGlobal.getInstance().getRootViews(activity.getActivityToken());
        for (int i = 0; i < views.size(); i++) {
            android.view.ViewRootImpl root = views.get(i);
            mWindowNodes.add(new android.app.assist.AssistStructure.WindowNode(this, root));
        }
    }

    public AssistStructure() {
        mHaveData = true;
        mActivityComponent = null;
    }

    /**
     *
     *
     * @unknown 
     */
    public AssistStructure(android.os.Parcel in) {
        mReceiveChannel = in.readStrongBinder();
    }

    /**
     *
     *
     * @unknown 
     */
    public void dump() {
        android.util.Log.i(android.app.assist.AssistStructure.TAG, "Activity: " + mActivityComponent.flattenToShortString());
        final int N = getWindowNodeCount();
        for (int i = 0; i < N; i++) {
            android.app.assist.AssistStructure.WindowNode node = getWindowNodeAt(i);
            android.util.Log.i(android.app.assist.AssistStructure.TAG, ((((((((((("Window #" + i) + " [") + node.getLeft()) + ",") + node.getTop()) + " ") + node.getWidth()) + "x") + node.getHeight()) + "]") + " ") + node.getTitle());
            dump("  ", node.getRootViewNode());
        }
    }

    void dump(java.lang.String prefix, android.app.assist.AssistStructure.ViewNode node) {
        android.util.Log.i(android.app.assist.AssistStructure.TAG, ((((((((((prefix + "View [") + node.getLeft()) + ",") + node.getTop()) + " ") + node.getWidth()) + "x") + node.getHeight()) + "]") + " ") + node.getClassName());
        int id = node.getId();
        if (id != 0) {
            java.lang.StringBuilder sb = new java.lang.StringBuilder();
            sb.append(prefix);
            sb.append("  ID: #");
            sb.append(java.lang.Integer.toHexString(id));
            java.lang.String entry = node.getIdEntry();
            if (entry != null) {
                java.lang.String type = node.getIdType();
                java.lang.String pkg = node.getIdPackage();
                sb.append(" ");
                sb.append(pkg);
                sb.append(":");
                sb.append(type);
                sb.append("/");
                sb.append(entry);
            }
            android.util.Log.i(android.app.assist.AssistStructure.TAG, sb.toString());
        }
        int scrollX = node.getScrollX();
        int scrollY = node.getScrollY();
        if ((scrollX != 0) || (scrollY != 0)) {
            android.util.Log.i(android.app.assist.AssistStructure.TAG, (((prefix + "  Scroll: ") + scrollX) + ",") + scrollY);
        }
        android.graphics.Matrix matrix = node.getTransformation();
        if (matrix != null) {
            android.util.Log.i(android.app.assist.AssistStructure.TAG, (prefix + "  Transformation: ") + matrix);
        }
        float elevation = node.getElevation();
        if (elevation != 0) {
            android.util.Log.i(android.app.assist.AssistStructure.TAG, (prefix + "  Elevation: ") + elevation);
        }
        float alpha = node.getAlpha();
        if (alpha != 0) {
            android.util.Log.i(android.app.assist.AssistStructure.TAG, (prefix + "  Alpha: ") + elevation);
        }
        java.lang.CharSequence contentDescription = node.getContentDescription();
        if (contentDescription != null) {
            android.util.Log.i(android.app.assist.AssistStructure.TAG, (prefix + "  Content description: ") + contentDescription);
        }
        java.lang.CharSequence text = node.getText();
        if (text != null) {
            android.util.Log.i(android.app.assist.AssistStructure.TAG, (((((prefix + "  Text (sel ") + node.getTextSelectionStart()) + "-") + node.getTextSelectionEnd()) + "): ") + text);
            android.util.Log.i(android.app.assist.AssistStructure.TAG, (((prefix + "  Text size: ") + node.getTextSize()) + " , style: #") + node.getTextStyle());
            android.util.Log.i(android.app.assist.AssistStructure.TAG, (((prefix + "  Text color fg: #") + java.lang.Integer.toHexString(node.getTextColor())) + ", bg: #") + java.lang.Integer.toHexString(node.getTextBackgroundColor()));
        }
        java.lang.String hint = node.getHint();
        if (hint != null) {
            android.util.Log.i(android.app.assist.AssistStructure.TAG, (prefix + "  Hint: ") + hint);
        }
        android.os.Bundle extras = node.getExtras();
        if (extras != null) {
            android.util.Log.i(android.app.assist.AssistStructure.TAG, (prefix + "  Extras: ") + extras);
        }
        if (node.isAssistBlocked()) {
            android.util.Log.i(android.app.assist.AssistStructure.TAG, prefix + "  BLOCKED");
        }
        final int NCHILDREN = node.getChildCount();
        if (NCHILDREN > 0) {
            android.util.Log.i(android.app.assist.AssistStructure.TAG, prefix + "  Children:");
            java.lang.String cprefix = prefix + "    ";
            for (int i = 0; i < NCHILDREN; i++) {
                android.app.assist.AssistStructure.ViewNode cnode = node.getChildAt(i);
                dump(cprefix, cnode);
            }
        }
    }

    /**
     * Return the activity this AssistStructure came from.
     */
    public android.content.ComponentName getActivityComponent() {
        ensureData();
        return mActivityComponent;
    }

    /**
     * Return the number of window contents that have been collected in this assist data.
     */
    public int getWindowNodeCount() {
        ensureData();
        return mWindowNodes.size();
    }

    /**
     * Return one of the windows in the assist data.
     *
     * @param index
     * 		Which window to retrieve, may be 0 to {@link #getWindowNodeCount()}-1.
     */
    public android.app.assist.AssistStructure.WindowNode getWindowNodeAt(int index) {
        ensureData();
        return mWindowNodes.get(index);
    }

    /**
     *
     *
     * @unknown 
     */
    public void ensureData() {
        if (mHaveData) {
            return;
        }
        mHaveData = true;
        android.app.assist.AssistStructure.ParcelTransferReader reader = new android.app.assist.AssistStructure.ParcelTransferReader(mReceiveChannel);
        reader.go();
    }

    boolean waitForReady() {
        boolean skipStructure = false;
        synchronized(this) {
            long endTime = android.os.SystemClock.uptimeMillis() + 5000;
            long now;
            while ((mPendingAsyncChildren.size() > 0) && ((now = android.os.SystemClock.uptimeMillis()) < endTime)) {
                try {
                    wait(endTime - now);
                } catch (java.lang.InterruptedException e) {
                }
            } 
            if (mPendingAsyncChildren.size() > 0) {
                // We waited too long, assume none of the assist structure is valid.
                android.util.Log.w(android.app.assist.AssistStructure.TAG, ("Skipping assist structure, waiting too long for async children (have " + mPendingAsyncChildren.size()) + " remaining");
                skipStructure = true;
            }
        }
        return !skipStructure;
    }

    /**
     *
     *
     * @unknown 
     */
    public void clearSendChannel() {
        if (mSendChannel != null) {
            mSendChannel.mAssistStructure = null;
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(android.os.Parcel out, int flags) {
        if (mHaveData) {
            // This object holds its data.  We want to write a send channel that the
            // other side can use to retrieve that data.
            if (mSendChannel == null) {
                mSendChannel = new android.app.assist.AssistStructure.SendChannel(this);
            }
            out.writeStrongBinder(mSendChannel);
        } else {
            // This object doesn't hold its data, so just propagate along its receive channel.
            out.writeStrongBinder(mReceiveChannel);
        }
    }

    public static final android.os.Parcelable.Creator<android.app.assist.AssistStructure> CREATOR = new android.os.Parcelable.Creator<android.app.assist.AssistStructure>() {
        public android.app.assist.AssistStructure createFromParcel(android.os.Parcel in) {
            return new android.app.assist.AssistStructure(in);
        }

        public android.app.assist.AssistStructure[] newArray(int size) {
            return new android.app.assist.AssistStructure[size];
        }
    };
}

