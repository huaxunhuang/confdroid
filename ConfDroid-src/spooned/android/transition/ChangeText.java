/**
 * Copyright (C) 2013 The Android Open Source Project
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
package android.transition;


/**
 * This transition tracks changes to the text in TextView targets. If the text
 * changes between the start and end scenes, the transition ensures that the
 * starting text stays until the transition ends, at which point it changes
 * to the end text.  This is useful in situations where you want to resize a
 * text view to its new size before displaying the text that goes there.
 *
 * @unknown 
 */
public class ChangeText extends android.transition.Transition {
    private static final java.lang.String LOG_TAG = "TextChange";

    private static final java.lang.String PROPNAME_TEXT = "android:textchange:text";

    private static final java.lang.String PROPNAME_TEXT_SELECTION_START = "android:textchange:textSelectionStart";

    private static final java.lang.String PROPNAME_TEXT_SELECTION_END = "android:textchange:textSelectionEnd";

    private static final java.lang.String PROPNAME_TEXT_COLOR = "android:textchange:textColor";

    private int mChangeBehavior = android.transition.ChangeText.CHANGE_BEHAVIOR_KEEP;

    /**
     * Flag specifying that the text in affected/changing TextView targets will keep
     * their original text during the transition, setting it to the final text when
     * the transition ends. This is the default behavior.
     *
     * @see #setChangeBehavior(int)
     */
    public static final int CHANGE_BEHAVIOR_KEEP = 0;

    /**
     * Flag specifying that the text changing animation should first fade
     * out the original text completely. The new text is set on the target
     * view at the end of the fade-out animation. This transition is typically
     * used with a later {@link #CHANGE_BEHAVIOR_IN} transition, allowing more
     * flexibility than the {@link #CHANGE_BEHAVIOR_OUT_IN} by allowing other
     * transitions to be run sequentially or in parallel with these fades.
     *
     * @see #setChangeBehavior(int)
     */
    public static final int CHANGE_BEHAVIOR_OUT = 1;

    /**
     * Flag specifying that the text changing animation should fade in the
     * end text into the affected target view(s). This transition is typically
     * used in conjunction with an earlier {@link #CHANGE_BEHAVIOR_OUT}
     * transition, possibly with other transitions running as well, such as
     * a sequence to fade out, then resize the view, then fade in.
     *
     * @see #setChangeBehavior(int)
     */
    public static final int CHANGE_BEHAVIOR_IN = 2;

    /**
     * Flag specifying that the text changing animation should first fade
     * out the original text completely and then fade in the
     * new text.
     *
     * @see #setChangeBehavior(int)
     */
    public static final int CHANGE_BEHAVIOR_OUT_IN = 3;

    private static final java.lang.String[] sTransitionProperties = new java.lang.String[]{ android.transition.ChangeText.PROPNAME_TEXT, android.transition.ChangeText.PROPNAME_TEXT_SELECTION_START, android.transition.ChangeText.PROPNAME_TEXT_SELECTION_END };

    /**
     * Sets the type of changing animation that will be run, one of
     * {@link #CHANGE_BEHAVIOR_KEEP}, {@link #CHANGE_BEHAVIOR_OUT},
     * {@link #CHANGE_BEHAVIOR_IN}, and {@link #CHANGE_BEHAVIOR_OUT_IN}.
     *
     * @param changeBehavior
     * 		The type of fading animation to use when this
     * 		transition is run.
     * @return this textChange object.
     */
    public android.transition.ChangeText setChangeBehavior(int changeBehavior) {
        if ((changeBehavior >= android.transition.ChangeText.CHANGE_BEHAVIOR_KEEP) && (changeBehavior <= android.transition.ChangeText.CHANGE_BEHAVIOR_OUT_IN)) {
            mChangeBehavior = changeBehavior;
        }
        return this;
    }

    @java.lang.Override
    public java.lang.String[] getTransitionProperties() {
        return android.transition.ChangeText.sTransitionProperties;
    }

    /**
     * Returns the type of changing animation that will be run.
     *
     * @return either {@link #CHANGE_BEHAVIOR_KEEP}, {@link #CHANGE_BEHAVIOR_OUT},
    {@link #CHANGE_BEHAVIOR_IN}, or {@link #CHANGE_BEHAVIOR_OUT_IN}.
     */
    public int getChangeBehavior() {
        return mChangeBehavior;
    }

