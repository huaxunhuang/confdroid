package android.databinding.testapp;


public class MultiThreadTest extends android.databinding.testapp.BaseDataBinderTest<android.databinding.testapp.databinding.MultiThreadLayoutBinding> {
    public MultiThreadTest() {
        super(android.databinding.testapp.databinding.MultiThreadLayoutBinding.class);
    }

    public void testSetOnBackgroundThread() throws java.lang.Throwable {
        initBinder();
        mBinder.setText("a");
        junit.framework.TestCase.assertEquals("a", mBinder.getText());
        java.lang.Thread.sleep(500);
        runTestOnUiThread(new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                junit.framework.TestCase.assertEquals("a", mBinder.myTextView.getText().toString());
            }
        });
        mBinder.setText("b");
        java.lang.Thread.sleep(500);
        junit.framework.TestCase.assertEquals("b", mBinder.getText());
        runTestOnUiThread(new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                junit.framework.TestCase.assertEquals("b", mBinder.myTextView.getText().toString());
            }
        });
    }
}

