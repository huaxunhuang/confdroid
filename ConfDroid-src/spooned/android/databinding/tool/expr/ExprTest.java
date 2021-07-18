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
package android.databinding.tool.expr;


public class ExprTest {
    private static class DummyExpr extends android.databinding.tool.expr.Expr {
        java.lang.String mKey;

        public DummyExpr(java.lang.String key, android.databinding.tool.expr.ExprTest.DummyExpr... children) {
            super(children);
            mKey = key;
        }

        @java.lang.Override
        protected android.databinding.tool.reflection.ModelClass resolveType(android.databinding.tool.reflection.ModelAnalyzer modelAnalyzer) {
            return modelAnalyzer.findClass(java.lang.Integer.class);
        }

        @java.lang.Override
        protected java.util.List<android.databinding.tool.expr.Dependency> constructDependencies() {
            return constructDynamicChildrenDependencies();
        }

        @java.lang.Override
        protected java.lang.String computeUniqueKey() {
            return mKey + super.computeUniqueKey();
        }

        @java.lang.Override
        protected android.databinding.tool.writer.KCode generateCode(boolean full) {
            return new android.databinding.tool.writer.KCode();
        }

        @java.lang.Override
        protected java.lang.String getInvertibleError() {
            return null;
        }

        @java.lang.Override
        public boolean isDynamic() {
            return true;
        }
    }

    @org.junit.Before
    public void setUp() throws java.lang.Exception {
        android.databinding.tool.reflection.java.JavaAnalyzer.initForTests();
    }

    @org.junit.Test(expected = java.lang.Throwable.class)
    public void testBadExpr() {
        android.databinding.tool.expr.Expr expr = new android.databinding.tool.expr.Expr() {
            @java.lang.Override
            protected android.databinding.tool.reflection.ModelClass resolveType(android.databinding.tool.reflection.ModelAnalyzer modelAnalyzer) {
                return modelAnalyzer.findClass(java.lang.Integer.class);
            }

            @java.lang.Override
            protected java.util.List<android.databinding.tool.expr.Dependency> constructDependencies() {
                return constructDynamicChildrenDependencies();
            }

            @java.lang.Override
            protected android.databinding.tool.writer.KCode generateCode(boolean full) {
                return new android.databinding.tool.writer.KCode();
            }

            @java.lang.Override
            protected java.lang.String getInvertibleError() {
                return null;
            }
        };
        expr.getUniqueKey();
    }

    @org.junit.Test
    public void testBasicInvalidationFlag() {
        android.databinding.tool.LayoutBinder lb = new android.databinding.tool.MockLayoutBinder();
        android.databinding.tool.expr.ExprModel model = lb.getModel();
        model.seal();
        android.databinding.tool.expr.ExprTest.DummyExpr d = new android.databinding.tool.expr.ExprTest.DummyExpr("a");
        d.setModel(model);
        d.setId(3);
        d.enableDirectInvalidation();
        org.junit.Assert.assertTrue(d.getInvalidFlags().get(3));
        java.util.BitSet clone = ((java.util.BitSet) (model.getInvalidateAnyBitSet().clone()));
        clone.and(d.getInvalidFlags());
        org.junit.Assert.assertEquals(1, clone.cardinality());
    }

    @org.junit.Test
    public void testCannotBeInvalidated() {
        android.databinding.tool.LayoutBinder lb = new android.databinding.tool.MockLayoutBinder();
        android.databinding.tool.expr.ExprModel model = lb.getModel();
        model.seal();
        android.databinding.tool.expr.ExprTest.DummyExpr d = new android.databinding.tool.expr.ExprTest.DummyExpr("a");
        d.setModel(model);
        d.setId(3);
        // +1 for invalidate all flag
        org.junit.Assert.assertEquals(1, d.getInvalidFlags().cardinality());
        org.junit.Assert.assertEquals(model.getInvalidateAnyBitSet(), d.getInvalidFlags());
    }

    @org.junit.Test
    public void testInvalidationInheritance() {
        android.databinding.tool.expr.ExprModel model = new android.databinding.tool.expr.ExprModel();
        android.databinding.tool.expr.ExprTest.DummyExpr a = model.register(new android.databinding.tool.expr.ExprTest.DummyExpr("a"));
        android.databinding.tool.expr.ExprTest.DummyExpr b = model.register(new android.databinding.tool.expr.ExprTest.DummyExpr("b"));
        android.databinding.tool.expr.ExprTest.DummyExpr c = model.register(new android.databinding.tool.expr.ExprTest.DummyExpr("c", a, b));
        a.enableDirectInvalidation();
        b.enableDirectInvalidation();
        c.setBindingExpression(true);
        model.seal();
        assertFlags(c, a, b);
    }

    @org.junit.Test
    public void testInvalidationInheritance2() {
        android.databinding.tool.expr.ExprModel model = new android.databinding.tool.expr.ExprModel();
        android.databinding.tool.expr.ExprTest.DummyExpr a = model.register(new android.databinding.tool.expr.ExprTest.DummyExpr("a"));
        android.databinding.tool.expr.ExprTest.DummyExpr b = model.register(new android.databinding.tool.expr.ExprTest.DummyExpr("b", a));
        android.databinding.tool.expr.ExprTest.DummyExpr c = model.register(new android.databinding.tool.expr.ExprTest.DummyExpr("c", b));
        a.enableDirectInvalidation();
        b.enableDirectInvalidation();
        c.setBindingExpression(true);
        model.seal();
        assertFlags(c, a, b);
    }

    @org.junit.Test
    public void testShouldReadFlags() {
        android.databinding.tool.expr.ExprModel model = new android.databinding.tool.expr.ExprModel();
        android.databinding.tool.expr.ExprTest.DummyExpr a = model.register(new android.databinding.tool.expr.ExprTest.DummyExpr("a"));
        a.enableDirectInvalidation();
        a.setBindingExpression(true);
        model.seal();
        assertFlags(a, a);
    }

    @org.junit.Test
    public void testShouldReadDependencyFlags() {
        android.databinding.tool.expr.ExprModel model = new android.databinding.tool.expr.ExprModel();
        android.databinding.tool.expr.ExprTest.DummyExpr a = model.register(new android.databinding.tool.expr.ExprTest.DummyExpr("a"));
        android.databinding.tool.expr.ExprTest.DummyExpr b = model.register(new android.databinding.tool.expr.ExprTest.DummyExpr("b", a));
        android.databinding.tool.expr.ExprTest.DummyExpr c = model.register(new android.databinding.tool.expr.ExprTest.DummyExpr("c", b));
        a.enableDirectInvalidation();
        b.enableDirectInvalidation();
        b.setBindingExpression(true);
        c.setBindingExpression(true);
        model.seal();
        assertFlags(b, a, b);
        assertFlags(c, a, b);
    }

    private void assertFlags(android.databinding.tool.expr.Expr a, android.databinding.tool.expr.Expr... exprs) {
        java.util.BitSet bitSet = a.getShouldReadFlags();
        for (android.databinding.tool.expr.Expr expr : exprs) {
            java.util.BitSet clone = ((java.util.BitSet) (bitSet.clone()));
            clone.and(expr.getInvalidFlags());
            org.junit.Assert.assertEquals((("should read flags of " + a.getUniqueKey()) + " should include ") + expr.getUniqueKey(), expr.getInvalidFlags(), clone);
        }
        java.util.BitSet composite = new java.util.BitSet();
        for (android.databinding.tool.expr.Expr expr : exprs) {
            composite.or(expr.getInvalidFlags());
        }
        org.junit.Assert.assertEquals("composite flags should match", composite, bitSet);
    }
}

