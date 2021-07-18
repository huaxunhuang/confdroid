package spoon;

import androidsourcecode.branchedflow.MainSourceSinkScan;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.junit.Assert;
import org.junit.Test;
import soot.toolkits.scalar.Pair;
import spoon.processing.Processor;
import spoon.reflect.code.CtCase;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtStatement;
import spoon.reflect.code.CtSwitch;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtConstructor;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.factory.Factory;
import spoon.reflect.visitor.Filter;
import spoon.reflect.visitor.filter.TypeFilter;
import spoon.support.QueueProcessingManager;
import spoon.testing.utils.ModelUtils;

public class SillyScanMethod {
    Set<Pair<String, String>> pair1 = new HashSet<>();

    Set<Pair<String, String>> pair2 = new HashSet<>();

    @Test
    public void test() {
        for(int apiLevel = 25; apiLevel <= 30;apiLevel++) {
            mainProcess("/Users/name1/Documents/android-java-test/android-" + apiLevel + "/", this.pair1);
            mainProcess("/Users/name1/Documents/android-java-test/android-" + (apiLevel - 1) + "/", this.pair2);
            System.out.println("==== begin printing diffs ====");
            String xlsPath = "/Users/name1/IdeaProjects/First/sillya" + apiLevel + ".xls";
            HSSFWorkbook hSSFWorkbook = new HSSFWorkbook();
            Sheet sheet = hSSFWorkbook.createSheet("output");
            int ptr = 0;
            System.out.println("begin diff " + apiLevel);
            for (Pair<String, String> p2 : this.pair2) {
                boolean flag = false;
                for (Pair<String, String> p1 : this.pair1) {
                    if (p2.getO1().equals(p1.getO1()) && p2.getO2().equals(p1.getO2()))
                        flag = true;
                }
                if (!flag) {
                    Row row = sheet.createRow(ptr);
                    row.createCell(0).setCellValue("" + p2.getO1());
                    row.createCell(1).setCellValue("" + p2.getO2());
                    row.createCell(2).setCellValue("" + apiLevel);
                    ptr++;
                }
            }
            System.out.println("begin diff " + (apiLevel - 1));
            for (Pair<String, String> p2 : this.pair1) {
                boolean flag = false;
                for (Pair<String, String> p1 : this.pair2) {
                    if (p2.getO1().equals(p1.getO1()) && p2.getO2().equals(p1.getO2()))
                        flag = true;
                }
                if (!flag) {
                    Row row = sheet.createRow(ptr);
                    row.createCell(0).setCellValue("" + p2.getO1());
                    row.createCell(1).setCellValue("" + p2.getO2());
                    row.createCell(2).setCellValue("" + (apiLevel - 1));
                    ptr++;
                }
            }
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(xlsPath);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                hSSFWorkbook.write(fos);
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void mainProcess(String path, Set<Pair<String, String>> pair) {
        System.out.println("path = " + path);
        String[] args = { "-i", path };
        Launcher launcher = new Launcher();
        launcher.setArgs(args);
        launcher.run();
        System.out.println("SillyScanMethod.mainProcess");
        Factory factory = launcher.getFactory();
        QueueProcessingManager queueProcessingManager = new QueueProcessingManager(factory);
        DemoProcessor processor = new DemoProcessor();
        queueProcessingManager.addProcessor((Processor)processor);
        queueProcessingManager.process(factory.Class().getAll());
        for (CtClass cls : processor.ctClassSet) {
            Set<CtConstructor> constructorList = cls.getConstructors();
            for (CtConstructor c : constructorList)
                scanConstructorBody(c, pair);
            Set<CtMethod> methodList = cls.getMethods();
            for (CtMethod m : methodList)
                scanMethodBody(m, pair);
        }
    }

    private void scanConstructorBody(CtConstructor stmt, Set<Pair<String, String>> pair) {
        try {
            if (stmt.toString().contains("R.styleable")) {
                List<CtSwitch<?>> switchList = stmt.getElements((Filter)new TypeFilter(CtSwitch.class));
                if (switchList.size() > 0)
                    for (int i = 0; i < switchList.size(); i++) {
                        if (switchList.get(i) instanceof CtSwitch)
                            scanSwitchStmt(switchList.get(i), pair);
                    }
                List<CtInvocation> invocationList = stmt.getElements((Filter)new TypeFilter(CtInvocation.class));
                if (invocationList.size() > 0)
                    for (int i = 0; i < invocationList.size(); i++)
                        scanInvocationStmt((CtStatement)invocationList.get(i), pair);
            }
        } catch (Exception exception) {}
    }

    private void scanMethodBody(CtMethod stmt, Set<Pair<String, String>> pair) {
        try {
            if (stmt.toString().contains("R.styleable")) {
                List<CtSwitch<?>> switchList = stmt.getElements((Filter)new TypeFilter(CtSwitch.class));
                if (switchList.size() > 0)
                    for (int i = 0; i < switchList.size(); i++) {
                        if (switchList.get(i) instanceof CtSwitch)
                            scanSwitchStmt(switchList.get(i), pair);
                    }
                List<CtInvocation> invocationList = stmt.getElements((Filter)new TypeFilter(CtInvocation.class));
                if (invocationList.size() > 0)
                    for (int i = 0; i < invocationList.size(); i++)
                        scanInvocationStmt((CtStatement)invocationList.get(i), pair);
            }
        } catch (Exception exception) {}
    }

    private void scanSwitchStmt(CtSwitch stmt, Set<Pair<String, String>> pair) {
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
                            String type = MainSourceSinkScan.getType(caseStmt.toString());
                            if (!type.startsWith("has"))
                                pair.add(new Pair(attr, type));
                            break;
                        }
                    }
                }
            }
        }
    }

    private void scanInvocationStmt(CtStatement stmt, Set<Pair<String, String>> pair) {
        String attr = getAttribute(stmt.toString());
        String type = MainSourceSinkScan.getType(stmt.toString());
        if (attr.length() > 0 && type.length() > 0 &&
                !type.startsWith("has"))
            pair.add(new Pair(attr, type));
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

    @Test
    public void testIterationStatements() {
        Factory factory = ModelUtils.createFactory();
        CtClass<?> clazz = (CtClass)factory.Code().createCodeSnippetStatement("class X {public void foo() { int x=0;switch(x) {case 3: foo();break;case 1: x=0;default: x=-1;}}};").compile();
        CtMethod<?> foo = (CtMethod)clazz.getMethods().toArray()[0];
        CtSwitch<?> sw = (CtSwitch<?>) foo.getElements((Filter)new TypeFilter(CtSwitch.class)).get(0);
        Assert.assertEquals(3L, sw.getCases().size());
        CtCase<?> c = sw.getCases().get(0);
        List<CtStatement> l = new ArrayList<>();
        for (CtStatement s : c)
            l.add(s);
    }
}
