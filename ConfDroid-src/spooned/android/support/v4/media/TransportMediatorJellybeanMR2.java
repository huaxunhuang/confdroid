/**
 * Copyright (C) 2013 The Android Open Source Project
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
package android.support.v4.media;


class TransportMediatorJellybeanMR2 {
    final android.content.Context mContext;

    final android.media.AudioManager mAudioManager;

    final android.view.View mTargetView;

    final android.support.v4.media.TransportMediatorCallback mTransportCallback;

    final java.lang.String mReceiverAction;

    final android.content.IntentFilter mReceiverFilter;

    final android.content.Intent mIntent;

    final android.view.ViewTreeObserver.OnWindowAttachListener mWindowAttachListener = new android.view.ViewTreeObserver.OnWindowAttachListener() {
        @java.lang.Override
        public void onWindowAttached() {
            windowAttached();
        }

        @java.lang.Override
        public void onWindowDetached() {
            windowDetached();
        }
    };

    final android.view.ViewTreeObserver.OnWindowFocusChangeListener mWindowFocusListener = new android.view.ViewTreeObserver.OnWindowFocusChangeListener() {
        @java.lang.Override
        public void onWindowFocusChanged(boolean hasFocus) {
            if (hasFocus)
                gainFocus();
            else
                loseFocus();

        }
    };

    final android.content.BroadcastReceiver mMediaButtonReceiver = new android.content.BroadcastReceiver() {
        @java.lang.Override
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            try {
                android.view.KeyEvent event = ((android.view.KeyEvent) (intent.getParcelableExtra(android.content.Intent.EXTRA_KEY_EVENT)));
                mTransportCallback.handleKey(event);
            } catch (java.lang.ClassCastException e) {
                android.util.Log.w("TransportController", e);
            }
        }
    };

    android.media.AudioManager.OnAudioFocusChangeListener mAudioFocusChangeListener = new android.media.AudioManager.OnAudioFocusChangeListener() {
        @java.lang.Override
        public void onAudioFocusChange(int focusChange) {
            mTransportCallback.handleAudioFocusChange(focusChange);
        }
    };

    final android.media.RemoteControlClient.OnGetPlaybackPositionListener mGetPlaybackPositionListener = new android.media.RemoteControlClient.OnGetPlaybackPositionListener() {
        @java.lang.Override
        public long onGetPlaybackPosition() {
            return mTransportCallback.getPlaybackPosition();
        }
    };

    final android.media.RemoteControlClient.OnPlaybackPositionUpdateListener mPlaybackPositionUpdateListener = new android.media.RemoteControlClient.OnPlaybackPositionUpdateListener() {
        public void onPlaybackPositionUpdate(long newPositionMs) {
            mTransportCallback.playbackPositionUpdate(newPositionMs);
        }
    };

    android.app.PendingIntent mPendingIntent;

    android.media.RemoteControlClient mRemoteControl;

    boolean mFocused;

    int mPlayState = 0;

    boolean mAudioFocused;

    public TransportMediatorJellybeanMR2(android.content.Context context, android.media.AudioManager audioManager, android.view.View view, android.support.v4.media.TransportMediatorCallback transportCallback) {
        mContext = context;
        mAudioManager = audioManager;
        mTargetView = view;
        mTransportCallback = transportCallback;
        mReceiverAction = (context.getPackageName() + ":transport:") + java.lang.System.identityHashCode(this);
        mIntent = new android.content.Intent(mReceiverAction);
        mIntent.setPackage(context.getPackageName());
        mReceiverFilter = new android.content.IntentFilter();
        mReceiverFilter.addAction(mReceiverAction);
        mTargetView.getViewTreeObserver().addOnWindowAttachListener(mWindowAttachListener);
        mTargetView.getViewTreeObserver().addOnWindowFocusChangeListener(mWindowFocusListener);
    }

    public java.lang.Object getRemoteControlClient() {
        return mRemoteControl;
    }

    public void destroy() {
        windowDetached();
        mTargetView.getViewTreeObserver().removeOnWindowAttachListener(mWindowAttachListener);
        mTargetView.getViewTreeObserver().removeOnWindowFocusChangeListener(mWindowFocusListener);
    }

    void windowAttached() {
        mContext.registerReceiver(mMediaButtonReceiver, mReceiverFilter);
        mPendingIntent = android.app.PendingIntent.getBroadcast(mContext, 0, mIntent, android.app.PendingIntent.FLAG_CANCEL_CURRENT);
        mRemoteControl = new android.media.RemoteControlClient(mPendingIntent);
        mRemoteControl.setOnGetPlaybackPositionListener(mGetPlaybackPositionListener);
        mRemoteControl.setPlaybackPositionUpdateListener(mPlaybackPositionUpdateListener);
    }

    void gainFocus() {
        if (!mFocused) {
            mFocused = true;
            mAudioManager.registerMediaButtonEventReceiver(mPendingIntent);
            mAudioManager.registerRemoteControlClient(mRemoteControl);
            if (mPlayState == android.media.RemoteControlClient.PLAYSTATE_PLAYING) {
                takeAudioFocus();
            }
        }
    }

    void takeAudioFocus() {
        if (!mAudioFocused) {
            mAudioFocused = true;
            mAudioManager.requestAudioFocus(mAudioFocusChangeListener, android.media.AudioManager.STREAM_MUSIC, android.media.AudioManager.AUDIOFOCUS_GAIN);
        }
    }

    public void startPlaying() {
        if (mPlayState != android.media.RemoteControlClient.PLAYSTATE_PLAYING) {
            mPlayState = android.media.RemoteControlClient.PLAYSTATE_PLAYING;
            mRemoteControl.setPlaybackState(android.media.RemoteControlClient.PLAYSTATE_PLAYING);
        }
        if (mFocused) {
            takeAudioFocus();
        }
    }

    public void refreshState(boolean playing, long position, int transportControls) {
        if (mRemoteControl != null) {
            mRemoteControl.setPlaybackState(playing ? android.media.RemoteControlClient.PLAYSTATE_PLAYING : android.media.RemoteControlClient.PLAYSTATE_STOPPED, position, playing ? 1 : 0);
            mRemoteControl.setTransportControlFlags(transportControls);
        }
    }

    public void pausePlaying() {
        if (mPlayState == android.media.RemoteControlClient.PLAYSTATE_PLAYING) {
            mPlayState = android.media.RemoteControlClient.PLAYSTATE_PAUSED;
            mRemoteControl.setPlaybackState(android.media.RemoteControlClient.PLAYSTATE_PAUSED);
        }
        dropAudioFocus();
    }

    public void stopPlaying() {
        if (mPlayState != android.media.RemoteControlClient.PLAYSTATE_STOPPED) {
            mPlayState = android.media.RemoteControlClient.PLAYSTATE_STOPPED;
            mRemoteControl.setPlaybackState(android.media.RemoteControlClient.PLAYSTATE_STOPPED);
        }
        dropAudioFocus();
    }

    void dropAudioFocus() {
        if (mAudioFocused) {
            mAudioFocused = false;
            mAudioManager.abandonAudioFocus(mAudioFocusChangeListener);
        }
    }

    void loseFocus() {
        dropAudioFocus();
        if (mFocused) {
            mFocused = false;
            mAudioManager.unregisterRemoteControlClient(mRemoteControl);
            mAudioManager.unregisterMediaButtonEventReceiver(mPendingIntent);
        }
    }

    void windowDetached() {
        loseFocus();
        if (mPendingIntent != null) {
            mContext.unregisterReceiver(mMediaButtonReceiver);
            mPendingIntent.cancel();
            mPendingIntent = null;
            mRemoteControl = null;
        }
    }
}

