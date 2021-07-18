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


public class ProcessBindableTest extends android.databinding.testapp.BaseDataBinderTest<android.databinding.testapp.databinding.BasicBindingBinding> {
    private static java.lang.String[] EXPECTED_BINDING_NAMES = new java.lang.String[]{ "bindableField1", "bindableField2", "bindableField3", "bindableField4", "mbindableField5", "bindableField6", "bindableField7", "bindableField8" };

    public ProcessBindableTest() {
        super(android.databinding.testapp.databinding.BasicBindingBinding.class);
    }

    public void testFieldsGenerated() throws java.lang.IllegalAccessException {
        java.lang.reflect.Field[] fields = android.databinding.testapp.BR.class.getFields();
        android.util.ArrayMap<java.lang.String, java.lang.Integer> fieldValues = new android.util.ArrayMap<>();
        int modifiers = (java.lang.reflect.Modifier.PUBLIC | java.lang.reflect.Modifier.STATIC) | java.lang.reflect.Modifier.FINAL;
        for (java.lang.reflect.Field field : fields) {
            junit.framework.TestCase.assertTrue(field.getModifiers() == modifiers);
            java.lang.String name = field.getName();
            fieldValues.put(name, field.getInt(null));
        }
        junit.framework.TestCase.assertTrue(fieldValues.containsKey("_all"));
        junit.framework.TestCase.assertEquals(0, ((int) (fieldValues.get("_all"))));
        java.util.HashSet<java.lang.Integer> values = new java.util.HashSet<>();
        values.add(0);
        for (java.lang.String fieldName : android.databinding.testapp.ProcessBindableTest.EXPECTED_BINDING_NAMES) {
            junit.framework.TestCase.assertTrue("missing field: " + fieldName, fieldValues.containsKey(fieldName));
            junit.framework.TestCase.assertFalse(values.contains(fieldValues.get(fieldName)));
            values.add(fieldValues.get(fieldName));
        }
    }
}

