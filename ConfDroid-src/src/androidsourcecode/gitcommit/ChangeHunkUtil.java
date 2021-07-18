/*    */ package androidsourcecode.gitcommit;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.FileInputStream;
/*    */ import java.io.FileNotFoundException;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Calendar;
/*    */ import java.util.Scanner;
/*    */ import utils.DateUtil;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ChangeHunkUtil
/*    */ {
/* 16 */   private static ArrayList<String> changedCode = new ArrayList<>();
/* 17 */   public static ArrayList<GitCommitLog> gitCommitLogs = new ArrayList<>();
/* 18 */   public static String gitCommitNum = "";
/* 19 */   public static Calendar gitCommitDate = null;
/*    */   
/*    */   public static void breakingGitDiff(String filePath, String gitFilePath) throws FileNotFoundException {
/* 22 */     gitCommitLogs.clear();
/* 23 */     changedCode.clear();
/* 24 */     Scanner scan = new Scanner(new FileInputStream(new File(filePath)));
/*    */     
/* 26 */     GitCommitLog gitCommitLog = null;
/* 27 */     boolean flag = false;
/*    */     
/* 29 */     if (!scan.hasNextLine()) {
/* 30 */       gitCommitLog = new GitCommitLog();
/* 31 */       gitCommitLog.gitCommit = "";
/* 32 */       gitCommitLog.filePath = gitFilePath;
/* 33 */       gitCommitLog.changedCode = new ArrayList<>();
/* 34 */       gitCommitLogs.add(gitCommitLog);
/* 35 */       scan.close();
/*    */       return;
/*    */     } 
/* 38 */     while (scan.hasNextLine()) {
/* 39 */       String nextLine = scan.nextLine();
/* 40 */       if (nextLine.startsWith("commit")) {
/* 41 */         gitCommitNum = nextLine.split(" ")[1]; continue;
/* 42 */       }  if (nextLine.startsWith("Date:")) {
/*    */         
/* 44 */         String gitCommitDateStr = nextLine.split(" ")[1] + " " + nextLine.split(" ")[2] + " " + nextLine.split(" ")[3] + " " + nextLine.split(" ")[4] + " " + nextLine.split(" ")[5] + " " + nextLine.split(" ")[6] + " " + nextLine.split(" ")[7];
/* 45 */         gitCommitDate = DateUtil.transferStrToCalendar(gitCommitDateStr); continue;
/*    */       } 
/* 47 */       if (nextLine.startsWith("diff --git ")) {
/* 48 */         if (!flag) {
/* 49 */           gitCommitLog = new GitCommitLog();
/* 50 */           gitCommitLog.gitCommit = gitCommitNum;
/* 51 */           gitCommitLog.gitCommitDate = gitCommitDate;
/* 52 */           flag = true; continue;
/*    */         } 
/* 54 */         ArrayList<String> copyChangedCode = new ArrayList<>(changedCode);
/* 55 */         gitCommitLog.changedCode = copyChangedCode;
/* 56 */         gitCommitLogs.add(gitCommitLog);
/* 57 */         gitCommitLog = new GitCommitLog();
/* 58 */         gitCommitLog.gitCommit = gitCommitNum;
/* 59 */         gitCommitLog.gitCommitDate = gitCommitDate;
/* 60 */         changedCode.clear();
/*    */         continue;
/*    */       } 
/* 63 */       if (nextLine.startsWith("--- a")) {
/* 64 */         String aFile = nextLine.split(" ")[1];
/* 65 */         if (!aFile.contains("null"))
/* 66 */           gitCommitLog.filePath = aFile; 
/*    */         continue;
/*    */       } 
/* 69 */       if (nextLine.startsWith("+++ b")) {
/* 70 */         String bFile = nextLine.split(" ")[1];
/* 71 */         if (!bFile.contains("null")) {
/* 72 */           gitCommitLog.filePath = bFile;
/*    */         }
/*    */         
/*    */         continue;
/*    */       } 
/* 77 */       changedCode.add(nextLine);
/*    */     } 
/*    */ 
/*    */     
/* 81 */     if (gitCommitLog != null) {
/*    */       
/* 83 */       ArrayList<String> copyChangedCode = new ArrayList<>(changedCode);
/* 84 */       gitCommitLog.changedCode = copyChangedCode;
/* 85 */       gitCommitLogs.add(gitCommitLog);
/*    */     } 
/*    */     
/* 88 */     scan.close();
/*    */   }
/*    */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/androidsourcecode/gitcommit/ChangeHunkUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */