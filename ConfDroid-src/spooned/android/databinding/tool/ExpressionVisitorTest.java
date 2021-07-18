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


public class ExpressionVisitorTest {
    android.databinding.tool.ExpressionParser mParser = new android.databinding.tool.ExpressionParser(new android.databinding.tool.expr.ExprModel());

    @org.junit.Before
    public void setUp() throws java.lang.Exception {
        android.databinding.tool.reflection.java.JavaAnalyzer.initForTests();
    }

    private <T extends android.databinding.tool.expr.Expr> T parse(java.lang.String input, java.lang.Class<T> klass) {
        final android.databinding.tool.expr.Expr parsed = mParser.parse(input, null);
        org.junit.Assert.assertSame(klass, parsed.getClass());
        return ((T) (parsed));
    }

    @org.junit.Test
    public void testSymbol() {
        final android.databinding.tool.expr.SymbolExpr res = parse("null", android.databinding.tool.expr.SymbolExpr.class);
        org.junit.Assert.assertEquals(1, mParser.getModel().size());
        org.junit.Assert.assertEquals("null", res.getText());
        org.junit.Assert.assertEquals(new android.databinding.tool.reflection.java.JavaClass(java.lang.Object.class), res.getResolvedType());
        org.junit.Assert.assertEquals(0, res.getDependencies().size());
    }

    @org.junit.runner.RunWith(org.junit.runners.Parameterized.class)
    public static class ComparisonExprTests {
        android.databinding.tool.ExpressionParser mParser = new android.databinding.tool.ExpressionParser(new android.databinding.tool.expr.ExprModel());

        private final java.lang.String mOp;

        @org.junit.Before
        public void setUp() throws java.lang.Exception {
            android.databinding.tool.reflection.java.JavaAnalyzer.initForTests();
        }

        @org.junit.runners.Parameterized.Parameters
        public static java.util.List<java.lang.Object[]> data() {
            return java.util.Arrays.asList(new java.lang.Object[][]{ new java.lang.Object[]{ "==" }, new java.lang.Object[]{ "<=" }, new java.lang.Object[]{ ">=" }, new java.lang.Object[]{ ">" }, new java.lang.Object[]{ "<" } });
        }

        public ComparisonExprTests(java.lang.String op) {
            mOp = op;
        }

        @org.junit.Test
        public void testComparison() {
            final android.databinding.tool.expr.Expr res = mParser.parse(("3 " + mOp) + " 5", null);
            org.junit.Assert.assertEquals(3, mParser.getModel().size());
            org.junit.Assert.assertTrue(res instanceof android.databinding.tool.expr.ComparisonExpr);
            // 0 because they are both static
            org.junit.Assert.assertEquals(0, res.getDependencies().size());
        }
    }

    @org.junit.Test
    public void testSimpleFieldAccess() {
        final android.databinding.tool.expr.FieldAccessExpr expr = parse("a.b", android.databinding.tool.expr.FieldAccessExpr.class);
        org.junit.Assert.assertEquals(2, mParser.mModel.size());
        org.junit.Assert.assertEquals("b", expr.getName());
        org.junit.Assert.assertEquals(1, expr.getChildren().size());
        final android.databinding.tool.expr.Expr parent = expr.getChildren().get(0);
        org.junit.Assert.assertTrue(parent instanceof android.databinding.tool.expr.IdentifierExpr);
        final android.databinding.tool.expr.IdentifierExpr id = ((android.databinding.tool.expr.IdentifierExpr) (parent));
        org.junit.Assert.assertEquals("a", id.getName());
        org.junit.Assert.assertEquals(0, id.getDependencies().size());
        org.junit.Assert.assertEquals(1, expr.getDependencies().size());
    }

    @org.junit.Test
    public void testIdentifier() {
        final android.databinding.tool.expr.IdentifierExpr id = parse("myStr", android.databinding.tool.expr.IdentifierExpr.class);
        org.junit.Assert.assertEquals(1, mParser.mModel.size());
        org.junit.Assert.assertEquals("myStr", id.getName());
        id.setUserDefinedType("java.lang.String");
        org.junit.Assert.assertEquals(new android.databinding.tool.reflection.java.JavaClass(java.lang.String.class), id.getResolvedType());
    }

    @org.junit.Test
    public void testTernary() {
        final android.databinding.tool.expr.TernaryExpr parsed = parse("a > b ? 5 : 4", android.databinding.tool.expr.TernaryExpr.class);
        org.junit.Assert.assertEquals(6, mParser.getModel().size());
        org.junit.Assert.assertTrue(parsed.getPred() instanceof android.databinding.tool.expr.ComparisonExpr);
        org.junit.Assert.assertTrue(parsed.getIfTrue() instanceof android.databinding.tool.expr.SymbolExpr);
        org.junit.Assert.assertTrue(parsed.getIfFalse() instanceof android.databinding.tool.expr.SymbolExpr);
        android.databinding.tool.expr.ComparisonExpr pred = ((android.databinding.tool.expr.ComparisonExpr) (parsed.getPred()));
        android.databinding.tool.expr.SymbolExpr ifTrue = ((android.databinding.tool.expr.SymbolExpr) (parsed.getIfTrue()));
        android.databinding.tool.expr.SymbolExpr ifFalse = ((android.databinding.tool.expr.SymbolExpr) (parsed.getIfFalse()));
        org.junit.Assert.assertEquals("5", ifTrue.getText());
        org.junit.Assert.assertEquals("4", ifFalse.getText());
        org.junit.Assert.assertEquals(1, parsed.getDependencies().size());
        for (android.databinding.tool.expr.Dependency dependency : parsed.getDependencies()) {
            org.junit.Assert.assertEquals(dependency.getOther() != pred, dependency.isConditional());
        }
    }

    @org.junit.Test
    public void testInheritedFieldResolution() {
        final android.databinding.tool.expr.FieldAccessExpr parsed = parse("myStr.length", android.databinding.tool.expr.FieldAccessExpr.class);
        org.junit.Assert.assertTrue(parsed.getChild() instanceof android.databinding.tool.expr.IdentifierExpr);
        final android.databinding.tool.expr.IdentifierExpr id = ((android.databinding.tool.expr.IdentifierExpr) (parsed.getChild()));
        id.setUserDefinedType("java.lang.String");
        org.junit.Assert.assertEquals(new android.databinding.tool.reflection.java.JavaClass(int.class), parsed.getResolvedType());
        android.databinding.tool.reflection.Callable getter = parsed.getGetter();
        org.junit.Assert.assertEquals(android.databinding.tool.reflection.Callable.Type.METHOD, getter.type);
        org.junit.Assert.assertEquals("length", getter.name);
        org.junit.Assert.assertEquals(1, parsed.getDependencies().size());
        final android.databinding.tool.expr.Dependency dep = parsed.getDependencies().get(0);
        org.junit.Assert.assertSame(id, dep.getOther());
        org.junit.Assert.assertFalse(dep.isConditional());
    }

    @org.junit.Test
    public void testGetterResolution() {
        final android.databinding.tool.expr.FieldAccessExpr parsed = parse("myStr.bytes", android.databinding.tool.expr.FieldAccessExpr.class);
        org.junit.Assert.assertTrue(parsed.getChild() instanceof android.databinding.tool.expr.IdentifierExpr);
        final android.databinding.tool.expr.IdentifierExpr id = ((android.databinding.tool.expr.IdentifierExpr) (parsed.getChild()));
        id.setUserDefinedType("java.lang.String");
        org.junit.Assert.assertEquals(new android.databinding.tool.reflection.java.JavaClass(byte[].class), parsed.getResolvedType());
        android.databinding.tool.reflection.Callable getter = parsed.getGetter();
        org.junit.Assert.assertEquals(android.databinding.tool.reflection.Callable.Type.METHOD, getter.type);
        org.junit.Assert.assertEquals("getBytes", getter.name);
        org.junit.Assert.assertEquals(1, parsed.getDependencies().size());
        final android.databinding.tool.expr.Dependency dep = parsed.getDependencies().get(0);
        org.junit.Assert.assertSame(id, dep.getOther());
        org.junit.Assert.assertFalse(dep.isConditional());
    }

    @org.junit.Test
    public void testMethodCall() {
        final android.databinding.tool.expr.MethodCallExpr parsed = parse("user.getName()", android.databinding.tool.expr.MethodCallExpr.class);
        org.junit.Assert.assertTrue(parsed.getTarget() instanceof android.databinding.tool.expr.IdentifierExpr);
        org.junit.Assert.assertEquals("getName", parsed.getName());
        org.junit.Assert.assertEquals(0, parsed.getArgs().size());
        org.junit.Assert.assertEquals(1, parsed.getDependencies().size());
        final android.databinding.tool.expr.Dependency dep = parsed.getDependencies().get(0);
        org.junit.Assert.assertSame(mParser.parse("user", null), dep.getOther());
        org.junit.Assert.assertFalse(dep.isConditional());
    }

    @org.junit.Test
    public void testMethodCallWithArgs() {
        final android.databinding.tool.expr.MethodCallExpr parsed = parse("str.substring(1, a)", android.databinding.tool.expr.MethodCallExpr.class);
        org.junit.Assert.assertTrue(parsed.getTarget() instanceof android.databinding.tool.expr.IdentifierExpr);
        org.junit.Assert.assertEquals("substring", parsed.getName());
        final java.util.List<android.databinding.tool.expr.Expr> args = parsed.getArgs();
        org.junit.Assert.assertEquals(2, args.size());
        org.junit.Assert.assertTrue(args.get(0) instanceof android.databinding.tool.expr.SymbolExpr);
        org.junit.Assert.assertTrue(args.get(1) instanceof android.databinding.tool.expr.IdentifierExpr);
        final java.util.List<android.databinding.tool.expr.Dependency> deps = parsed.getDependencies();
        org.junit.Assert.assertEquals(2, deps.size());
    }
}

