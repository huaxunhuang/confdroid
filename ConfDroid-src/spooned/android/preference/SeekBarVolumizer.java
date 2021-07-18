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
package android.preference;


/**
 * Turns a {@link SeekBar} into a volume control.
 *
 * @unknown 
 * @deprecated Use the <a href="{@docRoot }jetpack/androidx.html">AndroidX</a>
<a href="{@docRoot }reference/androidx/preference/package-summary.html">
Preference Library</a> for consistent behavior across all devices. For more information on
using the AndroidX Preference Library see
<a href="{@docRoot }guide/topics/ui/settings.html">Settings</a>.
 */
@java.lang.Deprecated
public class SeekBarVolumizer implements android.os.Handler.Callback , android.widget.SeekBar.OnSeekBarChangeListener {
    private static final java.lang.String TAG = "SeekBarVolumizer";

    public interface Callback {
        void onSampleStarting(android.preference.SeekBarVolumizer sbv);

        void onProgressChanged(android.widget.SeekBar seekBar, int progress, boolean fromTouch);

        void onMuted(boolean muted, boolean zenMuted);
    }

    private static final int MSG_GROUP_VOLUME_CHANGED = 1;

    private final android.os.Handler mVolumeHandler = new android.preference.SeekBarVolumizer.VolumeHandler();

    private android.media.AudioAttributes mAttributes;

    private int mVolumeGroupId;

    private final AudioManager.VolumeGroupCallback mVolumeGroupCallback = new android.media.AudioManager.VolumeGroupCallback() {
        @java.lang.Override
        public void onAudioVolumeGroupChanged(int group, int flags) {
            if (mHandler == null) {
                return;
            }
            com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
            args.arg1 = group;
            args.arg2 = flags;
            mVolumeHandler.sendMessage(mHandler.obtainMessage(android.preference.SeekBarVolumizer.MSG_GROUP_VOLUME_CHANGED, args));
        }
    };

    @android.annotation.UnsupportedAppUsage
    private final android.content.Context mContext;

    private final android.preference.SeekBarVolumizer.H mUiHandler = new android.preference.SeekBarVolumizer.H();

    private final android.preference.SeekBarVolumizer.Callback mCallback;

    private final android.net.Uri mDefaultUri;

    @android.annotation.UnsupportedAppUsage
    private final android.media.AudioManager mAudioManager;

    private final android.app.NotificationManager mNotificationManager;

    @android.annotation.UnsupportedAppUsage
    private final int mStreamType;

    private final int mMaxStreamVolume;

    private boolean mAffectedByRingerMode;

    private boolean mNotificationOrRing;

    private final android.preference.SeekBarVolumizer.Receiver mReceiver = new android.preference.SeekBarVolumizer.Receiver();

    private android.os.Handler mHandler;

    private android.preference.SeekBarVolumizer.Observer mVolumeObserver;

    @android.annotation.UnsupportedAppUsage
    private int mOriginalStreamVolume;

    private int mLastAudibleStreamVolume;

    // When the old handler is destroyed and a new one is created, there could be a situation where
    // this is accessed at the same time in different handlers. So, access to this field needs to be
    // synchronized.
    @com.android.internal.annotations.GuardedBy("this")
    @android.annotation.UnsupportedAppUsage
    private android.media.Ringtone mRingtone;

    @android.annotation.UnsupportedAppUsage
    private int mLastProgress = -1;

    private boolean mMuted;

    @android.annotation.UnsupportedAppUsage
    private android.widget.SeekBar mSeekBar;

    private int mVolumeBeforeMute = -1;

    private int mRingerMode;

    private int mZenMode;

    private boolean mPlaySample;

    private static final int MSG_SET_STREAM_VOLUME = 0;

    private static final int MSG_START_SAMPLE = 1;

    private static final int MSG_STOP_SAMPLE = 2;

    private static final int MSG_INIT_SAMPLE = 3;

    private static final int CHECK_RINGTONE_PLAYBACK_DELAY_MS = 1000;

    private NotificationManager.Policy mNotificationPolicy;

    private boolean mAllowAlarms;

    private boolean mAllowMedia;

    private boolean mAllowRinger;

    @android.annotation.UnsupportedAppUsage
    public SeekBarVolumizer(android.content.Context context, int streamType, android.net.Uri defaultUri, android.preference.SeekBarVolumizer.Callback callback) {
        /* playSample */
        this(context, streamType, defaultUri, callback, true);
    }

    public SeekBarVolumizer(android.content.Context context, int streamType, android.net.Uri defaultUri, android.preference.SeekBarVolumizer.Callback callback, boolean playSample) {
        mContext = context;
        mAudioManager = context.getSystemService(android.media.AudioManager.class);
        mNotificationManager = context.getSystemService(android.app.NotificationManager.class);
        mNotificationPolicy = mNotificationManager.getConsolidatedNotificationPolicy();
        mAllowAlarms = (mNotificationPolicy.priorityCategories & NotificationManager.Policy.PRIORITY_CATEGORY_ALARMS) != 0;
        mAllowMedia = (mNotificationPolicy.priorityCategories & NotificationManager.Policy.PRIORITY_CATEGORY_MEDIA) != 0;
        mAllowRinger = !android.service.notification.ZenModeConfig.areAllPriorityOnlyNotificationZenSoundsMuted(mNotificationPolicy);
        mStreamType = streamType;
        mAffectedByRingerMode = mAudioManager.isStreamAffectedByRingerMode(mStreamType);
        mNotificationOrRing = android.preference.SeekBarVolumizer.isNotificationOrRing(mStreamType);
        if (mNotificationOrRing) {
            mRingerMode = mAudioManager.getRingerModeInternal();
        }
        mZenMode = mNotificationManager.getZenMode();
        if (hasAudioProductStrategies()) {
            mVolumeGroupId = getVolumeGroupIdForLegacyStreamType(mStreamType);
            mAttributes = getAudioAttributesForLegacyStreamType(mStreamType);
        }
        mMaxStreamVolume = mAudioManager.getStreamMaxVolume(mStreamType);
        mCallback = callback;
        mOriginalStreamVolume = mAudioManager.getStreamVolume(mStreamType);
        mLastAudibleStreamVolume = mAudioManager.getLastAudibleStreamVolume(mStreamType);
        mMuted = mAudioManager.isStreamMute(mStreamType);
        mPlaySample = playSample;
        if (mCallback != null) {
            mCallback.onMuted(mMuted, isZenMuted());
        }
        if (defaultUri == null) {
            if (mStreamType == android.media.AudioManager.STREAM_RING) {
                defaultUri = android.provider.Settings.System.DEFAULT_RINGTONE_URI;
            } else
                if (mStreamType == android.media.AudioManager.STREAM_NOTIFICATION) {
                    defaultUri = android.provider.Settings.System.DEFAULT_NOTIFICATION_URI;
                } else {
                    defaultUri = android.provider.Settings.System.DEFAULT_ALARM_ALERT_URI;
                }

        }
        mDefaultUri = defaultUri;
    }

    private boolean hasAudioProductStrategies() {
        return android.media.AudioManager.getAudioProductStrategies().size() > 0;
    }

    private int getVolumeGroupIdForLegacyStreamType(int streamType) {
        for (final android.media.audiopolicy.AudioProductStrategy productStrategy : android.media.AudioManager.getAudioProductStrategies()) {
            int volumeGroupId = productStrategy.getVolumeGroupIdForLegacyStreamType(streamType);
            if (volumeGroupId != android.media.audiopolicy.AudioVolumeGroup.DEFAULT_VOLUME_GROUP) {
                return volumeGroupId;
            }
        }
        return android.media.AudioManager.getAudioProductStrategies().stream().map(( strategy) -> strategy.getVolumeGroupIdForAudioAttributes(AudioProductStrategy.sDefaultAttributes)).filter(( volumeGroupId) -> volumeGroupId != AudioVolumeGroup.DEFAULT_VOLUME_GROUP).findFirst().orElse(AudioVolumeGroup.DEFAULT_VOLUME_GROUP);
    }

    @android.annotation.NonNull
    private android.media.AudioAttributes getAudioAttributesForLegacyStreamType(int streamType) {
        for (final android.media.audiopolicy.AudioProductStrategy productStrategy : android.media.AudioManager.getAudioProductStrategies()) {
            android.media.AudioAttributes aa = productStrategy.getAudioAttributesForLegacyStreamType(streamType);
            if (aa != null) {
                return aa;
            }
        }
        return new android.media.AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_UNKNOWN).setUsage(AudioAttributes.USAGE_UNKNOWN).build();
    }

    private static boolean isNotificationOrRing(int stream) {
        return (stream == android.media.AudioManager.STREAM_RING) || (stream == android.media.AudioManager.STREAM_NOTIFICATION);
    }

    private static boolean isAlarmsStream(int stream) {
        return stream == android.media.AudioManager.STREAM_ALARM;
    }

    private static boolean isMediaStream(int stream) {
        return stream == android.media.AudioManager.STREAM_MUSIC;
    }

    public void setSeekBar(android.widget.SeekBar seekBar) {
        if (mSeekBar != null) {
            mSeekBar.setOnSeekBarChangeListener(null);
        }
        mSeekBar = seekBar;
        mSeekBar.setOnSeekBarChangeListener(null);
        mSeekBar.setMax(mMaxStreamVolume);
        updateSeekBar();
        mSeekBar.setOnSeekBarChangeListener(this);
    }

    private boolean isZenMuted() {
        return ((mNotificationOrRing && (mZenMode == android.provider.Settings.Global.ZEN_MODE_ALARMS)) || (mZenMode == android.provider.Settings.Global.ZEN_MODE_NO_INTERRUPTIONS)) || ((mZenMode == android.provider.Settings.Global.ZEN_MODE_IMPORTANT_INTERRUPTIONS) && ((((!mAllowAlarms) && android.preference.SeekBarVolumizer.isAlarmsStream(mStreamType)) || ((!mAllowMedia) && android.preference.SeekBarVolumizer.isMediaStream(mStreamType))) || ((!mAllowRinger) && android.preference.SeekBarVolumizer.isNotificationOrRing(mStreamType))));
    }

    protected void updateSeekBar() {
        final boolean zenMuted = isZenMuted();
        mSeekBar.setEnabled(!zenMuted);
        if (zenMuted) {
            mSeekBar.setProgress(mLastAudibleStreamVolume, true);
        } else
            if (mNotificationOrRing && (mRingerMode == android.media.AudioManager.RINGER_MODE_VIBRATE)) {
                mSeekBar.setProgress(0, true);
            } else
                if (mMuted) {
                    mSeekBar.setProgress(0, true);
                } else {
                    mSeekBar.setProgress(mLastProgress > (-1) ? mLastProgress : mOriginalStreamVolume, true);
                }


    }

    @java.lang.Override
    public boolean handleMessage(android.os.Message msg) {
        switch (msg.what) {
            case android.preference.SeekBarVolumizer.MSG_SET_STREAM_VOLUME :
                if (mMuted && (mLastProgress > 0)) {
                    mAudioManager.adjustStreamVolume(mStreamType, AudioManager.ADJUST_UNMUTE, 0);
                } else
                    if ((!mMuted) && (mLastProgress == 0)) {
                        mAudioManager.adjustStreamVolume(mStreamType, AudioManager.ADJUST_MUTE, 0);
                    }

                mAudioManager.setStreamVolume(mStreamType, mLastProgress, AudioManager.FLAG_SHOW_UI_WARNINGS);
                break;
            case android.preference.SeekBarVolumizer.MSG_START_SAMPLE :
                if (mPlaySample) {
                    onStartSample();
                }
                break;
            case android.preference.SeekBarVolumizer.MSG_STOP_SAMPLE :
                if (mPlaySample) {
                    onStopSample();
                }
                break;
            case android.preference.SeekBarVolumizer.MSG_INIT_SAMPLE :
                if (mPlaySample) {
                    onInitSample();
                }
                break;
            default :
                android.util.Log.e(android.preference.SeekBarVolumizer.TAG, "invalid SeekBarVolumizer message: " + msg.what);
        }
        return true;
    }

    private void onInitSample() {
        synchronized(this) {
            mRingtone = android.media.RingtoneManager.getRingtone(mContext, mDefaultUri);
            if (mRingtone != null) {
                mRingtone.setStreamType(mStreamType);
            }
        }
    }

    private void postStartSample() {
        if (mHandler == null)
            return;

        mHandler.removeMessages(android.preference.SeekBarVolumizer.MSG_START_SAMPLE);
        mHandler.sendMessageDelayed(mHandler.obtainMessage(android.preference.SeekBarVolumizer.MSG_START_SAMPLE), isSamplePlaying() ? android.preference.SeekBarVolumizer.CHECK_RINGTONE_PLAYBACK_DELAY_MS : 0);
    }

    private void onStartSample() {
        if (!isSamplePlaying()) {
            if (mCallback != null) {
                mCallback.onSampleStarting(this);
            }
            synchronized(this) {
                if (mRingtone != null) {
                    try {
                        mRingtone.setAudioAttributes(new android.media.AudioAttributes.Builder(mRingtone.getAudioAttributes()).setFlags(AudioAttributes.FLAG_BYPASS_MUTE).build());
                        mRingtone.play();
                    } catch (java.lang.Throwable e) {
                        android.util.Log.w(android.preference.SeekBarVolumizer.TAG, "Error playing ringtone, stream " + mStreamType, e);
                    }
                }
            }
        }
    }

    private void postStopSample() {
        if (mHandler == null)
            return;

        // remove pending delayed start messages
        mHandler.removeMessages(android.preference.SeekBarVolumizer.MSG_START_SAMPLE);
        mHandler.removeMessages(android.preference.SeekBarVolumizer.MSG_STOP_SAMPLE);
        mHandler.sendMessage(mHandler.obtainMessage(android.preference.SeekBarVolumizer.MSG_STOP_SAMPLE));
    }

    private void onStopSample() {
        synchronized(this) {
            if (mRingtone != null) {
                mRingtone.stop();
            }
        }
    }

    @android.annotation.UnsupportedAppUsage
    public void stop() {
        if (mHandler == null)
            return;
        // already stopped

        postStopSample();
        mContext.getContentResolver().unregisterContentObserver(mVolumeObserver);
        mReceiver.setListening(false);
        if (hasAudioProductStrategies()) {
            unregisterVolumeGroupCb();
        }
        mSeekBar.setOnSeekBarChangeListener(null);
        mHandler.getLooper().quitSafely();
        mHandler = null;
        mVolumeObserver = null;
    }

    public void start() {
        if (mHandler != null)
            return;
        // already started

        android.os.HandlerThread thread = new android.os.HandlerThread(android.preference.SeekBarVolumizer.TAG + ".CallbackHandler");
        thread.start();
        mHandler = new android.os.Handler(thread.getLooper(), this);
        mHandler.sendEmptyMessage(android.preference.SeekBarVolumizer.MSG_INIT_SAMPLE);
        mVolumeObserver = new android.preference.SeekBarVolumizer.Observer(mHandler);
        mContext.getContentResolver().registerContentObserver(java.lang.System.getUriFor(VOLUME_SETTINGS_INT[mStreamType]), false, mVolumeObserver);
        mReceiver.setListening(true);
        if (hasAudioProductStrategies()) {
            registerVolumeGroupCb();
        }
    }

    public void revertVolume() {
        mAudioManager.setStreamVolume(mStreamType, mOriginalStreamVolume, 0);
    }

    public void onProgressChanged(android.widget.SeekBar seekBar, int progress, boolean fromTouch) {
        if (fromTouch) {
            postSetVolume(progress);
        }
        if (mCallback != null) {
            mCallback.onProgressChanged(seekBar, progress, fromTouch);
        }
    }

    private void postSetVolume(int progress) {
        if (mHandler == null)
            return;

        // Do the volume changing separately to give responsive UI
        mLastProgress = progress;
        mHandler.removeMessages(android.preference.SeekBarVolumizer.MSG_SET_STREAM_VOLUME);
        mHandler.sendMessage(mHandler.obtainMessage(android.preference.SeekBarVolumizer.MSG_SET_STREAM_VOLUME));
    }

    public void onStartTrackingTouch(android.widget.SeekBar seekBar) {
    }

    public void onStopTrackingTouch(android.widget.SeekBar seekBar) {
        postStartSample();
    }

    public boolean isSamplePlaying() {
        synchronized(this) {
            return (mRingtone != null) && mRingtone.isPlaying();
        }
    }

    public void startSample() {
        postStartSample();
    }

    public void stopSample() {
        postStopSample();
    }

    public android.widget.SeekBar getSeekBar() {
        return mSeekBar;
    }

    public void changeVolumeBy(int amount) {
        mSeekBar.incrementProgressBy(amount);
        postSetVolume(mSeekBar.getProgress());
        postStartSample();
        mVolumeBeforeMute = -1;
    }

    public void muteVolume() {
        if (mVolumeBeforeMute != (-1)) {
            mSeekBar.setProgress(mVolumeBeforeMute, true);
            postSetVolume(mVolumeBeforeMute);
            postStartSample();
            mVolumeBeforeMute = -1;
        } else {
            mVolumeBeforeMute = mSeekBar.getProgress();
            mSeekBar.setProgress(0, true);
            postStopSample();
            postSetVolume(0);
        }
    }

    public void onSaveInstanceState(android.preference.VolumePreference.VolumeStore volumeStore) {
        if (mLastProgress >= 0) {
            volumeStore.volume = mLastProgress;
            volumeStore.originalVolume = mOriginalStreamVolume;
        }
    }

    public void onRestoreInstanceState(android.preference.VolumePreference.VolumeStore volumeStore) {
        if (volumeStore.volume != (-1)) {
            mOriginalStreamVolume = volumeStore.originalVolume;
            mLastProgress = volumeStore.volume;
            postSetVolume(mLastProgress);
        }
    }

    private final class H extends android.os.Handler {
        private static final int UPDATE_SLIDER = 1;

        @java.lang.Override
        public void handleMessage(android.os.Message msg) {
            if (msg.what == android.preference.SeekBarVolumizer.H.UPDATE_SLIDER) {
                if (mSeekBar != null) {
                    mLastProgress = msg.arg1;
                    mLastAudibleStreamVolume = msg.arg2;
                    final boolean muted = ((java.lang.Boolean) (msg.obj)).booleanValue();
                    if (muted != mMuted) {
                        mMuted = muted;
                        if (mCallback != null) {
                            mCallback.onMuted(mMuted, isZenMuted());
                        }
                    }
                    updateSeekBar();
                }
            }
        }

        public void postUpdateSlider(int volume, int lastAudibleVolume, boolean mute) {
            obtainMessage(android.preference.SeekBarVolumizer.H.UPDATE_SLIDER, volume, lastAudibleVolume, new java.lang.Boolean(mute)).sendToTarget();
        }
    }

    private void updateSlider() {
        if ((mSeekBar != null) && (mAudioManager != null)) {
            final int volume = mAudioManager.getStreamVolume(mStreamType);
            final int lastAudibleVolume = mAudioManager.getLastAudibleStreamVolume(mStreamType);
            final boolean mute = mAudioManager.isStreamMute(mStreamType);
            mUiHandler.postUpdateSlider(volume, lastAudibleVolume, mute);
        }
    }

    private final class Observer extends android.database.ContentObserver {
        public Observer(android.os.Handler handler) {
            super(handler);
        }

        @java.lang.Override
        public void onChange(boolean selfChange) {
            onChange(selfChange);
            updateSlider();
        }
    }

    private final class Receiver extends android.content.BroadcastReceiver {
        private boolean mListening;

        public void setListening(boolean listening) {
            if (mListening == listening)
                return;

            mListening = listening;
            if (listening) {
                final android.content.IntentFilter filter = new android.content.IntentFilter(android.media.AudioManager.VOLUME_CHANGED_ACTION);
                filter.addAction(AudioManager.INTERNAL_RINGER_MODE_CHANGED_ACTION);
                filter.addAction(NotificationManager.ACTION_INTERRUPTION_FILTER_CHANGED);
                filter.addAction(NotificationManager.ACTION_NOTIFICATION_POLICY_CHANGED);
                filter.addAction(AudioManager.STREAM_DEVICES_CHANGED_ACTION);
                mContext.registerReceiver(this, filter);
            } else {
                mContext.unregisterReceiver(this);
            }
        }

        @java.lang.Override
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            final java.lang.String action = intent.getAction();
            if (AudioManager.VOLUME_CHANGED_ACTION.equals(action)) {
                int streamType = intent.getIntExtra(AudioManager.EXTRA_VOLUME_STREAM_TYPE, -1);
                int streamValue = intent.getIntExtra(AudioManager.EXTRA_VOLUME_STREAM_VALUE, -1);
                if (hasAudioProductStrategies()) {
                    updateVolumeSlider(streamType, streamValue);
                }
            } else
                if (AudioManager.INTERNAL_RINGER_MODE_CHANGED_ACTION.equals(action)) {
                    if (mNotificationOrRing) {
                        mRingerMode = mAudioManager.getRingerModeInternal();
                    }
                    if (mAffectedByRingerMode) {
                        updateSlider();
                    }
                } else
                    if (AudioManager.STREAM_DEVICES_CHANGED_ACTION.equals(action)) {
                        int streamType = intent.getIntExtra(AudioManager.EXTRA_VOLUME_STREAM_TYPE, -1);
                        if (hasAudioProductStrategies()) {
                            int streamVolume = mAudioManager.getStreamVolume(streamType);
                            updateVolumeSlider(streamType, streamVolume);
                        } else {
                            int volumeGroup = getVolumeGroupIdForLegacyStreamType(streamType);
                            if ((volumeGroup != android.media.audiopolicy.AudioVolumeGroup.DEFAULT_VOLUME_GROUP) && (volumeGroup == mVolumeGroupId)) {
                                int streamVolume = mAudioManager.getStreamVolume(streamType);
                                updateVolumeSlider(streamType, streamVolume);
                            }
                        }
                    } else
                        if (NotificationManager.ACTION_INTERRUPTION_FILTER_CHANGED.equals(action)) {
                            mZenMode = mNotificationManager.getZenMode();
                            updateSlider();
                        } else
                            if (NotificationManager.ACTION_NOTIFICATION_POLICY_CHANGED.equals(action)) {
                                mNotificationPolicy = mNotificationManager.getConsolidatedNotificationPolicy();
                                mAllowAlarms = (mNotificationPolicy.priorityCategories & NotificationManager.Policy.PRIORITY_CATEGORY_ALARMS) != 0;
                                mAllowMedia = (mNotificationPolicy.priorityCategories & NotificationManager.Policy.PRIORITY_CATEGORY_MEDIA) != 0;
                                mAllowRinger = !android.service.notification.ZenModeConfig.areAllPriorityOnlyNotificationZenSoundsMuted(mNotificationPolicy);
                                updateSlider();
                            }




        }

        private void updateVolumeSlider(int streamType, int streamValue) {
            final boolean streamMatch = (mNotificationOrRing) ? android.preference.SeekBarVolumizer.isNotificationOrRing(streamType) : streamType == mStreamType;
            if (((mSeekBar != null) && streamMatch) && (streamValue != (-1))) {
                final boolean muted = mAudioManager.isStreamMute(mStreamType) || (streamValue == 0);
                mUiHandler.postUpdateSlider(streamValue, mLastAudibleStreamVolume, muted);
            }
        }
    }

    private void registerVolumeGroupCb() {
        if (mVolumeGroupId != android.media.audiopolicy.AudioVolumeGroup.DEFAULT_VOLUME_GROUP) {
            mAudioManager.registerVolumeGroupCallback(java.lang.Runnable::run, mVolumeGroupCallback);
            updateSlider();
        }
    }

    private void unregisterVolumeGroupCb() {
        if (mVolumeGroupId != android.media.audiopolicy.AudioVolumeGroup.DEFAULT_VOLUME_GROUP) {
            mAudioManager.unregisterVolumeGroupCallback(mVolumeGroupCallback);
        }
    }

    private class VolumeHandler extends android.os.Handler {
        @java.lang.Override
        public void handleMessage(android.os.Message msg) {
            com.android.internal.os.SomeArgs args = ((com.android.internal.os.SomeArgs) (msg.obj));
            switch (msg.what) {
                case android.preference.SeekBarVolumizer.MSG_GROUP_VOLUME_CHANGED :
                    int group = ((int) (args.arg1));
                    if ((mVolumeGroupId != group) || (mVolumeGroupId == android.media.audiopolicy.AudioVolumeGroup.DEFAULT_VOLUME_GROUP)) {
                        return;
                    }
                    updateSlider();
                    break;
            }
        }
    }
}

