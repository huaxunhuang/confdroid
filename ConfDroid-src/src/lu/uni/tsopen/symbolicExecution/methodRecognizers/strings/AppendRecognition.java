/*     */ package lu.uni.tsopen.symbolicExecution.methodRecognizers.strings;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import lu.uni.tsopen.symbolicExecution.ContextualValues;
/*     */ import lu.uni.tsopen.symbolicExecution.SymbolicExecution;
/*     */ import lu.uni.tsopen.symbolicExecution.symbolicValues.ConstantValue;
/*     */ import lu.uni.tsopen.symbolicExecution.symbolicValues.MethodRepresentationValue;
/*     */ import lu.uni.tsopen.symbolicExecution.symbolicValues.SymbolicValue;
/*     */ import lu.uni.tsopen.symbolicExecution.symbolicValues.UnknownValue;
/*     */ import lu.uni.tsopen.utils.Utils;
/*     */ import soot.SootMethod;
/*     */ import soot.Value;
/*     */ import soot.jimple.Constant;
/*     */ import soot.jimple.StringConstant;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AppendRecognition
/*     */   extends StringMethodsRecognitionHandler
/*     */ {
/*     */   public AppendRecognition(StringMethodsRecognitionHandler next, SymbolicExecution se) {
/*  50 */     super(next, se);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<SymbolicValue> processStringMethod(SootMethod method, Value base, List<Value> args) {
/*  55 */     List<SymbolicValue> values = null;
/*  56 */     List<SymbolicValue> results = new ArrayList<>();
/*  57 */     ContextualValues contextualValuesOfBase = null;
/*  58 */     if (method.getName().equals("append")) {
/*  59 */       contextualValuesOfBase = (ContextualValues)this.se.getContext().get(base);
/*  60 */       if (contextualValuesOfBase == null) {
/*  61 */         results.addAll(computeValue((SymbolicValue)new UnknownValue(this.se, base + "<appendRecognition>"), args, base, method));
/*     */       } else {
/*  63 */         values = contextualValuesOfBase.getLastCoherentValues(null);
/*  64 */         if (values != null) {
/*  65 */           for (SymbolicValue sv : values) {
/*  66 */             results.addAll(computeValue(sv, args, base, method));
/*     */           }
/*     */         }
/*     */       } 
/*  70 */       for (SymbolicValue sv : results) {
/*  71 */         Utils.propagateTags(base, sv, this.se);
/*     */       }
/*  73 */       return results;
/*     */     } 
/*  75 */     return null;
/*     */   }
/*     */   
/*     */   private List<SymbolicValue> computeValue(SymbolicValue symVal, List<Value> args, Value base, SootMethod method) {
/*  79 */     List<SymbolicValue> results = new ArrayList<>();
/*  80 */     Value effectiveArg = args.get(0);
/*  81 */     ContextualValues contextualValuesOfBase = null;
/*  82 */     List<SymbolicValue> values = null;
/*  83 */     SymbolicValue object = null;
/*  84 */     if (effectiveArg instanceof Constant) {
/*  85 */       MethodRepresentationValue methodRepresentationValue = null; if (symVal.isConstant()) {
/*  86 */         ConstantValue constantValue = new ConstantValue((Constant)StringConstant.v(String.format("%s%s", new Object[] { symVal, effectiveArg })), this.se);
/*     */       } else {
/*  88 */         methodRepresentationValue = new MethodRepresentationValue(base, args, method, this.se);
/*     */       } 
/*  90 */       addResult(results, (SymbolicValue)methodRepresentationValue);
/*     */     } else {
/*  92 */       contextualValuesOfBase = (ContextualValues)this.se.getContext().get(effectiveArg);
/*  93 */       if (contextualValuesOfBase == null) {
/*  94 */         MethodRepresentationValue methodRepresentationValue = null; if (symVal.isConstant()) {
/*  95 */           ConstantValue constantValue = new ConstantValue((Constant)StringConstant.v(String.format("%s%s", new Object[] { symVal, "UNKNOWN_STRING" })), this.se);
/*     */         } else {
/*  97 */           methodRepresentationValue = new MethodRepresentationValue(base, args, method, this.se);
/*     */         } 
/*  99 */         addResult(results, (SymbolicValue)methodRepresentationValue);
/*     */       } else {
/* 101 */         values = contextualValuesOfBase.getLastCoherentValues(null);
/* 102 */         if (values != null) {
/* 103 */           for (SymbolicValue sv : values) {
/* 104 */             MethodRepresentationValue methodRepresentationValue = null; if (symVal.isConstant() && sv.isConstant()) {
/* 105 */               ConstantValue constantValue = new ConstantValue((Constant)StringConstant.v(String.format("%s%s", new Object[] { symVal, sv })), this.se);
/*     */             } else {
/* 107 */               methodRepresentationValue = new MethodRepresentationValue(base, args, method, this.se);
/*     */             } 
/* 109 */             addResult(results, (SymbolicValue)methodRepresentationValue);
/*     */           } 
/*     */         }
/*     */       } 
/*     */     } 
/* 114 */     for (SymbolicValue sv : results) {
/* 115 */       for (Value arg : args) {
/* 116 */         Utils.propagateTags(arg, sv, this.se);
/*     */       }
/*     */     } 
/* 119 */     return results;
/*     */   }
/*     */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/lu/uni/tsopen/symbolicExecution/methodRecognizers/strings/AppendRecognition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */