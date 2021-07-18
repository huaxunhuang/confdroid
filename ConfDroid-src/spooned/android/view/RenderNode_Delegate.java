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
package android.view;


/**
 * Delegate implementing the native methods of {@link RenderNode}
 * <p/>
 * Through the layoutlib_create tool, some native methods of RenderNode have been replaced by calls
 * to methods of the same name in this delegate class.
 *
 * @see DelegateManager
 */
public class RenderNode_Delegate {
    // ---- delegate manager ----
    private static final com.android.layoutlib.bridge.impl.DelegateManager<android.view.RenderNode_Delegate> sManager = new com.android.layoutlib.bridge.impl.DelegateManager<android.view.RenderNode_Delegate>(android.view.RenderNode_Delegate.class);

    private static long sFinalizer = -1;

    private float mLift;

    private float mTranslationX;

    private float mTranslationY;

    private float mTranslationZ;

    private float mRotation;

    private float mScaleX = 1;

    private float mScaleY = 1;

    private float mPivotX;

    private float mPivotY;

    private boolean mPivotExplicitlySet;

    private int mLeft;

    private int mRight;

    private int mTop;

    private int mBottom;

    @java.lang.SuppressWarnings("UnusedDeclaration")
    private java.lang.String mName;

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static long nCreate(java.lang.String name) {
        android.view.RenderNode_Delegate renderNodeDelegate = new android.view.RenderNode_Delegate();
        renderNodeDelegate.mName = name;
        return android.view.RenderNode_Delegate.sManager.addNewDelegate(renderNodeDelegate);
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static long nGetNativeFinalizer() {
        synchronized(android.view.RenderNode_Delegate.class) {
            if (android.view.RenderNode_Delegate.sFinalizer == (-1)) {
                android.view.RenderNode_Delegate.sFinalizer = libcore.util.NativeAllocationRegistry_Delegate.createFinalizer(android.view.RenderNode_Delegate.sManager::removeJavaReferenceFor);
            }
        }
        return android.view.RenderNode_Delegate.sFinalizer;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static boolean nSetElevation(long renderNode, float lift) {
        android.view.RenderNode_Delegate delegate = android.view.RenderNode_Delegate.sManager.getDelegate(renderNode);
        if ((delegate != null) && (delegate.mLift != lift)) {
            delegate.mLift = lift;
            return true;
        }
        return false;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static float nGetElevation(long renderNode) {
        android.view.RenderNode_Delegate delegate = android.view.RenderNode_Delegate.sManager.getDelegate(renderNode);
        if (delegate != null) {
            return delegate.mLift;
        }
        return 0.0F;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static boolean nSetTranslationX(long renderNode, float translationX) {
        android.view.RenderNode_Delegate delegate = android.view.RenderNode_Delegate.sManager.getDelegate(renderNode);
        if ((delegate != null) && (delegate.mTranslationX != translationX)) {
            delegate.mTranslationX = translationX;
            return true;
        }
        return false;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static float nGetTranslationX(long renderNode) {
        android.view.RenderNode_Delegate delegate = android.view.RenderNode_Delegate.sManager.getDelegate(renderNode);
        if (delegate != null) {
            return delegate.mTranslationX;
        }
        return 0.0F;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static boolean nSetTranslationY(long renderNode, float translationY) {
        android.view.RenderNode_Delegate delegate = android.view.RenderNode_Delegate.sManager.getDelegate(renderNode);
        if ((delegate != null) && (delegate.mTranslationY != translationY)) {
            delegate.mTranslationY = translationY;
            return true;
        }
        return false;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static float nGetTranslationY(long renderNode) {
        android.view.RenderNode_Delegate delegate = android.view.RenderNode_Delegate.sManager.getDelegate(renderNode);
        if (delegate != null) {
            return delegate.mTranslationY;
        }
        return 0.0F;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static boolean nSetTranslationZ(long renderNode, float translationZ) {
        android.view.RenderNode_Delegate delegate = android.view.RenderNode_Delegate.sManager.getDelegate(renderNode);
        if ((delegate != null) && (delegate.mTranslationZ != translationZ)) {
            delegate.mTranslationZ = translationZ;
            return true;
        }
        return false;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static float nGetTranslationZ(long renderNode) {
        android.view.RenderNode_Delegate delegate = android.view.RenderNode_Delegate.sManager.getDelegate(renderNode);
        if (delegate != null) {
            return delegate.mTranslationZ;
        }
        return 0.0F;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static boolean nSetRotation(long renderNode, float rotation) {
        android.view.RenderNode_Delegate delegate = android.view.RenderNode_Delegate.sManager.getDelegate(renderNode);
        if ((delegate != null) && (delegate.mRotation != rotation)) {
            delegate.mRotation = rotation;
            return true;
        }
        return false;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static float nGetRotation(long renderNode) {
        android.view.RenderNode_Delegate delegate = android.view.RenderNode_Delegate.sManager.getDelegate(renderNode);
        if (delegate != null) {
            return delegate.mRotation;
        }
        return 0.0F;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static void getMatrix(android.view.RenderNode renderNode, android.graphics.Matrix outMatrix) {
        outMatrix.reset();
        if (renderNode != null) {
            float rotation = renderNode.getRotation();
            float translationX = renderNode.getTranslationX();
            float translationY = renderNode.getTranslationY();
            float pivotX = renderNode.getPivotX();
            float pivotY = renderNode.getPivotY();
            float scaleX = renderNode.getScaleX();
            float scaleY = renderNode.getScaleY();
            outMatrix.setTranslate(translationX, translationY);
            outMatrix.preRotate(rotation, pivotX, pivotY);
            outMatrix.preScale(scaleX, scaleY, pivotX, pivotY);
        }
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static boolean nSetLeft(long renderNode, int left) {
        android.view.RenderNode_Delegate delegate = android.view.RenderNode_Delegate.sManager.getDelegate(renderNode);
        if ((delegate != null) && (delegate.mLeft != left)) {
            delegate.mLeft = left;
            return true;
        }
        return false;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static boolean nSetTop(long renderNode, int top) {
        android.view.RenderNode_Delegate delegate = android.view.RenderNode_Delegate.sManager.getDelegate(renderNode);
        if ((delegate != null) && (delegate.mTop != top)) {
            delegate.mTop = top;
            return true;
        }
        return false;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static boolean nSetRight(long renderNode, int right) {
        android.view.RenderNode_Delegate delegate = android.view.RenderNode_Delegate.sManager.getDelegate(renderNode);
        if ((delegate != null) && (delegate.mRight != right)) {
            delegate.mRight = right;
            return true;
        }
        return false;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static boolean nSetBottom(long renderNode, int bottom) {
        android.view.RenderNode_Delegate delegate = android.view.RenderNode_Delegate.sManager.getDelegate(renderNode);
        if ((delegate != null) && (delegate.mBottom != bottom)) {
            delegate.mBottom = bottom;
            return true;
        }
        return false;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static boolean nSetLeftTopRightBottom(long renderNode, int left, int top, int right, int bottom) {
        android.view.RenderNode_Delegate delegate = android.view.RenderNode_Delegate.sManager.getDelegate(renderNode);
        if ((delegate != null) && ((((delegate.mLeft != left) || (delegate.mTop != top)) || (delegate.mRight != right)) || (delegate.mBottom != bottom))) {
            delegate.mLeft = left;
            delegate.mTop = top;
            delegate.mRight = right;
            delegate.mBottom = bottom;
            return true;
        }
        return false;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static boolean nIsPivotExplicitlySet(long renderNode) {
        android.view.RenderNode_Delegate delegate = android.view.RenderNode_Delegate.sManager.getDelegate(renderNode);
        return (delegate != null) && delegate.mPivotExplicitlySet;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static boolean nSetPivotX(long renderNode, float pivotX) {
        android.view.RenderNode_Delegate delegate = android.view.RenderNode_Delegate.sManager.getDelegate(renderNode);
        if (delegate != null) {
            delegate.mPivotX = pivotX;
            delegate.mPivotExplicitlySet = true;
            return true;
        }
        return false;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static float nGetPivotX(long renderNode) {
        android.view.RenderNode_Delegate delegate = android.view.RenderNode_Delegate.sManager.getDelegate(renderNode);
        if (delegate != null) {
            if (delegate.mPivotExplicitlySet) {
                return delegate.mPivotX;
            } else {
                return (delegate.mRight - delegate.mLeft) / 2.0F;
            }
        }
        return 0.0F;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static boolean nSetPivotY(long renderNode, float pivotY) {
        android.view.RenderNode_Delegate delegate = android.view.RenderNode_Delegate.sManager.getDelegate(renderNode);
        if (delegate != null) {
            delegate.mPivotY = pivotY;
            delegate.mPivotExplicitlySet = true;
            return true;
        }
        return false;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static float nGetPivotY(long renderNode) {
        android.view.RenderNode_Delegate delegate = android.view.RenderNode_Delegate.sManager.getDelegate(renderNode);
        if (delegate != null) {
            if (delegate.mPivotExplicitlySet) {
                return delegate.mPivotY;
            } else {
                return (delegate.mBottom - delegate.mTop) / 2.0F;
            }
        }
        return 0.0F;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static boolean nSetScaleX(long renderNode, float scaleX) {
        android.view.RenderNode_Delegate delegate = android.view.RenderNode_Delegate.sManager.getDelegate(renderNode);
        if ((delegate != null) && (delegate.mScaleX != scaleX)) {
            delegate.mScaleX = scaleX;
            return true;
        }
        return false;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static float nGetScaleX(long renderNode) {
        android.view.RenderNode_Delegate delegate = android.view.RenderNode_Delegate.sManager.getDelegate(renderNode);
        if (delegate != null) {
            return delegate.mScaleX;
        }
        return 0.0F;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static boolean nSetScaleY(long renderNode, float scaleY) {
        android.view.RenderNode_Delegate delegate = android.view.RenderNode_Delegate.sManager.getDelegate(renderNode);
        if ((delegate != null) && (delegate.mScaleY != scaleY)) {
            delegate.mScaleY = scaleY;
            return true;
        }
        return false;
    }

    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static float nGetScaleY(long renderNode) {
        android.view.RenderNode_Delegate delegate = android.view.RenderNode_Delegate.sManager.getDelegate(renderNode);
        if (delegate != null) {
            return delegate.mScaleY;
        }
        return 0.0F;
    }
}

