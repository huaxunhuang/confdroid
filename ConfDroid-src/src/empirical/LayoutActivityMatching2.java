/*     */ package empirical;
/*     */ import java.util.*;
/*     */
/*     */
/*     */ import org.junit.Test;
import soot.*;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ import soot.jimple.Stmt;
/*     */ import soot.jimple.infoflow.android.manifest.ProcessManifest;
/*     */ import soot.jimple.infoflow.android.resources.ARSCFileParser;
/*     */ import soot.jimple.infoflow.android.resources.LayoutFileParser;
/*     */ import soot.options.Options;
/*     */ import soot.util.Chain;
/*     */ 
/*     */ public class LayoutActivityMatching2 {
/*  19 */   public Map<String, List<Value>> activityLayoutMaps = new HashMap<>(); public ARSCFileParser resParser;
/*     */   
/*     */   public void initSoot(String projectPath) {
/*  22 */     G.reset();
/*     */ 
/*     */     
/*  25 */     Scene.v().addBasicClass("java.lang.Object", 2);
/*     */     
/*  27 */     Scene.v().addBasicClass("java.io.PrintStream", 2);
/*  28 */     Scene.v().addBasicClass("java.lang.System", 2);
/*  29 */     Options.v().set_process_multiple_dex(true);
/*  30 */     Options.v().set_src_prec(5);
/*  31 */     Options.v().set_force_android_jar("/Users/name1/Library/Android/sdk/platforms/android-29/android.jar");
/*  32 */     Options.v().set_process_dir(Collections.singletonList(projectPath));
/*  33 */     Options.v().set_allow_phantom_refs(true);
/*  34 */     Options.v().set_prepend_classpath(true);
/*  35 */     Options.v().set_output_format(12);
/*  36 */     Options.v().setPhaseOption("jtp", "enabled:false");
/*  37 */     Options.v().set_whole_program(true);
/*  38 */     Options.v().set_keep_line_number(true);
/*  39 */     Scene.v().loadNecessaryClasses();
/*     */     
/*  41 */     PackManager.v().runPacks();
/*     */   }
/*     */   
/*     */   private void buildActivityLayoutMaps(SootClass cls) {
/*  45 */     System.out.println("====cls: " + cls);
/*  46 */     List<Value> ret = new ArrayList<>();
/*  47 */     List<SootMethod> sootMethods = cls.getMethods();
/*  48 */     for (SootMethod mtd : sootMethods) {
/*  49 */       if (mtd.hasActiveBody()) {
/*  50 */         UnitPatchingChain unitPatchingChain = mtd.retrieveActiveBody().getUnits();
/*  51 */         for (Unit unit : unitPatchingChain) {
/*     */           
/*  53 */           Stmt stmt = (Stmt)unit;
/*  54 */           if (stmt.containsInvokeExpr() && stmt
/*  55 */             .getInvokeExpr().toString().contains("setContentView(")) {
/*  56 */             Value id = stmt.getInvokeExpr().getArg(0);
/*  57 */             System.out.println("mtd: " + mtd.toString());
/*  58 */             if (!(id instanceof soot.jimple.internal.JimpleLocal)) {
/*  59 */               System.out.println("idaaaa: " + id.toString());
/*  60 */               System.out.println(getActivityName(id));
/*     */             } else {
/*  62 */               System.out.println("special ida");
/*     */             } 
/*     */             
/*  65 */             ret.add(id);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/*  71 */     this.activityLayoutMaps.put(cls.getName(), ret);
/*     */   }
/*     */   
/*     */   public String getActivityName(Value id) {
/*     */     try {
/*  76 */       if (id != null) {
/*  77 */         int idInt = Integer.parseInt(id.toString());
/*  78 */         System.out.println("resourceName: " + this.resParser.findResource(idInt).getResourceName());
/*  79 */         return this.resParser.findResource(idInt).getResourceName();
/*     */       } 
/*  81 */     } catch (NumberFormatException e) {
/*  82 */       return "";
/*     */     } 
/*  84 */     return "";
/*     */   }
/*     */   
/*     */   private boolean isCallClassSubClass(SootClass call, String check) {
/*  88 */     if (!call.hasSuperclass()) {
/*  89 */       return false;
/*     */     }
/*  91 */     if (call.getSuperclass().toString().equals(check)) {
/*  92 */       return true;
/*     */     }
/*  94 */     return false;
/*     */   }
/*     */   
/*     */   @Test
/*     */   public void testMain() throws Exception {
/*  99 */     String apkPath = "/Volumes/DATASET/google_play/app/fitness/com.nike.plusgps.apk";
/* 100 */     initSoot(apkPath);
/* 101 */     ProcessManifest processMan = new ProcessManifest(apkPath);
/* 102 */     this.resParser = new ARSCFileParser();
/* 103 */     this.resParser.parse(apkPath);
/* 104 */     this.lfp = new LayoutFileParser(processMan.getPackageName(), this.resParser);
/*     */     
/* 106 */     Chain<SootClass> sootClasses = Scene.v().getClasses();
/* 107 */     for (SootClass cls : sootClasses) {
/* 108 */       buildActivityLayoutMaps(cls);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/* 113 */   public LayoutFileParser lfp = null;
/*     */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/empirical/LayoutActivityMatching2.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */