/*    */ package lu.uni.tsopen.symbolicExecution.methodRecognizers.dateTime;
/*    */ 
/*    */ import lu.uni.tsopen.symbolicExecution.SymbolicExecution;
/*    */ import lu.uni.tsopen.symbolicExecution.symbolicValues.SymbolicValue;
/*    */ import soot.SootMethod;
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
/*    */ public class SetToNowRecognition
/*    */   extends DateTimeMethodsRecognitionHandler
/*    */ {
/*    */   public SetToNowRecognition(DateTimeMethodsRecognitionHandler next, SymbolicExecution se) {
/* 40 */     super(next, se);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean processDateTimeMethod(SootMethod method, SymbolicValue sv) {
/* 45 */     String className = method.getDeclaringClass().getName();
/* 46 */     String methodName = method.getName();
/* 47 */     if (methodName.equals("setToNow") && className.equals("android.text.format.Time")) {
/* 48 */       sv.addTag(new StringConstantValueTag("#now"));
/* 49 */       return true;
/*    */     } 
/* 51 */     return false;
/*    */   }
/*    */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/lu/uni/tsopen/symbolicExecution/methodRecognizers/dateTime/SetToNowRecognition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */