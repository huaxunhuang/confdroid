/*     */ package precondition.entity;
/*     */ 
/*     */ import androidsourcecode.AndroidSourceCodeUtil;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import soot.SootMethod;
/*     */ import soot.Unit;
/*     */ 
/*     */ public class Precondition
/*     */ {
/*     */   public int apiLevel;
/*     */   public String attr;
/*     */   public int lineNumber;
/*     */   private List<Unit> units;
/*     */   public SootMethod mtd;
/*     */   public String cls;
/*     */   public String type;
/*  20 */   public List<PreconditionItem> itemList = new ArrayList<>();
/*     */   
/*     */   public Precondition clone() {
/*  23 */     return new Precondition(this.attr, this.lineNumber, this.units, this.mtd, this.type, this.apiLevel, this.itemList);
/*     */   }
/*     */   
/*     */   public Precondition(String attr, int lineNumber, List<Unit> units, SootMethod mtd, String type, int apiLevel, List<PreconditionItem> list) {
/*  27 */     this.apiLevel = apiLevel;
/*  28 */     this.lineNumber = lineNumber;
/*  29 */     this.attr = attr;
/*  30 */     this.units = units;
/*  31 */     this.mtd = mtd;
/*  32 */     this.type = type;
/*  33 */     this.cls = mtd.getDeclaringClass().getName();
/*  34 */     this.itemList.addAll(list);
/*     */   }
/*     */   
/*     */   public Precondition(String attr, int lineNumber, List<Unit> units, SootMethod mtd, String type, int apiLevel) {
/*  38 */     this.apiLevel = apiLevel;
/*  39 */     this.lineNumber = lineNumber;
/*  40 */     this.attr = attr;
/*  41 */     this.units = units;
/*  42 */     this.mtd = mtd;
/*  43 */     this.type = type;
/*  44 */     this.cls = mtd.getDeclaringClass().getName();
/*     */   }
/*     */   
/*     */   public String getStmt() {
/*     */     try {
/*  49 */       return AndroidSourceCodeUtil.readSourceCodeByLineNumber(this.apiLevel, this.mtd, this.lineNumber);
/*  50 */     } catch (FileNotFoundException e) {
/*  51 */       e.printStackTrace();
/*     */       
/*  53 */       return "";
/*     */     } 
/*     */   }
/*     */   public void removeLine(int lineNumber) {
/*  57 */     List<PreconditionItem> removeList = new ArrayList<>();
/*  58 */     for (PreconditionItem item : this.itemList) {
/*  59 */       if (item.lineNumber == lineNumber) {
/*  60 */         removeList.add(item);
/*     */       }
/*     */     } 
/*  63 */     this.itemList.removeAll(removeList);
/*     */   }
/*     */   
/*     */   public void addAll(List<PreconditionItem> items) {
/*  67 */     this.itemList.addAll(items);
/*     */   }
/*     */   public void removeAll(List<PreconditionItem> items) {
/*  70 */     this.itemList.removeAll(items);
/*     */   }
/*     */   public void addPreconditionItem(PreconditionItem item) {
/*  73 */     this.itemList.add(item);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  78 */     String ret = "=== begin precondition ===\n";
/*  79 */     ret = ret + "apiLevel: " + this.apiLevel + "\n";
/*  80 */     ret = ret + "attr: " + this.attr + "\n";
/*  81 */     ret = ret + "type: " + this.type + "\n";
/*  82 */     ret = ret + "lineNumber: " + this.lineNumber + "\n";
/*     */     try {
/*  84 */       ret = ret + "stmt: " + AndroidSourceCodeUtil.readSourceCodeByLineNumber(this.apiLevel, this.mtd, this.lineNumber) + "\n";
/*  85 */     } catch (FileNotFoundException e) {
/*  86 */       e.printStackTrace();
/*     */     } 
/*  88 */     ret = ret + "mtd: " + this.mtd.getSignature() + "\n";
/*  89 */     for (PreconditionItem item : this.itemList) {
/*  90 */       ret = ret + item.toString();
/*     */     }
/*  92 */     ret = ret + "=== end precondition ===\n";
/*  93 */     return ret;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  98 */     int hash1 = Objects.hash(new Object[] { this.attr, this.type, this.cls });
/*  99 */     int hash2 = 0;
/* 100 */     for (PreconditionItem item : this.itemList) {
/* 101 */       hash2 += item.hashCode();
/*     */     }
/* 103 */     return (hash1 + hash2) % 31;
/*     */   }
/*     */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/precondition/entity/Precondition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */