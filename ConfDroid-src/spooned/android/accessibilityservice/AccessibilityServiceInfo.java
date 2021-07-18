/**
 * Copyright (C) 2009 The Android Open Source Project
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
package android.accessibilityservice;


/**
 * This class describes an {@link AccessibilityService}. The system notifies an
 * {@link AccessibilityService} for {@link android.view.accessibility.AccessibilityEvent}s
 * according to the information encapsulated in this class.
 *
 * <div class="special reference">
 * <h3>Developer Guides</h3>
 * <p>For more information about creating AccessibilityServices, read the
 * <a href="{@docRoot }guide/topics/ui/accessibility/index.html">Accessibility</a>
 * developer guide.</p>
 * </div>
 *
 * @unknown ref android.R.styleable#AccessibilityService_accessibilityEventTypes
 * @unknown ref android.R.styleable#AccessibilityService_accessibilityFeedbackType
 * @unknown ref android.R.styleable#AccessibilityService_accessibilityFlags
 * @unknown ref android.R.styleable#AccessibilityService_canRequestEnhancedWebAccessibility
 * @unknown ref android.R.styleable#AccessibilityService_canRequestFilterKeyEvents
 * @unknown ref android.R.styleable#AccessibilityService_canRequestTouchExplorationMode
 * @unknown ref android.R.styleable#AccessibilityService_canRetrieveWindowContent
 * @unknown ref android.R.styleable#AccessibilityService_description
 * @unknown ref android.R.styleable#AccessibilityService_notificationTimeout
 * @unknown ref android.R.styleable#AccessibilityService_packageNames
 * @unknown ref android.R.styleable#AccessibilityService_settingsActivity
 * @see AccessibilityService
 * @see android.view.accessibility.AccessibilityEvent
 * @see android.view.accessibility.AccessibilityManager
 */
public class AccessibilityServiceInfo implements android.os.Parcelable {
    private static final java.lang.String TAG_ACCESSIBILITY_SERVICE = "accessibility-service";

    /**
     * Capability: This accessibility service can retrieve the active window content.
     *
     * @see android.R.styleable#AccessibilityService_canRetrieveWindowContent
     */
    public static final int CAPABILITY_CAN_RETRIEVE_WINDOW_CONTENT = 0x1;

    /**
     * Capability: This accessibility service can request touch exploration mode in which
     * touched items are spoken aloud and the UI can be explored via gestures.
     *
     * @see android.R.styleable#AccessibilityService_canRequestTouchExplorationMode
     */
    public static final int CAPABILITY_CAN_REQUEST_TOUCH_EXPLORATION = 0x2;

    /**
     * Capability: This accessibility service can request enhanced web accessibility
     * enhancements. For example, installing scripts to make app content more accessible.
     *
     * @see android.R.styleable#AccessibilityService_canRequestEnhancedWebAccessibility
     */
    public static final int CAPABILITY_CAN_REQUEST_ENHANCED_WEB_ACCESSIBILITY = 0x4;

    /**
     * Capability: This accessibility service can request to filter the key event stream.
     *
     * @see android.R.styleable#AccessibilityService_canRequestFilterKeyEvents
     */
    public static final int CAPABILITY_CAN_REQUEST_FILTER_KEY_EVENTS = 0x8;

    /**
     * Capability: This accessibility service can control display magnification.
     *
     * @see android.R.styleable#AccessibilityService_canControlMagnification
     */
    public static final int CAPABILITY_CAN_CONTROL_MAGNIFICATION = 0x10;

    /**
     * Capability: This accessibility service can perform gestures.
     *
     * @see android.R.styleable#AccessibilityService_canPerformGestures
     */
    public static final int CAPABILITY_CAN_PERFORM_GESTURES = 0x20;

    private static final android.util.SparseArray<android.accessibilityservice.AccessibilityServiceInfo.CapabilityInfo> sAvailableCapabilityInfos = new android.util.SparseArray<android.accessibilityservice.AccessibilityServiceInfo.CapabilityInfo>();

    static {
        android.accessibilityservice.AccessibilityServiceInfo.sAvailableCapabilityInfos.put(android.accessibilityservice.AccessibilityServiceInfo.CAPABILITY_CAN_RETRIEVE_WINDOW_CONTENT, new android.accessibilityservice.AccessibilityServiceInfo.CapabilityInfo(android.accessibilityservice.AccessibilityServiceInfo.CAPABILITY_CAN_RETRIEVE_WINDOW_CONTENT, R.string.capability_title_canRetrieveWindowContent, R.string.capability_desc_canRetrieveWindowContent));
        android.accessibilityservice.AccessibilityServiceInfo.sAvailableCapabilityInfos.put(android.accessibilityservice.AccessibilityServiceInfo.CAPABILITY_CAN_REQUEST_TOUCH_EXPLORATION, new android.accessibilityservice.AccessibilityServiceInfo.CapabilityInfo(android.accessibilityservice.AccessibilityServiceInfo.CAPABILITY_CAN_REQUEST_TOUCH_EXPLORATION, R.string.capability_title_canRequestTouchExploration, R.string.capability_desc_canRequestTouchExploration));
        android.accessibilityservice.AccessibilityServiceInfo.sAvailableCapabilityInfos.put(android.accessibilityservice.AccessibilityServiceInfo.CAPABILITY_CAN_REQUEST_ENHANCED_WEB_ACCESSIBILITY, new android.accessibilityservice.AccessibilityServiceInfo.CapabilityInfo(android.accessibilityservice.AccessibilityServiceInfo.CAPABILITY_CAN_REQUEST_ENHANCED_WEB_ACCESSIBILITY, R.string.capability_title_canRequestEnhancedWebAccessibility, R.string.capability_desc_canRequestEnhancedWebAccessibility));
        android.accessibilityservice.AccessibilityServiceInfo.sAvailableCapabilityInfos.put(android.accessibilityservice.AccessibilityServiceInfo.CAPABILITY_CAN_REQUEST_FILTER_KEY_EVENTS, new android.accessibilityservice.AccessibilityServiceInfo.CapabilityInfo(android.accessibilityservice.AccessibilityServiceInfo.CAPABILITY_CAN_REQUEST_FILTER_KEY_EVENTS, R.string.capability_title_canRequestFilterKeyEvents, R.string.capability_desc_canRequestFilterKeyEvents));
        android.accessibilityservice.AccessibilityServiceInfo.sAvailableCapabilityInfos.put(android.accessibilityservice.AccessibilityServiceInfo.CAPABILITY_CAN_CONTROL_MAGNIFICATION, new android.accessibilityservice.AccessibilityServiceInfo.CapabilityInfo(android.accessibilityservice.AccessibilityServiceInfo.CAPABILITY_CAN_CONTROL_MAGNIFICATION, R.string.capability_title_canControlMagnification, R.string.capability_desc_canControlMagnification));
        android.accessibilityservice.AccessibilityServiceInfo.sAvailableCapabilityInfos.put(android.accessibilityservice.AccessibilityServiceInfo.CAPABILITY_CAN_PERFORM_GESTURES, new android.accessibilityservice.AccessibilityServiceInfo.CapabilityInfo(android.accessibilityservice.AccessibilityServiceInfo.CAPABILITY_CAN_PERFORM_GESTURES, R.string.capability_title_canPerformGestures, R.string.capability_desc_canPerformGestures));
    }

    /**
     * Denotes spoken feedback.
     */
    public static final int FEEDBACK_SPOKEN = 0x1;

    /**
     * Denotes haptic feedback.
     */
    public static final int FEEDBACK_HAPTIC = 0x2;

    /**
     * Denotes audible (not spoken) feedback.
     */
    public static final int FEEDBACK_AUDIBLE = 0x4;

    /**
     * Denotes visual feedback.
     */
    public static final int FEEDBACK_VISUAL = 0x8;

    /**
     * Denotes generic feedback.
     */
    public static final int FEEDBACK_GENERIC = 0x10;

    /**
     * Denotes braille feedback.
     */
    public static final int FEEDBACK_BRAILLE = 0x20;

    /**
     * Mask for all feedback types.
     *
     * @see #FEEDBACK_SPOKEN
     * @see #FEEDBACK_HAPTIC
     * @see #FEEDBACK_AUDIBLE
     * @see #FEEDBACK_VISUAL
     * @see #FEEDBACK_GENERIC
     * @see #FEEDBACK_BRAILLE
     */
    public static final int FEEDBACK_ALL_MASK = 0xffffffff;

    /**
     * If an {@link AccessibilityService} is the default for a given type.
     * Default service is invoked only if no package specific one exists. In case of
     * more than one package specific service only the earlier registered is notified.
     */
    public static final int DEFAULT = 0x1;

    /**
     * If this flag is set the system will regard views that are not important
     * for accessibility in addition to the ones that are important for accessibility.
     * That is, views that are marked as not important for accessibility via
     * {@link View#IMPORTANT_FOR_ACCESSIBILITY_NO} or
     * {@link View#IMPORTANT_FOR_ACCESSIBILITY_NO_HIDE_DESCENDANTS} and views that are
     * marked as potentially important for accessibility via
     * {@link View#IMPORTANT_FOR_ACCESSIBILITY_AUTO} for which the system has determined
     * that are not important for accessibility, are reported while querying the window
     * content and also the accessibility service will receive accessibility events from
     * them.
     * <p>
     * <strong>Note:</strong> For accessibility services targeting API version
     * {@link Build.VERSION_CODES#JELLY_BEAN} or higher this flag has to be explicitly
     * set for the system to regard views that are not important for accessibility. For
     * accessibility services targeting API version lower than
     * {@link Build.VERSION_CODES#JELLY_BEAN} this flag is ignored and all views are
     * regarded for accessibility purposes.
     * </p>
     * <p>
     * Usually views not important for accessibility are layout managers that do not
     * react to user actions, do not draw any content, and do not have any special
     * semantics in the context of the screen content. For example, a three by three
     * grid can be implemented as three horizontal linear layouts and one vertical,
     * or three vertical linear layouts and one horizontal, or one grid layout, etc.
     * In this context the actual layout mangers used to achieve the grid configuration
     * are not important, rather it is important that there are nine evenly distributed
     * elements.
     * </p>
     */
    public static final int FLAG_INCLUDE_NOT_IMPORTANT_VIEWS = 0x2;

    /**
     * This flag requests that the system gets into touch exploration mode.
     * In this mode a single finger moving on the screen behaves as a mouse
     * pointer hovering over the user interface. The system will also detect
     * certain gestures performed on the touch screen and notify this service.
     * The system will enable touch exploration mode if there is at least one
     * accessibility service that has this flag set. Hence, clearing this
     * flag does not guarantee that the device will not be in touch exploration
     * mode since there may be another enabled service that requested it.
     * <p>
     * For accessibility services targeting API version higher than
     * {@link Build.VERSION_CODES#JELLY_BEAN_MR1} that want to set
     * this flag have to declare this capability in their meta-data by setting
     * the attribute {@link android.R.attr#canRequestTouchExplorationMode
     * canRequestTouchExplorationMode} to true, otherwise this flag will
     * be ignored. For how to declare the meta-data of a service refer to
     * {@value AccessibilityService#SERVICE_META_DATA}.
     * </p>
     * <p>
     * Services targeting API version equal to or lower than
     * {@link Build.VERSION_CODES#JELLY_BEAN_MR1} will work normally, i.e.
     * the first time they are run, if this flag is specified, a dialog is
     * shown to the user to confirm enabling explore by touch.
     * </p>
     *
     * @see android.R.styleable#AccessibilityService_canRequestTouchExplorationMode
     */
    public static final int FLAG_REQUEST_TOUCH_EXPLORATION_MODE = 0x4;

    /**
     * This flag requests from the system to enable web accessibility enhancing
     * extensions. Such extensions aim to provide improved accessibility support
     * for content presented in a {@link android.webkit.WebView}. An example of such
     * an extension is injecting JavaScript from a secure source. The system will enable
     * enhanced web accessibility if there is at least one accessibility service
     * that has this flag set. Hence, clearing this flag does not guarantee that the
     * device will not have enhanced web accessibility enabled since there may be
     * another enabled service that requested it.
     * <p>
     * Services that want to set this flag have to declare this capability
     * in their meta-data by setting the attribute {@link android.R.attr
     * #canRequestEnhancedWebAccessibility canRequestEnhancedWebAccessibility} to
     * true, otherwise this flag will be ignored. For how to declare the meta-data
     * of a service refer to {@value AccessibilityService#SERVICE_META_DATA}.
     * </p>
     *
     * @see android.R.styleable#AccessibilityService_canRequestEnhancedWebAccessibility
     */
    public static final int FLAG_REQUEST_ENHANCED_WEB_ACCESSIBILITY = 0x8;

    /**
     * This flag requests that the {@link AccessibilityNodeInfo}s obtained
     * by an {@link AccessibilityService} contain the id of the source view.
     * The source view id will be a fully qualified resource name of the
     * form "package:id/name", for example "foo.bar:id/my_list", and it is
     * useful for UI test automation. This flag is not set by default.
     */
    public static final int FLAG_REPORT_VIEW_IDS = 0x10;

    /**
     * This flag requests from the system to filter key events. If this flag
     * is set the accessibility service will receive the key events before
     * applications allowing it implement global shortcuts.
     * <p>
     * Services that want to set this flag have to declare this capability
     * in their meta-data by setting the attribute {@link android.R.attr
     * #canRequestFilterKeyEvents canRequestFilterKeyEvents} to true,
     * otherwise this flag will be ignored. For how to declare the meta-data
     * of a service refer to {@value AccessibilityService#SERVICE_META_DATA}.
     * </p>
     *
     * @see android.R.styleable#AccessibilityService_canRequestFilterKeyEvents
     */
    public static final int FLAG_REQUEST_FILTER_KEY_EVENTS = 0x20;

    /**
     * This flag indicates to the system that the accessibility service wants
     * to access content of all interactive windows. An interactive window is a
     * window that has input focus or can be touched by a sighted user when explore
     * by touch is not enabled. If this flag is not set your service will not receive
     * {@link android.view.accessibility.AccessibilityEvent#TYPE_WINDOWS_CHANGED}
     * events, calling AccessibilityService{@link AccessibilityService#getWindows()
     * AccessibilityService.getWindows()} will return an empty list, and {@link AccessibilityNodeInfo#getWindow() AccessibilityNodeInfo.getWindow()} will
     * return null.
     * <p>
     * Services that want to set this flag have to declare the capability
     * to retrieve window content in their meta-data by setting the attribute
     * {@link android.R.attr#canRetrieveWindowContent canRetrieveWindowContent} to
     * true, otherwise this flag will be ignored. For how to declare the meta-data
     * of a service refer to {@value AccessibilityService#SERVICE_META_DATA}.
     * </p>
     *
     * @see android.R.styleable#AccessibilityService_canRetrieveWindowContent
     */
    public static final int FLAG_RETRIEVE_INTERACTIVE_WINDOWS = 0x40;

    /**
     * {@hide }
     */
    public static final int FLAG_FORCE_DIRECT_BOOT_AWARE = 0x10000;

    /**
     * The event types an {@link AccessibilityService} is interested in.
     * <p>
     *   <strong>Can be dynamically set at runtime.</strong>
     * </p>
     *
     * @see android.view.accessibility.AccessibilityEvent#TYPE_VIEW_CLICKED
     * @see android.view.accessibility.AccessibilityEvent#TYPE_VIEW_LONG_CLICKED
     * @see android.view.accessibility.AccessibilityEvent#TYPE_VIEW_FOCUSED
     * @see android.view.accessibility.AccessibilityEvent#TYPE_VIEW_SELECTED
     * @see android.view.accessibility.AccessibilityEvent#TYPE_VIEW_TEXT_CHANGED
     * @see android.view.accessibility.AccessibilityEvent#TYPE_WINDOW_STATE_CHANGED
     * @see android.view.accessibility.AccessibilityEvent#TYPE_NOTIFICATION_STATE_CHANGED
     * @see android.view.accessibility.AccessibilityEvent#TYPE_TOUCH_EXPLORATION_GESTURE_START
     * @see android.view.accessibility.AccessibilityEvent#TYPE_TOUCH_EXPLORATION_GESTURE_END
     * @see android.view.accessibility.AccessibilityEvent#TYPE_VIEW_HOVER_ENTER
     * @see android.view.accessibility.AccessibilityEvent#TYPE_VIEW_HOVER_EXIT
     * @see android.view.accessibility.AccessibilityEvent#TYPE_VIEW_SCROLLED
     * @see android.view.accessibility.AccessibilityEvent#TYPE_VIEW_TEXT_SELECTION_CHANGED
     * @see android.view.accessibility.AccessibilityEvent#TYPE_WINDOW_CONTENT_CHANGED
     * @see android.view.accessibility.AccessibilityEvent#TYPE_TOUCH_INTERACTION_START
     * @see android.view.accessibility.AccessibilityEvent#TYPE_TOUCH_INTERACTION_END
     * @see android.view.accessibility.AccessibilityEvent#TYPE_ANNOUNCEMENT
     * @see android.view.accessibility.AccessibilityEvent#TYPE_GESTURE_DETECTION_START
     * @see android.view.accessibility.AccessibilityEvent#TYPE_GESTURE_DETECTION_END
     * @see android.view.accessibility.AccessibilityEvent#TYPE_VIEW_ACCESSIBILITY_FOCUSED
     * @see android.view.accessibility.AccessibilityEvent#TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED
     * @see android.view.accessibility.AccessibilityEvent#TYPE_VIEW_TEXT_TRAVERSED_AT_MOVEMENT_GRANULARITY
     * @see android.view.accessibility.AccessibilityEvent#TYPE_WINDOWS_CHANGED
     */
    public int eventTypes;

