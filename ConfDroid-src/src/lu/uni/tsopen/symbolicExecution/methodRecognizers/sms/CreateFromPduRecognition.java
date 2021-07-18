/*    */ package lu.uni.tsopen.symbolicExecution.methodRecognizers.sms;
/*    */ 
/*    */ import lu.uni.tsopen.symbolicExecution.SymbolicExecution;
/*    */ import lu.uni.tsopen.symbolicExecution.symbolicValues.SymbolicValue;
/*    */ import soot.SootClass;
/*    */ import soot.SootMethod;
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
/*    */ public class CreateFromPduRecognition
/*    */   extends SmsMethodsRecognitionHandler
/*    */ {
/*    */   public CreateFromPduRecognition(SmsMethodsRecognitionHandler next, SymbolicExecution se) {
/* 41 */     super(next, se);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean processSmsMethod(SootMethod method, SymbolicValue sv) {
/* 46 */     SootClass declaringClass = method.getDeclaringClass();
/* 47 */     String methodName = method.getName();
/* 48 */     if (methodName.equals("createFromPdu") && declaringClass.getName().equals("android.telephony.SmsMessage")) {
/* 49 */       sv.addTag(new StringConstantValueTag("#sms"));
/* 50 */       return true;
/*    */     } 
/* 52 */     return false;
/*    */   }
/*    */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/lu/uni/tsopen/symbolicExecution/methodRecognizers/sms/CreateFromPduRecognition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */