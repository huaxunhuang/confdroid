/*     */ package androidsourcecode.gitcommit;
/*     */ 
/*     */ import androidsourcecode.AndroidSourceCodeUtil;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import jas.Pair;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Calendar;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import soot.SootClass;
/*     */ import soot.SootMethod;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CodeChange
/*     */ {
/*  22 */   public String filePath = "";
/*  23 */   public String className = "";
/*  24 */   String gitCommit = "";
/*  25 */   public ArrayList<Pair<String, Integer>> aDiffPairs = new ArrayList<>();
/*  26 */   public ArrayList<Pair<String, Integer>> bDiffPairs = new ArrayList<>();
/*  27 */   Calendar gitCommitDate = null;
/*     */   
/*     */   int apiLevel;
/*  30 */   public Map<SootMethod, List<Pair<String, Integer>>> aMethodPairMap = new HashMap<>();
/*  31 */   public Map<SootMethod, List<Pair<String, Integer>>> bMethodPairMap = new HashMap<>();
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   private static String converToClassName(String filePath) {
/*  36 */     if (!filePath.endsWith(".java")) {
/*  37 */       return null;
/*     */     }
/*  39 */     int index = 0;
/*  40 */     if (filePath.contains("com/android/")) {
/*  41 */       index = filePath.indexOf("com/android/");
/*     */     }
/*  43 */     else if (filePath.contains("android/")) {
/*  44 */       index = filePath.indexOf("android/");
/*     */     } 
/*     */     
/*  47 */     String str = filePath.substring(index);
/*  48 */     str = str.replace(".java", "");
/*  49 */     str = str.replace('/', '.');
/*  50 */     return str;
/*     */   }
/*     */   
/*     */   public CodeChange(String filePath, ArrayList<Pair<String, Integer>> aDiffPairs, ArrayList<Pair<String, Integer>> bDiffPairs, String gitCommit, Calendar gitCommitDate, int apiLevel) {
/*  54 */     this.filePath = filePath;
/*  55 */     this.className = converToClassName(filePath);
/*  56 */     this.aDiffPairs = aDiffPairs;
/*  57 */     this.bDiffPairs = bDiffPairs;
/*  58 */     this.gitCommit = gitCommit;
/*  59 */     this.gitCommitDate = gitCommitDate;
/*  60 */     this.apiLevel = apiLevel;
/*  61 */     buildMethodPairMap(apiLevel);
/*     */   }
/*     */   
/*     */   public void buildMethodPairMap(int apiLevel) {
/*  65 */     SootClass sootClass1 = AndroidSourceCodeUtil.getSootClassByPath(this.filePath, apiLevel);
/*  66 */     SootClass sootClass2 = AndroidSourceCodeUtil.getSootClassByPath(this.filePath, apiLevel);
/*  67 */     if (sootClass1 == null && sootClass2 == null) {
/*     */       return;
/*     */     }
/*     */     
/*  71 */     List<SootMethod> aMethodList = sootClass1.getMethods();
/*  72 */     List<SootMethod> bMethodList = sootClass2.getMethods();
/*     */     
/*  74 */     if (sootClass1 != null) {
/*  75 */       for (Pair<String, Integer> aDiffPair : this.aDiffPairs) {
/*  76 */         for (SootMethod mtd : aMethodList) {
/*  77 */           int startLineNum = AndroidSourceCodeUtil.getStartLineNum(mtd);
/*  78 */           int endLineNum = AndroidSourceCodeUtil.getEndLineNum(mtd);
/*  79 */           int pairLineNum = ((Integer)aDiffPair.getO2()).intValue();
/*  80 */           if (startLineNum <= pairLineNum && pairLineNum <= endLineNum) {
/*  81 */             if (!this.aMethodPairMap.containsKey(mtd)) {
/*  82 */               this.aMethodPairMap.put(mtd, new ArrayList<>());
/*     */             }
/*     */             
/*  85 */             List<Pair<String, Integer>> tmpList = this.aMethodPairMap.get(mtd);
/*  86 */             tmpList.add(aDiffPair);
/*  87 */             this.aMethodPairMap.put(mtd, tmpList);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*  93 */     if (sootClass2 != null)
/*  94 */       for (Pair<String, Integer> bDiffPair : this.bDiffPairs) {
/*  95 */         for (SootMethod mtd : bMethodList) {
/*  96 */           int startLineNum = AndroidSourceCodeUtil.getStartLineNum(mtd);
/*  97 */           int endLineNum = AndroidSourceCodeUtil.getEndLineNum(mtd);
/*  98 */           int pairLineNum = ((Integer)bDiffPair.getO2()).intValue();
/*  99 */           if (startLineNum <= pairLineNum && pairLineNum <= endLineNum) {
/* 100 */             if (!this.bMethodPairMap.containsKey(mtd)) {
/* 101 */               this.bMethodPairMap.put(mtd, new ArrayList<>());
/*     */             }
/*     */             
/* 104 */             List<Pair<String, Integer>> tmpList = this.bMethodPairMap.get(mtd);
/* 105 */             tmpList.add(bDiffPair);
/* 106 */             this.bMethodPairMap.put(mtd, tmpList);
/*     */           } 
/*     */         } 
/*     */       }  
/*     */   }
/*     */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/androidsourcecode/gitcommit/CodeChange.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */