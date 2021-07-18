/*    */ package lu.uni.tsopen.symbolicExecution.methodRecognizers.strings;
/*    */ 
/*    */ import java.util.List;
/*    */ import com.sun.org.slf4j.internal.*;
import lu.uni.tsopen.symbolicExecution.ContextualValues;
/*    */ import lu.uni.tsopen.symbolicExecution.SymbolicExecution;
/*    */ import lu.uni.tsopen.symbolicExecution.symbolicValues.SymbolicValue;
/*    */ import lu.uni.tsopen.symbolicExecution.symbolicValues.UnknownValue;
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
/*    */ public abstract class StringMethodsRecognitionHandler
/*    */   implements StringMethodsRecognition
/*    */ {
/* 45 */   protected Logger logger = LoggerFactory.getLogger(getClass());
/*    */   
/*    */   private StringMethodsRecognitionHandler next;
/*    */   protected SymbolicExecution se;
/*    */   
/*    */   public StringMethodsRecognitionHandler(StringMethodsRecognitionHandler next, SymbolicExecution se) {
/* 51 */     this.next = next;
/* 52 */     this.se = se;
/*    */   }
/*    */ 
/*    */   
/*    */   public List<SymbolicValue> recognizeStringMethod(SootMethod method, Value base, List<Value> args) {
/* 57 */     List<SymbolicValue> result = processStringMethod(method, base, args);
/*    */     
/* 59 */     if (result != null && !result.isEmpty()) {
/* 60 */       return result;
/*    */     }
/* 62 */     if (this.next != null) {
/* 63 */       return this.next.recognizeStringMethod(method, base, args);
/*    */     }
/*    */     
/* 66 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void addSimpleResult(Value v, List<SymbolicValue> results) {
/* 71 */     ContextualValues contextualValues = (ContextualValues)this.se.getContext().get(v);
/* 72 */     List<SymbolicValue> values = null;
/* 73 */     if (contextualValues == null) {
/* 74 */       results.add(new UnknownValue(this.se, v + "<stringMethods>"));
/*    */     } else {
/* 76 */       values = contextualValues.getLastCoherentValues(null);
/* 77 */       if (values != null) {
/* 78 */         for (SymbolicValue sv : values) {
/* 79 */           addResult(results, sv);
/*    */         }
/*    */       }
/*    */     } 
/*    */   }
/*    */   
/*    */   protected void addResult(List<SymbolicValue> results, SymbolicValue object) {
/* 86 */     for (SymbolicValue sv : results) {
/* 87 */       if (sv.toString().equals(object.toString())) {
/*    */         return;
/*    */       }
/*    */     } 
/* 91 */     results.add(object);
/*    */   }
/*    */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/lu/uni/tsopen/symbolicExecution/methodRecognizers/strings/StringMethodsRecognitionHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */