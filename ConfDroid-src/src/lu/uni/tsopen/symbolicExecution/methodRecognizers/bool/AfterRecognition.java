/*    */ package lu.uni.tsopen.symbolicExecution.methodRecognizers.bool;
/*    */ 
/*    */ import java.util.List;
/*    */ import lu.uni.tsopen.symbolicExecution.SymbolicExecution;
/*    */ import lu.uni.tsopen.symbolicExecution.symbolicValues.SymbolicValue;
/*    */ import lu.uni.tsopen.utils.Utils;
/*    */ import soot.SootMethod;
/*    */ import soot.Value;
/*    */ import soot.tagkit.StringConstantValueTag;
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
/*    */ public class AfterRecognition
/*    */   extends BooleanMethodsRecognitionHandler
/*    */ {
/*    */   public AfterRecognition(BooleanMethodsRecognitionHandler next, SymbolicExecution se) {
/* 44 */     super(next, se);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean processBooleanMethod(SootMethod method, Value base, SymbolicValue sv, List<Value> args) {
/* 49 */     Value firstArg = null;
/* 50 */     String methodName = method.getName();
/* 51 */     if (methodName.equals("after")) {
/* 52 */       firstArg = args.get(0);
/* 53 */       if (Utils.containsTag(base, "#now", this.se) && (
/* 54 */         firstArg.getType().toString().equals("java.util.Date") || firstArg
/* 55 */         .getType().toString().equals("java.util.Calendar") || firstArg
/* 56 */         .getType().toString().equals("java.util.GregorianCalendar") || firstArg
/* 57 */         .getType().toString().equals("java.text.SimpleDateFormat") || firstArg
/* 58 */         .getType().toString().equals("java.time.LocalDateTime") || firstArg
/* 59 */         .getType().toString().equals("java.time.LocalDate"))) {
/* 60 */         sv.addTag(new StringConstantValueTag("#Suspicious"));
/* 61 */         return true;
/*    */       } 
/*    */     } 
/*    */     
/* 65 */     return false;
/*    */   }
/*    */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/lu/uni/tsopen/symbolicExecution/methodRecognizers/bool/AfterRecognition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */