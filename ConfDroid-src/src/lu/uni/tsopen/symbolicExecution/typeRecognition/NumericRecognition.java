/*     */ package lu.uni.tsopen.symbolicExecution.typeRecognition;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import lu.uni.tsopen.symbolicExecution.SymbolicExecution;
/*     */ import lu.uni.tsopen.symbolicExecution.methodRecognizers.numeric.NumericMethodsRecognitionHandler;
/*     */ import lu.uni.tsopen.symbolicExecution.symbolicValues.BinOpValue;
/*     */ import lu.uni.tsopen.symbolicExecution.symbolicValues.MethodRepresentationValue;
/*     */ import lu.uni.tsopen.symbolicExecution.symbolicValues.ObjectValue;
/*     */ import lu.uni.tsopen.symbolicExecution.symbolicValues.SymbolicValue;
/*     */ import lu.uni.tsopen.utils.Utils;
/*     */ import org.javatuples.Pair;
/*     */ import soot.SootMethod;
/*     */ import soot.Unit;
/*     */ import soot.Value;
/*     */ import soot.jimple.ArrayRef;
/*     */ import soot.jimple.AssignStmt;
/*     */ import soot.jimple.BinopExpr;
/*     */ import soot.jimple.CastExpr;
/*     */ import soot.jimple.DefinitionStmt;
/*     */ import soot.jimple.InstanceInvokeExpr;
/*     */ import soot.jimple.InvokeExpr;
/*     */ import soot.jimple.InvokeStmt;
/*     */ import soot.jimple.ParameterRef;
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
/*     */ public abstract class NumericRecognition
/*     */   extends TypeRecognitionHandler
/*     */ {
/*     */   protected NumericMethodsRecognitionHandler nmrh;
/*     */   
/*     */   public NumericRecognition(TypeRecognitionHandler next, SymbolicExecution se, InfoflowCFG icfg) {
/*  67 */     super(next, se, icfg);
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleConstructorTag(List<Value> args, ObjectValue object) {}
/*     */   
/*     */   public List<Pair<Value, SymbolicValue>> handleDefinitionStmt(DefinitionStmt defUnit) {
/*     */     ObjectValue objectValue = null;
/*  75 */     Value leftOp = defUnit.getLeftOp();
/*  76 */     Value rightOp = defUnit.getRightOp();
/*  77 */     Value base = null;
/*  78 */     Value binOp1 = null;
/*  79 */     Value binOp2 = null;
/*  80 */     List<Pair<Value, SymbolicValue>> results = new LinkedList<>();
/*  81 */     InvokeExpr rightOpInvExpr = null;
/*  82 */     SootMethod method = null;
/*  83 */     List<Value> args = null;
/*  84 */     SymbolicValue object = null;
/*  85 */     BinopExpr BinOpRightOp = null;
/*  86 */     Value callerRightOp = null;
/*  87 */     InvokeExpr invExprCaller = null;
/*  88 */     Collection<Unit> callers = null;
/*  89 */     InvokeStmt invStmtCaller = null;
/*  90 */     AssignStmt assignCaller = null;
/*     */     
/*  92 */     if (rightOp instanceof InvokeExpr) {
/*  93 */       rightOpInvExpr = (InvokeExpr)rightOp;
/*  94 */       method = rightOpInvExpr.getMethod();
/*  95 */       args = rightOpInvExpr.getArgs();
/*  96 */       if (rightOp instanceof InstanceInvokeExpr) {
/*  97 */         base = ((InstanceInvokeExpr)rightOpInvExpr).getBase();
/*     */       }
/*  99 */       MethodRepresentationValue methodRepresentationValue = new MethodRepresentationValue(base, args, method, this.se);
/* 100 */       if (this.nmrh != null) {
/* 101 */         this.nmrh.recognizeNumericMethod(method, base, (SymbolicValue)methodRepresentationValue);
/*     */       }
/* 103 */     } else if (rightOp instanceof soot.jimple.InstanceFieldRef) {
/* 104 */       checkAndProcessContextValues(rightOp, results, leftOp, (Unit)defUnit);
/* 105 */     } else if (rightOp instanceof soot.jimple.StaticFieldRef) {
/* 106 */       checkAndProcessContextValues(rightOp, results, leftOp, (Unit)defUnit);
/* 107 */     } else if (rightOp instanceof BinopExpr) {
/* 108 */       BinOpRightOp = (BinopExpr)rightOp;
/* 109 */       binOp1 = BinOpRightOp.getOp1();
/* 110 */       binOp2 = BinOpRightOp.getOp2();
/* 111 */       BinOpValue binOpValue = new BinOpValue(this.se, binOp1, binOp2, BinOpRightOp.getSymbol());
/* 112 */       Utils.propagateTags(binOp1, (SymbolicValue)binOpValue, this.se);
/* 113 */       Utils.propagateTags(binOp2, (SymbolicValue)binOpValue, this.se);
/* 114 */     } else if (rightOp instanceof ParameterRef) {
/* 115 */       callers = this.icfg.getCallersOf(this.icfg.getMethodOf((Unit)defUnit));
/* 116 */       for (Unit caller : callers) {
/* 117 */         if (caller instanceof InvokeStmt) {
/* 118 */           invStmtCaller = (InvokeStmt)caller;
/* 119 */           invExprCaller = invStmtCaller.getInvokeExpr();
/* 120 */         } else if (caller instanceof AssignStmt) {
/* 121 */           assignCaller = (AssignStmt)caller;
/* 122 */           callerRightOp = assignCaller.getRightOp();
/* 123 */           if (callerRightOp instanceof InvokeExpr) {
/* 124 */             invExprCaller = (InvokeExpr)callerRightOp;
/* 125 */           } else if (callerRightOp instanceof InvokeStmt) {
/* 126 */             invExprCaller = ((InvokeStmt)callerRightOp).getInvokeExpr();
/*     */           } 
/*     */         } 
/* 129 */         checkAndProcessContextValues(invExprCaller.getArg(((ParameterRef)rightOp).getIndex()), results, leftOp, caller);
/*     */       } 
/* 131 */     } else if (rightOp instanceof soot.jimple.NewArrayExpr) {
/* 132 */       objectValue = new ObjectValue(leftOp.getType(), (Value)null, this.se);
/* 133 */     } else if (rightOp instanceof ArrayRef) {
/* 134 */       checkAndProcessContextValues(((ArrayRef)rightOp).getBase(), results, leftOp, (Unit)defUnit);
/* 135 */     } else if (leftOp instanceof soot.jimple.StaticFieldRef) {
/* 136 */       checkAndProcessContextValues(rightOp, results, leftOp, (Unit)defUnit);
/* 137 */     } else if (leftOp instanceof soot.jimple.InstanceFieldRef && !(rightOp instanceof soot.jimple.Constant)) {
/* 138 */       checkAndProcessContextValues(rightOp, results, leftOp, (Unit)defUnit);
/* 139 */     } else if (leftOp instanceof soot.jimple.InstanceFieldRef && rightOp instanceof soot.jimple.Constant) {
/* 140 */       checkAndProcessContextValues(rightOp, results, leftOp, (Unit)defUnit);
/* 141 */     } else if (rightOp instanceof CastExpr) {
/* 142 */       checkAndProcessContextValues(((CastExpr)rightOp).getOp(), results, leftOp, (Unit)defUnit);
/*     */     } else {
/* 144 */       return results;
/*     */     } 
/* 146 */     if (objectValue != null) {
/* 147 */       results.add(new Pair(leftOp, objectValue));
/*     */     }
/* 149 */     return results;
/*     */   }
/*     */   
/*     */   public void handleInvokeTag(List<Value> args, Value base, SymbolicValue object, SootMethod method) {}
/*     */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/lu/uni/tsopen/symbolicExecution/typeRecognition/NumericRecognition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */