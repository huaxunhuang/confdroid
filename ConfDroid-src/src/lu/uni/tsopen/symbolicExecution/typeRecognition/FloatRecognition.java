/*    */ package lu.uni.tsopen.symbolicExecution.typeRecognition;
/*    */ 
/*    */ import lu.uni.tsopen.symbolicExecution.SymbolicExecution;
/*    */ import soot.jimple.infoflow.solver.cfg.InfoflowCFG;
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
/*    */ public class FloatRecognition
/*    */   extends NumericRecognition
/*    */ {
/*    */   public FloatRecognition(TypeRecognitionHandler next, SymbolicExecution se, InfoflowCFG icfg) {
/* 38 */     super(next, se, icfg);
/* 39 */     this.authorizedTypes.add("float");
/*    */   }
/*    */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/lu/uni/tsopen/symbolicExecution/typeRecognition/FloatRecognition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */