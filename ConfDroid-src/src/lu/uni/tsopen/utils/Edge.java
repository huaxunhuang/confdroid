/*    */ package lu.uni.tsopen.utils;
/*    */ 
/*    */ import org.logicng.formulas.Formula;
/*    */ import soot.Unit;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Edge
/*    */ {
/*    */   private final Unit source;
/*    */   private final Unit target;
/*    */   private Formula predicate;
/*    */   
/*    */   public Edge(Unit s, Unit t) {
/* 48 */     this.source = s;
/* 49 */     this.target = t;
/* 50 */     this.predicate = null;
/*    */   }
/*    */   
/*    */   public Unit getSource() {
/* 54 */     return this.source;
/*    */   }
/*    */   
/*    */   public Unit getTarget() {
/* 58 */     return this.target;
/*    */   }
/*    */   
/*    */   public Formula getPredicate() {
/* 62 */     return this.predicate;
/*    */   }
/*    */   
/*    */   public void setPredicate(Formula predicate) {
/* 66 */     this.predicate = predicate;
/*    */   }
/*    */   
/*    */   public boolean correspondsTo(Unit source, Unit target) {
/* 70 */     return (source == this.source && target == this.target);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 75 */     return String.format("(%s) -> (%s)", new Object[] { this.source, this.target });
/*    */   }
/*    */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/lu/uni/tsopen/utils/Edge.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */