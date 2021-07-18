/**
 * Copyright (C) 2011 The Android Open Source Project
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


/**
 * Helper for accessing {@link View.AccessibilityDelegate} introduced after
 * API level 4 in a backwards compatible fashion.
 * <p>
 * <strong>Note:</strong> On platform versions prior to
 * {@link android.os.Build.VERSION_CODES#M API 23}, delegate methods on
 * views in the {@code android.widget.*} package are called <i>before</i>
 * host methods. This prevents certain properties such as class name from
 * being modified by overriding
 * {@link AccessibilityDelegateCompat#onInitializeAccessibilityNodeInfo(View, AccessibilityNodeInfoCompat)},
 * as any changes will be overwritten by the host class.
 * <p>
 * Starting in {@link android.os.Build.VERSION_CODES#M API 23}, delegate
 * methods are called <i>after</i> host methods, which all properties to be
 * modified without being overwritten by the host class.
 */
public class AccessibilityDelegateCompat {
    static interface AccessibilityDelegateImpl {
        public java.lang.Object newAccessiblityDelegateDefaultImpl();

        public java.lang.Object newAccessiblityDelegateBridge(android.support.v4.view.AccessibilityDelegateCompat listener);

        public boolean dispatchPopulateAccessibilityEvent(java.lang.Object delegate, android.view.View host, android.view.accessibility.AccessibilityEvent event);

        public void onInitializeAccessibilityEvent(java.lang.Object delegate, android.view.View host, android.view.accessibility.AccessibilityEvent event);

        public void onInitializeAccessibilityNodeInfo(java.lang.Object delegate, android.view.View host, android.support.v4.view.accessibility.AccessibilityNodeInfoCompat info);

        public void onPopulateAccessibilityEvent(java.lang.Object delegate, android.view.View host, android.view.accessibility.AccessibilityEvent event);

        public boolean onRequestSendAccessibilityEvent(java.lang.Object delegate, android.view.ViewGroup host, android.view.View child, android.view.accessibility.AccessibilityEvent event);

        public void sendAccessibilityEvent(java.lang.Object delegate, android.view.View host, int eventType);

        public void sendAccessibilityEventUnchecked(java.lang.Object delegate, android.view.View host, android.view.accessibility.AccessibilityEvent event);

        public android.support.v4.view.accessibility.AccessibilityNodeProviderCompat getAccessibilityNodeProvider(java.lang.Object delegate, android.view.View host);

        public boolean performAccessibilityAction(java.lang.Object delegate, android.view.View host, int action, android.os.Bundle args);
    }

    static class AccessibilityDelegateStubImpl implements android.support.v4.view.AccessibilityDelegateCompat.AccessibilityDelegateImpl {
        @java.lang.Override
        public java.lang.Object newAccessiblityDelegateDefaultImpl() {
            return null;
        }

        @java.lang.Override
        public java.lang.Object newAccessiblityDelegateBridge(android.support.v4.view.AccessibilityDelegateCompat listener) {
            return null;
        }

        @java.lang.Override
        public boolean dispatchPopulateAccessibilityEvent(java.lang.Object delegate, android.view.View host, android.view.accessibility.AccessibilityEvent event) {
            return false;
        }

        @java.lang.Override
        public void onInitializeAccessibilityEvent(java.lang.Object delegate, android.view.View host, android.view.accessibility.AccessibilityEvent event) {
        }

        @java.lang.Override
        public void onInitializeAccessibilityNodeInfo(java.lang.Object delegate, android.view.View host, android.support.v4.view.accessibility.AccessibilityNodeInfoCompat info) {
        }

        @java.lang.Override
        public void onPopulateAccessibilityEvent(java.lang.Object delegate, android.view.View host, android.view.accessibility.AccessibilityEvent event) {
        }

        @java.lang.Override
        public boolean onRequestSendAccessibilityEvent(java.lang.Object delegate, android.view.ViewGroup host, android.view.View child, android.view.accessibility.AccessibilityEvent event) {
            return true;
        }

        @java.lang.Override
        public void sendAccessibilityEvent(java.lang.Object delegate, android.view.View host, int eventType) {
        }

        @java.lang.Override
        public void sendAccessibilityEventUnchecked(java.lang.Object delegate, android.view.View host, android.view.accessibility.AccessibilityEvent event) {
        }

        @java.lang.Override
        public android.support.v4.view.accessibility.AccessibilityNodeProviderCompat getAccessibilityNodeProvider(java.lang.Object delegate, android.view.View host) {
            return null;
        }

        @java.lang.Override
        public boolean performAccessibilityAction(java.lang.Object delegate, android.view.View host, int action, android.os.Bundle args) {
            return false;
        }
    }

    static class AccessibilityDelegateIcsImpl extends android.support.v4.view.AccessibilityDelegateCompat.AccessibilityDelegateStubImpl {
        @java.lang.Override
        public java.lang.Object newAccessiblityDelegateDefaultImpl() {
            return android.support.v4.view.AccessibilityDelegateCompatIcs.newAccessibilityDelegateDefaultImpl();
        }

        @java.lang.Override
        public java.lang.Object newAccessiblityDelegateBridge(final android.support.v4.view.AccessibilityDelegateCompat compat) {
            return android.support.v4.view.AccessibilityDelegateCompatIcs.newAccessibilityDelegateBridge(new android.support.v4.view.AccessibilityDelegateCompatIcs.AccessibilityDelegateBridge() {
                @java.lang.Override
                public boolean dispatchPopulateAccessibilityEvent(android.view.View host, android.view.accessibility.AccessibilityEvent event) {
                    return compat.dispatchPopulateAccessibilityEvent(host, event);
                }

                @java.lang.Override
                public void onInitializeAccessibilityEvent(android.view.View host, android.view.accessibility.AccessibilityEvent event) {
                    compat.onInitializeAccessibilityEvent(host, event);
                }

                @java.lang.Override
                public void onInitializeAccessibilityNodeInfo(android.view.View host, java.lang.Object info) {
                    compat.onInitializeAccessibilityNodeInfo(host, new android.support.v4.view.accessibility.AccessibilityNodeInfoCompat(info));
                }

                @java.lang.Override
                public void onPopulateAccessibilityEvent(android.view.View host, android.view.accessibility.AccessibilityEvent event) {
                    compat.onPopulateAccessibilityEvent(host, event);
                }

                @java.lang.Override
                public boolean onRequestSendAccessibilityEvent(android.view.ViewGroup host, android.view.View child, android.view.accessibility.AccessibilityEvent event) {
                    return compat.onRequestSendAccessibilityEvent(host, child, event);
                }

                @java.lang.Override
                public void sendAccessibilityEvent(android.view.View host, int eventType) {
                    compat.sendAccessibilityEvent(host, eventType);
                }

                @java.lang.Override
                public void sendAccessibilityEventUnchecked(android.view.View host, android.view.accessibility.AccessibilityEvent event) {
                    compat.sendAccessibilityEventUnchecked(host, event);
                }
            });
        }

        @java.lang.Override
        public boolean dispatchPopulateAccessibilityEvent(java.lang.Object delegate, android.view.View host, android.view.accessibility.AccessibilityEvent event) {
            return android.support.v4.view.AccessibilityDelegateCompatIcs.dispatchPopulateAccessibilityEvent(delegate, host, event);
        }

        @java.lang.Override
        public void onInitializeAccessibilityEvent(java.lang.Object delegate, android.view.View host, android.view.accessibility.AccessibilityEvent event) {
            android.support.v4.view.AccessibilityDelegateCompatIcs.onInitializeAccessibilityEvent(delegate, host, event);
        }

        @java.lang.Override
        public void onInitializeAccessibilityNodeInfo(java.lang.Object delegate, android.view.View host, android.support.v4.view.accessibility.AccessibilityNodeInfoCompat info) {
            android.support.v4.view.AccessibilityDelegateCompatIcs.onInitializeAccessibilityNodeInfo(delegate, host, info.getInfo());
        }

        @java.lang.Override
        public void onPopulateAccessibilityEvent(java.lang.Object delegate, android.view.View host, android.view.accessibility.AccessibilityEvent event) {
            android.support.v4.view.AccessibilityDelegateCompatIcs.onPopulateAccessibilityEvent(delegate, host, event);
        }

        @java.lang.Override
        public boolean onRequestSendAccessibilityEvent(java.lang.Object delegate, android.view.ViewGroup host, android.view.View child, android.view.accessibility.AccessibilityEvent event) {
            return android.support.v4.view.AccessibilityDelegateCompatIcs.onRequestSendAccessibilityEvent(delegate, host, child, event);
        }

        @java.lang.Override
        public void sendAccessibilityEvent(java.lang.Object delegate, android.view.View host, int eventType) {
            android.support.v4.view.AccessibilityDelegateCompatIcs.sendAccessibilityEvent(delegate, host, eventType);
        }

        @java.lang.Override
        public void sendAccessibilityEventUnchecked(java.lang.Object delegate, android.view.View host, android.view.accessibility.AccessibilityEvent event) {
            android.support.v4.view.AccessibilityDelegateCompatIcs.sendAccessibilityEventUnchecked(delegate, host, event);
        }
    }

    static class AccessibilityDelegateJellyBeanImpl extends android.support.v4.view.AccessibilityDelegateCompat.AccessibilityDelegateIcsImpl {
        @java.lang.Override
        public java.lang.Object newAccessiblityDelegateBridge(final android.support.v4.view.AccessibilityDelegateCompat compat) {
            return android.support.v4.view.AccessibilityDelegateCompatJellyBean.newAccessibilityDelegateBridge(new android.support.v4.view.AccessibilityDelegateCompatJellyBean.AccessibilityDelegateBridgeJellyBean() {
                @java.lang.Override
                public boolean dispatchPopulateAccessibilityEvent(android.view.View host, android.view.accessibility.AccessibilityEvent event) {
                    return compat.dispatchPopulateAccessibilityEvent(host, event);
                }

                @java.lang.Override
                public void onInitializeAccessibilityEvent(android.view.View host, android.view.accessibility.AccessibilityEvent event) {
                    compat.onInitializeAccessibilityEvent(host, event);
                }

                @java.lang.Override
                public void onInitializeAccessibilityNodeInfo(android.view.View host, java.lang.Object info) {
                    compat.onInitializeAccessibilityNodeInfo(host, new android.support.v4.view.accessibility.AccessibilityNodeInfoCompat(info));
                }

                @java.lang.Override
                public void onPopulateAccessibilityEvent(android.view.View host, android.view.accessibility.AccessibilityEvent event) {
                    compat.onPopulateAccessibilityEvent(host, event);
                }

                @java.lang.Override
                public boolean onRequestSendAccessibilityEvent(android.view.ViewGroup host, android.view.View child, android.view.accessibility.AccessibilityEvent event) {
                    return compat.onRequestSendAccessibilityEvent(host, child, event);
                }

                @java.lang.Override
                public void sendAccessibilityEvent(android.view.View host, int eventType) {
                    compat.sendAccessibilityEvent(host, eventType);
                }

                @java.lang.Override
                public void sendAccessibilityEventUnchecked(android.view.View host, android.view.accessibility.AccessibilityEvent event) {
                    compat.sendAccessibilityEventUnchecked(host, event);
                }

                @java.lang.Override
                public java.lang.Object getAccessibilityNodeProvider(android.view.View host) {
                    android.support.v4.view.accessibility.AccessibilityNodeProviderCompat provider = compat.getAccessibilityNodeProvider(host);
                    return provider != null ? provider.getProvider() : null;
                }

                @java.lang.Override
                public boolean performAccessibilityAction(android.view.View host, int action, android.os.Bundle args) {
                    return compat.performAccessibilityAction(host, action, args);
                }
            });
        }

        @java.lang.Override
        public android.support.v4.view.accessibility.AccessibilityNodeProviderCompat getAccessibilityNodeProvider(java.lang.Object delegate, android.view.View host) {
            java.lang.Object provider = android.support.v4.view.AccessibilityDelegateCompatJellyBean.getAccessibilityNodeProvider(delegate, host);
            if (provider != null) {
                return new android.support.v4.view.accessibility.AccessibilityNodeProviderCompat(provider);
            }
            return null;
        }

        @java.lang.Override
        public boolean performAccessibilityAction(java.lang.Object delegate, android.view.View host, int action, android.os.Bundle args) {
            return android.support.v4.view.AccessibilityDelegateCompatJellyBean.performAccessibilityAction(delegate, host, action, args);
        }
    }

    private static final android.support.v4.view.AccessibilityDelegateCompat.AccessibilityDelegateImpl IMPL;

    private static final java.lang.Object DEFAULT_DELEGATE;

    static {
        if (android.os.Build.VERSION.SDK_INT >= 16) {
            // JellyBean
            IMPL = new android.support.v4.view.AccessibilityDelegateCompat.AccessibilityDelegateJellyBeanImpl();
        } else
            if (android.os.Build.VERSION.SDK_INT >= 14) {
                // ICS
                IMPL = new android.support.v4.view.AccessibilityDelegateCompat.AccessibilityDelegateIcsImpl();
            } else {
                IMPL = new android.support.v4.view.AccessibilityDelegateCompat.AccessibilityDelegateStubImpl();
            }

        DEFAULT_DELEGATE = android.support.v4.view.AccessibilityDelegateCompat.IMPL.newAccessiblityDelegateDefaultImpl();
    }

    final java.lang.Object mBridge;

    /**
     * Creates a new instance.
     */
    public AccessibilityDelegateCompat() {
        mBridge = android.support.v4.view.AccessibilityDelegateCompat.IMPL.newAccessiblityDelegateBridge(this);
    }

    /**
     *
     *
     * @return The wrapped bridge implementation.
     */
    java.lang.Object getBridge() {
        return mBridge;
    }

    /**
     * Sends an accessibility event of the given type. If accessibility is not
     * enabled this method has no effect.
     * <p>
     * The default implementation behaves as {@link View#sendAccessibilityEvent(int)
     * View#sendAccessibilityEvent(int)} for the case of no accessibility delegate
     * been set.
     * </p>
     *
     * @param host
     * 		The View hosting the delegate.
     * @param eventType
     * 		The type of the event to send.
     * @see View#sendAccessibilityEvent(int) View#sendAccessibilityEvent(int)
     */
    public void sendAccessibilityEvent(android.view.View host, int eventType) {
        android.support.v4.view.AccessibilityDelegateCompat.IMPL.sendAccessibilityEvent(android.support.v4.view.AccessibilityDelegateCompat.DEFAULT_DELEGATE, host, eventType);
    }

    /**
     * Sends an accessibility event. This method behaves exactly as
     * {@link #sendAccessibilityEvent(View, int)} but takes as an argument an
     * empty {@link AccessibilityEvent} and does not perform a check whether
     * accessibility is enabled.
     * <p>
     * The default implementation behaves as
     * {@link View#sendAccessibilityEventUnchecked(AccessibilityEvent)
     * View#sendAccessibilityEventUnchecked(AccessibilityEvent)} for
     * the case of no accessibility delegate been set.
     * </p>
     *
     * @param host
     * 		The View hosting the delegate.
     * @param event
     * 		The event to send.
     * @see View#sendAccessibilityEventUnchecked(AccessibilityEvent)
    View#sendAccessibilityEventUnchecked(AccessibilityEvent)
     */
    public void sendAccessibilityEventUnchecked(android.view.View host, android.view.accessibility.AccessibilityEvent event) {
        android.support.v4.view.AccessibilityDelegateCompat.IMPL.sendAccessibilityEventUnchecked(android.support.v4.view.AccessibilityDelegateCompat.DEFAULT_DELEGATE, host, event);
    }

    /**
     * Dispatches an {@link AccessibilityEvent} to the host {@link View} first and then
     * to its children for adding their text content to the event.
     * <p>
     * The default implementation behaves as
     * {@link View#dispatchPopulateAccessibilityEvent(AccessibilityEvent)
     * View#dispatchPopulateAccessibilityEvent(AccessibilityEvent)} for
     * the case of no accessibility delegate been set.
     * </p>
     *
     * @param host
     * 		The View hosting the delegate.
     * @param event
     * 		The event.
     * @return True if the event population was completed.
     * @see View#dispatchPopulateAccessibilityEvent(AccessibilityEvent)
    View#dispatchPopulateAccessibilityEvent(AccessibilityEvent)
     */
    public boolean dispatchPopulateAccessibilityEvent(android.view.View host, android.view.accessibility.AccessibilityEvent event) {
        return android.support.v4.view.AccessibilityDelegateCompat.IMPL.dispatchPopulateAccessibilityEvent(android.support.v4.view.AccessibilityDelegateCompat.DEFAULT_DELEGATE, host, event);
    }

    /**
     * Gives a chance to the host View to populate the accessibility event with its
     * text content.
     * <p>
     * The default implementation behaves as
     * {@link ViewCompat#onPopulateAccessibilityEvent(View, AccessibilityEvent)
     * ViewCompat#onPopulateAccessibilityEvent(AccessibilityEvent)} for
     * the case of no accessibility delegate been set.
     * </p>
     *
     * @param host
     * 		The View hosting the delegate.
     * @param event
     * 		The accessibility event which to populate.
     * @see ViewCompat#onPopulateAccessibilityEvent(View ,AccessibilityEvent)
    ViewCompat#onPopulateAccessibilityEvent(View, AccessibilityEvent)
     */
    public void onPopulateAccessibilityEvent(android.view.View host, android.view.accessibility.AccessibilityEvent event) {
        android.support.v4.view.AccessibilityDelegateCompat.IMPL.onPopulateAccessibilityEvent(android.support.v4.view.AccessibilityDelegateCompat.DEFAULT_DELEGATE, host, event);
    }

    /**
     * Initializes an {@link AccessibilityEvent} with information about the
     * the host View which is the event source.
     * <p>
     * The default implementation behaves as
     * {@link ViewCompat#onInitializeAccessibilityEvent(View v, AccessibilityEvent event)
     * ViewCompat#onInitalizeAccessibilityEvent(View v, AccessibilityEvent event)} for
     * the case of no accessibility delegate been set.
     * </p>
     *
     * @param host
     * 		The View hosting the delegate.
     * @param event
     * 		The event to initialize.
     * @see ViewCompat#onInitializeAccessibilityEvent(View, AccessibilityEvent)
    ViewCompat#onInitializeAccessibilityEvent(View, AccessibilityEvent)
     */
    public void onInitializeAccessibilityEvent(android.view.View host, android.view.accessibility.AccessibilityEvent event) {
        android.support.v4.view.AccessibilityDelegateCompat.IMPL.onInitializeAccessibilityEvent(android.support.v4.view.AccessibilityDelegateCompat.DEFAULT_DELEGATE, host, event);
    }

    /**
     * Initializes an {@link AccessibilityNodeInfoCompat} with information about the host view.
     * <p>
     * The default implementation behaves as
     * {@link ViewCompat#onInitializeAccessibilityNodeInfo(View, AccessibilityNodeInfoCompat)
     * ViewCompat#onInitializeAccessibilityNodeInfo(View, AccessibilityNodeInfoCompat)} for
     * the case of no accessibility delegate been set.
     * </p>
     *
     * @param host
     * 		The View hosting the delegate.
     * @param info
     * 		The instance to initialize.
     * @see ViewCompat#onInitializeAccessibilityNodeInfo(View, AccessibilityNodeInfoCompat)
    ViewCompat#onInitializeAccessibilityNodeInfo(View, AccessibilityNodeInfoCompat)
     */
    public void onInitializeAccessibilityNodeInfo(android.view.View host, android.support.v4.view.accessibility.AccessibilityNodeInfoCompat info) {
        android.support.v4.view.AccessibilityDelegateCompat.IMPL.onInitializeAccessibilityNodeInfo(android.support.v4.view.AccessibilityDelegateCompat.DEFAULT_DELEGATE, host, info);
    }

    /**
     * Called when a child of the host View has requested sending an
     * {@link AccessibilityEvent} and gives an opportunity to the parent (the host)
     * to augment the event.
     * <p>
     * The default implementation behaves as
     * {@link ViewGroupCompat#onRequestSendAccessibilityEvent(ViewGroup, View, AccessibilityEvent)
     * ViewGroupCompat#onRequestSendAccessibilityEvent(ViewGroup, View, AccessibilityEvent)} for
     * the case of no accessibility delegate been set.
     * </p>
     *
     * @param host
     * 		The View hosting the delegate.
     * @param child
     * 		The child which requests sending the event.
     * @param event
     * 		The event to be sent.
     * @return True if the event should be sent
     * @see ViewGroupCompat#onRequestSendAccessibilityEvent(ViewGroup, View, AccessibilityEvent)
    ViewGroupCompat#onRequestSendAccessibilityEvent(ViewGroup, View, AccessibilityEvent)
     */
    public boolean onRequestSendAccessibilityEvent(android.view.ViewGroup host, android.view.View child, android.view.accessibility.AccessibilityEvent event) {
        return android.support.v4.view.AccessibilityDelegateCompat.IMPL.onRequestSendAccessibilityEvent(android.support.v4.view.AccessibilityDelegateCompat.DEFAULT_DELEGATE, host, child, event);
    }

    /**
     * Gets the provider for managing a virtual view hierarchy rooted at this View
     * and reported to {@link android.accessibilityservice.AccessibilityService}s
     * that explore the window content.
     * <p>
     * The default implementation behaves as
     * {@link ViewCompat#getAccessibilityNodeProvider(View) ViewCompat#getAccessibilityNodeProvider(View)}
     * for the case of no accessibility delegate been set.
     * </p>
     *
     * @return The provider.
     * @see AccessibilityNodeProviderCompat
     */
    public android.support.v4.view.accessibility.AccessibilityNodeProviderCompat getAccessibilityNodeProvider(android.view.View host) {
        return android.support.v4.view.AccessibilityDelegateCompat.IMPL.getAccessibilityNodeProvider(android.support.v4.view.AccessibilityDelegateCompat.DEFAULT_DELEGATE, host);
    }

    /**
     * Performs the specified accessibility action on the view. For
     * possible accessibility actions look at {@link AccessibilityNodeInfoCompat}.
     * <p>
     * The default implementation behaves as
     * {@link View#performAccessibilityAction(int, Bundle)
     *  View#performAccessibilityAction(int, Bundle)} for the case of
     *  no accessibility delegate been set.
     * </p>
     *
     * @param action
     * 		The action to perform.
     * @return Whether the action was performed.
     * @see View#performAccessibilityAction(int, Bundle)
    View#performAccessibilityAction(int, Bundle)
     */
    public boolean performAccessibilityAction(android.view.View host, int action, android.os.Bundle args) {
        return android.support.v4.view.AccessibilityDelegateCompat.IMPL.performAccessibilityAction(android.support.v4.view.AccessibilityDelegateCompat.DEFAULT_DELEGATE, host, action, args);
    }
}

