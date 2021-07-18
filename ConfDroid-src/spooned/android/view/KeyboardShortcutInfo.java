/**
 * Copyright (C) 2015 The Android Open Source Project
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
package android.view;


/**
 * Information about a Keyboard Shortcut.
 */
public final class KeyboardShortcutInfo implements android.os.Parcelable {
    private final java.lang.CharSequence mLabel;

    private final android.graphics.drawable.Icon mIcon;

    private final char mBaseCharacter;

    private final int mKeycode;

    private final int mModifiers;

    /**
     *
     *
     * @param label
     * 		The label that identifies the action performed by this shortcut.
     * @param icon
     * 		An icon that identifies the action performed by this shortcut.
     * @param keycode
     * 		The keycode that triggers the shortcut. This should be a valid constant
     * 		defined in {@link KeyEvent}.
     * @param modifiers
     * 		The set of modifiers that, combined with the key, trigger the shortcut.
     * 		These should be a combination of {@link KeyEvent#META_CTRL_ON},
     * 		{@link KeyEvent#META_SHIFT_ON}, {@link KeyEvent#META_META_ON},
     * 		{@link KeyEvent#META_ALT_ON}, {@link KeyEvent#META_FUNCTION_ON} and
     * 		{@link KeyEvent#META_SYM_ON}.
     * @unknown 
     */
    public KeyboardShortcutInfo(@android.annotation.Nullable
    java.lang.CharSequence label, @android.annotation.Nullable
    android.graphics.drawable.Icon icon, int keycode, int modifiers) {
        mLabel = label;
        mIcon = icon;
        mBaseCharacter = java.lang.Character.MIN_VALUE;
        com.android.internal.util.Preconditions.checkArgument((keycode >= android.view.KeyEvent.KEYCODE_UNKNOWN) && (keycode <= android.view.KeyEvent.getMaxKeyCode()));
        mKeycode = keycode;
        mModifiers = modifiers;
    }

    /**
     *
     *
     * @param label
     * 		The label that identifies the action performed by this shortcut.
     * @param keycode
     * 		The keycode that triggers the shortcut. This should be a valid constant
     * 		defined in {@link KeyEvent}.
     * @param modifiers
     * 		The set of modifiers that, combined with the key, trigger the shortcut.
     * 		These should be a combination of {@link KeyEvent#META_CTRL_ON},
     * 		{@link KeyEvent#META_SHIFT_ON}, {@link KeyEvent#META_META_ON},
     * 		{@link KeyEvent#META_ALT_ON}, {@link KeyEvent#META_FUNCTION_ON} and
     * 		{@link KeyEvent#META_SYM_ON}.
     */
    public KeyboardShortcutInfo(java.lang.CharSequence label, int keycode, int modifiers) {
        this(label, null, keycode, modifiers);
    }

    /**
     *
     *
     * @param label
     * 		The label that identifies the action performed by this shortcut.
     * @param baseCharacter
     * 		The character that triggers the shortcut.
     * @param modifiers
     * 		The set of modifiers that, combined with the key, trigger the shortcut.
     * 		These should be a combination of {@link KeyEvent#META_CTRL_ON},
     * 		{@link KeyEvent#META_SHIFT_ON}, {@link KeyEvent#META_META_ON},
     * 		{@link KeyEvent#META_ALT_ON}, {@link KeyEvent#META_FUNCTION_ON} and
     * 		{@link KeyEvent#META_SYM_ON}.
     */
    public KeyboardShortcutInfo(java.lang.CharSequence label, char baseCharacter, int modifiers) {
        mLabel = label;
        com.android.internal.util.Preconditions.checkArgument(baseCharacter != java.lang.Character.MIN_VALUE);
        mBaseCharacter = baseCharacter;
        mKeycode = android.view.KeyEvent.KEYCODE_UNKNOWN;
        mModifiers = modifiers;
        mIcon = null;
    }

    private KeyboardShortcutInfo(android.os.Parcel source) {
        mLabel = source.readCharSequence();
        mIcon = source.readParcelable(null);
        mBaseCharacter = ((char) (source.readInt()));
        mKeycode = source.readInt();
        mModifiers = source.readInt();
    }

    /**
     * Returns the label to be used to describe this shortcut.
     */
    @android.annotation.Nullable
    public java.lang.CharSequence getLabel() {
        return mLabel;
    }

    /**
     * Returns the icon to be used to describe this shortcut.
     *
     * @unknown 
     */
    @android.annotation.Nullable
    public android.graphics.drawable.Icon getIcon() {
        return mIcon;
    }

    /**
     * Returns the base keycode that, combined with the modifiers, triggers this shortcut. If the
     * base character was set instead, returns {@link KeyEvent#KEYCODE_UNKNOWN}. Valid keycodes are
     * defined as constants in {@link KeyEvent}.
     */
    public int getKeycode() {
        return mKeycode;
    }

    /**
     * Returns the base character that, combined with the modifiers, triggers this shortcut. If the
     * keycode was set instead, returns {@link Character#MIN_VALUE}.
     */
    public char getBaseCharacter() {
        return mBaseCharacter;
    }

    /**
     * Returns the set of modifiers that, combined with the key, trigger this shortcut. These can
     * be a combination of {@link KeyEvent#META_CTRL_ON}, {@link KeyEvent#META_SHIFT_ON},
     * {@link KeyEvent#META_META_ON}, {@link KeyEvent#META_ALT_ON},
     * {@link KeyEvent#META_FUNCTION_ON} and {@link KeyEvent#META_SYM_ON}.
     */
    public int getModifiers() {
        return mModifiers;
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeCharSequence(mLabel);
        dest.writeParcelable(mIcon, 0);
        dest.writeInt(mBaseCharacter);
        dest.writeInt(mKeycode);
        dest.writeInt(mModifiers);
    }

    @android.annotation.NonNull
    public static final android.view.Creator<android.view.KeyboardShortcutInfo> CREATOR = new android.view.Creator<android.view.KeyboardShortcutInfo>() {
        public android.view.KeyboardShortcutInfo createFromParcel(android.os.Parcel source) {
            return new android.view.KeyboardShortcutInfo(source);
        }

        public android.view.KeyboardShortcutInfo[] newArray(int size) {
            return new android.view.KeyboardShortcutInfo[size];
        }
    };
}

