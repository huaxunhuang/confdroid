/**
 * Copyright (C) 2017 The Android Open Source Project
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
package android.content.pm;


/**
 * Utility methods that need to be used in application space.
 *
 * @unknown 
 */
public final class SELinuxUtil {
    /**
     * Append to existing seinfo label for instant apps @hide
     */
    private static final java.lang.String INSTANT_APP_STR = ":ephemeralapp";

    /**
     * Append to existing seinfo when modifications are complete @hide
     */
    public static final java.lang.String COMPLETE_STR = ":complete";

    /**
     *
     *
     * @unknown 
     */
    public static java.lang.String assignSeinfoUser(android.content.pm.PackageUserState userState) {
        if (userState.instantApp) {
            return android.content.pm.SELinuxUtil.INSTANT_APP_STR + android.content.pm.SELinuxUtil.COMPLETE_STR;
        }
        return android.content.pm.SELinuxUtil.COMPLETE_STR;
    }
}

