/*    */ package utils;
/*    */ 
/*    */ import java.util.Calendar;
/*    */ import java.util.Date;
/*    */ import java.util.GregorianCalendar;
/*    */ import java.util.HashMap;
/*    */ 
/*    */ public class DateUtil
/*    */ {
/*    */   public static int getApiLevel(Calendar calendar) {
/* 11 */     Date date = calendar.getTime();
/* 12 */     for (int i = 18; i <= 28; i++) {
/* 13 */       Date apiLevelDate = ((Calendar)VersionReleaseDate.apiLevelReleasedDate.get(Integer.valueOf(i))).getTime();
/* 14 */       if (apiLevelDate.after(date)) {
/* 15 */         return i;
/*    */       }
/*    */     } 
/* 18 */     return -1;
/*    */   }
/*    */   
/*    */   public static String getCommitApi2(Calendar calendar) {
/* 22 */     Date date = calendar.getTime();
/* 23 */     for (int i = 18; i <= 28; i++) {
/* 24 */       Date apiLevelDate = ((Calendar)VersionReleaseDate.apiLevelReleasedDate.get(Integer.valueOf(i))).getTime();
/* 25 */       if (apiLevelDate.after(date)) {
/* 26 */         return VersionReleaseDate.apiLevelGitCommit.get(Integer.valueOf(i));
/*    */       }
/*    */     } 
/* 29 */     return "";
/*    */   }
/*    */   
/*    */   public static String getCommitApi1(Calendar calendar) {
/* 33 */     Date date = calendar.getTime();
/* 34 */     for (int i = 28; i >= 18; i--) {
/* 35 */       Date apiLevelDate = ((Calendar)VersionReleaseDate.apiLevelReleasedDate.get(Integer.valueOf(i))).getTime();
/* 36 */       if (apiLevelDate.before(date)) {
/* 37 */         return VersionReleaseDate.apiLevelGitCommit.get(Integer.valueOf(i));
/*    */       }
/*    */     } 
/* 40 */     return "";
/*    */   }
/*    */   
/* 43 */   public static HashMap<String, Integer> months = new HashMap<>();
/*    */   static {
/* 45 */     months.put("Jan", Integer.valueOf(1));
/* 46 */     months.put("Feb", Integer.valueOf(2));
/* 47 */     months.put("Mar", Integer.valueOf(3));
/* 48 */     months.put("Apr", Integer.valueOf(4));
/* 49 */     months.put("May", Integer.valueOf(5));
/* 50 */     months.put("Jun", Integer.valueOf(6));
/* 51 */     months.put("Jul", Integer.valueOf(7));
/* 52 */     months.put("Aug", Integer.valueOf(8));
/* 53 */     months.put("Sep", Integer.valueOf(9));
/* 54 */     months.put("Oct", Integer.valueOf(10));
/* 55 */     months.put("Nov", Integer.valueOf(11));
/* 56 */     months.put("Dec", Integer.valueOf(12));
/*    */   }
/*    */   
/*    */   public static Calendar transferStrToCalendar(String calendarStr) {
/* 60 */     String[] substr = calendarStr.split(" ");
/* 61 */     System.out.println(calendarStr);
/* 62 */     int month = ((Integer)months.get(substr[3])).intValue();
/* 63 */     int day = Integer.parseInt(substr[4]);
/* 64 */     int year = Integer.parseInt(substr[6]);
/*    */     
/* 66 */     return new GregorianCalendar(year, month - 1, day, 0, 0, 0);
/*    */   }
/*    */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/utils/DateUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */