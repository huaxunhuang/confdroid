/*    */ package lu.uni.tsopen.symbolicExecution.methodRecognizers.bool;
/*    */ 
/*    */ import java.util.List;
/*    */ import lu.uni.tsopen.symbolicExecution.SymbolicExecution;
/*    */ import lu.uni.tsopen.symbolicExecution.symbolicValues.SymbolicValue;
/*    */ import lu.uni.tsopen.utils.Utils;
/*    */ import soot.SootMethod;
/*    */ import soot.Value;
/*    */ import soot.tagkit.StringConstantValueTag;
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
/*    */ 
/*    */ public class StartsWithRecognition
/*    */   extends BooleanMethodsRecognitionHandler
/*    */ {
/*    */   public StartsWithRecognition(BooleanMethodsRecognitionHandler next, SymbolicExecution se) {
/* 45 */     super(next, se);
/*    */   }
/*    */   
/*    */   public boolean processBooleanMethod(SootMethod method, Value base, SymbolicValue sv, List<Value> args) {
/* 49 */     Value firstArg = null;
/* 50 */     String methodName = method.getName();
/* 51 */     if (methodName.equals("startsWith")) {
/* 52 */       firstArg = args.get(0);
/* 53 */       if ((Utils.containsTag(base, "#sms/#body", this.se) || Utils.containsTag(base, "#sms/#sender", this.se)) && (
/* 54 */         firstArg instanceof soot.jimple.Constant || 
/* 55 */         Utils.containsTags(firstArg, this.se))) {
/* 56 */         sv.addTag(new StringConstantValueTag("#Suspicious"));
/* 57 */         return true;
/*    */       } 
/*    */     } 
/*    */     
/* 61 */     return false;
/*    */   }
/*    */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/lu/uni/tsopen/symbolicExecution/methodRecognizers/bool/StartsWithRecognition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */