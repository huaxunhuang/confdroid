package lu.uni.tsopen;

import org.apache.commons.lang3.tuple.Triple;
import org.dom4j.Attribute;
import org.dom4j.io.SAXReader;
import org.javatuples.Triplet;
import org.w3c.dom.*;
import soot.G;
import soot.PackManager;
import soot.Scene;
import soot.SootClass;
import soot.options.Options;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.*;

public class ReadXmlFilesForCompatAttributes {
    static int num = 0;
    public static Set<String> colorSet = new HashSet<>();

    public static Map<String, String> colorMap = new HashMap<>();

    static String apkPath = "";
    public static List<String> appCompatList = new ArrayList<>();

    public static Set<String> arraySet = new HashSet<>();

    public static Map<String, org.w3c.dom.Element> arrayMap = new HashMap<>();

    public static Set<String> stringSet = new HashSet<>();

    public static Map<String, org.w3c.dom.Element> stringMap = new HashMap<>();

    public static Set<String> dimenSet = new HashSet<>();

    public static Map<String, org.w3c.dom.Element> dimenMap = new HashMap<>();

    public static Set<String> integerSet = new HashSet<>();

    public static Map<String, org.w3c.dom.Element> integerMap = new HashMap<>();

    public static Set<String> styleSet = new HashSet<>();

    public static Map<String, org.w3c.dom.Element> styleMap = new HashMap<>();

    public static Set<String> drawableSet = new HashSet<>();

    public static Map<String, org.w3c.dom.Element> drawableMap = new HashMap<>();

    public static Set<String> interestingProject = new HashSet<>();

    public static Map<String, Set<String>> indexMap = new HashMap<>();

    public static String resPath = "";

    public static List<String[]> outputList = (List)new ArrayList<>();

    public static String mode = "lint";

    public static void initSoot(String projectPath) {
        G.reset();
        Scene.v().addBasicClass("java.lang.Object", 2);
        Scene.v().addBasicClass("java.io.PrintStream", 2);
        Scene.v().addBasicClass("java.lang.System", 2);
        Options.v().set_src_prec(5);
        Options.v().set_force_android_jar("/home1/name1/google-top500/android.jar");
        Options.v().set_process_dir(Collections.singletonList(projectPath));
        Options.v().set_allow_phantom_refs(true);
        Options.v().set_prepend_classpath(true);
        Options.v().set_output_format(12);
        Options.v().setPhaseOption("jtp", "enabled:false");
        Options.v().set_whole_program(true);
        Options.v().set_keep_line_number(true);
        Scene.v().loadNecessaryClasses();
        PackManager.v().runPacks();
    }

    static boolean flag = false;

    static Map<String, Integer> lintRuleMap = new HashMap<>();

    static Map<String, Integer> orpLocatorRuleMap = new HashMap<>();

    static List<Triplet<String, String, Integer>> cDepRuleMap = new ArrayList<>();

    static List<Triple<String, String, Integer>> confDroidRuleMap = new ArrayList<>();

    public static void buildLintRules(){
        lintRuleMap.put("resizeClip", 22);
        lintRuleMap.put("collapseContentDescription", 22);
        lintRuleMap.put("accessibilityTraversalBefore", 22);
        lintRuleMap.put("accessibilityTraversalAfter", 22);
        lintRuleMap.put("dialogPreferredPadding", 22);
        lintRuleMap.put("searchHintIcon", 22);
        lintRuleMap.put("revisionCode", 22);
        lintRuleMap.put("drawableTint", 22);
        lintRuleMap.put("drawableTintMode", 22);
        lintRuleMap.put("fraction", 22);

        lintRuleMap.put("trackTint", 23);
        lintRuleMap.put("trackTintMode", 23);
        lintRuleMap.put("start", 23);
        lintRuleMap.put("end", 23);
        lintRuleMap.put("breakStrategy", 23);
        lintRuleMap.put("hyphenationFrequency", 23);
        lintRuleMap.put("allowUndo", 23);
        lintRuleMap.put("windowLightStatusBar", 23);
        lintRuleMap.put("numbersInnerTextColor", 23);
        lintRuleMap.put("colorBackgroundFloating", 23);
        lintRuleMap.put("titleTextColor", 23);
        lintRuleMap.put("subtitleTextColor", 23);
        lintRuleMap.put("thumbPosition", 23);
        lintRuleMap.put("scrollIndicators", 23);
        lintRuleMap.put("contextClickable", 23);
        lintRuleMap.put("fingerprintAuthDrawable", 23);
        lintRuleMap.put("logoDescription", 23);
        lintRuleMap.put("extractNativeLibs", 23);
        lintRuleMap.put("fullBackupContent", 23);
        lintRuleMap.put("usesCleartextTraffic", 23);
        lintRuleMap.put("lockTaskMode", 23);
        lintRuleMap.put("autoVerify", 23);
        lintRuleMap.put("showForAllUsers", 23);
        lintRuleMap.put("supportsAssist", 23);
        lintRuleMap.put("supportsLaunchVoiceAssistFromKeyguard", 23);

        lintRuleMap.put("listMenuViewStyle", 24);
        lintRuleMap.put("subMenuArrow", 24);
        lintRuleMap.put("defaultWidth", 24);
        lintRuleMap.put("defaultHeight", 24);
        lintRuleMap.put("resizeableActivity", 24);
        lintRuleMap.put("supportsPictureInPicture", 24);
        lintRuleMap.put("titleMargin", 24);
        lintRuleMap.put("titleMarginStart", 24);
        lintRuleMap.put("titleMarginEnd", 24);
        lintRuleMap.put("titleMarginTop", 24);
        lintRuleMap.put("titleMarginBottom", 24);
        lintRuleMap.put("maxButtonHeight", 24);
        lintRuleMap.put("buttonGravity", 24);
        lintRuleMap.put("collapseIcon", 24);
        lintRuleMap.put("level", 24);
        lintRuleMap.put("contextPopupMenuStyle", 24);
        lintRuleMap.put("textAppearancePopupMenuHeader", 24);
        lintRuleMap.put("windowBackgroundFallback", 24);
        lintRuleMap.put("defaultToDeviceProtectedStorage", 24);
        lintRuleMap.put("directBootAware", 24);
        lintRuleMap.put("preferenceFragmentStyle", 24);
        lintRuleMap.put("canControlMagnification", 24);
        lintRuleMap.put("languageTag", 24);
        lintRuleMap.put("pointerIcon", 24);
        lintRuleMap.put("tickMark", 24);
        lintRuleMap.put("tickMarkTint", 24);
        lintRuleMap.put("tickMarkTintMode", 24);
        lintRuleMap.put("canPerformGestures", 24);
        lintRuleMap.put("externalService", 24);
        lintRuleMap.put("supportsLocalInteraction", 24);
        lintRuleMap.put("startX", 24);
        lintRuleMap.put("startY", 24);
        lintRuleMap.put("endX", 24);
        lintRuleMap.put("endY", 24);
        lintRuleMap.put("offset", 24);
        lintRuleMap.put("use32bitAbi", 24);
        lintRuleMap.put("bitmap", 24);
        lintRuleMap.put("hotSpotX", 24);
        lintRuleMap.put("hotSpotY", 24);
        lintRuleMap.put("version", 24);
        lintRuleMap.put("backupInForeground", 24);
        lintRuleMap.put("countDown", 24);
        lintRuleMap.put("canRecord", 24);
        lintRuleMap.put("tunerCount", 24);
        lintRuleMap.put("fillType", 24);
        lintRuleMap.put("popupEnterTransition", 24);
        lintRuleMap.put("popupExitTransition", 24);
        lintRuleMap.put("forceHasOverlappingRendering", 24);
        lintRuleMap.put("contentInsetStartWithNavigation", 24);
        lintRuleMap.put("contentInsetEndWithActions", 24);
        lintRuleMap.put("numberPickerStyle", 24);
        lintRuleMap.put("enableVrMode", 24);
        lintRuleMap.put("hash", 24);
        lintRuleMap.put("networkSecurityConfig", 24);

        lintRuleMap.put("shortcutId", 25);
        lintRuleMap.put("shortcutShortLabel", 25);
        lintRuleMap.put("shortcutLongLabel", 25);
        lintRuleMap.put("shortcutDisabledMessage", 25);
        lintRuleMap.put("roundIcon", 25);
        lintRuleMap.put("contextUri", 25);
        lintRuleMap.put("contextDescription", 25);
        lintRuleMap.put("showMetadataInPreview", 25);
        lintRuleMap.put("colorSecondary", 25);

        lintRuleMap.put("visibleToInstantApps", 26);
        lintRuleMap.put("font", 26);
        lintRuleMap.put("fontWeight", 26);
        lintRuleMap.put("tooltipText", 26);
        lintRuleMap.put("autoSizeTextType", 26);
        lintRuleMap.put("autoSizeStepGranularity", 26);
        lintRuleMap.put("autoSizePresetSizes", 26);
        lintRuleMap.put("autoSizeMinTextSize", 26);
        lintRuleMap.put("min", 26);
        lintRuleMap.put("rotationAnimation", 26);
        lintRuleMap.put("fontStyle", 26);
        lintRuleMap.put("keyboardNavigationCluster", 26);
        lintRuleMap.put("targetProcesses", 26);
        lintRuleMap.put("nextClusterForward", 26);
        lintRuleMap.put("colorError", 26);
        lintRuleMap.put("focusedByDefault", 26);
        lintRuleMap.put("appCategory", 26);
        lintRuleMap.put("autoSizeMaxTextSize", 26);
        lintRuleMap.put("recreateOnConfigChanges", 26);
        lintRuleMap.put("certDigest", 26);
        lintRuleMap.put("splitName", 26);
        lintRuleMap.put("colorMode", 26);
        lintRuleMap.put("isolatedSplits", 26);
        lintRuleMap.put("targetSandboxVersion", 26);
        lintRuleMap.put("canRequestFingerprintGestures", 26);
        lintRuleMap.put("alphabeticModifiers", 26);
        lintRuleMap.put("numericModifiers", 26);
        lintRuleMap.put("fontProviderAuthority", 26);
        lintRuleMap.put("fontProviderQuery", 26);
        lintRuleMap.put("primaryContentAlpha", 26);
        lintRuleMap.put("secondaryContentAlpha", 26);
        lintRuleMap.put("requiredFeature", 26);
        lintRuleMap.put("requiredNotFeature", 26);
        lintRuleMap.put("autofillHints", 26);
        lintRuleMap.put("fontProviderPackage", 26);
        lintRuleMap.put("importantForAutofill", 26);
        lintRuleMap.put("recycleEnabled", 26);
        lintRuleMap.put("isStatic", 26);
        lintRuleMap.put("isFeatureSplit", 26);
        lintRuleMap.put("singleLineTitle", 26);
        lintRuleMap.put("fontProviderCerts", 26);
        lintRuleMap.put("iconTint", 26);
        lintRuleMap.put("iconTintMode", 26);
        lintRuleMap.put("maxAspectRatio", 26);
        lintRuleMap.put("iconSpaceReserved", 26);
        lintRuleMap.put("defaultFocusHighlightEnabled", 26);
        lintRuleMap.put("persistentWhenFeatureAvailable", 26);
        lintRuleMap.put("windowSplashscreenContent", 26);
        lintRuleMap.put("justificationMode", 26);
        lintRuleMap.put("autofilledHighlight", 26);

        lintRuleMap.put("showWhenLocked", 27);
        lintRuleMap.put("turnScreenOn", 27);
        lintRuleMap.put("classLoader", 27);
        lintRuleMap.put("windowLightNavigationBar", 27);
        lintRuleMap.put("navigationBarDividerColor", 27);

        lintRuleMap.put("cantSaveState", 28);
        lintRuleMap.put("ttcIndex", 28);
        lintRuleMap.put("fontVariationSettings", 28);
        lintRuleMap.put("dialogCornerRadius", 28);
        lintRuleMap.put("screenReaderFocusable", 28);
        lintRuleMap.put("buttonCornerRadius", 28);
        lintRuleMap.put("versionCodeMajor", 28);
        lintRuleMap.put("versionMajor", 28);
        lintRuleMap.put("widgetFeatures", 28);
        lintRuleMap.put("appComponentFactory", 28);
        lintRuleMap.put("fallbackLineSpacing", 28);
        lintRuleMap.put("accessibilityPaneTitle", 28);
        lintRuleMap.put("firstBaselineToTopHeight", 28);
        lintRuleMap.put("lastBaselineToBottomHeight", 28);
        lintRuleMap.put("lineHeight", 28);
        lintRuleMap.put("accessibilityHeading", 28);
        lintRuleMap.put("outlineSpotShadowColor", 28);
        lintRuleMap.put("outlineAmbientShadowColor", 28);
        lintRuleMap.put("maxLongVersionCode", 28);
        lintRuleMap.put("textFontWeight", 28);
        lintRuleMap.put("windowLayoutInDisplayCutoutMode", 28);

        lintRuleMap.put("packageType", 29);
        lintRuleMap.put("opticalInsetLeft", 29);
        lintRuleMap.put("opticalInsetTop", 29);
        lintRuleMap.put("opticalInsetRight", 29);
        lintRuleMap.put("opticalInsetBottom", 29);
        lintRuleMap.put("forceDarkAllowed", 29);
        lintRuleMap.put("nonInteractiveUiTimeout", 29);
        lintRuleMap.put("isLightTheme", 29);
        lintRuleMap.put("isSplitRequired", 29);
        lintRuleMap.put("textLocale", 29);
        lintRuleMap.put("settingsSliceUri", 29);
        lintRuleMap.put("shell", 29);
        lintRuleMap.put("interactiveUiTimeout", 29);
        lintRuleMap.put("supportsMultipleDisplays", 29);
        lintRuleMap.put("useAppZygote", 29);
        lintRuleMap.put("selectionDividerHeight", 29);
        lintRuleMap.put("foregroundServiceType", 29);
        lintRuleMap.put("hasFragileUserData", 29);
        lintRuleMap.put("minAspectRatio", 29);
        lintRuleMap.put("inheritShowWhenLocked", 29);
        lintRuleMap.put("zygotePreloadName", 29);
        lintRuleMap.put("useEmbeddedDex", 29);
        lintRuleMap.put("forceUriPermissions", 29);
        lintRuleMap.put("allowAudioPlaybackCapture", 29);
        lintRuleMap.put("secureElementName", 29);
        lintRuleMap.put("requestLegacyExternalStorage", 29);
        lintRuleMap.put("enforceStatusBarContrast", 29);
        lintRuleMap.put("enforceNavigationBarContrast", 29);
        lintRuleMap.put("identifier", 29);

        lintRuleMap.put("importantForContentCapture", 30);
        lintRuleMap.put("forceQueryable", 30);
        lintRuleMap.put("resourcesMap", 30);
        lintRuleMap.put("animatedImageDrawable", 30);
        lintRuleMap.put("htmlDescription", 30);
        lintRuleMap.put("preferMinimalPostProcessing", 30);
        lintRuleMap.put("supportsInlineSuggestions", 30);
        lintRuleMap.put("crossProfile", 30);
        lintRuleMap.put("canTakeScreenshot", 30);
        lintRuleMap.put("allowNativeHeapPointerTagging", 30);
        lintRuleMap.put("autoRevokePermissions", 30);
        lintRuleMap.put("preserveLegacyExternalStorage", 30);
        lintRuleMap.put("mimeGroup", 30);
        lintRuleMap.put("gwpAsanMode", 30);
    }

    public static void buildORPLocatorRuleMap(){
        orpLocatorRuleMap.put("accessibilityEventTypes",24);
        orpLocatorRuleMap.put("accessibilityFeedbackType",24);
        orpLocatorRuleMap.put("accessibilityFlags",24);
        orpLocatorRuleMap.put("canControlMagnification",24);
        orpLocatorRuleMap.put("canPerformGestures",24);
        orpLocatorRuleMap.put("canRetrieveWindowContent",24);
        orpLocatorRuleMap.put("notificationTimeout",24);
        orpLocatorRuleMap.put("packageNames",24);
        orpLocatorRuleMap.put("settingsActivity",24);
        orpLocatorRuleMap.put("alwaysFocusable",23);
        orpLocatorRuleMap.put("directBootAware",23);
        orpLocatorRuleMap.put("enableVrMode",23);
        orpLocatorRuleMap.put("inheritShowWhenLocked",28);
        orpLocatorRuleMap.put("lockTaskMode",22);
        orpLocatorRuleMap.put("minAspectRatio",28);
        orpLocatorRuleMap.put("resizeableActivity",22);
        orpLocatorRuleMap.put("showForAllUsers",22);
        orpLocatorRuleMap.put("supportsPictureInPicture",23);
        orpLocatorRuleMap.put("systemUserOnly",23);
        orpLocatorRuleMap.put("allowAudioPlaybackCapture",28);
        orpLocatorRuleMap.put("allowClearUserDataOnFailedRestore",28);
        orpLocatorRuleMap.put("backupInForeground",23);
        orpLocatorRuleMap.put("banner",29);
        orpLocatorRuleMap.put("defaultToDeviceProtectedStorage",23);
        orpLocatorRuleMap.put("directBootAware",23);
        orpLocatorRuleMap.put("extractNativeLibs",22);
        orpLocatorRuleMap.put("fullBackupContent",22);
        orpLocatorRuleMap.put("hasFragileUserData",28);
        orpLocatorRuleMap.put("label",29);
        orpLocatorRuleMap.put("minAspectRatio",28);
        orpLocatorRuleMap.put("networkSecurityConfig",23);
        orpLocatorRuleMap.put("requestLegacyExternalStorage",28);
        orpLocatorRuleMap.put("resizeableActivity",23);
        orpLocatorRuleMap.put("useEmbeddedDex",28);
        orpLocatorRuleMap.put("usesCleartextTraffic",22);
        orpLocatorRuleMap.put("usesNonSdkApi",28);
        orpLocatorRuleMap.put("zygotePreloadName",28);
        orpLocatorRuleMap.put("autoVerify",22);
        orpLocatorRuleMap.put("roundIcon",25);
        orpLocatorRuleMap.put("defaultHeight",23);
        orpLocatorRuleMap.put("defaultHeight",23);
        orpLocatorRuleMap.put("defaultWidth",23);
        orpLocatorRuleMap.put("defaultWidth",23);
        orpLocatorRuleMap.put("backgroundRequest",28);
        orpLocatorRuleMap.put("backgroundRequestDetail",28);
        orpLocatorRuleMap.put("requestDetail",28);
        orpLocatorRuleMap.put("shell",28);
        orpLocatorRuleMap.put("directBootAware",23);
        orpLocatorRuleMap.put("forceUriPermissions",28);
        orpLocatorRuleMap.put("targetName",28);
        orpLocatorRuleMap.put("directBootAware",23);
        orpLocatorRuleMap.put("externalService",23);
        orpLocatorRuleMap.put("foregroundServiceType",28);
        orpLocatorRuleMap.put("useAppZygote",28);
        orpLocatorRuleMap.put("version",23);
        orpLocatorRuleMap.put("hasRoundedCorners",29);
        orpLocatorRuleMap.put("showWallpaper",28);
        orpLocatorRuleMap.put("monthTextAppearance",23);
        orpLocatorRuleMap.put("countDown",24);
        orpLocatorRuleMap.put("headerTextColor",23);
        orpLocatorRuleMap.put("thumbPosition",23);
        orpLocatorRuleMap.put("fontProviderAuthority",26);
        orpLocatorRuleMap.put("fontProviderCerts",26);
        orpLocatorRuleMap.put("fontProviderPackage",26);
        orpLocatorRuleMap.put("fontProviderQuery",26);
        orpLocatorRuleMap.put("font",26);
        orpLocatorRuleMap.put("fontStyle",26);
        orpLocatorRuleMap.put("fontVariationSettings",28);
        orpLocatorRuleMap.put("fontWeight",26);
        orpLocatorRuleMap.put("ttcIndex",28);
        orpLocatorRuleMap.put("endX",24);
        orpLocatorRuleMap.put("endY",24);
        orpLocatorRuleMap.put("startX",24);
        orpLocatorRuleMap.put("startY",24);
        orpLocatorRuleMap.put("offset",24);
        orpLocatorRuleMap.put("isVrOnly",28);
        orpLocatorRuleMap.put("languageTag",24);
        orpLocatorRuleMap.put("end",23);
        orpLocatorRuleMap.put("start",23);
        orpLocatorRuleMap.put("alphabeticModifiers",26);
        orpLocatorRuleMap.put("iconTint",26);
        orpLocatorRuleMap.put("iconTintMode",26);
        orpLocatorRuleMap.put("numericModifiers",26);
        orpLocatorRuleMap.put("tooltipText",26);
        orpLocatorRuleMap.put("popupEnterTransition",23);
        orpLocatorRuleMap.put("popupExitTransition",23);
        orpLocatorRuleMap.put("min",26);
        orpLocatorRuleMap.put("level",24);
        orpLocatorRuleMap.put("defaultQueryHint",23);
        orpLocatorRuleMap.put("searchHintIcon",22);
        orpLocatorRuleMap.put("tickMark",24);
        orpLocatorRuleMap.put("tickMarkTint",24);
        orpLocatorRuleMap.put("tickMarkTintMode",24);
        orpLocatorRuleMap.put("languageTag",24);
        orpLocatorRuleMap.put("subtypeId",24);
        orpLocatorRuleMap.put("dropDownSelector",24);
        orpLocatorRuleMap.put("thumbTint",23);
        orpLocatorRuleMap.put("thumbTintMode",23);
        orpLocatorRuleMap.put("trackTint",23);
        orpLocatorRuleMap.put("trackTintMode",23);
        orpLocatorRuleMap.put("headerTextColor",23);
        orpLocatorRuleMap.put("numbersInnerTextColor",23);
        orpLocatorRuleMap.put("collapseContentDescription",22);
        orpLocatorRuleMap.put("contentInsetEndWithActions",24);
        orpLocatorRuleMap.put("contentInsetStartWithNavigation",24);
        orpLocatorRuleMap.put("logoDescription",23);
        orpLocatorRuleMap.put("subtitleTextColor",23);
        orpLocatorRuleMap.put("titleMargin",24);
        orpLocatorRuleMap.put("titleTextColor",23);
        orpLocatorRuleMap.put("fillType",24);
        orpLocatorRuleMap.put("marginHorizontal",26);
        orpLocatorRuleMap.put("marginVertical",26);
        orpLocatorRuleMap.put("calendarSelectedTextColor",22);
        orpLocatorRuleMap.put("calendarTextColor",22);
        orpLocatorRuleMap.put("dayOfWeekBackground",22);
        orpLocatorRuleMap.put("dayOfWeekTextAppearance",22);
        orpLocatorRuleMap.put("headerDayOfMonthTextAppearance",22);
        orpLocatorRuleMap.put("headerSelectedTextColor",22);
        orpLocatorRuleMap.put("headerYearTextAppearance",22);
        orpLocatorRuleMap.put("yearListItemTextAppearance",22);
        orpLocatorRuleMap.put("yearListSelectorColor",22);
        orpLocatorRuleMap.put("headerAmPmTextAppearance",22);
        orpLocatorRuleMap.put("amPmBackgroundColor",21);
        orpLocatorRuleMap.put("amPmSelectedBackgroundColor",21);
        orpLocatorRuleMap.put("amPmTextColor",21);
    }

    public static void buildcDepRuleMap(){
        cDepRuleMap.add(new Triplet<>("settingsActivity", "supportsInlineSuggestions", 30));
        cDepRuleMap.add(new Triplet<>("fontProviderAuthority", "fontProviderQuery", 26));
        cDepRuleMap.add(new Triplet<>("fontProviderAuthority", "fontProviderPackage", 26));
        cDepRuleMap.add(new Triplet<>("fontProviderQuery", "fontProviderPackage", 27));
        cDepRuleMap.add(new Triplet<>("min", "max", 26));
        cDepRuleMap.add(new Triplet<>("min", "progress", 26));
        cDepRuleMap.add(new Triplet<>("min", "secondaryProgress", 26));
        cDepRuleMap.add(new Triplet<>("min", "indeterminateOnly", 26));
        cDepRuleMap.add(new Triplet<>("gradientRadius", "type", 24));
        cDepRuleMap.add(new Triplet<>("fillColor", "trimPathOffset", 24));
        cDepRuleMap.add(new Triplet<>("fillColor", "strokeLineCap", 24));
        cDepRuleMap.add(new Triplet<>("strokeColor", "strokeLineCap", 24));
        cDepRuleMap.add(new Triplet<>("strokeColor", "trimPathOffset", 24));
        cDepRuleMap.add(new Triplet<>("thumbTint", "tickMark", 23));
        cDepRuleMap.add(new Triplet<>("yearListItemTextAppearance", "headerBackground", 23));
        cDepRuleMap.add(new Triplet<>("yearListItemTextAppearance", "firstDayOfWeek", 23));
        cDepRuleMap.add(new Triplet<>("yearListItemTextAppearance", "endYear", 23));
        cDepRuleMap.add(new Triplet<>("thumbTint", "textOn", 23));
    }

    public static void buildConfDroidRuleMap(){}
    public static void main(String[] args) {
        buildLintRules();
        buildORPLocatorRuleMap();
        buildcDepRuleMap();
        lintRuleMap.keySet().removeAll(orpLocatorRuleMap.keySet());
        for(String key : lintRuleMap.keySet()){
            System.out.println("key = " + key);
        }
        System.exit(0);
        String[] apkRootPaths = new String[]{
                "/Volumes/v/google-top500/google1/",
                "/Volumes/v/google-top500/google2/",
                "/Volumes/v/google-top500/google3/",
                "/Volumes/v/google-top500/google4/",
                "/Volumes/v/google-top500/google5/",
                "/Volumes/v/google-top500/google6/",
                "/Volumes/v/experiment-for-fdroid/experiment-for-fdroid-apk-files/"
        };

        for(String apkRootPath : apkRootPaths) {
            String[] filestrs = (new File(apkRootPath)).list();
            readfile(apkRootPath, filestrs);
        }
    }
    public static void readfile(String root, String[] paths) {
        if (paths == null) {
            return;
        }

        // build appCompatList
        for (String p : paths) {
            appCompatList.clear();
            File tempFile = new File(root+p);
            if (tempFile.isDirectory()) {
//                System.out.println(tempFile.toString());
                funcCheckSrcCompat(tempFile);
                // check whether appCompatList contains incompatible attributes
                funcCheckSrcCompatEvolvedAttributes(tempFile);
            }
        }

    }

    private static void funcCheckSrcCompatEvolvedAttributes(File file){
        File[] fs = file.listFiles();
        if (fs != null)
            for (File f : fs) {
                if (f.isDirectory())
                    funcCheckSrcCompatEvolvedAttributes(f);
                if (f.isFile() &&
                        f.toString().endsWith(".xml"))
                    try {
                        apkPath = f.toString();
                        apkPath = apkPath.substring(0, apkPath.indexOf("/res/")) + "/res/";
                        if (!apkPath.contains("abc_") && !apkPath.contains("notification_template") &&
                        !apkPath.contains("mtrl_")) {
//                            System.out.println("f.getPath() = " + f.getPath());
                            checkSrcCompatEvolvedAttributes(f.getPath());
                        }
                    } catch (Exception exception) {}
            }
    }

    private static void funcCheckSrcCompat(File file) {
        File[] fs = file.listFiles();
        if (fs != null)
            for (File f : fs) {
                if (f.isDirectory())
                    funcCheckSrcCompat(f);
                if (f.isFile() &&
                        f.toString().endsWith(".xml"))
                    try {
                        apkPath = f.toString();
                        apkPath = apkPath.substring(0, apkPath.indexOf("/res/")) + "/res/";
                        if (!apkPath.contains("abc_") && !apkPath.contains("notification_template"))
                            srcCompatDetection(f.getPath());
                    } catch (Exception exception) {}
            }
    }

    public static void parseStyle(String styleName, String path) {
        String path1 = path + "/values/styles.xml";
        File xmlFile = new File(path1);
        if (styleMap.containsKey(styleName))
            return;
        try {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("style");
            for (int i = 0; i < nList.getLength(); i++) {
                Node node = nList.item(i);
                org.w3c.dom.Element eleRoot = (org.w3c.dom.Element)node;
                if (node.getNodeType() == 1 && (
                        styleName.endsWith(eleRoot.getAttribute("name")) || styleName
                                .startsWith(eleRoot.getAttribute("name")))) {
                    styleMap.put(eleRoot.getAttribute("name"), eleRoot);
                    NodeList itemList = ((org.w3c.dom.Element)node).getElementsByTagName("item");
                    for (int j = 0; j < itemList.getLength(); j++) {
                        org.w3c.dom.Element eleItem = (org.w3c.dom.Element)itemList.item(j);
                        parseStyleItem(eleItem, path);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void parseStyleParent(String parent, String path) {
        parseStyle(parent, path);
    }

    public static void parseStyleItem(org.w3c.dom.Element eleItem, String path) {
        if (eleItem.getTextContent().startsWith("@color/")) {
            parseColor(eleItem.getTextContent().substring(7), path);
        } else if (eleItem.getTextContent().startsWith("@string/")) {
            parseString(eleItem.getTextContent().substring(9), path);
        } else if (eleItem.getTextContent().startsWith("@drawable/")) {
            parseDrawable(eleItem.getTextContent().substring(10), path);
        }
    }

    public static void parseColor(String colorName, String path) {
        String path1 = path + "/values/colors.xml";
        File xmlFile = new File(path1);
        try {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("color");
            for (int i = 0; i < nList.getLength(); i++) {
                Node node = nList.item(i);
                org.w3c.dom.Element ele = (org.w3c.dom.Element)node;
                if (node.getNodeType() == 1 &&
                        colorName.endsWith(ele.getAttribute("name")))
                    colorMap.put(ele.getAttribute("name"), ele.getTextContent());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String parseWidget(Node node, String path) {
        String ret = "";
        ret = ret + "\t<" + node.getNodeName() + "\n";
        NamedNodeMap nodeMap = node.getAttributes();
        for (int i = 0; i < nodeMap.getLength(); i++) {
            Node n = nodeMap.item(i);
            String nName = n.getNodeName();
            String nValue = n.getNodeValue();
            if (nValue.contains("@color/")) {
                String colorName = nValue.substring(nValue.indexOf("@color/"));
                colorName = colorName.substring(7);
                colorSet.add(colorName);
                parseColor(colorName, path);
            } else if (nValue.contains("@drawable/")) {
                String drawableName = nValue.substring(nValue.indexOf("@drawable/"));
                drawableName = drawableName.substring(10);
                drawableSet.add(drawableName);
                parseDrawable(drawableName, path);
            } else if (nValue.contains("@array/")) {
                String arrayName = nValue.substring(nValue.indexOf("@array/"));
                arrayName = arrayName.substring(7);
                arraySet.add(arrayName);
                parseArray(arrayName, path);
            } else if (nValue.contains("@string/")) {
                String arrayName = nValue.substring(nValue.indexOf("@string/"));
                arrayName = arrayName.substring(8);
                stringSet.add(arrayName);
                parseString(arrayName, path);
            } else if (nValue.contains("@dimen/")) {
                String dimenName = nValue.substring(nValue.indexOf("@dimen/"));
                dimenName = dimenName.substring(7);
                dimenSet.add(dimenName);
                parseDimen(dimenName, path);
            } else if (nValue.contains("@integer/")) {
                String integerName = nValue.substring(nValue.indexOf("@integer/"));
                integerName = integerName.substring(9);
                integerSet.add(integerName);
                parseInteger(integerName, path);
            } else if (nValue.contains("@style/")) {
                String styleName = nValue.substring(nValue.indexOf("@style/"));
                styleName = styleName.substring(7);
                styleSet.add(styleName);
                parseStyle(styleName, path);
            }
            ret = ret + "\t\t" + n + "\n";
        }
        ret = ret + "\t/>\n";
        StringUtils.printStringXml(stringMap, arrayMap);
        StringUtils.printThemeXml(styleMap);
        StringUtils.printColorXml(colorMap);
        return ret;
    }

    private static void parseDrawable(String drawableName, String path) {
        String path1 = path + "/drawable/" + drawableName + ".xml";
        File xmlFile = new File(path1);
        try {
            Scanner scanner = new Scanner(xmlFile);
            while (scanner.hasNextLine())
                System.out.println(scanner.nextLine());
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);
            doc.getDocumentElement().normalize();
            org.w3c.dom.Element rootEle = doc.getDocumentElement();
            NodeList nList = rootEle.getChildNodes();
            for (int i = 0; i < nList.getLength(); i++) {
                Node node = nList.item(i);
                if (node.getNodeType() == 1) {
                    org.w3c.dom.Element ele = (org.w3c.dom.Element)node;
                    NamedNodeMap attrs = ele.getAttributes();
                    for (int j = 0; j < attrs.getLength(); j++) {
                        Attr attr = (Attr)attrs.item(j);
                        parseValue(attr.getName(), attr.getValue());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void parseValue(String name, String value) {}

    private static void parseInteger(String stringName, String path) {
        String path1 = path + "/values/integers.xml";
        File xmlFile = new File(path1);
        try {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("integer");
            for (int i = 0; i < nList.getLength(); i++) {
                Node node = nList.item(i);
                org.w3c.dom.Element ele = (org.w3c.dom.Element)node;
                if (node.getNodeType() == 1 &&
                        stringName.endsWith(ele.getAttribute("name")))
                    integerMap.put(ele.getAttribute("name"), ele);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void parseDimen(String stringName, String path) {
        String path1 = path + "/values/dimens.xml";
        File xmlFile = new File(path1);
        try {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("dimen");
            for (int i = 0; i < nList.getLength(); i++) {
                Node node = nList.item(i);
                org.w3c.dom.Element ele = (org.w3c.dom.Element)node;
                if (node.getNodeType() == 1 &&
                        stringName.endsWith(ele.getAttribute("name")))
                    dimenMap.put(ele.getAttribute("name"), ele);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void parseString(String stringName, String path) {
        String path1 = path + "/values/strings.xml";
        File xmlFile = new File(path1);
        try {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("string");
            for (int i = 0; i < nList.getLength(); i++) {
                Node node = nList.item(i);
                org.w3c.dom.Element ele = (org.w3c.dom.Element)node;
                if (node.getNodeType() == 1 &&
                        stringName.endsWith(ele.getAttribute("name"))) {
                    System.out.println("outputstr: " + ele.getTextContent());
                    if (ele.getTextContent().contains("<"))
                        System.out.println("contains HTML tag");
                    stringMap.put(ele.getAttribute("name"), ele);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void parseArray(String arrayName, String path) {
        String path1 = path + "/values/strings.xml";
        File xmlFile = new File(path1);
        try {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("string-array");
            for (int i = 0; i < nList.getLength(); i++) {
                Node node = nList.item(i);
                org.w3c.dom.Element ele = (org.w3c.dom.Element)node;
                if (node.getNodeType() == 1 &&
                        arrayName.endsWith(ele.getAttribute("name")))
                    arrayMap.put(ele.getAttribute("name"), ele);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void srcCompatSearch(org.dom4j.Element element, String path) {
        List<org.dom4j.Element> nodes = new ArrayList<>();
        Queue<org.dom4j.Element> q = new LinkedList<>();
        q.add(element);
        while(!q.isEmpty()){
            org.dom4j.Element top = q.poll();
            nodes.add(top);
            for(org.dom4j.Element child : top.elements()){
                q.add(child);
            }
        }
        for(org.dom4j.Element node : nodes) {
//            System.out.println("path = " + path);
            int apiLevelInPath = 1;
            String apiLevelFolder = path.split("/")[path.split("/").length - 2];
            if (apiLevelFolder.contains("-v")) {
                apiLevelInPath = Integer.parseInt(apiLevelFolder.split("-v")[1]);
//                System.out.println("apiLevelInPath = " + apiLevelInPath);
            }
            List<Attribute> list1 = node.attributes();
            for (Attribute attr : list1) {
                if(attr.getNamespacePrefix().equals("app")){
//                    System.out.println("attr.getName() = " + attr.getName());
//                    System.out.println("attr.getValue() = " + attr.getValue());
                    if(attr.getValue().startsWith("@drawable/")){
                        appCompatList.add(attr.getValue());
                    }
                }
//                if(lintRuleMap.containsKey(name)) {
//                    int apiLevelInAttr = lintRuleMap.get(name);
//                    if (apiLevelInPath >= apiLevelInAttr) {
//                        System.out.println("path = " + path);
//                        System.out.println("element.asXML() = " + node.asXML());
//                    }
//                }
            }
        }
    }

    private static boolean contains(List<String> attrList, String name) {
        for (String attr : attrList) {
            if (attr.equals(name))
                return true;
        }
        return false;
    }

    public static void checkSrcCompatEvolvedAttributes(String path) throws Exception{
        if (path.contains("abc_") || path.contains("notification_template") || path.endsWith("design_password_eye.xml") || path
                .endsWith("/preference_list_divider_material.xml"))
            return;

        if(path.contains("/drawable") == false){
            return;
        }

        // check the path is in appCompatList
        String fileName = path.split("/")[path.split("/").length-1];
        fileName = fileName.replace(".xml","");
        fileName = "@drawable/"+fileName;
//        System.out.println("fileName = " + fileName);
        if(appCompatList.contains(fileName)){

        }
        SAXReader sax = new SAXReader();
        File xmlFile = new File(path);
        org.dom4j.Document document = sax.read(xmlFile);
        org.dom4j.Element root = document.getRootElement();

        List<org.dom4j.Element> nodes = new ArrayList<>();
        Queue<org.dom4j.Element> q = new LinkedList<>();
        q.add(root);
        while(!q.isEmpty()){
            org.dom4j.Element top = q.poll();
            nodes.add(top);
            for(org.dom4j.Element child : top.elements()){
                q.add(child);
            }
        }
        for(org.dom4j.Element node : nodes) {
            int apiLevelInPath = 1;
            String apiLevelFolder = path.split("/")[path.split("/").length - 2];
            if (apiLevelFolder.contains("-v")) {
                apiLevelInPath = Integer.parseInt(apiLevelFolder.split("-v")[1]);
            }
            List<Attribute> list1 = node.attributes();
            if(mode.equals("lint") || mode.equals("orplocator")) {
                for (Attribute attr : list1) {
                    if (mode.equals("lint")) {
                        if (lintRuleMap.containsKey(attr.getName())) {
                            System.out.println("path = " + path);
                            System.out.println("element.asXML() = " + node.asXML());
                        }
                    } else if (mode.equals("orplocator")) {
                        if (orpLocatorRuleMap.containsKey(attr.getName())) {
                            System.out.println("path = " + path);
                            System.out.println("element.asXML() = " + node.asXML());
                        }
                    }
                }
            }else if(mode.equals("cdep")){
                for(Triplet<String, String, Integer> triplet : cDepRuleMap){
                    String attrstr1 = triplet.getValue0();
                    String attrstr2 = triplet.getValue1();
                    boolean flag1 = false;
                    boolean flag2 = false;
                    for(Attribute attr : list1){
                        if(attr.getName().equals(attrstr1)){
                            flag1 = true;
                        }
                        if(attr.getName().equals(attrstr2)){
                            flag2 = true;
                        }
                        if(flag1 && flag2){
                            System.out.println("path = " + path);
                            System.out.println("element.asXML() = " + node.asXML());
                        }
                    }
                }
            }else if(mode.equals("confdroid")){
                    for (Attribute attr : list1) {
                        String name = attr.getName();
                        if (lintRuleMap.containsKey(name)) {
                            System.out.println("path = " + path);
                            System.out.println("element.asXML() = " + node.asXML());
                        }
                    }
                    String[] attrs;
                    List<String> attrList;
                    List<Attribute> listAttr;
                    String value;
                    String nodeName = node.getName();
                    switch (nodeName) {
                        case "item":
                        if (node.getParent().getName().equals("animated-selector")) {
                            String[] arrayOfString = {"id", "drawable"};
                            List<String> list = Arrays.asList(arrayOfString);
                            List<Attribute> list3 = node.attributes();
                            for (Attribute attr : list3) {
                                String name = attr.getName();
                                if (list.contains(name)) {
                                    String str = attr.getValue();
                                    System.out.println("path = " + path);
                                    System.out.println("+ name + " + str);
                                    System.out.println("node.asXML() = " + node.asXML());
                                }
                            }
                            break;
                        }
                        if (node.getParent().getName().equals("gradient") && path.contains("/color")) {
                            String[] arrayOfString = {"color", "offset"};
                            List<String> list = Arrays.asList(arrayOfString);
                            List<Attribute> list2 = node.attributes();
                            for (Attribute attr : list2) {
                                String name = attr.getName();
                                if (list.contains(name)) {
                                    String str = attr.getValue();
                                    System.out.println("path = " + path);
                                    System.out.println("+ name + " + str);
                                    System.out.println("node.asXML() = " + node.asXML());
                                }
                            }
                            break;
                        }
                        attrs = new String[]{
                                "android:headerAmPmTextAppearance", "android:amPmBackgroundColor", "android:headerTimeTextAppearance", "android:dayOfWeekBackground", "android:headerYearTextAppearance", "android:headerDayOfMonthAppearance", "android:headerMonthTextAppearance", "android:dayOfWeekTextAppearance", "android:calendarTextColor", "android:yearListSelectorColor",
                                "android:yearListItemTextAppearance", "android:dropDownSelector", "android:indeterminateTint", "android:progressBackgroundTintMode", "android:progressTintMode", "android:divider"};
                        value = node.attributeValue("name");
                        for (String attr : attrs) {
                            if (attr.equals(value)) {
                                System.out.println("path = " + path);
                                System.out.println("node.asXML() = " + node.asXML());
                            }
                        }
                        break;
                    case "transition":
                        attrs = new String[]{"fromId", "drawable", "toId", "reversible"};
                        attrList = Arrays.asList(attrs);
                        listAttr = node.attributes();
                        for (Attribute attr : listAttr) {
                            String name = attr.getName();
                            if (attrList.contains(name)) {
                                String str = attr.getValue();
                                System.out.println("path = " + path);
                                System.out.println("+ name + " + str);
                                System.out.println("node.asXML() = " + node.asXML());
                            }
                        }
                        break;
                    case "layer-list":
                        attrs = new String[]{"paddingBottom", "paddingEnd", "paddingLeft", "paddingRight", "paddingStart", "paddingTop"};
                        attrList = Arrays.asList(attrs);
                        listAttr = node.attributes();
                        for (Attribute attr : listAttr) {
                            String name = attr.getName();
                            if (attrList.contains(name)) {
                                String str = attr.getValue();
                                System.out.println("path = " + path);
                                System.out.println("+ name + " + str);
                                System.out.println("node.asXML() = " + node.asXML());
                            }
                        }
                        break;
                    case "ripple":
                        attrs = new String[]{"radius"};
                        attrList = Arrays.asList(attrs);
                        listAttr = node.attributes();
                        for (Attribute attr : listAttr) {
                            String name = attr.getName();
                            if (attrList.contains(name)) {
                                String str = attr.getValue();
                                System.out.println("path = " + path);
                                System.out.println("+ name + " + str);
                                System.out.println("node.asXML() = " + node.asXML());
                            }
                        }
                        break;
                    case "android.widget.Toolbar":
                        attrs = new String[]{"logo"};
                        attrList = Arrays.asList(attrs);
                        listAttr = node.attributes();
                        for (Attribute attr : listAttr) {
                            String name = attr.getName();
                            if (attrList.contains(name)) {
                                String str = attr.getValue();
                                System.out.println("path = " + path);
                                System.out.println("+ name + " + str);
                                System.out.println("node.asXML() = " + node.asXML());
                            }
                        }
                        break;
                    case "android.widget.AutoCompleteTextView":
                    case "android.widget.Spinner":
                        attrs = new String[]{"popupTheme"};
                        attrList = Arrays.asList(attrs);
                        listAttr = node.attributes();
                        for (Attribute attr : listAttr) {
                            String name = attr.getName();
                            if (attrList.contains(name)) {
                                String str = attr.getValue();
                                System.out.println("path = " + path);
                                System.out.println("+ name + " + str);
                                System.out.println("node.asXML() = " + node.asXML());
                            }
                        }
                        break;
                    case "android.widget.DatePicker":
                        attrs = new String[]{"dayOfWeekBackground", "headerYearTextAppearance", "headerDayOfMonthAppearance", "headerMonthTextAppearance", "dayOfWeekTextAppearance", "calendarTextColor", "yearListSelectorColor", "yearListItemTextAppearance"};
                        attrList = Arrays.asList(attrs);
                        listAttr = node.attributes();
                        for (Attribute attr : listAttr) {
                            String name = attr.getName();
                            if (attrList.contains(name)) {
                                System.out.println("path = " + path);
                                String str = attr.getValue();
                                System.out.println("+ name + " + str);
                                System.out.println("node.asXML() = " + node.asXML());
                            }
                        }
                        break;
                    case "android.widget.TimePicker":
                        attrs = new String[]{"headerAmPmTextAppearance", "amPmBackgroundColor", "headerTimeTextAppearance", "amPmSelectedBackgroundColor", "amPmTextColor"};
                        attrList = Arrays.asList(attrs);
                        listAttr = node.attributes();
                        for (Attribute attr : listAttr) {
                            String name = attr.getName();
                            if (attrList.contains(name)) {
                                System.out.println("path = " + path);
                                String str = attr.getValue();
                                System.out.println("+ name + " + str);
                                System.out.println("node.asXML() = " + node.asXML());
                            }
                        }
                        break;
                    case "android.widget.ProgressBar":
                        attrs = new String[]{"progressBackgroundTintMode", "progressTintMode"};
                        attrList = Arrays.asList(attrs);
                        listAttr = node.attributes();
                        for (Attribute attr : listAttr) {
                            String name = attr.getName();
                            if (contains(attrList, name)) {
                                System.out.println("path = " + path);
                                String str = attr.getValue();
                                System.out.println("+ name + " + str);
                                System.out.println("node.asXML() = " + node.asXML());
                            }
                        }
                        break;
                    case "shape":
                        attrs = new String[]{"tint", "tintMode", "gradientRadius"};
                        attrList = Arrays.asList(attrs);
                        listAttr = node.attributes();
                        for (Attribute attr : listAttr) {
                            String name = attr.getName();
                            if (contains(attrList, name)) {
                                System.out.println("path = " + path);
                                String str = attr.getValue();
                                System.out.println("+ name + " + str);
                                System.out.println("node.asXML() = " + node.asXML());
                            }
                        }
                        break;
                    case "adaptive-icon":
                        System.out.println("adaptive bug");
                        System.out.println("path = " + path);
                        System.out.println(node.asXML());
                        break;
                }
            }
        }

    }

    public static void srcCompatDetection(String path) throws Exception {
        if (path.contains("abc_") || path.contains("notification_template") || path.endsWith("design_password_eye.xml") || path
                .endsWith("/preference_list_divider_material.xml"))
            return;
        SAXReader sax = new SAXReader();
        File xmlFile = new File(path);
        org.dom4j.Document document = sax.read(xmlFile);
        org.dom4j.Element root = document.getRootElement();
        srcCompatSearch(root, path);
    }

    private static String getViewSuperClass(String nodeName) {
        SootClass sootCls = Scene.v().getSootClass(nodeName);
        Set<SootClass> visited = new HashSet<>();
        Queue<SootClass> queue = new LinkedList<>();
        queue.add(sootCls);
        while (!queue.isEmpty()) {
            SootClass top = queue.poll();
            if (!visited.contains(top)) {
                visited.add(top);
                if (top.toString().startsWith("android.widget."))
                    return top.toString();
                if (top.hasSuperclass())
                    queue.add(top.getSuperclass());
                queue.addAll((Collection<? extends SootClass>)top.getInterfaces());
            }
        }
        return "";
    }

    private static boolean checkAppCompat(String path, String mode) {
        System.out.println(path);
        String rootpath = path.split("/res/")[0];
        String filename = path.split("drawable/")[1];
        File rootfile = new File(rootpath);
        return funcc(rootfile, filename, mode);
    }

    private static boolean funcc(File file, String filename, String mode) {
        File[] fs = file.listFiles();
        for (File f : fs) {
            if (f.isDirectory())
                return funcc(f, filename, mode);
            if (f.isFile() &&
                    f.toString().endsWith(".xml"))
                try {
                    apkPath = f.toString();
                    apkPath = apkPath.substring(0, apkPath.indexOf("/res/")) + "/res/";
                    if (!apkPath.contains("abc_") && !apkPath.contains("notification_template"))
                        return ddetection(f.getPath(), filename, mode);
                } catch (Exception exception) {}
        }
        return true;
    }

    private static boolean ddetection(String path, String filename, String mode) throws Exception {
        File xmlFile = new File(path);
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = builderFactory.newDocumentBuilder();
        Document doc = builder.parse(xmlFile);
        doc.getDocumentElement().normalize();
        if (mode.equals("src")) {
            NodeList nList = doc.getElementsByTagName("ImageView");
            for (int i = 0; i < nList.getLength(); i++) {
                Node node = nList.item(i);
                System.out.println("Node name: " + node.getNodeName());
                System.out.println(path);
                org.w3c.dom.Element ele = (org.w3c.dom.Element)node;
                if (node.getNodeType() == 1 &&
                        ele.hasAttribute("android:src")) {
                    String attrValue = ele.getAttribute("android:src");
                    if (attrValue.contains(filename))
                        return false;
                }
            }
        } else if (mode.equals("dropdown")) {
            NodeList nList = doc.getElementsByTagName("Spinner");
            if (nList.getLength() > 0)
                return false;
        }
        return true;
    }
}
