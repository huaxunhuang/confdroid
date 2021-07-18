package android.databinding.testapp;


public class ConditionalBindingTest extends android.databinding.testapp.BaseDataBinderTest<android.databinding.testapp.databinding.ConditionalBindingBinding> {
    public ConditionalBindingTest() {
        super(android.databinding.testapp.databinding.ConditionalBindingBinding.class);
    }

    @android.test.UiThreadTest
    public void test1() {
        initBinder();
        testCorrectness(true, true);
    }

    @android.test.UiThreadTest
    public void testTernary() throws java.lang.Throwable {
        android.databinding.testapp.vo.ConditionalVo obj4 = new android.databinding.testapp.vo.ConditionalVo();
        initBinder();
        mBinder.setObj4(obj4);
        mBinder.executePendingBindings();
        junit.framework.TestCase.assertEquals("hello", mBinder.textView1.getText().toString());
        obj4.setUseHello(true);
        mBinder.executePendingBindings();
        junit.framework.TestCase.assertEquals("Hello World", mBinder.textView1.getText().toString());
    }

    @android.test.UiThreadTest
    public void testNullListener() throws java.lang.Throwable {
        android.databinding.testapp.vo.ConditionalVo obj4 = new android.databinding.testapp.vo.ConditionalVo();
        initBinder();
        mBinder.setObj4(obj4);
        mBinder.executePendingBindings();
        mBinder.view1.callOnClick();
        junit.framework.TestCase.assertFalse(obj4.wasClicked);
        mBinder.setCond1(true);
        mBinder.executePendingBindings();
        mBinder.view1.callOnClick();
        junit.framework.TestCase.assertTrue(obj4.wasClicked);
    }

    private void testCorrectness(boolean cond1, boolean cond2) {
        android.databinding.testapp.vo.NotBindableVo o1 = new android.databinding.testapp.vo.NotBindableVo("a");
        android.databinding.testapp.vo.NotBindableVo o2 = new android.databinding.testapp.vo.NotBindableVo("b");
        android.databinding.testapp.vo.NotBindableVo o3 = new android.databinding.testapp.vo.NotBindableVo("c");
        mBinder.setObj1(o1);
        mBinder.setObj2(o2);
        mBinder.setObj3(o3);
        mBinder.setCond1(cond1);
        mBinder.setCond2(cond2);
        mBinder.executePendingBindings();
        final java.lang.String text = mBinder.textView.getText().toString();
        junit.framework.TestCase.assertEquals(cond1 && cond2, "a".equals(text));
        junit.framework.TestCase.assertEquals(cond1 && (!cond2), "b".equals(text));
        junit.framework.TestCase.assertEquals(!cond1, "c".equals(text));
    }
}

