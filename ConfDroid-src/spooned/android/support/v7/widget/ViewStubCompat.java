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
package android.support.v7.widget;


/**
 * Backport of {@link android.view.ViewStub} so that we can set the
 * {@link android.view.LayoutInflater} on devices before Jelly Bean.
 *
 * @unknown 
 */
@android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
public final class ViewStubCompat extends android.view.View {
    private int mLayoutResource = 0;

    private int mInflatedId;

    private java.lang.ref.WeakReference<android.view.View> mInflatedViewRef;

    private android.view.LayoutInflater mInflater;

    private android.support.v7.widget.ViewStubCompat.OnInflateListener mInflateListener;

    public ViewStubCompat(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewStubCompat(android.content.Context context, android.util.AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        android.content.res.TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ViewStubCompat, defStyle, 0);
        mInflatedId = a.getResourceId(R.styleable.ViewStubCompat_android_inflatedId, android.view.View.NO_ID);
        mLayoutResource = a.getResourceId(R.styleable.ViewStubCompat_android_layout, 0);
        setId(a.getResourceId(R.styleable.ViewStubCompat_android_id, android.view.View.NO_ID));
        a.recycle();
        setVisibility(android.view.View.GONE);
        setWillNotDraw(true);
    }

    /**
     * Returns the id taken by the inflated view. If the inflated id is
     * {@link View#NO_ID}, the inflated view keeps its original id.
     *
     * @return A positive integer used to identify the inflated view or
    {@link #NO_ID} if the inflated view should keep its id.
     * @see #setInflatedId(int)
     * @unknown name android:inflatedId
     */
    public int getInflatedId() {
        return mInflatedId;
    }

    /**
     * Defines the id taken by the inflated view. If the inflated id is
     * {@link View#NO_ID}, the inflated view keeps its original id.
     *
     * @param inflatedId
     * 		A positive integer used to identify the inflated view or
     * 		{@link #NO_ID} if the inflated view should keep its id.
     * @see #getInflatedId()
     * @unknown name android:inflatedId
     */
    public void setInflatedId(int inflatedId) {
        mInflatedId = inflatedId;
    }

    /**
     * Returns the layout resource that will be used by {@link #setVisibility(int)} or
     * {@link #inflate()} to replace this StubbedView
     * in its parent by another view.
     *
     * @return The layout resource identifier used to inflate the new View.
     * @see #setLayoutResource(int)
     * @see #setVisibility(int)
     * @see #inflate()
     * @unknown name android:layout
     */
    public int getLayoutResource() {
        return mLayoutResource;
    }

    /**
     * Specifies the layout resource to inflate when this StubbedView becomes visible or invisible
     * or when {@link #inflate()} is invoked. The View created by inflating the layout resource is
     * used to replace this StubbedView in its parent.
     *
     * @param layoutResource
     * 		A valid layout resource identifier (different from 0.)
     * @see #getLayoutResource()
     * @see #setVisibility(int)
     * @see #inflate()
     * @unknown name android:layout
     */
    public void setLayoutResource(int layoutResource) {
        mLayoutResource = layoutResource;
    }

    /**
     * Set {@link LayoutInflater} to use in {@link #inflate()}, or {@code null}
     * to use the default.
     */
    public void setLayoutInflater(android.view.LayoutInflater inflater) {
        mInflater = inflater;
    }

    /**
     * Get current {@link LayoutInflater} used in {@link #inflate()}.
     */
    public android.view.LayoutInflater getLayoutInflater() {
        return mInflater;
    }

    @java.lang.Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(0, 0);
    }

    @java.lang.Override
    public void draw(android.graphics.Canvas canvas) {
    }

    @java.lang.Override
    protected void dispatchDraw(android.graphics.Canvas canvas) {
    }

    /**
     * When visibility is set to {@link #VISIBLE} or {@link #INVISIBLE},
     * {@link #inflate()} is invoked and this StubbedView is replaced in its parent
     * by the inflated layout resource. After that calls to this function are passed
     * through to the inflated view.
     *
     * @param visibility
     * 		One of {@link #VISIBLE}, {@link #INVISIBLE}, or {@link #GONE}.
     * @see #inflate()
     */
    @java.lang.Override
    public void setVisibility(int visibility) {
        if (mInflatedViewRef != null) {
            android.view.View view = mInflatedViewRef.get();
            if (view != null) {
                view.setVisibility(visibility);
            } else {
                throw new java.lang.IllegalStateException("setVisibility called on un-referenced view");
            }
        } else {
            super.setVisibility(visibility);
            if ((visibility == android.view.View.VISIBLE) || (visibility == android.view.View.INVISIBLE)) {
                inflate();
            }
        }
    }

    /**
     * Inflates the layout resource identified by {@link #getLayoutResource()}
     * and replaces this StubbedView in its parent by the inflated layout resource.
     *
     * @return The inflated layout resource.
     */
    public android.view.View inflate() {
        final android.view.ViewParent viewParent = getParent();
        if ((viewParent != null) && (viewParent instanceof android.view.ViewGroup)) {
            if (mLayoutResource != 0) {
                final android.view.ViewGroup parent = ((android.view.ViewGroup) (viewParent));
                final android.view.LayoutInflater factory;
                if (mInflater != null) {
                    factory = mInflater;
                } else {
                    factory = android.view.LayoutInflater.from(getContext());
                }
                final android.view.View view = factory.inflate(mLayoutResource, parent, false);
                if (mInflatedId != android.view.View.NO_ID) {
                    view.setId(mInflatedId);
                }
                final int index = parent.indexOfChild(this);
                parent.removeViewInLayout(this);
                final android.view.ViewGroup.LayoutParams layoutParams = getLayoutParams();
                if (layoutParams != null) {
                    parent.addView(view, index, layoutParams);
                } else {
                    parent.addView(view, index);
                }
                mInflatedViewRef = new java.lang.ref.WeakReference<android.view.View>(view);
                if (mInflateListener != null) {
                    mInflateListener.onInflate(this, view);
                }
                return view;
            } else {
                throw new java.lang.IllegalArgumentException("ViewStub must have a valid layoutResource");
            }
        } else {
            throw new java.lang.IllegalStateException("ViewStub must have a non-null ViewGroup viewParent");
        }
    }

    /**
     * Specifies the inflate listener to be notified after this ViewStub successfully
     * inflated its layout resource.
     *
     * @param inflateListener
     * 		The OnInflateListener to notify of successful inflation.
     * @see android.view.ViewStub.OnInflateListener
     */
    public void setOnInflateListener(android.support.v7.widget.ViewStubCompat.OnInflateListener inflateListener) {
        mInflateListener = inflateListener;
    }

    /**
     * Listener used to receive a notification after a ViewStub has successfully
     * inflated its layout resource.
     *
     * @see android.view.ViewStub#setOnInflateListener(android.view.ViewStub.OnInflateListener)
     */
    public static interface OnInflateListener {
        /**
         * Invoked after a ViewStub successfully inflated its layout resource.
         * This method is invoked after the inflated view was added to the
         * hierarchy but before the layout pass.
         *
         * @param stub
         * 		The ViewStub that initiated the inflation.
         * @param inflated
         * 		The inflated View.
         */
        void onInflate(android.support.v7.widget.ViewStubCompat stub, android.view.View inflated);
    }
}

