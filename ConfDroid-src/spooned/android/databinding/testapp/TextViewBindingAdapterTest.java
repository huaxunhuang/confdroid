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


public class TextViewBindingAdapterTest extends android.databinding.testapp.BindingAdapterTestBase<android.databinding.testapp.databinding.TextViewAdapterTestBinding, android.databinding.testapp.vo.TextViewBindingObject> {
    public TextViewBindingAdapterTest() {
        super(android.databinding.testapp.databinding.TextViewAdapterTestBinding.class, android.databinding.testapp.vo.TextViewBindingObject.class, R.layout.text_view_adapter_test);
    }

    public void testNumeric() throws java.lang.Throwable {
        android.widget.TextView view = mBinder.numericText;
        junit.framework.TestCase.assertTrue(view.getKeyListener() instanceof android.text.method.DigitsKeyListener);
        android.text.method.DigitsKeyListener listener = ((android.text.method.DigitsKeyListener) (view.getKeyListener()));
        junit.framework.TestCase.assertEquals(getExpectedNumericType(), listener.getInputType());
        changeValues();
        junit.framework.TestCase.assertTrue(view.getKeyListener() instanceof android.text.method.DigitsKeyListener);
        listener = ((android.text.method.DigitsKeyListener) (view.getKeyListener()));
        junit.framework.TestCase.assertEquals(getExpectedNumericType(), listener.getInputType());
    }

    private int getExpectedNumericType() {
        int expectedType = android.text.InputType.TYPE_CLASS_NUMBER;
        if ((mBindingObject.getNumeric() & android.databinding.adapters.TextViewBindingAdapter.SIGNED) != 0) {
            expectedType |= android.text.InputType.TYPE_NUMBER_FLAG_SIGNED;
        }
        if ((mBindingObject.getNumeric() & android.databinding.adapters.TextViewBindingAdapter.DECIMAL) != 0) {
            expectedType |= android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL;
        }
        return expectedType;
    }

    public void testDrawables() throws java.lang.Throwable {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.widget.TextView view = mBinder.textDrawableNormal;
            junit.framework.TestCase.assertEquals(mBindingObject.getDrawableLeft(), ((android.graphics.drawable.ColorDrawable) (view.getCompoundDrawables()[0])).getColor());
            junit.framework.TestCase.assertEquals(mBindingObject.getDrawableTop(), ((android.graphics.drawable.ColorDrawable) (view.getCompoundDrawables()[1])).getColor());
            junit.framework.TestCase.assertEquals(mBindingObject.getDrawableRight(), ((android.graphics.drawable.ColorDrawable) (view.getCompoundDrawables()[2])).getColor());
            junit.framework.TestCase.assertEquals(mBindingObject.getDrawableBottom(), ((android.graphics.drawable.ColorDrawable) (view.getCompoundDrawables()[3])).getColor());
            changeValues();
            junit.framework.TestCase.assertEquals(mBindingObject.getDrawableLeft(), ((android.graphics.drawable.ColorDrawable) (view.getCompoundDrawables()[0])).getColor());
            junit.framework.TestCase.assertEquals(mBindingObject.getDrawableTop(), ((android.graphics.drawable.ColorDrawable) (view.getCompoundDrawables()[1])).getColor());
            junit.framework.TestCase.assertEquals(mBindingObject.getDrawableRight(), ((android.graphics.drawable.ColorDrawable) (view.getCompoundDrawables()[2])).getColor());
            junit.framework.TestCase.assertEquals(mBindingObject.getDrawableBottom(), ((android.graphics.drawable.ColorDrawable) (view.getCompoundDrawables()[3])).getColor());
        }
    }

    public void testDrawableStartEnd() throws java.lang.Throwable {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            android.widget.TextView view = mBinder.textDrawableStartEnd;
            junit.framework.TestCase.assertEquals(mBindingObject.getDrawableStart(), ((android.graphics.drawable.ColorDrawable) (view.getCompoundDrawablesRelative()[0])).getColor());
            junit.framework.TestCase.assertEquals(mBindingObject.getDrawableEnd(), ((android.graphics.drawable.ColorDrawable) (view.getCompoundDrawablesRelative()[2])).getColor());
            changeValues();
            junit.framework.TestCase.assertEquals(mBindingObject.getDrawableStart(), ((android.graphics.drawable.ColorDrawable) (view.getCompoundDrawablesRelative()[0])).getColor());
            junit.framework.TestCase.assertEquals(mBindingObject.getDrawableEnd(), ((android.graphics.drawable.ColorDrawable) (view.getCompoundDrawablesRelative()[2])).getColor());
        }
    }

    public void testSimpleProperties() throws java.lang.Throwable {
        android.widget.TextView view = mBinder.textView;
        junit.framework.TestCase.assertEquals(mBindingObject.getAutoLink(), view.getAutoLinkMask());
        junit.framework.TestCase.assertEquals(mBindingObject.getDrawablePadding(), view.getCompoundDrawablePadding());
        junit.framework.TestCase.assertEquals(mBindingObject.getTextSize(), view.getTextSize());
        junit.framework.TestCase.assertEquals(mBindingObject.getTextColorHint(), view.getHintTextColors().getDefaultColor());
        junit.framework.TestCase.assertEquals(mBindingObject.getTextColorLink(), view.getLinkTextColors().getDefaultColor());
        junit.framework.TestCase.assertEquals(mBindingObject.isAutoText(), android.databinding.testapp.TextViewBindingAdapterTest.isAutoTextEnabled(view));
        junit.framework.TestCase.assertEquals(mBindingObject.getCapitalize(), android.databinding.testapp.TextViewBindingAdapterTest.getCapitalization(view));
        junit.framework.TestCase.assertEquals(mBindingObject.getImeActionLabel(), view.getImeActionLabel());
        junit.framework.TestCase.assertEquals(mBindingObject.getImeActionId(), view.getImeActionId());
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            junit.framework.TestCase.assertEquals(mBindingObject.getTextColorHighlight(), view.getHighlightColor());
            junit.framework.TestCase.assertEquals(mBindingObject.getLineSpacingExtra(), view.getLineSpacingExtra());
            junit.framework.TestCase.assertEquals(mBindingObject.getLineSpacingMultiplier(), view.getLineSpacingMultiplier());
            junit.framework.TestCase.assertEquals(mBindingObject.getShadowColor(), view.getShadowColor());
            junit.framework.TestCase.assertEquals(mBindingObject.getShadowDx(), view.getShadowDx());
            junit.framework.TestCase.assertEquals(mBindingObject.getShadowDy(), view.getShadowDy());
            junit.framework.TestCase.assertEquals(mBindingObject.getShadowRadius(), view.getShadowRadius());
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                junit.framework.TestCase.assertEquals(mBindingObject.getMaxLength(), android.databinding.testapp.TextViewBindingAdapterTest.getMaxLength(view));
            }
        }
        changeValues();
        junit.framework.TestCase.assertEquals(mBindingObject.getAutoLink(), view.getAutoLinkMask());
        junit.framework.TestCase.assertEquals(mBindingObject.getDrawablePadding(), view.getCompoundDrawablePadding());
        junit.framework.TestCase.assertEquals(mBindingObject.getTextSize(), view.getTextSize());
        junit.framework.TestCase.assertEquals(mBindingObject.getTextColorHint(), view.getHintTextColors().getDefaultColor());
        junit.framework.TestCase.assertEquals(mBindingObject.getTextColorLink(), view.getLinkTextColors().getDefaultColor());
        junit.framework.TestCase.assertEquals(mBindingObject.isAutoText(), android.databinding.testapp.TextViewBindingAdapterTest.isAutoTextEnabled(view));
        junit.framework.TestCase.assertEquals(mBindingObject.getCapitalize(), android.databinding.testapp.TextViewBindingAdapterTest.getCapitalization(view));
        junit.framework.TestCase.assertEquals(mBindingObject.getImeActionLabel(), view.getImeActionLabel());
        junit.framework.TestCase.assertEquals(mBindingObject.getImeActionId(), view.getImeActionId());
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            junit.framework.TestCase.assertEquals(mBindingObject.getTextColorHighlight(), view.getHighlightColor());
            junit.framework.TestCase.assertEquals(mBindingObject.getLineSpacingExtra(), view.getLineSpacingExtra());
            junit.framework.TestCase.assertEquals(mBindingObject.getLineSpacingMultiplier(), view.getLineSpacingMultiplier());
            junit.framework.TestCase.assertEquals(mBindingObject.getShadowColor(), view.getShadowColor());
            junit.framework.TestCase.assertEquals(mBindingObject.getShadowDx(), view.getShadowDx());
            junit.framework.TestCase.assertEquals(mBindingObject.getShadowDy(), view.getShadowDy());
            junit.framework.TestCase.assertEquals(mBindingObject.getShadowRadius(), view.getShadowRadius());
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                junit.framework.TestCase.assertEquals(mBindingObject.getMaxLength(), android.databinding.testapp.TextViewBindingAdapterTest.getMaxLength(view));
            }
        }
        runTestOnUiThread(new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                mBindingObject.setCapitalize(android.text.method.TextKeyListener.Capitalize.CHARACTERS);
                mBinder.executePendingBindings();
            }
        });
        junit.framework.TestCase.assertEquals(mBindingObject.getCapitalize(), android.databinding.testapp.TextViewBindingAdapterTest.getCapitalization(view));
        runTestOnUiThread(new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                mBindingObject.setCapitalize(android.text.method.TextKeyListener.Capitalize.WORDS);
                mBinder.executePendingBindings();
            }
        });
        junit.framework.TestCase.assertEquals(mBindingObject.getCapitalize(), android.databinding.testapp.TextViewBindingAdapterTest.getCapitalization(view));
    }

    private static boolean isAutoTextEnabled(android.widget.TextView view) {
        android.text.method.KeyListener keyListener = view.getKeyListener();
        if (keyListener == null) {
            return false;
        }
        if (!(keyListener instanceof android.text.method.TextKeyListener)) {
            return false;
        }
        android.text.method.TextKeyListener textKeyListener = ((android.text.method.TextKeyListener) (keyListener));
        return (textKeyListener.getInputType() & android.text.InputType.TYPE_TEXT_FLAG_AUTO_CORRECT) != 0;
    }

    private static android.text.method.TextKeyListener.Capitalize getCapitalization(android.widget.TextView view) {
        android.text.method.KeyListener keyListener = view.getKeyListener();
        if (keyListener == null) {
            return android.text.method.TextKeyListener.Capitalize.NONE;
        }
        int inputType = keyListener.getInputType();
        if ((inputType & android.text.InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS) != 0) {
            return android.text.method.TextKeyListener.Capitalize.CHARACTERS;
        } else
            if ((inputType & android.text.InputType.TYPE_TEXT_FLAG_CAP_WORDS) != 0) {
                return android.text.method.TextKeyListener.Capitalize.WORDS;
            } else
                if ((inputType & android.text.InputType.TYPE_TEXT_FLAG_CAP_SENTENCES) != 0) {
                    return android.text.method.TextKeyListener.Capitalize.SENTENCES;
                } else {
                    return android.text.method.TextKeyListener.Capitalize.NONE;
                }


    }

    @android.annotation.TargetApi(android.os.Build.VERSION_CODES.LOLLIPOP)
    private static int getMaxLength(android.widget.TextView view) {
        android.text.InputFilter[] filters = view.getFilters();
        for (android.text.InputFilter filter : filters) {
            if (filter instanceof android.text.InputFilter.LengthFilter) {
                android.text.InputFilter.LengthFilter lengthFilter = ((android.text.InputFilter.LengthFilter) (filter));
                return lengthFilter.getMax();
            }
        }
        return -1;
    }

    public void testAllCaps() throws java.lang.Throwable {
        android.widget.TextView view = mBinder.textAllCaps;
        junit.framework.TestCase.assertEquals(mBindingObject.isTextAllCaps(), view.getTransformationMethod() != null);
        if (view.getTransformationMethod() != null) {
            junit.framework.TestCase.assertEquals("ALL CAPS", view.getTransformationMethod().getTransformation("all caps", view));
        }
        changeValues();
        junit.framework.TestCase.assertEquals(mBindingObject.isTextAllCaps(), view.getTransformationMethod() != null);
        if (view.getTransformationMethod() != null) {
            junit.framework.TestCase.assertEquals("ALL CAPS", view.getTransformationMethod().getTransformation("all caps", view));
        }
    }

    public void testBufferType() throws java.lang.Throwable {
        android.widget.TextView view = mBinder.textBufferType;
        junit.framework.TestCase.assertEquals(mBindingObject.getBufferType(), android.databinding.testapp.TextViewBindingAdapterTest.getBufferType(view));
        changeValues();
        junit.framework.TestCase.assertEquals(mBindingObject.getBufferType(), android.databinding.testapp.TextViewBindingAdapterTest.getBufferType(view));
    }

    private static android.widget.TextView.BufferType getBufferType(android.widget.TextView view) {
        java.lang.CharSequence text = view.getText();
        if (text instanceof android.text.Editable) {
            return android.widget.TextView.BufferType.EDITABLE;
        }
        if (text instanceof android.text.Spannable) {
            return android.widget.TextView.BufferType.SPANNABLE;
        }
        return android.widget.TextView.BufferType.NORMAL;
    }

    public void testInputType() throws java.lang.Throwable {
        android.widget.TextView view = mBinder.textInputType;
        junit.framework.TestCase.assertEquals(mBindingObject.getInputType(), view.getInputType());
        changeValues();
        junit.framework.TestCase.assertEquals(mBindingObject.getInputType(), view.getInputType());
    }

    public void testDigits() throws java.lang.Throwable {
        android.widget.TextView view = mBinder.textDigits;
        junit.framework.TestCase.assertEquals(mBindingObject.getDigits(), android.databinding.testapp.TextViewBindingAdapterTest.getDigits(view));
        changeValues();
        junit.framework.TestCase.assertEquals(mBindingObject.getDigits(), android.databinding.testapp.TextViewBindingAdapterTest.getDigits(view));
    }

    private static java.lang.String getDigits(android.widget.TextView textView) {
        android.text.method.KeyListener keyListener = textView.getKeyListener();
        if (!(keyListener instanceof android.text.method.DigitsKeyListener)) {
            return null;
        }
        android.text.method.DigitsKeyListener digitsKeyListener = ((android.text.method.DigitsKeyListener) (keyListener));
        java.lang.String input = "abcdefghijklmnopqrstuvwxyz";
        android.text.Spannable spannable = android.text.Spannable.Factory.getInstance().newSpannable(input);
        return digitsKeyListener.filter(input, 0, input.length(), spannable, 0, input.length()).toString();
    }

    public void testPhoneNumber() throws java.lang.Throwable {
        android.widget.TextView textView = mBinder.textPhoneNumber;
        junit.framework.TestCase.assertEquals(mBindingObject.isPhoneNumber(), android.databinding.testapp.TextViewBindingAdapterTest.isPhoneNumber(textView));
        changeValues();
        junit.framework.TestCase.assertEquals(mBindingObject.isPhoneNumber(), android.databinding.testapp.TextViewBindingAdapterTest.isPhoneNumber(textView));
    }

    private static boolean isPhoneNumber(android.widget.TextView view) {
        android.text.method.KeyListener keyListener = view.getKeyListener();
        return keyListener instanceof android.text.method.DialerKeyListener;
    }

    public void testInputMethod() throws java.lang.Throwable {
        android.widget.TextView textView = mBinder.textInputMethod;
        junit.framework.TestCase.assertTrue(android.databinding.testapp.vo.TextViewBindingObject.KeyListener1.class.isInstance(textView.getKeyListener()));
        changeValues();
        junit.framework.TestCase.assertTrue(android.databinding.testapp.vo.TextViewBindingObject.KeyListener2.class.isInstance(textView.getKeyListener()));
    }

    @android.test.UiThreadTest
    public void testTextWithTheme() throws java.lang.Throwable {
        android.widget.TextView textView = mBinder.textWithTheme;
        junit.framework.TestCase.assertNotNull(textView.getTextColors());
    }

    @android.test.UiThreadTest
    public void testTextWithColor() throws java.lang.Throwable {
        android.widget.TextView textView = mBinder.textWithColor;
        int expectedColor = mBinder.getRoot().getResources().getColor(android.R.color.holo_blue_bright);
        junit.framework.TestCase.assertEquals(expectedColor, textView.getCurrentTextColor());
    }
}

