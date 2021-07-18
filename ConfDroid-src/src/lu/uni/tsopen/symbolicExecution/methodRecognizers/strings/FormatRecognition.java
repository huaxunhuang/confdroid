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
/*    */ public class FormatRecognition
/*    */   extends StringMethodsRecognitionHandler
/*    */ {
/*    */   public FormatRecognition(StringMethodsRecognitionHandler next, SymbolicExecution se) {
/* 44 */     super(next, se);
/*    */   }
/*    */ 
/*    */   
/*    */   public List<SymbolicValue> processStringMethod(SootMethod method, Value base, List<Value> args) {
/* 49 */     List<SymbolicValue> results = new ArrayList<>();
/* 50 */     if (method.getName().equals("format") && base != null && base.getType().toString().equals("java.text.SimpleDateFormat")) {
/* 51 */       addSimpleResult(base, results);
/* 52 */       for (Value arg : args) {
/* 53 */         for (SymbolicValue sv : results) {
/* 54 */           Utils.propagateTags(arg, sv, this.se);
/*    */         }
/*    */       } 
/* 57 */       return results;
/*    */     } 
/* 59 */     return null;
/*    */   }
/*    */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/lu/uni/tsopen/symbolicExecution/methodRecognizers/strings/FormatRecognition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */