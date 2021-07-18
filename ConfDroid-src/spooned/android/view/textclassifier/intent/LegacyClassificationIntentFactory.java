/**
 * Copyright (C) 2019 The Android Open Source Project
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
package android.view.textclassifier.intent;


/**
 * Creates intents based on the classification type.
 *
 * @unknown 
 */
// TODO: Consider to support {@code descriptionWithAppName}.
public final class LegacyClassificationIntentFactory implements android.view.textclassifier.intent.ClassificationIntentFactory {
    private static final java.lang.String TAG = "LegacyClassificationIntentFactory";

    private static final long MIN_EVENT_FUTURE_MILLIS = java.util.concurrent.TimeUnit.MINUTES.toMillis(5);

    private static final long DEFAULT_EVENT_DURATION = java.util.concurrent.TimeUnit.HOURS.toMillis(1);

    @android.annotation.NonNull
    @java.lang.Override
    public java.util.List<android.view.textclassifier.intent.LabeledIntent> create(android.content.Context context, java.lang.String text, boolean foreignText, @android.annotation.Nullable
    java.time.Instant referenceTime, com.google.android.textclassifier.AnnotatorModel.ClassificationResult classification) {
        final java.lang.String type = (classification != null) ? classification.getCollection().trim().toLowerCase(java.util.Locale.ENGLISH) : "";
        text = text.trim();
        final java.util.List<android.view.textclassifier.intent.LabeledIntent> actions;
        switch (type) {
            case android.view.textclassifier.TextClassifier.TYPE_EMAIL :
                actions = android.view.textclassifier.intent.LegacyClassificationIntentFactory.createForEmail(context, text);
                break;
            case android.view.textclassifier.TextClassifier.TYPE_PHONE :
                actions = android.view.textclassifier.intent.LegacyClassificationIntentFactory.createForPhone(context, text);
                break;
            case android.view.textclassifier.TextClassifier.TYPE_ADDRESS :
                actions = android.view.textclassifier.intent.LegacyClassificationIntentFactory.createForAddress(context, text);
                break;
            case android.view.textclassifier.TextClassifier.TYPE_URL :
                actions = android.view.textclassifier.intent.LegacyClassificationIntentFactory.createForUrl(context, text);
                break;
            case android.view.textclassifier.TextClassifier.TYPE_DATE :
                // fall through
            case android.view.textclassifier.TextClassifier.TYPE_DATE_TIME :
                if (classification.getDatetimeResult() != null) {
                    final java.time.Instant parsedTime = java.time.Instant.ofEpochMilli(classification.getDatetimeResult().getTimeMsUtc());
                    actions = android.view.textclassifier.intent.LegacyClassificationIntentFactory.createForDatetime(context, type, referenceTime, parsedTime);
                } else {
                    actions = new java.util.ArrayList<>();
                }
                break;
            case android.view.textclassifier.TextClassifier.TYPE_FLIGHT_NUMBER :
                actions = android.view.textclassifier.intent.LegacyClassificationIntentFactory.createForFlight(context, text);
                break;
            case android.view.textclassifier.TextClassifier.TYPE_DICTIONARY :
                actions = android.view.textclassifier.intent.LegacyClassificationIntentFactory.createForDictionary(context, text);
                break;
            default :
                actions = new java.util.ArrayList<>();
                break;
        }
        if (foreignText) {
            android.view.textclassifier.intent.ClassificationIntentFactory.insertTranslateAction(actions, context, text);
        }
        return actions;
    }

    @android.annotation.NonNull
    private static java.util.List<android.view.textclassifier.intent.LabeledIntent> createForEmail(android.content.Context context, java.lang.String text) {
        final java.util.List<android.view.textclassifier.intent.LabeledIntent> actions = new java.util.ArrayList<>();
        actions.add(/* titleWithEntity */
        /* descriptionWithAppName */
        new android.view.textclassifier.intent.LabeledIntent(context.getString(com.android.internal.R.string.email), null, context.getString(com.android.internal.R.string.email_desc), null, new android.content.Intent(android.content.Intent.ACTION_SENDTO).setData(android.net.Uri.parse(java.lang.String.format("mailto:%s", text))), android.view.textclassifier.intent.LabeledIntent.DEFAULT_REQUEST_CODE));
        actions.add(/* titleWithEntity */
        /* descriptionWithAppName */
        new android.view.textclassifier.intent.LabeledIntent(context.getString(com.android.internal.R.string.add_contact), null, context.getString(com.android.internal.R.string.add_contact_desc), null, new android.content.Intent(android.content.Intent.ACTION_INSERT_OR_EDIT).setType(ContactsContract.Contacts.CONTENT_ITEM_TYPE).putExtra(ContactsContract.Intents.Insert.EMAIL, text), text.hashCode()));
        return actions;
    }

    @android.annotation.NonNull
    private static java.util.List<android.view.textclassifier.intent.LabeledIntent> createForPhone(android.content.Context context, java.lang.String text) {
        final java.util.List<android.view.textclassifier.intent.LabeledIntent> actions = new java.util.ArrayList<>();
        final android.os.UserManager userManager = context.getSystemService(android.os.UserManager.class);
        final android.os.Bundle userRestrictions = (userManager != null) ? userManager.getUserRestrictions() : new android.os.Bundle();
        if (!userRestrictions.getBoolean(UserManager.DISALLOW_OUTGOING_CALLS, false)) {
            actions.add(/* titleWithEntity */
            /* descriptionWithAppName */
            new android.view.textclassifier.intent.LabeledIntent(context.getString(com.android.internal.R.string.dial), null, context.getString(com.android.internal.R.string.dial_desc), null, new android.content.Intent(android.content.Intent.ACTION_DIAL).setData(android.net.Uri.parse(java.lang.String.format("tel:%s", text))), android.view.textclassifier.intent.LabeledIntent.DEFAULT_REQUEST_CODE));
        }
        actions.add(/* titleWithEntity */
        /* descriptionWithAppName */
        new android.view.textclassifier.intent.LabeledIntent(context.getString(com.android.internal.R.string.add_contact), null, context.getString(com.android.internal.R.string.add_contact_desc), null, new android.content.Intent(android.content.Intent.ACTION_INSERT_OR_EDIT).setType(ContactsContract.Contacts.CONTENT_ITEM_TYPE).putExtra(ContactsContract.Intents.Insert.PHONE, text), text.hashCode()));
        if (!userRestrictions.getBoolean(UserManager.DISALLOW_SMS, false)) {
            actions.add(/* titleWithEntity */
            /* descriptionWithAppName */
            new android.view.textclassifier.intent.LabeledIntent(context.getString(com.android.internal.R.string.sms), null, context.getString(com.android.internal.R.string.sms_desc), null, new android.content.Intent(android.content.Intent.ACTION_SENDTO).setData(android.net.Uri.parse(java.lang.String.format("smsto:%s", text))), android.view.textclassifier.intent.LabeledIntent.DEFAULT_REQUEST_CODE));
        }
        return actions;
    }

    @android.annotation.NonNull
    private static java.util.List<android.view.textclassifier.intent.LabeledIntent> createForAddress(android.content.Context context, java.lang.String text) {
        final java.util.List<android.view.textclassifier.intent.LabeledIntent> actions = new java.util.ArrayList<>();
        try {
            final java.lang.String encText = java.net.URLEncoder.encode(text, "UTF-8");
            actions.add(/* titleWithEntity */
            /* descriptionWithAppName */
            new android.view.textclassifier.intent.LabeledIntent(context.getString(com.android.internal.R.string.map), null, context.getString(com.android.internal.R.string.map_desc), null, new android.content.Intent(android.content.Intent.ACTION_VIEW).setData(android.net.Uri.parse(java.lang.String.format("geo:0,0?q=%s", encText))), android.view.textclassifier.intent.LabeledIntent.DEFAULT_REQUEST_CODE));
        } catch (java.io.UnsupportedEncodingException e) {
            android.view.textclassifier.Log.e(android.view.textclassifier.intent.LegacyClassificationIntentFactory.TAG, "Could not encode address", e);
        }
        return actions;
    }

    @android.annotation.NonNull
    private static java.util.List<android.view.textclassifier.intent.LabeledIntent> createForUrl(android.content.Context context, java.lang.String text) {
        if (android.net.Uri.parse(text).getScheme() == null) {
            text = "http://" + text;
        }
        final java.util.List<android.view.textclassifier.intent.LabeledIntent> actions = new java.util.ArrayList<>();
        actions.add(/* titleWithEntity */
        /* descriptionWithAppName */
        new android.view.textclassifier.intent.LabeledIntent(context.getString(com.android.internal.R.string.browse), null, context.getString(com.android.internal.R.string.browse_desc), null, new android.content.Intent(android.content.Intent.ACTION_VIEW).setDataAndNormalize(android.net.Uri.parse(text)).putExtra(Browser.EXTRA_APPLICATION_ID, context.getPackageName()), android.view.textclassifier.intent.LabeledIntent.DEFAULT_REQUEST_CODE));
        return actions;
    }

    @android.annotation.NonNull
    private static java.util.List<android.view.textclassifier.intent.LabeledIntent> createForDatetime(android.content.Context context, java.lang.String type, @android.annotation.Nullable
    java.time.Instant referenceTime, java.time.Instant parsedTime) {
        if (referenceTime == null) {
            // If no reference time was given, use now.
            referenceTime = java.time.Instant.now();
        }
        java.util.List<android.view.textclassifier.intent.LabeledIntent> actions = new java.util.ArrayList<>();
        actions.add(android.view.textclassifier.intent.LegacyClassificationIntentFactory.createCalendarViewIntent(context, parsedTime));
        final long millisUntilEvent = referenceTime.until(parsedTime, java.time.temporal.ChronoUnit.MILLIS);
        if (millisUntilEvent > android.view.textclassifier.intent.LegacyClassificationIntentFactory.MIN_EVENT_FUTURE_MILLIS) {
            actions.add(android.view.textclassifier.intent.LegacyClassificationIntentFactory.createCalendarCreateEventIntent(context, parsedTime, type));
        }
        return actions;
    }

    @android.annotation.NonNull
    private static java.util.List<android.view.textclassifier.intent.LabeledIntent> createForFlight(android.content.Context context, java.lang.String text) {
        final java.util.List<android.view.textclassifier.intent.LabeledIntent> actions = new java.util.ArrayList<>();
        actions.add(/* titleWithEntity */
        /* descriptionWithAppName */
        new android.view.textclassifier.intent.LabeledIntent(context.getString(com.android.internal.R.string.view_flight), null, context.getString(com.android.internal.R.string.view_flight_desc), null, new android.content.Intent(android.content.Intent.ACTION_WEB_SEARCH).putExtra(SearchManager.QUERY, text), text.hashCode()));
        return actions;
    }

    @android.annotation.NonNull
    private static android.view.textclassifier.intent.LabeledIntent createCalendarViewIntent(android.content.Context context, java.time.Instant parsedTime) {
        android.net.Uri.Builder builder = CalendarContract.CONTENT_URI.buildUpon();
        builder.appendPath("time");
        android.content.ContentUris.appendId(builder, parsedTime.toEpochMilli());
        return /* titleWithEntity */
        /* descriptionWithAppName */
        new android.view.textclassifier.intent.LabeledIntent(context.getString(com.android.internal.R.string.view_calendar), null, context.getString(com.android.internal.R.string.view_calendar_desc), null, new android.content.Intent(android.content.Intent.ACTION_VIEW).setData(builder.build()), android.view.textclassifier.intent.LabeledIntent.DEFAULT_REQUEST_CODE);
    }

    @android.annotation.NonNull
    private static android.view.textclassifier.intent.LabeledIntent createCalendarCreateEventIntent(android.content.Context context, java.time.Instant parsedTime, @android.view.textclassifier.TextClassifier.EntityType
    java.lang.String type) {
        final boolean isAllDay = android.view.textclassifier.TextClassifier.TYPE_DATE.equals(type);
        return /* titleWithEntity */
        /* descriptionWithAppName */
        new android.view.textclassifier.intent.LabeledIntent(context.getString(com.android.internal.R.string.add_calendar_event), null, context.getString(com.android.internal.R.string.add_calendar_event_desc), null, new android.content.Intent(android.content.Intent.ACTION_INSERT).setData(CalendarContract.Events.CONTENT_URI).putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, isAllDay).putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, parsedTime.toEpochMilli()).putExtra(CalendarContract.EXTRA_EVENT_END_TIME, parsedTime.toEpochMilli() + android.view.textclassifier.intent.LegacyClassificationIntentFactory.DEFAULT_EVENT_DURATION), parsedTime.hashCode());
    }

    @android.annotation.NonNull
    private static java.util.List<android.view.textclassifier.intent.LabeledIntent> createForDictionary(android.content.Context context, java.lang.String text) {
        final java.util.List<android.view.textclassifier.intent.LabeledIntent> actions = new java.util.ArrayList<>();
        actions.add(/* titleWithEntity */
        /* descriptionWithAppName */
        new android.view.textclassifier.intent.LabeledIntent(context.getString(com.android.internal.R.string.define), null, context.getString(com.android.internal.R.string.define_desc), null, new android.content.Intent(android.content.Intent.ACTION_DEFINE).putExtra(android.content.Intent.EXTRA_TEXT, text), text.hashCode()));
        return actions;
    }
}

