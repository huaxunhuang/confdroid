/*     */ package empirical;
/*     */ import com.opencsv.CSVReader;
/*     */ import com.opencsv.CSVReaderBuilder;
/*     */ import jas.Pair;
/*     */ import java.io.BufferedReader;
import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.util.*;
/*     */
/*     */
/*     */
/*     */
/*     */ import org.junit.Test;
import soot.*;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ import soot.jimple.internal.JAssignStmt;
/*     */ import soot.jimple.internal.JInvokeStmt;
/*     */ import soot.options.Options;
/*     */ import soot.util.Chain;
/*     */ 
/*     */ public class ScanAppJavaCode {
/*  26 */   List<Pair<String, String>> sigs = new ArrayList<>();
/*  27 */   Map<Pair<String, String>, Integer> freqs = new HashMap<>();
/*     */   
/*     */   public void initSoot(String projectPath) {
/*  30 */     G.reset();
/*     */ 
/*     */     
/*  33 */     Scene.v().addBasicClass("java.lang.Object", 2);
/*     */     
/*  35 */     Scene.v().addBasicClass("java.io.PrintStream", 2);
/*  36 */     Scene.v().addBasicClass("java.lang.System", 2);
/*  37 */     Options.v().set_src_prec(5);
/*  38 */     Options.v().set_force_android_jar("/Users/name1/Library/Android/sdk/platforms/android-29/android.jar");
/*  39 */     Options.v().set_process_dir(Collections.singletonList(projectPath));
/*  40 */     Options.v().set_process_multiple_dex(true);
/*  41 */     Options.v().set_allow_phantom_refs(true);
/*  42 */     Options.v().set_prepend_classpath(true);
/*  43 */     Options.v().set_output_format(12);
/*  44 */     Options.v().setPhaseOption("jtp", "enabled:false");
/*  45 */     Options.v().set_whole_program(true);
/*  46 */     Options.v().set_keep_line_number(true);
/*  47 */     Scene.v().loadNecessaryClasses();
/*     */     
/*  49 */     PackManager.v().runPacks();
/*     */   }
/*     */   
/*     */   public void initRelatedMethods() {
/*  53 */     String srcPath = "/Users/name1/Documents/GitHub/mine-config/ConfDroid/related_methods.csv";
/*  54 */     String charset = "utf-8";
/*     */ 
/*     */     
/*  57 */     try (CSVReader csvReader = (new CSVReaderBuilder(new BufferedReader(new InputStreamReader(new FileInputStream(new File(srcPath)), charset)))).build()) {
/*     */       
/*  59 */       Iterator<String[]> iterator = csvReader.iterator();
/*     */       
/*  61 */       while (iterator.hasNext()) {
/*  62 */         String str = ((String[])iterator.next())[2];
/*  63 */         if (str.contains("#")) {
/*  64 */           String className = str.split("#")[0];
/*  65 */           String sig = str.split("#")[1];
/*  66 */           sig = sig.replaceAll("%20", " ");
/*  67 */           this.sigs.add(new Pair(className, sig));
/*     */         
/*     */         }
/*     */       
/*     */       }
/*     */     
/*     */     }
/*  74 */     catch (Exception e) {
/*  75 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void searchRelatedMethodsInAppCode(String prefix) throws Exception {
/*  81 */     Chain<SootClass> classes = Scene.v().getApplicationClasses();
/*  82 */     for (SootClass cls : classes) {
/*  83 */       List<SootMethod> mtds = cls.getMethods();
/*  84 */       for (SootMethod mtd : mtds) {
/*     */         
/*  86 */         if (mtd.hasActiveBody()) {
/*  87 */           UnitPatchingChain unitPatchingChain = mtd.retrieveActiveBody().getUnits();
/*  88 */           for (Unit unit : unitPatchingChain) {
/*  89 */             SootMethod invokeMtd = null;
/*  90 */             if (unit instanceof JAssignStmt) {
/*  91 */               if (((JAssignStmt)unit).containsInvokeExpr()) {
/*  92 */                 invokeMtd = ((JAssignStmt)unit).getInvokeExpr().getMethod();
/*     */               }
/*     */             }
/*  95 */             else if (unit instanceof JInvokeStmt && (
/*  96 */               (JInvokeStmt)unit).containsInvokeExpr()) {
/*  97 */               invokeMtd = ((JInvokeStmt)unit).getInvokeExpr().getMethod();
/*     */             } 
/*     */ 
/*     */             
/* 101 */             if (invokeMtd != null) {
/* 102 */               String clsa = invokeMtd.getDeclaringClass().getName();
/* 103 */               String subSignature = invokeMtd.getSubSignature();
/* 104 */               for (Pair<String, String> pair : this.sigs) {
/* 105 */                 if (((String)pair.getO1()).equals(clsa) && subSignature.endsWith((String)pair.getO2()) && 
/* 106 */                   mtd.getDeclaringClass().toString().contains(prefix)) {
/* 107 */                   System.out.println(mtd.getSignature());
/* 108 */                   System.out.println("unit: " + unit);
/* 109 */                   System.out.println("unitLineNum: " + unit.getJavaSourceStartLineNumber());
/* 110 */                   checkUnit(unit, mtd);
/* 111 */                   System.out.println();
/* 112 */                   int fr = ((Integer)this.freqs.get(pair)).intValue();
/* 113 */                   fr++;
/* 114 */                   this.freqs.put(pair, Integer.valueOf(fr));
/*     */                 } 
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkUnit(Unit unit, SootMethod mtd) {
/* 128 */     SootMethod invokeMtd = null;
/* 129 */     if (unit instanceof JAssignStmt) {
/* 130 */       if (((JAssignStmt)unit).containsInvokeExpr()) {
/* 131 */         invokeMtd = ((JAssignStmt)unit).getInvokeExpr().getMethod();
/* 132 */         List<Value> argsList = ((JAssignStmt)unit).getInvokeExpr().getArgs();
/* 133 */         boolean flag = false;
/* 134 */         for (Value arg : argsList) {
/* 135 */           if (arg instanceof soot.jimple.internal.JimpleLocal) {
/* 136 */             flag = true;
/* 137 */             System.out.println("Not Constanthaha");
/*     */             
/*     */             break;
/*     */           } 
/*     */         } 
/* 142 */         if (!flag) {
/* 143 */           System.out.println("Constanthaha");
/*     */         }
/*     */       } 
/* 146 */     } else if (unit instanceof JInvokeStmt && (
/* 147 */       (JInvokeStmt)unit).containsInvokeExpr()) {
/* 148 */       invokeMtd = ((JInvokeStmt)unit).getInvokeExpr().getMethod();
/* 149 */       List<Value> argsList = ((JInvokeStmt)unit).getInvokeExpr().getArgs();
/* 150 */       boolean flag = false;
/* 151 */       for (Value arg : argsList) {
/* 152 */         if (arg instanceof soot.jimple.internal.JimpleLocal) {
/* 153 */           flag = true;
/* 154 */           System.out.println("Not Constanthaha");
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/* 159 */       if (!flag) {
/* 160 */         System.out.println("Constanthaha");
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void initFreq() {
/* 169 */     for (Pair<String, String> sig : this.sigs) {
/* 170 */       this.freqs.put(sig, Integer.valueOf(0));
/*     */     }
/*     */   }
/*     */   
/*     */   public void printFreq() {
/* 175 */     System.out.println("==== ");
/*     */     
/* 177 */     for (Pair<String, String> key : this.freqs.keySet()) {
/* 178 */       int value = ((Integer)this.freqs.get(key)).intValue();
/* 179 */       if (value > 0) {
/* 180 */         System.out.println(key);
/* 181 */         System.out.println("value: " + value);
/*     */       } 
/*     */     } 
/* 184 */     System.out.println("==== ");
/*     */   }
/*     */ 
/*     */   
/*     */   @Test
/*     */   public void mainTest() {
/* 190 */     int ptr = 0;
/* 191 */     String projectPath = "/Volumes/DATASET/top3200/fitness";
/* 192 */     initRelatedMethods();
/* 193 */     initFreq();
/* 194 */     ArrayList<File> fileList = getFiles(projectPath);
/* 195 */     for (File file : fileList) {
/* 196 */       System.out.println("==num: " + ++ptr);
/*     */       try {
/* 198 */         String path = file.getPath();
/* 199 */         initSoot(path);
/* 200 */         String prefix = file.getName().split("\\.")[0] + "." + file.getName().split("\\.")[1];
/*     */         
/* 202 */         searchRelatedMethodsInAppCode(prefix);
/*     */         
/* 204 */         printFreq();
/* 205 */       } catch (Exception e) {}
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ArrayList<File> getFiles(String path) {
/* 212 */     ArrayList<File> files = new ArrayList<>();
/* 213 */     File file = new File(path);
/* 214 */     File[] tempList = file.listFiles();
/*     */     
/* 216 */     for (int i = 0; i < tempList.length; i++) {
/* 217 */       if (tempList[i].isFile() && 
/* 218 */         tempList[i].getPath().endsWith(".apk")) {
/* 219 */         files.add(tempList[i]);
/*     */       }
/*     */     } 
/*     */     
/* 223 */     return files;
/*     */   }
/*     */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/empirical/ScanAppJavaCode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */