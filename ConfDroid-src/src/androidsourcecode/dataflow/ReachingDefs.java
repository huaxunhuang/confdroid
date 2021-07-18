///*     */ package androidsourcecode.dataflow;
///*     */ import heros.FlowFunction;
///*     */ import heros.InterproceduralCFG;
///*     */ import java.util.Collections;
///*     */ import java.util.LinkedHashSet;
///*     */ import java.util.List;
///*     */ import java.util.Set;
///*     */ import heros.fieldsens.FlowFunctions;
//import soot.Local;
///*     */ import soot.SootMethod;
///*     */ import soot.Unit;
///*     */ import soot.Value;
///*     */ import soot.jimple.AssignStmt;
///*     */ import soot.jimple.DefinitionStmt;
///*     */ import soot.jimple.ReturnStmt;
///*     */ import soot.jimple.Stmt;
///*     */ import soot.jimple.toolkits.ide.DefaultJimpleIFDSTabulationProblem;
//import soot.toolkits.scalar.Pair;
///*     */
///*     */ public class ReachingDefs extends DefaultJimpleIFDSTabulationProblem<Pair<Value, Set<DefinitionStmt>>, InterproceduralCFG<Unit, SootMethod>> {
///*     */   public ReachingDefs(InterproceduralCFG<Unit, SootMethod> icfg) {
///*  20 */     super(icfg);
///*  21 */     this.icfg = icfg;
///*     */   }
///*     */   private InterproceduralCFG<Unit, SootMethod> icfg;
///*     */   public FlowFunctions<Unit, Pair<Value, Set<DefinitionStmt>>, SootMethod> createFlowFunctionsFactory() {
///*  25 */     return new FlowFunctions<Unit, Pair<Value, Set<DefinitionStmt>>, SootMethod>()
///*     */       {
///*     */         public FlowFunction<Pair<Value, Set<DefinitionStmt>>> getNormalFlowFunction(Unit curr, Unit succ)
///*     */         {
///*  29 */           System.out.println("ReachingDefs.getNormalFlowFunction");
///*  30 */           System.out.println("curr = " + curr);
///*  31 */           System.out.println("icfg.getMethodOf(curr) = " + ReachingDefs.this.icfg.getMethodOf(curr));
///*  32 */           System.out.println("succ = " + succ);
///*  33 */           if (((SootMethod)ReachingDefs.this.icfg.getMethodOf(curr)).toString().startsWith("java.") || (
///*  34 */             (SootMethod)ReachingDefs.this.icfg.getMethodOf(curr)).toString().startsWith("javax.") || (
///*  35 */             (SootMethod)ReachingDefs.this.icfg.getMethodOf(curr)).toString().startsWith("sun.")) {
///*  36 */             return (FlowFunction<Pair<Value, Set<DefinitionStmt>>>)Identity.v();
///*     */           }
///*  38 */           if (curr instanceof DefinitionStmt) {
///*  39 */             DefinitionStmt assignment = (DefinitionStmt)curr;
///*     */
///*  41 */             return source -> {
///*     */                 if (source != ReachingDefs.this.zeroValue()) {
///*     */                   return ((Value)source.getO1()).equivTo(assignment.getLeftOp()) ? Collections.emptySet() : Collections.singleton(source);
///*     */                 }
///*     */
///*     */
///*     */                 LinkedHashSet<Pair<Value, Set<DefinitionStmt>>> res = new LinkedHashSet<>();
///*     */
///*     */                 System.out.println("assignment.getLeftOp().hashCode() = " + assignment.getLeftOp().hashCode());
///*     */
///*     */                 res.add(new Pair(assignment.getLeftOp(), Collections.singleton(assignment)));
///*     */
///*     */                 return res;
///*     */               };
///*     */           }
///*     */
///*  57 */           return (FlowFunction<Pair<Value, Set<DefinitionStmt>>>)Identity.v();
///*     */         }
///*     */
///*     */
///*     */
///*     */         public FlowFunction<Pair<Value, Set<DefinitionStmt>>> getCallFlowFunction(Unit callStmt, SootMethod destinationMethod) {
///*  63 */           System.out.println("ReachingDefs.getCallFlowFunction");
///*  64 */           System.out.println("unit = " + callStmt);
///*  65 */           System.out.println("sootMethod = " + destinationMethod);
///*  66 */           String clsStr = destinationMethod.getDeclaringClass().toString();
///*  67 */           if (clsStr.startsWith("java.") || clsStr.startsWith("sun.") || clsStr.startsWith("javax.")) {
///*  68 */             return new FlowFunction<Pair<Value, Set<DefinitionStmt>>>()
///*     */               {
///*     */                 public Set<Pair<Value, Set<DefinitionStmt>>> computeTargets(Pair<Value, Set<DefinitionStmt>> valueSetPair) {
///*  71 */                   return Collections.emptySet();
///*     */                 }
///*     */               };
///*     */           }
///*  75 */           Stmt stmt = (Stmt)callStmt;
///*  76 */           InvokeExpr invokeExpr = stmt.getInvokeExpr();
///*  77 */           List<Value> args = invokeExpr.getArgs();
///*     */
///*  79 */           List<Local> localArguments = new ArrayList<>(args.size());
///*  80 */           for (Value value : args) {
///*  81 */             if (value instanceof Local) {
///*  82 */               localArguments.add((Local)value); continue;
///*     */             }
///*  84 */             localArguments.add(null);
///*     */           }
///*     */
///*     */
///*  88 */           return source -> {
///*     */               if (!destinationMethod.getName().equals("<clinit>") && !destinationMethod.getSubSignature().contains("void run()") && !destinationMethod.toString().contains("Object run()") && localArguments.contains(source.getO1())) {
///*     */                 int paramIndex = args.indexOf(source.getO1());
///*     */                 System.out.println("destinationMethod = " + destinationMethod.toString());
///*     */                 System.out.println("destinationMethod.getParameterType(paramIndex) = " + destinationMethod.getParameterType(paramIndex));
///*     */                 Pair<Value, Set<DefinitionStmt>> pair = new Pair(new EquivalentValue((Value)Jimple.v().newParameterRef(destinationMethod.getParameterType(paramIndex), paramIndex)), source.getO2());
///*     */                 return Collections.singleton(pair);
///*     */               }
///*     */               return Collections.emptySet();
///*     */             };
///*     */         }
///*     */
///*     */
///*     */
///*     */
///*     */
///*     */
///*     */
///*     */
///*     */
///*     */
///*     */
///*     */         public FlowFunction<Pair<Value, Set<DefinitionStmt>>> getReturnFlowFunction(Unit callSite, SootMethod calleeMethod, Unit exitStmt, Unit returnSite) {
///* 111 */           System.out.println("ReachingDefs.getReturnFlowFunction");
///* 112 */           System.out.println("callSite = " + callSite);
///* 113 */           System.out.println("calleeMethod = " + calleeMethod);
///* 114 */           System.out.println("exitStmt = " + exitStmt);
///* 115 */           System.out.println("returnSite = " + returnSite);
///* 116 */           if (!(callSite instanceof DefinitionStmt)) {
///* 117 */             return (FlowFunction<Pair<Value, Set<DefinitionStmt>>>)KillAll.v();
///*     */           }
///*     */
///* 120 */           if (exitStmt instanceof soot.jimple.ReturnVoidStmt) {
///* 121 */             return (FlowFunction<Pair<Value, Set<DefinitionStmt>>>)KillAll.v();
///*     */           }
///*     */
///* 124 */           return source -> {
///*     */               if (exitStmt instanceof ReturnStmt) {
///*     */                 ReturnStmt returnStmt = (ReturnStmt)exitStmt;
///*     */                 if (returnStmt.getOp().equivTo(source.getO1())) {
///*     */                   AssignStmt definitionStmt = (AssignStmt)callSite;
///*     */                   Pair<Value, Set<DefinitionStmt>> pair = new Pair(definitionStmt.getLeftOp(), source.getO2());
///*     */                   return Collections.singleton(pair);
///*     */                 }
///*     */               }
///*     */               return Collections.emptySet();
///*     */             };
///*     */         }
///*     */
///*     */
///*     */
///*     */         public FlowFunction<Pair<Value, Set<DefinitionStmt>>> getCallToReturnFlowFunction(Unit callSite, Unit returnSite) {
///* 140 */           System.out.println("ReachingDefs.getCallToReturnFlowFunction");
///* 141 */           System.out.println("callSite = " + callSite);
///* 142 */           System.out.println("returnSite = " + returnSite);
///* 143 */           if (!(callSite instanceof DefinitionStmt)) {
///* 144 */             return (FlowFunction<Pair<Value, Set<DefinitionStmt>>>)Identity.v();
///*     */           }
///*     */
///* 147 */           final DefinitionStmt definitionStmt = (DefinitionStmt)callSite;
///* 148 */           return new FlowFunction<Pair<Value, Set<DefinitionStmt>>>()
///*     */             {
///*     */               public Set<Pair<Value, Set<DefinitionStmt>>> computeTargets(Pair<Value, Set<DefinitionStmt>> source)
///*     */               {
///* 152 */                 if (((Value)source.getO1()).equivTo(definitionStmt.getLeftOp())) {
///* 153 */                   return Collections.emptySet();
///*     */                 }
///* 155 */                 return Collections.singleton(source);
///*     */               }
///*     */             };
///*     */         }
///*     */       };
///*     */   }
///*     */
///*     */
///*     */
///*     */
///*     */   public Map<Unit, Set<Pair<Value, Set<DefinitionStmt>>>> initialSeeds() {
///* 166 */     return DefaultSeeds.make(Collections.singleton(Scene.v().getMainMethod().getActiveBody().getUnits().getFirst()),
///* 167 */         zeroValue());
///*     */   }
///*     */
///*     */   public Pair<Value, Set<DefinitionStmt>> createZeroValue() {
///* 171 */     return new Pair(new JimpleLocal("<<zero>>", (Type)NullType.v()),
///* 172 */         Collections.emptySet());
///*     */   }
///*     */ }
//
//
///* Location:              /Users/name1/Documents/ConfDroid.jar!/androidsourcecode/dataflow/ReachingDefs.class
// * Java compiler version: 8 (52.0)
// * JD-Core Version:       1.1.3
// */