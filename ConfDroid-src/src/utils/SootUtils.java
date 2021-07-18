/*     */ package utils;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import soot.PackManager;
/*     */ import soot.Scene;
/*     */ import soot.SootClass;
/*     */ import soot.options.Options;
/*     */ import soot.util.Chain;
/*     */ 
/*     */ public class SootUtils {
/*  15 */   public static String gitCommitRootPath = ""; public static boolean DEBUG_MODE = false;
/*  16 */   public static String androidFrameworkRootPath = "/Users/name1/Documents/GitHub/platform_frameworks_base/";
/*  17 */   public static String projectRootPath = "/Users/name1/Documents/GitHub/mine-config/ConfDroid/";
/*  18 */   public static String androidClassesRootPath = "/Users/name1/Documents/android-classes/";
/*  19 */   public static String expOutRootPath = "";
/*     */   
/*     */   public static void setDebugMode(boolean debugMode) {
/*  22 */     DEBUG_MODE = debugMode;
/*  23 */     if (debugMode) {
/*  24 */       gitCommitRootPath = "/Users/name1/Documents/GitHub/untitled/honeycomb/";
/*  25 */       androidFrameworkRootPath = "/Users/name1/Documents/GitHub/platform_frameworks_base/";
/*  26 */       projectRootPath = "/Users/name1/Documents/GitHub/untitled/";
/*  27 */       androidClassesRootPath = "/Users/name1/Documents/android-classes/";
/*  28 */       expOutRootPath = "/Users/name1/Documents/GitHub/untitled/output/";
/*     */     } else {
/*  30 */       gitCommitRootPath = "/home1/name1/untitled/honeycomb/";
/*  31 */       androidFrameworkRootPath = "/home1/name1/platform_base/platform_frameworks_base/";
/*  32 */       projectRootPath = "/home1/name1/untitled/";
/*  33 */       androidClassesRootPath = "/home1/name1/android-classes/";
/*  34 */       expOutRootPath = "/home1/name1/untitled/output/";
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void initializeSoot(String projectPath) {
/*  41 */     Scene.v().addBasicClass("java.lang.Object", 2);
/*     */     
/*  43 */     Scene.v().addBasicClass("java.io.PrintStream", 2);
/*  44 */     Scene.v().addBasicClass("java.lang.System", 2);
/*  45 */     Options.v().set_src_prec(1);
/*  46 */     Options.v().set_process_dir(Collections.singletonList(projectPath));
/*  47 */     Options.v().set_allow_phantom_refs(true);
/*  48 */     Options.v().set_prepend_classpath(true);
/*  49 */     Options.v().set_output_format(10);
/*  50 */     Options.v().set_process_multiple_dex(true);
/*  51 */     Options.v().setPhaseOption("jtp", "enabled:true");
/*  52 */     Options.v().setPhaseOption("jb", "use-original-names");
/*  53 */     Options.v().set_whole_program(true);
/*  54 */     Options.v().set_keep_line_number(true);
/*  55 */     Scene.v().loadNecessaryClasses();
/*     */     
/*  57 */     PackManager.v().runPacks();
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isSuperClass(SootClass original, SootClass tested) {
/*  62 */     List<SootClass> workList = new ArrayList<>();
/*     */     
/*  64 */     if (original.hasSuperclass()) {
/*     */       
/*  66 */       SootClass superCls = original.getSuperclass();
/*     */       
/*  68 */       if (!superCls.getName().equals("java.lang.Object"))
/*     */       {
/*  70 */         workList.add(original.getSuperclass());
/*     */       }
/*     */     } 
/*  73 */     for (Iterator<SootClass> iter = original.getInterfaces().snapshotIterator(); iter.hasNext(); ) {
/*     */       
/*  75 */       SootClass sc = iter.next();
/*  76 */       workList.add(sc);
/*     */     } 
/*     */     
/*  79 */     while (!workList.isEmpty()) {
/*     */       
/*  81 */       SootClass sc = workList.remove(0);
/*     */       
/*  83 */       if (sc.getName().equals(tested.getName()))
/*     */       {
/*  85 */         return true;
/*     */       }
/*     */ 
/*     */       
/*  89 */       if (sc.hasSuperclass())
/*     */       {
/*  91 */         if (!sc.getSuperclass().getName().equals("java.lang.Object"))
/*     */         {
/*  93 */           workList.add(sc.getSuperclass());
/*     */         }
/*     */       }
/*     */       
/*  97 */       for (Iterator<SootClass> iterator = sc.getInterfaces().snapshotIterator(); iterator.hasNext();)
/*     */       {
/*  99 */         workList.add(iterator.next());
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 104 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public static Set<SootClass> getAllSubClasses(SootClass sootClass) {
/* 109 */     Set<SootClass> sootClasses = new HashSet<>();
/*     */     
/* 111 */     Chain<SootClass> applicationClasses = Scene.v().getApplicationClasses();
/* 112 */     for (Iterator<SootClass> iter = applicationClasses.snapshotIterator(); iter.hasNext(); ) {
/*     */       
/* 114 */       SootClass sc = iter.next();
/*     */       
/* 116 */       if (isSuperClass(sc, sootClass))
/*     */       {
/* 118 */         sootClasses.add(sc);
/*     */       }
/*     */     } 
/*     */     
/* 122 */     return sootClasses;
/*     */   }
/*     */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/utils/SootUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */