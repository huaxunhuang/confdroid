/*     */ package lu.uni.tsopen;
/*     */ 
/*     */ import callgraph.fcgbuilder.FCGBuilder;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintWriter;
/*     */ import java.security.MessageDigest;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.Vector;
/*     */ import lu.uni.tsopen.logicBombs.PotentialLogicBombsRecovery;
/*     */ import lu.uni.tsopen.pathPredicateRecovery.PathPredicateRecovery;
/*     */ import lu.uni.tsopen.pathPredicateRecovery.SimpleBlockPredicateExtraction;
/*     */ import lu.uni.tsopen.symbolicExecution.SymbolicExecution;
/*     */ import lu.uni.tsopen.symbolicExecution.symbolicValues.SymbolicValue;
/*     */ import lu.uni.tsopen.utils.CommandLineOptions;
/*     */ import lu.uni.tsopen.utils.Utils;
/*     */ import org.apache.poi.hssf.usermodel.HSSFWorkbook;
/*     */ import org.apache.poi.ss.usermodel.Row;
/*     */ import org.apache.poi.ss.usermodel.Sheet;
/*     */ import org.javatuples.Pair;
/*     */ import org.javatuples.Quartet;
/*     */ import org.javatuples.Septet;
/*     */ import org.javatuples.Triplet;
/*     */ import org.logicng.formulas.Formula;
/*     */ import org.logicng.formulas.Literal;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import soot.Body;
/*     */ import soot.G;
/*     */ import soot.Scene;
/*     */ import soot.SootClass;
/*     */ import soot.SootMethod;
/*     */ import soot.Unit;
/*     */ import soot.Value;
/*     */ import soot.jimple.Constant;
/*     */ import soot.jimple.IfStmt;
/*     */ import soot.jimple.infoflow.android.manifest.ProcessManifest;
/*     */ import soot.jimple.infoflow.solver.cfg.InfoflowCFG;
/*     */ import soot.util.Chain;
/*     */ import utils.TimeUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Analysis
/*     */ {
/*     */   private String pkgName;
/*     */   private CommandLineOptions options;
/*     */   private String fileName;
/*     */   private PotentialLogicBombsRecovery plbr;
/*     */   private InfoflowCFG icfg;
/*     */   private int dexSize;
/*     */   private int nbClasses;
/*     */   private String fileSha256;
/*     */   private String className;
/*     */   private SimpleBlockPredicateExtraction sbpe;
/*     */   private PathPredicateRecovery ppr;
/*  80 */   public Set<Quartet> xlsSet = new HashSet<>();
/*  81 */   private Logger logger = LoggerFactory.getLogger(ConfDroidMain.class);
/*  82 */   private List<Triplet<Unit, Formula, List<Septet<List<SymbolicValue>, List<SymbolicValue>, List<SymbolicValue>, Value, Value, Constant, Literal>>>> interestingFormulae = new ArrayList<>();
/*     */   
/*     */   private int apiLevel;
/*     */ 
/*     */   
/*     */   public List<Triplet<Unit, Formula, List<Septet<List<SymbolicValue>, List<SymbolicValue>, List<SymbolicValue>, Value, Value, Constant, Literal>>>> getInterestingFormulae() {
/*  88 */     return this.interestingFormulae;
/*     */   }
/*     */   
/*     */   public Analysis(String[] args) {
/*  92 */     this.options = new CommandLineOptions(args);
/*  93 */     this.fileName = this.options.getFile();
/*  94 */     this.pkgName = getPackageName(this.fileName);
/*  95 */     this.fileSha256 = getFileSha256(this.fileName);
/*  96 */     this.sbpe = null;
/*  97 */     this.plbr = null;
/*  98 */     this.icfg = null;
/*  99 */     this.ppr = null;
/*     */   }
/*     */   
/*     */   public Analysis(String className) {
/* 103 */     this.options = null;
/* 104 */     this.fileName = null;
/* 105 */     this.pkgName = null;
/* 106 */     this.fileSha256 = null;
/* 107 */     this.sbpe = null;
/* 108 */     this.plbr = null;
/* 109 */     this.icfg = null;
/* 110 */     this.ppr = null;
/* 111 */     this.className = className;
/*     */   }
/*     */   
/*     */   public Analysis() {
/* 115 */     this.options = null;
/* 116 */     this.fileName = null;
/* 117 */     this.pkgName = null;
/* 118 */     this.fileSha256 = null;
/* 119 */     this.sbpe = null;
/* 120 */     this.plbr = null;
/* 121 */     this.icfg = null;
/* 122 */     this.ppr = null;
/*     */   }
/*     */   
/*     */   public void run(int apiLevel, String path, String className) {
/*     */     try {
/* 127 */       G.reset();
/* 128 */       launchAnalysis(apiLevel, path, className);
/* 129 */     } catch (InterruptedException e) {
/* 130 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void launchAnalysis(int apiLevel, String path, String className) throws InterruptedException {
/* 135 */     this.apiLevel = apiLevel;
/*     */     
/* 137 */     String appRootPath = path;
/* 138 */     Map<Unit, Boolean> containSystemApi = new HashMap<>();
/*     */     
/* 140 */     System.out.println("appRootPath = " + appRootPath);
/*     */     
/* 142 */     FCGBuilder.initSoot(appRootPath, apiLevel);
/* 143 */     System.out.println("TimeUtil.getRunningTime() = " + TimeUtil.getRunningTime());
/*     */     
/* 145 */     this.icfg = new InfoflowCFG();
/*     */     
/* 147 */     this.nbClasses = Scene.v().getApplicationClasses().size();
/*     */     
/* 149 */     System.out.println("Scene.v().getApplicationClasses().size() = " + Scene.v().getApplicationClasses().size());
/* 150 */     int methodNum = 0;
/* 151 */     for (SootClass cls : Scene.v().getApplicationClasses()) {
/* 152 */       methodNum += cls.getMethods().size();
/*     */     }
/* 154 */     System.out.println("methodNum = " + methodNum);
/*     */     
/* 156 */     Vector<String> methodSignatures = new Vector<>();
/* 157 */     for (SootClass cls : Scene.v().getApplicationClasses()) {
/* 158 */       if (cls
/* 159 */         .toString().startsWith("android."))
/*     */       {
/* 161 */         for (SootMethod mtd : cls.getMethods()) {
/* 162 */           if (mtd.hasActiveBody()) {
/* 163 */             Body body = mtd.getActiveBody();
/* 164 */             boolean flag = false;
/* 165 */             for (Unit unit : body.getUnits()) {
/* 166 */               if (unit.toString().contains("TypedArray")) {
/* 167 */                 flag = true;
/*     */                 break;
/*     */               } 
/*     */             } 
/* 171 */             if (flag) {
/* 172 */               methodSignatures.add(mtd.toString());
/*     */             }
/*     */           } 
/*     */         } 
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 180 */     for (String methodSignature : methodSignatures) {
/* 181 */       if (!methodSignature.contains("android.")) {
/*     */         continue;
/*     */       }
/* 184 */       if (className != null && !methodSignature.contains(className)) {
/*     */         continue;
/*     */       }
/* 187 */       System.out.println("begin " + methodSignature);
/* 188 */       SootMethod dummyMainMethod = Scene.v().getMethod(methodSignature);
/*     */       
/* 190 */       this.sbpe = new SimpleBlockPredicateExtraction(this.icfg, dummyMainMethod);
/* 191 */       this.ppr = new PathPredicateRecovery(this.icfg, this.sbpe, dummyMainMethod, true);
/* 192 */       SymbolicExecution se = new SymbolicExecution(this.icfg, dummyMainMethod, this.sbpe, this.ppr);
/* 193 */       if (className != null) {
/* 194 */         se.setClassName(className);
/*     */       }
/* 196 */       this.sbpe.traverse();
/* 197 */       System.out.println("this.sbpe.finish");
/* 198 */       this.ppr.traverse();
/* 199 */       System.out.println("this.ppr.finish");
/* 200 */       System.out.println("this.se.traverse().begin");
/*     */       
/* 202 */       se.traverse();
/* 203 */       this.xlsSet.addAll(se.getOutputXls());
/* 204 */       System.out.println("se: finish");
/* 205 */       if (se.getInterestingFormulae() != null) {
/* 206 */         this.interestingFormulae.addAll(se.getInterestingFormulae());
/*     */       }
/* 208 */       containSystemApi.putAll(se.getContainSystemSpecificPreconditions());
/* 209 */       System.out.println("plbr: finish");
/*     */     } 
/* 211 */     System.out.println("this.interestingFormulae.size() = " + this.interestingFormulae.size());
/* 212 */     System.out.println("containSystemApi.keySet().size() = " + containSystemApi.keySet().size());
/* 213 */     System.out.println("TimeUtil.getRunningTime() = " + TimeUtil.getRunningTime());
/*     */   }
/*     */   
/*     */   public void outputXls() {
/* 217 */     if (this.xlsSet.size() == 0) {
/*     */       return;
/*     */     }
/* 220 */     HSSFWorkbook hSSFWorkbook = new HSSFWorkbook();
/* 221 */     Sheet sheet = hSSFWorkbook.createSheet("output");
/* 222 */     String xlsPath = "/Users/name1/Documents/GitHub/mine-config/ConfDroid/hehe_" + this.apiLevel + ".xls";
/* 223 */     int ptr = 0;
/*     */     
/* 225 */     for (Quartet p : this.xlsSet) {
/*     */       
/* 227 */       Row row = sheet.createRow(ptr);
/* 228 */       String value0 = p.getValue0().toString();
/* 229 */       if (value0.contains(">(")) {
/* 230 */         value0 = value0.split(">\\(")[1];
/*     */       }
/* 232 */       int pptr = 0;
/* 233 */       for (; pptr < value0.length() && (
/* 234 */         Character.isDigit(value0.charAt(pptr)) || Character.isLetter(value0.charAt(pptr)) || value0
/* 235 */         .charAt(pptr) == '_'); pptr++);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 242 */       value0 = value0.substring(0, pptr);
/*     */       
/* 244 */       String value1 = p.getValue1().toString();
/* 245 */       value1 = value1.split(":")[0].substring(1);
/*     */       
/* 247 */       row.createCell(0).setCellValue(value0);
/* 248 */       row.createCell(1).setCellValue("" + value1);
/* 249 */       row.createCell(2).setCellValue("" + p.getValue2());
/* 250 */       row.createCell(3).setCellValue("" + p.getValue3());
/* 251 */       ptr++;
/*     */     } 
/* 253 */     FileOutputStream fos = null;
/*     */     try {
/* 255 */       fos = new FileOutputStream(xlsPath);
/* 256 */     } catch (FileNotFoundException e) {
/* 257 */       e.printStackTrace();
/*     */     } 
/*     */     try {
/* 260 */       hSSFWorkbook.write(fos);
/* 261 */       fos.close();
/* 262 */     } catch (IOException e) {
/* 263 */       e.printStackTrace();
/*     */     } 
/*     */ 
/*     */     
/* 267 */     System.out.println();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private SootMethod getHostSootMethodOfStmt(Unit unit) {
/* 274 */     Chain<SootClass> sootClassChain = Scene.v().getClasses();
/* 275 */     for (SootClass cls : sootClassChain) {
/* 276 */       for (SootMethod mtd : cls.getMethods()) {
/* 277 */         if (mtd.hasActiveBody()) {
/* 278 */           Body body = mtd.getActiveBody();
/* 279 */           if (body.getUnits().contains(unit)) {
/* 280 */             return mtd;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/* 285 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void printResultsInFile(boolean timeoutReached) {
/* 293 */     PrintWriter writer = null;
/*     */     try {
/* 295 */       writer = new PrintWriter(new FileOutputStream(new File(this.options.getOutput()), true));
/* 296 */       writer.append(getRawResults(timeoutReached));
/* 297 */       writer.close();
/* 298 */     } catch (Exception e) {
/* 299 */       this.logger.error(e.getMessage());
/*     */     } 
/*     */   }
/*     */   
/*     */   private void printRawResults(boolean timeoutReached) {
/* 304 */     System.out.println(getRawResults(timeoutReached));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String getRawResults(boolean timeoutReached) {
/* 316 */     SootMethod ifMethod = null;
/* 317 */     SootClass ifClass = null;
/* 318 */     String ifStmtStr = null;
/* 319 */     String ifComponent = null;
/* 320 */     List<SymbolicValue> values = null;
/* 321 */     List<SymbolicValue> visitedValues = new ArrayList<>();
/* 322 */     SymbolicValue sv = null;
/* 323 */     IfStmt ifStmt = null;
/* 324 */     String symbolicValues = null;
/* 325 */     StringBuilder result = new StringBuilder();
/*     */ 
/*     */     
/* 328 */     if (this.plbr != null) {
/* 329 */       for (Map.Entry<IfStmt, Pair<List<SymbolicValue>, SootMethod>> e : (Iterable<Map.Entry<IfStmt, Pair<List<SymbolicValue>, SootMethod>>>)this.plbr.getPotentialLogicBombs().entrySet()) {
/* 330 */         ifStmt = e.getKey();
/* 331 */         symbolicValues = "";
/* 332 */         ifMethod = this.icfg.getMethodOf((Unit)ifStmt);
/* 333 */         ifClass = ifMethod.getDeclaringClass();
/* 334 */         ifStmtStr = String.format("if %s", new Object[] { ifStmt.getCondition() });
/* 335 */         ifComponent = Utils.getComponentType(ifMethod.getDeclaringClass());
/*     */       } 
/*     */     }
/*     */     
/* 339 */     return result.toString();
/*     */   }
/*     */   
/*     */   public void timeoutReachedPrintResults() {
/* 343 */     printResultsInFile(true);
/* 344 */     if (this.options.hasRaw()) {
/* 345 */       printRawResults(true);
/*     */     }
/*     */   }
/*     */   
/*     */   private String getPackageName(String fileName) {
/* 350 */     String pkgName = null;
/* 351 */     ProcessManifest pm = null;
/*     */     try {
/* 353 */       pm = new ProcessManifest(fileName);
/* 354 */       pkgName = pm.getPackageName();
/* 355 */       this.dexSize = pm.getApk().getInputStream("classes.dex").available();
/* 356 */       pm.close();
/* 357 */     } catch (Exception e) {
/* 358 */       this.logger.error(e.getMessage());
/*     */     } 
/* 360 */     return pkgName;
/*     */   }
/*     */   
/*     */   private String getFileSha256(String file) {
/* 364 */     MessageDigest sha256 = null;
/* 365 */     FileInputStream fis = null;
/* 366 */     StringBuffer sb = null;
/* 367 */     byte[] data = null;
/* 368 */     int read = 0;
/* 369 */     byte[] hashBytes = null;
/*     */     
/*     */     try {
/* 372 */       sha256 = MessageDigest.getInstance("SHA-256");
/* 373 */       fis = new FileInputStream(file);
/*     */       
/* 375 */       data = new byte[1024];
/* 376 */       read = 0;
/* 377 */       while ((read = fis.read(data)) != -1) {
/* 378 */         sha256.update(data, 0, read);
/*     */       }
/* 380 */       hashBytes = sha256.digest();
/*     */       
/* 382 */       sb = new StringBuffer();
/* 383 */       for (int i = 0; i < hashBytes.length; i++) {
/* 384 */         sb.append(Integer.toString((hashBytes[i] & 0xFF) + 256, 16).substring(1));
/*     */       }
/* 386 */       fis.close();
/* 387 */     } catch (Exception e) {
/* 388 */       this.logger.error(e.getMessage());
/*     */     } 
/* 390 */     return sb.toString();
/*     */   }
/*     */ }
