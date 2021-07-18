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
package android.databinding;


public class BindingExpressionParserTest {
    @org.junit.Test
    public void testSingleQuoteStringLiteral() throws java.lang.Exception {
        java.lang.String expr = "`test`";
        android.databinding.parser.BindingExpressionParser.LiteralContext literal = parseLiteral(expr);
        org.junit.Assert.assertNotNull(literal);
        android.databinding.parser.BindingExpressionParser.StringLiteralContext stringLiteral = literal.stringLiteral();
        org.junit.Assert.assertNotNull(stringLiteral);
        org.antlr.v4.runtime.tree.TerminalNode singleQuote = stringLiteral.SingleQuoteString();
        org.antlr.v4.runtime.Token token = singleQuote.getSymbol();
        org.junit.Assert.assertEquals("`test`", token.getText());
    }

    @org.junit.Test
    public void testDoubleQuoteStringLiteral() throws java.lang.Exception {
        java.lang.String expr = "\"test\"";
        android.databinding.parser.BindingExpressionParser.LiteralContext literal = parseLiteral(expr);
        android.databinding.parser.BindingExpressionParser.StringLiteralContext stringLiteral = literal.stringLiteral();
        org.antlr.v4.runtime.tree.TerminalNode singleQuote = stringLiteral.DoubleQuoteString();
        org.antlr.v4.runtime.Token token = singleQuote.getSymbol();
        org.junit.Assert.assertEquals("\"test\"", token.getText());
    }

    @org.junit.Test
    public void testSingleQuoteEscapeStringLiteral() throws java.lang.Exception {
        java.lang.String expr = "`\"t\\`est\"`";
        android.databinding.parser.BindingExpressionParser.LiteralContext literal = parseLiteral(expr);
        android.databinding.parser.BindingExpressionParser.StringLiteralContext stringLiteral = literal.stringLiteral();
        org.antlr.v4.runtime.tree.TerminalNode singleQuote = stringLiteral.SingleQuoteString();
        org.antlr.v4.runtime.Token token = singleQuote.getSymbol();
        org.junit.Assert.assertEquals("`\"t\\`est\"`", token.getText());
    }

    @org.junit.Test
    public void testCharLiteral() throws java.lang.Exception {
        android.databinding.parser.BindingExpressionParser.LiteralContext literal = parseLiteral("'c'");
        org.junit.Assert.assertEquals("'c'", literal.getText());
        literal = parseLiteral("\'\\u0054\'");
        org.junit.Assert.assertEquals("\'\\u0054\'", literal.getText());
        literal = parseLiteral("\'\\\'\'");
        org.junit.Assert.assertEquals("\'\\\'\'", literal.getText());
    }

    @org.junit.Test
    public void testIntLiterals() throws java.lang.Exception {
        compareIntLiteral("123");
        compareIntLiteral("123l");
        compareIntLiteral("1_2_3l");
        compareIntLiteral("123L");
        compareIntLiteral("0xdeadbeef");
        compareIntLiteral("0xdeadbeefl");
        compareIntLiteral("0Xdeadbeef");
        compareIntLiteral("0xdead_beefl");
        compareIntLiteral("0xdead_beefL");
        compareIntLiteral("01234567");
        compareIntLiteral("01234567L");
        compareIntLiteral("01234567l");
        compareIntLiteral("0123_45_67l");
        compareIntLiteral("0b0101");
        compareIntLiteral("0b0101_0101");
        compareIntLiteral("0B0101_0101");
        compareIntLiteral("0B0101_0101L");
        compareIntLiteral("0B0101_0101l");
    }

    @org.junit.Test
    public void testFloatLiterals() throws java.lang.Exception {
        compareFloatLiteral("0.12345");
        compareFloatLiteral("0.12345f");
        compareFloatLiteral("0.12345F");
        compareFloatLiteral("132450.12345F");
        compareFloatLiteral("132450.12345");
        compareFloatLiteral("132450e123");
        compareFloatLiteral("132450.4e123");
    }

    @org.junit.Test
    public void testBoolLiterals() throws java.lang.Exception {
        compareBoolLiteral("true");
        compareBoolLiteral("false");
    }

    @org.junit.Test
    public void testNullLiteral() throws java.lang.Exception {
        android.databinding.parser.BindingExpressionParser.LiteralContext literal = parseLiteral("null");
        java.lang.String token = literal.getText();
        org.junit.Assert.assertEquals("null", token);
    }

    @org.junit.Test
    public void testVoidExtraction() throws java.lang.Exception {
        android.databinding.parser.BindingExpressionParser.PrimaryContext primary = parsePrimary("void.class");
        org.junit.Assert.assertNotNull(primary.classExtraction());
        org.junit.Assert.assertNull(primary.classExtraction().type());
        org.junit.Assert.assertEquals("void", primary.classExtraction().getChild(0).getText());
    }

    @org.junit.Test
    public void testPrimitiveClassExtraction() throws java.lang.Exception {
        android.databinding.parser.BindingExpressionParser.PrimaryContext primary = parsePrimary("int.class");
        android.databinding.parser.BindingExpressionParser.PrimitiveTypeContext type = primary.classExtraction().type().primitiveType();
        org.junit.Assert.assertEquals("int", type.getText());
    }

    @org.junit.Test
    public void testIdentifier() throws java.lang.Exception {
        android.databinding.parser.BindingExpressionParser.PrimaryContext primary = parsePrimary("abcdEfg");
        org.junit.Assert.assertEquals("abcdEfg", primary.identifier().getText());
    }

    @org.junit.Test
    public void testUnaryOperators() throws java.lang.Exception {
        compareUnaryOperators("+");
        compareUnaryOperators("-");
        compareUnaryOperators("!");
        compareUnaryOperators("~");
    }

    @org.junit.Test
    public void testMathOperators() throws java.lang.Exception {
        compareMathOperators("+");
        compareMathOperators("-");
        compareMathOperators("*");
        compareMathOperators("/");
        compareMathOperators("%");
    }

    @org.junit.Test
    public void testBitShiftOperators() throws java.lang.Exception {
        compareBitShiftOperators(">>>");
        compareBitShiftOperators("<<");
        compareBitShiftOperators(">>");
    }

    @org.junit.Test
    public void testComparisonShiftOperators() throws java.lang.Exception {
        compareComparisonOperators("<");
        compareComparisonOperators(">");
        compareComparisonOperators("<=");
        compareComparisonOperators(">=");
        compareComparisonOperators("==");
        compareComparisonOperators("!=");
    }

    @org.junit.Test
    public void testAndOrOperators() throws java.lang.Exception {
        compareAndOrOperators("&&");
        compareAndOrOperators("||");
    }

    @org.junit.Test
    public void testBinaryOperators() throws java.lang.Exception {
        compareBinaryOperators("&");
        compareBinaryOperators("|");
        compareBinaryOperators("^");
    }

    @org.junit.Test
    public void testTernaryOperator() throws java.lang.Exception {
        android.databinding.parser.BindingExpressionParser.TernaryOpContext expression = parseExpression("true ? 1 : 0");
        org.junit.Assert.assertEquals(5, expression.getChildCount());
        org.junit.Assert.assertEquals("true", ((android.databinding.parser.BindingExpressionParser.PrimaryContext) (expression.left)).literal().javaLiteral().getText());
        org.junit.Assert.assertEquals("?", expression.op.getText());
        org.junit.Assert.assertEquals("1", ((android.databinding.parser.BindingExpressionParser.PrimaryContext) (expression.iftrue)).literal().javaLiteral().getText());
        org.junit.Assert.assertEquals(":", expression.getChild(3).getText());
        org.junit.Assert.assertEquals("0", ((android.databinding.parser.BindingExpressionParser.PrimaryContext) (expression.iffalse)).literal().javaLiteral().getText());
    }

    @org.junit.Test
    public void testDot() throws java.lang.Exception {
        android.databinding.parser.BindingExpressionParser.DotOpContext expression = parseExpression("one.two.three");
        org.junit.Assert.assertEquals(3, expression.getChildCount());
        org.junit.Assert.assertEquals("three", expression.Identifier().getText());
        org.junit.Assert.assertEquals(".", expression.getChild(1).getText());
        android.databinding.parser.BindingExpressionParser.DotOpContext left = ((android.databinding.parser.BindingExpressionParser.DotOpContext) (expression.expression()));
        org.junit.Assert.assertEquals("two", left.Identifier().getText());
        org.junit.Assert.assertEquals(".", left.getChild(1).getText());
        org.junit.Assert.assertEquals("one", ((android.databinding.parser.BindingExpressionParser.PrimaryContext) (left.expression())).identifier().getText());
    }

    @org.junit.Test
    public void testQuestionQuestion() throws java.lang.Exception {
        android.databinding.parser.BindingExpressionParser.QuestionQuestionOpContext expression = parseExpression("one ?? two");
        org.junit.Assert.assertEquals(3, expression.getChildCount());
        org.junit.Assert.assertEquals("one", ((android.databinding.parser.BindingExpressionParser.PrimaryContext) (expression.left)).identifier().getText());
        org.junit.Assert.assertEquals("two", ((android.databinding.parser.BindingExpressionParser.PrimaryContext) (expression.right)).identifier().getText());
        org.junit.Assert.assertEquals("??", expression.op.getText());
    }

    @org.junit.Test
    public void testResourceReference() throws java.lang.Exception {
        compareResource("@id/foo_bar");
        compareResource("@transition/foo_bar");
        compareResource("@anim/foo_bar");
        compareResource("@animator/foo_bar");
        compareResource("@android:id/foo_bar");
        compareResource("@app:id/foo_bar");
    }

    @org.junit.Test
    public void testDefaults() throws java.lang.Exception {
        android.databinding.parser.BindingExpressionParser.BindingSyntaxContext syntax = parseExpressionString("foo.bar, default = @id/foo_bar");
        android.databinding.parser.BindingExpressionParser.DefaultsContext defaults = syntax.defaults();
        org.junit.Assert.assertEquals("@id/foo_bar", defaults.constantValue().ResourceReference().getText());
    }

    @org.junit.Test
    public void testParentheses() throws java.lang.Exception {
        android.databinding.parser.BindingExpressionParser.GroupingContext grouping = parseExpression("(1234)");
        org.junit.Assert.assertEquals("1234", grouping.expression().getText());
    }

    // ---------------------- Helpers --------------------
    private void compareResource(java.lang.String value) throws java.lang.Exception {
        android.databinding.parser.BindingExpressionParser.ResourceContext resourceContext = parseExpression(value);
        org.junit.Assert.assertEquals(value, resourceContext.getText());
    }

    private void compareUnaryOperators(java.lang.String op) throws java.lang.Exception {
        android.databinding.parser.BindingExpressionParser.UnaryOpContext expression = parseExpression(op + " 2");
        org.junit.Assert.assertEquals(2, expression.getChildCount());
        org.junit.Assert.assertEquals(op, expression.op.getText());
        org.junit.Assert.assertEquals("2", ((android.databinding.parser.BindingExpressionParser.PrimaryContext) (expression.expression())).literal().javaLiteral().getText());
    }

    private void compareBinaryOperators(java.lang.String op) throws java.lang.Exception {
        android.databinding.parser.BindingExpressionParser.BinaryOpContext expression = parseExpression(("1 " + op) + " 2");
        org.junit.Assert.assertEquals(3, expression.getChildCount());
        org.junit.Assert.assertTrue(expression.left instanceof android.databinding.parser.BindingExpressionParser.ExpressionContext);
        java.lang.String one = ((android.databinding.parser.BindingExpressionParser.PrimaryContext) (expression.left)).literal().javaLiteral().getText();
        org.junit.Assert.assertEquals("1", one);
        org.junit.Assert.assertEquals(op, expression.op.getText());
        org.junit.Assert.assertTrue(expression.right instanceof android.databinding.parser.BindingExpressionParser.ExpressionContext);
        java.lang.String two = ((android.databinding.parser.BindingExpressionParser.PrimaryContext) (expression.right)).literal().javaLiteral().getText();
        org.junit.Assert.assertEquals("2", two);
    }

    private void compareMathOperators(java.lang.String op) throws java.lang.Exception {
        android.databinding.parser.BindingExpressionParser.MathOpContext expression = parseExpression(("1 " + op) + " 2");
        org.junit.Assert.assertEquals(3, expression.getChildCount());
        org.junit.Assert.assertTrue(expression.left instanceof android.databinding.parser.BindingExpressionParser.ExpressionContext);
        java.lang.String one = ((android.databinding.parser.BindingExpressionParser.PrimaryContext) (expression.left)).literal().javaLiteral().getText();
        org.junit.Assert.assertEquals("1", one);
        org.junit.Assert.assertEquals(op, expression.op.getText());
        org.junit.Assert.assertTrue(expression.right instanceof android.databinding.parser.BindingExpressionParser.ExpressionContext);
        java.lang.String two = ((android.databinding.parser.BindingExpressionParser.PrimaryContext) (expression.right)).literal().javaLiteral().getText();
        org.junit.Assert.assertEquals("2", two);
    }

    private void compareBitShiftOperators(java.lang.String op) throws java.lang.Exception {
        android.databinding.parser.BindingExpressionParser.BitShiftOpContext expression = parseExpression(("1 " + op) + " 2");
        org.junit.Assert.assertEquals(3, expression.getChildCount());
        org.junit.Assert.assertTrue(expression.left instanceof android.databinding.parser.BindingExpressionParser.ExpressionContext);
        java.lang.String one = ((android.databinding.parser.BindingExpressionParser.PrimaryContext) (expression.left)).literal().javaLiteral().getText();
        org.junit.Assert.assertEquals("1", one);
        org.junit.Assert.assertEquals(op, expression.op.getText());
        org.junit.Assert.assertTrue(expression.right instanceof android.databinding.parser.BindingExpressionParser.ExpressionContext);
        java.lang.String two = ((android.databinding.parser.BindingExpressionParser.PrimaryContext) (expression.right)).literal().javaLiteral().getText();
        org.junit.Assert.assertEquals("2", two);
    }

    private void compareComparisonOperators(java.lang.String op) throws java.lang.Exception {
        android.databinding.parser.BindingExpressionParser.ComparisonOpContext expression = parseExpression(("1 " + op) + " 2");
        org.junit.Assert.assertEquals(3, expression.getChildCount());
        org.junit.Assert.assertTrue(expression.left instanceof android.databinding.parser.BindingExpressionParser.ExpressionContext);
        java.lang.String one = ((android.databinding.parser.BindingExpressionParser.PrimaryContext) (expression.left)).literal().javaLiteral().getText();
        org.junit.Assert.assertEquals("1", one);
        org.junit.Assert.assertEquals(op, expression.op.getText());
        org.junit.Assert.assertTrue(expression.right instanceof android.databinding.parser.BindingExpressionParser.ExpressionContext);
        java.lang.String two = ((android.databinding.parser.BindingExpressionParser.PrimaryContext) (expression.right)).literal().javaLiteral().getText();
        org.junit.Assert.assertEquals("2", two);
    }

    private void compareAndOrOperators(java.lang.String op) throws java.lang.Exception {
        android.databinding.parser.BindingExpressionParser.AndOrOpContext expression = parseExpression(("1 " + op) + " 2");
        org.junit.Assert.assertEquals(3, expression.getChildCount());
        org.junit.Assert.assertTrue(expression.left instanceof android.databinding.parser.BindingExpressionParser.ExpressionContext);
        java.lang.String one = ((android.databinding.parser.BindingExpressionParser.PrimaryContext) (expression.left)).literal().javaLiteral().getText();
        org.junit.Assert.assertEquals("1", one);
        org.junit.Assert.assertEquals(op, expression.op.getText());
        org.junit.Assert.assertTrue(expression.right instanceof android.databinding.parser.BindingExpressionParser.ExpressionContext);
        java.lang.String two = ((android.databinding.parser.BindingExpressionParser.PrimaryContext) (expression.right)).literal().javaLiteral().getText();
        org.junit.Assert.assertEquals("2", two);
    }

    private void compareIntLiteral(java.lang.String constant) throws java.lang.Exception {
        android.databinding.parser.BindingExpressionParser.LiteralContext literal = parseLiteral(constant);
        java.lang.String token = literal.javaLiteral().getText();
        org.junit.Assert.assertEquals(constant, token);
    }

    private void compareFloatLiteral(java.lang.String constant) throws java.lang.Exception {
        android.databinding.parser.BindingExpressionParser.LiteralContext literal = parseLiteral(constant);
        java.lang.String token = literal.javaLiteral().getText();
        org.junit.Assert.assertEquals(constant, token);
    }

    private void compareBoolLiteral(java.lang.String constant) throws java.lang.Exception {
        android.databinding.parser.BindingExpressionParser.LiteralContext literal = parseLiteral(constant);
        java.lang.String token = literal.javaLiteral().getText();
        org.junit.Assert.assertEquals(constant, token);
    }

    private android.databinding.parser.BindingExpressionParser.BindingSyntaxContext parse(java.lang.String value) throws java.lang.Exception {
        return parseExpressionString(value);
    }

    private <T extends android.databinding.parser.BindingExpressionParser.ExpressionContext> T parseExpression(java.lang.String value) throws java.lang.Exception {
        android.databinding.parser.BindingExpressionParser.ExpressionContext expressionContext = parse(value).expression();
        return ((T) (expressionContext));
    }

    private android.databinding.parser.BindingExpressionParser.PrimaryContext parsePrimary(java.lang.String value) throws java.lang.Exception {
        return parseExpression(value);
    }

    private android.databinding.parser.BindingExpressionParser.LiteralContext parseLiteral(java.lang.String value) throws java.lang.Exception {
        return parsePrimary(value).literal();
    }

    android.databinding.parser.BindingExpressionParser.BindingSyntaxContext parseExpressionString(java.lang.String s) throws java.lang.Exception {
        org.antlr.v4.runtime.ANTLRInputStream input = new org.antlr.v4.runtime.ANTLRInputStream(new java.io.StringReader(s));
        android.databinding.parser.BindingExpressionLexer lexer = new android.databinding.parser.BindingExpressionLexer(input);
        org.antlr.v4.runtime.CommonTokenStream tokens = new org.antlr.v4.runtime.CommonTokenStream(lexer);
        android.databinding.parser.BindingExpressionParser parser = new android.databinding.parser.BindingExpressionParser(tokens);
        return parser.bindingSyntax();
    }
}

