/*   */ package androidsourcecode.branchedflow;
/*   */ 
/*   */ public class StringPredicate
/*   */   extends Predicate<String>
/*   */ {
/*   */   public Predicate put(String expr, Boolean bool) {
/* 7 */     return super.put("(" + expr + ")", bool);
/*   */   }
/*   */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/androidsourcecode/branchedflow/StringPredicate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */