/**
 * Copyright (C) 2007 The Android Open Source Project
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
package android.widget;


/**
 * Exercises {@link android.widget.RadioGroup}'s check feature.
 */
public class RadioGroupPreCheckedTest extends android.test.ActivityInstrumentationTestCase2<android.widget.RadioGroupActivity> {
    public RadioGroupPreCheckedTest() {
        super(android.widget.RadioGroupActivity.class);
    }

    @android.test.suitebuilder.annotation.MediumTest
    public void testRadioButtonPreChecked() throws java.lang.Exception {
        final android.widget.RadioGroupActivity activity = getActivity();
        android.widget.RadioButton radio = ((android.widget.RadioButton) (activity.findViewById(R.id.value_one)));
        assertTrue("The first radio button should be checked", radio.isChecked());
        android.widget.RadioGroup group = ((android.widget.RadioGroup) (activity.findViewById(R.id.group)));
        assertEquals("The first radio button should be checked", R.id.value_one, group.getCheckedRadioButtonId());
    }

    @android.test.suitebuilder.annotation.LargeTest
    public void testRadioButtonChangePreChecked() throws java.lang.Exception {
        final android.widget.RadioGroupActivity activity = getActivity();
        android.widget.RadioButton radio = ((android.widget.RadioButton) (activity.findViewById(R.id.value_two)));
        android.test.TouchUtils.clickView(this, radio);
        android.widget.RadioButton old = ((android.widget.RadioButton) (activity.findViewById(R.id.value_one)));
        assertFalse("The first radio button should not be checked", old.isChecked());
        assertTrue("The second radio button should be checked", radio.isChecked());
        android.widget.RadioGroup group = ((android.widget.RadioGroup) (activity.findViewById(R.id.group)));
        assertEquals("The second radio button should be checked", R.id.value_two, group.getCheckedRadioButtonId());
    }
}

