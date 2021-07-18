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
package android.databinding.testapp;


public class ImageViewBindingAdapterTest extends android.databinding.testapp.BindingAdapterTestBase<android.databinding.testapp.databinding.ImageViewAdapterTestBinding, android.databinding.testapp.vo.ImageViewBindingObject> {
    android.widget.ImageView mView;

    public ImageViewBindingAdapterTest() {
        super(android.databinding.testapp.databinding.ImageViewAdapterTestBinding.class, android.databinding.testapp.vo.ImageViewBindingObject.class, R.layout.image_view_adapter_test);
    }

    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
        super.setUp();
        mView = mBinder.view;
    }

    public void testImageView() throws java.lang.Throwable {
        junit.framework.TestCase.assertEquals(mBindingObject.getSrc(), mView.getDrawable());
        junit.framework.TestCase.assertEquals(mBindingObject.getTint(), mView.getImageTintList().getDefaultColor());
        junit.framework.TestCase.assertEquals(mBindingObject.getTintMode(), mView.getImageTintMode());
        changeValues();
        junit.framework.TestCase.assertEquals(mBindingObject.getSrc(), mView.getDrawable());
        junit.framework.TestCase.assertEquals(mBindingObject.getTint(), mView.getImageTintList().getDefaultColor());
        junit.framework.TestCase.assertEquals(mBindingObject.getTintMode(), mView.getImageTintMode());
    }

    @android.test.UiThreadTest
    public void testImageSource() throws java.lang.Throwable {
        junit.framework.TestCase.assertNull(mBinder.view2.getDrawable());
        junit.framework.TestCase.assertNull(mBinder.view3.getDrawable());
        java.lang.String uriString = (((android.content.ContentResolver.SCHEME_ANDROID_RESOURCE + "://") + getActivity().getResources().getResourcePackageName(R.drawable.ic_launcher)) + "/") + R.drawable.ic_launcher;
        mBinder.setUriString(uriString);
        mBinder.setUri(android.net.Uri.parse(uriString));
        mBinder.executePendingBindings();
        junit.framework.TestCase.assertNotNull(mBinder.view2.getDrawable());
        junit.framework.TestCase.assertNotNull(mBinder.view3.getDrawable());
    }

    @android.test.UiThreadTest
    public void testConditionalSource() throws java.lang.Throwable {
        mBinder.setObj(null);
        mBinder.executePendingBindings();
        junit.framework.TestCase.assertNotNull(mBinder.view4.getDrawable());
        mBinder.setObj(new android.databinding.testapp.vo.ImageViewBindingObject());
        mBinder.executePendingBindings();
        junit.framework.TestCase.assertNull(mBinder.view4.getDrawable());
    }
}

