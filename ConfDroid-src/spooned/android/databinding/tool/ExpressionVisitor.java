/**
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package android.databinding.tool;


public class ExpressionVisitor extends android.databinding.parser.BindingExpressionBaseVisitor<android.databinding.tool.expr.Expr> {
    private final android.databinding.tool.expr.ExprModel mModel;

    private org.antlr.v4.runtime.tree.ParseTreeListener mParseTreeListener;

    public ExpressionVisitor(android.databinding.tool.expr.ExprModel model) {
        mModel = model;
    }

    public void setParseTreeListener(org.antlr.v4.runtime.tree.ParseTreeListener parseTreeListener) {
        mParseTreeListener = parseTreeListener;
    }

    private void onEnter(org.antlr.v4.runtime.ParserRuleContext context) {
        if (mParseTreeListener != null) {
            mParseTreeListener.enterEveryRule(context);
        }
    }

    private void onExit(org.antlr.v4.runtime.ParserRuleContext context) {
        if (mParseTreeListener != null) {
            mParseTreeListener.exitEveryRule(context);
        }
    }

    @java.lang.Override
    public android.databinding.tool.expr.Expr visitStringLiteral(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.StringLiteralContext ctx) {
        try {
            onEnter(ctx);
            final java.lang.String javaString;
            if (ctx.SingleQuoteString() != null) {
                java.lang.String str = ctx.SingleQuoteString().getText();
                java.lang.String contents = str.substring(1, str.length() - 1);
                contents = contents.replace("\"", "\\\"").replace("\\`", "`");
                javaString = ('"' + contents) + '"';
            } else {
                javaString = ctx.DoubleQuoteString().getText();
            }
            return mModel.symbol(javaString, java.lang.String.class);
        } finally {
            onExit(ctx);
        }
    }

    @java.lang.Override
    public android.databinding.tool.expr.Expr visitGrouping(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.GroupingContext ctx) {
        try {
            onEnter(ctx);
            android.databinding.tool.util.Preconditions.check(ctx.children.size() == 3, "Grouping expression should have" + " 3 children. # of children: %d", ctx.children.size());
            return mModel.group(ctx.children.get(1).accept(this));
        } finally {
            onExit(ctx);
        }
    }

    @java.lang.Override
    public android.databinding.tool.expr.Expr visitBindingSyntax(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.BindingSyntaxContext ctx) {
        try {
            onEnter(ctx);
            // TODO handle defaults
            return mModel.bindingExpr(ctx.expression().accept(this));
        } catch (java.lang.Exception e) {
            java.lang.System.out.println("Error while parsing! " + ctx.getText());
            e.printStackTrace();
            throw new java.lang.RuntimeException(e);
        } finally {
            onExit(ctx);
        }
    }

    @java.lang.Override
    public android.databinding.tool.expr.Expr visitDotOp(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.DotOpContext ctx) {
        try {
            onEnter(ctx);
            android.databinding.tool.reflection.ModelAnalyzer analyzer = android.databinding.tool.reflection.ModelAnalyzer.getInstance();
            android.databinding.tool.reflection.ModelClass modelClass = analyzer.findClass(ctx.getText(), mModel.getImports());
            if (modelClass == null) {
                return mModel.field(ctx.expression().accept(this), ctx.Identifier().getSymbol().getText());
            } else {
                java.lang.String name = modelClass.toJavaCode();
                android.databinding.tool.expr.StaticIdentifierExpr expr = mModel.staticIdentifier(name);
                expr.setUserDefinedType(name);
                return expr;
            }
        } finally {
            onExit(ctx);
        }
    }

    @java.lang.Override
    public android.databinding.tool.expr.Expr visitQuestionQuestionOp(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.QuestionQuestionOpContext ctx) {
        try {
            onEnter(ctx);
            final android.databinding.tool.expr.Expr left = ctx.left.accept(this);
            return mModel.ternary(mModel.comparison("==", left, mModel.symbol("null", java.lang.Object.class)), ctx.right.accept(this), left);
        } finally {
            onExit(ctx);
        }
    }

    @java.lang.Override
    public android.databinding.tool.expr.Expr visitTerminal(@org.antlr.v4.runtime.misc.NotNull
    org.antlr.v4.runtime.tree.TerminalNode node) {
        try {
            onEnter(((org.antlr.v4.runtime.ParserRuleContext) (node.getParent().getRuleContext())));
            final int type = node.getSymbol().getType();
            java.lang.Class classType;
            switch (type) {
                case android.databinding.parser.BindingExpressionParser.IntegerLiteral :
                    classType = int.class;
                    break;
                case android.databinding.parser.BindingExpressionParser.FloatingPointLiteral :
                    classType = float.class;
                    break;
                case android.databinding.parser.BindingExpressionParser.BooleanLiteral :
                    classType = boolean.class;
                    break;
                case android.databinding.parser.BindingExpressionParser.CharacterLiteral :
                    classType = char.class;
                    break;
                case android.databinding.parser.BindingExpressionParser.SingleQuoteString :
                case android.databinding.parser.BindingExpressionParser.DoubleQuoteString :
                    classType = java.lang.String.class;
                    break;
                case android.databinding.parser.BindingExpressionParser.NullLiteral :
                    classType = java.lang.Object.class;
                    break;
                default :
                    throw new java.lang.RuntimeException("cannot create expression from terminal node " + node.toString());
            }
            return mModel.symbol(node.getText(), classType);
        } finally {
            onExit(((org.antlr.v4.runtime.ParserRuleContext) (node.getParent().getRuleContext())));
        }
    }

    @java.lang.Override
    public android.databinding.tool.expr.Expr visitComparisonOp(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.ComparisonOpContext ctx) {
        try {
            onEnter(ctx);
            return mModel.comparison(ctx.op.getText(), ctx.left.accept(this), ctx.right.accept(this));
        } finally {
            onExit(ctx);
        }
    }

    @java.lang.Override
    public android.databinding.tool.expr.Expr visitIdentifier(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.IdentifierContext ctx) {
        try {
            onEnter(ctx);
            return mModel.identifier(ctx.getText());
        } finally {
            onExit(ctx);
        }
    }

    @java.lang.Override
    public android.databinding.tool.expr.Expr visitTernaryOp(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.TernaryOpContext ctx) {
        try {
            onEnter(ctx);
            return mModel.ternary(ctx.left.accept(this), ctx.iftrue.accept(this), ctx.iffalse.accept(this));
        } finally {
            onExit(ctx);
        }
    }

    @java.lang.Override
    public android.databinding.tool.expr.Expr visitMethodInvocation(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.MethodInvocationContext ctx) {
        try {
            onEnter(ctx);
            java.util.List<android.databinding.tool.expr.Expr> args = new java.util.ArrayList<android.databinding.tool.expr.Expr>();
            if (ctx.args != null) {
                for (org.antlr.v4.runtime.tree.ParseTree item : ctx.args.children) {
                    if (com.google.common.base.Objects.equal(item.getText(), ",")) {
                        continue;
                    }
                    args.add(item.accept(this));
                }
            }
            return mModel.methodCall(ctx.target.accept(this), ctx.Identifier().getText(), args);
        } finally {
            onExit(ctx);
        }
    }

    @java.lang.Override
    public android.databinding.tool.expr.Expr visitMathOp(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.MathOpContext ctx) {
        try {
            onEnter(ctx);
            return mModel.math(ctx.left.accept(this), ctx.op.getText(), ctx.right.accept(this));
        } finally {
            onExit(ctx);
        }
    }

    @java.lang.Override
    public android.databinding.tool.expr.Expr visitAndOrOp(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.AndOrOpContext ctx) {
        try {
            onEnter(ctx);
            return mModel.logical(ctx.left.accept(this), ctx.op.getText(), ctx.right.accept(this));
        } finally {
            onExit(ctx);
        }
    }

    @java.lang.Override
    public android.databinding.tool.expr.Expr visitBinaryOp(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.BinaryOpContext ctx) {
        try {
            onEnter(ctx);
            return mModel.math(ctx.left.accept(this), ctx.op.getText(), ctx.right.accept(this));
        } finally {
            onExit(ctx);
        }
    }

    @java.lang.Override
    public android.databinding.tool.expr.Expr visitBitShiftOp(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.BitShiftOpContext ctx) {
        try {
            onEnter(ctx);
            return mModel.bitshift(ctx.left.accept(this), ctx.op.getText(), ctx.right.accept(this));
        } finally {
            onExit(ctx);
        }
    }

    @java.lang.Override
    public android.databinding.tool.expr.Expr visitInstanceOfOp(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.InstanceOfOpContext ctx) {
        try {
            onEnter(ctx);
            return mModel.instanceOfOp(ctx.expression().accept(this), ctx.type().getText());
        } finally {
            onExit(ctx);
        }
    }

    @java.lang.Override
    public android.databinding.tool.expr.Expr visitUnaryOp(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.UnaryOpContext ctx) {
        try {
            onEnter(ctx);
            return mModel.unary(ctx.op.getText(), ctx.expression().accept(this));
        } finally {
            onExit(ctx);
        }
    }

    @java.lang.Override
    public android.databinding.tool.expr.Expr visitResources(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.ResourcesContext ctx) {
        try {
            onEnter(ctx);
            final java.util.List<android.databinding.tool.expr.Expr> args = new java.util.ArrayList<android.databinding.tool.expr.Expr>();
            if (ctx.resourceParameters() != null) {
                for (org.antlr.v4.runtime.tree.ParseTree item : ctx.resourceParameters().expressionList().children) {
                    if (com.google.common.base.Objects.equal(item.getText(), ",")) {
                        continue;
                    }
                    args.add(item.accept(this));
                }
            }
            final java.lang.String resourceReference = ctx.ResourceReference().getText();
            final int colonIndex = resourceReference.indexOf(':');
            final int slashIndex = resourceReference.indexOf('/');
            final java.lang.String packageName = (colonIndex < 0) ? null : resourceReference.substring(1, colonIndex).trim();
            final int startIndex = java.lang.Math.max(1, colonIndex + 1);
            final java.lang.String resourceType = resourceReference.substring(startIndex, slashIndex).trim();
            final java.lang.String resourceName = resourceReference.substring(slashIndex + 1).trim();
            return mModel.resourceExpr(packageName, resourceType, resourceName, args);
        } finally {
            onExit(ctx);
        }
    }

    @java.lang.Override
    public android.databinding.tool.expr.Expr visitBracketOp(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.BracketOpContext ctx) {
        try {
            onEnter(ctx);
            return mModel.bracketExpr(visit(ctx.expression(0)), visit(ctx.expression(1)));
        } finally {
            onExit(ctx);
        }
    }

    @java.lang.Override
    public android.databinding.tool.expr.Expr visitCastOp(@org.antlr.v4.runtime.misc.NotNull
    android.databinding.parser.BindingExpressionParser.CastOpContext ctx) {
        try {
            onEnter(ctx);
            return mModel.castExpr(ctx.type().getText(), visit(ctx.expression()));
        } finally {
            onExit(ctx);
        }
    }
}

