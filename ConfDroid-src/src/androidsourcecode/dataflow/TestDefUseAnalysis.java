///*    */ package androidsourcecode.dataflow;
///*    */ import heros.InterproceduralCFG;
///*    */ import java.util.Iterator;
///*    */ import java.util.Map;
///*    */ import soot.PackManager;
///*    */ import soot.Scene;
///*    */ import soot.SceneTransformer;
///*    */ import soot.jimple.toolkits.callgraph.CallGraph;
///*    */ import soot.jimple.toolkits.callgraph.Edge;
///*    */ import soot.jimple.toolkits.ide.JimpleIFDSSolver;
///*    */ import soot.jimple.toolkits.ide.icfg.JimpleBasedInterproceduralCFG;
///*    */ import soot.options.Options;
///*    */
///*    */ public class TestDefUseAnalysis {
///*    */   public static void main(String[] args) {
///* 16 */     initSoot("/Users/name1/Documents/GitHub/InfoTestProj/out/production/InfoTestProj");
///* 17 */     performTestRerun("Test");
///* 18 */     System.out.println("runPacks");
///*    */   }
///*    */
///*    */
///*    */
///*    */   private static void performTestRerun(final String className) {
///* 24 */     System.out.println("TestDefUseAnalysis.performTestRerun");
///* 25 */     PackManager.v().getPack("wjtp").add(new Transform("wjtp.heroifds", (Transformer)new SceneTransformer() {
///*    */             protected void internalTransform(String phaseName, Map options) {
///* 27 */               System.out.println("TestDefUseAnalysis.internalTransform hugulu");
///* 28 */               Scene.v().getSootClass(className).setApplicationClass();
///* 29 */               long timeBefore = System.nanoTime();
///* 30 */               System.out.println("Running IFDS on initial CFG...");
///*    */
///* 32 */               long nanoBeforeCFG = System.nanoTime();
///* 33 */               CallGraph cg = Scene.v().getCallGraph();
///* 34 */               CallGraph newCg = new CallGraph();
///* 35 */               for (Iterator<Edge> iter = cg.iterator(); iter.hasNext(); ) {
///* 36 */                 Edge edge = iter.next();
///* 37 */                 String srcSig = edge.src().getSignature();
///* 38 */                 String tgtSig = edge.tgt().getSignature();
///* 39 */                 if (srcSig.contains("java.") || srcSig.contains("sun.") || srcSig.contains("javax.") || srcSig
///* 40 */                   .contains("jdk."))
///*    */                   continue;
///* 42 */                 if (tgtSig.contains("java.") || tgtSig.contains("sun.") || tgtSig.contains("javax.") || tgtSig
///* 43 */                   .contains("jdk.")) {
///*    */                   continue;
///*    */                 }
///* 46 */                 System.out.println(edge);
///* 47 */                 newCg.addEdge(edge);
///*    */               }
///*    */
///* 50 */               System.out.println("Scene.v().getCallGraph().size() = " + Scene.v().getCallGraph().size());
///*    */
///* 52 */               Scene.v().setCallGraph(newCg);
///* 53 */               JimpleBasedInterproceduralCFG jimpleBasedInterproceduralCFG = new JimpleBasedInterproceduralCFG();
///* 54 */               System.out.println("ICFG created in " + ((System.nanoTime() - nanoBeforeCFG) / 1.0E9D) + " seconds.");
///*    */
///* 56 */               ReachingDefs reachingDefs = new ReachingDefs((InterproceduralCFG<Unit, SootMethod>)jimpleBasedInterproceduralCFG);
///*    */
///*    */
///* 59 */               JimpleIFDSSolver solver = new JimpleIFDSSolver((IFDSTabulationProblem)reachingDefs);
///*    */
///*    */
///* 62 */               long beforeSolver = System.nanoTime();
///* 63 */               System.out.println("Running solver...");
///* 64 */               solver.solve();
///* 65 */               solver.dumpResults();
///*    */
///* 67 */               System.out.println("Solver done in " + ((System.nanoTime() - beforeSolver) / 1.0E9D) + " seconds.");
///*    */             }
///*    */           }));
///*    */
///*    */
///* 72 */     PackManager.v().runPacks();
///*    */   }
///*    */
///*    */
///*    */
///*    */
///*    */
///*    */
///*    */   public static void initSoot(String projectPath) {
///* 81 */     Scene.v().addBasicClass("java.lang.Object", 2);
///*    */
///* 83 */     Scene.v().addBasicClass("java.io.PrintStream", 2);
///* 84 */     Scene.v().addBasicClass("java.lang.System", 2);
///* 85 */     Options.v().set_src_prec(1);
///* 86 */     Options.v().set_process_dir(Collections.singletonList(projectPath));
///* 87 */     Options.v().set_allow_phantom_refs(true);
///* 88 */     Options.v().set_prepend_classpath(true);
///* 89 */     Options.v().set_output_format(12);
///* 90 */     Options.v().setPhaseOption("jtp", "enabled:false");
///* 91 */     Options.v().setPhaseOption("jb", "use-original-names");
///* 92 */     Options.v().set_whole_program(true);
///* 93 */     Options.v().set_keep_line_number(true);
///* 94 */     Options.v().set_no_writeout_body_releasing(true);
///* 95 */     Scene.v().loadNecessaryClasses();
///*    */
///* 97 */     PackManager.v().runPacks();
///*    */   }
///*    */ }
//
//
///* Location:              /Users/name1/Documents/ConfDroid.jar!/androidsourcecode/dataflow/TestDefUseAnalysis.class
// * Java compiler version: 8 (52.0)
// * JD-Core Version:       1.1.3
// */