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
package android.widget.focus;


import android.app.ListActivity;
import android.os.Bundle;


public class ListWithMailMessages extends ListActivity {
    @java.lang.Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.list_with_button_above);
        java.util.List<android.widget.focus.ListWithMailMessages.MailMessage> messages = com.google.android.collect.Lists.newArrayList();
        messages.add(new android.widget.focus.ListWithMailMessages.MailMessage("hello!", "<p>this is a test " + "message, with a bunch of text and stuff.</p>", true));
        // String android = "android";
        java.lang.String android = "<a href=\"www.android.com\">android</a>";
        java.lang.String sentance = ("all work and no play makes " + android) + " a dull... robot!";
        java.lang.StringBuffer longBody = new java.lang.StringBuffer().append("<ol>\n");
        for (int i = 0; i < 12; i++) {
            longBody.append("<li>").append(sentance).append("</li>");
        }
        longBody.append("</ol>");
        messages.add(new android.widget.focus.ListWithMailMessages.MailMessage("hello2!", longBody.toString(), true));
        messages.add(new android.widget.focus.ListWithMailMessages.MailMessage("phone number?", "<p>hey man, what's ur " + ("contact info? i need to mail you this photo of my two" + " cats, they've gotten soooo fat!</p>"), true));
        setListAdapter(new android.widget.focus.ListWithMailMessages.MyAdapter(this, R.layout.mail_message, messages));
        getListView().setItemsCanFocus(true);
    }

    /**
     * POJO mail message.
     */
    static class MailMessage {
        private java.lang.String mSubject;

        private java.lang.String mBody;

        private boolean mFocusable;

        public MailMessage(java.lang.String subject, java.lang.String body) {
            this(subject, body, false);
        }

        public MailMessage(java.lang.String subject, java.lang.String body, boolean focusable) {
            mSubject = subject;
            mBody = body;
            mFocusable = focusable;
        }

        public java.lang.String getSubject() {
            return mSubject;
        }

        public void setSubject(java.lang.String subject) {
            this.mSubject = subject;
        }

        public java.lang.String getBody() {
            return mBody;
        }

        public void setBody(java.lang.String body) {
            this.mBody = body;
        }

        public boolean isFocusable() {
            return mFocusable;
        }

        public void setFocusable(boolean focusable) {
            mFocusable = focusable;
        }
    }

    public static class MyAdapter extends android.widget.ArrayAdapter<android.widget.focus.ListWithMailMessages.MailMessage> {
        public MyAdapter(android.content.Context context, int resource, java.util.List<android.widget.focus.ListWithMailMessages.MailMessage> objects) {
            super(context, resource, objects);
        }

        final java.lang.String mimeType = "text/html";

        @java.lang.Override
        public android.view.View getView(int position, android.view.View convertView, android.view.ViewGroup parent) {
            android.widget.focus.ListWithMailMessages.MailMessage message = getItem(position);
            android.view.LayoutInflater inflater = ((android.view.LayoutInflater) (getContext().getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE)));
            android.widget.LinearLayout messageUi = ((android.widget.LinearLayout) (inflater.inflate(R.layout.mail_message, null)));
            android.widget.TextView subject = ((android.widget.TextView) (messageUi.findViewById(R.id.subject)));
            subject.setText(message.getSubject());
            android.webkit.WebView body = ((android.webkit.WebView) (messageUi.findViewById(R.id.body)));
            body.loadData(message.getBody(), mimeType, null);
            // body.setText(message.getBody());
            body.setFocusable(message.isFocusable());
            return messageUi;
        }
    }
}

