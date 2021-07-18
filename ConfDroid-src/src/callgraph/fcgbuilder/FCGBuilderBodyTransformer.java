/*     */ package callgraph.fcgbuilder;
/*     */ 
/*     */ import callgraph.graph.Edge;
/*     */ import callgraph.graph.FrameworkCallGraph;
/*     */ import callgraph.graph.FrameworkCallGraphInApiLevel;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import soot.Body;
/*     */ import soot.SootClass;
/*     */ import soot.SootMethod;
/*     */ import soot.Unit;
/*     */ import soot.jimple.Stmt;
/*     */ import soot.toolkits.graph.ExceptionalUnitGraph;
/*     */ import utils.SootUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FCGBuilderBodyTransformer
/*     */ {
/*     */   public static void scan(Body b, int apiLevel) {
/*  24 */     if (!b.getMethod().getDeclaringClass().getName().startsWith("android.support")) {
/*     */       
/*  26 */       ExceptionalUnitGraph graph = new ExceptionalUnitGraph(b);
/*     */       
/*  28 */       for (Unit unit : graph.getHeads())
/*     */       {
/*  30 */         traverse(b, graph, unit, new HashSet<>(), apiLevel); } 
/*     */     } 
/*     */   }
/*     */   
/*     */   static void traverse(Body b, ExceptionalUnitGraph graph, Unit unit, Set<Unit> visitedUnits, int apiLevel) {
/*     */     List<Unit> succUnits;
/*  36 */     if (visitedUnits.contains(unit)) {
/*     */       return;
/*     */     }
/*  39 */     visitedUnits.add(unit);
/*     */ 
/*     */ 
/*     */     
/*  43 */     Stmt stmt = (Stmt)unit;
/*     */     
/*  45 */     FrameworkCallGraphInApiLevel fcg = FrameworkCallGraph.getFCG(apiLevel);
/*     */ 
/*     */     
/*     */     while (true) {
/*  49 */       if (stmt.containsInvokeExpr()) {
/*  50 */         Edge edge = fcg.getEdge(b.getMethod().getSignature(), stmt.getInvokeExpr().getMethod().getSignature());
/*     */         
/*  52 */         fcg.addEdge(edge);
/*  53 */         FrameworkCallGraph.updateFCG(fcg, apiLevel);
/*     */         
/*  55 */         if (stmt.getInvokeExpr() instanceof soot.jimple.InterfaceInvokeExpr) {
/*  56 */           SootMethod sootMethod = stmt.getInvokeExpr().getMethod();
/*     */           
/*  58 */           if (sootMethod.getDeclaration().contains("private")) {
/*     */             continue;
/*     */           }
/*     */ 
/*     */ 
/*     */           
/*  64 */           SootClass sootClass = sootMethod.getDeclaringClass();
/*  65 */           Set<SootClass> subClasses = SootUtils.getAllSubClasses(sootClass);
/*     */           
/*  67 */           for (SootClass subClass : subClasses) {
/*     */             
/*  69 */             Edge e = fcg.getEdge(edge.srcSig, edge.tgtSig.replace(sootClass.getName() + ":", subClass.getName() + ":"));
/*  70 */             e.conditions.addAll(edge.conditions);
/*     */             
/*  72 */             fcg.addEdge(e);
/*  73 */             FrameworkCallGraph.updateFCG(fcg, apiLevel);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/*  78 */       succUnits = graph.getSuccsOf((Unit)stmt);
/*  79 */       if (succUnits.size() == 1) {
/*     */         
/*  81 */         stmt = (Stmt)succUnits.get(0);
/*     */         
/*  83 */         if (stmt instanceof soot.jimple.ReturnStmt) {
/*     */           return;
/*     */         }
/*     */ 
/*     */         
/*  88 */         if (visitedUnits.contains(stmt)) {
/*     */           return;
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*  94 */         visitedUnits.add(stmt); continue;
/*     */       }  break;
/*     */     } 
/*  97 */     if (succUnits.size() == 0) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 108 */     for (Unit u : succUnits)
/*     */     {
/* 110 */       traverse(b, graph, u, visitedUnits, apiLevel);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/callgraph/fcgbuilder/FCGBuilderBodyTransformer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */