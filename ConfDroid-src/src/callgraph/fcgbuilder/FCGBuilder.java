/*    */ package callgraph.fcgbuilder;
/*    */ import callgraph.graph.FrameworkCallGraph;
/*    */ import java.util.*;
/*    */
/*    */
/*    */ import soot.*;
/*    */
/*    */
/*    */
/*    */ import soot.options.Options;
/*    */ import soot.util.Chain;
import utils.CommonUtils;

/*    */
/*    */ public class FCGBuilder {
/* 14 */   public static Set<String> primaryAPIs = new HashSet<>();
/*    */   
/* 16 */   public static Map<String, Set<String>> api2callers = new HashMap<>();
/* 17 */   public static Set<String> usedAndroidAPIs = new HashSet<>();
/*    */   
/*    */   public static void initSoot(String projectPath, int apiLevel) {
/* 20 */     System.out.println("=== init soot ===");
/*    */ 
/*    */ 
/*    */     
/* 24 */     Scene.v().addBasicClass("java.lang.Object", 2);
/*    */     
/* 26 */     Scene.v().addBasicClass("java.io.PrintStream", 2);
/* 27 */     Scene.v().addBasicClass("java.lang.System", 2);
/* 28 */     Options.v().set_src_prec(1);
/*    */     
/* 30 */     Options.v().set_process_dir(Collections.singletonList(projectPath));
/* 31 */     Options.v().set_allow_phantom_refs(true);
/* 32 */     Options.v().set_prepend_classpath(true);
/* 33 */     Options.v().set_output_format(12);
/* 34 */     Options.v().set_verbose(true);
/* 35 */     Options.v().setPhaseOption("jtp", "enabled:false");
/* 36 */     Options.v().setPhaseOption("jb", "use-original-names");
/* 37 */     Options.v().setPhaseOption("cg", "all-reachable:true");
/* 38 */     Options.v().set_whole_program(true);
/* 39 */     Options.v().set_keep_line_number(true);
/* 40 */     Scene.v().loadNecessaryClasses();
/*    */     
/* 42 */     Options.v().set_output_format(12);
/*    */     
/* 44 */     Chain<SootClass> sootClassChain = Scene.v().getApplicationClasses();
/* 45 */     List<SootClass> sootClasses = new ArrayList<>();
/* 46 */     for (SootClass cls : sootClassChain) {
/* 47 */       sootClasses.add(cls);
/*    */     }
/*    */     
/* 50 */     FCGBuilderTransformer transformer = new FCGBuilderTransformer(apiLevel);
/* 51 */     if (FrameworkCallGraph.getFCG(apiLevel) == null) {
/* 52 */       FrameworkCallGraph.initialFCG(apiLevel, sootClasses);
/*    */     }
/*    */     
/* 55 */     PackManager.v().getPack("wstp").add(new Transform("wstp.FCGBuilderTransformer", (Transformer)transformer));
/*    */     
/* 57 */     PackManager.v().runPacks();
/* 58 */     primaryAPIs.addAll(transformer.accessedAndroidAPIs);
/* 59 */     usedAndroidAPIs.addAll(primaryAPIs);
/* 60 */     CommonUtils.put(api2callers, transformer.api2callers);
/* 61 */     System.out.println();
/*    */   }
/*    */   
/*    */   public static void initSoot(String projectPath) {
/* 65 */     initSoot(projectPath, -1);
/*    */   }
/*    */   
/*    */   public static void initSootByApiLevel(String projectPath, int apiLevel) {
/* 69 */     projectPath = projectPath + apiLevel;
/* 70 */     initSoot(projectPath, apiLevel);
/*    */   }
/*    */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/callgraph/fcgbuilder/FCGBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */