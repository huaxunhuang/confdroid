/*    */ package androidsourcecode.branchedflow.execorder;
/*    */ 
/*    */ import soot.Body;
/*    */ import soot.SootClass;
/*    */ import soot.SootMethod;
/*    */ import soot.Unit;
/*    */ import soot.toolkits.graph.DirectedGraph;
/*    */ import soot.toolkits.graph.ExceptionalUnitGraph;
/*    */ import soot.toolkits.graph.InverseGraph;
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
/*    */ public class ExecutionOrder
/*    */ {
/*    */   public static void analyzeExecutionOrder(SootMethod mtd) {
/* 27 */     SootClass cls = mtd.getDeclaringClass();
/* 28 */     Body mtdActiveBody = mtd.getActiveBody();
/*    */ 
/*    */     
/* 31 */     SootMethod cinit = null;
/* 32 */     for (SootMethod m : cls.getMethods()) {
/* 33 */       if (m.getSignature().contains("<cinit>")) {
/* 34 */         cinit = m;
/*    */         
/*    */         break;
/*    */       } 
/*    */     } 
/* 39 */     Body cinitActiveBody = cinit.getActiveBody();
/* 40 */     InverseGraph<Unit> cinitUnitGraph = new InverseGraph((DirectedGraph)new ExceptionalUnitGraph(cinitActiveBody));
/* 41 */     InverseGraph<Unit> mtdUnitGraph = new InverseGraph((DirectedGraph)new ExceptionalUnitGraph(mtdActiveBody));
/*    */   }
/*    */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/androidsourcecode/branchedflow/execorder/ExecutionOrder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */