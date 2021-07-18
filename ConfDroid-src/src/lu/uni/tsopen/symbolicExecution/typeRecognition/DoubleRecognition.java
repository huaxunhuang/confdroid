/*    */ package lu.uni.tsopen.symbolicExecution.typeRecognition;
/*    */ 
/*    */ import lu.uni.tsopen.symbolicExecution.SymbolicExecution;
/*    */ import lu.uni.tsopen.symbolicExecution.methodRecognizers.numeric.GetLatitudeRecognition;
/*    */ import lu.uni.tsopen.symbolicExecution.methodRecognizers.numeric.GetLongitudeRecognition;
/*    */ import lu.uni.tsopen.symbolicExecution.methodRecognizers.numeric.NumericMethodsRecognitionHandler;
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
/*    */ public class DoubleRecognition
/*    */   extends NumericRecognition
/*    */ {
/*    */   public DoubleRecognition(TypeRecognitionHandler next, SymbolicExecution se, InfoflowCFG icfg) {
/* 40 */     super(next, se, icfg);
/* 41 */     this.authorizedTypes.add("double");
/* 42 */     this.nmrh = (NumericMethodsRecognitionHandler)new GetLongitudeRecognition(null, se);
/* 43 */     this.nmrh = (NumericMethodsRecognitionHandler)new GetLatitudeRecognition(this.nmrh, se);
/*    */   }
/*    */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/lu/uni/tsopen/symbolicExecution/typeRecognition/DoubleRecognition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */