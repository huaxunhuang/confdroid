/**
 * Copyright (C) 2006 The Android Open Source Project
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
package android.text.method;


/**
 * This base class encapsulates the behavior for tracking the state of
 * meta keys such as SHIFT, ALT and SYM as well as the pseudo-meta state of selecting text.
 * <p>
 * Key listeners that care about meta state should inherit from this class;
 * you should not instantiate this class directly in a client.
 * </p><p>
 * This class provides two mechanisms for tracking meta state that can be used
 * together or independently.
 * </p>
 * <ul>
 * <li>Methods such as {@link #handleKeyDown(long, int, KeyEvent)} and
 * {@link #getMetaState(long)} operate on a meta key state bit mask.</li>
 * <li>Methods such as {@link #onKeyDown(View, Editable, int, KeyEvent)} and
 * {@link #getMetaState(CharSequence, int)} operate on meta key state flags stored
 * as spans in an {@link Editable} text buffer.  The spans only describe the current
 * meta key state of the text editor; they do not carry any positional information.</li>
 * </ul>
 * <p>
 * The behavior of this class varies according to the keyboard capabilities
 * described by the {@link KeyCharacterMap} of the keyboard device such as
 * the {@link KeyCharacterMap#getModifierBehavior() key modifier behavior}.
 * </p><p>
 * {@link MetaKeyKeyListener} implements chorded and toggled key modifiers.
 * When key modifiers are toggled into a latched or locked state, the state
 * of the modifier is stored in the {@link Editable} text buffer or in a
 * meta state integer managed by the client.  These latched or locked modifiers
 * should be considered to be held <b>in addition to</b> those that the
 * keyboard already reported as being pressed in {@link KeyEvent#getMetaState()}.
 * In other words, the {@link MetaKeyKeyListener} augments the meta state
 * provided by the keyboard; it does not replace it.  This distinction is important
 * to ensure that meta keys not handled by {@link MetaKeyKeyListener} such as
 * {@link KeyEvent#KEYCODE_CAPS_LOCK} or {@link KeyEvent#KEYCODE_NUM_LOCK} are
 * taken into consideration.
 * </p><p>
 * To ensure correct meta key behavior, the following pattern should be used
 * when mapping key codes to characters:
 * </p>
 * <code>
 * private char getUnicodeChar(TextKeyListener listener, KeyEvent event, Editable textBuffer) {
 *     // Use the combined meta states from the event and the key listener.
 *     int metaState = event.getMetaState() | listener.getMetaState(textBuffer);
 *     return event.getUnicodeChar(metaState);
 * }
 * </code>
 */
public abstract class MetaKeyKeyListener {
    /**
     * Flag that indicates that the SHIFT key is on.
     * Value equals {@link KeyEvent#META_SHIFT_ON}.
     */
    public static final int META_SHIFT_ON = android.view.KeyEvent.META_SHIFT_ON;

    /**
     * Flag that indicates that the ALT key is on.
     * Value equals {@link KeyEvent#META_ALT_ON}.
     */
    public static final int META_ALT_ON = android.view.KeyEvent.META_ALT_ON;

    /**
     * Flag that indicates that the SYM key is on.
     * Value equals {@link KeyEvent#META_SYM_ON}.
     */
    public static final int META_SYM_ON = android.view.KeyEvent.META_SYM_ON;

    /**
     * Flag that indicates that the SHIFT key is locked in CAPS mode.
     */
    public static final int META_CAP_LOCKED = android.view.KeyEvent.META_CAP_LOCKED;

    /**
     * Flag that indicates that the ALT key is locked.
     */
    public static final int META_ALT_LOCKED = android.view.KeyEvent.META_ALT_LOCKED;

    /**
     * Flag that indicates that the SYM key is locked.
     */
    public static final int META_SYM_LOCKED = android.view.KeyEvent.META_SYM_LOCKED;

    /**
     *
     *
     * @unknown pending API review
     */
    public static final int META_SELECTING = android.view.KeyEvent.META_SELECTING;

    // These bits are privately used by the meta key key listener.
    // They are deliberately assigned values outside of the representable range of an 'int'
    // so as not to conflict with any meta key states publicly defined by KeyEvent.
    private static final long META_CAP_USED = 1L << 32;

    private static final long META_ALT_USED = 1L << 33;

    private static final long META_SYM_USED = 1L << 34;

    private static final long META_CAP_PRESSED = 1L << 40;

    private static final long META_ALT_PRESSED = 1L << 41;

    private static final long META_SYM_PRESSED = 1L << 42;

    private static final long META_CAP_RELEASED = 1L << 48;

    private static final long META_ALT_RELEASED = 1L << 49;

    private static final long META_SYM_RELEASED = 1L << 50;

    private static final long META_SHIFT_MASK = (((android.text.method.MetaKeyKeyListener.META_SHIFT_ON | android.text.method.MetaKeyKeyListener.META_CAP_LOCKED) | android.text.method.MetaKeyKeyListener.META_CAP_USED) | android.text.method.MetaKeyKeyListener.META_CAP_PRESSED) | android.text.method.MetaKeyKeyListener.META_CAP_RELEASED;

    private static final long META_ALT_MASK = (((android.text.method.MetaKeyKeyListener.META_ALT_ON | android.text.method.MetaKeyKeyListener.META_ALT_LOCKED) | android.text.method.MetaKeyKeyListener.META_ALT_USED) | android.text.method.MetaKeyKeyListener.META_ALT_PRESSED) | android.text.method.MetaKeyKeyListener.META_ALT_RELEASED;

    private static final long META_SYM_MASK = (((android.text.method.MetaKeyKeyListener.META_SYM_ON | android.text.method.MetaKeyKeyListener.META_SYM_LOCKED) | android.text.method.MetaKeyKeyListener.META_SYM_USED) | android.text.method.MetaKeyKeyListener.META_SYM_PRESSED) | android.text.method.MetaKeyKeyListener.META_SYM_RELEASED;

    private static final java.lang.Object CAP = new android.text.NoCopySpan.Concrete();

    private static final java.lang.Object ALT = new android.text.NoCopySpan.Concrete();

    private static final java.lang.Object SYM = new android.text.NoCopySpan.Concrete();

    private static final java.lang.Object SELECTING = new android.text.NoCopySpan.Concrete();

    private static final int PRESSED_RETURN_VALUE = 1;

    private static final int LOCKED_RETURN_VALUE = 2;

    /**
     * Resets all meta state to inactive.
     */
    public static void resetMetaState(android.text.Spannable text) {
        text.removeSpan(android.text.method.MetaKeyKeyListener.CAP);
        text.removeSpan(android.text.method.MetaKeyKeyListener.ALT);
        text.removeSpan(android.text.method.MetaKeyKeyListener.SYM);
        text.removeSpan(android.text.method.MetaKeyKeyListener.SELECTING);
    }

    /**
     * Gets the state of the meta keys.
     *
     * @param text
     * 		the buffer in which the meta key would have been pressed.
     * @return an integer in which each bit set to one represents a pressed
    or locked meta key.
     */
    public static final int getMetaState(java.lang.CharSequence text) {
        return ((android.text.method.MetaKeyKeyListener.getActive(text, android.text.method.MetaKeyKeyListener.CAP, android.text.method.MetaKeyKeyListener.META_SHIFT_ON, android.text.method.MetaKeyKeyListener.META_CAP_LOCKED) | android.text.method.MetaKeyKeyListener.getActive(text, android.text.method.MetaKeyKeyListener.ALT, android.text.method.MetaKeyKeyListener.META_ALT_ON, android.text.method.MetaKeyKeyListener.META_ALT_LOCKED)) | android.text.method.MetaKeyKeyListener.getActive(text, android.text.method.MetaKeyKeyListener.SYM, android.text.method.MetaKeyKeyListener.META_SYM_ON, android.text.method.MetaKeyKeyListener.META_SYM_LOCKED)) | android.text.method.MetaKeyKeyListener.getActive(text, android.text.method.MetaKeyKeyListener.SELECTING, android.text.method.MetaKeyKeyListener.META_SELECTING, android.text.method.MetaKeyKeyListener.META_SELECTING);
    }

    /**
     * Gets the state of the meta keys for a specific key event.
     *
     * For input devices that use toggled key modifiers, the `toggled' state
     * is stored into the text buffer. This method retrieves the meta state
     * for this event, accounting for the stored state. If the event has been
     * created by a device that does not support toggled key modifiers, like
     * a virtual device for example, the stored state is ignored.
     *
     * @param text
     * 		the buffer in which the meta key would have been pressed.
     * @param event
     * 		the event for which to evaluate the meta state.
     * @return an integer in which each bit set to one represents a pressed
    or locked meta key.
     */
    public static final int getMetaState(final java.lang.CharSequence text, final android.view.KeyEvent event) {
        int metaState = event.getMetaState();
        if (event.getKeyCharacterMap().getModifierBehavior() == android.view.KeyCharacterMap.MODIFIER_BEHAVIOR_CHORDED_OR_TOGGLED) {
            metaState |= android.text.method.MetaKeyKeyListener.getMetaState(text);
        }
        return metaState;
    }

    // As META_SELECTING is @hide we should not mention it in public comments, hence the
    // omission in @param meta
    /**
     * Gets the state of a particular meta key.
     *
     * @param meta
     * 		META_SHIFT_ON, META_ALT_ON, META_SYM_ON
     * @param text
     * 		the buffer in which the meta key would have been pressed.
     * @return 0 if inactive, 1 if active, 2 if locked.
     */
    public static final int getMetaState(java.lang.CharSequence text, int meta) {
        switch (meta) {
            case android.text.method.MetaKeyKeyListener.META_SHIFT_ON :
                return android.text.method.MetaKeyKeyListener.getActive(text, android.text.method.MetaKeyKeyListener.CAP, android.text.method.MetaKeyKeyListener.PRESSED_RETURN_VALUE, android.text.method.MetaKeyKeyListener.LOCKED_RETURN_VALUE);
            case android.text.method.MetaKeyKeyListener.META_ALT_ON :
                return android.text.method.MetaKeyKeyListener.getActive(text, android.text.method.MetaKeyKeyListener.ALT, android.text.method.MetaKeyKeyListener.PRESSED_RETURN_VALUE, android.text.method.MetaKeyKeyListener.LOCKED_RETURN_VALUE);
            case android.text.method.MetaKeyKeyListener.META_SYM_ON :
                return android.text.method.MetaKeyKeyListener.getActive(text, android.text.method.MetaKeyKeyListener.SYM, android.text.method.MetaKeyKeyListener.PRESSED_RETURN_VALUE, android.text.method.MetaKeyKeyListener.LOCKED_RETURN_VALUE);
            case android.text.method.MetaKeyKeyListener.META_SELECTING :
                return android.text.method.MetaKeyKeyListener.getActive(text, android.text.method.MetaKeyKeyListener.SELECTING, android.text.method.MetaKeyKeyListener.PRESSED_RETURN_VALUE, android.text.method.MetaKeyKeyListener.LOCKED_RETURN_VALUE);
            default :
                return 0;
        }
    }

    /**
     * Gets the state of a particular meta key to use with a particular key event.
     *
     * If the key event has been created by a device that does not support toggled
     * key modifiers, like a virtual keyboard for example, only the meta state in
     * the key event is considered.
     *
     * @param meta
     * 		META_SHIFT_ON, META_ALT_ON, META_SYM_ON
     * @param text
     * 		the buffer in which the meta key would have been pressed.
     * @param event
     * 		the event for which to evaluate the meta state.
     * @return 0 if inactive, 1 if active, 2 if locked.
     */
    public static final int getMetaState(final java.lang.CharSequence text, final int meta, final android.view.KeyEvent event) {
        int metaState = event.getMetaState();
        if (event.getKeyCharacterMap().getModifierBehavior() == android.view.KeyCharacterMap.MODIFIER_BEHAVIOR_CHORDED_OR_TOGGLED) {
            metaState |= android.text.method.MetaKeyKeyListener.getMetaState(text);
        }
        if (android.text.method.MetaKeyKeyListener.META_SELECTING == meta) {
            // #getMetaState(long, int) does not support META_SELECTING, but we want the same
            // behavior as #getMetaState(CharSequence, int) so we need to do it here
            if ((metaState & android.text.method.MetaKeyKeyListener.META_SELECTING) != 0) {
                // META_SELECTING is only ever set to PRESSED and can't be LOCKED, so return 1
                return 1;
            }
            return 0;
        }
        return android.text.method.MetaKeyKeyListener.getMetaState(metaState, meta);
    }

    private static int getActive(java.lang.CharSequence text, java.lang.Object meta, int on, int lock) {
        if (!(text instanceof android.text.Spanned)) {
            return 0;
        }
        android.text.Spanned sp = ((android.text.Spanned) (text));
        int flag = sp.getSpanFlags(meta);
        if (flag == android.text.method.MetaKeyKeyListener.LOCKED) {
            return lock;
        } else
            if (flag != 0) {
                return on;
            } else {
                return 0;
            }

    }

    /**
     * Call this method after you handle a keypress so that the meta
     * state will be reset to unshifted (if it is not still down)
     * or primed to be reset to unshifted (once it is released).
     */
    public static void adjustMetaAfterKeypress(android.text.Spannable content) {
        android.text.method.MetaKeyKeyListener.adjust(content, android.text.method.MetaKeyKeyListener.CAP);
        android.text.method.MetaKeyKeyListener.adjust(content, android.text.method.MetaKeyKeyListener.ALT);
        android.text.method.MetaKeyKeyListener.adjust(content, android.text.method.MetaKeyKeyListener.SYM);
    }

    /**
     * Returns true if this object is one that this class would use to
     * keep track of any meta state in the specified text.
     */
    public static boolean isMetaTracker(java.lang.CharSequence text, java.lang.Object what) {
        return (((what == android.text.method.MetaKeyKeyListener.CAP) || (what == android.text.method.MetaKeyKeyListener.ALT)) || (what == android.text.method.MetaKeyKeyListener.SYM)) || (what == android.text.method.MetaKeyKeyListener.SELECTING);
    }

    /**
     * Returns true if this object is one that this class would use to
     * keep track of the selecting meta state in the specified text.
     */
    public static boolean isSelectingMetaTracker(java.lang.CharSequence text, java.lang.Object what) {
        return what == android.text.method.MetaKeyKeyListener.SELECTING;
    }

    private static void adjust(android.text.Spannable content, java.lang.Object what) {
        int current = content.getSpanFlags(what);
        if (current == android.text.method.MetaKeyKeyListener.PRESSED)
            content.setSpan(what, 0, 0, android.text.method.MetaKeyKeyListener.USED);
        else
            if (current == android.text.method.MetaKeyKeyListener.RELEASED)
                content.removeSpan(what);


    }

    /**
     * Call this if you are a method that ignores the locked meta state
     * (arrow keys, for example) and you handle a key.
     */
    protected static void resetLockedMeta(android.text.Spannable content) {
        android.text.method.MetaKeyKeyListener.resetLock(content, android.text.method.MetaKeyKeyListener.CAP);
        android.text.method.MetaKeyKeyListener.resetLock(content, android.text.method.MetaKeyKeyListener.ALT);
        android.text.method.MetaKeyKeyListener.resetLock(content, android.text.method.MetaKeyKeyListener.SYM);
        android.text.method.MetaKeyKeyListener.resetLock(content, android.text.method.MetaKeyKeyListener.SELECTING);
    }

    private static void resetLock(android.text.Spannable content, java.lang.Object what) {
        int current = content.getSpanFlags(what);
        if (current == android.text.method.MetaKeyKeyListener.LOCKED)
            content.removeSpan(what);

    }

    /**
     * Handles presses of the meta keys.
     */
    public boolean onKeyDown(android.view.View view, android.text.Editable content, int keyCode, android.view.KeyEvent event) {
        if ((keyCode == android.view.KeyEvent.KEYCODE_SHIFT_LEFT) || (keyCode == android.view.KeyEvent.KEYCODE_SHIFT_RIGHT)) {
            press(content, android.text.method.MetaKeyKeyListener.CAP);
            return true;
        }
        if (((keyCode == android.view.KeyEvent.KEYCODE_ALT_LEFT) || (keyCode == android.view.KeyEvent.KEYCODE_ALT_RIGHT)) || (keyCode == android.view.KeyEvent.KEYCODE_NUM)) {
            press(content, android.text.method.MetaKeyKeyListener.ALT);
            return true;
        }
        if (keyCode == android.view.KeyEvent.KEYCODE_SYM) {
            press(content, android.text.method.MetaKeyKeyListener.SYM);
            return true;
        }
        return false;// no super to call through to

    }

    private void press(android.text.Editable content, java.lang.Object what) {
        int state = content.getSpanFlags(what);
        // repeat before use
        if (state == android.text.method.MetaKeyKeyListener.PRESSED);else
            if (state == android.text.method.MetaKeyKeyListener.RELEASED)
                content.setSpan(what, 0, 0, android.text.method.MetaKeyKeyListener.LOCKED);
            else
                // repeat after use
                if (state == android.text.method.MetaKeyKeyListener.USED);else
                    if (state == android.text.method.MetaKeyKeyListener.LOCKED)
                        content.removeSpan(what);
                    else
                        content.setSpan(what, 0, 0, android.text.method.MetaKeyKeyListener.PRESSED);




    }

    /**
     * Start selecting text.
     *
     * @unknown pending API review
     */
    public static void startSelecting(android.view.View view, android.text.Spannable content) {
        content.setSpan(android.text.method.MetaKeyKeyListener.SELECTING, 0, 0, android.text.method.MetaKeyKeyListener.PRESSED);
    }

    /**
     * Stop selecting text.  This does not actually collapse the selection;
     * call {@link android.text.Selection#setSelection} too.
     *
     * @unknown pending API review
     */
    public static void stopSelecting(android.view.View view, android.text.Spannable content) {
        content.removeSpan(android.text.method.MetaKeyKeyListener.SELECTING);
    }

    /**
     * Handles release of the meta keys.
     */
    public boolean onKeyUp(android.view.View view, android.text.Editable content, int keyCode, android.view.KeyEvent event) {
        if ((keyCode == android.view.KeyEvent.KEYCODE_SHIFT_LEFT) || (keyCode == android.view.KeyEvent.KEYCODE_SHIFT_RIGHT)) {
            release(content, android.text.method.MetaKeyKeyListener.CAP, event);
            return true;
        }
        if (((keyCode == android.view.KeyEvent.KEYCODE_ALT_LEFT) || (keyCode == android.view.KeyEvent.KEYCODE_ALT_RIGHT)) || (keyCode == android.view.KeyEvent.KEYCODE_NUM)) {
            release(content, android.text.method.MetaKeyKeyListener.ALT, event);
            return true;
        }
        if (keyCode == android.view.KeyEvent.KEYCODE_SYM) {
            release(content, android.text.method.MetaKeyKeyListener.SYM, event);
            return true;
        }
        return false;// no super to call through to

    }

    private void release(android.text.Editable content, java.lang.Object what, android.view.KeyEvent event) {
        int current = content.getSpanFlags(what);
        switch (event.getKeyCharacterMap().getModifierBehavior()) {
            case android.view.KeyCharacterMap.MODIFIER_BEHAVIOR_CHORDED_OR_TOGGLED :
                if (current == android.text.method.MetaKeyKeyListener.USED)
                    content.removeSpan(what);
                else
                    if (current == android.text.method.MetaKeyKeyListener.PRESSED)
                        content.setSpan(what, 0, 0, android.text.method.MetaKeyKeyListener.RELEASED);


                break;
            default :
                content.removeSpan(what);
                break;
        }
    }

    public void clearMetaKeyState(android.view.View view, android.text.Editable content, int states) {
        android.text.method.MetaKeyKeyListener.clearMetaKeyState(content, states);
    }

    public static void clearMetaKeyState(android.text.Editable content, int states) {
        if ((states & android.text.method.MetaKeyKeyListener.META_SHIFT_ON) != 0)
            content.removeSpan(android.text.method.MetaKeyKeyListener.CAP);

        if ((states & android.text.method.MetaKeyKeyListener.META_ALT_ON) != 0)
            content.removeSpan(android.text.method.MetaKeyKeyListener.ALT);

        if ((states & android.text.method.MetaKeyKeyListener.META_SYM_ON) != 0)
            content.removeSpan(android.text.method.MetaKeyKeyListener.SYM);

        if ((states & android.text.method.MetaKeyKeyListener.META_SELECTING) != 0)
            content.removeSpan(android.text.method.MetaKeyKeyListener.SELECTING);

    }

    /**
     * Call this if you are a method that ignores the locked meta state
     * (arrow keys, for example) and you handle a key.
     */
    public static long resetLockedMeta(long state) {
        if ((state & android.text.method.MetaKeyKeyListener.META_CAP_LOCKED) != 0) {
            state &= ~android.text.method.MetaKeyKeyListener.META_SHIFT_MASK;
        }
        if ((state & android.text.method.MetaKeyKeyListener.META_ALT_LOCKED) != 0) {
            state &= ~android.text.method.MetaKeyKeyListener.META_ALT_MASK;
        }
        if ((state & android.text.method.MetaKeyKeyListener.META_SYM_LOCKED) != 0) {
            state &= ~android.text.method.MetaKeyKeyListener.META_SYM_MASK;
        }
        return state;
    }

    // ---------------------------------------------------------------------
    // Version of API that operates on a state bit mask
    // ---------------------------------------------------------------------
    /**
     * Gets the state of the meta keys.
     *
     * @param state
     * 		the current meta state bits.
     * @return an integer in which each bit set to one represents a pressed
    or locked meta key.
     */
    public static final int getMetaState(long state) {
        int result = 0;
        if ((state & android.text.method.MetaKeyKeyListener.META_CAP_LOCKED) != 0) {
            result |= android.text.method.MetaKeyKeyListener.META_CAP_LOCKED;
        } else
            if ((state & android.text.method.MetaKeyKeyListener.META_SHIFT_ON) != 0) {
                result |= android.text.method.MetaKeyKeyListener.META_SHIFT_ON;
            }

        if ((state & android.text.method.MetaKeyKeyListener.META_ALT_LOCKED) != 0) {
            result |= android.text.method.MetaKeyKeyListener.META_ALT_LOCKED;
        } else
            if ((state & android.text.method.MetaKeyKeyListener.META_ALT_ON) != 0) {
                result |= android.text.method.MetaKeyKeyListener.META_ALT_ON;
            }

        if ((state & android.text.method.MetaKeyKeyListener.META_SYM_LOCKED) != 0) {
            result |= android.text.method.MetaKeyKeyListener.META_SYM_LOCKED;
        } else
            if ((state & android.text.method.MetaKeyKeyListener.META_SYM_ON) != 0) {
                result |= android.text.method.MetaKeyKeyListener.META_SYM_ON;
            }

        return result;
    }

    /**
     * Gets the state of a particular meta key.
     *
     * @param state
     * 		the current state bits.
     * @param meta
     * 		META_SHIFT_ON, META_ALT_ON, or META_SYM_ON
     * @return 0 if inactive, 1 if active, 2 if locked.
     */
    public static final int getMetaState(long state, int meta) {
        switch (meta) {
            case android.text.method.MetaKeyKeyListener.META_SHIFT_ON :
                if ((state & android.text.method.MetaKeyKeyListener.META_CAP_LOCKED) != 0)
                    return android.text.method.MetaKeyKeyListener.LOCKED_RETURN_VALUE;

                if ((state & android.text.method.MetaKeyKeyListener.META_SHIFT_ON) != 0)
                    return android.text.method.MetaKeyKeyListener.PRESSED_RETURN_VALUE;

                return 0;
            case android.text.method.MetaKeyKeyListener.META_ALT_ON :
                if ((state & android.text.method.MetaKeyKeyListener.META_ALT_LOCKED) != 0)
                    return android.text.method.MetaKeyKeyListener.LOCKED_RETURN_VALUE;

                if ((state & android.text.method.MetaKeyKeyListener.META_ALT_ON) != 0)
                    return android.text.method.MetaKeyKeyListener.PRESSED_RETURN_VALUE;

                return 0;
            case android.text.method.MetaKeyKeyListener.META_SYM_ON :
                if ((state & android.text.method.MetaKeyKeyListener.META_SYM_LOCKED) != 0)
                    return android.text.method.MetaKeyKeyListener.LOCKED_RETURN_VALUE;

                if ((state & android.text.method.MetaKeyKeyListener.META_SYM_ON) != 0)
                    return android.text.method.MetaKeyKeyListener.PRESSED_RETURN_VALUE;

                return 0;
            default :
                return 0;
        }
    }

    /**
     * Call this method after you handle a keypress so that the meta
     * state will be reset to unshifted (if it is not still down)
     * or primed to be reset to unshifted (once it is released).  Takes
     * the current state, returns the new state.
     */
    public static long adjustMetaAfterKeypress(long state) {
        if ((state & android.text.method.MetaKeyKeyListener.META_CAP_PRESSED) != 0) {
            state = ((state & (~android.text.method.MetaKeyKeyListener.META_SHIFT_MASK)) | android.text.method.MetaKeyKeyListener.META_SHIFT_ON) | android.text.method.MetaKeyKeyListener.META_CAP_USED;
        } else
            if ((state & android.text.method.MetaKeyKeyListener.META_CAP_RELEASED) != 0) {
                state &= ~android.text.method.MetaKeyKeyListener.META_SHIFT_MASK;
            }

        if ((state & android.text.method.MetaKeyKeyListener.META_ALT_PRESSED) != 0) {
            state = ((state & (~android.text.method.MetaKeyKeyListener.META_ALT_MASK)) | android.text.method.MetaKeyKeyListener.META_ALT_ON) | android.text.method.MetaKeyKeyListener.META_ALT_USED;
        } else
            if ((state & android.text.method.MetaKeyKeyListener.META_ALT_RELEASED) != 0) {
                state &= ~android.text.method.MetaKeyKeyListener.META_ALT_MASK;
            }

        if ((state & android.text.method.MetaKeyKeyListener.META_SYM_PRESSED) != 0) {
            state = ((state & (~android.text.method.MetaKeyKeyListener.META_SYM_MASK)) | android.text.method.MetaKeyKeyListener.META_SYM_ON) | android.text.method.MetaKeyKeyListener.META_SYM_USED;
        } else
            if ((state & android.text.method.MetaKeyKeyListener.META_SYM_RELEASED) != 0) {
                state &= ~android.text.method.MetaKeyKeyListener.META_SYM_MASK;
            }

        return state;
    }

    /**
     * Handles presses of the meta keys.
     */
    public static long handleKeyDown(long state, int keyCode, android.view.KeyEvent event) {
        if ((keyCode == android.view.KeyEvent.KEYCODE_SHIFT_LEFT) || (keyCode == android.view.KeyEvent.KEYCODE_SHIFT_RIGHT)) {
            return android.text.method.MetaKeyKeyListener.press(state, android.text.method.MetaKeyKeyListener.META_SHIFT_ON, android.text.method.MetaKeyKeyListener.META_SHIFT_MASK, android.text.method.MetaKeyKeyListener.META_CAP_LOCKED, android.text.method.MetaKeyKeyListener.META_CAP_PRESSED, android.text.method.MetaKeyKeyListener.META_CAP_RELEASED, android.text.method.MetaKeyKeyListener.META_CAP_USED);
        }
        if (((keyCode == android.view.KeyEvent.KEYCODE_ALT_LEFT) || (keyCode == android.view.KeyEvent.KEYCODE_ALT_RIGHT)) || (keyCode == android.view.KeyEvent.KEYCODE_NUM)) {
            return android.text.method.MetaKeyKeyListener.press(state, android.text.method.MetaKeyKeyListener.META_ALT_ON, android.text.method.MetaKeyKeyListener.META_ALT_MASK, android.text.method.MetaKeyKeyListener.META_ALT_LOCKED, android.text.method.MetaKeyKeyListener.META_ALT_PRESSED, android.text.method.MetaKeyKeyListener.META_ALT_RELEASED, android.text.method.MetaKeyKeyListener.META_ALT_USED);
        }
        if (keyCode == android.view.KeyEvent.KEYCODE_SYM) {
            return android.text.method.MetaKeyKeyListener.press(state, android.text.method.MetaKeyKeyListener.META_SYM_ON, android.text.method.MetaKeyKeyListener.META_SYM_MASK, android.text.method.MetaKeyKeyListener.META_SYM_LOCKED, android.text.method.MetaKeyKeyListener.META_SYM_PRESSED, android.text.method.MetaKeyKeyListener.META_SYM_RELEASED, android.text.method.MetaKeyKeyListener.META_SYM_USED);
        }
        return state;
    }

    private static long press(long state, int what, long mask, long locked, long pressed, long released, long used) {
        if ((state & pressed) != 0) {
            // repeat before use
        } else
            if ((state & released) != 0) {
                state = ((state & (~mask)) | what) | locked;
            } else
                if ((state & used) != 0) {
                    // repeat after use
                } else
                    if ((state & locked) != 0) {
                        state &= ~mask;
                    } else {
                        state |= what | pressed;
                    }



        return state;
    }

    /**
     * Handles release of the meta keys.
     */
    public static long handleKeyUp(long state, int keyCode, android.view.KeyEvent event) {
        if ((keyCode == android.view.KeyEvent.KEYCODE_SHIFT_LEFT) || (keyCode == android.view.KeyEvent.KEYCODE_SHIFT_RIGHT)) {
            return android.text.method.MetaKeyKeyListener.release(state, android.text.method.MetaKeyKeyListener.META_SHIFT_ON, android.text.method.MetaKeyKeyListener.META_SHIFT_MASK, android.text.method.MetaKeyKeyListener.META_CAP_PRESSED, android.text.method.MetaKeyKeyListener.META_CAP_RELEASED, android.text.method.MetaKeyKeyListener.META_CAP_USED, event);
        }
        if (((keyCode == android.view.KeyEvent.KEYCODE_ALT_LEFT) || (keyCode == android.view.KeyEvent.KEYCODE_ALT_RIGHT)) || (keyCode == android.view.KeyEvent.KEYCODE_NUM)) {
            return android.text.method.MetaKeyKeyListener.release(state, android.text.method.MetaKeyKeyListener.META_ALT_ON, android.text.method.MetaKeyKeyListener.META_ALT_MASK, android.text.method.MetaKeyKeyListener.META_ALT_PRESSED, android.text.method.MetaKeyKeyListener.META_ALT_RELEASED, android.text.method.MetaKeyKeyListener.META_ALT_USED, event);
        }
        if (keyCode == android.view.KeyEvent.KEYCODE_SYM) {
            return android.text.method.MetaKeyKeyListener.release(state, android.text.method.MetaKeyKeyListener.META_SYM_ON, android.text.method.MetaKeyKeyListener.META_SYM_MASK, android.text.method.MetaKeyKeyListener.META_SYM_PRESSED, android.text.method.MetaKeyKeyListener.META_SYM_RELEASED, android.text.method.MetaKeyKeyListener.META_SYM_USED, event);
        }
        return state;
    }

    private static long release(long state, int what, long mask, long pressed, long released, long used, android.view.KeyEvent event) {
        switch (event.getKeyCharacterMap().getModifierBehavior()) {
            case android.view.KeyCharacterMap.MODIFIER_BEHAVIOR_CHORDED_OR_TOGGLED :
                if ((state & used) != 0) {
                    state &= ~mask;
                } else
                    if ((state & pressed) != 0) {
                        state |= what | released;
                    }

                break;
            default :
                state &= ~mask;
                break;
        }
        return state;
    }

    /**
     * Clears the state of the specified meta key if it is locked.
     *
     * @param state
     * 		the meta key state
     * @param which
     * 		meta keys to clear, may be a combination of {@link #META_SHIFT_ON},
     * 		{@link #META_ALT_ON} or {@link #META_SYM_ON}.
     */
    public long clearMetaKeyState(long state, int which) {
        if (((which & android.text.method.MetaKeyKeyListener.META_SHIFT_ON) != 0) && ((state & android.text.method.MetaKeyKeyListener.META_CAP_LOCKED) != 0)) {
            state &= ~android.text.method.MetaKeyKeyListener.META_SHIFT_MASK;
        }
        if (((which & android.text.method.MetaKeyKeyListener.META_ALT_ON) != 0) && ((state & android.text.method.MetaKeyKeyListener.META_ALT_LOCKED) != 0)) {
            state &= ~android.text.method.MetaKeyKeyListener.META_ALT_MASK;
        }
        if (((which & android.text.method.MetaKeyKeyListener.META_SYM_ON) != 0) && ((state & android.text.method.MetaKeyKeyListener.META_SYM_LOCKED) != 0)) {
            state &= ~android.text.method.MetaKeyKeyListener.META_SYM_MASK;
        }
        return state;
    }

    /**
     * The meta key has been pressed but has not yet been used.
     */
    private static final int PRESSED = android.text.Spannable.SPAN_MARK_MARK | (1 << android.text.Spannable.SPAN_USER_SHIFT);

    /**
     * The meta key has been pressed and released but has still
     * not yet been used.
     */
    private static final int RELEASED = android.text.Spannable.SPAN_MARK_MARK | (2 << android.text.Spannable.SPAN_USER_SHIFT);

    /**
     * The meta key has been pressed and used but has not yet been released.
     */
    private static final int USED = android.text.Spannable.SPAN_MARK_MARK | (3 << android.text.Spannable.SPAN_USER_SHIFT);

    /**
     * The meta key has been pressed and released without use, and then
     * pressed again; it may also have been released again.
     */
    private static final int LOCKED = android.text.Spannable.SPAN_MARK_MARK | (4 << android.text.Spannable.SPAN_USER_SHIFT);
}

