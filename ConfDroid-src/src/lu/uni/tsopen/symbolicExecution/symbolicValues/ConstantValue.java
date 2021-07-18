/*    */ package lu.uni.tsopen.symbolicExecution.symbolicValues;
/*    */ 
/*    */ import lu.uni.tsopen.symbolicExecution.SymbolicExecution;
/*    */ import soot.jimple.Constant;
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
/*    */ public class ConstantValue
/*    */   extends ConcreteValue
/*    */ {
/*    */   private Constant constant;
/*    */   
/*    */   public ConstantValue(Constant c, SymbolicExecution se) {
/* 39 */     super(se);
/* 40 */     this.constant = c;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getValue() {
/* 45 */     return this.constant.toString().replace("\"", "").replace("\\", "");
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isConstant() {
/* 50 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isObject() {
/* 55 */     return false;
/*    */   }
/*    */   
/*    */   public Constant getConstant() {
/* 59 */     return this.constant;
/*    */   }
/*    */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/lu/uni/tsopen/symbolicExecution/symbolicValues/ConstantValue.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */