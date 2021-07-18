package android.databinding.testapp;


public class CustomNamespaceAdapterTest extends android.databinding.testapp.BaseDataBinderTest<android.databinding.testapp.databinding.CustomNsAdapterBinding> {
    public CustomNamespaceAdapterTest() {
        super(android.databinding.testapp.databinding.CustomNsAdapterBinding.class);
    }

    @android.test.UiThreadTest
    public void testAndroidNs() {
        initBinder();
        mBinder.setStr1("a");
        mBinder.setStr2("b");
        mBinder.executePendingBindings();
        junit.framework.TestCase.assertEquals("a", mBinder.textView1.getText().toString());
    }

    @android.test.UiThreadTest
    public void testCustomNs() {
        initBinder();
        mBinder.setStr1("a");
        mBinder.setStr2("b");
        mBinder.executePendingBindings();
        junit.framework.TestCase.assertEquals("b", mBinder.textView2.getText().toString());
    }
}

