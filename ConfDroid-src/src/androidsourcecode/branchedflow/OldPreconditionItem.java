/*    */ package androidsourcecode.branchedflow;
/*    */ 
/*    */ import soot.SootMethod;
/*    */ import soot.Unit;
/*    */ 
/*    */ 
/*    */ public class OldPreconditionItem
/*    */ {
/*    */   int apiLevel;
/*    */   int depth;
/*    */   Unit unit;
/*    */   int lineNum;
/*    */   SootMethod mtd;
/*    */   String stmt;
/*    */   String stackInfo;
/*    */   String type;
/*    */   String value;
/*    */   String attr;
/*    */   String themeType;
/*    */   
/*    */   public OldPreconditionItem(int apiLevel, int depth, Unit unit, int lineNum, SootMethod mtd, String stmt, String stackInfo, String type, String value, String attr, String themeType) {
/* 22 */     this.apiLevel = apiLevel;
/* 23 */     this.depth = depth;
/* 24 */     this.unit = unit;
/* 25 */     this.lineNum = lineNum;
/* 26 */     this.mtd = mtd;
/* 27 */     this.stmt = stmt;
/* 28 */     this.stackInfo = stackInfo;
/* 29 */     this.type = type;
/* 30 */     this.value = value;
/* 31 */     this.attr = attr;
/* 32 */     this.themeType = themeType;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 38 */     String ret = "";
/* 39 */     ret = ret + "==== find a potential source ====\n";
/* 40 */     ret = ret + "api level: " + this.apiLevel + "\n";
/* 41 */     ret = ret + "depth: " + this.depth + "\n";
/* 42 */     ret = ret + "potentialSource.unit: " + this.unit + "\n";
/* 43 */     ret = ret + "potentialSource.lineNum: " + this.lineNum + "\n";
/* 44 */     ret = ret + "potentialSource.mtd: " + this.mtd + "\n";
/* 45 */     ret = ret + "potentialSource.stmt: " + this.stmt + "\n";
/* 46 */     ret = ret + "potentialSource.type: " + this.type + "\n";
/* 47 */     ret = ret + "potentialSource.value: " + this.value + "\n";
/* 48 */     ret = ret + "potentialSource.attr: " + this.attr + "\n";
/* 49 */     ret = ret + "potentialSource.stack: \n ";
/* 50 */     ret = ret + this.stackInfo + "\n";
/* 51 */     ret = ret + "potentialSource.themeType: " + this.themeType + "\n";
/* 52 */     ret = ret + "=== end find a potential source ===";
/* 53 */     return ret;
/*    */   }
/*    */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/androidsourcecode/branchedflow/OldPreconditionItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */