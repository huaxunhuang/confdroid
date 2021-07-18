/*    */ package androidsourcecode.gitcommit;
/*    */ 
/*    */ import jas.Pair;
/*    */ import java.util.ArrayList;
/*    */ 
/*    */ public class InterestingApiChanges
/*    */ {
/*  8 */   public int api_level = -1;
/*  9 */   public String gitCommit = "";
/* 10 */   public String filePath = "";
/* 11 */   public ArrayList<Pair<String, Integer>> aDiffPairs = new ArrayList<>();
/* 12 */   public ArrayList<Pair<String, Integer>> bDiffPairs = new ArrayList<>();
/*    */ 
/*    */   
/*    */   public String toString() {
/* 16 */     String str = "";
/* 17 */     str = str + "=== interesting fcgbuilder changes ===\n";
/* 18 */     str = str + "filePath: " + this.filePath + "\n";
/* 19 */     str = str + "fcgbuilder level: " + this.api_level + "\n";
/* 20 */     str = str + "aDiffPairs \n";
/* 21 */     str = str + this.aDiffPairs.toString() + "\n";
/* 22 */     str = str + "bDiffPairs \n";
/* 23 */     str = str + this.bDiffPairs.toString() + "\n";
/* 24 */     str = str + "====== ======";
/*    */     
/* 26 */     return str;
/*    */   }
/*    */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/androidsourcecode/gitcommit/InterestingApiChanges.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */