/*    */ package callgraph.graph;
/*    */ 
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
/*    */ 
/*    */ public class Edge {
/*  7 */   public String srcSig = "";
/*  8 */   public String tgtSig = "";
/*    */   
/* 10 */   public Set<String> conditions = new HashSet<>();
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 15 */     return this.conditions + ":" + this.srcSig + "-->" + this.tgtSig;
/*    */   }
/*    */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/callgraph/graph/Edge.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */