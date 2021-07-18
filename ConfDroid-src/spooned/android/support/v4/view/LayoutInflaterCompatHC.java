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
package android.support.v4.view;


class LayoutInflaterCompatHC {
    private static final java.lang.String TAG = "LayoutInflaterCompatHC";

    private static java.lang.reflect.Field sLayoutInflaterFactory2Field;

    private static boolean sCheckedField;

    static class FactoryWrapperHC extends android.support.v4.view.LayoutInflaterCompatBase.FactoryWrapper implements android.view.LayoutInflater.Factory2 {
        FactoryWrapperHC(android.support.v4.view.LayoutInflaterFactory delegateFactory) {
            super(delegateFactory);
        }

        @java.lang.Override
        public android.view.View onCreateView(android.view.View parent, java.lang.String name, android.content.Context context, android.util.AttributeSet attributeSet) {
            return mDelegateFactory.onCreateView(parent, name, context, attributeSet);
        }
    }

    static void setFactory(android.view.LayoutInflater inflater, android.support.v4.view.LayoutInflaterFactory factory) {
        final android.view.LayoutInflater.Factory2 factory2 = (factory != null) ? new android.support.v4.view.LayoutInflaterCompatHC.FactoryWrapperHC(factory) : null;
        inflater.setFactory2(factory2);
        final android.view.LayoutInflater.Factory f = inflater.getFactory();
        if (f instanceof android.view.LayoutInflater.Factory2) {
            // The merged factory is now set to getFactory(), but not getFactory2() (pre-v21).
            // We will now try and force set the merged factory to mFactory2
            android.support.v4.view.LayoutInflaterCompatHC.forceSetFactory2(inflater, ((android.view.LayoutInflater.Factory2) (f)));
        } else {
            // Else, we will force set the original wrapped Factory2
            android.support.v4.view.LayoutInflaterCompatHC.forceSetFactory2(inflater, factory2);
        }
    }

    /**
     * For APIs >= 11 && < 21, there was a framework bug that prevented a LayoutInflater's
     * Factory2 from being merged properly if set after a cloneInContext from a LayoutInflater
     * that already had a Factory2 registered. We work around that bug here. If we can't we
     * log an error.
     */
    static void forceSetFactory2(android.view.LayoutInflater inflater, android.view.LayoutInflater.Factory2 factory) {
        if (!android.support.v4.view.LayoutInflaterCompatHC.sCheckedField) {
            try {
                android.support.v4.view.LayoutInflaterCompatHC.sLayoutInflaterFactory2Field = android.view.LayoutInflater.class.getDeclaredField("mFactory2");
                android.support.v4.view.LayoutInflaterCompatHC.sLayoutInflaterFactory2Field.setAccessible(true);
            } catch (java.lang.NoSuchFieldException e) {
                android.util.Log.e(android.support.v4.view.LayoutInflaterCompatHC.TAG, ("forceSetFactory2 Could not find field 'mFactory2' on class " + android.view.LayoutInflater.class.getName()) + "; inflation may have unexpected results.", e);
            }
            android.support.v4.view.LayoutInflaterCompatHC.sCheckedField = true;
        }
        if (android.support.v4.view.LayoutInflaterCompatHC.sLayoutInflaterFactory2Field != null) {
            try {
                android.support.v4.view.LayoutInflaterCompatHC.sLayoutInflaterFactory2Field.set(inflater, factory);
            } catch (java.lang.IllegalAccessException e) {
                android.util.Log.e(android.support.v4.view.LayoutInflaterCompatHC.TAG, ("forceSetFactory2 could not set the Factory2 on LayoutInflater " + inflater) + "; inflation may have unexpected results.", e);
            }
        }
    }
}

