package lu.uni.tsopen.symbolicExecution.symbolicValues;

import java.util.List;
import soot.Value;
import soot.tagkit.StringConstantValueTag;

public interface SymbolicValue {
  String getValue();
  
  boolean isSymbolic();
  
  boolean isConstant();
  
  boolean isMethodRepresentation();
  
  boolean isObject();
  
  boolean hasTag();
  
  void addTag(StringConstantValueTag paramStringConstantValueTag);
  
  List<StringConstantValueTag> getTags();
  
  String getStringTags();
  
  boolean containsTag(String paramString);
  
  Value getBase();
}


/* Location:              /Users/name1/Documents/ConfDroid.jar!/lu/uni/tsopen/symbolicExecution/symbolicValues/SymbolicValue.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */