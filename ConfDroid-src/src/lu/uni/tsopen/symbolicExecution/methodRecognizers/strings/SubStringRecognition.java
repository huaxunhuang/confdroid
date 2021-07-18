/*    */ package lu.uni.tsopen.symbolicExecution.methodRecognizers.strings;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import lu.uni.tsopen.symbolicExecution.SymbolicExecution;
/*    */ import lu.uni.tsopen.symbolicExecution.symbolicValues.ConstantValue;
/*    */ import lu.uni.tsopen.symbolicExecution.symbolicValues.MethodRepresentationValue;
/*    */ import lu.uni.tsopen.symbolicExecution.symbolicValues.SymbolicValue;
/*    */ import soot.SootMethod;
/*    */ import soot.Value;
/*    */ import soot.jimple.Constant;
/*    */ import soot.jimple.IntConstant;
/*    */ import soot.jimple.StringConstant;
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
/*    */ public class SubStringRecognition
/*    */   extends StringMethodsRecognitionHandler
/*    */ {
/*    */   public SubStringRecognition(StringMethodsRecognitionHandler next, SymbolicExecution se) {
/* 47 */     super(next, se);
/*    */   }
/*    */ 
/*    */   
/*    */   public List<SymbolicValue> processStringMethod(SootMethod method, Value base, List<Value> args) {
/* 52 */     List<SymbolicValue> results = new ArrayList<>();
/* 53 */     StringConstant baseStr = null;
/* 54 */     Value arg1 = null;
/* 55 */     Value arg2 = null;
/* 56 */     int v1 = 0;
/* 57 */     int v2 = 0;
/* 58 */     SymbolicValue object = null;
/* 59 */     if (method.getName().equals("substring")) {
/* 60 */       MethodRepresentationValue methodRepresentationValue = null; if (base instanceof StringConstant) {
/* 61 */         baseStr = (StringConstant)base;
/* 62 */         if (baseStr.value.contains("UNKNOWN_STRING")) {
/* 63 */           methodRepresentationValue = new MethodRepresentationValue(base, args, method, this.se);
/*    */         } else {
/* 65 */           arg1 = args.get(0);
/* 66 */           if (arg1 instanceof IntConstant) {
/* 67 */             v1 = ((IntConstant)arg1).value;
/* 68 */             if (args.size() == 1) {
/* 69 */               ConstantValue constantValue = new ConstantValue((Constant)StringConstant.v(baseStr.value.substring(v1)), this.se);
/*    */             } else {
/* 71 */               arg2 = args.get(1);
/* 72 */               if (arg2 instanceof IntConstant) {
/* 73 */                 v2 = ((IntConstant)arg2).value;
/* 74 */                 ConstantValue constantValue = new ConstantValue((Constant)StringConstant.v(baseStr.value.substring(v1, v2)), this.se);
/*    */               } 
/*    */             } 
/*    */           } 
/*    */         } 
/*    */       } else {
/* 80 */         methodRepresentationValue = new MethodRepresentationValue(base, args, method, this.se);
/*    */       } 
/* 82 */       if (methodRepresentationValue != null) {
/* 83 */         addResult(results, (SymbolicValue)methodRepresentationValue);
/*    */       }
/* 85 */       return results;
/*    */     } 
/* 87 */     return null;
/*    */   }
/*    */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/lu/uni/tsopen/symbolicExecution/methodRecognizers/strings/SubStringRecognition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */