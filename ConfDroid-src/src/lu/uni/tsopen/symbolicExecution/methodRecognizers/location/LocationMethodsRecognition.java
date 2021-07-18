package lu.uni.tsopen.symbolicExecution.methodRecognizers.location;

import lu.uni.tsopen.symbolicExecution.symbolicValues.SymbolicValue;
import soot.SootMethod;

public interface LocationMethodsRecognition {
  boolean recognizeLocationMethod(SootMethod paramSootMethod, SymbolicValue paramSymbolicValue);
  
  boolean processLocationMethod(SootMethod paramSootMethod, SymbolicValue paramSymbolicValue);
}


/* Location:              /Users/name1/Documents/ConfDroid.jar!/lu/uni/tsopen/symbolicExecution/methodRecognizers/location/LocationMethodsRecognition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */