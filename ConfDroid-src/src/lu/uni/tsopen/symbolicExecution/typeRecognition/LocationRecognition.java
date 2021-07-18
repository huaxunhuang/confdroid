/*     */ package lu.uni.tsopen.symbolicExecution.typeRecognition;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import lu.uni.tsopen.symbolicExecution.SymbolicExecution;
/*     */ import lu.uni.tsopen.symbolicExecution.methodRecognizers.location.GetLastKnowLocationRecognition;
/*     */ import lu.uni.tsopen.symbolicExecution.methodRecognizers.location.GetLastLocationRecognition;
/*     */ import lu.uni.tsopen.symbolicExecution.methodRecognizers.location.LocationMethodsRecognitionHandler;
/*     */ import lu.uni.tsopen.symbolicExecution.symbolicValues.ObjectValue;
/*     */ import lu.uni.tsopen.symbolicExecution.symbolicValues.SymbolicValue;
/*     */ import org.javatuples.Pair;
/*     */ import soot.RefType;
/*     */ import soot.SootClass;
/*     */ import soot.SootMethod;
/*     */ import soot.Type;
/*     */ import soot.Unit;
/*     */ import soot.Value;
/*     */ import soot.jimple.AssignStmt;
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
/*     */ public class LocationRecognition
/*     */   extends TypeRecognitionHandler
/*     */ {
/*     */   private LocationMethodsRecognitionHandler lmrh;
/*     */   
/*     */   public LocationRecognition(TypeRecognitionHandler next, SymbolicExecution se, InfoflowCFG icfg) {
/*  64 */     super(next, se, icfg);
/*  65 */     this.authorizedTypes.add("android.location.Location");
/*  66 */     this.lmrh = (LocationMethodsRecognitionHandler)new GetLastKnowLocationRecognition(null, se);
/*  67 */     this.lmrh = (LocationMethodsRecognitionHandler)new GetLastLocationRecognition(this.lmrh, se);
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
/*  78 */     List<Pair<Value, SymbolicValue>> results = new LinkedList<>();
/*  79 */     InvokeExpr rightOpInvExpr = null;
/*  80 */     SootMethod method = null;
/*  81 */     SootClass declaringClass = null;
/*  82 */     List<Value> args = null;
/*  83 */     SymbolicValue object = null;
/*  84 */     Type type = null;
/*     */     
/*  86 */     Value callerRightOp = null;
/*  87 */     InvokeExpr invExprCaller = null;
/*  88 */     Collection<Unit> callers = null;
/*  89 */     InvokeStmt invStmtCaller = null;
/*  90 */     AssignStmt assignCaller = null;
/*     */     
/*  92 */     if (rightOp instanceof InvokeExpr) {
/*  93 */       rightOpInvExpr = (InvokeExpr)rightOp;
/*     */       
/*  95 */       method = rightOpInvExpr.getMethod();
/*  96 */       declaringClass = method.getDeclaringClass();
/*  97 */       args = rightOpInvExpr.getArgs();
/*     */       
/*  99 */       if (rightOpInvExpr instanceof soot.jimple.StaticInvokeExpr) {
/* 100 */         RefType refType = declaringClass.getType();
/* 101 */       } else if (rightOpInvExpr instanceof InstanceInvokeExpr) {
/* 102 */         base = ((InstanceInvokeExpr)rightOpInvExpr).getBase();
/* 103 */         type = base.getType();
/*     */       } 
/*     */       
/* 106 */       objectValue = new ObjectValue(type, args, this.se);
/* 107 */       this.lmrh.recognizeLocationMethod(method, (SymbolicValue)objectValue);
/* 108 */     } else if (rightOp instanceof ParameterRef) {
/* 109 */       method = this.icfg.getMethodOf((Unit)defUnit);
/* 110 */       declaringClass = method.getDeclaringClass();
/* 111 */       if (method.getName().equals("onLocationChanged")) {
/* 112 */         for (SootClass sc : declaringClass.getInterfaces()) {
/* 113 */           if (sc.getName().equals("android.location.LocationListener")) {
/* 114 */             type = method.retrieveActiveBody().getParameterLocal(0).getType();
/*     */           }
/*     */         }
/*     */       
/*     */       }
/*     */       else {
/*     */         
/* 121 */         callers = this.icfg.getCallersOf(this.icfg.getMethodOf((Unit)defUnit));
/* 122 */         for (Unit caller : callers) {
/* 123 */           if (caller instanceof InvokeStmt) {
/* 124 */             invStmtCaller = (InvokeStmt)caller;
/* 125 */             invExprCaller = invStmtCaller.getInvokeExpr();
/* 126 */           } else if (caller instanceof AssignStmt) {
/* 127 */             assignCaller = (AssignStmt)caller;
/* 128 */             callerRightOp = assignCaller.getRightOp();
/* 129 */             if (callerRightOp instanceof InvokeExpr) {
/* 130 */               invExprCaller = (InvokeExpr)callerRightOp;
/* 131 */             } else if (callerRightOp instanceof InvokeStmt) {
/* 132 */               invExprCaller = ((InvokeStmt)callerRightOp).getInvokeExpr();
/*     */             } 
/*     */           } 
/*     */           
/* 136 */           checkAndProcessContextValues(invExprCaller.getArg(((ParameterRef)rightOp).getIndex()), results, leftOp, caller);
/*     */         } 
/*     */       } 
/*     */     } 
/* 140 */     if (objectValue != null) {
/* 141 */       results.add(new Pair(leftOp, objectValue));
/*     */     }
/* 143 */     return results;
/*     */   }
/*     */   
/*     */   public void handleInvokeTag(List<Value> args, Value base, SymbolicValue object, SootMethod method) {}
/*     */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/lu/uni/tsopen/symbolicExecution/typeRecognition/LocationRecognition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */