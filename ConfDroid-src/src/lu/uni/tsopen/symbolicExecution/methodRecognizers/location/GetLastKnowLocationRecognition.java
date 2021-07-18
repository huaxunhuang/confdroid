/*    */ package lu.uni.tsopen.symbolicExecution.methodRecognizers.location;
/*    */ 
/*    */ import lu.uni.tsopen.symbolicExecution.SymbolicExecution;
/*    */ import lu.uni.tsopen.symbolicExecution.symbolicValues.SymbolicValue;
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
/*    */ public class GetLastKnowLocationRecognition
/*    */   extends LocationMethodsRecognitionHandler
/*    */ {
/*    */   public GetLastKnowLocationRecognition(LocationMethodsRecognitionHandler next, SymbolicExecution se) {
/* 40 */     super(next, se);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean processLocationMethod(SootMethod method, SymbolicValue sv) {
/* 45 */     String className = method.getDeclaringClass().getName();
/* 46 */     String methodName = method.getName();
/* 47 */     if (className.equals("android.location.LocationManager") && methodName.equals("getLastKnownLocation")) {
/* 48 */       sv.addTag(new StringConstantValueTag("#here"));
/* 49 */       return true;
/*    */     } 
/* 51 */     return false;
/*    */   }
/*    */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/lu/uni/tsopen/symbolicExecution/methodRecognizers/location/GetLastKnowLocationRecognition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */