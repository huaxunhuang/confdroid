/**
 * Copyright (C) 2015 The Android Open Source Project
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package android.databinding.testapp.multiconfig;


public class LandscapeConfigTest extends android.databinding.testapp.BaseLandDataBinderTest<android.databinding.testapp.databinding.MultiResLayoutBinding> {
    public LandscapeConfigTest() {
        super(android.databinding.testapp.databinding.MultiResLayoutBinding.class, android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    public void testSharedViewIdAndVariableInheritance() throws java.lang.InterruptedException, java.lang.NoSuchFieldException, java.lang.NoSuchMethodException {
        initBinder();
        junit.framework.TestCase.assertEquals("MultiResLayoutBindingLandImpl", mBinder.getClass().getSimpleName());
        assertPublicField(android.widget.TextView.class, "objectInLandTextView");
        assertPublicField(android.widget.TextView.class, "objectInDefaultTextView");
        assertPublicField(android.view.View.class, "objectInDefaultTextView2");
        assertField(android.databinding.testapp.vo.NotBindableVo.class, "mObjectInLand");
        assertField(android.databinding.testapp.vo.NotBindableVo.class, "mObjectInDefault");
        // includes
        assertPublicField(android.databinding.ViewDataBinding.class, "includedLayoutConflict");
        assertPublicField(android.databinding.testapp.databinding.BasicBindingBinding.class, "includedLayoutShared");
        assertPublicField(android.databinding.testapp.databinding.ConditionalBindingBinding.class, "includedLayoutPort");
        assertPublicField(android.databinding.testapp.databinding.ConditionalBindingBinding.class, "includedLayoutLand");
    }

    @android.test.UiThreadTest
    public void testSetVariable() throws java.lang.Throwable {
        initBinder();
        junit.framework.TestCase.assertTrue(mBinder.setVariable(BR.objectInBoth, null));
        junit.framework.TestCase.assertTrue(mBinder.setVariable(BR.objectInDefault, null));
        junit.framework.TestCase.assertTrue(mBinder.setVariable(BR.objectInLand, null));
        junit.framework.TestCase.assertFalse(mBinder.setVariable(BR.obj, null));
    }
}

