///*     */ package empirical;
///*     */ import java.io.IOException;
///*     */ import java.util.Collections;
///*     */ import java.util.Map;
///*     */ import java.util.Set;
///*     */ import org.junit.Test;
///*     */ import soot.G;
///*     */ import soot.PackManager;
///*     */ import soot.Scene;
///*     */ import soot.SootClass;
///*     */ import soot.SootMethod;
///*     */ import soot.Unit;
///*     */ import soot.jimple.infoflow.android.manifest.ProcessManifest;
///*     */ import soot.jimple.infoflow.android.resources.ARSCFileParser;
///*     */ import soot.jimple.infoflow.android.resources.LayoutFileParser;
/////*     */ import soot.jimple.infoflow.android.resources.controls.AndroidLayoutControl;
///*     */ import soot.options.Options;
///*     */ import soot.util.Chain;
///*     */ import soot.util.MultiMap;
///*     */
///*     */ public class CountNumberOfWidgetInJava {
///*  22 */   public ARSCFileParser resParser = null;
///*  23 */   public LayoutFileParser lfp = null;
///*     */
///*     */
///*     */   public void tryLayoutFileParser(String apkPath) throws IOException {
///*  27 */     ARSCFileParser resParser = new ARSCFileParser();
///*  28 */     resParser.parse(apkPath);
///*  29 */     System.out.println();
///*     */
///*     */     try {
///*  32 */       ProcessManifest processMan = new ProcessManifest(apkPath);
///*  33 */       String appPackageName = processMan.getPackageName();
///*  34 */       LayoutFileParser lfp = new LayoutFileParser(appPackageName, resParser);
///*     */
///*  36 */       lfp.parseLayoutFile(apkPath);
///*  37 */       PackManager.v().runPacks();
///*  38 */       PackManager.v().getPack("wspp").apply();
///*  39 */       PackManager.v().getPack("cg").apply();
///*  40 */       PackManager.v().getPack("wstp").apply();
///*  41 */       MultiMap<String, AndroidLayoutControl> userControlsByName = lfp.getUserControls();
///*  42 */       System.out.println(userControlsByName.keySet());
///*  43 */       Map<Integer, AndroidLayoutControl> userControlsById = lfp.getUserControlsByID();
///*  44 */       System.out.println("userControls: " + userControlsById.keySet().size());
///*  45 */       for (Integer id : userControlsById.keySet()) {
///*  46 */         System.out.println("id: " + id);
///*     */       }
///*  48 */       Set<Integer> visId = new HashSet<>();
///*     */
///*  50 */       Chain<SootClass> classes = Scene.v().getApplicationClasses();
///*  51 */       for (SootClass cls : classes) {
///*  52 */         for (SootMethod mtd : cls.getMethods()) {
///*  53 */           if (mtd.hasActiveBody()) {
///*  54 */             for (Unit unit : mtd.getActiveBody().getUnits()) {
///*  55 */               for (Integer id : userControlsById.keySet()) {
///*  56 */                 if (unit.toString().contains("" + id)) {
///*  57 */                   visId.add(id);
///*     */                 }
///*     */               }
///*     */             }
///*     */           }
///*     */         }
///*     */       }
///*     */
///*     */
///*  66 */       System.out.println("visId: " + visId.size());
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
///*     */
///*     */
///*     */
///*     */
///*     */     }
///*  83 */     catch (Exception e) {
///*  84 */       e.printStackTrace();
///*     */     }
///*     */   }
///*     */
///*     */
///*     */   @Test
///*     */   public void testMain() throws Exception {
///*  91 */     String apkPath = "/Volumes/DATASET/top3200/education/es.monkimun.lingokids.apk";
///*  92 */     initSoot(apkPath);
///*     */
///*  94 */     tryLayoutFileParser(apkPath);
///*     */   }
///*     */
///*     */
///*     */
///*     */
///*     */
///*     */
///*     */   public void initSoot(String projectPath) {
///* 103 */     G.reset();
///*     */
///*     */
///* 106 */     Scene.v().addBasicClass("java.lang.Object", 2);
///*     */
///* 108 */     Scene.v().addBasicClass("java.io.PrintStream", 2);
///* 109 */     Scene.v().addBasicClass("java.lang.System", 2);
///* 110 */     Options.v().set_src_prec(5);
///* 111 */     Options.v().set_force_android_jar("/Users/name1/Library/Android/sdk/platforms/android-29/android.jar");
///* 112 */     Options.v().set_process_dir(Collections.singletonList(projectPath));
///* 113 */     Options.v().set_process_multiple_dex(true);
///* 114 */     Options.v().set_allow_phantom_refs(true);
///* 115 */     Options.v().set_prepend_classpath(true);
///* 116 */     Options.v().set_output_format(12);
///* 117 */     Options.v().setPhaseOption("jtp", "enabled:false");
///* 118 */     Options.v().set_whole_program(true);
///* 119 */     Options.v().set_keep_line_number(true);
///* 120 */     Scene.v().loadNecessaryClasses();
///*     */
///* 122 */     PackManager.v().runPacks();
///*     */   }
///*     */ }
//
//
///* Location:              /Users/name1/Documents/ConfDroid.jar!/empirical/CountNumberOfWidgetInJava.class
// * Java compiler version: 8 (52.0)
// * JD-Core Version:       1.1.3
// */