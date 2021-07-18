package lu.uni.tsopen.symbolicExecution.methodRecognizers.bool;

import java.util.List;
import lu.uni.tsopen.symbolicExecution.symbolicValues.SymbolicValue;
import soot.SootMethod;
import soot.Value;

public interface BooleanMethodsRecognition {
  boolean recognizeBooleanMethod(SootMethod paramSootMethod, Value paramValue, SymbolicValue paramSymbolicValue, List<Value> paramList);
  
  boolean processBooleanMethod(SootMethod paramSootMethod, Value paramValue, SymbolicValue paramSymbolicValue, List<Value> paramList);
}


/* Location:              /Users/name1/Documents/ConfDroid.jar!/lu/uni/tsopen/symbolicExecution/methodRecognizers/bool/BooleanMethodsRecognition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */