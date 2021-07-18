/*    */ package lu.uni.tsopen.symbolicExecution.symbolicValues;
/*    */ 
/*    */ import java.util.List;
/*    */ import lu.uni.tsopen.symbolicExecution.SymbolicExecution;
/*    */ import soot.Value;
/*    */ 
/*    */ 
/*    */ public class SingleVariableValue
/*    */   extends AbstractSymbolicValue
/*    */ {
/*    */   Value v;
/*    */   
/*    */   public SingleVariableValue(Value v, SymbolicExecution se) {
/* 14 */     super(se);
/* 15 */     if (v != null) {
/* 16 */       this.v = v;
/* 17 */       if (!(v instanceof soot.jimple.Constant)) {
/* 18 */         this.values.put(v, getSymbolicValues(v));
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public String getValue() {
/* 25 */     List<SymbolicValue> symbolicValues = this.values.get(this.v);
/* 26 */     String ret = "SINGLEV(" + this.v + ")";
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 32 */     return ret;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isSymbolic() {
/* 37 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isConstant() {
/* 42 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isMethodRepresentation() {
/* 47 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isObject() {
/* 52 */     return true;
/*    */   }
/*    */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/lu/uni/tsopen/symbolicExecution/symbolicValues/SingleVariableValue.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */