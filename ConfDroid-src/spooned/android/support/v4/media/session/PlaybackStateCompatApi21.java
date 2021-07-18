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
package android.support.v4.media.session;


class PlaybackStateCompatApi21 {
    public static int getState(java.lang.Object stateObj) {
        return ((android.media.session.PlaybackState) (stateObj)).getState();
    }

    public static long getPosition(java.lang.Object stateObj) {
        return ((android.media.session.PlaybackState) (stateObj)).getPosition();
    }

    public static long getBufferedPosition(java.lang.Object stateObj) {
        return ((android.media.session.PlaybackState) (stateObj)).getBufferedPosition();
    }

    public static float getPlaybackSpeed(java.lang.Object stateObj) {
        return ((android.media.session.PlaybackState) (stateObj)).getPlaybackSpeed();
    }

    public static long getActions(java.lang.Object stateObj) {
        return ((android.media.session.PlaybackState) (stateObj)).getActions();
    }

    public static java.lang.CharSequence getErrorMessage(java.lang.Object stateObj) {
        return ((android.media.session.PlaybackState) (stateObj)).getErrorMessage();
    }

    public static long getLastPositionUpdateTime(java.lang.Object stateObj) {
        return ((android.media.session.PlaybackState) (stateObj)).getLastPositionUpdateTime();
    }

    public static java.util.List<java.lang.Object> getCustomActions(java.lang.Object stateObj) {
        return ((java.util.List) (((android.media.session.PlaybackState) (stateObj)).getCustomActions()));
    }

    public static long getActiveQueueItemId(java.lang.Object stateObj) {
        return ((android.media.session.PlaybackState) (stateObj)).getActiveQueueItemId();
    }

    public static java.lang.Object newInstance(int state, long position, long bufferedPosition, float speed, long actions, java.lang.CharSequence errorMessage, long updateTime, java.util.List<java.lang.Object> customActions, long activeItemId) {
        android.media.session.PlaybackState.Builder stateObj = new android.media.session.PlaybackState.Builder();
        stateObj.setState(state, position, speed, updateTime);
        stateObj.setBufferedPosition(bufferedPosition);
        stateObj.setActions(actions);
        stateObj.setErrorMessage(errorMessage);
        for (java.lang.Object customAction : customActions) {
            stateObj.addCustomAction(((android.media.session.PlaybackState.CustomAction) (customAction)));
        }
        stateObj.setActiveQueueItemId(activeItemId);
        return stateObj.build();
    }

    static final class CustomAction {
        public static java.lang.String getAction(java.lang.Object customActionObj) {
            return ((android.media.session.PlaybackState.CustomAction) (customActionObj)).getAction();
        }

        public static java.lang.CharSequence getName(java.lang.Object customActionObj) {
            return ((android.media.session.PlaybackState.CustomAction) (customActionObj)).getName();
        }

        public static int getIcon(java.lang.Object customActionObj) {
            return ((android.media.session.PlaybackState.CustomAction) (customActionObj)).getIcon();
        }

        public static android.os.Bundle getExtras(java.lang.Object customActionObj) {
            return ((android.media.session.PlaybackState.CustomAction) (customActionObj)).getExtras();
        }

        public static java.lang.Object newInstance(java.lang.String action, java.lang.CharSequence name, int icon, android.os.Bundle extras) {
            android.media.session.PlaybackState.CustomAction.Builder customActionObj = new android.media.session.PlaybackState.CustomAction.Builder(action, name, icon);
            customActionObj.setExtras(extras);
            return customActionObj.build();
        }
    }
}

