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


public class ExprModelTest {
    private static class DummyExpr extends android.databinding.tool.expr.Expr {
        java.lang.String mKey;

        public DummyExpr(java.lang.String key, android.databinding.tool.expr.ExprModelTest.DummyExpr... children) {
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
            return "DummyExpr cannot be 2-way.";
        }
    }

    android.databinding.tool.expr.ExprModel mExprModel;

    @org.junit.Rule
    public org.junit.rules.TestWatcher mTestWatcher = new org.junit.rules.TestWatcher() {
        @java.lang.Override
        protected void failed(java.lang.Throwable e, org.junit.runner.Description description) {
            if ((mExprModel != null) && (mExprModel.getFlagMapping() != null)) {
                final java.lang.String[] mapping = mExprModel.getFlagMapping();
                for (int i = 0; i < mapping.length; i++) {
                    android.databinding.tool.util.L.d("flag %d: %s", i, mapping[i]);
                }
            }
        }
    };

    @org.junit.Before
    public void setUp() throws java.lang.Exception {
        android.databinding.tool.reflection.java.JavaAnalyzer.initForTests();
        mExprModel = new android.databinding.tool.expr.ExprModel();
    }

    @org.junit.Test
    public void testAddNormal() {
        final android.databinding.tool.expr.ExprModelTest.DummyExpr d = new android.databinding.tool.expr.ExprModelTest.DummyExpr("a");
        org.junit.Assert.assertSame(d, mExprModel.register(d));
        org.junit.Assert.assertSame(d, mExprModel.register(d));
        org.junit.Assert.assertEquals(1, mExprModel.mExprMap.size());
    }

    @org.junit.Test
    public void testAddDupe1() {
        final android.databinding.tool.expr.ExprModelTest.DummyExpr d = new android.databinding.tool.expr.ExprModelTest.DummyExpr("a");
        org.junit.Assert.assertSame(d, mExprModel.register(d));
        org.junit.Assert.assertSame(d, mExprModel.register(new android.databinding.tool.expr.ExprModelTest.DummyExpr("a")));
        org.junit.Assert.assertEquals(1, mExprModel.mExprMap.size());
    }

    @org.junit.Test
    public void testAddMultiple() {
        mExprModel.register(new android.databinding.tool.expr.ExprModelTest.DummyExpr("a"));
        mExprModel.register(new android.databinding.tool.expr.ExprModelTest.DummyExpr("b"));
        org.junit.Assert.assertEquals(2, mExprModel.mExprMap.size());
    }

    @org.junit.Test
    public void testAddWithChildren() {
        android.databinding.tool.expr.ExprModelTest.DummyExpr a = new android.databinding.tool.expr.ExprModelTest.DummyExpr("a");
        android.databinding.tool.expr.ExprModelTest.DummyExpr b = new android.databinding.tool.expr.ExprModelTest.DummyExpr("b");
        android.databinding.tool.expr.ExprModelTest.DummyExpr c = new android.databinding.tool.expr.ExprModelTest.DummyExpr("c", a, b);
        mExprModel.register(c);
        android.databinding.tool.expr.ExprModelTest.DummyExpr a2 = new android.databinding.tool.expr.ExprModelTest.DummyExpr("a");
        android.databinding.tool.expr.ExprModelTest.DummyExpr b2 = new android.databinding.tool.expr.ExprModelTest.DummyExpr("b");
        android.databinding.tool.expr.ExprModelTest.DummyExpr c2 = new android.databinding.tool.expr.ExprModelTest.DummyExpr("c", a, b);
        org.junit.Assert.assertEquals(c, mExprModel.register(c2));
    }

    @org.junit.Test
    public void testShouldRead() {
        android.databinding.tool.MockLayoutBinder lb = new android.databinding.tool.MockLayoutBinder();
        mExprModel = lb.getModel();
        android.databinding.tool.expr.IdentifierExpr a = lb.addVariable("a", "java.lang.String", null);
        android.databinding.tool.expr.IdentifierExpr b = lb.addVariable("b", "java.lang.String", null);
        android.databinding.tool.expr.IdentifierExpr c = lb.addVariable("c", "java.lang.String", null);
        lb.parse("a == null ? b : c", false, null);
        mExprModel.comparison("==", a, mExprModel.symbol("null", java.lang.Object.class));
        lb.getModel().seal();
        java.util.List<android.databinding.tool.expr.Expr> shouldRead = getShouldRead();
        // a and a == null
        org.junit.Assert.assertEquals(2, shouldRead.size());
        final java.util.List<android.databinding.tool.expr.Expr> readFirst = getReadFirst(shouldRead, null);
        org.junit.Assert.assertEquals(1, readFirst.size());
        final android.databinding.tool.expr.Expr first = readFirst.get(0);
        org.junit.Assert.assertSame(a, first);
        // now , assume we've read this
        final java.util.BitSet shouldReadFlags = first.getShouldReadFlags();
        org.junit.Assert.assertNotNull(shouldReadFlags);
    }

    @org.junit.Test
    public void testReadConstantTernary() {
        android.databinding.tool.MockLayoutBinder lb = new android.databinding.tool.MockLayoutBinder();
        mExprModel = lb.getModel();
        android.databinding.tool.expr.IdentifierExpr a = lb.addVariable("a", "java.lang.String", null);
        android.databinding.tool.expr.IdentifierExpr b = lb.addVariable("b", "java.lang.String", null);
        android.databinding.tool.expr.TernaryExpr ternaryExpr = parse(lb, "true ? a : b", android.databinding.tool.expr.TernaryExpr.class);
        mExprModel.seal();
        java.util.List<android.databinding.tool.expr.Expr> shouldRead = getShouldRead();
        assertExactMatch(shouldRead, ternaryExpr.getPred());
        java.util.List<android.databinding.tool.expr.Expr> first = getReadFirst(shouldRead);
        assertExactMatch(first, ternaryExpr.getPred());
        mExprModel.markBitsRead();
        shouldRead = getShouldRead();
        assertExactMatch(shouldRead, a, b, ternaryExpr);
        first = getReadFirst(shouldRead);
        assertExactMatch(first, a, b);
        java.util.List<android.databinding.tool.expr.Expr> justRead = new java.util.ArrayList<android.databinding.tool.expr.Expr>();
        justRead.add(a);
        justRead.add(b);
        first = filterOut(getReadFirst(shouldRead, justRead), justRead);
        assertExactMatch(first, ternaryExpr);
        org.junit.Assert.assertFalse(mExprModel.markBitsRead());
    }

    @org.junit.Test
    public void testTernaryWithPlus() {
        android.databinding.tool.MockLayoutBinder lb = new android.databinding.tool.MockLayoutBinder();
        mExprModel = lb.getModel();
        android.databinding.tool.expr.IdentifierExpr user = lb.addVariable("user", "android.databinding.tool.expr.ExprModelTest.User", null);
        android.databinding.tool.expr.MathExpr parsed = parse(lb, "user.name + \" \" + (user.lastName ?? \"\")", android.databinding.tool.expr.MathExpr.class);
        mExprModel.seal();
        java.util.List<android.databinding.tool.expr.Expr> toRead = getShouldRead();
        java.util.List<android.databinding.tool.expr.Expr> readNow = getReadFirst(toRead);
        org.junit.Assert.assertEquals(1, readNow.size());
        org.junit.Assert.assertSame(user, readNow.get(0));
        java.util.List<android.databinding.tool.expr.Expr> justRead = new java.util.ArrayList<android.databinding.tool.expr.Expr>();
        justRead.add(user);
        readNow = filterOut(getReadFirst(toRead, justRead), justRead);
        org.junit.Assert.assertEquals(2, readNow.size());// user.name && user.lastName

        justRead.addAll(readNow);
        // user.lastname (T, F), user.name + " "
        readNow = filterOut(getReadFirst(toRead, justRead), justRead);
        org.junit.Assert.assertEquals(2, readNow.size());// user.name && user.lastName

        justRead.addAll(readNow);
        readNow = filterOut(getReadFirst(toRead, justRead), justRead);
        org.junit.Assert.assertEquals(0, readNow.size());
        mExprModel.markBitsRead();
        toRead = getShouldRead();
        org.junit.Assert.assertEquals(2, toRead.size());
        justRead.clear();
        readNow = filterOut(getReadFirst(toRead, justRead), justRead);
        org.junit.Assert.assertEquals(1, readNow.size());
        org.junit.Assert.assertSame(parsed.getRight(), readNow.get(0));
        justRead.addAll(readNow);
        readNow = filterOut(getReadFirst(toRead, justRead), justRead);
        org.junit.Assert.assertEquals(1, readNow.size());
        org.junit.Assert.assertSame(parsed, readNow.get(0));
        justRead.addAll(readNow);
        readNow = filterOut(getReadFirst(toRead, justRead), justRead);
        org.junit.Assert.assertEquals(0, readNow.size());
        mExprModel.markBitsRead();
        org.junit.Assert.assertEquals(0, getShouldRead().size());
    }

    private java.util.List<android.databinding.tool.expr.Expr> filterOut(java.util.List<android.databinding.tool.expr.Expr> itr, final java.util.List<android.databinding.tool.expr.Expr> exclude) {
        java.util.List<android.databinding.tool.expr.Expr> result = new java.util.ArrayList<android.databinding.tool.expr.Expr>();
        for (android.databinding.tool.expr.Expr expr : itr) {
            if (!exclude.contains(expr)) {
                result.add(expr);
            }
        }
        return result;
    }

    @org.junit.Test
    public void testTernaryInsideTernary() {
        android.databinding.tool.MockLayoutBinder lb = new android.databinding.tool.MockLayoutBinder();
        mExprModel = lb.getModel();
        android.databinding.tool.expr.IdentifierExpr cond1 = lb.addVariable("cond1", "boolean", null);
        android.databinding.tool.expr.IdentifierExpr cond2 = lb.addVariable("cond2", "boolean", null);
        android.databinding.tool.expr.IdentifierExpr a = lb.addVariable("a", "boolean", null);
        android.databinding.tool.expr.IdentifierExpr b = lb.addVariable("b", "boolean", null);
        android.databinding.tool.expr.IdentifierExpr c = lb.addVariable("c", "boolean", null);
        final android.databinding.tool.expr.TernaryExpr ternaryExpr = parse(lb, "cond1 ? cond2 ? a : b : c", android.databinding.tool.expr.TernaryExpr.class);
        final android.databinding.tool.expr.TernaryExpr innerTernary = ((android.databinding.tool.expr.TernaryExpr) (ternaryExpr.getIfTrue()));
        mExprModel.seal();
        java.util.List<android.databinding.tool.expr.Expr> toRead = getShouldRead();
        org.junit.Assert.assertEquals(1, toRead.size());
        org.junit.Assert.assertEquals(ternaryExpr.getPred(), toRead.get(0));
        java.util.List<android.databinding.tool.expr.Expr> readNow = getReadFirst(toRead);
        org.junit.Assert.assertEquals(1, readNow.size());
        org.junit.Assert.assertEquals(ternaryExpr.getPred(), readNow.get(0));
        int cond1True = ternaryExpr.getRequirementFlagIndex(true);
        int cond1False = ternaryExpr.getRequirementFlagIndex(false);
        // ok, it is read now.
        mExprModel.markBitsRead();
        // now it should read cond2 or c, depending on the flag from first
        toRead = getShouldRead();
        org.junit.Assert.assertEquals(2, toRead.size());
        assertExactMatch(toRead, ternaryExpr.getIfFalse(), innerTernary.getPred());
        assertFlags(ternaryExpr.getIfFalse(), cond1False);
        assertFlags(ternaryExpr.getIfTrue(), cond1True);
        mExprModel.markBitsRead();
        // now it should read a or b, innerTernary, outerTernary
        toRead = getShouldRead();
        assertExactMatch(toRead, innerTernary.getIfTrue(), innerTernary.getIfFalse(), ternaryExpr, innerTernary);
        assertFlags(innerTernary.getIfTrue(), innerTernary.getRequirementFlagIndex(true));
        assertFlags(innerTernary.getIfFalse(), innerTernary.getRequirementFlagIndex(false));
        org.junit.Assert.assertFalse(mExprModel.markBitsRead());
    }

    @org.junit.Test
    public void testRequirementFlags() {
        android.databinding.tool.MockLayoutBinder lb = new android.databinding.tool.MockLayoutBinder();
        mExprModel = lb.getModel();
        android.databinding.tool.expr.IdentifierExpr a = lb.addVariable("a", "java.lang.String", null);
        android.databinding.tool.expr.IdentifierExpr b = lb.addVariable("b", "java.lang.String", null);
        android.databinding.tool.expr.IdentifierExpr c = lb.addVariable("c", "java.lang.String", null);
        android.databinding.tool.expr.IdentifierExpr d = lb.addVariable("d", "java.lang.String", null);
        android.databinding.tool.expr.IdentifierExpr e = lb.addVariable("e", "java.lang.String", null);
        final android.databinding.tool.expr.Expr aTernary = lb.parse("a == null ? b == null ? c : d : e", false, null);
        org.junit.Assert.assertTrue(aTernary instanceof android.databinding.tool.expr.TernaryExpr);
        final android.databinding.tool.expr.Expr bTernary = ((android.databinding.tool.expr.TernaryExpr) (aTernary)).getIfTrue();
        org.junit.Assert.assertTrue(bTernary instanceof android.databinding.tool.expr.TernaryExpr);
        final android.databinding.tool.expr.Expr aIsNull = mExprModel.comparison("==", a, mExprModel.symbol("null", java.lang.Object.class));
        final android.databinding.tool.expr.Expr bIsNull = mExprModel.comparison("==", b, mExprModel.symbol("null", java.lang.Object.class));
        lb.getModel().seal();
        java.util.List<android.databinding.tool.expr.Expr> shouldRead = getShouldRead();
        // a and a == null
        org.junit.Assert.assertEquals(2, shouldRead.size());
        org.junit.Assert.assertFalse(a.getShouldReadFlags().isEmpty());
        org.junit.Assert.assertTrue(a.getShouldReadFlags().get(a.getId()));
        org.junit.Assert.assertTrue(b.getShouldReadFlags().isEmpty());
        org.junit.Assert.assertTrue(c.getShouldReadFlags().isEmpty());
        org.junit.Assert.assertTrue(d.getShouldReadFlags().isEmpty());
        org.junit.Assert.assertTrue(e.getShouldReadFlags().isEmpty());
        java.util.List<android.databinding.tool.expr.Expr> readFirst = getReadFirst(shouldRead, null);
        org.junit.Assert.assertEquals(1, readFirst.size());
        final android.databinding.tool.expr.Expr first = readFirst.get(0);
        org.junit.Assert.assertSame(a, first);
        org.junit.Assert.assertTrue(mExprModel.markBitsRead());
        for (android.databinding.tool.expr.Expr expr : mExprModel.getPendingExpressions()) {
            org.junit.Assert.assertNull(expr.mShouldReadFlags);
        }
        shouldRead = getShouldRead();
        assertExactMatch(shouldRead, e, b, bIsNull);
        assertFlags(e, aTernary.getRequirementFlagIndex(false));
        assertFlags(b, aTernary.getRequirementFlagIndex(true));
        assertFlags(bIsNull, aTernary.getRequirementFlagIndex(true));
        org.junit.Assert.assertTrue(mExprModel.markBitsRead());
        shouldRead = getShouldRead();
        org.junit.Assert.assertEquals(4, shouldRead.size());
        org.junit.Assert.assertTrue(shouldRead.contains(c));
        org.junit.Assert.assertTrue(shouldRead.contains(d));
        org.junit.Assert.assertTrue(shouldRead.contains(aTernary));
        org.junit.Assert.assertTrue(shouldRead.contains(bTernary));
        org.junit.Assert.assertTrue(c.getShouldReadFlags().get(bTernary.getRequirementFlagIndex(true)));
        org.junit.Assert.assertEquals(1, c.getShouldReadFlags().cardinality());
        org.junit.Assert.assertTrue(d.getShouldReadFlags().get(bTernary.getRequirementFlagIndex(false)));
        org.junit.Assert.assertEquals(1, d.getShouldReadFlags().cardinality());
        org.junit.Assert.assertTrue(bTernary.getShouldReadFlags().get(aTernary.getRequirementFlagIndex(true)));
        org.junit.Assert.assertEquals(1, bTernary.getShouldReadFlags().cardinality());
        // +1 for invalidate all flag
        org.junit.Assert.assertEquals(6, aTernary.getShouldReadFlags().cardinality());
        for (android.databinding.tool.expr.Expr expr : new android.databinding.tool.expr.Expr[]{ a, b, c, d, e }) {
            org.junit.Assert.assertTrue(aTernary.getShouldReadFlags().get(expr.getId()));
        }
        readFirst = getReadFirst(shouldRead);
        org.junit.Assert.assertEquals(2, readFirst.size());
        org.junit.Assert.assertTrue(readFirst.contains(c));
        org.junit.Assert.assertTrue(readFirst.contains(d));
        org.junit.Assert.assertFalse(mExprModel.markBitsRead());
    }

    @org.junit.Test
    public void testPostConditionalDependencies() {
        android.databinding.tool.MockLayoutBinder lb = new android.databinding.tool.MockLayoutBinder();
        mExprModel = lb.getModel();
        android.databinding.tool.expr.IdentifierExpr u1 = lb.addVariable("u1", android.databinding.tool.expr.ExprModelTest.User.class.getCanonicalName(), null);
        android.databinding.tool.expr.IdentifierExpr u2 = lb.addVariable("u2", android.databinding.tool.expr.ExprModelTest.User.class.getCanonicalName(), null);
        android.databinding.tool.expr.IdentifierExpr a = lb.addVariable("a", int.class.getCanonicalName(), null);
        android.databinding.tool.expr.IdentifierExpr b = lb.addVariable("b", int.class.getCanonicalName(), null);
        android.databinding.tool.expr.IdentifierExpr c = lb.addVariable("c", int.class.getCanonicalName(), null);
        android.databinding.tool.expr.IdentifierExpr d = lb.addVariable("d", int.class.getCanonicalName(), null);
        android.databinding.tool.expr.IdentifierExpr e = lb.addVariable("e", int.class.getCanonicalName(), null);
        android.databinding.tool.expr.TernaryExpr abTernary = parse(lb, "a > b ? u1.name : u2.name", android.databinding.tool.expr.TernaryExpr.class);
        android.databinding.tool.expr.TernaryExpr bcTernary = parse(lb, "b > c ? u1.getCond(d) ? u1.lastName : u2.lastName : `xx`" + " + u2.getCond(e) ", android.databinding.tool.expr.TernaryExpr.class);
        android.databinding.tool.expr.Expr abCmp = abTernary.getPred();
        android.databinding.tool.expr.Expr bcCmp = bcTernary.getPred();
        android.databinding.tool.expr.Expr u1GetCondD = ((android.databinding.tool.expr.TernaryExpr) (bcTernary.getIfTrue())).getPred();
        final android.databinding.tool.expr.MathExpr xxPlusU2getCondE = ((android.databinding.tool.expr.MathExpr) (bcTernary.getIfFalse()));
        android.databinding.tool.expr.Expr u2GetCondE = xxPlusU2getCondE.getRight();
        android.databinding.tool.expr.Expr u1Name = abTernary.getIfTrue();
        android.databinding.tool.expr.Expr u2Name = abTernary.getIfFalse();
        android.databinding.tool.expr.Expr u1LastName = ((android.databinding.tool.expr.TernaryExpr) (bcTernary.getIfTrue())).getIfTrue();
        android.databinding.tool.expr.Expr u2LastName = ((android.databinding.tool.expr.TernaryExpr) (bcTernary.getIfTrue())).getIfFalse();
        mExprModel.seal();
        java.util.List<android.databinding.tool.expr.Expr> shouldRead = getShouldRead();
        assertExactMatch(shouldRead, a, b, c, abCmp, bcCmp);
        java.util.List<android.databinding.tool.expr.Expr> firstRead = getReadFirst(shouldRead);
        assertExactMatch(firstRead, a, b, c);
        assertFlags(a, a, b, u1, u2, u1Name, u2Name);
        assertFlags(b, a, b, u1, u2, u1Name, u2Name, c, d, u1LastName, u2LastName, e);
        assertFlags(c, b, c, u1, d, u1LastName, u2LastName, e);
        assertFlags(abCmp, a, b, u1, u2, u1Name, u2Name);
        assertFlags(bcCmp, b, c, u1, d, u1LastName, u2LastName, e);
        org.junit.Assert.assertTrue(mExprModel.markBitsRead());
        shouldRead = getShouldRead();
        android.databinding.tool.expr.Expr[] batch = new android.databinding.tool.expr.Expr[]{ d, e, u1, u2, u1GetCondD, u2GetCondE, xxPlusU2getCondE, abTernary, abTernary.getIfTrue(), abTernary.getIfFalse() };
        assertExactMatch(shouldRead, batch);
        firstRead = getReadFirst(shouldRead);
        assertExactMatch(firstRead, d, e, u1, u2);
        assertFlags(d, bcTernary.getRequirementFlagIndex(true));
        assertFlags(e, bcTernary.getRequirementFlagIndex(false));
        assertFlags(u1, bcTernary.getRequirementFlagIndex(true), abTernary.getRequirementFlagIndex(true));
        assertFlags(u2, bcTernary.getRequirementFlagIndex(false), abTernary.getRequirementFlagIndex(false));
        assertFlags(u1GetCondD, bcTernary.getRequirementFlagIndex(true));
        assertFlags(u2GetCondE, bcTernary.getRequirementFlagIndex(false));
        assertFlags(xxPlusU2getCondE, bcTernary.getRequirementFlagIndex(false));
        assertFlags(abTernary, a, b, u1, u2, u1Name, u2Name);
        assertFlags(abTernary.getIfTrue(), abTernary.getRequirementFlagIndex(true));
        assertFlags(abTernary.getIfFalse(), abTernary.getRequirementFlagIndex(false));
        org.junit.Assert.assertTrue(mExprModel.markBitsRead());
        shouldRead = getShouldRead();
        // FIXME: there is no real case to read u1 anymore because if b>c was not true,
        // u1.getCond(d) will never be set. Right now, we don't have mechanism to figure this out
        // and also it does not affect correctness (just an unnecessary if stmt)
        assertExactMatch(shouldRead, u1, u2, u1LastName, u2LastName, bcTernary.getIfTrue(), bcTernary);
        firstRead = getReadFirst(shouldRead);
        assertExactMatch(firstRead, u1, u2);
        assertFlags(u1, bcTernary.getIfTrue().getRequirementFlagIndex(true));
        assertFlags(u2, bcTernary.getIfTrue().getRequirementFlagIndex(false));
        assertFlags(u1LastName, bcTernary.getIfTrue().getRequirementFlagIndex(true));
        assertFlags(u2LastName, bcTernary.getIfTrue().getRequirementFlagIndex(false));
        assertFlags(bcTernary.getIfTrue(), bcTernary.getRequirementFlagIndex(true));
        assertFlags(bcTernary, b, c, u1, u2, d, u1LastName, u2LastName, e);
        org.junit.Assert.assertFalse(mExprModel.markBitsRead());
    }

    @org.junit.Test
    public void testCircularDependency() {
        android.databinding.tool.MockLayoutBinder lb = new android.databinding.tool.MockLayoutBinder();
        mExprModel = lb.getModel();
        android.databinding.tool.expr.IdentifierExpr a = lb.addVariable("a", int.class.getCanonicalName(), null);
        android.databinding.tool.expr.IdentifierExpr b = lb.addVariable("b", int.class.getCanonicalName(), null);
        final android.databinding.tool.expr.TernaryExpr abTernary = parse(lb, "a > 3 ? a : b", android.databinding.tool.expr.TernaryExpr.class);
        mExprModel.seal();
        java.util.List<android.databinding.tool.expr.Expr> shouldRead = getShouldRead();
        assertExactMatch(shouldRead, a, abTernary.getPred());
        org.junit.Assert.assertTrue(mExprModel.markBitsRead());
        shouldRead = getShouldRead();
        assertExactMatch(shouldRead, b, abTernary);
        org.junit.Assert.assertFalse(mExprModel.markBitsRead());
    }

    @org.junit.Test
    public void testNestedCircularDependency() {
        android.databinding.tool.MockLayoutBinder lb = new android.databinding.tool.MockLayoutBinder();
        mExprModel = lb.getModel();
        android.databinding.tool.expr.IdentifierExpr a = lb.addVariable("a", int.class.getCanonicalName(), null);
        android.databinding.tool.expr.IdentifierExpr b = lb.addVariable("b", int.class.getCanonicalName(), null);
        android.databinding.tool.expr.IdentifierExpr c = lb.addVariable("c", int.class.getCanonicalName(), null);
        final android.databinding.tool.expr.TernaryExpr a3Ternary = parse(lb, "a > 3 ? c > 4 ? a : b : c", android.databinding.tool.expr.TernaryExpr.class);
        final android.databinding.tool.expr.TernaryExpr c4Ternary = ((android.databinding.tool.expr.TernaryExpr) (a3Ternary.getIfTrue()));
        mExprModel.seal();
        java.util.List<android.databinding.tool.expr.Expr> shouldRead = getShouldRead();
        assertExactMatch(shouldRead, a, a3Ternary.getPred());
        org.junit.Assert.assertTrue(mExprModel.markBitsRead());
        shouldRead = getShouldRead();
        assertExactMatch(shouldRead, c, c4Ternary.getPred());
        assertFlags(c, a3Ternary.getRequirementFlagIndex(true), a3Ternary.getRequirementFlagIndex(false));
        assertFlags(c4Ternary.getPred(), a3Ternary.getRequirementFlagIndex(true));
    }

    @org.junit.Test
    public void testInterExprDependency() {
        android.databinding.tool.MockLayoutBinder lb = new android.databinding.tool.MockLayoutBinder();
        mExprModel = lb.getModel();
        android.databinding.tool.expr.IdentifierExpr u = lb.addVariable("u", android.databinding.tool.expr.ExprModelTest.User.class.getCanonicalName(), null);
        final android.databinding.tool.expr.Expr uComment = parse(lb, "u.comment", android.databinding.tool.expr.FieldAccessExpr.class);
        final android.databinding.tool.expr.TernaryExpr uTernary = parse(lb, "u.getUseComment ? u.comment : `xx`", android.databinding.tool.expr.TernaryExpr.class);
        mExprModel.seal();
        org.junit.Assert.assertTrue(uTernary.getPred().canBeInvalidated());
        java.util.List<android.databinding.tool.expr.Expr> shouldRead = getShouldRead();
        assertExactMatch(shouldRead, u, uComment, uTernary.getPred());
        org.junit.Assert.assertTrue(mExprModel.markBitsRead());
        shouldRead = getShouldRead();
        assertExactMatch(shouldRead, uComment, uTernary);
    }

    @org.junit.Test
    public void testInterExprCircularDependency() {
        android.databinding.tool.MockLayoutBinder lb = new android.databinding.tool.MockLayoutBinder();
        mExprModel = lb.getModel();
        android.databinding.tool.expr.IdentifierExpr a = lb.addVariable("a", int.class.getCanonicalName(), null);
        android.databinding.tool.expr.IdentifierExpr b = lb.addVariable("b", int.class.getCanonicalName(), null);
        final android.databinding.tool.expr.TernaryExpr abTernary = parse(lb, "a > 3 ? a : b", android.databinding.tool.expr.TernaryExpr.class);
        final android.databinding.tool.expr.TernaryExpr abTernary2 = parse(lb, "b > 3 ? b : a", android.databinding.tool.expr.TernaryExpr.class);
        mExprModel.seal();
        java.util.List<android.databinding.tool.expr.Expr> shouldRead = getShouldRead();
        assertExactMatch(shouldRead, a, b, abTernary.getPred(), abTernary2.getPred());
        org.junit.Assert.assertTrue(mExprModel.markBitsRead());
        shouldRead = getShouldRead();
        assertExactMatch(shouldRead, abTernary, abTernary2);
    }

    @org.junit.Test
    public void testInterExprCircularDependency2() {
        android.databinding.tool.MockLayoutBinder lb = new android.databinding.tool.MockLayoutBinder();
        mExprModel = lb.getModel();
        android.databinding.tool.expr.IdentifierExpr a = lb.addVariable("a", boolean.class.getCanonicalName(), null);
        android.databinding.tool.expr.IdentifierExpr b = lb.addVariable("b", boolean.class.getCanonicalName(), null);
        final android.databinding.tool.expr.TernaryExpr abTernary = parse(lb, "a ? b : true", android.databinding.tool.expr.TernaryExpr.class);
        final android.databinding.tool.expr.TernaryExpr baTernary = parse(lb, "b ? a : false", android.databinding.tool.expr.TernaryExpr.class);
        mExprModel.seal();
        java.util.List<android.databinding.tool.expr.Expr> shouldRead = getShouldRead();
        assertExactMatch(shouldRead, a, b);
        assertFlags(a, a, b);
        assertFlags(b, a, b);
        java.util.List<android.databinding.tool.expr.Expr> readFirst = getReadFirst(shouldRead);
        assertExactMatch(readFirst, a, b);
        org.junit.Assert.assertTrue(mExprModel.markBitsRead());
        shouldRead = getShouldRead();
        assertExactMatch(shouldRead, abTernary, baTernary);
        readFirst = getReadFirst(shouldRead);
        assertExactMatch(readFirst, abTernary, baTernary);
        org.junit.Assert.assertFalse(mExprModel.markBitsRead());
        shouldRead = getShouldRead();
        org.junit.Assert.assertEquals(0, shouldRead.size());
    }

    @org.junit.Test
    public void testInterExprCircularDependency3() {
        android.databinding.tool.MockLayoutBinder lb = new android.databinding.tool.MockLayoutBinder();
        mExprModel = lb.getModel();
        android.databinding.tool.expr.IdentifierExpr a = lb.addVariable("a", boolean.class.getCanonicalName(), null);
        android.databinding.tool.expr.IdentifierExpr b = lb.addVariable("b", boolean.class.getCanonicalName(), null);
        android.databinding.tool.expr.IdentifierExpr c = lb.addVariable("c", boolean.class.getCanonicalName(), null);
        final android.databinding.tool.expr.TernaryExpr abTernary = parse(lb, "a ? b : c", android.databinding.tool.expr.TernaryExpr.class);
        final android.databinding.tool.expr.TernaryExpr abTernary2 = parse(lb, "b ? a : c", android.databinding.tool.expr.TernaryExpr.class);
        mExprModel.seal();
        java.util.List<android.databinding.tool.expr.Expr> shouldRead = getShouldRead();
        assertExactMatch(shouldRead, a, b);
        org.junit.Assert.assertTrue(mExprModel.markBitsRead());
        shouldRead = getShouldRead();
        // read a and b again, this time for their dependencies and also the rest since everything
        // is ready to be read
        assertExactMatch(shouldRead, c, abTernary, abTernary2);
        mExprModel.markBitsRead();
        shouldRead = getShouldRead();
        org.junit.Assert.assertEquals(0, shouldRead.size());
    }

    @org.junit.Test
    public void testInterExprCircularDependency4() {
        android.databinding.tool.MockLayoutBinder lb = new android.databinding.tool.MockLayoutBinder();
        mExprModel = lb.getModel();
        android.databinding.tool.expr.IdentifierExpr a = lb.addVariable("a", boolean.class.getCanonicalName(), null);
        android.databinding.tool.expr.IdentifierExpr b = lb.addVariable("b", boolean.class.getCanonicalName(), null);
        android.databinding.tool.expr.IdentifierExpr c = lb.addVariable("c", boolean.class.getCanonicalName(), null);
        android.databinding.tool.expr.IdentifierExpr d = lb.addVariable("d", boolean.class.getCanonicalName(), null);
        final android.databinding.tool.expr.TernaryExpr cTernary = parse(lb, "c ? (a ? d : false) : false", android.databinding.tool.expr.TernaryExpr.class);
        final android.databinding.tool.expr.TernaryExpr abTernary = parse(lb, "a ? b : true", android.databinding.tool.expr.TernaryExpr.class);
        final android.databinding.tool.expr.TernaryExpr baTernary = parse(lb, "b ? a : false", android.databinding.tool.expr.TernaryExpr.class);
        mExprModel.seal();
        java.util.List<android.databinding.tool.expr.Expr> shouldRead = getShouldRead();
        // check if a,b or c should be read. these are easily calculated from binding expressions'
        // invalidation
        assertExactMatch(shouldRead, c, a, b);
        java.util.List<android.databinding.tool.expr.Expr> justRead = new java.util.ArrayList<android.databinding.tool.expr.Expr>();
        java.util.List<android.databinding.tool.expr.Expr> readFirst = getReadFirst(shouldRead);
        assertExactMatch(readFirst, c, a, b);
        java.util.Collections.addAll(justRead, a, b, c);
        org.junit.Assert.assertEquals(0, filterOut(getReadFirst(shouldRead, justRead), justRead).size());
        org.junit.Assert.assertTrue(mExprModel.markBitsRead());
        shouldRead = getShouldRead();
        // if a and b are not invalid, a won't be read in the first step. But if c's expression
        // is invalid and c == true, a must be read. Depending on a, d might be read as well.
        // don't need to read b anymore because `a ? b : true` and `b ? a : false` has the same
        // invalidation flags.
        assertExactMatch(shouldRead, a, abTernary, baTernary);
        justRead.clear();
        readFirst = getReadFirst(shouldRead);
        // first must read `a`.
        assertExactMatch(readFirst, a);
        java.util.Collections.addAll(justRead, a);
        readFirst = filterOut(getReadFirst(shouldRead, justRead), justRead);
        assertExactMatch(readFirst, abTernary, baTernary);
        java.util.Collections.addAll(justRead, abTernary, baTernary);
        readFirst = filterOut(getReadFirst(shouldRead, justRead), justRead);
        org.junit.Assert.assertEquals(0, filterOut(getReadFirst(shouldRead, justRead), justRead).size());
        org.junit.Assert.assertTrue(mExprModel.markBitsRead());
        shouldRead = getShouldRead();
        // now we can read adf ternary and c ternary
        justRead.clear();
        assertExactMatch(shouldRead, d, cTernary.getIfTrue(), cTernary);
        readFirst = getReadFirst(shouldRead);
        assertExactMatch(readFirst, d);
        java.util.Collections.addAll(justRead, d);
        readFirst = filterOut(getReadFirst(shouldRead, justRead), justRead);
        assertExactMatch(readFirst, cTernary.getIfTrue());
        java.util.Collections.addAll(justRead, cTernary.getIfTrue());
        readFirst = filterOut(getReadFirst(shouldRead, justRead), justRead);
        assertExactMatch(readFirst, cTernary);
        java.util.Collections.addAll(justRead, cTernary);
        org.junit.Assert.assertEquals(0, filterOut(getReadFirst(shouldRead, justRead), justRead).size());
        org.junit.Assert.assertFalse(mExprModel.markBitsRead());
    }

    @org.junit.Test
    public void testInterExprDeepDependency() {
        android.databinding.tool.MockLayoutBinder lb = new android.databinding.tool.MockLayoutBinder();
        mExprModel = lb.getModel();
        android.databinding.tool.expr.IdentifierExpr a = lb.addVariable("a", boolean.class.getCanonicalName(), null);
        android.databinding.tool.expr.IdentifierExpr b = lb.addVariable("b", boolean.class.getCanonicalName(), null);
        android.databinding.tool.expr.IdentifierExpr c = lb.addVariable("c", boolean.class.getCanonicalName(), null);
        final android.databinding.tool.expr.TernaryExpr t1 = parse(lb, "c ? (a ? b : true) : false", android.databinding.tool.expr.TernaryExpr.class);
        final android.databinding.tool.expr.TernaryExpr t2 = parse(lb, "c ? (b ? a : false) : true", android.databinding.tool.expr.TernaryExpr.class);
        final android.databinding.tool.expr.TernaryExpr abTernary = ((android.databinding.tool.expr.TernaryExpr) (t1.getIfTrue()));
        final android.databinding.tool.expr.TernaryExpr baTernary = ((android.databinding.tool.expr.TernaryExpr) (t2.getIfTrue()));
        mExprModel.seal();
        java.util.List<android.databinding.tool.expr.Expr> shouldRead = getShouldRead();
        assertExactMatch(shouldRead, c);
        org.junit.Assert.assertTrue(mExprModel.markBitsRead());
        shouldRead = getShouldRead();
        assertExactMatch(shouldRead, a, b);
        org.junit.Assert.assertTrue(mExprModel.markBitsRead());
        shouldRead = getShouldRead();
        assertExactMatch(shouldRead, a, b, t1.getIfTrue(), t2.getIfTrue(), t1, t2);
        assertFlags(b, abTernary.getRequirementFlagIndex(true));
        assertFlags(a, baTernary.getRequirementFlagIndex(true));
        org.junit.Assert.assertFalse(mExprModel.markBitsRead());
    }

    @org.junit.Test
    public void testInterExprDependencyNotReadyYet() {
        android.databinding.tool.MockLayoutBinder lb = new android.databinding.tool.MockLayoutBinder();
        mExprModel = lb.getModel();
        android.databinding.tool.expr.IdentifierExpr a = lb.addVariable("a", boolean.class.getCanonicalName(), null);
        android.databinding.tool.expr.IdentifierExpr b = lb.addVariable("b", boolean.class.getCanonicalName(), null);
        android.databinding.tool.expr.IdentifierExpr c = lb.addVariable("c", boolean.class.getCanonicalName(), null);
        android.databinding.tool.expr.IdentifierExpr d = lb.addVariable("d", boolean.class.getCanonicalName(), null);
        android.databinding.tool.expr.IdentifierExpr e = lb.addVariable("e", boolean.class.getCanonicalName(), null);
        final android.databinding.tool.expr.TernaryExpr cTernary = parse(lb, "c ? (a ? d : false) : false", android.databinding.tool.expr.TernaryExpr.class);
        final android.databinding.tool.expr.TernaryExpr baTernary = parse(lb, "b ? a : false", android.databinding.tool.expr.TernaryExpr.class);
        final android.databinding.tool.expr.TernaryExpr eaTernary = parse(lb, "e ? a : false", android.databinding.tool.expr.TernaryExpr.class);
        mExprModel.seal();
        java.util.List<android.databinding.tool.expr.Expr> shouldRead = getShouldRead();
        assertExactMatch(shouldRead, b, c, e);
        org.junit.Assert.assertTrue(mExprModel.markBitsRead());
        shouldRead = getShouldRead();
        assertExactMatch(shouldRead, a, baTernary, eaTernary);
        org.junit.Assert.assertTrue(mExprModel.markBitsRead());
        shouldRead = getShouldRead();
        assertExactMatch(shouldRead, d, cTernary.getIfTrue(), cTernary);
        org.junit.Assert.assertFalse(mExprModel.markBitsRead());
    }

    @org.junit.Test
    public void testNoFlagsForNonBindingStatic() {
        android.databinding.tool.MockLayoutBinder lb = new android.databinding.tool.MockLayoutBinder();
        mExprModel = lb.getModel();
        lb.addVariable("a", int.class.getCanonicalName(), null);
        final android.databinding.tool.expr.MathExpr parsed = parse(lb, "a * (3 + 2)", android.databinding.tool.expr.MathExpr.class);
        mExprModel.seal();
        // +1 for invalidate all flag
        org.junit.Assert.assertEquals(1, parsed.getRight().getInvalidFlags().cardinality());
        // +1 for invalidate all flag
        org.junit.Assert.assertEquals(2, parsed.getLeft().getInvalidFlags().cardinality());
        // +1 for invalidate all flag
        org.junit.Assert.assertEquals(2, mExprModel.getInvalidateableFieldLimit());
    }

    @org.junit.Test
    public void testFlagsForBindingStatic() {
        android.databinding.tool.MockLayoutBinder lb = new android.databinding.tool.MockLayoutBinder();
        mExprModel = lb.getModel();
        lb.addVariable("a", int.class.getCanonicalName(), null);
        final android.databinding.tool.expr.Expr staticParsed = parse(lb, "3 + 2", android.databinding.tool.expr.MathExpr.class);
        final android.databinding.tool.expr.MathExpr parsed = parse(lb, "a * (3 + 2)", android.databinding.tool.expr.MathExpr.class);
        mExprModel.seal();
        org.junit.Assert.assertTrue(staticParsed.isBindingExpression());
        // +1 for invalidate all flag
        org.junit.Assert.assertEquals(1, staticParsed.getInvalidFlags().cardinality());
        org.junit.Assert.assertEquals(parsed.getRight().getInvalidFlags(), staticParsed.getInvalidFlags());
        // +1 for invalidate all flag
        org.junit.Assert.assertEquals(2, parsed.getLeft().getInvalidFlags().cardinality());
        // +1 for invalidate all flag
        org.junit.Assert.assertEquals(2, mExprModel.getInvalidateableFieldLimit());
    }

    @org.junit.Test
    public void testFinalFieldOfAVariable() {
        android.databinding.tool.MockLayoutBinder lb = new android.databinding.tool.MockLayoutBinder();
        mExprModel = lb.getModel();
        android.databinding.tool.expr.IdentifierExpr user = lb.addVariable("user", android.databinding.tool.expr.ExprModelTest.User.class.getCanonicalName(), null);
        android.databinding.tool.expr.Expr fieldGet = parse(lb, "user.finalField", android.databinding.tool.expr.FieldAccessExpr.class);
        mExprModel.seal();
        org.junit.Assert.assertTrue(fieldGet.isDynamic());
        // read user
        assertExactMatch(getShouldRead(), user, fieldGet);
        mExprModel.markBitsRead();
        // no need to read user.finalField
        org.junit.Assert.assertEquals(0, getShouldRead().size());
    }

    @org.junit.Test
    public void testFinalFieldOfAField() {
        android.databinding.tool.MockLayoutBinder lb = new android.databinding.tool.MockLayoutBinder();
        mExprModel = lb.getModel();
        lb.addVariable("user", android.databinding.tool.expr.ExprModelTest.User.class.getCanonicalName(), null);
        android.databinding.tool.expr.Expr finalFieldGet = parse(lb, "user.subObj.finalField", android.databinding.tool.expr.FieldAccessExpr.class);
        mExprModel.seal();
        org.junit.Assert.assertTrue(finalFieldGet.isDynamic());
        android.databinding.tool.expr.Expr userSubObjGet = finalFieldGet.getChildren().get(0);
        // read user
        java.util.List<android.databinding.tool.expr.Expr> shouldRead = getShouldRead();
        org.junit.Assert.assertEquals(3, shouldRead.size());
        assertExactMatch(shouldRead, userSubObjGet.getChildren().get(0), userSubObjGet, finalFieldGet);
        mExprModel.markBitsRead();
        // no need to read user.subObj.finalField because it is final
        org.junit.Assert.assertEquals(0, getShouldRead().size());
    }

    @org.junit.Test
    public void testFinalFieldOfAMethod() {
        android.databinding.tool.MockLayoutBinder lb = new android.databinding.tool.MockLayoutBinder();
        mExprModel = lb.getModel();
        lb.addVariable("user", android.databinding.tool.expr.ExprModelTest.User.class.getCanonicalName(), null);
        android.databinding.tool.expr.Expr finalFieldGet = parse(lb, "user.anotherSubObj.finalField", android.databinding.tool.expr.FieldAccessExpr.class);
        mExprModel.seal();
        org.junit.Assert.assertTrue(finalFieldGet.isDynamic());
        android.databinding.tool.expr.Expr userSubObjGet = finalFieldGet.getChildren().get(0);
        // read user
        java.util.List<android.databinding.tool.expr.Expr> shouldRead = getShouldRead();
        org.junit.Assert.assertEquals(3, shouldRead.size());
        assertExactMatch(shouldRead, userSubObjGet.getChildren().get(0), userSubObjGet, finalFieldGet);
        mExprModel.markBitsRead();
        // no need to read user.subObj.finalField because it is final
        org.junit.Assert.assertEquals(0, getShouldRead().size());
    }

    @org.junit.Test
    public void testFinalOfAClass() {
        android.databinding.tool.MockLayoutBinder lb = new android.databinding.tool.MockLayoutBinder();
        mExprModel = lb.getModel();
        mExprModel.addImport("View", "android.view.View", null);
        android.databinding.tool.expr.FieldAccessExpr fieldAccess = parse(lb, "View.VISIBLE", android.databinding.tool.expr.FieldAccessExpr.class);
        org.junit.Assert.assertFalse(fieldAccess.isDynamic());
        mExprModel.seal();
        org.junit.Assert.assertEquals(0, getShouldRead().size());
    }

    @org.junit.Test
    public void testStaticFieldOfInstance() {
        android.databinding.tool.MockLayoutBinder lb = new android.databinding.tool.MockLayoutBinder();
        mExprModel = lb.getModel();
        lb.addVariable("myView", "android.view.View", null);
        android.databinding.tool.expr.FieldAccessExpr fieldAccess = parse(lb, "myView.VISIBLE", android.databinding.tool.expr.FieldAccessExpr.class);
        org.junit.Assert.assertFalse(fieldAccess.isDynamic());
        mExprModel.seal();
        org.junit.Assert.assertEquals(0, getShouldRead().size());
        final android.databinding.tool.expr.Expr child = fieldAccess.getChild();
        org.junit.Assert.assertTrue(child instanceof android.databinding.tool.expr.StaticIdentifierExpr);
        android.databinding.tool.expr.StaticIdentifierExpr id = ((android.databinding.tool.expr.StaticIdentifierExpr) (child));
        org.junit.Assert.assertEquals(id.getResolvedType().getCanonicalName(), "android.view.View");
        // on demand import
        org.junit.Assert.assertEquals("android.view.View", mExprModel.getImports().get("View"));
    }

    @org.junit.Test
    public void testOnDemandImportConflict() {
        android.databinding.tool.MockLayoutBinder lb = new android.databinding.tool.MockLayoutBinder();
        mExprModel = lb.getModel();
        final android.databinding.tool.expr.IdentifierExpr myView = lb.addVariable("u", "android.view.View", null);
        mExprModel.addImport("View", android.databinding.tool.expr.ExprModelTest.User.class.getCanonicalName(), null);
        final android.databinding.tool.expr.StaticIdentifierExpr id = mExprModel.staticIdentifierFor(myView.getResolvedType());
        mExprModel.seal();
        // on demand import with conflict
        org.junit.Assert.assertEquals("android.view.View", mExprModel.getImports().get("View1"));
        org.junit.Assert.assertEquals("View1", id.getName());
        org.junit.Assert.assertEquals("android.view.View", id.getUserDefinedType());
    }

    @org.junit.Test
    public void testOnDemandImportAlreadyImported() {
        android.databinding.tool.MockLayoutBinder lb = new android.databinding.tool.MockLayoutBinder();
        mExprModel = lb.getModel();
        final android.databinding.tool.expr.StaticIdentifierExpr ux = mExprModel.addImport("UX", android.databinding.tool.expr.ExprModelTest.User.class.getCanonicalName(), null);
        final android.databinding.tool.expr.IdentifierExpr u = lb.addVariable("u", android.databinding.tool.expr.ExprModelTest.User.class.getCanonicalName(), null);
        final android.databinding.tool.expr.StaticIdentifierExpr id = mExprModel.staticIdentifierFor(u.getResolvedType());
        mExprModel.seal();
        // on demand import with conflict
        org.junit.Assert.assertSame(ux, id);
    }

    @org.junit.Test
    public void testStaticMethodOfInstance() {
        android.databinding.tool.MockLayoutBinder lb = new android.databinding.tool.MockLayoutBinder();
        mExprModel = lb.getModel();
        lb.addVariable("user", android.databinding.tool.expr.ExprModelTest.User.class.getCanonicalName(), null);
        android.databinding.tool.expr.MethodCallExpr methodCall = parse(lb, "user.ourStaticMethod()", android.databinding.tool.expr.MethodCallExpr.class);
        org.junit.Assert.assertTrue(methodCall.isDynamic());
        mExprModel.seal();
        final android.databinding.tool.expr.Expr child = methodCall.getTarget();
        org.junit.Assert.assertTrue(child instanceof android.databinding.tool.expr.StaticIdentifierExpr);
        android.databinding.tool.expr.StaticIdentifierExpr id = ((android.databinding.tool.expr.StaticIdentifierExpr) (child));
        org.junit.Assert.assertEquals(id.getResolvedType().getCanonicalName(), android.databinding.tool.expr.ExprModelTest.User.class.getCanonicalName());
    }

    @org.junit.Test
    public void testFinalOfStaticField() {
        android.databinding.tool.MockLayoutBinder lb = new android.databinding.tool.MockLayoutBinder();
        mExprModel = lb.getModel();
        mExprModel.addImport("UX", android.databinding.tool.expr.ExprModelTest.User.class.getCanonicalName(), null);
        android.databinding.tool.expr.FieldAccessExpr fieldAccess = parse(lb, "UX.innerStaticInstance.finalStaticField", android.databinding.tool.expr.FieldAccessExpr.class);
        org.junit.Assert.assertFalse(fieldAccess.isDynamic());
        mExprModel.seal();
        // nothing to read since it is all final and static
        org.junit.Assert.assertEquals(0, getShouldRead().size());
    }

    @org.junit.Test
    public void testFinalOfFinalStaticField() {
        android.databinding.tool.MockLayoutBinder lb = new android.databinding.tool.MockLayoutBinder();
        mExprModel = lb.getModel();
        mExprModel.addImport("User", android.databinding.tool.expr.ExprModelTest.User.class.getCanonicalName(), null);
        android.databinding.tool.expr.FieldAccessExpr fieldAccess = parse(lb, "User.innerFinalStaticInstance.finalStaticField", android.databinding.tool.expr.FieldAccessExpr.class);
        org.junit.Assert.assertFalse(fieldAccess.isDynamic());
        mExprModel.seal();
        org.junit.Assert.assertEquals(0, getShouldRead().size());
    }

    @org.junit.Test
    public void testLocationTracking() {
        android.databinding.tool.MockLayoutBinder lb = new android.databinding.tool.MockLayoutBinder();
        mExprModel = lb.getModel();
        final java.lang.String input = "a > 3 ? b : c";
        android.databinding.tool.expr.TernaryExpr ternaryExpr = parse(lb, input, android.databinding.tool.expr.TernaryExpr.class);
        final android.databinding.tool.store.Location location = ternaryExpr.getLocations().get(0);
        org.junit.Assert.assertNotNull(location);
        org.junit.Assert.assertEquals(0, location.startLine);
        org.junit.Assert.assertEquals(0, location.startOffset);
        org.junit.Assert.assertEquals(0, location.endLine);
        org.junit.Assert.assertEquals(input.length() - 1, location.endOffset);
        final android.databinding.tool.expr.ComparisonExpr comparison = ((android.databinding.tool.expr.ComparisonExpr) (ternaryExpr.getPred()));
        final android.databinding.tool.store.Location predLoc = comparison.getLocations().get(0);
        org.junit.Assert.assertNotNull(predLoc);
        org.junit.Assert.assertEquals(0, predLoc.startLine);
        org.junit.Assert.assertEquals(0, predLoc.startOffset);
        org.junit.Assert.assertEquals(0, predLoc.endLine);
        org.junit.Assert.assertEquals(4, predLoc.endOffset);
        final android.databinding.tool.store.Location aLoc = comparison.getLeft().getLocations().get(0);
        org.junit.Assert.assertNotNull(aLoc);
        org.junit.Assert.assertEquals(0, aLoc.startLine);
        org.junit.Assert.assertEquals(0, aLoc.startOffset);
        org.junit.Assert.assertEquals(0, aLoc.endLine);
        org.junit.Assert.assertEquals(0, aLoc.endOffset);
        final android.databinding.tool.store.Location tLoc = comparison.getRight().getLocations().get(0);
        org.junit.Assert.assertNotNull(tLoc);
        org.junit.Assert.assertEquals(0, tLoc.startLine);
        org.junit.Assert.assertEquals(4, tLoc.startOffset);
        org.junit.Assert.assertEquals(0, tLoc.endLine);
        org.junit.Assert.assertEquals(4, tLoc.endOffset);
        final android.databinding.tool.store.Location bLoc = ternaryExpr.getIfTrue().getLocations().get(0);
        org.junit.Assert.assertNotNull(bLoc);
        org.junit.Assert.assertEquals(0, bLoc.startLine);
        org.junit.Assert.assertEquals(8, bLoc.startOffset);
        org.junit.Assert.assertEquals(0, bLoc.endLine);
        org.junit.Assert.assertEquals(8, bLoc.endOffset);
        final android.databinding.tool.store.Location cLoc = ternaryExpr.getIfFalse().getLocations().get(0);
        org.junit.Assert.assertNotNull(cLoc);
        org.junit.Assert.assertEquals(0, cLoc.startLine);
        org.junit.Assert.assertEquals(12, cLoc.startOffset);
        org.junit.Assert.assertEquals(0, cLoc.endLine);
        org.junit.Assert.assertEquals(12, cLoc.endOffset);
    }

    // TODO uncomment when we have inner static access
    // @Test
    // public void testFinalOfInnerStaticClass() {
    // MockLayoutBinder lb = new MockLayoutBinder();
    // mExprModel = lb.getModel();
    // mExprModel.addImport("User", User.class.getCanonicalName());
    // FieldAccessExpr fieldAccess = parse(lb, "User.InnerStaticClass.finalStaticField", FieldAccessExpr.class);
    // assertFalse(fieldAccess.isDynamic());
    // mExprModel.seal();
    // assertEquals(0, getShouldRead().size());
    // }
    private void assertFlags(android.databinding.tool.expr.Expr a, int... flags) {
        java.util.BitSet bitset = new java.util.BitSet();
        for (int flag : flags) {
            bitset.set(flag);
        }
        org.junit.Assert.assertEquals("flag test for " + a.getUniqueKey(), bitset, a.getShouldReadFlags());
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

    private void assertExactMatch(java.util.List<android.databinding.tool.expr.Expr> iterable, android.databinding.tool.expr.Expr... exprs) {
        int i = 0;
        java.lang.String listLog = java.util.Arrays.toString(iterable.toArray());
        java.lang.String itemsLog = java.util.Arrays.toString(exprs);
        java.lang.String log = (("list: " + listLog) + "\nitems: ") + itemsLog;
        log("list", iterable);
        for (android.databinding.tool.expr.Expr expr : exprs) {
            org.junit.Assert.assertTrue(((((i++) + ":must contain ") + expr.getUniqueKey()) + "\n") + log, iterable.contains(expr));
        }
        i = 0;
        for (android.databinding.tool.expr.Expr expr : iterable) {
            org.junit.Assert.assertTrue(((((i++) + ":must be expected ") + expr.getUniqueKey()) + "\n") + log, java.util.Arrays.asList(exprs).contains(expr));
        }
    }

    private <T extends android.databinding.tool.expr.Expr> T parse(android.databinding.tool.LayoutBinder binder, java.lang.String input, java.lang.Class<T> klass) {
        final android.databinding.tool.expr.Expr parsed = binder.parse(input, false, null);
        org.junit.Assert.assertTrue(klass.isAssignableFrom(parsed.getClass()));
        return ((T) (parsed));
    }

    private void log(java.lang.String s, java.util.List<android.databinding.tool.expr.Expr> iterable) {
        android.databinding.tool.util.L.d(s);
        for (android.databinding.tool.expr.Expr e : iterable) {
            android.databinding.tool.util.L.d(": %s : %s allFlags: %s readSoFar: %s", e.getUniqueKey(), e.getShouldReadFlags(), e.getShouldReadFlagsWithConditionals(), e.getReadSoFar());
        }
        android.databinding.tool.util.L.d("end of %s", s);
    }

    private java.util.List<android.databinding.tool.expr.Expr> getReadFirst(java.util.List<android.databinding.tool.expr.Expr> shouldRead) {
        return getReadFirst(shouldRead, null);
    }

    private java.util.List<android.databinding.tool.expr.Expr> getReadFirst(java.util.List<android.databinding.tool.expr.Expr> shouldRead, final java.util.List<android.databinding.tool.expr.Expr> justRead) {
        java.util.List<android.databinding.tool.expr.Expr> result = new java.util.ArrayList<android.databinding.tool.expr.Expr>();
        for (android.databinding.tool.expr.Expr expr : shouldRead) {
            if (expr.shouldReadNow(justRead)) {
                result.add(expr);
            }
        }
        return result;
    }

    private java.util.List<android.databinding.tool.expr.Expr> getShouldRead() {
        return android.databinding.tool.expr.ExprModel.filterShouldRead(mExprModel.getPendingExpressions());
    }

    public static class User implements android.databinding.Observable {
        java.lang.String name;

        java.lang.String lastName;

        public final int finalField = 5;

        public static android.databinding.tool.expr.ExprModelTest.User.InnerStaticClass innerStaticInstance = new android.databinding.tool.expr.ExprModelTest.User.InnerStaticClass();

        public static final android.databinding.tool.expr.ExprModelTest.User.InnerStaticClass innerFinalStaticInstance = new android.databinding.tool.expr.ExprModelTest.User.InnerStaticClass();

        public android.databinding.tool.expr.ExprModelTest.SubObj subObj = new android.databinding.tool.expr.ExprModelTest.SubObj();

        public java.lang.String getName() {
            return name;
        }

        public java.lang.String getLastName() {
            return lastName;
        }

        public boolean getCond(int i) {
            return true;
        }

        public android.databinding.tool.expr.ExprModelTest.SubObj getAnotherSubObj() {
            return new android.databinding.tool.expr.ExprModelTest.SubObj();
        }

        public static boolean ourStaticMethod() {
            return true;
        }

        public java.lang.String comment;

        @android.databinding.Bindable
        public boolean getUseComment() {
            return true;
        }

        @java.lang.Override
        public void addOnPropertyChangedCallback(android.databinding.Observable.OnPropertyChangedCallback callback) {
        }

        @java.lang.Override
        public void removeOnPropertyChangedCallback(android.databinding.Observable.OnPropertyChangedCallback callback) {
        }

        public static class InnerStaticClass {
            public static final int finalField = 3;

            public static final int finalStaticField = 3;
        }
    }

    public static class SubObj {
        public final int finalField = 5;
    }
}

