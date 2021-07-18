package lu.uni.tsopen.symbolicExecution.methodRecognizers.strings;

import java.util.List;
import lu.uni.tsopen.symbolicExecution.symbolicValues.SymbolicValue;
import soot.SootMethod;
import soot.Value;

public interface StringMethodsRecognition {
  List<SymbolicValue> recognizeStringMethod(SootMethod paramSootMethod, Value paramValue, List<Value> paramList);
  
  List<SymbolicValue> processStringMethod(SootMethod paramSootMethod, Value paramValue, List<Value> paramList);
}


/* Location:              /Users/name1/Documents/ConfDroid.jar!/lu/uni/tsopen/symbolicExecution/methodRecognizers/strings/StringMethodsRecognition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */