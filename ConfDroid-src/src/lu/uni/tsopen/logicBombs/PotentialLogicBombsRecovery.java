/*     */ package lu.uni.tsopen.logicBombs;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import lu.uni.tsopen.pathPredicateRecovery.PathPredicateRecovery;
/*     */ import lu.uni.tsopen.pathPredicateRecovery.SimpleBlockPredicateExtraction;
/*     */ import lu.uni.tsopen.symbolicExecution.ContextualValues;
/*     */ import lu.uni.tsopen.symbolicExecution.SymbolicExecution;
/*     */ import lu.uni.tsopen.symbolicExecution.symbolicValues.BinOpValue;
/*     */ import lu.uni.tsopen.symbolicExecution.symbolicValues.ConstantValue;
/*     */ import lu.uni.tsopen.symbolicExecution.symbolicValues.SymbolicValue;
/*     */ import lu.uni.tsopen.utils.Utils;
/*     */ import org.javatuples.Pair;
/*     */ import org.javatuples.Septet;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import soot.SootClass;
/*     */ import soot.SootMethod;
/*     */ import soot.Unit;
/*     */ import soot.UnitPatchingChain;
/*     */ import soot.Value;
/*     */ import soot.ValueBox;
/*     */ import soot.jimple.AssignStmt;
/*     */ import soot.jimple.ConditionExpr;
/*     */ import soot.jimple.Constant;
/*     */ import soot.jimple.DoubleConstant;
/*     */ import soot.jimple.FloatConstant;
/*     */ import soot.jimple.IfStmt;
/*     */ import soot.jimple.IntConstant;
/*     */ import soot.jimple.LongConstant;
/*     */ import soot.jimple.ReturnStmt;
/*     */ import soot.jimple.Stmt;
/*     */ import soot.jimple.infoflow.solver.cfg.InfoflowCFG;
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
/*     */ public class PotentialLogicBombsRecovery
/*     */   implements Runnable
/*     */ {
/*     */   private final SimpleBlockPredicateExtraction sbpe;
/*     */   private final SymbolicExecution se;
/*     */   private final PathPredicateRecovery ppr;
/*     */   private Map<IfStmt, Pair<List<SymbolicValue>, SootMethod>> potentialLogicBombs;
/*     */   private List<SootMethod> visitedMethods;
/*     */   private List<IfStmt> visitedIfs;
/*     */   private InfoflowCFG icfg;
/*     */   private boolean containsSuspiciousCheck;
/*     */   private boolean containsSuspiciousCheckAfterControlDependency;
/*     */   private boolean containsSuspiciousCheckAfterPostFilterStep;
/*     */   private SootMethod currentSensitiveMethod;
/*  73 */   protected Logger logger = LoggerFactory.getLogger(getClass());
/*     */   
/*     */   public PotentialLogicBombsRecovery(SimpleBlockPredicateExtraction sbpe, SymbolicExecution se, PathPredicateRecovery ppr, InfoflowCFG icfg) {
/*  76 */     this.sbpe = sbpe;
/*  77 */     this.se = se;
/*  78 */     this.ppr = ppr;
/*  79 */     this.visitedMethods = new ArrayList<>();
/*  80 */     this.visitedIfs = new ArrayList<>();
/*  81 */     this.potentialLogicBombs = new HashMap<>();
/*  82 */     this.icfg = icfg;
/*  83 */     this.containsSuspiciousCheck = false;
/*  84 */     this.containsSuspiciousCheckAfterControlDependency = false;
/*  85 */     this.containsSuspiciousCheckAfterPostFilterStep = false;
/*  86 */     this.currentSensitiveMethod = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void run() {
/*  91 */     retrievePotentialLogicBombs();
/*     */   }
/*     */   
/*     */   public void retrievePotentialLogicBombs() {
/*  95 */     for (Stmt ifStmt : this.sbpe.getConditions()) {
/*  96 */       if (ifStmt instanceof IfStmt && !isTrigger((IfStmt)ifStmt)) {
/*  97 */         this.potentialLogicBombs.remove(ifStmt);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean isTrigger(IfStmt ifStmt) {
/* 103 */     this.visitedMethods.clear();
/* 104 */     if (!isSuspicious(ifStmt)) {
/* 105 */       return false;
/*     */     }
/* 107 */     this.containsSuspiciousCheck = true;
/* 108 */     if (this.logger.isDebugEnabled()) {
/* 109 */       this.logger.debug("Predicate is suspicious : {}", ifStmt);
/*     */     }
/* 111 */     if (!controlSensitiveAction(ifStmt)) {
/* 112 */       return false;
/*     */     }
/* 114 */     this.containsSuspiciousCheckAfterControlDependency = true;
/* 115 */     if (this.logger.isDebugEnabled()) {
/* 116 */       this.logger.debug("Predicate is suspicious after post filters : {}", ifStmt);
/*     */     }
/* 118 */     if (isSuspiciousAfterPostFilters(ifStmt)) {
/* 119 */       this.containsSuspiciousCheckAfterPostFilterStep = true;
/* 120 */       return true;
/*     */     } 
/* 122 */     if (this.logger.isDebugEnabled()) {
/* 123 */       this.logger.debug("Predicate does not control sensitive action : {}", ifStmt);
/*     */     }
/* 125 */     return false;
/*     */   }
/*     */   
/*     */   private boolean isSuspiciousAfterPostFilters(IfStmt ifStmt) {
/* 129 */     Septet<List<SymbolicValue>, List<SymbolicValue>, List<SymbolicValue>, Value, Value, Constant, Boolean> contextualValues = getContextualValues(ifStmt);
/* 130 */     List<SymbolicValue> values = (List<SymbolicValue>)contextualValues.getValue0();
/* 131 */     Constant constant = (Constant)contextualValues.getValue5();
/* 132 */     boolean isNullCheck = ((Boolean)contextualValues.getValue6()).booleanValue();
/* 133 */     boolean isSuspicious = false;
/*     */     
/* 135 */     if (values != null) {
/* 136 */       for (SymbolicValue sv : values) {
/* 137 */         if (((sv.containsTag("#now/#seconds") || sv
/* 138 */           .containsTag("#now/#minutes") || sv
/* 139 */           .containsTag("#now/#month") || sv
/* 140 */           .containsTag("#now/#hour") || sv
/* 141 */           .containsTag("#now/#year") || sv
/* 142 */           .containsTag("#here/#longitude") || sv
/* 143 */           .containsTag("#here/#latitude") || sv
/* 144 */           .containsTag("currentTimeMillis") || sv
/* 145 */           .containsTag("#now") || sv
/* 146 */           .containsTag("#here") || sv
/* 147 */           .containsTag("#sms") || sv
/* 148 */           .containsTag("#sms/#sender") || sv
/* 149 */           .containsTag("#sms/#body") || sv
/* 150 */           .containsTag("#Suspicious")) && constant != null && ((constant instanceof IntConstant && ((IntConstant)constant).value == -1) || constant instanceof soot.jimple.NullConstant)) || isNullCheck) {
/*     */           continue;
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 156 */         if (!sv.hasTag()) {
/*     */           continue;
/*     */         }
/* 159 */         isSuspicious = true;
/* 160 */         if (!isInFilteredLib(ifStmt)) {
/* 161 */           addPotentialLogicBomb(ifStmt, sv);
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/* 166 */     return isSuspicious;
/*     */   }
/*     */   
/*     */   private boolean isInFilteredLib(IfStmt ifStmt) {
/* 170 */     SootClass cl = this.icfg.getMethodOf((Unit)ifStmt).getDeclaringClass();
/* 171 */     if (Utils.isFilteredLib(cl)) {
/* 172 */       return true;
/*     */     }
/* 174 */     return false;
/*     */   }
/*     */   
/*     */   private void addPotentialLogicBomb(IfStmt ifStmt, SymbolicValue sv) {
/* 178 */     List<SymbolicValue> lbs = null;
/* 179 */     Pair<List<SymbolicValue>, SootMethod> pair = this.potentialLogicBombs.get(ifStmt);
/* 180 */     if (pair == null) {
/* 181 */       lbs = new ArrayList<>();
/* 182 */       pair = new Pair(lbs, this.currentSensitiveMethod);
/* 183 */       this.potentialLogicBombs.put(ifStmt, pair);
/*     */     } 
/* 185 */     lbs = (List<SymbolicValue>)pair.getValue0();
/* 186 */     lbs.add(sv);
/*     */   }
/*     */   
/*     */   private boolean controlSensitiveAction(IfStmt ifStmt) {
/* 190 */     List<Unit> guardedBlocks = this.ppr.getGuardedBlocks(ifStmt);
/* 191 */     if (isSensitive(guardedBlocks)) {
/* 192 */       return true;
/*     */     }
/* 194 */     for (Unit block : guardedBlocks) {
/* 195 */       for (ValueBox vb : block.getDefBoxes()) {
/* 196 */         for (IfStmt i : getRelatedPredicates(vb.getValue())) {
/* 197 */           if (ifStmt != i && !this.visitedIfs.contains(i)) {
/* 198 */             this.visitedIfs.add(i);
/* 199 */             if (controlSensitiveAction(i)) {
/* 200 */               return true;
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 206 */     if (ifControlBoolReturn(ifStmt)) {
/* 207 */       SootMethod methodOfIf = this.icfg.getMethodOf((Unit)ifStmt);
/* 208 */       AssignStmt callerAssign = null;
/* 209 */       Value leftOp = null;
/* 210 */       for (Unit caller : this.icfg.getCallersOf(methodOfIf)) {
/* 211 */         if (caller instanceof AssignStmt) {
/* 212 */           callerAssign = (AssignStmt)caller;
/* 213 */           leftOp = callerAssign.getLeftOp();
/* 214 */           for (Unit u : this.icfg.getSuccsOf((Unit)callerAssign)) {
/* 215 */             if (u instanceof IfStmt) {
/* 216 */               for (ValueBox vb : u.getUseBoxes()) {
/* 217 */                 if (vb.getValue() == leftOp) {
/* 218 */                   guardedBlocks = this.ppr.getGuardedBlocks((IfStmt)u);
/* 219 */                   if (isSensitive(guardedBlocks)) {
/* 220 */                     return true;
/*     */                   }
/*     */                 } 
/*     */               } 
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 229 */     return false;
/*     */   }
/*     */   
/*     */   private boolean ifControlBoolReturn(IfStmt ifStmt) {
/* 233 */     ReturnStmt ret = null;
/* 234 */     Value retOp = null;
/* 235 */     IntConstant retCons = null;
/* 236 */     boolean controlBoolReturn = false;
/* 237 */     for (Unit u : this.icfg.getSuccsOf((Unit)ifStmt)) {
/* 238 */       if (u instanceof ReturnStmt) {
/* 239 */         ret = (ReturnStmt)u;
/* 240 */         retOp = ret.getOp();
/* 241 */         if (retOp instanceof IntConstant) {
/* 242 */           retCons = (IntConstant)retOp;
/* 243 */           if (retCons.value == 1 || retCons.value == 0) {
/* 244 */             controlBoolReturn = true; continue;
/*     */           } 
/* 246 */           controlBoolReturn = false;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 251 */     return controlBoolReturn;
/*     */   }
/*     */   
/*     */   private List<IfStmt> getRelatedPredicates(Value value) {
/* 255 */     List<IfStmt> ifs = new ArrayList<>();
/* 256 */     for (Stmt ifStmt : this.sbpe.getConditions()) {
/* 257 */       if (ifStmt instanceof IfStmt) {
/* 258 */         for (ValueBox vb : ifStmt.getUseBoxes()) {
/* 259 */           if (vb.getValue() == value) {
/* 260 */             ifs.add((IfStmt)ifStmt);
/*     */           }
/*     */         } 
/*     */       }
/*     */     } 
/* 265 */     return ifs;
/*     */   }
/*     */   
/*     */   private boolean isSensitive(Collection<Unit> guardedBlocks) {
/* 269 */     Collection<Unit> units = null;
/* 270 */     for (Unit block : guardedBlocks) {
/* 271 */       for (SootMethod m : Utils.getInvokedMethods(block, this.icfg)) {
/* 272 */         if (!this.visitedMethods.contains(m)) {
/* 273 */           this.visitedMethods.add(m);
/* 274 */           if (Utils.isSensitiveMethod(m)) {
/* 275 */             this.currentSensitiveMethod = m;
/* 276 */             return true;
/*     */           } 
/* 278 */           if (m.getDeclaringClass().isApplicationClass() && m.isConcrete()) {
/* 279 */             UnitPatchingChain unitPatchingChain = m.retrieveActiveBody().getUnits();
/* 280 */             if (unitPatchingChain != null && 
/* 281 */               isSensitive((Collection<Unit>)unitPatchingChain)) {
/* 282 */               return true;
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 289 */     return false;
/*     */   }
/*     */   
/*     */   private boolean isSuspicious(IfStmt ifStmt) {
/* 293 */     Septet<List<SymbolicValue>, List<SymbolicValue>, List<SymbolicValue>, Value, Value, Constant, Boolean> contextualValues = getContextualValues(ifStmt);
/* 294 */     List<SymbolicValue> values = (List<SymbolicValue>)contextualValues.getValue0();
/* 295 */     List<SymbolicValue> valuesOp1 = (List<SymbolicValue>)contextualValues.getValue1();
/* 296 */     List<SymbolicValue> valuesOp2 = (List<SymbolicValue>)contextualValues.getValue2();
/* 297 */     Value op1 = (Value)contextualValues.getValue3();
/* 298 */     Value op2 = (Value)contextualValues.getValue4();
/*     */     
/* 300 */     if (values != null) {
/* 301 */       for (SymbolicValue sv : values) {
/* 302 */         if (sv.containsTag("#now/#seconds") || sv
/* 303 */           .containsTag("#now/#minutes") || sv
/* 304 */           .containsTag("#now/#hour") || sv
/* 305 */           .containsTag("#now/#year") || sv
/* 306 */           .containsTag("#now/#month") || sv
/* 307 */           .containsTag("#here/#longitude") || sv
/* 308 */           .containsTag("#here/#latitude") || sv
/* 309 */           .containsTag("currentTimeMillis") || sv
/* 310 */           .containsTag("#now") || sv
/* 311 */           .containsTag("#Suspicious")) {
/* 312 */           return true;
/*     */         }
/*     */       } 
/*     */     }
/* 316 */     if (!(op1 instanceof Constant) && !(op2 instanceof Constant)) {
/* 317 */       if (valuesOp1 != null) {
/* 318 */         for (SymbolicValue sv1 : valuesOp1) {
/* 319 */           if (sv1.containsTag("#here") || sv1
/* 320 */             .containsTag("#now") || sv1
/* 321 */             .containsTag("#now/#hour") || sv1
/* 322 */             .containsTag("#now/#year") || sv1
/* 323 */             .containsTag("#here/#latitude") || sv1
/* 324 */             .containsTag("#here/#longitude") || sv1
/* 325 */             .containsTag("#now/#minutes") || sv1
/* 326 */             .containsTag("#now/#seconds") || sv1
/* 327 */             .containsTag("#now/#month")) {
/* 328 */             return true;
/*     */           }
/*     */         } 
/*     */       }
/* 332 */       if (valuesOp2 != null) {
/* 333 */         for (SymbolicValue sv2 : valuesOp2) {
/* 334 */           if (sv2.containsTag("#here") || sv2
/* 335 */             .containsTag("#now/#hour") || sv2
/* 336 */             .containsTag("#now/#year") || sv2
/* 337 */             .containsTag("#now") || sv2
/* 338 */             .containsTag("#here/#latitude") || sv2
/* 339 */             .containsTag("#here/#longitude") || sv2
/* 340 */             .containsTag("#now/#minutes") || sv2
/* 341 */             .containsTag("#now/#seconds") || sv2
/* 342 */             .containsTag("#now/#month")) {
/* 343 */             return true;
/*     */           }
/*     */         } 
/*     */       }
/*     */     } 
/* 348 */     return false;
/*     */   }
/*     */   
/*     */   private Septet<List<SymbolicValue>, List<SymbolicValue>, List<SymbolicValue>, Value, Value, Constant, Boolean> getContextualValues(IfStmt ifStmt) {
/* 352 */     ConditionExpr conditionExpr = (ConditionExpr)ifStmt.getCondition();
/* 353 */     Value op1 = conditionExpr.getOp1();
/* 354 */     Value op2 = conditionExpr.getOp2();
/* 355 */     ContextualValues contextualValuesOp1 = null;
/* 356 */     ContextualValues contextualValuesOp2 = null;
/* 357 */     List<SymbolicValue> valuesOp1 = null;
/* 358 */     List<SymbolicValue> valuesOp2 = null;
/* 359 */     List<SymbolicValue> values = null;
/* 360 */     Constant constant = null;
/* 361 */     boolean isNullCheck = false;
/*     */     
/* 363 */     if (!(op1 instanceof Constant)) {
/* 364 */       contextualValuesOp1 = this.se.getContextualValues((Unit)ifStmt, op1);
/*     */     }
/* 366 */     if (!(op2 instanceof Constant)) {
/* 367 */       contextualValuesOp2 = this.se.getContextualValues((Unit)ifStmt, op2);
/*     */     }
/* 369 */     if (contextualValuesOp1 != null) {
/* 370 */       valuesOp1 = contextualValuesOp1.getLastCoherentValues((Unit)ifStmt);
/*     */     }
/* 372 */     if (contextualValuesOp2 != null) {
/* 373 */       valuesOp2 = contextualValuesOp2.getLastCoherentValues((Unit)ifStmt);
/*     */     }
/* 375 */     if (valuesOp1 != null) {
/* 376 */       values = valuesOp1;
/* 377 */       if (op2 instanceof Constant) {
/* 378 */         constant = (Constant)op2;
/* 379 */       } else if (containConstantSymbolicValue(op2)) {
/* 380 */         constant = getConstantValue(op2);
/*     */       } 
/* 382 */       isNullCheck = isNullCheck(valuesOp1);
/* 383 */     } else if (valuesOp2 != null) {
/* 384 */       values = valuesOp2;
/* 385 */       if (op1 instanceof Constant) {
/* 386 */         constant = (Constant)op1;
/* 387 */       } else if (containConstantSymbolicValue(op1)) {
/* 388 */         constant = getConstantValue(op1);
/*     */       } 
/* 390 */       isNullCheck = isNullCheck(valuesOp2);
/*     */     } 
/*     */     
/* 393 */     return new Septet(values, valuesOp1, valuesOp2, op1, op2, constant, Boolean.valueOf(isNullCheck));
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isNullCheck(List<SymbolicValue> values) {
/* 398 */     BinOpValue binOpValue = null;
/* 399 */     Value binOpValueOp = null;
/* 400 */     for (SymbolicValue sv : values) {
/* 401 */       if (sv instanceof BinOpValue) {
/* 402 */         binOpValue = (BinOpValue)sv;
/* 403 */         binOpValueOp = binOpValue.getOp2();
/* 404 */         if (binOpValueOp instanceof FloatConstant) {
/* 405 */           if (((FloatConstant)binOpValueOp).value == 0.0F || ((FloatConstant)binOpValueOp).value == -1.0F)
/* 406 */             return true;  continue;
/*     */         } 
/* 408 */         if (binOpValueOp instanceof IntConstant) {
/* 409 */           if (((IntConstant)binOpValueOp).value == 0 || ((IntConstant)binOpValueOp).value == -1)
/* 410 */             return true;  continue;
/*     */         } 
/* 412 */         if (binOpValueOp instanceof DoubleConstant) {
/* 413 */           if (((DoubleConstant)binOpValueOp).value == 0.0D || ((DoubleConstant)binOpValueOp).value == -1.0D)
/* 414 */             return true;  continue;
/*     */         } 
/* 416 */         if (binOpValueOp instanceof LongConstant && (
/* 417 */           ((LongConstant)binOpValueOp).value == 0L || ((LongConstant)binOpValueOp).value == -1L)) {
/* 418 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 423 */     return false;
/*     */   }
/*     */   
/*     */   private boolean containConstantSymbolicValue(Value v) {
/* 427 */     List<SymbolicValue> values = null;
/* 428 */     ContextualValues contextualValues = null;
/* 429 */     if (v != null) {
/* 430 */       contextualValues = (ContextualValues)this.se.getContext().get(v);
/* 431 */       if (contextualValues != null) {
/* 432 */         values = contextualValues.getAllValues();
/* 433 */         if (values != null) {
/* 434 */           for (SymbolicValue sv : values) {
/* 435 */             if (sv instanceof ConstantValue) {
/* 436 */               return true;
/*     */             }
/*     */           } 
/*     */         }
/*     */       } 
/*     */     } 
/* 442 */     return false;
/*     */   }
/*     */   
/*     */   private Constant getConstantValue(Value v) {
/* 446 */     List<SymbolicValue> values = null;
/* 447 */     ContextualValues contextualValues = null;
/* 448 */     ConstantValue cv = null;
/* 449 */     Constant c = null;
/* 450 */     if (v != null) {
/* 451 */       contextualValues = (ContextualValues)this.se.getContext().get(v);
/* 452 */       if (contextualValues != null) {
/* 453 */         values = contextualValues.getAllValues();
/* 454 */         if (values != null) {
/* 455 */           for (SymbolicValue sv : values) {
/* 456 */             if (sv instanceof ConstantValue) {
/* 457 */               cv = (ConstantValue)sv;
/* 458 */               c = cv.getConstant();
/* 459 */               if (c instanceof IntConstant) {
/* 460 */                 return (Constant)IntConstant.v(((IntConstant)c).value);
/*     */               }
/*     */             } 
/*     */           } 
/*     */         }
/*     */       } 
/*     */     } 
/* 467 */     return null;
/*     */   }
/*     */   
/*     */   public Map<IfStmt, Pair<List<SymbolicValue>, SootMethod>> getPotentialLogicBombs() {
/* 471 */     return this.potentialLogicBombs;
/*     */   }
/*     */   
/*     */   public boolean hasPotentialLogicBombs() {
/* 475 */     return !this.potentialLogicBombs.isEmpty();
/*     */   }
/*     */   
/*     */   public boolean ContainsSuspiciousCheck() {
/* 479 */     return this.containsSuspiciousCheck;
/*     */   }
/*     */   
/*     */   public boolean ContainsSuspiciousCheckAfterControlDependency() {
/* 483 */     return this.containsSuspiciousCheckAfterControlDependency;
/*     */   }
/*     */   
/*     */   public boolean ContainsSuspiciousCheckAfterPostFilterStep() {
/* 487 */     return this.containsSuspiciousCheckAfterPostFilterStep;
/*     */   }
/*     */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/lu/uni/tsopen/logicBombs/PotentialLogicBombsRecovery.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */