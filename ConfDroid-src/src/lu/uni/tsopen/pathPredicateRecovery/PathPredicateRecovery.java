/*     */ package lu.uni.tsopen.pathPredicateRecovery;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import lu.uni.tsopen.graphTraversal.ICFGBackwardTraversal;
/*     */ import lu.uni.tsopen.utils.Edge;
/*     */ import lu.uni.tsopen.utils.Utils;
/*     */ import org.javatuples.Pair;
/*     */ import org.logicng.formulas.Formula;
/*     */ import org.logicng.formulas.FormulaFactory;
/*     */ import org.logicng.formulas.Literal;
/*     */ import org.logicng.transformations.simplification.DistributiveSimplifier;
import soot.SootMethod;
/*     */ import soot.Unit;
/*     */ import soot.jimple.IfStmt;
/*     */ import soot.jimple.Stmt;
/*     */ import soot.jimple.SwitchStmt;
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
/*     */ public class PathPredicateRecovery
/*     */   extends ICFGBackwardTraversal
/*     */ {
/*     */   private final SimpleBlockPredicateExtraction sbpe;
/*     */   private Map<Unit, List<Formula>> nodeToPathPredicates;
/*     */   private Map<Unit, Formula> nodeToFullPathPredicate;
/*     */   private final FormulaFactory formulaFactory;
/*     */   private final DistributiveSimplifier simplifier;
/*     */   private final boolean handleExceptions;
/*     */   private Map<Literal, List<Unit>> guardedBlocks;
/*  58 */   private Set<Pair<SwitchStmt, Stmt>> visitedSwitchNodeNeighbour = new HashSet<>();
/*     */   
/*     */   public Map<Unit, Formula> getNodeToFullPathPredicate() {
/*  61 */     return this.nodeToFullPathPredicate;
/*     */   }
/*     */   
/*     */   public Map<Unit, List<Formula>> getNodeToPathPredicates() {
/*  65 */     return this.nodeToPathPredicates;
/*     */   }
/*     */   
/*     */   public PathPredicateRecovery(InfoflowCFG icfg, SimpleBlockPredicateExtraction sbpe, SootMethod mainMethod, boolean handleExceptions) {
/*  69 */     super(icfg, "Path Predicate Recovery", mainMethod);
/*  70 */     this.sbpe = sbpe;
/*  71 */     this.nodeToPathPredicates = new HashMap<>();
/*  72 */     this.nodeToFullPathPredicate = new HashMap<>();
/*  73 */     this.formulaFactory = new FormulaFactory();
/*  74 */     this.simplifier = new DistributiveSimplifier();
/*  75 */     this.handleExceptions = handleExceptions;
/*  76 */     this.guardedBlocks = new HashMap<>();
/*     */   }
/*     */   
/*     */   private void annotateNodeWithPathPredicate(Unit node, Unit neighbour) {
/*  80 */     if (neighbour instanceof SwitchStmt && this.visitedSwitchNodeNeighbour
/*  81 */       .contains(new Pair(neighbour, node))) {
/*     */       return;
/*     */     }
/*  84 */     Edge edge = this.sbpe.getAnnotatedEdge(neighbour, node);
/*  85 */     Formula currentPathPredicate = null;
/*  86 */     Formula neighborPathPredicate = this.nodeToFullPathPredicate.get(neighbour);
/*  87 */     List<Formula> nodePredicates = this.nodeToPathPredicates.get(node);
/*  88 */     if (edge != null && edge.getPredicate() != null) {
/*  89 */       if (neighborPathPredicate != null) {
/*  90 */         currentPathPredicate = this.formulaFactory.and(new Formula[] { edge.getPredicate(), neighborPathPredicate });
/*     */       } else {
/*  92 */         currentPathPredicate = edge.getPredicate();
/*     */       } 
/*     */     } else {
/*  95 */       currentPathPredicate = neighborPathPredicate;
/*     */     } 
/*  97 */     if (currentPathPredicate != null) {
/*  98 */       if (nodePredicates == null) {
/*  99 */         nodePredicates = new ArrayList<>();
/* 100 */         this.nodeToPathPredicates.put(node, nodePredicates);
/*     */       } 
/* 102 */       nodePredicates.add(currentPathPredicate);
/*     */     } 
/* 104 */     if (neighbour instanceof SwitchStmt) {
/* 105 */       this.visitedSwitchNodeNeighbour.add(new Pair(neighbour, node));
/*     */     }
/*     */   }
/*     */   
/*     */   public Formula getNodeFullPath(Unit node) {
/* 110 */     if (this.nodeToFullPathPredicate.containsKey(node)) {
/* 111 */       return this.nodeToFullPathPredicate.get(node);
/*     */     }
/* 113 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void processNeighbor(Unit node, Unit neighbour) {
/* 119 */     if (this.handleExceptions || !Utils.isCaughtException(node)) {
/* 120 */       annotateNodeWithPathPredicate(node, neighbour);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void processNodeAfterNeighbors(Unit node) {
/* 129 */     List<Formula> nodePredicates = this.nodeToPathPredicates.get(node);
/* 130 */     Formula simplifiedPredicate = null;
/* 131 */     Formula or = null;
/* 132 */     if (nodePredicates != null) {
/* 133 */       or = this.formulaFactory.or(nodePredicates);
/* 134 */       simplifiedPredicate = this.simplifier.apply(or, true);
/*     */       
/* 136 */       this.nodeToFullPathPredicate.put(node, simplifiedPredicate);
/* 137 */       computeGuardedBlocks(node);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void computeGuardedBlocks(Unit node) {
/* 142 */     Formula fullPath = getNodeFullPath(node);
/*     */     
/* 144 */     if (fullPath != null) {
/* 145 */       for (Literal lit : fullPath.literals()) {
/* 146 */         List<Unit> blocks = this.guardedBlocks.get(lit);
/* 147 */         if (lit != null) {
/* 148 */           if (blocks == null) {
/* 149 */             blocks = new ArrayList<>();
/* 150 */             this.guardedBlocks.put(lit, blocks);
/*     */           } 
/* 152 */           if (!blocks.contains(node)) {
/* 153 */             blocks.add(node);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public List<Unit> getGuardedBlocks(IfStmt ifStmt) {
/* 161 */     if (this.guardedBlocks.containsKey(ifStmt)) {
/* 162 */       return this.guardedBlocks.get(ifStmt);
/*     */     }
/* 164 */     return Collections.emptyList();
/*     */   }
/*     */   
/*     */   protected void processNodeBeforeNeighbors(Unit node) {}
/*     */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/lu/uni/tsopen/pathPredicateRecovery/PathPredicateRecovery.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */