/*    */ package lu.uni.tsopen.symbolicExecution.methodRecognizers.strings;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import lu.uni.tsopen.symbolicExecution.SymbolicExecution;
/*    */ import lu.uni.tsopen.symbolicExecution.symbolicValues.MethodRepresentationValue;
/*    */ import lu.uni.tsopen.symbolicExecution.symbolicValues.SymbolicValue;
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
/*    */ public class GetOriginatingAddressRecognition
/*    */   extends StringMethodsRecognitionHandler
/*    */ {
/*    */   public GetOriginatingAddressRecognition(StringMethodsRecognitionHandler next, SymbolicExecution se) {
/* 45 */     super(next, se);
/*    */   }
/*    */ 
/*    */   
/*    */   public List<SymbolicValue> processStringMethod(SootMethod method, Value base, List<Value> args) {
/* 50 */     List<SymbolicValue> results = new ArrayList<>();
/* 51 */     MethodRepresentationValue mrv = new MethodRepresentationValue(base, args, method, this.se);
/* 52 */     if (method.getName().equals("getOriginatingAddress") || method.getName().equals("getDisplayOriginatingAddress")) {
/* 53 */       mrv.addTag(new StringConstantValueTag("#sms/#sender"));
/* 54 */       addResult(results, (SymbolicValue)mrv);
/*    */     } 
/* 56 */     return results;
/*    */   }
/*    */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/lu/uni/tsopen/symbolicExecution/methodRecognizers/strings/GetOriginatingAddressRecognition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */