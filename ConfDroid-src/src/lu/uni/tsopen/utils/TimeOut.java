/*    */ package lu.uni.tsopen.utils;
/*    */ 
/*    */ import java.util.Calendar;
/*    */ import java.util.Timer;
/*    */ import java.util.TimerTask;
/*    */ import lu.uni.tsopen.Analysis;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
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
/*    */ 
/*    */ public class TimeOut
/*    */ {
/*    */   private Timer timer;
/* 43 */   private TimerTask exitTask = null;
/*    */   
/*    */   private int timeout;
/* 46 */   protected Logger logger = LoggerFactory.getLogger(getClass());
/*    */   
/*    */   public TimeOut(int n, final Analysis analysis) {
/* 49 */     this.timer = new Timer();
/* 50 */     this.exitTask = new TimerTask()
/*    */       {
/*    */         public void run() {
/* 53 */           TimeOut.this.logger.warn("Timeout reached !");
/* 54 */           TimeOut.this.logger.warn("Ending program...");
/* 55 */           analysis.timeoutReachedPrintResults();
/* 56 */           System.exit(0);
/*    */         }
/*    */       };
/* 59 */     this.timeout = (n != 0) ? n : 60;
/*    */   }
/*    */   
/*    */   public void trigger() {
/* 63 */     Calendar c = Calendar.getInstance();
/* 64 */     c.add(12, this.timeout);
/* 65 */     this.timer.schedule(this.exitTask, c.getTime());
/*    */   }
/*    */   
/*    */   public void cancel() {
/* 69 */     this.timer.cancel();
/*    */   }
/*    */   
/*    */   public int getTimeout() {
/* 73 */     return this.timeout;
/*    */   }
/*    */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/lu/uni/tsopen/utils/TimeOut.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */