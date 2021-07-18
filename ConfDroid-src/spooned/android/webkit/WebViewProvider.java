/**
 * Copyright (C) 2012 The Android Open Source Project
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
package android.webkit;


/**
 * WebView backend provider interface: this interface is the abstract backend to a WebView
 * instance; each WebView object is bound to exactly one WebViewProvider object which implements
 * the runtime behavior of that WebView.
 *
 * All methods must behave as per their namesake in {@link WebView}, unless otherwise noted.
 *
 * @unknown Not part of the public API; only required by system implementors.
 */
@android.annotation.SystemApi
public interface WebViewProvider {
    // -------------------------------------------------------------------------
    // Main interface for backend provider of the WebView class.
    // -------------------------------------------------------------------------
    /**
     * Initialize this WebViewProvider instance. Called after the WebView has fully constructed.
     *
     * @param javaScriptInterfaces
     * 		is a Map of interface names, as keys, and
     * 		object implementing those interfaces, as values.
     * @param privateBrowsing
     * 		If true the web view will be initialized in private / incognito mode.
     */
    public void init(java.util.Map<java.lang.String, java.lang.Object> javaScriptInterfaces, boolean privateBrowsing);

    // Deprecated - should never be called
    public void setHorizontalScrollbarOverlay(boolean overlay);

    // Deprecated - should never be called
    public void setVerticalScrollbarOverlay(boolean overlay);

    // Deprecated - should never be called
    public boolean overlayHorizontalScrollbar();

    // Deprecated - should never be called
    public boolean overlayVerticalScrollbar();

    public int getVisibleTitleHeight();

    public android.net.http.SslCertificate getCertificate();

    public void setCertificate(android.net.http.SslCertificate certificate);

    public void savePassword(java.lang.String host, java.lang.String username, java.lang.String password);

    public void setHttpAuthUsernamePassword(java.lang.String host, java.lang.String realm, java.lang.String username, java.lang.String password);

    public java.lang.String[] getHttpAuthUsernamePassword(java.lang.String host, java.lang.String realm);

    /**
     * See {@link WebView#destroy()}.
     * As well as releasing the internal state and resources held by the implementation,
     * the provider should null all references it holds on the WebView proxy class, and ensure
     * no further method calls are made to it.
     */
    public void destroy();

    public void setNetworkAvailable(boolean networkUp);

    public android.webkit.WebBackForwardList saveState(android.os.Bundle outState);

    public boolean savePicture(android.os.Bundle b, final java.io.File dest);

    public boolean restorePicture(android.os.Bundle b, java.io.File src);

    public android.webkit.WebBackForwardList restoreState(android.os.Bundle inState);

    public void loadUrl(java.lang.String url, java.util.Map<java.lang.String, java.lang.String> additionalHttpHeaders);

    public void loadUrl(java.lang.String url);

    public void postUrl(java.lang.String url, byte[] postData);

    public void loadData(java.lang.String data, java.lang.String mimeType, java.lang.String encoding);

    public void loadDataWithBaseURL(java.lang.String baseUrl, java.lang.String data, java.lang.String mimeType, java.lang.String encoding, java.lang.String historyUrl);

    public void evaluateJavaScript(java.lang.String script, android.webkit.ValueCallback<java.lang.String> resultCallback);

    public void saveWebArchive(java.lang.String filename);

    public void saveWebArchive(java.lang.String basename, boolean autoname, android.webkit.ValueCallback<java.lang.String> callback);

    public void stopLoading();

    public void reload();

    public boolean canGoBack();

    public void goBack();

    public boolean canGoForward();

    public void goForward();

    public boolean canGoBackOrForward(int steps);

    public void goBackOrForward(int steps);

    public boolean isPrivateBrowsingEnabled();

    public boolean pageUp(boolean top);

    public boolean pageDown(boolean bottom);

    public void insertVisualStateCallback(long requestId, android.webkit.WebView.VisualStateCallback callback);

    public void clearView();

    public android.graphics.Picture capturePicture();

    public android.print.PrintDocumentAdapter createPrintDocumentAdapter(java.lang.String documentName);

    public float getScale();

    public void setInitialScale(int scaleInPercent);

    public void invokeZoomPicker();

    public android.webkit.WebView.HitTestResult getHitTestResult();

    public void requestFocusNodeHref(android.os.Message hrefMsg);

    public void requestImageRef(android.os.Message msg);

    public java.lang.String getUrl();

    public java.lang.String getOriginalUrl();

    public java.lang.String getTitle();

    public android.graphics.Bitmap getFavicon();

    public java.lang.String getTouchIconUrl();

    public int getProgress();

    public int getContentHeight();

    public int getContentWidth();

    public void pauseTimers();

    public void resumeTimers();

    public void onPause();

    public void onResume();

    public boolean isPaused();

    public void freeMemory();

    public void clearCache(boolean includeDiskFiles);

    public void clearFormData();

    public void clearHistory();

    public void clearSslPreferences();

    public android.webkit.WebBackForwardList copyBackForwardList();

    public void setFindListener(android.webkit.WebView.FindListener listener);

    public void findNext(boolean forward);

    public int findAll(java.lang.String find);

    public void findAllAsync(java.lang.String find);

    public boolean showFindDialog(java.lang.String text, boolean showIme);

    public void clearMatches();

    public void documentHasImages(android.os.Message response);

    public void setWebViewClient(android.webkit.WebViewClient client);

    public void setDownloadListener(android.webkit.DownloadListener listener);

    public void setWebChromeClient(android.webkit.WebChromeClient client);

    public void setPictureListener(android.webkit.WebView.PictureListener listener);

    public void addJavascriptInterface(java.lang.Object obj, java.lang.String interfaceName);

    public void removeJavascriptInterface(java.lang.String interfaceName);

    public android.webkit.WebMessagePort[] createWebMessageChannel();

    public void postMessageToMainFrame(android.webkit.WebMessage message, android.net.Uri targetOrigin);

    public android.webkit.WebSettings getSettings();

    public void setMapTrackballToArrowKeys(boolean setMap);

    public void flingScroll(int vx, int vy);

    public android.view.View getZoomControls();

    public boolean canZoomIn();

    public boolean canZoomOut();

    public boolean zoomBy(float zoomFactor);

    public boolean zoomIn();

    public boolean zoomOut();

    public void dumpViewHierarchyWithProperties(java.io.BufferedWriter out, int level);

    public android.view.View findHierarchyView(java.lang.String className, int hashCode);

    // -------------------------------------------------------------------------
    // Provider internal methods
    // -------------------------------------------------------------------------
    /**
     *
     *
     * @return the ViewDelegate implementation. This provides the functionality to back all of
    the name-sake functions from the View and ViewGroup base classes of WebView.
     */
    /* package */
    android.webkit.WebViewProvider.ViewDelegate getViewDelegate();

    /**
     *
     *
     * @return a ScrollDelegate implementation. Normally this would be same object as is
    returned by getViewDelegate().
     */
    /* package */
    android.webkit.WebViewProvider.ScrollDelegate getScrollDelegate();

    /**
     * Only used by FindActionModeCallback to inform providers that the find dialog has
     * been dismissed.
     */
    public void notifyFindDialogDismissed();

    // -------------------------------------------------------------------------
    interface ViewDelegate {
        // View / ViewGroup delegation methods
        // -------------------------------------------------------------------------
        /**
         * Provides mechanism for the name-sake methods declared in View and ViewGroup to be delegated
         * into the WebViewProvider instance.
         * NOTE For many of these methods, the WebView will provide a super.Foo() call before or after
         * making the call into the provider instance. This is done for convenience in the common case
         * of maintaining backward compatibility. For remaining super class calls (e.g. where the
         * provider may need to only conditionally make the call based on some internal state) see the
         * {@link WebView.PrivateAccess} callback class.
         */
        // TODO: See if the pattern of the super-class calls can be rationalized at all, and document
        // the remainder on the methods below.
        public boolean shouldDelayChildPressedState();

        public void onProvideVirtualStructure(android.view.ViewStructure structure);

        public android.view.accessibility.AccessibilityNodeProvider getAccessibilityNodeProvider();

        public void onInitializeAccessibilityNodeInfo(android.view.accessibility.AccessibilityNodeInfo info);

        public void onInitializeAccessibilityEvent(android.view.accessibility.AccessibilityEvent event);

        public boolean performAccessibilityAction(int action, android.os.Bundle arguments);

        public void setOverScrollMode(int mode);

        public void setScrollBarStyle(int style);

        public void onDrawVerticalScrollBar(android.graphics.Canvas canvas, android.graphics.drawable.Drawable scrollBar, int l, int t, int r, int b);

        public void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY);

        public void onWindowVisibilityChanged(int visibility);

        public void onDraw(android.graphics.Canvas canvas);

        public void setLayoutParams(android.view.ViewGroup.LayoutParams layoutParams);

        public boolean performLongClick();

        public void onConfigurationChanged(android.content.res.Configuration newConfig);

        public android.view.inputmethod.InputConnection onCreateInputConnection(android.view.inputmethod.EditorInfo outAttrs);

        public boolean onDragEvent(android.view.DragEvent event);

        public boolean onKeyMultiple(int keyCode, int repeatCount, android.view.KeyEvent event);

        public boolean onKeyDown(int keyCode, android.view.KeyEvent event);

        public boolean onKeyUp(int keyCode, android.view.KeyEvent event);

        public void onAttachedToWindow();

        public void onDetachedFromWindow();

        public void onVisibilityChanged(android.view.View changedView, int visibility);

        public void onWindowFocusChanged(boolean hasWindowFocus);

        public void onFocusChanged(boolean focused, int direction, android.graphics.Rect previouslyFocusedRect);

        public boolean setFrame(int left, int top, int right, int bottom);

        public void onSizeChanged(int w, int h, int ow, int oh);

        public void onScrollChanged(int l, int t, int oldl, int oldt);

        public boolean dispatchKeyEvent(android.view.KeyEvent event);

        public boolean onTouchEvent(android.view.MotionEvent ev);

        public boolean onHoverEvent(android.view.MotionEvent event);

        public boolean onGenericMotionEvent(android.view.MotionEvent event);

        public boolean onTrackballEvent(android.view.MotionEvent ev);

        public boolean requestFocus(int direction, android.graphics.Rect previouslyFocusedRect);

        public void onMeasure(int widthMeasureSpec, int heightMeasureSpec);

        public boolean requestChildRectangleOnScreen(android.view.View child, android.graphics.Rect rect, boolean immediate);

        public void setBackgroundColor(int color);

        public void setLayerType(int layerType, android.graphics.Paint paint);

        public void preDispatchDraw(android.graphics.Canvas canvas);

        public void onStartTemporaryDetach();

        public void onFinishTemporaryDetach();

        public void onActivityResult(int requestCode, int resultCode, android.content.Intent data);

        public android.os.Handler getHandler(android.os.Handler originalHandler);

        public android.view.View findFocus(android.view.View originalFocusedView);
    }

    interface ScrollDelegate {
        // These methods are declared protected in the ViewGroup base class. This interface
        // exists to promote them to public so they may be called by the WebView proxy class.
        // TODO: Combine into ViewDelegate?
        /**
         * See {@link android.webkit.WebView#computeHorizontalScrollRange}
         */
        public int computeHorizontalScrollRange();

        /**
         * See {@link android.webkit.WebView#computeHorizontalScrollOffset}
         */
        public int computeHorizontalScrollOffset();

        /**
         * See {@link android.webkit.WebView#computeVerticalScrollRange}
         */
        public int computeVerticalScrollRange();

        /**
         * See {@link android.webkit.WebView#computeVerticalScrollOffset}
         */
        public int computeVerticalScrollOffset();

        /**
         * See {@link android.webkit.WebView#computeVerticalScrollExtent}
         */
        public int computeVerticalScrollExtent();

        /**
         * See {@link android.webkit.WebView#computeScroll}
         */
        public void computeScroll();
    }
}

