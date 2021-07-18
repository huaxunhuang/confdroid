/*    */ package lu.uni.tsopen;
/*    */ 
/*    */ import soot.jimple.toolkits.callgraph.CallGraph;
/*    */ 
/*    */ public class SimplifiedCallGraph
/*    */   extends CallGraph {
/*    */   private CallGraph originalCallGraph;
/*    */   
/*    */   public SimplifiedCallGraph(CallGraph cg) {
/* 10 */     this.originalCallGraph = cg;
/*    */   }
/*    */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/lu/uni/tsopen/SimplifiedCallGraph.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */