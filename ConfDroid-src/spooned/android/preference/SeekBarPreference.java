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
public class SeekBarPreference extends android.preference.Preference implements android.widget.SeekBar.OnSeekBarChangeListener {
    private int mProgress;

    private int mMax;

    private boolean mTrackingTouch;

    public SeekBarPreference(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        android.content.res.TypedArray a = context.obtainStyledAttributes(attrs, com.android.internal.R.styleable.ProgressBar, defStyleAttr, defStyleRes);
        setMax(a.getInt(com.android.internal.R.styleable.ProgressBar_max, mMax));
        a.recycle();
        a = context.obtainStyledAttributes(attrs, com.android.internal.R.styleable.SeekBarPreference, defStyleAttr, defStyleRes);
        final int layoutResId = a.getResourceId(com.android.internal.R.styleable.SeekBarPreference_layout, com.android.internal.R.layout.preference_widget_seekbar);
        a.recycle();
        setLayoutResource(layoutResId);
    }

    @android.annotation.UnsupportedAppUsage
    public SeekBarPreference(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    @android.annotation.UnsupportedAppUsage
    public SeekBarPreference(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, com.android.internal.R.attr.seekBarPreferenceStyle);
    }

    @android.annotation.UnsupportedAppUsage
    public SeekBarPreference(android.content.Context context) {
        this(context, null);
    }

    @java.lang.Override
    protected void onBindView(android.view.View view) {
        super.onBindView(view);
        android.widget.SeekBar seekBar = ((android.widget.SeekBar) (view.findViewById(com.android.internal.R.id.seekbar)));
        seekBar.setOnSeekBarChangeListener(this);
        seekBar.setMax(mMax);
        seekBar.setProgress(mProgress);
        seekBar.setEnabled(isEnabled());
    }

    @java.lang.Override
    protected void onSetInitialValue(boolean restoreValue, java.lang.Object defaultValue) {
        setProgress(restoreValue ? getPersistedInt(mProgress) : ((java.lang.Integer) (defaultValue)));
    }

    @java.lang.Override
    protected java.lang.Object onGetDefaultValue(android.content.res.TypedArray a, int index) {
        return a.getInt(index, 0);
    }

    @java.lang.Override
    public boolean onKey(android.view.View v, int keyCode, android.view.KeyEvent event) {
        if (event.getAction() != android.view.KeyEvent.ACTION_DOWN) {
            return false;
        }
        android.widget.SeekBar seekBar = ((android.widget.SeekBar) (v.findViewById(com.android.internal.R.id.seekbar)));
        if (seekBar == null) {
            return false;
        }
        return seekBar.onKeyDown(keyCode, event);
    }

    public void setMax(int max) {
        if (max != mMax) {
            mMax = max;
            notifyChanged();
        }
    }

    public void setProgress(int progress) {
        setProgress(progress, true);
    }

    private void setProgress(int progress, boolean notifyChanged) {
        if (progress > mMax) {
            progress = mMax;
        }
        if (progress < 0) {
            progress = 0;
        }
        if (progress != mProgress) {
            mProgress = progress;
            persistInt(progress);
            if (notifyChanged) {
                notifyChanged();
            }
        }
    }

    public int getProgress() {
        return mProgress;
    }

    /**
     * Persist the seekBar's progress value if callChangeListener
     * returns true, otherwise set the seekBar's progress to the stored value
     */
    void syncProgress(android.widget.SeekBar seekBar) {
        int progress = seekBar.getProgress();
        if (progress != mProgress) {
            if (callChangeListener(progress)) {
                setProgress(progress, false);
            } else {
                seekBar.setProgress(mProgress);
            }
        }
    }

    @java.lang.Override
    public void onProgressChanged(android.widget.SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser && (!mTrackingTouch)) {
            syncProgress(seekBar);
        }
    }

    @java.lang.Override
    public void onStartTrackingTouch(android.widget.SeekBar seekBar) {
        mTrackingTouch = true;
    }

    @java.lang.Override
    public void onStopTrackingTouch(android.widget.SeekBar seekBar) {
        mTrackingTouch = false;
        if (seekBar.getProgress() != mProgress) {
            syncProgress(seekBar);
        }
    }

    @java.lang.Override
    protected android.os.Parcelable onSaveInstanceState() {
        /* Suppose a client uses this preference type without persisting. We
        must save the instance state so it is able to, for example, survive
        orientation changes.
         */
        final android.os.Parcelable superState = super.onSaveInstanceState();
        if (isPersistent()) {
            // No need to save instance state since it's persistent
            return superState;
        }
        // Save the instance state
        final android.preference.SeekBarPreference.SavedState myState = new android.preference.SeekBarPreference.SavedState(superState);
        myState.progress = mProgress;
        myState.max = mMax;
        return myState;
    }

    @java.lang.Override
    protected void onRestoreInstanceState(android.os.Parcelable state) {
        if (!state.getClass().equals(android.preference.SeekBarPreference.SavedState.class)) {
            // Didn't save state for us in onSaveInstanceState
            super.onRestoreInstanceState(state);
            return;
        }
        // Restore the instance state
        android.preference.SeekBarPreference.SavedState myState = ((android.preference.SeekBarPreference.SavedState) (state));
        super.onRestoreInstanceState(myState.getSuperState());
        mProgress = myState.progress;
        mMax = myState.max;
        notifyChanged();
    }

    /**
     * SavedState, a subclass of {@link BaseSavedState}, will store the state
     * of MyPreference, a subclass of Preference.
     * <p>
     * It is important to always call through to super methods.
     */
    private static class SavedState extends android.preference.Preference.BaseSavedState {
        int progress;

        int max;

        public SavedState(android.os.Parcel source) {
            super(source);
            // Restore the click counter
            progress = source.readInt();
            max = source.readInt();
        }

        @java.lang.Override
        public void writeToParcel(android.os.Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            // Save the click counter
            dest.writeInt(progress);
            dest.writeInt(max);
        }

        public SavedState(android.os.Parcelable superState) {
            super(superState);
        }

        @java.lang.SuppressWarnings("unused")
        @android.annotation.NonNull
        public static final android.os.Parcelable.Creator<android.preference.SeekBarPreference.SavedState> CREATOR = new android.os.Parcelable.Creator<android.preference.SeekBarPreference.SavedState>() {
            public android.preference.SavedState createFromParcel(android.os.Parcel in) {
                return new android.preference.SavedState(in);
            }

            public android.preference.SavedState[] newArray(int size) {
                return new android.preference.SavedState[size];
            }
        };
    }
}