    /**
     * The package names an {@link AccessibilityService} is interested in. Setting
     * to <code>null</code> is equivalent to all packages.
     * <p>
     *   <strong>Can be dynamically set at runtime.</strong>
     * </p>
     */
    public java.lang.String[] packageNames;

    /**
     * The feedback type an {@link AccessibilityService} provides.
     * <p>
     *   <strong>Can be dynamically set at runtime.</strong>
     * </p>
     *
     * @see #FEEDBACK_AUDIBLE
     * @see #FEEDBACK_GENERIC
     * @see #FEEDBACK_HAPTIC
     * @see #FEEDBACK_SPOKEN
     * @see #FEEDBACK_VISUAL
     * @see #FEEDBACK_BRAILLE
     */
    public int feedbackType;

    /**
     * The timeout after the most recent event of a given type before an
     * {@link AccessibilityService} is notified.
     * <p>
     *   <strong>Can be dynamically set at runtime.</strong>.
     * </p>
     * <p>
     * <strong>Note:</strong> The event notification timeout is useful to avoid propagating
     *       events to the client too frequently since this is accomplished via an expensive
     *       interprocess call. One can think of the timeout as a criteria to determine when
     *       event generation has settled down.
     */
    public long notificationTimeout;

    /**
     * This field represents a set of flags used for configuring an
     * {@link AccessibilityService}.
     * <p>
     *   <strong>Can be dynamically set at runtime.</strong>
     * </p>
     *
     * @see #DEFAULT
     * @see #FLAG_INCLUDE_NOT_IMPORTANT_VIEWS
     * @see #FLAG_REQUEST_TOUCH_EXPLORATION_MODE
     * @see #FLAG_REQUEST_ENHANCED_WEB_ACCESSIBILITY
     * @see #FLAG_REQUEST_FILTER_KEY_EVENTS
     * @see #FLAG_REPORT_VIEW_IDS
     * @see #FLAG_RETRIEVE_INTERACTIVE_WINDOWS
     */
    public int flags;

    /**
     * The unique string Id to identify the accessibility service.
     */
    private java.lang.String mId;

    /**
     * The Service that implements this accessibility service component.
     */
    private android.content.pm.ResolveInfo mResolveInfo;

    /**
     * The accessibility service setting activity's name, used by the system
     * settings to launch the setting activity of this accessibility service.
     */
    private java.lang.String mSettingsActivityName;

    /**
     * Bit mask with capabilities of this service.
     */
    private int mCapabilities;

    /**
     * Resource id of the description of the accessibility service.
     */
    private int mDescriptionResId;

    /**
     * Non localized description of the accessibility service.
     */
    private java.lang.String mNonLocalizedDescription;

    /**
     * Creates a new instance.
     */
    public AccessibilityServiceInfo() {
        /* do nothing */
    }

    /**
     * Creates a new instance.
     *
     * @param resolveInfo
     * 		The service resolve info.
     * @param context
     * 		Context for accessing resources.
     * @throws XmlPullParserException
     * 		If a XML parsing error occurs.
     * @throws IOException
     * 		If a XML parsing error occurs.
     * @unknown 
     */
    public AccessibilityServiceInfo(android.content.pm.ResolveInfo resolveInfo, android.content.Context context) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        android.content.pm.ServiceInfo serviceInfo = resolveInfo.serviceInfo;
        mId = new android.content.ComponentName(serviceInfo.packageName, serviceInfo.name).flattenToShortString();
        mResolveInfo = resolveInfo;
        android.content.res.XmlResourceParser parser = null;
        try {
            android.content.pm.PackageManager packageManager = context.getPackageManager();
            parser = serviceInfo.loadXmlMetaData(packageManager, android.accessibilityservice.AccessibilityService.SERVICE_META_DATA);
            if (parser == null) {
                return;
            }
            int type = 0;
            while ((type != org.xmlpull.v1.XmlPullParser.END_DOCUMENT) && (type != org.xmlpull.v1.XmlPullParser.START_TAG)) {
                type = parser.next();
            } 
            java.lang.String nodeName = parser.getName();
            if (!android.accessibilityservice.AccessibilityServiceInfo.TAG_ACCESSIBILITY_SERVICE.equals(nodeName)) {
                throw new org.xmlpull.v1.XmlPullParserException(("Meta-data does not start with" + android.accessibilityservice.AccessibilityServiceInfo.TAG_ACCESSIBILITY_SERVICE) + " tag");
            }
            android.util.AttributeSet allAttributes = android.util.Xml.asAttributeSet(parser);
            android.content.res.Resources resources = packageManager.getResourcesForApplication(serviceInfo.applicationInfo);
            android.content.res.TypedArray asAttributes = resources.obtainAttributes(allAttributes, com.android.internal.R.styleable.AccessibilityService);
            eventTypes = asAttributes.getInt(com.android.internal.R.styleable.AccessibilityService_accessibilityEventTypes, 0);
            java.lang.String packageNamez = asAttributes.getString(com.android.internal.R.styleable.AccessibilityService_packageNames);
            if (packageNamez != null) {
                packageNames = packageNamez.split("(\\s)*,(\\s)*");
            }
            feedbackType = asAttributes.getInt(com.android.internal.R.styleable.AccessibilityService_accessibilityFeedbackType, 0);
            notificationTimeout = asAttributes.getInt(com.android.internal.R.styleable.AccessibilityService_notificationTimeout, 0);
            flags = asAttributes.getInt(com.android.internal.R.styleable.AccessibilityService_accessibilityFlags, 0);
            mSettingsActivityName = asAttributes.getString(com.android.internal.R.styleable.AccessibilityService_settingsActivity);
            if (asAttributes.getBoolean(com.android.internal.R.styleable.AccessibilityService_canRetrieveWindowContent, false)) {
                mCapabilities |= android.accessibilityservice.AccessibilityServiceInfo.CAPABILITY_CAN_RETRIEVE_WINDOW_CONTENT;
            }
            if (asAttributes.getBoolean(com.android.internal.R.styleable.AccessibilityService_canRequestTouchExplorationMode, false)) {
                mCapabilities |= android.accessibilityservice.AccessibilityServiceInfo.CAPABILITY_CAN_REQUEST_TOUCH_EXPLORATION;
            }
            if (asAttributes.getBoolean(com.android.internal.R.styleable.AccessibilityService_canRequestEnhancedWebAccessibility, false)) {
                mCapabilities |= android.accessibilityservice.AccessibilityServiceInfo.CAPABILITY_CAN_REQUEST_ENHANCED_WEB_ACCESSIBILITY;
            }
            if (asAttributes.getBoolean(com.android.internal.R.styleable.AccessibilityService_canRequestFilterKeyEvents, false)) {
                mCapabilities |= android.accessibilityservice.AccessibilityServiceInfo.CAPABILITY_CAN_REQUEST_FILTER_KEY_EVENTS;
            }
            if (asAttributes.getBoolean(com.android.internal.R.styleable.AccessibilityService_canControlMagnification, false)) {
                mCapabilities |= android.accessibilityservice.AccessibilityServiceInfo.CAPABILITY_CAN_CONTROL_MAGNIFICATION;
            }
            if (asAttributes.getBoolean(com.android.internal.R.styleable.AccessibilityService_canPerformGestures, false)) {
                mCapabilities |= android.accessibilityservice.AccessibilityServiceInfo.CAPABILITY_CAN_PERFORM_GESTURES;
            }
            android.util.TypedValue peekedValue = asAttributes.peekValue(com.android.internal.R.styleable.AccessibilityService_description);
            if (peekedValue != null) {
                mDescriptionResId = peekedValue.resourceId;
                java.lang.CharSequence nonLocalizedDescription = peekedValue.coerceToString();
                if (nonLocalizedDescription != null) {
                    mNonLocalizedDescription = nonLocalizedDescription.toString().trim();
                }
            }
            asAttributes.recycle();
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            throw new org.xmlpull.v1.XmlPullParserException("Unable to create context for: " + serviceInfo.packageName);
        } finally {
            if (parser != null) {
                parser.close();
            }
        }
    }

    /**
     * Updates the properties that an AccessibilitySerivice can change dynamically.
     *
     * @param other
     * 		The info from which to update the properties.
     * @unknown 
     */
    public void updateDynamicallyConfigurableProperties(android.accessibilityservice.AccessibilityServiceInfo other) {
        eventTypes = other.eventTypes;
        packageNames = other.packageNames;
        feedbackType = other.feedbackType;
        notificationTimeout = other.notificationTimeout;
        flags = other.flags;
    }

    /**
     *
     *
     * @unknown 
     */
    public void setComponentName(android.content.ComponentName component) {
        mId = component.flattenToShortString();
    }

    /**
     * The accessibility service id.
     * <p>
     *   <strong>Generated by the system.</strong>
     * </p>
     *
     * @return The id.
     */
    public java.lang.String getId() {
        return mId;
    }

    /**
     * The service {@link ResolveInfo}.
     * <p>
     *   <strong>Generated by the system.</strong>
     * </p>
     *
     * @return The info.
     */
    public android.content.pm.ResolveInfo getResolveInfo() {
        return mResolveInfo;
    }

    /**
     * The settings activity name.
     * <p>
     *    <strong>Statically set from
     *    {@link AccessibilityService#SERVICE_META_DATA meta-data}.</strong>
     * </p>
     *
     * @return The settings activity name.
     */
    public java.lang.String getSettingsActivityName() {
        return mSettingsActivityName;
    }

    /**
     * Whether this service can retrieve the current window's content.
     * <p>
     *    <strong>Statically set from
     *    {@link AccessibilityService#SERVICE_META_DATA meta-data}.</strong>
     * </p>
     *
     * @return True if window content can be retrieved.
     * @deprecated Use {@link #getCapabilities()}.
     */
    public boolean getCanRetrieveWindowContent() {
        return (mCapabilities & android.accessibilityservice.AccessibilityServiceInfo.CAPABILITY_CAN_RETRIEVE_WINDOW_CONTENT) != 0;
    }

    /**
     * Returns the bit mask of capabilities this accessibility service has such as
     * being able to retrieve the active window content, etc.
     *
     * @return The capability bit mask.
     * @see #CAPABILITY_CAN_RETRIEVE_WINDOW_CONTENT
     * @see #CAPABILITY_CAN_REQUEST_TOUCH_EXPLORATION
     * @see #CAPABILITY_CAN_REQUEST_ENHANCED_WEB_ACCESSIBILITY
     * @see #CAPABILITY_FILTER_KEY_EVENTS
     * @see #CAPABILITY_CAN_CONTROL_MAGNIFICATION
     * @see #CAPABILITY_CAN_PERFORM_GESTURES
     */
    public int getCapabilities() {
        return mCapabilities;
    }

    /**
     * Sets the bit mask of capabilities this accessibility service has such as
     * being able to retrieve the active window content, etc.
     *
     * @param capabilities
     * 		The capability bit mask.
     * @see #CAPABILITY_CAN_RETRIEVE_WINDOW_CONTENT
     * @see #CAPABILITY_CAN_REQUEST_TOUCH_EXPLORATION
     * @see #CAPABILITY_CAN_REQUEST_ENHANCED_WEB_ACCESSIBILITY
     * @see #CAPABILITY_FILTER_KEY_EVENTS
     * @see #CAPABILITY_CAN_CONTROL_MAGNIFICATION
     * @see #CAPABILITY_CAN_PERFORM_GESTURES
     * @unknown 
     */
    public void setCapabilities(int capabilities) {
        mCapabilities = capabilities;
    }

    /**
     * Gets the non-localized description of the accessibility service.
     * <p>
     *    <strong>Statically set from
     *    {@link AccessibilityService#SERVICE_META_DATA meta-data}.</strong>
     * </p>
     *
     * @return The description.
     * @deprecated Use {@link #loadDescription(PackageManager)}.
     */
    public java.lang.String getDescription() {
        return mNonLocalizedDescription;
    }

    /**
     * The localized description of the accessibility service.
     * <p>
     *    <strong>Statically set from
     *    {@link AccessibilityService#SERVICE_META_DATA meta-data}.</strong>
     * </p>
     *
     * @return The localized description.
     */
    public java.lang.String loadDescription(android.content.pm.PackageManager packageManager) {
        if (mDescriptionResId == 0) {
            return mNonLocalizedDescription;
        }
        android.content.pm.ServiceInfo serviceInfo = mResolveInfo.serviceInfo;
        java.lang.CharSequence description = packageManager.getText(serviceInfo.packageName, mDescriptionResId, serviceInfo.applicationInfo);
        if (description != null) {
            return description.toString().trim();
        }
        return null;
    }

    /**
     * {@hide }
     */
    public boolean isDirectBootAware() {
        return ((flags & android.accessibilityservice.AccessibilityServiceInfo.FLAG_FORCE_DIRECT_BOOT_AWARE) != 0) || mResolveInfo.serviceInfo.directBootAware;
    }

    /**
     * {@inheritDoc }
     */
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(android.os.Parcel parcel, int flagz) {
        parcel.writeInt(eventTypes);
        parcel.writeStringArray(packageNames);
        parcel.writeInt(feedbackType);
        parcel.writeLong(notificationTimeout);
        parcel.writeInt(flags);
        parcel.writeString(mId);
        parcel.writeParcelable(mResolveInfo, 0);
        parcel.writeString(mSettingsActivityName);
        parcel.writeInt(mCapabilities);
        parcel.writeInt(mDescriptionResId);
        parcel.writeString(mNonLocalizedDescription);
    }

    private void initFromParcel(android.os.Parcel parcel) {
        eventTypes = parcel.readInt();
        packageNames = parcel.readStringArray();
        feedbackType = parcel.readInt();
        notificationTimeout = parcel.readLong();
        flags = parcel.readInt();
        mId = parcel.readString();
        mResolveInfo = parcel.readParcelable(null);
        mSettingsActivityName = parcel.readString();
        mCapabilities = parcel.readInt();
        mDescriptionResId = parcel.readInt();
        mNonLocalizedDescription = parcel.readString();
    }

    @java.lang.Override
    public int hashCode() {
        return (31 * 1) + (mId == null ? 0 : mId.hashCode());
    }

    @java.lang.Override
    public boolean equals(java.lang.Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        android.accessibilityservice.AccessibilityServiceInfo other = ((android.accessibilityservice.AccessibilityServiceInfo) (obj));
        if (mId == null) {
            if (other.mId != null) {
                return false;
            }
        } else
            if (!mId.equals(other.mId)) {
                return false;
            }

        return true;
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder stringBuilder = new java.lang.StringBuilder();
        android.accessibilityservice.AccessibilityServiceInfo.appendEventTypes(stringBuilder, eventTypes);
        stringBuilder.append(", ");
        android.accessibilityservice.AccessibilityServiceInfo.appendPackageNames(stringBuilder, packageNames);
        stringBuilder.append(", ");
        android.accessibilityservice.AccessibilityServiceInfo.appendFeedbackTypes(stringBuilder, feedbackType);
        stringBuilder.append(", ");
        stringBuilder.append("notificationTimeout: ").append(notificationTimeout);
        stringBuilder.append(", ");
        android.accessibilityservice.AccessibilityServiceInfo.appendFlags(stringBuilder, flags);
        stringBuilder.append(", ");
        stringBuilder.append("id: ").append(mId);
        stringBuilder.append(", ");
        stringBuilder.append("resolveInfo: ").append(mResolveInfo);
        stringBuilder.append(", ");
        stringBuilder.append("settingsActivityName: ").append(mSettingsActivityName);
        stringBuilder.append(", ");
        android.accessibilityservice.AccessibilityServiceInfo.appendCapabilities(stringBuilder, mCapabilities);
        return stringBuilder.toString();
    }

    private static void appendFeedbackTypes(java.lang.StringBuilder stringBuilder, int feedbackTypes) {
        stringBuilder.append("feedbackTypes:");
        stringBuilder.append("[");
        while (feedbackTypes != 0) {
            final int feedbackTypeBit = 1 << java.lang.Integer.numberOfTrailingZeros(feedbackTypes);
            stringBuilder.append(android.accessibilityservice.AccessibilityServiceInfo.feedbackTypeToString(feedbackTypeBit));
            feedbackTypes &= ~feedbackTypeBit;
            if (feedbackTypes != 0) {
                stringBuilder.append(", ");
            }
        } 
        stringBuilder.append("]");
    }

    private static void appendPackageNames(java.lang.StringBuilder stringBuilder, java.lang.String[] packageNames) {
        stringBuilder.append("packageNames:");
        stringBuilder.append("[");
        if (packageNames != null) {
            final int packageNameCount = packageNames.length;
            for (int i = 0; i < packageNameCount; i++) {
                stringBuilder.append(packageNames[i]);
                if (i < (packageNameCount - 1)) {
                    stringBuilder.append(", ");
                }
            }
        }
        stringBuilder.append("]");
    }

    private static void appendEventTypes(java.lang.StringBuilder stringBuilder, int eventTypes) {
        stringBuilder.append("eventTypes:");
        stringBuilder.append("[");
        while (eventTypes != 0) {
            final int eventTypeBit = 1 << java.lang.Integer.numberOfTrailingZeros(eventTypes);
            stringBuilder.append(android.view.accessibility.AccessibilityEvent.eventTypeToString(eventTypeBit));
            eventTypes &= ~eventTypeBit;
            if (eventTypes != 0) {
                stringBuilder.append(", ");
            }
        } 
        stringBuilder.append("]");
    }

    private static void appendFlags(java.lang.StringBuilder stringBuilder, int flags) {
        stringBuilder.append("flags:");
        stringBuilder.append("[");
        while (flags != 0) {
            final int flagBit = 1 << java.lang.Integer.numberOfTrailingZeros(flags);
            stringBuilder.append(android.accessibilityservice.AccessibilityServiceInfo.flagToString(flagBit));
            flags &= ~flagBit;
            if (flags != 0) {
                stringBuilder.append(", ");
            }
        } 
        stringBuilder.append("]");
    }

    private static void appendCapabilities(java.lang.StringBuilder stringBuilder, int capabilities) {
        stringBuilder.append("capabilities:");
        stringBuilder.append("[");
        while (capabilities != 0) {
            final int capabilityBit = 1 << java.lang.Integer.numberOfTrailingZeros(capabilities);
            stringBuilder.append(android.accessibilityservice.AccessibilityServiceInfo.capabilityToString(capabilityBit));
            capabilities &= ~capabilityBit;
            if (capabilities != 0) {
                stringBuilder.append(", ");
            }
        } 
        stringBuilder.append("]");
    }

    /**
     * Returns the string representation of a feedback type. For example,
     * {@link #FEEDBACK_SPOKEN} is represented by the string FEEDBACK_SPOKEN.
     *
     * @param feedbackType
     * 		The feedback type.
     * @return The string representation.
     */
    public static java.lang.String feedbackTypeToString(int feedbackType) {
        java.lang.StringBuilder builder = new java.lang.StringBuilder();
        builder.append("[");
        while (feedbackType != 0) {
            final int feedbackTypeFlag = 1 << java.lang.Integer.numberOfTrailingZeros(feedbackType);
            feedbackType &= ~feedbackTypeFlag;
            switch (feedbackTypeFlag) {
                case android.accessibilityservice.AccessibilityServiceInfo.FEEDBACK_AUDIBLE :
                    if (builder.length() > 1) {
                        builder.append(", ");
                    }
                    builder.append("FEEDBACK_AUDIBLE");
                    break;
                case android.accessibilityservice.AccessibilityServiceInfo.FEEDBACK_HAPTIC :
                    if (builder.length() > 1) {
                        builder.append(", ");
                    }
                    builder.append("FEEDBACK_HAPTIC");
                    break;
                case android.accessibilityservice.AccessibilityServiceInfo.FEEDBACK_GENERIC :
                    if (builder.length() > 1) {
                        builder.append(", ");
                    }
                    builder.append("FEEDBACK_GENERIC");
                    break;
                case android.accessibilityservice.AccessibilityServiceInfo.FEEDBACK_SPOKEN :
                    if (builder.length() > 1) {
                        builder.append(", ");
                    }
                    builder.append("FEEDBACK_SPOKEN");
                    break;
                case android.accessibilityservice.AccessibilityServiceInfo.FEEDBACK_VISUAL :
                    if (builder.length() > 1) {
                        builder.append(", ");
                    }
                    builder.append("FEEDBACK_VISUAL");
                    break;
                case android.accessibilityservice.AccessibilityServiceInfo.FEEDBACK_BRAILLE :
                    if (builder.length() > 1) {
                        builder.append(", ");
                    }
                    builder.append("FEEDBACK_BRAILLE");
                    break;
            }
        } 
        builder.append("]");
        return builder.toString();
    }

    /**
     * Returns the string representation of a flag. For example,
     * {@link #DEFAULT} is represented by the string DEFAULT.
     *
     * @param flag
     * 		The flag.
     * @return The string representation.
     */
    public static java.lang.String flagToString(int flag) {
        switch (flag) {
            case android.accessibilityservice.AccessibilityServiceInfo.DEFAULT :
                return "DEFAULT";
            case android.accessibilityservice.AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS :
                return "FLAG_INCLUDE_NOT_IMPORTANT_VIEWS";
            case android.accessibilityservice.AccessibilityServiceInfo.FLAG_REQUEST_TOUCH_EXPLORATION_MODE :
                return "FLAG_REQUEST_TOUCH_EXPLORATION_MODE";
            case android.accessibilityservice.AccessibilityServiceInfo.FLAG_REQUEST_ENHANCED_WEB_ACCESSIBILITY :
                return "FLAG_REQUEST_ENHANCED_WEB_ACCESSIBILITY";
            case android.accessibilityservice.AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS :
                return "FLAG_REPORT_VIEW_IDS";
            case android.accessibilityservice.AccessibilityServiceInfo.FLAG_REQUEST_FILTER_KEY_EVENTS :
                return "FLAG_REQUEST_FILTER_KEY_EVENTS";
            case android.accessibilityservice.AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS :
                return "FLAG_RETRIEVE_INTERACTIVE_WINDOWS";
            default :
                return null;
        }
    }

    /**
     * Returns the string representation of a capability. For example,
     * {@link #CAPABILITY_CAN_RETRIEVE_WINDOW_CONTENT} is represented
     * by the string CAPABILITY_CAN_RETRIEVE_WINDOW_CONTENT.
     *
     * @param capability
     * 		The capability.
     * @return The string representation.
     */
    public static java.lang.String capabilityToString(int capability) {
        switch (capability) {
            case android.accessibilityservice.AccessibilityServiceInfo.CAPABILITY_CAN_RETRIEVE_WINDOW_CONTENT :
                return "CAPABILITY_CAN_RETRIEVE_WINDOW_CONTENT";
            case android.accessibilityservice.AccessibilityServiceInfo.CAPABILITY_CAN_REQUEST_TOUCH_EXPLORATION :
                return "CAPABILITY_CAN_REQUEST_TOUCH_EXPLORATION";
            case android.accessibilityservice.AccessibilityServiceInfo.CAPABILITY_CAN_REQUEST_ENHANCED_WEB_ACCESSIBILITY :
                return "CAPABILITY_CAN_REQUEST_ENHANCED_WEB_ACCESSIBILITY";
            case android.accessibilityservice.AccessibilityServiceInfo.CAPABILITY_CAN_REQUEST_FILTER_KEY_EVENTS :
                return "CAPABILITY_CAN_FILTER_KEY_EVENTS";
            case android.accessibilityservice.AccessibilityServiceInfo.CAPABILITY_CAN_CONTROL_MAGNIFICATION :
                return "CAPABILITY_CAN_CONTROL_MAGNIFICATION";
            case android.accessibilityservice.AccessibilityServiceInfo.CAPABILITY_CAN_PERFORM_GESTURES :
                return "CAPABILITY_CAN_PERFORM_GESTURES";
            default :
                return "UNKNOWN";
        }
    }

    /**
     *
     *
     * @unknown 
     * @return The list of {@link CapabilityInfo} objects.
     */
    public java.util.List<android.accessibilityservice.AccessibilityServiceInfo.CapabilityInfo> getCapabilityInfos() {
        if (mCapabilities == 0) {
            return java.util.Collections.emptyList();
        }
        int capabilities = mCapabilities;
        java.util.List<android.accessibilityservice.AccessibilityServiceInfo.CapabilityInfo> capabilityInfos = new java.util.ArrayList<android.accessibilityservice.AccessibilityServiceInfo.CapabilityInfo>();
        while (capabilities != 0) {
            final int capabilityBit = 1 << java.lang.Integer.numberOfTrailingZeros(capabilities);
            capabilities &= ~capabilityBit;
            android.accessibilityservice.AccessibilityServiceInfo.CapabilityInfo capabilityInfo = android.accessibilityservice.AccessibilityServiceInfo.sAvailableCapabilityInfos.get(capabilityBit);
            if (capabilityInfo != null) {
                capabilityInfos.add(capabilityInfo);
            }
        } 
        return capabilityInfos;
    }

    /**
     *
     *
     * @unknown 
     */
    public static final class CapabilityInfo {
        public final int capability;

        public final int titleResId;

        public final int descResId;

        public CapabilityInfo(int capability, int titleResId, int descResId) {
            this.capability = capability;
            this.titleResId = titleResId;
            this.descResId = descResId;
        }
    }

    /**
     *
     *
     * @see Parcelable.Creator
     */
    public static final android.os.Parcelable.Creator<android.accessibilityservice.AccessibilityServiceInfo> CREATOR = new android.os.Parcelable.Creator<android.accessibilityservice.AccessibilityServiceInfo>() {
        public android.accessibilityservice.AccessibilityServiceInfo createFromParcel(android.os.Parcel parcel) {
            android.accessibilityservice.AccessibilityServiceInfo info = new android.accessibilityservice.AccessibilityServiceInfo();
            info.initFromParcel(parcel);
            return info;
        }

        public android.accessibilityservice.AccessibilityServiceInfo[] newArray(int size) {
            return new android.accessibilityservice.AccessibilityServiceInfo[size];
        }
    };
}

