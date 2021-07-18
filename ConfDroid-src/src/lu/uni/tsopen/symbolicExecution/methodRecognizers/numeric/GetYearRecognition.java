/*    */ package lu.uni.tsopen.symbolicExecution.methodRecognizers.numeric;
/*    */ 
/*    */ import lu.uni.tsopen.symbolicExecution.SymbolicExecution;
/*    */ import lu.uni.tsopen.symbolicExecution.symbolicValues.SymbolicValue;
/*    */ import soot.SootMethod;
/*    */ import soot.Value;
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
/*    */ public class GetYearRecognition
/*    */   extends NumericMethodsRecognitionHandler
/*    */ {
/*    */   public GetYearRecognition(NumericMethodsRecognitionHandler next, SymbolicExecution se) {
/* 40 */     super(next, se);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean processNumericMethod(SootMethod method, Value base, SymbolicValue sv) {
/* 45 */     return genericProcessNumericMethod(method, base, sv, "java.util.Date", "getYear", "#now", "#now/#year");
/*    */   }
/*    */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/lu/uni/tsopen/symbolicExecution/methodRecognizers/numeric/GetYearRecognition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */