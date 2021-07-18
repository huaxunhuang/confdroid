/*     */ package lu.uni.tsopen.logicBombs;
/*     */ 
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import lu.uni.tsopen.graphTraversal.ICFGForwardTraversal;
/*     */ import lu.uni.tsopen.pathPredicateRecovery.PathPredicateRecovery;
/*     */ import lu.uni.tsopen.pathPredicateRecovery.SimpleBlockPredicateExtraction;
/*     */ import lu.uni.tsopen.symbolicExecution.ContextualValues;
/*     */ import lu.uni.tsopen.symbolicExecution.SymbolicExecution;
/*     */ import lu.uni.tsopen.symbolicExecution.symbolicValues.BinOpValue;
/*     */ import lu.uni.tsopen.symbolicExecution.symbolicValues.ConstantValue;
/*     */ import lu.uni.tsopen.symbolicExecution.symbolicValues.SymbolicValue;
/*     */ import org.javatuples.Septet;
/*     */ import org.logicng.formulas.Formula;
/*     */ import org.logicng.formulas.Literal;
/*     */ import soot.SootMethod;
/*     */ import soot.Unit;
/*     */ import soot.Value;
/*     */ import soot.jimple.ConditionExpr;
/*     */ import soot.jimple.Constant;
/*     */ import soot.jimple.DoubleConstant;
/*     */ import soot.jimple.FloatConstant;
/*     */ import soot.jimple.IfStmt;
/*     */ import soot.jimple.IntConstant;
/*     */ import soot.jimple.LongConstant;
/*     */ import soot.jimple.Stmt;
/*     */ import soot.jimple.SwitchStmt;
/*     */ import soot.jimple.infoflow.solver.cfg.InfoflowCFG;
/*     */ 
/*     */ public class AttributeSignatureGenerator extends ICFGForwardTraversal {
/*     */   public AttributeSignatureGenerator(InfoflowCFG icfg, SootMethod dummyMainMethod, SimpleBlockPredicateExtraction sbpe, PathPredicateRecovery ppr, SymbolicExecution se) {
/*  34 */     super(icfg, "Attribute Signature Generator", dummyMainMethod);
/*  35 */     this.sbpe = sbpe;
/*  36 */     this.ppr = ppr;
/*  37 */     this.se = se;
/*     */   }
/*     */   
/*     */   private SimpleBlockPredicateExtraction sbpe;
/*     */   
/*     */   protected void processNodeBeforeNeighbors(Unit node) {
/*  43 */     LinkedList<Unit> currentPath = getCurrentPath();
/*  44 */     if (node.toString().contains("getInt(") && node
/*  45 */       .toString().contains("TypedArray") && !node.toString().contains("goto ")) {
/*     */       
/*  47 */       System.out.println("begin interesting():::===");
/*  48 */       System.out.println("hugulu: " + node.toString() + " " + this.icfg.getMethodOf(node) + " " + node.getJavaSourceStartLineNumber());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  57 */       boolean flag = false;
/*  58 */       System.out.println("currentNode: " + currentPath.getLast() + " " + this.icfg.getMethodOf(node));
/*     */       
/*  60 */       System.out.println("formula: " + this.ppr.getNodeToFullPathPredicate().get(node));
/*  61 */       Formula formula = (Formula)this.ppr.getNodeToFullPathPredicate().get(node);
/*  62 */       Set<Stmt> literalStmts = new HashSet<>();
/*  63 */       if (formula != null && formula.literals().size() > 0) {
/*  64 */         for (Literal literal : formula.literals()) {
/*  65 */           literalStmts.add(this.sbpe.getCondtionFromLiteral(literal));
/*     */         }
/*     */       }
/*     */       
/*  69 */       for (Stmt literalStmt : literalStmts) {
/*  70 */         if (!currentPath.contains(literalStmt)) {
/*  71 */           flag = true;
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/*     */       
/*  77 */       if (!flag && formula != null && formula.literals() != null) {
/*  78 */         for (Literal literal : formula.literals()) {
/*  79 */           System.out.println("literal: " + literal + " " + this.sbpe
/*  80 */               .getCondtionFromLiteral(literal).getJavaSourceStartLineNumber());
/*     */           
/*  82 */           Septet<List<SymbolicValue>, List<SymbolicValue>, List<SymbolicValue>, Value, Value, Constant, Boolean> septet = getContextualValues(literal);
/*  83 */           System.out.println("septet.valueOp1: " + septet.getValue1());
/*  84 */           System.out.println("septet.valueOp2: " + septet.getValue2());
/*  85 */           System.out.println("septet.op1: " + septet.getValue3());
/*  86 */           System.out.println("septet.op2: " + septet.getValue4());
/*  87 */           System.out.println();
/*     */         } 
/*     */       }
/*  90 */       System.out.println("end getDimensionPixelSize():::===");
/*  91 */       System.out.println();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private PathPredicateRecovery ppr;
/*     */   private SymbolicExecution se;
/*     */   
/*     */   private Septet<List<SymbolicValue>, List<SymbolicValue>, List<SymbolicValue>, Value, Value, Constant, Boolean> getContextualValuesByStmt(Stmt stmt) {
/* 100 */     if (stmt.containsInvokeExpr()) {
/* 101 */       SootMethod invokeMethod = stmt.getInvokeExpr().getMethod();
/* 102 */       if (invokeMethod.getSignature().equals("<android.content.res.TypedArray: int getInt(int,int)>")) {
/* 103 */         Value defaultSymbol = stmt.getInvokeExpr().getArg(1);
/* 104 */         if (!(defaultSymbol instanceof Constant)) {
/* 105 */           ContextualValues defaultContextualValues = this.se.getContextualValues(defaultSymbol);
/* 106 */           System.out.println();
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 112 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   private Septet<List<SymbolicValue>, List<SymbolicValue>, List<SymbolicValue>, Value, Value, Constant, Boolean> getContextualValues(Literal literal) {
/* 117 */     Stmt stmt = this.sbpe.getCondtionFromLiteral(literal);
/* 118 */     if (stmt instanceof IfStmt) {
/* 119 */       IfStmt ifStmt = (IfStmt)stmt;
/* 120 */       ConditionExpr conditionExpr = (ConditionExpr)ifStmt.getCondition();
/* 121 */       Value op1 = conditionExpr.getOp1();
/* 122 */       Value op2 = conditionExpr.getOp2();
/* 123 */       ContextualValues contextualValuesOp1 = null;
/* 124 */       ContextualValues contextualValuesOp2 = null;
/* 125 */       List<SymbolicValue> valuesOp1 = null;
/* 126 */       List<SymbolicValue> valuesOp2 = null;
/* 127 */       List<SymbolicValue> values = null;
/* 128 */       Constant constant = null;
/* 129 */       boolean isNullCheck = false;
/*     */       
/* 131 */       if (!(op1 instanceof Constant)) {
/* 132 */         contextualValuesOp1 = this.se.getContextualValues(op1);
/*     */       }
/* 134 */       if (!(op2 instanceof Constant)) {
/* 135 */         contextualValuesOp2 = this.se.getContextualValues(op2);
/*     */       }
/* 137 */       if (contextualValuesOp1 != null) {
/* 138 */         valuesOp1 = contextualValuesOp1.getLastCoherentValues((Unit)ifStmt);
/*     */       }
/* 140 */       if (contextualValuesOp2 != null) {
/* 141 */         valuesOp2 = contextualValuesOp2.getLastCoherentValues((Unit)ifStmt);
/*     */       }
/* 143 */       if (valuesOp1 != null) {
/* 144 */         values = valuesOp1;
/* 145 */         if (op2 instanceof Constant) {
/* 146 */           constant = (Constant)op2;
/* 147 */         } else if (containConstantSymbolicValue(op2)) {
/* 148 */           constant = getConstantValue(op2);
/*     */         } 
/* 150 */         isNullCheck = isNullCheck(valuesOp1);
/* 151 */       } else if (valuesOp2 != null) {
/* 152 */         values = valuesOp2;
/* 153 */         if (op1 instanceof Constant) {
/* 154 */           constant = (Constant)op1;
/* 155 */         } else if (containConstantSymbolicValue(op1)) {
/* 156 */           constant = getConstantValue(op1);
/*     */         } 
/* 158 */         isNullCheck = isNullCheck(valuesOp2);
/*     */       } 
/*     */       
/* 161 */       return new Septet(values, valuesOp1, valuesOp2, op1, op2, constant, Boolean.valueOf(isNullCheck));
/* 162 */     }  if (stmt instanceof SwitchStmt) {
/* 163 */       SwitchStmt switchStmt = (SwitchStmt)stmt;
/*     */       
/* 165 */       Value op1 = switchStmt.getKey();
/* 166 */       int index = Integer.parseInt(literal.toString().split("== ")[1].split("\\)")[0]);
/* 167 */       IntConstant intConstant = IntConstant.v(index);
/*     */       
/* 169 */       ContextualValues contextualValuesOp1 = null;
/* 170 */       ContextualValues contextualValuesOp2 = null;
/* 171 */       List<SymbolicValue> valuesOp1 = null;
/* 172 */       List<SymbolicValue> valuesOp2 = null;
/* 173 */       List<SymbolicValue> values = null;
/* 174 */       Constant constant = null;
/* 175 */       boolean isNullCheck = false;
/*     */       
/* 177 */       if (!(op1 instanceof Constant)) {
/* 178 */         contextualValuesOp1 = this.se.getContextualValues(op1);
/*     */       }
/* 180 */       if (!(intConstant instanceof Constant)) {
/* 181 */         contextualValuesOp2 = this.se.getContextualValues((Value)intConstant);
/*     */       }
/* 183 */       if (contextualValuesOp1 != null) {
/* 184 */         valuesOp1 = contextualValuesOp1.getLastCoherentValues((Unit)switchStmt);
/*     */       }
/* 186 */       if (contextualValuesOp2 != null) {
/* 187 */         valuesOp2 = contextualValuesOp2.getLastCoherentValues((Unit)switchStmt);
/*     */       }
/* 189 */       if (valuesOp1 != null) {
/* 190 */         values = valuesOp1;
/* 191 */         if (intConstant instanceof Constant) {
/* 192 */           constant = (Constant)intConstant;
/* 193 */         } else if (containConstantSymbolicValue((Value)intConstant)) {
/* 194 */           constant = getConstantValue((Value)intConstant);
/*     */         } 
/* 196 */         isNullCheck = isNullCheck(valuesOp1);
/* 197 */       } else if (valuesOp2 != null) {
/* 198 */         values = valuesOp2;
/* 199 */         if (op1 instanceof Constant) {
/* 200 */           constant = (Constant)op1;
/* 201 */         } else if (containConstantSymbolicValue(op1)) {
/* 202 */           constant = getConstantValue(op1);
/*     */         } 
/* 204 */         isNullCheck = isNullCheck(valuesOp2);
/*     */       } 
/*     */       
/* 207 */       return new Septet(values, valuesOp1, valuesOp2, op1, intConstant, constant, Boolean.valueOf(isNullCheck));
/*     */     } 
/* 209 */     return null;
/*     */   }
/*     */   
/*     */   private Constant getConstantValue(Value v) {
/* 213 */     List<SymbolicValue> values = null;
/* 214 */     ContextualValues contextualValues = null;
/* 215 */     ConstantValue cv = null;
/* 216 */     Constant c = null;
/* 217 */     if (v != null) {
/* 218 */       contextualValues = (ContextualValues)this.se.getContext().get(v);
/* 219 */       if (contextualValues != null) {
/* 220 */         values = contextualValues.getAllValues();
/* 221 */         if (values != null) {
/* 222 */           for (SymbolicValue sv : values) {
/* 223 */             if (sv instanceof ConstantValue) {
/* 224 */               cv = (ConstantValue)sv;
/* 225 */               c = cv.getConstant();
/* 226 */               if (c instanceof IntConstant) {
/* 227 */                 return (Constant)IntConstant.v(((IntConstant)c).value);
/*     */               }
/*     */             } 
/*     */           } 
/*     */         }
/*     */       } 
/*     */     } 
/* 234 */     return null;
/*     */   }
/*     */   
/*     */   private boolean isNullCheck(List<SymbolicValue> values) {
/* 238 */     BinOpValue binOpValue = null;
/* 239 */     Value binOpValueOp = null;
/* 240 */     for (SymbolicValue sv : values) {
/* 241 */       if (sv instanceof BinOpValue) {
/* 242 */         binOpValue = (BinOpValue)sv;
/* 243 */         binOpValueOp = binOpValue.getOp2();
/* 244 */         if (binOpValueOp instanceof FloatConstant) {
/* 245 */           if (((FloatConstant)binOpValueOp).value == 0.0F || ((FloatConstant)binOpValueOp).value == -1.0F)
/* 246 */             return true;  continue;
/*     */         } 
/* 248 */         if (binOpValueOp instanceof IntConstant) {
/* 249 */           if (((IntConstant)binOpValueOp).value == 0 || ((IntConstant)binOpValueOp).value == -1)
/* 250 */             return true;  continue;
/*     */         } 
/* 252 */         if (binOpValueOp instanceof DoubleConstant) {
/* 253 */           if (((DoubleConstant)binOpValueOp).value == 0.0D || ((DoubleConstant)binOpValueOp).value == -1.0D)
/* 254 */             return true;  continue;
/*     */         } 
/* 256 */         if (binOpValueOp instanceof LongConstant && (
/* 257 */           ((LongConstant)binOpValueOp).value == 0L || ((LongConstant)binOpValueOp).value == -1L)) {
/* 258 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 263 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean containConstantSymbolicValue(Value v) {
/* 268 */     List<SymbolicValue> values = null;
/* 269 */     ContextualValues contextualValues = null;
/* 270 */     if (v != null) {
/* 271 */       contextualValues = (ContextualValues)this.se.getContext().get(v);
/* 272 */       if (contextualValues != null) {
/* 273 */         values = contextualValues.getAllValues();
/* 274 */         if (values != null) {
/* 275 */           for (SymbolicValue sv : values) {
/* 276 */             if (sv instanceof ConstantValue) {
/* 277 */               return true;
/*     */             }
/*     */           } 
/*     */         }
/*     */       } 
/*     */     } 
/* 283 */     return false;
/*     */   }
/*     */   
/*     */   protected void processNodeAfterNeighbors(Unit node) {}
/*     */   
/*     */   protected void processNeighbor(Unit node, Unit neighbour) {}
/*     */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/lu/uni/tsopen/logicBombs/AttributeSignatureGenerator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */