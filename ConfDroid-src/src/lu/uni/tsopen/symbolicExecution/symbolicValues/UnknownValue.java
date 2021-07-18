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
/*    */ 
/*    */ public class UnknownValue
/*    */   extends AbstractSymbolicValue
/*    */ {
/*    */   private String additionalValues;
/*    */   
/*    */   public UnknownValue(SymbolicExecution se) {
/* 39 */     super(se);
/* 40 */     this.additionalValues = "";
/*    */   }
/*    */   
/*    */   public UnknownValue(SymbolicExecution se, String additionalValues) {
/* 44 */     super(se);
/* 45 */     this.additionalValues = additionalValues;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getValue() {
/* 50 */     if (this.additionalValues.isEmpty()) {
/* 51 */       return "{#}";
/*    */     }
/* 53 */     return String.format("%s_%s", new Object[] { "{#}", this.additionalValues });
/*    */   }
/*    */ 
/*    */   
/*    */   public void addValue(String s) {
/* 58 */     this.additionalValues += s;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isSymbolic() {
/* 63 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isConstant() {
/* 68 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isMethodRepresentation() {
/* 73 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isObject() {
/* 78 */     return false;
/*    */   }
/*    */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/lu/uni/tsopen/symbolicExecution/symbolicValues/UnknownValue.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */