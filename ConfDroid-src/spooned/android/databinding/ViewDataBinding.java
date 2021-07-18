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
package android.databinding;


/**
 * Base class for generated data binding classes. If possible, the generated binding should
 * be instantiated using one of its generated static bind or inflate methods. If the specific
 * binding is unknown, {@link DataBindingUtil#bind(View)} or
 * {@link DataBindingUtil#inflate(LayoutInflater, int, ViewGroup, boolean)} should be used.
 */
public abstract class ViewDataBinding extends android.databinding.BaseObservable {
    /**
     * Instead of directly accessing Build.VERSION.SDK_INT, generated code uses this value so that
     * we can test API dependent behavior.
     */
    static int SDK_INT = android.os.Build.VERSION.SDK_INT;

    private static final int REBIND = 1;

    private static final int HALTED = 2;

    private static final int REBOUND = 3;

    /**
     * Prefix for android:tag on Views with binding. The root View and include tags will not have
     * android:tag attributes and will use ids instead.
     *
     * @unknown 
     */
    public static final java.lang.String BINDING_TAG_PREFIX = "binding_";

    // The length of BINDING_TAG_PREFIX prevents calling length repeatedly.
    private static final int BINDING_NUMBER_START = android.databinding.ViewDataBinding.BINDING_TAG_PREFIX.length();

    // ICS (v 14) fixes a leak when using setTag(int, Object)
    private static final boolean USE_TAG_ID = android.databinding.DataBinderMapper.TARGET_MIN_SDK >= 14;

    private static final boolean USE_CHOREOGRAPHER = android.databinding.ViewDataBinding.SDK_INT >= 16;

    /**
     * Method object extracted out to attach a listener to a bound Observable object.
     */
    private static final android.databinding.ViewDataBinding.CreateWeakListener CREATE_PROPERTY_LISTENER = new android.databinding.ViewDataBinding.CreateWeakListener() {
        @java.lang.Override
        public android.databinding.ViewDataBinding.WeakListener create(android.databinding.ViewDataBinding viewDataBinding, int localFieldId) {
            return new android.databinding.ViewDataBinding.WeakPropertyListener(viewDataBinding, localFieldId).getListener();
        }
    };

    /**
     * Method object extracted out to attach a listener to a bound ObservableList object.
     */
    private static final android.databinding.ViewDataBinding.CreateWeakListener CREATE_LIST_LISTENER = new android.databinding.ViewDataBinding.CreateWeakListener() {
        @java.lang.Override
        public android.databinding.ViewDataBinding.WeakListener create(android.databinding.ViewDataBinding viewDataBinding, int localFieldId) {
            return new android.databinding.ViewDataBinding.WeakListListener(viewDataBinding, localFieldId).getListener();
        }
    };

    /**
     * Method object extracted out to attach a listener to a bound ObservableMap object.
     */
    private static final android.databinding.ViewDataBinding.CreateWeakListener CREATE_MAP_LISTENER = new android.databinding.ViewDataBinding.CreateWeakListener() {
        @java.lang.Override
        public android.databinding.ViewDataBinding.WeakListener create(android.databinding.ViewDataBinding viewDataBinding, int localFieldId) {
            return new android.databinding.ViewDataBinding.WeakMapListener(viewDataBinding, localFieldId).getListener();
        }
    };

    private static final android.databinding.CallbackRegistry.NotifierCallback<android.databinding.OnRebindCallback, android.databinding.ViewDataBinding, java.lang.Void> REBIND_NOTIFIER = new android.databinding.CallbackRegistry.NotifierCallback<android.databinding.OnRebindCallback, android.databinding.ViewDataBinding, java.lang.Void>() {
        @java.lang.Override
        public void onNotifyCallback(android.databinding.OnRebindCallback callback, android.databinding.ViewDataBinding sender, int mode, java.lang.Void arg2) {
            switch (mode) {
                case android.databinding.ViewDataBinding.REBIND :
                    if (!callback.onPreBind(sender)) {
                        sender.mRebindHalted = true;
                    }
                    break;
                case android.databinding.ViewDataBinding.HALTED :
                    callback.onCanceled(sender);
                    break;
                case android.databinding.ViewDataBinding.REBOUND :
                    callback.onBound(sender);
                    break;
            }
        }
    };

    private static final android.view.View.OnAttachStateChangeListener ROOT_REATTACHED_LISTENER;

    static {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.KITKAT) {
            ROOT_REATTACHED_LISTENER = null;
        } else {
            ROOT_REATTACHED_LISTENER = new android.view.View.OnAttachStateChangeListener() {
                @android.annotation.TargetApi(android.os.Build.VERSION_CODES.KITKAT)
                @java.lang.Override
                public void onViewAttachedToWindow(android.view.View v) {
                    // execute the pending bindings.
                    final android.databinding.ViewDataBinding binding = android.databinding.ViewDataBinding.getBinding(v);
                    binding.mRebindRunnable.run();
                    v.removeOnAttachStateChangeListener(this);
                }

                @java.lang.Override
                public void onViewDetachedFromWindow(android.view.View v) {
                }
            };
        }
    }

    /**
     * Runnable executed on animation heartbeat to rebind the dirty Views.
     */
    private final java.lang.Runnable mRebindRunnable = new java.lang.Runnable() {
        @java.lang.Override
        public void run() {
            synchronized(this) {
                mPendingRebind = false;
            }
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                // Nested so that we don't get a lint warning in IntelliJ
                if (!mRoot.isAttachedToWindow()) {
                    // Don't execute the pending bindings until the View
                    // is attached again.
                    mRoot.removeOnAttachStateChangeListener(android.databinding.ViewDataBinding.ROOT_REATTACHED_LISTENER);
                    mRoot.addOnAttachStateChangeListener(android.databinding.ViewDataBinding.ROOT_REATTACHED_LISTENER);
                    return;
                }
            }
            executePendingBindings();
        }
    };

    /**
     * Flag indicates that there are pending bindings that need to be reevaluated.
     */
    private boolean mPendingRebind = false;

    /**
     * Indicates that a onPreBind has stopped the executePendingBindings call.
     */
    private boolean mRebindHalted = false;

    /**
     * The observed expressions.
     */
    private android.databinding.ViewDataBinding.WeakListener[] mLocalFieldObservers;

    /**
     * The root View that this Binding is associated with.
     */
    private final android.view.View mRoot;

    /**
     * The collection of OnRebindCallbacks.
     */
    private android.databinding.CallbackRegistry<android.databinding.OnRebindCallback, android.databinding.ViewDataBinding, java.lang.Void> mRebindCallbacks;

    /**
     * Flag to prevent reentrant executePendingBinding calls.
     */
    private boolean mIsExecutingPendingBindings;

    // null api < 16
    private android.view.Choreographer mChoreographer;

    private final android.view.Choreographer.FrameCallback mFrameCallback;

    // null api >= 16
    private android.os.Handler mUIThreadHandler;

    /**
     * The DataBindingComponent used by this data binding. This is used for BindingAdapters
     * that are instance methods to retrieve the class instance that implements the
     * adapter.
     *
     * @unknown 
     */
    protected final android.databinding.DataBindingComponent mBindingComponent;

    /**
     *
     *
     * @unknown 
     */
    protected ViewDataBinding(android.databinding.DataBindingComponent bindingComponent, android.view.View root, int localFieldCount) {
        mBindingComponent = bindingComponent;
        mLocalFieldObservers = new android.databinding.ViewDataBinding.WeakListener[localFieldCount];
        this.mRoot = root;
        if (android.os.Looper.myLooper() == null) {
            throw new java.lang.IllegalStateException("DataBinding must be created in view's UI Thread");
        }
        if (android.databinding.ViewDataBinding.USE_CHOREOGRAPHER) {
            mChoreographer = android.view.Choreographer.getInstance();
            mFrameCallback = new android.view.Choreographer.FrameCallback() {
                @java.lang.Override
                public void doFrame(long frameTimeNanos) {
                    mRebindRunnable.run();
                }
            };
        } else {
            mFrameCallback = null;
            mUIThreadHandler = new android.os.Handler(android.os.Looper.myLooper());
        }
    }

    /**
     *
     *
     * @unknown 
     */
    protected void setRootTag(android.view.View view) {
        if (android.databinding.ViewDataBinding.USE_TAG_ID) {
            view.setTag(R.id.dataBinding, this);
        } else {
            view.setTag(this);
        }
    }

    /**
     *
     *
     * @unknown 
     */
    protected void setRootTag(android.view.View[] views) {
        if (android.databinding.ViewDataBinding.USE_TAG_ID) {
            for (android.view.View view : views) {
                view.setTag(R.id.dataBinding, this);
            }
        } else {
            for (android.view.View view : views) {
                view.setTag(this);
            }
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public static int getBuildSdkInt() {
        return android.databinding.ViewDataBinding.SDK_INT;
    }

    /**
     * Called when an observed object changes. Sets the appropriate dirty flag if applicable.
     *
     * @param localFieldId
     * 		The index into mLocalFieldObservers that this Object resides in.
     * @param object
     * 		The object that has changed.
     * @param fieldId
     * 		The BR ID of the field being changed or _all if
     * 		no specific field is being notified.
     * @return true if this change should cause a change to the UI.
     * @unknown 
     */
    protected abstract boolean onFieldChange(int localFieldId, java.lang.Object object, int fieldId);

    /**
     * Set a value value in the Binding class.
     * <p>
     * Typically, the developer will be able to call the subclass's set method directly. For
     * example, if there is a variable <code>x</code> in the Binding, a <code>setX</code> method
     * will be generated. However, there are times when the specific subclass of ViewDataBinding
     * is unknown, so the generated method cannot be discovered without reflection. The
     * setVariable call allows the values of variables to be set without reflection.
     *
     * @param variableId
     * 		the BR id of the variable to be set. For example, if the variable is
     * 		<code>x</code>, then variableId will be <code>BR.x</code>.
     * @param value
     * 		The new value of the variable to be set.
     * @return <code>true</code> if the variable is declared or used in the binding or
    <code>false</code> otherwise.
     */
    public abstract boolean setVariable(int variableId, java.lang.Object value);

    /**
     * Add a listener to be called when reevaluating dirty fields. This also allows automatic
     * updates to be halted, but does not stop explicit calls to {@link #executePendingBindings()}.
     *
     * @param listener
     * 		The listener to add.
     */
    public void addOnRebindCallback(android.databinding.OnRebindCallback listener) {
        if (mRebindCallbacks == null) {
            mRebindCallbacks = new android.databinding.CallbackRegistry<android.databinding.OnRebindCallback, android.databinding.ViewDataBinding, java.lang.Void>(android.databinding.ViewDataBinding.REBIND_NOTIFIER);
        }
        mRebindCallbacks.add(listener);
    }

    /**
     * Removes a listener that was added in {@link #addOnRebindCallback(OnRebindCallback)}.
     *
     * @param listener
     * 		The listener to remove.
     */
    public void removeOnRebindCallback(android.databinding.OnRebindCallback listener) {
        if (mRebindCallbacks != null) {
            mRebindCallbacks.remove(listener);
        }
    }

    /**
     * Evaluates the pending bindings, updating any Views that have expressions bound to
     * modified variables. This <b>must</b> be run on the UI thread.
     */
    public void executePendingBindings() {
        if (mIsExecutingPendingBindings) {
            requestRebind();
            return;
        }
        if (!hasPendingBindings()) {
            return;
        }
        mIsExecutingPendingBindings = true;
        mRebindHalted = false;
        if (mRebindCallbacks != null) {
            mRebindCallbacks.notifyCallbacks(this, android.databinding.ViewDataBinding.REBIND, null);
            // The onRebindListeners will change mPendingHalted
            if (mRebindHalted) {
                mRebindCallbacks.notifyCallbacks(this, android.databinding.ViewDataBinding.HALTED, null);
            }
        }
        if (!mRebindHalted) {
            executeBindings();
            if (mRebindCallbacks != null) {
                mRebindCallbacks.notifyCallbacks(this, android.databinding.ViewDataBinding.REBOUND, null);
            }
        }
        mIsExecutingPendingBindings = false;
    }

    void forceExecuteBindings() {
        executeBindings();
    }

    /**
     *
     *
     * @unknown 
     */
    protected abstract void executeBindings();

    /**
     * Invalidates all binding expressions and requests a new rebind to refresh UI.
     */
    public abstract void invalidateAll();

    /**
     * Returns whether the UI needs to be refresh to represent the current data.
     *
     * @return true if any field has changed and the binding should be evaluated.
     */
    public abstract boolean hasPendingBindings();

    /**
     * Removes binding listeners to expression variables.
     */
    public void unbind() {
        for (android.databinding.ViewDataBinding.WeakListener weakListener : mLocalFieldObservers) {
            if (weakListener != null) {
                weakListener.unregister();
            }
        }
    }

    @java.lang.Override
    protected void finalize() throws java.lang.Throwable {
        unbind();
    }

    static android.databinding.ViewDataBinding getBinding(android.view.View v) {
        if (v != null) {
            if (android.databinding.ViewDataBinding.USE_TAG_ID) {
                return ((android.databinding.ViewDataBinding) (v.getTag(R.id.dataBinding)));
            } else {
                final java.lang.Object tag = v.getTag();
                if (tag instanceof android.databinding.ViewDataBinding) {
                    return ((android.databinding.ViewDataBinding) (tag));
                }
            }
        }
        return null;
    }

    /**
     * Returns the outermost View in the layout file associated with the Binding. If this
     * binding is for a merge layout file, this will return the first root in the merge tag.
     *
     * @return the outermost View in the layout file associated with the Binding.
     */
    public android.view.View getRoot() {
        return mRoot;
    }

    private void handleFieldChange(int mLocalFieldId, java.lang.Object object, int fieldId) {
        boolean result = onFieldChange(mLocalFieldId, object, fieldId);
        if (result) {
            requestRebind();
        }
    }

    /**
     *
     *
     * @unknown 
     */
    protected boolean unregisterFrom(int localFieldId) {
        android.databinding.ViewDataBinding.WeakListener listener = mLocalFieldObservers[localFieldId];
        if (listener != null) {
            return listener.unregister();
        }
        return false;
    }

    /**
     *
     *
     * @unknown 
     */
    protected void requestRebind() {
        synchronized(this) {
            if (mPendingRebind) {
                return;
            }
            mPendingRebind = true;
        }
        if (android.databinding.ViewDataBinding.USE_CHOREOGRAPHER) {
            mChoreographer.postFrameCallback(mFrameCallback);
        } else {
            mUIThreadHandler.post(mRebindRunnable);
        }
    }

    /**
     *
     *
     * @unknown 
     */
    protected java.lang.Object getObservedField(int localFieldId) {
        android.databinding.ViewDataBinding.WeakListener listener = mLocalFieldObservers[localFieldId];
        if (listener == null) {
            return null;
        }
        return listener.getTarget();
    }

    private boolean updateRegistration(int localFieldId, java.lang.Object observable, android.databinding.ViewDataBinding.CreateWeakListener listenerCreator) {
        if (observable == null) {
            return unregisterFrom(localFieldId);
        }
        android.databinding.ViewDataBinding.WeakListener listener = mLocalFieldObservers[localFieldId];
        if (listener == null) {
            registerTo(localFieldId, observable, listenerCreator);
            return true;
        }
        if (listener.getTarget() == observable) {
            return false;// nothing to do, same object

        }
        unregisterFrom(localFieldId);
        registerTo(localFieldId, observable, listenerCreator);
        return true;
    }

    /**
     *
     *
     * @unknown 
     */
    protected boolean updateRegistration(int localFieldId, android.databinding.Observable observable) {
        return updateRegistration(localFieldId, observable, android.databinding.ViewDataBinding.CREATE_PROPERTY_LISTENER);
    }

    /**
     *
     *
     * @unknown 
     */
    protected boolean updateRegistration(int localFieldId, android.databinding.ObservableList observable) {
        return updateRegistration(localFieldId, observable, android.databinding.ViewDataBinding.CREATE_LIST_LISTENER);
    }

    /**
     *
     *
     * @unknown 
     */
    protected boolean updateRegistration(int localFieldId, android.databinding.ObservableMap observable) {
        return updateRegistration(localFieldId, observable, android.databinding.ViewDataBinding.CREATE_MAP_LISTENER);
    }

    /**
     *
     *
     * @unknown 
     */
    protected void ensureBindingComponentIsNotNull(java.lang.Class<?> oneExample) {
        if (mBindingComponent == null) {
            java.lang.String errorMessage = (((((("Required DataBindingComponent is null in class " + getClass().getSimpleName()) + ". A BindingAdapter in ") + oneExample.getCanonicalName()) + " is not static and requires an object to use, retrieved from the ") + "DataBindingComponent. If you don't use an inflation method taking a ") + "DataBindingComponent, use DataBindingUtil.setDefaultComponent or ") + "make all BindingAdapter methods static.";
            throw new java.lang.IllegalStateException(errorMessage);
        }
    }

    /**
     *
     *
     * @unknown 
     */
    protected void registerTo(int localFieldId, java.lang.Object observable, android.databinding.ViewDataBinding.CreateWeakListener listenerCreator) {
        if (observable == null) {
            return;
        }
        android.databinding.ViewDataBinding.WeakListener listener = mLocalFieldObservers[localFieldId];
        if (listener == null) {
            listener = listenerCreator.create(this, localFieldId);
            mLocalFieldObservers[localFieldId] = listener;
        }
        listener.setTarget(observable);
    }

    /**
     *
     *
     * @unknown 
     */
    protected static android.databinding.ViewDataBinding bind(android.databinding.DataBindingComponent bindingComponent, android.view.View view, int layoutId) {
        return android.databinding.DataBindingUtil.bind(bindingComponent, view, layoutId);
    }

    /**
     * Walks the view hierarchy under root and pulls out tagged Views, includes, and views with
     * IDs into an Object[] that is returned. This is used to walk the view hierarchy once to find
     * all bound and ID'd views.
     *
     * @param bindingComponent
     * 		The binding component to use with this binding.
     * @param root
     * 		The root of the view hierarchy to walk.
     * @param numBindings
     * 		The total number of ID'd views, views with expressions, and includes
     * @param includes
     * 		The include layout information, indexed by their container's index.
     * @param viewsWithIds
     * 		Indexes of views that don't have tags, but have IDs.
     * @return An array of size numBindings containing all Views in the hierarchy that have IDs
    (with elements in viewsWithIds), are tagged containing expressions, or the bindings for
    included layouts.
     * @unknown 
     */
    protected static java.lang.Object[] mapBindings(android.databinding.DataBindingComponent bindingComponent, android.view.View root, int numBindings, android.databinding.ViewDataBinding.IncludedLayouts includes, android.util.SparseIntArray viewsWithIds) {
        java.lang.Object[] bindings = new java.lang.Object[numBindings];
        android.databinding.ViewDataBinding.mapBindings(bindingComponent, root, bindings, includes, viewsWithIds, true);
        return bindings;
    }

    /**
     *
     *
     * @unknown 
     */
    protected int getColorFromResource(int resourceId) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            return getRoot().getContext().getColor(resourceId);
        } else {
            return getRoot().getResources().getColor(resourceId);
        }
    }

    /**
     *
     *
     * @unknown 
     */
    protected android.content.res.ColorStateList getColorStateListFromResource(int resourceId) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            return getRoot().getContext().getColorStateList(resourceId);
        } else {
            return getRoot().getResources().getColorStateList(resourceId);
        }
    }

    /**
     *
     *
     * @unknown 
     */
    protected android.graphics.drawable.Drawable getDrawableFromResource(int resourceId) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            return getRoot().getContext().getDrawable(resourceId);
        } else {
            return getRoot().getResources().getDrawable(resourceId);
        }
    }

    /**
     *
     *
     * @unknown 
     */
    protected static <T> T getFromArray(T[] arr, int index) {
        if (((arr == null) || (index < 0)) || (index >= arr.length)) {
            return null;
        }
        return arr[index];
    }

    /**
     *
     *
     * @unknown 
     */
    protected static <T> void setTo(T[] arr, int index, T value) {
        if (((arr == null) || (index < 0)) || (index >= arr.length)) {
            return;
        }
        arr[index] = value;
    }

    /**
     *
     *
     * @unknown 
     */
    protected static boolean getFromArray(boolean[] arr, int index) {
        if (((arr == null) || (index < 0)) || (index >= arr.length)) {
            return false;
        }
        return arr[index];
    }

    /**
     *
     *
     * @unknown 
     */
    protected static void setTo(boolean[] arr, int index, boolean value) {
        if (((arr == null) || (index < 0)) || (index >= arr.length)) {
            return;
        }
        arr[index] = value;
    }

    /**
     *
     *
     * @unknown 
     */
    protected static byte getFromArray(byte[] arr, int index) {
        if (((arr == null) || (index < 0)) || (index >= arr.length)) {
            return 0;
        }
        return arr[index];
    }

    /**
     *
     *
     * @unknown 
     */
    protected static void setTo(byte[] arr, int index, byte value) {
        if (((arr == null) || (index < 0)) || (index >= arr.length)) {
            return;
        }
        arr[index] = value;
    }

    /**
     *
     *
     * @unknown 
     */
    protected static short getFromArray(short[] arr, int index) {
        if (((arr == null) || (index < 0)) || (index >= arr.length)) {
            return 0;
        }
        return arr[index];
    }

    /**
     *
     *
     * @unknown 
     */
    protected static void setTo(short[] arr, int index, short value) {
        if (((arr == null) || (index < 0)) || (index >= arr.length)) {
            return;
        }
        arr[index] = value;
    }

    /**
     *
     *
     * @unknown 
     */
    protected static char getFromArray(char[] arr, int index) {
        if (((arr == null) || (index < 0)) || (index >= arr.length)) {
            return 0;
        }
        return arr[index];
    }

    /**
     *
     *
     * @unknown 
     */
    protected static void setTo(char[] arr, int index, char value) {
        if (((arr == null) || (index < 0)) || (index >= arr.length)) {
            return;
        }
        arr[index] = value;
    }

    /**
     *
     *
     * @unknown 
     */
    protected static int getFromArray(int[] arr, int index) {
        if (((arr == null) || (index < 0)) || (index >= arr.length)) {
            return 0;
        }
        return arr[index];
    }

    /**
     *
     *
     * @unknown 
     */
    protected static void setTo(int[] arr, int index, int value) {
        if (((arr == null) || (index < 0)) || (index >= arr.length)) {
            return;
        }
        arr[index] = value;
    }

    /**
     *
     *
     * @unknown 
     */
    protected static long getFromArray(long[] arr, int index) {
        if (((arr == null) || (index < 0)) || (index >= arr.length)) {
            return 0;
        }
        return arr[index];
    }

    /**
     *
     *
     * @unknown 
     */
    protected static void setTo(long[] arr, int index, long value) {
        if (((arr == null) || (index < 0)) || (index >= arr.length)) {
            return;
        }
        arr[index] = value;
    }

    /**
     *
     *
     * @unknown 
     */
    protected static float getFromArray(float[] arr, int index) {
        if (((arr == null) || (index < 0)) || (index >= arr.length)) {
            return 0;
        }
        return arr[index];
    }

    /**
     *
     *
     * @unknown 
     */
    protected static void setTo(float[] arr, int index, float value) {
        if (((arr == null) || (index < 0)) || (index >= arr.length)) {
            return;
        }
        arr[index] = value;
    }

    /**
     *
     *
     * @unknown 
     */
    protected static double getFromArray(double[] arr, int index) {
        if (((arr == null) || (index < 0)) || (index >= arr.length)) {
            return 0;
        }
        return arr[index];
    }

    /**
     *
     *
     * @unknown 
     */
    protected static void setTo(double[] arr, int index, double value) {
        if (((arr == null) || (index < 0)) || (index >= arr.length)) {
            return;
        }
        arr[index] = value;
    }

    /**
     *
     *
     * @unknown 
     */
    protected static <T> T getFromList(java.util.List<T> list, int index) {
        if (((list == null) || (index < 0)) || (index >= list.size())) {
            return null;
        }
        return list.get(index);
    }

    /**
     *
     *
     * @unknown 
     */
    protected static <T> void setTo(java.util.List<T> list, int index, T value) {
        if (((list == null) || (index < 0)) || (index >= list.size())) {
            return;
        }
        list.set(index, value);
    }

    /**
     *
     *
     * @unknown 
     */
    protected static <T> T getFromList(android.util.SparseArray<T> list, int index) {
        if ((list == null) || (index < 0)) {
            return null;
        }
        return list.get(index);
    }

    /**
     *
     *
     * @unknown 
     */
    protected static <T> void setTo(android.util.SparseArray<T> list, int index, T value) {
        if (((list == null) || (index < 0)) || (index >= list.size())) {
            return;
        }
        list.put(index, value);
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.TargetApi(android.os.Build.VERSION_CODES.JELLY_BEAN)
    protected static <T> T getFromList(android.util.LongSparseArray<T> list, int index) {
        if ((list == null) || (index < 0)) {
            return null;
        }
        return list.get(index);
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.TargetApi(android.os.Build.VERSION_CODES.JELLY_BEAN)
    protected static <T> void setTo(android.util.LongSparseArray<T> list, int index, T value) {
        if (((list == null) || (index < 0)) || (index >= list.size())) {
            return;
        }
        list.put(index, value);
    }

    /**
     *
     *
     * @unknown 
     */
    protected static <T> T getFromList(android.support.v4.util.LongSparseArray<T> list, int index) {
        if ((list == null) || (index < 0)) {
            return null;
        }
        return list.get(index);
    }

    /**
     *
     *
     * @unknown 
     */
    protected static <T> void setTo(android.support.v4.util.LongSparseArray<T> list, int index, T value) {
        if (((list == null) || (index < 0)) || (index >= list.size())) {
            return;
        }
        list.put(index, value);
    }

    /**
     *
     *
     * @unknown 
     */
    protected static boolean getFromList(android.util.SparseBooleanArray list, int index) {
        if ((list == null) || (index < 0)) {
            return false;
        }
        return list.get(index);
    }

    /**
     *
     *
     * @unknown 
     */
    protected static void setTo(android.util.SparseBooleanArray list, int index, boolean value) {
        if (((list == null) || (index < 0)) || (index >= list.size())) {
            return;
        }
        list.put(index, value);
    }

    /**
     *
     *
     * @unknown 
     */
    protected static int getFromList(android.util.SparseIntArray list, int index) {
        if ((list == null) || (index < 0)) {
            return 0;
        }
        return list.get(index);
    }

    /**
     *
     *
     * @unknown 
     */
    protected static void setTo(android.util.SparseIntArray list, int index, int value) {
        if (((list == null) || (index < 0)) || (index >= list.size())) {
            return;
        }
        list.put(index, value);
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.TargetApi(android.os.Build.VERSION_CODES.JELLY_BEAN_MR2)
    protected static long getFromList(android.util.SparseLongArray list, int index) {
        if ((list == null) || (index < 0)) {
            return 0;
        }
        return list.get(index);
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.TargetApi(android.os.Build.VERSION_CODES.JELLY_BEAN_MR2)
    protected static void setTo(android.util.SparseLongArray list, int index, long value) {
        if (((list == null) || (index < 0)) || (index >= list.size())) {
            return;
        }
        list.put(index, value);
    }

    /**
     *
     *
     * @unknown 
     */
    protected static <K, T> T getFrom(java.util.Map<K, T> map, K key) {
        if (map == null) {
            return null;
        }
        return map.get(key);
    }

    /**
     *
     *
     * @unknown 
     */
    protected static <K, T> void setTo(java.util.Map<K, T> map, K key, T value) {
        if (map == null) {
            return;
        }
        map.put(key, value);
    }

    /**
     *
     *
     * @unknown 
     */
    protected static void setBindingInverseListener(android.databinding.ViewDataBinding binder, android.databinding.InverseBindingListener oldListener, android.databinding.ViewDataBinding.PropertyChangedInverseListener listener) {
        if (oldListener != listener) {
            if (oldListener != null) {
                binder.removeOnPropertyChangedCallback(((android.databinding.ViewDataBinding.PropertyChangedInverseListener) (oldListener)));
            }
            if (listener != null) {
                binder.addOnPropertyChangedCallback(listener);
            }
        }
    }

    /**
     * Walks the view hierarchy under roots and pulls out tagged Views, includes, and views with
     * IDs into an Object[] that is returned. This is used to walk the view hierarchy once to find
     * all bound and ID'd views.
     *
     * @param bindingComponent
     * 		The binding component to use with this binding.
     * @param roots
     * 		The root Views of the view hierarchy to walk. This is used with merge tags.
     * @param numBindings
     * 		The total number of ID'd views, views with expressions, and includes
     * @param includes
     * 		The include layout information, indexed by their container's index.
     * @param viewsWithIds
     * 		Indexes of views that don't have tags, but have IDs.
     * @return An array of size numBindings containing all Views in the hierarchy that have IDs
    (with elements in viewsWithIds), are tagged containing expressions, or the bindings for
    included layouts.
     * @unknown 
     */
    protected static java.lang.Object[] mapBindings(android.databinding.DataBindingComponent bindingComponent, android.view.View[] roots, int numBindings, android.databinding.ViewDataBinding.IncludedLayouts includes, android.util.SparseIntArray viewsWithIds) {
        java.lang.Object[] bindings = new java.lang.Object[numBindings];
        for (int i = 0; i < roots.length; i++) {
            android.databinding.ViewDataBinding.mapBindings(bindingComponent, roots[i], bindings, includes, viewsWithIds, true);
        }
        return bindings;
    }

    private static void mapBindings(android.databinding.DataBindingComponent bindingComponent, android.view.View view, java.lang.Object[] bindings, android.databinding.ViewDataBinding.IncludedLayouts includes, android.util.SparseIntArray viewsWithIds, boolean isRoot) {
        final int indexInIncludes;
        final android.databinding.ViewDataBinding existingBinding = android.databinding.ViewDataBinding.getBinding(view);
        if (existingBinding != null) {
            return;
        }
        final java.lang.String tag = ((java.lang.String) (view.getTag()));
        boolean isBound = false;
        if ((isRoot && (tag != null)) && tag.startsWith("layout")) {
            final int underscoreIndex = tag.lastIndexOf('_');
            if ((underscoreIndex > 0) && android.databinding.ViewDataBinding.isNumeric(tag, underscoreIndex + 1)) {
                final int index = android.databinding.ViewDataBinding.parseTagInt(tag, underscoreIndex + 1);
                if (bindings[index] == null) {
                    bindings[index] = view;
                }
                indexInIncludes = (includes == null) ? -1 : index;
                isBound = true;
            } else {
                indexInIncludes = -1;
            }
        } else
            if ((tag != null) && tag.startsWith(android.databinding.ViewDataBinding.BINDING_TAG_PREFIX)) {
                int tagIndex = android.databinding.ViewDataBinding.parseTagInt(tag, android.databinding.ViewDataBinding.BINDING_NUMBER_START);
                if (bindings[tagIndex] == null) {
                    bindings[tagIndex] = view;
                }
                isBound = true;
                indexInIncludes = (includes == null) ? -1 : tagIndex;
            } else {
                // Not a bound view
                indexInIncludes = -1;
            }

        if (!isBound) {
            final int id = view.getId();
            if (id > 0) {
                int index;
                if (((viewsWithIds != null) && ((index = viewsWithIds.get(id, -1)) >= 0)) && (bindings[index] == null)) {
                    bindings[index] = view;
                }
            }
        }
        if (view instanceof android.view.ViewGroup) {
            final android.view.ViewGroup viewGroup = ((android.view.ViewGroup) (view));
            final int count = viewGroup.getChildCount();
            int minInclude = 0;
            for (int i = 0; i < count; i++) {
                final android.view.View child = viewGroup.getChildAt(i);
                boolean isInclude = false;
                if (indexInIncludes >= 0) {
                    java.lang.String childTag = ((java.lang.String) (child.getTag()));
                    if ((((childTag != null) && childTag.endsWith("_0")) && childTag.startsWith("layout")) && (childTag.indexOf('/') > 0)) {
                        // This *could* be an include. Test against the expected includes.
                        int includeIndex = android.databinding.ViewDataBinding.findIncludeIndex(childTag, minInclude, includes, indexInIncludes);
                        if (includeIndex >= 0) {
                            isInclude = true;
                            minInclude = includeIndex + 1;
                            final int index = includes.indexes[indexInIncludes][includeIndex];
                            final int layoutId = includes.layoutIds[indexInIncludes][includeIndex];
                            int lastMatchingIndex = android.databinding.ViewDataBinding.findLastMatching(viewGroup, i);
                            if (lastMatchingIndex == i) {
                                bindings[index] = android.databinding.DataBindingUtil.bind(bindingComponent, child, layoutId);
                            } else {
                                final int includeCount = (lastMatchingIndex - i) + 1;
                                final android.view.View[] included = new android.view.View[includeCount];
                                for (int j = 0; j < includeCount; j++) {
                                    included[j] = viewGroup.getChildAt(i + j);
                                }
                                bindings[index] = android.databinding.DataBindingUtil.bind(bindingComponent, included, layoutId);
                                i += includeCount - 1;
                            }
                        }
                    }
                }
                if (!isInclude) {
                    android.databinding.ViewDataBinding.mapBindings(bindingComponent, child, bindings, includes, viewsWithIds, false);
                }
            }
        }
    }

    private static int findIncludeIndex(java.lang.String tag, int minInclude, android.databinding.ViewDataBinding.IncludedLayouts included, int includedIndex) {
        final int slashIndex = tag.indexOf('/');
        final java.lang.CharSequence layoutName = tag.subSequence(slashIndex + 1, tag.length() - 2);
        final java.lang.String[] layouts = included.layouts[includedIndex];
        final int length = layouts.length;
        for (int i = minInclude; i < length; i++) {
            final java.lang.String layout = layouts[i];
            if (android.text.TextUtils.equals(layoutName, layout)) {
                return i;
            }
        }
        return -1;
    }

    private static int findLastMatching(android.view.ViewGroup viewGroup, int firstIncludedIndex) {
        final android.view.View firstView = viewGroup.getChildAt(firstIncludedIndex);
        final java.lang.String firstViewTag = ((java.lang.String) (firstView.getTag()));
        final java.lang.String tagBase = firstViewTag.substring(0, firstViewTag.length() - 1);// don't include the "0"

        final int tagSequenceIndex = tagBase.length();
        final int count = viewGroup.getChildCount();
        int max = firstIncludedIndex;
        for (int i = firstIncludedIndex + 1; i < count; i++) {
            final android.view.View view = viewGroup.getChildAt(i);
            final java.lang.String tag = ((java.lang.String) (view.getTag()));
            if ((tag != null) && tag.startsWith(tagBase)) {
                if ((tag.length() == firstViewTag.length()) && (tag.charAt(tag.length() - 1) == '0')) {
                    return max;// Found another instance of the include

                }
                if (android.databinding.ViewDataBinding.isNumeric(tag, tagSequenceIndex)) {
                    max = i;
                }
            }
        }
        return max;
    }

    private static boolean isNumeric(java.lang.String tag, int startIndex) {
        int length = tag.length();
        if (length == startIndex) {
            return false;// no numerals

        }
        for (int i = startIndex; i < length; i++) {
            if (!java.lang.Character.isDigit(tag.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Parse the tag without creating a new String object. This is fast and assumes the
     * tag is in the correct format.
     *
     * @param str
     * 		The tag string.
     * @return The binding tag number parsed from the tag string.
     */
    private static int parseTagInt(java.lang.String str, int startIndex) {
        final int end = str.length();
        int val = 0;
        for (int i = startIndex; i < end; i++) {
            val *= 10;
            char c = str.charAt(i);
            val += c - '0';
        }
        return val;
    }

    private interface ObservableReference<T> {
        android.databinding.ViewDataBinding.WeakListener<T> getListener();

        void addListener(T target);

        void removeListener(T target);
    }

    private static class WeakListener<T> extends java.lang.ref.WeakReference<android.databinding.ViewDataBinding> {
        private final android.databinding.ViewDataBinding.ObservableReference<T> mObservable;

        protected final int mLocalFieldId;

        private T mTarget;

        public WeakListener(android.databinding.ViewDataBinding binder, int localFieldId, android.databinding.ViewDataBinding.ObservableReference<T> observable) {
            super(binder);
            mLocalFieldId = localFieldId;
            mObservable = observable;
        }

        public void setTarget(T object) {
            unregister();
            mTarget = object;
            if (mTarget != null) {
                mObservable.addListener(mTarget);
            }
        }

        public boolean unregister() {
            boolean unregistered = false;
            if (mTarget != null) {
                mObservable.removeListener(mTarget);
                unregistered = true;
            }
            mTarget = null;
            return unregistered;
        }

        public T getTarget() {
            return mTarget;
        }

        protected android.databinding.ViewDataBinding getBinder() {
            android.databinding.ViewDataBinding binder = get();
            if (binder == null) {
                unregister();// The binder is dead

            }
            return binder;
        }
    }

    private static class WeakPropertyListener extends android.databinding.Observable.OnPropertyChangedCallback implements android.databinding.ViewDataBinding.ObservableReference<android.databinding.Observable> {
        final android.databinding.ViewDataBinding.WeakListener<android.databinding.Observable> mListener;

        public WeakPropertyListener(android.databinding.ViewDataBinding binder, int localFieldId) {
            mListener = new android.databinding.ViewDataBinding.WeakListener<android.databinding.Observable>(binder, localFieldId, this);
        }

        @java.lang.Override
        public android.databinding.ViewDataBinding.WeakListener<android.databinding.Observable> getListener() {
            return mListener;
        }

        @java.lang.Override
        public void addListener(android.databinding.Observable target) {
            target.addOnPropertyChangedCallback(this);
        }

        @java.lang.Override
        public void removeListener(android.databinding.Observable target) {
            target.removeOnPropertyChangedCallback(this);
        }

        @java.lang.Override
        public void onPropertyChanged(android.databinding.Observable sender, int propertyId) {
            android.databinding.ViewDataBinding binder = mListener.getBinder();
            if (binder == null) {
                return;
            }
            android.databinding.Observable obj = mListener.getTarget();
            if (obj != sender) {
                return;// notification from the wrong object?

            }
            binder.handleFieldChange(mListener.mLocalFieldId, sender, propertyId);
        }
    }

    private static class WeakListListener extends android.databinding.ObservableList.OnListChangedCallback implements android.databinding.ViewDataBinding.ObservableReference<android.databinding.ObservableList> {
        final android.databinding.ViewDataBinding.WeakListener<android.databinding.ObservableList> mListener;

        public WeakListListener(android.databinding.ViewDataBinding binder, int localFieldId) {
            mListener = new android.databinding.ViewDataBinding.WeakListener<android.databinding.ObservableList>(binder, localFieldId, this);
        }

        @java.lang.Override
        public android.databinding.ViewDataBinding.WeakListener<android.databinding.ObservableList> getListener() {
            return mListener;
        }

        @java.lang.Override
        public void addListener(android.databinding.ObservableList target) {
            target.addOnListChangedCallback(this);
        }

        @java.lang.Override
        public void removeListener(android.databinding.ObservableList target) {
            target.removeOnListChangedCallback(this);
        }

        @java.lang.Override
        public void onChanged(android.databinding.ObservableList sender) {
            android.databinding.ViewDataBinding binder = mListener.getBinder();
            if (binder == null) {
                return;
            }
            android.databinding.ObservableList target = mListener.getTarget();
            if (target != sender) {
                return;// We expect notifications only from sender

            }
            binder.handleFieldChange(mListener.mLocalFieldId, target, 0);
        }

        @java.lang.Override
        public void onItemRangeChanged(android.databinding.ObservableList sender, int positionStart, int itemCount) {
            onChanged(sender);
        }

        @java.lang.Override
        public void onItemRangeInserted(android.databinding.ObservableList sender, int positionStart, int itemCount) {
            onChanged(sender);
        }

        @java.lang.Override
        public void onItemRangeMoved(android.databinding.ObservableList sender, int fromPosition, int toPosition, int itemCount) {
            onChanged(sender);
        }

        @java.lang.Override
        public void onItemRangeRemoved(android.databinding.ObservableList sender, int positionStart, int itemCount) {
            onChanged(sender);
        }
    }

    private static class WeakMapListener extends android.databinding.ObservableMap.OnMapChangedCallback implements android.databinding.ViewDataBinding.ObservableReference<android.databinding.ObservableMap> {
        final android.databinding.ViewDataBinding.WeakListener<android.databinding.ObservableMap> mListener;

        public WeakMapListener(android.databinding.ViewDataBinding binder, int localFieldId) {
            mListener = new android.databinding.ViewDataBinding.WeakListener<android.databinding.ObservableMap>(binder, localFieldId, this);
        }

        @java.lang.Override
        public android.databinding.ViewDataBinding.WeakListener<android.databinding.ObservableMap> getListener() {
            return mListener;
        }

        @java.lang.Override
        public void addListener(android.databinding.ObservableMap target) {
            target.addOnMapChangedCallback(this);
        }

        @java.lang.Override
        public void removeListener(android.databinding.ObservableMap target) {
            target.removeOnMapChangedCallback(this);
        }

        @java.lang.Override
        public void onMapChanged(android.databinding.ObservableMap sender, java.lang.Object key) {
            android.databinding.ViewDataBinding binder = mListener.getBinder();
            if ((binder == null) || (sender != mListener.getTarget())) {
                return;
            }
            binder.handleFieldChange(mListener.mLocalFieldId, sender, 0);
        }
    }

    private interface CreateWeakListener {
        android.databinding.ViewDataBinding.WeakListener create(android.databinding.ViewDataBinding viewDataBinding, int localFieldId);
    }

    /**
     * This class is used by generated subclasses of {@link ViewDataBinding} to track the
     * included layouts contained in the bound layout. This class is an implementation
     * detail of how binding expressions are mapped to Views after inflation.
     *
     * @unknown 
     */
    protected static class IncludedLayouts {
        public final java.lang.String[][] layouts;

        public final int[][] indexes;

        public final int[][] layoutIds;

        public IncludedLayouts(int bindingCount) {
            layouts = new java.lang.String[bindingCount][];
            indexes = new int[bindingCount][];
            layoutIds = new int[bindingCount][];
        }

        public void setIncludes(int index, java.lang.String[] layouts, int[] indexes, int[] layoutIds) {
            this.layouts[index] = layouts;
            this.indexes[index] = indexes;
            this.layoutIds[index] = layoutIds;
        }
    }

    /**
     * This class is used by generated subclasses of {@link ViewDataBinding} to listen for
     * changes on variables of Bindings. This is important for two-way data binding on variables
     * in included Bindings.
     *
     * @unknown 
     */
    protected static abstract class PropertyChangedInverseListener extends android.databinding.Observable.OnPropertyChangedCallback implements android.databinding.InverseBindingListener {
        final int mPropertyId;

        public PropertyChangedInverseListener(int propertyId) {
            mPropertyId = propertyId;
        }

        @java.lang.Override
        public void onPropertyChanged(android.databinding.Observable sender, int propertyId) {
            if ((propertyId == mPropertyId) || (propertyId == 0)) {
                onChange();
            }
        }
    }
}

