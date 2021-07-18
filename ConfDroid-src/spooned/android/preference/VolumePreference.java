/**
 * Copyright (C) 2007 The Android Open Source Project
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
package android.preference;


/**
 *
 *
 * @unknown 
 * @deprecated Use the <a href="{@docRoot }jetpack/androidx.html">AndroidX</a>
<a href="{@docRoot }reference/androidx/preference/package-summary.html">
Preference Library</a> for consistent behavior across all devices. For more information on
using the AndroidX Preference Library see
<a href="{@docRoot }guide/topics/ui/settings.html">Settings</a>.
 */
@java.lang.Deprecated
public class VolumePreference extends android.preference.SeekBarDialogPreference implements android.preference.PreferenceManager.OnActivityStopListener , android.preference.SeekBarVolumizer.Callback , android.view.View.OnKeyListener {
    @android.annotation.UnsupportedAppUsage
    private int mStreamType;

    /**
     * May be null if the dialog isn't visible.
     */
    private android.preference.SeekBarVolumizer mSeekBarVolumizer;

    public VolumePreference(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        final android.content.res.TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.VolumePreference, defStyleAttr, defStyleRes);
        mStreamType = a.getInt(android.R.styleable.VolumePreference_streamType, 0);
        a.recycle();
    }

    public VolumePreference(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    @android.annotation.UnsupportedAppUsage
    public VolumePreference(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, R.attr.seekBarDialogPreferenceStyle);
    }

    public VolumePreference(android.content.Context context) {
        this(context, null);
    }

    public void setStreamType(int streamType) {
        mStreamType = streamType;
    }

    @java.lang.Override
    protected void onBindDialogView(android.view.View view) {
        super.onBindDialogView(view);
        final android.widget.SeekBar seekBar = ((android.widget.SeekBar) (view.findViewById(R.id.seekbar)));
        mSeekBarVolumizer = new android.preference.SeekBarVolumizer(getContext(), mStreamType, null, this);
        mSeekBarVolumizer.start();
        mSeekBarVolumizer.setSeekBar(seekBar);
        getPreferenceManager().registerOnActivityStopListener(this);
        // grab focus and key events so that pressing the volume buttons in the
        // dialog doesn't also show the normal volume adjust toast.
        view.setOnKeyListener(this);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
    }

    public boolean onKey(android.view.View v, int keyCode, android.view.KeyEvent event) {
        // If key arrives immediately after the activity has been cleaned up.
        if (mSeekBarVolumizer == null)
            return true;

        boolean isdown = event.getAction() == android.view.KeyEvent.ACTION_DOWN;
        switch (keyCode) {
            case android.view.KeyEvent.KEYCODE_VOLUME_DOWN :
                if (isdown) {
                    mSeekBarVolumizer.changeVolumeBy(-1);
                }
                return true;
            case android.view.KeyEvent.KEYCODE_VOLUME_UP :
                if (isdown) {
                    mSeekBarVolumizer.changeVolumeBy(1);
                }
                return true;
            case android.view.KeyEvent.KEYCODE_VOLUME_MUTE :
                if (isdown) {
                    mSeekBarVolumizer.muteVolume();
                }
                return true;
            default :
                return false;
        }
    }

    @java.lang.Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);
        if ((!positiveResult) && (mSeekBarVolumizer != null)) {
            mSeekBarVolumizer.revertVolume();
        }
        cleanup();
    }

    public void onActivityStop() {
        if (mSeekBarVolumizer != null) {
            mSeekBarVolumizer.stopSample();
        }
    }

    /**
     * Do clean up.  This can be called multiple times!
     */
    private void cleanup() {
        getPreferenceManager().unregisterOnActivityStopListener(this);
        if (mSeekBarVolumizer != null) {
            final android.app.Dialog dialog = getDialog();
            if ((dialog != null) && dialog.isShowing()) {
                final android.view.View view = dialog.getWindow().getDecorView().findViewById(R.id.seekbar);
                if (view != null) {
                    view.setOnKeyListener(null);
                }
                // Stopped while dialog was showing, revert changes
                mSeekBarVolumizer.revertVolume();
            }
            mSeekBarVolumizer.stop();
            mSeekBarVolumizer = null;
        }
    }

    @java.lang.Override
    public void onSampleStarting(android.preference.SeekBarVolumizer volumizer) {
        if ((mSeekBarVolumizer != null) && (volumizer != mSeekBarVolumizer)) {
            mSeekBarVolumizer.stopSample();
        }
    }

    @java.lang.Override
    public void onProgressChanged(android.widget.SeekBar seekBar, int progress, boolean fromTouch) {
        // noop
    }

    @java.lang.Override
    public void onMuted(boolean muted, boolean zenMuted) {
        // noop
    }

    @java.lang.Override
    protected android.os.Parcelable onSaveInstanceState() {
        final android.os.Parcelable superState = super.onSaveInstanceState();
        if (isPersistent()) {
            // No need to save instance state since it's persistent
            return superState;
        }
        final android.preference.VolumePreference.SavedState myState = new android.preference.VolumePreference.SavedState(superState);
        if (mSeekBarVolumizer != null) {
            mSeekBarVolumizer.onSaveInstanceState(myState.getVolumeStore());
        }
        return myState;
    }

    @java.lang.Override
    protected void onRestoreInstanceState(android.os.Parcelable state) {
        if ((state == null) || (!state.getClass().equals(android.preference.VolumePreference.SavedState.class))) {
            // Didn't save state for us in onSaveInstanceState
            super.onRestoreInstanceState(state);
            return;
        }
        android.preference.VolumePreference.SavedState myState = ((android.preference.VolumePreference.SavedState) (state));
        super.onRestoreInstanceState(myState.getSuperState());
        if (mSeekBarVolumizer != null) {
            mSeekBarVolumizer.onRestoreInstanceState(myState.getVolumeStore());
        }
    }

    public static class VolumeStore {
        @android.annotation.UnsupportedAppUsage
        public int volume = -1;

        @android.annotation.UnsupportedAppUsage
        public int originalVolume = -1;
    }

    private static class SavedState extends android.preference.Preference.BaseSavedState {
        android.preference.VolumePreference.VolumeStore mVolumeStore = new android.preference.VolumePreference.VolumeStore();

        public SavedState(android.os.Parcel source) {
            super(source);
            mVolumeStore.volume = source.readInt();
            mVolumeStore.originalVolume = source.readInt();
        }

        @java.lang.Override
        public void writeToParcel(android.os.Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(mVolumeStore.volume);
            dest.writeInt(mVolumeStore.originalVolume);
        }

        android.preference.VolumePreference.VolumeStore getVolumeStore() {
            return mVolumeStore;
        }

        public SavedState(android.os.Parcelable superState) {
            super(superState);
        }

        @android.annotation.NonNull
        public static final android.os.Parcelable.Creator<android.preference.VolumePreference.SavedState> CREATOR = new android.os.Parcelable.Creator<android.preference.VolumePreference.SavedState>() {
            public android.preference.SavedState createFromParcel(android.os.Parcel in) {
                return new android.preference.SavedState(in);
            }

            public android.preference.SavedState[] newArray(int size) {
                return new android.preference.SavedState[size];
            }
        };
    }
}

