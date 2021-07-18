/**
 * Copyright (C) 2012 The Android Open Source Project
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
package android.bordeaux.services;


// This class manages the learning sessions from multiple applications.
// The learning sessions are automatically backed up to the storage.
// 
class BordeauxSessionManager {
    private static final java.lang.String TAG = "BordeauxSessionManager";

    private android.bordeaux.services.BordeauxSessionStorage mSessionStorage;

    static class Session {
        java.lang.Class learnerClass;

        android.bordeaux.services.IBordeauxLearner learner;

        boolean modified = false;
    }

    static class SessionKey {
        java.lang.String value;
    }

    // Thread to periodically save the sessions to storage
    class PeriodicSave extends java.lang.Thread implements java.lang.Runnable {
        long mSavingInterval = 60000;// 60 seconds


        boolean mQuit = false;

        PeriodicSave() {
        }

        public void run() {
            while (!mQuit) {
                try {
                    java.lang.Thread.sleep(mSavingInterval);
                } catch (java.lang.InterruptedException e) {
                    // thread waked up.
                    // ignore
                }
                saveSessions();
            } 
        }
    }

    android.bordeaux.services.BordeauxSessionManager.PeriodicSave mSavingThread = new android.bordeaux.services.BordeauxSessionManager.PeriodicSave();

    private java.util.concurrent.ConcurrentHashMap<java.lang.String, android.bordeaux.services.BordeauxSessionManager.Session> mSessions = new java.util.concurrent.ConcurrentHashMap<java.lang.String, android.bordeaux.services.BordeauxSessionManager.Session>();

    public BordeauxSessionManager(final android.content.Context context) {
        mSessionStorage = new android.bordeaux.services.BordeauxSessionStorage(context);
        mSavingThread.start();
    }

    class LearningUpdateCallback implements android.bordeaux.services.IBordeauxLearner.ModelChangeCallback {
        private java.lang.String mKey;

        public LearningUpdateCallback(java.lang.String key) {
            mKey = key;
        }

        public void modelChanged(android.bordeaux.services.IBordeauxLearner learner) {
            // Save the session
            android.bordeaux.services.BordeauxSessionManager.Session session = mSessions.get(mKey);
            if (session != null) {
                synchronized(session) {
                    if (session.learner != learner) {
                        throw new java.lang.RuntimeException("Session data corrupted!");
                    }
                    session.modified = true;
                }
            }
        }
    }

    // internal unique key that identifies the learning instance.
    // Composed by the package id of the calling process, learning class name
    // and user specified name.
    public android.bordeaux.services.BordeauxSessionManager.SessionKey getSessionKey(java.lang.String callingUid, java.lang.Class learnerClass, java.lang.String name) {
        android.bordeaux.services.BordeauxSessionManager.SessionKey key = new android.bordeaux.services.BordeauxSessionManager.SessionKey();
        key.value = ((((callingUid + "#") + "_") + name) + "_") + learnerClass.getName();
        return key;
    }

    public android.os.IBinder getSessionBinder(java.lang.Class learnerClass, android.bordeaux.services.BordeauxSessionManager.SessionKey key) {
        if (mSessions.containsKey(key.value)) {
            return mSessions.get(key.value).learner.getBinder();
        }
        // not in memory cache
        try {
            // try to find it in the database
            android.bordeaux.services.BordeauxSessionManager.Session stored = mSessionStorage.getSession(key.value);
            if (stored != null) {
                // set the callback, so that we can save the state
                stored.learner.setModelChangeCallback(new android.bordeaux.services.BordeauxSessionManager.LearningUpdateCallback(key.value));
                // found session in the storage, put in the cache
                mSessions.put(key.value, stored);
                return stored.learner.getBinder();
            }
            // if session is not already stored, create a new one.
            android.util.Log.i(android.bordeaux.services.BordeauxSessionManager.TAG, "create a new learning session: " + key.value);
            android.bordeaux.services.IBordeauxLearner learner = ((android.bordeaux.services.IBordeauxLearner) (learnerClass.getConstructor().newInstance()));
            // set the callback, so that we can save the state
            learner.setModelChangeCallback(new android.bordeaux.services.BordeauxSessionManager.LearningUpdateCallback(key.value));
            android.bordeaux.services.BordeauxSessionManager.Session session = new android.bordeaux.services.BordeauxSessionManager.Session();
            session.learnerClass = learnerClass;
            session.learner = learner;
            mSessions.put(key.value, session);
            return learner.getBinder();
        } catch (java.lang.Exception e) {
            throw new java.lang.RuntimeException("Can't instantiate class: " + learnerClass.getName());
        }
    }

    public void saveSessions() {
        for (java.util.Map.Entry<java.lang.String, android.bordeaux.services.BordeauxSessionManager.Session> session : mSessions.entrySet()) {
            synchronized(session) {
                // Save the session if it's modified.
                if (session.getValue().modified) {
                    android.bordeaux.services.BordeauxSessionManager.SessionKey skey = new android.bordeaux.services.BordeauxSessionManager.SessionKey();
                    skey.value = session.getKey();
                    saveSession(skey);
                }
            }
        }
    }

    public boolean saveSession(android.bordeaux.services.BordeauxSessionManager.SessionKey key) {
        android.bordeaux.services.BordeauxSessionManager.Session session = mSessions.get(key.value);
        if (session != null) {
            synchronized(session) {
                byte[] model = session.learner.getModel();
                // write to database
                boolean res = mSessionStorage.saveSession(key.value, session.learnerClass, model);
                if (res)
                    session.modified = false;
                else {
                    android.util.Log.e(android.bordeaux.services.BordeauxSessionManager.TAG, "Can't save session: " + key.value);
                }
                return res;
            }
        }
        android.util.Log.e(android.bordeaux.services.BordeauxSessionManager.TAG, "Session not found: " + key.value);
        return false;
    }

    // Load all session data into memory.
    // The session data will be loaded into the memory from the database, even
    // if this method is not called.
    public void loadSessions() {
        synchronized(mSessions) {
            mSessionStorage.getAllSessions(mSessions);
            for (java.util.Map.Entry<java.lang.String, android.bordeaux.services.BordeauxSessionManager.Session> session : mSessions.entrySet()) {
                // set the callback, so that we can save the state
                session.getValue().learner.setModelChangeCallback(new android.bordeaux.services.BordeauxSessionManager.LearningUpdateCallback(session.getKey()));
            }
        }
    }

    public void removeAllSessionsFromCaller(java.lang.String callingUid) {
        // remove in the hash table
        java.util.ArrayList<java.lang.String> remove_keys = new java.util.ArrayList<java.lang.String>();
        for (java.util.Map.Entry<java.lang.String, android.bordeaux.services.BordeauxSessionManager.Session> session : mSessions.entrySet()) {
            if (session.getKey().startsWith(callingUid + "#")) {
                remove_keys.add(session.getKey());
            }
        }
        for (java.lang.String key : remove_keys) {
            mSessions.remove(key);
        }
        // remove all session data from the callingUid in database
        // % is used as wild match for the rest of the string in sql
        int nDeleted = mSessionStorage.removeSessions(callingUid + "#%");
        if (nDeleted > 0)
            android.util.Log.i(android.bordeaux.services.BordeauxSessionManager.TAG, ("Successfully deleted " + nDeleted) + "sessions");

    }
}

