/**
 * Copyright (C) 2018 The Android Open Source Project
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
package android.view;


/**
 * Holder for state of system windows that cause window insets for all other windows in the system.
 *
 * @unknown 
 */
public class InsetsState implements android.os.Parcelable {
    /**
     * Internal representation of inset source types. This is different from the public API in
     * {@link WindowInsets.Type} as one type from the public API might indicate multiple windows
     * at the same time.
     */
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @android.annotation.IntDef(prefix = "TYPE", value = { android.view.InsetsState.TYPE_TOP_BAR, android.view.InsetsState.TYPE_SIDE_BAR_1, android.view.InsetsState.TYPE_SIDE_BAR_2, android.view.InsetsState.TYPE_SIDE_BAR_3, android.view.InsetsState.TYPE_TOP_GESTURES, android.view.InsetsState.TYPE_BOTTOM_GESTURES, android.view.InsetsState.TYPE_LEFT_GESTURES, android.view.InsetsState.TYPE_RIGHT_GESTURES, android.view.InsetsState.TYPE_TOP_TAPPABLE_ELEMENT, android.view.InsetsState.TYPE_BOTTOM_TAPPABLE_ELEMENT, android.view.InsetsState.TYPE_IME })
    public @interface InternalInsetType {}

    static final int FIRST_TYPE = 0;

    /**
     * Top bar. Can be status bar or caption in freeform windowing mode.
     */
    public static final int TYPE_TOP_BAR = android.view.InsetsState.FIRST_TYPE;

    /**
     * Up to 3 side bars that appear on left/right/bottom. On phones there is only one side bar
     * (the navigation bar, see {@link #TYPE_NAVIGATION_BAR}), but other form factors might have
     * multiple, like Android Auto.
     */
    public static final int TYPE_SIDE_BAR_1 = 1;

    public static final int TYPE_SIDE_BAR_2 = 2;

    public static final int TYPE_SIDE_BAR_3 = 3;

    public static final int TYPE_TOP_GESTURES = 4;

    public static final int TYPE_BOTTOM_GESTURES = 5;

    public static final int TYPE_LEFT_GESTURES = 6;

    public static final int TYPE_RIGHT_GESTURES = 7;

    public static final int TYPE_TOP_TAPPABLE_ELEMENT = 8;

    public static final int TYPE_BOTTOM_TAPPABLE_ELEMENT = 9;

    /**
     * Input method window.
     */
    public static final int TYPE_IME = 10;

    static final int LAST_TYPE = android.view.InsetsState.TYPE_IME;

    // Derived types
    /**
     * First side bar is navigation bar.
     */
    public static final int TYPE_NAVIGATION_BAR = android.view.InsetsState.TYPE_SIDE_BAR_1;

    /**
     * A shelf is the same as the navigation bar.
     */
    public static final int TYPE_SHELF = android.view.InsetsState.TYPE_NAVIGATION_BAR;

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @android.annotation.IntDef(prefix = "INSET_SIDE", value = { android.view.InsetsState.INSET_SIDE_LEFT, android.view.InsetsState.INSET_SIDE_TOP, android.view.InsetsState.INSET_SIDE_RIGHT, android.view.InsetsState.INSET_SIDE_BOTTOM, android.view.InsetsState.INSET_SIDE_UNKNWON })
    public @interface InsetSide {}

    static final int INSET_SIDE_LEFT = 0;

    static final int INSET_SIDE_TOP = 1;

    static final int INSET_SIDE_RIGHT = 2;

    static final int INSET_SIDE_BOTTOM = 3;

    static final int INSET_SIDE_UNKNWON = 4;

    private final android.util.ArrayMap<java.lang.Integer, android.view.InsetsSource> mSources = new android.util.ArrayMap();

    /**
     * The frame of the display these sources are relative to.
     */
    private final android.graphics.Rect mDisplayFrame = new android.graphics.Rect();

    public InsetsState() {
    }

    public InsetsState(android.view.InsetsState copy) {
        set(copy);
    }

    public InsetsState(android.view.InsetsState copy, boolean copySources) {
        set(copy, copySources);
    }

    /**
     * Calculates {@link WindowInsets} based on the current source configuration.
     *
     * @param frame
     * 		The frame to calculate the insets relative to.
     * @return The calculated insets.
     */
    public android.view.WindowInsets calculateInsets(android.graphics.Rect frame, boolean isScreenRound, boolean alwaysConsumeSystemBars, android.view.DisplayCutout cutout, @android.annotation.Nullable
    android.graphics.Rect legacyContentInsets, @android.annotation.Nullable
    android.graphics.Rect legacyStableInsets, int legacySoftInputMode, @android.annotation.Nullable
    @android.view.InsetsState.InsetSide
    android.util.SparseIntArray typeSideMap) {
        android.graphics.Insets[] typeInsetsMap = new android.graphics.Insets[android.view.WindowInsets.Type.SIZE];
        android.graphics.Insets[] typeMaxInsetsMap = new android.graphics.Insets[android.view.WindowInsets.Type.SIZE];
        boolean[] typeVisibilityMap = new boolean[android.view.WindowInsets.Type.SIZE];
        final android.graphics.Rect relativeFrame = new android.graphics.Rect(frame);
        final android.graphics.Rect relativeFrameMax = new android.graphics.Rect(frame);
        if (((android.view.ViewRootImpl.sNewInsetsMode != android.view.ViewRootImpl.NEW_INSETS_MODE_FULL) && (legacyContentInsets != null)) && (legacyStableInsets != null)) {
            android.view.WindowInsets.assignCompatInsets(typeInsetsMap, legacyContentInsets);
            android.view.WindowInsets.assignCompatInsets(typeMaxInsetsMap, legacyStableInsets);
        }
        for (int type = android.view.InsetsState.FIRST_TYPE; type <= android.view.InsetsState.LAST_TYPE; type++) {
            android.view.InsetsSource source = mSources.get(type);
            if (source == null) {
                continue;
            }
            boolean skipSystemBars = (android.view.ViewRootImpl.sNewInsetsMode != android.view.ViewRootImpl.NEW_INSETS_MODE_FULL) && ((type == android.view.InsetsState.TYPE_TOP_BAR) || (type == android.view.InsetsState.TYPE_NAVIGATION_BAR));
            boolean skipIme = (source.getType() == android.view.InsetsState.TYPE_IME) && ((legacySoftInputMode & android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE) == 0);
            boolean skipLegacyTypes = (android.view.ViewRootImpl.sNewInsetsMode == android.view.ViewRootImpl.NEW_INSETS_MODE_NONE) && ((android.view.InsetsState.toPublicType(type) & android.view.WindowInsets.Type.compatSystemInsets()) != 0);
            if ((skipSystemBars || skipIme) || skipLegacyTypes) {
                typeVisibilityMap[android.view.WindowInsets.Type.indexOf(android.view.InsetsState.toPublicType(type))] = source.isVisible();
                continue;
            }
            /* ignoreVisibility */
            processSource(source, relativeFrame, false, typeInsetsMap, typeSideMap, typeVisibilityMap);
            // IME won't be reported in max insets as the size depends on the EditorInfo of the IME
            // target.
            if (source.getType() != android.view.InsetsState.TYPE_IME) {
                /* ignoreVisibility */
                /* typeSideMap */
                /* typeVisibilityMap */
                processSource(source, relativeFrameMax, true, typeMaxInsetsMap, null, null);
            }
        }
        return new android.view.WindowInsets(typeInsetsMap, typeMaxInsetsMap, typeVisibilityMap, isScreenRound, alwaysConsumeSystemBars, cutout);
    }

    private void processSource(android.view.InsetsSource source, android.graphics.Rect relativeFrame, boolean ignoreVisibility, android.graphics.Insets[] typeInsetsMap, @android.annotation.Nullable
    @android.view.InsetsState.InsetSide
    android.util.SparseIntArray typeSideMap, @android.annotation.Nullable
    boolean[] typeVisibilityMap) {
        android.graphics.Insets insets = source.calculateInsets(relativeFrame, ignoreVisibility);
        int type = android.view.InsetsState.toPublicType(source.getType());
        processSourceAsPublicType(source, typeInsetsMap, typeSideMap, typeVisibilityMap, insets, type);
        if (type == android.view.WindowInsets.Type.MANDATORY_SYSTEM_GESTURES) {
            // Mandatory system gestures are also system gestures.
            // TODO: find a way to express this more generally. One option would be to define
            // Type.systemGestureInsets() as NORMAL | MANDATORY, but then we lose the
            // ability to set systemGestureInsets() independently from
            // mandatorySystemGestureInsets() in the Builder.
            processSourceAsPublicType(source, typeInsetsMap, typeSideMap, typeVisibilityMap, insets, android.view.WindowInsets.Type.SYSTEM_GESTURES);
        }
    }

    private void processSourceAsPublicType(android.view.InsetsSource source, android.graphics.Insets[] typeInsetsMap, @android.view.InsetsState.InsetSide
    @android.annotation.Nullable
    android.util.SparseIntArray typeSideMap, @android.annotation.Nullable
    boolean[] typeVisibilityMap, android.graphics.Insets insets, int type) {
        int index = android.view.WindowInsets.Type.indexOf(type);
        android.graphics.Insets existing = typeInsetsMap[index];
        if (existing == null) {
            typeInsetsMap[index] = insets;
        } else {
            typeInsetsMap[index] = android.graphics.Insets.max(existing, insets);
        }
        if (typeVisibilityMap != null) {
            typeVisibilityMap[index] = source.isVisible();
        }
        if ((typeSideMap != null) && (!android.graphics.Insets.NONE.equals(insets))) {
            @android.view.InsetsState.InsetSide
            int insetSide = getInsetSide(insets);
            if (insetSide != android.view.InsetsState.INSET_SIDE_UNKNWON) {
                typeSideMap.put(source.getType(), getInsetSide(insets));
            }
        }
    }

    /**
     * Retrieves the side for a certain {@code insets}. It is required that only one field l/t/r/b
     * is set in order that this method returns a meaningful result.
     */
    @android.view.InsetsState.InsetSide
    private int getInsetSide(android.graphics.Insets insets) {
        if (insets.left != 0) {
            return android.view.InsetsState.INSET_SIDE_LEFT;
        }
        if (insets.top != 0) {
            return android.view.InsetsState.INSET_SIDE_TOP;
        }
        if (insets.right != 0) {
            return android.view.InsetsState.INSET_SIDE_RIGHT;
        }
        if (insets.bottom != 0) {
            return android.view.InsetsState.INSET_SIDE_BOTTOM;
        }
        return android.view.InsetsState.INSET_SIDE_UNKNWON;
    }

    public android.view.InsetsSource getSource(@android.view.InsetsState.InternalInsetType
    int type) {
        return mSources.computeIfAbsent(type, android.view.InsetsSource::new);
    }

    public void setDisplayFrame(android.graphics.Rect frame) {
        mDisplayFrame.set(frame);
    }

    public android.graphics.Rect getDisplayFrame() {
        return mDisplayFrame;
    }

    /**
     * Modifies the state of this class to exclude a certain type to make it ready for dispatching
     * to the client.
     *
     * @param type
     * 		The {@link InternalInsetType} of the source to remove
     */
    public void removeSource(int type) {
        mSources.remove(type);
    }

    public void set(android.view.InsetsState other) {
        /* copySources */
        set(other, false);
    }

    public void set(android.view.InsetsState other, boolean copySources) {
        mDisplayFrame.set(other.mDisplayFrame);
        mSources.clear();
        if (copySources) {
            for (int i = 0; i < other.mSources.size(); i++) {
                android.view.InsetsSource source = other.mSources.valueAt(i);
                mSources.put(source.getType(), new android.view.InsetsSource(source));
            }
        } else {
            mSources.putAll(other.mSources);
        }
    }

    public void addSource(android.view.InsetsSource source) {
        mSources.put(source.getType(), source);
    }

    public int getSourcesCount() {
        return mSources.size();
    }

    public android.view.InsetsSource sourceAt(int index) {
        return mSources.valueAt(index);
    }

    @android.view.InsetsState.InternalInsetType
    public static android.util.ArraySet<java.lang.Integer> toInternalType(@android.view.WindowInsets.Type.InsetType
    int insetTypes) {
        final android.util.ArraySet<java.lang.Integer> result = new android.util.ArraySet();
        if ((insetTypes & android.view.WindowInsets.Type.TOP_BAR) != 0) {
            result.add(android.view.InsetsState.TYPE_TOP_BAR);
        }
        if ((insetTypes & android.view.WindowInsets.Type.SIDE_BARS) != 0) {
            result.add(android.view.InsetsState.TYPE_SIDE_BAR_1);
            result.add(android.view.InsetsState.TYPE_SIDE_BAR_2);
            result.add(android.view.InsetsState.TYPE_SIDE_BAR_3);
        }
        if ((insetTypes & android.view.WindowInsets.Type.IME) != 0) {
            result.add(android.view.InsetsState.TYPE_IME);
        }
        return result;
    }

    @android.view.WindowInsets.Type.InsetType
    static int toPublicType(@android.view.InsetsState.InternalInsetType
    int type) {
        switch (type) {
            case android.view.InsetsState.TYPE_TOP_BAR :
                return android.view.WindowInsets.Type.TOP_BAR;
            case android.view.InsetsState.TYPE_SIDE_BAR_1 :
            case android.view.InsetsState.TYPE_SIDE_BAR_2 :
            case android.view.InsetsState.TYPE_SIDE_BAR_3 :
                return android.view.WindowInsets.Type.SIDE_BARS;
            case android.view.InsetsState.TYPE_IME :
                return android.view.WindowInsets.Type.IME;
            case android.view.InsetsState.TYPE_TOP_GESTURES :
            case android.view.InsetsState.TYPE_BOTTOM_GESTURES :
                return android.view.WindowInsets.Type.MANDATORY_SYSTEM_GESTURES;
            case android.view.InsetsState.TYPE_LEFT_GESTURES :
            case android.view.InsetsState.TYPE_RIGHT_GESTURES :
                return android.view.WindowInsets.Type.SYSTEM_GESTURES;
            case android.view.InsetsState.TYPE_TOP_TAPPABLE_ELEMENT :
            case android.view.InsetsState.TYPE_BOTTOM_TAPPABLE_ELEMENT :
                return android.view.WindowInsets.Type.TAPPABLE_ELEMENT;
            default :
                throw new java.lang.IllegalArgumentException("Unknown type: " + type);
        }
    }

    public static boolean getDefaultVisibility(@android.view.WindowInsets.Type.InsetType
    int type) {
        switch (type) {
            case android.view.InsetsState.TYPE_TOP_BAR :
            case android.view.InsetsState.TYPE_SIDE_BAR_1 :
            case android.view.InsetsState.TYPE_SIDE_BAR_2 :
            case android.view.InsetsState.TYPE_SIDE_BAR_3 :
                return true;
            case android.view.InsetsState.TYPE_IME :
                return false;
            default :
                return true;
        }
    }

    public void dump(java.lang.String prefix, java.io.PrintWriter pw) {
        pw.println(prefix + "InsetsState");
        for (int i = mSources.size() - 1; i >= 0; i--) {
            dump(prefix + "  ", pw);
        }
    }

    public static java.lang.String typeToString(int type) {
        switch (type) {
            case android.view.InsetsState.TYPE_TOP_BAR :
                return "TYPE_TOP_BAR";
            case android.view.InsetsState.TYPE_SIDE_BAR_1 :
                return "TYPE_SIDE_BAR_1";
            case android.view.InsetsState.TYPE_SIDE_BAR_2 :
                return "TYPE_SIDE_BAR_2";
            case android.view.InsetsState.TYPE_SIDE_BAR_3 :
                return "TYPE_SIDE_BAR_3";
            case android.view.InsetsState.TYPE_TOP_GESTURES :
                return "TYPE_TOP_GESTURES";
            case android.view.InsetsState.TYPE_BOTTOM_GESTURES :
                return "TYPE_BOTTOM_GESTURES";
            case android.view.InsetsState.TYPE_LEFT_GESTURES :
                return "TYPE_LEFT_GESTURES";
            case android.view.InsetsState.TYPE_RIGHT_GESTURES :
                return "TYPE_RIGHT_GESTURES";
            case android.view.InsetsState.TYPE_TOP_TAPPABLE_ELEMENT :
                return "TYPE_TOP_TAPPABLE_ELEMENT";
            case android.view.InsetsState.TYPE_BOTTOM_TAPPABLE_ELEMENT :
                return "TYPE_BOTTOM_TAPPABLE_ELEMENT";
            default :
                return "TYPE_UNKNOWN_" + type;
        }
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if ((o == null) || (getClass() != o.getClass())) {
            return false;
        }
        android.view.InsetsState state = ((android.view.InsetsState) (o));
        if (!mDisplayFrame.equals(state.mDisplayFrame)) {
            return false;
        }
        if (mSources.size() != state.mSources.size()) {
            return false;
        }
        for (int i = mSources.size() - 1; i >= 0; i--) {
            android.view.InsetsSource source = mSources.valueAt(i);
            android.view.InsetsSource otherSource = state.mSources.get(source.getType());
            if (otherSource == null) {
                return false;
            }
            if (!otherSource.equals(source)) {
                return false;
            }
        }
        return true;
    }

    @java.lang.Override
    public int hashCode() {
        return java.util.Objects.hash(mDisplayFrame, mSources);
    }

    public InsetsState(android.os.Parcel in) {
        readFromParcel(in);
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeParcelable(mDisplayFrame, flags);
        dest.writeInt(mSources.size());
        for (int i = 0; i < mSources.size(); i++) {
            dest.writeParcelable(mSources.valueAt(i), flags);
        }
    }

    @android.annotation.NonNull
    public static final android.view.Creator<android.view.InsetsState> CREATOR = new android.view.Creator<android.view.InsetsState>() {
        public android.view.InsetsState createFromParcel(android.os.Parcel in) {
            return new android.view.InsetsState(in);
        }

        public android.view.InsetsState[] newArray(int size) {
            return new android.view.InsetsState[size];
        }
    };

    public void readFromParcel(android.os.Parcel in) {
        mSources.clear();
        mDisplayFrame.set(/* loader */
        in.readParcelable(null));
        final int size = in.readInt();
        for (int i = 0; i < size; i++) {
            final android.view.InsetsSource source = /* loader */
            in.readParcelable(null);
            mSources.put(source.getType(), source);
        }
    }
}

