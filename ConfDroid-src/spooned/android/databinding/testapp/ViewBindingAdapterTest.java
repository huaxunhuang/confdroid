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


public class ViewBindingAdapterTest extends android.databinding.testapp.BindingAdapterTestBase<android.databinding.testapp.databinding.ViewAdapterTestBinding, android.databinding.testapp.vo.ViewBindingObject> {
    public ViewBindingAdapterTest() {
        super(android.databinding.testapp.databinding.ViewAdapterTestBinding.class, android.databinding.testapp.vo.ViewBindingObject.class, R.layout.view_adapter_test);
    }

    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
        super.setUp();
    }

    public void testPadding() throws java.lang.Throwable {
        android.view.View view = mBinder.padding;
        junit.framework.TestCase.assertEquals(mBindingObject.getPadding(), view.getPaddingBottom());
        junit.framework.TestCase.assertEquals(mBindingObject.getPadding(), view.getPaddingTop());
        junit.framework.TestCase.assertEquals(mBindingObject.getPadding(), view.getPaddingRight());
        junit.framework.TestCase.assertEquals(mBindingObject.getPadding(), view.getPaddingLeft());
        changeValues();
        junit.framework.TestCase.assertEquals(mBindingObject.getPadding(), view.getPaddingBottom());
        junit.framework.TestCase.assertEquals(mBindingObject.getPadding(), view.getPaddingTop());
        junit.framework.TestCase.assertEquals(mBindingObject.getPadding(), view.getPaddingRight());
        junit.framework.TestCase.assertEquals(mBindingObject.getPadding(), view.getPaddingLeft());
    }

    public void testPaddingLeftRight() throws java.lang.Throwable {
        android.view.View view = mBinder.paddingLeftRight;
        junit.framework.TestCase.assertEquals(mBindingObject.getPaddingLeft(), view.getPaddingLeft());
        junit.framework.TestCase.assertEquals(mBindingObject.getPaddingRight(), view.getPaddingRight());
        changeValues();
        junit.framework.TestCase.assertEquals(mBindingObject.getPaddingLeft(), view.getPaddingLeft());
        junit.framework.TestCase.assertEquals(mBindingObject.getPaddingRight(), view.getPaddingRight());
    }

    public void testPaddingStartEnd() throws java.lang.Throwable {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            android.view.View view = mBinder.paddingStartEnd;
            junit.framework.TestCase.assertEquals(mBindingObject.getPaddingStart(), view.getPaddingStart());
            junit.framework.TestCase.assertEquals(mBindingObject.getPaddingEnd(), view.getPaddingEnd());
            changeValues();
            junit.framework.TestCase.assertEquals(mBindingObject.getPaddingStart(), view.getPaddingStart());
            junit.framework.TestCase.assertEquals(mBindingObject.getPaddingEnd(), view.getPaddingEnd());
        }
    }

    public void testPaddingTopBottom() throws java.lang.Throwable {
        android.view.View view = mBinder.paddingTopBottom;
        junit.framework.TestCase.assertEquals(mBindingObject.getPaddingTop(), view.getPaddingTop());
        junit.framework.TestCase.assertEquals(mBindingObject.getPaddingBottom(), view.getPaddingBottom());
        changeValues();
        junit.framework.TestCase.assertEquals(mBindingObject.getPaddingTop(), view.getPaddingTop());
        junit.framework.TestCase.assertEquals(mBindingObject.getPaddingBottom(), view.getPaddingBottom());
    }

    public void testBackgroundTint() throws java.lang.Throwable {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            android.view.View view = mBinder.backgroundTint;
            junit.framework.TestCase.assertNotNull(view.getBackgroundTintList());
            android.content.res.ColorStateList colorStateList = view.getBackgroundTintList();
            junit.framework.TestCase.assertEquals(mBindingObject.getBackgroundTint(), colorStateList.getDefaultColor());
            changeValues();
            junit.framework.TestCase.assertNotNull(view.getBackgroundTintList());
            colorStateList = view.getBackgroundTintList();
            junit.framework.TestCase.assertEquals(mBindingObject.getBackgroundTint(), colorStateList.getDefaultColor());
        }
    }

    public void testFadeScrollbars() throws java.lang.Throwable {
        android.view.View view = mBinder.fadeScrollbars;
        junit.framework.TestCase.assertEquals(mBindingObject.getFadeScrollbars(), view.isScrollbarFadingEnabled());
        changeValues();
        junit.framework.TestCase.assertEquals(mBindingObject.getFadeScrollbars(), view.isScrollbarFadingEnabled());
    }

    public void testNextFocus() throws java.lang.Throwable {
        android.view.View view = mBinder.nextFocus;
        junit.framework.TestCase.assertEquals(mBindingObject.getNextFocusDown(), view.getNextFocusDownId());
        junit.framework.TestCase.assertEquals(mBindingObject.getNextFocusUp(), view.getNextFocusUpId());
        junit.framework.TestCase.assertEquals(mBindingObject.getNextFocusLeft(), view.getNextFocusLeftId());
        junit.framework.TestCase.assertEquals(mBindingObject.getNextFocusRight(), view.getNextFocusRightId());
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            junit.framework.TestCase.assertEquals(mBindingObject.getNextFocusForward(), view.getNextFocusForwardId());
        }
        changeValues();
        junit.framework.TestCase.assertEquals(mBindingObject.getNextFocusDown(), view.getNextFocusDownId());
        junit.framework.TestCase.assertEquals(mBindingObject.getNextFocusUp(), view.getNextFocusUpId());
        junit.framework.TestCase.assertEquals(mBindingObject.getNextFocusLeft(), view.getNextFocusLeftId());
        junit.framework.TestCase.assertEquals(mBindingObject.getNextFocusRight(), view.getNextFocusRightId());
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            junit.framework.TestCase.assertEquals(mBindingObject.getNextFocusForward(), view.getNextFocusForwardId());
        }
    }

    public void testRequiresFadingEdge() throws java.lang.Throwable {
        android.view.View view = mBinder.requiresFadingEdge;
        junit.framework.TestCase.assertTrue(view.isVerticalFadingEdgeEnabled());
        junit.framework.TestCase.assertFalse(view.isHorizontalFadingEdgeEnabled());
        changeValues();
        junit.framework.TestCase.assertFalse(view.isVerticalFadingEdgeEnabled());
        junit.framework.TestCase.assertTrue(view.isHorizontalFadingEdgeEnabled());
    }

    public void testScrollbar() throws java.lang.Throwable {
        android.view.View view = mBinder.scrollbar;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            junit.framework.TestCase.assertEquals(mBindingObject.getScrollbarDefaultDelayBeforeFade(), view.getScrollBarDefaultDelayBeforeFade());
            junit.framework.TestCase.assertEquals(mBindingObject.getScrollbarFadeDuration(), view.getScrollBarFadeDuration());
            junit.framework.TestCase.assertEquals(mBindingObject.getScrollbarSize(), view.getScrollBarSize());
        }
        junit.framework.TestCase.assertEquals(mBindingObject.getScrollbarStyle(), view.getScrollBarStyle());
        changeValues();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            junit.framework.TestCase.assertEquals(mBindingObject.getScrollbarDefaultDelayBeforeFade(), view.getScrollBarDefaultDelayBeforeFade());
            junit.framework.TestCase.assertEquals(mBindingObject.getScrollbarFadeDuration(), view.getScrollBarFadeDuration());
            junit.framework.TestCase.assertEquals(mBindingObject.getScrollbarSize(), view.getScrollBarSize());
        }
        junit.framework.TestCase.assertEquals(mBindingObject.getScrollbarStyle(), view.getScrollBarStyle());
    }

    public void testTransformPivot() throws java.lang.Throwable {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.view.View view = mBinder.transformPivot;
            junit.framework.TestCase.assertEquals(mBindingObject.getTransformPivotX(), view.getPivotX());
            junit.framework.TestCase.assertEquals(mBindingObject.getTransformPivotY(), view.getPivotY());
            changeValues();
            junit.framework.TestCase.assertEquals(mBindingObject.getTransformPivotX(), view.getPivotX());
            junit.framework.TestCase.assertEquals(mBindingObject.getTransformPivotY(), view.getPivotY());
        }
    }

    @android.test.UiThreadTest
    public void testBackgroundDrawableDrawable() throws java.lang.Throwable {
        android.view.View view = mBinder.backgroundDrawable;
        android.graphics.drawable.Drawable drawable = view.getBackground();
        junit.framework.TestCase.assertNotNull(drawable);
    }

    @android.test.UiThreadTest
    public void testBackgroundDrawableWithTheme() throws java.lang.Throwable {
        android.view.View view = mBinder.backgroundWithTheme;
        junit.framework.TestCase.assertNotNull(view.getBackground());
    }
}

