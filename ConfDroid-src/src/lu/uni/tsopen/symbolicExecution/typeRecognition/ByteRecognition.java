/*    */ package lu.uni.tsopen.symbolicExecution.typeRecognition;
/*    */ 
/*    */ import java.util.LinkedList;
/*    */ import java.util.List;
/*    */ import lu.uni.tsopen.symbolicExecution.SymbolicExecution;
/*    */ import lu.uni.tsopen.symbolicExecution.symbolicValues.BinOpValue;
/*    */ import lu.uni.tsopen.symbolicExecution.symbolicValues.ObjectValue;
/*    */ import lu.uni.tsopen.symbolicExecution.symbolicValues.SymbolicValue;
/*    */ import lu.uni.tsopen.utils.Utils;
/*    */ import org.javatuples.Pair;
/*    */ import soot.SootMethod;
/*    */ import soot.Value;
/*    */ import soot.jimple.BinopExpr;
/*    */ import soot.jimple.DefinitionStmt;
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
/*    */ 
/*    */ 
/*    */ public class ByteRecognition
/*    */   extends TypeRecognitionHandler
/*    */ {
/*    */   public ByteRecognition(TypeRecognitionHandler next, SymbolicExecution se, InfoflowCFG icfg) {
/* 51 */     super(next, se, icfg);
/* 52 */     this.authorizedTypes.add("byte");
/*    */   }
/*    */ 
/*    */   
/*    */   public void handleConstructorTag(List<Value> args, ObjectValue object) {}
/*    */   
/*    */   public List<Pair<Value, SymbolicValue>> handleDefinitionStmt(DefinitionStmt defUnit) {
/*    */     BinOpValue binOpValue;
/* 60 */     Value leftOp = defUnit.getLeftOp();
/* 61 */     Value rightOp = defUnit.getRightOp();
/* 62 */     Value binOp1 = null;
/* 63 */     Value binOp2 = null;
/* 64 */     List<Pair<Value, SymbolicValue>> results = new LinkedList<>();
/* 65 */     SymbolicValue object = null;
/* 66 */     BinopExpr BinOpRightOp = null;
/* 67 */     if (rightOp instanceof BinopExpr) {
/* 68 */       BinOpRightOp = (BinopExpr)rightOp;
/* 69 */       binOp1 = BinOpRightOp.getOp1();
/* 70 */       binOp2 = BinOpRightOp.getOp2();
/* 71 */       binOpValue = new BinOpValue(this.se, binOp1, binOp2, BinOpRightOp.getSymbol());
/* 72 */       Utils.propagateTags(binOp1, (SymbolicValue)binOpValue, this.se);
/* 73 */       Utils.propagateTags(binOp2, (SymbolicValue)binOpValue, this.se);
/*    */     } else {
/* 75 */       return results;
/*    */     } 
/* 77 */     results.add(new Pair(leftOp, binOpValue));
/* 78 */     return results;
/*    */   }
/*    */   
/*    */   public void handleInvokeTag(List<Value> args, Value base, SymbolicValue object, SootMethod method) {}
/*    */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/lu/uni/tsopen/symbolicExecution/typeRecognition/ByteRecognition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */