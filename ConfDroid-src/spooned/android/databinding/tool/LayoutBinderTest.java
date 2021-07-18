/**
 * Copyright (C) 2015 The Android Open Source Project
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package android.databinding.tool;


public class LayoutBinderTest {
    android.databinding.tool.MockLayoutBinder mLayoutBinder;

    android.databinding.tool.expr.ExprModel mExprModel;

    @org.junit.Before
    public void setUp() throws java.lang.Exception {
        mLayoutBinder = new android.databinding.tool.MockLayoutBinder();
        mExprModel = mLayoutBinder.getModel();
        android.databinding.tool.reflection.java.JavaAnalyzer.initForTests();
    }

    @org.junit.Test
    public void testRegisterId() {
        int originalSize = mExprModel.size();
        mLayoutBinder.addVariable("test", "java.lang.String", null);
        org.junit.Assert.assertEquals(originalSize + 1, mExprModel.size());
        final java.util.Map.Entry<java.lang.String, android.databinding.tool.expr.Expr> entry = findIdentifier("test");
        final android.databinding.tool.expr.Expr value = entry.getValue();
        org.junit.Assert.assertEquals(value.getClass(), android.databinding.tool.expr.IdentifierExpr.class);
        final android.databinding.tool.expr.IdentifierExpr id = ((android.databinding.tool.expr.IdentifierExpr) (value));
        org.junit.Assert.assertEquals("test", id.getName());
        org.junit.Assert.assertEquals(new android.databinding.tool.reflection.java.JavaClass(java.lang.String.class), id.getResolvedType());
        org.junit.Assert.assertTrue(id.isDynamic());
    }

    @org.junit.Test
    public void testRegisterImport() {
        int originalSize = mExprModel.size();
        mExprModel.addImport("test", "java.lang.String", null);
        org.junit.Assert.assertEquals(originalSize + 1, mExprModel.size());
        final java.util.Map.Entry<java.lang.String, android.databinding.tool.expr.Expr> entry = findIdentifier("test");
        final android.databinding.tool.expr.Expr value = entry.getValue();
        org.junit.Assert.assertEquals(value.getClass(), android.databinding.tool.expr.StaticIdentifierExpr.class);
        final android.databinding.tool.expr.IdentifierExpr id = ((android.databinding.tool.expr.IdentifierExpr) (value));
        org.junit.Assert.assertEquals("test", id.getName());
        org.junit.Assert.assertEquals(new android.databinding.tool.reflection.java.JavaClass(java.lang.String.class), id.getResolvedType());
        org.junit.Assert.assertFalse(id.isDynamic());
    }

    @org.junit.Test
    public void testParse() {
        int originalSize = mExprModel.size();
        mLayoutBinder.addVariable("user", "android.databinding.tool2.LayoutBinderTest.TestUser", null);
        mLayoutBinder.parse("user.name", false, null);
        mLayoutBinder.parse("user.lastName", false, null);
        org.junit.Assert.assertEquals(originalSize + 3, mExprModel.size());
        final java.util.List<android.databinding.tool.expr.Expr> bindingExprs = mExprModel.getBindingExpressions();
        org.junit.Assert.assertEquals(2, bindingExprs.size());
        android.databinding.tool.expr.IdentifierExpr id = mExprModel.identifier("user");
        org.junit.Assert.assertTrue(bindingExprs.get(0) instanceof android.databinding.tool.expr.FieldAccessExpr);
        org.junit.Assert.assertTrue(bindingExprs.get(1) instanceof android.databinding.tool.expr.FieldAccessExpr);
        org.junit.Assert.assertEquals(2, id.getParents().size());
        org.junit.Assert.assertTrue(bindingExprs.get(0).getChildren().contains(id));
        org.junit.Assert.assertTrue(bindingExprs.get(1).getChildren().contains(id));
    }

    @org.junit.Test
    public void testParseWithMethods() {
        mLayoutBinder.addVariable("user", "android.databinding.tool.LayoutBinderTest.TestUser", null);
        mLayoutBinder.parse("user.fullName", false, null);
        android.databinding.tool.expr.Expr item = mExprModel.getBindingExpressions().get(0);
        org.junit.Assert.assertTrue(item instanceof android.databinding.tool.expr.FieldAccessExpr);
        android.databinding.tool.expr.IdentifierExpr id = mExprModel.identifier("user");
        android.databinding.tool.expr.FieldAccessExpr fa = ((android.databinding.tool.expr.FieldAccessExpr) (item));
        fa.getResolvedType();
        final android.databinding.tool.reflection.Callable getter = fa.getGetter();
        org.junit.Assert.assertTrue(getter.type == android.databinding.tool.reflection.Callable.Type.METHOD);
        org.junit.Assert.assertSame(id, fa.getChild());
        org.junit.Assert.assertTrue(fa.isDynamic());
    }

    private java.util.Map.Entry<java.lang.String, android.databinding.tool.expr.Expr> findIdentifier(java.lang.String name) {
        for (java.util.Map.Entry<java.lang.String, android.databinding.tool.expr.Expr> entry : mExprModel.getExprMap().entrySet()) {
            if (entry.getValue() instanceof android.databinding.tool.expr.IdentifierExpr) {
                android.databinding.tool.expr.IdentifierExpr expr = ((android.databinding.tool.expr.IdentifierExpr) (entry.getValue()));
                if (name.equals(expr.getName())) {
                    return entry;
                }
            }
        }
        return null;
    }

    static class TestUser {
        public java.lang.String name;

        public java.lang.String lastName;

        public java.lang.String fullName() {
            return (name + " ") + lastName;
        }
    }
}

