/**
 * Generated from BindingExpression.g4 by ANTLR 4.5
 */
package android.databinding.parser;


/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link BindingExpressionParser}.
 */
public interface BindingExpressionListener extends org.antlr.v4.runtime.tree.ParseTreeListener {
    /**
     * Enter a parse tree produced by the {@code BracketOp}
     * labeled alternative in {@link BindingExpressionParser#expression}.
     *
     * @param ctx
     * 		the parse tree
     */
    void enterBracketOp(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.BracketOpContext ctx);

    /**
     * Exit a parse tree produced by the {@code BracketOp}
     * labeled alternative in {@link BindingExpressionParser#expression}.
     *
     * @param ctx
     * 		the parse tree
     */
    void exitBracketOp(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.BracketOpContext ctx);

    /**
     * Enter a parse tree produced by the {@code Resource}
     * labeled alternative in {@link BindingExpressionParser#expression}.
     *
     * @param ctx
     * 		the parse tree
     */
    void enterResource(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.ResourceContext ctx);

    /**
     * Exit a parse tree produced by the {@code Resource}
     * labeled alternative in {@link BindingExpressionParser#expression}.
     *
     * @param ctx
     * 		the parse tree
     */
    void exitResource(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.ResourceContext ctx);

    /**
     * Enter a parse tree produced by the {@code CastOp}
     * labeled alternative in {@link BindingExpressionParser#expression}.
     *
     * @param ctx
     * 		the parse tree
     */
    void enterCastOp(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.CastOpContext ctx);

    /**
     * Exit a parse tree produced by the {@code CastOp}
     * labeled alternative in {@link BindingExpressionParser#expression}.
     *
     * @param ctx
     * 		the parse tree
     */
    void exitCastOp(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.CastOpContext ctx);

    /**
     * Enter a parse tree produced by the {@code UnaryOp}
     * labeled alternative in {@link BindingExpressionParser#expression}.
     *
     * @param ctx
     * 		the parse tree
     */
    void enterUnaryOp(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.UnaryOpContext ctx);

    /**
     * Exit a parse tree produced by the {@code UnaryOp}
     * labeled alternative in {@link BindingExpressionParser#expression}.
     *
     * @param ctx
     * 		the parse tree
     */
    void exitUnaryOp(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.UnaryOpContext ctx);

    /**
     * Enter a parse tree produced by the {@code AndOrOp}
     * labeled alternative in {@link BindingExpressionParser#expression}.
     *
     * @param ctx
     * 		the parse tree
     */
    void enterAndOrOp(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.AndOrOpContext ctx);

    /**
     * Exit a parse tree produced by the {@code AndOrOp}
     * labeled alternative in {@link BindingExpressionParser#expression}.
     *
     * @param ctx
     * 		the parse tree
     */
    void exitAndOrOp(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.AndOrOpContext ctx);

    /**
     * Enter a parse tree produced by the {@code MethodInvocation}
     * labeled alternative in {@link BindingExpressionParser#expression}.
     *
     * @param ctx
     * 		the parse tree
     */
    void enterMethodInvocation(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.MethodInvocationContext ctx);

    /**
     * Exit a parse tree produced by the {@code MethodInvocation}
     * labeled alternative in {@link BindingExpressionParser#expression}.
     *
     * @param ctx
     * 		the parse tree
     */
    void exitMethodInvocation(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.MethodInvocationContext ctx);

    /**
     * Enter a parse tree produced by the {@code Primary}
     * labeled alternative in {@link BindingExpressionParser#expression}.
     *
     * @param ctx
     * 		the parse tree
     */
    void enterPrimary(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.PrimaryContext ctx);

    /**
     * Exit a parse tree produced by the {@code Primary}
     * labeled alternative in {@link BindingExpressionParser#expression}.
     *
     * @param ctx
     * 		the parse tree
     */
    void exitPrimary(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.PrimaryContext ctx);

    /**
     * Enter a parse tree produced by the {@code Grouping}
     * labeled alternative in {@link BindingExpressionParser#expression}.
     *
     * @param ctx
     * 		the parse tree
     */
    void enterGrouping(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.GroupingContext ctx);

    /**
     * Exit a parse tree produced by the {@code Grouping}
     * labeled alternative in {@link BindingExpressionParser#expression}.
     *
     * @param ctx
     * 		the parse tree
     */
    void exitGrouping(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.GroupingContext ctx);

    /**
     * Enter a parse tree produced by the {@code TernaryOp}
     * labeled alternative in {@link BindingExpressionParser#expression}.
     *
     * @param ctx
     * 		the parse tree
     */
    void enterTernaryOp(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.TernaryOpContext ctx);

    /**
     * Exit a parse tree produced by the {@code TernaryOp}
     * labeled alternative in {@link BindingExpressionParser#expression}.
     *
     * @param ctx
     * 		the parse tree
     */
    void exitTernaryOp(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.TernaryOpContext ctx);

    /**
     * Enter a parse tree produced by the {@code ComparisonOp}
     * labeled alternative in {@link BindingExpressionParser#expression}.
     *
     * @param ctx
     * 		the parse tree
     */
    void enterComparisonOp(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.ComparisonOpContext ctx);

    /**
     * Exit a parse tree produced by the {@code ComparisonOp}
     * labeled alternative in {@link BindingExpressionParser#expression}.
     *
     * @param ctx
     * 		the parse tree
     */
    void exitComparisonOp(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.ComparisonOpContext ctx);

    /**
     * Enter a parse tree produced by the {@code DotOp}
     * labeled alternative in {@link BindingExpressionParser#expression}.
     *
     * @param ctx
     * 		the parse tree
     */
    void enterDotOp(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.DotOpContext ctx);

    /**
     * Exit a parse tree produced by the {@code DotOp}
     * labeled alternative in {@link BindingExpressionParser#expression}.
     *
     * @param ctx
     * 		the parse tree
     */
    void exitDotOp(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.DotOpContext ctx);

    /**
     * Enter a parse tree produced by the {@code MathOp}
     * labeled alternative in {@link BindingExpressionParser#expression}.
     *
     * @param ctx
     * 		the parse tree
     */
    void enterMathOp(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.MathOpContext ctx);

    /**
     * Exit a parse tree produced by the {@code MathOp}
     * labeled alternative in {@link BindingExpressionParser#expression}.
     *
     * @param ctx
     * 		the parse tree
     */
    void exitMathOp(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.MathOpContext ctx);

    /**
     * Enter a parse tree produced by the {@code QuestionQuestionOp}
     * labeled alternative in {@link BindingExpressionParser#expression}.
     *
     * @param ctx
     * 		the parse tree
     */
    void enterQuestionQuestionOp(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.QuestionQuestionOpContext ctx);

    /**
     * Exit a parse tree produced by the {@code QuestionQuestionOp}
     * labeled alternative in {@link BindingExpressionParser#expression}.
     *
     * @param ctx
     * 		the parse tree
     */
    void exitQuestionQuestionOp(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.QuestionQuestionOpContext ctx);

    /**
     * Enter a parse tree produced by the {@code BitShiftOp}
     * labeled alternative in {@link BindingExpressionParser#expression}.
     *
     * @param ctx
     * 		the parse tree
     */
    void enterBitShiftOp(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.BitShiftOpContext ctx);

    /**
     * Exit a parse tree produced by the {@code BitShiftOp}
     * labeled alternative in {@link BindingExpressionParser#expression}.
     *
     * @param ctx
     * 		the parse tree
     */
    void exitBitShiftOp(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.BitShiftOpContext ctx);

    /**
     * Enter a parse tree produced by the {@code InstanceOfOp}
     * labeled alternative in {@link BindingExpressionParser#expression}.
     *
     * @param ctx
     * 		the parse tree
     */
    void enterInstanceOfOp(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.InstanceOfOpContext ctx);

    /**
     * Exit a parse tree produced by the {@code InstanceOfOp}
     * labeled alternative in {@link BindingExpressionParser#expression}.
     *
     * @param ctx
     * 		the parse tree
     */
    void exitInstanceOfOp(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.InstanceOfOpContext ctx);

    /**
     * Enter a parse tree produced by the {@code BinaryOp}
     * labeled alternative in {@link BindingExpressionParser#expression}.
     *
     * @param ctx
     * 		the parse tree
     */
    void enterBinaryOp(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.BinaryOpContext ctx);

    /**
     * Exit a parse tree produced by the {@code BinaryOp}
     * labeled alternative in {@link BindingExpressionParser#expression}.
     *
     * @param ctx
     * 		the parse tree
     */
    void exitBinaryOp(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.BinaryOpContext ctx);

    /**
     * Enter a parse tree produced by {@link BindingExpressionParser#bindingSyntax}.
     *
     * @param ctx
     * 		the parse tree
     */
    void enterBindingSyntax(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.BindingSyntaxContext ctx);

    /**
     * Exit a parse tree produced by {@link BindingExpressionParser#bindingSyntax}.
     *
     * @param ctx
     * 		the parse tree
     */
    void exitBindingSyntax(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.BindingSyntaxContext ctx);

    /**
     * Enter a parse tree produced by {@link BindingExpressionParser#defaults}.
     *
     * @param ctx
     * 		the parse tree
     */
    void enterDefaults(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.DefaultsContext ctx);

    /**
     * Exit a parse tree produced by {@link BindingExpressionParser#defaults}.
     *
     * @param ctx
     * 		the parse tree
     */
    void exitDefaults(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.DefaultsContext ctx);

    /**
     * Enter a parse tree produced by {@link BindingExpressionParser#constantValue}.
     *
     * @param ctx
     * 		the parse tree
     */
    void enterConstantValue(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.ConstantValueContext ctx);

    /**
     * Exit a parse tree produced by {@link BindingExpressionParser#constantValue}.
     *
     * @param ctx
     * 		the parse tree
     */
    void exitConstantValue(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.ConstantValueContext ctx);

    /**
     * Enter a parse tree produced by {@link BindingExpressionParser#expression}.
     *
     * @param ctx
     * 		the parse tree
     */
    void enterExpression(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.ExpressionContext ctx);

    /**
     * Exit a parse tree produced by {@link BindingExpressionParser#expression}.
     *
     * @param ctx
     * 		the parse tree
     */
    void exitExpression(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.ExpressionContext ctx);

    /**
     * Enter a parse tree produced by {@link BindingExpressionParser#classExtraction}.
     *
     * @param ctx
     * 		the parse tree
     */
    void enterClassExtraction(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.ClassExtractionContext ctx);

    /**
     * Exit a parse tree produced by {@link BindingExpressionParser#classExtraction}.
     *
     * @param ctx
     * 		the parse tree
     */
    void exitClassExtraction(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.ClassExtractionContext ctx);

    /**
     * Enter a parse tree produced by {@link BindingExpressionParser#expressionList}.
     *
     * @param ctx
     * 		the parse tree
     */
    void enterExpressionList(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.ExpressionListContext ctx);

    /**
     * Exit a parse tree produced by {@link BindingExpressionParser#expressionList}.
     *
     * @param ctx
     * 		the parse tree
     */
    void exitExpressionList(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.ExpressionListContext ctx);

    /**
     * Enter a parse tree produced by {@link BindingExpressionParser#literal}.
     *
     * @param ctx
     * 		the parse tree
     */
    void enterLiteral(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.LiteralContext ctx);

    /**
     * Exit a parse tree produced by {@link BindingExpressionParser#literal}.
     *
     * @param ctx
     * 		the parse tree
     */
    void exitLiteral(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.LiteralContext ctx);

    /**
     * Enter a parse tree produced by {@link BindingExpressionParser#identifier}.
     *
     * @param ctx
     * 		the parse tree
     */
    void enterIdentifier(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.IdentifierContext ctx);

    /**
     * Exit a parse tree produced by {@link BindingExpressionParser#identifier}.
     *
     * @param ctx
     * 		the parse tree
     */
    void exitIdentifier(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.IdentifierContext ctx);

    /**
     * Enter a parse tree produced by {@link BindingExpressionParser#javaLiteral}.
     *
     * @param ctx
     * 		the parse tree
     */
    void enterJavaLiteral(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.JavaLiteralContext ctx);

    /**
     * Exit a parse tree produced by {@link BindingExpressionParser#javaLiteral}.
     *
     * @param ctx
     * 		the parse tree
     */
    void exitJavaLiteral(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.JavaLiteralContext ctx);

    /**
     * Enter a parse tree produced by {@link BindingExpressionParser#stringLiteral}.
     *
     * @param ctx
     * 		the parse tree
     */
    void enterStringLiteral(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.StringLiteralContext ctx);

    /**
     * Exit a parse tree produced by {@link BindingExpressionParser#stringLiteral}.
     *
     * @param ctx
     * 		the parse tree
     */
    void exitStringLiteral(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.StringLiteralContext ctx);

    /**
     * Enter a parse tree produced by {@link BindingExpressionParser#explicitGenericInvocation}.
     *
     * @param ctx
     * 		the parse tree
     */
    void enterExplicitGenericInvocation(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.ExplicitGenericInvocationContext ctx);

    /**
     * Exit a parse tree produced by {@link BindingExpressionParser#explicitGenericInvocation}.
     *
     * @param ctx
     * 		the parse tree
     */
    void exitExplicitGenericInvocation(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.ExplicitGenericInvocationContext ctx);

    /**
     * Enter a parse tree produced by {@link BindingExpressionParser#typeArguments}.
     *
     * @param ctx
     * 		the parse tree
     */
    void enterTypeArguments(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.TypeArgumentsContext ctx);

    /**
     * Exit a parse tree produced by {@link BindingExpressionParser#typeArguments}.
     *
     * @param ctx
     * 		the parse tree
     */
    void exitTypeArguments(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.TypeArgumentsContext ctx);

    /**
     * Enter a parse tree produced by {@link BindingExpressionParser#type}.
     *
     * @param ctx
     * 		the parse tree
     */
    void enterType(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.TypeContext ctx);

    /**
     * Exit a parse tree produced by {@link BindingExpressionParser#type}.
     *
     * @param ctx
     * 		the parse tree
     */
    void exitType(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.TypeContext ctx);

    /**
     * Enter a parse tree produced by {@link BindingExpressionParser#explicitGenericInvocationSuffix}.
     *
     * @param ctx
     * 		the parse tree
     */
    void enterExplicitGenericInvocationSuffix(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.ExplicitGenericInvocationSuffixContext ctx);

    /**
     * Exit a parse tree produced by {@link BindingExpressionParser#explicitGenericInvocationSuffix}.
     *
     * @param ctx
     * 		the parse tree
     */
    void exitExplicitGenericInvocationSuffix(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.ExplicitGenericInvocationSuffixContext ctx);

    /**
     * Enter a parse tree produced by {@link BindingExpressionParser#arguments}.
     *
     * @param ctx
     * 		the parse tree
     */
    void enterArguments(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.ArgumentsContext ctx);

    /**
     * Exit a parse tree produced by {@link BindingExpressionParser#arguments}.
     *
     * @param ctx
     * 		the parse tree
     */
    void exitArguments(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.ArgumentsContext ctx);

    /**
     * Enter a parse tree produced by {@link BindingExpressionParser#classOrInterfaceType}.
     *
     * @param ctx
     * 		the parse tree
     */
    void enterClassOrInterfaceType(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.ClassOrInterfaceTypeContext ctx);

    /**
     * Exit a parse tree produced by {@link BindingExpressionParser#classOrInterfaceType}.
     *
     * @param ctx
     * 		the parse tree
     */
    void exitClassOrInterfaceType(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.ClassOrInterfaceTypeContext ctx);

    /**
     * Enter a parse tree produced by {@link BindingExpressionParser#primitiveType}.
     *
     * @param ctx
     * 		the parse tree
     */
    void enterPrimitiveType(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.PrimitiveTypeContext ctx);

    /**
     * Exit a parse tree produced by {@link BindingExpressionParser#primitiveType}.
     *
     * @param ctx
     * 		the parse tree
     */
    void exitPrimitiveType(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.PrimitiveTypeContext ctx);

    /**
     * Enter a parse tree produced by {@link BindingExpressionParser#resources}.
     *
     * @param ctx
     * 		the parse tree
     */
    void enterResources(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.ResourcesContext ctx);

    /**
     * Exit a parse tree produced by {@link BindingExpressionParser#resources}.
     *
     * @param ctx
     * 		the parse tree
     */
    void exitResources(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.ResourcesContext ctx);

    /**
     * Enter a parse tree produced by {@link BindingExpressionParser#resourceParameters}.
     *
     * @param ctx
     * 		the parse tree
     */
    void enterResourceParameters(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.ResourceParametersContext ctx);

    /**
     * Exit a parse tree produced by {@link BindingExpressionParser#resourceParameters}.
     *
     * @param ctx
     * 		the parse tree
     */
    void exitResourceParameters(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.ResourceParametersContext ctx);
}

