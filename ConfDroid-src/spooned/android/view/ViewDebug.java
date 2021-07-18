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
package android.view;


/**
 * Various debugging/tracing tools related to {@link View} and the view hierarchy.
 */
public class ViewDebug {
    /**
     *
     *
     * @deprecated This flag is now unused
     */
    @java.lang.Deprecated
    public static final boolean TRACE_HIERARCHY = false;

    /**
     *
     *
     * @deprecated This flag is now unused
     */
    @java.lang.Deprecated
    public static final boolean TRACE_RECYCLER = false;

    /**
     * Enables detailed logging of drag/drop operations.
     *
     * @unknown 
     */
    public static final boolean DEBUG_DRAG = false;

    /**
     * Enables detailed logging of task positioning operations.
     *
     * @unknown 
     */
    public static final boolean DEBUG_POSITIONING = false;

    /**
     * This annotation can be used to mark fields and methods to be dumped by
     * the view server. Only non-void methods with no arguments can be annotated
     * by this annotation.
     */
    @java.lang.annotation.Target({ java.lang.annotation.ElementType.FIELD, java.lang.annotation.ElementType.METHOD })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
    public @interface ExportedProperty {
        /**
         * When resolveId is true, and if the annotated field/method return value
         * is an int, the value is converted to an Android's resource name.
         *
         * @return true if the property's value must be transformed into an Android
        resource name, false otherwise
         */
        boolean resolveId() default false;

        /**
         * A mapping can be defined to map int values to specific strings. For
         * instance, View.getVisibility() returns 0, 4 or 8. However, these values
         * actually mean VISIBLE, INVISIBLE and GONE. A mapping can be used to see
         * these human readable values:
         *
         * <pre>
         * {@literal @}ViewDebug.ExportedProperty(mapping = {
         *     {@literal @}ViewDebug.IntToString(from = 0, to = "VISIBLE"),
         *     {@literal @}ViewDebug.IntToString(from = 4, to = "INVISIBLE"),
         *     {@literal @}ViewDebug.IntToString(from = 8, to = "GONE")
         * })
         * public int getVisibility() { ...
         * <pre>
         *
         * @return An array of int to String mappings
         * @see android.view.ViewDebug.IntToString
         */
        android.view.ViewDebug.IntToString[] mapping() default {  };

        /**
         * A mapping can be defined to map array indices to specific strings.
         * A mapping can be used to see human readable values for the indices
         * of an array:
         *
         * <pre>
         * {@literal @}ViewDebug.ExportedProperty(indexMapping = {
         *     {@literal @}ViewDebug.IntToString(from = 0, to = "INVALID"),
         *     {@literal @}ViewDebug.IntToString(from = 1, to = "FIRST"),
         *     {@literal @}ViewDebug.IntToString(from = 2, to = "SECOND")
         * })
         * private int[] mElements;
         * <pre>
         *
         * @return An array of int to String mappings
         * @see android.view.ViewDebug.IntToString
         * @see #mapping()
         */
        android.view.ViewDebug.IntToString[] indexMapping() default {  };

        /**
         * A flags mapping can be defined to map flags encoded in an integer to
         * specific strings. A mapping can be used to see human readable values
         * for the flags of an integer:
         *
         * <pre>
         * {@literal @}ViewDebug.ExportedProperty(flagMapping = {
         *     {@literal @}ViewDebug.FlagToString(mask = ENABLED_MASK, equals = ENABLED,
         *             name = "ENABLED"),
         *     {@literal @}ViewDebug.FlagToString(mask = ENABLED_MASK, equals = DISABLED,
         *             name = "DISABLED"),
         * })
         * private int mFlags;
         * <pre>
         *
         * A specified String is output when the following is true:
         *
         * @return An array of int to String mappings
         */
        android.view.ViewDebug.FlagToString[] flagMapping() default {  };

        /**
         * When deep export is turned on, this property is not dumped. Instead, the
         * properties contained in this property are dumped. Each child property
         * is prefixed with the name of this property.
         *
         * @return true if the properties of this property should be dumped
         * @see #prefix()
         */
        boolean deepExport() default false;

        /**
         * The prefix to use on child properties when deep export is enabled
         *
         * @return a prefix as a String
         * @see #deepExport()
         */
        java.lang.String prefix() default "";

        /**
         * Specifies the category the property falls into, such as measurement,
         * layout, drawing, etc.
         *
         * @return the category as String
         */
        java.lang.String category() default "";

        /**
         * Indicates whether or not to format an {@code int} or {@code byte} value as a hex string.
         *
         * @return true if the supported values should be formatted as a hex string.
         */
        boolean formatToHexString() default false;

        /**
         * Indicates whether or not the key to value mappings are held in adjacent indices.
         *
         * Note: Applies only to fields and methods that return String[].
         *
         * @return true if the key to value mappings are held in adjacent indices.
         */
        boolean hasAdjacentMapping() default false;
    }

    /**
     * Defines a mapping from an int value to a String. Such a mapping can be used
     * in an @ExportedProperty to provide more meaningful values to the end user.
     *
     * @see android.view.ViewDebug.ExportedProperty
     */
    @java.lang.annotation.Target({ java.lang.annotation.ElementType.TYPE })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
    public @interface IntToString {
        /**
         * The original int value to map to a String.
         *
         * @return An arbitrary int value.
         */
        int from();

        /**
         * The String to use in place of the original int value.
         *
         * @return An arbitrary non-null String.
         */
        java.lang.String to();
    }

    /**
     * Defines a mapping from a flag to a String. Such a mapping can be used
     * in an @ExportedProperty to provide more meaningful values to the end user.
     *
     * @see android.view.ViewDebug.ExportedProperty
     */
    @java.lang.annotation.Target({ java.lang.annotation.ElementType.TYPE })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
    public @interface FlagToString {
        /**
         * The mask to apply to the original value.
         *
         * @return An arbitrary int value.
         */
        int mask();

        /**
         * The value to compare to the result of:
         * <code>original value &amp; {@link #mask()}</code>.
         *
         * @return An arbitrary value.
         */
        int equals();

        /**
         * The String to use in place of the original int value.
         *
         * @return An arbitrary non-null String.
         */
        java.lang.String name();

        /**
         * Indicates whether to output the flag when the test is true,
         * or false. Defaults to true.
         */
        boolean outputIf() default true;
    }

    /**
     * This annotation can be used to mark fields and methods to be dumped when
     * the view is captured. Methods with this annotation must have no arguments
     * and must return a valid type of data.
     */
    @java.lang.annotation.Target({ java.lang.annotation.ElementType.FIELD, java.lang.annotation.ElementType.METHOD })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
    public @interface CapturedViewProperty {
        /**
         * When retrieveReturn is true, we need to retrieve second level methods
         * e.g., we need myView.getFirstLevelMethod().getSecondLevelMethod()
         * we will set retrieveReturn = true on the annotation of
         * myView.getFirstLevelMethod()
         *
         * @return true if we need the second level methods
         */
        boolean retrieveReturn() default false;
    }

    /**
     * Allows a View to inject custom children into HierarchyViewer. For example,
     * WebView uses this to add its internal layer tree as a child to itself
     *
     * @unknown 
     */
    public interface HierarchyHandler {
        /**
         * Dumps custom children to hierarchy viewer.
         * See ViewDebug.dumpViewWithProperties(Context, View, BufferedWriter, int)
         * for the format
         *
         * An empty implementation should simply do nothing
         *
         * @param out
         * 		The output writer
         * @param level
         * 		The indentation level
         */
        public void dumpViewHierarchyWithProperties(java.io.BufferedWriter out, int level);

        /**
         * Returns a View to enable grabbing screenshots from custom children
         * returned in dumpViewHierarchyWithProperties.
         *
         * @param className
         * 		The className of the view to find
         * @param hashCode
         * 		The hashCode of the view to find
         * @return the View to capture from, or null if not found
         */
        public android.view.View findHierarchyView(java.lang.String className, int hashCode);
    }

    private static java.util.HashMap<java.lang.Class<?>, java.lang.reflect.Method[]> mCapturedViewMethodsForClasses = null;

    private static java.util.HashMap<java.lang.Class<?>, java.lang.reflect.Field[]> mCapturedViewFieldsForClasses = null;

    // Maximum delay in ms after which we stop trying to capture a View's drawing
    private static final int CAPTURE_TIMEOUT = 4000;

    private static final java.lang.String REMOTE_COMMAND_CAPTURE = "CAPTURE";

    private static final java.lang.String REMOTE_COMMAND_DUMP = "DUMP";

    private static final java.lang.String REMOTE_COMMAND_DUMP_THEME = "DUMP_THEME";

    private static final java.lang.String REMOTE_COMMAND_INVALIDATE = "INVALIDATE";

    private static final java.lang.String REMOTE_COMMAND_REQUEST_LAYOUT = "REQUEST_LAYOUT";

    private static final java.lang.String REMOTE_PROFILE = "PROFILE";

    private static final java.lang.String REMOTE_COMMAND_CAPTURE_LAYERS = "CAPTURE_LAYERS";

    private static final java.lang.String REMOTE_COMMAND_OUTPUT_DISPLAYLIST = "OUTPUT_DISPLAYLIST";

    private static java.util.HashMap<java.lang.Class<?>, java.lang.reflect.Field[]> sFieldsForClasses;

    private static java.util.HashMap<java.lang.Class<?>, java.lang.reflect.Method[]> sMethodsForClasses;

    private static java.util.HashMap<java.lang.reflect.AccessibleObject, android.view.ViewDebug.ExportedProperty> sAnnotations;

    /**
     *
     *
     * @deprecated This enum is now unused
     */
    @java.lang.Deprecated
    public enum HierarchyTraceType {

        INVALIDATE,
        INVALIDATE_CHILD,
        INVALIDATE_CHILD_IN_PARENT,
        REQUEST_LAYOUT,
        ON_LAYOUT,
        ON_MEASURE,
        DRAW,
        BUILD_CACHE;}

    /**
     *
     *
     * @deprecated This enum is now unused
     */
    @java.lang.Deprecated
    public enum RecyclerTraceType {

        NEW_VIEW,
        BIND_VIEW,
        RECYCLE_FROM_ACTIVE_HEAP,
        RECYCLE_FROM_SCRAP_HEAP,
        MOVE_TO_SCRAP_HEAP,
        MOVE_FROM_ACTIVE_TO_SCRAP_HEAP;}

    /**
     * Returns the number of instanciated Views.
     *
     * @return The number of Views instanciated in the current process.
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public static long getViewInstanceCount() {
        return android.os.Debug.countInstancesOfClass(android.view.View.class);
    }

    /**
     * Returns the number of instanciated ViewAncestors.
     *
     * @return The number of ViewAncestors instanciated in the current process.
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public static long getViewRootImplCount() {
        return android.os.Debug.countInstancesOfClass(android.view.ViewRootImpl.class);
    }

    /**
     *
     *
     * @deprecated This method is now unused and invoking it is a no-op
     */
    @java.lang.Deprecated
    @java.lang.SuppressWarnings({ "UnusedParameters", "deprecation" })
    public static void trace(android.view.View view, android.view.ViewDebug.RecyclerTraceType type, int... parameters) {
    }

    /**
     *
     *
     * @deprecated This method is now unused and invoking it is a no-op
     */
    @java.lang.Deprecated
    @java.lang.SuppressWarnings("UnusedParameters")
    public static void startRecyclerTracing(java.lang.String prefix, android.view.View view) {
    }

    /**
     *
     *
     * @deprecated This method is now unused and invoking it is a no-op
     */
    @java.lang.Deprecated
    @java.lang.SuppressWarnings("UnusedParameters")
    public static void stopRecyclerTracing() {
    }

    /**
     *
     *
     * @deprecated This method is now unused and invoking it is a no-op
     */
    @java.lang.Deprecated
    @java.lang.SuppressWarnings({ "UnusedParameters", "deprecation" })
    public static void trace(android.view.View view, android.view.ViewDebug.HierarchyTraceType type) {
    }

    /**
     *
     *
     * @deprecated This method is now unused and invoking it is a no-op
     */
    @java.lang.Deprecated
    @java.lang.SuppressWarnings("UnusedParameters")
    public static void startHierarchyTracing(java.lang.String prefix, android.view.View view) {
    }

    /**
     *
     *
     * @deprecated This method is now unused and invoking it is a no-op
     */
    @java.lang.Deprecated
    public static void stopHierarchyTracing() {
    }

    @android.annotation.UnsupportedAppUsage
    static void dispatchCommand(android.view.View view, java.lang.String command, java.lang.String parameters, java.io.OutputStream clientStream) throws java.io.IOException {
        // Paranoid but safe...
        view = view.getRootView();
        if (android.view.ViewDebug.REMOTE_COMMAND_DUMP.equalsIgnoreCase(command)) {
            android.view.ViewDebug.dump(view, false, true, clientStream);
        } else
            if (android.view.ViewDebug.REMOTE_COMMAND_DUMP_THEME.equalsIgnoreCase(command)) {
                android.view.ViewDebug.dumpTheme(view, clientStream);
            } else
                if (android.view.ViewDebug.REMOTE_COMMAND_CAPTURE_LAYERS.equalsIgnoreCase(command)) {
                    android.view.ViewDebug.captureLayers(view, new java.io.DataOutputStream(clientStream));
                } else {
                    final java.lang.String[] params = parameters.split(" ");
                    if (android.view.ViewDebug.REMOTE_COMMAND_CAPTURE.equalsIgnoreCase(command)) {
                        android.view.ViewDebug.capture(view, clientStream, params[0]);
                    } else
                        if (android.view.ViewDebug.REMOTE_COMMAND_OUTPUT_DISPLAYLIST.equalsIgnoreCase(command)) {
                            android.view.ViewDebug.outputDisplayList(view, params[0]);
                        } else
                            if (android.view.ViewDebug.REMOTE_COMMAND_INVALIDATE.equalsIgnoreCase(command)) {
                                android.view.ViewDebug.invalidate(view, params[0]);
                            } else
                                if (android.view.ViewDebug.REMOTE_COMMAND_REQUEST_LAYOUT.equalsIgnoreCase(command)) {
                                    android.view.ViewDebug.requestLayout(view, params[0]);
                                } else
                                    if (android.view.ViewDebug.REMOTE_PROFILE.equalsIgnoreCase(command)) {
                                        android.view.ViewDebug.profile(view, clientStream, params[0]);
                                    }




                }


    }

    /**
     *
     *
     * @unknown 
     */
    public static android.view.View findView(android.view.View root, java.lang.String parameter) {
        // Look by type/hashcode
        if (parameter.indexOf('@') != (-1)) {
            final java.lang.String[] ids = parameter.split("@");
            final java.lang.String className = ids[0];
            final int hashCode = ((int) (java.lang.Long.parseLong(ids[1], 16)));
            android.view.View view = root.getRootView();
            if (view instanceof android.view.ViewGroup) {
                return android.view.ViewDebug.findView(((android.view.ViewGroup) (view)), className, hashCode);
            }
        } else {
            // Look by id
            final int id = root.getResources().getIdentifier(parameter, null, null);
            return root.getRootView().findViewById(id);
        }
        return null;
    }

    private static void invalidate(android.view.View root, java.lang.String parameter) {
        final android.view.View view = android.view.ViewDebug.findView(root, parameter);
        if (view != null) {
            view.postInvalidate();
        }
    }

    private static void requestLayout(android.view.View root, java.lang.String parameter) {
        final android.view.View view = android.view.ViewDebug.findView(root, parameter);
        if (view != null) {
            root.post(new java.lang.Runnable() {
                public void run() {
                    view.requestLayout();
                }
            });
        }
    }

    private static void profile(android.view.View root, java.io.OutputStream clientStream, java.lang.String parameter) throws java.io.IOException {
        final android.view.View view = android.view.ViewDebug.findView(root, parameter);
        java.io.BufferedWriter out = null;
        try {
            out = new java.io.BufferedWriter(new java.io.OutputStreamWriter(clientStream), 32 * 1024);
            if (view != null) {
                android.view.ViewDebug.profileViewAndChildren(view, out);
            } else {
                out.write("-1 -1 -1");
                out.newLine();
            }
            out.write("DONE.");
            out.newLine();
        } catch (java.lang.Exception e) {
            android.util.android.util.Log.w("View", "Problem profiling the view:", e);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public static void profileViewAndChildren(final android.view.View view, java.io.BufferedWriter out) throws java.io.IOException {
        android.graphics.RenderNode node = android.graphics.RenderNode.create("ViewDebug", null);
        android.view.ViewDebug.profileViewAndChildren(view, node, out, true);
    }

    private static void profileViewAndChildren(android.view.View view, android.graphics.RenderNode node, java.io.BufferedWriter out, boolean root) throws java.io.IOException {
        long durationMeasure = (root || ((view.mPrivateFlags & android.view.View.PFLAG_MEASURED_DIMENSION_SET) != 0)) ? android.view.ViewDebug.profileViewMeasure(view) : 0;
        long durationLayout = (root || ((view.mPrivateFlags & android.view.View.PFLAG_LAYOUT_REQUIRED) != 0)) ? android.view.ViewDebug.profileViewLayout(view) : 0;
        long durationDraw = ((root || (!view.willNotDraw())) || ((view.mPrivateFlags & android.view.View.PFLAG_DRAWN) != 0)) ? android.view.ViewDebug.profileViewDraw(view, node) : 0;
        out.write(java.lang.String.valueOf(durationMeasure));
        out.write(' ');
        out.write(java.lang.String.valueOf(durationLayout));
        out.write(' ');
        out.write(java.lang.String.valueOf(durationDraw));
        out.newLine();
        if (view instanceof android.view.ViewGroup) {
            android.view.ViewGroup group = ((android.view.ViewGroup) (view));
            final int count = group.getChildCount();
            for (int i = 0; i < count; i++) {
                android.view.ViewDebug.profileViewAndChildren(group.getChildAt(i), node, out, false);
            }
        }
    }

    private static long profileViewMeasure(final android.view.View view) {
        return android.view.ViewDebug.profileViewOperation(view, new android.view.ViewDebug.ViewOperation() {
            @java.lang.Override
            public void pre() {
                forceLayout(view);
            }

            private void forceLayout(android.view.View view) {
                view.forceLayout();
                if (view instanceof android.view.ViewGroup) {
                    android.view.ViewGroup group = ((android.view.ViewGroup) (view));
                    final int count = group.getChildCount();
                    for (int i = 0; i < count; i++) {
                        forceLayout(group.getChildAt(i));
                    }
                }
            }

            @java.lang.Override
            public void run() {
                view.measure(view.mOldWidthMeasureSpec, view.mOldHeightMeasureSpec);
            }
        });
    }

    private static long profileViewLayout(android.view.View view) {
        return android.view.ViewDebug.profileViewOperation(view, () -> view.layout(view.mLeft, view.mTop, view.mRight, view.mBottom));
    }

    private static long profileViewDraw(android.view.View view, android.graphics.RenderNode node) {
        android.util.DisplayMetrics dm = view.getResources().getDisplayMetrics();
        if (dm == null) {
            return 0;
        }
        if (view.isHardwareAccelerated()) {
            android.graphics.RecordingCanvas canvas = node.beginRecording(dm.widthPixels, dm.heightPixels);
            try {
                return android.view.ViewDebug.profileViewOperation(view, () -> view.draw(canvas));
            } finally {
                node.endRecording();
            }
        } else {
            android.graphics.Bitmap bitmap = android.graphics.Bitmap.createBitmap(dm, dm.widthPixels, dm.heightPixels, android.graphics.Bitmap.Config.RGB_565);
            android.graphics.Canvas canvas = new android.graphics.Canvas(bitmap);
            try {
                return android.view.ViewDebug.profileViewOperation(view, () -> view.draw(canvas));
            } finally {
                canvas.setBitmap(null);
                bitmap.recycle();
            }
        }
    }

    interface ViewOperation {
        default void pre() {
        }

        void run();
    }

    private static long profileViewOperation(android.view.View view, final android.view.ViewDebug.ViewOperation operation) {
        final java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(1);
        final long[] duration = new long[1];
        view.post(() -> {
            try {
                operation.pre();
                long start = android.os.Debug.threadCpuTimeNanos();
                // noinspection unchecked
                operation.run();
                duration[0] = android.os.Debug.threadCpuTimeNanos() - start;
            } finally {
                latch.countDown();
            }
        });
        try {
            if (!latch.await(android.view.ViewDebug.CAPTURE_TIMEOUT, java.util.concurrent.TimeUnit.MILLISECONDS)) {
                android.util.Log.w("View", "Could not complete the profiling of the view " + view);
                return -1;
            }
        } catch (java.lang.InterruptedException e) {
            android.util.Log.w("View", "Could not complete the profiling of the view " + view);
            java.lang.Thread.currentThread().interrupt();
            return -1;
        }
        return duration[0];
    }

    /**
     *
     *
     * @unknown 
     */
    public static void captureLayers(android.view.View root, final java.io.DataOutputStream clientStream) throws java.io.IOException {
        try {
            android.graphics.Rect outRect = new android.graphics.Rect();
            try {
                root.mAttachInfo.mSession.getDisplayFrame(root.mAttachInfo.mWindow, outRect);
            } catch (android.os.RemoteException e) {
                // Ignore
            }
            clientStream.writeInt(outRect.width());
            clientStream.writeInt(outRect.height());
            android.view.ViewDebug.captureViewLayer(root, clientStream, true);
            clientStream.write(2);
        } finally {
            clientStream.close();
        }
    }

    private static void captureViewLayer(android.view.View view, java.io.DataOutputStream clientStream, boolean visible) throws java.io.IOException {
        final boolean localVisible = (view.getVisibility() == android.view.View.VISIBLE) && visible;
        if ((view.mPrivateFlags & android.view.View.PFLAG_SKIP_DRAW) != android.view.View.PFLAG_SKIP_DRAW) {
            final int id = view.getId();
            java.lang.String name = view.getClass().getSimpleName();
            if (id != android.view.View.NO_ID) {
                name = android.view.ViewDebug.resolveId(view.getContext(), id).toString();
            }
            clientStream.write(1);
            clientStream.writeUTF(name);
            clientStream.writeByte(localVisible ? 1 : 0);
            int[] position = new int[2];
            // XXX: Should happen on the UI thread
            view.getLocationInWindow(position);
            clientStream.writeInt(position[0]);
            clientStream.writeInt(position[1]);
            clientStream.flush();
            android.graphics.Bitmap b = android.view.ViewDebug.performViewCapture(view, true);
            if (b != null) {
                java.io.ByteArrayOutputStream arrayOut = new java.io.ByteArrayOutputStream((b.getWidth() * b.getHeight()) * 2);
                b.compress(android.graphics.Bitmap.CompressFormat.PNG, 100, arrayOut);
                clientStream.writeInt(arrayOut.size());
                arrayOut.writeTo(clientStream);
            }
            clientStream.flush();
        }
        if (view instanceof android.view.ViewGroup) {
            android.view.ViewGroup group = ((android.view.ViewGroup) (view));
            int count = group.getChildCount();
            for (int i = 0; i < count; i++) {
                android.view.ViewDebug.captureViewLayer(group.getChildAt(i), clientStream, localVisible);
            }
        }
        if (view.mOverlay != null) {
            android.view.ViewGroup overlayContainer = view.getOverlay().mOverlayViewGroup;
            android.view.ViewDebug.captureViewLayer(overlayContainer, clientStream, localVisible);
        }
    }

    private static void outputDisplayList(android.view.View root, java.lang.String parameter) throws java.io.IOException {
        final android.view.View view = android.view.ViewDebug.findView(root, parameter);
        view.getViewRootImpl().outputDisplayList(view);
    }

    /**
     *
     *
     * @unknown 
     */
    public static void outputDisplayList(android.view.View root, android.view.View target) {
        root.getViewRootImpl().outputDisplayList(target);
    }

    private static class PictureCallbackHandler implements android.graphics.HardwareRenderer.PictureCapturedCallback , java.lang.AutoCloseable , java.lang.Runnable {
        private final android.graphics.HardwareRenderer mRenderer;

        private final java.util.function.Function<android.graphics.Picture, java.lang.Boolean> mCallback;

        private final java.util.concurrent.Executor mExecutor;

        private final java.util.concurrent.locks.ReentrantLock mLock = new java.util.concurrent.locks.ReentrantLock(false);

        private final java.util.ArrayDeque<android.graphics.Picture> mQueue = new java.util.ArrayDeque<>(3);

        private boolean mStopListening;

        private java.lang.Thread mRenderThread;

        private PictureCallbackHandler(android.graphics.HardwareRenderer renderer, java.util.function.Function<android.graphics.Picture, java.lang.Boolean> callback, java.util.concurrent.Executor executor) {
            mRenderer = renderer;
            mCallback = callback;
            mExecutor = executor;
            mRenderer.setPictureCaptureCallback(this);
        }

        @java.lang.Override
        public void close() {
            mLock.lock();
            mStopListening = true;
            mLock.unlock();
            mRenderer.setPictureCaptureCallback(null);
        }

        @java.lang.Override
        public void onPictureCaptured(android.graphics.Picture picture) {
            mLock.lock();
            if (mStopListening) {
                mLock.unlock();
                mRenderer.setPictureCaptureCallback(null);
                return;
            }
            if (mRenderThread == null) {
                mRenderThread = java.lang.Thread.currentThread();
            }
            android.graphics.Picture toDestroy = null;
            if (mQueue.size() == 3) {
                toDestroy = mQueue.removeLast();
            }
            mQueue.add(picture);
            mLock.unlock();
            if (toDestroy == null) {
                mExecutor.execute(this);
            } else {
                toDestroy.close();
            }
        }

        @java.lang.Override
        public void run() {
            mLock.lock();
            final android.graphics.Picture picture = mQueue.poll();
            final boolean isStopped = mStopListening;
            mLock.unlock();
            if (java.lang.Thread.currentThread() == mRenderThread) {
                close();
                throw new java.lang.IllegalStateException("ViewDebug#startRenderingCommandsCapture must be given an executor that " + "invokes asynchronously");
            }
            if (isStopped) {
                picture.close();
                return;
            }
            final boolean keepReceiving = mCallback.apply(picture);
            if (!keepReceiving) {
                close();
            }
        }
    }

    /**
     * Begins capturing the entire rendering commands for the view tree referenced by the given
     * view. The view passed may be any View in the tree as long as it is attached. That is,
     * {@link View#isAttachedToWindow()} must be true.
     *
     * Every time a frame is rendered a Picture will be passed to the given callback via the given
     * executor. As long as the callback returns 'true' it will continue to receive new frames.
     * The system will only invoke the callback at a rate that the callback is able to keep up with.
     * That is, if it takes 48ms for the callback to complete and there is a 60fps animation running
     * then the callback will only receive 33% of the frames produced.
     *
     * This method must be called on the same thread as the View tree.
     *
     * @param tree
     * 		The View tree to capture the rendering commands.
     * @param callback
     * 		The callback to invoke on every frame produced. Should return true to
     * 		continue receiving new frames, false to stop capturing.
     * @param executor
     * 		The executor to invoke the callback on. Recommend using a background thread
     * 		to avoid stalling the UI thread. Must be an asynchronous invoke or an
     * 		exception will be thrown.
     * @return a closeable that can be used to stop capturing. May be invoked on any thread. Note
    that the callback may continue to receive another frame or two depending on thread timings.
    Returns null if the capture stream cannot be started, such as if there's no
    HardwareRenderer for the given view tree.
     * @unknown 
     * @deprecated use {@link #startRenderingCommandsCapture(View, Executor, Callable)} instead.
     */
    @android.annotation.TestApi
    @android.annotation.Nullable
    @java.lang.Deprecated
    public static java.lang.AutoCloseable startRenderingCommandsCapture(android.view.View tree, java.util.concurrent.Executor executor, java.util.function.Function<android.graphics.Picture, java.lang.Boolean> callback) {
        final android.view.View.AttachInfo attachInfo = tree.mAttachInfo;
        if (attachInfo == null) {
            throw new java.lang.IllegalArgumentException("Given view isn't attached");
        }
        if (attachInfo.mHandler.getLooper() != android.os.Looper.myLooper()) {
            throw new java.lang.IllegalStateException("Called on the wrong thread." + " Must be called on the thread that owns the given View");
        }
        final android.graphics.HardwareRenderer renderer = attachInfo.mThreadedRenderer;
        if (renderer != null) {
            return new android.view.ViewDebug.PictureCallbackHandler(renderer, callback, executor);
        }
        return null;
    }

    /**
     * Begins capturing the entire rendering commands for the view tree referenced by the given
     * view. The view passed may be any View in the tree as long as it is attached. That is,
     * {@link View#isAttachedToWindow()} must be true.
     *
     * Every time a frame is rendered the callback will be invoked on the given executor to
     * provide an OutputStream to serialize to. As long as the callback returns a valid
     * OutputStream the capturing will continue. The system will only invoke the callback at a rate
     * that the callback & OutputStream is able to keep up with. That is, if it takes 48ms for the
     * callback & serialization to complete and there is a 60fps animation running
     * then the callback will only receive 33% of the frames produced.
     *
     * This method must be called on the same thread as the View tree.
     *
     * @param tree
     * 		The View tree to capture the rendering commands.
     * @param callback
     * 		The callback to invoke on every frame produced. Should return an
     * 		OutputStream to write the data to. Return null to cancel capture. The
     * 		same stream may be returned each time as the serialized data contains
     * 		start & end markers. The callback will not be invoked while a previous
     * 		serialization is being performed, so if a single continuous stream is being
     * 		used it is valid for the callback to write its own metadata to that stream
     * 		in response to callback invocation.
     * @param executor
     * 		The executor to invoke the callback on. Recommend using a background thread
     * 		to avoid stalling the UI thread. Must be an asynchronous invoke or an
     * 		exception will be thrown.
     * @return a closeable that can be used to stop capturing. May be invoked on any thread. Note
    that the callback may continue to receive another frame or two depending on thread timings.
    Returns null if the capture stream cannot be started, such as if there's no
    HardwareRenderer for the given view tree.
     * @unknown 
     */
    @android.annotation.TestApi
    @android.annotation.Nullable
    public static java.lang.AutoCloseable startRenderingCommandsCapture(android.view.View tree, java.util.concurrent.Executor executor, java.util.concurrent.Callable<java.io.OutputStream> callback) {
        final android.view.View.AttachInfo attachInfo = tree.mAttachInfo;
        if (attachInfo == null) {
            throw new java.lang.IllegalArgumentException("Given view isn't attached");
        }
        if (attachInfo.mHandler.getLooper() != android.os.Looper.myLooper()) {
            throw new java.lang.IllegalStateException("Called on the wrong thread." + " Must be called on the thread that owns the given View");
        }
        final android.graphics.HardwareRenderer renderer = attachInfo.mThreadedRenderer;
        if (renderer != null) {
            return new android.view.ViewDebug.PictureCallbackHandler(renderer, ( picture) -> {
                try {
                    java.io.OutputStream stream = callback.call();
                    if (stream != null) {
                        picture.writeToStream(stream);
                        return true;
                    }
                } catch (java.lang.Exception ex) {
                    // fall through
                }
                return false;
            }, executor);
        }
        return null;
    }

    private static void capture(android.view.View root, final java.io.OutputStream clientStream, java.lang.String parameter) throws java.io.IOException {
        final android.view.View captureView = android.view.ViewDebug.findView(root, parameter);
        android.view.ViewDebug.capture(root, clientStream, captureView);
    }

    /**
     *
     *
     * @unknown 
     */
    public static void capture(android.view.View root, final java.io.OutputStream clientStream, android.view.View captureView) throws java.io.IOException {
        android.graphics.Bitmap b = android.view.ViewDebug.performViewCapture(captureView, false);
        if (b == null) {
            android.util.Log.w("View", "Failed to create capture bitmap!");
            // Send an empty one so that it doesn't get stuck waiting for
            // something.
            b = android.graphics.Bitmap.createBitmap(root.getResources().getDisplayMetrics(), 1, 1, android.graphics.Bitmap.Config.ARGB_8888);
        }
        java.io.BufferedOutputStream out = null;
        try {
            out = new java.io.BufferedOutputStream(clientStream, 32 * 1024);
            b.compress(android.graphics.Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
        } finally {
            if (out != null) {
                out.close();
            }
            b.recycle();
        }
    }

    private static android.graphics.Bitmap performViewCapture(final android.view.View captureView, final boolean skipChildren) {
        if (captureView != null) {
            final java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(1);
            final android.graphics.Bitmap[] cache = new android.graphics.Bitmap[1];
            captureView.post(() -> {
                try {
                    android.view.ViewDebug.CanvasProvider provider = (captureView.isHardwareAccelerated()) ? new android.view.ViewDebug.HardwareCanvasProvider() : new android.view.ViewDebug.SoftwareCanvasProvider();
                    cache[0] = captureView.createSnapshot(provider, skipChildren);
                } catch (java.lang.OutOfMemoryError e) {
                    android.util.Log.w("View", "Out of memory for bitmap");
                } finally {
                    latch.countDown();
                }
            });
            try {
                latch.await(android.view.ViewDebug.CAPTURE_TIMEOUT, java.util.concurrent.TimeUnit.MILLISECONDS);
                return cache[0];
            } catch (java.lang.InterruptedException e) {
                android.util.Log.w("View", "Could not complete the capture of the view " + captureView);
                java.lang.Thread.currentThread().interrupt();
            }
        }
        return null;
    }

    /**
     * Dumps the view hierarchy starting from the given view.
     *
     * @deprecated See {@link #dumpv2(View, ByteArrayOutputStream)} below.
     * @unknown 
     */
    @java.lang.Deprecated
    @android.annotation.UnsupportedAppUsage
    public static void dump(android.view.View root, boolean skipChildren, boolean includeProperties, java.io.OutputStream clientStream) throws java.io.IOException {
        java.io.BufferedWriter out = null;
        try {
            out = new java.io.BufferedWriter(new java.io.OutputStreamWriter(clientStream, "utf-8"), 32 * 1024);
            android.view.View view = root.getRootView();
            if (view instanceof android.view.ViewGroup) {
                android.view.ViewGroup group = ((android.view.ViewGroup) (view));
                android.view.ViewDebug.dumpViewHierarchy(group.getContext(), group, out, 0, skipChildren, includeProperties);
            }
            out.write("DONE.");
            out.newLine();
        } catch (java.lang.Exception e) {
            android.util.android.util.Log.w("View", "Problem dumping the view:", e);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    /**
     * Dumps the view hierarchy starting from the given view.
     * Rather than using reflection, it uses View's encode method to obtain all the properties.
     *
     * @unknown 
     */
    public static void dumpv2(@android.annotation.NonNull
    final android.view.View view, @android.annotation.NonNull
    java.io.ByteArrayOutputStream out) throws java.lang.InterruptedException {
        final android.view.ViewHierarchyEncoder encoder = new android.view.ViewHierarchyEncoder(out);
        final java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(1);
        view.post(new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                encoder.addProperty("window:left", view.mAttachInfo.mWindowLeft);
                encoder.addProperty("window:top", view.mAttachInfo.mWindowTop);
                view.encode(encoder);
                latch.countDown();
            }
        });
        latch.await(2, java.util.concurrent.TimeUnit.SECONDS);
        encoder.endStream();
    }

    /**
     * Dumps the theme attributes from the given View.
     *
     * @unknown 
     */
    public static void dumpTheme(android.view.View view, java.io.OutputStream clientStream) throws java.io.IOException {
        java.io.BufferedWriter out = null;
        try {
            out = new java.io.BufferedWriter(new java.io.OutputStreamWriter(clientStream, "utf-8"), 32 * 1024);
            java.lang.String[] attributes = android.view.ViewDebug.getStyleAttributesDump(view.getContext().getResources(), view.getContext().getTheme());
            if (attributes != null) {
                for (int i = 0; i < attributes.length; i += 2) {
                    if (attributes[i] != null) {
                        out.write(attributes[i] + "\n");
                        out.write(attributes[i + 1] + "\n");
                    }
                }
            }
            out.write("DONE.");
            out.newLine();
        } catch (java.lang.Exception e) {
            android.util.android.util.Log.w("View", "Problem dumping View Theme:", e);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    /**
     * Gets the style attributes from the {@link Resources.Theme}. For debugging only.
     *
     * @param resources
     * 		Resources to resolve attributes from.
     * @param theme
     * 		Theme to dump.
     * @return a String array containing pairs of adjacent Theme attribute data: name followed by
    its value.
     * @unknown 
     */
    private static java.lang.String[] getStyleAttributesDump(android.content.res.Resources resources, android.content.res.Resources.Theme theme) {
        android.util.TypedValue outValue = new android.util.TypedValue();
        java.lang.String nullString = "null";
        int i = 0;
        int[] attributes = theme.getAllAttributes();
        java.lang.String[] data = new java.lang.String[attributes.length * 2];
        for (int attributeId : attributes) {
            try {
                data[i] = resources.getResourceName(attributeId);
                data[i + 1] = (theme.resolveAttribute(attributeId, outValue, true)) ? outValue.coerceToString().toString() : nullString;
                i += 2;
                // attempt to replace reference data with its name
                if (outValue.type == android.util.TypedValue.TYPE_REFERENCE) {
                    data[i - 1] = resources.getResourceName(outValue.resourceId);
                }
            } catch (android.content.res.Resources.NotFoundException e) {
                // ignore resources we can't resolve
            }
        }
        return data;
    }

    private static android.view.View findView(android.view.ViewGroup group, java.lang.String className, int hashCode) {
        if (android.view.ViewDebug.isRequestedView(group, className, hashCode)) {
            return group;
        }
        final int count = group.getChildCount();
        for (int i = 0; i < count; i++) {
            final android.view.View view = group.getChildAt(i);
            if (view instanceof android.view.ViewGroup) {
                final android.view.View found = android.view.ViewDebug.findView(((android.view.ViewGroup) (view)), className, hashCode);
                if (found != null) {
                    return found;
                }
            } else
                if (android.view.ViewDebug.isRequestedView(view, className, hashCode)) {
                    return view;
                }

            if (view.mOverlay != null) {
                final android.view.View found = android.view.ViewDebug.findView(((android.view.ViewGroup) (view.mOverlay.mOverlayViewGroup)), className, hashCode);
                if (found != null) {
                    return found;
                }
            }
            if (view instanceof android.view.ViewDebug.HierarchyHandler) {
                final android.view.View found = ((android.view.ViewDebug.HierarchyHandler) (view)).findHierarchyView(className, hashCode);
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
    }

    private static boolean isRequestedView(android.view.View view, java.lang.String className, int hashCode) {
        if (view.hashCode() == hashCode) {
            java.lang.String viewClassName = view.getClass().getName();
            if (className.equals("ViewOverlay")) {
                return viewClassName.equals("android.view.ViewOverlay$OverlayViewGroup");
            } else {
                return className.equals(viewClassName);
            }
        }
        return false;
    }

    private static void dumpViewHierarchy(android.content.Context context, android.view.ViewGroup group, java.io.BufferedWriter out, int level, boolean skipChildren, boolean includeProperties) {
        if (!android.view.ViewDebug.dumpView(context, group, out, level, includeProperties)) {
            return;
        }
        if (skipChildren) {
            return;
        }
        final int count = group.getChildCount();
        for (int i = 0; i < count; i++) {
            final android.view.View view = group.getChildAt(i);
            if (view instanceof android.view.ViewGroup) {
                android.view.ViewDebug.dumpViewHierarchy(context, ((android.view.ViewGroup) (view)), out, level + 1, skipChildren, includeProperties);
            } else {
                android.view.ViewDebug.dumpView(context, view, out, level + 1, includeProperties);
            }
            if (view.mOverlay != null) {
                android.view.ViewOverlay overlay = view.getOverlay();
                android.view.ViewGroup overlayContainer = overlay.mOverlayViewGroup;
                android.view.ViewDebug.dumpViewHierarchy(context, overlayContainer, out, level + 2, skipChildren, includeProperties);
            }
        }
        if (group instanceof android.view.ViewDebug.HierarchyHandler) {
            ((android.view.ViewDebug.HierarchyHandler) (group)).dumpViewHierarchyWithProperties(out, level + 1);
        }
    }

    private static boolean dumpView(android.content.Context context, android.view.View view, java.io.BufferedWriter out, int level, boolean includeProperties) {
        try {
            for (int i = 0; i < level; i++) {
                out.write(' ');
            }
            java.lang.String className = view.getClass().getName();
            if (className.equals("android.view.ViewOverlay$OverlayViewGroup")) {
                className = "ViewOverlay";
            }
            out.write(className);
            out.write('@');
            out.write(java.lang.Integer.toHexString(view.hashCode()));
            out.write(' ');
            if (includeProperties) {
                android.view.ViewDebug.dumpViewProperties(context, view, out);
            }
            out.newLine();
        } catch (java.io.IOException e) {
            android.util.Log.w("View", "Error while dumping hierarchy tree");
            return false;
        }
        return true;
    }

    private static java.lang.reflect.Field[] getExportedPropertyFields(java.lang.Class<?> klass) {
        if (android.view.ViewDebug.sFieldsForClasses == null) {
            android.view.ViewDebug.sFieldsForClasses = new java.util.HashMap<java.lang.Class<?>, java.lang.reflect.Field[]>();
        }
        if (android.view.ViewDebug.sAnnotations == null) {
            android.view.ViewDebug.sAnnotations = new java.util.HashMap<java.lang.reflect.AccessibleObject, android.view.ViewDebug.ExportedProperty>(512);
        }
        final java.util.HashMap<java.lang.Class<?>, java.lang.reflect.Field[]> map = android.view.ViewDebug.sFieldsForClasses;
        java.lang.reflect.Field[] fields = map.get(klass);
        if (fields != null) {
            return fields;
        }
        try {
            final java.lang.reflect.Field[] declaredFields = klass.getDeclaredFieldsUnchecked(false);
            final java.util.ArrayList<java.lang.reflect.Field> foundFields = new java.util.ArrayList<java.lang.reflect.Field>();
            for (final java.lang.reflect.Field field : declaredFields) {
                // Fields which can't be resolved have a null type.
                if ((field.getType() != null) && field.isAnnotationPresent(android.view.ViewDebug.ExportedProperty.class)) {
                    field.setAccessible(true);
                    foundFields.add(field);
                    android.view.ViewDebug.sAnnotations.put(field, field.getAnnotation(android.view.ViewDebug.ExportedProperty.class));
                }
            }
            fields = foundFields.toArray(new java.lang.reflect.Field[foundFields.size()]);
            map.put(klass, fields);
        } catch (java.lang.NoClassDefFoundError e) {
            throw new java.lang.AssertionError(e);
        }
        return fields;
    }

    private static java.lang.reflect.Method[] getExportedPropertyMethods(java.lang.Class<?> klass) {
        if (android.view.ViewDebug.sMethodsForClasses == null) {
            android.view.ViewDebug.sMethodsForClasses = new java.util.HashMap<java.lang.Class<?>, java.lang.reflect.Method[]>(100);
        }
        if (android.view.ViewDebug.sAnnotations == null) {
            android.view.ViewDebug.sAnnotations = new java.util.HashMap<java.lang.reflect.AccessibleObject, android.view.ViewDebug.ExportedProperty>(512);
        }
        final java.util.HashMap<java.lang.Class<?>, java.lang.reflect.Method[]> map = android.view.ViewDebug.sMethodsForClasses;
        java.lang.reflect.Method[] methods = map.get(klass);
        if (methods != null) {
            return methods;
        }
        methods = klass.getDeclaredMethodsUnchecked(false);
        final java.util.ArrayList<java.lang.reflect.Method> foundMethods = new java.util.ArrayList<java.lang.reflect.Method>();
        for (final java.lang.reflect.Method method : methods) {
            // Ensure the method return and parameter types can be resolved.
            try {
                method.getReturnType();
                method.getParameterTypes();
            } catch (java.lang.NoClassDefFoundError e) {
                continue;
            }
            if (((method.getParameterTypes().length == 0) && method.isAnnotationPresent(android.view.ViewDebug.ExportedProperty.class)) && (method.getReturnType() != java.lang.Void.class)) {
                method.setAccessible(true);
                foundMethods.add(method);
                android.view.ViewDebug.sAnnotations.put(method, method.getAnnotation(android.view.ViewDebug.ExportedProperty.class));
            }
        }
        methods = foundMethods.toArray(new java.lang.reflect.Method[foundMethods.size()]);
        map.put(klass, methods);
        return methods;
    }

    private static void dumpViewProperties(android.content.Context context, java.lang.Object view, java.io.BufferedWriter out) throws java.io.IOException {
        android.view.ViewDebug.dumpViewProperties(context, view, out, "");
    }

    private static void dumpViewProperties(android.content.Context context, java.lang.Object view, java.io.BufferedWriter out, java.lang.String prefix) throws java.io.IOException {
        if (view == null) {
            out.write(prefix + "=4,null ");
            return;
        }
        java.lang.Class<?> klass = view.getClass();
        do {
            android.view.ViewDebug.exportFields(context, view, out, klass, prefix);
            android.view.ViewDebug.exportMethods(context, view, out, klass, prefix);
            klass = klass.getSuperclass();
        } while (klass != java.lang.Object.class );
    }

    private static java.lang.Object callMethodOnAppropriateTheadBlocking(final java.lang.reflect.Method method, final java.lang.Object object) throws java.lang.IllegalAccessException, java.lang.reflect.InvocationTargetException, java.util.concurrent.TimeoutException {
        if (!(object instanceof android.view.View)) {
            return method.invoke(object, ((java.lang.Object[]) (null)));
        }
        final android.view.View view = ((android.view.View) (object));
        java.util.concurrent.Callable<java.lang.Object> callable = new java.util.concurrent.Callable<java.lang.Object>() {
            @java.lang.Override
            public java.lang.Object call() throws java.lang.IllegalAccessException, java.lang.reflect.InvocationTargetException {
                return method.invoke(view, ((java.lang.Object[]) (null)));
            }
        };
        java.util.concurrent.FutureTask<java.lang.Object> future = new java.util.concurrent.FutureTask<java.lang.Object>(callable);
        // Try to use the handler provided by the view
        android.os.Handler handler = view.getHandler();
        // Fall back on using the main thread
        if (handler == null) {
            handler = new android.os.Handler(android.os.android.os.Looper.getMainLooper());
        }
        handler.post(future);
        while (true) {
            try {
                return future.get(android.view.ViewDebug.CAPTURE_TIMEOUT, java.util.concurrent.TimeUnit.MILLISECONDS);
            } catch (java.util.concurrent.ExecutionException e) {
                java.lang.Throwable t = e.getCause();
                if (t instanceof java.lang.IllegalAccessException) {
                    throw ((java.lang.IllegalAccessException) (t));
                }
                if (t instanceof java.lang.reflect.InvocationTargetException) {
                    throw ((java.lang.reflect.InvocationTargetException) (t));
                }
                throw new java.lang.RuntimeException("Unexpected exception", t);
            } catch (java.lang.InterruptedException e) {
                // Call get again
            } catch (java.util.concurrent.CancellationException e) {
                throw new java.lang.RuntimeException("Unexpected cancellation exception", e);
            }
        } 
    }

    private static java.lang.String formatIntToHexString(int value) {
        return "0x" + java.lang.Integer.toHexString(value).toUpperCase();
    }

    private static void exportMethods(android.content.Context context, java.lang.Object view, java.io.BufferedWriter out, java.lang.Class<?> klass, java.lang.String prefix) throws java.io.IOException {
        final java.lang.reflect.Method[] methods = android.view.ViewDebug.getExportedPropertyMethods(klass);
        int count = methods.length;
        for (int i = 0; i < count; i++) {
            final java.lang.reflect.Method method = methods[i];
            // noinspection EmptyCatchBlock
            try {
                java.lang.Object methodValue = android.view.ViewDebug.callMethodOnAppropriateTheadBlocking(method, view);
                final java.lang.Class<?> returnType = method.getReturnType();
                final android.view.ViewDebug.ExportedProperty property = android.view.ViewDebug.sAnnotations.get(method);
                java.lang.String categoryPrefix = (property.category().length() != 0) ? property.category() + ":" : "";
                if (returnType == int.class) {
                    if (property.resolveId() && (context != null)) {
                        final int id = ((java.lang.Integer) (methodValue));
                        methodValue = android.view.ViewDebug.resolveId(context, id);
                    } else {
                        final android.view.ViewDebug.FlagToString[] flagsMapping = property.flagMapping();
                        if (flagsMapping.length > 0) {
                            final int intValue = ((java.lang.Integer) (methodValue));
                            final java.lang.String valuePrefix = ((categoryPrefix + prefix) + method.getName()) + '_';
                            android.view.ViewDebug.exportUnrolledFlags(out, flagsMapping, intValue, valuePrefix);
                        }
                        final android.view.ViewDebug.IntToString[] mapping = property.mapping();
                        if (mapping.length > 0) {
                            final int intValue = ((java.lang.Integer) (methodValue));
                            boolean mapped = false;
                            int mappingCount = mapping.length;
                            for (int j = 0; j < mappingCount; j++) {
                                final android.view.ViewDebug.IntToString mapper = mapping[j];
                                if (mapper.from() == intValue) {
                                    methodValue = mapper.to();
                                    mapped = true;
                                    break;
                                }
                            }
                            if (!mapped) {
                                methodValue = intValue;
                            }
                        }
                    }
                } else
                    if (returnType == int[].class) {
                        final int[] array = ((int[]) (methodValue));
                        final java.lang.String valuePrefix = ((categoryPrefix + prefix) + method.getName()) + '_';
                        final java.lang.String suffix = "()";
                        android.view.ViewDebug.exportUnrolledArray(context, out, property, array, valuePrefix, suffix);
                        continue;
                    } else
                        if (returnType == java.lang.String[].class) {
                            final java.lang.String[] array = ((java.lang.String[]) (methodValue));
                            if (property.hasAdjacentMapping() && (array != null)) {
                                for (int j = 0; j < array.length; j += 2) {
                                    if (array[j] != null) {
                                        android.view.ViewDebug.writeEntry(out, categoryPrefix + prefix, array[j], "()", array[j + 1] == null ? "null" : array[j + 1]);
                                    }
                                }
                            }
                            continue;
                        } else
                            if (!returnType.isPrimitive()) {
                                if (property.deepExport()) {
                                    android.view.ViewDebug.dumpViewProperties(context, methodValue, out, prefix + property.prefix());
                                    continue;
                                }
                            }



                android.view.ViewDebug.writeEntry(out, categoryPrefix + prefix, method.getName(), "()", methodValue);
            } catch (java.lang.IllegalAccessException e) {
            } catch (java.lang.reflect.InvocationTargetException e) {
            } catch (java.util.concurrent.TimeoutException e) {
            }
        }
    }

    private static void exportFields(android.content.Context context, java.lang.Object view, java.io.BufferedWriter out, java.lang.Class<?> klass, java.lang.String prefix) throws java.io.IOException {
        final java.lang.reflect.Field[] fields = android.view.ViewDebug.getExportedPropertyFields(klass);
        int count = fields.length;
        for (int i = 0; i < count; i++) {
            final java.lang.reflect.Field field = fields[i];
            // noinspection EmptyCatchBlock
            try {
                java.lang.Object fieldValue = null;
                final java.lang.Class<?> type = field.getType();
                final android.view.ViewDebug.ExportedProperty property = android.view.ViewDebug.sAnnotations.get(field);
                java.lang.String categoryPrefix = (property.category().length() != 0) ? property.category() + ":" : "";
                if ((type == int.class) || (type == byte.class)) {
                    if (property.resolveId() && (context != null)) {
                        final int id = field.getInt(view);
                        fieldValue = android.view.ViewDebug.resolveId(context, id);
                    } else {
                        final android.view.ViewDebug.FlagToString[] flagsMapping = property.flagMapping();
                        if (flagsMapping.length > 0) {
                            final int intValue = field.getInt(view);
                            final java.lang.String valuePrefix = ((categoryPrefix + prefix) + field.getName()) + '_';
                            android.view.ViewDebug.exportUnrolledFlags(out, flagsMapping, intValue, valuePrefix);
                        }
                        final android.view.ViewDebug.IntToString[] mapping = property.mapping();
                        if (mapping.length > 0) {
                            final int intValue = field.getInt(view);
                            int mappingCount = mapping.length;
                            for (int j = 0; j < mappingCount; j++) {
                                final android.view.ViewDebug.IntToString mapped = mapping[j];
                                if (mapped.from() == intValue) {
                                    fieldValue = mapped.to();
                                    break;
                                }
                            }
                            if (fieldValue == null) {
                                fieldValue = intValue;
                            }
                        }
                        if (property.formatToHexString()) {
                            fieldValue = field.get(view);
                            if (type == int.class) {
                                fieldValue = android.view.ViewDebug.formatIntToHexString(((java.lang.Integer) (fieldValue)));
                            } else
                                if (type == byte.class) {
                                    fieldValue = "0x" + java.lang.Byte.toHexString(((java.lang.Byte) (fieldValue)), true);
                                }

                        }
                    }
                } else
                    if (type == int[].class) {
                        final int[] array = ((int[]) (field.get(view)));
                        final java.lang.String valuePrefix = ((categoryPrefix + prefix) + field.getName()) + '_';
                        final java.lang.String suffix = "";
                        android.view.ViewDebug.exportUnrolledArray(context, out, property, array, valuePrefix, suffix);
                        continue;
                    } else
                        if (type == java.lang.String[].class) {
                            final java.lang.String[] array = ((java.lang.String[]) (field.get(view)));
                            if (property.hasAdjacentMapping() && (array != null)) {
                                for (int j = 0; j < array.length; j += 2) {
                                    if (array[j] != null) {
                                        android.view.ViewDebug.writeEntry(out, categoryPrefix + prefix, array[j], "", array[j + 1] == null ? "null" : array[j + 1]);
                                    }
                                }
                            }
                            continue;
                        } else
                            if (!type.isPrimitive()) {
                                if (property.deepExport()) {
                                    android.view.ViewDebug.dumpViewProperties(context, field.get(view), out, prefix + property.prefix());
                                    continue;
                                }
                            }



                if (fieldValue == null) {
                    fieldValue = field.get(view);
                }
                android.view.ViewDebug.writeEntry(out, categoryPrefix + prefix, field.getName(), "", fieldValue);
            } catch (java.lang.IllegalAccessException e) {
            }
        }
    }

    private static void writeEntry(java.io.BufferedWriter out, java.lang.String prefix, java.lang.String name, java.lang.String suffix, java.lang.Object value) throws java.io.IOException {
        out.write(prefix);
        out.write(name);
        out.write(suffix);
        out.write("=");
        android.view.ViewDebug.writeValue(out, value);
        out.write(' ');
    }

    private static void exportUnrolledFlags(java.io.BufferedWriter out, android.view.ViewDebug.FlagToString[] mapping, int intValue, java.lang.String prefix) throws java.io.IOException {
        final int count = mapping.length;
        for (int j = 0; j < count; j++) {
            final android.view.ViewDebug.FlagToString flagMapping = mapping[j];
            final boolean ifTrue = flagMapping.outputIf();
            final int maskResult = intValue & flagMapping.mask();
            final boolean test = maskResult == flagMapping.equals();
            if ((test && ifTrue) || ((!test) && (!ifTrue))) {
                final java.lang.String name = flagMapping.name();
                final java.lang.String value = android.view.ViewDebug.formatIntToHexString(maskResult);
                android.view.ViewDebug.writeEntry(out, prefix, name, "", value);
            }
        }
    }

    /**
     * Converts an integer from a field that is mapped with {@link IntToString} to its string
     * representation.
     *
     * @param clazz
     * 		The class the field is defined on.
     * @param field
     * 		The field on which the {@link ExportedProperty} is defined on.
     * @param integer
     * 		The value to convert.
     * @return The value converted into its string representation.
     * @unknown 
     */
    public static java.lang.String intToString(java.lang.Class<?> clazz, java.lang.String field, int integer) {
        final android.view.ViewDebug.IntToString[] mapping = android.view.ViewDebug.getMapping(clazz, field);
        if (mapping == null) {
            return java.lang.Integer.toString(integer);
        }
        final int count = mapping.length;
        for (int j = 0; j < count; j++) {
            final android.view.ViewDebug.IntToString map = mapping[j];
            if (map.from() == integer) {
                return map.to();
            }
        }
        return java.lang.Integer.toString(integer);
    }

    /**
     * Converts a set of flags from a field that is mapped with {@link FlagToString} to its string
     * representation.
     *
     * @param clazz
     * 		The class the field is defined on.
     * @param field
     * 		The field on which the {@link ExportedProperty} is defined on.
     * @param flags
     * 		The flags to convert.
     * @return The flags converted into their string representations.
     * @unknown 
     */
    public static java.lang.String flagsToString(java.lang.Class<?> clazz, java.lang.String field, int flags) {
        final android.view.ViewDebug.FlagToString[] mapping = android.view.ViewDebug.getFlagMapping(clazz, field);
        if (mapping == null) {
            return java.lang.Integer.toHexString(flags);
        }
        final java.lang.StringBuilder result = new java.lang.StringBuilder();
        final int count = mapping.length;
        for (int j = 0; j < count; j++) {
            final android.view.ViewDebug.FlagToString flagMapping = mapping[j];
            final boolean ifTrue = flagMapping.outputIf();
            final int maskResult = flags & flagMapping.mask();
            final boolean test = maskResult == flagMapping.equals();
            if (test && ifTrue) {
                final java.lang.String name = flagMapping.name();
                result.append(name).append(' ');
            }
        }
        if (result.length() > 0) {
            result.deleteCharAt(result.length() - 1);
        }
        return result.toString();
    }

    private static android.view.ViewDebug.FlagToString[] getFlagMapping(java.lang.Class<?> clazz, java.lang.String field) {
        try {
            return clazz.getDeclaredField(field).getAnnotation(android.view.ViewDebug.ExportedProperty.class).flagMapping();
        } catch (java.lang.NoSuchFieldException e) {
            return null;
        }
    }

    private static android.view.ViewDebug.IntToString[] getMapping(java.lang.Class<?> clazz, java.lang.String field) {
        try {
            return clazz.getDeclaredField(field).getAnnotation(android.view.ViewDebug.ExportedProperty.class).mapping();
        } catch (java.lang.NoSuchFieldException e) {
            return null;
        }
    }

    private static void exportUnrolledArray(android.content.Context context, java.io.BufferedWriter out, android.view.ViewDebug.ExportedProperty property, int[] array, java.lang.String prefix, java.lang.String suffix) throws java.io.IOException {
        final android.view.ViewDebug.IntToString[] indexMapping = property.indexMapping();
        final boolean hasIndexMapping = indexMapping.length > 0;
        final android.view.ViewDebug.IntToString[] mapping = property.mapping();
        final boolean hasMapping = mapping.length > 0;
        final boolean resolveId = property.resolveId() && (context != null);
        final int valuesCount = array.length;
        for (int j = 0; j < valuesCount; j++) {
            java.lang.String name;
            java.lang.String value = null;
            final int intValue = array[j];
            name = java.lang.String.valueOf(j);
            if (hasIndexMapping) {
                int mappingCount = indexMapping.length;
                for (int k = 0; k < mappingCount; k++) {
                    final android.view.ViewDebug.IntToString mapped = indexMapping[k];
                    if (mapped.from() == j) {
                        name = mapped.to();
                        break;
                    }
                }
            }
            if (hasMapping) {
                int mappingCount = mapping.length;
                for (int k = 0; k < mappingCount; k++) {
                    final android.view.ViewDebug.IntToString mapped = mapping[k];
                    if (mapped.from() == intValue) {
                        value = mapped.to();
                        break;
                    }
                }
            }
            if (resolveId) {
                if (value == null)
                    value = ((java.lang.String) (android.view.ViewDebug.resolveId(context, intValue)));

            } else {
                value = java.lang.String.valueOf(intValue);
            }
            android.view.ViewDebug.writeEntry(out, prefix, name, suffix, value);
        }
    }

    static java.lang.Object resolveId(android.content.Context context, int id) {
        java.lang.Object fieldValue;
        final android.content.res.Resources resources = context.getResources();
        if (id >= 0) {
            try {
                fieldValue = (resources.getResourceTypeName(id) + '/') + resources.getResourceEntryName(id);
            } catch (android.content.res.Resources.NotFoundException e) {
                fieldValue = "id/" + android.view.ViewDebug.formatIntToHexString(id);
            }
        } else {
            fieldValue = "NO_ID";
        }
        return fieldValue;
    }

    private static void writeValue(java.io.BufferedWriter out, java.lang.Object value) throws java.io.IOException {
        if (value != null) {
            java.lang.String output = "[EXCEPTION]";
            try {
                output = value.toString().replace("\n", "\\n");
            } finally {
                out.write(java.lang.String.valueOf(output.length()));
                out.write(",");
                out.write(output);
            }
        } else {
            out.write("4,null");
        }
    }

    private static java.lang.reflect.Field[] capturedViewGetPropertyFields(java.lang.Class<?> klass) {
        if (android.view.ViewDebug.mCapturedViewFieldsForClasses == null) {
            android.view.ViewDebug.mCapturedViewFieldsForClasses = new java.util.HashMap<java.lang.Class<?>, java.lang.reflect.Field[]>();
        }
        final java.util.HashMap<java.lang.Class<?>, java.lang.reflect.Field[]> map = android.view.ViewDebug.mCapturedViewFieldsForClasses;
        java.lang.reflect.Field[] fields = map.get(klass);
        if (fields != null) {
            return fields;
        }
        final java.util.ArrayList<java.lang.reflect.Field> foundFields = new java.util.ArrayList<java.lang.reflect.Field>();
        fields = klass.getFields();
        int count = fields.length;
        for (int i = 0; i < count; i++) {
            final java.lang.reflect.Field field = fields[i];
            if (field.isAnnotationPresent(android.view.ViewDebug.CapturedViewProperty.class)) {
                field.setAccessible(true);
                foundFields.add(field);
            }
        }
        fields = foundFields.toArray(new java.lang.reflect.Field[foundFields.size()]);
        map.put(klass, fields);
        return fields;
    }

    private static java.lang.reflect.Method[] capturedViewGetPropertyMethods(java.lang.Class<?> klass) {
        if (android.view.ViewDebug.mCapturedViewMethodsForClasses == null) {
            android.view.ViewDebug.mCapturedViewMethodsForClasses = new java.util.HashMap<java.lang.Class<?>, java.lang.reflect.Method[]>();
        }
        final java.util.HashMap<java.lang.Class<?>, java.lang.reflect.Method[]> map = android.view.ViewDebug.mCapturedViewMethodsForClasses;
        java.lang.reflect.Method[] methods = map.get(klass);
        if (methods != null) {
            return methods;
        }
        final java.util.ArrayList<java.lang.reflect.Method> foundMethods = new java.util.ArrayList<java.lang.reflect.Method>();
        methods = klass.getMethods();
        int count = methods.length;
        for (int i = 0; i < count; i++) {
            final java.lang.reflect.Method method = methods[i];
            if (((method.getParameterTypes().length == 0) && method.isAnnotationPresent(android.view.ViewDebug.CapturedViewProperty.class)) && (method.getReturnType() != java.lang.Void.class)) {
                method.setAccessible(true);
                foundMethods.add(method);
            }
        }
        methods = foundMethods.toArray(new java.lang.reflect.Method[foundMethods.size()]);
        map.put(klass, methods);
        return methods;
    }

    private static java.lang.String capturedViewExportMethods(java.lang.Object obj, java.lang.Class<?> klass, java.lang.String prefix) {
        if (obj == null) {
            return "null";
        }
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        final java.lang.reflect.Method[] methods = android.view.ViewDebug.capturedViewGetPropertyMethods(klass);
        int count = methods.length;
        for (int i = 0; i < count; i++) {
            final java.lang.reflect.Method method = methods[i];
            try {
                java.lang.Object methodValue = method.invoke(obj, ((java.lang.Object[]) (null)));
                final java.lang.Class<?> returnType = method.getReturnType();
                android.view.ViewDebug.CapturedViewProperty property = method.getAnnotation(android.view.ViewDebug.CapturedViewProperty.class);
                if (property.retrieveReturn()) {
                    // we are interested in the second level data only
                    sb.append(android.view.ViewDebug.capturedViewExportMethods(methodValue, returnType, method.getName() + "#"));
                } else {
                    sb.append(prefix);
                    sb.append(method.getName());
                    sb.append("()=");
                    if (methodValue != null) {
                        final java.lang.String value = methodValue.toString().replace("\n", "\\n");
                        sb.append(value);
                    } else {
                        sb.append("null");
                    }
                    sb.append("; ");
                }
            } catch (java.lang.IllegalAccessException e) {
                // Exception IllegalAccess, it is OK here
                // we simply ignore this method
            } catch (java.lang.reflect.InvocationTargetException e) {
                // Exception InvocationTarget, it is OK here
                // we simply ignore this method
            }
        }
        return sb.toString();
    }

    private static java.lang.String capturedViewExportFields(java.lang.Object obj, java.lang.Class<?> klass, java.lang.String prefix) {
        if (obj == null) {
            return "null";
        }
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        final java.lang.reflect.Field[] fields = android.view.ViewDebug.capturedViewGetPropertyFields(klass);
        int count = fields.length;
        for (int i = 0; i < count; i++) {
            final java.lang.reflect.Field field = fields[i];
            try {
                java.lang.Object fieldValue = field.get(obj);
                sb.append(prefix);
                sb.append(field.getName());
                sb.append("=");
                if (fieldValue != null) {
                    final java.lang.String value = fieldValue.toString().replace("\n", "\\n");
                    sb.append(value);
                } else {
                    sb.append("null");
                }
                sb.append(' ');
            } catch (java.lang.IllegalAccessException e) {
                // Exception IllegalAccess, it is OK here
                // we simply ignore this field
            }
        }
        return sb.toString();
    }

    /**
     * Dump view info for id based instrument test generation
     * (and possibly further data analysis). The results are dumped
     * to the log.
     *
     * @param tag
     * 		for log
     * @param view
     * 		for dump
     */
    public static void dumpCapturedView(java.lang.String tag, java.lang.Object view) {
        java.lang.Class<?> klass = view.getClass();
        java.lang.StringBuilder sb = new java.lang.StringBuilder(klass.getName() + ": ");
        sb.append(android.view.ViewDebug.capturedViewExportFields(view, klass, ""));
        sb.append(android.view.ViewDebug.capturedViewExportMethods(view, klass, ""));
        android.util.Log.d(tag, sb.toString());
    }

    /**
     * Invoke a particular method on given view.
     * The given method is always invoked on the UI thread. The caller thread will stall until the
     * method invocation is complete. Returns an object equal to the result of the method
     * invocation, null if the method is declared to return void
     *
     * @throws Exception
     * 		if the method invocation caused any exception
     * @unknown 
     */
    public static java.lang.Object invokeViewMethod(final android.view.View view, final java.lang.reflect.Method method, final java.lang.Object[] args) {
        final java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(1);
        final java.util.concurrent.atomic.AtomicReference<java.lang.Object> result = new java.util.concurrent.atomic.AtomicReference<java.lang.Object>();
        final java.util.concurrent.atomic.AtomicReference<java.lang.Throwable> exception = new java.util.concurrent.atomic.AtomicReference<java.lang.Throwable>();
        view.post(new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                try {
                    result.set(method.invoke(view, args));
                } catch (java.lang.reflect.InvocationTargetException e) {
                    exception.set(e.getCause());
                } catch (java.lang.Exception e) {
                    exception.set(e);
                }
                latch.countDown();
            }
        });
        try {
            latch.await();
        } catch (java.lang.InterruptedException e) {
            throw new java.lang.RuntimeException(e);
        }
        if (exception.get() != null) {
            throw new java.lang.RuntimeException(exception.get());
        }
        return result.get();
    }

    /**
     *
     *
     * @unknown 
     */
    public static void setLayoutParameter(final android.view.View view, final java.lang.String param, final int value) throws java.lang.IllegalAccessException, java.lang.NoSuchFieldException {
        final android.view.ViewGroup.LayoutParams p = view.getLayoutParams();
        final java.lang.reflect.Field f = p.getClass().getField(param);
        if (f.getType() != int.class) {
            throw new java.lang.RuntimeException((("Only integer layout parameters can be set. Field " + param) + " is of type ") + f.getType().getSimpleName());
        }
        f.set(p, java.lang.Integer.valueOf(value));
        view.post(new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                view.setLayoutParams(p);
            }
        });
    }

    /**
     *
     *
     * @unknown 
     */
    public static class SoftwareCanvasProvider implements android.view.ViewDebug.CanvasProvider {
        private android.graphics.Canvas mCanvas;

        private android.graphics.Bitmap mBitmap;

        private boolean mEnabledHwBitmapsInSwMode;

        @java.lang.Override
        public android.graphics.Canvas getCanvas(android.view.View view, int width, int height) {
            mBitmap = android.graphics.Bitmap.createBitmap(view.getResources().getDisplayMetrics(), width, height, android.graphics.Bitmap.Config.ARGB_8888);
            if (mBitmap == null) {
                throw new java.lang.OutOfMemoryError();
            }
            mBitmap.setDensity(view.getResources().getDisplayMetrics().densityDpi);
            if (view.mAttachInfo != null) {
                mCanvas = view.mAttachInfo.mCanvas;
            }
            if (mCanvas == null) {
                mCanvas = new android.graphics.Canvas();
            }
            mEnabledHwBitmapsInSwMode = mCanvas.isHwBitmapsInSwModeEnabled();
            mCanvas.setBitmap(mBitmap);
            return mCanvas;
        }

        @java.lang.Override
        public android.graphics.Bitmap createBitmap() {
            mCanvas.setBitmap(null);
            mCanvas.setHwBitmapsInSwModeEnabled(mEnabledHwBitmapsInSwMode);
            return mBitmap;
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public static class HardwareCanvasProvider implements android.view.ViewDebug.CanvasProvider {
        private android.graphics.Picture mPicture;

        @java.lang.Override
        public android.graphics.Canvas getCanvas(android.view.View view, int width, int height) {
            mPicture = new android.graphics.Picture();
            return mPicture.beginRecording(width, height);
        }

        @java.lang.Override
        public android.graphics.Bitmap createBitmap() {
            mPicture.endRecording();
            return android.graphics.Bitmap.createBitmap(mPicture);
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public interface CanvasProvider {
        /**
         * Returns a canvas which can be used to draw {@param view}
         */
        android.graphics.Canvas getCanvas(android.view.View view, int width, int height);

        /**
         * Creates a bitmap from previously returned canvas
         *
         * @return 
         */
        android.graphics.Bitmap createBitmap();
    }
}

