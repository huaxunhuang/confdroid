package android.databinding.testapp;


public class ReadComplexTernaryTest extends android.databinding.testapp.BaseDataBinderTest<android.databinding.testapp.databinding.ReadComplexTernaryBinding> {
    public ReadComplexTernaryTest() {
        super(android.databinding.testapp.databinding.ReadComplexTernaryBinding.class);
    }

    @android.test.UiThreadTest
    public void testWhenNull() {
        initBinder();
        android.databinding.testapp.vo.User user = new android.databinding.testapp.vo.User();
        user.setName("a");
        user.setFullName("a b");
        mBinder.setUser(user);
        mBinder.executePendingBindings();
        junit.framework.TestCase.assertEquals("?", mBinder.textView.getText().toString());
    }
}

