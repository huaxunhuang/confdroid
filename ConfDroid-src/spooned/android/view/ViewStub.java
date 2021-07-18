/**
 * Copyright (C) 2008 The Android Open Source Project
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
 * A ViewStub is an invisible, zero-sized View that can be used to lazily inflate
 * layout resources at runtime.
 *
 * When a ViewStub is made visible, or when {@link #inflate()}  is invoked, the layout resource
 * is inflated. The ViewStub then replaces itself in its parent with the inflated View or Views.
 * Therefore, the ViewStub exists in the view hierarchy until {@link #setVisibility(int)} or
 * {@link #inflate()} is invoked.
 *
 * The inflated View is added to the ViewStub's parent with the ViewStub's layout
 * parameters. Similarly, you can define/override the inflate View's id by using the
 * ViewStub's inflatedId property. For instance:
 *
 * <pre>
 *     &lt;ViewStub android:id="@+id/stub"
 *               android:inflatedId="@+id/subTree"
 *               android:layout="@layout/mySubTree"
 *               android:layout_width="120dip"
 *               android:layout_height="40dip" /&gt;
 * </pre>
 *
 * The ViewStub thus defined can be found using the id "stub." After inflation of
 * the layout resource "mySubTree," the ViewStub is removed from its parent. The
 * View created by inflating the layout resource "mySubTree" can be found using the
 * id "subTree," specified by the inflatedId property. The inflated View is finally
 * assigned a width of 120dip and a height of 40dip.
 *
 * The preferred way to perform the inflation of the layout resource is the following:
 *
 * <pre>
 *     ViewStub stub = findViewById(R.id.stub);
 *     View inflated = stub.inflate();
 * </pre>
 *
 * When {@link #inflate()} is invoked, the ViewStub is replaced by the inflated View
 * and the inflated View is returned. This lets applications get a reference to the
 * inflated View without executing an extra findViewById().
 *
 * @unknown ref android.R.styleable#ViewStub_inflatedId
 * @unknown ref android.R.styleable#ViewStub_layout
 */
@android.widget.RemoteViews.RemoteView
public final class ViewStub extends android.view.View {
    private int mInflatedId;

    private int mLayoutResource;

    private java.lang.ref.WeakReference<android.view.View> mInflatedViewRef;

    private android.view.LayoutInflater mInflater;

    private android.view.ViewStub.OnInflateListener mInflateListener;

    public ViewStub(android.content.Context context) {
        this(context, 0);
    }

    /**
     * Creates a new ViewStub with the specified layout resource.
     *
     * @param context
     * 		The application's environment.
     * @param layoutResource
     * 		The reference to a layout resource that will be inflated.
     */
    public ViewStub(android.content.Context context, @android.annotation.LayoutRes
    int layoutResource) {
        this(context, null);
        mLayoutResource = layoutResource;
    }

    public ViewStub(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewStub(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public ViewStub(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context);
        final android.content.res.TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ViewStub, defStyleAttr, defStyleRes);
        saveAttributeDataForStyleable(context, R.styleable.ViewStub, attrs, a, defStyleAttr, defStyleRes);
        mInflatedId = a.getResourceId(R.styleable.ViewStub_inflatedId, android.view.View.NO_ID);
        mLayoutResource = a.getResourceId(R.styleable.ViewStub_layout, 0);
        mID = a.getResourceId(R.styleable.ViewStub_id, android.view.View.NO_ID);
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
     * @unknown ref android.R.styleable#ViewStub_inflatedId
     */
    @android.annotation.IdRes
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
     * @unknown ref android.R.styleable#ViewStub_inflatedId
     */
    @android.view.RemotableViewMethod(asyncImpl = "setInflatedIdAsync")
    public void setInflatedId(@android.annotation.IdRes
    int inflatedId) {
        mInflatedId = inflatedId;
    }

    /**
     *
     *
     * @unknown *
     */
    public java.lang.Runnable setInflatedIdAsync(@android.annotation.IdRes
    int inflatedId) {
        mInflatedId = inflatedId;
        return null;
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
     * @unknown ref android.R.styleable#ViewStub_layout
     */
    @android.annotation.LayoutRes
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
     * @unknown ref android.R.styleable#ViewStub_layout
     */
    @android.view.RemotableViewMethod(asyncImpl = "setLayoutResourceAsync")
    public void setLayoutResource(@android.annotation.LayoutRes
    int layoutResource) {
        mLayoutResource = layoutResource;
    }

    /**
     *
     *
     * @unknown *
     */
    public java.lang.Runnable setLayoutResourceAsync(@android.annotation.LayoutRes
    int layoutResource) {
        mLayoutResource = layoutResource;
        return null;
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
    @android.view.RemotableViewMethod(asyncImpl = "setVisibilityAsync")
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
     *
     *
     * @unknown *
     */
    public java.lang.Runnable setVisibilityAsync(int visibility) {
        if ((visibility == android.view.View.VISIBLE) || (visibility == android.view.View.INVISIBLE)) {
            android.view.ViewGroup parent = ((android.view.ViewGroup) (getParent()));
            return new android.view.ViewStub.ViewReplaceRunnable(inflateViewNoAdd(parent));
        } else {
            return null;
        }
    }

    private android.view.View inflateViewNoAdd(android.view.ViewGroup parent) {
        final android.view.LayoutInflater factory;
        if (mInflater != null) {
            factory = mInflater;
        } else {
            factory = android.view.LayoutInflater.from(mContext);
        }
        final android.view.View view = factory.inflate(mLayoutResource, parent, false);
        if (mInflatedId != android.view.View.NO_ID) {
            view.setId(mInflatedId);
        }
        return view;
    }

    private void replaceSelfWithView(android.view.View view, android.view.ViewGroup parent) {
        final int index = parent.indexOfChild(this);
        parent.removeViewInLayout(this);
        final android.view.ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (layoutParams != null) {
            parent.addView(view, index, layoutParams);
        } else {
            parent.addView(view, index);
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
                final android.view.View view = inflateViewNoAdd(parent);
                replaceSelfWithView(view, parent);
                mInflatedViewRef = new java.lang.ref.WeakReference<>(view);
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
    public void setOnInflateListener(android.view.ViewStub.OnInflateListener inflateListener) {
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
        void onInflate(android.view.ViewStub stub, android.view.View inflated);
    }

    /**
     *
     *
     * @unknown *
     */
    public class ViewReplaceRunnable implements java.lang.Runnable {
        public final android.view.View view;

        ViewReplaceRunnable(android.view.View view) {
            this.view = view;
        }

        @java.lang.Override
        public void run() {
            replaceSelfWithView(view, ((android.view.ViewGroup) (getParent())));
        }
    }
}

