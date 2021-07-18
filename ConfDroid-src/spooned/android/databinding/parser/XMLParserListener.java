/**
 * Generated from XMLParser.g4 by ANTLR 4.4
 */
package android.databinding.parser;


/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link XMLParser}.
 */
public interface XMLParserListener extends org.antlr.v4.runtime.tree.ParseTreeListener {
    /**
     * Enter a parse tree produced by {@link XMLParser#content}.
     *
     * @param ctx
     * 		the parse tree
     */
    void enterContent(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.XMLParser.ContentContext ctx);

    /**
     * Exit a parse tree produced by {@link XMLParser#content}.
     *
     * @param ctx
     * 		the parse tree
     */
    void exitContent(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.XMLParser.ContentContext ctx);

    /**
     * Enter a parse tree produced by {@link XMLParser#element}.
     *
     * @param ctx
     * 		the parse tree
     */
    void enterElement(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.XMLParser.ElementContext ctx);

    /**
     * Exit a parse tree produced by {@link XMLParser#element}.
     *
     * @param ctx
     * 		the parse tree
     */
    void exitElement(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.XMLParser.ElementContext ctx);

    /**
     * Enter a parse tree produced by {@link XMLParser#prolog}.
     *
     * @param ctx
     * 		the parse tree
     */
    void enterProlog(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.XMLParser.PrologContext ctx);

    /**
     * Exit a parse tree produced by {@link XMLParser#prolog}.
     *
     * @param ctx
     * 		the parse tree
     */
    void exitProlog(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.XMLParser.PrologContext ctx);

    /**
     * Enter a parse tree produced by {@link XMLParser#document}.
     *
     * @param ctx
     * 		the parse tree
     */
    void enterDocument(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.XMLParser.DocumentContext ctx);

    /**
     * Exit a parse tree produced by {@link XMLParser#document}.
     *
     * @param ctx
     * 		the parse tree
     */
    void exitDocument(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.XMLParser.DocumentContext ctx);

    /**
     * Enter a parse tree produced by {@link XMLParser#attribute}.
     *
     * @param ctx
     * 		the parse tree
     */
    void enterAttribute(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.XMLParser.AttributeContext ctx);

    /**
     * Exit a parse tree produced by {@link XMLParser#attribute}.
     *
     * @param ctx
     * 		the parse tree
     */
    void exitAttribute(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.XMLParser.AttributeContext ctx);

    /**
     * Enter a parse tree produced by {@link XMLParser#chardata}.
     *
     * @param ctx
     * 		the parse tree
     */
    void enterChardata(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.XMLParser.ChardataContext ctx);

    /**
     * Exit a parse tree produced by {@link XMLParser#chardata}.
     *
     * @param ctx
     * 		the parse tree
     */
    void exitChardata(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.XMLParser.ChardataContext ctx);

    /**
     * Enter a parse tree produced by {@link XMLParser#reference}.
     *
     * @param ctx
     * 		the parse tree
     */
    void enterReference(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.XMLParser.ReferenceContext ctx);

    /**
     * Exit a parse tree produced by {@link XMLParser#reference}.
     *
     * @param ctx
     * 		the parse tree
     */
    void exitReference(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.XMLParser.ReferenceContext ctx);

    /**
     * Enter a parse tree produced by {@link XMLParser#misc}.
     *
     * @param ctx
     * 		the parse tree
     */
    void enterMisc(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.XMLParser.MiscContext ctx);

    /**
     * Exit a parse tree produced by {@link XMLParser#misc}.
     *
     * @param ctx
     * 		the parse tree
     */
    void exitMisc(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.XMLParser.MiscContext ctx);
}

