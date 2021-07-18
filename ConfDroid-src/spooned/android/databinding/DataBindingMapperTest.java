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
package android.databinding;


public class DataBindingMapperTest extends android.test.AndroidTestCase {
    public void testNotDataBindingId() {
        android.view.View view = new android.view.View(getContext());
        view.setTag("layout/unexpected");
        android.databinding.DataBinderMapper mapper = new android.databinding.DataBinderMapper();
        android.databinding.ViewDataBinding binding = mapper.getDataBinder(null, view, 1);
        junit.framework.TestCase.assertNull(binding);
    }

    public void testInvalidView() {
        android.view.View view = new android.view.View(getContext());
        view.setTag("layout/unexpected");
        android.databinding.DataBinderMapper mapper = new android.databinding.DataBinderMapper();
        java.lang.Throwable error = null;
        try {
            mapper.getDataBinder(null, view, android.databinding.testapp.R.layout.multi_res_layout);
        } catch (java.lang.Throwable t) {
            error = t;
        }
        junit.framework.TestCase.assertNotNull(error);
        junit.framework.TestCase.assertEquals("The tag for multi_res_layout is invalid. Received: layout/unexpected", error.getMessage());
    }
}

