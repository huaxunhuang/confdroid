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


class LayoutInflaterCompatBase {
    static class FactoryWrapper implements android.view.LayoutInflater.Factory {
        final android.support.v4.view.LayoutInflaterFactory mDelegateFactory;

        FactoryWrapper(android.support.v4.view.LayoutInflaterFactory delegateFactory) {
            mDelegateFactory = delegateFactory;
        }

        @java.lang.Override
        public android.view.View onCreateView(java.lang.String name, android.content.Context context, android.util.AttributeSet attrs) {
            return mDelegateFactory.onCreateView(null, name, context, attrs);
        }

        public java.lang.String toString() {
            return ((getClass().getName() + "{") + mDelegateFactory) + "}";
        }
    }

    static void setFactory(android.view.LayoutInflater inflater, android.support.v4.view.LayoutInflaterFactory factory) {
        inflater.setFactory(factory != null ? new android.support.v4.view.LayoutInflaterCompatBase.FactoryWrapper(factory) : null);
    }

    static android.support.v4.view.LayoutInflaterFactory getFactory(android.view.LayoutInflater inflater) {
        android.view.LayoutInflater.Factory factory = inflater.getFactory();
        if (factory instanceof android.support.v4.view.LayoutInflaterCompatBase.FactoryWrapper) {
            return ((android.support.v4.view.LayoutInflaterCompatBase.FactoryWrapper) (factory)).mDelegateFactory;
        }
        return null;
    }
}

