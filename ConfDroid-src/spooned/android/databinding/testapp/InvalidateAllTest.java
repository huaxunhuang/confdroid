package android.databinding.testapp;


public class InvalidateAllTest extends android.databinding.testapp.BaseDataBinderTest<android.databinding.testapp.databinding.InvalidateAllLayoutBinding> {
    public InvalidateAllTest() {
        super(android.databinding.testapp.databinding.InvalidateAllLayoutBinding.class);
    }

    public void testRefreshViaInvalidateAll() throws java.lang.InterruptedException {
        final java.util.concurrent.Semaphore semaphore = new java.util.concurrent.Semaphore(1);
        semaphore.acquire();
        final android.databinding.testapp.vo.NotBindableVo vo = new android.databinding.testapp.vo.NotBindableVo("foo");
        initBinder(new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                mBinder.setVo(vo);
                mBinder.addOnRebindCallback(new android.databinding.OnRebindCallback() {
                    @java.lang.Override
                    public void onBound(android.databinding.ViewDataBinding binding) {
                        super.onBound(binding);
                        semaphore.release();
                    }
                });
            }
        });
        junit.framework.TestCase.assertTrue(semaphore.tryAcquire(2, java.util.concurrent.TimeUnit.SECONDS));
        junit.framework.TestCase.assertEquals("foo", mBinder.textView.getText().toString());
        vo.setStringValue("bar");
        mBinder.invalidateAll();
        junit.framework.TestCase.assertTrue(semaphore.tryAcquire(2, java.util.concurrent.TimeUnit.SECONDS));
        junit.framework.TestCase.assertEquals("bar", mBinder.textView.getText().toString());
    }
}

