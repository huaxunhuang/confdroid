/*    */ package lu.uni.tsopen.symbolicExecution.methodRecognizers.bool;
/*    */ 
/*    */ import java.util.List;
/*    */ import lu.uni.tsopen.symbolicExecution.SymbolicExecution;
/*    */ import lu.uni.tsopen.symbolicExecution.symbolicValues.SymbolicValue;
/*    */ import soot.SootMethod;
/*    */ import soot.Value;
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
/*    */ public abstract class BooleanMethodsRecognitionHandler
/*    */   implements BooleanMethodsRecognition
/*    */ {
/*    */   private BooleanMethodsRecognitionHandler next;
/*    */   protected SymbolicExecution se;
/*    */   
/*    */   public BooleanMethodsRecognitionHandler(BooleanMethodsRecognitionHandler next, SymbolicExecution se) {
/* 44 */     this.next = next;
/* 45 */     this.se = se;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean recognizeBooleanMethod(SootMethod method, Value base, SymbolicValue sv, List<Value> args) {
/* 50 */     boolean recognized = processBooleanMethod(method, base, sv, args);
/*    */     
/* 52 */     if (recognized) {
/* 53 */       return recognized;
/*    */     }
/* 55 */     if (this.next != null) {
/* 56 */       return this.next.recognizeBooleanMethod(method, base, sv, args);
/*    */     }
/*    */     
/* 59 */     return false;
/*    */   }
/*    */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/lu/uni/tsopen/symbolicExecution/methodRecognizers/bool/BooleanMethodsRecognitionHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */