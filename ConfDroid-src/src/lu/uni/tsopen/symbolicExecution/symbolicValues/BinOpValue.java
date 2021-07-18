/*    */ package lu.uni.tsopen.symbolicExecution.symbolicValues;
/*    */ 
/*    */ import lu.uni.tsopen.symbolicExecution.SymbolicExecution;
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
/*    */ public class BinOpValue
/*    */   extends AbstractSymbolicValue
/*    */ {
/*    */   private Value op1;
/*    */   private Value op2;
/*    */   private String symbol;
/*    */   
/*    */   public BinOpValue(SymbolicExecution se, Value op1, Value op2, String symbol) {
/* 41 */     super(se);
/* 42 */     this.op1 = op1;
/* 43 */     this.op2 = op2;
/* 44 */     this.symbol = symbol;
/* 45 */     if (this.op1 != null) {
/* 46 */       this.values.put(this.op1, getSymbolicValues(this.op1));
/*    */     }
/* 48 */     if (this.op2 != null) {
/* 49 */       this.values.put(this.op2, getSymbolicValues(this.op2));
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public String getValue() {
/* 55 */     return String.format("%s%s%s", new Object[] { computeValue(this.op1), this.symbol, computeValue(this.op2) });
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isSymbolic() {
/* 60 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isConstant() {
/* 65 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isMethodRepresentation() {
/* 70 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isObject() {
/* 75 */     return false;
/*    */   }
/*    */   
/*    */   public Value getOp1() {
/* 79 */     return this.op1;
/*    */   }
/*    */   
/*    */   public Value getOp2() {
/* 83 */     return this.op2;
/*    */   }
/*    */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/lu/uni/tsopen/symbolicExecution/symbolicValues/BinOpValue.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */