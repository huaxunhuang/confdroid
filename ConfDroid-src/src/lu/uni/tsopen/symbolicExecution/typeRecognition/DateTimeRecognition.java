/*    */ package lu.uni.tsopen.symbolicExecution.typeRecognition;
/*    */ 
/*    */ import java.util.LinkedList;
/*    */ import java.util.List;
/*    */ import lu.uni.tsopen.symbolicExecution.SymbolicExecution;
/*    */ import lu.uni.tsopen.symbolicExecution.methodRecognizers.dateTime.DateTimeMethodsRecognitionHandler;
/*    */ import lu.uni.tsopen.symbolicExecution.methodRecognizers.dateTime.GetInstanceRecognition;
/*    */ import lu.uni.tsopen.symbolicExecution.methodRecognizers.dateTime.NowRecognition;
/*    */ import lu.uni.tsopen.symbolicExecution.methodRecognizers.dateTime.SetToNowRecognition;
/*    */ import lu.uni.tsopen.symbolicExecution.symbolicValues.ObjectValue;
/*    */ import lu.uni.tsopen.symbolicExecution.symbolicValues.SymbolicValue;
/*    */ import org.javatuples.Pair;
/*    */ import soot.SootMethod;
/*    */ import soot.Type;
/*    */ import soot.Value;
/*    */ import soot.jimple.DefinitionStmt;
/*    */ import soot.jimple.StaticInvokeExpr;
/*    */ import soot.jimple.infoflow.solver.cfg.InfoflowCFG;
/*    */ import soot.tagkit.StringConstantValueTag;
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
/*    */ public class DateTimeRecognition
/*    */   extends TypeRecognitionHandler
/*    */ {
/*    */   private DateTimeMethodsRecognitionHandler dtmrh;
/*    */   
/*    */   public DateTimeRecognition(TypeRecognitionHandler next, SymbolicExecution se, InfoflowCFG icfg) {
/* 56 */     super(next, se, icfg);
/* 57 */     this.authorizedTypes.add("java.util.Date");
/* 58 */     this.authorizedTypes.add("java.util.Calendar");
/* 59 */     this.authorizedTypes.add("java.util.GregorianCalendar");
/* 60 */     this.authorizedTypes.add("java.time.LocalDateTime");
/* 61 */     this.authorizedTypes.add("java.time.LocalDate");
/* 62 */     this.authorizedTypes.add("java.text.SimpleDateFormat");
/* 63 */     this.authorizedTypes.add("android.text.format.Time");
/* 64 */     this.dtmrh = (DateTimeMethodsRecognitionHandler)new GetInstanceRecognition(null, se);
/* 65 */     this.dtmrh = (DateTimeMethodsRecognitionHandler)new NowRecognition(this.dtmrh, se);
/* 66 */     this.dtmrh = (DateTimeMethodsRecognitionHandler)new SetToNowRecognition(this.dtmrh, se);
/*    */   }
/*    */ 
/*    */   
/*    */   public List<Pair<Value, SymbolicValue>> handleDefinitionStmt(DefinitionStmt defUnit) {
/* 71 */     Value leftOp = defUnit.getLeftOp();
/* 72 */     Value rightOp = defUnit.getRightOp();
/* 73 */     List<Pair<Value, SymbolicValue>> results = new LinkedList<>();
/* 74 */     StaticInvokeExpr rightOpStaticInvokeExpr = null;
/* 75 */     SootMethod method = null;
/* 76 */     List<Value> args = null;
/* 77 */     ObjectValue object = null;
/*    */     
/* 79 */     if (rightOp instanceof StaticInvokeExpr) {
/* 80 */       rightOpStaticInvokeExpr = (StaticInvokeExpr)rightOp;
/* 81 */       method = rightOpStaticInvokeExpr.getMethod();
/* 82 */       args = rightOpStaticInvokeExpr.getArgs();
/* 83 */       object = new ObjectValue((Type)method.getDeclaringClass().getType(), args, this.se);
/* 84 */       this.dtmrh.recognizeDateTimeMethod(method, (SymbolicValue)object);
/* 85 */       results.add(new Pair(leftOp, object));
/*    */     } 
/* 87 */     return results;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handleConstructorTag(List<Value> args, ObjectValue object) {
/* 92 */     if (args.size() == 0) {
/* 93 */       object.addTag(new StringConstantValueTag("#now"));
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void handleInvokeTag(List<Value> args, Value base, SymbolicValue object, SootMethod method) {
/* 99 */     this.dtmrh.recognizeDateTimeMethod(method, object);
/*    */   }
/*    */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/lu/uni/tsopen/symbolicExecution/typeRecognition/DateTimeRecognition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */