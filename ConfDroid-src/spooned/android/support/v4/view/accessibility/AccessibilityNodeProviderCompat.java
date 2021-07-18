/**
 * Copyright (C) 2012 The Android Open Source Project
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
package android.support.v4.view.accessibility;


/**
 * Helper for accessing {@link android.view.accessibility.AccessibilityNodeProvider}
 * introduced after API level 4 in a backwards compatible fashion.
 */
public class AccessibilityNodeProviderCompat {
    interface AccessibilityNodeProviderImpl {
        java.lang.Object newAccessibilityNodeProviderBridge(android.support.v4.view.accessibility.AccessibilityNodeProviderCompat compat);
    }

    static class AccessibilityNodeProviderStubImpl implements android.support.v4.view.accessibility.AccessibilityNodeProviderCompat.AccessibilityNodeProviderImpl {
        @java.lang.Override
        public java.lang.Object newAccessibilityNodeProviderBridge(android.support.v4.view.accessibility.AccessibilityNodeProviderCompat compat) {
            return null;
        }
    }

    private static class AccessibilityNodeProviderJellyBeanImpl extends android.support.v4.view.accessibility.AccessibilityNodeProviderCompat.AccessibilityNodeProviderStubImpl {
        AccessibilityNodeProviderJellyBeanImpl() {
        }

        @java.lang.Override
        public java.lang.Object newAccessibilityNodeProviderBridge(final android.support.v4.view.accessibility.AccessibilityNodeProviderCompat compat) {
            return android.support.v4.view.accessibility.AccessibilityNodeProviderCompatJellyBean.newAccessibilityNodeProviderBridge(new android.support.v4.view.accessibility.AccessibilityNodeProviderCompatJellyBean.AccessibilityNodeInfoBridge() {
                @java.lang.Override
                public boolean performAction(int virtualViewId, int action, android.os.Bundle arguments) {
                    return compat.performAction(virtualViewId, action, arguments);
                }

                @java.lang.Override
                public java.util.List<java.lang.Object> findAccessibilityNodeInfosByText(java.lang.String text, int virtualViewId) {
                    final java.util.List<android.support.v4.view.accessibility.AccessibilityNodeInfoCompat> compatInfos = compat.findAccessibilityNodeInfosByText(text, virtualViewId);
                    if (compatInfos == null) {
                        return null;
                    } else {
                        final java.util.List<java.lang.Object> infos = new java.util.ArrayList<>();
                        final int infoCount = compatInfos.size();
                        for (int i = 0; i < infoCount; i++) {
                            android.support.v4.view.accessibility.AccessibilityNodeInfoCompat infoCompat = compatInfos.get(i);
                            infos.add(infoCompat.getInfo());
                        }
                        return infos;
                    }
                }

                @java.lang.Override
                public java.lang.Object createAccessibilityNodeInfo(int virtualViewId) {
                    final android.support.v4.view.accessibility.AccessibilityNodeInfoCompat compatInfo = compat.createAccessibilityNodeInfo(virtualViewId);
                    if (compatInfo == null) {
                        return null;
                    } else {
                        return compatInfo.getInfo();
                    }
                }
            });
        }
    }

    private static class AccessibilityNodeProviderKitKatImpl extends android.support.v4.view.accessibility.AccessibilityNodeProviderCompat.AccessibilityNodeProviderStubImpl {
        AccessibilityNodeProviderKitKatImpl() {
        }

        @java.lang.Override
        public java.lang.Object newAccessibilityNodeProviderBridge(final android.support.v4.view.accessibility.AccessibilityNodeProviderCompat compat) {
            return android.support.v4.view.accessibility.AccessibilityNodeProviderCompatKitKat.newAccessibilityNodeProviderBridge(new android.support.v4.view.accessibility.AccessibilityNodeProviderCompatKitKat.AccessibilityNodeInfoBridge() {
                @java.lang.Override
                public boolean performAction(int virtualViewId, int action, android.os.Bundle arguments) {
                    return compat.performAction(virtualViewId, action, arguments);
                }

                @java.lang.Override
                public java.util.List<java.lang.Object> findAccessibilityNodeInfosByText(java.lang.String text, int virtualViewId) {
                    final java.util.List<android.support.v4.view.accessibility.AccessibilityNodeInfoCompat> compatInfos = compat.findAccessibilityNodeInfosByText(text, virtualViewId);
                    if (compatInfos == null) {
                        return null;
                    } else {
                        final java.util.List<java.lang.Object> infos = new java.util.ArrayList<>();
                        final int infoCount = compatInfos.size();
                        for (int i = 0; i < infoCount; i++) {
                            android.support.v4.view.accessibility.AccessibilityNodeInfoCompat infoCompat = compatInfos.get(i);
                            infos.add(infoCompat.getInfo());
                        }
                        return infos;
                    }
                }

                @java.lang.Override
                public java.lang.Object createAccessibilityNodeInfo(int virtualViewId) {
                    final android.support.v4.view.accessibility.AccessibilityNodeInfoCompat compatInfo = compat.createAccessibilityNodeInfo(virtualViewId);
                    if (compatInfo == null) {
                        return null;
                    } else {
                        return compatInfo.getInfo();
                    }
                }

                @java.lang.Override
                public java.lang.Object findFocus(int focus) {
                    final android.support.v4.view.accessibility.AccessibilityNodeInfoCompat compatInfo = compat.findFocus(focus);
                    if (compatInfo == null) {
                        return null;
                    } else {
                        return compatInfo.getInfo();
                    }
                }
            });
        }
    }

    /**
     * The virtual id for the hosting View.
     */
    public static final int HOST_VIEW_ID = -1;

    private static final android.support.v4.view.accessibility.AccessibilityNodeProviderCompat.AccessibilityNodeProviderImpl IMPL;

    private final java.lang.Object mProvider;

    static {
        if (android.os.Build.VERSION.SDK_INT >= 19) {
            // KitKat
            IMPL = new android.support.v4.view.accessibility.AccessibilityNodeProviderCompat.AccessibilityNodeProviderKitKatImpl();
        } else
            if (android.os.Build.VERSION.SDK_INT >= 16) {
                // JellyBean
                IMPL = new android.support.v4.view.accessibility.AccessibilityNodeProviderCompat.AccessibilityNodeProviderJellyBeanImpl();
            } else {
                IMPL = new android.support.v4.view.accessibility.AccessibilityNodeProviderCompat.AccessibilityNodeProviderStubImpl();
            }

    }

    /**
     * Creates a new instance.
     */
    public AccessibilityNodeProviderCompat() {
        mProvider = android.support.v4.view.accessibility.AccessibilityNodeProviderCompat.IMPL.newAccessibilityNodeProviderBridge(this);
    }

    /**
     * Creates a new instance wrapping an
     * {@link android.view.accessibility.AccessibilityNodeProvider}.
     *
     * @param provider
     * 		The provider.
     */
    public AccessibilityNodeProviderCompat(java.lang.Object provider) {
        mProvider = provider;
    }

    /**
     *
     *
     * @return The wrapped {@link android.view.accessibility.AccessibilityNodeProvider}.
     */
    public java.lang.Object getProvider() {
        return mProvider;
    }

    /**
     * Returns an {@link AccessibilityNodeInfoCompat} representing a virtual view,
     * i.e. a descendant of the host View, with the given <code>virtualViewId</code>
     * or the host View itself if <code>virtualViewId</code> equals to {@link #HOST_VIEW_ID}.
     * <p>
     * A virtual descendant is an imaginary View that is reported as a part of the view
     * hierarchy for accessibility purposes. This enables custom views that draw complex
     * content to report them selves as a tree of virtual views, thus conveying their
     * logical structure.
     * </p>
     * <p>
     * The implementer is responsible for obtaining an accessibility node info from the
     * pool of reusable instances and setting the desired properties of the node info
     * before returning it.
     * </p>
     *
     * @param virtualViewId
     * 		A client defined virtual view id.
     * @return A populated {@link AccessibilityNodeInfoCompat} for a virtual descendant
    or the host View.
     * @see AccessibilityNodeInfoCompat
     */
    @android.support.annotation.Nullable
    public android.support.v4.view.accessibility.AccessibilityNodeInfoCompat createAccessibilityNodeInfo(int virtualViewId) {
        return null;
    }

    /**
     * Performs an accessibility action on a virtual view, i.e. a descendant of the
     * host View, with the given <code>virtualViewId</code> or the host View itself
     * if <code>virtualViewId</code> equals to {@link #HOST_VIEW_ID}.
     *
     * @param virtualViewId
     * 		A client defined virtual view id.
     * @param action
     * 		The action to perform.
     * @param arguments
     * 		Optional arguments.
     * @return True if the action was performed.
     * @see #createAccessibilityNodeInfo(int)
     * @see AccessibilityNodeInfoCompat
     */
    public boolean performAction(int virtualViewId, int action, android.os.Bundle arguments) {
        return false;
    }

    /**
     * Finds {@link AccessibilityNodeInfoCompat}s by text. The match is case insensitive
     * containment. The search is relative to the virtual view, i.e. a descendant of the
     * host View, with the given <code>virtualViewId</code> or the host View itself
     * <code>virtualViewId</code> equals to {@link #HOST_VIEW_ID}.
     *
     * @param virtualViewId
     * 		A client defined virtual view id which defined
     * 		the root of the tree in which to perform the search.
     * @param text
     * 		The searched text.
     * @return A list of node info.
     * @see #createAccessibilityNodeInfo(int)
     * @see AccessibilityNodeInfoCompat
     */
    @android.support.annotation.Nullable
    public java.util.List<android.support.v4.view.accessibility.AccessibilityNodeInfoCompat> findAccessibilityNodeInfosByText(java.lang.String text, int virtualViewId) {
        return null;
    }

    /**
     * Find the virtual view, i.e. a descendant of the host View, that has the
     * specified focus type.
     *
     * @param focus
     * 		The focus to find. One of
     * 		{@link AccessibilityNodeInfoCompat#FOCUS_INPUT} or
     * 		{@link AccessibilityNodeInfoCompat#FOCUS_ACCESSIBILITY}.
     * @return The node info of the focused view or null.
     * @see AccessibilityNodeInfoCompat#FOCUS_INPUT
     * @see AccessibilityNodeInfoCompat#FOCUS_ACCESSIBILITY
     */
    @android.support.annotation.Nullable
    public android.support.v4.view.accessibility.AccessibilityNodeInfoCompat findFocus(int focus) {
        return null;
    }
}

