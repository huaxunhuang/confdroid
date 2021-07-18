/**
 * Copyright (C) 2011 The Android Open Source Project
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
package android.media.filterfw.samples;


public class CameraEffectsRecordingSample extends android.app.Activity {
    private android.widget.Button mRunButton;

    private android.view.SurfaceView mCameraView;

    private android.filterfw.core.GraphRunner mRunner;

    private int mCameraId = 0;

    private java.lang.String mOutFileName = android.os.Environment.getExternalStorageDirectory().toString() + "/CameraEffectsRecordingSample.mp4";

    public void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mRunButton = ((android.widget.Button) (findViewById(R.id.runbutton)));
        mCameraView = ((android.view.SurfaceView) (findViewById(R.id.cameraview)));
        mRunButton.setOnClickListener(mRunButtonClick);
        android.content.Intent intent = getIntent();
        if (intent.hasExtra("OUTPUT_FILENAME")) {
            mOutFileName = intent.getStringExtra("OUTPUT_FILENAME");
        }
        // Set up the references and load the filter graph
        createGraph();
    }

    @java.lang.Override
    public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        switch (keyCode) {
            case android.view.KeyEvent.KEYCODE_CAMERA :
                mRunButton.performClick();
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void createGraph() {
        android.graphics.Bitmap sourceBitmap = android.graphics.BitmapFactory.decodeResource(getResources(), R.drawable.android);
        android.filterfw.GraphEnvironment graphEnvironment = new android.filterfw.GraphEnvironment();
        graphEnvironment.createGLEnvironment();
        graphEnvironment.addReferences("cameraView", mCameraView);
        graphEnvironment.addReferences("cameraId", mCameraId);
        graphEnvironment.addReferences("outputFileName", mOutFileName);
        int graphId = graphEnvironment.loadGraph(this, R.raw.cameraeffectsrecordingsample);
        mRunner = graphEnvironment.getRunner(graphId, android.filterfw.GraphEnvironment.MODE_ASYNCHRONOUS);
    }

    protected void onPause() {
        super.onPause();
        if (mRunner.isRunning()) {
            mRunner.stop();
            mRunButton.setText("Record");
        }
    }

    private android.view.View.OnClickListener mRunButtonClick = new android.view.View.OnClickListener() {
        @java.lang.Override
        public void onClick(android.view.View v) {
            if (mRunner.isRunning()) {
                mRunner.stop();
                mRunButton.setText("Record");
            } else {
                mRunner.run();
                mRunButton.setText("Stop");
            }
        }
    };
}

