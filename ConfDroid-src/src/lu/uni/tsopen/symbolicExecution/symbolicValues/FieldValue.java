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
/*    */ public class FieldValue
/*    */   extends AbstractSymbolicValue
/*    */ {
/*    */   private Value base;
/*    */   private String field;
/*    */   
/*    */   public FieldValue(Value base, String field, SymbolicExecution se) {
/* 40 */     super(se);
/* 41 */     this.base = base;
/* 42 */     this.field = field;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getValue() {
/* 47 */     return String.format("%s.%s", new Object[] { (this.base == null) ? "" : this.base, this.field });
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isSymbolic() {
/* 52 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isConstant() {
/* 57 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isMethodRepresentation() {
/* 62 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isObject() {
/* 67 */     return false;
/*    */   }
/*    */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/lu/uni/tsopen/symbolicExecution/symbolicValues/FieldValue.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */