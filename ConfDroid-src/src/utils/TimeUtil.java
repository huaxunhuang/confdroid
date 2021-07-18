/*    */ package utils;
/*    */ 
/*    */ public class TimeUtil {
/*    */   public static long startTime;
/*    */   
/*    */   public static void start() {
/*  7 */     startTime = System.currentTimeMillis();
/*    */   }
/*    */   
/*    */   public static long getRunningTime() {
/* 11 */     return System.currentTimeMillis() - startTime;
/*    */   }
/*    */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/utils/TimeUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */