package lu.uni.tsopen.symbolicExecution.typeRecognition;

import java.util.List;
import lu.uni.tsopen.symbolicExecution.symbolicValues.ObjectValue;
import lu.uni.tsopen.symbolicExecution.symbolicValues.SymbolicValue;
import org.javatuples.Pair;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.jimple.DefinitionStmt;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.ReturnStmt;

public interface TypeRecognition {
  List<Pair<Value, SymbolicValue>> recognizeType(Unit paramUnit);
  
  List<Pair<Value, SymbolicValue>> processDefinitionStmt(DefinitionStmt paramDefinitionStmt);
  
  List<Pair<Value, SymbolicValue>> processInvokeStmt(InvokeStmt paramInvokeStmt);
  
  void handleConstructor(InvokeExpr paramInvokeExpr, Value paramValue, List<Pair<Value, SymbolicValue>> paramList);
  
  void handleConstructorTag(List<Value> paramList, ObjectValue paramObjectValue);
  
  List<Pair<Value, SymbolicValue>> handleDefinitionStmt(DefinitionStmt paramDefinitionStmt);
  
  List<Pair<Value, SymbolicValue>> processReturnStmt(ReturnStmt paramReturnStmt);
  
  void handleInvokeTag(List<Value> paramList, Value paramValue, SymbolicValue paramSymbolicValue, SootMethod paramSootMethod);
}


/* Location:              /Users/name1/Documents/ConfDroid.jar!/lu/uni/tsopen/symbolicExecution/typeRecognition/TypeRecognition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */