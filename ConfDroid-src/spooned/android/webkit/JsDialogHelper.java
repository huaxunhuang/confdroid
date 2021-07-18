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
package android.webkit;


/**
 * Helper class to create JavaScript dialogs. It is used by
 * different WebView implementations.
 *
 * @unknown 
 */
@android.annotation.SystemApi
public class JsDialogHelper {
    private static final java.lang.String TAG = "JsDialogHelper";

    // Dialog types
    public static final int ALERT = 1;

    public static final int CONFIRM = 2;

    public static final int PROMPT = 3;

    public static final int UNLOAD = 4;

    private final java.lang.String mDefaultValue;

    private final android.webkit.JsPromptResult mResult;

    private final java.lang.String mMessage;

    private final int mType;

    private final java.lang.String mUrl;

    public JsDialogHelper(android.webkit.JsPromptResult result, int type, java.lang.String defaultValue, java.lang.String message, java.lang.String url) {
        mResult = result;
        mDefaultValue = defaultValue;
        mMessage = message;
        mType = type;
        mUrl = url;
    }

    public JsDialogHelper(android.webkit.JsPromptResult result, android.os.Message msg) {
        mResult = result;
        mDefaultValue = msg.getData().getString("default");
        mMessage = msg.getData().getString("message");
        mType = msg.getData().getInt("type");
        mUrl = msg.getData().getString("url");
    }

    public boolean invokeCallback(android.webkit.WebChromeClient client, android.webkit.WebView webView) {
        switch (mType) {
            case android.webkit.JsDialogHelper.ALERT :
                return client.onJsAlert(webView, mUrl, mMessage, mResult);
            case android.webkit.JsDialogHelper.CONFIRM :
                return client.onJsConfirm(webView, mUrl, mMessage, mResult);
            case android.webkit.JsDialogHelper.UNLOAD :
                return client.onJsBeforeUnload(webView, mUrl, mMessage, mResult);
            case android.webkit.JsDialogHelper.PROMPT :
                return client.onJsPrompt(webView, mUrl, mMessage, mDefaultValue, mResult);
            default :
                throw new java.lang.IllegalArgumentException("Unexpected type: " + mType);
        }
    }

    public void showDialog(android.content.Context context) {
        if (!android.webkit.JsDialogHelper.canShowAlertDialog(context)) {
            android.util.Log.w(android.webkit.JsDialogHelper.TAG, "Cannot create a dialog, the WebView context is not an Activity");
            mResult.cancel();
            return;
        }
        java.lang.String title;
        java.lang.String displayMessage;
        int positiveTextId;
        int negativeTextId;
        if (mType == android.webkit.JsDialogHelper.UNLOAD) {
            title = context.getString(com.android.internal.R.string.js_dialog_before_unload_title);
            displayMessage = context.getString(com.android.internal.R.string.js_dialog_before_unload, mMessage);
            positiveTextId = com.android.internal.R.string.js_dialog_before_unload_positive_button;
            negativeTextId = com.android.internal.R.string.js_dialog_before_unload_negative_button;
        } else {
            title = getJsDialogTitle(context);
            displayMessage = mMessage;
            positiveTextId = com.android.internal.R.string.ok;
            negativeTextId = com.android.internal.R.string.cancel;
        }
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setOnCancelListener(new android.webkit.JsDialogHelper.CancelListener());
        if (mType != android.webkit.JsDialogHelper.PROMPT) {
            builder.setMessage(displayMessage);
            builder.setPositiveButton(positiveTextId, new android.webkit.JsDialogHelper.PositiveListener(null));
        } else {
            final android.view.View view = android.view.LayoutInflater.from(context).inflate(com.android.internal.R.layout.js_prompt, null);
            android.widget.EditText edit = ((android.widget.EditText) (view.findViewById(com.android.internal.R.id.value)));
            edit.setText(mDefaultValue);
            builder.setPositiveButton(positiveTextId, new android.webkit.JsDialogHelper.PositiveListener(edit));
            ((android.widget.TextView) (view.findViewById(com.android.internal.R.id.message))).setText(mMessage);
            builder.setView(view);
        }
        if (mType != android.webkit.JsDialogHelper.ALERT) {
            builder.setNegativeButton(negativeTextId, new android.webkit.JsDialogHelper.CancelListener());
        }
        builder.show();
    }

    private class CancelListener implements android.content.DialogInterface.OnCancelListener , android.content.DialogInterface.OnClickListener {
        @java.lang.Override
        public void onCancel(android.content.DialogInterface dialog) {
            mResult.cancel();
        }

        @java.lang.Override
        public void onClick(android.content.DialogInterface dialog, int which) {
            mResult.cancel();
        }
    }

    private class PositiveListener implements android.content.DialogInterface.OnClickListener {
        private final android.widget.EditText mEdit;

        public PositiveListener(android.widget.EditText edit) {
            mEdit = edit;
        }

        @java.lang.Override
        public void onClick(android.content.DialogInterface dialog, int which) {
            if (mEdit == null) {
                mResult.confirm();
            } else {
                mResult.confirm(mEdit.getText().toString());
            }
        }
    }

    private java.lang.String getJsDialogTitle(android.content.Context context) {
        java.lang.String title = mUrl;
        if (android.webkit.URLUtil.isDataUrl(mUrl)) {
            // For data: urls, we just display 'JavaScript' similar to Chrome.
            title = context.getString(com.android.internal.R.string.js_dialog_title_default);
        } else {
            try {
                java.net.URL alertUrl = new java.net.URL(mUrl);
                // For example: "The page at 'http://www.mit.edu' says:"
                title = context.getString(com.android.internal.R.string.js_dialog_title, (alertUrl.getProtocol() + "://") + alertUrl.getHost());
            } catch (java.net.MalformedURLException ex) {
                // do nothing. just use the url as the title
            }
        }
        return title;
    }

    private static boolean canShowAlertDialog(android.content.Context context) {
        // We can only display the alert dialog if mContext is
        // an Activity context.
        // FIXME: Should we display dialogs if mContext does
        // not have the window focus (e.g. if the user is viewing
        // another Activity when the alert should be displayed) ?
        // See bug 3166409
        return context instanceof android.app.Activity;
    }
}