    private void captureValues(android.transition.TransitionValues transitionValues) {
        if (transitionValues.view instanceof android.widget.TextView) {
            android.widget.TextView textview = ((android.widget.TextView) (transitionValues.view));
            transitionValues.values.put(android.transition.ChangeText.PROPNAME_TEXT, textview.getText());
            if (textview instanceof android.widget.EditText) {
                transitionValues.values.put(android.transition.ChangeText.PROPNAME_TEXT_SELECTION_START, textview.getSelectionStart());
                transitionValues.values.put(android.transition.ChangeText.PROPNAME_TEXT_SELECTION_END, textview.getSelectionEnd());
            }
            if (mChangeBehavior > android.transition.ChangeText.CHANGE_BEHAVIOR_KEEP) {
                transitionValues.values.put(android.transition.ChangeText.PROPNAME_TEXT_COLOR, textview.getCurrentTextColor());
            }
        }
    }

    @java.lang.Override
    public void captureStartValues(android.transition.TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    @java.lang.Override
    public void captureEndValues(android.transition.TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    @java.lang.Override
    public android.animation.Animator createAnimator(android.view.ViewGroup sceneRoot, android.transition.TransitionValues startValues, android.transition.TransitionValues endValues) {
        if ((((startValues == null) || (endValues == null)) || (!(startValues.view instanceof android.widget.TextView))) || (!(endValues.view instanceof android.widget.TextView))) {
            return null;
        }
        final android.widget.TextView view = ((android.widget.TextView) (endValues.view));
        java.util.Map<java.lang.String, java.lang.Object> startVals = startValues.values;
        java.util.Map<java.lang.String, java.lang.Object> endVals = endValues.values;
        final java.lang.CharSequence startText = (startVals.get(android.transition.ChangeText.PROPNAME_TEXT) != null) ? ((java.lang.CharSequence) (startVals.get(android.transition.ChangeText.PROPNAME_TEXT))) : "";
        final java.lang.CharSequence endText = (endVals.get(android.transition.ChangeText.PROPNAME_TEXT) != null) ? ((java.lang.CharSequence) (endVals.get(android.transition.ChangeText.PROPNAME_TEXT))) : "";
        final int startSelectionStart;
        final int startSelectionEnd;
        final int endSelectionStart;
        final int endSelectionEnd;
        if (view instanceof android.widget.EditText) {
            startSelectionStart = (startVals.get(android.transition.ChangeText.PROPNAME_TEXT_SELECTION_START) != null) ? ((java.lang.Integer) (startVals.get(android.transition.ChangeText.PROPNAME_TEXT_SELECTION_START))) : -1;
            startSelectionEnd = (startVals.get(android.transition.ChangeText.PROPNAME_TEXT_SELECTION_END) != null) ? ((java.lang.Integer) (startVals.get(android.transition.ChangeText.PROPNAME_TEXT_SELECTION_END))) : startSelectionStart;
            endSelectionStart = (endVals.get(android.transition.ChangeText.PROPNAME_TEXT_SELECTION_START) != null) ? ((java.lang.Integer) (endVals.get(android.transition.ChangeText.PROPNAME_TEXT_SELECTION_START))) : -1;
            endSelectionEnd = (endVals.get(android.transition.ChangeText.PROPNAME_TEXT_SELECTION_END) != null) ? ((java.lang.Integer) (endVals.get(android.transition.ChangeText.PROPNAME_TEXT_SELECTION_END))) : endSelectionStart;
        } else {
            startSelectionStart = startSelectionEnd = endSelectionStart = endSelectionEnd = -1;
        }
        if (!startText.equals(endText)) {
            final int startColor;
            final int endColor;
            if (mChangeBehavior != android.transition.ChangeText.CHANGE_BEHAVIOR_IN) {
                view.setText(startText);
                if (view instanceof android.widget.EditText) {
                    setSelection(((android.widget.EditText) (view)), startSelectionStart, startSelectionEnd);
                }
            }
            android.animation.Animator anim;
            if (mChangeBehavior == android.transition.ChangeText.CHANGE_BEHAVIOR_KEEP) {
                startColor = endColor = 0;
                anim = android.animation.ValueAnimator.ofFloat(0, 1);
                anim.addListener(new android.animation.AnimatorListenerAdapter() {
                    @java.lang.Override
                    public void onAnimationEnd(android.animation.Animator animation) {
                        if (startText.equals(view.getText())) {
                            // Only set if it hasn't been changed since anim started
                            view.setText(endText);
                            if (view instanceof android.widget.EditText) {
                                setSelection(((android.widget.EditText) (view)), endSelectionStart, endSelectionEnd);
                            }
                        }
                    }
                });
            } else {
                startColor = ((java.lang.Integer) (startVals.get(android.transition.ChangeText.PROPNAME_TEXT_COLOR)));
                endColor = ((java.lang.Integer) (endVals.get(android.transition.ChangeText.PROPNAME_TEXT_COLOR)));
                // Fade out start text
                android.animation.ValueAnimator outAnim = null;
                android.animation.ValueAnimator inAnim = null;
                if ((mChangeBehavior == android.transition.ChangeText.CHANGE_BEHAVIOR_OUT_IN) || (mChangeBehavior == android.transition.ChangeText.CHANGE_BEHAVIOR_OUT)) {
                    outAnim = android.animation.ValueAnimator.ofInt(android.graphics.Color.alpha(startColor), 0);
                    outAnim.addUpdateListener(new android.animation.ValueAnimator.AnimatorUpdateListener() {
                        @java.lang.Override
                        public void onAnimationUpdate(android.animation.ValueAnimator animation) {
                            int currAlpha = ((java.lang.Integer) (animation.getAnimatedValue()));
                            view.setTextColor((currAlpha << 24) | (startColor & 0xffffff));
                        }
                    });
                    outAnim.addListener(new android.animation.AnimatorListenerAdapter() {
                        @java.lang.Override
                        public void onAnimationEnd(android.animation.Animator animation) {
                            if (startText.equals(view.getText())) {
                                // Only set if it hasn't been changed since anim started
                                view.setText(endText);
                                if (view instanceof android.widget.EditText) {
                                    setSelection(((android.widget.EditText) (view)), endSelectionStart, endSelectionEnd);
                                }
                            }
                            // restore opaque alpha and correct end color
                            view.setTextColor(endColor);
                        }
                    });
                }
                if ((mChangeBehavior == android.transition.ChangeText.CHANGE_BEHAVIOR_OUT_IN) || (mChangeBehavior == android.transition.ChangeText.CHANGE_BEHAVIOR_IN)) {
                    inAnim = android.animation.ValueAnimator.ofInt(0, android.graphics.Color.alpha(endColor));
                    inAnim.addUpdateListener(new android.animation.ValueAnimator.AnimatorUpdateListener() {
                        @java.lang.Override
                        public void onAnimationUpdate(android.animation.ValueAnimator animation) {
                            int currAlpha = ((java.lang.Integer) (animation.getAnimatedValue()));
                            view.setTextColor((currAlpha << 24) | (endColor & 0xffffff));
                        }
                    });
                    inAnim.addListener(new android.animation.AnimatorListenerAdapter() {
                        @java.lang.Override
                        public void onAnimationCancel(android.animation.Animator animation) {
                            // restore opaque alpha and correct end color
                            view.setTextColor(endColor);
                        }
                    });
                }
                if ((outAnim != null) && (inAnim != null)) {
                    anim = new android.animation.AnimatorSet();
                    ((android.animation.AnimatorSet) (anim)).playSequentially(outAnim, inAnim);
                } else
                    if (outAnim != null) {
                        anim = outAnim;
                    } else {
                        // Must be an in-only animation
                        anim = inAnim;
                    }

            }
            android.transition.Transition.TransitionListener transitionListener = new android.transition.TransitionListenerAdapter() {
                int mPausedColor = 0;

                @java.lang.Override
                public void onTransitionPause(android.transition.Transition transition) {
                    if (mChangeBehavior != android.transition.ChangeText.CHANGE_BEHAVIOR_IN) {
                        view.setText(endText);
                        if (view instanceof android.widget.EditText) {
                            setSelection(((android.widget.EditText) (view)), endSelectionStart, endSelectionEnd);
                        }
                    }
                    if (mChangeBehavior > android.transition.ChangeText.CHANGE_BEHAVIOR_KEEP) {
                        mPausedColor = view.getCurrentTextColor();
                        view.setTextColor(endColor);
                    }
                }

                @java.lang.Override
                public void onTransitionResume(android.transition.Transition transition) {
                    if (mChangeBehavior != android.transition.ChangeText.CHANGE_BEHAVIOR_IN) {
                        view.setText(startText);
                        if (view instanceof android.widget.EditText) {
                            setSelection(((android.widget.EditText) (view)), startSelectionStart, startSelectionEnd);
                        }
                    }
                    if (mChangeBehavior > android.transition.ChangeText.CHANGE_BEHAVIOR_KEEP) {
                        view.setTextColor(mPausedColor);
                    }
                }

                @java.lang.Override
                public void onTransitionEnd(android.transition.Transition transition) {
                    transition.removeListener(this);
                }
            };
            addListener(transitionListener);
            if (android.transition.Transition.DBG) {
                android.util.Log.d(android.transition.ChangeText.LOG_TAG, "createAnimator returning " + anim);
            }
            return anim;
        }
        return null;
    }

    private void setSelection(android.widget.EditText editText, int start, int end) {
        if ((start >= 0) && (end >= 0)) {
            editText.setSelection(start, end);
        }
    }
}

