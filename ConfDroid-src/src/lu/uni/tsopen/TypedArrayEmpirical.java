/*    */ package lu.uni.tsopen;
/*    */ import java.util.*;
/*    */
/*    */
/*    */ import soot.*;
/*    */
/*    */
import soot.jimple.Stmt;
/*    */ import soot.jimple.toolkits.callgraph.CallGraph;
/*    */ import soot.jimple.toolkits.callgraph.Edge;
/*    */ 
/*    */ public class TypedArrayEmpirical {
/* 12 */   Set<SootMethod> typedArrayInvokedMtd = new HashSet<>();
/*    */   
/*    */   public void scanTypedArray() {
/* 15 */     System.out.println("Empirical study on how many methods invoke TypedArray");
/* 16 */     CallGraph cg = Scene.v().getCallGraph();
/* 17 */     for (SootClass cls : Scene.v().getApplicationClasses()) {
/* 18 */       for (SootMethod mtd : cls.getMethods()) {
/* 19 */         if (mtd.hasActiveBody()) {
/* 20 */           UnitPatchingChain unitPatchingChain = mtd.getActiveBody().getUnits();
/* 21 */           for (Unit unit : unitPatchingChain) {
/* 22 */             Stmt stmt = (Stmt)unit;
/* 23 */             if (stmt.containsInvokeExpr() && 
/* 24 */               stmt.getInvokeExpr().getMethod().toString().contains(".TypedArray"))
/*    */             {
/*    */               
/* 27 */               this.typedArrayInvokedMtd.add(mtd);
/*    */             }
/*    */           } 
/*    */         } 
/*    */       } 
/*    */     } 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 37 */     analyzeTypedArrayInvokedMtd();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private void analyzeTypedArrayInvokedMtd() {
/* 43 */     CallGraph cg = Scene.v().getCallGraph();
/* 44 */     for (SootMethod mtd1 : this.typedArrayInvokedMtd) {
/* 45 */       Queue<SootMethod> q = new LinkedList<>();
/* 46 */       Set<SootMethod> visited = new HashSet<>();
/* 47 */       q.add(mtd1);
/*    */       
/* 49 */       while (!q.isEmpty()) {
/* 50 */         SootMethod topMtd = q.poll();
/* 51 */         visited.add(topMtd);
/* 52 */         for (Iterator<Edge> iter = cg.edgesInto((MethodOrMethodContext)topMtd); iter.hasNext(); ) {
/* 53 */           Edge e = iter.next();
/* 54 */           System.out.println("e.src() = " + e.src());
/* 55 */           if (this.typedArrayInvokedMtd.contains(e.src())) {
/* 56 */             System.out.println(mtd1 + " can be transtively invoked by another");
/*    */             break;
/*    */           } 
/* 59 */           if (!visited.contains(e.src())) {
/* 60 */             q.add(e.src());
/*    */           }
/*    */         } 
/*    */       } 
/* 64 */       System.out.println(mtd1 + " is an \"entry\" method.");
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/lu/uni/tsopen/TypedArrayEmpirical.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */