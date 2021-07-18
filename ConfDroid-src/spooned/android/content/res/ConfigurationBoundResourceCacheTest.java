/**
 * Copyright (C) 2014 The Android Open Source Project
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
package android.content.res;


public class ConfigurationBoundResourceCacheTest extends android.test.ActivityInstrumentationTestCase2<android.content.res.ResourceCacheActivity> {
    android.content.res.ConfigurationBoundResourceCache<java.lang.Float> mCache;

    java.lang.reflect.Method mCalcConfigChanges;

    public ConfigurationBoundResourceCacheTest() {
        super(android.content.res.ResourceCacheActivity.class);
    }

    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
        super.setUp();
        mCache = new android.content.res.ConfigurationBoundResourceCache<java.lang.Float>(getActivity().getResources());
    }

    public void testGetEmpty() {
        assertNull(mCache.get(-1, null));
    }

    public void testSetGet() {
        mCache.put(1, null, new android.content.res.ConfigurationBoundResourceCacheTest.DummyFloatConstantState(5.0F));
        assertEquals(5.0F, mCache.get(1, null));
        assertNotSame(5.0F, mCache.get(1, null));
        assertEquals(null, mCache.get(1, getActivity().getTheme()));
    }

    public void testSetGetThemed() {
        mCache.put(1, getActivity().getTheme(), new android.content.res.ConfigurationBoundResourceCacheTest.DummyFloatConstantState(5.0F));
        assertEquals(null, mCache.get(1, null));
        assertEquals(5.0F, mCache.get(1, getActivity().getTheme()));
        assertNotSame(5.0F, mCache.get(1, getActivity().getTheme()));
    }

    public void testMultiThreadPutGet() {
        mCache.put(1, getActivity().getTheme(), new android.content.res.ConfigurationBoundResourceCacheTest.DummyFloatConstantState(5.0F));
        mCache.put(1, null, new android.content.res.ConfigurationBoundResourceCacheTest.DummyFloatConstantState(10.0F));
        assertEquals(10.0F, mCache.get(1, null));
        assertNotSame(10.0F, mCache.get(1, null));
        assertEquals(5.0F, mCache.get(1, getActivity().getTheme()));
        assertNotSame(5.0F, mCache.get(1, getActivity().getTheme()));
    }

    public void testVoidConfigChange() throws java.lang.IllegalAccessException, java.lang.NoSuchMethodException, java.lang.reflect.InvocationTargetException {
        android.util.TypedValue staticValue = new android.util.TypedValue();
        long key = 3L;
        final android.content.res.Resources res = getActivity().getResources();
        res.getValue(R.dimen.resource_cache_test_generic, staticValue, true);
        float staticDim = android.util.TypedValue.complexToDimension(staticValue.data, res.getDisplayMetrics());
        mCache.put(key, getActivity().getTheme(), new android.content.res.ConfigurationBoundResourceCacheTest.DummyFloatConstantState(staticDim, staticValue.changingConfigurations));
        final android.content.res.Configuration cfg = res.getConfiguration();
        android.content.res.Configuration newCnf = new android.content.res.Configuration(cfg);
        newCnf.orientation = (cfg.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE) ? android.content.res.Configuration.ORIENTATION_PORTRAIT : android.content.res.Configuration.ORIENTATION_LANDSCAPE;
        int changes = calcConfigChanges(res, newCnf);
        assertEquals(staticDim, mCache.get(key, getActivity().getTheme()));
        mCache.onConfigurationChange(changes);
        assertEquals(staticDim, mCache.get(key, getActivity().getTheme()));
    }

    public void testEffectiveConfigChange() throws java.lang.IllegalAccessException, java.lang.NoSuchMethodException, java.lang.reflect.InvocationTargetException {
        android.util.TypedValue changingValue = new android.util.TypedValue();
        long key = 4L;
        final android.content.res.Resources res = getActivity().getResources();
        res.getValue(R.dimen.resource_cache_test_orientation_dependent, changingValue, true);
        float changingDim = android.util.TypedValue.complexToDimension(changingValue.data, res.getDisplayMetrics());
        mCache.put(key, getActivity().getTheme(), new android.content.res.ConfigurationBoundResourceCacheTest.DummyFloatConstantState(changingDim, changingValue.changingConfigurations));
        final android.content.res.Configuration cfg = res.getConfiguration();
        android.content.res.Configuration newCnf = new android.content.res.Configuration(cfg);
        newCnf.orientation = (cfg.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE) ? android.content.res.Configuration.ORIENTATION_PORTRAIT : android.content.res.Configuration.ORIENTATION_LANDSCAPE;
        int changes = calcConfigChanges(res, newCnf);
        assertEquals(changingDim, mCache.get(key, getActivity().getTheme()));
        mCache.onConfigurationChange(changes);
        assertNull(mCache.get(key, getActivity().getTheme()));
    }

    public void testConfigChangeMultipleResources() throws java.lang.IllegalAccessException, java.lang.NoSuchMethodException, java.lang.reflect.InvocationTargetException {
        android.util.TypedValue staticValue = new android.util.TypedValue();
        android.util.TypedValue changingValue = new android.util.TypedValue();
        final android.content.res.Resources res = getActivity().getResources();
        res.getValue(R.dimen.resource_cache_test_generic, staticValue, true);
        res.getValue(R.dimen.resource_cache_test_orientation_dependent, changingValue, true);
        float staticDim = android.util.TypedValue.complexToDimension(staticValue.data, res.getDisplayMetrics());
        float changingDim = android.util.TypedValue.complexToDimension(changingValue.data, res.getDisplayMetrics());
        mCache.put(R.dimen.resource_cache_test_generic, getActivity().getTheme(), new android.content.res.ConfigurationBoundResourceCacheTest.DummyFloatConstantState(staticDim, staticValue.changingConfigurations));
        mCache.put(R.dimen.resource_cache_test_orientation_dependent, getActivity().getTheme(), new android.content.res.ConfigurationBoundResourceCacheTest.DummyFloatConstantState(changingDim, changingValue.changingConfigurations));
        final android.content.res.Configuration cfg = res.getConfiguration();
        android.content.res.Configuration newCnf = new android.content.res.Configuration(cfg);
        newCnf.orientation = (cfg.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE) ? android.content.res.Configuration.ORIENTATION_PORTRAIT : android.content.res.Configuration.ORIENTATION_LANDSCAPE;
        int changes = calcConfigChanges(res, newCnf);
        assertEquals(staticDim, mCache.get(R.dimen.resource_cache_test_generic, getActivity().getTheme()));
        assertEquals(changingDim, mCache.get(R.dimen.resource_cache_test_orientation_dependent, getActivity().getTheme()));
        mCache.onConfigurationChange(changes);
        assertEquals(staticDim, mCache.get(R.dimen.resource_cache_test_generic, getActivity().getTheme()));
        assertNull(mCache.get(R.dimen.resource_cache_test_orientation_dependent, getActivity().getTheme()));
    }

    public void testConfigChangeMultipleThemes() throws java.lang.IllegalAccessException, java.lang.NoSuchMethodException, java.lang.reflect.InvocationTargetException {
        android.util.TypedValue[] staticValues = new android.util.TypedValue[]{ new android.util.TypedValue(), new android.util.TypedValue() };
        android.util.TypedValue[] changingValues = new android.util.TypedValue[]{ new android.util.TypedValue(), new android.util.TypedValue() };
        float staticDim = 0;
        float changingDim = 0;
        final android.content.res.Resources res = getActivity().getResources();
        for (int i = 0; i < 2; i++) {
            res.getValue(R.dimen.resource_cache_test_generic, staticValues[i], true);
            staticDim = android.util.TypedValue.complexToDimension(staticValues[i].data, res.getDisplayMetrics());
            res.getValue(R.dimen.resource_cache_test_orientation_dependent, changingValues[i], true);
            changingDim = android.util.TypedValue.complexToDimension(changingValues[i].data, res.getDisplayMetrics());
            final android.content.res.Resources.Theme theme = (i == 0) ? getActivity().getTheme() : null;
            mCache.put(R.dimen.resource_cache_test_generic, theme, new android.content.res.ConfigurationBoundResourceCacheTest.DummyFloatConstantState(staticDim, staticValues[i].changingConfigurations));
            mCache.put(R.dimen.resource_cache_test_orientation_dependent, theme, new android.content.res.ConfigurationBoundResourceCacheTest.DummyFloatConstantState(changingDim, changingValues[i].changingConfigurations));
        }
        final android.content.res.Configuration cfg = res.getConfiguration();
        android.content.res.Configuration newCnf = new android.content.res.Configuration(cfg);
        newCnf.orientation = (cfg.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE) ? android.content.res.Configuration.ORIENTATION_PORTRAIT : android.content.res.Configuration.ORIENTATION_LANDSCAPE;
        int changes = calcConfigChanges(res, newCnf);
        for (int i = 0; i < 2; i++) {
            final android.content.res.Resources.Theme theme = (i == 0) ? getActivity().getTheme() : null;
            assertEquals(staticDim, mCache.get(R.dimen.resource_cache_test_generic, theme));
            assertEquals(changingDim, mCache.get(R.dimen.resource_cache_test_orientation_dependent, theme));
        }
        mCache.onConfigurationChange(changes);
        for (int i = 0; i < 2; i++) {
            final android.content.res.Resources.Theme theme = (i == 0) ? getActivity().getTheme() : null;
            assertEquals(staticDim, mCache.get(R.dimen.resource_cache_test_generic, theme));
            assertNull(mCache.get(R.dimen.resource_cache_test_orientation_dependent, theme));
        }
    }

    private int calcConfigChanges(android.content.res.Resources resources, android.content.res.Configuration configuration) throws java.lang.IllegalAccessException, java.lang.NoSuchMethodException, java.lang.reflect.InvocationTargetException {
        if (mCalcConfigChanges == null) {
            mCalcConfigChanges = android.content.res.Resources.class.getDeclaredMethod("calcConfigChanges", android.content.res.Configuration.class);
            mCalcConfigChanges.setAccessible(true);
        }
        return ((java.lang.Integer) (mCalcConfigChanges.invoke(resources, configuration)));
    }

    static class DummyFloatConstantState extends android.content.res.ConstantState<java.lang.Float> {
        final java.lang.Float mObj;

        int mChangingConf = 0;

        DummyFloatConstantState(java.lang.Float obj) {
            mObj = obj;
        }

        DummyFloatConstantState(java.lang.Float obj, int changingConf) {
            mObj = obj;
            mChangingConf = changingConf;
        }

        @java.lang.Override
        public int getChangingConfigurations() {
            return mChangingConf;
        }

        @java.lang.Override
        public java.lang.Float newInstance() {
            return new java.lang.Float(mObj);
        }
    }
}

