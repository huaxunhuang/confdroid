/*     */ package lu.uni.tsopen;
/*     */ 
/*     */ import callgraph.fcgbuilder.FCGBuilder;
/*     */ import java.io.File;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Queue;
/*     */ import java.util.Set;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import lu.uni.tsopen.symbolicExecution.symbolicValues.SymbolicValue;
/*     */ import org.apache.poi.hssf.usermodel.HSSFWorkbook;
/*     */ import org.apache.poi.ss.usermodel.Row;
/*     */ import org.apache.poi.ss.usermodel.Sheet;
/*     */ import org.javatuples.Quartet;
/*     */ import org.javatuples.Septet;
/*     */ import org.javatuples.Triplet;
/*     */ import org.logicng.formulas.Formula;
/*     */ import org.logicng.formulas.FormulaFactory;
/*     */ import org.logicng.formulas.Literal;
/*     */ import org.logicng.formulas.Variable;
/*     */ import soot.G;
/*     */ import soot.Scene;
/*     */ import soot.SootClass;
/*     */ import soot.SootMethod;
/*     */ import soot.Unit;
/*     */ import soot.Value;
/*     */ import soot.jimple.Constant;
/*     */ import utils.TimeUtil;
/*     */ public class ConfDroidMain
/*     */ {
/*     */   public static List<String> parseClassNameList(String path0) {
/*  63 */     List<String> class0List = new ArrayList<>();
/*  64 */     File rootFilePath = new File(path0);
/*     */
/*  66 */     if (rootFilePath.isDirectory()) {
/*  67 */       File[] subRootFiles = rootFilePath.listFiles();
/*  68 */       int progress = 0;
/*  69 */       for (File subRoot : subRootFiles) {
/*     */
/*  71 */         if (subRoot.isDirectory() && (subRoot
/*  72 */           .toString().contains("android_graphics_") || subRoot
/*  73 */           .toString().contains("android_widget_") || subRoot
/*  74 */           .toString().contains("android_content_pm_") || subRoot
/*  75 */           .toString().contains("android_animation_") || subRoot
/*  76 */           .toString().contains("android_transition") || subRoot
/*  77 */           .toString().contains("android_preference") || subRoot
/*  78 */           .toString().contains("android_view_") || subRoot
/*  79 */           .toString().contains("android_content_") || subRoot
/*  80 */           .toString().contains("android_service_") || subRoot
/*  81 */           .toString().contains("android_nfc_") || subRoot
/*  82 */           .toString().contains("android_inputmethodservice") || subRoot
/*  83 */           .toString().contains("android_speech_") || subRoot
/*  84 */           .toString().contains("android_app_")) &&
/*     */
/*  86 */           !subRoot.toString().contains("$")) {
/*     */
/*  88 */           String className = subRoot.toString().split("/")[(subRoot.toString().split("/")).length - 1];
/*  89 */           className = className.replaceAll("_", ".");
/*  90 */           class0List.add(className);
/*     */         }
/*     */       }
/*     */     }
/*  94 */     return class0List;
/*     */   }
/*     */
/*     */
/*     */
/*     */   public static void main(String[] args) {
/* 100 */     boolean wholeCallGraph = true;
/* 101 */     Set<Quartet> xlsaaa = new HashSet<>();
/*     */
/* 103 */     int apiLevel = Integer.parseInt(args[0]);
/* 107 */     String path0 = "/data/name1/ConfDroid/test_whole_cfg/framework-" + apiLevel + "/";
/* 108 */     String path1 = "/data/name1/ConfDroid/test_whole_cfg/framework-" + (apiLevel + 1) + "/";
/*     */
/*     */
/* 111 */     if (!wholeCallGraph) {
/* 112 */       List<String> class0List = parseClassNameList(path0);
/* 113 */       List<String> class1List = parseClassNameList(path1);
/*     */
/* 115 */       for (String cls : class1List) {
/* 116 */         if (class0List.contains(cls)) {
/*     */
/* 118 */           String cls0Path = path0 + cls.replaceAll("\\.", "_");
/* 119 */           Analysis analysis1 = new Analysis(cls);
/* 120 */           analysis1.run(apiLevel, cls0Path, cls);
/*     */
/*     */
/* 129 */           List<Triplet<Unit, Formula, List<Septet<List<SymbolicValue>, List<SymbolicValue>, List<SymbolicValue>, Value, Value, Constant, Literal>>>> intere1 = analysis1.getInterestingFormulae();
/*     */
/* 131 */           System.out.println("=== finish ===");
/*     */         }
/*     */       }
/*     */
/*     */
/*     */
/* 143 */       System.out.println("TimeUtil.getRunningTime() = " + TimeUtil.getRunningTime());
/*     */     } else {
/* 145 */       Analysis analysis1 = new Analysis();
/* 146 */       Analysis analysis2 = new Analysis();
/* 147 */       TimeUtil.start();
/* 148 */       analysis1.run(apiLevel, path0, null);
/*     */     }
/*     */   }
}
/*     */   
