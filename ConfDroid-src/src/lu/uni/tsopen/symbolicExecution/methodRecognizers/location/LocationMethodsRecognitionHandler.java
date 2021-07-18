/*    */ package lu.uni.tsopen.symbolicExecution.methodRecognizers.location;
/*    */ 
/*    */ import lu.uni.tsopen.symbolicExecution.SymbolicExecution;
/*    */ import lu.uni.tsopen.symbolicExecution.symbolicValues.SymbolicValue;
/*    */ import soot.SootMethod;
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
/*    */ public abstract class LocationMethodsRecognitionHandler
/*    */   implements LocationMethodsRecognition
/*    */ {
/*    */   private LocationMethodsRecognitionHandler next;
/*    */   protected SymbolicExecution se;
/*    */   
/*    */   public LocationMethodsRecognitionHandler(LocationMethodsRecognitionHandler next, SymbolicExecution se) {
/* 41 */     this.next = next;
/* 42 */     this.se = se;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean recognizeLocationMethod(SootMethod method, SymbolicValue sv) {
/* 47 */     boolean recognized = processLocationMethod(method, sv);
/*    */     
/* 49 */     if (recognized) {
/* 50 */       return recognized;
/*    */     }
/* 52 */     if (this.next != null) {
/* 53 */       return this.next.recognizeLocationMethod(method, sv);
/*    */     }
/*    */     
/* 56 */     return false;
/*    */   }
/*    */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/lu/uni/tsopen/symbolicExecution/methodRecognizers/location/LocationMethodsRecognitionHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */