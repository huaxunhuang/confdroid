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
package android.support.v4.view;


class KeyEventCompatEclair {
    public static java.lang.Object getKeyDispatcherState(android.view.View view) {
        return view.getKeyDispatcherState();
    }

    public static boolean dispatch(android.view.KeyEvent event, android.view.KeyEvent.Callback receiver, java.lang.Object state, java.lang.Object target) {
        return event.dispatch(receiver, ((android.view.KeyEvent.DispatcherState) (state)), target);
    }

    public static void startTracking(android.view.KeyEvent event) {
        event.startTracking();
    }

    public static boolean isTracking(android.view.KeyEvent event) {
        return event.isTracking();
    }
}

