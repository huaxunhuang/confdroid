package spoon;

import androidsourcecode.branchedflow.MainSourceSinkScan;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.tuple.MutableTriple;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.junit.Test;
import spoon.processing.Processor;
import spoon.reflect.code.*;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtConstructor;
import spoon.reflect.declaration.CtField;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.factory.Factory;
import spoon.reflect.visitor.Filter;
import spoon.reflect.visitor.filter.TypeFilter;
import spoon.support.QueueProcessingManager;


public class SillyScanDefaultMethod {
    Set<CtField> field1 = new HashSet<>();

    Set<Triple<String, CtField, CtStatement>> fieldSet1 = new HashSet<>();

    Set<CtField> field2 = new HashSet<>();

    Set<Triple<String, CtField, CtStatement>> fieldSet2 = new HashSet<>();

    @Test
    public void test() {
        int apiLevel = 29;
        System.out.println("=== mainProcess " + apiLevel + " ===");
        mainProcess("/Users/name1/Documents/android-java-test/" + apiLevel + "/", this.fieldSet1);
        System.out.println("=== mainProcess " + (apiLevel - 1) + " ===");
        mainProcess("/Users/name1/Documents/android-java-test/" + (apiLevel - 1) + "/", this.fieldSet2);
        System.out.println("==== begin printing diffs ====");
        String xlsPath = "/Users/name1/Documents/GitHub/mine-config/ConfDroid/default" + apiLevel + ".xls";
        HSSFWorkbook hSSFWorkbook = new HSSFWorkbook();
        Sheet sheet = hSSFWorkbook.createSheet("output");
        int ptr = 0;
        System.out.println("begin diff " + apiLevel);
    }

    public void mainProcess(String path, Set<Triple<String, CtField, CtStatement>> fieldSets) {
        String[] args = { "-i", path };
        Launcher launcher = new Launcher();
        launcher.setArgs(args);
        launcher.run();
        Factory factory = launcher.getFactory();
        QueueProcessingManager queueProcessingManager = new QueueProcessingManager(factory);
        DemoProcessor processor = new DemoProcessor();
        queueProcessingManager.addProcessor((Processor)processor);
        queueProcessingManager.process(factory.Class().getAll());
        for (CtClass cls : processor.ctClassSet)
            scanClass(cls, fieldSets);
    }

    private void scanClass(CtClass cls, Set<Triple<String, CtField, CtStatement>> fieldSet) {
        Set<CtConstructor> constructorList = cls.getConstructors();
        for (CtConstructor constructor : constructorList)
            scanConstructorBody(cls, constructor, fieldSet);
        Set<CtMethod> methodList = cls.getMethods();
        for (CtMethod method : methodList)
            scanMethodBody(cls, method, fieldSet);
    }

    private void scanConstructorBody(CtClass cls, CtConstructor constructor, Set<Triple<String, CtField, CtStatement>> fieldSet) {
        if (constructor.toString().contains("R.styleable")) {
            List<CtSwitch<?>> switchList = constructor.getElements((Filter)new TypeFilter(CtSwitch.class));
            if (switchList.size() > 0)
                for (int i = 0; i < switchList.size(); i++) {
                    if (switchList.get(i) instanceof CtSwitch)
                        scanSwitchStmt(cls, switchList.get(i), fieldSet);
                }
            List<CtInvocation> invocationList = constructor.getElements((Filter)new TypeFilter(CtInvocation.class));
            if (invocationList.size() > 0)
                for (int i = 0; i < invocationList.size(); i++)
                    scanInvocationStmt(cls, (CtStatement)invocationList.get(i), fieldSet);
        }
    }

    private void scanMethodBody(CtClass cls, CtMethod stmt, Set<Triple<String, CtField, CtStatement>> fieldSet) {
        try {
            if (stmt.toString().contains("R.styleable")) {
                List<CtSwitch<?>> switchList = stmt.getElements((Filter)new TypeFilter(CtSwitch.class));
                if (switchList.size() > 0)
                    for (int i = 0; i < switchList.size(); i++) {
                        if (switchList.get(i) instanceof CtSwitch)
                            scanSwitchStmt(cls, switchList.get(i), fieldSet);
                    }
                List<CtInvocation> invocationList = stmt.getElements((Filter)new TypeFilter(CtInvocation.class));
                if (invocationList.size() > 0)
                    for (int i = 0; i < invocationList.size(); i++)
                        scanInvocationStmt(cls, (CtStatement)invocationList.get(i), fieldSet);
            }
        } catch (Exception exception) {}
    }

    private void scanSwitchStmt(CtClass cls, CtSwitch stmt, Set<Triple<String, CtField, CtStatement>> fieldSet) {
        if (stmt == null)
            return;
        for (int i = 0; i < stmt.getCases().size(); i++) {
            CtCase ctCase = (CtCase) stmt.getCases().get(i);
            if (ctCase != null) {
                CtExpression expression = ctCase.getCaseExpression();
                if (expression != null) {
                    String caseExpressionStr = expression.toString();
                    for (CtStatement caseStmt : ctCase.getStatements()) {
                        if (MainSourceSinkScan.getType(caseStmt.toString()).length() > 0) {
                            String attr = getAttribute(caseExpressionStr);
                            CtField field = getField(cls, caseStmt);
                            if (field != null)
                                fieldSet.add(new MutableTriple(attr, field, caseStmt));
                            break;
                        }
                    }
                }
            }
        }
    }

    private CtField getField(CtClass cls, CtStatement caseStmt) {
        if (caseStmt instanceof CtAssignment) {
            String fieldStr = ((CtAssignment)caseStmt).getAssigned().toString();
            for (int i = 0; i < cls.getFields().size(); i++) {
                CtField field = (CtField) cls.getFields().get(i);
                if (fieldStr.contains(field.getSimpleName()))
                    return field;
            }
        }
        return null;
    }

    private void scanInvocationStmt(CtClass cls, CtStatement stmt, Set<Triple<String, CtField, CtStatement>> fieldSet) {
        String attr = getAttribute(stmt.toString());
        String type = MainSourceSinkScan.getType(stmt.toString());
        if (attr.length() > 0 && type.length() > 0) {
            CtField field = getField(cls, stmt);
            if (field != null)
                fieldSet.add(new MutableTriple(attr, field, stmt));
        }
    }

    public static String getAttribute(String str) {
        int index = -1;
        if (str == null)
            str = "";
        if (str.contains("R.style")) {
            index = str.indexOf("R.style");
        } else if (str.contains("R.attr")) {
            index = str.indexOf("R.attr");
        }
        if (index != -1) {
            for (int i = index; i < str.length(); ) {
                char chr = str.charAt(i);
                if ((chr >= 'A' && chr <= 'Z') || (chr >= 'a' && chr <= 'z') || (chr >= '1' && chr <= '9') || chr == '.' || chr == '_') {
                    i++;
                    continue;
                }
                return str.substring(index, i);
            }
            return str.substring(index);
        }
        return "";
    }
}
