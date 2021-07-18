/**
 * Generated from BindingExpression.g4 by ANTLR 4.5
 */
package android.databinding.parser;


import static android.databinding.parser.BindingExpressionParser.RULE_arguments;
import static android.databinding.parser.BindingExpressionParser.RULE_bindingSyntax;
import static android.databinding.parser.BindingExpressionParser.RULE_classExtraction;
import static android.databinding.parser.BindingExpressionParser.RULE_classOrInterfaceType;
import static android.databinding.parser.BindingExpressionParser.RULE_constantValue;
import static android.databinding.parser.BindingExpressionParser.RULE_defaults;
import static android.databinding.parser.BindingExpressionParser.RULE_explicitGenericInvocation;
import static android.databinding.parser.BindingExpressionParser.RULE_explicitGenericInvocationSuffix;
import static android.databinding.parser.BindingExpressionParser.RULE_expressionList;
import static android.databinding.parser.BindingExpressionParser.RULE_identifier;
import static android.databinding.parser.BindingExpressionParser.RULE_javaLiteral;
import static android.databinding.parser.BindingExpressionParser.RULE_literal;
import static android.databinding.parser.BindingExpressionParser.RULE_primitiveType;
import static android.databinding.parser.BindingExpressionParser.RULE_resourceParameters;
import static android.databinding.parser.BindingExpressionParser.RULE_resources;
import static android.databinding.parser.BindingExpressionParser.RULE_stringLiteral;
import static android.databinding.parser.BindingExpressionParser.RULE_type;
import static android.databinding.parser.BindingExpressionParser.RULE_typeArguments;


public class BindingExpressionParser extends android.databinding.parser.Parser {
    public static final int T__0 = 1;

    public static final int T__1 = 2;

    public static final int T__2 = 3;

    public static final int T__3 = 4;

    public static final int T__4 = 5;

    public static final int T__5 = 6;

    public static final int T__6 = 7;

    public static final int T__7 = 8;

    public static final int T__8 = 9;

    public static final int T__9 = 10;

    public static final int T__10 = 11;

    public static final int T__11 = 12;

    public static final int T__12 = 13;

    public static final int T__13 = 14;

    public static final int T__14 = 15;

    public static final int T__15 = 16;

    public static final int T__16 = 17;

    public static final int T__17 = 18;

    public static final int T__18 = 19;

    public static final int T__19 = 20;

    public static final int T__20 = 21;

    public static final int T__21 = 22;

    public static final int T__22 = 23;

    public static final int T__23 = 24;

    public static final int T__24 = 25;

    public static final int T__25 = 26;

    public static final int T__26 = 27;

    public static final int T__27 = 28;

    public static final int T__28 = 29;

    public static final int T__29 = 30;

    public static final int T__30 = 31;

    public static final int T__31 = 32;

    public static final int T__32 = 33;

    public static final int T__33 = 34;

    public static final int T__34 = 35;

    public static final int T__35 = 36;

    public static final int T__36 = 37;

    public static final int T__37 = 38;

    public static final int T__38 = 39;

    public static final int T__39 = 40;

    public static final int T__40 = 41;

    public static final int T__41 = 42;

    public static final int T__42 = 43;

    public static final int THIS = 44;

    public static final int IntegerLiteral = 45;

    public static final int FloatingPointLiteral = 46;

    public static final int BooleanLiteral = 47;

    public static final int CharacterLiteral = 48;

    public static final int SingleQuoteString = 49;

    public static final int DoubleQuoteString = 50;

    public static final int NullLiteral = 51;

    public static final int Identifier = 52;

    public static final int WS = 53;

    public static final int ResourceReference = 54;

    public static final int PackageName = 55;

    public static final int ResourceType = 56;

    public static final int RULE_bindingSyntax = 0;

    public static final int RULE_defaults = 1;

    public static final int RULE_constantValue = 2;

    public static final int RULE_expression = 3;

    public static final int RULE_classExtraction = 4;

    public static final int RULE_expressionList = 5;

    public static final int RULE_literal = 6;

    public static final int RULE_identifier = 7;

    public static final int RULE_javaLiteral = 8;

    public static final int RULE_stringLiteral = 9;

    public static final int RULE_explicitGenericInvocation = 10;

    public static final int RULE_typeArguments = 11;

    public static final int RULE_type = 12;

    public static final int RULE_explicitGenericInvocationSuffix = 13;

    public static final int RULE_arguments = 14;

    public static final int RULE_classOrInterfaceType = 15;

    public static final int RULE_primitiveType = 16;

    public static final int RULE_resources = 17;

    public static final int RULE_resourceParameters = 18;

    public static final java.lang.String[] ruleNames = new java.lang.String[]{ "bindingSyntax", "defaults", "constantValue", "expression", "classExtraction", "expressionList", "literal", "identifier", "javaLiteral", "stringLiteral", "explicitGenericInvocation", "typeArguments", "type", "explicitGenericInvocationSuffix", "arguments", "classOrInterfaceType", "primitiveType", "resources", "resourceParameters" };

    private static final java.lang.String[] _LITERAL_NAMES = new java.lang.String[]{ null, "','", "'default'", "'='", "'('", "')'", "'.'", "'['", "']'", "'+'", "'-'", "'~'", "'!'", "'*'", "'/'", "'%'", "'<<'", "'>>>'", "'>>'", "'<='", "'>='", "'>'", "'<'", "'instanceof'", "'=='", "'!='", "'&'", "'^'", "'|'", "'&&'", "'||'", "'?'", "':'", "'??'", "'class'", "'void'", "'boolean'", "'char'", "'byte'", "'short'", "'int'", "'long'", "'float'", "'double'", "'this'", null, null, null, null, null, null, "'null'" };

    private static final java.lang.String[] _SYMBOLIC_NAMES = new java.lang.String[]{ null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "THIS", "IntegerLiteral", "FloatingPointLiteral", "BooleanLiteral", "CharacterLiteral", "SingleQuoteString", "DoubleQuoteString", "NullLiteral", "Identifier", "WS", "ResourceReference", "PackageName", "ResourceType" };

    public static final android.databinding.parser.Vocabulary VOCABULARY = new android.databinding.parser.VocabularyImpl(android.databinding.parser.BindingExpressionParser._LITERAL_NAMES, android.databinding.parser.BindingExpressionParser._SYMBOLIC_NAMES);

    /**
     *
     *
     * @deprecated Use {@link #VOCABULARY} instead.
     */
    @java.lang.Deprecated
    public static final java.lang.String[] tokenNames;

    static {
        tokenNames = new java.lang.String[android.databinding.parser.BindingExpressionParser._SYMBOLIC_NAMES.length];
        for (int i = 0; i < android.databinding.parser.BindingExpressionParser.tokenNames.length; i++) {
            android.databinding.parser.BindingExpressionParser.tokenNames[i] = android.databinding.parser.BindingExpressionParser.VOCABULARY.getLiteralName(i);
            if (android.databinding.parser.BindingExpressionParser.tokenNames[i] == null) {
                android.databinding.parser.BindingExpressionParser.tokenNames[i] = android.databinding.parser.BindingExpressionParser.VOCABULARY.getSymbolicName(i);
            }
            if (android.databinding.parser.BindingExpressionParser.tokenNames[i] == null) {
                android.databinding.parser.BindingExpressionParser.tokenNames[i] = "<INVALID>";
            }
        }
    }

    @java.lang.Override
    @java.lang.Deprecated
    public java.lang.String[] getTokenNames() {
        return android.databinding.parser.BindingExpressionParser.tokenNames;
    }

    @java.lang.Override
    @android.databinding.parser.NotNull
    public android.databinding.parser.Vocabulary getVocabulary() {
        return android.databinding.parser.BindingExpressionParser.VOCABULARY;
    }

    @java.lang.Override
    public java.lang.String getGrammarFileName() {
        return "BindingExpression.g4";
    }

    @java.lang.Override
    public java.lang.String[] getRuleNames() {
        return android.databinding.parser.BindingExpressionParser.ruleNames;
    }

    @java.lang.Override
    public java.lang.String getSerializedATN() {
        return android.databinding.parser.BindingExpressionParser._serializedATN;
    }

    public BindingExpressionParser(android.databinding.parser.TokenStream input) {
        super(input);
        _interp = new android.databinding.parser.ParserATNSimulator(this, android.databinding.parser.BindingExpressionParser._ATN);
    }

    public static class BindingSyntaxContext extends android.databinding.parser.ParserRuleContext {
        public android.databinding.parser.BindingExpressionParser.ExpressionContext expression() {
            return getRuleContext(android.databinding.parser.BindingExpressionParser.ExpressionContext.class, 0);
        }

        public android.databinding.parser.BindingExpressionParser.DefaultsContext defaults() {
            return getRuleContext(android.databinding.parser.BindingExpressionParser.DefaultsContext.class, 0);
        }

        public BindingSyntaxContext(android.databinding.parser.ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @java.lang.Override
        public int getRuleIndex() {
            return RULE_bindingSyntax;
        }

        @java.lang.Override
        public void enterRule(android.databinding.parser.ParseTreeListener listener) {
            if (listener instanceof android.databinding.parser.BindingExpressionListener)
                ((android.databinding.parser.BindingExpressionListener) (listener)).enterBindingSyntax(this);

        }

        @java.lang.Override
        public void exitRule(android.databinding.parser.ParseTreeListener listener) {
            if (listener instanceof android.databinding.parser.BindingExpressionListener)
                ((android.databinding.parser.BindingExpressionListener) (listener)).exitBindingSyntax(this);

        }

        @java.lang.Override
        public <Result> Result accept(android.databinding.parser.ParseTreeVisitor<? extends Result> visitor) {
            if (visitor instanceof android.databinding.parser.BindingExpressionVisitor<?>)
                return ((android.databinding.parser.BindingExpressionVisitor<? extends Result>) (visitor)).visitBindingSyntax(this);
            else
                return visitor.visitChildren(this);

        }
    }

    @android.databinding.parser.RuleVersion(0)
    public final android.databinding.parser.BindingExpressionParser.BindingSyntaxContext bindingSyntax() throws android.databinding.parser.RecognitionException {
        android.databinding.parser.BindingExpressionParser.BindingSyntaxContext _localctx = new android.databinding.parser.BindingExpressionParser.BindingSyntaxContext(_ctx, getState());
        enterRule(_localctx, 0, RULE_bindingSyntax);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(38);
                expression(0);
                setState(40);
                _la = _input.LA(1);
                if (_la == android.databinding.parser.BindingExpressionParser.T__0) {
                    {
                        setState(39);
                        defaults();
                    }
                }
            }
        } catch (android.databinding.parser.RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class DefaultsContext extends android.databinding.parser.ParserRuleContext {
        public android.databinding.parser.BindingExpressionParser.ConstantValueContext constantValue() {
            return getRuleContext(android.databinding.parser.BindingExpressionParser.ConstantValueContext.class, 0);
        }

        public DefaultsContext(android.databinding.parser.ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @java.lang.Override
        public int getRuleIndex() {
            return RULE_defaults;
        }

        @java.lang.Override
        public void enterRule(android.databinding.parser.ParseTreeListener listener) {
            if (listener instanceof android.databinding.parser.BindingExpressionListener)
                ((android.databinding.parser.BindingExpressionListener) (listener)).enterDefaults(this);

        }

        @java.lang.Override
        public void exitRule(android.databinding.parser.ParseTreeListener listener) {
            if (listener instanceof android.databinding.parser.BindingExpressionListener)
                ((android.databinding.parser.BindingExpressionListener) (listener)).exitDefaults(this);

        }

        @java.lang.Override
        public <Result> Result accept(android.databinding.parser.ParseTreeVisitor<? extends Result> visitor) {
            if (visitor instanceof android.databinding.parser.BindingExpressionVisitor<?>)
                return ((android.databinding.parser.BindingExpressionVisitor<? extends Result>) (visitor)).visitDefaults(this);
            else
                return visitor.visitChildren(this);

        }
    }

    @android.databinding.parser.RuleVersion(0)
    public final android.databinding.parser.BindingExpressionParser.DefaultsContext defaults() throws android.databinding.parser.RecognitionException {
        android.databinding.parser.BindingExpressionParser.DefaultsContext _localctx = new android.databinding.parser.BindingExpressionParser.DefaultsContext(_ctx, getState());
        enterRule(_localctx, 2, RULE_defaults);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(42);
                match(android.databinding.parser.BindingExpressionParser.T__0);
                setState(43);
                match(android.databinding.parser.BindingExpressionParser.T__1);
                setState(44);
                match(android.databinding.parser.BindingExpressionParser.T__2);
                setState(45);
                constantValue();
            }
        } catch (android.databinding.parser.RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class ConstantValueContext extends android.databinding.parser.ParserRuleContext {
        public android.databinding.parser.BindingExpressionParser.LiteralContext literal() {
            return getRuleContext(android.databinding.parser.BindingExpressionParser.LiteralContext.class, 0);
        }

        public android.databinding.parser.TerminalNode ResourceReference() {
            return getToken(android.databinding.parser.BindingExpressionParser.ResourceReference, 0);
        }

        public android.databinding.parser.BindingExpressionParser.IdentifierContext identifier() {
            return getRuleContext(android.databinding.parser.BindingExpressionParser.IdentifierContext.class, 0);
        }

        public ConstantValueContext(android.databinding.parser.ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @java.lang.Override
        public int getRuleIndex() {
            return RULE_constantValue;
        }

        @java.lang.Override
        public void enterRule(android.databinding.parser.ParseTreeListener listener) {
            if (listener instanceof android.databinding.parser.BindingExpressionListener)
                ((android.databinding.parser.BindingExpressionListener) (listener)).enterConstantValue(this);

        }

        @java.lang.Override
        public void exitRule(android.databinding.parser.ParseTreeListener listener) {
            if (listener instanceof android.databinding.parser.BindingExpressionListener)
                ((android.databinding.parser.BindingExpressionListener) (listener)).exitConstantValue(this);

        }

        @java.lang.Override
        public <Result> Result accept(android.databinding.parser.ParseTreeVisitor<? extends Result> visitor) {
            if (visitor instanceof android.databinding.parser.BindingExpressionVisitor<?>)
                return ((android.databinding.parser.BindingExpressionVisitor<? extends Result>) (visitor)).visitConstantValue(this);
            else
                return visitor.visitChildren(this);

        }
    }

    @android.databinding.parser.RuleVersion(0)
    public final android.databinding.parser.BindingExpressionParser.ConstantValueContext constantValue() throws android.databinding.parser.RecognitionException {
        android.databinding.parser.BindingExpressionParser.ConstantValueContext _localctx = new android.databinding.parser.BindingExpressionParser.ConstantValueContext(_ctx, getState());
        enterRule(_localctx, 4, RULE_constantValue);
        try {
            setState(50);
            switch (_input.LA(1)) {
                case android.databinding.parser.BindingExpressionParser.IntegerLiteral :
                case android.databinding.parser.BindingExpressionParser.FloatingPointLiteral :
                case android.databinding.parser.BindingExpressionParser.BooleanLiteral :
                case android.databinding.parser.BindingExpressionParser.CharacterLiteral :
                case android.databinding.parser.BindingExpressionParser.SingleQuoteString :
                case android.databinding.parser.BindingExpressionParser.DoubleQuoteString :
                case android.databinding.parser.BindingExpressionParser.NullLiteral :
                    enterOuterAlt(_localctx, 1);
                    {
                        setState(47);
                        literal();
                    }
                    break;
                case android.databinding.parser.BindingExpressionParser.ResourceReference :
                    enterOuterAlt(_localctx, 2);
                    {
                        setState(48);
                        match(android.databinding.parser.BindingExpressionParser.ResourceReference);
                    }
                    break;
                case android.databinding.parser.BindingExpressionParser.Identifier :
                    enterOuterAlt(_localctx, 3);
                    {
                        setState(49);
                        identifier();
                    }
                    break;
                default :
                    throw new android.databinding.parser.NoViableAltException(this);
            }
        } catch (android.databinding.parser.RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class ExpressionContext extends android.databinding.parser.ParserRuleContext {
        public ExpressionContext(android.databinding.parser.ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @java.lang.Override
        public int getRuleIndex() {
            return android.databinding.parser.BindingExpressionParser.RULE_expression;
        }

        public ExpressionContext() {
        }

        public void copyFrom(android.databinding.parser.BindingExpressionParser.ExpressionContext ctx) {
            copyFrom(ctx);
        }
    }

    public static class BracketOpContext extends android.databinding.parser.BindingExpressionParser.ExpressionContext {
        public java.util.List<? extends android.databinding.parser.BindingExpressionParser.ExpressionContext> expression() {
            return getRuleContexts(android.databinding.parser.BindingExpressionParser.ExpressionContext.class);
        }

        public android.databinding.parser.BindingExpressionParser.ExpressionContext expression(int i) {
            return getRuleContext(android.databinding.parser.BindingExpressionParser.ExpressionContext.class, i);
        }

        public BracketOpContext(android.databinding.parser.BindingExpressionParser.ExpressionContext ctx) {
            copyFrom(ctx);
        }

        @java.lang.Override
        public void enterRule(android.databinding.parser.ParseTreeListener listener) {
            if (listener instanceof android.databinding.parser.BindingExpressionListener)
                ((android.databinding.parser.BindingExpressionListener) (listener)).enterBracketOp(this);

        }

        @java.lang.Override
        public void exitRule(android.databinding.parser.ParseTreeListener listener) {
            if (listener instanceof android.databinding.parser.BindingExpressionListener)
                ((android.databinding.parser.BindingExpressionListener) (listener)).exitBracketOp(this);

        }

        @java.lang.Override
        public <Result> Result accept(android.databinding.parser.ParseTreeVisitor<? extends Result> visitor) {
            if (visitor instanceof android.databinding.parser.BindingExpressionVisitor<?>)
                return ((android.databinding.parser.BindingExpressionVisitor<? extends Result>) (visitor)).visitBracketOp(this);
            else
                return visitor.visitChildren(this);

        }
    }

    public static class ResourceContext extends android.databinding.parser.BindingExpressionParser.ExpressionContext {
        public android.databinding.parser.BindingExpressionParser.ResourcesContext resources() {
            return getRuleContext(android.databinding.parser.BindingExpressionParser.ResourcesContext.class, 0);
        }

        public ResourceContext(android.databinding.parser.BindingExpressionParser.ExpressionContext ctx) {
            copyFrom(ctx);
        }

        @java.lang.Override
        public void enterRule(android.databinding.parser.ParseTreeListener listener) {
            if (listener instanceof android.databinding.parser.BindingExpressionListener)
                ((android.databinding.parser.BindingExpressionListener) (listener)).enterResource(this);

        }

        @java.lang.Override
        public void exitRule(android.databinding.parser.ParseTreeListener listener) {
            if (listener instanceof android.databinding.parser.BindingExpressionListener)
                ((android.databinding.parser.BindingExpressionListener) (listener)).exitResource(this);

        }

        @java.lang.Override
        public <Result> Result accept(android.databinding.parser.ParseTreeVisitor<? extends Result> visitor) {
            if (visitor instanceof android.databinding.parser.BindingExpressionVisitor<?>)
                return ((android.databinding.parser.BindingExpressionVisitor<? extends Result>) (visitor)).visitResource(this);
            else
                return visitor.visitChildren(this);

        }
    }

    public static class CastOpContext extends android.databinding.parser.BindingExpressionParser.ExpressionContext {
        public android.databinding.parser.BindingExpressionParser.TypeContext type() {
            return getRuleContext(android.databinding.parser.BindingExpressionParser.TypeContext.class, 0);
        }

        public android.databinding.parser.BindingExpressionParser.ExpressionContext expression() {
            return getRuleContext(android.databinding.parser.BindingExpressionParser.ExpressionContext.class, 0);
        }

        public CastOpContext(android.databinding.parser.BindingExpressionParser.ExpressionContext ctx) {
            copyFrom(ctx);
        }

        @java.lang.Override
        public void enterRule(android.databinding.parser.ParseTreeListener listener) {
            if (listener instanceof android.databinding.parser.BindingExpressionListener)
                ((android.databinding.parser.BindingExpressionListener) (listener)).enterCastOp(this);

        }

        @java.lang.Override
        public void exitRule(android.databinding.parser.ParseTreeListener listener) {
            if (listener instanceof android.databinding.parser.BindingExpressionListener)
                ((android.databinding.parser.BindingExpressionListener) (listener)).exitCastOp(this);

        }

        @java.lang.Override
        public <Result> Result accept(android.databinding.parser.ParseTreeVisitor<? extends Result> visitor) {
            if (visitor instanceof android.databinding.parser.BindingExpressionVisitor<?>)
                return ((android.databinding.parser.BindingExpressionVisitor<? extends Result>) (visitor)).visitCastOp(this);
            else
                return visitor.visitChildren(this);

        }
    }

    public static class UnaryOpContext extends android.databinding.parser.BindingExpressionParser.ExpressionContext {
        public android.databinding.parser.Token op;

        public android.databinding.parser.BindingExpressionParser.ExpressionContext expression() {
            return getRuleContext(android.databinding.parser.BindingExpressionParser.ExpressionContext.class, 0);
        }

        public UnaryOpContext(android.databinding.parser.BindingExpressionParser.ExpressionContext ctx) {
            copyFrom(ctx);
        }

        @java.lang.Override
        public void enterRule(android.databinding.parser.ParseTreeListener listener) {
            if (listener instanceof android.databinding.parser.BindingExpressionListener)
                ((android.databinding.parser.BindingExpressionListener) (listener)).enterUnaryOp(this);

        }

        @java.lang.Override
        public void exitRule(android.databinding.parser.ParseTreeListener listener) {
            if (listener instanceof android.databinding.parser.BindingExpressionListener)
                ((android.databinding.parser.BindingExpressionListener) (listener)).exitUnaryOp(this);

        }

        @java.lang.Override
        public <Result> Result accept(android.databinding.parser.ParseTreeVisitor<? extends Result> visitor) {
            if (visitor instanceof android.databinding.parser.BindingExpressionVisitor<?>)
                return ((android.databinding.parser.BindingExpressionVisitor<? extends Result>) (visitor)).visitUnaryOp(this);
            else
                return visitor.visitChildren(this);

        }
    }

    public static class AndOrOpContext extends android.databinding.parser.BindingExpressionParser.ExpressionContext {
        public android.databinding.parser.BindingExpressionParser.ExpressionContext left;

        public android.databinding.parser.Token op;

        public android.databinding.parser.BindingExpressionParser.ExpressionContext right;

        public java.util.List<? extends android.databinding.parser.BindingExpressionParser.ExpressionContext> expression() {
            return getRuleContexts(android.databinding.parser.BindingExpressionParser.ExpressionContext.class);
        }

        public android.databinding.parser.BindingExpressionParser.ExpressionContext expression(int i) {
            return getRuleContext(android.databinding.parser.BindingExpressionParser.ExpressionContext.class, i);
        }

        public AndOrOpContext(android.databinding.parser.BindingExpressionParser.ExpressionContext ctx) {
            copyFrom(ctx);
        }

        @java.lang.Override
        public void enterRule(android.databinding.parser.ParseTreeListener listener) {
            if (listener instanceof android.databinding.parser.BindingExpressionListener)
                ((android.databinding.parser.BindingExpressionListener) (listener)).enterAndOrOp(this);

        }

        @java.lang.Override
        public void exitRule(android.databinding.parser.ParseTreeListener listener) {
            if (listener instanceof android.databinding.parser.BindingExpressionListener)
                ((android.databinding.parser.BindingExpressionListener) (listener)).exitAndOrOp(this);

        }

        @java.lang.Override
        public <Result> Result accept(android.databinding.parser.ParseTreeVisitor<? extends Result> visitor) {
            if (visitor instanceof android.databinding.parser.BindingExpressionVisitor<?>)
                return ((android.databinding.parser.BindingExpressionVisitor<? extends Result>) (visitor)).visitAndOrOp(this);
            else
                return visitor.visitChildren(this);

        }
    }

    public static class MethodInvocationContext extends android.databinding.parser.BindingExpressionParser.ExpressionContext {
        public android.databinding.parser.BindingExpressionParser.ExpressionContext target;

        public android.databinding.parser.Token methodName;

        public android.databinding.parser.BindingExpressionParser.ExpressionListContext args;

        public android.databinding.parser.BindingExpressionParser.ExpressionContext expression() {
            return getRuleContext(android.databinding.parser.BindingExpressionParser.ExpressionContext.class, 0);
        }

        public android.databinding.parser.TerminalNode Identifier() {
            return getToken(android.databinding.parser.BindingExpressionParser.Identifier, 0);
        }

        public android.databinding.parser.BindingExpressionParser.ExpressionListContext expressionList() {
            return getRuleContext(android.databinding.parser.BindingExpressionParser.ExpressionListContext.class, 0);
        }

        public MethodInvocationContext(android.databinding.parser.BindingExpressionParser.ExpressionContext ctx) {
            copyFrom(ctx);
        }

        @java.lang.Override
        public void enterRule(android.databinding.parser.ParseTreeListener listener) {
            if (listener instanceof android.databinding.parser.BindingExpressionListener)
                ((android.databinding.parser.BindingExpressionListener) (listener)).enterMethodInvocation(this);

        }

        @java.lang.Override
        public void exitRule(android.databinding.parser.ParseTreeListener listener) {
            if (listener instanceof android.databinding.parser.BindingExpressionListener)
                ((android.databinding.parser.BindingExpressionListener) (listener)).exitMethodInvocation(this);

        }

        @java.lang.Override
        public <Result> Result accept(android.databinding.parser.ParseTreeVisitor<? extends Result> visitor) {
            if (visitor instanceof android.databinding.parser.BindingExpressionVisitor<?>)
                return ((android.databinding.parser.BindingExpressionVisitor<? extends Result>) (visitor)).visitMethodInvocation(this);
            else
                return visitor.visitChildren(this);

        }
    }

    public static class PrimaryContext extends android.databinding.parser.BindingExpressionParser.ExpressionContext {
        public android.databinding.parser.BindingExpressionParser.LiteralContext literal() {
            return getRuleContext(android.databinding.parser.BindingExpressionParser.LiteralContext.class, 0);
        }

        public android.databinding.parser.BindingExpressionParser.IdentifierContext identifier() {
            return getRuleContext(android.databinding.parser.BindingExpressionParser.IdentifierContext.class, 0);
        }

        public android.databinding.parser.BindingExpressionParser.ClassExtractionContext classExtraction() {
            return getRuleContext(android.databinding.parser.BindingExpressionParser.ClassExtractionContext.class, 0);
        }

        public PrimaryContext(android.databinding.parser.BindingExpressionParser.ExpressionContext ctx) {
            copyFrom(ctx);
        }

        @java.lang.Override
        public void enterRule(android.databinding.parser.ParseTreeListener listener) {
            if (listener instanceof android.databinding.parser.BindingExpressionListener)
                ((android.databinding.parser.BindingExpressionListener) (listener)).enterPrimary(this);

        }

        @java.lang.Override
        public void exitRule(android.databinding.parser.ParseTreeListener listener) {
            if (listener instanceof android.databinding.parser.BindingExpressionListener)
                ((android.databinding.parser.BindingExpressionListener) (listener)).exitPrimary(this);

        }

        @java.lang.Override
        public <Result> Result accept(android.databinding.parser.ParseTreeVisitor<? extends Result> visitor) {
            if (visitor instanceof android.databinding.parser.BindingExpressionVisitor<?>)
                return ((android.databinding.parser.BindingExpressionVisitor<? extends Result>) (visitor)).visitPrimary(this);
            else
                return visitor.visitChildren(this);

        }
    }

    public static class GroupingContext extends android.databinding.parser.BindingExpressionParser.ExpressionContext {
        public android.databinding.parser.BindingExpressionParser.ExpressionContext expression() {
            return getRuleContext(android.databinding.parser.BindingExpressionParser.ExpressionContext.class, 0);
        }

        public GroupingContext(android.databinding.parser.BindingExpressionParser.ExpressionContext ctx) {
            copyFrom(ctx);
        }

        @java.lang.Override
        public void enterRule(android.databinding.parser.ParseTreeListener listener) {
            if (listener instanceof android.databinding.parser.BindingExpressionListener)
                ((android.databinding.parser.BindingExpressionListener) (listener)).enterGrouping(this);

        }

        @java.lang.Override
        public void exitRule(android.databinding.parser.ParseTreeListener listener) {
            if (listener instanceof android.databinding.parser.BindingExpressionListener)
                ((android.databinding.parser.BindingExpressionListener) (listener)).exitGrouping(this);

        }

        @java.lang.Override
        public <Result> Result accept(android.databinding.parser.ParseTreeVisitor<? extends Result> visitor) {
            if (visitor instanceof android.databinding.parser.BindingExpressionVisitor<?>)
                return ((android.databinding.parser.BindingExpressionVisitor<? extends Result>) (visitor)).visitGrouping(this);
            else
                return visitor.visitChildren(this);

        }
    }

    public static class TernaryOpContext extends android.databinding.parser.BindingExpressionParser.ExpressionContext {
        public android.databinding.parser.BindingExpressionParser.ExpressionContext left;

        public android.databinding.parser.Token op;

        public android.databinding.parser.BindingExpressionParser.ExpressionContext iftrue;

        public android.databinding.parser.BindingExpressionParser.ExpressionContext iffalse;

        public java.util.List<? extends android.databinding.parser.BindingExpressionParser.ExpressionContext> expression() {
            return getRuleContexts(android.databinding.parser.BindingExpressionParser.ExpressionContext.class);
        }

        public android.databinding.parser.BindingExpressionParser.ExpressionContext expression(int i) {
            return getRuleContext(android.databinding.parser.BindingExpressionParser.ExpressionContext.class, i);
        }

        public TernaryOpContext(android.databinding.parser.BindingExpressionParser.ExpressionContext ctx) {
            copyFrom(ctx);
        }

        @java.lang.Override
        public void enterRule(android.databinding.parser.ParseTreeListener listener) {
            if (listener instanceof android.databinding.parser.BindingExpressionListener)
                ((android.databinding.parser.BindingExpressionListener) (listener)).enterTernaryOp(this);

        }

        @java.lang.Override
        public void exitRule(android.databinding.parser.ParseTreeListener listener) {
            if (listener instanceof android.databinding.parser.BindingExpressionListener)
                ((android.databinding.parser.BindingExpressionListener) (listener)).exitTernaryOp(this);

        }

        @java.lang.Override
        public <Result> Result accept(android.databinding.parser.ParseTreeVisitor<? extends Result> visitor) {
            if (visitor instanceof android.databinding.parser.BindingExpressionVisitor<?>)
                return ((android.databinding.parser.BindingExpressionVisitor<? extends Result>) (visitor)).visitTernaryOp(this);
            else
                return visitor.visitChildren(this);

        }
    }

    public static class ComparisonOpContext extends android.databinding.parser.BindingExpressionParser.ExpressionContext {
        public android.databinding.parser.BindingExpressionParser.ExpressionContext left;

        public android.databinding.parser.Token op;

        public android.databinding.parser.BindingExpressionParser.ExpressionContext right;

        public java.util.List<? extends android.databinding.parser.BindingExpressionParser.ExpressionContext> expression() {
            return getRuleContexts(android.databinding.parser.BindingExpressionParser.ExpressionContext.class);
        }

        public android.databinding.parser.BindingExpressionParser.ExpressionContext expression(int i) {
            return getRuleContext(android.databinding.parser.BindingExpressionParser.ExpressionContext.class, i);
        }

        public ComparisonOpContext(android.databinding.parser.BindingExpressionParser.ExpressionContext ctx) {
            copyFrom(ctx);
        }

        @java.lang.Override
        public void enterRule(android.databinding.parser.ParseTreeListener listener) {
            if (listener instanceof android.databinding.parser.BindingExpressionListener)
                ((android.databinding.parser.BindingExpressionListener) (listener)).enterComparisonOp(this);

        }

        @java.lang.Override
        public void exitRule(android.databinding.parser.ParseTreeListener listener) {
            if (listener instanceof android.databinding.parser.BindingExpressionListener)
                ((android.databinding.parser.BindingExpressionListener) (listener)).exitComparisonOp(this);

        }

        @java.lang.Override
        public <Result> Result accept(android.databinding.parser.ParseTreeVisitor<? extends Result> visitor) {
            if (visitor instanceof android.databinding.parser.BindingExpressionVisitor<?>)
                return ((android.databinding.parser.BindingExpressionVisitor<? extends Result>) (visitor)).visitComparisonOp(this);
            else
                return visitor.visitChildren(this);

        }
    }

    public static class DotOpContext extends android.databinding.parser.BindingExpressionParser.ExpressionContext {
        public android.databinding.parser.BindingExpressionParser.ExpressionContext expression() {
            return getRuleContext(android.databinding.parser.BindingExpressionParser.ExpressionContext.class, 0);
        }

        public android.databinding.parser.TerminalNode Identifier() {
            return getToken(android.databinding.parser.BindingExpressionParser.Identifier, 0);
        }

        public DotOpContext(android.databinding.parser.BindingExpressionParser.ExpressionContext ctx) {
            copyFrom(ctx);
        }

        @java.lang.Override
        public void enterRule(android.databinding.parser.ParseTreeListener listener) {
            if (listener instanceof android.databinding.parser.BindingExpressionListener)
                ((android.databinding.parser.BindingExpressionListener) (listener)).enterDotOp(this);

        }

        @java.lang.Override
        public void exitRule(android.databinding.parser.ParseTreeListener listener) {
            if (listener instanceof android.databinding.parser.BindingExpressionListener)
                ((android.databinding.parser.BindingExpressionListener) (listener)).exitDotOp(this);

        }

        @java.lang.Override
        public <Result> Result accept(android.databinding.parser.ParseTreeVisitor<? extends Result> visitor) {
            if (visitor instanceof android.databinding.parser.BindingExpressionVisitor<?>)
                return ((android.databinding.parser.BindingExpressionVisitor<? extends Result>) (visitor)).visitDotOp(this);
            else
                return visitor.visitChildren(this);

        }
    }

    public static class MathOpContext extends android.databinding.parser.BindingExpressionParser.ExpressionContext {
        public android.databinding.parser.BindingExpressionParser.ExpressionContext left;

        public android.databinding.parser.Token op;

        public android.databinding.parser.BindingExpressionParser.ExpressionContext right;

        public java.util.List<? extends android.databinding.parser.BindingExpressionParser.ExpressionContext> expression() {
            return getRuleContexts(android.databinding.parser.BindingExpressionParser.ExpressionContext.class);
        }

        public android.databinding.parser.BindingExpressionParser.ExpressionContext expression(int i) {
            return getRuleContext(android.databinding.parser.BindingExpressionParser.ExpressionContext.class, i);
        }

        public MathOpContext(android.databinding.parser.BindingExpressionParser.ExpressionContext ctx) {
            copyFrom(ctx);
        }

        @java.lang.Override
        public void enterRule(android.databinding.parser.ParseTreeListener listener) {
            if (listener instanceof android.databinding.parser.BindingExpressionListener)
                ((android.databinding.parser.BindingExpressionListener) (listener)).enterMathOp(this);

        }

        @java.lang.Override
        public void exitRule(android.databinding.parser.ParseTreeListener listener) {
            if (listener instanceof android.databinding.parser.BindingExpressionListener)
                ((android.databinding.parser.BindingExpressionListener) (listener)).exitMathOp(this);

        }

        @java.lang.Override
        public <Result> Result accept(android.databinding.parser.ParseTreeVisitor<? extends Result> visitor) {
            if (visitor instanceof android.databinding.parser.BindingExpressionVisitor<?>)
                return ((android.databinding.parser.BindingExpressionVisitor<? extends Result>) (visitor)).visitMathOp(this);
            else
                return visitor.visitChildren(this);

        }
    }

    public static class QuestionQuestionOpContext extends android.databinding.parser.BindingExpressionParser.ExpressionContext {
        public android.databinding.parser.BindingExpressionParser.ExpressionContext left;

        public android.databinding.parser.Token op;

        public android.databinding.parser.BindingExpressionParser.ExpressionContext right;

        public java.util.List<? extends android.databinding.parser.BindingExpressionParser.ExpressionContext> expression() {
            return getRuleContexts(android.databinding.parser.BindingExpressionParser.ExpressionContext.class);
        }

        public android.databinding.parser.BindingExpressionParser.ExpressionContext expression(int i) {
            return getRuleContext(android.databinding.parser.BindingExpressionParser.ExpressionContext.class, i);
        }

        public QuestionQuestionOpContext(android.databinding.parser.BindingExpressionParser.ExpressionContext ctx) {
            copyFrom(ctx);
        }

        @java.lang.Override
        public void enterRule(android.databinding.parser.ParseTreeListener listener) {
            if (listener instanceof android.databinding.parser.BindingExpressionListener)
                ((android.databinding.parser.BindingExpressionListener) (listener)).enterQuestionQuestionOp(this);

        }

        @java.lang.Override
        public void exitRule(android.databinding.parser.ParseTreeListener listener) {
            if (listener instanceof android.databinding.parser.BindingExpressionListener)
                ((android.databinding.parser.BindingExpressionListener) (listener)).exitQuestionQuestionOp(this);

        }

        @java.lang.Override
        public <Result> Result accept(android.databinding.parser.ParseTreeVisitor<? extends Result> visitor) {
            if (visitor instanceof android.databinding.parser.BindingExpressionVisitor<?>)
                return ((android.databinding.parser.BindingExpressionVisitor<? extends Result>) (visitor)).visitQuestionQuestionOp(this);
            else
                return visitor.visitChildren(this);

        }
    }

    public static class BitShiftOpContext extends android.databinding.parser.BindingExpressionParser.ExpressionContext {
        public android.databinding.parser.BindingExpressionParser.ExpressionContext left;

        public android.databinding.parser.Token op;

        public android.databinding.parser.BindingExpressionParser.ExpressionContext right;

        public java.util.List<? extends android.databinding.parser.BindingExpressionParser.ExpressionContext> expression() {
            return getRuleContexts(android.databinding.parser.BindingExpressionParser.ExpressionContext.class);
        }

        public android.databinding.parser.BindingExpressionParser.ExpressionContext expression(int i) {
            return getRuleContext(android.databinding.parser.BindingExpressionParser.ExpressionContext.class, i);
        }

        public BitShiftOpContext(android.databinding.parser.BindingExpressionParser.ExpressionContext ctx) {
            copyFrom(ctx);
        }

        @java.lang.Override
        public void enterRule(android.databinding.parser.ParseTreeListener listener) {
            if (listener instanceof android.databinding.parser.BindingExpressionListener)
                ((android.databinding.parser.BindingExpressionListener) (listener)).enterBitShiftOp(this);

        }

        @java.lang.Override
        public void exitRule(android.databinding.parser.ParseTreeListener listener) {
            if (listener instanceof android.databinding.parser.BindingExpressionListener)
                ((android.databinding.parser.BindingExpressionListener) (listener)).exitBitShiftOp(this);

        }

        @java.lang.Override
        public <Result> Result accept(android.databinding.parser.ParseTreeVisitor<? extends Result> visitor) {
            if (visitor instanceof android.databinding.parser.BindingExpressionVisitor<?>)
                return ((android.databinding.parser.BindingExpressionVisitor<? extends Result>) (visitor)).visitBitShiftOp(this);
            else
                return visitor.visitChildren(this);

        }
    }

    public static class InstanceOfOpContext extends android.databinding.parser.BindingExpressionParser.ExpressionContext {
        public android.databinding.parser.BindingExpressionParser.ExpressionContext expression() {
            return getRuleContext(android.databinding.parser.BindingExpressionParser.ExpressionContext.class, 0);
        }

        public android.databinding.parser.BindingExpressionParser.TypeContext type() {
            return getRuleContext(android.databinding.parser.BindingExpressionParser.TypeContext.class, 0);
        }

        public InstanceOfOpContext(android.databinding.parser.BindingExpressionParser.ExpressionContext ctx) {
            copyFrom(ctx);
        }

        @java.lang.Override
        public void enterRule(android.databinding.parser.ParseTreeListener listener) {
            if (listener instanceof android.databinding.parser.BindingExpressionListener)
                ((android.databinding.parser.BindingExpressionListener) (listener)).enterInstanceOfOp(this);

        }

        @java.lang.Override
        public void exitRule(android.databinding.parser.ParseTreeListener listener) {
            if (listener instanceof android.databinding.parser.BindingExpressionListener)
                ((android.databinding.parser.BindingExpressionListener) (listener)).exitInstanceOfOp(this);

        }

        @java.lang.Override
        public <Result> Result accept(android.databinding.parser.ParseTreeVisitor<? extends Result> visitor) {
            if (visitor instanceof android.databinding.parser.BindingExpressionVisitor<?>)
                return ((android.databinding.parser.BindingExpressionVisitor<? extends Result>) (visitor)).visitInstanceOfOp(this);
            else
                return visitor.visitChildren(this);

        }
    }

    public static class BinaryOpContext extends android.databinding.parser.BindingExpressionParser.ExpressionContext {
        public android.databinding.parser.BindingExpressionParser.ExpressionContext left;

        public android.databinding.parser.Token op;

        public android.databinding.parser.BindingExpressionParser.ExpressionContext right;

        public java.util.List<? extends android.databinding.parser.BindingExpressionParser.ExpressionContext> expression() {
            return getRuleContexts(android.databinding.parser.BindingExpressionParser.ExpressionContext.class);
        }

        public android.databinding.parser.BindingExpressionParser.ExpressionContext expression(int i) {
            return getRuleContext(android.databinding.parser.BindingExpressionParser.ExpressionContext.class, i);
        }

        public BinaryOpContext(android.databinding.parser.BindingExpressionParser.ExpressionContext ctx) {
            copyFrom(ctx);
        }

        @java.lang.Override
        public void enterRule(android.databinding.parser.ParseTreeListener listener) {
            if (listener instanceof android.databinding.parser.BindingExpressionListener)
                ((android.databinding.parser.BindingExpressionListener) (listener)).enterBinaryOp(this);

        }

        @java.lang.Override
        public void exitRule(android.databinding.parser.ParseTreeListener listener) {
            if (listener instanceof android.databinding.parser.BindingExpressionListener)
                ((android.databinding.parser.BindingExpressionListener) (listener)).exitBinaryOp(this);

        }

        @java.lang.Override
        public <Result> Result accept(android.databinding.parser.ParseTreeVisitor<? extends Result> visitor) {
            if (visitor instanceof android.databinding.parser.BindingExpressionVisitor<?>)
                return ((android.databinding.parser.BindingExpressionVisitor<? extends Result>) (visitor)).visitBinaryOp(this);
            else
                return visitor.visitChildren(this);

        }
    }

    @android.databinding.parser.RuleVersion(0)
    public final android.databinding.parser.BindingExpressionParser.ExpressionContext expression() throws android.databinding.parser.RecognitionException {
        return expression(0);
    }

    private android.databinding.parser.BindingExpressionParser.ExpressionContext expression(int _p) throws android.databinding.parser.RecognitionException {
        android.databinding.parser.ParserRuleContext _parentctx = _ctx;
        int _parentState = getState();
        android.databinding.parser.BindingExpressionParser.ExpressionContext _localctx = new android.databinding.parser.BindingExpressionParser.ExpressionContext(_ctx, _parentState);
        android.databinding.parser.BindingExpressionParser.ExpressionContext _prevctx = _localctx;
        int _startState = 6;
        enterRecursionRule(_localctx, 6, android.databinding.parser.BindingExpressionParser.RULE_expression, _p);
        int _la;
        try {
            int _alt;
            enterOuterAlt(_localctx, 1);
            {
                setState(70);
                switch (getInterpreter().adaptivePredict(_input, 2, _ctx)) {
                    case 1 :
                        {
                            _localctx = new android.databinding.parser.BindingExpressionParser.CastOpContext(_localctx);
                            _ctx = _localctx;
                            _prevctx = _localctx;
                            setState(53);
                            match(android.databinding.parser.BindingExpressionParser.T__3);
                            setState(54);
                            type();
                            setState(55);
                            match(android.databinding.parser.BindingExpressionParser.T__4);
                            setState(56);
                            expression(16);
                        }
                        break;
                    case 2 :
                        {
                            _localctx = new android.databinding.parser.BindingExpressionParser.UnaryOpContext(_localctx);
                            _ctx = _localctx;
                            _prevctx = _localctx;
                            setState(58);
                            ((android.databinding.parser.BindingExpressionParser.UnaryOpContext) (_localctx)).op = _input.LT(1);
                            _la = _input.LA(1);
                            if (!((_la == android.databinding.parser.BindingExpressionParser.T__8) || (_la == android.databinding.parser.BindingExpressionParser.T__9))) {
                                ((android.databinding.parser.BindingExpressionParser.UnaryOpContext) (_localctx)).op = _errHandler.recoverInline(this);
                            } else {
                                consume();
                            }
                            setState(59);
                            expression(15);
                        }
                        break;
                    case 3 :
                        {
                            _localctx = new android.databinding.parser.BindingExpressionParser.UnaryOpContext(_localctx);
                            _ctx = _localctx;
                            _prevctx = _localctx;
                            setState(60);
                            ((android.databinding.parser.BindingExpressionParser.UnaryOpContext) (_localctx)).op = _input.LT(1);
                            _la = _input.LA(1);
                            if (!((_la == android.databinding.parser.BindingExpressionParser.T__10) || (_la == android.databinding.parser.BindingExpressionParser.T__11))) {
                                ((android.databinding.parser.BindingExpressionParser.UnaryOpContext) (_localctx)).op = _errHandler.recoverInline(this);
                            } else {
                                consume();
                            }
                            setState(61);
                            expression(14);
                        }
                        break;
                    case 4 :
                        {
                            _localctx = new android.databinding.parser.BindingExpressionParser.GroupingContext(_localctx);
                            _ctx = _localctx;
                            _prevctx = _localctx;
                            setState(62);
                            match(android.databinding.parser.BindingExpressionParser.T__3);
                            setState(63);
                            expression(0);
                            setState(64);
                            match(android.databinding.parser.BindingExpressionParser.T__4);
                        }
                        break;
                    case 5 :
                        {
                            _localctx = new android.databinding.parser.BindingExpressionParser.PrimaryContext(_localctx);
                            _ctx = _localctx;
                            _prevctx = _localctx;
                            setState(66);
                            literal();
                        }
                        break;
                    case 6 :
                        {
                            _localctx = new android.databinding.parser.BindingExpressionParser.PrimaryContext(_localctx);
                            _ctx = _localctx;
                            _prevctx = _localctx;
                            setState(67);
                            identifier();
                        }
                        break;
                    case 7 :
                        {
                            _localctx = new android.databinding.parser.BindingExpressionParser.PrimaryContext(_localctx);
                            _ctx = _localctx;
                            _prevctx = _localctx;
                            setState(68);
                            classExtraction();
                        }
                        break;
                    case 8 :
                        {
                            _localctx = new android.databinding.parser.BindingExpressionParser.ResourceContext(_localctx);
                            _ctx = _localctx;
                            _prevctx = _localctx;
                            setState(69);
                            resources();
                        }
                        break;
                }
                _ctx.stop = _input.LT(-1);
                setState(132);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input, 5, _ctx);
                while ((_alt != 2) && (_alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER)) {
                    if (_alt == 1) {
                        if (_parseListeners != null)
                            triggerExitRuleEvent();

                        _prevctx = _localctx;
                        {
                            setState(130);
                            switch (getInterpreter().adaptivePredict(_input, 4, _ctx)) {
                                case 1 :
                                    {
                                        _localctx = new android.databinding.parser.BindingExpressionParser.MathOpContext(new android.databinding.parser.BindingExpressionParser.ExpressionContext(_parentctx, _parentState));
                                        ((android.databinding.parser.BindingExpressionParser.MathOpContext) (_localctx)).left = _prevctx;
                                        pushNewRecursionContext(_localctx, _startState, android.databinding.parser.BindingExpressionParser.RULE_expression);
                                        setState(72);
                                        if (!precpred(_ctx, 13))
                                            throw new android.databinding.parser.FailedPredicateException(this, "precpred(_ctx, 13)");

                                        setState(73);
                                        ((android.databinding.parser.BindingExpressionParser.MathOpContext) (_localctx)).op = _input.LT(1);
                                        _la = _input.LA(1);
                                        if (!(((_la & (~0x3f)) == 0) && (((1L << _la) & (((1L << android.databinding.parser.BindingExpressionParser.T__12) | (1L << android.databinding.parser.BindingExpressionParser.T__13)) | (1L << android.databinding.parser.BindingExpressionParser.T__14))) != 0))) {
                                            ((android.databinding.parser.BindingExpressionParser.MathOpContext) (_localctx)).op = _errHandler.recoverInline(this);
                                        } else {
                                            consume();
                                        }
                                        setState(74);
                                        ((android.databinding.parser.BindingExpressionParser.MathOpContext) (_localctx)).right = expression(14);
                                    }
                                    break;
                                case 2 :
                                    {
                                        _localctx = new android.databinding.parser.BindingExpressionParser.MathOpContext(new android.databinding.parser.BindingExpressionParser.ExpressionContext(_parentctx, _parentState));
                                        ((android.databinding.parser.BindingExpressionParser.MathOpContext) (_localctx)).left = _prevctx;
                                        pushNewRecursionContext(_localctx, _startState, android.databinding.parser.BindingExpressionParser.RULE_expression);
                                        setState(75);
                                        if (!precpred(_ctx, 12))
                                            throw new android.databinding.parser.FailedPredicateException(this, "precpred(_ctx, 12)");

                                        setState(76);
                                        ((android.databinding.parser.BindingExpressionParser.MathOpContext) (_localctx)).op = _input.LT(1);
                                        _la = _input.LA(1);
                                        if (!((_la == android.databinding.parser.BindingExpressionParser.T__8) || (_la == android.databinding.parser.BindingExpressionParser.T__9))) {
                                            ((android.databinding.parser.BindingExpressionParser.MathOpContext) (_localctx)).op = _errHandler.recoverInline(this);
                                        } else {
                                            consume();
                                        }
                                        setState(77);
                                        ((android.databinding.parser.BindingExpressionParser.MathOpContext) (_localctx)).right = expression(13);
                                    }
                                    break;
                                case 3 :
                                    {
                                        _localctx = new android.databinding.parser.BindingExpressionParser.BitShiftOpContext(new android.databinding.parser.BindingExpressionParser.ExpressionContext(_parentctx, _parentState));
                                        ((android.databinding.parser.BindingExpressionParser.BitShiftOpContext) (_localctx)).left = _prevctx;
                                        pushNewRecursionContext(_localctx, _startState, android.databinding.parser.BindingExpressionParser.RULE_expression);
                                        setState(78);
                                        if (!precpred(_ctx, 11))
                                            throw new android.databinding.parser.FailedPredicateException(this, "precpred(_ctx, 11)");

                                        setState(79);
                                        ((android.databinding.parser.BindingExpressionParser.BitShiftOpContext) (_localctx)).op = _input.LT(1);
                                        _la = _input.LA(1);
                                        if (!(((_la & (~0x3f)) == 0) && (((1L << _la) & (((1L << android.databinding.parser.BindingExpressionParser.T__15) | (1L << android.databinding.parser.BindingExpressionParser.T__16)) | (1L << android.databinding.parser.BindingExpressionParser.T__17))) != 0))) {
                                            ((android.databinding.parser.BindingExpressionParser.BitShiftOpContext) (_localctx)).op = _errHandler.recoverInline(this);
                                        } else {
                                            consume();
                                        }
                                        setState(80);
                                        ((android.databinding.parser.BindingExpressionParser.BitShiftOpContext) (_localctx)).right = expression(12);
                                    }
                                    break;
                                case 4 :
                                    {
                                        _localctx = new android.databinding.parser.BindingExpressionParser.ComparisonOpContext(new android.databinding.parser.BindingExpressionParser.ExpressionContext(_parentctx, _parentState));
                                        ((android.databinding.parser.BindingExpressionParser.ComparisonOpContext) (_localctx)).left = _prevctx;
                                        pushNewRecursionContext(_localctx, _startState, android.databinding.parser.BindingExpressionParser.RULE_expression);
                                        setState(81);
                                        if (!precpred(_ctx, 10))
                                            throw new android.databinding.parser.FailedPredicateException(this, "precpred(_ctx, 10)");

                                        setState(82);
                                        ((android.databinding.parser.BindingExpressionParser.ComparisonOpContext) (_localctx)).op = _input.LT(1);
                                        _la = _input.LA(1);
                                        if (!(((_la & (~0x3f)) == 0) && (((1L << _la) & ((((1L << android.databinding.parser.BindingExpressionParser.T__18) | (1L << android.databinding.parser.BindingExpressionParser.T__19)) | (1L << android.databinding.parser.BindingExpressionParser.T__20)) | (1L << android.databinding.parser.BindingExpressionParser.T__21))) != 0))) {
                                            ((android.databinding.parser.BindingExpressionParser.ComparisonOpContext) (_localctx)).op = _errHandler.recoverInline(this);
                                        } else {
                                            consume();
                                        }
                                        setState(83);
                                        ((android.databinding.parser.BindingExpressionParser.ComparisonOpContext) (_localctx)).right = expression(11);
                                    }
                                    break;
                                case 5 :
                                    {
                                        _localctx = new android.databinding.parser.BindingExpressionParser.ComparisonOpContext(new android.databinding.parser.BindingExpressionParser.ExpressionContext(_parentctx, _parentState));
                                        ((android.databinding.parser.BindingExpressionParser.ComparisonOpContext) (_localctx)).left = _prevctx;
                                        pushNewRecursionContext(_localctx, _startState, android.databinding.parser.BindingExpressionParser.RULE_expression);
                                        setState(84);
                                        if (!precpred(_ctx, 8))
                                            throw new android.databinding.parser.FailedPredicateException(this, "precpred(_ctx, 8)");

                                        setState(85);
                                        ((android.databinding.parser.BindingExpressionParser.ComparisonOpContext) (_localctx)).op = _input.LT(1);
                                        _la = _input.LA(1);
                                        if (!((_la == android.databinding.parser.BindingExpressionParser.T__23) || (_la == android.databinding.parser.BindingExpressionParser.T__24))) {
                                            ((android.databinding.parser.BindingExpressionParser.ComparisonOpContext) (_localctx)).op = _errHandler.recoverInline(this);
                                        } else {
                                            consume();
                                        }
                                        setState(86);
                                        ((android.databinding.parser.BindingExpressionParser.ComparisonOpContext) (_localctx)).right = expression(9);
                                    }
                                    break;
                                case 6 :
                                    {
                                        _localctx = new android.databinding.parser.BindingExpressionParser.BinaryOpContext(new android.databinding.parser.BindingExpressionParser.ExpressionContext(_parentctx, _parentState));
                                        ((android.databinding.parser.BindingExpressionParser.BinaryOpContext) (_localctx)).left = _prevctx;
                                        pushNewRecursionContext(_localctx, _startState, android.databinding.parser.BindingExpressionParser.RULE_expression);
                                        setState(87);
                                        if (!precpred(_ctx, 7))
                                            throw new android.databinding.parser.FailedPredicateException(this, "precpred(_ctx, 7)");

                                        setState(88);
                                        ((android.databinding.parser.BindingExpressionParser.BinaryOpContext) (_localctx)).op = match(android.databinding.parser.BindingExpressionParser.T__25);
                                        setState(89);
                                        ((android.databinding.parser.BindingExpressionParser.BinaryOpContext) (_localctx)).right = expression(8);
                                    }
                                    break;
                                case 7 :
                                    {
                                        _localctx = new android.databinding.parser.BindingExpressionParser.BinaryOpContext(new android.databinding.parser.BindingExpressionParser.ExpressionContext(_parentctx, _parentState));
                                        ((android.databinding.parser.BindingExpressionParser.BinaryOpContext) (_localctx)).left = _prevctx;
                                        pushNewRecursionContext(_localctx, _startState, android.databinding.parser.BindingExpressionParser.RULE_expression);
                                        setState(90);
                                        if (!precpred(_ctx, 6))
                                            throw new android.databinding.parser.FailedPredicateException(this, "precpred(_ctx, 6)");

                                        setState(91);
                                        ((android.databinding.parser.BindingExpressionParser.BinaryOpContext) (_localctx)).op = match(android.databinding.parser.BindingExpressionParser.T__26);
                                        setState(92);
                                        ((android.databinding.parser.BindingExpressionParser.BinaryOpContext) (_localctx)).right = expression(7);
                                    }
                                    break;
                                case 8 :
                                    {
                                        _localctx = new android.databinding.parser.BindingExpressionParser.BinaryOpContext(new android.databinding.parser.BindingExpressionParser.ExpressionContext(_parentctx, _parentState));
                                        ((android.databinding.parser.BindingExpressionParser.BinaryOpContext) (_localctx)).left = _prevctx;
                                        pushNewRecursionContext(_localctx, _startState, android.databinding.parser.BindingExpressionParser.RULE_expression);
                                        setState(93);
                                        if (!precpred(_ctx, 5))
                                            throw new android.databinding.parser.FailedPredicateException(this, "precpred(_ctx, 5)");

                                        setState(94);
                                        ((android.databinding.parser.BindingExpressionParser.BinaryOpContext) (_localctx)).op = match(android.databinding.parser.BindingExpressionParser.T__27);
                                        setState(95);
                                        ((android.databinding.parser.BindingExpressionParser.BinaryOpContext) (_localctx)).right = expression(6);
                                    }
                                    break;
                                case 9 :
                                    {
                                        _localctx = new android.databinding.parser.BindingExpressionParser.AndOrOpContext(new android.databinding.parser.BindingExpressionParser.ExpressionContext(_parentctx, _parentState));
                                        ((android.databinding.parser.BindingExpressionParser.AndOrOpContext) (_localctx)).left = _prevctx;
                                        pushNewRecursionContext(_localctx, _startState, android.databinding.parser.BindingExpressionParser.RULE_expression);
                                        setState(96);
                                        if (!precpred(_ctx, 4))
                                            throw new android.databinding.parser.FailedPredicateException(this, "precpred(_ctx, 4)");

                                        setState(97);
                                        ((android.databinding.parser.BindingExpressionParser.AndOrOpContext) (_localctx)).op = match(android.databinding.parser.BindingExpressionParser.T__28);
                                        setState(98);
                                        ((android.databinding.parser.BindingExpressionParser.AndOrOpContext) (_localctx)).right = expression(5);
                                    }
                                    break;
                                case 10 :
                                    {
                                        _localctx = new android.databinding.parser.BindingExpressionParser.AndOrOpContext(new android.databinding.parser.BindingExpressionParser.ExpressionContext(_parentctx, _parentState));
                                        ((android.databinding.parser.BindingExpressionParser.AndOrOpContext) (_localctx)).left = _prevctx;
                                        pushNewRecursionContext(_localctx, _startState, android.databinding.parser.BindingExpressionParser.RULE_expression);
                                        setState(99);
                                        if (!precpred(_ctx, 3))
                                            throw new android.databinding.parser.FailedPredicateException(this, "precpred(_ctx, 3)");

                                        setState(100);
                                        ((android.databinding.parser.BindingExpressionParser.AndOrOpContext) (_localctx)).op = match(android.databinding.parser.BindingExpressionParser.T__29);
                                        setState(101);
                                        ((android.databinding.parser.BindingExpressionParser.AndOrOpContext) (_localctx)).right = expression(4);
                                    }
                                    break;
                                case 11 :
                                    {
                                        _localctx = new android.databinding.parser.BindingExpressionParser.TernaryOpContext(new android.databinding.parser.BindingExpressionParser.ExpressionContext(_parentctx, _parentState));
                                        ((android.databinding.parser.BindingExpressionParser.TernaryOpContext) (_localctx)).left = _prevctx;
                                        pushNewRecursionContext(_localctx, _startState, android.databinding.parser.BindingExpressionParser.RULE_expression);
                                        setState(102);
                                        if (!precpred(_ctx, 2))
                                            throw new android.databinding.parser.FailedPredicateException(this, "precpred(_ctx, 2)");

                                        setState(103);
                                        ((android.databinding.parser.BindingExpressionParser.TernaryOpContext) (_localctx)).op = match(android.databinding.parser.BindingExpressionParser.T__30);
                                        setState(104);
                                        ((android.databinding.parser.BindingExpressionParser.TernaryOpContext) (_localctx)).iftrue = expression(0);
                                        setState(105);
                                        match(android.databinding.parser.BindingExpressionParser.T__31);
                                        setState(106);
                                        ((android.databinding.parser.BindingExpressionParser.TernaryOpContext) (_localctx)).iffalse = expression(2);
                                    }
                                    break;
                                case 12 :
                                    {
                                        _localctx = new android.databinding.parser.BindingExpressionParser.QuestionQuestionOpContext(new android.databinding.parser.BindingExpressionParser.ExpressionContext(_parentctx, _parentState));
                                        ((android.databinding.parser.BindingExpressionParser.QuestionQuestionOpContext) (_localctx)).left = _prevctx;
                                        pushNewRecursionContext(_localctx, _startState, android.databinding.parser.BindingExpressionParser.RULE_expression);
                                        setState(108);
                                        if (!precpred(_ctx, 1))
                                            throw new android.databinding.parser.FailedPredicateException(this, "precpred(_ctx, 1)");

                                        setState(109);
                                        ((android.databinding.parser.BindingExpressionParser.QuestionQuestionOpContext) (_localctx)).op = match(android.databinding.parser.BindingExpressionParser.T__32);
                                        setState(110);
                                        ((android.databinding.parser.BindingExpressionParser.QuestionQuestionOpContext) (_localctx)).right = expression(2);
                                    }
                                    break;
                                case 13 :
                                    {
                                        _localctx = new android.databinding.parser.BindingExpressionParser.DotOpContext(new android.databinding.parser.BindingExpressionParser.ExpressionContext(_parentctx, _parentState));
                                        pushNewRecursionContext(_localctx, _startState, android.databinding.parser.BindingExpressionParser.RULE_expression);
                                        setState(111);
                                        if (!precpred(_ctx, 19))
                                            throw new android.databinding.parser.FailedPredicateException(this, "precpred(_ctx, 19)");

                                        setState(112);
                                        match(android.databinding.parser.BindingExpressionParser.T__5);
                                        setState(113);
                                        match(android.databinding.parser.BindingExpressionParser.Identifier);
                                    }
                                    break;
                                case 14 :
                                    {
                                        _localctx = new android.databinding.parser.BindingExpressionParser.BracketOpContext(new android.databinding.parser.BindingExpressionParser.ExpressionContext(_parentctx, _parentState));
                                        pushNewRecursionContext(_localctx, _startState, android.databinding.parser.BindingExpressionParser.RULE_expression);
                                        setState(114);
                                        if (!precpred(_ctx, 18))
                                            throw new android.databinding.parser.FailedPredicateException(this, "precpred(_ctx, 18)");

                                        setState(115);
                                        match(android.databinding.parser.BindingExpressionParser.T__6);
                                        setState(116);
                                        expression(0);
                                        setState(117);
                                        match(android.databinding.parser.BindingExpressionParser.T__7);
                                    }
                                    break;
                                case 15 :
                                    {
                                        _localctx = new android.databinding.parser.BindingExpressionParser.MethodInvocationContext(new android.databinding.parser.BindingExpressionParser.ExpressionContext(_parentctx, _parentState));
                                        ((android.databinding.parser.BindingExpressionParser.MethodInvocationContext) (_localctx)).target = _prevctx;
                                        pushNewRecursionContext(_localctx, _startState, android.databinding.parser.BindingExpressionParser.RULE_expression);
                                        setState(119);
                                        if (!precpred(_ctx, 17))
                                            throw new android.databinding.parser.FailedPredicateException(this, "precpred(_ctx, 17)");

                                        setState(120);
                                        match(android.databinding.parser.BindingExpressionParser.T__5);
                                        setState(121);
                                        ((android.databinding.parser.BindingExpressionParser.MethodInvocationContext) (_localctx)).methodName = match(android.databinding.parser.BindingExpressionParser.Identifier);
                                        setState(122);
                                        match(android.databinding.parser.BindingExpressionParser.T__3);
                                        setState(124);
                                        _la = _input.LA(1);
                                        if (((_la & (~0x3f)) == 0) && (((1L << _la) & (((((((((((((((((((((((1L << android.databinding.parser.BindingExpressionParser.T__3) | (1L << android.databinding.parser.BindingExpressionParser.T__8)) | (1L << android.databinding.parser.BindingExpressionParser.T__9)) | (1L << android.databinding.parser.BindingExpressionParser.T__10)) | (1L << android.databinding.parser.BindingExpressionParser.T__11)) | (1L << android.databinding.parser.BindingExpressionParser.T__34)) | (1L << android.databinding.parser.BindingExpressionParser.T__35)) | (1L << android.databinding.parser.BindingExpressionParser.T__36)) | (1L << android.databinding.parser.BindingExpressionParser.T__37)) | (1L << android.databinding.parser.BindingExpressionParser.T__38)) | (1L << android.databinding.parser.BindingExpressionParser.T__39)) | (1L << android.databinding.parser.BindingExpressionParser.T__40)) | (1L << android.databinding.parser.BindingExpressionParser.T__41)) | (1L << android.databinding.parser.BindingExpressionParser.T__42)) | (1L << android.databinding.parser.BindingExpressionParser.IntegerLiteral)) | (1L << android.databinding.parser.BindingExpressionParser.FloatingPointLiteral)) | (1L << android.databinding.parser.BindingExpressionParser.BooleanLiteral)) | (1L << android.databinding.parser.BindingExpressionParser.CharacterLiteral)) | (1L << android.databinding.parser.BindingExpressionParser.SingleQuoteString)) | (1L << android.databinding.parser.BindingExpressionParser.DoubleQuoteString)) | (1L << android.databinding.parser.BindingExpressionParser.NullLiteral)) | (1L << android.databinding.parser.BindingExpressionParser.Identifier)) | (1L << android.databinding.parser.BindingExpressionParser.ResourceReference))) != 0)) {
                                            {
                                                setState(123);
                                                ((android.databinding.parser.BindingExpressionParser.MethodInvocationContext) (_localctx)).args = expressionList();
                                            }
                                        }
                                        setState(126);
                                        match(android.databinding.parser.BindingExpressionParser.T__4);
                                    }
                                    break;
                                case 16 :
                                    {
                                        _localctx = new android.databinding.parser.BindingExpressionParser.InstanceOfOpContext(new android.databinding.parser.BindingExpressionParser.ExpressionContext(_parentctx, _parentState));
                                        pushNewRecursionContext(_localctx, _startState, android.databinding.parser.BindingExpressionParser.RULE_expression);
                                        setState(127);
                                        if (!precpred(_ctx, 9))
                                            throw new android.databinding.parser.FailedPredicateException(this, "precpred(_ctx, 9)");

                                        setState(128);
                                        match(android.databinding.parser.BindingExpressionParser.T__22);
                                        setState(129);
                                        type();
                                    }
                                    break;
                            }
                        }
                    }
                    setState(134);
                    _errHandler.sync(this);
                    _alt = getInterpreter().adaptivePredict(_input, 5, _ctx);
                } 
            }
        } catch (android.databinding.parser.RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            unrollRecursionContexts(_parentctx);
        }
        return _localctx;
    }

    public static class ClassExtractionContext extends android.databinding.parser.ParserRuleContext {
        public android.databinding.parser.BindingExpressionParser.TypeContext type() {
            return getRuleContext(android.databinding.parser.BindingExpressionParser.TypeContext.class, 0);
        }

        public ClassExtractionContext(android.databinding.parser.ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @java.lang.Override
        public int getRuleIndex() {
            return RULE_classExtraction;
        }

        @java.lang.Override
        public void enterRule(android.databinding.parser.ParseTreeListener listener) {
            if (listener instanceof android.databinding.parser.BindingExpressionListener)
                ((android.databinding.parser.BindingExpressionListener) (listener)).enterClassExtraction(this);

        }

        @java.lang.Override
        public void exitRule(android.databinding.parser.ParseTreeListener listener) {
            if (listener instanceof android.databinding.parser.BindingExpressionListener)
                ((android.databinding.parser.BindingExpressionListener) (listener)).exitClassExtraction(this);

        }

        @java.lang.Override
        public <Result> Result accept(android.databinding.parser.ParseTreeVisitor<? extends Result> visitor) {
            if (visitor instanceof android.databinding.parser.BindingExpressionVisitor<?>)
                return ((android.databinding.parser.BindingExpressionVisitor<? extends Result>) (visitor)).visitClassExtraction(this);
            else
                return visitor.visitChildren(this);

        }
    }

    @android.databinding.parser.RuleVersion(0)
    public final android.databinding.parser.BindingExpressionParser.ClassExtractionContext classExtraction() throws android.databinding.parser.RecognitionException {
        android.databinding.parser.BindingExpressionParser.ClassExtractionContext _localctx = new android.databinding.parser.BindingExpressionParser.ClassExtractionContext(_ctx, getState());
        enterRule(_localctx, 8, RULE_classExtraction);
        try {
            setState(142);
            switch (_input.LA(1)) {
                case android.databinding.parser.BindingExpressionParser.T__35 :
                case android.databinding.parser.BindingExpressionParser.T__36 :
                case android.databinding.parser.BindingExpressionParser.T__37 :
                case android.databinding.parser.BindingExpressionParser.T__38 :
                case android.databinding.parser.BindingExpressionParser.T__39 :
                case android.databinding.parser.BindingExpressionParser.T__40 :
                case android.databinding.parser.BindingExpressionParser.T__41 :
                case android.databinding.parser.BindingExpressionParser.T__42 :
                case android.databinding.parser.BindingExpressionParser.Identifier :
                    enterOuterAlt(_localctx, 1);
                    {
                        setState(135);
                        type();
                        setState(136);
                        match(android.databinding.parser.BindingExpressionParser.T__5);
                        setState(137);
                        match(android.databinding.parser.BindingExpressionParser.T__33);
                    }
                    break;
                case android.databinding.parser.BindingExpressionParser.T__34 :
                    enterOuterAlt(_localctx, 2);
                    {
                        setState(139);
                        match(android.databinding.parser.BindingExpressionParser.T__34);
                        setState(140);
                        match(android.databinding.parser.BindingExpressionParser.T__5);
                        setState(141);
                        match(android.databinding.parser.BindingExpressionParser.T__33);
                    }
                    break;
                default :
                    throw new android.databinding.parser.NoViableAltException(this);
            }
        } catch (android.databinding.parser.RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class ExpressionListContext extends android.databinding.parser.ParserRuleContext {
        public java.util.List<? extends android.databinding.parser.BindingExpressionParser.ExpressionContext> expression() {
            return getRuleContexts(android.databinding.parser.BindingExpressionParser.ExpressionContext.class);
        }

        public android.databinding.parser.BindingExpressionParser.ExpressionContext expression(int i) {
            return getRuleContext(android.databinding.parser.BindingExpressionParser.ExpressionContext.class, i);
        }

        public ExpressionListContext(android.databinding.parser.ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @java.lang.Override
        public int getRuleIndex() {
            return RULE_expressionList;
        }

        @java.lang.Override
        public void enterRule(android.databinding.parser.ParseTreeListener listener) {
            if (listener instanceof android.databinding.parser.BindingExpressionListener)
                ((android.databinding.parser.BindingExpressionListener) (listener)).enterExpressionList(this);

        }

        @java.lang.Override
        public void exitRule(android.databinding.parser.ParseTreeListener listener) {
            if (listener instanceof android.databinding.parser.BindingExpressionListener)
                ((android.databinding.parser.BindingExpressionListener) (listener)).exitExpressionList(this);

        }

        @java.lang.Override
        public <Result> Result accept(android.databinding.parser.ParseTreeVisitor<? extends Result> visitor) {
            if (visitor instanceof android.databinding.parser.BindingExpressionVisitor<?>)
                return ((android.databinding.parser.BindingExpressionVisitor<? extends Result>) (visitor)).visitExpressionList(this);
            else
                return visitor.visitChildren(this);

        }
    }

    @android.databinding.parser.RuleVersion(0)
    public final android.databinding.parser.BindingExpressionParser.ExpressionListContext expressionList() throws android.databinding.parser.RecognitionException {
        android.databinding.parser.BindingExpressionParser.ExpressionListContext _localctx = new android.databinding.parser.BindingExpressionParser.ExpressionListContext(_ctx, getState());
        enterRule(_localctx, 10, RULE_expressionList);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(144);
                expression(0);
                setState(149);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == android.databinding.parser.BindingExpressionParser.T__0) {
                    {
                        {
                            setState(145);
                            match(android.databinding.parser.BindingExpressionParser.T__0);
                            setState(146);
                            expression(0);
                        }
                    }
                    setState(151);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                } 
            }
        } catch (android.databinding.parser.RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class LiteralContext extends android.databinding.parser.ParserRuleContext {
        public android.databinding.parser.BindingExpressionParser.JavaLiteralContext javaLiteral() {
            return getRuleContext(android.databinding.parser.BindingExpressionParser.JavaLiteralContext.class, 0);
        }

        public android.databinding.parser.BindingExpressionParser.StringLiteralContext stringLiteral() {
            return getRuleContext(android.databinding.parser.BindingExpressionParser.StringLiteralContext.class, 0);
        }

        public LiteralContext(android.databinding.parser.ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @java.lang.Override
        public int getRuleIndex() {
            return RULE_literal;
        }

        @java.lang.Override
        public void enterRule(android.databinding.parser.ParseTreeListener listener) {
            if (listener instanceof android.databinding.parser.BindingExpressionListener)
                ((android.databinding.parser.BindingExpressionListener) (listener)).enterLiteral(this);

        }

        @java.lang.Override
        public void exitRule(android.databinding.parser.ParseTreeListener listener) {
            if (listener instanceof android.databinding.parser.BindingExpressionListener)
                ((android.databinding.parser.BindingExpressionListener) (listener)).exitLiteral(this);

        }

        @java.lang.Override
        public <Result> Result accept(android.databinding.parser.ParseTreeVisitor<? extends Result> visitor) {
            if (visitor instanceof android.databinding.parser.BindingExpressionVisitor<?>)
                return ((android.databinding.parser.BindingExpressionVisitor<? extends Result>) (visitor)).visitLiteral(this);
            else
                return visitor.visitChildren(this);

        }
    }

    @android.databinding.parser.RuleVersion(0)
    public final android.databinding.parser.BindingExpressionParser.LiteralContext literal() throws android.databinding.parser.RecognitionException {
        android.databinding.parser.BindingExpressionParser.LiteralContext _localctx = new android.databinding.parser.BindingExpressionParser.LiteralContext(_ctx, getState());
        enterRule(_localctx, 12, RULE_literal);
        try {
            setState(154);
            switch (_input.LA(1)) {
                case android.databinding.parser.BindingExpressionParser.IntegerLiteral :
                case android.databinding.parser.BindingExpressionParser.FloatingPointLiteral :
                case android.databinding.parser.BindingExpressionParser.BooleanLiteral :
                case android.databinding.parser.BindingExpressionParser.CharacterLiteral :
                case android.databinding.parser.BindingExpressionParser.NullLiteral :
                    enterOuterAlt(_localctx, 1);
                    {
                        setState(152);
                        javaLiteral();
                    }
                    break;
                case android.databinding.parser.BindingExpressionParser.SingleQuoteString :
                case android.databinding.parser.BindingExpressionParser.DoubleQuoteString :
                    enterOuterAlt(_localctx, 2);
                    {
                        setState(153);
                        stringLiteral();
                    }
                    break;
                default :
                    throw new android.databinding.parser.NoViableAltException(this);
            }
        } catch (android.databinding.parser.RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class IdentifierContext extends android.databinding.parser.ParserRuleContext {
        public android.databinding.parser.TerminalNode Identifier() {
            return getToken(android.databinding.parser.BindingExpressionParser.Identifier, 0);
        }

        public IdentifierContext(android.databinding.parser.ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @java.lang.Override
        public int getRuleIndex() {
            return RULE_identifier;
        }

        @java.lang.Override
        public void enterRule(android.databinding.parser.ParseTreeListener listener) {
            if (listener instanceof android.databinding.parser.BindingExpressionListener)
                ((android.databinding.parser.BindingExpressionListener) (listener)).enterIdentifier(this);

        }

        @java.lang.Override
        public void exitRule(android.databinding.parser.ParseTreeListener listener) {
            if (listener instanceof android.databinding.parser.BindingExpressionListener)
                ((android.databinding.parser.BindingExpressionListener) (listener)).exitIdentifier(this);

        }

        @java.lang.Override
        public <Result> Result accept(android.databinding.parser.ParseTreeVisitor<? extends Result> visitor) {
            if (visitor instanceof android.databinding.parser.BindingExpressionVisitor<?>)
                return ((android.databinding.parser.BindingExpressionVisitor<? extends Result>) (visitor)).visitIdentifier(this);
            else
                return visitor.visitChildren(this);

        }
    }

    @android.databinding.parser.RuleVersion(0)
    public final android.databinding.parser.BindingExpressionParser.IdentifierContext identifier() throws android.databinding.parser.RecognitionException {
        android.databinding.parser.BindingExpressionParser.IdentifierContext _localctx = new android.databinding.parser.BindingExpressionParser.IdentifierContext(_ctx, getState());
        enterRule(_localctx, 14, RULE_identifier);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(156);
                match(android.databinding.parser.BindingExpressionParser.Identifier);
            }
        } catch (android.databinding.parser.RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class JavaLiteralContext extends android.databinding.parser.ParserRuleContext {
        public android.databinding.parser.TerminalNode IntegerLiteral() {
            return getToken(android.databinding.parser.BindingExpressionParser.IntegerLiteral, 0);
        }

        public android.databinding.parser.TerminalNode FloatingPointLiteral() {
            return getToken(android.databinding.parser.BindingExpressionParser.FloatingPointLiteral, 0);
        }

        public android.databinding.parser.TerminalNode BooleanLiteral() {
            return getToken(android.databinding.parser.BindingExpressionParser.BooleanLiteral, 0);
        }

        public android.databinding.parser.TerminalNode NullLiteral() {
            return getToken(android.databinding.parser.BindingExpressionParser.NullLiteral, 0);
        }

        public android.databinding.parser.TerminalNode CharacterLiteral() {
            return getToken(android.databinding.parser.BindingExpressionParser.CharacterLiteral, 0);
        }

        public JavaLiteralContext(android.databinding.parser.ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @java.lang.Override
        public int getRuleIndex() {
            return RULE_javaLiteral;
        }

        @java.lang.Override
        public void enterRule(android.databinding.parser.ParseTreeListener listener) {
            if (listener instanceof android.databinding.parser.BindingExpressionListener)
                ((android.databinding.parser.BindingExpressionListener) (listener)).enterJavaLiteral(this);

        }

        @java.lang.Override
        public void exitRule(android.databinding.parser.ParseTreeListener listener) {
            if (listener instanceof android.databinding.parser.BindingExpressionListener)
                ((android.databinding.parser.BindingExpressionListener) (listener)).exitJavaLiteral(this);

        }

        @java.lang.Override
        public <Result> Result accept(android.databinding.parser.ParseTreeVisitor<? extends Result> visitor) {
            if (visitor instanceof android.databinding.parser.BindingExpressionVisitor<?>)
                return ((android.databinding.parser.BindingExpressionVisitor<? extends Result>) (visitor)).visitJavaLiteral(this);
            else
                return visitor.visitChildren(this);

        }
    }

    @android.databinding.parser.RuleVersion(0)
    public final android.databinding.parser.BindingExpressionParser.JavaLiteralContext javaLiteral() throws android.databinding.parser.RecognitionException {
        android.databinding.parser.BindingExpressionParser.JavaLiteralContext _localctx = new android.databinding.parser.BindingExpressionParser.JavaLiteralContext(_ctx, getState());
        enterRule(_localctx, 16, RULE_javaLiteral);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(158);
                _la = _input.LA(1);
                if (!(((_la & (~0x3f)) == 0) && (((1L << _la) & (((((1L << android.databinding.parser.BindingExpressionParser.IntegerLiteral) | (1L << android.databinding.parser.BindingExpressionParser.FloatingPointLiteral)) | (1L << android.databinding.parser.BindingExpressionParser.BooleanLiteral)) | (1L << android.databinding.parser.BindingExpressionParser.CharacterLiteral)) | (1L << android.databinding.parser.BindingExpressionParser.NullLiteral))) != 0))) {
                    _errHandler.recoverInline(this);
                } else {
                    consume();
                }
            }
        } catch (android.databinding.parser.RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class StringLiteralContext extends android.databinding.parser.ParserRuleContext {
        public android.databinding.parser.TerminalNode SingleQuoteString() {
            return getToken(android.databinding.parser.BindingExpressionParser.SingleQuoteString, 0);
        }

        public android.databinding.parser.TerminalNode DoubleQuoteString() {
            return getToken(android.databinding.parser.BindingExpressionParser.DoubleQuoteString, 0);
        }

        public StringLiteralContext(android.databinding.parser.ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @java.lang.Override
        public int getRuleIndex() {
            return RULE_stringLiteral;
        }

        @java.lang.Override
        public void enterRule(android.databinding.parser.ParseTreeListener listener) {
            if (listener instanceof android.databinding.parser.BindingExpressionListener)
                ((android.databinding.parser.BindingExpressionListener) (listener)).enterStringLiteral(this);

        }

        @java.lang.Override
        public void exitRule(android.databinding.parser.ParseTreeListener listener) {
            if (listener instanceof android.databinding.parser.BindingExpressionListener)
                ((android.databinding.parser.BindingExpressionListener) (listener)).exitStringLiteral(this);

        }

        @java.lang.Override
        public <Result> Result accept(android.databinding.parser.ParseTreeVisitor<? extends Result> visitor) {
            if (visitor instanceof android.databinding.parser.BindingExpressionVisitor<?>)
                return ((android.databinding.parser.BindingExpressionVisitor<? extends Result>) (visitor)).visitStringLiteral(this);
            else
                return visitor.visitChildren(this);

        }
    }

    @android.databinding.parser.RuleVersion(0)
    public final android.databinding.parser.BindingExpressionParser.StringLiteralContext stringLiteral() throws android.databinding.parser.RecognitionException {
        android.databinding.parser.BindingExpressionParser.StringLiteralContext _localctx = new android.databinding.parser.BindingExpressionParser.StringLiteralContext(_ctx, getState());
        enterRule(_localctx, 18, RULE_stringLiteral);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(160);
                _la = _input.LA(1);
                if (!((_la == android.databinding.parser.BindingExpressionParser.SingleQuoteString) || (_la == android.databinding.parser.BindingExpressionParser.DoubleQuoteString))) {
                    _errHandler.recoverInline(this);
                } else {
                    consume();
                }
            }
        } catch (android.databinding.parser.RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class ExplicitGenericInvocationContext extends android.databinding.parser.ParserRuleContext {
        public android.databinding.parser.BindingExpressionParser.TypeArgumentsContext typeArguments() {
            return getRuleContext(android.databinding.parser.BindingExpressionParser.TypeArgumentsContext.class, 0);
        }

        public android.databinding.parser.BindingExpressionParser.ExplicitGenericInvocationSuffixContext explicitGenericInvocationSuffix() {
            return getRuleContext(android.databinding.parser.BindingExpressionParser.ExplicitGenericInvocationSuffixContext.class, 0);
        }

        public ExplicitGenericInvocationContext(android.databinding.parser.ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @java.lang.Override
        public int getRuleIndex() {
            return RULE_explicitGenericInvocation;
        }

        @java.lang.Override
        public void enterRule(android.databinding.parser.ParseTreeListener listener) {
            if (listener instanceof android.databinding.parser.BindingExpressionListener)
                ((android.databinding.parser.BindingExpressionListener) (listener)).enterExplicitGenericInvocation(this);

        }

        @java.lang.Override
        public void exitRule(android.databinding.parser.ParseTreeListener listener) {
            if (listener instanceof android.databinding.parser.BindingExpressionListener)
                ((android.databinding.parser.BindingExpressionListener) (listener)).exitExplicitGenericInvocation(this);

        }

        @java.lang.Override
        public <Result> Result accept(android.databinding.parser.ParseTreeVisitor<? extends Result> visitor) {
            if (visitor instanceof android.databinding.parser.BindingExpressionVisitor<?>)
                return ((android.databinding.parser.BindingExpressionVisitor<? extends Result>) (visitor)).visitExplicitGenericInvocation(this);
            else
                return visitor.visitChildren(this);

        }
    }

    @android.databinding.parser.RuleVersion(0)
    public final android.databinding.parser.BindingExpressionParser.ExplicitGenericInvocationContext explicitGenericInvocation() throws android.databinding.parser.RecognitionException {
        android.databinding.parser.BindingExpressionParser.ExplicitGenericInvocationContext _localctx = new android.databinding.parser.BindingExpressionParser.ExplicitGenericInvocationContext(_ctx, getState());
        enterRule(_localctx, 20, RULE_explicitGenericInvocation);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(162);
                typeArguments();
                setState(163);
                explicitGenericInvocationSuffix();
            }
        } catch (android.databinding.parser.RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class TypeArgumentsContext extends android.databinding.parser.ParserRuleContext {
        public java.util.List<? extends android.databinding.parser.BindingExpressionParser.TypeContext> type() {
            return getRuleContexts(android.databinding.parser.BindingExpressionParser.TypeContext.class);
        }

        public android.databinding.parser.BindingExpressionParser.TypeContext type(int i) {
            return getRuleContext(android.databinding.parser.BindingExpressionParser.TypeContext.class, i);
        }

        public TypeArgumentsContext(android.databinding.parser.ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @java.lang.Override
        public int getRuleIndex() {
            return RULE_typeArguments;
        }

        @java.lang.Override
        public void enterRule(android.databinding.parser.ParseTreeListener listener) {
            if (listener instanceof android.databinding.parser.BindingExpressionListener)
                ((android.databinding.parser.BindingExpressionListener) (listener)).enterTypeArguments(this);

        }

        @java.lang.Override
        public void exitRule(android.databinding.parser.ParseTreeListener listener) {
            if (listener instanceof android.databinding.parser.BindingExpressionListener)
                ((android.databinding.parser.BindingExpressionListener) (listener)).exitTypeArguments(this);

        }

        @java.lang.Override
        public <Result> Result accept(android.databinding.parser.ParseTreeVisitor<? extends Result> visitor) {
            if (visitor instanceof android.databinding.parser.BindingExpressionVisitor<?>)
                return ((android.databinding.parser.BindingExpressionVisitor<? extends Result>) (visitor)).visitTypeArguments(this);
            else
                return visitor.visitChildren(this);

        }
    }

    @android.databinding.parser.RuleVersion(0)
    public final android.databinding.parser.BindingExpressionParser.TypeArgumentsContext typeArguments() throws android.databinding.parser.RecognitionException {
        android.databinding.parser.BindingExpressionParser.TypeArgumentsContext _localctx = new android.databinding.parser.BindingExpressionParser.TypeArgumentsContext(_ctx, getState());
        enterRule(_localctx, 22, RULE_typeArguments);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(165);
                match(android.databinding.parser.BindingExpressionParser.T__21);
                setState(166);
                type();
                setState(171);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == android.databinding.parser.BindingExpressionParser.T__0) {
                    {
                        {
                            setState(167);
                            match(android.databinding.parser.BindingExpressionParser.T__0);
                            setState(168);
                            type();
                        }
                    }
                    setState(173);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                } 
                setState(174);
                match(android.databinding.parser.BindingExpressionParser.T__20);
            }
        } catch (android.databinding.parser.RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class TypeContext extends android.databinding.parser.ParserRuleContext {
        public android.databinding.parser.BindingExpressionParser.ClassOrInterfaceTypeContext classOrInterfaceType() {
            return getRuleContext(android.databinding.parser.BindingExpressionParser.ClassOrInterfaceTypeContext.class, 0);
        }

        public android.databinding.parser.BindingExpressionParser.PrimitiveTypeContext primitiveType() {
            return getRuleContext(android.databinding.parser.BindingExpressionParser.PrimitiveTypeContext.class, 0);
        }

        public TypeContext(android.databinding.parser.ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @java.lang.Override
        public int getRuleIndex() {
            return RULE_type;
        }

        @java.lang.Override
        public void enterRule(android.databinding.parser.ParseTreeListener listener) {
            if (listener instanceof android.databinding.parser.BindingExpressionListener)
                ((android.databinding.parser.BindingExpressionListener) (listener)).enterType(this);

        }

        @java.lang.Override
        public void exitRule(android.databinding.parser.ParseTreeListener listener) {
            if (listener instanceof android.databinding.parser.BindingExpressionListener)
                ((android.databinding.parser.BindingExpressionListener) (listener)).exitType(this);

        }

        @java.lang.Override
        public <Result> Result accept(android.databinding.parser.ParseTreeVisitor<? extends Result> visitor) {
            if (visitor instanceof android.databinding.parser.BindingExpressionVisitor<?>)
                return ((android.databinding.parser.BindingExpressionVisitor<? extends Result>) (visitor)).visitType(this);
            else
                return visitor.visitChildren(this);

        }
    }

    @android.databinding.parser.RuleVersion(0)
    public final android.databinding.parser.BindingExpressionParser.TypeContext type() throws android.databinding.parser.RecognitionException {
        android.databinding.parser.BindingExpressionParser.TypeContext _localctx = new android.databinding.parser.BindingExpressionParser.TypeContext(_ctx, getState());
        enterRule(_localctx, 24, RULE_type);
        try {
            int _alt;
            setState(192);
            switch (_input.LA(1)) {
                case android.databinding.parser.BindingExpressionParser.Identifier :
                    enterOuterAlt(_localctx, 1);
                    {
                        setState(176);
                        classOrInterfaceType();
                        setState(181);
                        _errHandler.sync(this);
                        _alt = getInterpreter().adaptivePredict(_input, 10, _ctx);
                        while ((_alt != 2) && (_alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER)) {
                            if (_alt == 1) {
                                {
                                    {
                                        setState(177);
                                        match(android.databinding.parser.BindingExpressionParser.T__6);
                                        setState(178);
                                        match(android.databinding.parser.BindingExpressionParser.T__7);
                                    }
                                }
                            }
                            setState(183);
                            _errHandler.sync(this);
                            _alt = getInterpreter().adaptivePredict(_input, 10, _ctx);
                        } 
                    }
                    break;
                case android.databinding.parser.BindingExpressionParser.T__35 :
                case android.databinding.parser.BindingExpressionParser.T__36 :
                case android.databinding.parser.BindingExpressionParser.T__37 :
                case android.databinding.parser.BindingExpressionParser.T__38 :
                case android.databinding.parser.BindingExpressionParser.T__39 :
                case android.databinding.parser.BindingExpressionParser.T__40 :
                case android.databinding.parser.BindingExpressionParser.T__41 :
                case android.databinding.parser.BindingExpressionParser.T__42 :
                    enterOuterAlt(_localctx, 2);
                    {
                        setState(184);
                        primitiveType();
                        setState(189);
                        _errHandler.sync(this);
                        _alt = getInterpreter().adaptivePredict(_input, 11, _ctx);
                        while ((_alt != 2) && (_alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER)) {
                            if (_alt == 1) {
                                {
                                    {
                                        setState(185);
                                        match(android.databinding.parser.BindingExpressionParser.T__6);
                                        setState(186);
                                        match(android.databinding.parser.BindingExpressionParser.T__7);
                                    }
                                }
                            }
                            setState(191);
                            _errHandler.sync(this);
                            _alt = getInterpreter().adaptivePredict(_input, 11, _ctx);
                        } 
                    }
                    break;
                default :
                    throw new android.databinding.parser.NoViableAltException(this);
            }
        } catch (android.databinding.parser.RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class ExplicitGenericInvocationSuffixContext extends android.databinding.parser.ParserRuleContext {
        public android.databinding.parser.TerminalNode Identifier() {
            return getToken(android.databinding.parser.BindingExpressionParser.Identifier, 0);
        }

        public android.databinding.parser.BindingExpressionParser.ArgumentsContext arguments() {
            return getRuleContext(android.databinding.parser.BindingExpressionParser.ArgumentsContext.class, 0);
        }

        public ExplicitGenericInvocationSuffixContext(android.databinding.parser.ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @java.lang.Override
        public int getRuleIndex() {
            return RULE_explicitGenericInvocationSuffix;
        }

        @java.lang.Override
        public void enterRule(android.databinding.parser.ParseTreeListener listener) {
            if (listener instanceof android.databinding.parser.BindingExpressionListener)
                ((android.databinding.parser.BindingExpressionListener) (listener)).enterExplicitGenericInvocationSuffix(this);

        }

        @java.lang.Override
        public void exitRule(android.databinding.parser.ParseTreeListener listener) {
            if (listener instanceof android.databinding.parser.BindingExpressionListener)
                ((android.databinding.parser.BindingExpressionListener) (listener)).exitExplicitGenericInvocationSuffix(this);

        }

        @java.lang.Override
        public <Result> Result accept(android.databinding.parser.ParseTreeVisitor<? extends Result> visitor) {
            if (visitor instanceof android.databinding.parser.BindingExpressionVisitor<?>)
                return ((android.databinding.parser.BindingExpressionVisitor<? extends Result>) (visitor)).visitExplicitGenericInvocationSuffix(this);
            else
                return visitor.visitChildren(this);

        }
    }

    @android.databinding.parser.RuleVersion(0)
    public final android.databinding.parser.BindingExpressionParser.ExplicitGenericInvocationSuffixContext explicitGenericInvocationSuffix() throws android.databinding.parser.RecognitionException {
        android.databinding.parser.BindingExpressionParser.ExplicitGenericInvocationSuffixContext _localctx = new android.databinding.parser.BindingExpressionParser.ExplicitGenericInvocationSuffixContext(_ctx, getState());
        enterRule(_localctx, 26, RULE_explicitGenericInvocationSuffix);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(194);
                match(android.databinding.parser.BindingExpressionParser.Identifier);
                setState(195);
                arguments();
            }
        } catch (android.databinding.parser.RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class ArgumentsContext extends android.databinding.parser.ParserRuleContext {
        public android.databinding.parser.BindingExpressionParser.ExpressionListContext expressionList() {
            return getRuleContext(android.databinding.parser.BindingExpressionParser.ExpressionListContext.class, 0);
        }

        public ArgumentsContext(android.databinding.parser.ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @java.lang.Override
        public int getRuleIndex() {
            return RULE_arguments;
        }

        @java.lang.Override
        public void enterRule(android.databinding.parser.ParseTreeListener listener) {
            if (listener instanceof android.databinding.parser.BindingExpressionListener)
                ((android.databinding.parser.BindingExpressionListener) (listener)).enterArguments(this);

        }

        @java.lang.Override
        public void exitRule(android.databinding.parser.ParseTreeListener listener) {
            if (listener instanceof android.databinding.parser.BindingExpressionListener)
                ((android.databinding.parser.BindingExpressionListener) (listener)).exitArguments(this);

        }

        @java.lang.Override
        public <Result> Result accept(android.databinding.parser.ParseTreeVisitor<? extends Result> visitor) {
            if (visitor instanceof android.databinding.parser.BindingExpressionVisitor<?>)
                return ((android.databinding.parser.BindingExpressionVisitor<? extends Result>) (visitor)).visitArguments(this);
            else
                return visitor.visitChildren(this);

        }
    }

    @android.databinding.parser.RuleVersion(0)
    public final android.databinding.parser.BindingExpressionParser.ArgumentsContext arguments() throws android.databinding.parser.RecognitionException {
        android.databinding.parser.BindingExpressionParser.ArgumentsContext _localctx = new android.databinding.parser.BindingExpressionParser.ArgumentsContext(_ctx, getState());
        enterRule(_localctx, 28, RULE_arguments);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(197);
                match(android.databinding.parser.BindingExpressionParser.T__3);
                setState(199);
                _la = _input.LA(1);
                if (((_la & (~0x3f)) == 0) && (((1L << _la) & (((((((((((((((((((((((1L << android.databinding.parser.BindingExpressionParser.T__3) | (1L << android.databinding.parser.BindingExpressionParser.T__8)) | (1L << android.databinding.parser.BindingExpressionParser.T__9)) | (1L << android.databinding.parser.BindingExpressionParser.T__10)) | (1L << android.databinding.parser.BindingExpressionParser.T__11)) | (1L << android.databinding.parser.BindingExpressionParser.T__34)) | (1L << android.databinding.parser.BindingExpressionParser.T__35)) | (1L << android.databinding.parser.BindingExpressionParser.T__36)) | (1L << android.databinding.parser.BindingExpressionParser.T__37)) | (1L << android.databinding.parser.BindingExpressionParser.T__38)) | (1L << android.databinding.parser.BindingExpressionParser.T__39)) | (1L << android.databinding.parser.BindingExpressionParser.T__40)) | (1L << android.databinding.parser.BindingExpressionParser.T__41)) | (1L << android.databinding.parser.BindingExpressionParser.T__42)) | (1L << android.databinding.parser.BindingExpressionParser.IntegerLiteral)) | (1L << android.databinding.parser.BindingExpressionParser.FloatingPointLiteral)) | (1L << android.databinding.parser.BindingExpressionParser.BooleanLiteral)) | (1L << android.databinding.parser.BindingExpressionParser.CharacterLiteral)) | (1L << android.databinding.parser.BindingExpressionParser.SingleQuoteString)) | (1L << android.databinding.parser.BindingExpressionParser.DoubleQuoteString)) | (1L << android.databinding.parser.BindingExpressionParser.NullLiteral)) | (1L << android.databinding.parser.BindingExpressionParser.Identifier)) | (1L << android.databinding.parser.BindingExpressionParser.ResourceReference))) != 0)) {
                    {
                        setState(198);
                        expressionList();
                    }
                }
                setState(201);
                match(android.databinding.parser.BindingExpressionParser.T__4);
            }
        } catch (android.databinding.parser.RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class ClassOrInterfaceTypeContext extends android.databinding.parser.ParserRuleContext {
        public android.databinding.parser.BindingExpressionParser.IdentifierContext identifier() {
            return getRuleContext(android.databinding.parser.BindingExpressionParser.IdentifierContext.class, 0);
        }

        public java.util.List<? extends android.databinding.parser.BindingExpressionParser.TypeArgumentsContext> typeArguments() {
            return getRuleContexts(android.databinding.parser.BindingExpressionParser.TypeArgumentsContext.class);
        }

        public android.databinding.parser.BindingExpressionParser.TypeArgumentsContext typeArguments(int i) {
            return getRuleContext(android.databinding.parser.BindingExpressionParser.TypeArgumentsContext.class, i);
        }

        public java.util.List<? extends android.databinding.parser.TerminalNode> Identifier() {
            return getTokens(android.databinding.parser.BindingExpressionParser.Identifier);
        }

        public android.databinding.parser.TerminalNode Identifier(int i) {
            return getToken(android.databinding.parser.BindingExpressionParser.Identifier, i);
        }

        public ClassOrInterfaceTypeContext(android.databinding.parser.ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @java.lang.Override
        public int getRuleIndex() {
            return RULE_classOrInterfaceType;
        }

        @java.lang.Override
        public void enterRule(android.databinding.parser.ParseTreeListener listener) {
            if (listener instanceof android.databinding.parser.BindingExpressionListener)
                ((android.databinding.parser.BindingExpressionListener) (listener)).enterClassOrInterfaceType(this);

        }

        @java.lang.Override
        public void exitRule(android.databinding.parser.ParseTreeListener listener) {
            if (listener instanceof android.databinding.parser.BindingExpressionListener)
                ((android.databinding.parser.BindingExpressionListener) (listener)).exitClassOrInterfaceType(this);

        }

        @java.lang.Override
        public <Result> Result accept(android.databinding.parser.ParseTreeVisitor<? extends Result> visitor) {
            if (visitor instanceof android.databinding.parser.BindingExpressionVisitor<?>)
                return ((android.databinding.parser.BindingExpressionVisitor<? extends Result>) (visitor)).visitClassOrInterfaceType(this);
            else
                return visitor.visitChildren(this);

        }
    }

    @android.databinding.parser.RuleVersion(0)
    public final android.databinding.parser.BindingExpressionParser.ClassOrInterfaceTypeContext classOrInterfaceType() throws android.databinding.parser.RecognitionException {
        android.databinding.parser.BindingExpressionParser.ClassOrInterfaceTypeContext _localctx = new android.databinding.parser.BindingExpressionParser.ClassOrInterfaceTypeContext(_ctx, getState());
        enterRule(_localctx, 30, RULE_classOrInterfaceType);
        try {
            int _alt;
            enterOuterAlt(_localctx, 1);
            {
                setState(203);
                identifier();
                setState(205);
                switch (getInterpreter().adaptivePredict(_input, 14, _ctx)) {
                    case 1 :
                        {
                            setState(204);
                            typeArguments();
                        }
                        break;
                }
                setState(214);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input, 16, _ctx);
                while ((_alt != 2) && (_alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER)) {
                    if (_alt == 1) {
                        {
                            {
                                setState(207);
                                match(android.databinding.parser.BindingExpressionParser.T__5);
                                setState(208);
                                match(android.databinding.parser.BindingExpressionParser.Identifier);
                                setState(210);
                                switch (getInterpreter().adaptivePredict(_input, 15, _ctx)) {
                                    case 1 :
                                        {
                                            setState(209);
                                            typeArguments();
                                        }
                                        break;
                                }
                            }
                        }
                    }
                    setState(216);
                    _errHandler.sync(this);
                    _alt = getInterpreter().adaptivePredict(_input, 16, _ctx);
                } 
            }
        } catch (android.databinding.parser.RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class PrimitiveTypeContext extends android.databinding.parser.ParserRuleContext {
        public PrimitiveTypeContext(android.databinding.parser.ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @java.lang.Override
        public int getRuleIndex() {
            return RULE_primitiveType;
        }

        @java.lang.Override
        public void enterRule(android.databinding.parser.ParseTreeListener listener) {
            if (listener instanceof android.databinding.parser.BindingExpressionListener)
                ((android.databinding.parser.BindingExpressionListener) (listener)).enterPrimitiveType(this);

        }

        @java.lang.Override
        public void exitRule(android.databinding.parser.ParseTreeListener listener) {
            if (listener instanceof android.databinding.parser.BindingExpressionListener)
                ((android.databinding.parser.BindingExpressionListener) (listener)).exitPrimitiveType(this);

        }

        @java.lang.Override
        public <Result> Result accept(android.databinding.parser.ParseTreeVisitor<? extends Result> visitor) {
            if (visitor instanceof android.databinding.parser.BindingExpressionVisitor<?>)
                return ((android.databinding.parser.BindingExpressionVisitor<? extends Result>) (visitor)).visitPrimitiveType(this);
            else
                return visitor.visitChildren(this);

        }
    }

    @android.databinding.parser.RuleVersion(0)
    public final android.databinding.parser.BindingExpressionParser.PrimitiveTypeContext primitiveType() throws android.databinding.parser.RecognitionException {
        android.databinding.parser.BindingExpressionParser.PrimitiveTypeContext _localctx = new android.databinding.parser.BindingExpressionParser.PrimitiveTypeContext(_ctx, getState());
        enterRule(_localctx, 32, RULE_primitiveType);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(217);
                _la = _input.LA(1);
                if (!(((_la & (~0x3f)) == 0) && (((1L << _la) & ((((((((1L << android.databinding.parser.BindingExpressionParser.T__35) | (1L << android.databinding.parser.BindingExpressionParser.T__36)) | (1L << android.databinding.parser.BindingExpressionParser.T__37)) | (1L << android.databinding.parser.BindingExpressionParser.T__38)) | (1L << android.databinding.parser.BindingExpressionParser.T__39)) | (1L << android.databinding.parser.BindingExpressionParser.T__40)) | (1L << android.databinding.parser.BindingExpressionParser.T__41)) | (1L << android.databinding.parser.BindingExpressionParser.T__42))) != 0))) {
                    _errHandler.recoverInline(this);
                } else {
                    consume();
                }
            }
        } catch (android.databinding.parser.RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class ResourcesContext extends android.databinding.parser.ParserRuleContext {
        public android.databinding.parser.TerminalNode ResourceReference() {
            return getToken(android.databinding.parser.BindingExpressionParser.ResourceReference, 0);
        }

        public android.databinding.parser.BindingExpressionParser.ResourceParametersContext resourceParameters() {
            return getRuleContext(android.databinding.parser.BindingExpressionParser.ResourceParametersContext.class, 0);
        }

        public ResourcesContext(android.databinding.parser.ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @java.lang.Override
        public int getRuleIndex() {
            return RULE_resources;
        }

        @java.lang.Override
        public void enterRule(android.databinding.parser.ParseTreeListener listener) {
            if (listener instanceof android.databinding.parser.BindingExpressionListener)
                ((android.databinding.parser.BindingExpressionListener) (listener)).enterResources(this);

        }

        @java.lang.Override
        public void exitRule(android.databinding.parser.ParseTreeListener listener) {
            if (listener instanceof android.databinding.parser.BindingExpressionListener)
                ((android.databinding.parser.BindingExpressionListener) (listener)).exitResources(this);

        }

        @java.lang.Override
        public <Result> Result accept(android.databinding.parser.ParseTreeVisitor<? extends Result> visitor) {
            if (visitor instanceof android.databinding.parser.BindingExpressionVisitor<?>)
                return ((android.databinding.parser.BindingExpressionVisitor<? extends Result>) (visitor)).visitResources(this);
            else
                return visitor.visitChildren(this);

        }
    }

    @android.databinding.parser.RuleVersion(0)
    public final android.databinding.parser.BindingExpressionParser.ResourcesContext resources() throws android.databinding.parser.RecognitionException {
        android.databinding.parser.BindingExpressionParser.ResourcesContext _localctx = new android.databinding.parser.BindingExpressionParser.ResourcesContext(_ctx, getState());
        enterRule(_localctx, 34, RULE_resources);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(219);
                match(android.databinding.parser.BindingExpressionParser.ResourceReference);
                setState(221);
                switch (getInterpreter().adaptivePredict(_input, 17, _ctx)) {
                    case 1 :
                        {
                            setState(220);
                            resourceParameters();
                        }
                        break;
                }
            }
        } catch (android.databinding.parser.RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class ResourceParametersContext extends android.databinding.parser.ParserRuleContext {
        public android.databinding.parser.BindingExpressionParser.ExpressionListContext expressionList() {
            return getRuleContext(android.databinding.parser.BindingExpressionParser.ExpressionListContext.class, 0);
        }

        public ResourceParametersContext(android.databinding.parser.ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @java.lang.Override
        public int getRuleIndex() {
            return RULE_resourceParameters;
        }

        @java.lang.Override
        public void enterRule(android.databinding.parser.ParseTreeListener listener) {
            if (listener instanceof android.databinding.parser.BindingExpressionListener)
                ((android.databinding.parser.BindingExpressionListener) (listener)).enterResourceParameters(this);

        }

        @java.lang.Override
        public void exitRule(android.databinding.parser.ParseTreeListener listener) {
            if (listener instanceof android.databinding.parser.BindingExpressionListener)
                ((android.databinding.parser.BindingExpressionListener) (listener)).exitResourceParameters(this);

        }

        @java.lang.Override
        public <Result> Result accept(android.databinding.parser.ParseTreeVisitor<? extends Result> visitor) {
            if (visitor instanceof android.databinding.parser.BindingExpressionVisitor<?>)
                return ((android.databinding.parser.BindingExpressionVisitor<? extends Result>) (visitor)).visitResourceParameters(this);
            else
                return visitor.visitChildren(this);

        }
    }

    @android.databinding.parser.RuleVersion(0)
    public final android.databinding.parser.BindingExpressionParser.ResourceParametersContext resourceParameters() throws android.databinding.parser.RecognitionException {
        android.databinding.parser.BindingExpressionParser.ResourceParametersContext _localctx = new android.databinding.parser.BindingExpressionParser.ResourceParametersContext(_ctx, getState());
        enterRule(_localctx, 36, RULE_resourceParameters);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(223);
                match(android.databinding.parser.BindingExpressionParser.T__3);
                setState(224);
                expressionList();
                setState(225);
                match(android.databinding.parser.BindingExpressionParser.T__4);
            }
        } catch (android.databinding.parser.RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public boolean sempred(android.databinding.parser.RuleContext _localctx, int ruleIndex, int predIndex) {
        switch (ruleIndex) {
            case 3 :
                return expression_sempred(((android.databinding.parser.BindingExpressionParser.ExpressionContext) (_localctx)), predIndex);
        }
        return true;
    }

    private boolean expression_sempred(android.databinding.parser.BindingExpressionParser.ExpressionContext _localctx, int predIndex) {
        switch (predIndex) {
            case 0 :
                return precpred(_ctx, 13);
            case 1 :
                return precpred(_ctx, 12);
            case 2 :
                return precpred(_ctx, 11);
            case 3 :
                return precpred(_ctx, 10);
            case 4 :
                return precpred(_ctx, 8);
            case 5 :
                return precpred(_ctx, 7);
            case 6 :
                return precpred(_ctx, 6);
            case 7 :
                return precpred(_ctx, 5);
            case 8 :
                return precpred(_ctx, 4);
            case 9 :
                return precpred(_ctx, 3);
            case 10 :
                return precpred(_ctx, 2);
            case 11 :
                return precpred(_ctx, 1);
            case 12 :
                return precpred(_ctx, 19);
            case 13 :
                return precpred(_ctx, 18);
            case 14 :
                return precpred(_ctx, 17);
            case 15 :
                return precpred(_ctx, 9);
        }
        return true;
    }

    public static final java.lang.String _serializedATN = "\u0003\uaf6f\u8320\u479d\ub75c\u4880\u1605\u191c\uab37\u0003:\u00e6\u0004\u0002\t\u0002\u0004" + ((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((("\u0003\t\u0003\u0004\u0004\t\u0004\u0004\u0005\t\u0005\u0004\u0006\t\u0006\u0004\u0007\t\u0007\u0004\b\t\b\u0004\t\t\t\u0004\n\t\n\u0004\u000b\t" + "\u000b\u0004\f\t\f\u0004\r\t\r\u0004\u000e\t\u000e\u0004\u000f\t\u000f\u0004\u0010\t\u0010\u0004\u0011\t\u0011\u0004\u0012\t\u0012") + "\u0004\u0013\t\u0013\u0004\u0014\t\u0014\u0003\u0002\u0003\u0002\u0005\u0002+\n\u0002\u0003\u0003\u0003\u0003\u0003\u0003\u0003\u0003\u0003\u0003\u0003\u0004\u0003\u0004\u0003\u0004") + "\u0005\u00045\n\u0004\u0003\u0005\u0003\u0005\u0003\u0005\u0003\u0005\u0003\u0005\u0003\u0005\u0003\u0005\u0003\u0005\u0003\u0005\u0003\u0005\u0003\u0005\u0003\u0005\u0003\u0005\u0003\u0005\u0003") + "\u0005\u0003\u0005\u0003\u0005\u0003\u0005\u0005\u0005I\n\u0005\u0003\u0005\u0003\u0005\u0003\u0005\u0003\u0005\u0003\u0005\u0003\u0005\u0003\u0005\u0003\u0005\u0003\u0005\u0003\u0005\u0003\u0005\u0003") + "\u0005\u0003\u0005\u0003\u0005\u0003\u0005\u0003\u0005\u0003\u0005\u0003\u0005\u0003\u0005\u0003\u0005\u0003\u0005\u0003\u0005\u0003\u0005\u0003\u0005\u0003\u0005\u0003\u0005\u0003\u0005\u0003\u0005\u0003\u0005") + "\u0003\u0005\u0003\u0005\u0003\u0005\u0003\u0005\u0003\u0005\u0003\u0005\u0003\u0005\u0003\u0005\u0003\u0005\u0003\u0005\u0003\u0005\u0003\u0005\u0003\u0005\u0003\u0005\u0003\u0005\u0003\u0005\u0003\u0005\u0003") + "\u0005\u0003\u0005\u0003\u0005\u0003\u0005\u0003\u0005\u0003\u0005\u0005\u0005\u007f\n\u0005\u0003\u0005\u0003\u0005\u0003\u0005\u0003\u0005\u0007\u0005\u0085\n\u0005\f\u0005\u000e") + "\u0005\u0088\u000b\u0005\u0003\u0006\u0003\u0006\u0003\u0006\u0003\u0006\u0003\u0006\u0003\u0006\u0003\u0006\u0005\u0006\u0091\n\u0006\u0003\u0007\u0003\u0007\u0003\u0007\u0007") + "\u0007\u0096\n\u0007\f\u0007\u000e\u0007\u0099\u000b\u0007\u0003\b\u0003\b\u0005\b\u009d\n\b\u0003\t\u0003\t\u0003\n\u0003\n") + "\u0003\u000b\u0003\u000b\u0003\f\u0003\f\u0003\f\u0003\r\u0003\r\u0003\r\u0003\r\u0007\r\u00ac\n\r\f\r\u000e\r\u00af\u000b") + "\r\u0003\r\u0003\r\u0003\u000e\u0003\u000e\u0003\u000e\u0007\u000e\u00b6\n\u000e\f\u000e\u000e\u000e\u00b9\u000b\u000e\u0003\u000e") + "\u0003\u000e\u0003\u000e\u0007\u000e\u00be\n\u000e\f\u000e\u000e\u000e\u00c1\u000b\u000e\u0005\u000e\u00c3\n\u000e\u0003\u000f") + "\u0003\u000f\u0003\u000f\u0003\u0010\u0003\u0010\u0005\u0010\u00ca\n\u0010\u0003\u0010\u0003\u0010\u0003\u0011\u0003\u0011\u0005\u0011\u00d0\n") + "\u0011\u0003\u0011\u0003\u0011\u0003\u0011\u0005\u0011\u00d5\n\u0011\u0007\u0011\u00d7\n\u0011\f\u0011\u000e\u0011\u00da\u000b") + "\u0011\u0003\u0012\u0003\u0012\u0003\u0013\u0003\u0013\u0005\u0013\u00e0\n\u0013\u0003\u0014\u0003\u0014\u0003\u0014\u0003\u0014\u0003\u0014\u0002\u0002\u0003") + "\b\u0015\u0002\u0002\u0004\u0002\u0006\u0002\b\u0002\n\u0002\f\u0002\u000e\u0002\u0010\u0002\u0012\u0002\u0014\u0002\u0016\u0002\u0018\u0002\u001a\u0002\u001c\u0002") + "\u001e\u0002 \u0002\"\u0002$\u0002&\u0002\u0002\u000b\u0003\u0002\u000b\f\u0003\u0002\r\u000e\u0003\u0002\u000f\u0011\u0003\u0002\u0012\u0014\u0003\u0002\u0015\u0018") + "\u0003\u0002\u001a\u001b\u0004\u0002/255\u0003\u000234\u0003\u0002&-\u00f9\u0002(\u0003\u0002\u0002\u0002\u0004,\u0003\u0002\u0002\u0002\u0006") + "4\u0003\u0002\u0002\u0002\bH\u0003\u0002\u0002\u0002\n\u0090\u0003\u0002\u0002\u0002\f\u0092\u0003\u0002\u0002\u0002\u000e\u009c\u0003\u0002\u0002") + "\u0002\u0010\u009e\u0003\u0002\u0002\u0002\u0012\u00a0\u0003\u0002\u0002\u0002\u0014\u00a2\u0003\u0002\u0002\u0002\u0016\u00a4\u0003\u0002\u0002\u0002") + "\u0018\u00a7\u0003\u0002\u0002\u0002\u001a\u00c2\u0003\u0002\u0002\u0002\u001c\u00c4\u0003\u0002\u0002\u0002\u001e\u00c7\u0003\u0002\u0002\u0002 ") + "\u00cd\u0003\u0002\u0002\u0002\"\u00db\u0003\u0002\u0002\u0002$\u00dd\u0003\u0002\u0002\u0002&\u00e1\u0003\u0002\u0002\u0002(*\u0005\b\u0005\u0002") + ")+\u0005\u0004\u0003\u0002*)\u0003\u0002\u0002\u0002*+\u0003\u0002\u0002\u0002+\u0003\u0003\u0002\u0002\u0002,-\u0007\u0003\u0002\u0002-.\u0007\u0004\u0002\u0002./\u0007\u0005\u0002") + "\u0002/0\u0005\u0006\u0004\u00020\u0005\u0003\u0002\u0002\u000215\u0005\u000e\b\u000225\u00078\u0002\u000235\u0005\u0010\t\u0002") + "41\u0003\u0002\u0002\u000242\u0003\u0002\u0002\u000243\u0003\u0002\u0002\u00025\u0007\u0003\u0002\u0002\u000267\b\u0005\u0001\u0002") + "78\u0007\u0006\u0002\u000289\u0005\u001a\u000e\u00029:\u0007\u0007\u0002\u0002:;\u0005\b\u0005\u0012;I\u0003\u0002\u0002\u0002<=\t\u0002\u0002\u0002=I\u0005") + "\b\u0005\u0011>?\t\u0003\u0002\u0002?I\u0005\b\u0005\u0010@A\u0007\u0006\u0002\u0002AB\u0005\b\u0005\u0002BC\u0007\u0007\u0002\u0002CI\u0003\u0002\u0002\u0002D") + "I\u0005\u000e\b\u0002EI\u0005\u0010\t\u0002FI\u0005\n\u0006\u0002GI\u0005$\u0013\u0002H6\u0003\u0002\u0002\u0002H<\u0003\u0002\u0002\u0002H>\u0003\u0002") + "\u0002\u0002H@\u0003\u0002\u0002\u0002HD\u0003\u0002\u0002\u0002HE\u0003\u0002\u0002\u0002HF\u0003\u0002\u0002\u0002HG\u0003\u0002\u0002\u0002I\u0086\u0003\u0002\u0002\u0002") + "JK\f\u000f\u0002\u0002KL\t\u0004\u0002\u0002L\u0085\u0005\b\u0005\u0010MN\f\u000e\u0002\u0002NO\t\u0002\u0002\u0002O\u0085\u0005\b") + "\u0005\u000fPQ\f\r\u0002\u0002QR\t\u0005\u0002\u0002R\u0085\u0005\b\u0005\u000eST\f\f\u0002\u0002TU\t\u0006\u0002\u0002U\u0085\u0005") + "\b\u0005\rVW\f\n\u0002\u0002WX\t\u0007\u0002\u0002X\u0085\u0005\b\u0005\u000bYZ\f\t\u0002\u0002Z[\u0007\u001c\u0002\u0002[\u0085") + "\u0005\b\u0005\n\\]\f\b\u0002\u0002]^\u0007\u001d\u0002\u0002^\u0085\u0005\b\u0005\t_`\f\u0007\u0002\u0002`a\u0007\u001e\u0002\u0002a\u0085") + "\u0005\b\u0005\bbc\f\u0006\u0002\u0002cd\u0007\u001f\u0002\u0002d\u0085\u0005\b\u0005\u0007ef\f\u0005\u0002\u0002fg\u0007 \u0002\u0002g\u0085") + "\u0005\b\u0005\u0006hi\f\u0004\u0002\u0002ij\u0007!\u0002\u0002jk\u0005\b\u0005\u0002kl\u0007\"\u0002\u0002lm\u0005\b\u0005\u0004m\u0085\u0003\u0002\u0002") + "\u0002no\f\u0003\u0002\u0002op\u0007#\u0002\u0002p\u0085\u0005\b\u0005\u0004qr\f\u0015\u0002\u0002rs\u0007\b\u0002\u0002s\u0085\u00076") + "\u0002\u0002tu\f\u0014\u0002\u0002uv\u0007\t\u0002\u0002vw\u0005\b\u0005\u0002wx\u0007\n\u0002\u0002x\u0085\u0003\u0002\u0002\u0002yz\f\u0013\u0002") + "\u0002z{\u0007\b\u0002\u0002{|\u00076\u0002\u0002|~\u0007\u0006\u0002\u0002}\u007f\u0005\f\u0007\u0002~}\u0003\u0002\u0002\u0002~\u007f\u0003\u0002\u0002\u0002") + "\u007f\u0080\u0003\u0002\u0002\u0002\u0080\u0085\u0007\u0007\u0002\u0002\u0081\u0082\f\u000b\u0002\u0002\u0082\u0083") + "\u0007\u0019\u0002\u0002\u0083\u0085\u0005\u001a\u000e\u0002\u0084J\u0003\u0002\u0002\u0002\u0084M\u0003\u0002\u0002\u0002\u0084P\u0003") + "\u0002\u0002\u0002\u0084S\u0003\u0002\u0002\u0002\u0084V\u0003\u0002\u0002\u0002\u0084Y\u0003\u0002\u0002\u0002\u0084\\\u0003\u0002\u0002\u0002\u0084") + "_\u0003\u0002\u0002\u0002\u0084b\u0003\u0002\u0002\u0002\u0084e\u0003\u0002\u0002\u0002\u0084h\u0003\u0002\u0002\u0002\u0084n\u0003\u0002\u0002\u0002") + "\u0084q\u0003\u0002\u0002\u0002\u0084t\u0003\u0002\u0002\u0002\u0084y\u0003\u0002\u0002\u0002\u0084\u0081\u0003\u0002\u0002\u0002\u0085") + "\u0088\u0003\u0002\u0002\u0002\u0086\u0084\u0003\u0002\u0002\u0002\u0086\u0087\u0003\u0002\u0002\u0002\u0087\t\u0003\u0002\u0002\u0002") + "\u0088\u0086\u0003\u0002\u0002\u0002\u0089\u008a\u0005\u001a\u000e\u0002\u008a\u008b\u0007\b\u0002\u0002\u008b\u008c") + "\u0007$\u0002\u0002\u008c\u0091\u0003\u0002\u0002\u0002\u008d\u008e\u0007%\u0002\u0002\u008e\u008f\u0007\b\u0002\u0002\u008f") + "\u0091\u0007$\u0002\u0002\u0090\u0089\u0003\u0002\u0002\u0002\u0090\u008d\u0003\u0002\u0002\u0002\u0091\u000b\u0003\u0002\u0002\u0002") + "\u0092\u0097\u0005\b\u0005\u0002\u0093\u0094\u0007\u0003\u0002\u0002\u0094\u0096\u0005\b\u0005\u0002\u0095\u0093") + "\u0003\u0002\u0002\u0002\u0096\u0099\u0003\u0002\u0002\u0002\u0097\u0095\u0003\u0002\u0002\u0002\u0097\u0098\u0003\u0002\u0002\u0002\u0098") + "\r\u0003\u0002\u0002\u0002\u0099\u0097\u0003\u0002\u0002\u0002\u009a\u009d\u0005\u0012\n\u0002\u009b\u009d\u0005\u0014\u000b") + "\u0002\u009c\u009a\u0003\u0002\u0002\u0002\u009c\u009b\u0003\u0002\u0002\u0002\u009d\u000f\u0003\u0002\u0002\u0002\u009e\u009f") + "\u00076\u0002\u0002\u009f\u0011\u0003\u0002\u0002\u0002\u00a0\u00a1\t\b\u0002\u0002\u00a1\u0013\u0003\u0002\u0002\u0002\u00a2") + "\u00a3\t\t\u0002\u0002\u00a3\u0015\u0003\u0002\u0002\u0002\u00a4\u00a5\u0005\u0018\r\u0002\u00a5\u00a6\u0005\u001c") + "\u000f\u0002\u00a6\u0017\u0003\u0002\u0002\u0002\u00a7\u00a8\u0007\u0018\u0002\u0002\u00a8\u00ad\u0005\u001a\u000e\u0002\u00a9") + "\u00aa\u0007\u0003\u0002\u0002\u00aa\u00ac\u0005\u001a\u000e\u0002\u00ab\u00a9\u0003\u0002\u0002\u0002\u00ac\u00af\u0003") + "\u0002\u0002\u0002\u00ad\u00ab\u0003\u0002\u0002\u0002\u00ad\u00ae\u0003\u0002\u0002\u0002\u00ae\u00b0\u0003\u0002\u0002\u0002\u00af") + "\u00ad\u0003\u0002\u0002\u0002\u00b0\u00b1\u0007\u0017\u0002\u0002\u00b1\u0019\u0003\u0002\u0002\u0002\u00b2\u00b7\u0005 \u0011") + "\u0002\u00b3\u00b4\u0007\t\u0002\u0002\u00b4\u00b6\u0007\n\u0002\u0002\u00b5\u00b3\u0003\u0002\u0002\u0002\u00b6\u00b9") + "\u0003\u0002\u0002\u0002\u00b7\u00b5\u0003\u0002\u0002\u0002\u00b7\u00b8\u0003\u0002\u0002\u0002\u00b8\u00c3\u0003\u0002\u0002\u0002\u00b9") + "\u00b7\u0003\u0002\u0002\u0002\u00ba\u00bf\u0005\"\u0012\u0002\u00bb\u00bc\u0007\t\u0002\u0002\u00bc\u00be\u0007") + "\n\u0002\u0002\u00bd\u00bb\u0003\u0002\u0002\u0002\u00be\u00c1\u0003\u0002\u0002\u0002\u00bf\u00bd\u0003\u0002\u0002\u0002\u00bf") + "\u00c0\u0003\u0002\u0002\u0002\u00c0\u00c3\u0003\u0002\u0002\u0002\u00c1\u00bf\u0003\u0002\u0002\u0002\u00c2\u00b2\u0003\u0002") + "\u0002\u0002\u00c2\u00ba\u0003\u0002\u0002\u0002\u00c3\u001b\u0003\u0002\u0002\u0002\u00c4\u00c5\u00076\u0002\u0002\u00c5\u00c6") + "\u0005\u001e\u0010\u0002\u00c6\u001d\u0003\u0002\u0002\u0002\u00c7\u00c9\u0007\u0006\u0002\u0002\u00c8\u00ca\u0005\f\u0007\u0002\u00c9") + "\u00c8\u0003\u0002\u0002\u0002\u00c9\u00ca\u0003\u0002\u0002\u0002\u00ca\u00cb\u0003\u0002\u0002\u0002\u00cb\u00cc\u0007\u0007") + "\u0002\u0002\u00cc\u001f\u0003\u0002\u0002\u0002\u00cd\u00cf\u0005\u0010\t\u0002\u00ce\u00d0\u0005\u0018\r\u0002\u00cf") + "\u00ce\u0003\u0002\u0002\u0002\u00cf\u00d0\u0003\u0002\u0002\u0002\u00d0\u00d8\u0003\u0002\u0002\u0002\u00d1\u00d2\u0007\b") + "\u0002\u0002\u00d2\u00d4\u00076\u0002\u0002\u00d3\u00d5\u0005\u0018\r\u0002\u00d4\u00d3\u0003\u0002\u0002\u0002\u00d4") + "\u00d5\u0003\u0002\u0002\u0002\u00d5\u00d7\u0003\u0002\u0002\u0002\u00d6\u00d1\u0003\u0002\u0002\u0002\u00d7\u00da\u0003\u0002") + "\u0002\u0002\u00d8\u00d6\u0003\u0002\u0002\u0002\u00d8\u00d9\u0003\u0002\u0002\u0002\u00d9!\u0003\u0002\u0002\u0002\u00da\u00d8") + "\u0003\u0002\u0002\u0002\u00db\u00dc\t\n\u0002\u0002\u00dc#\u0003\u0002\u0002\u0002\u00dd\u00df\u00078\u0002\u0002\u00de\u00e0") + "\u0005&\u0014\u0002\u00df\u00de\u0003\u0002\u0002\u0002\u00df\u00e0\u0003\u0002\u0002\u0002\u00e0%\u0003\u0002\u0002\u0002\u00e1") + "\u00e2\u0007\u0006\u0002\u0002\u00e2\u00e3\u0005\f\u0007\u0002\u00e3\u00e4\u0007\u0007\u0002\u0002\u00e4\'\u0003\u0002\u0002\u0002") + "\u0014*4H~\u0084\u0086\u0090\u0097\u009c\u00ad\u00b7\u00bf\u00c2\u00c9") + "\u00cf\u00d4\u00d8\u00df");

    public static final android.databinding.parser.ATN _ATN = new android.databinding.parser.ATNDeserializer().deserialize(android.databinding.parser.BindingExpressionParser._serializedATN.toCharArray());

    static {
    }
}

