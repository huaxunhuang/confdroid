package lu.uni.tsopen.symbolicExecution.methodRecognizers.numeric;

import lu.uni.tsopen.symbolicExecution.symbolicValues.SymbolicValue;
import soot.SootMethod;
import soot.Value;

public interface NumericMethodsRecognition {
  boolean recognizeNumericMethod(SootMethod paramSootMethod, Value paramValue, SymbolicValue paramSymbolicValue);
  
  boolean processNumericMethod(SootMethod paramSootMethod, Value paramValue, SymbolicValue paramSymbolicValue);
  
  boolean genericProcessNumericMethod(SootMethod paramSootMethod, Value paramValue, SymbolicValue paramSymbolicValue, String paramString1, String paramString2, String paramString3, String paramString4);
  
  boolean isTagHandled(String paramString1, String paramString2, Value paramValue, SymbolicValue paramSymbolicValue);
}


/* Location:              /Users/name1/Documents/ConfDroid.jar!/lu/uni/tsopen/symbolicExecution/methodRecognizers/numeric/NumericMethodsRecognition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */