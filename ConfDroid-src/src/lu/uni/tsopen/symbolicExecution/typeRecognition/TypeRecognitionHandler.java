/*     */ package lu.uni.tsopen.symbolicExecution.typeRecognition;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import lu.uni.tsopen.symbolicExecution.ContextualValues;
/*     */ import lu.uni.tsopen.symbolicExecution.SymbolicExecution;
/*     */ import lu.uni.tsopen.symbolicExecution.methodRecognizers.location.DistanceBetweenRecognition;
/*     */ import lu.uni.tsopen.symbolicExecution.methodRecognizers.location.LocationMethodsRecognitionHandler;
/*     */ import lu.uni.tsopen.symbolicExecution.symbolicValues.ConstantValue;
/*     */ import lu.uni.tsopen.symbolicExecution.symbolicValues.MethodRepresentationValue;
/*     */ import lu.uni.tsopen.symbolicExecution.symbolicValues.ObjectValue;
/*     */ import lu.uni.tsopen.symbolicExecution.symbolicValues.SingleVariableValue;
/*     */ import lu.uni.tsopen.symbolicExecution.symbolicValues.SymbolicValue;
/*     */ import lu.uni.tsopen.symbolicExecution.symbolicValues.UnknownValue;
/*     */ import lu.uni.tsopen.utils.Utils;
/*     */ import org.javatuples.Pair;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import soot.SootMethod;
/*     */ import soot.Unit;
/*     */ import soot.Value;
/*     */ import soot.jimple.AssignStmt;
/*     */ import soot.jimple.Constant;
/*     */ import soot.jimple.DefinitionStmt;
/*     */ import soot.jimple.InstanceInvokeExpr;
/*     */ import soot.jimple.InvokeExpr;
/*     */ import soot.jimple.InvokeStmt;
/*     */ import soot.jimple.ReturnStmt;
/*     */ import soot.jimple.infoflow.solver.cfg.InfoflowCFG;
/*     */ import soot.jimple.internal.JInstanceFieldRef;
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
/*     */ public abstract class TypeRecognitionHandler
/*     */   implements TypeRecognition
/*     */ {
/*     */   private TypeRecognitionHandler next;
/*     */   protected SymbolicExecution se;
/*     */   protected InfoflowCFG icfg;
/*     */   protected List<String> authorizedTypes;
/*     */   private LocationMethodsRecognitionHandler lmrh;
/*  62 */   protected Logger logger = LoggerFactory.getLogger(getClass());
/*     */   
/*     */   public TypeRecognitionHandler(TypeRecognitionHandler next, SymbolicExecution se, InfoflowCFG icfg) {
/*  65 */     this.next = next;
/*  66 */     this.se = se;
/*  67 */     this.icfg = icfg;
/*  68 */     this.authorizedTypes = new LinkedList<>();
/*  69 */     this.lmrh = (LocationMethodsRecognitionHandler)new DistanceBetweenRecognition(null, se);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<Pair<Value, SymbolicValue>> recognizeType(Unit node) {
/*  74 */     List<Pair<Value, SymbolicValue>> result = null;
/*  75 */     if (node instanceof DefinitionStmt) {
/*  76 */       result = processDefinitionStmt((DefinitionStmt)node);
/*  77 */     } else if (node instanceof InvokeStmt) {
/*  78 */       result = processInvokeStmt((InvokeStmt)node);
/*  79 */     } else if (node instanceof ReturnStmt) {
/*  80 */       result = processReturnStmt((ReturnStmt)node);
/*     */     } 
/*     */     
/*  83 */     if (result != null && !result.isEmpty()) {
/*  84 */       return result;
/*     */     }
/*  86 */     if (this.next != null) {
/*  87 */       return this.next.recognizeType(node);
/*     */     }
/*     */     
/*  90 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Pair<Value, SymbolicValue>> processReturnStmt(ReturnStmt returnStmt) {
/*  96 */     Collection<Unit> callers = this.icfg.getCallersOf(this.icfg.getMethodOf((Unit)returnStmt));
/*  97 */     AssignStmt callerAssign = null;
/*  98 */     Value leftOp = null;
/*  99 */     Value returnOp = returnStmt.getOp();
/* 100 */     List<SymbolicValue> values = null;
/* 101 */     ContextualValues contextualValues = (ContextualValues)this.se.getContext().get(returnOp);
/* 102 */     List<Pair<Value, SymbolicValue>> results = new LinkedList<>();
/* 103 */     SymbolicValue object = null;
/* 104 */     SootMethod callerMethod = null;
/*     */     
/* 106 */     for (Unit caller : callers) {
/* 107 */       callerMethod = this.icfg.getMethodOf(caller);
/* 108 */       if (caller instanceof AssignStmt) {
/* 109 */         callerAssign = (AssignStmt)caller;
/* 110 */         leftOp = callerAssign.getLeftOp();
/* 111 */         if (contextualValues == null) {
/* 112 */           UnknownValue unknownValue = null; if (returnOp instanceof Constant) {
/* 113 */             ConstantValue constantValue = new ConstantValue((Constant)returnOp, this.se);
/*     */           }
/*     */           else {
/*     */             
/* 117 */             unknownValue = new UnknownValue(this.se, returnOp.toString() + "<return>");
/*     */           } 
/*     */           
/* 120 */           Utils.propagateTags(returnOp, (SymbolicValue)unknownValue, this.se);
/* 121 */           results.add(new Pair(leftOp, unknownValue));
/*     */         } else {
/* 123 */           values = contextualValues.getLastCoherentValues(null);
/* 124 */           if (values != null) {
/* 125 */             for (SymbolicValue sv : values) {
/* 126 */               results.add(new Pair(leftOp, sv));
/*     */             }
/*     */           }
/*     */         } 
/*     */       } 
/* 131 */       if (this.se.isMethodVisited(callerMethod)) {
/* 132 */         this.se.addMethodToWorkList(callerMethod);
/*     */       }
/*     */     } 
/* 135 */     return results;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<Pair<Value, SymbolicValue>> processDefinitionStmt(DefinitionStmt defUnit) {
/* 140 */     if (isAuthorizedType(defUnit.getLeftOp().getType().toString())) {
/* 141 */       return handleDefinitionStmt(defUnit);
/*     */     }
/* 143 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<Pair<Value, SymbolicValue>> processInvokeStmt(InvokeStmt invUnit) {
/* 148 */     Value base = null;
/* 149 */     InvokeExpr invExprUnit = invUnit.getInvokeExpr();
/* 150 */     SootMethod m = invExprUnit.getMethod();
/* 151 */     List<Pair<Value, SymbolicValue>> results = new LinkedList<>();
/*     */     
/* 153 */     if (invExprUnit instanceof InstanceInvokeExpr) {
/* 154 */       base = ((InstanceInvokeExpr)invExprUnit).getBase();
/* 155 */       if (base != null && 
/* 156 */         isAuthorizedType(base.getType().toString())) {
/* 157 */         if (m.isConstructor()) {
/* 158 */           handleConstructor(invExprUnit, base, results);
/* 159 */         } else if (invExprUnit instanceof InstanceInvokeExpr) {
/* 160 */           handleInvokeStmt(invExprUnit, base, results);
/*     */         }
/*     */       
/*     */       }
/* 164 */     } else if (invExprUnit instanceof soot.jimple.StaticInvokeExpr) {
/* 165 */       handleInvokeStmt(invExprUnit, base, results);
/*     */     } 
/* 167 */     return results;
/*     */   }
/*     */   
/*     */   protected void handleInvokeStmt(InvokeExpr invExprUnit, Value base, List<Pair<Value, SymbolicValue>> results) {
/* 171 */     SootMethod method = invExprUnit.getMethod();
/* 172 */     List<Value> args = invExprUnit.getArgs();
/* 173 */     MethodRepresentationValue methodRepresentationValue = new MethodRepresentationValue(base, args, method, this.se);
/* 174 */     this.lmrh.recognizeLocationMethod(method, (SymbolicValue)methodRepresentationValue);
/* 175 */     handleInvokeTag(args, base, (SymbolicValue)methodRepresentationValue, method);
/* 176 */     results.add(new Pair(base, methodRepresentationValue));
/*     */   }
/*     */ 
/*     */   
/*     */   public abstract void handleInvokeTag(List<Value> paramList, Value paramValue, SymbolicValue paramSymbolicValue, SootMethod paramSootMethod);
/*     */ 
/*     */   
/*     */   public void handleConstructor(InvokeExpr invExprUnit, Value base, List<Pair<Value, SymbolicValue>> results) {
/* 184 */     List<Value> args = invExprUnit.getArgs();
/* 185 */     ObjectValue object = new ObjectValue(base.getType(), args, this.se);
/* 186 */     handleConstructorTag(args, object);
/* 187 */     results.add(new Pair(base, object));
/*     */   }
/*     */   
/*     */   protected boolean isAuthorizedType(String type) {
/* 191 */     return this.authorizedTypes.contains(type);
/*     */   }
/*     */   
/*     */   protected void checkAndProcessContextValues(Value v, List<Pair<Value, SymbolicValue>> results, Value leftOp, Unit node) {
/* 195 */     ContextualValues contextualValues = (ContextualValues)this.se.getContext().get(v);
/* 196 */     if (contextualValues == null && v instanceof Constant) {
/* 197 */       contextualValues = new ContextualValues(this.se, v);
/*     */     }
/* 199 */     List<SymbolicValue> values = null;
/* 200 */     if (contextualValues == null) {
/* 201 */       if (v instanceof JInstanceFieldRef) {
/*     */         
/* 203 */         Value v1 = this.se.getSbpe().getFieldValue(((JInstanceFieldRef)v).getFieldRef().toString());
/* 204 */         if (v1 instanceof Constant) {
/* 205 */           results.add(new Pair(leftOp, new ConstantValue((Constant)v1, this.se)));
/*     */         
/*     */         }
/*     */         else {
/*     */ 
/*     */           
/* 211 */           results.add(new Pair(leftOp, new SingleVariableValue(v, this.se)));
/*     */         } 
/*     */       } 
/*     */     } else {
/* 215 */       if (v instanceof Constant) {
/* 216 */         values = new ArrayList<>();
/* 217 */         values.add(new ConstantValue((Constant)v, this.se));
/*     */       } else {
/* 219 */         values = contextualValues.getLastCoherentValues(node);
/*     */       } 
/* 221 */       if (values != null)
/* 222 */         for (SymbolicValue sv : values)
/* 223 */           results.add(new Pair(leftOp, sv));  
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/lu/uni/tsopen/symbolicExecution/typeRecognition/TypeRecognitionHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */