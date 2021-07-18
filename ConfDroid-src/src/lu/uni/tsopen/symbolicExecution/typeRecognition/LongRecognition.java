/*    */ package lu.uni.tsopen.symbolicExecution.typeRecognition;
/*    */ 
/*    */ import lu.uni.tsopen.symbolicExecution.SymbolicExecution;
/*    */ import lu.uni.tsopen.symbolicExecution.methodRecognizers.numeric.CurrentTimeMillisRecognition;
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
/*    */ public class LongRecognition
/*    */   extends NumericRecognition
/*    */ {
/*    */   public LongRecognition(TypeRecognitionHandler next, SymbolicExecution se, InfoflowCFG icfg) {
/* 41 */     super(next, se, icfg);
/* 42 */     this.authorizedTypes.add("long");
/* 43 */     this.nmrh = (NumericMethodsRecognitionHandler)new GetLongitudeRecognition(null, se);
/* 44 */     this.nmrh = (NumericMethodsRecognitionHandler)new GetLatitudeRecognition(this.nmrh, se);
/* 45 */     this.nmrh = (NumericMethodsRecognitionHandler)new CurrentTimeMillisRecognition(this.nmrh, se);
/*    */   }
/*    */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/lu/uni/tsopen/symbolicExecution/typeRecognition/LongRecognition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */