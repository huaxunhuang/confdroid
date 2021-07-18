package android.support.v17.leanback.widget;


final class ForegroundHelper {
    static final android.support.v17.leanback.widget.ForegroundHelper sInstance = new android.support.v17.leanback.widget.ForegroundHelper();

    android.support.v17.leanback.widget.ForegroundHelper.ForegroundHelperVersionImpl mImpl;

    /**
     * Interface implemented by classes that support Shadow.
     */
    static interface ForegroundHelperVersionImpl {
        public void setForeground(android.view.View view, android.graphics.drawable.Drawable drawable);

        public android.graphics.drawable.Drawable getForeground(android.view.View view);
    }

    /**
     * Implementation used on api 23 (and above).
     */
    private static final class ForegroundHelperApi23Impl implements android.support.v17.leanback.widget.ForegroundHelper.ForegroundHelperVersionImpl {
        ForegroundHelperApi23Impl() {
        }

        @java.lang.Override
        public void setForeground(android.view.View view, android.graphics.drawable.Drawable drawable) {
            android.support.v17.leanback.widget.ForegroundHelperApi23.setForeground(view, drawable);
        }

        @java.lang.Override
        public android.graphics.drawable.Drawable getForeground(android.view.View view) {
            return android.support.v17.leanback.widget.ForegroundHelperApi23.getForeground(view);
        }
    }

    /**
     * Stub implementation
     */
    private static final class ForegroundHelperStubImpl implements android.support.v17.leanback.widget.ForegroundHelper.ForegroundHelperVersionImpl {
        ForegroundHelperStubImpl() {
        }

        @java.lang.Override
        public void setForeground(android.view.View view, android.graphics.drawable.Drawable drawable) {
        }

        @java.lang.Override
        public android.graphics.drawable.Drawable getForeground(android.view.View view) {
            return null;
        }
    }

    private ForegroundHelper() {
        if (android.support.v17.leanback.widget.ForegroundHelper.supportsForeground()) {
            mImpl = new android.support.v17.leanback.widget.ForegroundHelper.ForegroundHelperApi23Impl();
        } else {
            mImpl = new android.support.v17.leanback.widget.ForegroundHelper.ForegroundHelperStubImpl();
        }
    }

    public static android.support.v17.leanback.widget.ForegroundHelper getInstance() {
        return android.support.v17.leanback.widget.ForegroundHelper.sInstance;
    }

    /**
     * Returns true if view.setForeground() is supported.
     */
    public static boolean supportsForeground() {
        return android.os.Build.VERSION.SDK_INT >= 23;
    }

    public android.graphics.drawable.Drawable getForeground(android.view.View view) {
        return mImpl.getForeground(view);
    }

    public void setForeground(android.view.View view, android.graphics.drawable.Drawable drawable) {
        mImpl.setForeground(view, drawable);
    }
}

