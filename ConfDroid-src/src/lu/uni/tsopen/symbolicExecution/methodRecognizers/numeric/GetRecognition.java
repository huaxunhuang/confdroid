/*    */ package lu.uni.tsopen.symbolicExecution.methodRecognizers.numeric;
/*    */ 
/*    */ import lu.uni.tsopen.symbolicExecution.SymbolicExecution;
/*    */ import lu.uni.tsopen.symbolicExecution.symbolicValues.SymbolicValue;
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
/*    */ public class GetRecognition
/*    */   extends NumericMethodsRecognitionHandler
/*    */ {
/*    */   public GetRecognition(NumericMethodsRecognitionHandler next, SymbolicExecution se) {
/* 41 */     super(next, se);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean processNumericMethod(SootMethod method, Value base, SymbolicValue sv) {
/* 46 */     String className = method.getDeclaringClass().getName();
/* 47 */     String methodName = method.getName();
/* 48 */     if (methodName.equals("get") && (className.equals("java.util.Calendar") || className.equals("java.util.GregorianCalendar"))) {
/* 49 */       sv.addTag(new StringConstantValueTag("#now"));
/* 50 */       return true;
/*    */     } 
/* 52 */     return false;
/*    */   }
/*    */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/lu/uni/tsopen/symbolicExecution/methodRecognizers/numeric/GetRecognition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */