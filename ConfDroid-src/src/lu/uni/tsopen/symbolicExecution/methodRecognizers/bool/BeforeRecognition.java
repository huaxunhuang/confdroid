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
/*    */ public class BeforeRecognition
/*    */   extends BooleanMethodsRecognitionHandler
/*    */ {
/*    */   public BeforeRecognition(BooleanMethodsRecognitionHandler next, SymbolicExecution se) {
/* 44 */     super(next, se);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean processBooleanMethod(SootMethod method, Value base, SymbolicValue sv, List<Value> args) {
/* 49 */     Value firstArg = null;
/* 50 */     String methodName = method.getName();
/* 51 */     if (methodName.equals("before")) {
/* 52 */       firstArg = args.get(0);
/* 53 */       if (Utils.containsTag(base, "#now", this.se) && 
/* 54 */         firstArg.getType().toString().equals("java.util.Date")) {
/* 55 */         sv.addTag(new StringConstantValueTag("#Suspicious"));
/* 56 */         return true;
/*    */       } 
/*    */     } 
/*    */     
/* 60 */     return false;
/*    */   }
/*    */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/lu/uni/tsopen/symbolicExecution/methodRecognizers/bool/BeforeRecognition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */