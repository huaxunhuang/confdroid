/*     */ package lu.uni.tsopen.graphTraversal;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import lu.uni.tsopen.utils.Utils;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import soot.SootMethod;
/*     */ import soot.Unit;
/*     */ import soot.jimple.DefinitionStmt;
/*     */ import soot.jimple.infoflow.solver.cfg.InfoflowCFG;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class ICFGTraversal
/*     */   implements Runnable
/*     */ {
/*     */   protected final String nameOfAnalysis;
/*     */   protected final InfoflowCFG icfg;
/*     */   private List<SootMethod> visitedMethods;
/*     */   private LinkedList<SootMethod> methodWorkList;
/*     */   private Map<Unit, String> visitedNodes;
/*     */   private LinkedList<Unit> currentPath;
/*  65 */   protected Logger logger = LoggerFactory.getLogger(getClass());
/*     */   
/*     */   public ICFGTraversal(InfoflowCFG icfg, String nameOfAnalysis, SootMethod mainMethod) {
/*  68 */     this.nameOfAnalysis = nameOfAnalysis;
/*  69 */     this.icfg = icfg;
/*  70 */     this.visitedMethods = new LinkedList<>();
/*  71 */     this.methodWorkList = new LinkedList<>();
/*  72 */     this.visitedNodes = new HashMap<>();
/*  73 */     this.currentPath = new LinkedList<>();
/*  74 */     this.methodWorkList.add(mainMethod);
/*     */   }
/*     */ 
/*     */   
/*     */   public void run() {
/*  79 */     traverse();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void traverse() {
/*  87 */     SootMethod methodToAnalyze = null;
/*  88 */     Collection<Unit> extremities = null;
/*  89 */     while (!this.methodWorkList.isEmpty()) {
/*  90 */       methodToAnalyze = this.methodWorkList.removeFirst();
/*  91 */       this.visitedMethods.add(methodToAnalyze);
/*  92 */       extremities = getExtremities(methodToAnalyze);
/*  93 */       for (Unit extremity : extremities) {
/*  94 */         traverseNode(extremity);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void traverseNode(Unit node) {
/* 106 */     DefinitionStmt defUnit = null;
/* 107 */     if (checkNodeColor(node)) {
/* 108 */       this.currentPath.add(node);
/* 109 */       if (node instanceof soot.jimple.InvokeStmt) {
/* 110 */         propagateTargetMethod(node);
/* 111 */       } else if (node instanceof DefinitionStmt) {
/* 112 */         defUnit = (DefinitionStmt)node;
/* 113 */         if (defUnit.getRightOp() instanceof soot.jimple.InvokeExpr) {
/* 114 */           propagateTargetMethod((Unit)defUnit);
/*     */         }
/*     */       } 
/* 117 */       processNodeBeforeNeighbors(node);
/* 118 */       for (Unit neighbour : getNeighbors(node)) {
/* 119 */         traverseNode(neighbour);
/* 120 */         processNeighbor(node, neighbour);
/*     */       } 
/* 122 */       this.currentPath.removeLast();
/* 123 */       processNodeAfterNeighbors(node);
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean checkNodeColor(Unit node) {
/* 128 */     String nodeColor = this.visitedNodes.get(node);
/* 129 */     if (nodeColor == null) {
/* 130 */       nodeColor = "white";
/* 131 */       this.visitedNodes.put(node, nodeColor);
/*     */     } 
/* 133 */     if (nodeColor != "black") {
/* 134 */       if (nodeColor.equals("white")) {
/* 135 */         nodeColor = "grey";
/* 136 */       } else if (nodeColor.equals("grey")) {
/* 137 */         nodeColor = "black";
/*     */       } 
/* 139 */       this.visitedNodes.put(node, nodeColor);
/* 140 */       return true;
/*     */     } 
/* 142 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void propagateTargetMethod(Unit invocation) {
/* 152 */     Collection<SootMethod> pointsTo = Utils.getInvokedMethods(invocation, this.icfg);
/* 153 */     for (SootMethod callee : pointsTo) {
/* 154 */       if (callee.getDeclaringClass().isApplicationClass() && 
/* 155 */         !this.visitedMethods.contains(callee)) {
/* 156 */         this.methodWorkList.add(callee);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void addMethodToWorkList(SootMethod m) {
/* 163 */     if (!this.methodWorkList.contains(m)) {
/* 164 */       this.methodWorkList.add(m);
/*     */     } else {
/* 166 */       while (this.methodWorkList.contains(m)) {
/* 167 */         this.methodWorkList.remove(m);
/*     */       }
/* 169 */       this.methodWorkList.add(m);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isMethodVisited(SootMethod m) {
/* 174 */     return this.visitedMethods.contains(m);
/*     */   }
/*     */   
/*     */   public LinkedList<Unit> getCurrentPath() {
/* 178 */     return this.currentPath;
/*     */   }
/*     */   
/*     */   protected abstract void processNodeAfterNeighbors(Unit paramUnit);
/*     */   
/*     */   protected abstract void processNodeBeforeNeighbors(Unit paramUnit);
/*     */   
/*     */   protected abstract void processNeighbor(Unit paramUnit1, Unit paramUnit2);
/*     */   
/*     */   protected abstract List<Unit> getNeighbors(Unit paramUnit);
/*     */   
/*     */   protected abstract Collection<Unit> getExtremities(SootMethod paramSootMethod);
/*     */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/lu/uni/tsopen/graphTraversal/ICFGTraversal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */