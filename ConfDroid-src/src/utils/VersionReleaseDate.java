/*    */ package utils;
/*    */ 
/*    */ import java.util.Calendar;
/*    */ import java.util.GregorianCalendar;
/*    */ import java.util.HashMap;
/*    */ 
/*    */ public class VersionReleaseDate
/*    */ {
/*  9 */   public static HashMap<Integer, String> apiLevelGitCommit = new HashMap<>();
/* 10 */   public static HashMap<Integer, Calendar> apiLevelReleasedDate = new HashMap<>();
/*    */   
/*    */   static {
/* 13 */     apiLevelReleasedDate.put(Integer.valueOf(28), new GregorianCalendar(2018, 7, 6, 0, 0, 0));
/* 14 */     apiLevelReleasedDate.put(Integer.valueOf(27), new GregorianCalendar(2017, 11, 5, 0, 0, 0));
/* 15 */     apiLevelReleasedDate.put(Integer.valueOf(26), new GregorianCalendar(2017, 7, 21, 0, 0, 0));
/* 16 */     apiLevelReleasedDate.put(Integer.valueOf(25), new GregorianCalendar(2016, 11, 5, 0, 0, 0));
/* 17 */     apiLevelReleasedDate.put(Integer.valueOf(24), new GregorianCalendar(2016, 4, 18, 0, 0, 0));
/* 18 */     apiLevelReleasedDate.put(Integer.valueOf(23), new GregorianCalendar(2015, 9, 5, 0, 0, 0));
/* 19 */     apiLevelReleasedDate.put(Integer.valueOf(22), new GregorianCalendar(2015, 1, 9, 0, 0, 0));
/* 20 */     apiLevelReleasedDate.put(Integer.valueOf(21), new GregorianCalendar(2014, 10, 12, 0, 0, 0));
/* 21 */     apiLevelReleasedDate.put(Integer.valueOf(20), new GregorianCalendar(2014, 10, 11, 0, 0, 0));
/* 22 */     apiLevelReleasedDate.put(Integer.valueOf(19), new GregorianCalendar(2013, 9, 31, 0, 0, 0));
/* 23 */     apiLevelReleasedDate.put(Integer.valueOf(18), new GregorianCalendar(2013, 5, 24, 0, 0, 0));
/*    */     
/* 25 */     apiLevelGitCommit.put(Integer.valueOf(28), "6549309");
/* 26 */     apiLevelGitCommit.put(Integer.valueOf(27), "250714c");
/* 27 */     apiLevelGitCommit.put(Integer.valueOf(26), "899eecd");
/* 28 */     apiLevelGitCommit.put(Integer.valueOf(25), "b537505");
/* 29 */     apiLevelGitCommit.put(Integer.valueOf(24), "0813c5a");
/* 30 */     apiLevelGitCommit.put(Integer.valueOf(23), "c02a10d");
/* 31 */     apiLevelGitCommit.put(Integer.valueOf(22), "d0f748a");
/* 32 */     apiLevelGitCommit.put(Integer.valueOf(21), "ba35a77");
/* 33 */     apiLevelGitCommit.put(Integer.valueOf(20), "ba35a77");
/* 34 */     apiLevelGitCommit.put(Integer.valueOf(19), "1aed5ea");
/* 35 */     apiLevelGitCommit.put(Integer.valueOf(18), "2e55547");
/*    */   }
/*    */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/utils/VersionReleaseDate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */