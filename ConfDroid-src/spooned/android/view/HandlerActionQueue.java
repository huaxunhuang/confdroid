/**
 * Copyright (C) 2015 The Android Open Source Project
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
 * Class used to enqueue pending work from Views when no Handler is attached.
 *
 * @unknown Exposed for test framework only.
 */
public class HandlerActionQueue {
    private android.view.HandlerActionQueue.HandlerAction[] mActions;

    private int mCount;

    public void post(java.lang.Runnable action) {
        postDelayed(action, 0);
    }

    public void postDelayed(java.lang.Runnable action, long delayMillis) {
        final android.view.HandlerActionQueue.HandlerAction handlerAction = new android.view.HandlerActionQueue.HandlerAction(action, delayMillis);
        synchronized(this) {
            if (mActions == null) {
                mActions = new android.view.HandlerActionQueue.HandlerAction[4];
            }
            mActions = com.android.internal.util.GrowingArrayUtils.append(mActions, mCount, handlerAction);
            mCount++;
        }
    }

    public void removeCallbacks(java.lang.Runnable action) {
        synchronized(this) {
            final int count = mCount;
            int j = 0;
            final android.view.HandlerActionQueue.HandlerAction[] actions = mActions;
            for (int i = 0; i < count; i++) {
                if (actions[i].matches(action)) {
                    // Remove this action by overwriting it within
                    // this loop or nulling it out later.
                    continue;
                }
                if (j != i) {
                    // At least one previous entry was removed, so
                    // this one needs to move to the "new" list.
                    actions[j] = actions[i];
                }
                j++;
            }
            // The "new" list only has j entries.
            mCount = j;
            // Null out any remaining entries.
            for (; j < count; j++) {
                actions[j] = null;
            }
        }
    }

    public void executeActions(android.os.Handler handler) {
        synchronized(this) {
            final android.view.HandlerActionQueue.HandlerAction[] actions = mActions;
            for (int i = 0, count = mCount; i < count; i++) {
                final android.view.HandlerActionQueue.HandlerAction handlerAction = actions[i];
                handler.postDelayed(handlerAction.action, handlerAction.delay);
            }
            mActions = null;
            mCount = 0;
        }
    }

    public int size() {
        return mCount;
    }

    public java.lang.Runnable getRunnable(int index) {
        if (index >= mCount) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        return mActions[index].action;
    }

    public long getDelay(int index) {
        if (index >= mCount) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        return mActions[index].delay;
    }

    private static class HandlerAction {
        final java.lang.Runnable action;

        final long delay;

        public HandlerAction(java.lang.Runnable action, long delay) {
            this.action = action;
            this.delay = delay;
        }

        public boolean matches(java.lang.Runnable otherAction) {
            return ((otherAction == null) && (action == null)) || ((action != null) && action.equals(otherAction));
        }
    }
}

