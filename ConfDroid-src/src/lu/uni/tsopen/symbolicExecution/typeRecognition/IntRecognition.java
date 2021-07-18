/*    */ package lu.uni.tsopen.symbolicExecution.typeRecognition;
/*    */ 
/*    */ import lu.uni.tsopen.symbolicExecution.SymbolicExecution;
/*    */ import lu.uni.tsopen.symbolicExecution.methodRecognizers.numeric.GetHoursRecognition;
/*    */ import lu.uni.tsopen.symbolicExecution.methodRecognizers.numeric.GetMinutesRecognition;
/*    */ import lu.uni.tsopen.symbolicExecution.methodRecognizers.numeric.GetMonthRecognition;
/*    */ import lu.uni.tsopen.symbolicExecution.methodRecognizers.numeric.GetRecognition;
/*    */ import lu.uni.tsopen.symbolicExecution.methodRecognizers.numeric.GetSecondsRecognition;
/*    */ import lu.uni.tsopen.symbolicExecution.methodRecognizers.numeric.GetYearRecognition;
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
/*    */ public class IntRecognition
/*    */   extends NumericRecognition
/*    */ {
/*    */   public IntRecognition(TypeRecognitionHandler next, SymbolicExecution se, InfoflowCFG icfg) {
/* 44 */     super(next, se, icfg);
/* 45 */     this.authorizedTypes.add("int");
/* 46 */     this.nmrh = (NumericMethodsRecognitionHandler)new GetMonthRecognition(null, se);
/* 47 */     this.nmrh = (NumericMethodsRecognitionHandler)new GetMinutesRecognition(this.nmrh, se);
/* 48 */     this.nmrh = (NumericMethodsRecognitionHandler)new GetSecondsRecognition(this.nmrh, se);
/* 49 */     this.nmrh = (NumericMethodsRecognitionHandler)new GetHoursRecognition(this.nmrh, se);
/* 50 */     this.nmrh = (NumericMethodsRecognitionHandler)new GetYearRecognition(this.nmrh, se);
/* 51 */     this.nmrh = (NumericMethodsRecognitionHandler)new GetRecognition(this.nmrh, se);
/*    */   }
/*    */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/lu/uni/tsopen/symbolicExecution/typeRecognition/IntRecognition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */