/*    */ package lu.uni.tsopen.symbolicExecution.methodRecognizers.location;
/*    */ 
/*    */ import java.util.List;
/*    */ import lu.uni.tsopen.symbolicExecution.ContextualValues;
/*    */ import lu.uni.tsopen.symbolicExecution.SymbolicExecution;
/*    */ import lu.uni.tsopen.symbolicExecution.symbolicValues.MethodRepresentationValue;
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
/*    */ public class DistanceBetweenRecognition
/*    */   extends LocationMethodsRecognitionHandler
/*    */ {
/*    */   public DistanceBetweenRecognition(LocationMethodsRecognitionHandler next, SymbolicExecution se) {
/* 46 */     super(next, se);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean processLocationMethod(SootMethod method, SymbolicValue sv) {
/* 51 */     MethodRepresentationValue mrv = null;
/* 52 */     Value lastArg = null;
/* 53 */     List<Value> args = null;
/* 54 */     ContextualValues contextualValues = null;
/* 55 */     List<SymbolicValue> values = null;
/*    */     
/* 57 */     if (method.getName().equals("distanceBetween")) {
/* 58 */       if (sv instanceof MethodRepresentationValue) {
/* 59 */         mrv = (MethodRepresentationValue)sv;
/* 60 */         args = mrv.getArgs();
/* 61 */         for (Value arg : args) {
/* 62 */           if (Utils.containsTag(arg, "#here/#latitude", this.se) || 
/* 63 */             Utils.containsTag(arg, "#here/#longitude", this.se)) {
/* 64 */             lastArg = args.get(args.size() - 1);
/* 65 */             contextualValues = this.se.getContextualValues(lastArg);
/* 66 */             if (contextualValues != null) {
/* 67 */               values = contextualValues.getLastCoherentValues(null);
/* 68 */               if (values != null) {
/* 69 */                 for (SymbolicValue symval : values) {
/* 70 */                   symval.addTag(new StringConstantValueTag("#Suspicious"));
/*    */                 }
/*    */               }
/*    */             } 
/*    */           } 
/*    */         } 
/*    */       } 
/* 77 */       return true;
/*    */     } 
/* 79 */     return false;
/*    */   }
/*    */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/lu/uni/tsopen/symbolicExecution/methodRecognizers/location/DistanceBetweenRecognition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */