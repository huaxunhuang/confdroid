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
package android.hardware.camera2.legacy;


/**
 * Hold important data necessary to build the camera1 parameters up from a capture request.
 */
public class LegacyRequest {
    /**
     * Immutable characteristics for the camera corresponding to this request
     */
    public final android.hardware.camera2.CameraCharacteristics characteristics;

    /**
     * Immutable capture request, as requested by the user
     */
    public final android.hardware.camera2.CaptureRequest captureRequest;

    /**
     * Immutable api1 preview buffer size at the time of the request
     */
    public final android.util.Size previewSize;

    /**
     * <em>Mutable</em> camera parameters
     */
    public final android.hardware.Camera.Parameters parameters;

    /**
     * Create a new legacy request; the parameters are copied.
     *
     * @param characteristics
     * 		immutable static camera characteristics for this camera
     * @param captureRequest
     * 		immutable user-defined capture request
     * @param previewSize
     * 		immutable internal preview size used for {@link Camera#setPreviewSurface}
     * @param parameters
     * 		the initial camera1 parameter state; (copied) can be mutated
     */
    public LegacyRequest(android.hardware.camera2.CameraCharacteristics characteristics, android.hardware.camera2.CaptureRequest captureRequest, android.util.Size previewSize, android.hardware.Camera.Parameters parameters) {
        this.characteristics = checkNotNull(characteristics, "characteristics must not be null");
        this.captureRequest = checkNotNull(captureRequest, "captureRequest must not be null");
        this.previewSize = checkNotNull(previewSize, "previewSize must not be null");
        checkNotNull(parameters, "parameters must not be null");
        this.parameters = android.hardware.Camera.getParametersCopy(parameters);
    }

    /**
     * Update the current parameters in-place to be a copy of the new parameters.
     *
     * @param parameters
     * 		non-{@code null} parameters for api1 camera
     */
    public void setParameters(android.hardware.Camera.Parameters parameters) {
        checkNotNull(parameters, "parameters must not be null");
        this.parameters.copyFrom(parameters);
    }
}

