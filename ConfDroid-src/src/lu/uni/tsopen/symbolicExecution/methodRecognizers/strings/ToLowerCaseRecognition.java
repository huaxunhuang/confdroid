/*    */ package lu.uni.tsopen.symbolicExecution.methodRecognizers.strings;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import lu.uni.tsopen.symbolicExecution.SymbolicExecution;
/*    */ import lu.uni.tsopen.symbolicExecution.symbolicValues.SymbolicValue;
/*    */ import lu.uni.tsopen.utils.Utils;
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
/*    */ 
/*    */ public class ToLowerCaseRecognition
/*    */   extends StringMethodsRecognitionHandler
/*    */ {
/*    */   public ToLowerCaseRecognition(StringMethodsRecognitionHandler next, SymbolicExecution se) {
/* 44 */     super(next, se);
/*    */   }
/*    */ 
/*    */   
/*    */   public List<SymbolicValue> processStringMethod(SootMethod method, Value base, List<Value> args) {
/* 49 */     List<SymbolicValue> results = new ArrayList<>();
/* 50 */     if (method.getName().equals("toLowerCase")) {
/* 51 */       addSimpleResult(base, results);
/* 52 */       for (SymbolicValue sv : results) {
/* 53 */         Utils.propagateTags(base, sv, this.se);
/*    */       }
/* 55 */       return results;
/*    */     } 
/* 57 */     return null;
/*    */   }
/*    */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/lu/uni/tsopen/symbolicExecution/methodRecognizers/strings/ToLowerCaseRecognition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */