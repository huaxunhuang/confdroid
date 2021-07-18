/*    */ package lu.uni.tsopen.symbolicExecution.methodRecognizers.strings;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import lu.uni.tsopen.symbolicExecution.SymbolicExecution;
/*    */ import lu.uni.tsopen.symbolicExecution.symbolicValues.ConstantValue;
/*    */ import lu.uni.tsopen.symbolicExecution.symbolicValues.SymbolicValue;
/*    */ import lu.uni.tsopen.utils.Utils;
/*    */ import soot.SootMethod;
/*    */ import soot.Value;
/*    */ import soot.jimple.Constant;
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
/*    */ public class ValueOfRecognition
/*    */   extends StringMethodsRecognitionHandler
/*    */ {
/*    */   public ValueOfRecognition(StringMethodsRecognitionHandler next, SymbolicExecution se) {
/* 46 */     super(next, se);
/*    */   }
/*    */ 
/*    */   
/*    */   public List<SymbolicValue> processStringMethod(SootMethod method, Value base, List<Value> args) {
/* 51 */     List<SymbolicValue> results = new ArrayList<>();
/* 52 */     Value effectiveArg = null;
/* 53 */     if (method.getName().equals("valueOf")) {
/* 54 */       effectiveArg = args.get(0);
/* 55 */       if (effectiveArg instanceof Constant) {
/* 56 */         results.add(new ConstantValue((Constant)effectiveArg, this.se));
/*    */       } else {
/* 58 */         addSimpleResult(effectiveArg, results);
/* 59 */         for (SymbolicValue sv : results) {
/* 60 */           Utils.propagateTags(effectiveArg, sv, this.se);
/*    */         }
/*    */       } 
/* 63 */       return results;
/*    */     } 
/* 65 */     return null;
/*    */   }
/*    */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/lu/uni/tsopen/symbolicExecution/methodRecognizers/strings/ValueOfRecognition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */