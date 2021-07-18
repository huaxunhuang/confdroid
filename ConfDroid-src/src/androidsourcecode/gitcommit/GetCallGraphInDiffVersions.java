/*    */ package androidsourcecode.gitcommit;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collections;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import soot.G;
/*    */ import soot.PackManager;
/*    */ import soot.Scene;
/*    */ import soot.SootClass;
/*    */ import soot.options.Options;
/*    */ import soot.util.Chain;
/*    */ 
/*    */ public class GetCallGraphInDiffVersions
/*    */ {
/* 16 */   public static Map<Integer, Chain<SootClass>> sootClassMap = new HashMap<>();
/* 17 */   public static String androidClassesRootPath = "/Users/name1/Documents/android-classes/";
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static {
/* 23 */     for (int i = 23; i <= 23; i++) {
/* 24 */       System.out.println("Generating call graph in API level " + i + ".");
/* 25 */       System.out.println(androidClassesRootPath);
/*    */ 
/*    */ 
/*    */       
/* 29 */       Scene.v().addBasicClass("java.lang.Object", 2);
/*    */       
/* 31 */       Scene.v().addBasicClass("java.io.PrintStream", 2);
/* 32 */       Scene.v().addBasicClass("java.lang.System", 2);
/* 33 */       Options.v().set_src_prec(1);
/* 34 */       Options.v().set_process_dir(Collections.singletonList(androidClassesRootPath + "android-classes-" + i + "/"));
/* 35 */       Options.v().set_allow_phantom_refs(true);
/* 36 */       Options.v().set_prepend_classpath(true);
/* 37 */       Options.v().set_output_format(10);
/* 38 */       Options.v().set_process_multiple_dex(true);
/* 39 */       Options.v().setPhaseOption("jtp", "enabled:true");
/* 40 */       Options.v().setPhaseOption("jb", "use-original-names");
/* 41 */       Options.v().set_whole_program(true);
/* 42 */       Options.v().set_keep_line_number(true);
/* 43 */       Scene.v().loadNecessaryClasses();
/*    */       
/* 45 */       PackManager.v().runPacks();
/* 46 */       Chain<SootClass> classChain = Scene.v().getApplicationClasses();
/*    */       
/* 48 */       sootClassMap.put(Integer.valueOf(i), classChain);
/* 49 */       G.reset();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public static ArrayList<SootClass> findSootClassesInAPILevel(String className, int apiLevel) {
/* 55 */     System.out.println("apiLevel: " + apiLevel);
/* 56 */     Chain<SootClass> sootClassChain = sootClassMap.get(Integer.valueOf(apiLevel));
/* 57 */     ArrayList<SootClass> retList = new ArrayList<>();
/* 58 */     for (SootClass cls : sootClassChain) {
/* 59 */       if (cls.getName().equals(className) || cls.getName().startsWith(className + "$")) {
/* 60 */         retList.add(cls);
/*    */       }
/*    */     } 
/* 63 */     return retList;
/*    */   }
/*    */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/androidsourcecode/gitcommit/GetCallGraphInDiffVersions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */