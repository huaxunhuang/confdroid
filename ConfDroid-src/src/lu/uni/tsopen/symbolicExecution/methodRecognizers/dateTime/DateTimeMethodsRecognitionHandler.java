/*    */ package lu.uni.tsopen.symbolicExecution.methodRecognizers.dateTime;
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
/*    */ public abstract class DateTimeMethodsRecognitionHandler
/*    */   implements DateTimeMethodsRecognition
/*    */ {
/*    */   private DateTimeMethodsRecognitionHandler next;
/*    */   protected SymbolicExecution se;
/*    */   
/*    */   public DateTimeMethodsRecognitionHandler(DateTimeMethodsRecognitionHandler next, SymbolicExecution se) {
/* 41 */     this.next = next;
/* 42 */     this.se = se;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean recognizeDateTimeMethod(SootMethod method, SymbolicValue sv) {
/* 47 */     boolean recognized = processDateTimeMethod(method, sv);
/*    */     
/* 49 */     if (recognized) {
/* 50 */       return recognized;
/*    */     }
/* 52 */     if (this.next != null) {
/* 53 */       return this.next.recognizeDateTimeMethod(method, sv);
/*    */     }
/*    */     
/* 56 */     return false;
/*    */   }
/*    */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/lu/uni/tsopen/symbolicExecution/methodRecognizers/dateTime/DateTimeMethodsRecognitionHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */