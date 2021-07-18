/**
 * Copyright (C) 2016 The Android Open Source Project
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
package android.support.v4.app;


class NotificationManagerCompatKitKat {
    private static final java.lang.String CHECK_OP_NO_THROW = "checkOpNoThrow";

    private static final java.lang.String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";

    public static boolean areNotificationsEnabled(android.content.Context context) {
        android.app.AppOpsManager appOps = ((android.app.AppOpsManager) (context.getSystemService(android.content.Context.APP_OPS_SERVICE)));
        android.content.pm.ApplicationInfo appInfo = context.getApplicationInfo();
        java.lang.String pkg = context.getApplicationContext().getPackageName();
        int uid = appInfo.uid;
        try {
            java.lang.Class<?> appOpsClass = java.lang.Class.forName(android.app.AppOpsManager.class.getName());
            java.lang.reflect.Method checkOpNoThrowMethod = appOpsClass.getMethod(android.support.v4.app.NotificationManagerCompatKitKat.CHECK_OP_NO_THROW, java.lang.Integer.TYPE, java.lang.Integer.TYPE, java.lang.String.class);
            java.lang.reflect.Field opPostNotificationValue = appOpsClass.getDeclaredField(android.support.v4.app.NotificationManagerCompatKitKat.OP_POST_NOTIFICATION);
            int value = ((int) (opPostNotificationValue.get(java.lang.Integer.class)));
            return ((int) (checkOpNoThrowMethod.invoke(appOps, value, uid, pkg))) == android.app.AppOpsManager.MODE_ALLOWED;
        } catch (java.lang.ClassNotFoundException | java.lang.NoSuchMethodException | java.lang.NoSuchFieldException | java.lang.reflect.InvocationTargetException | java.lang.IllegalAccessException | java.lang.RuntimeException e) {
            return true;
        }
    }
}

