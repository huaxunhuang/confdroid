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
package android.databinding.adapters;


@android.databinding.BindingMethods({ @android.databinding.BindingMethod(type = android.widget.TextView.class, attribute = "android:autoLink", method = "setAutoLinkMask"), @android.databinding.BindingMethod(type = android.widget.TextView.class, attribute = "android:drawablePadding", method = "setCompoundDrawablePadding"), @android.databinding.BindingMethod(type = android.widget.TextView.class, attribute = "android:editorExtras", method = "setInputExtras"), @android.databinding.BindingMethod(type = android.widget.TextView.class, attribute = "android:inputType", method = "setRawInputType"), @android.databinding.BindingMethod(type = android.widget.TextView.class, attribute = "android:scrollHorizontally", method = "setHorizontallyScrolling"), @android.databinding.BindingMethod(type = android.widget.TextView.class, attribute = "android:textAllCaps", method = "setAllCaps"), @android.databinding.BindingMethod(type = android.widget.TextView.class, attribute = "android:textColorHighlight", method = "setHighlightColor"), @android.databinding.BindingMethod(type = android.widget.TextView.class, attribute = "android:textColorHint", method = "setHintTextColor"), @android.databinding.BindingMethod(type = android.widget.TextView.class, attribute = "android:textColorLink", method = "setLinkTextColor"), @android.databinding.BindingMethod(type = android.widget.TextView.class, attribute = "android:onEditorAction", method = "setOnEditorActionListener") })
public class TextViewBindingAdapter {
    private static final java.lang.String TAG = "TextViewBindingAdapters";

    public static final int INTEGER = 0x1;

    public static final int SIGNED = 0x3;

    public static final int DECIMAL = 0x5;

    @android.databinding.BindingAdapter("android:text")
    public static void setText(android.widget.TextView view, java.lang.CharSequence text) {
        final java.lang.CharSequence oldText = view.getText();
        if ((text == oldText) || ((text == null) && (oldText.length() == 0))) {
            return;
        }
        if (text instanceof android.text.Spanned) {
            if (text.equals(oldText)) {
                return;// No change in the spans, so don't set anything.

            }
        } else
            if (!android.databinding.adapters.TextViewBindingAdapter.haveContentsChanged(text, oldText)) {
                return;// No content changes, so don't set anything.

            }

        view.setText(text);
    }

    @android.databinding.InverseBindingAdapter(attribute = "android:text", event = "android:textAttrChanged")
    public static java.lang.String getTextString(android.widget.TextView view) {
        return view.getText().toString();
    }

    @android.databinding.BindingAdapter({ "android:autoText" })
    public static void setAutoText(android.widget.TextView view, boolean autoText) {
        android.text.method.KeyListener listener = view.getKeyListener();
        android.text.method.TextKeyListener.Capitalize capitalize = android.text.method.TextKeyListener.Capitalize.NONE;
        int inputType = (listener != null) ? listener.getInputType() : 0;
        if ((inputType & android.text.InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS) != 0) {
            capitalize = android.text.method.TextKeyListener.Capitalize.CHARACTERS;
        } else
            if ((inputType & android.text.InputType.TYPE_TEXT_FLAG_CAP_WORDS) != 0) {
                capitalize = android.text.method.TextKeyListener.Capitalize.WORDS;
            } else
                if ((inputType & android.text.InputType.TYPE_TEXT_FLAG_CAP_SENTENCES) != 0) {
                    capitalize = android.text.method.TextKeyListener.Capitalize.SENTENCES;
                }


        view.setKeyListener(android.text.method.TextKeyListener.getInstance(autoText, capitalize));
    }

    @android.databinding.BindingAdapter({ "android:capitalize" })
    public static void setCapitalize(android.widget.TextView view, android.text.method.TextKeyListener.Capitalize capitalize) {
        android.text.method.KeyListener listener = view.getKeyListener();
        int inputType = listener.getInputType();
        boolean autoText = (inputType & android.text.InputType.TYPE_TEXT_FLAG_AUTO_CORRECT) != 0;
        view.setKeyListener(android.text.method.TextKeyListener.getInstance(autoText, capitalize));
    }

    @android.databinding.BindingAdapter({ "android:bufferType" })
    public static void setBufferType(android.widget.TextView view, android.widget.TextView.BufferType bufferType) {
        view.setText(view.getText(), bufferType);
    }

    @android.databinding.BindingAdapter({ "android:digits" })
    public static void setDigits(android.widget.TextView view, java.lang.CharSequence digits) {
        if (digits != null) {
            view.setKeyListener(android.text.method.DigitsKeyListener.getInstance(digits.toString()));
        } else
            if (view.getKeyListener() instanceof android.text.method.DigitsKeyListener) {
                view.setKeyListener(null);
            }

    }

    @android.databinding.BindingAdapter({ "android:numeric" })
    public static void setNumeric(android.widget.TextView view, int numeric) {
        view.setKeyListener(android.text.method.DigitsKeyListener.getInstance((numeric & android.databinding.adapters.TextViewBindingAdapter.SIGNED) != 0, (numeric & android.databinding.adapters.TextViewBindingAdapter.DECIMAL) != 0));
    }

    @android.databinding.BindingAdapter({ "android:phoneNumber" })
    public static void setPhoneNumber(android.widget.TextView view, boolean phoneNumber) {
        if (phoneNumber) {
            view.setKeyListener(android.text.method.DialerKeyListener.getInstance());
        } else
            if (view.getKeyListener() instanceof android.text.method.DialerKeyListener) {
                view.setKeyListener(null);
            }

    }

    private static void setIntrinsicBounds(android.graphics.drawable.Drawable drawable) {
        if (drawable != null) {
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        }
    }

    @android.databinding.BindingAdapter({ "android:drawableBottom" })
    public static void setDrawableBottom(android.widget.TextView view, android.graphics.drawable.Drawable drawable) {
        android.databinding.adapters.TextViewBindingAdapter.setIntrinsicBounds(drawable);
        android.graphics.drawable.Drawable[] drawables = view.getCompoundDrawables();
        view.setCompoundDrawables(drawables[0], drawables[1], drawables[2], drawable);
    }

    @android.databinding.BindingAdapter({ "android:drawableLeft" })
    public static void setDrawableLeft(android.widget.TextView view, android.graphics.drawable.Drawable drawable) {
        android.databinding.adapters.TextViewBindingAdapter.setIntrinsicBounds(drawable);
        android.graphics.drawable.Drawable[] drawables = view.getCompoundDrawables();
        view.setCompoundDrawables(drawable, drawables[1], drawables[2], drawables[3]);
    }

    @android.databinding.BindingAdapter({ "android:drawableRight" })
    public static void setDrawableRight(android.widget.TextView view, android.graphics.drawable.Drawable drawable) {
        android.databinding.adapters.TextViewBindingAdapter.setIntrinsicBounds(drawable);
        android.graphics.drawable.Drawable[] drawables = view.getCompoundDrawables();
        view.setCompoundDrawables(drawables[0], drawables[1], drawable, drawables[3]);
    }

    @android.databinding.BindingAdapter({ "android:drawableTop" })
    public static void setDrawableTop(android.widget.TextView view, android.graphics.drawable.Drawable drawable) {
        android.databinding.adapters.TextViewBindingAdapter.setIntrinsicBounds(drawable);
        android.graphics.drawable.Drawable[] drawables = view.getCompoundDrawables();
        view.setCompoundDrawables(drawables[0], drawable, drawables[2], drawables[3]);
    }

    @android.databinding.BindingAdapter({ "android:drawableStart" })
    public static void setDrawableStart(android.widget.TextView view, android.graphics.drawable.Drawable drawable) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            android.databinding.adapters.TextViewBindingAdapter.setDrawableLeft(view, drawable);
        } else {
            android.databinding.adapters.TextViewBindingAdapter.setIntrinsicBounds(drawable);
            android.graphics.drawable.Drawable[] drawables = view.getCompoundDrawablesRelative();
            view.setCompoundDrawablesRelative(drawable, drawables[1], drawables[2], drawables[3]);
        }
    }

    @android.databinding.BindingAdapter({ "android:drawableEnd" })
    public static void setDrawableEnd(android.widget.TextView view, android.graphics.drawable.Drawable drawable) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            android.databinding.adapters.TextViewBindingAdapter.setDrawableRight(view, drawable);
        } else {
            android.databinding.adapters.TextViewBindingAdapter.setIntrinsicBounds(drawable);
            android.graphics.drawable.Drawable[] drawables = view.getCompoundDrawablesRelative();
            view.setCompoundDrawablesRelative(drawables[0], drawables[1], drawable, drawables[3]);
        }
    }

    @android.databinding.BindingAdapter({ "android:imeActionLabel" })
    public static void setImeActionLabel(android.widget.TextView view, java.lang.CharSequence value) {
        view.setImeActionLabel(value, view.getImeActionId());
    }

    @android.databinding.BindingAdapter({ "android:imeActionId" })
    public static void setImeActionLabel(android.widget.TextView view, int value) {
        view.setImeActionLabel(view.getImeActionLabel(), value);
    }

    @android.databinding.BindingAdapter({ "android:inputMethod" })
    public static void setInputMethod(android.widget.TextView view, java.lang.CharSequence inputMethod) {
        try {
            java.lang.Class<?> c = java.lang.Class.forName(inputMethod.toString());
            view.setKeyListener(((android.text.method.KeyListener) (c.newInstance())));
        } catch (java.lang.ClassNotFoundException e) {
            android.util.Log.e(android.databinding.adapters.TextViewBindingAdapter.TAG, "Could not create input method: " + inputMethod, e);
        } catch (java.lang.InstantiationException e) {
            android.util.Log.e(android.databinding.adapters.TextViewBindingAdapter.TAG, "Could not create input method: " + inputMethod, e);
        } catch (java.lang.IllegalAccessException e) {
            android.util.Log.e(android.databinding.adapters.TextViewBindingAdapter.TAG, "Could not create input method: " + inputMethod, e);
        }
    }

    @android.databinding.BindingAdapter({ "android:lineSpacingExtra" })
    public static void setLineSpacingExtra(android.widget.TextView view, float value) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            view.setLineSpacing(value, view.getLineSpacingMultiplier());
        } else {
            view.setLineSpacing(value, 1);
        }
    }

    @android.databinding.BindingAdapter({ "android:lineSpacingMultiplier" })
    public static void setLineSpacingMultiplier(android.widget.TextView view, float value) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            view.setLineSpacing(view.getLineSpacingExtra(), value);
        } else {
            view.setLineSpacing(0, value);
        }
    }

    @android.databinding.BindingAdapter({ "android:maxLength" })
    public static void setMaxLength(android.widget.TextView view, int value) {
        android.text.InputFilter[] filters = view.getFilters();
        if (filters == null) {
            filters = new android.text.InputFilter[]{ new android.text.InputFilter.LengthFilter(value) };
        } else {
            boolean foundMaxLength = false;
            for (int i = 0; i < filters.length; i++) {
                android.text.InputFilter filter = filters[i];
                if (filter instanceof android.text.InputFilter.LengthFilter) {
                    foundMaxLength = true;
                    boolean replace = true;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        replace = ((android.text.InputFilter.LengthFilter) (filter)).getMax() != value;
                    }
                    if (replace) {
                        filters[i] = new android.text.InputFilter.LengthFilter(value);
                    }
                    break;
                }
            }
            if (!foundMaxLength) {
                // can't use Arrays.copyOf -- it shows up in API 9
                android.text.InputFilter[] oldFilters = filters;
                filters = new android.text.InputFilter[oldFilters.length + 1];
                java.lang.System.arraycopy(oldFilters, 0, filters, 0, oldFilters.length);
                filters[filters.length - 1] = new android.text.InputFilter.LengthFilter(value);
            }
        }
        view.setFilters(filters);
    }

    @android.databinding.BindingAdapter({ "android:password" })
    public static void setPassword(android.widget.TextView view, boolean password) {
        if (password) {
            view.setTransformationMethod(android.text.method.PasswordTransformationMethod.getInstance());
        } else
            if (view.getTransformationMethod() instanceof android.text.method.PasswordTransformationMethod) {
                view.setTransformationMethod(null);
            }

    }

    @android.databinding.BindingAdapter({ "android:shadowColor" })
    public static void setShadowColor(android.widget.TextView view, int color) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            float dx = view.getShadowDx();
            float dy = view.getShadowDy();
            float r = view.getShadowRadius();
            view.setShadowLayer(r, dx, dy, color);
        }
    }

    @android.databinding.BindingAdapter({ "android:shadowDx" })
    public static void setShadowDx(android.widget.TextView view, float dx) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            int color = view.getShadowColor();
            float dy = view.getShadowDy();
            float r = view.getShadowRadius();
            view.setShadowLayer(r, dx, dy, color);
        }
    }

    @android.databinding.BindingAdapter({ "android:shadowDy" })
    public static void setShadowDy(android.widget.TextView view, float dy) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            int color = view.getShadowColor();
            float dx = view.getShadowDx();
            float r = view.getShadowRadius();
            view.setShadowLayer(r, dx, dy, color);
        }
    }

    @android.databinding.BindingAdapter({ "android:shadowRadius" })
    public static void setShadowRadius(android.widget.TextView view, float r) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            int color = view.getShadowColor();
            float dx = view.getShadowDx();
            float dy = view.getShadowDy();
            view.setShadowLayer(r, dx, dy, color);
        }
    }

    @android.databinding.BindingAdapter({ "android:textSize" })
    public static void setTextSize(android.widget.TextView view, float size) {
        view.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, size);
    }

    private static boolean haveContentsChanged(java.lang.CharSequence str1, java.lang.CharSequence str2) {
        if ((str1 == null) != (str2 == null)) {
            return true;
        } else
            if (str1 == null) {
                return false;
            }

        final int length = str1.length();
        if (length != str2.length()) {
            return true;
        }
        for (int i = 0; i < length; i++) {
            if (str1.charAt(i) != str2.charAt(i)) {
                return true;
            }
        }
        return false;
    }

    @android.databinding.BindingAdapter(value = { "android:beforeTextChanged", "android:onTextChanged", "android:afterTextChanged", "android:textAttrChanged" }, requireAll = false)
    public static void setTextWatcher(android.widget.TextView view, final android.databinding.adapters.TextViewBindingAdapter.BeforeTextChanged before, final android.databinding.adapters.TextViewBindingAdapter.OnTextChanged on, final android.databinding.adapters.TextViewBindingAdapter.AfterTextChanged after, final android.databinding.InverseBindingListener textAttrChanged) {
        final android.text.TextWatcher newValue;
        if ((((before == null) && (after == null)) && (on == null)) && (textAttrChanged == null)) {
            newValue = null;
        } else {
            newValue = new android.text.TextWatcher() {
                @java.lang.Override
                public void beforeTextChanged(java.lang.CharSequence s, int start, int count, int after) {
                    if (before != null) {
                        before.beforeTextChanged(s, start, count, after);
                    }
                }

                @java.lang.Override
                public void onTextChanged(java.lang.CharSequence s, int start, int before, int count) {
                    if (on != null) {
                        on.onTextChanged(s, start, before, count);
                    }
                    if (textAttrChanged != null) {
                        textAttrChanged.onChange();
                    }
                }

                @java.lang.Override
                public void afterTextChanged(android.text.Editable s) {
                    if (after != null) {
                        after.afterTextChanged(s);
                    }
                }
            };
        }
        final android.text.TextWatcher oldValue = android.databinding.adapters.ListenerUtil.trackListener(view, newValue, R.id.textWatcher);
        if (oldValue != null) {
            view.removeTextChangedListener(oldValue);
        }
        if (newValue != null) {
            view.addTextChangedListener(newValue);
        }
    }

    public interface AfterTextChanged {
        void afterTextChanged(android.text.Editable s);
    }

    public interface BeforeTextChanged {
        void beforeTextChanged(java.lang.CharSequence s, int start, int count, int after);
    }

    public interface OnTextChanged {
        void onTextChanged(java.lang.CharSequence s, int start, int before, int count);
    }
}

