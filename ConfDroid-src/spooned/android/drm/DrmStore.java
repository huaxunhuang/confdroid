/**
 * Copyright (C) 2010 The Android Open Source Project
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
package android.drm;


/**
 * Defines constants that are used by the DRM framework.
 */
public class DrmStore {
    /**
     * Interface definition for the columns that represent DRM constraints.
     * {@link android.drm.DrmManagerClient#getConstraints DrmManagerClient.getConstraints()}
     * can be called by an application to find out the contraints on the
     * {@link android.drm.DrmStore.Action actions} that can be performed
     * on right-protected content. The constants defined in this interface
     * represent three most common types of constraints: count-based,
     * date-based, and duration-based. Two or more constraints can be used
     * at the same time to represent more sophisticated constraints.
     * In addition, user-defined constraint,
     * {@link #EXTENDED_METADATA extended metadata}, can be
     * used if these three types of constraints are not sufficient.
     */
    public interface ConstraintsColumns {
        /**
         * This is a count-based constraint. It represents the maximum
         * repeat count that can be performed on an
         * {@link android.drm.DrmStore.Action action}.
         * <p>
         * Type: INTEGER
         */
        public static final java.lang.String MAX_REPEAT_COUNT = "max_repeat_count";

        /**
         * This is a count-based constraint. It represents the remaining
         * repeat count that can be performed on an
         * {@link android.drm.DrmStore.Action action}.
         * <p>
         * Type: INTEGER
         */
        public static final java.lang.String REMAINING_REPEAT_COUNT = "remaining_repeat_count";

        /**
         * This is a date-based constraint. It represents the time before which
         * an {@link android.drm.DrmStore.Action action} can be performed on
         * the rights-protected content.
         * <p>
         * Type: TEXT
         */
        public static final java.lang.String LICENSE_START_TIME = "license_start_time";

        /**
         * This is a date-based constaint. It represents the time after which
         * an {@link android.drm.DrmStore.Action action} can not be performed on
         * the rights-protected content.
         * <p>
         * Type: TEXT
         */
        public static final java.lang.String LICENSE_EXPIRY_TIME = "license_expiry_time";

        /**
         * This is a duration-based constaint. It represents the available time left
         * before the license expires.
         * <p>
         * Type: TEXT
         */
        public static final java.lang.String LICENSE_AVAILABLE_TIME = "license_available_time";

        /**
         * This is a user-defined constraint. It represents the additional constraint
         * using extended metadata.
         * <p>
         * Type: TEXT
         */
        public static final java.lang.String EXTENDED_METADATA = "extended_metadata";
    }

    /**
     * Defines DRM object types.
     */
    public static class DrmObjectType {
        /**
         * An unknown object type.
         */
        public static final int UNKNOWN = 0x0;

        /**
         * A rights-protected file object type.
         */
        public static final int CONTENT = 0x1;

        /**
         * A rights information object type.
         */
        public static final int RIGHTS_OBJECT = 0x2;

        /**
         * A trigger information object type.
         */
        public static final int TRIGGER_OBJECT = 0x3;

        /**
         *
         *
         * @deprecated This class should have been an interface instead.
        The default constuctor should have not been exposed.
         */
        public DrmObjectType() {
        }
    }

    /**
     * Defines playback states for content.
     */
    public static class Playback {
        /**
         * Playback started.
         */
        public static final int START = 0x0;

        /**
         * Playback stopped.
         */
        public static final int STOP = 0x1;

        /**
         * Playback paused.
         */
        public static final int PAUSE = 0x2;

        /**
         * Playback resumed.
         */
        public static final int RESUME = 0x3;

        /* package */
        static boolean isValid(int playbackStatus) {
            boolean isValid = false;
            switch (playbackStatus) {
                case android.drm.DrmStore.Playback.START :
                case android.drm.DrmStore.Playback.STOP :
                case android.drm.DrmStore.Playback.PAUSE :
                case android.drm.DrmStore.Playback.RESUME :
                    isValid = true;
            }
            return isValid;
        }

        /**
         *
         *
         * @deprecated This class should have been an interface instead.
        The default constuctor should have not been exposed.
         */
        public Playback() {
        }
    }

    /**
     * Defines actions that can be performed on rights-protected content.
     */
    public static class Action {
        /**
         * The default action.
         */
        public static final int DEFAULT = 0x0;

        /**
         * The rights-protected content can be played.
         */
        public static final int PLAY = 0x1;

        /**
         * The rights-protected content can be set as a ringtone.
         */
        public static final int RINGTONE = 0x2;

        /**
         * The rights-protected content can be transferred.
         */
        public static final int TRANSFER = 0x3;

        /**
         * The rights-protected content can be set as output.
         */
        public static final int OUTPUT = 0x4;

        /**
         * The rights-protected content can be previewed.
         */
        public static final int PREVIEW = 0x5;

        /**
         * The rights-protected content can be executed.
         */
        public static final int EXECUTE = 0x6;

        /**
         * The rights-protected content can be displayed.
         */
        public static final int DISPLAY = 0x7;

        /* package */
        static boolean isValid(int action) {
            boolean isValid = false;
            switch (action) {
                case android.drm.DrmStore.Action.DEFAULT :
                case android.drm.DrmStore.Action.PLAY :
                case android.drm.DrmStore.Action.RINGTONE :
                case android.drm.DrmStore.Action.TRANSFER :
                case android.drm.DrmStore.Action.OUTPUT :
                case android.drm.DrmStore.Action.PREVIEW :
                case android.drm.DrmStore.Action.EXECUTE :
                case android.drm.DrmStore.Action.DISPLAY :
                    isValid = true;
            }
            return isValid;
        }

        /**
         *
         *
         * @deprecated This class should have been an interface instead.
        The default constuctor should have not been exposed.
         */
        public Action() {
        }
    }

    /**
     * Defines status notifications for digital rights.
     */
    public static class RightsStatus {
        /**
         * The digital rights are valid.
         */
        public static final int RIGHTS_VALID = 0x0;

        /**
         * The digital rights are invalid.
         */
        public static final int RIGHTS_INVALID = 0x1;

        /**
         * The digital rights have expired.
         */
        public static final int RIGHTS_EXPIRED = 0x2;

        /**
         * The digital rights have not been acquired for the rights-protected content.
         */
        public static final int RIGHTS_NOT_ACQUIRED = 0x3;

        /**
         *
         *
         * @deprecated This class should have been an interface instead.
        The default constuctor should have not been exposed.
         */
        public RightsStatus() {
        }
    }

    /**
     *
     *
     * @deprecated This class should have been an interface instead.
    The default constuctor should have not been exposed.
     */
    public DrmStore() {
    }
}

