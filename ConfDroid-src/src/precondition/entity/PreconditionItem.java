/*    */ package precondition.entity;
/*    */ 
/*    */ import androidsourcecode.branchedflow.MainSourceSinkScan;
/*    */ import java.util.Objects;
/*    */ import soot.SootMethod;
/*    */ import soot.Unit;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PreconditionItem
/*    */ {
/*    */   public String attr;
/*    */   public SootMethod mtd;
/*    */   private Unit unit;
/*    */   private String operator;
/*    */   private String value;
/*    */   public String type;
/*    */   private boolean branchValue;
/*    */   public int lineNumber;
/*    */   
/*    */   public PreconditionItem(Unit unit, SootMethod mtd, String attr, String operator, String value, boolean branchValue) {
/* 25 */     this.unit = unit;
/* 26 */     this.mtd = mtd;
/* 27 */     this.attr = attr;
/* 28 */     this.operator = operator;
/* 29 */     this.value = value;
/* 30 */     this.branchValue = branchValue;
/* 31 */     this.lineNumber = unit.getJavaSourceStartLineNumber();
/* 32 */     this.type = MainSourceSinkScan.getType(unit);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 37 */     String ret = "--precondition item--\n";
/* 38 */     ret = ret + "mtd: " + this.mtd + "\n";
/* 39 */     ret = ret + "unit: " + this.unit + "\n";
/* 40 */     ret = ret + "lineNumber: " + this.unit.getJavaSourceStartLineNumber() + "\n";
/* 41 */     ret = ret + "attr: " + this.attr + "\n";
/* 42 */     ret = ret + "operator: " + this.operator + "\n";
/* 43 */     ret = ret + "value: " + this.value + "\n";
/* 44 */     ret = ret + "branchValue: " + this.branchValue + "\n";
/* 45 */     return ret;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 50 */     return Objects.hash(new Object[] { this.attr, this.operator });
/*    */   }
/*    */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/precondition/entity/PreconditionItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */