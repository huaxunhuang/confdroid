package lu.uni.tsopen.symbolicExecution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lu.uni.tsopen.graphTraversal.ICFGForwardTraversal;
import lu.uni.tsopen.pathPredicateRecovery.PathPredicateRecovery;
import lu.uni.tsopen.pathPredicateRecovery.SimpleBlockPredicateExtraction;
import lu.uni.tsopen.symbolicExecution.symbolicValues.BinOpValue;
import lu.uni.tsopen.symbolicExecution.symbolicValues.ConstantValue;
import lu.uni.tsopen.symbolicExecution.symbolicValues.SingleVariableValue;
import lu.uni.tsopen.symbolicExecution.symbolicValues.SymbolicValue;
import lu.uni.tsopen.symbolicExecution.typeRecognition.BooleanRecognition;
import lu.uni.tsopen.symbolicExecution.typeRecognition.ByteRecognition;
import lu.uni.tsopen.symbolicExecution.typeRecognition.DateTimeRecognition;
import lu.uni.tsopen.symbolicExecution.typeRecognition.DoubleRecognition;
import lu.uni.tsopen.symbolicExecution.typeRecognition.FloatArrayRecognition;
import lu.uni.tsopen.symbolicExecution.typeRecognition.FloatRecognition;
import lu.uni.tsopen.symbolicExecution.typeRecognition.IntRecognition;
import lu.uni.tsopen.symbolicExecution.typeRecognition.LocationRecognition;
import lu.uni.tsopen.symbolicExecution.typeRecognition.LongRecognition;
import lu.uni.tsopen.symbolicExecution.typeRecognition.SmsRecognition;
import lu.uni.tsopen.symbolicExecution.typeRecognition.StringRecognition;
import lu.uni.tsopen.symbolicExecution.typeRecognition.TypeRecognition;
import lu.uni.tsopen.symbolicExecution.typeRecognition.TypeRecognitionHandler;
import org.javatuples.Pair;
import org.javatuples.Quartet;
import org.javatuples.Septet;
import org.javatuples.Triplet;
import org.logicng.formulas.CTrue;
import org.logicng.formulas.Formula;
import org.logicng.formulas.FormulaFactory;
import org.logicng.formulas.Literal;
import org.logicng.formulas.Variable;
import soot.Local;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.ConditionExpr;
import soot.jimple.Constant;
import soot.jimple.DefinitionStmt;
import soot.jimple.DoubleConstant;
import soot.jimple.FloatConstant;
import soot.jimple.IdentityStmt;
import soot.jimple.IfStmt;
import soot.jimple.IntConstant;
import soot.jimple.InvokeStmt;
import soot.jimple.LongConstant;
import soot.jimple.ReturnStmt;
import soot.jimple.Stmt;
import soot.jimple.SwitchStmt;
import soot.jimple.infoflow.solver.cfg.InfoflowCFG;
import soot.toolkits.graph.CompleteUnitGraph;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.LocalDefs;
import soot.toolkits.scalar.SimpleLocalDefs;
import soot.toolkits.scalar.SimpleLocalUses;

public class SymbolicExecution extends ICFGForwardTraversal {
    private SimpleBlockPredicateExtraction sbpe;

    private PathPredicateRecovery ppr;

    private Map<Unit, Map<Value, ContextualValues>> pathSensitiveSymbolicExecutionResults;

    private Map<Value, ContextualValues> symbolicExecutionResults;

    private Map<Unit, Map<Value, List<SymbolicValue>>> valuesAtNode;

    private Map<Value, List<SymbolicValue>> currentValues;

    private TypeRecognitionHandler trh;

    private IntRecognition intRecognition;

    private BooleanRecognition booleanRecognition;

    private StringRecognition stringRecognition;

    private FloatRecognition floatRecognition;

    private DoubleRecognition doubleRecognition;

    private Map<Unit, Boolean> containSystemSpecificPreconditions;

    private SootMethod mainMethod;

    private List<Triplet<Unit, Formula, List<Septet<List<SymbolicValue>, List<SymbolicValue>, List<SymbolicValue>, Value, Value, Constant, Literal>>>> interestingFormulae;

    private String className;

    public List<Quartet> outputXls = new ArrayList<>();

    public void setClassName(String className) {
        this.className = className;
    }

    public SimpleBlockPredicateExtraction getSbpe() {
        return this.sbpe;
    }

    public SymbolicExecution(InfoflowCFG icfg, SootMethod mainMethod) {
        super(icfg, "Symbolic Execution", mainMethod);
        this.interestingFormulae = new ArrayList<>();
        this.symbolicExecutionResults = new HashMap<>();
        this.pathSensitiveSymbolicExecutionResults = new HashMap<>();
        this.valuesAtNode = new HashMap<>();
        this.currentValues = new HashMap<>();
        this.stringRecognition = new StringRecognition(null, this, this.icfg);
        this.trh = (TypeRecognitionHandler)new DateTimeRecognition(this.trh, this, this.icfg);
        this.trh = (TypeRecognitionHandler)new LocationRecognition(this.trh, this, this.icfg);
        this.trh = (TypeRecognitionHandler)new SmsRecognition(this.trh, this, this.icfg);
        this.trh = (TypeRecognitionHandler)new LongRecognition(this.trh, this, this.icfg);
        this.intRecognition = new IntRecognition(this.trh, this, this.icfg);
        this.booleanRecognition = new BooleanRecognition(this.trh, this, icfg);
        this.trh = (TypeRecognitionHandler)new ByteRecognition(this.trh, this, icfg);
        this.doubleRecognition = new DoubleRecognition(this.trh, this, icfg);
        this.trh = (TypeRecognitionHandler)new FloatArrayRecognition(this.trh, this, icfg);
        this.floatRecognition = new FloatRecognition(this.trh, this, icfg);
        this.containSystemSpecificPreconditions = new HashMap<>();
        this.mainMethod = mainMethod;
    }

    public Map<Unit, Boolean> getContainSystemSpecificPreconditions() {
        return this.containSystemSpecificPreconditions;
    }

    public SymbolicExecution(InfoflowCFG icfg, SootMethod mainMethod, SimpleBlockPredicateExtraction sbpe, PathPredicateRecovery ppr) {
        this(icfg, mainMethod);
        this.sbpe = sbpe;
        this.ppr = ppr;
    }

    public List<Pair<Value, SymbolicValue>> recognizeType(Unit node) {
        TypeRecognition recognition = null;
        List<Pair<Value, SymbolicValue>> result = null;
        if (node instanceof DefinitionStmt) {
            if (((DefinitionStmt)node).getLeftOp().getType() instanceof soot.IntType) {
                result = this.intRecognition.processDefinitionStmt((DefinitionStmt)node);
            } else if (((DefinitionStmt)node).getLeftOp().getType() instanceof soot.FloatType) {
                result = this.floatRecognition.processDefinitionStmt((DefinitionStmt)node);
            } else if (((DefinitionStmt)node).getLeftOp().getType() instanceof soot.DoubleType) {
                result = this.doubleRecognition.processDefinitionStmt((DefinitionStmt)node);
            } else {
                result = this.trh.processDefinitionStmt((DefinitionStmt)node);
            }
        } else if (node instanceof InvokeStmt) {
            result = this.trh.processInvokeStmt((InvokeStmt)node);
        } else if (node instanceof ReturnStmt) {
            result = this.trh.processReturnStmt((ReturnStmt)node);
        }
        if (result != null && !result.isEmpty())
            return result;
        return null;
    }

    public List<Quartet> getOutputXls() {
        return this.outputXls;
    }

    protected void processNodeBeforeNeighbors(Unit node) {
        ContextualValues contextualValues = null;
        List<Pair<Value, SymbolicValue>> results = recognizeType(node);
        Value value = null;
        SymbolicValue symbolicValue = null;
        updateValuesAtNode(results, node);
        if (results != null)
            for (Pair<Value, SymbolicValue> p : results) {
                value = (Value)p.getValue0();
                symbolicValue = (SymbolicValue)p.getValue1();
                contextualValues = this.symbolicExecutionResults.get(value);
                if (contextualValues == null) {
                    contextualValues = new ContextualValues(this, value);
                    if (value != null)
                        this.symbolicExecutionResults.put(value, contextualValues);
                }
                contextualValues.addValue(node, symbolicValue);
                if (this.pathSensitiveSymbolicExecutionResults.get(node) == null)
                    this.pathSensitiveSymbolicExecutionResults.put(node, new HashMap<>());
                contextualValues = (ContextualValues)((Map)this.pathSensitiveSymbolicExecutionResults.get(node)).get(value);
                if (contextualValues == null) {
                    contextualValues = new ContextualValues(this, value);
                    if (value != null)
                        ((Map<Value, ContextualValues>)this.pathSensitiveSymbolicExecutionResults.get(node)).put(value, contextualValues);
                }
                contextualValues.addValue(node, symbolicValue);
            }
        LinkedList<Unit> currentPath = getCurrentPath();
        if ((node

                .toString().contains("getInt(") || node
                .toString().contains("getInteger(") || node
                .toString().contains("getNonConfigurationString(") || node
                .toString().contains("getColorStateList(") || node
                .toString().contains("getColor(") || node
                .toString().contains("getFont(") || node
                .toString().contains("getFloat(") || node
                .toString().contains("getString(") || node
                .toString().contains("getDimension(") || node
                .toString().contains("getDimensionPixelOffset(") || node
                .toString().contains("getDimensionPixelSize(") || node
                .toString().contains("getResourceId(") || node
                .toString().contains("getText(") || node
                .toString().contains("getTextArray(") || node
                .toString().contains("getNonResourceString(") || node
                .toString().contains("getFraction(") || node
                .toString().contains("getDrawable(") || node
                .toString().contains("getBoolean(") || node
                .toString().contains("getLayoutDimension(")) && node
                .toString().contains("TypedArray") && !node.toString().contains("goto ")) {
            CTrue cTrue = null;
            boolean systemApiFlag = false;
            System.out.println("begin interesting():::===");
            System.out.println("jiajia: " + node
                    .toString() + " " + this.icfg.getMethodOf(node) + " " + node.getJavaSourceStartLineNumber());
            boolean flag = false;
            System.out.println("currentNode: " + currentPath.getLast() + " " + this.icfg.getMethodOf(node));
            Formula formula = (Formula)this.ppr.getNodeToFullPathPredicate().get(node);
            if (formula == null)
                cTrue = (new FormulaFactory()).verum();
            Formula formula1 = null;
            if (((Stmt)node).containsInvokeExpr()) {
                List<Value> args = ((Stmt)node).getInvokeExpr().getArgs();
                for (Value arg : args) {
                    if (arg.toString().startsWith("$")) {
                        String literalStr = String.format("([] => %s)", new Object[] { arg.toString() });
                        Literal argLiteral = (new FormulaFactory()).literal(literalStr, true);
                        formula1 = (new FormulaFactory()).and(new Formula[] { (Formula)cTrue, (Formula)argLiteral });
                    }
                }
            }
            formula1 = formula1.cnf();
            this.outputXls.add(new Quartet(node.toString(), this.icfg.getMethodOf(node), Integer.valueOf(node.getJavaSourceStartLineNumber()), formula1.toString()));
            Set<Stmt> literalStmts = new HashSet<>();
            if (formula1 != null && formula1.literals().size() > 0)
                for (Literal literal : formula1.literals()) {
                    Stmt tempStmt = this.sbpe.getCondtionFromLiteral(literal);
                    if (tempStmt != null)
                        literalStmts.add(this.sbpe.getCondtionFromLiteral(literal));
                }
            List<Literal> removedLiterals = new ArrayList<>();
            for (Literal literal : formula1.literals()) {
                Stmt literalStmt = this.sbpe.getCondtionFromLiteral(literal);
                if (!currentPath.contains(literalStmt))
                    removedLiterals.add(literal);
            }
            System.out.println("formula = " + formula1);
            for (Literal literal : removedLiterals) {
                if (literal.getClass().toString().contains(".Literal")) {
                    Variable v = literal.variable();
                    formula1 = formula1.substitute(v, (Formula)(new FormulaFactory()).falsum());
                    continue;
                }
                if (literal.getClass().toString().contains(".Variable"))
                    formula1 = formula1.substitute((Variable)literal, (Formula)(new FormulaFactory()).verum());
            }
            System.out.println("formulaAfter1 = " + formula1);
            if (formula1 != null && formula1.literals() != null) {
                List<Septet<List<SymbolicValue>, List<SymbolicValue>, List<SymbolicValue>, Value, Value, Constant, Literal>> septets = new ArrayList<>();
                ArrayList<Pair<Literal, Literal>> literalReplacePairs = new ArrayList<>();
                for (Literal literal : formula1.literals()) {
                    Stmt literalStmt = this.sbpe.getCondtionFromLiteral(literal);
                    if (!currentPath.contains(literalStmt))
                        continue;
                    if (this.sbpe.getCondtionFromLiteral(literal) != null) {
                        System.out.println("literal = " + literal);
                        System.out.println("literalStmt.line: " + this.sbpe.getCondtionFromLiteral(literal).getJavaSourceStartLineNumber());
                        Septet<List<SymbolicValue>, List<SymbolicValue>, List<SymbolicValue>, Value, Value, Constant, Literal> septet = getContextualValues(literal);
                        if (septet != null) {
                            septets.add(septet);
                            Literal newLiteral = processingSeptet(literal, septet);
                            literalReplacePairs.add(new Pair(literal, newLiteral));
                            continue;
                        }
                        systemApiFlag = true;
                        System.out.println("septet == null");
                    }
                }
                for (Pair<Literal, Literal> pair : literalReplacePairs) {
                    Literal oldL = (Literal)pair.getValue0();
                    Literal newL = (Literal)pair.getValue1();
                    if (oldL != null && newL != null)
                        formula1 = formula1.substitute(oldL.variable(), (Formula)newL.variable());
                }
                System.out.println("formulaAfter2 = " + formula1);
                String nodeToString = node.toString();
                if (nodeToString.contains("<")) {
                    nodeToString = nodeToString.substring(nodeToString.indexOf("<"));
                    List<Value> argList = ((Stmt)node).getInvokeExpr().getArgs();
                    for (Value arg : argList) {
                        if (arg.toString().startsWith("$stack"))
                            nodeToString = nodeToString.replaceAll(arg.toString(), "fakestack");
                    }
                }
                formula1 = (new FormulaFactory()).and(new Formula[] { formula1, (Formula)(new FormulaFactory()).literal(nodeToString, true), (Formula)(new FormulaFactory()).literal(this.icfg.getMethodOf(node).getDeclaringClass().toString(), true) }).cnf();
                System.out.println("formulaAfter3 = " + formula1);
                this.interestingFormulae.add(new Triplet(node, formula1, septets));
            }
            this.containSystemSpecificPreconditions.put(node, Boolean.valueOf(systemApiFlag));
            System.out.println("end getDimensionPixelSize():::===");
            System.out.println();
        }
    }

    private Literal processingSeptet(Literal l, Septet<List<SymbolicValue>, List<SymbolicValue>, List<SymbolicValue>, Value, Value, Constant, Literal> septet) {
        Literal literal = null;
        List<SymbolicValue> valueOp1 = (List<SymbolicValue>)septet.getValue1(), valueOp2 = (List<SymbolicValue>)septet.getValue2();
        Value value1 = (Value)septet.getValue3(), value2 = (Value)septet.getValue4();
        String operator = obtainOperator(septet);
        boolean indexFlag = true;
        if (valueOp1 != null && valueOp1.size() > 0)
            for (SymbolicValue v1 : valueOp1) {
                if (!v1.toString().contains(".getIndex("))
                    indexFlag = false;
            }
        if (indexFlag) {
            literal = (new FormulaFactory()).literal("(android.content.res.TypedArray.getIndex(int)) " + operator + " " + value2, true);
            if (l.toString().startsWith("~"))
                literal = literal.negate();
            return literal;
        }
        if (valueOp1 != null && valueOp1.size() == 1 && (
                (SymbolicValue)valueOp1.get(0)).toString().contains("TypedArray")) {
            literal = (new FormulaFactory()).literal((new StringBuilder()).append(valueOp1.get(0)).append(" ").append(operator).append(" ").append(value2).toString(), true);
            if (l.toString().startsWith("~"))
                literal = literal.negate();
            return literal;
        }
        if (valueOp1 != null && valueOp1.size() > 1) {
            SymbolicValue v0 = valueOp1.get(0);
            boolean equiFlag = true;
            for (int i = 1; i < valueOp1.size(); i++) {
                if (!v0.toString().equals(((SymbolicValue)valueOp1.get(i)).toString())) {
                    equiFlag = false;
                    break;
                }
            }
            if (equiFlag &&
                    v0.toString().contains("TypedArray")) {
                literal = (new FormulaFactory()).literal((new StringBuilder()).append(valueOp1.get(0)).append(" ").append(operator).append(" ").append(value2).toString(), true);
                if (l.toString().startsWith("~"))
                    literal = literal.negate();
                return literal;
            }
        }
        return null;
    }

    private String obtainOperator(Septet<List<SymbolicValue>, List<SymbolicValue>, List<SymbolicValue>, Value, Value, Constant, Literal> septet) {
        Literal literal = (Literal)septet.getValue6();
        String operatorStr = literal.toString().split(" \\=\\> ")[1];
        operatorStr = operatorStr.split(" ")[1];
        return operatorStr;
    }

    public List<Triplet<Unit, Formula, List<Septet<List<SymbolicValue>, List<SymbolicValue>, List<SymbolicValue>, Value, Value, Constant, Literal>>>> getInterestingFormulae() {
        return this.interestingFormulae;
    }

    private Septet<List<SymbolicValue>, List<SymbolicValue>, List<SymbolicValue>, Value, Value, Constant, Literal> getContextualValues(Literal literal) {
        Stmt conditionStmt = this.sbpe.getCondtionFromLiteral(literal);
        if (conditionStmt == null) {
            conditionStmt =(Stmt) getCurrentPath().getLast();
            String variableName = literal.name().split(" => ")[1];
            variableName = variableName.substring(0, variableName.length() - 1);
            Value vararg = null;
            if (conditionStmt.containsInvokeExpr()) {
                List<Value> args = conditionStmt.getInvokeExpr().getArgs();
                for (Value arg : args) {
                    if (arg.toString().equals(variableName)) {
                        vararg = arg;
                        break;
                    }
                }
            }
            if (vararg != null) {
                System.out.println("vararg.toString() = " + vararg.toString());
                Value op1 = vararg;
                Value op2 = null;
                ContextualValues contextualValuesOp1 = null;
                ContextualValues contextualValuesOp2 = null;
                List<SymbolicValue> valuesOp1 = null;
                List<SymbolicValue> valuesOp2 = null;
                List<SymbolicValue> values = null;
                Constant constant = null;
                boolean isNullCheck = false;
                if (!(op1 instanceof Constant))
                    contextualValuesOp1 = getContextualValues((Unit)conditionStmt, op1);
                if (contextualValuesOp1 != null)
                    valuesOp1 = contextualValuesOp1.getLastCoherentValues((Unit)conditionStmt);
                if (valuesOp1 != null) {
                    values = valuesOp1;
                    if (op2 instanceof Constant) {
                        constant = (Constant)op2;
                    } else if (containConstantSymbolicValue((Unit)conditionStmt, op2)) {
                        constant = getConstantValue((Unit)conditionStmt, op2);
                    }
                    isNullCheck = isNullCheck(valuesOp1);
                } else if (valuesOp2 != null) {
                    values = valuesOp2;
                    if (op1 instanceof Constant) {
                        constant = (Constant)op1;
                    } else if (containConstantSymbolicValue((Unit)conditionStmt, op1)) {
                        constant = getConstantValue((Unit)conditionStmt, op1);
                    }
                    isNullCheck = isNullCheck(valuesOp2);
                }
                return new Septet(values, valuesOp1, valuesOp2, op1, op2, constant, literal);
            }
        } else {
            if (conditionStmt instanceof IfStmt) {
                IfStmt ifStmt = (IfStmt)conditionStmt;
                ConditionExpr conditionExpr = (ConditionExpr)ifStmt.getCondition();
                Value op1 = conditionExpr.getOp1();
                Value op2 = conditionExpr.getOp2();
                ContextualValues contextualValuesOp1 = null;
                ContextualValues contextualValuesOp2 = null;
                List<SymbolicValue> valuesOp1 = null;
                List<SymbolicValue> valuesOp2 = null;
                List<SymbolicValue> values = null;
                Constant constant = null;
                boolean isNullCheck = false;
                if (!(op1 instanceof Constant))
                    contextualValuesOp1 = getContextualValues((Unit)conditionStmt, op1);
                if (!(op2 instanceof Constant))
                    contextualValuesOp2 = getContextualValues((Unit)conditionStmt, op2);
                if (contextualValuesOp1 != null) {
                    valuesOp1 = contextualValuesOp1.getLastCoherentValues((Unit)ifStmt);
                    if (valuesOp1 == null || valuesOp1.size() == 0) {
                        valuesOp1 = new ArrayList<>();
                        valuesOp1.add(new SingleVariableValue(contextualValuesOp1.getReceiver(), this));
                    }
                }
                if (contextualValuesOp2 != null)
                    valuesOp2 = contextualValuesOp2.getLastCoherentValues((Unit)ifStmt);
                if (valuesOp1 != null) {
                    values = valuesOp1;
                    if (op2 instanceof Constant) {
                        constant = (Constant)op2;
                    } else if (containConstantSymbolicValue((Unit)conditionStmt, op2)) {
                        constant = getConstantValue((Unit)conditionStmt, op2);
                    }
                    isNullCheck = isNullCheck(valuesOp1);
                } else if (valuesOp2 != null) {
                    values = valuesOp2;
                    if (op1 instanceof Constant) {
                        constant = (Constant)op1;
                    } else if (containConstantSymbolicValue((Unit)conditionStmt, op1)) {
                        constant = getConstantValue((Unit)conditionStmt, op1);
                    }
                    isNullCheck = isNullCheck(valuesOp2);
                }
                return new Septet(values, valuesOp1, valuesOp2, op1, op2, constant, literal);
            }
            if (conditionStmt instanceof SwitchStmt) {
                SwitchStmt switchStmt = (SwitchStmt)conditionStmt;
                Value op1 = switchStmt.getKey();
                int index = Integer.parseInt(literal.toString().split("== ")[1].split("\\)")[0]);
                IntConstant intConstant = IntConstant.v(index);
                ContextualValues contextualValuesOp1 = null;
                ContextualValues contextualValuesOp2 = null;
                List<SymbolicValue> valuesOp1 = null;
                List<SymbolicValue> valuesOp2 = null;
                List<SymbolicValue> values = null;
                Constant constant = null;
                boolean isNullCheck = false;
                if (!(op1 instanceof Constant))
                    contextualValuesOp1 = getContextualValues((Unit)conditionStmt, op1);
                if (!(intConstant instanceof Constant))
                    contextualValuesOp2 = getContextualValues((Unit)conditionStmt, (Value)intConstant);
                if (contextualValuesOp1 != null)
                    valuesOp1 = contextualValuesOp1.getLastCoherentValues((Unit)switchStmt);
                if (contextualValuesOp2 != null)
                    valuesOp2 = contextualValuesOp2.getLastCoherentValues((Unit)switchStmt);
                if (valuesOp1 != null) {
                    values = valuesOp1;
                    if (intConstant instanceof Constant) {
                        constant = (Constant)intConstant;
                    } else if (containConstantSymbolicValue((Unit)conditionStmt, (Value)intConstant)) {
                        constant = getConstantValue((Unit)conditionStmt, (Value)intConstant);
                    }
                    isNullCheck = isNullCheck(valuesOp1);
                } else if (valuesOp2 != null) {
                    values = valuesOp2;
                    if (op1 instanceof Constant) {
                        constant = (Constant)op1;
                    } else if (containConstantSymbolicValue((Unit)conditionStmt, op1)) {
                        constant = getConstantValue((Unit)conditionStmt, op1);
                    }
                }
                return new Septet(values, valuesOp1, valuesOp2, op1, intConstant, constant, literal);
            }
        }
        return null;
    }

    private Constant getConstantValue(Unit u, Value v) {
        List<SymbolicValue> values = null;
        ContextualValues contextualValues = null;
        ConstantValue cv = null;
        Constant c = null;
        if (v != null) {
            contextualValues = (ContextualValues)((Map)getPathSensitiveContext().get(u)).get(v);
            if (contextualValues != null) {
                values = contextualValues.getAllValues();
                if (values != null)
                    for (SymbolicValue sv : values) {
                        if (sv instanceof ConstantValue) {
                            cv = (ConstantValue)sv;
                            c = cv.getConstant();
                            if (c instanceof IntConstant)
                                return (Constant)IntConstant.v(((IntConstant)c).value);
                        }
                    }
            }
        }
        return null;
    }

    private boolean isNullCheck(List<SymbolicValue> values) {
        BinOpValue binOpValue = null;
        Value binOpValueOp = null;
        for (SymbolicValue sv : values) {
            if (sv instanceof BinOpValue) {
                binOpValue = (BinOpValue)sv;
                binOpValueOp = binOpValue.getOp2();
                if (binOpValueOp instanceof FloatConstant) {
                    if (((FloatConstant)binOpValueOp).value == 0.0F || ((FloatConstant)binOpValueOp).value == -1.0F)
                        return true;
                    continue;
                }
                if (binOpValueOp instanceof IntConstant) {
                    if (((IntConstant)binOpValueOp).value == 0 || ((IntConstant)binOpValueOp).value == -1)
                        return true;
                    continue;
                }
                if (binOpValueOp instanceof DoubleConstant) {
                    if (((DoubleConstant)binOpValueOp).value == 0.0D || ((DoubleConstant)binOpValueOp).value == -1.0D)
                        return true;
                    continue;
                }
                if (binOpValueOp instanceof LongConstant && (
                        ((LongConstant)binOpValueOp).value == 0L || ((LongConstant)binOpValueOp).value == -1L))
                    return true;
            }
        }
        return false;
    }

    private boolean containConstantSymbolicValue(Unit u, Value v) {
        List<SymbolicValue> values = null;
        ContextualValues contextualValues = null;
        if (v != null) {
            if (getPathSensitiveContext().get(u) == null)
                getPathSensitiveContext().put(u, new HashMap<>());
            contextualValues = (ContextualValues)((Map)getPathSensitiveContext().get(u)).get(v);
            if (contextualValues != null) {
                values = contextualValues.getAllValues();
                if (values != null)
                    for (SymbolicValue sv : values) {
                        if (sv instanceof ConstantValue)
                            return true;
                    }
            }
        }
        return false;
    }

    private void updateValuesAtNode(List<Pair<Value, SymbolicValue>> results, Unit node) {
        Value value = null;
        SymbolicValue symbolicValue = null;
        List<SymbolicValue> symValues = null;
        Map<Value, List<SymbolicValue>> currentSymValues = new HashMap<>();
        Map<Value, List<SymbolicValue>> tmpSymValues = new HashMap<>();
        currentSymValues.putAll(this.currentValues);
        if (results != null) {
            for (Pair<Value, SymbolicValue> p : results) {
                value = (Value)p.getValue0();
                symbolicValue = (SymbolicValue)p.getValue1();
                symValues = tmpSymValues.get(value);
                if (symValues == null) {
                    symValues = new ArrayList<>();
                    tmpSymValues.put(value, symValues);
                }
                symValues.add(symbolicValue);
            }
            for (Map.Entry<Value, List<SymbolicValue>> e : tmpSymValues.entrySet())
                currentSymValues.put(e.getKey(), e.getValue());
        }
        this.currentValues = currentSymValues;
    }

    public Map<Value, ContextualValues> getContext() {
        return this.symbolicExecutionResults;
    }

    public Map<Unit, Map<Value, ContextualValues>> getPathSensitiveContext() {
        return this.pathSensitiveSymbolicExecutionResults;
    }

    public ContextualValues getContextualValueDebug(String str) {
        Set<Value> keys = this.symbolicExecutionResults.keySet();
        for (Value key : keys) {
            if (key.toString().equals(str))
                return this.symbolicExecutionResults.get(key);
        }
        return null;
    }

    public ContextualValues getContextualValues(Unit u, Value v) {
        Set<Value> keys = this.symbolicExecutionResults.keySet();
        for (Value key : keys) {
            if (key.toString().equals(v.toString()) && this.symbolicExecutionResults.get(v) != null)
                return this.symbolicExecutionResults.get(v);
        }
        SootMethod mtd = this.icfg.getMethodOf(u);
        SimpleLocalDefs simpleLocalDefs = new SimpleLocalDefs((UnitGraph)new CompleteUnitGraph(mtd.getActiveBody()));
        SimpleLocalUses simpleLocalUses = new SimpleLocalUses((UnitGraph)new CompleteUnitGraph(mtd.getActiveBody()), (LocalDefs)simpleLocalDefs);
        if (v instanceof Local && simpleLocalDefs.getDefsOfAt((Local)v, u).size() > 0) {
            Unit defUnit = simpleLocalDefs.getDefsOfAt((Local)v, u).get(0);
            if (defUnit != null) {
                if ((defUnit.toString().contains("boolean hasValue(") || defUnit.toString().contains("hasValueOrEmpty(") || defUnit
                        .toString().contains("getBoolean") || defUnit.toString().contains("getInt(") || defUnit
                        .toString().contains("getFloat(") || defUnit.toString().contains("getColor(")) && defUnit instanceof AssignStmt && ((AssignStmt)defUnit)
                        .containsInvokeExpr())
                    return new ContextualValues(this, ((AssignStmt)defUnit).getRightOp());
                if (defUnit instanceof AssignStmt && ((AssignStmt)defUnit).getRightOp() instanceof soot.jimple.FieldRef)
                    return new ContextualValues(this, ((AssignStmt)defUnit).getRightOp());
                if (defUnit instanceof AssignStmt && ((AssignStmt)defUnit).getRightOp() instanceof Constant)
                    return new ContextualValues(this, ((AssignStmt)defUnit).getRightOp());
                if (defUnit instanceof IdentityStmt && ((IdentityStmt)defUnit).getRightOp() instanceof soot.jimple.ParameterRef)
                    return new ContextualValues(this, ((IdentityStmt)defUnit).getRightOp());
                if (defUnit instanceof AssignStmt && ((AssignStmt)defUnit).containsInvokeExpr())
                    return new ContextualValues(this, ((AssignStmt)defUnit).getRightOp());
            }
        }
        return null;
    }

    public ContextualValues getContextualValues(Value v) {
        Set<Value> keys = this.symbolicExecutionResults.keySet();
        for (Value key : keys) {
            if (key.toString().equals(v.toString()))
                return this.symbolicExecutionResults.get(v);
        }
        return null;
    }

    public Map<Value, List<SymbolicValue>> getValuesAtNode(Unit node) {
        return this.valuesAtNode.get(node);
    }

    protected void processNeighbor(Unit node, Unit neighbour) {}

    protected void processNodeAfterNeighbors(Unit node) {
        this.valuesAtNode.remove(node);
    }
}
