/*    */ package lu.uni.tsopen.symbolicExecution.symbolicValues;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import lu.uni.tsopen.symbolicExecution.SymbolicExecution;
/*    */ import soot.Type;
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
/*    */ public class ObjectValue
/*    */   extends ConcreteValue
/*    */ {
/*    */   private Type type;
/*    */   private List<Value> args;
/*    */   
/*    */   public ObjectValue(Type t, Value arg, SymbolicExecution se) {
/* 44 */     super(se);
/* 45 */     this.type = t;
/* 46 */     this.args = new ArrayList<>();
/* 47 */     this.args.add(arg);
/*    */   }
/*    */   
/*    */   public ObjectValue(Type t, List<Value> args, SymbolicExecution se) {
/* 51 */     super(se);
/* 52 */     this.type = t;
/* 53 */     this.args = (args == null) ? new ArrayList<>() : args;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getValue() {
/* 58 */     return String.format("%s(%s)", new Object[] { this.type, computeArgs() });
/*    */   }
/*    */   
/*    */   private String computeArgs() {
/* 62 */     String args = "";
/* 63 */     List<SymbolicValue> values = null;
/* 64 */     for (Value arg : this.args) {
/* 65 */       values = getSymbolicValues(arg);
/* 66 */       if (values != null) {
/* 67 */         for (SymbolicValue sv : values) {
/* 68 */           args = args + sv;
/* 69 */           if (sv != values.get(values.size() - 1)) {
/* 70 */             args = args + " | ";
/*    */           }
/*    */         }
/*    */       
/* 74 */       } else if (arg != null) {
/* 75 */         args = args + arg.getType();
/*    */       } 
/*    */       
/* 78 */       if (arg != this.args.get(this.args.size() - 1)) {
/* 79 */         args = args + ", ";
/*    */       }
/*    */     } 
/* 82 */     return args;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isConstant() {
/* 87 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isObject() {
/* 92 */     return true;
/*    */   }
/*    */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/lu/uni/tsopen/symbolicExecution/symbolicValues/ObjectValue.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */