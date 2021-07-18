/*     */ package lu.uni.tsopen.pathPredicateRecovery;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import lu.uni.tsopen.graphTraversal.ICFGForwardTraversal;
/*     */ import lu.uni.tsopen.utils.Edge;
/*     */ import lu.uni.tsopen.utils.Utils;
/*     */ import org.logicng.formulas.Formula;
/*     */ import org.logicng.formulas.FormulaFactory;
/*     */ import org.logicng.formulas.Literal;
/*     */ import soot.Local;
/*     */ import soot.Scene;
/*     */ import soot.SootClass;
/*     */ import soot.SootMethod;
/*     */ import soot.Unit;
/*     */ import soot.UnitPatchingChain;
/*     */ import soot.Value;
/*     */ import soot.jimple.AssignStmt;
/*     */ import soot.jimple.IfStmt;
/*     */ import soot.jimple.Stmt;
/*     */ import soot.jimple.SwitchStmt;
/*     */ import soot.jimple.infoflow.solver.cfg.InfoflowCFG;
/*     */ import soot.jimple.internal.JInstanceFieldRef;
/*     */ import soot.util.Chain;
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
/*     */ public class SimpleBlockPredicateExtraction
/*     */   extends ICFGForwardTraversal
/*     */ {
/*  59 */   private Map<Literal, Stmt> literalToCondition = null;
/*     */   private List<Stmt> conditions;
/*     */   private List<Edge> annotatedEdges;
/*     */   private final FormulaFactory formulaFactory;
/*     */   private List<Stmt> visitedIfs;
/*     */   private Map<SootMethod, Integer> countOfIfByMethod;
/*     */   private List<SootMethod> visitedMethods;
/*     */   private int countOfObject;
/*     */   private int maxIf;
/*     */   private Map<String, Value> fieldValueMap;
/*     */   
/*     */   public SimpleBlockPredicateExtraction(InfoflowCFG icfg, SootMethod mainMethod) {
/*  71 */     super(icfg, "Simple Block Predicate Extraction", mainMethod);
/*  72 */     this.literalToCondition = new HashMap<>();
/*  73 */     this.annotatedEdges = new ArrayList<>();
/*  74 */     this.formulaFactory = new FormulaFactory();
/*  75 */     this.conditions = new ArrayList<>();
/*  76 */     this.visitedIfs = new ArrayList<>();
/*  77 */     this.countOfIfByMethod = new HashMap<>();
/*  78 */     this.visitedMethods = new ArrayList<>();
/*  79 */     this.countOfObject = 0;
/*  80 */     this.maxIf = 0;
/*  81 */     this.fieldValueMap = new HashMap<>();
/*     */     
/*  83 */     Chain<SootClass> sootClasses = Scene.v().getClasses();
/*  84 */     for (SootClass cls : sootClasses) {
/*  85 */       if (!cls.getName().startsWith("android")) {
/*     */         continue;
/*     */       }
/*     */       
/*  89 */       List<SootMethod> methods = cls.getMethods();
/*  90 */       for (SootMethod method : methods) {
/*  91 */         if (method.hasActiveBody()) {
/*  92 */           UnitPatchingChain unitPatchingChain = method.retrieveActiveBody().getUnits();
/*  93 */           for (Unit unit : unitPatchingChain) {
/*     */             
/*  95 */             if (unit instanceof AssignStmt && unit.toString().startsWith("this")) {
/*  96 */               Value leftOp = ((AssignStmt)unit).getLeftOp();
/*  97 */               Value rightOp = ((AssignStmt)unit).getRightOp();
/*  98 */               if (leftOp instanceof JInstanceFieldRef) {
/*  99 */                 JInstanceFieldRef fieldRef = (JInstanceFieldRef)leftOp;
/* 100 */                 this.fieldValueMap.put(fieldRef.getFieldRef().toString(), rightOp);
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public Value getFieldValue(String field) {
/* 110 */     return this.fieldValueMap.get(field);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void annotateEdgeWithSimplePredicate(Unit node, Unit successor) {
/* 120 */     IfStmt ifStmt = null;
/* 121 */     SwitchStmt switchStmt = null;
/* 122 */     String condition = null;
/* 123 */     Edge edge = null;
/* 124 */     Literal simplePredicate = null;
/* 125 */     SootMethod method = this.icfg.getMethodOf(node);
/* 126 */     Integer countOfIfByMethod = null;
/*     */     
/* 128 */     if (!Utils.isCaughtException(successor)) {
/* 129 */       if (node instanceof IfStmt && !Utils.isDummy(this.icfg.getMethodOf(node))) {
/* 130 */         edge = new Edge(node, successor);
/* 131 */         this.annotatedEdges.add(edge);
/* 132 */         ifStmt = (IfStmt)node;
/* 133 */         condition = String.format("([%s] => %s)", new Object[] { this.icfg.getMethodOf((Unit)ifStmt).getDeclaringClass().toString(), ifStmt
/* 134 */               .getCondition().toString() });
/* 135 */         if (successor == ifStmt.getTarget()) {
/* 136 */           simplePredicate = this.formulaFactory.literal(condition, true);
/*     */         } else {
/* 138 */           simplePredicate = this.formulaFactory.literal(condition, false);
/*     */         } 
/* 140 */         this.literalToCondition.put(simplePredicate, ifStmt);
/* 141 */         if (!this.conditions.contains(ifStmt)) {
/* 142 */           this.conditions.add(ifStmt);
/*     */         }
/* 144 */         edge.setPredicate((Formula)simplePredicate);
/* 145 */         if (!this.visitedIfs.contains(ifStmt)) {
/* 146 */           this.visitedIfs.add(ifStmt);
/* 147 */           countOfIfByMethod = this.countOfIfByMethod.get(method);
/* 148 */           if (countOfIfByMethod == null) {
/* 149 */             this.countOfIfByMethod.put(method, Integer.valueOf(1));
/*     */           } else {
/* 151 */             countOfIfByMethod = Integer.valueOf(countOfIfByMethod.intValue() + 1);
/* 152 */             this.countOfIfByMethod.put(method, countOfIfByMethod);
/* 153 */             if (countOfIfByMethod.intValue() > this.maxIf) {
/* 154 */               this.maxIf = countOfIfByMethod.intValue();
/*     */             }
/*     */           } 
/*     */         } 
/* 158 */       } else if (node instanceof SwitchStmt && !Utils.isDummy(this.icfg.getMethodOf(node))) {
/* 159 */         switchStmt = (SwitchStmt)node;
/*     */         
/* 161 */         edge = new Edge(node, successor);
/* 162 */         this.annotatedEdges.add(edge);
/* 163 */         condition = null;
/* 164 */         if (switchStmt.getTargets().contains(successor)) {
/* 165 */           int index = switchStmt.getTargets().indexOf(successor);
/* 166 */           condition = String.format("([%s] => %s)", new Object[] { this.icfg.getMethodOf((Unit)switchStmt).getDeclaringClass(), switchStmt
/* 167 */                 .getKey().toString() + " == " + index });
/*     */           
/* 169 */           simplePredicate = this.formulaFactory.literal(condition, true);
/*     */         } 
/* 171 */         if (simplePredicate != null) {
/* 172 */           this.literalToCondition.put(simplePredicate, switchStmt);
/* 173 */           if (!this.conditions.contains(switchStmt)) {
/* 174 */             this.conditions.add(switchStmt);
/*     */           }
/* 176 */           edge.setPredicate((Formula)simplePredicate);
/* 177 */           if (!this.visitedIfs.contains(switchStmt)) {
/* 178 */             this.visitedIfs.add(switchStmt);
/* 179 */             countOfIfByMethod = this.countOfIfByMethod.get(method);
/* 180 */             if (countOfIfByMethod == null) {
/* 181 */               this.countOfIfByMethod.put(method, Integer.valueOf(1));
/*     */             } else {
/* 183 */               countOfIfByMethod = Integer.valueOf(countOfIfByMethod.intValue() + 1);
/* 184 */               this.countOfIfByMethod.put(method, countOfIfByMethod);
/* 185 */               if (countOfIfByMethod.intValue() > this.maxIf) {
/* 186 */                 this.maxIf = countOfIfByMethod.intValue();
/*     */               }
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 193 */       if (!this.visitedMethods.contains(method)) {
/* 194 */         this.visitedMethods.add(method);
/* 195 */         for (Local l : method.retrieveActiveBody().getLocals()) {
/* 196 */           if (l.getType() instanceof soot.RefLikeType) {
/* 197 */             this.countOfObject++;
/*     */           }
/*     */         } 
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
/*     */   
/*     */   public Edge getAnnotatedEdge(Unit source, Unit target) {
/* 212 */     for (Edge edge : this.annotatedEdges) {
/* 213 */       if (edge.correspondsTo(source, target)) {
/* 214 */         return edge;
/*     */       }
/*     */     } 
/* 217 */     return null;
/*     */   }
/*     */   
/*     */   public Stmt getCondtionFromLiteral(Literal l) {
/* 221 */     if (this.literalToCondition.containsKey(l)) {
/* 222 */       return this.literalToCondition.get(l);
/*     */     }
/* 224 */     return null;
/*     */   }
/*     */   
/*     */   public List<Stmt> getConditions() {
/* 228 */     return this.conditions;
/*     */   }
/*     */   
/*     */   public int getIfCount() {
/* 232 */     return this.visitedIfs.size();
/*     */   }
/*     */   
/*     */   public int getIfDepthInMethods() {
/* 236 */     return this.maxIf;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void processNeighbor(Unit node, Unit neighbour) {
/* 241 */     annotateEdgeWithSimplePredicate(node, neighbour);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void processNodeAfterNeighbors(Unit node) {}
/*     */ 
/*     */   
/*     */   protected void processNodeBeforeNeighbors(Unit node) {}
/*     */   
/*     */   public int getCountOfObject() {
/* 251 */     return this.countOfObject;
/*     */   }
/*     */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/lu/uni/tsopen/pathPredicateRecovery/SimpleBlockPredicateExtraction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */