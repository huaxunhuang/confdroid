/*     */ package androidsourcecode;
/*     */ import java.io.FileNotFoundException;
import java.io.FileReader;
/*     */ import java.util.*;
/*     */
/*     */
/*     */
/*     */ import callgraph.graph.FrameworkCallGraph;
import soot.Body;
/*     */ import soot.SootClass;
/*     */ import soot.SootMethod;
/*     */ import soot.Unit;
/*     */ import soot.UnitPatchingChain;
import soot.toolkits.graph.ExceptionalUnitGraph;

/*     */
/*     */ public class AndroidSourceCodeUtil {
/*  14 */   private static String androidSourceRootPath = "/Users/name1/Library/Android/sdk/sources";
/*     */   
/*  16 */   private static Map<Integer, Map<String, ArrayList<String>>> androidSourceCodeMapApiLevel = new HashMap<>();
/*  17 */   private static Map<Integer, Map<String, ArrayList<String>>> androidStatementsMapApiLevel = new HashMap<>();
/*     */   
/*     */   public static ArrayList<Unit> findUnitsByLineNumber(SootMethod mtd, int lineNumber) {
/*  20 */     ArrayList<Unit> ret = new ArrayList<>();
/*  21 */     UnitPatchingChain unitPatchingChain = mtd.getActiveBody().getUnits();
/*  22 */     for (Unit unit : unitPatchingChain) {
/*  23 */       if (unit.getJavaSourceStartLineNumber() == lineNumber) {
/*  24 */         ret.add(unit);
/*     */       }
/*     */     } 
/*  27 */     return ret;
/*     */   }
/*     */   
/*     */   public static String readSourceCodeByLineNumber(int apiLevel, SootMethod mtd, int lineNumber) throws FileNotFoundException {
/*  31 */     String androidSourcePath = androidSourceRootPath + "/android-" + apiLevel + "/";
/*  32 */     String className = mtd.getDeclaringClass().getName();
/*  33 */     if (className.contains("$")) {
/*  34 */       className = className.split("\\$")[0];
/*     */     }
/*  36 */     String subPath = className.replaceAll("\\.", "/");
/*  37 */     androidSourcePath = androidSourcePath + subPath + ".java";
/*  38 */     if (!androidSourceCodeMapApiLevel.containsKey(Integer.valueOf(apiLevel))) {
/*  39 */       androidSourceCodeMapApiLevel.put(Integer.valueOf(apiLevel), new HashMap<>());
/*     */     }
/*     */     
/*  42 */     if (!androidStatementsMapApiLevel.containsKey(Integer.valueOf(apiLevel))) {
/*  43 */       androidStatementsMapApiLevel.put(Integer.valueOf(apiLevel), new HashMap<>());
/*     */     }
/*     */     
/*  46 */     Map<String, ArrayList<String>> androidSourceCodeMap = androidSourceCodeMapApiLevel.get(Integer.valueOf(apiLevel));
/*  47 */     if (!androidSourceCodeMap.containsKey(className)) {
/*  48 */       ArrayList<String> arrayList = new ArrayList<>();
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/*  53 */         FileReader reader = new FileReader(androidSourcePath);
/*     */         
/*  55 */         Scanner in = new Scanner(reader);
/*  56 */         while (in.hasNextLine()) {
/*  57 */           String lineStr = in.nextLine();
/*  58 */           arrayList.add(lineStr);
/*     */         } 
/*  60 */         androidSourceCodeMap.put(className, arrayList);
/*  61 */       } catch (FileNotFoundException e) {
/*  62 */         return "";
/*     */       } 
/*     */     } 
/*     */     
/*  66 */     ArrayList<String> codeList = androidSourceCodeMap.get(className);
/*     */ 
/*     */     
/*  69 */     StringBuilder ret = new StringBuilder();
/*  70 */     Body mtdBody = mtd.getActiveBody();
/*  71 */     UnitPatchingChain unitPatchingChain = mtdBody.getUnits();
/*     */     
/*  73 */     for (Unit mtdUnit : unitPatchingChain) {
/*  74 */       int mtdLineNum = mtdUnit.getJavaSourceStartLineNumber();
/*  75 */       if (mtdLineNum == -1 || mtdLineNum != lineNumber) {
/*     */         continue;
/*     */       }
/*     */ 
/*     */       
/*  80 */       int ptr = -1;
/*  81 */       while (!isStatementEnd(ret.toString().trim()) && mtdLineNum + ptr < codeList.size()) {
/*  82 */         ret.append(((String)codeList.get(mtdLineNum + ptr)).trim());
/*  83 */         ptr++;
/*     */       } 
/*     */     } 
/*     */     
/*  87 */     if (lineNumber >= 2 && lineNumber - 1 < codeList
/*  88 */       .size() && ((String)codeList
/*  89 */       .get(lineNumber - 1)).trim().startsWith("case ") && ((String)codeList
/*  90 */       .get(lineNumber - 1)).trim().contains(":")) {
/*  91 */       return ((String)codeList.get(lineNumber - 1)).trim();
/*     */     }
/*  93 */     return ret.toString().trim();
/*     */   }
/*     */   
/*     */   private static boolean isStatementEnd(String str) {
/*  97 */     return (str.endsWith("{") || str.endsWith("}") || str.endsWith(";") || (str.contains("case ") && str.endsWith(":")));
/*     */   }
/*     */   
/*     */   public static ArrayList<Integer> readSourceCodeByLineNumberWithBreakStrategy(int apiLevel, SootMethod mtd, int lineNumber) throws FileNotFoundException {
/* 101 */     String androidSourcePath = androidSourceRootPath + "/android-" + apiLevel + "/";
/* 102 */     String className = mtd.getDeclaringClass().getName();
/* 103 */     if (className.contains("$")) {
/* 104 */       className = className.split("\\$")[0];
/*     */     }
/* 106 */     String subPath = className.replaceAll("\\.", "/");
/* 107 */     androidSourcePath = androidSourcePath + subPath + ".java";
/*     */     
/* 109 */     if (!androidSourceCodeMapApiLevel.containsKey(Integer.valueOf(apiLevel))) {
/* 110 */       androidSourceCodeMapApiLevel.put(Integer.valueOf(apiLevel), new HashMap<>());
/*     */     }
/*     */     
/* 113 */     Map<String, ArrayList<String>> androidSourceCodeMap = androidSourceCodeMapApiLevel.get(Integer.valueOf(apiLevel));
/* 114 */     if (!androidSourceCodeMap.containsKey(className)) {
/* 115 */       ArrayList<String> codeList = new ArrayList<>();
/*     */ 
/*     */       
/* 118 */       FileReader reader = new FileReader(androidSourcePath);
/* 119 */       Scanner in = new Scanner(reader);
/* 120 */       while (in.hasNextLine()) {
/* 121 */         String lineStr = in.nextLine();
/* 122 */         codeList.add(lineStr);
/*     */       } 
/* 124 */       androidSourceCodeMap.put(className, codeList);
/*     */     } 
/*     */     
/* 127 */     ArrayList<Integer> ret = new ArrayList<>();
/* 128 */     if (lineNumber >= 1) {
/* 129 */       Body mtdBody = mtd.getActiveBody();
/* 130 */       UnitPatchingChain unitPatchingChain = mtdBody.getUnits();
/* 131 */       int minLineNum = Integer.MAX_VALUE;
/* 132 */       for (Unit mtdUnit : unitPatchingChain) {
/* 133 */         int mtdLineNum = mtdUnit.getJavaSourceStartLineNumber();
/* 134 */         if (mtdLineNum > lineNumber && mtdLineNum < minLineNum) {
/* 135 */           minLineNum = Math.min(minLineNum, mtdLineNum);
/*     */         }
/*     */       } 
/*     */       
/* 139 */       if (minLineNum != Integer.MAX_VALUE) {
/* 140 */         for (int i = lineNumber; i < minLineNum; i++) {
/* 141 */           ret.add(Integer.valueOf(i));
/*     */         }
/*     */       }
/*     */     } 
/*     */     
/* 146 */     return ret;
/*     */   }
/*     */   
/*     */   public static String extractAttributeName(String stmtStr) {
/* 150 */     String[] keywords = stmtStr.split("[,()?:&| {};=><+\\-/*\\[\\]]+");
/* 151 */     for (String keyword : keywords) {
/* 152 */       if (!keyword.contains("\"") && keyword.contains("R.") && (keyword.contains("_") || keyword.contains("R.attr"))) {
/* 153 */         return keyword;
/*     */       }
/*     */     } 
/* 156 */     return null;
/*     */   }
/*     */   
/*     */   public static int getStartLineNum(SootMethod mtd) {
/* 160 */     return mtd.getJavaSourceStartLineNumber();
/*     */   }
/*     */   
/*     */   public static SootClass getSootClassByPath(String path, int apiLevel) {
/* 164 */     SootClass ret = null;
/* 165 */     if (!path.contains("core/java") || !path.endsWith(".java")) {
/* 166 */       return null;
/*     */     }
/* 168 */     path = path.replace(".java", "");
/* 169 */     int index = path.indexOf("/android/");
/* 170 */     path = path.substring(index + 1);
/* 171 */     path = path.replace("/", ".");
/*     */     
/* 173 */     ret = FrameworkCallGraph.getFCG(apiLevel).getSootClass(path);
/*     */     
/* 175 */     return ret;
/*     */   }
/*     */   
/*     */   public static SootMethod getHostSootMethodByLineNumber(SootClass cls, int lineNum) {
/* 179 */     List<SootMethod> mtds = cls.getMethods();
/* 180 */     for (SootMethod mtd : mtds) {
/* 181 */       int startLine = getStartLineNum(mtd);
/* 182 */       int endLine = getEndLineNum(mtd);
/* 183 */       if (startLine <= lineNum && lineNum <= endLine) {
/* 184 */         return mtd;
/*     */       }
/*     */     } 
/* 187 */     return null;
/*     */   }
/*     */   
/*     */   public static int getEndLineNum(SootMethod mtd) {
/*     */     try {
/* 192 */       ExceptionalUnitGraph exceptionalUnitGraph = new ExceptionalUnitGraph(mtd.retrieveActiveBody());
/* 193 */       List<Unit> tailList = exceptionalUnitGraph.getTails();
/* 194 */       int lineNum = Integer.MIN_VALUE;
/* 195 */       for (Unit unit : tailList) {
/* 196 */         lineNum = Math.max(lineNum, unit.getJavaSourceStartLineNumber());
/*     */       }
/* 198 */       return lineNum;
/* 199 */     } catch (RuntimeException runtimeException) {
/*     */ 
/*     */       
/* 202 */       return -1;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/androidsourcecode/AndroidSourceCodeUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */