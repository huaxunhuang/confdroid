/*      */ package androidsourcecode.branchedflow;
/*      */ import androidsourcecode.AndroidSourceCodeUtil;
/*      */ import androidsourcecode.gitcommit.BuildGitCommitsInDifferentVersions;
/*      */ import androidsourcecode.gitcommit.CodeChange;
/*      */ import callgraph.graph.Edge;
/*      */ import callgraph.graph.FrameworkCallGraph;
/*      */ import callgraph.graph.FrameworkCallGraphInApiLevel;
/*      */ import com.google.common.annotations.VisibleForTesting;
import jas.Pair;
/*      */ import java.io.FileNotFoundException;
/*      */ import java.util.*;
import java.util.stream.Collectors;
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */ import precondition.IntraPreconditionUtil;
import precondition.entity.Precondition;
import soot.Body;
/*      */ import soot.SootClass;
/*      */ import soot.SootMethod;
/*      */ import soot.Unit;
/*      */ import soot.UnitBox;
/*      */ import soot.UnitPatchingChain;
/*      */ import soot.Value;
/*      */ import soot.ValueBox;
/*      */ import soot.jimple.AssignStmt;
import soot.jimple.Stmt;
/*      */ import soot.jimple.internal.*;
/*      */
/*      */
/*      */
/*      */ import soot.toolkits.graph.DirectedGraph;
/*      */ import soot.toolkits.graph.ExceptionalUnitGraph;
/*      */ import soot.toolkits.graph.InverseGraph;
/*      */ 
/*      */ public class MainSourceSinkScan {
/*   36 */   private static List<OldPreconditionItem> oldPreconditionItemList = new ArrayList<>();
/*      */   
/*   38 */   private static int MAX_DEPTH = 1; public static boolean containsAttributeNameInStmt(String stmtStr) {
/*   39 */     return (AndroidSourceCodeUtil.extractAttributeName(stmtStr) == null);
/*      */   }
/*      */   
/*      */   private static List<OldPreconditionItem> getPotentialSourcesInApiLevel(int apiLevel) {
/*   43 */     List<OldPreconditionItem> ret = new ArrayList<>();
/*   44 */     for (OldPreconditionItem oldPreconditionItem : oldPreconditionItemList) {
/*   45 */       if (oldPreconditionItem.apiLevel == apiLevel) {
/*   46 */         ret.add(oldPreconditionItem);
/*      */       }
/*      */     } 
/*   49 */     return ret;
/*      */   }
/*      */   
/*      */   @VisibleForTesting
/*      */   private static List<Unit> getStmtsByLineNum(SootMethod mtd, int lineNum) {
/*   54 */     List<Unit> retList = new ArrayList<>();
/*   55 */     Body body = mtd.retrieveActiveBody();
/*   56 */     UnitPatchingChain unitPatchingChain = body.getUnits();
/*      */     
/*   58 */     for (Unit unit : unitPatchingChain) {
/*   59 */       if (lineNum == unit.getJavaSourceStartLineNumber()) {
/*   60 */         retList.add(unit);
/*      */       }
/*      */     } 
/*      */     
/*   64 */     return retList;
/*      */   }
/*      */   
/*      */   @VisibleForTesting
/*      */   private static List<Unit> getStmtsByLineNums(SootMethod mtd, List<Integer> lineNums) {
/*   69 */     List<Unit> retList = new ArrayList<>();
/*   70 */     Body body = mtd.retrieveActiveBody();
/*   71 */     UnitPatchingChain unitPatchingChain = body.getUnits();
/*      */     
/*   73 */     for (Unit unit : unitPatchingChain) {
/*   74 */       int lineNum = unit.getJavaSourceStartLineNumber();
/*   75 */       if (lineNums.contains(Integer.valueOf(lineNum))) {
/*   76 */         retList.add(unit);
/*      */       }
/*      */     } 
/*      */     
/*   80 */     return retList;
/*      */   }
/*      */ 
/*      */   
/*      */   public static void analyzeDefaultValues(int apiLevel) throws Exception {
/*   85 */     FrameworkCallGraphInApiLevel cg = FrameworkCallGraph.getFCG(apiLevel);
/*   86 */     List<SootClass> sootClasses = cg.getSootClasses();
/*   87 */     for (SootClass cls : sootClasses) {
/*   88 */       if (!cls.toString().startsWith("android.")) {
/*      */         continue;
/*      */       }
/*   91 */       for (SootMethod mtd : cls.getMethods()) {
/*   92 */         if (!mtd.hasActiveBody()) {
/*      */           continue;
/*      */         }
/*   95 */         UnitPatchingChain unitPatchingChain = mtd.getActiveBody().getUnits();
/*   96 */         Set<Integer> interestingLineNumbers = new HashSet<>();
/*   97 */         for (Unit unit : unitPatchingChain) {
/*   98 */           int lineNumber = unit.getJavaSourceStartLineNumber();
/*   99 */           if (lineNumber != -1) {
/*  100 */             String srcCode = AndroidSourceCodeUtil.readSourceCodeByLineNumber(apiLevel, mtd, lineNumber);
/*  101 */             String type = getType(srcCode);
/*  102 */             if (srcCode.contains("R.styleable") || (srcCode.contains("R.attr") && !srcCode.contains("R.styleable.Theme"))) {
/*  103 */               interestingLineNumbers.add(Integer.valueOf(lineNumber)); continue;
/*  104 */             }  if (type.length() > 0) {
/*  105 */               String lastSrcCode = AndroidSourceCodeUtil.readSourceCodeByLineNumber(apiLevel, mtd, lineNumber - 1);
/*  106 */               if (lastSrcCode.contains("case") && (lastSrcCode.contains("R.styleable") || lastSrcCode.contains("R.attr"))) {
/*      */ 
/*      */ 
/*      */                 
/*  110 */                 System.out.println("1111");
/*  111 */                 System.out.println("srcCode: " + lastSrcCode);
/*  112 */                 String attr = parseAttrFromSrcCode(lastSrcCode);
/*  113 */                 System.out.println("attr: " + attr);
/*  114 */                 List<Unit> lnunits = getUnitsByLineNumber(mtd, lineNumber);
/*  115 */                 String srccCode = AndroidSourceCodeUtil.readSourceCodeByLineNumber(apiLevel, mtd, lineNumber);
/*  116 */                 List<String> defaultValues = getDefaultValueSimple(lnunits, srccCode, mtd);
/*  117 */                 if (attr != null && attr.length() > 0) {
/*  118 */                   for (String defaultValue : defaultValues) {
/*  119 */                     attrDef.add(new Pair(attr, defaultValue));
/*      */                   }
/*      */                 }
/*  122 */                 System.out.println();
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         } 
/*  127 */         if (interestingLineNumbers.size() > 0) {
/*  128 */           for (Integer ln : interestingLineNumbers) {
/*  129 */             List<Unit> lnunits = getUnitsByLineNumber(mtd, ln.intValue());
/*  130 */             boolean flag = false;
/*  131 */             for (Unit lnunit : lnunits) {
/*  132 */               if (lnunit.toString().contains("hasValue")) {
/*  133 */                 flag = true;
/*      */                 break;
/*      */               } 
/*      */             } 
/*  137 */             if (!flag) {
/*  138 */               String lastSrcCode = AndroidSourceCodeUtil.readSourceCodeByLineNumber(apiLevel, mtd, ln.intValue());
/*  139 */               if (lastSrcCode.contains("case") && (lastSrcCode.contains("R.styleable") || lastSrcCode.contains("R.attr"))) {
/*  140 */                 String str = AndroidSourceCodeUtil.readSourceCodeByLineNumber(apiLevel, mtd, ln.intValue() + 1);
/*      */ 
/*      */ 
/*      */ 
/*      */                 
/*      */                 continue;
/*      */               } 
/*      */ 
/*      */ 
/*      */               
/*  150 */               String attr = parseAttrFromSrcCode(lastSrcCode);
/*  151 */               List<String> defaultValues = getDefaultValueSimple(lnunits, lastSrcCode, mtd);
/*  152 */               if (attr != null && attr.length() > 0) {
/*  153 */                 for (String defaultValue : defaultValues) {
/*  154 */                   attrDef.add(new Pair(attr, defaultValue));
/*      */                 }
/*      */               }
/*      */             } 
/*      */           } 
/*      */         }
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  164 */     for (Pair<String, String> ad : attrDef) {
/*  165 */       System.out.println("ad: " + ad);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*  170 */   public static Set<Pair<String, String>> attrDef = new HashSet<>();
/*      */   
/*      */   public static List<String> getDefaultValueSimple(List<Unit> units, String srcCode, SootMethod mtd) {
/*  173 */     for (Unit unit : units) {
/*  174 */       String type = getType(unit);
/*  175 */       if (type.length() <= 0 || 
/*  176 */         !((Stmt)unit).containsInvokeExpr()) {
/*      */         continue;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  183 */       List<String> retString = new ArrayList<>();
/*  184 */       List<Value> argList = ((Stmt)unit).getInvokeExpr().getArgs();
/*  185 */       if (argList.size() > 1) {
/*  186 */         if (argList.get(1) instanceof soot.jimple.internal.JimpleLocal) {
/*      */ 
/*      */           
/*  189 */           List<Value> ret = resolveDefValue(mtd, unit, argList.get(1));
/*      */           
/*  191 */           for (Value v : ret) {
/*      */             
/*  193 */             if (v instanceof JInstanceFieldRef) {
/*  194 */               retString.add(((JInstanceFieldRef)v).getFieldRef().toString()); continue;
/*  195 */             }  if (v instanceof JVirtualInvokeExpr) {
/*  196 */               retString.add(((JVirtualInvokeExpr)v).getMethod().toString()); continue;
/*  197 */             }  if (v instanceof JDynamicInvokeExpr) {
/*  198 */               retString.add(((JDynamicInvokeExpr)v).getMethod().toString()); continue;
/*  199 */             }  if (v instanceof JSpecialInvokeExpr) {
/*  200 */               retString.add(((JSpecialInvokeExpr)v).getMethod().toString()); continue;
/*  201 */             }  if (v instanceof JStaticInvokeExpr) {
/*  202 */               retString.add(((JStaticInvokeExpr)v).getMethod().toString()); continue;
/*  203 */             }  if (v instanceof JInterfaceInvokeExpr) {
/*  204 */               retString.add(((JInterfaceInvokeExpr)v).getMethod().toString());
/*      */             }
/*      */           } 
/*  207 */           System.out.println("");
/*      */         } else {
/*  209 */           retString.add(((Value)argList.get(1)).toString());
/*      */         } 
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  219 */       return retString;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  224 */     return new ArrayList<>();
/*      */   }
/*      */   
/*      */   public static String parseAttrFromSrcCode(String srcCode) {
/*  228 */     if (srcCode.contains("R.styleable")) {
/*  229 */       int index = srcCode.indexOf("R.styleable");
/*  230 */       for (int i = index; i < srcCode.length(); ) {
/*  231 */         char chr = srcCode.charAt(i);
/*  232 */         if ((chr >= 'A' && chr <= 'Z') || (chr >= 'a' && chr <= 'z') || (chr >= '0' && chr <= '9') || chr == '.' || chr == '_') {
/*      */           i++;
/*      */           continue;
/*      */         } 
/*  236 */         return srcCode.substring(index, i);
/*      */       } 
/*      */     } 
/*      */     
/*  240 */     return "";
/*      */   }
/*      */   
/*      */   public static List<Value> resolveDefValue(SootMethod mtd, Unit unit, Value value) {
/*  244 */     Map<Unit, List<Value>> unitListMap = new HashMap<>();
/*  245 */     List<Value> temp = new ArrayList<>();
/*  246 */     temp.add(value);
/*  247 */     unitListMap.put(unit, temp);
/*  248 */     Queue<Unit> queue = new LinkedList<>();
/*  249 */     Set<Unit> visited = new HashSet<>();
/*  250 */     if (!mtd.hasActiveBody()) {
/*  251 */       return temp;
/*      */     }
/*  253 */     Body body = mtd.retrieveActiveBody();
/*  254 */     ExceptionalUnitGraph exceptionalUnitGraph = new ExceptionalUnitGraph(body);
/*      */     
/*  256 */     queue.add(unit);
/*  257 */     while (!queue.isEmpty()) {
/*  258 */       Unit top = queue.poll();
/*  259 */       if (visited.contains(top)) {
/*      */         continue;
/*      */       }
/*  262 */       visited.add(top);
/*      */ 
/*      */       
/*  265 */       List<Value> topValueList = unitListMap.get(top);
/*  266 */       List<ValueBox> defBoxes = top.getDefBoxes();
/*  267 */       for (Value topValue : topValueList) {
/*  268 */         if (containValues(defBoxes, topValue)) {
/*  269 */           if (top instanceof AssignStmt) {
/*  270 */             topValueList.add(((AssignStmt)top).getRightOp());
/*  271 */             List<ValueBox> valueBoxList = ((AssignStmt)top).getRightOp().getUseBoxes();
/*  272 */             for (ValueBox valueBox : valueBoxList) {
/*  273 */               topValueList.add(valueBox.getValue());
/*      */             }
/*      */           } 
/*      */           break;
/*      */         } 
/*      */       } 
/*  279 */       unitListMap.put(top, topValueList);
/*  280 */       List<Unit> preds = exceptionalUnitGraph.getPredsOf(top);
/*  281 */       queue.addAll(preds);
/*  282 */       for (Unit pred : preds) {
/*  283 */         List<Value> temp1 = new ArrayList<>(unitListMap.get(top));
/*  284 */         unitListMap.put(pred, temp1);
/*      */       } 
/*      */     } 
/*      */     
/*  288 */     List<Value> ret = new ArrayList<>();
/*  289 */     for (Unit headUnit : exceptionalUnitGraph.getHeads()) {
/*  290 */       List<Value> valueList = unitListMap.get(headUnit);
/*  291 */       if (valueList != null) {
/*  292 */         ret.addAll(valueList);
/*      */       }
/*      */     } 
/*      */     
/*  296 */     List<Value> retList = (List<Value>)ret.stream().distinct().collect(Collectors.toList());
/*  297 */     return retList;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean containValues(List<ValueBox> valueBoxes, Value value) {
/*  302 */     for (ValueBox valueBox : valueBoxes) {
/*  303 */       if (value.equals(valueBox.getValue())) {
/*  304 */         return true;
/*      */       }
/*      */     } 
/*  307 */     return false;
/*      */   }
/*      */   
/*      */   public static void mainProcess(int apiLevel) throws Exception {
/*  311 */     FrameworkCallGraphInApiLevel cg = FrameworkCallGraph.getFCG(apiLevel);
/*  312 */     System.out.println(cg.getSootClasses().size());
/*  313 */     List<SootClass> sootClasses = cg.getSootClasses();
/*  314 */     for (SootClass cls : sootClasses) {
/*  315 */       if (!cls.toString().startsWith("android.")) {
/*      */         continue;
/*      */       }
/*  318 */       for (SootMethod mtd : cls.getMethods()) {
/*  319 */         if (!mtd.hasActiveBody()) {
/*      */           continue;
/*      */         }
/*  322 */         UnitPatchingChain unitPatchingChain = mtd.getActiveBody().getUnits();
/*  323 */         Set<Integer> interestingLineNumbers = new HashSet<>();
/*  324 */         for (Unit unit : unitPatchingChain) {
/*  325 */           int lineNumber = unit.getJavaSourceStartLineNumber();
/*  326 */           if (lineNumber != -1) {
/*  327 */             String srcCode = AndroidSourceCodeUtil.readSourceCodeByLineNumber(apiLevel, mtd, lineNumber);
/*  328 */             String type = getType(srcCode);
/*  329 */             if (srcCode.contains("R.styleable") || (srcCode.contains("R.attr") && !srcCode.contains("R.styleable.Theme"))) {
/*  330 */               interestingLineNumbers.add(Integer.valueOf(lineNumber)); continue;
/*  331 */             }  if (type.length() > 0) {
/*  332 */               String lastSrcCode = AndroidSourceCodeUtil.readSourceCodeByLineNumber(apiLevel, mtd, lineNumber - 1);
/*  333 */               if (lastSrcCode.contains("case") && (lastSrcCode.contains("R.styleable") || lastSrcCode.contains("R.attr"))) {
/*  334 */                 IntraPreconditionUtil.getIntraPreconditionSimple(mtd, unit, lineNumber, srcCode, lastSrcCode, type, apiLevel);
/*      */               }
/*      */             } 
/*      */           } 
/*      */         } 
/*      */ 
/*      */         
/*  341 */         if (interestingLineNumbers.size() > 0) {
/*  342 */           for (Integer ln : interestingLineNumbers) {
/*  343 */             List<Unit> lnunits = getUnitsByLineNumber(mtd, ln.intValue());
/*  344 */             boolean flag = false;
/*  345 */             for (Unit lnunit : lnunits) {
/*  346 */               if (lnunit.toString().contains("hasValue")) {
/*  347 */                 flag = true;
/*      */                 break;
/*      */               } 
/*      */             } 
/*  351 */             if (!flag) {
/*  352 */               String lastSrcCode = AndroidSourceCodeUtil.readSourceCodeByLineNumber(apiLevel, mtd, ln.intValue());
/*  353 */               if (lastSrcCode.contains("case") && (lastSrcCode.contains("R.styleable") || lastSrcCode.contains("R.attr"))) {
/*  354 */                 String srcCode = AndroidSourceCodeUtil.readSourceCodeByLineNumber(apiLevel, mtd, ln.intValue() + 1);
/*  355 */                 String type = getType(srcCode);
/*  356 */                 if (type.length() > 0) {
/*  357 */                   IntraPreconditionUtil.getIntraPreconditionSimple(mtd, null, ln.intValue() + 1, srcCode, lastSrcCode, type, apiLevel);
/*      */                 }
/*      */                 
/*      */                 continue;
/*      */               } 
/*      */               
/*  363 */               IntraPreconditionUtil.getIntraPrecondition(mtd, lnunits, ln.intValue(), apiLevel, null, new ArrayList(), 0);
/*      */             } 
/*      */           } 
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*  373 */   static Precondition precondition = null;
/*      */   
/*      */   public static List<Unit> getUnitsByLineNumber(SootMethod mtd, int lineNum) {
/*  376 */     List<Unit> ret = new ArrayList<>();
/*  377 */     UnitPatchingChain unitPatchingChain = mtd.getActiveBody().getUnits();
/*  378 */     for (Unit unit : unitPatchingChain) {
/*  379 */       int ln = unit.getJavaSourceStartLineNumber();
/*  380 */       if (ln == lineNum) {
/*  381 */         ret.add(unit);
/*      */       }
/*      */     } 
/*  384 */     return ret;
/*      */   }
/*      */   
/*      */   public static ArrayList<CodeChange> legacyMainProcess(String filePath, int apiLevel, String plusOrMinus) {
/*  388 */     BuildGitCommitsInDifferentVersions buildGitCommitsInDifferentVersions = null;
/*      */     try {
/*  390 */       buildGitCommitsInDifferentVersions = new BuildGitCommitsInDifferentVersions(filePath, apiLevel);
/*  391 */     } catch (Exception e) {
/*  392 */       e.printStackTrace();
/*      */     } 
/*  394 */     ArrayList<CodeChange> codeChanges = buildGitCommitsInDifferentVersions.codeChangeArrayList;
/*  395 */     ArrayList<CodeChange> ret = new ArrayList<>();
/*      */     
/*  397 */     for (CodeChange codeChange : codeChanges) {
/*  398 */       if (codeChange.className == null) {
/*      */         continue;
/*      */       }
/*      */       
/*  402 */       ret.add(codeChange);
/*      */     } 
/*      */     
/*  405 */     for (CodeChange codeChange : codeChanges) {
/*  406 */       if (codeChange.className == null) {
/*      */         continue;
/*      */       }
/*      */       
/*  410 */       if (plusOrMinus.equals("plus")) {
/*  411 */         Map<SootMethod, List<Integer>> plusDiffMap = readPlusDiffs(codeChange, apiLevel);
/*  412 */         if (plusDiffMap != null)
/*  413 */           for (SootMethod mtd : plusDiffMap.keySet()) {
/*  414 */             if (mtd.getDeclaringClass().toString().startsWith("android."))
/*  415 */               scanForSource(mtd, mtd, plusDiffMap.get(mtd), apiLevel, "plus"); 
/*      */           }  
/*      */         continue;
/*      */       } 
/*  419 */       if (plusOrMinus.equals("minus")) {
/*  420 */         Map<SootMethod, List<Integer>> minusDiffMap = readMinusDiffs(codeChange, apiLevel);
/*  421 */         if (minusDiffMap != null) {
/*  422 */           for (SootMethod mtd : minusDiffMap.keySet()) {
/*  423 */             if (mtd.getDeclaringClass().toString().startsWith("android.")) {
/*  424 */               scanForSource(mtd, mtd, minusDiffMap.get(mtd), apiLevel, "minus");
/*      */             }
/*      */           } 
/*      */         }
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  432 */     return ret;
/*      */   }
/*      */ 
/*      */   
/*      */   public static Map<SootMethod, List<Integer>> readMinusDiffs(CodeChange codeChange, int apiLevel) {
/*  437 */     FrameworkCallGraphInApiLevel fcg = FrameworkCallGraph.getFCG(apiLevel);
/*  438 */     List<SootClass> sootClasses = fcg.getSootClasses();
/*  439 */     String className = codeChange.className;
/*  440 */     List<SootClass> gradientClasses = new ArrayList<>();
/*  441 */     for (SootClass sootClass : sootClasses) {
/*  442 */       if (sootClass.getName().equals(className)) {
/*  443 */         gradientClasses.add(sootClass); continue;
/*      */       } 
/*  445 */       if (sootClass.getName().startsWith(className + "$")) {
/*  446 */         gradientClasses.add(sootClass);
/*      */       }
/*      */     } 
/*      */     
/*  450 */     Map<SootMethod, List<Integer>> mtdLineMap = new HashMap<>();
/*  451 */     for (SootClass gradientClass : gradientClasses) {
/*  452 */       for (SootMethod mtd : gradientClass.getMethods()) {
/*      */         
/*  454 */         Set<Integer> lineNums = new HashSet<>();
/*  455 */         if (mtd.hasActiveBody()) {
/*  456 */           UnitPatchingChain unitPatchingChain = mtd.getActiveBody().getUnits();
/*  457 */           for (Unit unit : unitPatchingChain) {
/*  458 */             int lineNum = unit.getJavaSourceStartLineNumber();
/*  459 */             ArrayList<Integer> llineNums = null;
/*      */             try {
/*  461 */               llineNums = AndroidSourceCodeUtil.readSourceCodeByLineNumberWithBreakStrategy(apiLevel, mtd, lineNum);
/*  462 */             } catch (FileNotFoundException e) {
/*      */               continue;
/*      */             } 
/*  465 */             if (llineNums.size() > 0) {
/*  466 */               for (Pair<String, Integer> aDiffPair : (Iterable<Pair<String, Integer>>)codeChange.aDiffPairs) {
/*  467 */                 if (llineNums.contains(aDiffPair.getO2()) && 
/*  468 */                   !lineNums.contains(llineNums.get(0))) {
/*  469 */                   lineNums.add(llineNums.get(0));
/*      */                 }
/*      */               } 
/*      */             }
/*      */           } 
/*      */         } 
/*      */ 
/*      */         
/*  477 */         if (lineNums.size() > 0) {
/*  478 */           List<Integer> mapList = new ArrayList<>();
/*  479 */           mapList.addAll(lineNums);
/*  480 */           Collections.sort(mapList);
/*  481 */           mtdLineMap.put(mtd, mapList);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/*  486 */     return mtdLineMap;
/*      */   }
/*      */ 
/*      */   
/*      */   public static Map<SootMethod, List<Integer>> readPlusDiffs(CodeChange codeChange, int apiLevel) {
/*  491 */     FrameworkCallGraphInApiLevel fcg = FrameworkCallGraph.getFCG(apiLevel);
/*  492 */     List<SootClass> sootClasses = fcg.getSootClasses();
/*  493 */     String className = codeChange.className;
/*  494 */     List<SootClass> gradientClasses = new ArrayList<>();
/*  495 */     for (SootClass sootClass : sootClasses) {
/*  496 */       if (sootClass.getName().equals(className)) {
/*  497 */         gradientClasses.add(sootClass); continue;
/*      */       } 
/*  499 */       if (sootClass.getName().startsWith(className + "$")) {
/*  500 */         gradientClasses.add(sootClass);
/*      */       }
/*      */     } 
/*      */     
/*  504 */     Map<SootMethod, List<Integer>> mtdLineMap = new HashMap<>();
/*  505 */     for (SootClass gradientClass : gradientClasses) {
/*  506 */       for (SootMethod mtd : gradientClass.getMethods()) {
/*      */         
/*  508 */         Set<Integer> lineNums = new HashSet<>();
/*  509 */         if (mtd.hasActiveBody()) {
/*  510 */           UnitPatchingChain unitPatchingChain = mtd.getActiveBody().getUnits();
/*  511 */           for (Unit unit : unitPatchingChain) {
/*  512 */             int lineNum = unit.getJavaSourceStartLineNumber();
/*  513 */             ArrayList<Integer> llineNums = null;
/*      */             try {
/*  515 */               llineNums = AndroidSourceCodeUtil.readSourceCodeByLineNumberWithBreakStrategy(apiLevel, mtd, lineNum);
/*  516 */             } catch (FileNotFoundException e) {
/*      */               continue;
/*      */             } 
/*      */             
/*  520 */             if (llineNums.size() > 0) {
/*  521 */               for (Pair<String, Integer> bDiffPair : (Iterable<Pair<String, Integer>>)codeChange.bDiffPairs) {
/*  522 */                 if (llineNums.contains(bDiffPair.getO2()) && 
/*  523 */                   !lineNums.contains(llineNums.get(0))) {
/*  524 */                   lineNums.add(llineNums.get(0));
/*      */                 }
/*      */               } 
/*      */             }
/*      */           } 
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  535 */         if (lineNums.size() > 0) {
/*  536 */           List<Integer> mapList = new ArrayList<>();
/*  537 */           mapList.addAll(lineNums);
/*  538 */           Collections.sort(mapList);
/*  539 */           mtdLineMap.put(mtd, mapList);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/*  544 */     return mtdLineMap;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void scanForSource(SootMethod mtd, SootMethod sourceMtd, List<Integer> interestingLineNums, int apiLevel, String plusOrMinus) {
/*  551 */     System.out.println("\n=-=-=- begin scanning for source -=-=-=");
/*      */     
/*  553 */     List<Unit> units = getStmtsByLineNums(mtd, interestingLineNums);
/*  554 */     System.out.println("interesting mtd: " + mtd.getSignature());
/*  555 */     System.out.println("api level: " + apiLevel);
/*  556 */     System.out.println("\n=== stmts by line numbers ===");
/*  557 */     ArrayList<Integer> unitsLineNums = new ArrayList<>();
/*  558 */     for (Unit unit : units) {
/*  559 */       if (!unitsLineNums.contains(Integer.valueOf(unit.getJavaSourceStartLineNumber()))) {
/*  560 */         unitsLineNums.add(Integer.valueOf(unit.getJavaSourceStartLineNumber()));
/*      */       }
/*      */       
/*  563 */       if (unit instanceof JLookupSwitchStmt) {
/*  564 */         JLookupSwitchStmt switchStmt = (JLookupSwitchStmt)unit;
/*  565 */         List<UnitBox> unitBoxes = switchStmt.getUnitBoxes();
/*  566 */         for (UnitBox unitBox : unitBoxes) {
/*  567 */           int ln = unitBox.getUnit().getJavaSourceStartLineNumber();
/*  568 */           if (!unitsLineNums.contains(Integer.valueOf(ln - 1))) {
/*  569 */             unitsLineNums.add(Integer.valueOf(ln - 1));
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  578 */     UnitPatchingChain unitPatchingChain = mtd.getActiveBody().getUnits();
/*  579 */     for (Unit unit1 : unitPatchingChain) {
/*  580 */       if (unit1 instanceof JLookupSwitchStmt) {
/*  581 */         JLookupSwitchStmt switchStmt = (JLookupSwitchStmt)unit1;
/*  582 */         List<UnitBox> unitBoxes = switchStmt.getUnitBoxes();
/*  583 */         for (UnitBox unitBox : unitBoxes) {
/*  584 */           int ln = unitBox.getUnit().getJavaSourceStartLineNumber();
/*  585 */           if (!unitsLineNums.contains(Integer.valueOf(ln - 1)) && unitsLineNums.contains(Integer.valueOf(ln)))
/*  586 */             unitsLineNums.add(Integer.valueOf(ln - 1)); 
/*      */         }  continue;
/*      */       } 
/*  589 */       if (unit1 instanceof JTableSwitchStmt) {
/*  590 */         JTableSwitchStmt switchStmt = (JTableSwitchStmt)unit1;
/*  591 */         List<UnitBox> unitBoxes = switchStmt.getUnitBoxes();
/*  592 */         for (UnitBox unitBox : unitBoxes) {
/*  593 */           int ln = unitBox.getUnit().getJavaSourceStartLineNumber();
/*  594 */           if (!unitsLineNums.contains(Integer.valueOf(ln - 1)) && unitsLineNums.contains(Integer.valueOf(ln))) {
/*  595 */             unitsLineNums.add(Integer.valueOf(ln - 1));
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/*  600 */     Collections.sort(unitsLineNums);
/*      */     
/*      */     try {
/*  603 */       for (int i = 0; i < unitsLineNums.size(); i++)
/*      */       {
/*  605 */         Integer unitLineNumber = unitsLineNums.get(i);
/*  606 */         String stmtStr = AndroidSourceCodeUtil.readSourceCodeByLineNumber(apiLevel, sourceMtd, unitLineNumber
/*  607 */             .intValue());
/*  608 */         System.out.println("stmt: " + stmtStr + " --- " + unitLineNumber);
/*      */       }
/*      */     
/*  611 */     } catch (FileNotFoundException e) {
/*  612 */       e.printStackTrace();
/*      */     } 
/*      */     
/*  615 */     System.out.println("=== end stmts by line numbers ===\n");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  622 */     Stack<SootMethod> stk = new Stack<>();
/*  623 */     scanForSourceInternal(mtd, sourceMtd, stk, units, units, 0, new ArrayList<>(), apiLevel, plusOrMinus);
/*      */     
/*  625 */     System.out.println("=-=-=- end scanning for source -=-=-=\n");
/*      */   }
/*      */   
/*      */   private static SootMethod getInitMethod(SootClass declaringClass) {
/*  629 */     SootMethod initMethod = null;
/*      */     
/*  631 */     for (SootMethod m : declaringClass.getMethods()) {
/*  632 */       if (m.getSignature().contains("<init>")) {
/*  633 */         if (initMethod == null) {
/*  634 */           initMethod = m;
/*      */           continue;
/*      */         } 
/*  637 */         int size = m.getActiveBody().getUnits().size();
/*  638 */         if (size > initMethod.getActiveBody().getUnits().size()) {
/*  639 */           initMethod = m;
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/*  644 */     return initMethod;
/*      */   }
/*      */ 
/*      */   
/*      */   private static SootMethod getCinitMethod(SootClass declaringClass) {
/*  649 */     for (SootMethod m : declaringClass.getMethods()) {
/*  650 */       if (m.getSignature().contains("<cinit>")) {
/*  651 */         return m;
/*      */       }
/*      */     } 
/*  654 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void scanForSourceInternal(SootMethod mtd, SootMethod sourceMtd, Stack<SootMethod> stkk, List<Unit> sourceUnits, List<Unit> units, int depth, List<Pair<Unit, SootMethod>> potentialSources, int apiLevel, String plusOrMinus) {
/*  662 */     if (depth > MAX_DEPTH) {
/*      */       return;
/*      */     }
/*      */     
/*  666 */     stkk.push(mtd);
/*  667 */     Set<ValueBox> taintedValues = new HashSet<>();
/*  668 */     Body mBody = mtd.retrieveActiveBody();
/*  669 */     InverseGraph<Unit> unitGraph = new InverseGraph((DirectedGraph)new ExceptionalUnitGraph(mBody));
/*  670 */     SootMethod cinitMethod = getCinitMethod(mtd.getDeclaringClass());
/*  671 */     InverseGraph<Unit> cinitGraph = null;
/*  672 */     if (cinitMethod != null)
/*  673 */       cinitGraph = new InverseGraph((DirectedGraph)new ExceptionalUnitGraph(cinitMethod.getActiveBody())); 
/*  674 */     SootMethod initMethod = getInitMethod(mtd.getDeclaringClass());
/*  675 */     InverseGraph<Unit> initGraph = null;
/*  676 */     if (initMethod.hasActiveBody()) {
/*  677 */       initGraph = new InverseGraph((DirectedGraph)new ExceptionalUnitGraph(initMethod.getActiveBody()));
/*      */     }
/*      */     
/*  680 */     Stack<Unit> stk = new Stack<>();
/*  681 */     for (Unit unit : units) {
/*  682 */       stk.push(unit);
/*  683 */       taintedValues.addAll(unit.getUseAndDefBoxes());
/*      */     } 
/*      */     
/*  686 */     Set<Unit> visited = new HashSet<>();
/*      */     
/*  688 */     while (!stk.isEmpty()) {
/*  689 */       Unit top = stk.pop();
/*  690 */       visited.add(top);
/*      */       
/*  692 */       Stmt stmt = (Stmt)top;
/*  693 */       List<ValueBox> values = stmt.getUseAndDefBoxes();
/*  694 */       boolean flag = false;
/*  695 */       for (ValueBox value : values) {
/*  696 */         if (value.getValue().toString().equals("a") || value
/*  697 */           .getValue().toString().equals("this")) {
/*      */           continue;
/*      */         }
/*  700 */         for (ValueBox taintedValue : taintedValues) {
/*  701 */           if (value
/*  702 */             .getValue().equals(taintedValue.getValue())) {
/*  703 */             flag = true;
/*      */             break;
/*      */           } 
/*      */         } 
/*  707 */         if (flag) {
/*      */           break;
/*      */         }
/*      */       } 
/*      */       
/*  712 */       if (flag) {
/*  713 */         taintedValues.addAll(values);
/*      */ 
/*      */         
/*  716 */         if (checkForAttribute(apiLevel, mtd, top)) {
/*  717 */           boolean fFlag = false;
/*  718 */           for (Pair<Unit, SootMethod> pair : potentialSources) {
/*  719 */             Unit unit1 = (Unit)pair.getO1();
/*  720 */             SootMethod mtd2 = (SootMethod)pair.getO2();
/*  721 */             if (unit1.getJavaSourceStartLineNumber() == top.getJavaSourceStartLineNumber() && mtd2
/*  722 */               .getSignature().equals(mtd.getSignature())) {
/*  723 */               fFlag = true;
/*      */             }
/*      */           } 
/*  726 */           if (!fFlag) {
/*  727 */             Pair<Unit, SootMethod> potentialSource = new Pair(top, mtd);
/*  728 */             potentialSources.add(potentialSource);
/*  729 */             System.out.println(" ==== find a potential source ====");
/*  730 */             System.out.println("api level: " + apiLevel);
/*  731 */             System.out.println("depth: " + depth);
/*  732 */             System.out.println("plusMinus: " + plusOrMinus);
/*  733 */             System.out.println("potentialSource.unit: " + potentialSource.getO1());
/*  734 */             System.out.println("top: " + top);
/*  735 */             int ln = ((Unit)potentialSource.getO1()).getJavaSourceStartLineNumber();
/*  736 */             System.out.println("potentialSource.lineNum: " + ln);
/*  737 */             SootMethod potentialSourceMtd = (SootMethod)potentialSource.getO2();
/*  738 */             System.out.println("potentialSource.cls: " + potentialSourceMtd.getDeclaringClass().toString());
/*  739 */             System.out.println("potentialSource.mtd: " + potentialSourceMtd.getSignature());
/*      */             try {
/*  741 */               String s = AndroidSourceCodeUtil.readSourceCodeByLineNumber(apiLevel, potentialSourceMtd, ln);
/*  742 */               System.out.println("potentialSource.stmt: " + s);
/*      */               
/*  744 */               String attr = getAttribute(s, apiLevel, mtd, ln);
/*  745 */               System.out.println("potentialSource.attr: " + attr);
/*  746 */               ArrayList<Unit> lineUnits = AndroidSourceCodeUtil.findUnitsByLineNumber(mtd, ln);
/*  747 */               String type = getType(lineUnits);
/*  748 */               System.out.println("potentialSource.type: " + type);
/*  749 */               String value = getValue(top);
/*  750 */               System.out.println("potentialSource.value: " + value);
/*      */               
/*  752 */               String stackInfo = "";
/*  753 */               for (SootMethod aStkk : stkk) {
/*  754 */                 System.out.println("== begin stack ==");
/*  755 */                 stackInfo = stackInfo + "== begin stack ==\n";
/*  756 */                 SootMethod stkkMtd = aStkk;
/*  757 */                 System.out.println("stkk: " + stkkMtd);
/*  758 */                 stackInfo = stackInfo + "stkk: " + stkkMtd + "\n";
/*  759 */                 System.out.println("== end stack ==");
/*  760 */                 stackInfo = stackInfo + "== end stack ==\n";
/*      */               } 
/*      */ 
/*      */               
/*  764 */               List<Unit> units1 = new ArrayList<>();
/*  765 */               units1.add(potentialSource.getO1());
/*      */               
/*  767 */               String themeType = inferThemeType(apiLevel, units1, ln, potentialSourceMtd);
/*  768 */               System.out.println("potentialSource.themeType: " + themeType);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */               
/*  776 */               System.out.println(" ==== end find a potential source ==== \n");
/*  777 */               OldPreconditionItem p = new OldPreconditionItem(apiLevel, depth, (Unit)potentialSource.getO1(), ln, potentialSourceMtd, s, stackInfo, type, value, attr, themeType);
/*      */               
/*  779 */               oldPreconditionItemList.add(p);
/*  780 */             } catch (FileNotFoundException e) {
/*  781 */               System.out.println("no source code available!");
/*  782 */               System.out.println("==== end find a potential source ==== \n");
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */       
/*  788 */       List<Unit> succList = unitGraph.getSuccsOf(top);
/*  789 */       if (cinitMethod != null) {
/*  790 */         List<Unit> cinitHeads = cinitGraph.getHeads();
/*  791 */         Unit cinitHead = cinitHeads.get(0);
/*  792 */         for (Unit h : cinitHeads) {
/*  793 */           if (h.getJavaSourceStartLineNumber() > cinitHead.getJavaSourceStartLineNumber()) {
/*  794 */             cinitHead = h;
/*      */           }
/*      */         } 
/*      */         
/*  798 */         if (initGraph != null) {
/*  799 */           List<Unit> initHeads = initGraph.getHeads();
/*  800 */           Unit initHead = initHeads.get(0);
/*  801 */           for (Unit h : initHeads) {
/*  802 */             if (h.getJavaSourceStartLineNumber() > initHead.getJavaSourceStartLineNumber()) {
/*  803 */               initHead = h;
/*      */             }
/*      */           } 
/*  806 */           if (mtd.getSignature().contains("<init>")) {
/*  807 */             succList.add(initHead);
/*  808 */             succList.addAll(initGraph.getSuccsOf(initHead));
/*  809 */           } else if (!mtd.getSignature().contains("<cinit>")) {
/*      */ 
/*      */             
/*  812 */             succList.add(initHead);
/*  813 */             succList.addAll(initGraph.getSuccsOf(initHead));
/*  814 */             succList.add(cinitHead);
/*  815 */             succList.addAll(cinitGraph.getSuccsOf(cinitHead));
/*      */           } 
/*      */         } 
/*      */       } 
/*      */       
/*  820 */       for (Unit succ : succList) {
/*  821 */         if (succ.getJavaSourceStartLineNumber() >= top.getJavaSourceStartLineNumber()) {
/*      */           continue;
/*      */         }
/*  824 */         if (stk.contains(succ)) {
/*      */           continue;
/*      */         }
/*  827 */         if (!visited.contains(succ)) {
/*  828 */           stk.push(succ);
/*      */         }
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  834 */     scanForSourceCallGraph(mtd, sourceMtd, stkk, sourceUnits, depth + 1, potentialSources, apiLevel, plusOrMinus);
/*  835 */     stkk.pop();
/*      */   }
/*      */ 
/*      */   
/*      */   private static String inferInitValue(int apiLevel, List<Unit> units, int ln, SootMethod mtd) {
/*  840 */     SootClass cls = mtd.getDeclaringClass();
/*  841 */     SootMethod ptrMtd = mtd;
/*      */     
/*  843 */     Set<Unit> visited = new HashSet<>();
/*  844 */     Set<ValueBox> taintedValues = new HashSet<>();
/*      */     
/*  846 */     units.clear();
/*  847 */     boolean hasSourceUnit = false;
/*  848 */     for (Unit unit : mtd.getActiveBody().getUnits()) {
/*  849 */       if (unit.getJavaSourceStartLineNumber() == ln) {
/*  850 */         if (checkForSourceUnit(unit)) {
/*  851 */           hasSourceUnit = true;
/*      */         }
/*  853 */         units.add(unit);
/*      */       } 
/*      */     } 
/*      */     
/*  857 */     if (hasSourceUnit) {
/*  858 */       Set<ValueBox> defBoxes = new HashSet<>();
/*  859 */       for (Unit unit : units) {
/*  860 */         List<ValueBox> defs = unit.getDefBoxes();
/*  861 */         for (ValueBox def : defs) {
/*  862 */           if (!def.getValue().getClass().getName().contains("Local")) {
/*  863 */             defBoxes.add(def);
/*      */           }
/*      */         } 
/*      */       } 
/*      */       
/*  868 */       if (defBoxes.size() >= 1) {
/*  869 */         for (ValueBox def : defBoxes) {
/*  870 */           if (def.getValue() instanceof JInstanceFieldRef) {
/*  871 */             JInstanceFieldRef defValue = (JInstanceFieldRef)def.getValue();
/*  872 */             SootClass defDeclaringClass = defValue.getFieldRef().declaringClass();
/*  873 */             String defName = defValue.getFieldRef().name();
/*  874 */             String defInitValue = getDefInitValue(defDeclaringClass, defName);
/*  875 */             if (defInitValue.length() > 0) {
/*  876 */               return defInitValue;
/*      */             }
/*      */           } 
/*      */         } 
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/*  884 */     return "";
/*      */   }
/*      */   
/*      */   private static String getDefInitValue(SootClass defDeclaringClass, String defName) {
/*  888 */     for (SootMethod mtd : defDeclaringClass.getMethods()) {
/*  889 */       if (mtd.getSignature().contains("<init>")) {
/*  890 */         UnitPatchingChain unitPatchingChain = mtd.getActiveBody().getUnits();
/*  891 */         for (Unit unit : unitPatchingChain) {
/*  892 */           if (unit instanceof JAssignStmt) {
/*  893 */             JAssignStmt unitAssignStmt = (JAssignStmt)unit;
/*  894 */             Value unitValue = unitAssignStmt.leftBox.getValue();
/*  895 */             if (unitValue instanceof JInstanceFieldRef) {
/*  896 */               JInstanceFieldRef unitFieldRef = (JInstanceFieldRef)unitValue;
/*  897 */               if (unitFieldRef.getFieldRef().name().equals(defName)) {
/*  898 */                 return unitAssignStmt.rightBox.getValue().toString();
/*      */               }
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*  905 */     return "";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static String inferThemeType(int apiLevel, List<Unit> units, int ln, SootMethod mtd) {
/*  924 */     SootClass cls = mtd.getDeclaringClass();
/*  925 */     SootMethod ptrMtd = mtd;
/*      */     
/*  927 */     Set<Unit> visited = new HashSet<>();
/*  928 */     Set<ValueBox> taintedValues = new HashSet<>();
/*      */     
/*  930 */     Stack<Unit> stk = new Stack<>();
/*  931 */     for (Unit unit : units) {
/*  932 */       taintedValues.addAll(unit.getUseAndDefBoxes());
/*  933 */       stk.push(unit);
/*      */     } 
/*      */     
/*  936 */     InverseGraph<Unit> mtdInverseGraph = new InverseGraph((DirectedGraph)new ExceptionalUnitGraph(ptrMtd.getActiveBody()));
/*      */     
/*  938 */     while (!stk.isEmpty()) {
/*  939 */       Unit top = stk.pop();
/*  940 */       if (visited.contains(top)) {
/*      */         continue;
/*      */       }
/*  943 */       visited.add(top);
/*      */       
/*  945 */       Stmt stmt = (Stmt)top;
/*  946 */       List<ValueBox> values = stmt.getUseAndDefBoxes();
/*  947 */       taintedValues.addAll(values);
/*      */       
/*  949 */       boolean flag = false;
/*  950 */       for (ValueBox value : values) {
/*  951 */         if (taintedValues.contains(value)) {
/*  952 */           flag = true;
/*      */           
/*      */           break;
/*      */         } 
/*      */       } 
/*  957 */       if (flag) {
/*  958 */         if (top.toString().contains("ContextThemeWrapper")) {
/*  959 */           return "context_theme_wrapper";
/*      */         }
/*  961 */         for (ValueBox value : values) {
/*  962 */           String valueType = value.getValue().getType().toString();
/*  963 */           if (valueType.contains("android.content.res.Resources$Theme")) {
/*  964 */             return "has_theme";
/*      */           }
/*      */         } 
/*      */       } 
/*      */       
/*  969 */       List<Unit> nextUnits = mtdInverseGraph.getSuccsOf(top);
/*  970 */       for (Unit nextUnit : nextUnits) {
/*  971 */         stk.push(nextUnit);
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/*  976 */     return "";
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean checkForAttribute(int apiLevel, SootMethod mtd, Unit unit) {
/*  982 */     int lineNumber = unit.getJavaSourceStartLineNumber();
/*  983 */     String src = null;
/*      */     try {
/*  985 */       src = AndroidSourceCodeUtil.readSourceCodeByLineNumber(apiLevel, mtd, lineNumber);
/*  986 */     } catch (FileNotFoundException e) {
/*  987 */       return false;
/*      */     } 
/*  989 */     if (src.contains("R.styleable.")) {
/*  990 */       return true;
/*      */     }
/*      */     
/*  993 */     if (checkForSourceUnit(unit)) {
/*  994 */       return true;
/*      */     }
/*      */ 
/*      */     
/*  998 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private static String getValue(Unit unit) {
/* 1003 */     return "";
/*      */   }
/*      */   
/*      */   public static String getAttribute(String str, int apiLevel, SootMethod mtd, int lineNumber) {
/* 1007 */     int index = -1;
/* 1008 */     if (str == null) {
/* 1009 */       str = "";
/*      */     }
/* 1011 */     if (str.contains("R.style")) {
/* 1012 */       index = str.indexOf("R.style");
/* 1013 */     } else if (str.contains("R.attr")) {
/* 1014 */       index = str.indexOf("R.attr");
/*      */     } 
/* 1016 */     if (index != -1) {
/* 1017 */       for (int j = index; j < str.length(); ) {
/* 1018 */         char chr = str.charAt(j);
/* 1019 */         if ((chr >= 'A' && chr <= 'Z') || (chr >= 'a' && chr <= 'z') || (chr >= '1' && chr <= '9') || chr == '.' || chr == '_') {
/*      */           j++; continue;
/* 1021 */         }  return str.substring(index, j);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1027 */     for (int i = 1; i <= 6; i++) {
/*      */       try {
/* 1029 */         String hh = AndroidSourceCodeUtil.readSourceCodeByLineNumber(apiLevel, mtd, lineNumber - i);
/* 1030 */         index = hh.indexOf("R.style");
/* 1031 */         if (index != -1) {
/* 1032 */           for (int j = index; j < hh.length(); ) {
/* 1033 */             char chr = hh.charAt(j);
/* 1034 */             if ((chr >= 'A' && chr <= 'Z') || (chr >= 'a' && chr <= 'z') || chr == '.' || chr == '_') {
/*      */               j++; continue;
/* 1036 */             }  return hh.substring(index, j);
/*      */           } 
/*      */         }
/* 1039 */       } catch (FileNotFoundException e) {}
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1044 */     return null;
/*      */   }
/*      */   
/*      */   public static String getType(Unit unit) {
/* 1048 */     ArrayList<Unit> units = new ArrayList<>();
/* 1049 */     units.add(unit);
/* 1050 */     return getType(units);
/*      */   }
/*      */   
/*      */   public static String getType(String sig) {
/* 1054 */     if (sig.contains("()")) {
/* 1055 */       return "";
/*      */     }
/* 1057 */     if (sig.contains("getInt("))
/* 1058 */       return "integer"; 
/* 1059 */     if (sig.contains("getFloat("))
/* 1060 */       return "float"; 
/* 1061 */     if (sig.contains("getColor("))
/* 1062 */       return "integer"; 
/* 1063 */     if (sig.contains("getString(") || sig.contains("getText("))
/* 1064 */       return "string"; 
/* 1065 */     if (sig.contains("getColorStateList("))
/* 1066 */       return "color_state_list"; 
/* 1067 */     if (sig.contains("getDimension(") && !sig.contains("getDimension(android.util.DisplayMetrics)"))
/* 1068 */       return "float_dimension"; 
/* 1069 */     if (sig.contains("hasValue(") || sig.contains("hasValueOrEmpty("))
/* 1070 */       return "has_value"; 
/* 1071 */     if (sig.contains("getDimensionPixelOffset(") || sig
/* 1072 */       .contains("getDimensionPixelSize("))
/* 1073 */       return "integer_dimension"; 
/* 1074 */     if (sig.contains("getFraction(") && !sig.contains("getFraction(float,float)"))
/* 1075 */       return "fraction"; 
/* 1076 */     if (sig.contains("getDrawable("))
/* 1077 */       return "drawable"; 
/* 1078 */     if (sig.contains("getBoolean("))
/* 1079 */       return "boolean"; 
/* 1080 */     if (sig.contains("getComplexColor("))
/* 1081 */       return "gradient_color"; 
/* 1082 */     if (sig.contains("peekValue("))
/* 1083 */       return "peek_value"; 
/* 1084 */     if (sig.contains("getAttributeFloatValue("))
/* 1085 */       return "float_dimension"; 
/* 1086 */     if (sig.contains("getAttributeIntValue("))
/* 1087 */       return "integer"; 
/* 1088 */     if (sig.contains("getAttributeValue("))
/* 1089 */       return "string"; 
/* 1090 */     if (sig.contains("getAttributeBooleanValue("))
/* 1091 */       return "boolean"; 
/* 1092 */     if (sig.contains("getResourceId(")) {
/* 1093 */       return "resource_id";
/*      */     }
/* 1095 */     return "";
/*      */   }
/*      */   public static String getType(List<Unit> units) {
/* 1098 */     for (Unit unit : units) {
/*      */       
/* 1100 */       String sig = unit.toString();
/* 1101 */       if (sig.contains("()")) {
/*      */         continue;
/*      */       }
/* 1104 */       if (sig.contains("getInt("))
/* 1105 */         return "integer"; 
/* 1106 */       if (sig.contains("getFloat("))
/* 1107 */         return "float"; 
/* 1108 */       if (sig.contains("getColor("))
/* 1109 */         return "integer"; 
/* 1110 */       if (sig.contains("getString(") || sig.contains("getText("))
/* 1111 */         return "string"; 
/* 1112 */       if (sig.contains("getColorStateList("))
/* 1113 */         return "color_state_list"; 
/* 1114 */       if (sig.contains("getDimension(") && !sig.contains("getDimension(android.util.DisplayMetrics)"))
/* 1115 */         return "float_dimension"; 
/* 1116 */       if (sig.contains("hasValue(") || sig.contains("hasValueOrEmpty("))
/* 1117 */         return "has_value"; 
/* 1118 */       if (sig.contains("getDimensionPixelOffset(") || sig
/* 1119 */         .contains("getDimensionPixelSize("))
/* 1120 */         return "integer_dimension"; 
/* 1121 */       if (sig.contains("getFraction(") && !sig.contains("getFraction(float,float)"))
/* 1122 */         return "fraction"; 
/* 1123 */       if (sig.contains("getDrawable("))
/* 1124 */         return "drawable"; 
/* 1125 */       if (sig.contains("getBoolean("))
/* 1126 */         return "boolean"; 
/* 1127 */       if (sig.contains("getComplexColor("))
/* 1128 */         return "gradient_color"; 
/* 1129 */       if (sig.contains("peekValue("))
/* 1130 */         return "peek_value"; 
/* 1131 */       if (sig.contains("getAttributeFloatValue("))
/* 1132 */         return "float_dimension"; 
/* 1133 */       if (sig.contains("getAttributeIntValue("))
/* 1134 */         return "integer"; 
/* 1135 */       if (sig.contains("getAttributeValue("))
/* 1136 */         return "string"; 
/* 1137 */       if (sig.contains("getAttributeBooleanValue("))
/* 1138 */         return "boolean"; 
/* 1139 */       if (sig.contains("getResourceId(")) {
/* 1140 */         return "resource_id";
/*      */       }
/*      */     } 
/* 1143 */     return "";
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean checkForSourceUnit(Unit unit) {
/* 1149 */     Stmt stmt = (Stmt)unit;
/* 1150 */     if (stmt.containsInvokeExpr()) {
/* 1151 */       SootMethod sMethod = stmt.getInvokeExpr().getMethod();
/* 1152 */       if (sMethod.getSignature().contains("obtainStyledAttributes(") || (sMethod
/* 1153 */         .getSignature().contains("getInt(") && sMethod.getSignature().contains("TypedArray")) || (sMethod
/* 1154 */         .getSignature().contains("getFloat(") && sMethod.getSignature().contains("TypedArray")) || (sMethod
/* 1155 */         .getSignature().contains("getFloat(") && stmt.toString().contains("android.util.TypedValue")) || sMethod
/* 1156 */         .getSignature().contains("peekValue(") || (sMethod
/* 1157 */         .getSignature().contains("getColor(") && sMethod.getSignature().contains("TypedArray")) || sMethod
/* 1158 */         .getSignature().contains("getComplexColor(") || sMethod
/* 1159 */         .getSignature().contains("hasValueOrEmpty(") || sMethod
/* 1160 */         .getSignature().contains("hasValue(") || (sMethod
/* 1161 */         .getSignature().contains("getString(") && sMethod.getSignature().contains("TypedArray")) || sMethod
/* 1162 */         .getSignature().contains("getDimensionPixelOffset(") || (sMethod
/* 1163 */         .getSignature().contains("getDimension(") && sMethod.getSignature().contains("TypedArray")) || sMethod
/* 1164 */         .getSignature().contains("getResourceId(") || sMethod
/* 1165 */         .getSignature().contains("getColorStateList(") || sMethod
/* 1166 */         .getSignature().contains("getComplexColor(") || (sMethod
/* 1167 */         .getSignature().contains("getFraction(") && sMethod.getSignature().contains("TypedArray")) || sMethod
/* 1168 */         .getSignature().contains("getText(") || (sMethod
/* 1169 */         .getSignature().contains("getDrawable(") && sMethod.getSignature().contains("TypedArray")) || (sMethod
/* 1170 */         .getSignature().contains("getBoolean(") && sMethod.getSignature().contains("TypedArray"))) {
/* 1171 */         return true;
/*      */       }
/*      */     } 
/* 1174 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void scanForSourceCallGraph(SootMethod tgtMtd, SootMethod sourceMtd, Stack<SootMethod> stk, List<Unit> sourceUnits, int depth, List<Pair<Unit, SootMethod>> potentialSources, int apiLevel, String plusOrMinus) {
/* 1181 */     FrameworkCallGraphInApiLevel fcg = FrameworkCallGraph.getFCG(apiLevel);
/*      */     
/* 1183 */     Set<Edge> edges = (Set<Edge>)fcg.tgtMethod2edges.get(tgtMtd.getSignature());
/* 1184 */     if (edges == null) {
/*      */       return;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1190 */     for (Edge edge : edges) {
/* 1191 */       String srcSig = edge.srcSig, tgtSig = edge.tgtSig;
/* 1192 */       if (tgtMtd.getSignature().equals(tgtSig)) {
/* 1193 */         SootMethod srcMtd = fcg.getMethod(srcSig);
/* 1194 */         UnitPatchingChain unitPatchingChain = srcMtd.getActiveBody().getUnits();
/* 1195 */         for (Unit srcUnit : unitPatchingChain) {
/* 1196 */           Stmt srcStmt = (Stmt)srcUnit;
/* 1197 */           if (srcStmt.containsInvokeExpr()) {
/* 1198 */             SootMethod uMtd = srcStmt.getInvokeExpr().getMethod();
/* 1199 */             if (uMtd.getSignature().equals(tgtMtd.getSignature())) {
/* 1200 */               int uLineNum = srcStmt.getJavaSourceStartLineNumber();
/* 1201 */               List<Unit> uUnits = getStmtsByLineNum(srcMtd, uLineNum);
/* 1202 */               scanForSourceInternal(srcMtd, sourceMtd, stk, sourceUnits, uUnits, depth, potentialSources, apiLevel, plusOrMinus);
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/androidsourcecode/branchedflow/MainSourceSinkScan.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */