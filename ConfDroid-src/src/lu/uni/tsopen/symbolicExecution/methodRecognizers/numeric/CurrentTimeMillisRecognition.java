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
/*    */ public class CurrentTimeMillisRecognition
/*    */   extends NumericMethodsRecognitionHandler
/*    */ {
/*    */   public CurrentTimeMillisRecognition(NumericMethodsRecognitionHandler next, SymbolicExecution se) {
/* 41 */     super(next, se);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean processNumericMethod(SootMethod method, Value base, SymbolicValue sv) {
/* 46 */     return genericProcessNumericMethod(method, base, sv, "java.lang.System", "currentTimeMillis", null, "#now");
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isTagHandled(String containedTag, String addedTag, Value base, SymbolicValue sv) {
/* 51 */     sv.addTag(new StringConstantValueTag(addedTag));
/* 52 */     return true;
/*    */   }
/*    */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/lu/uni/tsopen/symbolicExecution/methodRecognizers/numeric/CurrentTimeMillisRecognition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */