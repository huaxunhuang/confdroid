/*     */ package androidsourcecode.branchedflow;
/*     */ 
/*     */ import java.util.List;
/*     */ import soot.Body;
/*     */ import soot.SootMethod;
/*     */ import soot.Unit;
/*     */ import soot.jimple.IfStmt;
/*     */ import soot.jimple.Stmt;
/*     */ import soot.jimple.SwitchStmt;
/*     */ import soot.toolkits.graph.BriefUnitGraph;
/*     */ import soot.toolkits.graph.UnitGraph;
/*     */ import soot.toolkits.scalar.ForwardBranchedFlowAnalysis;
/*     */ 
/*     */ public class BranchedIntraFlowAnalysis
/*     */   extends ForwardBranchedFlowAnalysis<StringPredicate> {
/*     */   private final UnitGraph g;
/*     */   private int level;
/*     */   private StringPredicate initPredicate;
/*     */   
/*     */   public BranchedIntraFlowAnalysis(UnitGraph g) {
/*  21 */     super(g);
/*  22 */     this.g = g;
/*  23 */     this.level = 0;
/*  24 */     this.initPredicate = new StringPredicate();
/*  25 */     doAnalysis();
/*     */   }
/*     */   
/*     */   public BranchedIntraFlowAnalysis(UnitGraph g, int level, StringPredicate initPredicate) {
/*  29 */     super(g);
/*  30 */     this.g = g;
/*  31 */     this.level = level;
/*  32 */     this.initPredicate = initPredicate;
/*  33 */     doAnalysis();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void flowThrough(StringPredicate in, Unit unit, List<StringPredicate> fallOut, List<StringPredicate> branchOuts) {
/*  38 */     Stmt s = (Stmt)unit;
/*  39 */     for (StringPredicate p : fallOut)
/*  40 */       copy(in, p); 
/*  41 */     for (StringPredicate p : branchOuts) {
/*  42 */       copy(in, p);
/*     */     }
/*  44 */     if (s instanceof IfStmt) {
/*  45 */       IfStmt ifs = (IfStmt)s;
/*  46 */       for (StringPredicate p : branchOuts)
/*  47 */         p.put(ifs.getCondition().toString(), Boolean.valueOf(true)); 
/*  48 */       for (StringPredicate p : fallOut)
/*  49 */         p.put(ifs.getCondition().toString(), Boolean.valueOf(false)); 
/*  50 */     } else if (s instanceof SwitchStmt) {
/*     */       
/*  52 */       SwitchStmt switchStmt = (SwitchStmt)s;
/*     */     } 
/*     */ 
/*     */     
/*  56 */     System.out.println(s);
/*  57 */     System.out.println("in: " + in);
/*  58 */     System.out.println("fallOut: " + fallOut);
/*  59 */     System.out.println("branchOuts: " + branchOuts);
/*  60 */     System.out.println("succs: " + this.g.getSuccsOf(unit));
/*     */ 
/*     */     
/*  63 */     if (s.containsFieldRef()) {
/*  64 */       System.out.println("getFieldRef(): ");
/*  65 */       System.out.println(s.getFieldRef());
/*     */     } 
/*  67 */     System.out.println("getUseAndDefBoxes(): ");
/*  68 */     System.out.println(s.getUseAndDefBoxes());
/*     */     
/*  70 */     System.out.println();
/*  71 */     if (s.containsInvokeExpr()) {
/*  72 */       SootMethod invokedMtd = s.getInvokeExpr().getMethod();
/*  73 */       if (invokedMtd.hasActiveBody()) {
/*  74 */         Body mBody = invokedMtd.retrieveActiveBody();
/*  75 */         BriefUnitGraph briefUnitGraph = new BriefUnitGraph(mBody);
/*  76 */         new BranchedIntraFlowAnalysis((UnitGraph)briefUnitGraph, this.level + 1, in);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void copy(StringPredicate source, StringPredicate dest) {
/*  83 */     dest.copy(source);
/*     */   }
/*     */   
/*     */   protected void merge(StringPredicate in1, StringPredicate in2, StringPredicate out) {
/*  87 */     out.copy(in1).merge(in2);
/*     */   }
/*     */ 
/*     */   
/*     */   protected StringPredicate entryInitialFlow() {
/*  92 */     return this.initPredicate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected StringPredicate newInitialFlow() {
/* 100 */     StringPredicate predicate = new StringPredicate();
/*     */     
/* 102 */     return predicate;
/*     */   }
/*     */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/androidsourcecode/branchedflow/BranchedIntraFlowAnalysis.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */