/*    */ package lu.uni.tsopen.symbolicExecution.typeRecognition;
/*    */ 
/*    */ import java.util.LinkedList;
/*    */ import java.util.List;
/*    */ import lu.uni.tsopen.symbolicExecution.SymbolicExecution;
/*    */ import lu.uni.tsopen.symbolicExecution.methodRecognizers.sms.CreateFromPduRecognition;
/*    */ import lu.uni.tsopen.symbolicExecution.methodRecognizers.sms.SmsMethodsRecognitionHandler;
/*    */ import lu.uni.tsopen.symbolicExecution.symbolicValues.ObjectValue;
/*    */ import lu.uni.tsopen.symbolicExecution.symbolicValues.SymbolicValue;
/*    */ import org.javatuples.Pair;
/*    */ import soot.SootMethod;
/*    */ import soot.Type;
/*    */ import soot.Value;
/*    */ import soot.jimple.DefinitionStmt;
/*    */ import soot.jimple.StaticInvokeExpr;
/*    */ import soot.jimple.infoflow.solver.cfg.InfoflowCFG;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SmsRecognition
/*    */   extends TypeRecognitionHandler
/*    */ {
/*    */   private SmsMethodsRecognitionHandler smrh;
/*    */   
/*    */   public SmsRecognition(TypeRecognitionHandler next, SymbolicExecution se, InfoflowCFG icfg) {
/* 53 */     super(next, se, icfg);
/* 54 */     this.authorizedTypes.add("android.telephony.SmsMessage");
/* 55 */     this.smrh = (SmsMethodsRecognitionHandler)new CreateFromPduRecognition(null, se);
/*    */   }
/*    */ 
/*    */   
/*    */   public void handleConstructorTag(List<Value> args, ObjectValue object) {}
/*    */ 
/*    */   
/*    */   public List<Pair<Value, SymbolicValue>> handleDefinitionStmt(DefinitionStmt defUnit) {
/* 63 */     Value leftOp = defUnit.getLeftOp();
/* 64 */     Value rightOp = defUnit.getRightOp();
/* 65 */     List<Pair<Value, SymbolicValue>> results = new LinkedList<>();
/* 66 */     StaticInvokeExpr rightOpStaticInvokeExpr = null;
/* 67 */     SootMethod method = null;
/* 68 */     List<Value> args = null;
/* 69 */     ObjectValue object = null;
/*    */     
/* 71 */     if (rightOp instanceof StaticInvokeExpr) {
/* 72 */       rightOpStaticInvokeExpr = (StaticInvokeExpr)rightOp;
/* 73 */       method = rightOpStaticInvokeExpr.getMethod();
/* 74 */       args = rightOpStaticInvokeExpr.getArgs();
/* 75 */       object = new ObjectValue((Type)method.getDeclaringClass().getType(), args, this.se);
/* 76 */       this.smrh.recognizeSmsMethod(method, (SymbolicValue)object);
/* 77 */       results.add(new Pair(leftOp, object));
/*    */     } 
/* 79 */     return results;
/*    */   }
/*    */   
/*    */   public void handleInvokeTag(List<Value> args, Value base, SymbolicValue object, SootMethod method) {}
/*    */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/lu/uni/tsopen/symbolicExecution/typeRecognition/SmsRecognition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */