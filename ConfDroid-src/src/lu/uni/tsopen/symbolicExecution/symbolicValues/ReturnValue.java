/*    */ package lu.uni.tsopen.symbolicExecution.symbolicValues;
/*    */ 
/*    */ import java.util.List;
/*    */ import lu.uni.tsopen.symbolicExecution.SymbolicExecution;
/*    */ import soot.Value;
/*    */ 
/*    */ public class ReturnValue
/*    */   extends AbstractSymbolicValue
/*    */ {
/*    */   Value v;
/*    */   
/*    */   public ReturnValue(Value v, SymbolicExecution se) {
/* 13 */     super(se);
/* 14 */     if (v != null) {
/* 15 */       this.v = v;
/* 16 */       if (!(v instanceof soot.jimple.Constant)) {
/* 17 */         this.values.put(v, getSymbolicValues(v));
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public String getValue() {
/* 24 */     List<SymbolicValue> symbolicValues = this.values.get(this.v);
/* 25 */     String ret = "RETURNV(" + this.v + ")";
/* 26 */     if (symbolicValues != null) {
/* 27 */       for (SymbolicValue symbolicValue : symbolicValues) {
/* 28 */         ret = ret + symbolicValue + " ";
/*    */       }
/*    */     }
/* 31 */     return ret;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isSymbolic() {
/* 36 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isConstant() {
/* 41 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isMethodRepresentation() {
/* 46 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isObject() {
/* 51 */     return true;
/*    */   }
/*    */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/lu/uni/tsopen/symbolicExecution/symbolicValues/ReturnValue.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */