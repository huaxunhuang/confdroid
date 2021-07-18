package falseattranalysis;

import lu.uni.tsopen.StringUtils;
import org.apache.poi.util.StringUtil;
import org.dom4j.Attribute;
import org.dom4j.io.SAXReader;
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

/**
 * To check in the apk files to see how many attributes are actually unused in their host XML elements
 */
public class UnusedAttrProportionAnalysis {
    static int num = 0;
    static int totalNum = 0;
    static int interestingNum = 0;
    public static Set<String> colorSet = new HashSet<>();

    public static Map<String, String> colorMap = new HashMap<>();

    static String apkPath = "";

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

    public static void initSoot(String projectPath) {
        G.reset();
        Scene.v().addBasicClass("java.lang.Object", 2);
        Scene.v().addBasicClass("java.io.PrintStream", 2);
        Scene.v().addBasicClass("java.lang.System", 2);
        Options.v().set_src_prec(5);
        Options.v().set_force_android_jar("/Users/name1/Documents/android.jar");
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
    public static void main(String[] args) {
        String apkRootPath = "/Volumes/v/google-play/";
        File[] filesa = (new File(apkRootPath)).listFiles();
        try {
            BuildClassAttrPairs.init();
        } catch (Exception e) {
            e.printStackTrace();
        }
        readfile(filesa);
        System.out.println("num: "+num);
    }
    public static void readfile(File[] files) {
        if (files == null) {// 如果目录为空，直接退出
            return;
        }
        for (File apkFile : files) {
            if(apkFile.toString().endsWith(".apk")) {
                try {
//                    initializeClassHierarchies(apkFile.toString());
                    resPath = apkFile.toString();
                    resPath = resPath.replace(".apk", "");
                    System.out.println("resPath = " + resPath);
                    File file = new File(resPath);
                    func(file);
                }catch(Exception e){}
            }
            else if (apkFile.isDirectory()) {
                readfile(apkFile.listFiles());
            }
        }
    }
    private static void initializeClassHierarchies(String path) {
        initSoot(path);
    }

    private static boolean isSuperClass(String cls1, String cls2) {
        SootClass sootCls1 = Scene.v().getSootClass(cls1);
        SootClass sootCls2 = Scene.v().getSootClass(cls2);
        Set<SootClass> visited = new HashSet<>();
        Queue<SootClass> queue = new LinkedList<>();
        queue.add(sootCls1);
        while (!queue.isEmpty()) {
            SootClass top = queue.poll();
            if (!visited.contains(top)) {
                visited.add(top);
                if (top.equals(sootCls2))
                    return true;
                if (top.hasSuperclass())
                    queue.add(top.getSuperclass());
                queue.addAll((Collection<? extends SootClass>)top.getInterfaces());
            }
        }
        return false;
    }

    private static void func(File file) {
        File[] fs = file.listFiles();
        if (fs != null)
            for (File f : fs) {
                if (f.isDirectory())
                    func(f);
                if (f.isFile() &&
                        f.toString().endsWith(".xml"))
                    try {
                        apkPath = f.toString();
                        apkPath = apkPath.substring(0, apkPath.indexOf("/res/")) + "/res/";
                        if (!apkPath.contains("abc_") && !apkPath.contains("notification_template"))
                            detection(f.getPath());
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

    public static void getNodes(org.dom4j.Element node, String path) {
        String nodeName = node.getName();
        if (nodeName.contains(".") && !nodeName.startsWith("android."))
            nodeName = getViewSuperClass(node.getName());
//        if (StringUtil.isUpperCase(nodeName.charAt(0)))
//            nodeName = "android.widget." + nodeName;
        if (nodeName.length() > 0) {
            totalNum = totalNum+1;
            String[] attrs;
            List<String> attrList;
            List<Attribute> listAttr;
            String value;

            String attributeList = BuildClassAttrPairs.checkPairs(nodeName);
            attributeList += BuildClassAttrPairs.checkPairs("View");
            attributeList += BuildClassAttrPairs.checkPairs("ViewGroup");
//            System.out.println("attributeList = " + attributeList);
            if(attributeList.length()>0) {
                List<Attribute> list1 = node.attributes();
                for (Attribute attr : list1) {
                    String name = attr.getName();
                    if (attributeList.contains(name) == false &&
                    path.contains("/values") == false && attr.getNamespace().getPrefix().equals("android")
                    && nodeName.equals("view") == false && nodeName.equals("translate") == false
                    && nodeName.contains("shortcut") == false) {
                        interestingNum = interestingNum + 1;
                        System.out.println("path = " + path);
                        System.out.println("name: " + name);
                        System.out.println("node.asXML() = " + node.asXML());
                        System.out.println("proportion = " + (float)interestingNum/(float) totalNum);
                    }
                }
            }
        }
        List<org.dom4j.Element> listElement = node.elements();
        for (org.dom4j.Element e : listElement) {
            getNodes(e, path);
        }
    }

    private static boolean contains(List<String> attrList, String name) {
        for (String attr : attrList) {
            if (attr.equals(name))
                return true;
        }
        return false;
    }

    public static void detection(String path) throws Exception {
        if (path.contains("abc_") || path.contains("notification_template") || path.contains("notification_action") || path.endsWith("design_password_eye.xml") || path
                .endsWith("/preference_list_divider_material.xml") || path.contains("mtrl_"))
            return;
        SAXReader sax = new SAXReader();
        File xmlFile = new File(path);
        org.dom4j.Document document = sax.read(xmlFile);
        org.dom4j.Element root = document.getRootElement();
        getNodes(root, path);
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
