/*     */ package lu.uni.tsopen.diffAnalysis;
/*     */ import com.microsoft.z3.*;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ import java.util.*;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ import com.microsoft.z3.Context;
import lu.uni.tsopen.symbolicExecution.symbolicValues.SymbolicValue;
/*     */ import org.apache.commons.lang3.StringUtils;
import org.javatuples.Septet;
/*     */ import org.javatuples.Triplet;
/*     */ import org.logicng.formulas.Formula;
/*     */ import org.logicng.formulas.Literal;
/*     */ import soot.*;
/*     */
/*     */
/*     */
/*     */
/*     */ import soot.jimple.AssignStmt;
/*     */ import soot.jimple.Constant;
/*     */ import soot.jimple.Stmt;
import soot.jimple.infoflow.solver.cfg.InfoflowCFG;
import soot.toolkits.graph.CompleteUnitGraph;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.SimpleLocalDefs;
import soot.util.Chain;

/*     */ 
/*     */ public class DiffAnalysis {
/*  32 */   private Map<String, Set<Value>> clsMap = new HashMap<>(); private int apiLevel;
/*  33 */   private Map<String, Set<Integer>> clsAttrMap = new HashMap<>();
/*     */   
/*     */   private List<Triplet<Unit, Formula, List<Septet<List<SymbolicValue>, List<SymbolicValue>, List<SymbolicValue>, Value, Value, Constant, Literal>>>> interestingFormulae;
/*  36 */   private Map<String, BoolExpr> attrExprMap = new HashMap<>();
/*     */   
/*     */   private InfoflowCFG icfg;
/*     */   
/*     */   private Context context;
/*     */   
/*     */   public DiffAnalysis(List<Triplet<Unit, Formula, List<Septet<List<SymbolicValue>, List<SymbolicValue>, List<SymbolicValue>, Value, Value, Constant, Literal>>>> interestingFormulae, int apiLevel, InfoflowCFG icfg) {
/*  43 */     this.interestingFormulae = interestingFormulae;
/*  44 */     this.apiLevel = apiLevel;
/*  45 */     this.icfg = icfg;
/*  46 */     this.context = new Context();
/*     */   }
/*     */ 
/*     */   
/*     */   public void generateRules() {
/*  51 */     for (SootClass cls : Scene.v().getApplicationClasses()) {
/*  52 */       System.out.println("=== start analyzing the class: " + cls.toString());
/*  53 */       buildAttributeList(cls);
/*     */     } 
/*     */ 
/*     */     
/*  57 */     processInterestingFormulaeByZ3();
/*  58 */     System.out.println("interestingFormulae = " + this.interestingFormulae.size());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void processInterestingFormulaeByZ3() {
/*  73 */     for (Triplet<Unit, Formula, List<Septet<List<SymbolicValue>, List<SymbolicValue>, List<SymbolicValue>, Value, Value, Constant, Literal>>> triplet : this.interestingFormulae)
/*     */     {
/*  75 */       parseFormulaToZ3Constraint((Unit)triplet.getValue0(), (Formula)triplet.getValue1(), (List<Septet<List<SymbolicValue>, List<SymbolicValue>, List<SymbolicValue>, Value, Value, Constant, Literal>>)triplet.getValue2());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void parseFormulaToZ3Constraint(Unit value0, Formula value1, List<Septet<List<SymbolicValue>, List<SymbolicValue>, List<SymbolicValue>, Value, Value, Constant, Literal>> value2) {
/*  82 */     System.out.println("value0 = " + value0 + " " + value0.getJavaSourceStartLineNumber());
/*  83 */     Context context1 = this.context;
/*  84 */     List<Symbol> symbols = new ArrayList<>();
/*  85 */     SortedSet<Literal> literals = value1.literals();
/*  86 */     Map<Literal, BoolExpr> literalBoolExprMap = new HashMap<>();
/*  87 */     for (Literal l : literals) {
/*     */ 
/*     */       
/*  90 */       List<Septet<List<SymbolicValue>, List<SymbolicValue>, List<SymbolicValue>, Value, Value, Constant, Literal>> septetsInValue2 = findSeptetsInList(l, value2);
/*  91 */       Set<Symbol> symbols1 = createSymbols(context1, septetsInValue2);
/*     */       
/*  93 */       symbols.addAll(symbols1);
/*  94 */       StringSymbol stringSymbol = context1.mkSymbol("para1");
/*  95 */       symbols.add(stringSymbol);
/*     */       
/*  97 */       if (!literalBoolExprMap.containsKey(l)) {
/*  98 */         literalBoolExprMap.put(l, context1.mkFalse());
/*     */       }
/*     */ 
/*     */       
/* 102 */       for (Septet<List<SymbolicValue>, List<SymbolicValue>, List<SymbolicValue>, Value, Value, Constant, Literal> s : septetsInValue2) {
/* 103 */         BoolExpr newBoolExprs = createBoolExprsForSeptet(context1, s, symbols1, value0, (Symbol)stringSymbol);
/* 104 */         context1.mkOr(new BoolExpr[] { literalBoolExprMap.get(l), newBoolExprs });
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 109 */     System.out.println("end value0");
/* 110 */     System.out.println();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private BoolExpr createBoolExprsForSeptet(Context context, Septet<List<SymbolicValue>, List<SymbolicValue>, List<SymbolicValue>, Value, Value, Constant, Literal> s, Set<Symbol> symbolss, Unit value0, Symbol para1Symbol) {
/* 116 */     BoolExpr boolExpr = context.mkTrue();
/*     */     
/* 118 */     List<Symbol> symbols1 = new ArrayList<>(), symbols2 = new ArrayList<>();
/* 119 */     IntNum num1 = null, num2 = null;
/* 120 */     IntSymbol num1Symbol = null, num2Symbol = null;
/* 121 */     if (s.getValue1() != null) {
/* 122 */       symbols1 = parseSeptetValue(context, (List<SymbolicValue>)s.getValue1());
/*     */     }
/* 124 */     if (s.getValue2() != null) {
/* 125 */       symbols2 = parseSeptetValue(context, (List<SymbolicValue>)s.getValue2());
/*     */     }
/*     */     
/* 128 */     Stmt stmt = (Stmt)value0;
/* 129 */     BoolExpr value0Expr = null;
/* 130 */     String variableStr = stmt.getInvokeExpr().getArg(0).toString();
/* 131 */     Value value3 = (Value)s.getValue3(), value4 = (Value)s.getValue4();
/* 132 */     String symbolName = "";
/*     */     
/* 134 */     System.out.println("s.getValue3().toString() = " + ((Value)s.getValue3()).toString());
/* 135 */     System.out.println("variableStr = " + variableStr);
/* 136 */     boolean flag3 = false;
/* 137 */     if (((Value)s.getValue3()).toString().equals(variableStr)) {
/* 138 */       flag3 = true;
/*     */     }
/* 140 */     if (parseSeptetOp(context, (Value)s.getValue3()) != null) {
/* 141 */       num1 = parseSeptetOp(context, (Value)s.getValue3());
/*     */     }
/*     */     
/* 144 */     System.out.println("s.getValue4().toString() = " + ((Value)s.getValue4()).toString());
/* 145 */     System.out.println("variableStr = " + variableStr);
/* 146 */     boolean flag4 = false;
/* 147 */     if (((Value)s.getValue4()).toString().equals(variableStr)) {
/* 148 */       flag4 = true;
/*     */     }
/* 150 */     if (parseSeptetOp(context, (Value)s.getValue4()) != null) {
/* 151 */       num2 = parseSeptetOp(context, (Value)s.getValue4());
/*     */     }
/*     */     
/* 154 */     if (value0Expr != null) {
/* 155 */       System.out.println("value0Expr = " + value0Expr);
/*     */     }
/*     */     
/* 158 */     if (num1 != null) {
/* 159 */       num1Symbol = context.mkSymbol(num1.getInt());
/* 160 */       if (num1Symbol != null) {
/* 161 */         symbols1.add(num1Symbol);
/*     */       }
/*     */     } 
/*     */     
/* 165 */     if (num2 != null) {
/* 166 */       num2Symbol = context.mkSymbol(num2.getInt());
/* 167 */       if (num2Symbol != null) {
/* 168 */         symbols2.add(num2Symbol);
/*     */       }
/*     */     } 
/*     */     
/* 172 */     System.out.println("s.getValue6() = " + s.getValue6());
/* 173 */     System.out.println("symbols1 = " + symbols1);
/* 174 */     System.out.println("symbols2 = " + symbols2);
/*     */ 
/*     */     
/* 177 */     String operator = parseOperatorFromLiteral((Literal)s.getValue6());
/* 178 */     System.out.println("operator = " + operator);
/*     */     
/* 180 */     BoolExpr attrExpr = context.mkFalse();
/* 181 */     if (StringUtils.isNumeric(variableStr)) {
/*     */ 
/*     */       
/* 184 */       attrExpr = context.mkOr(new BoolExpr[] { attrExpr, context.mkEq((Expr)context.mkIntConst(para1Symbol), (Expr)context.mkInt(variableStr)) });
/*     */     } else {
/*     */       
/* 187 */       if (flag3)
/*     */       {
/* 189 */         for (SymbolicValue sv : s.getValue1()) {
/* 190 */           Symbol symbol = findSymbol(sv, symbolss);
/* 191 */           if (symbol != null) {
/* 192 */             attrExpr = context.mkOr(new BoolExpr[] { context.mkEq((Expr)context.mkIntConst(symbol), (Expr)context.mkIntConst(para1Symbol)), attrExpr });
/*     */           }
/*     */         } 
/*     */       }
/* 196 */       if (flag4)
/*     */       {
/* 198 */         for (SymbolicValue sv : s.getValue2()) {
/* 199 */           Symbol symbol = findSymbol(sv, symbolss);
/* 200 */           if (symbol != null) {
/* 201 */             attrExpr = context.mkOr(new BoolExpr[] { context.mkEq((Expr)context.mkIntConst(symbol), (Expr)context.mkIntConst(para1Symbol)), attrExpr });
/*     */           }
/*     */         } 
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 209 */     BoolExpr constraint = generateConstraintBoolExpr(context, symbols1, symbols2, operator);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 215 */     Solver solver = context.mkSolver();
/* 216 */     BoolExpr boolExpr1 = context.mkAnd(new BoolExpr[] { constraint, attrExpr });
/* 217 */     solver.add(new BoolExpr[] { boolExpr1 });
/* 218 */     System.out.println("solver.check() = " + solver.check());
/*     */     
/* 220 */     if (solver.check().toString().equals("SATISFIABLE")) {
/* 221 */       Model model = solver.getModel();
/* 222 */       for (int i = 0; i < 100; i++) {
/* 223 */         BoolExpr hugulu = context.mkEq((Expr)context.mkIntConst(para1Symbol), (Expr)context.mkInt(i + 1));
/* 224 */         if (model.eval((Expr)hugulu, true).toString().equals("true")) {
/* 225 */           System.out.println("i hugulu = " + i);
/* 226 */           System.out.println("value0.getJavaSourceStartLineNumber() = " + value0.getJavaSourceStartLineNumber() + " " + this.icfg.getMethodOf(value0));
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 261 */     System.out.println("=== end\n");
/*     */     
/* 263 */     return boolExpr;
/*     */   }
/*     */   
/*     */   private void dealWithLineStrAndModel(String lineStr, BoolExpr expr, Context context) {
/* 267 */     if (!this.attrExprMap.containsKey(lineStr)) {
/* 268 */       this.attrExprMap.put(lineStr, expr);
/*     */       return;
/*     */     } 
/* 271 */     BoolExpr newExpr = this.attrExprMap.get(lineStr);
/* 272 */     System.out.println("expr = " + expr);
/* 273 */     System.out.println("newExpr = " + newExpr);
/* 274 */     newExpr = context.mkOr(new BoolExpr[] { expr, newExpr });
/* 275 */     this.attrExprMap.put(lineStr, (BoolExpr)newExpr.simplify());
/* 276 */     System.out.println("adding attrExprMap");
/* 277 */     System.out.println("lineStr = " + lineStr);
/* 278 */     System.out.println("newExpr = " + newExpr.simplify());
/*     */   }
/*     */   
/*     */   private Symbol findSymbol(SymbolicValue sv, Set<Symbol> symbols) {
/* 282 */     for (Symbol symbol : symbols) {
/* 283 */       if (("x_" + sv.toString()).equals(symbol.toString())) {
/* 284 */         return symbol;
/*     */       }
/*     */     } 
/* 287 */     return null;
/*     */   }
/*     */   
/*     */   private BoolExpr generateConstraintBoolExpr(Context context, List<Symbol> symbols1, List<Symbol> symbols2, String operator) {
/* 291 */     BoolExpr boolExpr = context.mkFalse();
/*     */     
/* 293 */     for (Symbol s1 : symbols1) {
/* 294 */       for (Symbol s2 : symbols2) {
/* 295 */         BoolExpr subExpr = generateSubConstraintBoolExpr(context, s1, s2, operator);
/* 296 */         boolExpr = context.mkOr(new BoolExpr[] { subExpr, boolExpr });
/*     */       } 
/*     */     } 
/* 299 */     return boolExpr;
/*     */   }
/*     */   private BoolExpr generateSubConstraintBoolExpr(Context context, Symbol s1, Symbol s2, String operator) {
/*     */     IntNum intNum = null;
/* 303 */     BoolExpr boolExpr = context.mkTrue();
/* 304 */     ArithExpr s1ae = (ArithExpr)context.mkConst(s1, (Sort)context.mkIntSort());
/* 305 */     ArithExpr s2ae = null;
/* 306 */     if (StringUtils.isNumeric(s2.toString())) {
/* 307 */       intNum = context.mkInt(Integer.parseInt(s2.toString()));
/*     */     }
/*     */     
/* 310 */     if (intNum == null) {
/* 311 */       return boolExpr;
/*     */     }
/*     */     
/* 314 */     switch (operator) {
/*     */       case "==":
/* 316 */         boolExpr = context.mkEq((Expr)s1ae, (Expr)intNum);
/*     */         break;
/*     */       case "!=":
/* 319 */         boolExpr = context.mkNot(context.mkEq((Expr)s1ae, (Expr)intNum));
/*     */         break;
/*     */       case ">":
/* 322 */         boolExpr = context.mkGt(s1ae, (ArithExpr)intNum);
/*     */         break;
/*     */       case "<":
/* 325 */         boolExpr = context.mkLt(s1ae, (ArithExpr)intNum);
/*     */         break;
/*     */       case ">=":
/* 328 */         boolExpr = context.mkGe(s1ae, (ArithExpr)intNum);
/*     */         break;
/*     */       case "<=":
/* 331 */         boolExpr = context.mkLe(s1ae, (ArithExpr)intNum);
/*     */         break;
/*     */     } 
/*     */ 
/*     */     
/* 336 */     return boolExpr;
/*     */   }
/*     */ 
/*     */   
/*     */   private String parseOperatorFromLiteral(Literal value6) {
/* 341 */     String str = value6.toString();
/* 342 */     String operator = str.split(" ")[3];
/* 343 */     return operator;
/*     */   }
/*     */   
/*     */   private IntNum parseSeptetOp(Context context, Value value3) {
/* 347 */     IntNum ret = null;
/* 348 */     if (value3 != null && 
/* 349 */       value3.getType().toString().equals("int") && 
/* 350 */       StringUtils.isNumeric(value3.toString()))
/*     */     {
/* 352 */       ret = context.mkInt(value3.toString());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 358 */     return ret;
/*     */   }
/*     */   
/*     */   private List<Symbol> parseSeptetValue(Context context, List<SymbolicValue> value2) {
/* 362 */     List<Symbol> ret = new ArrayList<>();
/* 363 */     for (SymbolicValue v : value2) {
/* 364 */       if (v != null) {
/* 365 */         StringSymbol stringSymbol = context.mkSymbol("x_" + v.toString());
/* 366 */         ret.add(stringSymbol);
/*     */       } 
/*     */     } 
/* 369 */     return ret;
/*     */   }
/*     */ 
/*     */   
/*     */   private Set<Symbol> createSymbols(Context context, List<Septet<List<SymbolicValue>, List<SymbolicValue>, List<SymbolicValue>, Value, Value, Constant, Literal>> septetsInValue2) {
/* 374 */     Set<Symbol> ret = new HashSet<>();
/*     */     
/* 376 */     for (Septet<List<SymbolicValue>, List<SymbolicValue>, List<SymbolicValue>, Value, Value, Constant, Literal> septet : septetsInValue2) {
/*     */       
/* 378 */       List<Symbol> symbols1 = new ArrayList<>(), symbols2 = new ArrayList<>();
/* 379 */       if (septet.getValue1() != null) {
/* 380 */         symbols1 = parseSeptetValue(context, (List<SymbolicValue>)septet.getValue1());
/*     */       }
/* 382 */       if (septet.getValue2() != null) {
/* 383 */         symbols2 = parseSeptetValue(context, (List<SymbolicValue>)septet.getValue2());
/*     */       }
/*     */       
/* 386 */       ret.addAll(symbols1);
/* 387 */       ret.addAll(symbols2);
/*     */     } 
/* 389 */     return ret;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<Septet<List<SymbolicValue>, List<SymbolicValue>, List<SymbolicValue>, Value, Value, Constant, Literal>> findSeptetsInList(Literal l, List<Septet<List<SymbolicValue>, List<SymbolicValue>, List<SymbolicValue>, Value, Value, Constant, Literal>> value2) {
/* 396 */     List<Septet<List<SymbolicValue>, List<SymbolicValue>, List<SymbolicValue>, Value, Value, Constant, Literal>> ret = new ArrayList<>();
/*     */     
/* 398 */     for (Septet<List<SymbolicValue>, List<SymbolicValue>, List<SymbolicValue>, Value, Value, Constant, Literal> v : value2) {
/* 399 */       if (((Literal)v.getValue6()).toString().equals(l.toString())) {
/* 400 */         ret.add(v);
/*     */       }
/*     */     } 
/* 403 */     return ret;
/*     */   }
/*     */ 
/*     */   
/*     */   private void buildAttributeList(SootClass cls) {
/* 408 */     String hostClsName = cls.getName().split("\\$")[0];
/* 409 */     if (!this.clsMap.containsKey(hostClsName)) {
/* 410 */       this.clsMap.put(hostClsName, new HashSet<>());
/*     */     }
/* 412 */     if (!this.clsAttrMap.containsKey(hostClsName)) {
/* 413 */       this.clsAttrMap.put(hostClsName, new HashSet<>());
/*     */     }
/*     */     
/* 416 */     List<SootMethod> methods = cls.getMethods();
/* 417 */     for (SootMethod mtd : methods) {
/* 418 */       if (mtd.hasActiveBody()) {
/* 419 */         UnitPatchingChain unitPatchingChain = mtd.getActiveBody().getUnits();
/* 420 */         for (Unit unit : unitPatchingChain) {
/* 421 */           SimpleLocalDefs simpleLocalDefs = new SimpleLocalDefs((UnitGraph)new CompleteUnitGraph(mtd.getActiveBody()));
/* 422 */           Value para = null;
/* 423 */           if (unit.toString().contains("obtainStyledAttributes(int,int[])")) {
/* 424 */             para = ((ValueBox)unit.getUseBoxes().get(2)).getValue();
/*     */           }
/* 426 */           else if (unit.toString().contains("obtainStyledAttributes(android.util.AttributeSet,int[],int,int)")) {
/* 427 */             para = ((ValueBox)unit.getUseBoxes().get(2)).getValue();
/*     */           }
/* 429 */           else if (unit.toString().contains("obtainStyledAttributes(int[])")) {
/* 430 */             para = ((ValueBox)unit.getUseBoxes().get(1)).getValue();
/*     */           } 
/* 432 */           if (para == null) {
/*     */             continue;
/*     */           }
/* 435 */           Chain<Local> locals = mtd.getActiveBody().getLocals();
/* 436 */           for (Local local : locals) {
/* 437 */             if (local.toString().equals(para.toString())) {
/* 438 */               for (Unit defUnit : simpleLocalDefs.getDefsOfAt(local, unit)) {
/* 439 */                 if (defUnit instanceof AssignStmt) {
/* 440 */                   AssignStmt assignStmt = (AssignStmt)defUnit;
/* 441 */                   ((Set<Value>)this.clsMap.get(hostClsName)).add(assignStmt.getRightOp());
/* 442 */                   ((Set<Integer>)this.clsAttrMap.get(hostClsName)).addAll(parseAttributeList(assignStmt.getRightOp()));
/*     */                 } 
/*     */               } 
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Set<Integer> parseAttributeList(Value rightOp) {
/* 458 */     Set<Integer> ret = new HashSet<>();
/* 459 */     SootClass rstyleablecls = Scene.v().getSootClass("android.R$styleable");
/* 460 */     SootMethod cinitMethod = null;
/* 461 */     for (SootMethod mtd : rstyleablecls.getMethods()) {
/* 462 */       if (mtd.toString().contains("clinit")) {
/* 463 */         cinitMethod = mtd;
/*     */       }
/*     */     } 
/*     */     
/* 467 */     Body body = cinitMethod.getActiveBody();
/* 468 */     Value compassValue = null;
/* 469 */     for (Unit u : body.getUnits()) {
/* 470 */       String rightOpStr = rightOp.toString();
/* 471 */       rightOpStr = rightOpStr.replace("com.android.internal", "android");
/* 472 */       if (u.toString().contains(rightOpStr) && 
/* 473 */         u instanceof AssignStmt) {
/* 474 */         compassValue = ((AssignStmt)u).getRightOp();
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 479 */     if (compassValue != null) {
/* 480 */       for (Unit u : body.getUnits()) {
/* 481 */         if (u instanceof AssignStmt) {
/* 482 */           Value lOp = ((AssignStmt)u).getLeftOp();
/* 483 */           Value rOp = ((AssignStmt)u).getRightOp();
/* 484 */           if (lOp.toString().contains(compassValue.toString()) && 
/* 485 */             rOp.getType().toString().equals("int")) {
/* 486 */             ret.add(Integer.valueOf(Integer.parseInt(rOp.toString())));
/*     */           }
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 493 */     return ret;
/*     */   }
/*     */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/lu/uni/tsopen/diffAnalysis/DiffAnalysis.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */