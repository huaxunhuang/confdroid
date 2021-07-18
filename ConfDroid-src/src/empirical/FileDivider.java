/*    */ package empirical;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.FileOutputStream;
/*    */ import java.io.PrintWriter;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.Scanner;
/*    */ 
/*    */ public class FileDivider {
/*    */   public static void main(String[] args) {
/*    */     try {
/* 13 */       File file = new File("/Users/name1/Documents/interestingConfigGitCommit/output.txt");
/* 14 */       Scanner scanner = new Scanner(file);
/* 15 */       List<String> tempList = new ArrayList<>();
/*    */       
/* 17 */       boolean flag = false;
/*    */       
/* 19 */       int index = 0;
/* 20 */       while (scanner.hasNextLine()) {
/* 21 */         String next = scanner.nextLine();
/* 22 */         if (next.contains("#interesting diffs#")) {
/* 23 */           if (flag) {
/* 24 */             List<String> copyList = new ArrayList<>(tempList);
/* 25 */             tempList.clear();
/* 26 */             PrintWriter out = new PrintWriter(new FileOutputStream("/Users/name1/Documents/interestingConfigGitCommit/" + index + ".txt"));
/* 27 */             for (String str : copyList) {
/* 28 */               out.println(str);
/*    */             }
/* 30 */             out.close();
/*    */           } 
/* 32 */           flag = true;
/* 33 */           index++; continue;
/*    */         } 
/* 35 */         tempList.add(next);
/*    */       }
/*    */     
/*    */     }
/* 39 */     catch (Exception e) {
/* 40 */       e.printStackTrace();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/empirical/FileDivider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */