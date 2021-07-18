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
/*    */ public class GetInstanceRecognition
/*    */   extends DateTimeMethodsRecognitionHandler
/*    */ {
/*    */   public GetInstanceRecognition(DateTimeMethodsRecognitionHandler next, SymbolicExecution se) {
/* 40 */     super(next, se);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean processDateTimeMethod(SootMethod method, SymbolicValue sv) {
/* 45 */     String className = method.getDeclaringClass().getName();
/* 46 */     String methodName = method.getName();
/* 47 */     if (methodName.equals("getInstance") && (className.equals("java.util.Calendar") || className.equals("java.util.GregorianCalendar"))) {
/* 48 */       sv.addTag(new StringConstantValueTag("#now"));
/* 49 */       return true;
/*    */     } 
/* 51 */     return false;
/*    */   }
/*    */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/lu/uni/tsopen/symbolicExecution/methodRecognizers/dateTime/GetInstanceRecognition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */