/*     */ package androidsourcecode.gitcommit;
/*     */ 
/*     */ import jas.Pair;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import utils.LinuxUtil;
/*     */ import utils.SootUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BuildGitCommitsInDifferentVersions
/*     */ {
/*  14 */   private static String androidFrameworkRootPath = SootUtils.androidFrameworkRootPath;
/*     */   
/*  16 */   public ArrayList<CodeChange> codeChangeArrayList = new ArrayList<>();
/*     */ 
/*     */ 
/*     */   
/*     */   public static ArrayList<CodeChange> getInterestingApiChangesFromGitCommit(String filePath, int apiLevel) throws Exception {
/*  21 */     ArrayList<CodeChange> retList = new ArrayList<>();
/*  22 */     ArrayList<CodeChange> srcHunks = CodeChangeAnalysis.runCodeChangeAnalysis(filePath, "", apiLevel);
/*     */     
/*  24 */     for (CodeChange srcHunk : srcHunks) {
/*  25 */       String gitFilePath = srcHunk.filePath;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  32 */       if (gitFilePath == null || gitFilePath.length() == 0);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  40 */     ArrayList<CodeChange> hunks = CodeChangeAnalysis.runCodeChangeAnalysis(filePath, "", apiLevel);
/*     */     
/*  42 */     retList.addAll(hunks);
/*  43 */     return retList;
/*     */   }
/*     */   
/*     */   public BuildGitCommitsInDifferentVersions(String filePath, int apiLevel) throws Exception {
/*  47 */     this.codeChangeArrayList.clear();
/*  48 */     System.out.println("filePath: " + filePath);
/*     */     
/*  50 */     this.codeChangeArrayList.addAll(getInterestingApiChangesFromGitCommit(filePath, apiLevel));
/*  51 */     System.out.println("codeChangeArrayList.size(): " + this.codeChangeArrayList.size());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static ArrayList<Pair<String, Integer>> getDiff(ArrayList<CodeChange> hunks1, ArrayList<CodeChange> hunks2, String type) {
/*  57 */     ArrayList<Pair<CodeChange, CodeChange>> equivalentChangeHunkPairs = new ArrayList<>();
/*  58 */     ArrayList<Pair<String, Integer>> ret = new ArrayList<>();
/*  59 */     for (CodeChange hunk1 : hunks1) {
/*  60 */       for (CodeChange hunk2 : hunks2) {
/*  61 */         if (hunk1.filePath.substring(2).equals(hunk2.filePath.substring(2))) {
/*  62 */           equivalentChangeHunkPairs.add(new Pair(hunk1, hunk2));
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/*  67 */     for (Pair<CodeChange, CodeChange> pair : equivalentChangeHunkPairs) {
/*  68 */       CodeChange h1 = (CodeChange)pair.getO1(), h2 = (CodeChange)pair.getO2();
/*  69 */       if (type.equals("-")) {
/*  70 */         ArrayList<Pair<String, Integer>> h1Diffs = h1.aDiffPairs;
/*  71 */         ArrayList<Pair<String, Integer>> h2Diffs = h2.aDiffPairs;
/*     */         
/*  73 */         for (Pair<String, Integer> h1Diff : h1Diffs) {
/*  74 */           boolean flag = false;
/*  75 */           for (Pair<String, Integer> h2Diff : h2Diffs) {
/*  76 */             if (((String)h1Diff.getO1()).equals(h2Diff.getO1()) && Math.abs(((Integer)h1Diff.getO2()).intValue() - ((Integer)h2Diff.getO2()).intValue()) < 10) {
/*  77 */               flag = true;
/*     */             }
/*     */           } 
/*  80 */           if (!flag && 
/*  81 */             !ret.contains(h1Diff))
/*  82 */             ret.add(h1Diff); 
/*     */         } 
/*     */         continue;
/*     */       } 
/*  86 */       if (type.equals("+")) {
/*  87 */         ArrayList<Pair<String, Integer>> h1Diffs = h1.bDiffPairs;
/*  88 */         ArrayList<Pair<String, Integer>> h2Diffs = h2.bDiffPairs;
/*  89 */         for (Pair<String, Integer> h1Diff : h1Diffs) {
/*  90 */           boolean flag = false;
/*  91 */           for (Pair<String, Integer> h2Diff : h2Diffs) {
/*  92 */             if (((String)h1Diff.getO1()).equals(h2Diff.getO1()) && Math.abs(((Integer)h1Diff.getO2()).intValue() - ((Integer)h2Diff.getO2()).intValue()) < 10) {
/*  93 */               flag = true;
/*     */             }
/*     */           } 
/*  96 */           if (!flag && 
/*  97 */             !ret.contains(h1Diff)) {
/*  98 */             ret.add(h1Diff);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 104 */     return ret;
/*     */   }
/*     */ 
/*     */   
/*     */   private static ArrayList<Pair<String, Integer>> getIntersect(ArrayList<CodeChange> hunks1, ArrayList<CodeChange> hunks2, String type) {
/* 109 */     ArrayList<Pair<CodeChange, CodeChange>> equivalentChangeHunkPairs = new ArrayList<>();
/* 110 */     ArrayList<Pair<String, Integer>> ret = new ArrayList<>();
/* 111 */     for (CodeChange hunk1 : hunks1) {
/* 112 */       for (CodeChange hunk2 : hunks2) {
/* 113 */         if (hunk1.filePath.substring(2).equals(hunk2.filePath.substring(2))) {
/* 114 */           equivalentChangeHunkPairs.add(new Pair(hunk1, hunk2));
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 119 */     for (Pair<CodeChange, CodeChange> pair : equivalentChangeHunkPairs) {
/* 120 */       CodeChange h1 = (CodeChange)pair.getO1(), h2 = (CodeChange)pair.getO2();
/* 121 */       if (type.equals("-")) {
/* 122 */         ArrayList<Pair<String, Integer>> h1Diffs = h1.aDiffPairs;
/* 123 */         ArrayList<Pair<String, Integer>> h2Diffs = h2.aDiffPairs;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 129 */         for (Pair<String, Integer> h1Diff : h1Diffs) {
/* 130 */           for (Pair<String, Integer> h2Diff : h2Diffs) {
/* 131 */             if (((String)h1Diff.getO1()).equals(h2Diff.getO1()) && Math.abs(((Integer)h1Diff.getO2()).intValue() - ((Integer)h2Diff.getO2()).intValue()) < 10 && 
/* 132 */               !ret.contains(h1Diff))
/* 133 */               ret.add(h1Diff); 
/*     */           } 
/*     */         } 
/*     */         continue;
/*     */       } 
/* 138 */       if (type.equals("+")) {
/* 139 */         ArrayList<Pair<String, Integer>> h1Diffs = h1.bDiffPairs;
/* 140 */         ArrayList<Pair<String, Integer>> h2Diffs = h2.bDiffPairs;
/* 141 */         for (Pair<String, Integer> h1Diff : h1Diffs) {
/*     */           
/* 143 */           for (Pair<String, Integer> h2Diff : h2Diffs) {
/* 144 */             if (((String)h1Diff.getO1()).equals(h2Diff.getO1()) && Math.abs(((Integer)h1Diff.getO2()).intValue() - ((Integer)h2Diff.getO2()).intValue()) < 10)
/*     */             {
/* 146 */               if (!ret.contains(h2Diff)) {
/* 147 */                 ret.add(h2Diff);
/*     */               }
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 154 */     return ret;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void gitDiffOf2Commits(String commitNum1, String commitNum2, String path, String outputFilePath) throws Exception {
/* 160 */     List<String> commands = new ArrayList<>();
/* 161 */     System.out.println("git diff " + commitNum1 + " " + commitNum2 + " " + path + " > " + outputFilePath);
/* 162 */     commands.add("cd " + SootUtils.androidFrameworkRootPath);
/* 163 */     commands.add("git diff " + commitNum1 + " " + commitNum2 + " " + path + " > " + outputFilePath);
/* 164 */     LinuxUtil.exec(commands);
/*     */   }
/*     */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/androidsourcecode/gitcommit/BuildGitCommitsInDifferentVersions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */