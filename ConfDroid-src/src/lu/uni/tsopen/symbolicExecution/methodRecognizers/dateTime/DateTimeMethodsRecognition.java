package lu.uni.tsopen.symbolicExecution.methodRecognizers.dateTime;

import lu.uni.tsopen.symbolicExecution.symbolicValues.SymbolicValue;
import soot.SootMethod;

public interface DateTimeMethodsRecognition {
  boolean recognizeDateTimeMethod(SootMethod paramSootMethod, SymbolicValue paramSymbolicValue);
  
  boolean processDateTimeMethod(SootMethod paramSootMethod, SymbolicValue paramSymbolicValue);
}


/* Location:              /Users/name1/Documents/ConfDroid.jar!/lu/uni/tsopen/symbolicExecution/methodRecognizers/dateTime/DateTimeMethodsRecognition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */