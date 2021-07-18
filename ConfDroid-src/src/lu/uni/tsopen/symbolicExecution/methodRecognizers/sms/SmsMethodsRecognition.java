package lu.uni.tsopen.symbolicExecution.methodRecognizers.sms;

import lu.uni.tsopen.symbolicExecution.symbolicValues.SymbolicValue;
import soot.SootMethod;

public interface SmsMethodsRecognition {
  boolean recognizeSmsMethod(SootMethod paramSootMethod, SymbolicValue paramSymbolicValue);
  
  boolean processSmsMethod(SootMethod paramSootMethod, SymbolicValue paramSymbolicValue);
}


/* Location:              /Users/name1/Documents/ConfDroid.jar!/lu/uni/tsopen/symbolicExecution/methodRecognizers/sms/SmsMethodsRecognition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */