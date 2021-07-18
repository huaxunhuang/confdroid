/*    */ package lu.uni.tsopen.symbolicExecution.symbolicValues;
/*    */ 
/*    */ import lu.uni.tsopen.symbolicExecution.SymbolicExecution;
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
/*    */ public abstract class ConcreteValue
/*    */   extends AbstractSymbolicValue
/*    */ {
/*    */   public ConcreteValue(SymbolicExecution se) {
/* 36 */     super(se);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isSymbolic() {
/* 41 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isMethodRepresentation() {
/* 46 */     return false;
/*    */   }
/*    */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/lu/uni/tsopen/symbolicExecution/symbolicValues/ConcreteValue.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */