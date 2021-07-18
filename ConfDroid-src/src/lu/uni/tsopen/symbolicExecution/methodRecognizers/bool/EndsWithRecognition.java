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
/*    */ public class EndsWithRecognition
/*    */   extends BooleanMethodsRecognitionHandler
/*    */ {
/*    */   public EndsWithRecognition(BooleanMethodsRecognitionHandler next, SymbolicExecution se) {
/* 45 */     super(next, se);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean processBooleanMethod(SootMethod method, Value base, SymbolicValue sv, List<Value> args) {
/* 50 */     Value firstArg = null;
/* 51 */     String methodName = method.getName();
/* 52 */     if (methodName.equals("endsWith")) {
/* 53 */       firstArg = args.get(0);
/* 54 */       if ((Utils.containsTag(base, "#sms/#body", this.se) || Utils.containsTag(base, "#sms/#sender", this.se)) && (
/* 55 */         firstArg instanceof soot.jimple.Constant || 
/* 56 */         Utils.containsTags(firstArg, this.se))) {
/* 57 */         sv.addTag(new StringConstantValueTag("#Suspicious"));
/* 58 */         return true;
/*    */       } 
/*    */     } 
/*    */     
/* 62 */     return false;
/*    */   }
/*    */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/lu/uni/tsopen/symbolicExecution/methodRecognizers/bool/EndsWithRecognition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */