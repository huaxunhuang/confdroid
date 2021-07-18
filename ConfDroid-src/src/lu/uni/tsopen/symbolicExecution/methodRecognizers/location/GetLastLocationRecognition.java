/*    */ package lu.uni.tsopen.symbolicExecution.methodRecognizers.location;
/*    */ 
/*    */ import lu.uni.tsopen.symbolicExecution.SymbolicExecution;
/*    */ import lu.uni.tsopen.symbolicExecution.symbolicValues.SymbolicValue;
/*    */ import soot.SootMethod;
/*    */ import soot.Type;
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
/*    */ public class GetLastLocationRecognition
/*    */   extends LocationMethodsRecognitionHandler
/*    */ {
/*    */   public GetLastLocationRecognition(LocationMethodsRecognitionHandler next, SymbolicExecution se) {
/* 42 */     super(next, se);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean processLocationMethod(SootMethod method, SymbolicValue sv) {
/* 47 */     String methodName = method.getName();
/* 48 */     Value base = sv.getBase();
/* 49 */     Type type = (base == null) ? (Type)method.getDeclaringClass().getType() : base.getType();
/* 50 */     if (base != null && type.toString().equals("com.google.android.gms.location.LocationResult") && methodName.equals("getLastLocation")) {
/* 51 */       sv.addTag(new StringConstantValueTag("#here"));
/* 52 */       return true;
/*    */     } 
/* 54 */     return false;
/*    */   }
/*    */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/lu/uni/tsopen/symbolicExecution/methodRecognizers/location/GetLastLocationRecognition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */