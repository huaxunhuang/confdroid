/*     */ package lu.uni.tsopen.symbolicExecution;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import lu.uni.tsopen.symbolicExecution.symbolicValues.SymbolicValue;
/*     */ import soot.Unit;
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
/*     */ 
/*     */ public class ContextualValues
/*     */ {
/*     */   private Value receiver;
/*     */   private LinkedHashMap<Unit, LinkedList<SymbolicValue>> nodesToSymbolicValues;
/*     */   private SymbolicExecution se;
/*     */   
/*     */   public ContextualValues(SymbolicExecution se, Value receiver) {
/*  50 */     this.nodesToSymbolicValues = new LinkedHashMap<>();
/*  51 */     this.se = se;
/*  52 */     this.receiver = receiver;
/*     */   }
/*     */   
/*     */   public Value getReceiver() {
/*  56 */     return this.receiver;
/*     */   }
/*     */   
/*     */   public void addValue(Unit node, SymbolicValue sv) {
/*  60 */     LinkedList<SymbolicValue> valuesOfNode = this.nodesToSymbolicValues.get(node);
/*  61 */     if (valuesOfNode == null) {
/*  62 */       valuesOfNode = new LinkedList<>();
/*  63 */       this.nodesToSymbolicValues.put(node, valuesOfNode);
/*     */     } 
/*  65 */     valuesOfNode.add(sv);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<SymbolicValue> getLastCoherentValues(Unit node) {
/*  74 */     Iterator<Unit> it = this.se.getCurrentPath().descendingIterator();
/*     */ 
/*     */ 
/*     */     
/*  78 */     if (node == null) {
/*  79 */       while (it.hasNext()) {
/*  80 */         Unit n = it.next();
/*  81 */         if (n != this.se.getCurrentPath().getLast()) {
/*  82 */           LinkedList<SymbolicValue> values = this.nodesToSymbolicValues.get(n);
/*  83 */           if (values != null) {
/*  84 */             return values;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } else {
/*  89 */       Map<Value, List<SymbolicValue>> valuesAtNode = this.se.getValuesAtNode(node);
/*  90 */       if (valuesAtNode != null) {
/*  91 */         return valuesAtNode.get(this.receiver);
/*     */       }
/*     */     } 
/*  94 */     return getAllValues();
/*     */   }
/*     */   
/*     */   public List<SymbolicValue> getAllValues() {
/*  98 */     List<SymbolicValue> values = new ArrayList<>();
/*  99 */     for (Map.Entry<Unit, LinkedList<SymbolicValue>> e : this.nodesToSymbolicValues.entrySet()) {
/* 100 */       values.addAll(e.getValue());
/*     */     }
/* 102 */     return values;
/*     */   }
/*     */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/lu/uni/tsopen/symbolicExecution/ContextualValues.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */