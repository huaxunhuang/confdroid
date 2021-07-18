/**
 * Generated from BindingExpression.g4 by ANTLR 4.5
 */
package android.databinding.parser;


/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link BindingExpressionParser}.
 *
 * @param <Result>
 * 		The return type of the visit operation. Use {@link Void} for
 * 		operations with no return type.
 */
public interface BindingExpressionVisitor<Result> extends org.antlr.v4.runtime.tree.ParseTreeVisitor<Result> {
    /**
     * Visit a parse tree produced by the {@code BracketOp}
     * labeled alternative in {@link BindingExpressionParser#expression}.
     *
     * @param ctx
     * 		the parse tree
     * @return the visitor result
     */
    Result visitBracketOp(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.BracketOpContext ctx);

    /**
     * Visit a parse tree produced by the {@code Resource}
     * labeled alternative in {@link BindingExpressionParser#expression}.
     *
     * @param ctx
     * 		the parse tree
     * @return the visitor result
     */
    Result visitResource(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.ResourceContext ctx);

    /**
     * Visit a parse tree produced by the {@code CastOp}
     * labeled alternative in {@link BindingExpressionParser#expression}.
     *
     * @param ctx
     * 		the parse tree
     * @return the visitor result
     */
    Result visitCastOp(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.CastOpContext ctx);

    /**
     * Visit a parse tree produced by the {@code UnaryOp}
     * labeled alternative in {@link BindingExpressionParser#expression}.
     *
     * @param ctx
     * 		the parse tree
     * @return the visitor result
     */
    Result visitUnaryOp(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.UnaryOpContext ctx);

    /**
     * Visit a parse tree produced by the {@code AndOrOp}
     * labeled alternative in {@link BindingExpressionParser#expression}.
     *
     * @param ctx
     * 		the parse tree
     * @return the visitor result
     */
    Result visitAndOrOp(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.AndOrOpContext ctx);

    /**
     * Visit a parse tree produced by the {@code MethodInvocation}
     * labeled alternative in {@link BindingExpressionParser#expression}.
     *
     * @param ctx
     * 		the parse tree
     * @return the visitor result
     */
    Result visitMethodInvocation(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.MethodInvocationContext ctx);

    /**
     * Visit a parse tree produced by the {@code Primary}
     * labeled alternative in {@link BindingExpressionParser#expression}.
     *
     * @param ctx
     * 		the parse tree
     * @return the visitor result
     */
    Result visitPrimary(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.PrimaryContext ctx);

    /**
     * Visit a parse tree produced by the {@code Grouping}
     * labeled alternative in {@link BindingExpressionParser#expression}.
     *
     * @param ctx
     * 		the parse tree
     * @return the visitor result
     */
    Result visitGrouping(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.GroupingContext ctx);

    /**
     * Visit a parse tree produced by the {@code TernaryOp}
     * labeled alternative in {@link BindingExpressionParser#expression}.
     *
     * @param ctx
     * 		the parse tree
     * @return the visitor result
     */
    Result visitTernaryOp(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.TernaryOpContext ctx);

    /**
     * Visit a parse tree produced by the {@code ComparisonOp}
     * labeled alternative in {@link BindingExpressionParser#expression}.
     *
     * @param ctx
     * 		the parse tree
     * @return the visitor result
     */
    Result visitComparisonOp(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.ComparisonOpContext ctx);

    /**
     * Visit a parse tree produced by the {@code DotOp}
     * labeled alternative in {@link BindingExpressionParser#expression}.
     *
     * @param ctx
     * 		the parse tree
     * @return the visitor result
     */
    Result visitDotOp(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.DotOpContext ctx);

    /**
     * Visit a parse tree produced by the {@code MathOp}
     * labeled alternative in {@link BindingExpressionParser#expression}.
     *
     * @param ctx
     * 		the parse tree
     * @return the visitor result
     */
    Result visitMathOp(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.MathOpContext ctx);

    /**
     * Visit a parse tree produced by the {@code QuestionQuestionOp}
     * labeled alternative in {@link BindingExpressionParser#expression}.
     *
     * @param ctx
     * 		the parse tree
     * @return the visitor result
     */
    Result visitQuestionQuestionOp(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.QuestionQuestionOpContext ctx);

    /**
     * Visit a parse tree produced by the {@code BitShiftOp}
     * labeled alternative in {@link BindingExpressionParser#expression}.
     *
     * @param ctx
     * 		the parse tree
     * @return the visitor result
     */
    Result visitBitShiftOp(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.BitShiftOpContext ctx);

    /**
     * Visit a parse tree produced by the {@code InstanceOfOp}
     * labeled alternative in {@link BindingExpressionParser#expression}.
     *
     * @param ctx
     * 		the parse tree
     * @return the visitor result
     */
    Result visitInstanceOfOp(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.InstanceOfOpContext ctx);

    /**
     * Visit a parse tree produced by the {@code BinaryOp}
     * labeled alternative in {@link BindingExpressionParser#expression}.
     *
     * @param ctx
     * 		the parse tree
     * @return the visitor result
     */
    Result visitBinaryOp(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.BinaryOpContext ctx);

    /**
     * Visit a parse tree produced by {@link BindingExpressionParser#bindingSyntax}.
     *
     * @param ctx
     * 		the parse tree
     * @return the visitor result
     */
    Result visitBindingSyntax(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.BindingSyntaxContext ctx);

    /**
     * Visit a parse tree produced by {@link BindingExpressionParser#defaults}.
     *
     * @param ctx
     * 		the parse tree
     * @return the visitor result
     */
    Result visitDefaults(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.DefaultsContext ctx);

    /**
     * Visit a parse tree produced by {@link BindingExpressionParser#constantValue}.
     *
     * @param ctx
     * 		the parse tree
     * @return the visitor result
     */
    Result visitConstantValue(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.ConstantValueContext ctx);

    /**
     * Visit a parse tree produced by {@link BindingExpressionParser#expression}.
     *
     * @param ctx
     * 		the parse tree
     * @return the visitor result
     */
    Result visitExpression(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.ExpressionContext ctx);

    /**
     * Visit a parse tree produced by {@link BindingExpressionParser#classExtraction}.
     *
     * @param ctx
     * 		the parse tree
     * @return the visitor result
     */
    Result visitClassExtraction(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.ClassExtractionContext ctx);

    /**
     * Visit a parse tree produced by {@link BindingExpressionParser#expressionList}.
     *
     * @param ctx
     * 		the parse tree
     * @return the visitor result
     */
    Result visitExpressionList(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.ExpressionListContext ctx);

    /**
     * Visit a parse tree produced by {@link BindingExpressionParser#literal}.
     *
     * @param ctx
     * 		the parse tree
     * @return the visitor result
     */
    Result visitLiteral(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.LiteralContext ctx);

    /**
     * Visit a parse tree produced by {@link BindingExpressionParser#identifier}.
     *
     * @param ctx
     * 		the parse tree
     * @return the visitor result
     */
    Result visitIdentifier(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.IdentifierContext ctx);

    /**
     * Visit a parse tree produced by {@link BindingExpressionParser#javaLiteral}.
     *
     * @param ctx
     * 		the parse tree
     * @return the visitor result
     */
    Result visitJavaLiteral(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.JavaLiteralContext ctx);

    /**
     * Visit a parse tree produced by {@link BindingExpressionParser#stringLiteral}.
     *
     * @param ctx
     * 		the parse tree
     * @return the visitor result
     */
    Result visitStringLiteral(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.StringLiteralContext ctx);

    /**
     * Visit a parse tree produced by {@link BindingExpressionParser#explicitGenericInvocation}.
     *
     * @param ctx
     * 		the parse tree
     * @return the visitor result
     */
    Result visitExplicitGenericInvocation(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.ExplicitGenericInvocationContext ctx);

    /**
     * Visit a parse tree produced by {@link BindingExpressionParser#typeArguments}.
     *
     * @param ctx
     * 		the parse tree
     * @return the visitor result
     */
    Result visitTypeArguments(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.TypeArgumentsContext ctx);

    /**
     * Visit a parse tree produced by {@link BindingExpressionParser#type}.
     *
     * @param ctx
     * 		the parse tree
     * @return the visitor result
     */
    Result visitType(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.TypeContext ctx);

    /**
     * Visit a parse tree produced by {@link BindingExpressionParser#explicitGenericInvocationSuffix}.
     *
     * @param ctx
     * 		the parse tree
     * @return the visitor result
     */
    Result visitExplicitGenericInvocationSuffix(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.ExplicitGenericInvocationSuffixContext ctx);

    /**
     * Visit a parse tree produced by {@link BindingExpressionParser#arguments}.
     *
     * @param ctx
     * 		the parse tree
     * @return the visitor result
     */
    Result visitArguments(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.ArgumentsContext ctx);

    /**
     * Visit a parse tree produced by {@link BindingExpressionParser#classOrInterfaceType}.
     *
     * @param ctx
     * 		the parse tree
     * @return the visitor result
     */
    Result visitClassOrInterfaceType(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.ClassOrInterfaceTypeContext ctx);

    /**
     * Visit a parse tree produced by {@link BindingExpressionParser#primitiveType}.
     *
     * @param ctx
     * 		the parse tree
     * @return the visitor result
     */
    Result visitPrimitiveType(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.PrimitiveTypeContext ctx);

    /**
     * Visit a parse tree produced by {@link BindingExpressionParser#resources}.
     *
     * @param ctx
     * 		the parse tree
     * @return the visitor result
     */
    Result visitResources(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.ResourcesContext ctx);

    /**
     * Visit a parse tree produced by {@link BindingExpressionParser#resourceParameters}.
     *
     * @param ctx
     * 		the parse tree
     * @return the visitor result
     */
    Result visitResourceParameters(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.ResourceParametersContext ctx);
}

