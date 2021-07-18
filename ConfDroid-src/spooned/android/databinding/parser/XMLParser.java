/**
 * Generated from XMLParser.g4 by ANTLR 4.4
 */
package android.databinding.parser;


import static android.databinding.parser.XMLParser.RULE_attribute;
import static android.databinding.parser.XMLParser.RULE_chardata;
import static android.databinding.parser.XMLParser.RULE_content;
import static android.databinding.parser.XMLParser.RULE_document;
import static android.databinding.parser.XMLParser.RULE_element;
import static android.databinding.parser.XMLParser.RULE_misc;
import static android.databinding.parser.XMLParser.RULE_prolog;
import static android.databinding.parser.XMLParser.RULE_reference;


public class XMLParser extends android.databinding.parser.Parser {
    public static final int OPEN = 7;

    public static final int CDATA = 2;

    public static final int SLASH = 13;

    public static final int CharRef = 5;

    public static final int SEA_WS = 6;

    public static final int SPECIAL_CLOSE = 11;

    public static final int CLOSE = 10;

    public static final int DTD = 3;

    public static final int Name = 16;

    public static final int EQUALS = 14;

    public static final int PI = 18;

    public static final int S = 17;

    public static final int SLASH_CLOSE = 12;

    public static final int TEXT = 9;

    public static final int COMMENT = 1;

    public static final int XMLDeclOpen = 8;

    public static final int EntityRef = 4;

    public static final int STRING = 15;

    public static final java.lang.String[] tokenNames = new java.lang.String[]{ "<INVALID>", "COMMENT", "CDATA", "DTD", "EntityRef", "CharRef", "SEA_WS", "'<'", "XMLDeclOpen", "TEXT", "'>'", "SPECIAL_CLOSE", "'/>'", "'/'", "'='", "STRING", "Name", "S", "PI" };

    public static final int RULE_document = 0;

    public static final int RULE_prolog = 1;

    public static final int RULE_content = 2;

    public static final int RULE_element = 3;

    public static final int RULE_reference = 4;

    public static final int RULE_attribute = 5;

    public static final int RULE_chardata = 6;

    public static final int RULE_misc = 7;

    public static final java.lang.String[] ruleNames = new java.lang.String[]{ "document", "prolog", "content", "element", "reference", "attribute", "chardata", "misc" };

    @java.lang.Override
    public java.lang.String getGrammarFileName() {
        return "XMLParser.g4";
    }

    @java.lang.Override
    public java.lang.String[] getTokenNames() {
        return android.databinding.parser.XMLParser.tokenNames;
    }

    @java.lang.Override
    public java.lang.String[] getRuleNames() {
        return android.databinding.parser.XMLParser.ruleNames;
    }

    @java.lang.Override
    public java.lang.String getSerializedATN() {
        return android.databinding.parser.XMLParser._serializedATN;
    }

    public XMLParser(android.databinding.parser.TokenStream input) {
        super(input);
        _interp = new android.databinding.parser.ParserATNSimulator(this, android.databinding.parser.XMLParser._ATN);
    }

    public static class DocumentContext extends android.databinding.parser.ParserRuleContext {
        public android.databinding.parser.XMLParser.ElementContext element() {
            return getRuleContext(android.databinding.parser.XMLParser.ElementContext.class, 0);
        }

        public java.util.List<? extends android.databinding.parser.XMLParser.MiscContext> misc() {
            return getRuleContexts(android.databinding.parser.XMLParser.MiscContext.class);
        }

        public android.databinding.parser.XMLParser.PrologContext prolog() {
            return getRuleContext(android.databinding.parser.XMLParser.PrologContext.class, 0);
        }

        public android.databinding.parser.XMLParser.MiscContext misc(int i) {
            return getRuleContext(android.databinding.parser.XMLParser.MiscContext.class, i);
        }

        public DocumentContext(android.databinding.parser.ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @java.lang.Override
        public int getRuleIndex() {
            return RULE_document;
        }

        @java.lang.Override
        public void enterRule(android.databinding.parser.ParseTreeListener listener) {
            if (listener instanceof android.databinding.parser.XMLParserListener)
                ((android.databinding.parser.XMLParserListener) (listener)).enterDocument(this);

        }

        @java.lang.Override
        public void exitRule(android.databinding.parser.ParseTreeListener listener) {
            if (listener instanceof android.databinding.parser.XMLParserListener)
                ((android.databinding.parser.XMLParserListener) (listener)).exitDocument(this);

        }

        @java.lang.Override
        public <Result> Result accept(android.databinding.parser.ParseTreeVisitor<? extends Result> visitor) {
            if (visitor instanceof android.databinding.parser.XMLParserVisitor<?>)
                return ((android.databinding.parser.XMLParserVisitor<? extends Result>) (visitor)).visitDocument(this);
            else
                return visitor.visitChildren(this);

        }
    }

    @android.databinding.parser.RuleVersion(0)
    public final android.databinding.parser.XMLParser.DocumentContext document() throws android.databinding.parser.RecognitionException {
        android.databinding.parser.XMLParser.DocumentContext _localctx = new android.databinding.parser.XMLParser.DocumentContext(_ctx, getState());
        enterRule(_localctx, 0, RULE_document);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(17);
                _la = _input.LA(1);
                if (_la == android.databinding.parser.XMLParser.XMLDeclOpen) {
                    {
                        setState(16);
                        prolog();
                    }
                }
                setState(22);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (((_la & (~0x3f)) == 0) && (((1L << _la) & (((1L << android.databinding.parser.XMLParser.COMMENT) | (1L << android.databinding.parser.XMLParser.SEA_WS)) | (1L << android.databinding.parser.XMLParser.PI))) != 0)) {
                    {
                        {
                            setState(19);
                            misc();
                        }
                    }
                    setState(24);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                } 
                setState(25);
                element();
                setState(29);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (((_la & (~0x3f)) == 0) && (((1L << _la) & (((1L << android.databinding.parser.XMLParser.COMMENT) | (1L << android.databinding.parser.XMLParser.SEA_WS)) | (1L << android.databinding.parser.XMLParser.PI))) != 0)) {
                    {
                        {
                            setState(26);
                            misc();
                        }
                    }
                    setState(31);
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

    public static class PrologContext extends android.databinding.parser.ParserRuleContext {
        public android.databinding.parser.TerminalNode SPECIAL_CLOSE() {
            return getToken(android.databinding.parser.XMLParser.SPECIAL_CLOSE, 0);
        }

        public java.util.List<? extends android.databinding.parser.XMLParser.AttributeContext> attribute() {
            return getRuleContexts(android.databinding.parser.XMLParser.AttributeContext.class);
        }

        public android.databinding.parser.XMLParser.AttributeContext attribute(int i) {
            return getRuleContext(android.databinding.parser.XMLParser.AttributeContext.class, i);
        }

        public android.databinding.parser.TerminalNode XMLDeclOpen() {
            return getToken(android.databinding.parser.XMLParser.XMLDeclOpen, 0);
        }

        public PrologContext(android.databinding.parser.ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @java.lang.Override
        public int getRuleIndex() {
            return RULE_prolog;
        }

        @java.lang.Override
        public void enterRule(android.databinding.parser.ParseTreeListener listener) {
            if (listener instanceof android.databinding.parser.XMLParserListener)
                ((android.databinding.parser.XMLParserListener) (listener)).enterProlog(this);

        }

        @java.lang.Override
        public void exitRule(android.databinding.parser.ParseTreeListener listener) {
            if (listener instanceof android.databinding.parser.XMLParserListener)
                ((android.databinding.parser.XMLParserListener) (listener)).exitProlog(this);

        }

        @java.lang.Override
        public <Result> Result accept(android.databinding.parser.ParseTreeVisitor<? extends Result> visitor) {
            if (visitor instanceof android.databinding.parser.XMLParserVisitor<?>)
                return ((android.databinding.parser.XMLParserVisitor<? extends Result>) (visitor)).visitProlog(this);
            else
                return visitor.visitChildren(this);

        }
    }

    @android.databinding.parser.RuleVersion(0)
    public final android.databinding.parser.XMLParser.PrologContext prolog() throws android.databinding.parser.RecognitionException {
        android.databinding.parser.XMLParser.PrologContext _localctx = new android.databinding.parser.XMLParser.PrologContext(_ctx, getState());
        enterRule(_localctx, 2, RULE_prolog);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(32);
                match(android.databinding.parser.XMLParser.XMLDeclOpen);
                setState(36);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == android.databinding.parser.XMLParser.Name) {
                    {
                        {
                            setState(33);
                            attribute();
                        }
                    }
                    setState(38);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                } 
                setState(39);
                match(android.databinding.parser.XMLParser.SPECIAL_CLOSE);
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

    public static class ContentContext extends android.databinding.parser.ParserRuleContext {
        public java.util.List<? extends android.databinding.parser.TerminalNode> PI() {
            return getTokens(android.databinding.parser.XMLParser.PI);
        }

        public java.util.List<? extends android.databinding.parser.TerminalNode> CDATA() {
            return getTokens(android.databinding.parser.XMLParser.CDATA);
        }

        public java.util.List<? extends android.databinding.parser.XMLParser.ElementContext> element() {
            return getRuleContexts(android.databinding.parser.XMLParser.ElementContext.class);
        }

        public android.databinding.parser.TerminalNode PI(int i) {
            return getToken(android.databinding.parser.XMLParser.PI, i);
        }

        public android.databinding.parser.XMLParser.ElementContext element(int i) {
            return getRuleContext(android.databinding.parser.XMLParser.ElementContext.class, i);
        }

        public android.databinding.parser.TerminalNode COMMENT(int i) {
            return getToken(android.databinding.parser.XMLParser.COMMENT, i);
        }

        public android.databinding.parser.TerminalNode CDATA(int i) {
            return getToken(android.databinding.parser.XMLParser.CDATA, i);
        }

        public android.databinding.parser.XMLParser.ReferenceContext reference(int i) {
            return getRuleContext(android.databinding.parser.XMLParser.ReferenceContext.class, i);
        }

        public java.util.List<? extends android.databinding.parser.TerminalNode> COMMENT() {
            return getTokens(android.databinding.parser.XMLParser.COMMENT);
        }

        public android.databinding.parser.XMLParser.ChardataContext chardata(int i) {
            return getRuleContext(android.databinding.parser.XMLParser.ChardataContext.class, i);
        }

        public java.util.List<? extends android.databinding.parser.XMLParser.ChardataContext> chardata() {
            return getRuleContexts(android.databinding.parser.XMLParser.ChardataContext.class);
        }

        public java.util.List<? extends android.databinding.parser.XMLParser.ReferenceContext> reference() {
            return getRuleContexts(android.databinding.parser.XMLParser.ReferenceContext.class);
        }

        public ContentContext(android.databinding.parser.ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @java.lang.Override
        public int getRuleIndex() {
            return RULE_content;
        }

        @java.lang.Override
        public void enterRule(android.databinding.parser.ParseTreeListener listener) {
            if (listener instanceof android.databinding.parser.XMLParserListener)
                ((android.databinding.parser.XMLParserListener) (listener)).enterContent(this);

        }

        @java.lang.Override
        public void exitRule(android.databinding.parser.ParseTreeListener listener) {
            if (listener instanceof android.databinding.parser.XMLParserListener)
                ((android.databinding.parser.XMLParserListener) (listener)).exitContent(this);

        }

        @java.lang.Override
        public <Result> Result accept(android.databinding.parser.ParseTreeVisitor<? extends Result> visitor) {
            if (visitor instanceof android.databinding.parser.XMLParserVisitor<?>)
                return ((android.databinding.parser.XMLParserVisitor<? extends Result>) (visitor)).visitContent(this);
            else
                return visitor.visitChildren(this);

        }
    }

    @android.databinding.parser.RuleVersion(0)
    public final android.databinding.parser.XMLParser.ContentContext content() throws android.databinding.parser.RecognitionException {
        android.databinding.parser.XMLParser.ContentContext _localctx = new android.databinding.parser.XMLParser.ContentContext(_ctx, getState());
        enterRule(_localctx, 4, RULE_content);
        int _la;
        try {
            int _alt;
            enterOuterAlt(_localctx, 1);
            {
                setState(42);
                _la = _input.LA(1);
                if ((_la == android.databinding.parser.XMLParser.SEA_WS) || (_la == android.databinding.parser.XMLParser.TEXT)) {
                    {
                        setState(41);
                        chardata();
                    }
                }
                setState(56);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input, 7, _ctx);
                while ((_alt != 2) && (_alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER)) {
                    if (_alt == 1) {
                        {
                            {
                                setState(49);
                                switch (_input.LA(1)) {
                                    case android.databinding.parser.XMLParser.OPEN :
                                        {
                                            setState(44);
                                            element();
                                        }
                                        break;
                                    case android.databinding.parser.XMLParser.EntityRef :
                                    case android.databinding.parser.XMLParser.CharRef :
                                        {
                                            setState(45);
                                            reference();
                                        }
                                        break;
                                    case android.databinding.parser.XMLParser.CDATA :
                                        {
                                            setState(46);
                                            match(android.databinding.parser.XMLParser.CDATA);
                                        }
                                        break;
                                    case android.databinding.parser.XMLParser.PI :
                                        {
                                            setState(47);
                                            match(android.databinding.parser.XMLParser.PI);
                                        }
                                        break;
                                    case android.databinding.parser.XMLParser.COMMENT :
                                        {
                                            setState(48);
                                            match(android.databinding.parser.XMLParser.COMMENT);
                                        }
                                        break;
                                    default :
                                        throw new android.databinding.parser.NoViableAltException(this);
                                }
                                setState(52);
                                _la = _input.LA(1);
                                if ((_la == android.databinding.parser.XMLParser.SEA_WS) || (_la == android.databinding.parser.XMLParser.TEXT)) {
                                    {
                                        setState(51);
                                        chardata();
                                    }
                                }
                            }
                        }
                    }
                    setState(58);
                    _errHandler.sync(this);
                    _alt = getInterpreter().adaptivePredict(_input, 7, _ctx);
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

    public static class ElementContext extends android.databinding.parser.ParserRuleContext {
        public android.databinding.parser.Token elmName;

        public java.util.List<? extends android.databinding.parser.XMLParser.AttributeContext> attribute() {
            return getRuleContexts(android.databinding.parser.XMLParser.AttributeContext.class);
        }

        public android.databinding.parser.XMLParser.AttributeContext attribute(int i) {
            return getRuleContext(android.databinding.parser.XMLParser.AttributeContext.class, i);
        }

        public android.databinding.parser.TerminalNode Name(int i) {
            return getToken(android.databinding.parser.XMLParser.Name, i);
        }

        public java.util.List<? extends android.databinding.parser.TerminalNode> Name() {
            return getTokens(android.databinding.parser.XMLParser.Name);
        }

        public android.databinding.parser.XMLParser.ContentContext content() {
            return getRuleContext(android.databinding.parser.XMLParser.ContentContext.class, 0);
        }

        public ElementContext(android.databinding.parser.ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @java.lang.Override
        public int getRuleIndex() {
            return RULE_element;
        }

        @java.lang.Override
        public void enterRule(android.databinding.parser.ParseTreeListener listener) {
            if (listener instanceof android.databinding.parser.XMLParserListener)
                ((android.databinding.parser.XMLParserListener) (listener)).enterElement(this);

        }

        @java.lang.Override
        public void exitRule(android.databinding.parser.ParseTreeListener listener) {
            if (listener instanceof android.databinding.parser.XMLParserListener)
                ((android.databinding.parser.XMLParserListener) (listener)).exitElement(this);

        }

        @java.lang.Override
        public <Result> Result accept(android.databinding.parser.ParseTreeVisitor<? extends Result> visitor) {
            if (visitor instanceof android.databinding.parser.XMLParserVisitor<?>)
                return ((android.databinding.parser.XMLParserVisitor<? extends Result>) (visitor)).visitElement(this);
            else
                return visitor.visitChildren(this);

        }
    }

    @android.databinding.parser.RuleVersion(0)
    public final android.databinding.parser.XMLParser.ElementContext element() throws android.databinding.parser.RecognitionException {
        android.databinding.parser.XMLParser.ElementContext _localctx = new android.databinding.parser.XMLParser.ElementContext(_ctx, getState());
        enterRule(_localctx, 6, RULE_element);
        int _la;
        try {
            setState(83);
            switch (getInterpreter().adaptivePredict(_input, 10, _ctx)) {
                case 1 :
                    enterOuterAlt(_localctx, 1);
                    {
                        setState(59);
                        match(android.databinding.parser.XMLParser.OPEN);
                        setState(60);
                        _localctx.elmName = match(android.databinding.parser.XMLParser.Name);
                        setState(64);
                        _errHandler.sync(this);
                        _la = _input.LA(1);
                        while (_la == android.databinding.parser.XMLParser.Name) {
                            {
                                {
                                    setState(61);
                                    attribute();
                                }
                            }
                            setState(66);
                            _errHandler.sync(this);
                            _la = _input.LA(1);
                        } 
                        setState(67);
                        match(android.databinding.parser.XMLParser.CLOSE);
                        setState(68);
                        content();
                        setState(69);
                        match(android.databinding.parser.XMLParser.OPEN);
                        setState(70);
                        match(android.databinding.parser.XMLParser.SLASH);
                        setState(71);
                        match(android.databinding.parser.XMLParser.Name);
                        setState(72);
                        match(android.databinding.parser.XMLParser.CLOSE);
                    }
                    break;
                case 2 :
                    enterOuterAlt(_localctx, 2);
                    {
                        setState(74);
                        match(android.databinding.parser.XMLParser.OPEN);
                        setState(75);
                        _localctx.elmName = match(android.databinding.parser.XMLParser.Name);
                        setState(79);
                        _errHandler.sync(this);
                        _la = _input.LA(1);
                        while (_la == android.databinding.parser.XMLParser.Name) {
                            {
                                {
                                    setState(76);
                                    attribute();
                                }
                            }
                            setState(81);
                            _errHandler.sync(this);
                            _la = _input.LA(1);
                        } 
                        setState(82);
                        match(android.databinding.parser.XMLParser.SLASH_CLOSE);
                    }
                    break;
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

    public static class ReferenceContext extends android.databinding.parser.ParserRuleContext {
        public android.databinding.parser.TerminalNode CharRef() {
            return getToken(android.databinding.parser.XMLParser.CharRef, 0);
        }

        public android.databinding.parser.TerminalNode EntityRef() {
            return getToken(android.databinding.parser.XMLParser.EntityRef, 0);
        }

        public ReferenceContext(android.databinding.parser.ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @java.lang.Override
        public int getRuleIndex() {
            return RULE_reference;
        }

        @java.lang.Override
        public void enterRule(android.databinding.parser.ParseTreeListener listener) {
            if (listener instanceof android.databinding.parser.XMLParserListener)
                ((android.databinding.parser.XMLParserListener) (listener)).enterReference(this);

        }

        @java.lang.Override
        public void exitRule(android.databinding.parser.ParseTreeListener listener) {
            if (listener instanceof android.databinding.parser.XMLParserListener)
                ((android.databinding.parser.XMLParserListener) (listener)).exitReference(this);

        }

        @java.lang.Override
        public <Result> Result accept(android.databinding.parser.ParseTreeVisitor<? extends Result> visitor) {
            if (visitor instanceof android.databinding.parser.XMLParserVisitor<?>)
                return ((android.databinding.parser.XMLParserVisitor<? extends Result>) (visitor)).visitReference(this);
            else
                return visitor.visitChildren(this);

        }
    }

    @android.databinding.parser.RuleVersion(0)
    public final android.databinding.parser.XMLParser.ReferenceContext reference() throws android.databinding.parser.RecognitionException {
        android.databinding.parser.XMLParser.ReferenceContext _localctx = new android.databinding.parser.XMLParser.ReferenceContext(_ctx, getState());
        enterRule(_localctx, 8, RULE_reference);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(85);
                _la = _input.LA(1);
                if (!((_la == android.databinding.parser.XMLParser.EntityRef) || (_la == android.databinding.parser.XMLParser.CharRef))) {
                    _errHandler.recoverInline(this);
                }
                consume();
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

    public static class AttributeContext extends android.databinding.parser.ParserRuleContext {
        public android.databinding.parser.Token attrName;

        public android.databinding.parser.Token attrValue;

        public android.databinding.parser.TerminalNode Name() {
            return getToken(android.databinding.parser.XMLParser.Name, 0);
        }

        public android.databinding.parser.TerminalNode STRING() {
            return getToken(android.databinding.parser.XMLParser.STRING, 0);
        }

        public AttributeContext(android.databinding.parser.ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @java.lang.Override
        public int getRuleIndex() {
            return RULE_attribute;
        }

        @java.lang.Override
        public void enterRule(android.databinding.parser.ParseTreeListener listener) {
            if (listener instanceof android.databinding.parser.XMLParserListener)
                ((android.databinding.parser.XMLParserListener) (listener)).enterAttribute(this);

        }

        @java.lang.Override
        public void exitRule(android.databinding.parser.ParseTreeListener listener) {
            if (listener instanceof android.databinding.parser.XMLParserListener)
                ((android.databinding.parser.XMLParserListener) (listener)).exitAttribute(this);

        }

        @java.lang.Override
        public <Result> Result accept(android.databinding.parser.ParseTreeVisitor<? extends Result> visitor) {
            if (visitor instanceof android.databinding.parser.XMLParserVisitor<?>)
                return ((android.databinding.parser.XMLParserVisitor<? extends Result>) (visitor)).visitAttribute(this);
            else
                return visitor.visitChildren(this);

        }
    }

    @android.databinding.parser.RuleVersion(0)
    public final android.databinding.parser.XMLParser.AttributeContext attribute() throws android.databinding.parser.RecognitionException {
        android.databinding.parser.XMLParser.AttributeContext _localctx = new android.databinding.parser.XMLParser.AttributeContext(_ctx, getState());
        enterRule(_localctx, 10, RULE_attribute);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(87);
                _localctx.attrName = match(android.databinding.parser.XMLParser.Name);
                setState(88);
                match(android.databinding.parser.XMLParser.EQUALS);
                setState(89);
                _localctx.attrValue = match(android.databinding.parser.XMLParser.STRING);
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

    public static class ChardataContext extends android.databinding.parser.ParserRuleContext {
        public android.databinding.parser.TerminalNode SEA_WS() {
            return getToken(android.databinding.parser.XMLParser.SEA_WS, 0);
        }

        public android.databinding.parser.TerminalNode TEXT() {
            return getToken(android.databinding.parser.XMLParser.TEXT, 0);
        }

        public ChardataContext(android.databinding.parser.ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @java.lang.Override
        public int getRuleIndex() {
            return RULE_chardata;
        }

        @java.lang.Override
        public void enterRule(android.databinding.parser.ParseTreeListener listener) {
            if (listener instanceof android.databinding.parser.XMLParserListener)
                ((android.databinding.parser.XMLParserListener) (listener)).enterChardata(this);

        }

        @java.lang.Override
        public void exitRule(android.databinding.parser.ParseTreeListener listener) {
            if (listener instanceof android.databinding.parser.XMLParserListener)
                ((android.databinding.parser.XMLParserListener) (listener)).exitChardata(this);

        }

        @java.lang.Override
        public <Result> Result accept(android.databinding.parser.ParseTreeVisitor<? extends Result> visitor) {
            if (visitor instanceof android.databinding.parser.XMLParserVisitor<?>)
                return ((android.databinding.parser.XMLParserVisitor<? extends Result>) (visitor)).visitChardata(this);
            else
                return visitor.visitChildren(this);

        }
    }

    @android.databinding.parser.RuleVersion(0)
    public final android.databinding.parser.XMLParser.ChardataContext chardata() throws android.databinding.parser.RecognitionException {
        android.databinding.parser.XMLParser.ChardataContext _localctx = new android.databinding.parser.XMLParser.ChardataContext(_ctx, getState());
        enterRule(_localctx, 12, RULE_chardata);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(91);
                _la = _input.LA(1);
                if (!((_la == android.databinding.parser.XMLParser.SEA_WS) || (_la == android.databinding.parser.XMLParser.TEXT))) {
                    _errHandler.recoverInline(this);
                }
                consume();
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

    public static class MiscContext extends android.databinding.parser.ParserRuleContext {
        public android.databinding.parser.TerminalNode SEA_WS() {
            return getToken(android.databinding.parser.XMLParser.SEA_WS, 0);
        }

        public android.databinding.parser.TerminalNode PI() {
            return getToken(android.databinding.parser.XMLParser.PI, 0);
        }

        public android.databinding.parser.TerminalNode COMMENT() {
            return getToken(android.databinding.parser.XMLParser.COMMENT, 0);
        }

        public MiscContext(android.databinding.parser.ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @java.lang.Override
        public int getRuleIndex() {
            return RULE_misc;
        }

        @java.lang.Override
        public void enterRule(android.databinding.parser.ParseTreeListener listener) {
            if (listener instanceof android.databinding.parser.XMLParserListener)
                ((android.databinding.parser.XMLParserListener) (listener)).enterMisc(this);

        }

        @java.lang.Override
        public void exitRule(android.databinding.parser.ParseTreeListener listener) {
            if (listener instanceof android.databinding.parser.XMLParserListener)
                ((android.databinding.parser.XMLParserListener) (listener)).exitMisc(this);

        }

        @java.lang.Override
        public <Result> Result accept(android.databinding.parser.ParseTreeVisitor<? extends Result> visitor) {
            if (visitor instanceof android.databinding.parser.XMLParserVisitor<?>)
                return ((android.databinding.parser.XMLParserVisitor<? extends Result>) (visitor)).visitMisc(this);
            else
                return visitor.visitChildren(this);

        }
    }

    @android.databinding.parser.RuleVersion(0)
    public final android.databinding.parser.XMLParser.MiscContext misc() throws android.databinding.parser.RecognitionException {
        android.databinding.parser.XMLParser.MiscContext _localctx = new android.databinding.parser.XMLParser.MiscContext(_ctx, getState());
        enterRule(_localctx, 14, RULE_misc);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(93);
                _la = _input.LA(1);
                if (!(((_la & (~0x3f)) == 0) && (((1L << _la) & (((1L << android.databinding.parser.XMLParser.COMMENT) | (1L << android.databinding.parser.XMLParser.SEA_WS)) | (1L << android.databinding.parser.XMLParser.PI))) != 0))) {
                    _errHandler.recoverInline(this);
                }
                consume();
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

    public static final java.lang.String _serializedATN = "\u0003\uaf6f\u8320\u479d\ub75c\u4880\u1605\u191c\uab37\u0003\u0014b\u0004\u0002\t\u0002\u0004\u0003\t" + (((((((((((((((((((((((("\u0003\u0004\u0004\t\u0004\u0004\u0005\t\u0005\u0004\u0006\t\u0006\u0004\u0007\t\u0007\u0004\b\t\b\u0004\t\t\t\u0003\u0002\u0005\u0002\u0014\n\u0002\u0003\u0002" + "\u0007\u0002\u0017\n\u0002\f\u0002\u000e\u0002\u001a\u000b\u0002\u0003\u0002\u0003\u0002\u0007\u0002\u001e\n\u0002\f\u0002\u000e\u0002!\u000b\u0002\u0003\u0003\u0003\u0003") + "\u0007\u0003%\n\u0003\f\u0003\u000e\u0003(\u000b\u0003\u0003\u0003\u0003\u0003\u0003\u0004\u0005\u0004-\n\u0004\u0003\u0004\u0003\u0004\u0003\u0004\u0003\u0004\u0003\u0004\u0005\u0004") + "4\n\u0004\u0003\u0004\u0005\u00047\n\u0004\u0007\u00049\n\u0004\f\u0004\u000e\u0004<\u000b\u0004\u0003\u0005\u0003\u0005\u0003\u0005\u0007\u0005A\n\u0005\f") + "\u0005\u000e\u0005D\u000b\u0005\u0003\u0005\u0003\u0005\u0003\u0005\u0003\u0005\u0003\u0005\u0003\u0005\u0003\u0005\u0003\u0005\u0003\u0005\u0003\u0005\u0007\u0005P\n\u0005\f\u0005\u000e") + "\u0005S\u000b\u0005\u0003\u0005\u0005\u0005V\n\u0005\u0003\u0006\u0003\u0006\u0003\u0007\u0003\u0007\u0003\u0007\u0003\u0007\u0003\b\u0003\b\u0003\t\u0003\t\u0003\t\u0002\u0002") + "\u0002\n\u0002\u0002\u0004\u0002\u0006\u0002\b\u0002\n\u0002\f\u0002\u000e\u0002\u0010\u0002\u0002\u0005\u0003\u0002\u0006\u0007\u0004\u0002\b\b\u000b\u000b\u0005\u0002\u0003") + "\u0003\b\b\u0014\u0014g\u0002\u0013\u0003\u0002\u0002\u0002\u0004\"\u0003\u0002\u0002\u0002\u0006,\u0003\u0002\u0002\u0002\bU\u0003\u0002\u0002\u0002\nW\u0003\u0002\u0002") + "\u0002\fY\u0003\u0002\u0002\u0002\u000e]\u0003\u0002\u0002\u0002\u0010_\u0003\u0002\u0002\u0002\u0012\u0014\u0005\u0004\u0003\u0002\u0013\u0012\u0003\u0002\u0002\u0002\u0013\u0014") + "\u0003\u0002\u0002\u0002\u0014\u0018\u0003\u0002\u0002\u0002\u0015\u0017\u0005\u0010\t\u0002\u0016\u0015\u0003\u0002\u0002\u0002\u0017\u001a\u0003\u0002\u0002\u0002\u0018\u0016") + "\u0003\u0002\u0002\u0002\u0018\u0019\u0003\u0002\u0002\u0002\u0019\u001b\u0003\u0002\u0002\u0002\u001a\u0018\u0003\u0002\u0002\u0002\u001b\u001f\u0005\b\u0005\u0002\u001c\u001e") + "\u0005\u0010\t\u0002\u001d\u001c\u0003\u0002\u0002\u0002\u001e!\u0003\u0002\u0002\u0002\u001f\u001d\u0003\u0002\u0002\u0002\u001f \u0003\u0002\u0002\u0002 \u0003\u0003\u0002\u0002") + "\u0002!\u001f\u0003\u0002\u0002\u0002\"&\u0007\n\u0002\u0002#%\u0005\f\u0007\u0002$#\u0003\u0002\u0002\u0002%(\u0003\u0002\u0002\u0002&$\u0003\u0002\u0002\u0002&\'\u0003") + "\u0002\u0002\u0002\')\u0003\u0002\u0002\u0002(&\u0003\u0002\u0002\u0002)*\u0007\r\u0002\u0002*\u0005\u0003\u0002\u0002\u0002+-\u0005\u000e\b\u0002,+\u0003\u0002\u0002\u0002") + ",-\u0003\u0002\u0002\u0002-:\u0003\u0002\u0002\u0002.4\u0005\b\u0005\u0002/4\u0005\n\u0006\u000204\u0007\u0004\u0002\u000214\u0007\u0014") + "\u0002\u000224\u0007\u0003\u0002\u00023.\u0003\u0002\u0002\u00023/\u0003\u0002\u0002\u000230\u0003\u0002\u0002\u000231\u0003\u0002\u0002\u0002") + "32\u0003\u0002\u0002\u000246\u0003\u0002\u0002\u000257\u0005\u000e\b\u000265\u0003\u0002\u0002\u000267\u0003\u0002\u0002") + "\u000279\u0003\u0002\u0002\u000283\u0003\u0002\u0002\u00029<\u0003\u0002\u0002\u0002:8\u0003\u0002\u0002\u0002:;\u0003\u0002\u0002\u0002;\u0007\u0003\u0002\u0002\u0002<:") + "\u0003\u0002\u0002\u0002=>\u0007\t\u0002\u0002>B\u0007\u0012\u0002\u0002?A\u0005\f\u0007\u0002@?\u0003\u0002\u0002\u0002AD\u0003\u0002\u0002\u0002B@\u0003\u0002\u0002\u0002") + "BC\u0003\u0002\u0002\u0002CE\u0003\u0002\u0002\u0002DB\u0003\u0002\u0002\u0002EF\u0007\f\u0002\u0002FG\u0005\u0006\u0004\u0002GH\u0007\t\u0002\u0002HI\u0007\u000f\u0002") + "\u0002IJ\u0007\u0012\u0002\u0002JK\u0007\f\u0002\u0002KV\u0003\u0002\u0002\u0002LM\u0007\t\u0002\u0002MQ\u0007\u0012\u0002\u0002NP\u0005\f\u0007\u0002ON\u0003\u0002") + "\u0002\u0002PS\u0003\u0002\u0002\u0002QO\u0003\u0002\u0002\u0002QR\u0003\u0002\u0002\u0002RT\u0003\u0002\u0002\u0002SQ\u0003\u0002\u0002\u0002TV\u0007\u000e\u0002\u0002U=\u0003") + "\u0002\u0002\u0002UL\u0003\u0002\u0002\u0002V\t\u0003\u0002\u0002\u0002WX\t\u0002\u0002\u0002X\u000b\u0003\u0002\u0002\u0002YZ\u0007\u0012\u0002\u0002Z[\u0007\u0010\u0002") + "\u0002[\\\u0007\u0011\u0002\u0002\\\r\u0003\u0002\u0002\u0002]^\t\u0003\u0002\u0002^\u000f\u0003\u0002\u0002\u0002_`\t\u0004\u0002\u0002`\u0011\u0003\u0002\u0002\u0002") + "\r\u0013\u0018\u001f&,36:BQU");

    public static final android.databinding.parser.ATN _ATN = new android.databinding.parser.ATNDeserializer().deserialize(android.databinding.parser.XMLParser._serializedATN.toCharArray());

    static {
    }
}

