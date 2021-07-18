/*     */ package lu.uni.tsopen.symbolicExecution.typeRecognition;
/*     */ 
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import lu.uni.tsopen.symbolicExecution.SymbolicExecution;
/*     */ import lu.uni.tsopen.symbolicExecution.methodRecognizers.bool.AfterRecognition;
/*     */ import lu.uni.tsopen.symbolicExecution.methodRecognizers.bool.BeforeRecognition;
/*     */ import lu.uni.tsopen.symbolicExecution.methodRecognizers.bool.BooleanMethodsRecognitionHandler;
/*     */ import lu.uni.tsopen.symbolicExecution.methodRecognizers.bool.ContainsRecognition;
/*     */ import lu.uni.tsopen.symbolicExecution.methodRecognizers.bool.EndsWithRecognition;
/*     */ import lu.uni.tsopen.symbolicExecution.methodRecognizers.bool.EqualsRecognition;
/*     */ import lu.uni.tsopen.symbolicExecution.methodRecognizers.bool.MatchesRecognition;
/*     */ import lu.uni.tsopen.symbolicExecution.methodRecognizers.bool.StartsWithRecognition;
/*     */ import lu.uni.tsopen.symbolicExecution.symbolicValues.FieldValue;
/*     */ import lu.uni.tsopen.symbolicExecution.symbolicValues.MethodRepresentationValue;
/*     */ import lu.uni.tsopen.symbolicExecution.symbolicValues.ObjectValue;
/*     */ import lu.uni.tsopen.symbolicExecution.symbolicValues.SymbolicValue;
/*     */ import lu.uni.tsopen.utils.Utils;
/*     */ import org.javatuples.Pair;
/*     */ import soot.SootMethod;
/*     */ import soot.Value;
/*     */ import soot.jimple.DefinitionStmt;
/*     */ import soot.jimple.InstanceFieldRef;
/*     */ import soot.jimple.InstanceInvokeExpr;
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
/*     */ public class BooleanRecognition
/*     */   extends TypeRecognitionHandler
/*     */ {
/*     */   private BooleanMethodsRecognitionHandler bmrh;
/*     */   
/*     */   public BooleanRecognition(TypeRecognitionHandler next, SymbolicExecution se, InfoflowCFG icfg) {
/*  63 */     super(next, se, icfg);
/*  64 */     this.authorizedTypes.add("boolean");
/*  65 */     this.bmrh = (BooleanMethodsRecognitionHandler)new AfterRecognition(null, se);
/*  66 */     this.bmrh = (BooleanMethodsRecognitionHandler)new BeforeRecognition(this.bmrh, se);
/*  67 */     this.bmrh = (BooleanMethodsRecognitionHandler)new EqualsRecognition(this.bmrh, se);
/*  68 */     this.bmrh = (BooleanMethodsRecognitionHandler)new ContainsRecognition(this.bmrh, se);
/*  69 */     this.bmrh = (BooleanMethodsRecognitionHandler)new StartsWithRecognition(this.bmrh, se);
/*  70 */     this.bmrh = (BooleanMethodsRecognitionHandler)new EndsWithRecognition(this.bmrh, se);
/*  71 */     this.bmrh = (BooleanMethodsRecognitionHandler)new MatchesRecognition(this.bmrh, se);
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleConstructorTag(List<Value> args, ObjectValue object) {}
/*     */   
/*     */   public List<Pair<Value, SymbolicValue>> handleDefinitionStmt(DefinitionStmt defUnit) {
/*     */     FieldValue fieldValue = null;
/*  79 */     Value leftOp = defUnit.getLeftOp();
/*  80 */     Value rightOp = defUnit.getRightOp();
/*  81 */     Value base = null;
/*  82 */     List<Pair<Value, SymbolicValue>> results = new LinkedList<>();
/*  83 */     InstanceInvokeExpr rightOpInvExpr = null;
/*  84 */     InstanceFieldRef field = null;
/*  85 */     SootMethod method = null;
/*  86 */     List<Value> args = null;
/*  87 */     SymbolicValue object = null;
/*     */     
/*  89 */     if (rightOp instanceof InstanceInvokeExpr) {
/*  90 */       rightOpInvExpr = (InstanceInvokeExpr)rightOp;
/*  91 */       method = rightOpInvExpr.getMethod();
/*  92 */       args = rightOpInvExpr.getArgs();
/*  93 */       base = rightOpInvExpr.getBase();
/*  94 */       MethodRepresentationValue methodRepresentationValue = new MethodRepresentationValue(base, args, method, this.se);
/*  95 */       this.bmrh.recognizeBooleanMethod(method, base, (SymbolicValue)methodRepresentationValue, args);
/*  96 */     } else if (rightOp instanceof InstanceFieldRef) {
/*  97 */       field = (InstanceFieldRef)rightOp;
/*  98 */       base = field.getBase();
/*  99 */       fieldValue = new FieldValue(base, field.getField().getName(), this.se);
/* 100 */       Utils.propagateTags(rightOp, (SymbolicValue)fieldValue, this.se);
/*     */     } else {
/* 102 */       return results;
/*     */     } 
/* 104 */     results.add(new Pair(leftOp, fieldValue));
/* 105 */     return results;
/*     */   }
/*     */   
/*     */   public void handleInvokeTag(List<Value> args, Value base, SymbolicValue object, SootMethod method) {}
/*     */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/lu/uni/tsopen/symbolicExecution/typeRecognition/BooleanRecognition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */