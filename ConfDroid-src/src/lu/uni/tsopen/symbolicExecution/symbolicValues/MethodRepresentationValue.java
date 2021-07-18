/*     */ package lu.uni.tsopen.symbolicExecution.symbolicValues;
/*     */ 
/*     */ import java.util.List;
/*     */ import lu.uni.tsopen.symbolicExecution.SymbolicExecution;
/*     */ import soot.SootMethod;
/*     */ import soot.Value;
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
/*     */ public class MethodRepresentationValue
/*     */   extends AbstractSymbolicValue
/*     */ {
/*     */   private Value base;
/*     */   private List<Value> args;
/*     */   private SootMethod method;
/*     */   
/*     */   public MethodRepresentationValue(Value b, List<Value> a, SootMethod m, SymbolicExecution se) {
/*  45 */     super(se);
/*  46 */     this.method = m;
/*  47 */     this.base = b;
/*  48 */     this.args = a;
/*     */     
/*  50 */     if (this.base != null) {
/*  51 */       this.values.put(this.base, getSymbolicValues(this.base));
/*     */     }
/*  53 */     for (Value arg : this.args) {
/*  54 */       if (!(arg instanceof soot.jimple.Constant)) {
/*  55 */         this.values.put(arg, getSymbolicValues(arg));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String getValue() {
/*  62 */     String value = "(";
/*  63 */     if (this.base != null) {
/*  64 */       value = value + computeValue(this.base);
/*  65 */       value = value + ".";
/*     */     } 
/*  67 */     value = value + this.method.getName();
/*  68 */     value = value + "(";
/*  69 */     for (Value arg : this.args) {
/*  70 */       value = value + computeValue(arg);
/*  71 */       if (arg != this.args.get(this.args.size() - 1)) {
/*  72 */         value = value + ", ";
/*     */       }
/*     */     } 
/*  75 */     value = value + "))";
/*  76 */     return value;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSymbolic() {
/*  81 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isConstant() {
/*  86 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isMethodRepresentation() {
/*  91 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isObject() {
/*  96 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public Value getBase() {
/* 101 */     return this.base;
/*     */   }
/*     */   
/*     */   public List<Value> getArgs() {
/* 105 */     return this.args;
/*     */   }
/*     */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/lu/uni/tsopen/symbolicExecution/symbolicValues/MethodRepresentationValue.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */