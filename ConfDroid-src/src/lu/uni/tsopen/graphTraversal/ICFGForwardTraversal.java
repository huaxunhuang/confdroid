/*    */ package lu.uni.tsopen.graphTraversal;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import java.util.List;
/*    */ import soot.SootMethod;
/*    */ import soot.Unit;
/*    */ import soot.jimple.infoflow.solver.cfg.InfoflowCFG;
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
/*    */ public abstract class ICFGForwardTraversal
/*    */   extends ICFGTraversal
/*    */ {
/*    */   public ICFGForwardTraversal(InfoflowCFG icfg, String nameOfAnalysis, SootMethod mainMethod) {
/* 46 */     super(icfg, nameOfAnalysis, mainMethod);
/*    */   }
/*    */ 
/*    */   
/*    */   public List<Unit> getNeighbors(Unit u) {
/* 51 */     return this.icfg.getSuccsOf(u);
/*    */   }
/*    */ 
/*    */   
/*    */   public Collection<Unit> getExtremities(SootMethod m) {
/* 56 */     return this.icfg.getStartPointsOf(m);
/*    */   }
/*    */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/lu/uni/tsopen/graphTraversal/ICFGForwardTraversal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */