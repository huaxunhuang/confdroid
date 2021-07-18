/**
 * Copyright (C) 2020 The Android Open Source Project
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
package android.content.pm.parsing.component;


/**
 *
 *
 * @unknown 
 */
public class ParsedActivityUtils {
    private static final java.lang.String TAG = android.content.pm.parsing.ParsingPackageUtils.TAG;

    @android.annotation.NonNull
    @com.android.internal.annotations.VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
    public static android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.component.ParsedActivity> parseActivityOrReceiver(java.lang.String[] separateProcesses, android.content.pm.parsing.ParsingPackage pkg, android.content.res.Resources res, android.content.res.XmlResourceParser parser, int flags, boolean useRoundIcon, android.content.pm.parsing.result.ParseInput input) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        final java.lang.String packageName = pkg.getPackageName();
        final android.content.pm.parsing.component.ParsedActivity activity = new android.content.pm.parsing.component.ParsedActivity();
        boolean receiver = "receiver".equals(parser.getName());
        java.lang.String tag = ("<" + parser.getName()) + ">";
        android.content.res.TypedArray sa = res.obtainAttributes(parser, R.styleable.AndroidManifestActivity);
        try {
            android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.component.ParsedActivity> result = android.content.pm.parsing.component.ParsedMainComponentUtils.parseMainComponent(activity, tag, separateProcesses, pkg, sa, flags, useRoundIcon, input, R.styleable.AndroidManifestActivity_banner, R.styleable.AndroidManifestActivity_description, R.styleable.AndroidManifestActivity_directBootAware, R.styleable.AndroidManifestActivity_enabled, R.styleable.AndroidManifestActivity_icon, R.styleable.AndroidManifestActivity_label, R.styleable.AndroidManifestActivity_logo, R.styleable.AndroidManifestActivity_name, R.styleable.AndroidManifestActivity_process, R.styleable.AndroidManifestActivity_roundIcon, R.styleable.AndroidManifestActivity_splitName);
            if (result.isError()) {
                return result;
            }
            if (receiver && pkg.isCantSaveState()) {
                // A heavy-weight application can not have receivers in its main process
                if (java.util.Objects.equals(activity.getProcessName(), packageName)) {
                    return input.error("Heavy-weight applications can not have receivers " + "in main process");
                }
            }
            // The following section has formatting off to make it easier to read the flags.
            // Multi-lining them to fit within the column restriction makes it hard to tell what
            // field is assigned where.
            // @formatter:off
            activity.theme = sa.getResourceId(R.styleable.AndroidManifestActivity_theme, 0);
            activity.uiOptions = sa.getInt(R.styleable.AndroidManifestActivity_uiOptions, pkg.getUiOptions());
            activity.flags |= (((((((((((android.content.pm.parsing.component.ComponentParseUtils.flag(android.content.pm.ActivityInfo.FLAG_ALLOW_TASK_REPARENTING, R.styleable.AndroidManifestActivity_allowTaskReparenting, pkg.isAllowTaskReparenting(), sa) | android.content.pm.parsing.component.ComponentParseUtils.flag(android.content.pm.ActivityInfo.FLAG_ALWAYS_RETAIN_TASK_STATE, R.styleable.AndroidManifestActivity_alwaysRetainTaskState, sa)) | android.content.pm.parsing.component.ComponentParseUtils.flag(android.content.pm.ActivityInfo.FLAG_CLEAR_TASK_ON_LAUNCH, R.styleable.AndroidManifestActivity_clearTaskOnLaunch, sa)) | android.content.pm.parsing.component.ComponentParseUtils.flag(android.content.pm.ActivityInfo.FLAG_EXCLUDE_FROM_RECENTS, R.styleable.AndroidManifestActivity_excludeFromRecents, sa)) | android.content.pm.parsing.component.ComponentParseUtils.flag(android.content.pm.ActivityInfo.FLAG_FINISH_ON_CLOSE_SYSTEM_DIALOGS, R.styleable.AndroidManifestActivity_finishOnCloseSystemDialogs, sa)) | android.content.pm.parsing.component.ComponentParseUtils.flag(android.content.pm.ActivityInfo.FLAG_FINISH_ON_TASK_LAUNCH, R.styleable.AndroidManifestActivity_finishOnTaskLaunch, sa)) | android.content.pm.parsing.component.ComponentParseUtils.flag(android.content.pm.ActivityInfo.FLAG_IMMERSIVE, R.styleable.AndroidManifestActivity_immersive, sa)) | android.content.pm.parsing.component.ComponentParseUtils.flag(android.content.pm.ActivityInfo.FLAG_MULTIPROCESS, R.styleable.AndroidManifestActivity_multiprocess, sa)) | android.content.pm.parsing.component.ComponentParseUtils.flag(android.content.pm.ActivityInfo.FLAG_NO_HISTORY, R.styleable.AndroidManifestActivity_noHistory, sa)) | android.content.pm.parsing.component.ComponentParseUtils.flag(android.content.pm.ActivityInfo.FLAG_SHOW_FOR_ALL_USERS, R.styleable.AndroidManifestActivity_showForAllUsers, sa)) | android.content.pm.parsing.component.ComponentParseUtils.flag(android.content.pm.ActivityInfo.FLAG_SHOW_FOR_ALL_USERS, R.styleable.AndroidManifestActivity_showOnLockScreen, sa)) | android.content.pm.parsing.component.ComponentParseUtils.flag(android.content.pm.ActivityInfo.FLAG_STATE_NOT_NEEDED, R.styleable.AndroidManifestActivity_stateNotNeeded, sa)) | android.content.pm.parsing.component.ComponentParseUtils.flag(android.content.pm.ActivityInfo.FLAG_SYSTEM_USER_ONLY, R.styleable.AndroidManifestActivity_systemUserOnly, sa);
            if (!receiver) {
                activity.flags |= ((((((((android.content.pm.parsing.component.ComponentParseUtils.flag(android.content.pm.ActivityInfo.FLAG_HARDWARE_ACCELERATED, R.styleable.AndroidManifestActivity_hardwareAccelerated, pkg.isBaseHardwareAccelerated(), sa) | android.content.pm.parsing.component.ComponentParseUtils.flag(android.content.pm.ActivityInfo.FLAG_ALLOW_EMBEDDED, R.styleable.AndroidManifestActivity_allowEmbedded, sa)) | android.content.pm.parsing.component.ComponentParseUtils.flag(android.content.pm.ActivityInfo.FLAG_ALWAYS_FOCUSABLE, R.styleable.AndroidManifestActivity_alwaysFocusable, sa)) | android.content.pm.parsing.component.ComponentParseUtils.flag(android.content.pm.ActivityInfo.FLAG_AUTO_REMOVE_FROM_RECENTS, R.styleable.AndroidManifestActivity_autoRemoveFromRecents, sa)) | android.content.pm.parsing.component.ComponentParseUtils.flag(android.content.pm.ActivityInfo.FLAG_RELINQUISH_TASK_IDENTITY, R.styleable.AndroidManifestActivity_relinquishTaskIdentity, sa)) | android.content.pm.parsing.component.ComponentParseUtils.flag(android.content.pm.ActivityInfo.FLAG_RESUME_WHILE_PAUSING, R.styleable.AndroidManifestActivity_resumeWhilePausing, sa)) | android.content.pm.parsing.component.ComponentParseUtils.flag(android.content.pm.ActivityInfo.FLAG_SHOW_WHEN_LOCKED, R.styleable.AndroidManifestActivity_showWhenLocked, sa)) | android.content.pm.parsing.component.ComponentParseUtils.flag(android.content.pm.ActivityInfo.FLAG_SUPPORTS_PICTURE_IN_PICTURE, R.styleable.AndroidManifestActivity_supportsPictureInPicture, sa)) | android.content.pm.parsing.component.ComponentParseUtils.flag(android.content.pm.ActivityInfo.FLAG_TURN_SCREEN_ON, R.styleable.AndroidManifestActivity_turnScreenOn, sa)) | android.content.pm.parsing.component.ComponentParseUtils.flag(android.content.pm.ActivityInfo.FLAG_PREFER_MINIMAL_POST_PROCESSING, R.styleable.AndroidManifestActivity_preferMinimalPostProcessing, sa);
                activity.privateFlags |= android.content.pm.parsing.component.ComponentParseUtils.flag(android.content.pm.ActivityInfo.FLAG_INHERIT_SHOW_WHEN_LOCKED, R.styleable.AndroidManifestActivity_inheritShowWhenLocked, sa);
                activity.colorMode = sa.getInt(R.styleable.AndroidManifestActivity_colorMode, android.content.pm.ActivityInfo.COLOR_MODE_DEFAULT);
                activity.documentLaunchMode = sa.getInt(R.styleable.AndroidManifestActivity_documentLaunchMode, android.content.pm.ActivityInfo.DOCUMENT_LAUNCH_NONE);
                activity.launchMode = sa.getInt(R.styleable.AndroidManifestActivity_launchMode, android.content.pm.ActivityInfo.LAUNCH_MULTIPLE);
                activity.lockTaskLaunchMode = sa.getInt(R.styleable.AndroidManifestActivity_lockTaskMode, 0);
                activity.maxRecents = sa.getInt(R.styleable.AndroidManifestActivity_maxRecents, android.app.ActivityTaskManager.getDefaultAppRecentsLimitStatic());
                activity.persistableMode = sa.getInteger(R.styleable.AndroidManifestActivity_persistableMode, android.content.pm.ActivityInfo.PERSIST_ROOT_ONLY);
                activity.requestedVrComponent = sa.getString(R.styleable.AndroidManifestActivity_enableVrMode);
                activity.rotationAnimation = sa.getInt(R.styleable.AndroidManifestActivity_rotationAnimation, android.view.WindowManager.LayoutParams.ROTATION_ANIMATION_UNSPECIFIED);
                activity.softInputMode = sa.getInt(R.styleable.AndroidManifestActivity_windowSoftInputMode, 0);
                activity.configChanges = android.content.pm.PackageParser.getActivityConfigChanges(sa.getInt(R.styleable.AndroidManifestActivity_configChanges, 0), sa.getInt(R.styleable.AndroidManifestActivity_recreateOnConfigChanges, 0));
                int screenOrientation = sa.getInt(R.styleable.AndroidManifestActivity_screenOrientation, android.content.pm.ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                int resizeMode = android.content.pm.parsing.component.ParsedActivityUtils.getActivityResizeMode(pkg, sa, screenOrientation);
                activity.screenOrientation = screenOrientation;
                activity.resizeMode = resizeMode;
                if (sa.hasValue(R.styleable.AndroidManifestActivity_maxAspectRatio) && (sa.getType(R.styleable.AndroidManifestActivity_maxAspectRatio) == android.util.TypedValue.TYPE_FLOAT)) {
                    activity.setMaxAspectRatio(resizeMode, /* default */
                    sa.getFloat(R.styleable.AndroidManifestActivity_maxAspectRatio, 0));
                }
                if (sa.hasValue(R.styleable.AndroidManifestActivity_minAspectRatio) && (sa.getType(R.styleable.AndroidManifestActivity_minAspectRatio) == android.util.TypedValue.TYPE_FLOAT)) {
                    activity.setMinAspectRatio(resizeMode, /* default */
                    sa.getFloat(R.styleable.AndroidManifestActivity_minAspectRatio, 0));
                }
            } else {
                activity.launchMode = android.content.pm.ActivityInfo.LAUNCH_MULTIPLE;
                activity.configChanges = 0;
                activity.flags |= android.content.pm.parsing.component.ComponentParseUtils.flag(android.content.pm.ActivityInfo.FLAG_SINGLE_USER, R.styleable.AndroidManifestActivity_singleUser, sa);
            }
            // @formatter:on
            java.lang.String taskAffinity = sa.getNonConfigurationString(R.styleable.AndroidManifestActivity_taskAffinity, android.content.res.Configuration.NATIVE_CONFIG_VERSION);
            android.content.pm.parsing.result.ParseResult<java.lang.String> affinityNameResult = android.content.pm.parsing.component.ComponentParseUtils.buildTaskAffinityName(packageName, pkg.getTaskAffinity(), taskAffinity, input);
            if (affinityNameResult.isError()) {
                return input.error(affinityNameResult);
            }
            activity.taskAffinity = affinityNameResult.getResult();
            boolean visibleToEphemeral = sa.getBoolean(R.styleable.AndroidManifestActivity_visibleToInstantApps, false);
            if (visibleToEphemeral) {
                activity.flags |= android.content.pm.ActivityInfo.FLAG_VISIBLE_TO_INSTANT_APP;
                pkg.setVisibleToInstantApps(true);
            }
            return /* isAlias */
            android.content.pm.parsing.component.ParsedActivityUtils.parseActivityOrAlias(activity, pkg, tag, parser, res, sa, receiver, false, visibleToEphemeral, input, R.styleable.AndroidManifestActivity_parentActivityName, R.styleable.AndroidManifestActivity_permission, R.styleable.AndroidManifestActivity_exported);
        } finally {
            sa.recycle();
        }
    }

    @android.annotation.NonNull
    public static android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.component.ParsedActivity> parseActivityAlias(android.content.pm.parsing.ParsingPackage pkg, android.content.res.Resources res, android.content.res.XmlResourceParser parser, boolean useRoundIcon, android.content.pm.parsing.result.ParseInput input) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        android.content.res.TypedArray sa = res.obtainAttributes(parser, R.styleable.AndroidManifestActivityAlias);
        try {
            java.lang.String targetActivity = sa.getNonConfigurationString(R.styleable.AndroidManifestActivityAlias_targetActivity, android.content.res.Configuration.NATIVE_CONFIG_VERSION);
            if (targetActivity == null) {
                return input.error("<activity-alias> does not specify android:targetActivity");
            }
            java.lang.String packageName = pkg.getPackageName();
            targetActivity = android.content.pm.parsing.ParsingUtils.buildClassName(packageName, targetActivity);
            if (targetActivity == null) {
                return input.error("Empty class name in package " + packageName);
            }
            android.content.pm.parsing.component.ParsedActivity target = null;
            java.util.List<android.content.pm.parsing.component.ParsedActivity> activities = pkg.getActivities();
            final int activitiesSize = com.android.internal.util.ArrayUtils.size(activities);
            for (int i = 0; i < activitiesSize; i++) {
                android.content.pm.parsing.component.ParsedActivity t = activities.get(i);
                if (targetActivity.equals(t.getName())) {
                    target = t;
                    break;
                }
            }
            if (target == null) {
                return input.error((((("<activity-alias> target activity " + targetActivity) + " not found in manifest with activities = ") + pkg.getActivities()) + ", parsedActivities = ") + activities);
            }
            android.content.pm.parsing.component.ParsedActivity activity = android.content.pm.parsing.component.ParsedActivity.makeAlias(targetActivity, target);
            java.lang.String tag = ("<" + parser.getName()) + ">";
            android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.component.ParsedActivity> result = /* directBootAwareAttr */
            /* processAttr */
            /* splitNameAttr */
            android.content.pm.parsing.component.ParsedMainComponentUtils.parseMainComponent(activity, tag, null, pkg, sa, 0, useRoundIcon, input, R.styleable.AndroidManifestActivityAlias_banner, R.styleable.AndroidManifestActivityAlias_description, null, R.styleable.AndroidManifestActivityAlias_enabled, R.styleable.AndroidManifestActivityAlias_icon, R.styleable.AndroidManifestActivityAlias_label, R.styleable.AndroidManifestActivityAlias_logo, R.styleable.AndroidManifestActivityAlias_name, null, R.styleable.AndroidManifestActivityAlias_roundIcon, null);
            if (result.isError()) {
                return result;
            }
            // TODO add visibleToInstantApps attribute to activity alias
            final boolean visibleToEphemeral = (activity.getFlags() & android.content.pm.ActivityInfo.FLAG_VISIBLE_TO_INSTANT_APP) != 0;
            return /* isReceiver */
            /* isAlias */
            android.content.pm.parsing.component.ParsedActivityUtils.parseActivityOrAlias(activity, pkg, tag, parser, res, sa, false, true, visibleToEphemeral, input, R.styleable.AndroidManifestActivityAlias_parentActivityName, R.styleable.AndroidManifestActivityAlias_permission, R.styleable.AndroidManifestActivityAlias_exported);
        } finally {
            sa.recycle();
        }
    }

    /**
     * This method shares parsing logic between Activity/Receiver/alias instances, but requires
     * passing in booleans for isReceiver/isAlias, since there's no indicator in the other
     * parameters.
     *
     * They're used to filter the parsed tags and their behavior. This makes the method rather
     * messy, but it's more maintainable than writing 3 separate methods for essentially the same
     * type of logic.
     */
    @android.annotation.NonNull
    private static android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.component.ParsedActivity> parseActivityOrAlias(android.content.pm.parsing.component.ParsedActivity activity, android.content.pm.parsing.ParsingPackage pkg, java.lang.String tag, android.content.res.XmlResourceParser parser, android.content.res.Resources resources, android.content.res.TypedArray array, boolean isReceiver, boolean isAlias, boolean visibleToEphemeral, android.content.pm.parsing.result.ParseInput input, int parentActivityNameAttr, int permissionAttr, int exportedAttr) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        java.lang.String parentActivityName = array.getNonConfigurationString(parentActivityNameAttr, android.content.res.Configuration.NATIVE_CONFIG_VERSION);
        if (parentActivityName != null) {
            java.lang.String packageName = pkg.getPackageName();
            java.lang.String parentClassName = android.content.pm.parsing.ParsingUtils.buildClassName(packageName, parentActivityName);
            if (parentClassName == null) {
                android.util.Log.e(android.content.pm.parsing.component.ParsedActivityUtils.TAG, (("Activity " + activity.getName()) + " specified invalid parentActivityName ") + parentActivityName);
            } else {
                activity.setParentActivity(parentClassName);
            }
        }
        java.lang.String permission = array.getNonConfigurationString(permissionAttr, 0);
        if (isAlias) {
            // An alias will override permissions to allow referencing an Activity through its alias
            // without needing the original permission. If an alias needs the same permission,
            // it must be re-declared.
            activity.setPermission(permission);
        } else {
            activity.setPermission(permission != null ? permission : pkg.getPermission());
        }
        final boolean setExported = array.hasValue(exportedAttr);
        if (setExported) {
            activity.exported = array.getBoolean(exportedAttr, false);
        }
        final int depth = parser.getDepth();
        int type;
        while (((type = parser.next()) != org.xmlpull.v1.XmlPullParser.END_DOCUMENT) && ((type != org.xmlpull.v1.XmlPullParser.END_TAG) || (parser.getDepth() > depth))) {
            if (type != org.xmlpull.v1.XmlPullParser.START_TAG) {
                continue;
            }
            final android.content.pm.parsing.result.ParseResult result;
            if (parser.getName().equals("intent-filter")) {
                android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.component.ParsedIntentInfo> intentResult = android.content.pm.parsing.component.ParsedActivityUtils.parseIntentFilter(pkg, activity, !isReceiver, visibleToEphemeral, resources, parser, input);
                if (intentResult.isSuccess()) {
                    android.content.pm.parsing.component.ParsedIntentInfo intent = intentResult.getResult();
                    if (intent != null) {
                        activity.order = java.lang.Math.max(intent.getOrder(), activity.order);
                        activity.addIntent(intent);
                        if ((android.content.pm.PackageParser.LOG_UNSAFE_BROADCASTS && isReceiver) && (pkg.getTargetSdkVersion() >= Build.VERSION_CODES.O)) {
                            int actionCount = intent.countActions();
                            for (int i = 0; i < actionCount; i++) {
                                final java.lang.String action = intent.getAction(i);
                                if ((action == null) || (!action.startsWith("android."))) {
                                    continue;
                                }
                                if (!android.content.pm.PackageParser.SAFE_BROADCASTS.contains(action)) {
                                    android.util.Slog.w(android.content.pm.parsing.component.ParsedActivityUtils.TAG, (((("Broadcast " + action) + " may never be delivered to ") + pkg.getPackageName()) + " as requested at: ") + parser.getPositionDescription());
                                }
                            }
                        }
                    }
                }
                result = intentResult;
            } else
                if (parser.getName().equals("meta-data")) {
                    result = android.content.pm.parsing.component.ParsedComponentUtils.addMetaData(activity, pkg, resources, parser, input);
                } else
                    if (((!isReceiver) && (!isAlias)) && parser.getName().equals("preferred")) {
                        android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.component.ParsedIntentInfo> intentResult = /* allowImplicitEphemeralVisibility */
                        android.content.pm.parsing.component.ParsedActivityUtils.parseIntentFilter(pkg, activity, true, visibleToEphemeral, resources, parser, input);
                        if (intentResult.isSuccess()) {
                            android.content.pm.parsing.component.ParsedIntentInfo intent = intentResult.getResult();
                            if (intent != null) {
                                pkg.addPreferredActivityFilter(activity.getClassName(), intent);
                            }
                        }
                        result = intentResult;
                    } else
                        if (((!isReceiver) && (!isAlias)) && parser.getName().equals("layout")) {
                            android.content.pm.parsing.result.ParseResult<android.content.pm.ActivityInfo.WindowLayout> layoutResult = android.content.pm.parsing.component.ParsedActivityUtils.parseLayout(resources, parser, input);
                            if (layoutResult.isSuccess()) {
                                activity.windowLayout = layoutResult.getResult();
                            }
                            result = layoutResult;
                        } else {
                            result = android.content.pm.parsing.ParsingUtils.unknownTag(tag, pkg, parser, input);
                        }



            if (result.isError()) {
                return input.error(result);
            }
        } 
        android.content.pm.parsing.result.ParseResult<android.content.pm.ActivityInfo.WindowLayout> layoutResult = android.content.pm.parsing.component.ParsedActivityUtils.resolveWindowLayout(activity, input);
        if (layoutResult.isError()) {
            return input.error(layoutResult);
        }
        activity.windowLayout = layoutResult.getResult();
        if (!setExported) {
            activity.exported = activity.getIntents().size() > 0;
        }
        return input.success(activity);
    }

    @android.annotation.NonNull
    private static android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.component.ParsedIntentInfo> parseIntentFilter(android.content.pm.parsing.ParsingPackage pkg, android.content.pm.parsing.component.ParsedActivity activity, boolean allowImplicitEphemeralVisibility, boolean visibleToEphemeral, android.content.res.Resources resources, android.content.res.XmlResourceParser parser, android.content.pm.parsing.result.ParseInput input) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.component.ParsedIntentInfo> result = /* allowGlobs */
        /* allowAutoVerify */
        /* failOnNoActions */
        android.content.pm.parsing.component.ParsedMainComponentUtils.parseIntentFilter(activity, pkg, resources, parser, visibleToEphemeral, true, true, allowImplicitEphemeralVisibility, true, input);
        if (result.isError()) {
            return input.error(result);
        }
        android.content.pm.parsing.component.ParsedIntentInfo intent = result.getResult();
        if (intent != null) {
            if (intent.isVisibleToInstantApp()) {
                activity.flags |= android.content.pm.ActivityInfo.FLAG_VISIBLE_TO_INSTANT_APP;
            }
            if (intent.isImplicitlyVisibleToInstantApp()) {
                activity.flags |= android.content.pm.ActivityInfo.FLAG_IMPLICITLY_VISIBLE_TO_INSTANT_APP;
            }
        }
        return input.success(intent);
    }

    private static int getActivityResizeMode(android.content.pm.parsing.ParsingPackage pkg, android.content.res.TypedArray sa, int screenOrientation) {
        java.lang.Boolean resizeableActivity = pkg.getResizeableActivity();
        if (sa.hasValue(R.styleable.AndroidManifestActivity_resizeableActivity) || (resizeableActivity != null)) {
            // Activity or app explicitly set if it is resizeable or not;
            if (sa.getBoolean(R.styleable.AndroidManifestActivity_resizeableActivity, (resizeableActivity != null) && resizeableActivity)) {
                return android.content.pm.ActivityInfo.RESIZE_MODE_RESIZEABLE;
            } else {
                return android.content.pm.ActivityInfo.RESIZE_MODE_UNRESIZEABLE;
            }
        }
        if (pkg.isResizeableActivityViaSdkVersion()) {
            // The activity or app didn't explicitly set the resizing option, however we want to
            // make it resize due to the sdk version it is targeting.
            return android.content.pm.ActivityInfo.RESIZE_MODE_RESIZEABLE_VIA_SDK_VERSION;
        }
        // resize preference isn't set and target sdk version doesn't support resizing apps by
        // default. For the app to be resizeable if it isn't fixed orientation or immersive.
        if (android.content.pm.ActivityInfo.isFixedOrientationPortrait(screenOrientation)) {
            return android.content.pm.ActivityInfo.RESIZE_MODE_FORCE_RESIZABLE_PORTRAIT_ONLY;
        } else
            if (android.content.pm.ActivityInfo.isFixedOrientationLandscape(screenOrientation)) {
                return android.content.pm.ActivityInfo.RESIZE_MODE_FORCE_RESIZABLE_LANDSCAPE_ONLY;
            } else
                if (screenOrientation == android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LOCKED) {
                    return android.content.pm.ActivityInfo.RESIZE_MODE_FORCE_RESIZABLE_PRESERVE_ORIENTATION;
                } else {
                    return android.content.pm.ActivityInfo.RESIZE_MODE_FORCE_RESIZEABLE;
                }


    }

    @android.annotation.NonNull
    private static android.content.pm.parsing.result.ParseResult<android.content.pm.ActivityInfo.WindowLayout> parseLayout(android.content.res.Resources res, android.util.AttributeSet attrs, android.content.pm.parsing.result.ParseInput input) {
        android.content.res.TypedArray sw = res.obtainAttributes(attrs, R.styleable.AndroidManifestLayout);
        try {
            int width = -1;
            float widthFraction = -1.0F;
            int height = -1;
            float heightFraction = -1.0F;
            final int widthType = sw.getType(R.styleable.AndroidManifestLayout_defaultWidth);
            if (widthType == android.util.TypedValue.TYPE_FRACTION) {
                widthFraction = sw.getFraction(R.styleable.AndroidManifestLayout_defaultWidth, 1, 1, -1);
            } else
                if (widthType == android.util.TypedValue.TYPE_DIMENSION) {
                    width = sw.getDimensionPixelSize(R.styleable.AndroidManifestLayout_defaultWidth, -1);
                }

            final int heightType = sw.getType(R.styleable.AndroidManifestLayout_defaultHeight);
            if (heightType == android.util.TypedValue.TYPE_FRACTION) {
                heightFraction = sw.getFraction(R.styleable.AndroidManifestLayout_defaultHeight, 1, 1, -1);
            } else
                if (heightType == android.util.TypedValue.TYPE_DIMENSION) {
                    height = sw.getDimensionPixelSize(R.styleable.AndroidManifestLayout_defaultHeight, -1);
                }

            int gravity = sw.getInt(R.styleable.AndroidManifestLayout_gravity, android.view.Gravity.CENTER);
            int minWidth = sw.getDimensionPixelSize(R.styleable.AndroidManifestLayout_minWidth, -1);
            int minHeight = sw.getDimensionPixelSize(R.styleable.AndroidManifestLayout_minHeight, -1);
            return input.success(new android.content.pm.ActivityInfo.WindowLayout(width, widthFraction, height, heightFraction, gravity, minWidth, minHeight));
        } finally {
            sw.recycle();
        }
    }

    /**
     * Resolves values in {@link ActivityInfo.WindowLayout}.
     *
     * <p>{@link ActivityInfo.WindowLayout#windowLayoutAffinity} has a fallback metadata used in
     * Android R and some variants of pre-R.
     */
    private static android.content.pm.parsing.result.ParseResult<android.content.pm.ActivityInfo.WindowLayout> resolveWindowLayout(android.content.pm.parsing.component.ParsedActivity activity, android.content.pm.parsing.result.ParseInput input) {
        // There isn't a metadata for us to fall back. Whatever is in layout is correct.
        if ((activity.metaData == null) || (!activity.metaData.containsKey(android.content.pm.PackageParser.METADATA_ACTIVITY_WINDOW_LAYOUT_AFFINITY))) {
            return input.success(activity.windowLayout);
        }
        // Layout already specifies a value. We should just use that one.
        if ((activity.windowLayout != null) && (activity.windowLayout.windowLayoutAffinity != null)) {
            return input.success(activity.windowLayout);
        }
        java.lang.String windowLayoutAffinity = activity.metaData.getString(android.content.pm.PackageParser.METADATA_ACTIVITY_WINDOW_LAYOUT_AFFINITY);
        android.content.pm.ActivityInfo.WindowLayout layout = activity.windowLayout;
        if (layout == null) {
            layout = /* width */
            /* widthFraction */
            /* height */
            /* heightFraction */
            /* minWidth */
            /* minHeight */
            new android.content.pm.ActivityInfo.WindowLayout(-1, -1, -1, -1, android.view.Gravity.NO_GRAVITY, -1, -1);
        }
        layout.windowLayoutAffinity = windowLayoutAffinity;
        return input.success(layout);
    }
}

