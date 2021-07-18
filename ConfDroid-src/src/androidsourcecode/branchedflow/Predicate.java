/*    */ package androidsourcecode.branchedflow;
/*    */ 
/*    */ import java.util.LinkedHashMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Predicate<T>
/*    */ {
/* 12 */   private final Map<T, Boolean> exprsMap = new LinkedHashMap<>();
/* 13 */   private int nullCount = -1;
/*    */   
/*    */   public Predicate copy(Predicate<T> p) {
/* 16 */     this.exprsMap.clear();
/* 17 */     this.exprsMap.putAll(p.exprsMap);
/* 18 */     this.nullCount = p.nullCount;
/* 19 */     return this;
/*    */   }
/*    */   
/*    */   public Predicate merge(Predicate<T> p) {
/* 23 */     for (Map.Entry<T, Boolean> e : p.exprsMap.entrySet()) {
/* 24 */       if (!this.exprsMap.containsKey(e.getKey())) {
/* 25 */         this.exprsMap.put(e.getKey(), e.getValue()); continue;
/*    */       } 
/* 27 */       Boolean bool = this.exprsMap.get(e.getKey());
/* 28 */       if (e.getValue() != bool && 
/* 29 */         null != bool) {
/* 30 */         this.exprsMap.put(e.getKey(), null);
/*    */       }
/*    */     } 
/* 33 */     this.nullCount = -1;
/* 34 */     return this;
/*    */   }
/*    */   
/*    */   public Predicate put(T expr, Boolean bool) {
/* 38 */     this.exprsMap.put(expr, bool);
/* 39 */     this.nullCount = -1;
/* 40 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 45 */     if (!(o instanceof Predicate))
/* 46 */       return false; 
/* 47 */     Predicate p = (Predicate)o;
/* 48 */     return this.exprsMap.keySet().equals(p.exprsMap.keySet());
/*    */   }
/*    */   
/*    */   public int size() {
/* 52 */     return this.exprsMap.size();
/*    */   }
/*    */   
/*    */   public int getNullCount() {
/* 56 */     if (-1 == this.nullCount) {
/* 57 */       this.nullCount = 0;
/* 58 */       for (Boolean b : this.exprsMap.values()) {
/* 59 */         if (null == b)
/* 60 */           this.nullCount++; 
/*    */       } 
/* 62 */     }  return this.nullCount;
/*    */   }
/*    */   
/*    */   public Predicate update() {
/* 66 */     getNullCount();
/* 67 */     return this;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 71 */     return this.exprsMap.toString();
/*    */   }
/*    */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/androidsourcecode/branchedflow/Predicate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */