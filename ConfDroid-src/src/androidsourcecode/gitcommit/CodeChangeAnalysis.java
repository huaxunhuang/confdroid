/*    */ package androidsourcecode.gitcommit;
/*    */ 
/*    */ import jas.Pair;
/*    */ import java.io.FileNotFoundException;
/*    */ import java.util.ArrayList;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CodeChangeAnalysis
/*    */ {
/* 15 */   public static ArrayList<CodeChange> codeChanges = new ArrayList<>();
/*    */ 
/*    */   
/*    */   public static ArrayList<CodeChange> runCodeChangeAnalysis(String filePath, String gitFilePath, int apiLevel) {
/* 19 */     codeChanges.clear();
/* 20 */     ArrayList<CodeChange> ret = new ArrayList<>();
/*    */     try {
/* 22 */       ChangeHunkUtil.breakingGitDiff(filePath, gitFilePath);
/* 23 */       ArrayList<GitCommitLog> gitCommitLogs = ChangeHunkUtil.gitCommitLogs;
/* 24 */       for (GitCommitLog gitCommitLog : gitCommitLogs) {
/* 25 */         ret.add(analyzeChangedCode(gitCommitLog, apiLevel));
/*    */       }
/* 27 */       return ret;
/* 28 */     } catch (FileNotFoundException e) {
/* 29 */       e.printStackTrace();
/*    */       
/* 31 */       return ret;
/*    */     } 
/*    */   }
/*    */   private static CodeChange analyzeChangedCode(GitCommitLog gitCommitLog, int apiLevel) {
/* 35 */     boolean flag = false;
/* 36 */     ArrayList<Pair<String, Integer>> aDiffPairs = new ArrayList<>();
/* 37 */     ArrayList<Pair<String, Integer>> bDiffPairs = new ArrayList<>();
/* 38 */     int tempAStart = -1;
/* 39 */     int tempBStart = -1;
/*    */     
/* 41 */     for (int index = 0; index < gitCommitLog.changedCode.size(); index++) {
/* 42 */       String stmt = gitCommitLog.changedCode.get(index);
/*    */       
/* 44 */       if (stmt.startsWith("@@")) {
/* 45 */         String aStartStr = stmt.split(" ")[1];
/* 46 */         String bStartStr = stmt.split(" ")[2];
/*    */         
/* 48 */         int aStart = Integer.parseInt(aStartStr.split(",")[0]) * -1;
/* 49 */         int bStart = Integer.parseInt(bStartStr.split(",")[0]);
/* 50 */         tempAStart = aStart;
/* 51 */         tempBStart = bStart;
/*    */       }
/* 53 */       else if (stmt.startsWith("-")) {
/* 54 */         aDiffPairs.add(new Pair(stmt, Integer.valueOf(tempAStart)));
/* 55 */         tempAStart++;
/* 56 */       } else if (stmt.startsWith("+")) {
/* 57 */         bDiffPairs.add(new Pair(stmt, Integer.valueOf(tempBStart)));
/* 58 */         tempBStart++;
/*    */       } else {
/* 60 */         tempAStart++;
/* 61 */         tempBStart++;
/*    */       } 
/*    */     } 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 74 */     CodeChange codeChange = new CodeChange(gitCommitLog.filePath, aDiffPairs, bDiffPairs, gitCommitLog.gitCommit, gitCommitLog.gitCommitDate, apiLevel);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 81 */     codeChanges.add(codeChange);
/* 82 */     return codeChange;
/*    */   }
/*    */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/androidsourcecode/gitcommit/CodeChangeAnalysis.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */