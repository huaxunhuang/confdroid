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
public class ParsedServiceUtils {
    private static final java.lang.String TAG = android.content.pm.parsing.ParsingPackageUtils.TAG;

    @android.annotation.NonNull
    public static android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.component.ParsedService> parseService(java.lang.String[] separateProcesses, android.content.pm.parsing.ParsingPackage pkg, android.content.res.Resources res, android.content.res.XmlResourceParser parser, int flags, boolean useRoundIcon, android.content.pm.parsing.result.ParseInput input) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        boolean visibleToEphemeral;
        boolean setExported;
        final java.lang.String packageName = pkg.getPackageName();
        final android.content.pm.parsing.component.ParsedService service = new android.content.pm.parsing.component.ParsedService();
        java.lang.String tag = parser.getName();
        android.content.res.TypedArray sa = res.obtainAttributes(parser, R.styleable.AndroidManifestService);
        try {
            android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.component.ParsedService> result = android.content.pm.parsing.component.ParsedMainComponentUtils.parseMainComponent(service, tag, separateProcesses, pkg, sa, flags, useRoundIcon, input, R.styleable.AndroidManifestService_banner, R.styleable.AndroidManifestService_description, R.styleable.AndroidManifestService_directBootAware, R.styleable.AndroidManifestService_enabled, R.styleable.AndroidManifestService_icon, R.styleable.AndroidManifestService_label, R.styleable.AndroidManifestService_logo, R.styleable.AndroidManifestService_name, R.styleable.AndroidManifestService_process, R.styleable.AndroidManifestService_roundIcon, R.styleable.AndroidManifestService_splitName);
            if (result.isError()) {
                return result;
            }
            setExported = sa.hasValue(R.styleable.AndroidManifestService_exported);
            if (setExported) {
                service.exported = sa.getBoolean(R.styleable.AndroidManifestService_exported, false);
            }
            java.lang.String permission = sa.getNonConfigurationString(R.styleable.AndroidManifestService_permission, 0);
            service.setPermission(permission != null ? permission : pkg.getPermission());
            service.foregroundServiceType = sa.getInt(R.styleable.AndroidManifestService_foregroundServiceType, android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_NONE);
            service.flags |= (((android.content.pm.parsing.component.ComponentParseUtils.flag(android.content.pm.ServiceInfo.FLAG_STOP_WITH_TASK, R.styleable.AndroidManifestService_stopWithTask, sa) | android.content.pm.parsing.component.ComponentParseUtils.flag(android.content.pm.ServiceInfo.FLAG_ISOLATED_PROCESS, R.styleable.AndroidManifestService_isolatedProcess, sa)) | android.content.pm.parsing.component.ComponentParseUtils.flag(android.content.pm.ServiceInfo.FLAG_EXTERNAL_SERVICE, R.styleable.AndroidManifestService_externalService, sa)) | android.content.pm.parsing.component.ComponentParseUtils.flag(android.content.pm.ServiceInfo.FLAG_USE_APP_ZYGOTE, R.styleable.AndroidManifestService_useAppZygote, sa)) | android.content.pm.parsing.component.ComponentParseUtils.flag(android.content.pm.ServiceInfo.FLAG_SINGLE_USER, R.styleable.AndroidManifestService_singleUser, sa);
            visibleToEphemeral = sa.getBoolean(R.styleable.AndroidManifestService_visibleToInstantApps, false);
            if (visibleToEphemeral) {
                service.flags |= android.content.pm.ActivityInfo.FLAG_VISIBLE_TO_INSTANT_APP;
                pkg.setVisibleToInstantApps(true);
            }
        } finally {
            sa.recycle();
        }
        if (pkg.isCantSaveState()) {
            // A heavy-weight application can not have services in its main process
            // We can do direct compare because we intern all strings.
            if (java.util.Objects.equals(service.getProcessName(), packageName)) {
                return input.error("Heavy-weight applications can not have services " + "in main process");
            }
        }
        final int depth = parser.getDepth();
        int type;
        while (((type = parser.next()) != org.xmlpull.v1.XmlPullParser.END_DOCUMENT) && ((type != org.xmlpull.v1.XmlPullParser.END_TAG) || (parser.getDepth() > depth))) {
            if (type != org.xmlpull.v1.XmlPullParser.START_TAG) {
                continue;
            }
            final android.content.pm.parsing.result.ParseResult parseResult;
            switch (parser.getName()) {
                case "intent-filter" :
                    android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.component.ParsedIntentInfo> intentResult = /* allowGlobs */
                    /* allowAutoVerify */
                    /* allowImplicitEphemeralVisibility */
                    /* failOnNoActions */
                    android.content.pm.parsing.component.ParsedMainComponentUtils.parseIntentFilter(service, pkg, res, parser, visibleToEphemeral, true, false, false, false, input);
                    parseResult = intentResult;
                    if (intentResult.isSuccess()) {
                        android.content.pm.parsing.component.ParsedIntentInfo intent = intentResult.getResult();
                        service.order = java.lang.Math.max(intent.getOrder(), service.order);
                        service.addIntent(intent);
                    }
                    break;
                case "meta-data" :
                    parseResult = android.content.pm.parsing.component.ParsedComponentUtils.addMetaData(service, pkg, res, parser, input);
                    break;
                default :
                    parseResult = android.content.pm.parsing.ParsingUtils.unknownTag(tag, pkg, parser, input);
                    break;
            }
            if (parseResult.isError()) {
                return input.error(parseResult);
            }
        } 
        if (!setExported) {
            service.exported = service.getIntents().size() > 0;
        }
        return input.success(service);
    }
}

