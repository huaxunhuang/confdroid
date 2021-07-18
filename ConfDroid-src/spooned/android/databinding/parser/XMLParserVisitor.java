/**
 * Generated from XMLParser.g4 by ANTLR 4.4
 */
package android.databinding.parser;


/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link XMLParser}.
 *
 * @param <Result>
 * 		The return type of the visit operation. Use {@link Void} for
 * 		operations with no return type.
 */
public interface XMLParserVisitor<Result> extends org.antlr.v4.runtime.tree.ParseTreeVisitor<Result> {
    /**
     * Visit a parse tree produced by {@link XMLParser#content}.
     *
     * @param ctx
     * 		the parse tree
     * @return the visitor result
     */
    Result visitContent(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.XMLParser.ContentContext ctx);

    /**
     * Visit a parse tree produced by {@link XMLParser#element}.
     *
     * @param ctx
     * 		the parse tree
     * @return the visitor result
     */
    Result visitElement(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.XMLParser.ElementContext ctx);

    /**
     * Visit a parse tree produced by {@link XMLParser#prolog}.
     *
     * @param ctx
     * 		the parse tree
     * @return the visitor result
     */
    Result visitProlog(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.XMLParser.PrologContext ctx);

    /**
     * Visit a parse tree produced by {@link XMLParser#document}.
     *
     * @param ctx
     * 		the parse tree
     * @return the visitor result
     */
    Result visitDocument(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.XMLParser.DocumentContext ctx);

    /**
     * Visit a parse tree produced by {@link XMLParser#attribute}.
     *
     * @param ctx
     * 		the parse tree
     * @return the visitor result
     */
    Result visitAttribute(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.XMLParser.AttributeContext ctx);

    /**
     * Visit a parse tree produced by {@link XMLParser#chardata}.
     *
     * @param ctx
     * 		the parse tree
     * @return the visitor result
     */
    Result visitChardata(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.XMLParser.ChardataContext ctx);

    /**
     * Visit a parse tree produced by {@link XMLParser#reference}.
     *
     * @param ctx
     * 		the parse tree
     * @return the visitor result
     */
    Result visitReference(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.XMLParser.ReferenceContext ctx);

    /**
     * Visit a parse tree produced by {@link XMLParser#misc}.
     *
     * @param ctx
     * 		the parse tree
     * @return the visitor result
     */
    Result visitMisc(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.XMLParser.MiscContext ctx);
}

