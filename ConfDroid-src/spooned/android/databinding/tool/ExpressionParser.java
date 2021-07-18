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


public class ExpressionParser {
    final android.databinding.tool.expr.ExprModel mModel;

    final android.databinding.tool.ExpressionVisitor visitor;

    public ExpressionParser(android.databinding.tool.expr.ExprModel model) {
        mModel = model;
        visitor = new android.databinding.tool.ExpressionVisitor(mModel);
    }

    public android.databinding.tool.expr.Expr parse(java.lang.String input, @org.antlr.v4.runtime.misc.Nullable
    android.databinding.tool.store.Location locationInFile) {
        org.antlr.v4.runtime.ANTLRInputStream inputStream = new org.antlr.v4.runtime.ANTLRInputStream(input);
        android.databinding.parser.BindingExpressionLexer lexer = new android.databinding.parser.BindingExpressionLexer(inputStream);
        org.antlr.v4.runtime.CommonTokenStream tokenStream = new org.antlr.v4.runtime.CommonTokenStream(lexer);
        final android.databinding.parser.BindingExpressionParser parser = new android.databinding.parser.BindingExpressionParser(tokenStream);
        parser.addErrorListener(new org.antlr.v4.runtime.BaseErrorListener() {
            @java.lang.Override
            public <T extends org.antlr.v4.runtime.Token> void syntaxError(org.antlr.v4.runtime.Recognizer<T, ?> recognizer, @org.antlr.v4.runtime.misc.Nullable
            T offendingSymbol, int line, int charPositionInLine, java.lang.String msg, @org.antlr.v4.runtime.misc.Nullable
            org.antlr.v4.runtime.RecognitionException e) {
                android.databinding.tool.util.L.e(android.databinding.tool.processing.ErrorMessages.SYNTAX_ERROR, msg);
            }
        });
        android.databinding.parser.BindingExpressionParser.BindingSyntaxContext root = parser.bindingSyntax();
        try {
            mModel.setCurrentLocationInFile(locationInFile);
            visitor.setParseTreeListener(new org.antlr.v4.runtime.tree.ParseTreeListener() {
                java.util.List<org.antlr.v4.runtime.ParserRuleContext> mStack = new java.util.ArrayList<org.antlr.v4.runtime.ParserRuleContext>();

                @java.lang.Override
                public void visitTerminal(org.antlr.v4.runtime.tree.TerminalNode node) {
                }

                @java.lang.Override
                public void visitErrorNode(org.antlr.v4.runtime.tree.ErrorNode node) {
                }

                @java.lang.Override
                public void enterEveryRule(org.antlr.v4.runtime.ParserRuleContext ctx) {
                    mStack.add(ctx);
                    mModel.setCurrentParserContext(ctx);
                }

                @java.lang.Override
                public void exitEveryRule(org.antlr.v4.runtime.ParserRuleContext ctx) {
                    android.databinding.tool.util.Preconditions.check(ctx == mStack.get(mStack.size() - 1), "Inconsistent exit from context. Received %s, expecting %s", ctx.toInfoString(parser), mStack.get(mStack.size() - 1).toInfoString(parser));
                    mStack.remove(mStack.size() - 1);
                    if (mStack.size() > 0) {
                        mModel.setCurrentParserContext(mStack.get(mStack.size() - 1));
                    } else {
                        mModel.setCurrentParserContext(null);
                    }
                }
            });
            return root.accept(visitor);
        } finally {
            mModel.setCurrentLocationInFile(null);
        }
    }

    public android.databinding.tool.expr.ExprModel getModel() {
        return mModel;
    }
}

