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
package android.app;


/**
 * Helper for monitoring the current importance of applications.
 *
 * @unknown 
 */
public class AppImportanceMonitor {
    final android.content.Context mContext;

    final android.util.SparseArray<android.app.AppImportanceMonitor.AppEntry> mApps = new android.util.SparseArray<>();

    static class AppEntry {
        final int uid;

        final android.util.SparseArray<java.lang.Integer> procs = new android.util.SparseArray<>(1);

        int importance = android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_GONE;

        AppEntry(int _uid) {
            uid = _uid;
        }
    }

    final android.app.IProcessObserver mProcessObserver = new android.app.IProcessObserver.Stub() {
        @java.lang.Override
        public void onForegroundActivitiesChanged(int pid, int uid, boolean foregroundActivities) {
        }

        @java.lang.Override
        public void onProcessStateChanged(int pid, int uid, int procState) {
            synchronized(mApps) {
                updateImportanceLocked(pid, uid, android.app.ActivityManager.RunningAppProcessInfo.procStateToImportance(procState), true);
            }
        }

        @java.lang.Override
        public void onProcessDied(int pid, int uid) {
            synchronized(mApps) {
                updateImportanceLocked(pid, uid, android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_GONE, true);
            }
        }
    };

    static final int MSG_UPDATE = 1;

    final android.os.Handler mHandler;

    public AppImportanceMonitor(android.content.Context context, android.os.Looper looper) {
        mContext = context;
        mHandler = new android.os.Handler(looper) {
            @java.lang.Override
            public void handleMessage(android.os.Message msg) {
                switch (msg.what) {
                    case android.app.AppImportanceMonitor.MSG_UPDATE :
                        onImportanceChanged(msg.arg1, msg.arg2 & 0xffff, msg.arg2 >> 16);
                        break;
                    default :
                        super.handleMessage(msg);
                }
            }
        };
        android.app.ActivityManager am = ((android.app.ActivityManager) (context.getSystemService(android.content.Context.ACTIVITY_SERVICE)));
        try {
            android.app.ActivityManagerNative.getDefault().registerProcessObserver(mProcessObserver);
        } catch (android.os.RemoteException e) {
        }
        java.util.List<android.app.ActivityManager.RunningAppProcessInfo> apps = am.getRunningAppProcesses();
        if (apps != null) {
            for (int i = 0; i < apps.size(); i++) {
                android.app.ActivityManager.RunningAppProcessInfo app = apps.get(i);
                updateImportanceLocked(app.uid, app.pid, app.importance, false);
            }
        }
    }

    public int getImportance(int uid) {
        android.app.AppImportanceMonitor.AppEntry ent = mApps.get(uid);
        if (ent == null) {
            return android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_GONE;
        }
        return ent.importance;
    }

    /**
     * Report when an app's importance changed. Called on looper given to constructor.
     */
    public void onImportanceChanged(int uid, int importance, int oldImportance) {
    }

    void updateImportanceLocked(int uid, int pid, int importance, boolean repChange) {
        android.app.AppImportanceMonitor.AppEntry ent = mApps.get(uid);
        if (ent == null) {
            ent = new android.app.AppImportanceMonitor.AppEntry(uid);
            mApps.put(uid, ent);
        }
        if (importance >= android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_GONE) {
            ent.procs.remove(pid);
        } else {
            ent.procs.put(pid, importance);
        }
        updateImportanceLocked(ent, repChange);
    }

    void updateImportanceLocked(android.app.AppImportanceMonitor.AppEntry ent, boolean repChange) {
        int appImp = android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_GONE;
        for (int i = 0; i < ent.procs.size(); i++) {
            int procImp = ent.procs.valueAt(i);
            if (procImp < appImp) {
                appImp = procImp;
            }
        }
        if (appImp != ent.importance) {
            int impCode = appImp | (ent.importance << 16);
            ent.importance = appImp;
            if (appImp >= android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_GONE) {
                mApps.remove(ent.uid);
            }
            if (repChange) {
                mHandler.obtainMessage(android.app.AppImportanceMonitor.MSG_UPDATE, ent.uid, impCode).sendToTarget();
            }
        }
    }
}

