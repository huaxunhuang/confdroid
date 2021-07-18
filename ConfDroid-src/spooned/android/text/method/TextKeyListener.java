/**
 * Copyright (C) 2007 The Android Open Source Project
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
 * This is the key listener for typing normal text.  It delegates to
 * other key listeners appropriate to the current keyboard and language.
 * <p></p>
 * As for all implementations of {@link KeyListener}, this class is only concerned
 * with hardware keyboards.  Software input methods have no obligation to trigger
 * the methods in this class.
 */
public class TextKeyListener extends android.text.method.BaseKeyListener implements android.text.SpanWatcher {
    private static android.text.method.TextKeyListener[] sInstance = new android.text.method.TextKeyListener[android.text.method.TextKeyListener.Capitalize.values().length * 2];

    /* package */
    static final java.lang.Object ACTIVE = new android.text.NoCopySpan.Concrete();

    /* package */
    static final java.lang.Object CAPPED = new android.text.NoCopySpan.Concrete();

    /* package */
    static final java.lang.Object INHIBIT_REPLACEMENT = new android.text.NoCopySpan.Concrete();

    /* package */
    static final java.lang.Object LAST_TYPED = new android.text.NoCopySpan.Concrete();

    private android.text.method.TextKeyListener.Capitalize mAutoCap;

    private boolean mAutoText;

    private int mPrefs;

    private boolean mPrefsInited;

    /* package */
    static final int AUTO_CAP = 1;

    /* package */
    static final int AUTO_TEXT = 2;

    /* package */
    static final int AUTO_PERIOD = 4;

    /* package */
    static final int SHOW_PASSWORD = 8;

    private java.lang.ref.WeakReference<android.content.ContentResolver> mResolver;

    private android.text.method.TextKeyListener.SettingsObserver mObserver;

    /**
     * Creates a new TextKeyListener with the specified capitalization
     * and correction properties.
     *
     * @param cap
     * 		when, if ever, to automatically capitalize.
     * @param autotext
     * 		whether to automatically do spelling corrections.
     */
    public TextKeyListener(android.text.method.TextKeyListener.Capitalize cap, boolean autotext) {
        mAutoCap = cap;
        mAutoText = autotext;
    }

    /**
     * Returns a new or existing instance with the specified capitalization
     * and correction properties.
     *
     * @param cap
     * 		when, if ever, to automatically capitalize.
     * @param autotext
     * 		whether to automatically do spelling corrections.
     */
    public static android.text.method.TextKeyListener getInstance(boolean autotext, android.text.method.TextKeyListener.Capitalize cap) {
        int off = (cap.ordinal() * 2) + (autotext ? 1 : 0);
        if (android.text.method.TextKeyListener.sInstance[off] == null) {
            android.text.method.TextKeyListener.sInstance[off] = new android.text.method.TextKeyListener(cap, autotext);
        }
        return android.text.method.TextKeyListener.sInstance[off];
    }

    /**
     * Returns a new or existing instance with no automatic capitalization
     * or correction.
     */
    public static android.text.method.TextKeyListener getInstance() {
        return android.text.method.TextKeyListener.getInstance(false, android.text.method.TextKeyListener.Capitalize.NONE);
    }

    /**
     * Returns whether it makes sense to automatically capitalize at the
     * specified position in the specified text, with the specified rules.
     *
     * @param cap
     * 		the capitalization rules to consider.
     * @param cs
     * 		the text in which an insertion is being made.
     * @param off
     * 		the offset into that text where the insertion is being made.
     * @return whether the character being inserted should be capitalized.
     */
    public static boolean shouldCap(android.text.method.TextKeyListener.Capitalize cap, java.lang.CharSequence cs, int off) {
        int i;
        char c;
        if (cap == android.text.method.TextKeyListener.Capitalize.NONE) {
            return false;
        }
        if (cap == android.text.method.TextKeyListener.Capitalize.CHARACTERS) {
            return true;
        }
        return android.text.TextUtils.getCapsMode(cs, off, cap == android.text.method.TextKeyListener.Capitalize.WORDS ? android.text.TextUtils.CAP_MODE_WORDS : android.text.TextUtils.CAP_MODE_SENTENCES) != 0;
    }

    public int getInputType() {
        return android.text.method.BaseKeyListener.makeTextContentType(mAutoCap, mAutoText);
    }

    @java.lang.Override
    public boolean onKeyDown(android.view.View view, android.text.Editable content, int keyCode, android.view.KeyEvent event) {
        android.text.method.KeyListener im = getKeyListener(event);
        return im.onKeyDown(view, content, keyCode, event);
    }

    @java.lang.Override
    public boolean onKeyUp(android.view.View view, android.text.Editable content, int keyCode, android.view.KeyEvent event) {
        android.text.method.KeyListener im = getKeyListener(event);
        return im.onKeyUp(view, content, keyCode, event);
    }

    @java.lang.Override
    public boolean onKeyOther(android.view.View view, android.text.Editable content, android.view.KeyEvent event) {
        android.text.method.KeyListener im = getKeyListener(event);
        return im.onKeyOther(view, content, event);
    }

    /**
     * Clear all the input state (autotext, autocap, multitap, undo)
     * from the specified Editable, going beyond Editable.clear(), which
     * just clears the text but not the input state.
     *
     * @param e
     * 		the buffer whose text and state are to be cleared.
     */
    public static void clear(android.text.Editable e) {
        e.clear();
        e.removeSpan(android.text.method.TextKeyListener.ACTIVE);
        e.removeSpan(android.text.method.TextKeyListener.CAPPED);
        e.removeSpan(android.text.method.TextKeyListener.INHIBIT_REPLACEMENT);
        e.removeSpan(android.text.method.TextKeyListener.LAST_TYPED);
        android.text.method.QwertyKeyListener.Replaced[] repl = e.getSpans(0, e.length(), android.text.method.QwertyKeyListener.Replaced.class);
        final int count = repl.length;
        for (int i = 0; i < count; i++) {
            e.removeSpan(repl[i]);
        }
    }

    public void onSpanAdded(android.text.Spannable s, java.lang.Object what, int start, int end) {
    }

    public void onSpanRemoved(android.text.Spannable s, java.lang.Object what, int start, int end) {
    }

    public void onSpanChanged(android.text.Spannable s, java.lang.Object what, int start, int end, int st, int en) {
        if (what == android.text.Selection.SELECTION_END) {
            s.removeSpan(android.text.method.TextKeyListener.ACTIVE);
        }
    }

    private android.text.method.KeyListener getKeyListener(android.view.KeyEvent event) {
        android.view.KeyCharacterMap kmap = event.getKeyCharacterMap();
        int kind = kmap.getKeyboardType();
        if (kind == android.view.KeyCharacterMap.ALPHA) {
            return android.text.method.QwertyKeyListener.getInstance(mAutoText, mAutoCap);
        } else
            if (kind == android.view.KeyCharacterMap.NUMERIC) {
                return android.text.method.MultiTapKeyListener.getInstance(mAutoText, mAutoCap);
            } else
                if ((kind == android.view.KeyCharacterMap.FULL) || (kind == android.view.KeyCharacterMap.SPECIAL_FUNCTION)) {
                    // We consider special function keyboards full keyboards as a workaround for
                    // devices that do not have built-in keyboards.  Applications may try to inject
                    // key events using the built-in keyboard device id which may be configured as
                    // a special function keyboard using a default key map.  Ideally, as of Honeycomb,
                    // these applications should be modified to use KeyCharacterMap.VIRTUAL_KEYBOARD.
                    return android.text.method.QwertyKeyListener.getInstanceForFullKeyboard();
                }


        return android.text.method.TextKeyListener.NullKeyListener.getInstance();
    }

    public enum Capitalize {

        NONE,
        SENTENCES,
        WORDS,
        CHARACTERS;}

    private static class NullKeyListener implements android.text.method.KeyListener {
        public int getInputType() {
            return android.text.InputType.TYPE_NULL;
        }

        public boolean onKeyDown(android.view.View view, android.text.Editable content, int keyCode, android.view.KeyEvent event) {
            return false;
        }

        public boolean onKeyUp(android.view.View view, android.text.Editable content, int keyCode, android.view.KeyEvent event) {
            return false;
        }

        public boolean onKeyOther(android.view.View view, android.text.Editable content, android.view.KeyEvent event) {
            return false;
        }

        public void clearMetaKeyState(android.view.View view, android.text.Editable content, int states) {
        }

        public static android.text.method.TextKeyListener.NullKeyListener getInstance() {
            if (android.text.method.TextKeyListener.NullKeyListener.sInstance != null)
                return android.text.method.TextKeyListener.NullKeyListener.sInstance;

            android.text.method.TextKeyListener.NullKeyListener.sInstance = new android.text.method.TextKeyListener.NullKeyListener();
            return android.text.method.TextKeyListener.NullKeyListener.sInstance;
        }

        private static android.text.method.TextKeyListener.NullKeyListener sInstance;
    }

    public void release() {
        if (mResolver != null) {
            final android.content.ContentResolver contentResolver = mResolver.get();
            if (contentResolver != null) {
                contentResolver.unregisterContentObserver(mObserver);
                mResolver.clear();
            }
            mObserver = null;
            mResolver = null;
            mPrefsInited = false;
        }
    }

    private void initPrefs(android.content.Context context) {
        final android.content.ContentResolver contentResolver = context.getContentResolver();
        mResolver = new java.lang.ref.WeakReference<android.content.ContentResolver>(contentResolver);
        if (mObserver == null) {
            mObserver = new android.text.method.TextKeyListener.SettingsObserver();
            contentResolver.registerContentObserver(android.provider.Settings.System.CONTENT_URI, true, mObserver);
        }
        updatePrefs(contentResolver);
        mPrefsInited = true;
    }

    private class SettingsObserver extends android.database.ContentObserver {
        public SettingsObserver() {
            super(new android.os.Handler());
        }

        @java.lang.Override
        public void onChange(boolean selfChange) {
            if (mResolver != null) {
                final android.content.ContentResolver contentResolver = mResolver.get();
                if (contentResolver == null) {
                    mPrefsInited = false;
                } else {
                    updatePrefs(contentResolver);
                }
            } else {
                mPrefsInited = false;
            }
        }
    }

    private void updatePrefs(android.content.ContentResolver resolver) {
        boolean cap = android.provider.Settings.System.getInt(resolver, android.provider.Settings.System.TEXT_AUTO_CAPS, 1) > 0;
        boolean text = android.provider.Settings.System.getInt(resolver, android.provider.Settings.System.TEXT_AUTO_REPLACE, 1) > 0;
        boolean period = android.provider.Settings.System.getInt(resolver, android.provider.Settings.System.TEXT_AUTO_PUNCTUATE, 1) > 0;
        boolean pw = android.provider.Settings.System.getInt(resolver, android.provider.Settings.System.TEXT_SHOW_PASSWORD, 1) > 0;
        mPrefs = (((cap ? android.text.method.TextKeyListener.AUTO_CAP : 0) | (text ? android.text.method.TextKeyListener.AUTO_TEXT : 0)) | (period ? android.text.method.TextKeyListener.AUTO_PERIOD : 0)) | (pw ? android.text.method.TextKeyListener.SHOW_PASSWORD : 0);
    }

    /* package */
    int getPrefs(android.content.Context context) {
        synchronized(this) {
            if ((!mPrefsInited) || (mResolver.get() == null)) {
                initPrefs(context);
            }
        }
        return mPrefs;
    }
}

