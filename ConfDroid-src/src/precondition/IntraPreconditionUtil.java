/*     */ package precondition;
/*     */ import androidsourcecode.AndroidSourceCodeUtil;
/*     */ import androidsourcecode.branchedflow.MainSourceSinkScan;
/*     */ import callgraph.graph.Edge;
/*     */ import callgraph.graph.FrameworkCallGraph;
import callgraph.graph.FrameworkCallGraphInApiLevel;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.util.*;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ import precondition.entity.Precondition;
/*     */ import precondition.entity.PreconditionItem;
/*     */ import soot.SootMethod;
/*     */ import soot.Unit;
/*     */ import soot.UnitBox;
/*     */ import soot.UnitPatchingChain;
/*     */ import soot.Value;
/*     */ import soot.ValueBox;
/*     */ import soot.jimple.AssignStmt;
/*     */ import soot.jimple.BinopExpr;
import soot.jimple.IfStmt;
/*     */ import soot.jimple.Stmt;
/*     */ import soot.jimple.internal.JLookupSwitchStmt;
/*     */ import soot.jimple.internal.JTableSwitchStmt;
/*     */ import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.graph.ExceptionalUnitGraph;
/*     */ import soot.toolkits.graph.InverseGraph;
import soot.toolkits.scalar.Pair;

/*     */
/*     */ public class IntraPreconditionUtil {
/*  33 */   public static List<Precondition> preconditionSet = new ArrayList<>();
/*     */ 
/*     */   
/*     */   public static void getIntraPreconditionSimple(SootMethod mtd, Unit unit, int lineNumber, String stmt, String lastStmt, String type, int apiLevel) {
/*  37 */     String attr = lastStmt.split(" ")[1];
/*  38 */     attr = attr.substring(0, attr.length() - 1);
/*  39 */     if (attr.contains("R.styleable")) {
/*  40 */       attr = attr.substring(attr.indexOf("R.styleable"));
/*     */     }
/*  42 */     type = MainSourceSinkScan.getType(stmt);
/*  43 */     List<Unit> units = new ArrayList<>();
/*  44 */     units.add(unit);
/*  45 */     Precondition precondition = new Precondition(attr, lineNumber, units, mtd, type, apiLevel);
/*  46 */     System.out.println("simple precondition: " + precondition);
/*  47 */     preconditionSet.add(precondition);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void getIntraPrecondition(SootMethod mtd, List<Unit> unitInLineNumber, int lineNumberr, int apiLevel, Precondition precondition, List<SootMethod> visitedMtd, int depth) {
/*  54 */     boolean continueFlag = (precondition == null);
/*  55 */     if (depth >= 2) {
/*  56 */       if (precondition.attr != null && precondition.attr.contains("_") && precondition.type
/*  57 */         .length() > 0 && !precondition.type.equals("has_value") && 
/*  58 */         !preconditionSet.contains(precondition)) {
/*  59 */         preconditionSet.add(precondition);
/*  60 */         System.out.println("precondition: " + precondition);
/*  61 */         System.out.println("size: " + preconditionSet.size());
/*     */       } 
/*     */ 
/*     */ 
/*     */
/*     */       return;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  71 */     ExceptionalUnitGraph exceptionalUnitGraph = new ExceptionalUnitGraph(mtd.retrieveActiveBody());
/*  72 */     if (visitedMtd.contains(mtd)) {
/*  73 */       if (precondition.attr != null && precondition.attr.contains("_") && precondition.type
/*  74 */         .length() > 0 && !precondition.type.equals("has_value") && 
/*  75 */         !preconditionSet.contains(precondition)) {
/*  76 */         Precondition clonePrecondition = precondition.clone();
/*  77 */         preconditionSet.add(clonePrecondition);
/*  78 */         System.out.println("precondition: " + precondition);
/*  79 */         System.out.println("size: " + preconditionSet.size());
/*     */       } 
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/*  85 */     visitedMtd.add(mtd);
/*     */ 
/*     */     
/*  88 */     Set<Unit> visited = new HashSet<>();
/*  89 */     Queue<Unit> stkk = new LinkedList<>();
/*  90 */     Map<String, Boolean> ret = new HashMap<>();
/*  91 */     Set<Pair<String, Unit>> conditionUnitPairs = new HashSet<>();
/*  92 */     int ln = -1;
/*  93 */     for (Unit unit : unitInLineNumber) {
/*  94 */       ln = unit.getJavaSourceStartLineNumber();
/*  95 */       stkk.add(unit);
/*     */     } 
/*     */     
/*  98 */     String s = null;
/*     */
    /* 100 */
    try {
        s = AndroidSourceCodeUtil.readSourceCodeByLineNumber(apiLevel, mtd, ln);
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    }
    /* 101 */
    /* 104 */     String attr = MainSourceSinkScan.getAttribute(s, apiLevel, mtd, ln);
/* 105 */     if (attr == null) {
/*     */       return;
/*     */     }
/*     */     
/* 109 */     String type = MainSourceSinkScan.getType(s);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 116 */     if (type.length() == 0) {
/*     */
        /* 118 */
        try {
            s = AndroidSourceCodeUtil.readSourceCodeByLineNumber(apiLevel, mtd, ln);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        /*     */
        /* 120 */
        if (s.contains("R.attr")) {
        /* 121 */           for (Unit unit1 : unitInLineNumber) {
        /* 122 */             boolean branch = false;
        /* 123 */             boolean got = false;
        /* 124 */             if (unit1.branches()) {
        /* 125 */               branch = true;
        /* 126 */             } else if (unit1.toString().startsWith("goto")) {
        /* 127 */               got = true;
        /*     */             }
        /* 129 */             if (branch) {
        /* 130 */               UnitPatchingChain unitPatchingChain = mtd.getActiveBody().getUnits();
        /* 131 */               boolean bool = false;
        /* 132 */               for (Unit unit2 : unitPatchingChain) {
        /* 133 */                 if (unit1.toString().equals(unit2.toString())) {
        /* 134 */                   bool = true;
        /*     */                 }
        /* 136 */                 if (unit2.toString().startsWith("goto ") && bool) {
        /*     */                   break;
        /*     */                 }
        /* 139 */                 if (bool &&
        /* 140 */                   MainSourceSinkScan.getType(unit2).length() > 0) {
        /* 141 */                   type = MainSourceSinkScan.getType(unit2);
        /*     */
        /*     */                   break;
        /*     */                 }
        /*     */               }
        /*     */               break;
        /*     */             }
        /* 148 */             if (got) {
        /* 149 */               UnitPatchingChain unitPatchingChain = mtd.getActiveBody().getUnits();
        /* 150 */               boolean bool = false;
        /* 151 */               for (Unit unit2 : unitPatchingChain) {
        /* 152 */                 if (unit2.toString().startsWith("goto ") && bool) {
        /*     */                   break;
        /*     */                 }
        /* 155 */                 if (unit1.toString().equals(unit2.toString())) {
        /* 156 */                   bool = true;
        /*     */                 }
        /* 158 */                 if (bool) {
        /* 159 */                   String subType = MainSourceSinkScan.getType(unit2);
        /* 160 */                   if (subType.length() > 0) {
        /* 161 */                     type = subType;
        /*     */                     break;
        /*     */                   }
        /*     */                 }
        /*     */               }
        /*     */               break;
        /*     */             }
        /*     */           }
        /*     */         }
        /* 170 */
        /*     */     }
/*     */     
/* 175 */     if (precondition == null) {
/* 176 */       precondition = new Precondition(attr, ln, unitInLineNumber, mtd, type, apiLevel);
/*     */     }
/* 178 */     while (!stkk.isEmpty()) {
/* 179 */       Unit top = stkk.poll();
/*     */       
/* 181 */       if (visited.contains(top)) {
/*     */         continue;
/*     */       }
/* 184 */       visited.add(top);
/* 185 */       List<Unit> succList = exceptionalUnitGraph.getPredsOf(top);
/* 186 */       for (Unit succ : succList) {
/* 187 */         if (succ.branches()) {
/* 188 */           if (succ instanceof IfStmt) {
/* 189 */             IfStmt succIf = (IfStmt)succ;
/* 190 */             String condition = succIf.getCondition().toString();
/* 191 */             conditionUnitPairs.add(new Pair(condition, succ));
/* 192 */             if (top.equals(succIf.getTarget())) {
/* 193 */               if (!ret.containsKey(condition)) {
/* 194 */                 ret.put(condition, Boolean.valueOf(true));
/* 195 */               } else if (!((Boolean)ret.get(condition)).booleanValue()) {
/* 196 */                 ret.remove(condition);
/*     */               }
/*     */             
/* 199 */             } else if (!ret.containsKey(condition)) {
/* 200 */               ret.put(condition, Boolean.valueOf(false));
/* 201 */             } else if (((Boolean)ret.get(condition)).booleanValue()) {
/* 202 */               ret.remove(condition);
/*     */             }
/*     */           
/* 205 */           } else if (succ instanceof JLookupSwitchStmt) {
/* 206 */             JLookupSwitchStmt succSwitch = (JLookupSwitchStmt)succ;
/* 207 */             for (int i = 0; i < succSwitch.getUnitBoxes().size() - 1; i++) {
/* 208 */               UnitBox units = succSwitch.getUnitBoxes().get(i);
/* 209 */               Unit u = units.getUnit();
/* 210 */               if (u.getJavaSourceStartLineNumber() == lineNumberr) {
/* 211 */                 conditionUnitPairs.add(new Pair(succSwitch.getKeyBox().getValue().toString() + " == " + succSwitch
/* 212 */                       .getLookupValue(i), u));
/* 213 */                 ret.put(succSwitch.getKeyBox().getValue().toString() + " == " + succSwitch
/* 214 */                     .getLookupValue(i), Boolean.valueOf(true));
/*     */               } 
/*     */             } 
/* 217 */           } else if (succ instanceof JTableSwitchStmt) {
/* 218 */             JTableSwitchStmt succSwitch = (JTableSwitchStmt)succ;
/* 219 */             for (int i = succSwitch.getLowIndex(); i <= succSwitch.getHighIndex(); i++) {
/* 220 */               Unit u = succSwitch.getTarget(i - succSwitch.getLowIndex());
/* 221 */               int uln = u.getJavaSourceStartLineNumber();
/* 222 */               List<Unit> unitList = MainSourceSinkScan.getUnitsByLineNumber(mtd, uln);
/* 223 */               if (u.getJavaSourceStartLineNumber() == lineNumberr || u
/* 224 */                 .getJavaSourceStartLineNumber() == lineNumberr - 1) {
/* 225 */                 conditionUnitPairs.add(new Pair(succSwitch.getKeyBox().getValue().toString() + " == " + i, u));
/*     */                 
/* 227 */                 ret.put(succSwitch.getKeyBox().getValue().toString() + " == " + i, 
/* 228 */                     Boolean.valueOf(true));
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 240 */         if (!visited.contains(succ)) {
/* 241 */           stkk.add(succ);
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 248 */     if (ret.size() > 0)
/*     */     {
/* 250 */       for (Pair<String, Unit> conditionUnitPair : conditionUnitPairs) {
/* 251 */         if (ret.containsKey(conditionUnitPair.getO1())) {
/* 252 */           boolean branchValue = ((Boolean)ret.get(conditionUnitPair.getO1())).booleanValue();
/* 253 */           List<PreconditionItem> preconditionItems = parseConditionAttr((String)conditionUnitPair.getO1(), (Unit)conditionUnitPair.getO2(), mtd, apiLevel, branchValue);
/* 254 */           precondition.addAll(preconditionItems);
/* 255 */           precondition.removeLine(precondition.lineNumber);
/* 256 */           if (precondition.attr != null && precondition.attr.contains("_") && precondition.type
/* 257 */             .length() > 0 && !precondition.type.equals("has_value") && 
/* 258 */             !preconditionSet.contains(precondition)) {
/* 259 */             Precondition clonePrecondition = precondition.clone();
/* 260 */             preconditionSet.add(clonePrecondition);
/* 261 */             System.out.println("precondition: " + precondition);
/* 262 */             System.out.println("size: " + preconditionSet.size());
/*     */           } 
/*     */           
/* 265 */           precondition.removeAll(preconditionItems);
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 271 */     FrameworkCallGraphInApiLevel fcg = FrameworkCallGraph.getFCG(apiLevel);
/* 272 */     Set<Edge> edges = (Set<Edge>)fcg.tgtMethod2edges.get(mtd.getSignature());
/* 273 */     boolean flag = false;
/* 274 */     if (edges != null && continueFlag) {
/* 275 */       for (Edge edge : edges) {
/* 276 */         String srcSig = edge.srcSig;
/*     */         
/* 278 */         SootMethod tgtMtd = fcg.getMethod(srcSig);
/* 279 */         if (tgtMtd.getDeclaringClass().getPackageName().startsWith("android.widget") || tgtMtd
/* 280 */           .getDeclaringClass().getPackageName().startsWith("android.graphics.") || tgtMtd
/* 281 */           .getDeclaringClass().getPackageName().startsWith("android.content") || tgtMtd
/* 282 */           .getDeclaringClass().getPackageName().startsWith("android.view")) {
/*     */           
/* 284 */           UnitPatchingChain unitPatchingChain = tgtMtd.getActiveBody().getUnits();
/* 285 */           for (Unit unit : unitPatchingChain) {
/* 286 */             int uLineNum = unit.getJavaSourceStartLineNumber();
/* 287 */             Stmt stmt = (Stmt)unit;
/*     */             
/* 289 */             if (stmt.toString().contains(mtd.getSignature())) {
/* 290 */               flag = true;
/* 291 */               getIntraPrecondition(tgtMtd, MainSourceSinkScan.getUnitsByLineNumber(tgtMtd, uLineNum), uLineNum, apiLevel, precondition, visitedMtd, depth + 1);
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 299 */     if (!flag) {
/* 300 */       String attrr = precondition.attr;
/* 301 */       String typee = precondition.type;
/* 302 */       if (attrr != null && attrr.contains("_") && typee.length() > 0 && !typee.equals("has_value") && 
/* 303 */         !preconditionSet.contains(precondition)) {
/* 304 */         Precondition clonePrecondition = precondition.clone();
/* 305 */         preconditionSet.add(clonePrecondition);
/* 306 */         System.out.println("precondition: " + precondition);
/* 307 */         System.out.println("size: " + preconditionSet.size());
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static List<PreconditionItem> parseConditionAttr(String condition, Unit succ, SootMethod mtd, int apiLevel, boolean branchValue) {
/*     */     AssignStmt assignStmt = null;
/* 316 */     List<PreconditionItem> itemList = new ArrayList<>();
/* 317 */     InverseGraph<Unit> graph = new InverseGraph((DirectedGraph)new ExceptionalUnitGraph(mtd.getActiveBody()));
/*     */ 
/*     */     
/* 320 */     String conditionExpr = "";
/* 321 */     if (succ instanceof IfStmt) {
/* 322 */       IfStmt ifStmt = (IfStmt)succ;
/* 323 */       Value ifCondition = ifStmt.getCondition();
/* 324 */       if (ifCondition instanceof BinopExpr) {
/* 325 */         conditionExpr = ((BinopExpr)ifCondition).getOp1().toString();
/*     */       }
/*     */     } else {
/* 328 */       conditionExpr = condition.split(" ")[0];
/*     */     } 
/*     */     
/* 331 */     if (conditionExpr.length() <= 0) {
/* 332 */       return itemList;
/*     */     }
/*     */ 
/*     */     
/* 336 */     UnitPatchingChain unitPatchingChain = mtd.getActiveBody().getUnits();
/* 337 */     Unit targetUnit = null;
/* 338 */     for (Unit u : unitPatchingChain) {
/* 339 */       if (u instanceof AssignStmt) {
/* 340 */         AssignStmt assignStmt1 = (AssignStmt)u;
/* 341 */         if (assignStmt1.getLeftOp().toString().equals(conditionExpr)) {
/* 342 */           assignStmt = assignStmt1;
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     } 
/* 348 */     if (assignStmt != null) {
/* 349 */       Stack<Unit> stk = new Stack<>();
/* 350 */       List ret = new ArrayList();
/* 351 */       Set<Unit> visited = new HashSet<>();
/* 352 */       Set<String> variableSet = new HashSet<>();
/* 353 */       variableSet.add(conditionExpr);
/* 354 */       stk.push(assignStmt);
/* 355 */       while (!stk.isEmpty()) {
/* 356 */         Unit top = stk.pop();
/* 357 */         if (visited.contains(top)) {
/*     */           continue;
/*     */         }
/* 360 */         visited.add(top);
/* 361 */         List<ValueBox> defBoxes = top.getDefBoxes();
/* 362 */         List<ValueBox> useBoxes = top.getUseBoxes();
/*     */         
/* 364 */         for (ValueBox def : defBoxes) {
/* 365 */           boolean hasDef = false;
/* 366 */           for (String v : variableSet) {
/* 367 */             if (def.getValue().toString().equals(v)) {
/* 368 */               hasDef = true;
/*     */               break;
/*     */             } 
/*     */           } 
/* 372 */           if (hasDef) {
/* 373 */             for (ValueBox variableValueBox : useBoxes) {
/* 374 */               String variableStr = variableValueBox.getValue().toString();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 383 */               variableSet.add(variableStr);
/*     */             } 
/*     */           }
/*     */         } 
/*     */ 
/*     */ 
/*     */         
/* 390 */         if (top instanceof AssignStmt) {
/* 391 */           String type = MainSourceSinkScan.getType(top);
/* 392 */           String variableStr = ((AssignStmt)top).getLeftOp().toString();
/* 393 */           if (variableSet.contains(variableStr) && type.length() > 0) {
/*     */             
/*     */
                    /*     */
                    /* 397 */
                    String srcCode = null;
                    try {
                        srcCode = AndroidSourceCodeUtil.readSourceCodeByLineNumber(apiLevel, mtd, top
                        /* 398 */                   .getJavaSourceStartLineNumber());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    /* 399 */
                    String attr = MainSourceSinkScan.getAttribute(srcCode, apiLevel, mtd, top
                    /* 400 */                   .getJavaSourceStartLineNumber());
                    /*     */
                    /*     */
                    /* 403 */
                    PreconditionItem item = new PreconditionItem(top, mtd, attr, condition, null, branchValue);
                    /* 404 */
                    if (attr != null && !attr.startsWith("R.styleable.Theme")) {
                    /* 405 */                 itemList.add(item);
                    /*     */               }
                    /* 407 */
                    /*     */           }
/*     */         } 
/*     */         
/* 413 */         List<Unit> succsOf = graph.getSuccsOf(top);
/*     */         
/* 415 */         for (Unit sunits : succsOf) {
/* 416 */           stk.push(sunits);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 421 */     return itemList;
/*     */   }
/*     */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/precondition/IntraPreconditionUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */