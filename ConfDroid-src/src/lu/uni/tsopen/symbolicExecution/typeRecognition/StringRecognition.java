/*     */ package lu.uni.tsopen.symbolicExecution.typeRecognition;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import lu.uni.tsopen.symbolicExecution.SymbolicExecution;
/*     */ import lu.uni.tsopen.symbolicExecution.methodRecognizers.strings.AppendRecognition;
/*     */ import lu.uni.tsopen.symbolicExecution.methodRecognizers.strings.FormatRecognition;
/*     */ import lu.uni.tsopen.symbolicExecution.methodRecognizers.strings.GetMessageBodyRecognition;
/*     */ import lu.uni.tsopen.symbolicExecution.methodRecognizers.strings.GetOriginatingAddressRecognition;
/*     */ import lu.uni.tsopen.symbolicExecution.methodRecognizers.strings.StringMethodsRecognitionHandler;
/*     */ import lu.uni.tsopen.symbolicExecution.methodRecognizers.strings.SubStringRecognition;
/*     */ import lu.uni.tsopen.symbolicExecution.methodRecognizers.strings.ToLowerCaseRecognition;
/*     */ import lu.uni.tsopen.symbolicExecution.methodRecognizers.strings.ToStringRecognition;
/*     */ import lu.uni.tsopen.symbolicExecution.methodRecognizers.strings.ToUpperCaseRecognition;
/*     */ import lu.uni.tsopen.symbolicExecution.methodRecognizers.strings.ValueOfRecognition;
/*     */ import lu.uni.tsopen.symbolicExecution.symbolicValues.ConstantValue;
/*     */ import lu.uni.tsopen.symbolicExecution.symbolicValues.MethodRepresentationValue;
/*     */ import lu.uni.tsopen.symbolicExecution.symbolicValues.ObjectValue;
/*     */ import lu.uni.tsopen.symbolicExecution.symbolicValues.SymbolicValue;
/*     */ import lu.uni.tsopen.utils.Utils;
/*     */ import org.javatuples.Pair;
/*     */ import soot.SootMethod;
/*     */ import soot.Unit;
/*     */ import soot.Value;
/*     */ import soot.jimple.AssignStmt;
/*     */ import soot.jimple.CastExpr;
/*     */ import soot.jimple.Constant;
/*     */ import soot.jimple.DefinitionStmt;
/*     */ import soot.jimple.InstanceInvokeExpr;
/*     */ import soot.jimple.InvokeExpr;
/*     */ import soot.jimple.InvokeStmt;
/*     */ import soot.jimple.ParameterRef;
/*     */ import soot.jimple.ReturnStmt;
/*     */ import soot.jimple.StringConstant;
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
/*     */ public class StringRecognition
/*     */   extends TypeRecognitionHandler
/*     */ {
/*     */   private StringMethodsRecognitionHandler smrh;
/*     */   
/*     */   public StringRecognition(TypeRecognitionHandler next, SymbolicExecution se, InfoflowCFG icfg) {
/*  75 */     super(next, se, icfg);
/*  76 */     this.smrh = (StringMethodsRecognitionHandler)new AppendRecognition(null, se);
/*  77 */     this.smrh = (StringMethodsRecognitionHandler)new ValueOfRecognition(this.smrh, se);
/*  78 */     this.smrh = (StringMethodsRecognitionHandler)new ToStringRecognition(this.smrh, se);
/*  79 */     this.smrh = (StringMethodsRecognitionHandler)new SubStringRecognition(this.smrh, se);
/*  80 */     this.smrh = (StringMethodsRecognitionHandler)new GetMessageBodyRecognition(this.smrh, se);
/*  81 */     this.smrh = (StringMethodsRecognitionHandler)new FormatRecognition(this.smrh, se);
/*  82 */     this.smrh = (StringMethodsRecognitionHandler)new ToLowerCaseRecognition(this.smrh, se);
/*  83 */     this.smrh = (StringMethodsRecognitionHandler)new ToUpperCaseRecognition(this.smrh, se);
/*  84 */     this.smrh = (StringMethodsRecognitionHandler)new GetOriginatingAddressRecognition(this.smrh, se);
/*  85 */     this.authorizedTypes.add("java.lang.String");
/*  86 */     this.authorizedTypes.add("java.lang.StringBuffer");
/*  87 */     this.authorizedTypes.add("java.lang.StringBuilder");
/*     */   }
/*     */ 
/*     */   
/*     */   public List<Pair<Value, SymbolicValue>> handleDefinitionStmt(DefinitionStmt defUnit) {
/*  92 */     Value leftOp = defUnit.getLeftOp();
/*  93 */     Value rightOp = defUnit.getRightOp();
/*  94 */     Value callerRightOp = null;
/*  95 */     Value base = null;
/*  96 */     InvokeExpr rightOpInvokeExpr = null;
/*  97 */     InvokeExpr invExprCaller = null;
/*  98 */     SootMethod method = null;
/*  99 */     List<Value> args = null;
/* 100 */     List<Pair<Value, SymbolicValue>> results = new LinkedList<>();
/* 101 */     CastExpr rightOpExpr = null;
/* 102 */     Collection<Unit> callers = null;
/* 103 */     InvokeStmt invStmtCaller = null;
/* 104 */     AssignStmt assignCaller = null;
/* 105 */     List<SymbolicValue> recognizedValues = null;
/* 106 */     SymbolicValue object = null;
/*     */     
/* 108 */     if (rightOp instanceof StringConstant) {
/* 109 */       results.add(new Pair(leftOp, new ConstantValue((Constant)rightOp, this.se)));
/* 110 */     } else if (rightOp instanceof ParameterRef) {
/* 111 */       callers = this.icfg.getCallersOf(this.icfg.getMethodOf((Unit)defUnit));
/* 112 */       for (Unit caller : callers) {
/* 113 */         if (caller instanceof InvokeStmt) {
/* 114 */           invStmtCaller = (InvokeStmt)caller;
/* 115 */           invExprCaller = invStmtCaller.getInvokeExpr();
/* 116 */         } else if (caller instanceof AssignStmt) {
/* 117 */           assignCaller = (AssignStmt)caller;
/* 118 */           callerRightOp = assignCaller.getRightOp();
/* 119 */           if (callerRightOp instanceof InvokeExpr) {
/* 120 */             invExprCaller = (InvokeExpr)callerRightOp;
/* 121 */           } else if (callerRightOp instanceof InvokeStmt) {
/* 122 */             invExprCaller = ((InvokeStmt)callerRightOp).getInvokeExpr();
/*     */           } 
/*     */         } 
/* 125 */         checkAndProcessContextValues(invExprCaller.getArg(((ParameterRef)rightOp).getIndex()), results, leftOp, caller);
/*     */       } 
/* 127 */     } else if (rightOp instanceof soot.Local && !(leftOp instanceof soot.jimple.InstanceFieldRef)) {
/* 128 */       checkAndProcessContextValues(rightOp, results, leftOp, null);
/* 129 */     } else if (rightOp instanceof CastExpr) {
/* 130 */       rightOpExpr = (CastExpr)rightOp;
/* 131 */       checkAndProcessContextValues(rightOpExpr.getOp(), results, leftOp, null);
/* 132 */     } else if (rightOp instanceof InvokeExpr) {
/* 133 */       MethodRepresentationValue methodRepresentationValue = null; rightOpInvokeExpr = (InvokeExpr)rightOp;
/* 134 */       method = rightOpInvokeExpr.getMethod();
/* 135 */       args = rightOpInvokeExpr.getArgs();
/* 136 */       base = (rightOpInvokeExpr instanceof InstanceInvokeExpr) ? ((InstanceInvokeExpr)rightOpInvokeExpr).getBase() : null;
/* 137 */       recognizedValues = this.smrh.recognizeStringMethod(method, base, args);
/* 138 */       if (recognizedValues != null) {
/* 139 */         for (SymbolicValue recognizedValue : recognizedValues) {
/* 140 */           results.add(new Pair(leftOp, recognizedValue));
/*     */         }
/*     */       } else {
/* 143 */         methodRepresentationValue = new MethodRepresentationValue(base, args, method, this.se);
/* 144 */         results.add(new Pair(leftOp, methodRepresentationValue));
/*     */       } 
/* 146 */       if (!this.se.isMethodVisited(method)) {
/* 147 */         this.se.addMethodToWorkList(this.icfg.getMethodOf((Unit)defUnit));
/*     */       }
/* 149 */       else if (method.isConcrete()) {
/* 150 */         for (Unit u : method.retrieveActiveBody().getUnits()) {
/* 151 */           if (u instanceof ReturnStmt && 
/* 152 */             methodRepresentationValue != null) {
/* 153 */             Utils.propagateTags(((ReturnStmt)u).getOp(), (SymbolicValue)methodRepresentationValue, this.se);
/*     */           }
/*     */         }
/*     */       
/*     */       }
/*     */     
/* 159 */     } else if (rightOp instanceof soot.jimple.InstanceFieldRef) {
/* 160 */       checkAndProcessContextValues(rightOp, results, leftOp, (Unit)defUnit);
/* 161 */     } else if (leftOp instanceof soot.jimple.InstanceFieldRef) {
/* 162 */       checkAndProcessContextValues(rightOp, results, leftOp, (Unit)defUnit);
/*     */     } 
/* 164 */     return results;
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleConstructor(InvokeExpr invExprUnit, Value base, List<Pair<Value, SymbolicValue>> results) {
/* 169 */     Value arg = null;
/* 170 */     List<Value> args = invExprUnit.getArgs();
/* 171 */     ConstantValue cv = null;
/* 172 */     if (args.size() == 0) {
/* 173 */       results.add(new Pair(base, new ConstantValue((Constant)StringConstant.v(""), this.se)));
/*     */     } else {
/* 175 */       arg = args.get(0);
/* 176 */       if (arg instanceof soot.Local) {
/* 177 */         checkAndProcessContextValues(arg, results, base, null);
/*     */       } else {
/* 179 */         if (arg instanceof StringConstant) {
/* 180 */           cv = new ConstantValue((Constant)arg, this.se);
/*     */         } else {
/*     */           
/* 183 */           cv = new ConstantValue((Constant)StringConstant.v(""), this.se);
/*     */         } 
/* 185 */         results.add(new Pair(base, cv));
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void handleConstructorTag(List<Value> args, ObjectValue object) {}
/*     */   
/*     */   public void handleInvokeTag(List<Value> args, Value base, SymbolicValue object, SootMethod method) {}
/*     */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/lu/uni/tsopen/symbolicExecution/typeRecognition/StringRecognition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */