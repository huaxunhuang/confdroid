/**
 * Copyright (C) 2008 The Android Open Source Project
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
 * Dialog for choosing accented characters related to a base character.
 */
public class CharacterPickerDialog extends android.app.Dialog implements android.view.View.OnClickListener , android.widget.AdapterView.OnItemClickListener {
    private android.view.View mView;

    private android.text.Editable mText;

    private java.lang.String mOptions;

    private boolean mInsert;

    private android.view.LayoutInflater mInflater;

    private android.widget.Button mCancelButton;

    /**
     * Creates a new CharacterPickerDialog that presents the specified
     * <code>options</code> for insertion or replacement (depending on
     * the sense of <code>insert</code>) into <code>text</code>.
     */
    public CharacterPickerDialog(android.content.Context context, android.view.View view, android.text.Editable text, java.lang.String options, boolean insert) {
        super(context, com.android.internal.R.style.Theme_Panel);
        mView = view;
        mText = text;
        mOptions = options;
        mInsert = insert;
        mInflater = android.view.LayoutInflater.from(context);
    }

    @java.lang.Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.view.WindowManager.LayoutParams params = getWindow().getAttributes();
        params.token = mView.getApplicationWindowToken();
        params.type = params.TYPE_APPLICATION_ATTACHED_DIALOG;
        params.flags = params.flags | android.view.Window.FEATURE_NO_TITLE;
        setContentView(R.layout.character_picker);
        android.widget.GridView grid = ((android.widget.GridView) (findViewById(R.id.characterPicker)));
        grid.setAdapter(new android.text.method.CharacterPickerDialog.OptionsAdapter(getContext()));
        grid.setOnItemClickListener(this);
        mCancelButton = ((android.widget.Button) (findViewById(R.id.cancel)));
        mCancelButton.setOnClickListener(this);
    }

    /**
     * Handles clicks on the character buttons.
     */
    public void onItemClick(android.widget.AdapterView parent, android.view.View view, int position, long id) {
        java.lang.String result = java.lang.String.valueOf(mOptions.charAt(position));
        replaceCharacterAndClose(result);
    }

    private void replaceCharacterAndClose(java.lang.CharSequence replace) {
        int selEnd = android.text.Selection.getSelectionEnd(mText);
        if (mInsert || (selEnd == 0)) {
            mText.insert(selEnd, replace);
        } else {
            mText.replace(selEnd - 1, selEnd, replace);
        }
        dismiss();
    }

    /**
     * Handles clicks on the Cancel button.
     */
    public void onClick(android.view.View v) {
        if (v == mCancelButton) {
            dismiss();
        } else
            if (v instanceof android.widget.Button) {
                java.lang.CharSequence result = ((android.widget.Button) (v)).getText();
                replaceCharacterAndClose(result);
            }

    }

    private class OptionsAdapter extends android.widget.BaseAdapter {
        public OptionsAdapter(android.content.Context context) {
            super();
        }

        public android.view.View getView(int position, android.view.View convertView, android.view.ViewGroup parent) {
            android.widget.Button b = ((android.widget.Button) (mInflater.inflate(R.layout.character_picker_button, null)));
            b.setText(java.lang.String.valueOf(mOptions.charAt(position)));
            b.setOnClickListener(android.text.method.CharacterPickerDialog.this);
            return b;
        }

        public final int getCount() {
            return mOptions.length();
        }

        public final java.lang.Object getItem(int position) {
            return java.lang.String.valueOf(mOptions.charAt(position));
        }

        public final long getItemId(int position) {
            return position;
        }
    }
}

