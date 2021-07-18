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
/*    */ 
/*    */ public class MatchesRecognition
/*    */   extends BooleanMethodsRecognitionHandler
/*    */ {
/*    */   public MatchesRecognition(BooleanMethodsRecognitionHandler next, SymbolicExecution se) {
/* 45 */     super(next, se);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean processBooleanMethod(SootMethod method, Value base, SymbolicValue sv, List<Value> args) {
/* 50 */     Value firstArg = null;
/* 51 */     String methodName = method.getName();
/* 52 */     if (methodName.equals("matches") && 
/* 53 */       args.size() > 0) {
/* 54 */       firstArg = args.get(0);
/* 55 */       if ((Utils.containsTag(base, "#sms/#body", this.se) || 
/* 56 */         Utils.containsTag(base, "#sms/#sender", this.se) || 
/* 57 */         Utils.containsTag(base, "#now", this.se)) && 
/* 58 */         firstArg instanceof soot.jimple.Constant) {
/* 59 */         sv.addTag(new StringConstantValueTag("#Suspicious"));
/* 60 */         return true;
/*    */       } 
/*    */     } 
/*    */ 
/*    */     
/* 65 */     return false;
/*    */   }
/*    */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/lu/uni/tsopen/symbolicExecution/methodRecognizers/bool/MatchesRecognition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */