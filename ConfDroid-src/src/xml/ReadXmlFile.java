/*     */ package xml;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.StringReader;
/*     */ import java.io.StringWriter;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import javax.xml.transform.Transformer;
/*     */ import javax.xml.transform.TransformerException;
/*     */ import javax.xml.transform.TransformerFactory;
/*     */ import javax.xml.transform.dom.DOMSource;
/*     */ import javax.xml.transform.stream.StreamResult;
/*     */ import org.w3c.dom.Attr;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.NamedNodeMap;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ import org.xml.sax.InputSource;
/*     */ 
/*     */ public class ReadXmlFile
/*     */ {
/*     */   public static void main(String[] args) {
/*  24 */     initDoc();
/*     */   }
/*     */   
/*     */   public static Document styleDoc;
/*     */   public static Document colorDoc;
/*     */   
/*     */   public static String initColorData() {
/*  31 */     String ret = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<resources>\n    <color name=\"colorPrimary\">#008577</color>\n    <color name=\"colorPrimaryDark\">#00574B</color>\n    <color name=\"colorAccent\">#D81B60</color>\n</resources>\n";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  37 */     return ret;
/*     */   }
/*     */   
/*     */   public static String initStyleData() {
/*  41 */     String ret = "<resources>\n\n    <style name=\"AppTheme\" parent=\"Theme.AppCompat.Light.DarkActionBar\">\n        <!-- Customize your theme here. -->\n        <item name=\"colorPrimary\">@color/colorPrimary</item>\n        <item name=\"colorPrimaryDark\">@color/colorPrimaryDark</item>\n        <item name=\"colorAccent\">@color/colorAccent</item>\n    </style>\n    \n</resources>";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  51 */     return ret;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void initDoc() {
/*     */     try {
/*  57 */       DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
/*  58 */       colorDoc = factory.newDocumentBuilder().parse(new InputSource(new StringReader(
/*  59 */               initColorData())));
/*  60 */       System.out.println(documentToString(colorDoc));
/*  61 */       styleDoc = factory.newDocumentBuilder().parse(new InputSource(new StringReader(
/*  62 */               initStyleData())));
/*  63 */       System.out.println(documentToString(styleDoc));
/*  64 */       styleDoc.getElementsByTagName("resource");
/*  65 */     } catch (Exception e) {
/*  66 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */   
/*     */   public static String documentToString(Document document) {
/*     */     try {
/*  72 */       TransformerFactory tf = TransformerFactory.newInstance();
/*  73 */       Transformer trans = tf.newTransformer();
/*  74 */       StringWriter sw = new StringWriter();
/*  75 */       trans.transform(new DOMSource(document), new StreamResult(sw));
/*  76 */       return sw.toString();
/*  77 */     } catch (TransformerException tEx) {
/*  78 */       tEx.printStackTrace();
/*     */       
/*  80 */       return null;
/*     */     } 
/*     */   }
/*     */   private static void func(File file) {
/*  84 */     File[] fs = file.listFiles();
/*  85 */     for (File f : fs) {
/*  86 */       if (f.isDirectory())
/*  87 */         func(f); 
/*  88 */       if (f.isFile() && 
/*  89 */         f.toString().endsWith(".xml")) {
/*     */         try {
/*  91 */           detection(f.getPath());
/*  92 */         } catch (Exception exception) {}
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void detection(String path) throws Exception {
/* 100 */     File xmlFile = new File(path);
/*     */     
/* 102 */     DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
/* 103 */     DocumentBuilder builder = builderFactory.newDocumentBuilder();
/* 104 */     Document doc = builder.parse(xmlFile);
/* 105 */     doc.getDocumentElement().normalize();
/* 106 */     NodeList nList = doc.getElementsByTagName("TextView");
/* 107 */     for (int i = 0; i < nList.getLength(); i++) {
/* 108 */       Node node = nList.item(i);
/* 109 */       Element ele = (Element)node;
/* 110 */       if (node.getNodeType() == 1 && 
/* 111 */         ele.hasAttribute("android:text")) {
/* 112 */         System.out.println("----------------------------");
/* 113 */         printElement(ele);
/* 114 */         System.out.println("===========");
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void printElement(Element element) {
/* 123 */     System.out.println("<" + element.getTagName());
/* 124 */     NamedNodeMap attrs = element.getAttributes();
/*     */     
/* 126 */     int numAttrs = attrs.getLength();
/* 127 */     for (int i = 0; i < numAttrs; i++) {
/* 128 */       Attr attr = (Attr)attrs.item(i);
/* 129 */       String attrName = attr.getNodeName();
/* 130 */       String attrValue = attr.getNodeValue();
/* 131 */       System.out.println(attrName + "=\"" + attrValue + "\"");
/*     */     } 
/* 133 */     System.out.println("/>");
/*     */   }
/*     */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/xml/ReadXmlFile.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */