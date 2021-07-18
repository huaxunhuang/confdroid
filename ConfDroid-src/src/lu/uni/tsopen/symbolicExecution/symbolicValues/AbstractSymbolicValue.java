/*     */ package lu.uni.tsopen.symbolicExecution.symbolicValues;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import lu.uni.tsopen.symbolicExecution.ContextualValues;
/*     */ import lu.uni.tsopen.symbolicExecution.SymbolicExecution;
/*     */ import soot.Value;
/*     */ import soot.jimple.Constant;
/*     */ import soot.tagkit.StringConstantValueTag;
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
/*     */ public abstract class AbstractSymbolicValue
/*     */   implements SymbolicValue
/*     */ {
/*     */   protected List<StringConstantValueTag> tags;
/*     */   protected SymbolicExecution se;
/*     */   protected Map<Value, List<SymbolicValue>> values;
/*     */   
/*     */   public AbstractSymbolicValue(SymbolicExecution se) {
/*  46 */     this.tags = new ArrayList<>();
/*  47 */     this.se = se;
/*  48 */     this.values = new HashMap<>();
/*     */   }
/*     */   
/*     */   protected String computeValue(Value v) {
/*  52 */     List<SymbolicValue> values = null;
/*  53 */     String s = "";
/*  54 */     values = this.values.get(v);
/*  55 */     if (values != null) {
/*  56 */       s = s + values.get(0);
/*  57 */       return s;
/*  58 */     }  if (v instanceof Constant) {
/*  59 */       return ((Constant)v).toString();
/*     */     }
/*  61 */     return v.getType().toString();
/*     */   }
/*     */   
/*     */   protected List<SymbolicValue> getSymbolicValues(Value v) {
/*  65 */     Map<Value, ContextualValues> context = this.se.getContext();
/*  66 */     if (context.containsKey(v)) {
/*  67 */       return ((ContextualValues)context.get(v)).getLastCoherentValues(null);
/*     */     }
/*  69 */     Set<String> contextKeySet = new HashSet<>();
/*     */     
/*  71 */     for (Value contextKey : context.keySet())
/*     */     {
/*     */ 
/*     */       
/*  75 */       contextKeySet.add(contextKey.toString());
/*     */     }
/*     */     
/*  78 */     for (Value contextKey : context.keySet()) {
/*  79 */       if (contextKey != null && v != null && contextKey.toString().equals(v.toString()) && 
/*  80 */         context.get(contextKey) != null) {
/*  81 */         return ((ContextualValues)context.get(contextKey)).getLastCoherentValues(null);
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/*  86 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<StringConstantValueTag> getTags() {
/*  91 */     return this.tags;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getStringTags() {
/*  96 */     String tags = "";
/*  97 */     for (StringConstantValueTag scvt : this.tags) {
/*  98 */       tags = tags + String.format("%s ", new Object[] { scvt.getStringValue() });
/*     */     } 
/* 100 */     return tags;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasTag() {
/* 105 */     return !this.tags.isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 110 */     String value = "";
/* 111 */     List<String> usedStrings = new ArrayList<>();
/* 112 */     String tagValue = null;
/* 113 */     if (!this.tags.isEmpty()) {
/* 114 */       for (StringConstantValueTag tag : this.tags) {
/* 115 */         tagValue = tag.getStringValue();
/* 116 */         if (!usedStrings.contains(tagValue)) {
/* 117 */           usedStrings.add(tagValue);
/* 118 */           value = value + tag.getStringValue();
/* 119 */           if (tag != this.tags.get(this.tags.size() - 1)) {
/* 120 */             value = value + " | ";
/*     */           }
/*     */         } 
/*     */       } 
/* 124 */       return value;
/*     */     } 
/* 126 */     return getValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public void addTag(StringConstantValueTag tag) {
/* 131 */     this.tags.add(tag);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsTag(String t) {
/* 136 */     for (StringConstantValueTag tag : this.tags) {
/* 137 */       if (tag.getStringValue().equals(t)) {
/* 138 */         return true;
/*     */       }
/*     */     } 
/* 141 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public Value getBase() {
/* 146 */     return null;
/*     */   }
/*     */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/lu/uni/tsopen/symbolicExecution/symbolicValues/AbstractSymbolicValue.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */