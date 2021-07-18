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
 * A class that describes a view hierarchy that can be displayed in
 * another process. The hierarchy is inflated from a layout resource
 * file, and this class provides some basic operations for modifying
 * the content of the inflated hierarchy.
 *
 * <p>{@code RemoteViews} is limited to support for the following layouts:</p>
 * <ul>
 *   <li>{@link android.widget.AdapterViewFlipper}</li>
 *   <li>{@link android.widget.FrameLayout}</li>
 *   <li>{@link android.widget.GridLayout}</li>
 *   <li>{@link android.widget.GridView}</li>
 *   <li>{@link android.widget.LinearLayout}</li>
 *   <li>{@link android.widget.ListView}</li>
 *   <li>{@link android.widget.RelativeLayout}</li>
 *   <li>{@link android.widget.StackView}</li>
 *   <li>{@link android.widget.ViewFlipper}</li>
 * </ul>
 * <p>And the following widgets:</p>
 * <ul>
 *   <li>{@link android.widget.AnalogClock}</li>
 *   <li>{@link android.widget.Button}</li>
 *   <li>{@link android.widget.Chronometer}</li>
 *   <li>{@link android.widget.ImageButton}</li>
 *   <li>{@link android.widget.ImageView}</li>
 *   <li>{@link android.widget.ProgressBar}</li>
 *   <li>{@link android.widget.TextClock}</li>
 *   <li>{@link android.widget.TextView}</li>
 * </ul>
 * <p>Descendants of these classes are not supported.</p>
 */
public class RemoteViews implements android.os.Parcelable , android.view.LayoutInflater.Filter {
    private static final java.lang.String LOG_TAG = "RemoteViews";

    /**
     * The intent extra that contains the appWidgetId.
     *
     * @unknown 
     */
    static final java.lang.String EXTRA_REMOTEADAPTER_APPWIDGET_ID = "remoteAdapterAppWidgetId";

    /**
     * The intent extra that contains {@code true} if inflating as dak text theme.
     *
     * @unknown 
     */
    static final java.lang.String EXTRA_REMOTEADAPTER_ON_LIGHT_BACKGROUND = "remoteAdapterOnLightBackground";

    /**
     * The intent extra that contains the bounds for all shared elements.
     */
    public static final java.lang.String EXTRA_SHARED_ELEMENT_BOUNDS = "android.widget.extra.SHARED_ELEMENT_BOUNDS";

    /**
     * Maximum depth of nested views calls from {@link #addView(int, RemoteViews)} and
     * {@link #RemoteViews(RemoteViews, RemoteViews)}.
     */
    private static final int MAX_NESTED_VIEWS = 10;

    // The unique identifiers for each custom {@link Action}.
    private static final int SET_ON_CLICK_RESPONSE_TAG = 1;

    private static final int REFLECTION_ACTION_TAG = 2;

    private static final int SET_DRAWABLE_TINT_TAG = 3;

    private static final int VIEW_GROUP_ACTION_ADD_TAG = 4;

    private static final int VIEW_CONTENT_NAVIGATION_TAG = 5;

    private static final int SET_EMPTY_VIEW_ACTION_TAG = 6;

    private static final int VIEW_GROUP_ACTION_REMOVE_TAG = 7;

    private static final int SET_PENDING_INTENT_TEMPLATE_TAG = 8;

    private static final int SET_REMOTE_VIEW_ADAPTER_INTENT_TAG = 10;

    private static final int TEXT_VIEW_DRAWABLE_ACTION_TAG = 11;

    private static final int BITMAP_REFLECTION_ACTION_TAG = 12;

    private static final int TEXT_VIEW_SIZE_ACTION_TAG = 13;

    private static final int VIEW_PADDING_ACTION_TAG = 14;

    private static final int SET_REMOTE_VIEW_ADAPTER_LIST_TAG = 15;

    private static final int SET_REMOTE_INPUTS_ACTION_TAG = 18;

    private static final int LAYOUT_PARAM_ACTION_TAG = 19;

    private static final int OVERRIDE_TEXT_COLORS_TAG = 20;

    private static final int SET_RIPPLE_DRAWABLE_COLOR_TAG = 21;

    private static final int SET_INT_TAG_TAG = 22;

    /**
     *
     *
     * @unknown *
     */
    @android.annotation.IntDef(flag = true, value = { android.widget.RemoteViews.FLAG_REAPPLY_DISALLOWED, android.widget.RemoteViews.FLAG_WIDGET_IS_COLLECTION_CHILD, android.widget.RemoteViews.FLAG_USE_LIGHT_BACKGROUND_LAYOUT })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface ApplyFlags {}

    /**
     * Whether reapply is disallowed on this remoteview. This maybe be true if some actions modify
     * the layout in a way that isn't recoverable, since views are being removed.
     *
     * @unknown 
     */
    public static final int FLAG_REAPPLY_DISALLOWED = 1;

    /**
     * This flag indicates whether this RemoteViews object is being created from a
     * RemoteViewsService for use as a child of a widget collection. This flag is used
     * to determine whether or not certain features are available, in particular,
     * setting on click extras and setting on click pending intents. The former is enabled,
     * and the latter disabled when this flag is true.
     *
     * @unknown 
     */
    public static final int FLAG_WIDGET_IS_COLLECTION_CHILD = 2;

    /**
     * When this flag is set, the views is inflated with {@link #mLightBackgroundLayoutId} instead
     * of {link #mLayoutId}
     *
     * @unknown 
     */
    public static final int FLAG_USE_LIGHT_BACKGROUND_LAYOUT = 4;

    /**
     * Application that hosts the remote views.
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public android.content.pm.ApplicationInfo mApplication;

    /**
     * The resource ID of the layout file. (Added to the parcel)
     */
    @android.annotation.UnsupportedAppUsage
    private final int mLayoutId;

    /**
     * The resource ID of the layout file in dark text mode. (Added to the parcel)
     */
    private int mLightBackgroundLayoutId = 0;

    /**
     * An array of actions to perform on the view tree once it has been
     * inflated
     */
    @android.annotation.UnsupportedAppUsage
    private java.util.ArrayList<android.widget.RemoteViews.Action> mActions;

    /**
     * Maps bitmaps to unique indicies to avoid Bitmap duplication.
     */
    @android.annotation.UnsupportedAppUsage
    private android.widget.RemoteViews.BitmapCache mBitmapCache;

    /**
     * Indicates whether or not this RemoteViews object is contained as a child of any other
     * RemoteViews.
     */
    private boolean mIsRoot = true;

    /**
     * Constants to whether or not this RemoteViews is composed of a landscape and portrait
     * RemoteViews.
     */
    private static final int MODE_NORMAL = 0;

    private static final int MODE_HAS_LANDSCAPE_AND_PORTRAIT = 1;

    /**
     * Used in conjunction with the special constructor
     * {@link #RemoteViews(RemoteViews, RemoteViews)} to keep track of the landscape and portrait
     * RemoteViews.
     */
    private android.widget.RemoteViews mLandscape = null;

    @android.annotation.UnsupportedAppUsage
    private android.widget.RemoteViews mPortrait = null;

    @android.widget.RemoteViews.ApplyFlags
    private int mApplyFlags = 0;

    /**
     * Class cookies of the Parcel this instance was read from.
     */
    private final java.util.Map<java.lang.Class, java.lang.Object> mClassCookies;

    private static final android.widget.RemoteViews.OnClickHandler DEFAULT_ON_CLICK_HANDLER = ( view, pendingIntent, response) -> android.widget.RemoteViews.startPendingIntent(view, pendingIntent, response.getLaunchOptions(view));

    private static final android.util.ArrayMap<android.widget.RemoteViews.MethodKey, android.widget.RemoteViews.MethodArgs> sMethods = new android.util.ArrayMap();

    /**
     * This key is used to perform lookups in sMethods without causing allocations.
     */
    private static final android.widget.RemoteViews.MethodKey sLookupKey = new android.widget.RemoteViews.MethodKey();

    /**
     *
     *
     * @unknown 
     */
    public void setRemoteInputs(int viewId, android.app.RemoteInput[] remoteInputs) {
        mActions.add(new android.widget.RemoteViews.SetRemoteInputsAction(viewId, remoteInputs));
    }

    /**
     * Reduces all images and ensures that they are all below the given sizes.
     *
     * @param maxWidth
     * 		the maximum width allowed
     * @param maxHeight
     * 		the maximum height allowed
     * @unknown 
     */
    public void reduceImageSizes(int maxWidth, int maxHeight) {
        java.util.ArrayList<android.graphics.Bitmap> cache = mBitmapCache.mBitmaps;
        for (int i = 0; i < cache.size(); i++) {
            android.graphics.Bitmap bitmap = cache.get(i);
            cache.set(i, android.graphics.drawable.Icon.scaleDownIfNecessary(bitmap, maxWidth, maxHeight));
        }
    }

    /**
     * Override all text colors in this layout and replace them by the given text color.
     *
     * @param textColor
     * 		The color to use.
     * @unknown 
     */
    public void overrideTextColors(int textColor) {
        addAction(new android.widget.RemoteViews.OverrideTextColorsAction(textColor));
    }

    /**
     * Sets an integer tag to the view.
     *
     * @unknown 
     */
    public void setIntTag(int viewId, int key, int tag) {
        addAction(new android.widget.RemoteViews.SetIntTagAction(viewId, key, tag));
    }

    /**
     * Set that it is disallowed to reapply another remoteview with the same layout as this view.
     * This should be done if an action is destroying the view tree of the base layout.
     *
     * @unknown 
     */
    public void addFlags(@android.widget.RemoteViews.ApplyFlags
    int flags) {
        mApplyFlags = mApplyFlags | flags;
    }

    /**
     *
     *
     * @unknown 
     */
    public boolean hasFlags(@android.widget.RemoteViews.ApplyFlags
    int flag) {
        return (mApplyFlags & flag) == flag;
    }

    /**
     * Stores information related to reflection method lookup.
     */
    static class MethodKey {
        public java.lang.Class targetClass;

        public java.lang.Class paramClass;

        public java.lang.String methodName;

        @java.lang.Override
        public boolean equals(java.lang.Object o) {
            if (!(o instanceof android.widget.RemoteViews.MethodKey)) {
                return false;
            }
            android.widget.RemoteViews.MethodKey p = ((android.widget.RemoteViews.MethodKey) (o));
            return (java.util.Objects.equals(p.targetClass, targetClass) && java.util.Objects.equals(p.paramClass, paramClass)) && java.util.Objects.equals(p.methodName, methodName);
        }

        @java.lang.Override
        public int hashCode() {
            return (java.util.Objects.hashCode(targetClass) ^ java.util.Objects.hashCode(paramClass)) ^ java.util.Objects.hashCode(methodName);
        }

        public void set(java.lang.Class targetClass, java.lang.Class paramClass, java.lang.String methodName) {
            this.targetClass = targetClass;
            this.paramClass = paramClass;
            this.methodName = methodName;
        }
    }

    /**
     * Stores information related to reflection method lookup result.
     */
    static class MethodArgs {
        public java.lang.invoke.MethodHandle syncMethod;

        public java.lang.invoke.MethodHandle asyncMethod;

        public java.lang.String asyncMethodName;
    }

    /**
     * This annotation indicates that a subclass of View is allowed to be used
     * with the {@link RemoteViews} mechanism.
     */
    @java.lang.annotation.Target({ java.lang.annotation.ElementType.TYPE })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
    public @interface RemoteView {}

    /**
     * Exception to send when something goes wrong executing an action
     */
    public static class ActionException extends java.lang.RuntimeException {
        public ActionException(java.lang.Exception ex) {
            super(ex);
        }

        public ActionException(java.lang.String message) {
            super(message);
        }

        /**
         *
         *
         * @unknown 
         */
        public ActionException(java.lang.Throwable t) {
            super(t);
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public interface OnClickHandler {
        /**
         *
         *
         * @unknown 
         */
        boolean onClickHandler(android.view.View view, android.app.PendingIntent pendingIntent, android.widget.RemoteViews.RemoteResponse response);
    }

    /**
     * Base class for all actions that can be performed on an
     * inflated view.
     *
     *  SUBCLASSES MUST BE IMMUTABLE SO CLONE WORKS!!!!!
     */
    private static abstract class Action implements android.os.Parcelable {
        public abstract void apply(android.view.View root, android.view.ViewGroup rootParent, android.widget.RemoteViews.OnClickHandler handler) throws android.widget.RemoteViews.ActionException;

        public static final int MERGE_REPLACE = 0;

        public static final int MERGE_APPEND = 1;

        public static final int MERGE_IGNORE = 2;

        public int describeContents() {
            return 0;
        }

        public void setBitmapCache(android.widget.RemoteViews.BitmapCache bitmapCache) {
            // Do nothing
        }

        @android.annotation.UnsupportedAppUsage
        public int mergeBehavior() {
            return android.widget.RemoteViews.Action.MERGE_REPLACE;
        }

        public abstract int getActionTag();

        public java.lang.String getUniqueKey() {
            return (getActionTag() + "_") + viewId;
        }

        /**
         * This is called on the background thread. It should perform any non-ui computations
         * and return the final action which will run on the UI thread.
         * Override this if some of the tasks can be performed async.
         */
        public android.widget.RemoteViews.Action initActionAsync(android.widget.RemoteViews.ViewTree root, android.view.ViewGroup rootParent, android.widget.RemoteViews.OnClickHandler handler) {
            return this;
        }

        public boolean prefersAsyncApply() {
            return false;
        }

        /**
         * Overridden by subclasses which have (or inherit) an ApplicationInfo instance
         * as member variable
         */
        public boolean hasSameAppInfo(android.content.pm.ApplicationInfo parentInfo) {
            return true;
        }

        public void visitUris(@android.annotation.NonNull
        java.util.function.Consumer<android.net.Uri> visitor) {
            // Nothing to visit by default
        }

        @android.annotation.UnsupportedAppUsage
        int viewId;
    }

    /**
     * Action class used during async inflation of RemoteViews. Subclasses are not parcelable.
     */
    private static abstract class RuntimeAction extends android.widget.RemoteViews.Action {
        @java.lang.Override
        public final int getActionTag() {
            return 0;
        }

        @java.lang.Override
        public final void writeToParcel(android.os.Parcel dest, int flags) {
            throw new java.lang.UnsupportedOperationException();
        }
    }

    // Constant used during async execution. It is not parcelable.
    private static final android.widget.RemoteViews.Action ACTION_NOOP = new android.widget.RemoteViews.RuntimeAction() {
        @java.lang.Override
        public void apply(android.view.View root, android.view.ViewGroup rootParent, android.widget.RemoteViews.OnClickHandler handler) {
        }
    };

    /**
     * Merges the passed RemoteViews actions with this RemoteViews actions according to
     * action-specific merge rules.
     *
     * @param newRv
     * 		
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public void mergeRemoteViews(android.widget.RemoteViews newRv) {
        if (newRv == null)
            return;

        // We first copy the new RemoteViews, as the process of merging modifies the way the actions
        // reference the bitmap cache. We don't want to modify the object as it may need to
        // be merged and applied multiple times.
        android.widget.RemoteViews copy = new android.widget.RemoteViews(newRv);
        java.util.HashMap<java.lang.String, android.widget.RemoteViews.Action> map = new java.util.HashMap<java.lang.String, android.widget.RemoteViews.Action>();
        if (mActions == null) {
            mActions = new java.util.ArrayList<android.widget.RemoteViews.Action>();
        }
        int count = mActions.size();
        for (int i = 0; i < count; i++) {
            android.widget.RemoteViews.Action a = mActions.get(i);
            map.put(a.getUniqueKey(), a);
        }
        java.util.ArrayList<android.widget.RemoteViews.Action> newActions = copy.mActions;
        if (newActions == null)
            return;

        count = newActions.size();
        for (int i = 0; i < count; i++) {
            android.widget.RemoteViews.Action a = newActions.get(i);
            java.lang.String key = newActions.get(i).getUniqueKey();
            int mergeBehavior = newActions.get(i).mergeBehavior();
            if (map.containsKey(key) && (mergeBehavior == android.widget.RemoteViews.Action.MERGE_REPLACE)) {
                mActions.remove(map.get(key));
                map.remove(key);
            }
            // If the merge behavior is ignore, we don't bother keeping the extra action
            if ((mergeBehavior == android.widget.RemoteViews.Action.MERGE_REPLACE) || (mergeBehavior == android.widget.RemoteViews.Action.MERGE_APPEND)) {
                mActions.add(a);
            }
        }
        // Because pruning can remove the need for bitmaps, we reconstruct the bitmap cache
        mBitmapCache = new android.widget.RemoteViews.BitmapCache();
        setBitmapCache(mBitmapCache);
    }

    /**
     * Note all {@link Uri} that are referenced internally, with the expectation
     * that Uri permission grants will need to be issued to ensure the recipient
     * of this object is able to render its contents.
     *
     * @unknown 
     */
    public void visitUris(@android.annotation.NonNull
    java.util.function.Consumer<android.net.Uri> visitor) {
        if (mActions != null) {
            for (int i = 0; i < mActions.size(); i++) {
                mActions.get(i).visitUris(visitor);
            }
        }
    }

    private static void visitIconUri(android.graphics.drawable.Icon icon, @android.annotation.NonNull
    java.util.function.Consumer<android.net.Uri> visitor) {
        if ((icon != null) && (icon.getType() == android.graphics.drawable.Icon.TYPE_URI)) {
            visitor.accept(icon.getUri());
        }
    }

    private static class RemoteViewsContextWrapper extends android.content.ContextWrapper {
        private final android.content.Context mContextForResources;

        RemoteViewsContextWrapper(android.content.Context context, android.content.Context contextForResources) {
            super(context);
            mContextForResources = contextForResources;
        }

        @java.lang.Override
        public android.content.res.Resources getResources() {
            return mContextForResources.getResources();
        }

        @java.lang.Override
        public android.content.res.Resources.Theme getTheme() {
            return mContextForResources.getTheme();
        }

        @java.lang.Override
        public java.lang.String getPackageName() {
            return mContextForResources.getPackageName();
        }
    }

    private class SetEmptyView extends android.widget.RemoteViews.Action {
        int emptyViewId;

        SetEmptyView(int viewId, int emptyViewId) {
            this.viewId = viewId;
            this.emptyViewId = emptyViewId;
        }

        SetEmptyView(android.os.Parcel in) {
            this.viewId = in.readInt();
            this.emptyViewId = in.readInt();
        }

        public void writeToParcel(android.os.Parcel out, int flags) {
            out.writeInt(this.viewId);
            out.writeInt(this.emptyViewId);
        }

        @java.lang.Override
        public void apply(android.view.View root, android.view.ViewGroup rootParent, android.widget.RemoteViews.OnClickHandler handler) {
            final android.view.View view = root.findViewById(viewId);
            if (!(view instanceof android.widget.AdapterView<?>))
                return;

            android.widget.AdapterView<?> adapterView = ((android.widget.AdapterView<?>) (view));
            final android.view.View emptyView = root.findViewById(emptyViewId);
            if (emptyView == null)
                return;

            adapterView.setEmptyView(emptyView);
        }

        @java.lang.Override
        public int getActionTag() {
            return android.widget.RemoteViews.SET_EMPTY_VIEW_ACTION_TAG;
        }
    }

    private class SetPendingIntentTemplate extends android.widget.RemoteViews.Action {
        public SetPendingIntentTemplate(int id, android.app.PendingIntent pendingIntentTemplate) {
            this.viewId = id;
            this.pendingIntentTemplate = pendingIntentTemplate;
        }

        public SetPendingIntentTemplate(android.os.Parcel parcel) {
            viewId = parcel.readInt();
            pendingIntentTemplate = android.app.PendingIntent.readPendingIntentOrNullFromParcel(parcel);
        }

        public void writeToParcel(android.os.Parcel dest, int flags) {
            dest.writeInt(viewId);
            android.app.PendingIntent.writePendingIntentOrNullToParcel(pendingIntentTemplate, dest);
        }

        @java.lang.Override
        public void apply(android.view.View root, android.view.ViewGroup rootParent, final android.widget.RemoteViews.OnClickHandler handler) {
            final android.view.View target = root.findViewById(viewId);
            if (target == null)
                return;

            // If the view isn't an AdapterView, setting a PendingIntent template doesn't make sense
            if (target instanceof android.widget.AdapterView<?>) {
                android.widget.AdapterView<?> av = ((android.widget.AdapterView<?>) (target));
                // The PendingIntent template is stored in the view's tag.
                android.widget.AdapterView.OnItemClickListener listener = new android.widget.AdapterView.OnItemClickListener() {
                    public void onItemClick(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                        // The view should be a frame layout
                        if (view instanceof android.view.ViewGroup) {
                            android.view.ViewGroup vg = ((android.view.ViewGroup) (view));
                            // AdapterViews contain their children in a frame
                            // so we need to go one layer deeper here.
                            if (parent instanceof android.widget.AdapterViewAnimator) {
                                vg = ((android.view.ViewGroup) (vg.getChildAt(0)));
                            }
                            if (vg == null)
                                return;

                            android.widget.RemoteViews.RemoteResponse response = null;
                            int childCount = vg.getChildCount();
                            for (int i = 0; i < childCount; i++) {
                                java.lang.Object tag = vg.getChildAt(i).getTag(com.android.internal.R.id.fillInIntent);
                                if (tag instanceof android.widget.RemoteViews.RemoteResponse) {
                                    response = ((android.widget.RemoteViews.RemoteResponse) (tag));
                                    break;
                                }
                            }
                            if (response == null)
                                return;

                            response.handleViewClick(view, handler);
                        }
                    }
                };
                av.setOnItemClickListener(listener);
                av.setTag(pendingIntentTemplate);
            } else {
                android.util.Log.e(android.widget.RemoteViews.LOG_TAG, (("Cannot setPendingIntentTemplate on a view which is not" + "an AdapterView (id: ") + viewId) + ")");
                return;
            }
        }

        @java.lang.Override
        public int getActionTag() {
            return android.widget.RemoteViews.SET_PENDING_INTENT_TEMPLATE_TAG;
        }

        @android.annotation.UnsupportedAppUsage
        android.app.PendingIntent pendingIntentTemplate;
    }

    private class SetRemoteViewsAdapterList extends android.widget.RemoteViews.Action {
        public SetRemoteViewsAdapterList(int id, java.util.ArrayList<android.widget.RemoteViews> list, int viewTypeCount) {
            this.viewId = id;
            this.list = list;
            this.viewTypeCount = viewTypeCount;
        }

        public SetRemoteViewsAdapterList(android.os.Parcel parcel) {
            viewId = parcel.readInt();
            viewTypeCount = parcel.readInt();
            list = parcel.createTypedArrayList(CREATOR);
        }

        public void writeToParcel(android.os.Parcel dest, int flags) {
            dest.writeInt(viewId);
            dest.writeInt(viewTypeCount);
            dest.writeTypedList(list, flags);
        }

        @java.lang.Override
        public void apply(android.view.View root, android.view.ViewGroup rootParent, android.widget.RemoteViews.OnClickHandler handler) {
            final android.view.View target = root.findViewById(viewId);
            if (target == null)
                return;

            // Ensure that we are applying to an AppWidget root
            if (!(rootParent instanceof android.appwidget.AppWidgetHostView)) {
                android.util.Log.e(android.widget.RemoteViews.LOG_TAG, (("SetRemoteViewsAdapterIntent action can only be used for " + "AppWidgets (root id: ") + viewId) + ")");
                return;
            }
            // Ensure that we are calling setRemoteAdapter on an AdapterView that supports it
            if ((!(target instanceof android.widget.AbsListView)) && (!(target instanceof android.widget.AdapterViewAnimator))) {
                android.util.Log.e(android.widget.RemoteViews.LOG_TAG, (("Cannot setRemoteViewsAdapter on a view which is not " + "an AbsListView or AdapterViewAnimator (id: ") + viewId) + ")");
                return;
            }
            if (target instanceof android.widget.AbsListView) {
                android.widget.AbsListView v = ((android.widget.AbsListView) (target));
                android.widget.Adapter a = v.getAdapter();
                if ((a instanceof android.widget.RemoteViewsListAdapter) && (viewTypeCount <= a.getViewTypeCount())) {
                    ((android.widget.RemoteViewsListAdapter) (a)).setViewsList(list);
                } else {
                    v.setAdapter(new android.widget.RemoteViewsListAdapter(v.getContext(), list, viewTypeCount));
                }
            } else
                if (target instanceof android.widget.AdapterViewAnimator) {
                    android.widget.AdapterViewAnimator v = ((android.widget.AdapterViewAnimator) (target));
                    android.widget.Adapter a = v.getAdapter();
                    if ((a instanceof android.widget.RemoteViewsListAdapter) && (viewTypeCount <= a.getViewTypeCount())) {
                        ((android.widget.RemoteViewsListAdapter) (a)).setViewsList(list);
                    } else {
                        v.setAdapter(new android.widget.RemoteViewsListAdapter(v.getContext(), list, viewTypeCount));
                    }
                }

        }

        @java.lang.Override
        public int getActionTag() {
            return android.widget.RemoteViews.SET_REMOTE_VIEW_ADAPTER_LIST_TAG;
        }

        int viewTypeCount;

        java.util.ArrayList<android.widget.RemoteViews> list;
    }

    private class SetRemoteViewsAdapterIntent extends android.widget.RemoteViews.Action {
        public SetRemoteViewsAdapterIntent(int id, android.content.Intent intent) {
            this.viewId = id;
            this.intent = intent;
        }

        public SetRemoteViewsAdapterIntent(android.os.Parcel parcel) {
            viewId = parcel.readInt();
            intent = parcel.readTypedObject(android.content.Intent.this.CREATOR);
        }

        public void writeToParcel(android.os.Parcel dest, int flags) {
            dest.writeInt(viewId);
            dest.writeTypedObject(intent, flags);
        }

        @java.lang.Override
        public void apply(android.view.View root, android.view.ViewGroup rootParent, android.widget.RemoteViews.OnClickHandler handler) {
            final android.view.View target = root.findViewById(viewId);
            if (target == null)
                return;

            // Ensure that we are applying to an AppWidget root
            if (!(rootParent instanceof android.appwidget.AppWidgetHostView)) {
                android.util.Log.e(android.widget.RemoteViews.LOG_TAG, (("SetRemoteViewsAdapterIntent action can only be used for " + "AppWidgets (root id: ") + viewId) + ")");
                return;
            }
            // Ensure that we are calling setRemoteAdapter on an AdapterView that supports it
            if ((!(target instanceof android.widget.AbsListView)) && (!(target instanceof android.widget.AdapterViewAnimator))) {
                android.util.Log.e(android.widget.RemoteViews.LOG_TAG, (("Cannot setRemoteViewsAdapter on a view which is not " + "an AbsListView or AdapterViewAnimator (id: ") + viewId) + ")");
                return;
            }
            // Embed the AppWidget Id for use in RemoteViewsAdapter when connecting to the intent
            // RemoteViewsService
            android.appwidget.AppWidgetHostView host = ((android.appwidget.AppWidgetHostView) (rootParent));
            intent.putExtra(android.widget.RemoteViews.EXTRA_REMOTEADAPTER_APPWIDGET_ID, host.getAppWidgetId()).putExtra(android.widget.RemoteViews.EXTRA_REMOTEADAPTER_ON_LIGHT_BACKGROUND, hasFlags(android.widget.RemoteViews.FLAG_USE_LIGHT_BACKGROUND_LAYOUT));
            if (target instanceof android.widget.AbsListView) {
                android.widget.AbsListView v = ((android.widget.AbsListView) (target));
                v.setRemoteViewsAdapter(intent, isAsync);
                v.setRemoteViewsOnClickHandler(handler);
            } else
                if (target instanceof android.widget.AdapterViewAnimator) {
                    android.widget.AdapterViewAnimator v = ((android.widget.AdapterViewAnimator) (target));
                    v.setRemoteViewsAdapter(intent, isAsync);
                    v.setRemoteViewsOnClickHandler(handler);
                }

        }

        @java.lang.Override
        public android.widget.RemoteViews.Action initActionAsync(android.widget.RemoteViews.ViewTree root, android.view.ViewGroup rootParent, android.widget.RemoteViews.OnClickHandler handler) {
            android.widget.RemoteViews.SetRemoteViewsAdapterIntent copy = new android.widget.RemoteViews.SetRemoteViewsAdapterIntent(viewId, intent);
            copy.isAsync = true;
            return copy;
        }

        @java.lang.Override
        public int getActionTag() {
            return android.widget.RemoteViews.SET_REMOTE_VIEW_ADAPTER_INTENT_TAG;
        }

        android.content.Intent intent;

        boolean isAsync = false;
    }

    /**
     * Equivalent to calling
     * {@link android.view.View#setOnClickListener(android.view.View.OnClickListener)}
     * to launch the provided {@link PendingIntent}.
     */
    private class SetOnClickResponse extends android.widget.RemoteViews.Action {
        SetOnClickResponse(int id, android.widget.RemoteViews.RemoteResponse response) {
            this.viewId = id;
            this.mResponse = response;
        }

        SetOnClickResponse(android.os.Parcel parcel) {
            viewId = parcel.readInt();
            mResponse = new android.widget.RemoteViews.RemoteResponse();
            mResponse.readFromParcel(parcel);
        }

        public void writeToParcel(android.os.Parcel dest, int flags) {
            dest.writeInt(viewId);
            mResponse.writeToParcel(dest, flags);
        }

        @java.lang.Override
        public void apply(android.view.View root, android.view.ViewGroup rootParent, final android.widget.RemoteViews.OnClickHandler handler) {
            final android.view.View target = root.findViewById(viewId);
            if (target == null)
                return;

            if (mResponse.mPendingIntent != null) {
                // If the view is an AdapterView, setting a PendingIntent on click doesn't make
                // much sense, do they mean to set a PendingIntent template for the
                // AdapterView's children?
                if (hasFlags(android.widget.RemoteViews.FLAG_WIDGET_IS_COLLECTION_CHILD)) {
                    android.util.Log.w(android.widget.RemoteViews.LOG_TAG, (("Cannot SetOnClickResponse for collection item " + "(id: ") + viewId) + ")");
                    android.content.pm.ApplicationInfo appInfo = root.getContext().getApplicationInfo();
                    // We let this slide for HC and ICS so as to not break compatibility. It should
                    // have been disabled from the outset, but was left open by accident.
                    if ((appInfo != null) && (appInfo.targetSdkVersion >= Build.VERSION_CODES.JELLY_BEAN)) {
                        return;
                    }
                }
                target.setTagInternal(R.id.pending_intent_tag, mResponse.mPendingIntent);
            } else
                if (mResponse.mFillIntent != null) {
                    if (!hasFlags(android.widget.RemoteViews.FLAG_WIDGET_IS_COLLECTION_CHILD)) {
                        android.util.Log.e(android.widget.RemoteViews.LOG_TAG, "The method setOnClickFillInIntent is available " + "only from RemoteViewsFactory (ie. on collection items).");
                        return;
                    }
                    if (target == root) {
                        // Target is a root node of an AdapterView child. Set the response in the tag.
                        // Actual click handling is done by OnItemClickListener in
                        // SetPendingIntentTemplate, which uses this tag information.
                        target.setTagInternal(com.android.internal.R.id.fillInIntent, mResponse);
                        return;
                    }
                } else {
                    // No intent to apply
                    target.setOnClickListener(null);
                    return;
                }

            target.setOnClickListener(( v) -> mResponse.handleViewClick(v, handler));
        }

        @java.lang.Override
        public int getActionTag() {
            return android.widget.RemoteViews.SET_ON_CLICK_RESPONSE_TAG;
        }

        final android.widget.RemoteViews.RemoteResponse mResponse;
    }

    /**
     *
     *
     * @unknown *
     */
    public static android.graphics.Rect getSourceBounds(android.view.View v) {
        final float appScale = v.getContext().getResources().getCompatibilityInfo().applicationScale;
        final int[] pos = new int[2];
        v.getLocationOnScreen(pos);
        final android.graphics.Rect rect = new android.graphics.Rect();
        rect.left = ((int) ((pos[0] * appScale) + 0.5F));
        rect.top = ((int) ((pos[1] * appScale) + 0.5F));
        rect.right = ((int) (((pos[0] + v.getWidth()) * appScale) + 0.5F));
        rect.bottom = ((int) (((pos[1] + v.getHeight()) * appScale) + 0.5F));
        return rect;
    }

    private java.lang.invoke.MethodHandle getMethod(android.view.View view, java.lang.String methodName, java.lang.Class<?> paramType, boolean async) {
        android.widget.RemoteViews.MethodArgs result;
        java.lang.Class<? extends android.view.View> klass = view.getClass();
        synchronized(android.widget.RemoteViews.sMethods) {
            // The key is defined by the view class, param class and method name.
            android.widget.RemoteViews.sLookupKey.set(klass, paramType, methodName);
            result = android.widget.RemoteViews.sMethods.get(android.widget.RemoteViews.sLookupKey);
            if (result == null) {
                java.lang.reflect.Method method;
                try {
                    if (paramType == null) {
                        method = klass.getMethod(methodName);
                    } else {
                        method = klass.getMethod(methodName, paramType);
                    }
                    if (!method.isAnnotationPresent(android.view.RemotableViewMethod.class)) {
                        throw new android.widget.RemoteViews.ActionException(((("view: " + klass.getName()) + " can't use method with RemoteViews: ") + methodName) + android.widget.RemoteViews.getParameters(paramType));
                    }
                    result = new android.widget.RemoteViews.MethodArgs();
                    result.syncMethod = java.lang.invoke.MethodHandles.publicLookup().unreflect(method);
                    result.asyncMethodName = method.getAnnotation(android.view.RemotableViewMethod.class).asyncImpl();
                } catch (java.lang.NoSuchMethodException | java.lang.IllegalAccessException ex) {
                    throw new android.widget.RemoteViews.ActionException(((("view: " + klass.getName()) + " doesn't have method: ") + methodName) + android.widget.RemoteViews.getParameters(paramType));
                }
                android.widget.RemoteViews.MethodKey key = new android.widget.RemoteViews.MethodKey();
                key.set(klass, paramType, methodName);
                android.widget.RemoteViews.sMethods.put(key, result);
            }
            if (!async) {
                return result.syncMethod;
            }
            // Check this so see if async method is implemented or not.
            if (result.asyncMethodName.isEmpty()) {
                return null;
            }
            // Async method is lazily loaded. If it is not yet loaded, load now.
            if (result.asyncMethod == null) {
                java.lang.invoke.MethodType asyncType = result.syncMethod.type().dropParameterTypes(0, 1).changeReturnType(java.lang.Runnable.class);
                try {
                    result.asyncMethod = java.lang.invoke.MethodHandles.publicLookup().findVirtual(klass, result.asyncMethodName, asyncType);
                } catch (java.lang.NoSuchMethodException | java.lang.IllegalAccessException ex) {
                    throw new android.widget.RemoteViews.ActionException(((((((("Async implementation declared as " + result.asyncMethodName) + " but not defined for ") + methodName) + ": public Runnable ") + result.asyncMethodName) + " (") + android.text.TextUtils.join(",", asyncType.parameterArray())) + ")");
                }
            }
            return result.asyncMethod;
        }
    }

    private static java.lang.String getParameters(java.lang.Class<?> paramType) {
        if (paramType == null)
            return "()";

        return ("(" + paramType) + ")";
    }

    /**
     * Equivalent to calling
     * {@link Drawable#setColorFilter(int, android.graphics.PorterDuff.Mode)},
     * on the {@link Drawable} of a given view.
     * <p>
     * The operation will be performed on the {@link Drawable} returned by the
     * target {@link View#getBackground()} by default.  If targetBackground is false,
     * we assume the target is an {@link ImageView} and try applying the operations
     * to {@link ImageView#getDrawable()}.
     * <p>
     */
    private class SetDrawableTint extends android.widget.RemoteViews.Action {
        SetDrawableTint(int id, boolean targetBackground, int colorFilter, @android.annotation.NonNull
        android.graphics.PorterDuff.Mode mode) {
            this.viewId = id;
            this.targetBackground = targetBackground;
            this.colorFilter = colorFilter;
            this.filterMode = mode;
        }

        SetDrawableTint(android.os.Parcel parcel) {
            viewId = parcel.readInt();
            targetBackground = parcel.readInt() != 0;
            colorFilter = parcel.readInt();
            filterMode = android.graphics.PorterDuff.intToMode(parcel.readInt());
        }

        public void writeToParcel(android.os.Parcel dest, int flags) {
            dest.writeInt(viewId);
            dest.writeInt(targetBackground ? 1 : 0);
            dest.writeInt(colorFilter);
            dest.writeInt(android.graphics.PorterDuff.modeToInt(filterMode));
        }

        @java.lang.Override
        public void apply(android.view.View root, android.view.ViewGroup rootParent, android.widget.RemoteViews.OnClickHandler handler) {
            final android.view.View target = root.findViewById(viewId);
            if (target == null)
                return;

            // Pick the correct drawable to modify for this view
            android.graphics.drawable.Drawable targetDrawable = null;
            if (targetBackground) {
                targetDrawable = target.getBackground();
            } else
                if (target instanceof android.widget.ImageView) {
                    android.widget.ImageView imageView = ((android.widget.ImageView) (target));
                    targetDrawable = imageView.getDrawable();
                }

            if (targetDrawable != null) {
                targetDrawable.mutate().setColorFilter(colorFilter, filterMode);
            }
        }

        @java.lang.Override
        public int getActionTag() {
            return android.widget.RemoteViews.SET_DRAWABLE_TINT_TAG;
        }

        boolean targetBackground;

        int colorFilter;

        android.graphics.PorterDuff.Mode filterMode;
    }

    /**
     * Equivalent to calling
     * {@link RippleDrawable#setColor(ColorStateList)},
     * on the {@link Drawable} of a given view.
     * <p>
     * The operation will be performed on the {@link Drawable} returned by the
     * target {@link View#getBackground()}.
     * <p>
     */
    private class SetRippleDrawableColor extends android.widget.RemoteViews.Action {
        android.content.res.ColorStateList mColorStateList;

        SetRippleDrawableColor(int id, android.content.res.ColorStateList colorStateList) {
            this.viewId = id;
            this.mColorStateList = colorStateList;
        }

        SetRippleDrawableColor(android.os.Parcel parcel) {
            viewId = parcel.readInt();
            mColorStateList = parcel.readParcelable(null);
        }

        public void writeToParcel(android.os.Parcel dest, int flags) {
            dest.writeInt(viewId);
            dest.writeParcelable(mColorStateList, 0);
        }

        @java.lang.Override
        public void apply(android.view.View root, android.view.ViewGroup rootParent, android.widget.RemoteViews.OnClickHandler handler) {
            final android.view.View target = root.findViewById(viewId);
            if (target == null)
                return;

            // Pick the correct drawable to modify for this view
            android.graphics.drawable.Drawable targetDrawable = target.getBackground();
            if (targetDrawable instanceof android.graphics.drawable.RippleDrawable) {
                ((android.graphics.drawable.RippleDrawable) (targetDrawable.mutate())).setColor(mColorStateList);
            }
        }

        @java.lang.Override
        public int getActionTag() {
            return android.widget.RemoteViews.SET_RIPPLE_DRAWABLE_COLOR_TAG;
        }
    }

    private final class ViewContentNavigation extends android.widget.RemoteViews.Action {
        final boolean mNext;

        ViewContentNavigation(int viewId, boolean next) {
            this.viewId = viewId;
            this.mNext = next;
        }

        ViewContentNavigation(android.os.Parcel in) {
            this.viewId = in.readInt();
            this.mNext = in.readBoolean();
        }

        public void writeToParcel(android.os.Parcel out, int flags) {
            out.writeInt(this.viewId);
            out.writeBoolean(this.mNext);
        }

        @java.lang.Override
        public void apply(android.view.View root, android.view.ViewGroup rootParent, android.widget.RemoteViews.OnClickHandler handler) {
            final android.view.View view = root.findViewById(viewId);
            if (view == null)
                return;

            try {
                /* async */
                getMethod(view, mNext ? "showNext" : "showPrevious", null, false).invoke(view);
            } catch (java.lang.Throwable ex) {
                throw new android.widget.RemoteViews.ActionException(ex);
            }
        }

        public int mergeBehavior() {
            return android.widget.RemoteViews.Action.MERGE_IGNORE;
        }

        @java.lang.Override
        public int getActionTag() {
            return android.widget.RemoteViews.VIEW_CONTENT_NAVIGATION_TAG;
        }
    }

    private static class BitmapCache {
        @android.annotation.UnsupportedAppUsage
        java.util.ArrayList<android.graphics.Bitmap> mBitmaps;

        int mBitmapMemory = -1;

        public BitmapCache() {
            mBitmaps = new java.util.ArrayList<>();
        }

        public BitmapCache(android.os.Parcel source) {
            mBitmaps = source.createTypedArrayList(android.graphics.Bitmap.this.CREATOR);
        }

        public int getBitmapId(android.graphics.Bitmap b) {
            if (b == null) {
                return -1;
            } else {
                if (mBitmaps.contains(b)) {
                    return mBitmaps.indexOf(b);
                } else {
                    mBitmaps.add(b);
                    mBitmapMemory = -1;
                    return mBitmaps.size() - 1;
                }
            }
        }

        public android.graphics.Bitmap getBitmapForId(int id) {
            if ((id == (-1)) || (id >= mBitmaps.size())) {
                return null;
            } else {
                return mBitmaps.get(id);
            }
        }

        public void writeBitmapsToParcel(android.os.Parcel dest, int flags) {
            dest.writeTypedList(mBitmaps, flags);
        }

        public int getBitmapMemory() {
            if (mBitmapMemory < 0) {
                mBitmapMemory = 0;
                int count = mBitmaps.size();
                for (int i = 0; i < count; i++) {
                    mBitmapMemory += mBitmaps.get(i).getAllocationByteCount();
                }
            }
            return mBitmapMemory;
        }
    }

    private class BitmapReflectionAction extends android.widget.RemoteViews.Action {
        int bitmapId;

        @android.annotation.UnsupportedAppUsage
        android.graphics.Bitmap bitmap;

        @android.annotation.UnsupportedAppUsage
        java.lang.String methodName;

        BitmapReflectionAction(int viewId, java.lang.String methodName, android.graphics.Bitmap bitmap) {
            this.bitmap = bitmap;
            this.viewId = viewId;
            this.methodName = methodName;
            bitmapId = mBitmapCache.getBitmapId(bitmap);
        }

        BitmapReflectionAction(android.os.Parcel in) {
            viewId = in.readInt();
            methodName = in.readString();
            bitmapId = in.readInt();
            bitmap = mBitmapCache.getBitmapForId(bitmapId);
        }

        @java.lang.Override
        public void writeToParcel(android.os.Parcel dest, int flags) {
            dest.writeInt(viewId);
            dest.writeString(methodName);
            dest.writeInt(bitmapId);
        }

        @java.lang.Override
        public void apply(android.view.View root, android.view.ViewGroup rootParent, android.widget.RemoteViews.OnClickHandler handler) throws android.widget.RemoteViews.ActionException {
            android.widget.RemoteViews.ReflectionAction ra = new android.widget.RemoteViews.ReflectionAction(viewId, methodName, android.widget.RemoteViews.ReflectionAction.BITMAP, bitmap);
            ra.apply(root, rootParent, handler);
        }

        @java.lang.Override
        public void setBitmapCache(android.widget.RemoteViews.BitmapCache bitmapCache) {
            bitmapId = bitmapCache.getBitmapId(bitmap);
        }

        @java.lang.Override
        public int getActionTag() {
            return android.widget.RemoteViews.BITMAP_REFLECTION_ACTION_TAG;
        }
    }

    /**
     * Base class for the reflection actions.
     */
    private final class ReflectionAction extends android.widget.RemoteViews.Action {
        static final int BOOLEAN = 1;

        static final int BYTE = 2;

        static final int SHORT = 3;

        static final int INT = 4;

        static final int LONG = 5;

        static final int FLOAT = 6;

        static final int DOUBLE = 7;

        static final int CHAR = 8;

        static final int STRING = 9;

        static final int CHAR_SEQUENCE = 10;

        static final int URI = 11;

        // BITMAP actions are never stored in the list of actions. They are only used locally
        // to implement BitmapReflectionAction, which eliminates duplicates using BitmapCache.
        static final int BITMAP = 12;

        static final int BUNDLE = 13;

        static final int INTENT = 14;

        static final int COLOR_STATE_LIST = 15;

        static final int ICON = 16;

        @android.annotation.UnsupportedAppUsage
        java.lang.String methodName;

        int type;

        @android.annotation.UnsupportedAppUsage
        java.lang.Object value;

        ReflectionAction(int viewId, java.lang.String methodName, int type, java.lang.Object value) {
            this.viewId = viewId;
            this.methodName = methodName;
            this.type = type;
            this.value = value;
        }

        ReflectionAction(android.os.Parcel in) {
            this.viewId = in.readInt();
            this.methodName = in.readString();
            this.type = in.readInt();
            // noinspection ConstantIfStatement
            if (false) {
                android.util.Log.d(android.widget.RemoteViews.LOG_TAG, (((("read viewId=0x" + java.lang.Integer.toHexString(this.viewId)) + " methodName=") + this.methodName) + " type=") + this.type);
            }
            // For some values that may have been null, we first check a flag to see if they were
            // written to the parcel.
            switch (this.type) {
                case android.widget.RemoteViews.ReflectionAction.BOOLEAN :
                    this.value = in.readBoolean();
                    break;
                case android.widget.RemoteViews.ReflectionAction.BYTE :
                    this.value = in.readByte();
                    break;
                case android.widget.RemoteViews.ReflectionAction.SHORT :
                    this.value = ((short) (in.readInt()));
                    break;
                case android.widget.RemoteViews.ReflectionAction.INT :
                    this.value = in.readInt();
                    break;
                case android.widget.RemoteViews.ReflectionAction.LONG :
                    this.value = in.readLong();
                    break;
                case android.widget.RemoteViews.ReflectionAction.FLOAT :
                    this.value = in.readFloat();
                    break;
                case android.widget.RemoteViews.ReflectionAction.DOUBLE :
                    this.value = in.readDouble();
                    break;
                case android.widget.RemoteViews.ReflectionAction.CHAR :
                    this.value = ((char) (in.readInt()));
                    break;
                case android.widget.RemoteViews.ReflectionAction.STRING :
                    this.value = in.readString();
                    break;
                case android.widget.RemoteViews.ReflectionAction.CHAR_SEQUENCE :
                    this.value = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
                    break;
                case android.widget.RemoteViews.ReflectionAction.URI :
                    this.value = in.readTypedObject(Uri.CREATOR);
                    break;
                case android.widget.RemoteViews.ReflectionAction.BITMAP :
                    this.value = in.readTypedObject(android.graphics.Bitmap.this.CREATOR);
                    break;
                case android.widget.RemoteViews.ReflectionAction.BUNDLE :
                    this.value = in.readBundle();
                    break;
                case android.widget.RemoteViews.ReflectionAction.INTENT :
                    this.value = in.readTypedObject(android.content.Intent.this.CREATOR);
                    break;
                case android.widget.RemoteViews.ReflectionAction.COLOR_STATE_LIST :
                    this.value = in.readTypedObject(android.content.res.ColorStateList.this.CREATOR);
                    break;
                case android.widget.RemoteViews.ReflectionAction.ICON :
                    this.value = in.readTypedObject(android.graphics.drawable.Icon.this.CREATOR);
                default :
                    break;
            }
        }

        public void writeToParcel(android.os.Parcel out, int flags) {
            out.writeInt(this.viewId);
            out.writeString(this.methodName);
            out.writeInt(this.type);
            // noinspection ConstantIfStatement
            if (false) {
                android.util.Log.d(android.widget.RemoteViews.LOG_TAG, (((("write viewId=0x" + java.lang.Integer.toHexString(this.viewId)) + " methodName=") + this.methodName) + " type=") + this.type);
            }
            // For some values which are null, we record an integer flag to indicate whether
            // we have written a valid value to the parcel.
            switch (this.type) {
                case android.widget.RemoteViews.ReflectionAction.BOOLEAN :
                    out.writeBoolean(((java.lang.Boolean) (this.value)));
                    break;
                case android.widget.RemoteViews.ReflectionAction.BYTE :
                    out.writeByte(((java.lang.Byte) (this.value)));
                    break;
                case android.widget.RemoteViews.ReflectionAction.SHORT :
                    out.writeInt(((java.lang.Short) (this.value)));
                    break;
                case android.widget.RemoteViews.ReflectionAction.INT :
                    out.writeInt(((java.lang.Integer) (this.value)));
                    break;
                case android.widget.RemoteViews.ReflectionAction.LONG :
                    out.writeLong(((java.lang.Long) (this.value)));
                    break;
                case android.widget.RemoteViews.ReflectionAction.FLOAT :
                    out.writeFloat(((java.lang.Float) (this.value)));
                    break;
                case android.widget.RemoteViews.ReflectionAction.DOUBLE :
                    out.writeDouble(((java.lang.Double) (this.value)));
                    break;
                case android.widget.RemoteViews.ReflectionAction.CHAR :
                    out.writeInt(((int) (((java.lang.Character) (this.value)).charValue())));
                    break;
                case android.widget.RemoteViews.ReflectionAction.STRING :
                    out.writeString(((java.lang.String) (this.value)));
                    break;
                case android.widget.RemoteViews.ReflectionAction.CHAR_SEQUENCE :
                    android.text.TextUtils.writeToParcel(((java.lang.CharSequence) (this.value)), out, flags);
                    break;
                case android.widget.RemoteViews.ReflectionAction.BUNDLE :
                    out.writeBundle(((android.os.Bundle) (this.value)));
                    break;
                case android.widget.RemoteViews.ReflectionAction.URI :
                case android.widget.RemoteViews.ReflectionAction.BITMAP :
                case android.widget.RemoteViews.ReflectionAction.INTENT :
                case android.widget.RemoteViews.ReflectionAction.COLOR_STATE_LIST :
                case android.widget.RemoteViews.ReflectionAction.ICON :
                    out.writeTypedObject(((android.os.Parcelable) (this.value)), flags);
                    break;
                default :
                    break;
            }
        }

        private java.lang.Class<?> getParameterType() {
            switch (this.type) {
                case android.widget.RemoteViews.ReflectionAction.BOOLEAN :
                    return boolean.class;
                case android.widget.RemoteViews.ReflectionAction.BYTE :
                    return byte.class;
                case android.widget.RemoteViews.ReflectionAction.SHORT :
                    return short.class;
                case android.widget.RemoteViews.ReflectionAction.INT :
                    return int.class;
                case android.widget.RemoteViews.ReflectionAction.LONG :
                    return long.class;
                case android.widget.RemoteViews.ReflectionAction.FLOAT :
                    return float.class;
                case android.widget.RemoteViews.ReflectionAction.DOUBLE :
                    return double.class;
                case android.widget.RemoteViews.ReflectionAction.CHAR :
                    return char.class;
                case android.widget.RemoteViews.ReflectionAction.STRING :
                    return java.lang.String.class;
                case android.widget.RemoteViews.ReflectionAction.CHAR_SEQUENCE :
                    return java.lang.CharSequence.class;
                case android.widget.RemoteViews.ReflectionAction.URI :
                    return android.net.Uri.class;
                case android.widget.RemoteViews.ReflectionAction.BITMAP :
                    return android.graphics.Bitmap.class;
                case android.widget.RemoteViews.ReflectionAction.BUNDLE :
                    return android.os.Bundle.class;
                case android.widget.RemoteViews.ReflectionAction.INTENT :
                    return android.content.Intent.class;
                case android.widget.RemoteViews.ReflectionAction.COLOR_STATE_LIST :
                    return android.content.res.ColorStateList.class;
                case android.widget.RemoteViews.ReflectionAction.ICON :
                    return android.graphics.drawable.Icon.class;
                default :
                    return null;
            }
        }

        @java.lang.Override
        public void apply(android.view.View root, android.view.ViewGroup rootParent, android.widget.RemoteViews.OnClickHandler handler) {
            final android.view.View view = root.findViewById(viewId);
            if (view == null)
                return;

            java.lang.Class<?> param = getParameterType();
            if (param == null) {
                throw new android.widget.RemoteViews.ActionException("bad type: " + this.type);
            }
            try {
                /* async */
                getMethod(view, this.methodName, param, false).invoke(view, this.value);
            } catch (java.lang.Throwable ex) {
                throw new android.widget.RemoteViews.ActionException(ex);
            }
        }

        @java.lang.Override
        public android.widget.RemoteViews.Action initActionAsync(android.widget.RemoteViews.ViewTree root, android.view.ViewGroup rootParent, android.widget.RemoteViews.OnClickHandler handler) {
            final android.view.View view = root.findViewById(viewId);
            if (view == null)
                return android.widget.RemoteViews.ACTION_NOOP;

            java.lang.Class<?> param = getParameterType();
            if (param == null) {
                throw new android.widget.RemoteViews.ActionException("bad type: " + this.type);
            }
            try {
                java.lang.invoke.MethodHandle method = /* async */
                getMethod(view, this.methodName, param, true);
                if (method != null) {
                    java.lang.Runnable endAction = ((java.lang.Runnable) (method.invoke(view, this.value)));
                    if (endAction == null) {
                        return android.widget.RemoteViews.ACTION_NOOP;
                    } else {
                        // Special case view stub
                        if (endAction instanceof android.view.ViewStub.ViewReplaceRunnable) {
                            root.createTree();
                            // Replace child tree
                            root.findViewTreeById(viewId).replaceView(((android.view.ViewStub.ViewReplaceRunnable) (endAction)).view);
                        }
                        return new android.widget.RemoteViews.RunnableAction(endAction);
                    }
                }
            } catch (java.lang.Throwable ex) {
                throw new android.widget.RemoteViews.ActionException(ex);
            }
            return this;
        }

        public int mergeBehavior() {
            // smoothScrollBy is cumulative, everything else overwites.
            if (methodName.equals("smoothScrollBy")) {
                return android.widget.RemoteViews.Action.MERGE_APPEND;
            } else {
                return android.widget.RemoteViews.Action.MERGE_REPLACE;
            }
        }

        @java.lang.Override
        public int getActionTag() {
            return android.widget.RemoteViews.REFLECTION_ACTION_TAG;
        }

        @java.lang.Override
        public java.lang.String getUniqueKey() {
            // Each type of reflection action corresponds to a setter, so each should be seen as
            // unique from the standpoint of merging.
            return (super.getUniqueKey() + this.methodName) + this.type;
        }

        @java.lang.Override
        public boolean prefersAsyncApply() {
            return (this.type == android.widget.RemoteViews.ReflectionAction.URI) || (this.type == android.widget.RemoteViews.ReflectionAction.ICON);
        }

        @java.lang.Override
        public void visitUris(@android.annotation.NonNull
        java.util.function.Consumer<android.net.Uri> visitor) {
            switch (this.type) {
                case android.widget.RemoteViews.ReflectionAction.URI :
                    final android.net.Uri uri = ((android.net.Uri) (this.value));
                    visitor.accept(uri);
                    break;
                case android.widget.RemoteViews.ReflectionAction.ICON :
                    final android.graphics.drawable.Icon icon = ((android.graphics.drawable.Icon) (this.value));
                    android.widget.RemoteViews.visitIconUri(icon, visitor);
                    break;
            }
        }
    }

    /**
     * This is only used for async execution of actions and it not parcelable.
     */
    private static final class RunnableAction extends android.widget.RemoteViews.RuntimeAction {
        private final java.lang.Runnable mRunnable;

        RunnableAction(java.lang.Runnable r) {
            mRunnable = r;
        }

        @java.lang.Override
        public void apply(android.view.View root, android.view.ViewGroup rootParent, android.widget.RemoteViews.OnClickHandler handler) {
            mRunnable.run();
        }
    }

    private void configureRemoteViewsAsChild(android.widget.RemoteViews rv) {
        rv.setBitmapCache(mBitmapCache);
        rv.setNotRoot();
    }

    void setNotRoot() {
        mIsRoot = false;
    }

    /**
     * ViewGroup methods that are related to adding Views.
     */
    private class ViewGroupActionAdd extends android.widget.RemoteViews.Action {
        @android.annotation.UnsupportedAppUsage
        private android.widget.RemoteViews mNestedViews;

        private int mIndex;

        ViewGroupActionAdd(int viewId, android.widget.RemoteViews nestedViews) {
            /* index */
            this(viewId, nestedViews, -1);
        }

        ViewGroupActionAdd(int viewId, android.widget.RemoteViews nestedViews, int index) {
            this.viewId = viewId;
            mNestedViews = nestedViews;
            mIndex = index;
            if (nestedViews != null) {
                configureRemoteViewsAsChild(nestedViews);
            }
        }

        ViewGroupActionAdd(android.os.Parcel parcel, android.widget.RemoteViews.BitmapCache bitmapCache, android.content.pm.ApplicationInfo info, int depth, java.util.Map<java.lang.Class, java.lang.Object> classCookies) {
            viewId = parcel.readInt();
            mIndex = parcel.readInt();
            mNestedViews = new android.widget.RemoteViews(parcel, bitmapCache, info, depth, classCookies);
            mNestedViews.addFlags(mApplyFlags);
        }

        public void writeToParcel(android.os.Parcel dest, int flags) {
            dest.writeInt(viewId);
            dest.writeInt(mIndex);
            mNestedViews.writeToParcel(dest, flags);
        }

        @java.lang.Override
        public boolean hasSameAppInfo(android.content.pm.ApplicationInfo parentInfo) {
            return mNestedViews.hasSameAppInfo(parentInfo);
        }

        @java.lang.Override
        public void apply(android.view.View root, android.view.ViewGroup rootParent, android.widget.RemoteViews.OnClickHandler handler) {
            final android.content.Context context = root.getContext();
            final android.view.ViewGroup target = root.findViewById(viewId);
            if (target == null) {
                return;
            }
            // Inflate nested views and add as children
            target.addView(mNestedViews.apply(context, target, handler), mIndex);
        }

        @java.lang.Override
        public android.widget.RemoteViews.Action initActionAsync(android.widget.RemoteViews.ViewTree root, android.view.ViewGroup rootParent, android.widget.RemoteViews.OnClickHandler handler) {
            // In the async implementation, update the view tree so that subsequent calls to
            // findViewById return the current view.
            root.createTree();
            android.widget.RemoteViews.ViewTree target = root.findViewTreeById(viewId);
            if ((target == null) || (!(target.mRoot instanceof android.view.ViewGroup))) {
                return android.widget.RemoteViews.ACTION_NOOP;
            }
            final android.view.ViewGroup targetVg = ((android.view.ViewGroup) (target.mRoot));
            // Inflate nested views and perform all the async tasks for the child remoteView.
            final android.content.Context context = root.mRoot.getContext();
            final android.widget.RemoteViews.AsyncApplyTask task = mNestedViews.getAsyncApplyTask(context, targetVg, null, handler);
            final android.widget.RemoteViews.ViewTree tree = task.doInBackground();
            if (tree == null) {
                throw new android.widget.RemoteViews.ActionException(task.mError);
            }
            // Update the global view tree, so that next call to findViewTreeById
            // goes through the subtree as well.
            target.addChild(tree, mIndex);
            return new android.widget.RemoteViews.RuntimeAction() {
                @java.lang.Override
                public void apply(android.view.View root, android.view.ViewGroup rootParent, android.widget.RemoteViews.OnClickHandler handler) throws android.widget.RemoteViews.ActionException {
                    task.onPostExecute(tree);
                    targetVg.addView(task.mResult, mIndex);
                }
            };
        }

        @java.lang.Override
        public void setBitmapCache(android.widget.RemoteViews.BitmapCache bitmapCache) {
            mNestedViews.setBitmapCache(bitmapCache);
        }

        @java.lang.Override
        public int mergeBehavior() {
            return android.widget.RemoteViews.Action.MERGE_APPEND;
        }

        @java.lang.Override
        public boolean prefersAsyncApply() {
            return mNestedViews.prefersAsyncApply();
        }

        @java.lang.Override
        public int getActionTag() {
            return android.widget.RemoteViews.VIEW_GROUP_ACTION_ADD_TAG;
        }
    }

    /**
     * ViewGroup methods related to removing child views.
     */
    private class ViewGroupActionRemove extends android.widget.RemoteViews.Action {
        /**
         * Id that indicates that all child views of the affected ViewGroup should be removed.
         *
         * <p>Using -2 because the default id is -1. This avoids accidentally matching that.
         */
        private static final int REMOVE_ALL_VIEWS_ID = -2;

        private int mViewIdToKeep;

        ViewGroupActionRemove(int viewId) {
            this(viewId, android.widget.RemoteViews.ViewGroupActionRemove.REMOVE_ALL_VIEWS_ID);
        }

        ViewGroupActionRemove(int viewId, int viewIdToKeep) {
            this.viewId = viewId;
            mViewIdToKeep = viewIdToKeep;
        }

        ViewGroupActionRemove(android.os.Parcel parcel) {
            viewId = parcel.readInt();
            mViewIdToKeep = parcel.readInt();
        }

        public void writeToParcel(android.os.Parcel dest, int flags) {
            dest.writeInt(viewId);
            dest.writeInt(mViewIdToKeep);
        }

        @java.lang.Override
        public void apply(android.view.View root, android.view.ViewGroup rootParent, android.widget.RemoteViews.OnClickHandler handler) {
            final android.view.ViewGroup target = root.findViewById(viewId);
            if (target == null) {
                return;
            }
            if (mViewIdToKeep == android.widget.RemoteViews.ViewGroupActionRemove.REMOVE_ALL_VIEWS_ID) {
                target.removeAllViews();
                return;
            }
            removeAllViewsExceptIdToKeep(target);
        }

        @java.lang.Override
        public android.widget.RemoteViews.Action initActionAsync(android.widget.RemoteViews.ViewTree root, android.view.ViewGroup rootParent, android.widget.RemoteViews.OnClickHandler handler) {
            // In the async implementation, update the view tree so that subsequent calls to
            // findViewById return the current view.
            root.createTree();
            android.widget.RemoteViews.ViewTree target = root.findViewTreeById(viewId);
            if ((target == null) || (!(target.mRoot instanceof android.view.ViewGroup))) {
                return android.widget.RemoteViews.ACTION_NOOP;
            }
            final android.view.ViewGroup targetVg = ((android.view.ViewGroup) (target.mRoot));
            // Clear all children when nested views omitted
            target.mChildren = null;
            return new android.widget.RemoteViews.RuntimeAction() {
                @java.lang.Override
                public void apply(android.view.View root, android.view.ViewGroup rootParent, android.widget.RemoteViews.OnClickHandler handler) throws android.widget.RemoteViews.ActionException {
                    if (mViewIdToKeep == android.widget.RemoteViews.ViewGroupActionRemove.REMOVE_ALL_VIEWS_ID) {
                        targetVg.removeAllViews();
                        return;
                    }
                    removeAllViewsExceptIdToKeep(targetVg);
                }
            };
        }

        /**
         * Iterates through the children in the given ViewGroup and removes all the views that
         * do not have an id of {@link #mViewIdToKeep}.
         */
        private void removeAllViewsExceptIdToKeep(android.view.ViewGroup viewGroup) {
            // Otherwise, remove all the views that do not match the id to keep.
            int index = viewGroup.getChildCount() - 1;
            while (index >= 0) {
                if (viewGroup.getChildAt(index).getId() != mViewIdToKeep) {
                    viewGroup.removeViewAt(index);
                }
                index--;
            } 
        }

        @java.lang.Override
        public int getActionTag() {
            return android.widget.RemoteViews.VIEW_GROUP_ACTION_REMOVE_TAG;
        }

        @java.lang.Override
        public int mergeBehavior() {
            return android.widget.RemoteViews.Action.MERGE_APPEND;
        }
    }

    /**
     * Helper action to set compound drawables on a TextView. Supports relative
     * (s/t/e/b) or cardinal (l/t/r/b) arrangement.
     */
    private class TextViewDrawableAction extends android.widget.RemoteViews.Action {
        public TextViewDrawableAction(int viewId, boolean isRelative, int d1, int d2, int d3, int d4) {
            this.viewId = viewId;
            this.isRelative = isRelative;
            this.useIcons = false;
            this.d1 = d1;
            this.d2 = d2;
            this.d3 = d3;
            this.d4 = d4;
        }

        public TextViewDrawableAction(int viewId, boolean isRelative, android.graphics.drawable.Icon i1, android.graphics.drawable.Icon i2, android.graphics.drawable.Icon i3, android.graphics.drawable.Icon i4) {
            this.viewId = viewId;
            this.isRelative = isRelative;
            this.useIcons = true;
            this.i1 = i1;
            this.i2 = i2;
            this.i3 = i3;
            this.i4 = i4;
        }

        public TextViewDrawableAction(android.os.Parcel parcel) {
            viewId = parcel.readInt();
            isRelative = parcel.readInt() != 0;
            useIcons = parcel.readInt() != 0;
            if (useIcons) {
                i1 = parcel.readTypedObject(android.graphics.drawable.Icon.this.CREATOR);
                i2 = parcel.readTypedObject(android.graphics.drawable.Icon.this.CREATOR);
                i3 = parcel.readTypedObject(android.graphics.drawable.Icon.this.CREATOR);
                i4 = parcel.readTypedObject(android.graphics.drawable.Icon.this.CREATOR);
            } else {
                d1 = parcel.readInt();
                d2 = parcel.readInt();
                d3 = parcel.readInt();
                d4 = parcel.readInt();
            }
        }

        public void writeToParcel(android.os.Parcel dest, int flags) {
            dest.writeInt(viewId);
            dest.writeInt(isRelative ? 1 : 0);
            dest.writeInt(useIcons ? 1 : 0);
            if (useIcons) {
                dest.writeTypedObject(i1, 0);
                dest.writeTypedObject(i2, 0);
                dest.writeTypedObject(i3, 0);
                dest.writeTypedObject(i4, 0);
            } else {
                dest.writeInt(d1);
                dest.writeInt(d2);
                dest.writeInt(d3);
                dest.writeInt(d4);
            }
        }

        @java.lang.Override
        public void apply(android.view.View root, android.view.ViewGroup rootParent, android.widget.RemoteViews.OnClickHandler handler) {
            final android.widget.TextView target = root.findViewById(viewId);
            if (target == null)
                return;

            if (drawablesLoaded) {
                if (isRelative) {
                    target.setCompoundDrawablesRelativeWithIntrinsicBounds(id1, id2, id3, id4);
                } else {
                    target.setCompoundDrawablesWithIntrinsicBounds(id1, id2, id3, id4);
                }
            } else
                if (useIcons) {
                    final android.content.Context ctx = target.getContext();
                    final android.graphics.drawable.Drawable id1 = (i1 == null) ? null : i1.loadDrawable(ctx);
                    final android.graphics.drawable.Drawable id2 = (i2 == null) ? null : i2.loadDrawable(ctx);
                    final android.graphics.drawable.Drawable id3 = (i3 == null) ? null : i3.loadDrawable(ctx);
                    final android.graphics.drawable.Drawable id4 = (i4 == null) ? null : i4.loadDrawable(ctx);
                    if (isRelative) {
                        target.setCompoundDrawablesRelativeWithIntrinsicBounds(id1, id2, id3, id4);
                    } else {
                        target.setCompoundDrawablesWithIntrinsicBounds(id1, id2, id3, id4);
                    }
                } else {
                    if (isRelative) {
                        target.setCompoundDrawablesRelativeWithIntrinsicBounds(d1, d2, d3, d4);
                    } else {
                        target.setCompoundDrawablesWithIntrinsicBounds(d1, d2, d3, d4);
                    }
                }

        }

        @java.lang.Override
        public android.widget.RemoteViews.Action initActionAsync(android.widget.RemoteViews.ViewTree root, android.view.ViewGroup rootParent, android.widget.RemoteViews.OnClickHandler handler) {
            final android.widget.TextView target = root.findViewById(viewId);
            if (target == null)
                return android.widget.RemoteViews.ACTION_NOOP;

            android.widget.RemoteViews.TextViewDrawableAction copy = (useIcons) ? new android.widget.RemoteViews.TextViewDrawableAction(viewId, isRelative, i1, i2, i3, i4) : new android.widget.RemoteViews.TextViewDrawableAction(viewId, isRelative, d1, d2, d3, d4);
            // Load the drawables on the background thread.
            copy.drawablesLoaded = true;
            final android.content.Context ctx = target.getContext();
            if (useIcons) {
                copy.id1 = (i1 == null) ? null : i1.loadDrawable(ctx);
                copy.id2 = (i2 == null) ? null : i2.loadDrawable(ctx);
                copy.id3 = (i3 == null) ? null : i3.loadDrawable(ctx);
                copy.id4 = (i4 == null) ? null : i4.loadDrawable(ctx);
            } else {
                copy.id1 = (d1 == 0) ? null : ctx.getDrawable(d1);
                copy.id2 = (d2 == 0) ? null : ctx.getDrawable(d2);
                copy.id3 = (d3 == 0) ? null : ctx.getDrawable(d3);
                copy.id4 = (d4 == 0) ? null : ctx.getDrawable(d4);
            }
            return copy;
        }

        @java.lang.Override
        public boolean prefersAsyncApply() {
            return useIcons;
        }

        @java.lang.Override
        public int getActionTag() {
            return android.widget.RemoteViews.TEXT_VIEW_DRAWABLE_ACTION_TAG;
        }

        @java.lang.Override
        public void visitUris(@android.annotation.NonNull
        java.util.function.Consumer<android.net.Uri> visitor) {
            if (useIcons) {
                android.widget.RemoteViews.visitIconUri(i1, visitor);
                android.widget.RemoteViews.visitIconUri(i2, visitor);
                android.widget.RemoteViews.visitIconUri(i3, visitor);
                android.widget.RemoteViews.visitIconUri(i4, visitor);
            }
        }

        boolean isRelative = false;

        boolean useIcons = false;

        int d1;

        int d2;

        int d3;

        int d4;

        android.graphics.drawable.Icon i1;

        android.graphics.drawable.Icon i2;

        android.graphics.drawable.Icon i3;

        android.graphics.drawable.Icon i4;

        boolean drawablesLoaded = false;

        android.graphics.drawable.Drawable id1;

        android.graphics.drawable.Drawable id2;

        android.graphics.drawable.Drawable id3;

        android.graphics.drawable.Drawable id4;
    }

    /**
     * Helper action to set text size on a TextView in any supported units.
     */
    private class TextViewSizeAction extends android.widget.RemoteViews.Action {
        public TextViewSizeAction(int viewId, int units, float size) {
            this.viewId = viewId;
            this.units = units;
            this.size = size;
        }

        public TextViewSizeAction(android.os.Parcel parcel) {
            viewId = parcel.readInt();
            units = parcel.readInt();
            size = parcel.readFloat();
        }

        public void writeToParcel(android.os.Parcel dest, int flags) {
            dest.writeInt(viewId);
            dest.writeInt(units);
            dest.writeFloat(size);
        }

        @java.lang.Override
        public void apply(android.view.View root, android.view.ViewGroup rootParent, android.widget.RemoteViews.OnClickHandler handler) {
            final android.widget.TextView target = root.findViewById(viewId);
            if (target == null)
                return;

            target.setTextSize(units, size);
        }

        @java.lang.Override
        public int getActionTag() {
            return android.widget.RemoteViews.TEXT_VIEW_SIZE_ACTION_TAG;
        }

        int units;

        float size;
    }

    /**
     * Helper action to set padding on a View.
     */
    private class ViewPaddingAction extends android.widget.RemoteViews.Action {
        public ViewPaddingAction(int viewId, int left, int top, int right, int bottom) {
            this.viewId = viewId;
            this.left = left;
            this.top = top;
            this.right = right;
            this.bottom = bottom;
        }

        public ViewPaddingAction(android.os.Parcel parcel) {
            viewId = parcel.readInt();
            left = parcel.readInt();
            top = parcel.readInt();
            right = parcel.readInt();
            bottom = parcel.readInt();
        }

        public void writeToParcel(android.os.Parcel dest, int flags) {
            dest.writeInt(viewId);
            dest.writeInt(left);
            dest.writeInt(top);
            dest.writeInt(right);
            dest.writeInt(bottom);
        }

        @java.lang.Override
        public void apply(android.view.View root, android.view.ViewGroup rootParent, android.widget.RemoteViews.OnClickHandler handler) {
            final android.view.View target = root.findViewById(viewId);
            if (target == null)
                return;

            target.setPadding(left, top, right, bottom);
        }

        @java.lang.Override
        public int getActionTag() {
            return android.widget.RemoteViews.VIEW_PADDING_ACTION_TAG;
        }

        int left;

        int top;

        int right;

        int bottom;
    }

    /**
     * Helper action to set layout params on a View.
     */
    private static class LayoutParamAction extends android.widget.RemoteViews.Action {
        /**
         * Set marginEnd
         */
        public static final int LAYOUT_MARGIN_END_DIMEN = 1;

        /**
         * Set width
         */
        public static final int LAYOUT_WIDTH = 2;

        public static final int LAYOUT_MARGIN_BOTTOM_DIMEN = 3;

        public static final int LAYOUT_MARGIN_END = 4;

        final int mProperty;

        final int mValue;

        /**
         *
         *
         * @param viewId
         * 		ID of the view alter
         * @param property
         * 		which layout parameter to alter
         * @param value
         * 		new value of the layout parameter
         */
        public LayoutParamAction(int viewId, int property, int value) {
            this.viewId = viewId;
            this.mProperty = property;
            this.mValue = value;
        }

        public LayoutParamAction(android.os.Parcel parcel) {
            viewId = parcel.readInt();
            mProperty = parcel.readInt();
            mValue = parcel.readInt();
        }

        public void writeToParcel(android.os.Parcel dest, int flags) {
            dest.writeInt(viewId);
            dest.writeInt(mProperty);
            dest.writeInt(mValue);
        }

        @java.lang.Override
        public void apply(android.view.View root, android.view.ViewGroup rootParent, android.widget.RemoteViews.OnClickHandler handler) {
            final android.view.View target = root.findViewById(viewId);
            if (target == null) {
                return;
            }
            android.view.ViewGroup.LayoutParams layoutParams = target.getLayoutParams();
            if (layoutParams == null) {
                return;
            }
            int value = mValue;
            switch (mProperty) {
                case android.widget.RemoteViews.LayoutParamAction.LAYOUT_MARGIN_END_DIMEN :
                    value = android.widget.RemoteViews.LayoutParamAction.resolveDimenPixelOffset(target, mValue);
                    // fall-through
                case android.widget.RemoteViews.LayoutParamAction.LAYOUT_MARGIN_END :
                    if (layoutParams instanceof android.view.ViewGroup.MarginLayoutParams) {
                        ((android.view.ViewGroup.MarginLayoutParams) (layoutParams)).setMarginEnd(value);
                        target.setLayoutParams(layoutParams);
                    }
                    break;
                case android.widget.RemoteViews.LayoutParamAction.LAYOUT_MARGIN_BOTTOM_DIMEN :
                    if (layoutParams instanceof android.view.ViewGroup.MarginLayoutParams) {
                        int resolved = android.widget.RemoteViews.LayoutParamAction.resolveDimenPixelOffset(target, mValue);
                        ((android.view.ViewGroup.MarginLayoutParams) (layoutParams)).bottomMargin = resolved;
                        target.setLayoutParams(layoutParams);
                    }
                    break;
                case android.widget.RemoteViews.LayoutParamAction.LAYOUT_WIDTH :
                    layoutParams.width = mValue;
                    target.setLayoutParams(layoutParams);
                    break;
                default :
                    throw new java.lang.IllegalArgumentException("Unknown property " + mProperty);
            }
        }

        private static int resolveDimenPixelOffset(android.view.View target, int value) {
            if (value == 0) {
                return 0;
            }
            return target.getContext().getResources().getDimensionPixelOffset(value);
        }

        @java.lang.Override
        public int getActionTag() {
            return android.widget.RemoteViews.LAYOUT_PARAM_ACTION_TAG;
        }

        @java.lang.Override
        public java.lang.String getUniqueKey() {
            return super.getUniqueKey() + mProperty;
        }
    }

    /**
     * Helper action to add a view tag with RemoteInputs.
     */
    private class SetRemoteInputsAction extends android.widget.RemoteViews.Action {
        public SetRemoteInputsAction(int viewId, android.app.RemoteInput[] remoteInputs) {
            this.viewId = viewId;
            this.remoteInputs = remoteInputs;
        }

        public SetRemoteInputsAction(android.os.Parcel parcel) {
            viewId = parcel.readInt();
            remoteInputs = parcel.createTypedArray(RemoteInput.CREATOR);
        }

        public void writeToParcel(android.os.Parcel dest, int flags) {
            dest.writeInt(viewId);
            dest.writeTypedArray(remoteInputs, flags);
        }

        @java.lang.Override
        public void apply(android.view.View root, android.view.ViewGroup rootParent, android.widget.RemoteViews.OnClickHandler handler) {
            final android.view.View target = root.findViewById(viewId);
            if (target == null)
                return;

            target.setTagInternal(R.id.remote_input_tag, remoteInputs);
        }

        @java.lang.Override
        public int getActionTag() {
            return android.widget.RemoteViews.SET_REMOTE_INPUTS_ACTION_TAG;
        }

        final android.os.Parcelable[] remoteInputs;
    }

    /**
     * Helper action to override all textViewColors
     */
    private class OverrideTextColorsAction extends android.widget.RemoteViews.Action {
        private final int textColor;

        public OverrideTextColorsAction(int textColor) {
            this.textColor = textColor;
        }

        public OverrideTextColorsAction(android.os.Parcel parcel) {
            textColor = parcel.readInt();
        }

        public void writeToParcel(android.os.Parcel dest, int flags) {
            dest.writeInt(textColor);
        }

        @java.lang.Override
        public void apply(android.view.View root, android.view.ViewGroup rootParent, android.widget.RemoteViews.OnClickHandler handler) {
            // Let's traverse the viewtree and override all textColors!
            java.util.Stack<android.view.View> viewsToProcess = new java.util.Stack<>();
            viewsToProcess.add(root);
            while (!viewsToProcess.isEmpty()) {
                android.view.View v = viewsToProcess.pop();
                if (v instanceof android.widget.TextView) {
                    android.widget.TextView textView = ((android.widget.TextView) (v));
                    textView.setText(com.android.internal.util.ContrastColorUtil.clearColorSpans(textView.getText()));
                    textView.setTextColor(textColor);
                }
                if (v instanceof android.view.ViewGroup) {
                    android.view.ViewGroup viewGroup = ((android.view.ViewGroup) (v));
                    for (int i = 0; i < viewGroup.getChildCount(); i++) {
                        viewsToProcess.push(viewGroup.getChildAt(i));
                    }
                }
            } 
        }

        @java.lang.Override
        public int getActionTag() {
            return android.widget.RemoteViews.OVERRIDE_TEXT_COLORS_TAG;
        }
    }

    private class SetIntTagAction extends android.widget.RemoteViews.Action {
        private final int mViewId;

        private final int mKey;

        private final int mTag;

        SetIntTagAction(int viewId, int key, int tag) {
            mViewId = viewId;
            mKey = key;
            mTag = tag;
        }

        SetIntTagAction(android.os.Parcel parcel) {
            mViewId = parcel.readInt();
            mKey = parcel.readInt();
            mTag = parcel.readInt();
        }

        public void writeToParcel(android.os.Parcel dest, int flags) {
            dest.writeInt(mViewId);
            dest.writeInt(mKey);
            dest.writeInt(mTag);
        }

        @java.lang.Override
        public void apply(android.view.View root, android.view.ViewGroup rootParent, android.widget.RemoteViews.OnClickHandler handler) {
            final android.view.View target = root.findViewById(mViewId);
            if (target == null)
                return;

            target.setTagInternal(mKey, mTag);
        }

        @java.lang.Override
        public int getActionTag() {
            return android.widget.RemoteViews.SET_INT_TAG_TAG;
        }
    }

    /**
     * Create a new RemoteViews object that will display the views contained
     * in the specified layout file.
     *
     * @param packageName
     * 		Name of the package that contains the layout resource
     * @param layoutId
     * 		The id of the layout resource
     */
    public RemoteViews(java.lang.String packageName, int layoutId) {
        this(android.widget.RemoteViews.getApplicationInfo(packageName, android.os.UserHandle.myUserId()), layoutId);
    }

    /**
     * Create a new RemoteViews object that will display the views contained
     * in the specified layout file.
     *
     * @param packageName
     * 		Name of the package that contains the layout resource.
     * @param userId
     * 		The user under which the package is running.
     * @param layoutId
     * 		The id of the layout resource.
     * @unknown 
     */
    public RemoteViews(java.lang.String packageName, int userId, @android.annotation.LayoutRes
    int layoutId) {
        this(android.widget.RemoteViews.getApplicationInfo(packageName, userId), layoutId);
    }

    /**
     * Create a new RemoteViews object that will display the views contained
     * in the specified layout file.
     *
     * @param application
     * 		The application whose content is shown by the views.
     * @param layoutId
     * 		The id of the layout resource.
     * @unknown 
     */
    protected RemoteViews(android.content.pm.ApplicationInfo application, @android.annotation.LayoutRes
    int layoutId) {
        mApplication = application;
        mLayoutId = layoutId;
        mBitmapCache = new android.widget.RemoteViews.BitmapCache();
        mClassCookies = null;
    }

    private boolean hasLandscapeAndPortraitLayouts() {
        return (mLandscape != null) && (mPortrait != null);
    }

    /**
     * Create a new RemoteViews object that will inflate as the specified
     * landspace or portrait RemoteViews, depending on the current configuration.
     *
     * @param landscape
     * 		The RemoteViews to inflate in landscape configuration
     * @param portrait
     * 		The RemoteViews to inflate in portrait configuration
     */
    public RemoteViews(android.widget.RemoteViews landscape, android.widget.RemoteViews portrait) {
        if ((landscape == null) || (portrait == null)) {
            throw new java.lang.RuntimeException("Both RemoteViews must be non-null");
        }
        if (!landscape.hasSameAppInfo(portrait.mApplication)) {
            throw new java.lang.RuntimeException("Both RemoteViews must share the same package and user");
        }
        mApplication = portrait.mApplication;
        mLayoutId = portrait.mLayoutId;
        mLightBackgroundLayoutId = portrait.mLightBackgroundLayoutId;
        mLandscape = landscape;
        mPortrait = portrait;
        mBitmapCache = new android.widget.RemoteViews.BitmapCache();
        configureRemoteViewsAsChild(landscape);
        configureRemoteViewsAsChild(portrait);
        mClassCookies = (portrait.mClassCookies != null) ? portrait.mClassCookies : landscape.mClassCookies;
    }

    /**
     * Creates a copy of another RemoteViews.
     */
    public RemoteViews(android.widget.RemoteViews src) {
        mBitmapCache = src.mBitmapCache;
        mApplication = src.mApplication;
        mIsRoot = src.mIsRoot;
        mLayoutId = src.mLayoutId;
        mLightBackgroundLayoutId = src.mLightBackgroundLayoutId;
        mApplyFlags = src.mApplyFlags;
        mClassCookies = src.mClassCookies;
        if (src.hasLandscapeAndPortraitLayouts()) {
            mLandscape = new android.widget.RemoteViews(src.mLandscape);
            mPortrait = new android.widget.RemoteViews(src.mPortrait);
        }
        if (src.mActions != null) {
            android.os.Parcel p = android.os.Parcel.obtain();
            p.putClassCookies(mClassCookies);
            src.writeActionsToParcel(p);
            p.setDataPosition(0);
            // Since src is already in memory, we do not care about stack overflow as it has
            // already been read once.
            readActionsFromParcel(p, 0);
            p.recycle();
        }
        // Now that everything is initialized and duplicated, setting a new BitmapCache will
        // re-initialize the cache.
        setBitmapCache(new android.widget.RemoteViews.BitmapCache());
    }

    /**
     * Reads a RemoteViews object from a parcel.
     *
     * @param parcel
     * 		
     */
    public RemoteViews(android.os.Parcel parcel) {
        this(parcel, null, null, 0, null);
    }

    private RemoteViews(android.os.Parcel parcel, android.widget.RemoteViews.BitmapCache bitmapCache, android.content.pm.ApplicationInfo info, int depth, java.util.Map<java.lang.Class, java.lang.Object> classCookies) {
        if ((depth > android.widget.RemoteViews.MAX_NESTED_VIEWS) && (android.os.UserHandle.getAppId(android.os.Binder.getCallingUid()) != SYSTEM_UID)) {
            throw new java.lang.IllegalArgumentException("Too many nested views.");
        }
        depth++;
        int mode = parcel.readInt();
        // We only store a bitmap cache in the root of the RemoteViews.
        if (bitmapCache == null) {
            mBitmapCache = new android.widget.RemoteViews.BitmapCache(parcel);
            // Store the class cookies such that they are available when we clone this RemoteView.
            mClassCookies = parcel.copyClassCookies();
        } else {
            setBitmapCache(bitmapCache);
            mClassCookies = classCookies;
            setNotRoot();
        }
        if (mode == android.widget.RemoteViews.MODE_NORMAL) {
            mApplication = (parcel.readInt() == 0) ? info : this.CREATOR.createFromParcel(parcel);
            mLayoutId = parcel.readInt();
            mLightBackgroundLayoutId = parcel.readInt();
            readActionsFromParcel(parcel, depth);
        } else {
            // MODE_HAS_LANDSCAPE_AND_PORTRAIT
            mLandscape = new android.widget.RemoteViews(parcel, mBitmapCache, info, depth, mClassCookies);
            mPortrait = new android.widget.RemoteViews(parcel, mBitmapCache, mLandscape.mApplication, depth, mClassCookies);
            mApplication = mPortrait.mApplication;
            mLayoutId = mPortrait.mLayoutId;
            mLightBackgroundLayoutId = mPortrait.mLightBackgroundLayoutId;
        }
        mApplyFlags = parcel.readInt();
    }

    private void readActionsFromParcel(android.os.Parcel parcel, int depth) {
        int count = parcel.readInt();
        if (count > 0) {
            mActions = new java.util.ArrayList<>(count);
            for (int i = 0; i < count; i++) {
                mActions.add(getActionFromParcel(parcel, depth));
            }
        }
    }

    private android.widget.RemoteViews.Action getActionFromParcel(android.os.Parcel parcel, int depth) {
        int tag = parcel.readInt();
        switch (tag) {
            case android.widget.RemoteViews.SET_ON_CLICK_RESPONSE_TAG :
                return new android.widget.RemoteViews.SetOnClickResponse(parcel);
            case android.widget.RemoteViews.SET_DRAWABLE_TINT_TAG :
                return new android.widget.RemoteViews.SetDrawableTint(parcel);
            case android.widget.RemoteViews.REFLECTION_ACTION_TAG :
                return new android.widget.RemoteViews.ReflectionAction(parcel);
            case android.widget.RemoteViews.VIEW_GROUP_ACTION_ADD_TAG :
                return new android.widget.RemoteViews.ViewGroupActionAdd(parcel, mBitmapCache, mApplication, depth, mClassCookies);
            case android.widget.RemoteViews.VIEW_GROUP_ACTION_REMOVE_TAG :
                return new android.widget.RemoteViews.ViewGroupActionRemove(parcel);
            case android.widget.RemoteViews.VIEW_CONTENT_NAVIGATION_TAG :
                return new android.widget.RemoteViews.ViewContentNavigation(parcel);
            case android.widget.RemoteViews.SET_EMPTY_VIEW_ACTION_TAG :
                return new android.widget.RemoteViews.SetEmptyView(parcel);
            case android.widget.RemoteViews.SET_PENDING_INTENT_TEMPLATE_TAG :
                return new android.widget.RemoteViews.SetPendingIntentTemplate(parcel);
            case android.widget.RemoteViews.SET_REMOTE_VIEW_ADAPTER_INTENT_TAG :
                return new android.widget.RemoteViews.SetRemoteViewsAdapterIntent(parcel);
            case android.widget.RemoteViews.TEXT_VIEW_DRAWABLE_ACTION_TAG :
                return new android.widget.RemoteViews.TextViewDrawableAction(parcel);
            case android.widget.RemoteViews.TEXT_VIEW_SIZE_ACTION_TAG :
                return new android.widget.RemoteViews.TextViewSizeAction(parcel);
            case android.widget.RemoteViews.VIEW_PADDING_ACTION_TAG :
                return new android.widget.RemoteViews.ViewPaddingAction(parcel);
            case android.widget.RemoteViews.BITMAP_REFLECTION_ACTION_TAG :
                return new android.widget.RemoteViews.BitmapReflectionAction(parcel);
            case android.widget.RemoteViews.SET_REMOTE_VIEW_ADAPTER_LIST_TAG :
                return new android.widget.RemoteViews.SetRemoteViewsAdapterList(parcel);
            case android.widget.RemoteViews.SET_REMOTE_INPUTS_ACTION_TAG :
                return new android.widget.RemoteViews.SetRemoteInputsAction(parcel);
            case android.widget.RemoteViews.LAYOUT_PARAM_ACTION_TAG :
                return new android.widget.RemoteViews.LayoutParamAction(parcel);
            case android.widget.RemoteViews.OVERRIDE_TEXT_COLORS_TAG :
                return new android.widget.RemoteViews.OverrideTextColorsAction(parcel);
            case android.widget.RemoteViews.SET_RIPPLE_DRAWABLE_COLOR_TAG :
                return new android.widget.RemoteViews.SetRippleDrawableColor(parcel);
            case android.widget.RemoteViews.SET_INT_TAG_TAG :
                return new android.widget.RemoteViews.SetIntTagAction(parcel);
            default :
                throw new android.widget.RemoteViews.ActionException(("Tag " + tag) + " not found");
        }
    }

    /**
     * Returns a deep copy of the RemoteViews object. The RemoteView may not be
     * attached to another RemoteView -- it must be the root of a hierarchy.
     *
     * @deprecated use {@link #RemoteViews(RemoteViews)} instead.
     * @throws IllegalStateException
     * 		if this is not the root of a RemoteView
     * 		hierarchy
     */
    @java.lang.Override
    @java.lang.Deprecated
    public android.widget.RemoteViews clone() {
        com.android.internal.util.Preconditions.checkState(mIsRoot, "RemoteView has been attached to another RemoteView. " + "May only clone the root of a RemoteView hierarchy.");
        return new android.widget.RemoteViews(this);
    }

    public java.lang.String getPackage() {
        return mApplication != null ? mApplication.packageName : null;
    }

    /**
     * Returns the layout id of the root layout associated with this RemoteViews. In the case
     * that the RemoteViews has both a landscape and portrait root, this will return the layout
     * id associated with the portrait layout.
     *
     * @return the layout id.
     */
    public int getLayoutId() {
        return hasFlags(android.widget.RemoteViews.FLAG_USE_LIGHT_BACKGROUND_LAYOUT) && (mLightBackgroundLayoutId != 0) ? mLightBackgroundLayoutId : mLayoutId;
    }

    /**
     * Recursively sets BitmapCache in the hierarchy and update the bitmap ids.
     */
    private void setBitmapCache(android.widget.RemoteViews.BitmapCache bitmapCache) {
        mBitmapCache = bitmapCache;
        if (!hasLandscapeAndPortraitLayouts()) {
            if (mActions != null) {
                final int count = mActions.size();
                for (int i = 0; i < count; ++i) {
                    mActions.get(i).setBitmapCache(bitmapCache);
                }
            }
        } else {
            mLandscape.setBitmapCache(bitmapCache);
            mPortrait.setBitmapCache(bitmapCache);
        }
    }

    /**
     * Returns an estimate of the bitmap heap memory usage for this RemoteViews.
     */
    /**
     *
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public int estimateMemoryUsage() {
        return mBitmapCache.getBitmapMemory();
    }

    /**
     * Add an action to be executed on the remote side when apply is called.
     *
     * @param a
     * 		The action to add
     */
    private void addAction(android.widget.RemoteViews.Action a) {
        if (hasLandscapeAndPortraitLayouts()) {
            throw new java.lang.RuntimeException("RemoteViews specifying separate landscape and portrait" + (" layouts cannot be modified. Instead, fully configure the landscape and" + " portrait layouts individually before constructing the combined layout."));
        }
        if (mActions == null) {
            mActions = new java.util.ArrayList<>();
        }
        mActions.add(a);
    }

    /**
     * Equivalent to calling {@link ViewGroup#addView(View)} after inflating the
     * given {@link RemoteViews}. This allows users to build "nested"
     * {@link RemoteViews}. In cases where consumers of {@link RemoteViews} may
     * recycle layouts, use {@link #removeAllViews(int)} to clear any existing
     * children.
     *
     * @param viewId
     * 		The id of the parent {@link ViewGroup} to add child into.
     * @param nestedView
     * 		{@link RemoteViews} that describes the child.
     */
    public void addView(int viewId, android.widget.RemoteViews nestedView) {
        addAction(nestedView == null ? new android.widget.RemoteViews.ViewGroupActionRemove(viewId) : new android.widget.RemoteViews.ViewGroupActionAdd(viewId, nestedView));
    }

    /**
     * Equivalent to calling {@link ViewGroup#addView(View, int)} after inflating the
     * given {@link RemoteViews}.
     *
     * @param viewId
     * 		The id of the parent {@link ViewGroup} to add the child into.
     * @param nestedView
     * 		{@link RemoteViews} of the child to add.
     * @param index
     * 		The position at which to add the child.
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public void addView(int viewId, android.widget.RemoteViews nestedView, int index) {
        addAction(new android.widget.RemoteViews.ViewGroupActionAdd(viewId, nestedView, index));
    }

    /**
     * Equivalent to calling {@link ViewGroup#removeAllViews()}.
     *
     * @param viewId
     * 		The id of the parent {@link ViewGroup} to remove all
     * 		children from.
     */
    public void removeAllViews(int viewId) {
        addAction(new android.widget.RemoteViews.ViewGroupActionRemove(viewId));
    }

    /**
     * Removes all views in the {@link ViewGroup} specified by the {@code viewId} except for any
     * child that has the {@code viewIdToKeep} as its id.
     *
     * @param viewId
     * 		The id of the parent {@link ViewGroup} to remove children from.
     * @param viewIdToKeep
     * 		The id of a child that should not be removed.
     * @unknown 
     */
    public void removeAllViewsExceptId(int viewId, int viewIdToKeep) {
        addAction(new android.widget.RemoteViews.ViewGroupActionRemove(viewId, viewIdToKeep));
    }

    /**
     * Equivalent to calling {@link AdapterViewAnimator#showNext()}
     *
     * @param viewId
     * 		The id of the view on which to call {@link AdapterViewAnimator#showNext()}
     */
    public void showNext(int viewId) {
        addAction(/* next */
        new android.widget.RemoteViews.ViewContentNavigation(viewId, true));
    }

    /**
     * Equivalent to calling {@link AdapterViewAnimator#showPrevious()}
     *
     * @param viewId
     * 		The id of the view on which to call {@link AdapterViewAnimator#showPrevious()}
     */
    public void showPrevious(int viewId) {
        addAction(/* next */
        new android.widget.RemoteViews.ViewContentNavigation(viewId, false));
    }

    /**
     * Equivalent to calling {@link AdapterViewAnimator#setDisplayedChild(int)}
     *
     * @param viewId
     * 		The id of the view on which to call
     * 		{@link AdapterViewAnimator#setDisplayedChild(int)}
     */
    public void setDisplayedChild(int viewId, int childIndex) {
        setInt(viewId, "setDisplayedChild", childIndex);
    }

    /**
     * Equivalent to calling {@link View#setVisibility(int)}
     *
     * @param viewId
     * 		The id of the view whose visibility should change
     * @param visibility
     * 		The new visibility for the view
     */
    public void setViewVisibility(int viewId, int visibility) {
        setInt(viewId, "setVisibility", visibility);
    }

    /**
     * Equivalent to calling {@link TextView#setText(CharSequence)}
     *
     * @param viewId
     * 		The id of the view whose text should change
     * @param text
     * 		The new text for the view
     */
    public void setTextViewText(int viewId, java.lang.CharSequence text) {
        setCharSequence(viewId, "setText", text);
    }

    /**
     * Equivalent to calling {@link TextView#setTextSize(int, float)}
     *
     * @param viewId
     * 		The id of the view whose text size should change
     * @param units
     * 		The units of size (e.g. COMPLEX_UNIT_SP)
     * @param size
     * 		The size of the text
     */
    public void setTextViewTextSize(int viewId, int units, float size) {
        addAction(new android.widget.RemoteViews.TextViewSizeAction(viewId, units, size));
    }

    /**
     * Equivalent to calling
     * {@link TextView#setCompoundDrawablesWithIntrinsicBounds(int, int, int, int)}.
     *
     * @param viewId
     * 		The id of the view whose text should change
     * @param left
     * 		The id of a drawable to place to the left of the text, or 0
     * @param top
     * 		The id of a drawable to place above the text, or 0
     * @param right
     * 		The id of a drawable to place to the right of the text, or 0
     * @param bottom
     * 		The id of a drawable to place below the text, or 0
     */
    public void setTextViewCompoundDrawables(int viewId, int left, int top, int right, int bottom) {
        addAction(new android.widget.RemoteViews.TextViewDrawableAction(viewId, false, left, top, right, bottom));
    }

    /**
     * Equivalent to calling {@link TextView#setCompoundDrawablesRelativeWithIntrinsicBounds(int, int, int, int)}.
     *
     * @param viewId
     * 		The id of the view whose text should change
     * @param start
     * 		The id of a drawable to place before the text (relative to the
     * 		layout direction), or 0
     * @param top
     * 		The id of a drawable to place above the text, or 0
     * @param end
     * 		The id of a drawable to place after the text, or 0
     * @param bottom
     * 		The id of a drawable to place below the text, or 0
     */
    public void setTextViewCompoundDrawablesRelative(int viewId, int start, int top, int end, int bottom) {
        addAction(new android.widget.RemoteViews.TextViewDrawableAction(viewId, true, start, top, end, bottom));
    }

    /**
     * Equivalent to calling {@link TextView#setCompoundDrawablesWithIntrinsicBounds(Drawable, Drawable, Drawable, Drawable)}
     * using the drawables yielded by {@link Icon#loadDrawable(Context)}.
     *
     * @param viewId
     * 		The id of the view whose text should change
     * @param left
     * 		an Icon to place to the left of the text, or 0
     * @param top
     * 		an Icon to place above the text, or 0
     * @param right
     * 		an Icon to place to the right of the text, or 0
     * @param bottom
     * 		an Icon to place below the text, or 0
     * @unknown 
     */
    public void setTextViewCompoundDrawables(int viewId, android.graphics.drawable.Icon left, android.graphics.drawable.Icon top, android.graphics.drawable.Icon right, android.graphics.drawable.Icon bottom) {
        addAction(new android.widget.RemoteViews.TextViewDrawableAction(viewId, false, left, top, right, bottom));
    }

    /**
     * Equivalent to calling {@link TextView#setCompoundDrawablesRelativeWithIntrinsicBounds(Drawable, Drawable, Drawable, Drawable)}
     * using the drawables yielded by {@link Icon#loadDrawable(Context)}.
     *
     * @param viewId
     * 		The id of the view whose text should change
     * @param start
     * 		an Icon to place before the text (relative to the
     * 		layout direction), or 0
     * @param top
     * 		an Icon to place above the text, or 0
     * @param end
     * 		an Icon to place after the text, or 0
     * @param bottom
     * 		an Icon to place below the text, or 0
     * @unknown 
     */
    public void setTextViewCompoundDrawablesRelative(int viewId, android.graphics.drawable.Icon start, android.graphics.drawable.Icon top, android.graphics.drawable.Icon end, android.graphics.drawable.Icon bottom) {
        addAction(new android.widget.RemoteViews.TextViewDrawableAction(viewId, true, start, top, end, bottom));
    }

    /**
     * Equivalent to calling {@link ImageView#setImageResource(int)}
     *
     * @param viewId
     * 		The id of the view whose drawable should change
     * @param srcId
     * 		The new resource id for the drawable
     */
    public void setImageViewResource(int viewId, int srcId) {
        setInt(viewId, "setImageResource", srcId);
    }

    /**
     * Equivalent to calling {@link ImageView#setImageURI(Uri)}
     *
     * @param viewId
     * 		The id of the view whose drawable should change
     * @param uri
     * 		The Uri for the image
     */
    public void setImageViewUri(int viewId, android.net.Uri uri) {
        setUri(viewId, "setImageURI", uri);
    }

    /**
     * Equivalent to calling {@link ImageView#setImageBitmap(Bitmap)}
     *
     * @param viewId
     * 		The id of the view whose bitmap should change
     * @param bitmap
     * 		The new Bitmap for the drawable
     */
    public void setImageViewBitmap(int viewId, android.graphics.Bitmap bitmap) {
        setBitmap(viewId, "setImageBitmap", bitmap);
    }

    /**
     * Equivalent to calling {@link ImageView#setImageIcon(Icon)}
     *
     * @param viewId
     * 		The id of the view whose bitmap should change
     * @param icon
     * 		The new Icon for the ImageView
     */
    public void setImageViewIcon(int viewId, android.graphics.drawable.Icon icon) {
        setIcon(viewId, "setImageIcon", icon);
    }

    /**
     * Equivalent to calling {@link AdapterView#setEmptyView(View)}
     *
     * @param viewId
     * 		The id of the view on which to set the empty view
     * @param emptyViewId
     * 		The view id of the empty view
     */
    public void setEmptyView(int viewId, int emptyViewId) {
        addAction(new android.widget.RemoteViews.SetEmptyView(viewId, emptyViewId));
    }

    /**
     * Equivalent to calling {@link Chronometer#setBase Chronometer.setBase},
     * {@link Chronometer#setFormat Chronometer.setFormat},
     * and {@link Chronometer#start Chronometer.start()} or
     * {@link Chronometer#stop Chronometer.stop()}.
     *
     * @param viewId
     * 		The id of the {@link Chronometer} to change
     * @param base
     * 		The time at which the timer would have read 0:00.  This
     * 		time should be based off of
     * 		{@link android.os.SystemClock#elapsedRealtime SystemClock.elapsedRealtime()}.
     * @param format
     * 		The Chronometer format string, or null to
     * 		simply display the timer value.
     * @param started
     * 		True if you want the clock to be started, false if not.
     * @see #setChronometerCountDown(int, boolean)
     */
    public void setChronometer(int viewId, long base, java.lang.String format, boolean started) {
        setLong(viewId, "setBase", base);
        setString(viewId, "setFormat", format);
        setBoolean(viewId, "setStarted", started);
    }

    /**
     * Equivalent to calling {@link Chronometer#setCountDown(boolean) Chronometer.setCountDown} on
     * the chronometer with the given viewId.
     *
     * @param viewId
     * 		The id of the {@link Chronometer} to change
     * @param isCountDown
     * 		True if you want the chronometer to count down to base instead of
     * 		counting up.
     */
    public void setChronometerCountDown(int viewId, boolean isCountDown) {
        setBoolean(viewId, "setCountDown", isCountDown);
    }

    /**
     * Equivalent to calling {@link ProgressBar#setMax ProgressBar.setMax},
     * {@link ProgressBar#setProgress ProgressBar.setProgress}, and
     * {@link ProgressBar#setIndeterminate ProgressBar.setIndeterminate}
     *
     * If indeterminate is true, then the values for max and progress are ignored.
     *
     * @param viewId
     * 		The id of the {@link ProgressBar} to change
     * @param max
     * 		The 100% value for the progress bar
     * @param progress
     * 		The current value of the progress bar.
     * @param indeterminate
     * 		True if the progress bar is indeterminate,
     * 		false if not.
     */
    public void setProgressBar(int viewId, int max, int progress, boolean indeterminate) {
        setBoolean(viewId, "setIndeterminate", indeterminate);
        if (!indeterminate) {
            setInt(viewId, "setMax", max);
            setInt(viewId, "setProgress", progress);
        }
    }

    /**
     * Equivalent to calling
     * {@link android.view.View#setOnClickListener(android.view.View.OnClickListener)}
     * to launch the provided {@link PendingIntent}. The source bounds
     * ({@link Intent#getSourceBounds()}) of the intent will be set to the bounds of the clicked
     * view in screen space.
     * Note that any activity options associated with the mPendingIntent may get overridden
     * before starting the intent.
     *
     * When setting the on-click action of items within collections (eg. {@link ListView},
     * {@link StackView} etc.), this method will not work. Instead, use {@link RemoteViews#setPendingIntentTemplate(int, PendingIntent)} in conjunction with
     * {@link RemoteViews#setOnClickFillInIntent(int, Intent)}.
     *
     * @param viewId
     * 		The id of the view that will trigger the {@link PendingIntent} when clicked
     * @param pendingIntent
     * 		The {@link PendingIntent} to send when user clicks
     */
    public void setOnClickPendingIntent(int viewId, android.app.PendingIntent pendingIntent) {
        setOnClickResponse(viewId, android.widget.RemoteViews.RemoteResponse.fromPendingIntent(pendingIntent));
    }

    /**
     * Equivalent of calling
     * {@link android.view.View#setOnClickListener(android.view.View.OnClickListener)}
     * to launch the provided {@link RemoteResponse}.
     *
     * @param viewId
     * 		The id of the view that will trigger the {@link RemoteResponse} when clicked
     * @param response
     * 		The {@link RemoteResponse} to send when user clicks
     */
    public void setOnClickResponse(int viewId, @android.annotation.NonNull
    android.widget.RemoteViews.RemoteResponse response) {
        addAction(new android.widget.RemoteViews.SetOnClickResponse(viewId, response));
    }

    /**
     * When using collections (eg. {@link ListView}, {@link StackView} etc.) in widgets, it is very
     * costly to set PendingIntents on the individual items, and is hence not permitted. Instead
     * this method should be used to set a single PendingIntent template on the collection, and
     * individual items can differentiate their on-click behavior using
     * {@link RemoteViews#setOnClickFillInIntent(int, Intent)}.
     *
     * @param viewId
     * 		The id of the collection who's children will use this PendingIntent template
     * 		when clicked
     * @param pendingIntentTemplate
     * 		The {@link PendingIntent} to be combined with extras specified
     * 		by a child of viewId and executed when that child is clicked
     */
    public void setPendingIntentTemplate(int viewId, android.app.PendingIntent pendingIntentTemplate) {
        addAction(new android.widget.RemoteViews.SetPendingIntentTemplate(viewId, pendingIntentTemplate));
    }

    /**
     * When using collections (eg. {@link ListView}, {@link StackView} etc.) in widgets, it is very
     * costly to set PendingIntents on the individual items, and is hence not permitted. Instead
     * a single PendingIntent template can be set on the collection, see {@link RemoteViews#setPendingIntentTemplate(int, PendingIntent)}, and the individual on-click
     * action of a given item can be distinguished by setting a fillInIntent on that item. The
     * fillInIntent is then combined with the PendingIntent template in order to determine the final
     * intent which will be executed when the item is clicked. This works as follows: any fields
     * which are left blank in the PendingIntent template, but are provided by the fillInIntent
     * will be overwritten, and the resulting PendingIntent will be used. The rest
     * of the PendingIntent template will then be filled in with the associated fields that are
     * set in fillInIntent. See {@link Intent#fillIn(Intent, int)} for more details.
     *
     * @param viewId
     * 		The id of the view on which to set the fillInIntent
     * @param fillInIntent
     * 		The intent which will be combined with the parent's PendingIntent
     * 		in order to determine the on-click behavior of the view specified by viewId
     */
    public void setOnClickFillInIntent(int viewId, android.content.Intent fillInIntent) {
        setOnClickResponse(viewId, android.widget.RemoteViews.RemoteResponse.fromFillInIntent(fillInIntent));
    }

    /**
     *
     *
     * @unknown Equivalent to calling
    {@link Drawable#setColorFilter(int, android.graphics.PorterDuff.Mode)},
    on the {@link Drawable} of a given view.
    <p>
     * @param viewId
     * 		The id of the view that contains the target
     * 		{@link Drawable}
     * @param targetBackground
     * 		If true, apply these parameters to the
     * 		{@link Drawable} returned by
     * 		{@link android.view.View#getBackground()}. Otherwise, assume
     * 		the target view is an {@link ImageView} and apply them to
     * 		{@link ImageView#getDrawable()}.
     * @param colorFilter
     * 		Specify a color for a
     * 		{@link android.graphics.ColorFilter} for this drawable. This will be ignored if
     * 		{@code mode} is {@code null}.
     * @param mode
     * 		Specify a PorterDuff mode for this drawable, or null to leave
     * 		unchanged.
     */
    public void setDrawableTint(int viewId, boolean targetBackground, int colorFilter, @android.annotation.NonNull
    android.graphics.PorterDuff.Mode mode) {
        addAction(new android.widget.RemoteViews.SetDrawableTint(viewId, targetBackground, colorFilter, mode));
    }

    /**
     *
     *
     * @unknown Equivalent to calling
    {@link RippleDrawable#setColor(ColorStateList)} on the {@link Drawable} of a given view,
    assuming it's a {@link RippleDrawable}.
    <p>
     * @param viewId
     * 		The id of the view that contains the target
     * 		{@link RippleDrawable}
     * @param colorStateList
     * 		Specify a color for a
     * 		{@link ColorStateList} for this drawable.
     */
    public void setRippleDrawableColor(int viewId, android.content.res.ColorStateList colorStateList) {
        addAction(new android.widget.RemoteViews.SetRippleDrawableColor(viewId, colorStateList));
    }

    /**
     *
     *
     * @unknown Equivalent to calling {@link android.widget.ProgressBar#setProgressTintList}.
     * @param viewId
     * 		The id of the view whose tint should change
     * @param tint
     * 		the tint to apply, may be {@code null} to clear tint
     */
    public void setProgressTintList(int viewId, android.content.res.ColorStateList tint) {
        addAction(new android.widget.RemoteViews.ReflectionAction(viewId, "setProgressTintList", android.widget.RemoteViews.ReflectionAction.COLOR_STATE_LIST, tint));
    }

    /**
     *
     *
     * @unknown Equivalent to calling {@link android.widget.ProgressBar#setProgressBackgroundTintList}.
     * @param viewId
     * 		The id of the view whose tint should change
     * @param tint
     * 		the tint to apply, may be {@code null} to clear tint
     */
    public void setProgressBackgroundTintList(int viewId, android.content.res.ColorStateList tint) {
        addAction(new android.widget.RemoteViews.ReflectionAction(viewId, "setProgressBackgroundTintList", android.widget.RemoteViews.ReflectionAction.COLOR_STATE_LIST, tint));
    }

    /**
     *
     *
     * @unknown Equivalent to calling {@link android.widget.ProgressBar#setIndeterminateTintList}.
     * @param viewId
     * 		The id of the view whose tint should change
     * @param tint
     * 		the tint to apply, may be {@code null} to clear tint
     */
    public void setProgressIndeterminateTintList(int viewId, android.content.res.ColorStateList tint) {
        addAction(new android.widget.RemoteViews.ReflectionAction(viewId, "setIndeterminateTintList", android.widget.RemoteViews.ReflectionAction.COLOR_STATE_LIST, tint));
    }

    /**
     * Equivalent to calling {@link android.widget.TextView#setTextColor(int)}.
     *
     * @param viewId
     * 		The id of the view whose text color should change
     * @param color
     * 		Sets the text color for all the states (normal, selected,
     * 		focused) to be this color.
     */
    public void setTextColor(int viewId, @android.annotation.ColorInt
    int color) {
        setInt(viewId, "setTextColor", color);
    }

    /**
     *
     *
     * @unknown Equivalent to calling {@link android.widget.TextView#setTextColor(ColorStateList)}.
     * @param viewId
     * 		The id of the view whose text color should change
     * @param colors
     * 		the text colors to set
     */
    public void setTextColor(int viewId, @android.annotation.ColorInt
    android.content.res.ColorStateList colors) {
        addAction(new android.widget.RemoteViews.ReflectionAction(viewId, "setTextColor", android.widget.RemoteViews.ReflectionAction.COLOR_STATE_LIST, colors));
    }

    /**
     * Equivalent to calling {@link android.widget.AbsListView#setRemoteViewsAdapter(Intent)}.
     *
     * @param appWidgetId
     * 		The id of the app widget which contains the specified view. (This
     * 		parameter is ignored in this deprecated method)
     * @param viewId
     * 		The id of the {@link AdapterView}
     * @param intent
     * 		The intent of the service which will be
     * 		providing data to the RemoteViewsAdapter
     * @deprecated This method has been deprecated. See
    {@link android.widget.RemoteViews#setRemoteAdapter(int, Intent)}
     */
    @java.lang.Deprecated
    public void setRemoteAdapter(int appWidgetId, int viewId, android.content.Intent intent) {
        setRemoteAdapter(viewId, intent);
    }

    /**
     * Equivalent to calling {@link android.widget.AbsListView#setRemoteViewsAdapter(Intent)}.
     * Can only be used for App Widgets.
     *
     * @param viewId
     * 		The id of the {@link AdapterView}
     * @param intent
     * 		The intent of the service which will be
     * 		providing data to the RemoteViewsAdapter
     */
    public void setRemoteAdapter(int viewId, android.content.Intent intent) {
        addAction(new android.widget.RemoteViews.SetRemoteViewsAdapterIntent(viewId, intent));
    }

    /**
     * Creates a simple Adapter for the viewId specified. The viewId must point to an AdapterView,
     * ie. {@link ListView}, {@link GridView}, {@link StackView} or {@link AdapterViewAnimator}.
     * This is a simpler but less flexible approach to populating collection widgets. Its use is
     * encouraged for most scenarios, as long as the total memory within the list of RemoteViews
     * is relatively small (ie. doesn't contain large or numerous Bitmaps, see {@link RemoteViews#setImageViewBitmap}). In the case of numerous images, the use of API is still
     * possible by setting image URIs instead of Bitmaps, see {@link RemoteViews#setImageViewUri}.
     *
     * This API is supported in the compatibility library for previous API levels, see
     * RemoteViewsCompat.
     *
     * @param viewId
     * 		The id of the {@link AdapterView}
     * @param list
     * 		The list of RemoteViews which will populate the view specified by viewId.
     * @param viewTypeCount
     * 		The maximum number of unique layout id's used to construct the list of
     * 		RemoteViews. This count cannot change during the life-cycle of a given widget, so this
     * 		parameter should account for the maximum possible number of types that may appear in the
     * 		See {@link Adapter#getViewTypeCount()}.
     * @unknown 
     * @deprecated this appears to have no users outside of UnsupportedAppUsage?
     */
    @android.annotation.UnsupportedAppUsage
    @java.lang.Deprecated
    public void setRemoteAdapter(int viewId, java.util.ArrayList<android.widget.RemoteViews> list, int viewTypeCount) {
        addAction(new android.widget.RemoteViews.SetRemoteViewsAdapterList(viewId, list, viewTypeCount));
    }

    /**
     * Equivalent to calling {@link ListView#smoothScrollToPosition(int)}.
     *
     * @param viewId
     * 		The id of the view to change
     * @param position
     * 		Scroll to this adapter position
     */
    public void setScrollPosition(int viewId, int position) {
        setInt(viewId, "smoothScrollToPosition", position);
    }

    /**
     * Equivalent to calling {@link ListView#smoothScrollByOffset(int)}.
     *
     * @param viewId
     * 		The id of the view to change
     * @param offset
     * 		Scroll by this adapter position offset
     */
    public void setRelativeScrollPosition(int viewId, int offset) {
        setInt(viewId, "smoothScrollByOffset", offset);
    }

    /**
     * Equivalent to calling {@link android.view.View#setPadding(int, int, int, int)}.
     *
     * @param viewId
     * 		The id of the view to change
     * @param left
     * 		the left padding in pixels
     * @param top
     * 		the top padding in pixels
     * @param right
     * 		the right padding in pixels
     * @param bottom
     * 		the bottom padding in pixels
     */
    public void setViewPadding(int viewId, int left, int top, int right, int bottom) {
        addAction(new android.widget.RemoteViews.ViewPaddingAction(viewId, left, top, right, bottom));
    }

    /**
     *
     *
     * @unknown Equivalent to calling {@link android.view.ViewGroup.MarginLayoutParams#setMarginEnd(int)}.
    Only works if the {@link View#getLayoutParams()} supports margins.
    Hidden for now since we don't want to support this for all different layout margins yet.
     * @param viewId
     * 		The id of the view to change
     * @param endMarginDimen
     * 		a dimen resource to read the margin from or 0 to clear the margin.
     */
    public void setViewLayoutMarginEndDimen(int viewId, @android.annotation.DimenRes
    int endMarginDimen) {
        addAction(new android.widget.RemoteViews.LayoutParamAction(viewId, android.widget.RemoteViews.LayoutParamAction.LAYOUT_MARGIN_END_DIMEN, endMarginDimen));
    }

    /**
     * Equivalent to calling {@link android.view.ViewGroup.MarginLayoutParams#setMarginEnd(int)}.
     * Only works if the {@link View#getLayoutParams()} supports margins.
     * Hidden for now since we don't want to support this for all different layout margins yet.
     *
     * @param viewId
     * 		The id of the view to change
     * @param endMargin
     * 		a value in pixels for the end margin.
     * @unknown 
     */
    public void setViewLayoutMarginEnd(int viewId, @android.annotation.DimenRes
    int endMargin) {
        addAction(new android.widget.RemoteViews.LayoutParamAction(viewId, android.widget.RemoteViews.LayoutParamAction.LAYOUT_MARGIN_END, endMargin));
    }

    /**
     * Equivalent to setting {@link android.view.ViewGroup.MarginLayoutParams#bottomMargin}.
     *
     * @param bottomMarginDimen
     * 		a dimen resource to read the margin from or 0 to clear the margin.
     * @unknown 
     */
    public void setViewLayoutMarginBottomDimen(int viewId, @android.annotation.DimenRes
    int bottomMarginDimen) {
        addAction(new android.widget.RemoteViews.LayoutParamAction(viewId, android.widget.RemoteViews.LayoutParamAction.LAYOUT_MARGIN_BOTTOM_DIMEN, bottomMarginDimen));
    }

    /**
     * Equivalent to setting {@link android.view.ViewGroup.LayoutParams#width}.
     *
     * @param layoutWidth
     * 		one of 0, MATCH_PARENT or WRAP_CONTENT. Other sizes are not allowed
     * 		because they behave poorly when the density changes.
     * @unknown 
     */
    public void setViewLayoutWidth(int viewId, int layoutWidth) {
        if (((layoutWidth != 0) && (layoutWidth != android.view.ViewGroup.LayoutParams.MATCH_PARENT)) && (layoutWidth != android.view.ViewGroup.LayoutParams.WRAP_CONTENT)) {
            throw new java.lang.IllegalArgumentException("Only supports 0, WRAP_CONTENT and MATCH_PARENT");
        }
        mActions.add(new android.widget.RemoteViews.LayoutParamAction(viewId, android.widget.RemoteViews.LayoutParamAction.LAYOUT_WIDTH, layoutWidth));
    }

    /**
     * Call a method taking one boolean on a view in the layout for this RemoteViews.
     *
     * @param viewId
     * 		The id of the view on which to call the method.
     * @param methodName
     * 		The name of the method to call.
     * @param value
     * 		The value to pass to the method.
     */
    public void setBoolean(int viewId, java.lang.String methodName, boolean value) {
        addAction(new android.widget.RemoteViews.ReflectionAction(viewId, methodName, android.widget.RemoteViews.ReflectionAction.BOOLEAN, value));
    }

    /**
     * Call a method taking one byte on a view in the layout for this RemoteViews.
     *
     * @param viewId
     * 		The id of the view on which to call the method.
     * @param methodName
     * 		The name of the method to call.
     * @param value
     * 		The value to pass to the method.
     */
    public void setByte(int viewId, java.lang.String methodName, byte value) {
        addAction(new android.widget.RemoteViews.ReflectionAction(viewId, methodName, android.widget.RemoteViews.ReflectionAction.BYTE, value));
    }

    /**
     * Call a method taking one short on a view in the layout for this RemoteViews.
     *
     * @param viewId
     * 		The id of the view on which to call the method.
     * @param methodName
     * 		The name of the method to call.
     * @param value
     * 		The value to pass to the method.
     */
    public void setShort(int viewId, java.lang.String methodName, short value) {
        addAction(new android.widget.RemoteViews.ReflectionAction(viewId, methodName, android.widget.RemoteViews.ReflectionAction.SHORT, value));
    }

    /**
     * Call a method taking one int on a view in the layout for this RemoteViews.
     *
     * @param viewId
     * 		The id of the view on which to call the method.
     * @param methodName
     * 		The name of the method to call.
     * @param value
     * 		The value to pass to the method.
     */
    public void setInt(int viewId, java.lang.String methodName, int value) {
        addAction(new android.widget.RemoteViews.ReflectionAction(viewId, methodName, android.widget.RemoteViews.ReflectionAction.INT, value));
    }

    /**
     * Call a method taking one ColorStateList on a view in the layout for this RemoteViews.
     *
     * @param viewId
     * 		The id of the view on which to call the method.
     * @param methodName
     * 		The name of the method to call.
     * @param value
     * 		The value to pass to the method.
     * @unknown 
     */
    public void setColorStateList(int viewId, java.lang.String methodName, android.content.res.ColorStateList value) {
        addAction(new android.widget.RemoteViews.ReflectionAction(viewId, methodName, android.widget.RemoteViews.ReflectionAction.COLOR_STATE_LIST, value));
    }

    /**
     * Call a method taking one long on a view in the layout for this RemoteViews.
     *
     * @param viewId
     * 		The id of the view on which to call the method.
     * @param methodName
     * 		The name of the method to call.
     * @param value
     * 		The value to pass to the method.
     */
    public void setLong(int viewId, java.lang.String methodName, long value) {
        addAction(new android.widget.RemoteViews.ReflectionAction(viewId, methodName, android.widget.RemoteViews.ReflectionAction.LONG, value));
    }

    /**
     * Call a method taking one float on a view in the layout for this RemoteViews.
     *
     * @param viewId
     * 		The id of the view on which to call the method.
     * @param methodName
     * 		The name of the method to call.
     * @param value
     * 		The value to pass to the method.
     */
    public void setFloat(int viewId, java.lang.String methodName, float value) {
        addAction(new android.widget.RemoteViews.ReflectionAction(viewId, methodName, android.widget.RemoteViews.ReflectionAction.FLOAT, value));
    }

    /**
     * Call a method taking one double on a view in the layout for this RemoteViews.
     *
     * @param viewId
     * 		The id of the view on which to call the method.
     * @param methodName
     * 		The name of the method to call.
     * @param value
     * 		The value to pass to the method.
     */
    public void setDouble(int viewId, java.lang.String methodName, double value) {
        addAction(new android.widget.RemoteViews.ReflectionAction(viewId, methodName, android.widget.RemoteViews.ReflectionAction.DOUBLE, value));
    }

    /**
     * Call a method taking one char on a view in the layout for this RemoteViews.
     *
     * @param viewId
     * 		The id of the view on which to call the method.
     * @param methodName
     * 		The name of the method to call.
     * @param value
     * 		The value to pass to the method.
     */
    public void setChar(int viewId, java.lang.String methodName, char value) {
        addAction(new android.widget.RemoteViews.ReflectionAction(viewId, methodName, android.widget.RemoteViews.ReflectionAction.CHAR, value));
    }

    /**
     * Call a method taking one String on a view in the layout for this RemoteViews.
     *
     * @param viewId
     * 		The id of the view on which to call the method.
     * @param methodName
     * 		The name of the method to call.
     * @param value
     * 		The value to pass to the method.
     */
    public void setString(int viewId, java.lang.String methodName, java.lang.String value) {
        addAction(new android.widget.RemoteViews.ReflectionAction(viewId, methodName, android.widget.RemoteViews.ReflectionAction.STRING, value));
    }

    /**
     * Call a method taking one CharSequence on a view in the layout for this RemoteViews.
     *
     * @param viewId
     * 		The id of the view on which to call the method.
     * @param methodName
     * 		The name of the method to call.
     * @param value
     * 		The value to pass to the method.
     */
    public void setCharSequence(int viewId, java.lang.String methodName, java.lang.CharSequence value) {
        addAction(new android.widget.RemoteViews.ReflectionAction(viewId, methodName, android.widget.RemoteViews.ReflectionAction.CHAR_SEQUENCE, value));
    }

    /**
     * Call a method taking one Uri on a view in the layout for this RemoteViews.
     *
     * @param viewId
     * 		The id of the view on which to call the method.
     * @param methodName
     * 		The name of the method to call.
     * @param value
     * 		The value to pass to the method.
     */
    public void setUri(int viewId, java.lang.String methodName, android.net.Uri value) {
        if (value != null) {
            // Resolve any filesystem path before sending remotely
            value = value.getCanonicalUri();
            if (android.os.StrictMode.vmFileUriExposureEnabled()) {
                value.checkFileUriExposed("RemoteViews.setUri()");
            }
        }
        addAction(new android.widget.RemoteViews.ReflectionAction(viewId, methodName, android.widget.RemoteViews.ReflectionAction.URI, value));
    }

    /**
     * Call a method taking one Bitmap on a view in the layout for this RemoteViews.
     *
     * @unknown <p class="note">The bitmap will be flattened into the parcel if this object is
    sent across processes, so it may end up using a lot of memory, and may be fairly slow.</p>
     * @param viewId
     * 		The id of the view on which to call the method.
     * @param methodName
     * 		The name of the method to call.
     * @param value
     * 		The value to pass to the method.
     */
    public void setBitmap(int viewId, java.lang.String methodName, android.graphics.Bitmap value) {
        addAction(new android.widget.RemoteViews.BitmapReflectionAction(viewId, methodName, value));
    }

    /**
     * Call a method taking one Bundle on a view in the layout for this RemoteViews.
     *
     * @param viewId
     * 		The id of the view on which to call the method.
     * @param methodName
     * 		The name of the method to call.
     * @param value
     * 		The value to pass to the method.
     */
    public void setBundle(int viewId, java.lang.String methodName, android.os.Bundle value) {
        addAction(new android.widget.RemoteViews.ReflectionAction(viewId, methodName, android.widget.RemoteViews.ReflectionAction.BUNDLE, value));
    }

    /**
     * Call a method taking one Intent on a view in the layout for this RemoteViews.
     *
     * @param viewId
     * 		The id of the view on which to call the method.
     * @param methodName
     * 		The name of the method to call.
     * @param value
     * 		The {@link android.content.Intent} to pass the method.
     */
    public void setIntent(int viewId, java.lang.String methodName, android.content.Intent value) {
        addAction(new android.widget.RemoteViews.ReflectionAction(viewId, methodName, android.widget.RemoteViews.ReflectionAction.INTENT, value));
    }

    /**
     * Call a method taking one Icon on a view in the layout for this RemoteViews.
     *
     * @param viewId
     * 		The id of the view on which to call the method.
     * @param methodName
     * 		The name of the method to call.
     * @param value
     * 		The {@link android.graphics.drawable.Icon} to pass the method.
     */
    public void setIcon(int viewId, java.lang.String methodName, android.graphics.drawable.Icon value) {
        addAction(new android.widget.RemoteViews.ReflectionAction(viewId, methodName, android.widget.RemoteViews.ReflectionAction.ICON, value));
    }

    /**
     * Equivalent to calling View.setContentDescription(CharSequence).
     *
     * @param viewId
     * 		The id of the view whose content description should change.
     * @param contentDescription
     * 		The new content description for the view.
     */
    public void setContentDescription(int viewId, java.lang.CharSequence contentDescription) {
        setCharSequence(viewId, "setContentDescription", contentDescription);
    }

    /**
     * Equivalent to calling {@link android.view.View#setAccessibilityTraversalBefore(int)}.
     *
     * @param viewId
     * 		The id of the view whose before view in accessibility traversal to set.
     * @param nextId
     * 		The id of the next in the accessibility traversal.
     */
    public void setAccessibilityTraversalBefore(int viewId, int nextId) {
        setInt(viewId, "setAccessibilityTraversalBefore", nextId);
    }

    /**
     * Equivalent to calling {@link android.view.View#setAccessibilityTraversalAfter(int)}.
     *
     * @param viewId
     * 		The id of the view whose after view in accessibility traversal to set.
     * @param nextId
     * 		The id of the next in the accessibility traversal.
     */
    public void setAccessibilityTraversalAfter(int viewId, int nextId) {
        setInt(viewId, "setAccessibilityTraversalAfter", nextId);
    }

    /**
     * Equivalent to calling {@link View#setLabelFor(int)}.
     *
     * @param viewId
     * 		The id of the view whose property to set.
     * @param labeledId
     * 		The id of a view for which this view serves as a label.
     */
    public void setLabelFor(int viewId, int labeledId) {
        setInt(viewId, "setLabelFor", labeledId);
    }

    /**
     * Provides an alternate layout ID, which can be used to inflate this view. This layout will be
     * used by the host when the widgets displayed on a light-background where foreground elements
     * and text can safely draw using a dark color without any additional background protection.
     */
    public void setLightBackgroundLayoutId(@android.annotation.LayoutRes
    int layoutId) {
        mLightBackgroundLayoutId = layoutId;
    }

    /**
     * If this view supports dark text versions, creates a copy representing that version,
     * otherwise returns itself.
     *
     * @unknown 
     */
    public android.widget.RemoteViews getDarkTextViews() {
        if (hasFlags(android.widget.RemoteViews.FLAG_USE_LIGHT_BACKGROUND_LAYOUT)) {
            return this;
        }
        try {
            addFlags(android.widget.RemoteViews.FLAG_USE_LIGHT_BACKGROUND_LAYOUT);
            return new android.widget.RemoteViews(this);
        } finally {
            mApplyFlags &= ~android.widget.RemoteViews.FLAG_USE_LIGHT_BACKGROUND_LAYOUT;
        }
    }

    private android.widget.RemoteViews getRemoteViewsToApply(android.content.Context context) {
        if (hasLandscapeAndPortraitLayouts()) {
            int orientation = context.getResources().getConfiguration().orientation;
            if (orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE) {
                return mLandscape;
            } else {
                return mPortrait;
            }
        }
        return this;
    }

    /**
     * Inflates the view hierarchy represented by this object and applies
     * all of the actions.
     *
     * <p><strong>Caller beware: this may throw</strong>
     *
     * @param context
     * 		Default context to use
     * @param parent
     * 		Parent that the resulting view hierarchy will be attached to. This method
     * 		does <strong>not</strong> attach the hierarchy. The caller should do so when appropriate.
     * @return The inflated view hierarchy
     */
    public android.view.View apply(android.content.Context context, android.view.ViewGroup parent) {
        return apply(context, parent, null);
    }

    /**
     *
     *
     * @unknown 
     */
    public android.view.View apply(android.content.Context context, android.view.ViewGroup parent, android.widget.RemoteViews.OnClickHandler handler) {
        android.widget.RemoteViews rvToApply = getRemoteViewsToApply(context);
        android.view.View result = inflateView(context, rvToApply, parent);
        rvToApply.performApply(result, parent, handler);
        return result;
    }

    /**
     *
     *
     * @unknown 
     */
    public android.view.View applyWithTheme(android.content.Context context, android.view.ViewGroup parent, android.widget.RemoteViews.OnClickHandler handler, @android.annotation.StyleRes
    int applyThemeResId) {
        android.widget.RemoteViews rvToApply = getRemoteViewsToApply(context);
        android.view.View result = inflateView(context, rvToApply, parent, applyThemeResId);
        rvToApply.performApply(result, parent, handler);
        return result;
    }

    private android.view.View inflateView(android.content.Context context, android.widget.RemoteViews rv, android.view.ViewGroup parent) {
        return inflateView(context, rv, parent, 0);
    }

    private android.view.View inflateView(android.content.Context context, android.widget.RemoteViews rv, android.view.ViewGroup parent, @android.annotation.StyleRes
    int applyThemeResId) {
        // RemoteViews may be built by an application installed in another
        // user. So build a context that loads resources from that user but
        // still returns the current users userId so settings like data / time formats
        // are loaded without requiring cross user persmissions.
        final android.content.Context contextForResources = getContextForResources(context);
        android.content.Context inflationContext = new android.widget.RemoteViews.RemoteViewsContextWrapper(context, contextForResources);
        // If mApplyThemeResId is not given, Theme.DeviceDefault will be used.
        if (applyThemeResId != 0) {
            inflationContext = new android.view.ContextThemeWrapper(inflationContext, applyThemeResId);
        }
        android.view.LayoutInflater inflater = ((android.view.LayoutInflater) (context.getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE)));
        // Clone inflater so we load resources from correct context and
        // we don't add a filter to the static version returned by getSystemService.
        inflater = inflater.cloneInContext(inflationContext);
        inflater.setFilter(this);
        android.view.View v = inflater.inflate(rv.getLayoutId(), parent, false);
        v.setTagInternal(R.id.widget_frame, rv.getLayoutId());
        return v;
    }

    /**
     * Implement this interface to receive a callback when
     * {@link #applyAsync} or {@link #reapplyAsync} is finished.
     *
     * @unknown 
     */
    public interface OnViewAppliedListener {
        /**
         * Callback when the RemoteView has finished inflating,
         * but no actions have been applied yet.
         */
        default void onViewInflated(android.view.View v) {
        }

        void onViewApplied(android.view.View v);

        void onError(java.lang.Exception e);
    }

    /**
     * Applies the views asynchronously, moving as much of the task on the background
     * thread as possible.
     *
     * @see #apply(Context, ViewGroup)
     * @param context
     * 		Default context to use
     * @param parent
     * 		Parent that the resulting view hierarchy will be attached to. This method
     * 		does <strong>not</strong> attach the hierarchy. The caller should do so when appropriate.
     * @param listener
     * 		the callback to run when all actions have been applied. May be null.
     * @param executor
     * 		The executor to use. If null {@link AsyncTask#THREAD_POOL_EXECUTOR} is used.
     * @return CancellationSignal
     * @unknown 
     */
    public android.os.CancellationSignal applyAsync(android.content.Context context, android.view.ViewGroup parent, java.util.concurrent.Executor executor, android.widget.RemoteViews.OnViewAppliedListener listener) {
        return applyAsync(context, parent, executor, listener, null);
    }

    private android.os.CancellationSignal startTaskOnExecutor(android.widget.RemoteViews.AsyncApplyTask task, java.util.concurrent.Executor executor) {
        android.os.CancellationSignal cancelSignal = new android.os.CancellationSignal();
        cancelSignal.setOnCancelListener(task);
        task.executeOnExecutor(executor == null ? android.os.AsyncTask.THREAD_POOL_EXECUTOR : executor);
        return cancelSignal;
    }

    /**
     *
     *
     * @unknown 
     */
    public android.os.CancellationSignal applyAsync(android.content.Context context, android.view.ViewGroup parent, java.util.concurrent.Executor executor, android.widget.RemoteViews.OnViewAppliedListener listener, android.widget.RemoteViews.OnClickHandler handler) {
        return startTaskOnExecutor(getAsyncApplyTask(context, parent, listener, handler), executor);
    }

    private android.widget.RemoteViews.AsyncApplyTask getAsyncApplyTask(android.content.Context context, android.view.ViewGroup parent, android.widget.RemoteViews.OnViewAppliedListener listener, android.widget.RemoteViews.OnClickHandler handler) {
        return new android.widget.RemoteViews.AsyncApplyTask(getRemoteViewsToApply(context), parent, context, listener, handler, null);
    }

    private class AsyncApplyTask extends android.os.AsyncTask<java.lang.Void, java.lang.Void, android.widget.RemoteViews.ViewTree> implements android.os.CancellationSignal.OnCancelListener {
        final android.widget.RemoteViews mRV;

        final android.view.ViewGroup mParent;

        final android.content.Context mContext;

        final android.widget.RemoteViews.OnViewAppliedListener mListener;

        final android.widget.RemoteViews.OnClickHandler mHandler;

        private android.view.View mResult;

        private android.widget.RemoteViews.ViewTree mTree;

        private android.widget.RemoteViews.Action[] mActions;

        private java.lang.Exception mError;

        private AsyncApplyTask(android.widget.RemoteViews rv, android.view.ViewGroup parent, android.content.Context context, android.widget.RemoteViews.OnViewAppliedListener listener, android.widget.RemoteViews.OnClickHandler handler, android.view.View result) {
            mRV = rv;
            mParent = parent;
            mContext = context;
            mListener = listener;
            mHandler = handler;
            mResult = result;
        }

        @java.lang.Override
        protected android.widget.RemoteViews.ViewTree doInBackground(java.lang.Void... params) {
            try {
                if (mResult == null) {
                    mResult = inflateView(mContext, mRV, mParent);
                }
                mTree = new android.widget.RemoteViews.ViewTree(mResult);
                if (mRV.mActions != null) {
                    int count = mRV.mActions.size();
                    mActions = new android.widget.RemoteViews.Action[count];
                    for (int i = 0; (i < count) && (!isCancelled()); i++) {
                        // TODO: check if isCancelled in nested views.
                        mActions[i] = mRV.mActions.get(i).initActionAsync(mTree, mParent, mHandler);
                    }
                } else {
                    mActions = null;
                }
                return mTree;
            } catch (java.lang.Exception e) {
                mError = e;
                return null;
            }
        }

        @java.lang.Override
        protected void onPostExecute(android.widget.RemoteViews.ViewTree viewTree) {
            if (mError == null) {
                if (mListener != null) {
                    mListener.onViewInflated(viewTree.mRoot);
                }
                try {
                    if (mActions != null) {
                        android.widget.RemoteViews.OnClickHandler handler = (mHandler == null) ? android.widget.RemoteViews.DEFAULT_ON_CLICK_HANDLER : mHandler;
                        for (android.widget.RemoteViews.Action a : mActions) {
                            a.apply(viewTree.mRoot, mParent, handler);
                        }
                    }
                } catch (java.lang.Exception e) {
                    mError = e;
                }
            }
            if (mListener != null) {
                if (mError != null) {
                    mListener.onError(mError);
                } else {
                    mListener.onViewApplied(viewTree.mRoot);
                }
            } else
                if (mError != null) {
                    if (mError instanceof android.widget.RemoteViews.ActionException) {
                        throw ((android.widget.RemoteViews.ActionException) (mError));
                    } else {
                        throw new android.widget.RemoteViews.ActionException(mError);
                    }
                }

        }

        @java.lang.Override
        public void onCancel() {
            cancel(true);
        }
    }

    /**
     * Applies all of the actions to the provided view.
     *
     * <p><strong>Caller beware: this may throw</strong>
     *
     * @param v
     * 		The view to apply the actions to.  This should be the result of
     * 		the {@link #apply(Context,ViewGroup)} call.
     */
    public void reapply(android.content.Context context, android.view.View v) {
        reapply(context, v, null);
    }

    /**
     *
     *
     * @unknown 
     */
    public void reapply(android.content.Context context, android.view.View v, android.widget.RemoteViews.OnClickHandler handler) {
        android.widget.RemoteViews rvToApply = getRemoteViewsToApply(context);
        // In the case that a view has this RemoteViews applied in one orientation, is persisted
        // across orientation change, and has the RemoteViews re-applied in the new orientation,
        // we throw an exception, since the layouts may be completely unrelated.
        if (hasLandscapeAndPortraitLayouts()) {
            if (((java.lang.Integer) (v.getTag(R.id.widget_frame))) != rvToApply.getLayoutId()) {
                throw new java.lang.RuntimeException("Attempting to re-apply RemoteViews to a view that" + " that does not share the same root layout id.");
            }
        }
        rvToApply.performApply(v, ((android.view.ViewGroup) (v.getParent())), handler);
    }

    /**
     * Applies all the actions to the provided view, moving as much of the task on the background
     * thread as possible.
     *
     * @see #reapply(Context, View)
     * @param context
     * 		Default context to use
     * @param v
     * 		The view to apply the actions to.  This should be the result of
     * 		the {@link #apply(Context,ViewGroup)} call.
     * @param listener
     * 		the callback to run when all actions have been applied. May be null.
     * @param executor
     * 		The executor to use. If null {@link AsyncTask#THREAD_POOL_EXECUTOR} is used
     * @return CancellationSignal
     * @unknown 
     */
    public android.os.CancellationSignal reapplyAsync(android.content.Context context, android.view.View v, java.util.concurrent.Executor executor, android.widget.RemoteViews.OnViewAppliedListener listener) {
        return reapplyAsync(context, v, executor, listener, null);
    }

    /**
     *
     *
     * @unknown 
     */
    public android.os.CancellationSignal reapplyAsync(android.content.Context context, android.view.View v, java.util.concurrent.Executor executor, android.widget.RemoteViews.OnViewAppliedListener listener, android.widget.RemoteViews.OnClickHandler handler) {
        android.widget.RemoteViews rvToApply = getRemoteViewsToApply(context);
        // In the case that a view has this RemoteViews applied in one orientation, is persisted
        // across orientation change, and has the RemoteViews re-applied in the new orientation,
        // we throw an exception, since the layouts may be completely unrelated.
        if (hasLandscapeAndPortraitLayouts()) {
            if (((java.lang.Integer) (v.getTag(R.id.widget_frame))) != rvToApply.getLayoutId()) {
                throw new java.lang.RuntimeException("Attempting to re-apply RemoteViews to a view that" + " that does not share the same root layout id.");
            }
        }
        return startTaskOnExecutor(new android.widget.RemoteViews.AsyncApplyTask(rvToApply, ((android.view.ViewGroup) (v.getParent())), context, listener, handler, v), executor);
    }

    private void performApply(android.view.View v, android.view.ViewGroup parent, android.widget.RemoteViews.OnClickHandler handler) {
        if (mActions != null) {
            handler = (handler == null) ? android.widget.RemoteViews.DEFAULT_ON_CLICK_HANDLER : handler;
            final int count = mActions.size();
            for (int i = 0; i < count; i++) {
                android.widget.RemoteViews.Action a = mActions.get(i);
                a.apply(v, parent, handler);
            }
        }
    }

    /**
     * Returns true if the RemoteViews contains potentially costly operations and should be
     * applied asynchronously.
     *
     * @unknown 
     */
    public boolean prefersAsyncApply() {
        if (mActions != null) {
            final int count = mActions.size();
            for (int i = 0; i < count; i++) {
                if (mActions.get(i).prefersAsyncApply()) {
                    return true;
                }
            }
        }
        return false;
    }

    private android.content.Context getContextForResources(android.content.Context context) {
        if (mApplication != null) {
            if ((context.getUserId() == android.os.UserHandle.getUserId(mApplication.uid)) && context.getPackageName().equals(mApplication.packageName)) {
                return context;
            }
            try {
                return context.createApplicationContext(mApplication, android.content.Context.CONTEXT_RESTRICTED);
            } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                android.util.Log.e(android.widget.RemoteViews.LOG_TAG, ("Package name " + mApplication.packageName) + " not found");
            }
        }
        return context;
    }

    /**
     * Returns the number of actions in this RemoteViews. Can be used as a sequence number.
     *
     * @unknown 
     */
    public int getSequenceNumber() {
        return mActions == null ? 0 : mActions.size();
    }

    /* (non-Javadoc)
    Used to restrict the views which can be inflated

    @see android.view.LayoutInflater.Filter#onLoadClass(java.lang.Class)
     */
    public boolean onLoadClass(java.lang.Class clazz) {
        return clazz.isAnnotationPresent(android.widget.RemoteViews.RemoteView.class);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(android.os.Parcel dest, int flags) {
        if (!hasLandscapeAndPortraitLayouts()) {
            dest.writeInt(android.widget.RemoteViews.MODE_NORMAL);
            // We only write the bitmap cache if we are the root RemoteViews, as this cache
            // is shared by all children.
            if (mIsRoot) {
                mBitmapCache.writeBitmapsToParcel(dest, flags);
            }
            if ((!mIsRoot) && ((flags & PARCELABLE_ELIDE_DUPLICATES) != 0)) {
                dest.writeInt(0);
            } else {
                dest.writeInt(1);
                mApplication.writeToParcel(dest, flags);
            }
            dest.writeInt(mLayoutId);
            dest.writeInt(mLightBackgroundLayoutId);
            writeActionsToParcel(dest);
        } else {
            dest.writeInt(android.widget.RemoteViews.MODE_HAS_LANDSCAPE_AND_PORTRAIT);
            // We only write the bitmap cache if we are the root RemoteViews, as this cache
            // is shared by all children.
            if (mIsRoot) {
                mBitmapCache.writeBitmapsToParcel(dest, flags);
            }
            mLandscape.writeToParcel(dest, flags);
            // Both RemoteViews already share the same package and user
            mPortrait.writeToParcel(dest, flags | PARCELABLE_ELIDE_DUPLICATES);
        }
        dest.writeInt(mApplyFlags);
    }

    private void writeActionsToParcel(android.os.Parcel parcel) {
        int count;
        if (mActions != null) {
            count = mActions.size();
        } else {
            count = 0;
        }
        parcel.writeInt(count);
        for (int i = 0; i < count; i++) {
            android.widget.RemoteViews.Action a = mActions.get(i);
            parcel.writeInt(a.getActionTag());
            a.writeToParcel(parcel, a.hasSameAppInfo(mApplication) ? PARCELABLE_ELIDE_DUPLICATES : 0);
        }
    }

    private static android.content.pm.ApplicationInfo getApplicationInfo(java.lang.String packageName, int userId) {
        if (packageName == null) {
            return null;
        }
        // Get the application for the passed in package and user.
        android.app.Application application = android.app.ActivityThread.currentApplication();
        if (application == null) {
            throw new java.lang.IllegalStateException("Cannot create remote views out of an aplication.");
        }
        android.content.pm.ApplicationInfo applicationInfo = application.getApplicationInfo();
        if ((android.os.UserHandle.getUserId(applicationInfo.uid) != userId) || (!applicationInfo.packageName.equals(packageName))) {
            try {
                android.content.Context context = application.getBaseContext().createPackageContextAsUser(packageName, 0, new android.os.UserHandle(userId));
                applicationInfo = context.getApplicationInfo();
            } catch (android.content.pm.PackageManager.NameNotFoundException nnfe) {
                throw new java.lang.IllegalArgumentException("No such package " + packageName);
            }
        }
        return applicationInfo;
    }

    /**
     * Returns true if the {@link #mApplication} is same as the provided info.
     *
     * @unknown 
     */
    public boolean hasSameAppInfo(android.content.pm.ApplicationInfo info) {
        return mApplication.packageName.equals(info.packageName) && (mApplication.uid == info.uid);
    }

    /**
     * Parcelable.Creator that instantiates RemoteViews objects
     */
    @android.annotation.NonNull
    public static final android.os.Parcelable.Creator<android.widget.RemoteViews> CREATOR = new android.os.Parcelable.Creator<android.widget.RemoteViews>() {
        public android.widget.RemoteViews createFromParcel(android.os.Parcel parcel) {
            return new android.widget.RemoteViews(parcel);
        }

        public android.widget.RemoteViews[] newArray(int size) {
            return new android.widget.RemoteViews[size];
        }
    };

    /**
     * A representation of the view hierarchy. Only views which have a valid ID are added
     * and can be searched.
     */
    private static class ViewTree {
        private static final int INSERT_AT_END_INDEX = -1;

        private android.view.View mRoot;

        private java.util.ArrayList<android.widget.RemoteViews.ViewTree> mChildren;

        private ViewTree(android.view.View root) {
            mRoot = root;
        }

        public void createTree() {
            if (mChildren != null) {
                return;
            }
            mChildren = new java.util.ArrayList<>();
            if (mRoot instanceof android.view.ViewGroup) {
                android.view.ViewGroup vg = ((android.view.ViewGroup) (mRoot));
                int count = vg.getChildCount();
                for (int i = 0; i < count; i++) {
                    addViewChild(vg.getChildAt(i));
                }
            }
        }

        public android.widget.RemoteViews.ViewTree findViewTreeById(int id) {
            if (mRoot.getId() == id) {
                return this;
            }
            if (mChildren == null) {
                return null;
            }
            for (android.widget.RemoteViews.ViewTree tree : mChildren) {
                android.widget.RemoteViews.ViewTree result = tree.findViewTreeById(id);
                if (result != null) {
                    return result;
                }
            }
            return null;
        }

        public void replaceView(android.view.View v) {
            mRoot = v;
            mChildren = null;
            createTree();
        }

        public <T extends android.view.View> T findViewById(int id) {
            if (mChildren == null) {
                return mRoot.findViewById(id);
            }
            android.widget.RemoteViews.ViewTree tree = findViewTreeById(id);
            return tree == null ? null : ((T) (tree.mRoot));
        }

        public void addChild(android.widget.RemoteViews.ViewTree child) {
            addChild(child, android.widget.RemoteViews.ViewTree.INSERT_AT_END_INDEX);
        }

        /**
         * Adds the given {@link ViewTree} as a child at the given index.
         *
         * @param index
         * 		The position at which to add the child or -1 to add last.
         */
        public void addChild(android.widget.RemoteViews.ViewTree child, int index) {
            if (mChildren == null) {
                mChildren = new java.util.ArrayList<>();
            }
            child.createTree();
            if (index == android.widget.RemoteViews.ViewTree.INSERT_AT_END_INDEX) {
                mChildren.add(child);
                return;
            }
            mChildren.add(index, child);
        }

        private void addViewChild(android.view.View v) {
            // ViewTree only contains Views which can be found using findViewById.
            // If isRootNamespace is true, this view is skipped.
            // @see ViewGroup#findViewTraversal(int)
            if (v.isRootNamespace()) {
                return;
            }
            final android.widget.RemoteViews.ViewTree target;
            // If the view has a valid id, i.e., if can be found using findViewById, add it to the
            // tree, otherwise skip this view and add its children instead.
            if (v.getId() != 0) {
                android.widget.RemoteViews.ViewTree tree = new android.widget.RemoteViews.ViewTree(v);
                mChildren.add(tree);
                target = tree;
            } else {
                target = this;
            }
            if (v instanceof android.view.ViewGroup) {
                if (target.mChildren == null) {
                    target.mChildren = new java.util.ArrayList<>();
                    android.view.ViewGroup vg = ((android.view.ViewGroup) (v));
                    int count = vg.getChildCount();
                    for (int i = 0; i < count; i++) {
                        target.addViewChild(vg.getChildAt(i));
                    }
                }
            }
        }
    }

    /**
     * Class representing a response to an action performed on any element of a RemoteViews.
     */
    public static class RemoteResponse {
        private android.app.PendingIntent mPendingIntent;

        private android.content.Intent mFillIntent;

        private android.util.IntArray mViewIds;

        private java.util.ArrayList<java.lang.String> mElementNames;

        /**
         * Creates a response which sends a pending intent as part of the response. The source
         * bounds ({@link Intent#getSourceBounds()}) of the intent will be set to the bounds of the
         * target view in screen space.
         * Note that any activity options associated with the mPendingIntent may get overridden
         * before starting the intent.
         *
         * @param pendingIntent
         * 		The {@link PendingIntent} to send as part of the response
         */
        @android.annotation.NonNull
        public static android.widget.RemoteViews.RemoteResponse fromPendingIntent(@android.annotation.NonNull
        android.app.PendingIntent pendingIntent) {
            android.widget.RemoteViews.RemoteResponse response = new android.widget.RemoteViews.RemoteResponse();
            response.mPendingIntent = pendingIntent;
            return response;
        }

        /**
         * When using collections (eg. {@link ListView}, {@link StackView} etc.) in widgets, it is
         * very costly to set PendingIntents on the individual items, and is hence not permitted.
         * Instead a single PendingIntent template can be set on the collection, see {@link RemoteViews#setPendingIntentTemplate(int, PendingIntent)}, and the individual on-click
         * action of a given item can be distinguished by setting a fillInIntent on that item. The
         * fillInIntent is then combined with the PendingIntent template in order to determine the
         * final intent which will be executed when the item is clicked. This works as follows: any
         * fields which are left blank in the PendingIntent template, but are provided by the
         * fillInIntent will be overwritten, and the resulting PendingIntent will be used. The rest
         * of the PendingIntent template will then be filled in with the associated fields that are
         * set in fillInIntent. See {@link Intent#fillIn(Intent, int)} for more details.
         * Creates a response which sends a pending intent as part of the response. The source
         * bounds ({@link Intent#getSourceBounds()}) of the intent will be set to the bounds of the
         * target view in screen space.
         * Note that any activity options associated with the mPendingIntent may get overridden
         * before starting the intent.
         *
         * @param fillIntent
         * 		The intent which will be combined with the parent's PendingIntent in
         * 		order to determine the behavior of the response
         * @see RemoteViews#setPendingIntentTemplate(int, PendingIntent)
         * @see RemoteViews#setOnClickFillInIntent(int, Intent)
         * @return 
         */
        @android.annotation.NonNull
        public static android.widget.RemoteViews.RemoteResponse fromFillInIntent(@android.annotation.NonNull
        android.content.Intent fillIntent) {
            android.widget.RemoteViews.RemoteResponse response = new android.widget.RemoteViews.RemoteResponse();
            response.mFillIntent = fillIntent;
            return response;
        }

        /**
         * Adds a shared element to be transferred as part of the transition between Activities
         * using cross-Activity scene animations. The position of the first element will be used as
         * the epicenter for the exit Transition. The position of the associated shared element in
         * the launched Activity will be the epicenter of its entering Transition.
         *
         * @param viewId
         * 		The id of the view to be shared as part of the transition
         * @param sharedElementName
         * 		The shared element name for this view
         * @see ActivityOptions#makeSceneTransitionAnimation(Activity, Pair[])
         */
        @android.annotation.NonNull
        public android.widget.RemoteViews.RemoteResponse addSharedElement(int viewId, @android.annotation.NonNull
        java.lang.String sharedElementName) {
            if (mViewIds == null) {
                mViewIds = new android.util.IntArray();
                mElementNames = new java.util.ArrayList<>();
            }
            mViewIds.add(viewId);
            mElementNames.add(sharedElementName);
            return this;
        }

        private void writeToParcel(android.os.Parcel dest, int flags) {
            android.app.PendingIntent.writePendingIntentOrNullToParcel(mPendingIntent, dest);
            if (mPendingIntent == null) {
                // Only write the intent if pending intent is null
                dest.writeTypedObject(mFillIntent, flags);
            }
            dest.writeIntArray(mViewIds == null ? null : mViewIds.toArray());
            dest.writeStringList(mElementNames);
        }

        private void readFromParcel(android.os.Parcel parcel) {
            mPendingIntent = android.app.PendingIntent.readPendingIntentOrNullFromParcel(parcel);
            if (mPendingIntent == null) {
                mFillIntent = parcel.readTypedObject(android.content.Intent.this.CREATOR);
            }
            int[] viewIds = parcel.createIntArray();
            mViewIds = (viewIds == null) ? null : android.util.IntArray.wrap(viewIds);
            mElementNames = parcel.createStringArrayList();
        }

        private void handleViewClick(android.view.View v, android.widget.RemoteViews.OnClickHandler handler) {
            final android.app.PendingIntent pi;
            if (mPendingIntent != null) {
                pi = mPendingIntent;
            } else
                if (mFillIntent != null) {
                    // Insure that this view is a child of an AdapterView
                    android.view.View parent = ((android.view.View) (v.getParent()));
                    // Break the for loop on the first encounter of:
                    // 1) an AdapterView,
                    // 2) an AppWidgetHostView that is not a RemoteViewsFrameLayout, or
                    // 3) a null parent.
                    // 2) and 3) are unexpected and catch the case where a child is not
                    // correctly parented in an AdapterView.
                    while (((parent != null) && (!(parent instanceof android.widget.AdapterView<?>))) && (!((parent instanceof android.appwidget.AppWidgetHostView) && (!(parent instanceof android.widget.RemoteViewsAdapter.RemoteViewsFrameLayout))))) {
                        parent = ((android.view.View) (parent.getParent()));
                    } 
                    if (!(parent instanceof android.widget.AdapterView<?>)) {
                        // Somehow they've managed to get this far without having
                        // and AdapterView as a parent.
                        android.util.Log.e(android.widget.RemoteViews.LOG_TAG, "Collection item doesn't have AdapterView parent");
                        return;
                    }
                    // Insure that a template pending intent has been set on an ancestor
                    if (!(parent.getTag() instanceof android.app.PendingIntent)) {
                        android.util.Log.e(android.widget.RemoteViews.LOG_TAG, "Attempting setOnClickFillInIntent without" + " calling setPendingIntentTemplate on parent.");
                        return;
                    }
                    pi = ((android.app.PendingIntent) (parent.getTag()));
                } else {
                    android.util.Log.e(android.widget.RemoteViews.LOG_TAG, "Response has neither pendingIntent nor fillInIntent");
                    return;
                }

            handler.onClickHandler(v, pi, this);
        }

        /**
         *
         *
         * @unknown 
         */
        public android.util.Pair<android.content.Intent, android.app.ActivityOptions> getLaunchOptions(android.view.View view) {
            android.content.Intent intent = (mPendingIntent != null) ? new android.content.Intent() : new android.content.Intent(mFillIntent);
            intent.setSourceBounds(android.widget.RemoteViews.getSourceBounds(view));
            android.app.ActivityOptions opts = null;
            android.content.Context context = view.getContext();
            if (context.getResources().getBoolean(com.android.internal.R.bool.config_overrideRemoteViewsActivityTransition)) {
                android.content.res.TypedArray windowStyle = context.getTheme().obtainStyledAttributes(com.android.internal.R.styleable.Window);
                int windowAnimations = windowStyle.getResourceId(com.android.internal.R.styleable.Window_windowAnimationStyle, 0);
                android.content.res.TypedArray windowAnimationStyle = context.obtainStyledAttributes(windowAnimations, com.android.internal.R.styleable.WindowAnimation);
                int enterAnimationId = windowAnimationStyle.getResourceId(com.android.internal.R.styleable.WindowAnimation_activityOpenRemoteViewsEnterAnimation, 0);
                windowStyle.recycle();
                windowAnimationStyle.recycle();
                if (enterAnimationId != 0) {
                    opts = android.app.ActivityOptions.makeCustomAnimation(context, enterAnimationId, 0);
                    opts.setPendingIntentLaunchFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
                }
            }
            if (((opts == null) && (mViewIds != null)) && (mElementNames != null)) {
                android.view.View parent = ((android.view.View) (view.getParent()));
                while ((parent != null) && (!(parent instanceof android.appwidget.AppWidgetHostView))) {
                    parent = ((android.view.View) (parent.getParent()));
                } 
                if (parent instanceof android.appwidget.AppWidgetHostView) {
                    opts = ((android.appwidget.AppWidgetHostView) (parent)).createSharedElementActivityOptions(mViewIds.toArray(), mElementNames.toArray(new java.lang.String[mElementNames.size()]), intent);
                }
            }
            if (opts == null) {
                opts = android.app.ActivityOptions.makeBasic();
                opts.setPendingIntentLaunchFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            return android.util.Pair.create(intent, opts);
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public static boolean startPendingIntent(android.view.View view, android.app.PendingIntent pendingIntent, android.util.Pair<android.content.Intent, android.app.ActivityOptions> options) {
        try {
            // TODO: Unregister this handler if PendingIntent.FLAG_ONE_SHOT?
            android.content.Context context = view.getContext();
            // The NEW_TASK flags are applied through the activity options and not as a part of
            // the call to startIntentSender() to ensure that they are consistently applied to
            // both mutable and immutable PendingIntents.
            context.startIntentSender(pendingIntent.getIntentSender(), options.first, 0, 0, 0, options.second.toBundle());
        } catch (android.content.IntentSender.SendIntentException e) {
            android.util.Log.e(android.widget.RemoteViews.LOG_TAG, "Cannot send pending intent: ", e);
            return false;
        } catch (java.lang.Exception e) {
            android.util.Log.e(android.widget.RemoteViews.LOG_TAG, "Cannot send pending intent due to unknown exception: ", e);
            return false;
        }
        return true;
    }
}

