/**
 * Generated from XMLParser.g4 by ANTLR 4.4
 */
package android.databinding.parser;


/**
 * This class provides an empty implementation of {@link XMLParserVisitor},
 * which can be extended to create a visitor which only needs to handle a subset
 * of the available methods.
 *
 * @param <Result>
 * 		The return type of the visit operation. Use {@link Void} for
 * 		operations with no return type.
 */
public class XMLParserBaseVisitor<Result> extends org.antlr.v4.runtime.tree.AbstractParseTreeVisitor<Result> implements android.databinding.parser.XMLParserVisitor<Result> {
    /**
     * {@inheritDoc }
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @java.lang.Override
    public Result visitContent(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.XMLParser.ContentContext ctx) {
        return android.databinding.parser.XMLParserBaseVisitor.visitChildren(ctx);
    }

    /**
     * {@inheritDoc }
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @java.lang.Override
    public Result visitElement(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.XMLParser.ElementContext ctx) {
        return android.databinding.parser.XMLParserBaseVisitor.visitChildren(ctx);
    }

    /**
     * {@inheritDoc }
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @java.lang.Override
    public Result visitProlog(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.XMLParser.PrologContext ctx) {
        return android.databinding.parser.XMLParserBaseVisitor.visitChildren(ctx);
    }

    /**
     * {@inheritDoc }
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @java.lang.Override
    public Result visitDocument(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.XMLParser.DocumentContext ctx) {
        return android.databinding.parser.XMLParserBaseVisitor.visitChildren(ctx);
    }

    /**
     * {@inheritDoc }
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @java.lang.Override
    public Result visitAttribute(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.XMLParser.AttributeContext ctx) {
        return android.databinding.parser.XMLParserBaseVisitor.visitChildren(ctx);
    }

    /**
     * {@inheritDoc }
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @java.lang.Override
    public Result visitChardata(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.XMLParser.ChardataContext ctx) {
        return android.databinding.parser.XMLParserBaseVisitor.visitChildren(ctx);
    }

    /**
     * {@inheritDoc }
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @java.lang.Override
    public Result visitReference(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.XMLParser.ReferenceContext ctx) {
        return android.databinding.parser.XMLParserBaseVisitor.visitChildren(ctx);
    }

    /**
     * {@inheritDoc }
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @java.lang.Override
    public Result visitMisc(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.XMLParser.MiscContext ctx) {
        return android.databinding.parser.XMLParserBaseVisitor.visitChildren(ctx);
    }
}

