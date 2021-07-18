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
package android.content.pm;


/**
 * Information needed to make an instant application resolution request.
 *
 * @unknown 
 */
public final class InstantAppRequest {
    /**
     * Response from the first phase of instant application resolution
     */
    public final android.content.pm.AuxiliaryResolveInfo responseObj;

    /**
     * The original intent that triggered instant application resolution
     */
    public final android.content.Intent origIntent;

    /**
     * Resolved type of the intent
     */
    public final java.lang.String resolvedType;

    /**
     * The name of the package requesting the instant application
     */
    public final java.lang.String callingPackage;

    /**
     * ID of the user requesting the instant application
     */
    public final int userId;

    /**
     * Optional extra bundle provided by the source application to the installer for additional
     * verification.
     */
    public final android.os.Bundle verificationBundle;

    /**
     * Whether resolution occurs because an application is starting
     */
    public final boolean resolveForStart;

    /**
     * The instant app digest for this request
     */
    public final android.content.pm.InstantAppResolveInfo.InstantAppDigest digest;

    public InstantAppRequest(android.content.pm.AuxiliaryResolveInfo responseObj, android.content.Intent origIntent, java.lang.String resolvedType, java.lang.String callingPackage, int userId, android.os.Bundle verificationBundle, boolean resolveForStart) {
        this.responseObj = responseObj;
        this.origIntent = origIntent;
        this.resolvedType = resolvedType;
        this.callingPackage = callingPackage;
        this.userId = userId;
        this.verificationBundle = verificationBundle;
        this.resolveForStart = resolveForStart;
        if ((origIntent.getData() != null) && (!android.text.TextUtils.isEmpty(origIntent.getData().getHost()))) {
            digest = /* maxDigests */
            new android.content.pm.InstantAppResolveInfo.InstantAppDigest(origIntent.getData().getHost(), 5);
        } else {
            digest = android.content.pm.InstantAppResolveInfo.InstantAppDigest.UNDEFINED;
        }
    }
}

