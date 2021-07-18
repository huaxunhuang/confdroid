/*    */ package lu.uni.tsopen.symbolicExecution.methodRecognizers.numeric;
/*    */ 
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
/*    */ public abstract class NumericMethodsRecognitionHandler
/*    */   implements NumericMethodsRecognition
/*    */ {
/*    */   private NumericMethodsRecognitionHandler next;
/*    */   protected SymbolicExecution se;
/*    */   
/*    */   public NumericMethodsRecognitionHandler(NumericMethodsRecognitionHandler next, SymbolicExecution se) {
/* 44 */     this.next = next;
/* 45 */     this.se = se;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean recognizeNumericMethod(SootMethod method, Value base, SymbolicValue sv) {
/* 50 */     boolean recognized = processNumericMethod(method, base, sv);
/*    */     
/* 52 */     if (recognized) {
/* 53 */       return recognized;
/*    */     }
/* 55 */     if (this.next != null) {
/* 56 */       return this.next.recognizeNumericMethod(method, base, sv);
/*    */     }
/*    */     
/* 59 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean genericProcessNumericMethod(SootMethod method, Value base, SymbolicValue sv, String className, String methodName, String containedTag, String addedTag) {
/* 66 */     if (method.getDeclaringClass().getName().equals(className) && method.getName().equals(methodName) && 
/* 67 */       isTagHandled(containedTag, addedTag, base, sv)) {
/* 68 */       return true;
/*    */     }
/*    */     
/* 71 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isTagHandled(String containedTag, String addedTag, Value base, SymbolicValue sv) {
/* 76 */     if (Utils.containsTag(base, containedTag, this.se)) {
/* 77 */       sv.addTag(new StringConstantValueTag(addedTag));
/* 78 */       return true;
/*    */     } 
/* 80 */     return false;
/*    */   }
/*    */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/lu/uni/tsopen/symbolicExecution/methodRecognizers/numeric/NumericMethodsRecognitionHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */