/**
 * Copyright (c) 2014, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package android.service.notification;


/**
 * Persisted configuration for zen mode.
 *
 * @unknown 
 */
public class ZenModeConfig implements android.os.Parcelable {
    private static java.lang.String TAG = "ZenModeConfig";

    public static final int SOURCE_ANYONE = 0;

    public static final int SOURCE_CONTACT = 1;

    public static final int SOURCE_STAR = 2;

    public static final int MAX_SOURCE = android.service.notification.ZenModeConfig.SOURCE_STAR;

    private static final int DEFAULT_SOURCE = android.service.notification.ZenModeConfig.SOURCE_CONTACT;

    public static final int[] ALL_DAYS = new int[]{ java.util.Calendar.SUNDAY, java.util.Calendar.MONDAY, java.util.Calendar.TUESDAY, java.util.Calendar.WEDNESDAY, java.util.Calendar.THURSDAY, java.util.Calendar.FRIDAY, java.util.Calendar.SATURDAY };

    public static final int[] WEEKNIGHT_DAYS = new int[]{ java.util.Calendar.SUNDAY, java.util.Calendar.MONDAY, java.util.Calendar.TUESDAY, java.util.Calendar.WEDNESDAY, java.util.Calendar.THURSDAY };

    public static final int[] WEEKEND_DAYS = new int[]{ java.util.Calendar.FRIDAY, java.util.Calendar.SATURDAY };

    public static final int[] MINUTE_BUCKETS = android.service.notification.ZenModeConfig.generateMinuteBuckets();

    private static final int SECONDS_MS = 1000;

    private static final int MINUTES_MS = 60 * android.service.notification.ZenModeConfig.SECONDS_MS;

    private static final int DAY_MINUTES = 24 * 60;

    private static final int ZERO_VALUE_MS = 10 * android.service.notification.ZenModeConfig.SECONDS_MS;

    private static final boolean DEFAULT_ALLOW_CALLS = true;

    private static final boolean DEFAULT_ALLOW_MESSAGES = false;

    private static final boolean DEFAULT_ALLOW_REMINDERS = true;

    private static final boolean DEFAULT_ALLOW_EVENTS = true;

    private static final boolean DEFAULT_ALLOW_REPEAT_CALLERS = false;

    private static final boolean DEFAULT_ALLOW_SCREEN_OFF = true;

    private static final boolean DEFAULT_ALLOW_SCREEN_ON = true;

    private static final int XML_VERSION = 2;

    private static final java.lang.String ZEN_TAG = "zen";

    private static final java.lang.String ZEN_ATT_VERSION = "version";

    private static final java.lang.String ZEN_ATT_USER = "user";

    private static final java.lang.String ALLOW_TAG = "allow";

    private static final java.lang.String ALLOW_ATT_CALLS = "calls";

    private static final java.lang.String ALLOW_ATT_REPEAT_CALLERS = "repeatCallers";

    private static final java.lang.String ALLOW_ATT_MESSAGES = "messages";

    private static final java.lang.String ALLOW_ATT_FROM = "from";

    private static final java.lang.String ALLOW_ATT_CALLS_FROM = "callsFrom";

    private static final java.lang.String ALLOW_ATT_MESSAGES_FROM = "messagesFrom";

    private static final java.lang.String ALLOW_ATT_REMINDERS = "reminders";

    private static final java.lang.String ALLOW_ATT_EVENTS = "events";

    private static final java.lang.String ALLOW_ATT_SCREEN_OFF = "visualScreenOff";

    private static final java.lang.String ALLOW_ATT_SCREEN_ON = "visualScreenOn";

    private static final java.lang.String CONDITION_TAG = "condition";

    private static final java.lang.String CONDITION_ATT_COMPONENT = "component";

    private static final java.lang.String CONDITION_ATT_ID = "id";

    private static final java.lang.String CONDITION_ATT_SUMMARY = "summary";

    private static final java.lang.String CONDITION_ATT_LINE1 = "line1";

    private static final java.lang.String CONDITION_ATT_LINE2 = "line2";

    private static final java.lang.String CONDITION_ATT_ICON = "icon";

    private static final java.lang.String CONDITION_ATT_STATE = "state";

    private static final java.lang.String CONDITION_ATT_FLAGS = "flags";

    private static final java.lang.String MANUAL_TAG = "manual";

    private static final java.lang.String AUTOMATIC_TAG = "automatic";

    private static final java.lang.String RULE_ATT_ID = "ruleId";

    private static final java.lang.String RULE_ATT_ENABLED = "enabled";

    private static final java.lang.String RULE_ATT_SNOOZING = "snoozing";

    private static final java.lang.String RULE_ATT_NAME = "name";

    private static final java.lang.String RULE_ATT_COMPONENT = "component";

    private static final java.lang.String RULE_ATT_ZEN = "zen";

    private static final java.lang.String RULE_ATT_CONDITION_ID = "conditionId";

    private static final java.lang.String RULE_ATT_CREATION_TIME = "creationTime";

    private static final java.lang.String RULE_ATT_ENABLER = "enabler";

    public boolean allowCalls = android.service.notification.ZenModeConfig.DEFAULT_ALLOW_CALLS;

    public boolean allowRepeatCallers = android.service.notification.ZenModeConfig.DEFAULT_ALLOW_REPEAT_CALLERS;

    public boolean allowMessages = android.service.notification.ZenModeConfig.DEFAULT_ALLOW_MESSAGES;

    public boolean allowReminders = android.service.notification.ZenModeConfig.DEFAULT_ALLOW_REMINDERS;

    public boolean allowEvents = android.service.notification.ZenModeConfig.DEFAULT_ALLOW_EVENTS;

    public int allowCallsFrom = android.service.notification.ZenModeConfig.DEFAULT_SOURCE;

    public int allowMessagesFrom = android.service.notification.ZenModeConfig.DEFAULT_SOURCE;

    public int user = android.os.UserHandle.USER_SYSTEM;

    public boolean allowWhenScreenOff = android.service.notification.ZenModeConfig.DEFAULT_ALLOW_SCREEN_OFF;

    public boolean allowWhenScreenOn = android.service.notification.ZenModeConfig.DEFAULT_ALLOW_SCREEN_ON;

    public android.service.notification.ZenModeConfig.ZenRule manualRule;

    public android.util.ArrayMap<java.lang.String, android.service.notification.ZenModeConfig.ZenRule> automaticRules = new android.util.ArrayMap<>();

    public ZenModeConfig() {
    }

    public ZenModeConfig(android.os.Parcel source) {
        allowCalls = source.readInt() == 1;
        allowRepeatCallers = source.readInt() == 1;
        allowMessages = source.readInt() == 1;
        allowReminders = source.readInt() == 1;
        allowEvents = source.readInt() == 1;
        allowCallsFrom = source.readInt();
        allowMessagesFrom = source.readInt();
        user = source.readInt();
        manualRule = source.readParcelable(null);
        final int len = source.readInt();
        if (len > 0) {
            final java.lang.String[] ids = new java.lang.String[len];
            final android.service.notification.ZenModeConfig.ZenRule[] rules = new android.service.notification.ZenModeConfig.ZenRule[len];
            source.readStringArray(ids);
            source.readTypedArray(rules, android.service.notification.ZenModeConfig.ZenRule.CREATOR);
            for (int i = 0; i < len; i++) {
                automaticRules.put(ids[i], rules[i]);
            }
        }
        allowWhenScreenOff = source.readInt() == 1;
        allowWhenScreenOn = source.readInt() == 1;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeInt(allowCalls ? 1 : 0);
        dest.writeInt(allowRepeatCallers ? 1 : 0);
        dest.writeInt(allowMessages ? 1 : 0);
        dest.writeInt(allowReminders ? 1 : 0);
        dest.writeInt(allowEvents ? 1 : 0);
        dest.writeInt(allowCallsFrom);
        dest.writeInt(allowMessagesFrom);
        dest.writeInt(user);
        dest.writeParcelable(manualRule, 0);
        if (!automaticRules.isEmpty()) {
            final int len = automaticRules.size();
            final java.lang.String[] ids = new java.lang.String[len];
            final android.service.notification.ZenModeConfig.ZenRule[] rules = new android.service.notification.ZenModeConfig.ZenRule[len];
            for (int i = 0; i < len; i++) {
                ids[i] = automaticRules.keyAt(i);
                rules[i] = automaticRules.valueAt(i);
            }
            dest.writeInt(len);
            dest.writeStringArray(ids);
            dest.writeTypedArray(rules, 0);
        } else {
            dest.writeInt(0);
        }
        dest.writeInt(allowWhenScreenOff ? 1 : 0);
        dest.writeInt(allowWhenScreenOn ? 1 : 0);
    }

    @java.lang.Override
    public java.lang.String toString() {
        return new java.lang.StringBuilder(android.service.notification.ZenModeConfig.class.getSimpleName()).append('[').append("user=").append(user).append(",allowCalls=").append(allowCalls).append(",allowRepeatCallers=").append(allowRepeatCallers).append(",allowMessages=").append(allowMessages).append(",allowCallsFrom=").append(android.service.notification.ZenModeConfig.sourceToString(allowCallsFrom)).append(",allowMessagesFrom=").append(android.service.notification.ZenModeConfig.sourceToString(allowMessagesFrom)).append(",allowReminders=").append(allowReminders).append(",allowEvents=").append(allowEvents).append(",allowWhenScreenOff=").append(allowWhenScreenOff).append(",allowWhenScreenOn=").append(allowWhenScreenOn).append(",automaticRules=").append(automaticRules).append(",manualRule=").append(manualRule).append(']').toString();
    }

    private android.service.notification.ZenModeConfig.Diff diff(android.service.notification.ZenModeConfig to) {
        final android.service.notification.ZenModeConfig.Diff d = new android.service.notification.ZenModeConfig.Diff();
        if (to == null) {
            return d.addLine("config", "delete");
        }
        if (user != to.user) {
            d.addLine("user", user, to.user);
        }
        if (allowCalls != to.allowCalls) {
            d.addLine("allowCalls", allowCalls, to.allowCalls);
        }
        if (allowRepeatCallers != to.allowRepeatCallers) {
            d.addLine("allowRepeatCallers", allowRepeatCallers, to.allowRepeatCallers);
        }
        if (allowMessages != to.allowMessages) {
            d.addLine("allowMessages", allowMessages, to.allowMessages);
        }
        if (allowCallsFrom != to.allowCallsFrom) {
            d.addLine("allowCallsFrom", allowCallsFrom, to.allowCallsFrom);
        }
        if (allowMessagesFrom != to.allowMessagesFrom) {
            d.addLine("allowMessagesFrom", allowMessagesFrom, to.allowMessagesFrom);
        }
        if (allowReminders != to.allowReminders) {
            d.addLine("allowReminders", allowReminders, to.allowReminders);
        }
        if (allowEvents != to.allowEvents) {
            d.addLine("allowEvents", allowEvents, to.allowEvents);
        }
        if (allowWhenScreenOff != to.allowWhenScreenOff) {
            d.addLine("allowWhenScreenOff", allowWhenScreenOff, to.allowWhenScreenOff);
        }
        if (allowWhenScreenOn != to.allowWhenScreenOn) {
            d.addLine("allowWhenScreenOn", allowWhenScreenOn, to.allowWhenScreenOn);
        }
        final android.util.ArraySet<java.lang.String> allRules = new android.util.ArraySet<>();
        android.service.notification.ZenModeConfig.addKeys(allRules, automaticRules);
        android.service.notification.ZenModeConfig.addKeys(allRules, to.automaticRules);
        final int N = allRules.size();
        for (int i = 0; i < N; i++) {
            final java.lang.String rule = allRules.valueAt(i);
            final android.service.notification.ZenModeConfig.ZenRule fromRule = (automaticRules != null) ? automaticRules.get(rule) : null;
            final android.service.notification.ZenModeConfig.ZenRule toRule = (to.automaticRules != null) ? to.automaticRules.get(rule) : null;
            android.service.notification.ZenModeConfig.ZenRule.appendDiff(d, ("automaticRule[" + rule) + "]", fromRule, toRule);
        }
        android.service.notification.ZenModeConfig.ZenRule.appendDiff(d, "manualRule", manualRule, to.manualRule);
        return d;
    }

    public static android.service.notification.ZenModeConfig.Diff diff(android.service.notification.ZenModeConfig from, android.service.notification.ZenModeConfig to) {
        if (from == null) {
            final android.service.notification.ZenModeConfig.Diff d = new android.service.notification.ZenModeConfig.Diff();
            if (to != null) {
                d.addLine("config", "insert");
            }
            return d;
        }
        return from.diff(to);
    }

    private static <T> void addKeys(android.util.ArraySet<T> set, android.util.ArrayMap<T, ?> map) {
        if (map != null) {
            for (int i = 0; i < map.size(); i++) {
                set.add(map.keyAt(i));
            }
        }
    }

    public boolean isValid() {
        if (!android.service.notification.ZenModeConfig.isValidManualRule(manualRule))
            return false;

        final int N = automaticRules.size();
        for (int i = 0; i < N; i++) {
            if (!android.service.notification.ZenModeConfig.isValidAutomaticRule(automaticRules.valueAt(i)))
                return false;

        }
        return true;
    }

    private static boolean isValidManualRule(android.service.notification.ZenModeConfig.ZenRule rule) {
        return (rule == null) || (android.provider.Settings.Global.isValidZenMode(rule.zenMode) && android.service.notification.ZenModeConfig.sameCondition(rule));
    }

    private static boolean isValidAutomaticRule(android.service.notification.ZenModeConfig.ZenRule rule) {
        return ((((rule != null) && (!android.text.TextUtils.isEmpty(rule.name))) && android.provider.Settings.Global.isValidZenMode(rule.zenMode)) && (rule.conditionId != null)) && android.service.notification.ZenModeConfig.sameCondition(rule);
    }

    private static boolean sameCondition(android.service.notification.ZenModeConfig.ZenRule rule) {
        if (rule == null)
            return false;

        if (rule.conditionId == null) {
            return rule.condition == null;
        } else {
            return (rule.condition == null) || rule.conditionId.equals(rule.condition.id);
        }
    }

    private static int[] generateMinuteBuckets() {
        final int maxHrs = 12;
        final int[] buckets = new int[maxHrs + 3];
        buckets[0] = 15;
        buckets[1] = 30;
        buckets[2] = 45;
        for (int i = 1; i <= maxHrs; i++) {
            buckets[2 + i] = 60 * i;
        }
        return buckets;
    }

    public static java.lang.String sourceToString(int source) {
        switch (source) {
            case android.service.notification.ZenModeConfig.SOURCE_ANYONE :
                return "anyone";
            case android.service.notification.ZenModeConfig.SOURCE_CONTACT :
                return "contacts";
            case android.service.notification.ZenModeConfig.SOURCE_STAR :
                return "stars";
            default :
                return "UNKNOWN";
        }
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (!(o instanceof android.service.notification.ZenModeConfig))
            return false;

        if (o == this)
            return true;

        final android.service.notification.ZenModeConfig other = ((android.service.notification.ZenModeConfig) (o));
        return (((((((((((other.allowCalls == allowCalls) && (other.allowRepeatCallers == allowRepeatCallers)) && (other.allowMessages == allowMessages)) && (other.allowCallsFrom == allowCallsFrom)) && (other.allowMessagesFrom == allowMessagesFrom)) && (other.allowReminders == allowReminders)) && (other.allowEvents == allowEvents)) && (other.allowWhenScreenOff == allowWhenScreenOff)) && (other.allowWhenScreenOn == allowWhenScreenOn)) && (other.user == user)) && java.util.Objects.equals(other.automaticRules, automaticRules)) && java.util.Objects.equals(other.manualRule, manualRule);
    }

    @java.lang.Override
    public int hashCode() {
        return java.util.Objects.hash(allowCalls, allowRepeatCallers, allowMessages, allowCallsFrom, allowMessagesFrom, allowReminders, allowEvents, allowWhenScreenOff, allowWhenScreenOn, user, automaticRules, manualRule);
    }

    private static java.lang.String toDayList(int[] days) {
        if ((days == null) || (days.length == 0))
            return "";

        final java.lang.StringBuilder sb = new java.lang.StringBuilder();
        for (int i = 0; i < days.length; i++) {
            if (i > 0)
                sb.append('.');

            sb.append(days[i]);
        }
        return sb.toString();
    }

    private static int[] tryParseDayList(java.lang.String dayList, java.lang.String sep) {
        if (dayList == null)
            return null;

        final java.lang.String[] tokens = dayList.split(sep);
        if (tokens.length == 0)
            return null;

        final int[] rt = new int[tokens.length];
        for (int i = 0; i < tokens.length; i++) {
            final int day = android.service.notification.ZenModeConfig.tryParseInt(tokens[i], -1);
            if (day == (-1))
                return null;

            rt[i] = day;
        }
        return rt;
    }

    private static int tryParseInt(java.lang.String value, int defValue) {
        if (android.text.TextUtils.isEmpty(value))
            return defValue;

        try {
            return java.lang.Integer.parseInt(value);
        } catch (java.lang.NumberFormatException e) {
            return defValue;
        }
    }

    private static long tryParseLong(java.lang.String value, long defValue) {
        if (android.text.TextUtils.isEmpty(value))
            return defValue;

        try {
            return java.lang.Long.valueOf(value);
        } catch (java.lang.NumberFormatException e) {
            return defValue;
        }
    }

    public static android.service.notification.ZenModeConfig readXml(org.xmlpull.v1.XmlPullParser parser, android.service.notification.ZenModeConfig.Migration migration) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        int type = parser.getEventType();
        if (type != org.xmlpull.v1.XmlPullParser.START_TAG)
            return null;

        java.lang.String tag = parser.getName();
        if (!android.service.notification.ZenModeConfig.ZEN_TAG.equals(tag))
            return null;

        final android.service.notification.ZenModeConfig rt = new android.service.notification.ZenModeConfig();
        final int version = android.service.notification.ZenModeConfig.safeInt(parser, android.service.notification.ZenModeConfig.ZEN_ATT_VERSION, android.service.notification.ZenModeConfig.XML_VERSION);
        if (version == 1) {
            final android.service.notification.ZenModeConfig.XmlV1 v1 = android.service.notification.ZenModeConfig.XmlV1.readXml(parser);
            return migration.migrate(v1);
        }
        rt.user = android.service.notification.ZenModeConfig.safeInt(parser, android.service.notification.ZenModeConfig.ZEN_ATT_USER, rt.user);
        while ((type = parser.next()) != org.xmlpull.v1.XmlPullParser.END_DOCUMENT) {
            tag = parser.getName();
            if ((type == org.xmlpull.v1.XmlPullParser.END_TAG) && android.service.notification.ZenModeConfig.ZEN_TAG.equals(tag)) {
                return rt;
            }
            if (type == org.xmlpull.v1.XmlPullParser.START_TAG) {
                if (android.service.notification.ZenModeConfig.ALLOW_TAG.equals(tag)) {
                    rt.allowCalls = android.service.notification.ZenModeConfig.safeBoolean(parser, android.service.notification.ZenModeConfig.ALLOW_ATT_CALLS, false);
                    rt.allowRepeatCallers = android.service.notification.ZenModeConfig.safeBoolean(parser, android.service.notification.ZenModeConfig.ALLOW_ATT_REPEAT_CALLERS, android.service.notification.ZenModeConfig.DEFAULT_ALLOW_REPEAT_CALLERS);
                    rt.allowMessages = android.service.notification.ZenModeConfig.safeBoolean(parser, android.service.notification.ZenModeConfig.ALLOW_ATT_MESSAGES, false);
                    rt.allowReminders = android.service.notification.ZenModeConfig.safeBoolean(parser, android.service.notification.ZenModeConfig.ALLOW_ATT_REMINDERS, android.service.notification.ZenModeConfig.DEFAULT_ALLOW_REMINDERS);
                    rt.allowEvents = android.service.notification.ZenModeConfig.safeBoolean(parser, android.service.notification.ZenModeConfig.ALLOW_ATT_EVENTS, android.service.notification.ZenModeConfig.DEFAULT_ALLOW_EVENTS);
                    final int from = android.service.notification.ZenModeConfig.safeInt(parser, android.service.notification.ZenModeConfig.ALLOW_ATT_FROM, -1);
                    final int callsFrom = android.service.notification.ZenModeConfig.safeInt(parser, android.service.notification.ZenModeConfig.ALLOW_ATT_CALLS_FROM, -1);
                    final int messagesFrom = android.service.notification.ZenModeConfig.safeInt(parser, android.service.notification.ZenModeConfig.ALLOW_ATT_MESSAGES_FROM, -1);
                    if (android.service.notification.ZenModeConfig.isValidSource(callsFrom) && android.service.notification.ZenModeConfig.isValidSource(messagesFrom)) {
                        rt.allowCallsFrom = callsFrom;
                        rt.allowMessagesFrom = messagesFrom;
                    } else
                        if (android.service.notification.ZenModeConfig.isValidSource(from)) {
                            android.util.Slog.i(android.service.notification.ZenModeConfig.TAG, "Migrating existing shared 'from': " + android.service.notification.ZenModeConfig.sourceToString(from));
                            rt.allowCallsFrom = from;
                            rt.allowMessagesFrom = from;
                        } else {
                            rt.allowCallsFrom = android.service.notification.ZenModeConfig.DEFAULT_SOURCE;
                            rt.allowMessagesFrom = android.service.notification.ZenModeConfig.DEFAULT_SOURCE;
                        }

                    rt.allowWhenScreenOff = android.service.notification.ZenModeConfig.safeBoolean(parser, android.service.notification.ZenModeConfig.ALLOW_ATT_SCREEN_OFF, android.service.notification.ZenModeConfig.DEFAULT_ALLOW_SCREEN_OFF);
                    rt.allowWhenScreenOn = android.service.notification.ZenModeConfig.safeBoolean(parser, android.service.notification.ZenModeConfig.ALLOW_ATT_SCREEN_ON, android.service.notification.ZenModeConfig.DEFAULT_ALLOW_SCREEN_ON);
                } else
                    if (android.service.notification.ZenModeConfig.MANUAL_TAG.equals(tag)) {
                        rt.manualRule = android.service.notification.ZenModeConfig.readRuleXml(parser);
                    } else
                        if (android.service.notification.ZenModeConfig.AUTOMATIC_TAG.equals(tag)) {
                            final java.lang.String id = parser.getAttributeValue(null, android.service.notification.ZenModeConfig.RULE_ATT_ID);
                            final android.service.notification.ZenModeConfig.ZenRule automaticRule = android.service.notification.ZenModeConfig.readRuleXml(parser);
                            if ((id != null) && (automaticRule != null)) {
                                automaticRule.id = id;
                                rt.automaticRules.put(id, automaticRule);
                            }
                        }


            }
        } 
        throw new java.lang.IllegalStateException("Failed to reach END_DOCUMENT");
    }

    public void writeXml(org.xmlpull.v1.XmlSerializer out) throws java.io.IOException {
        out.startTag(null, android.service.notification.ZenModeConfig.ZEN_TAG);
        out.attribute(null, android.service.notification.ZenModeConfig.ZEN_ATT_VERSION, java.lang.Integer.toString(android.service.notification.ZenModeConfig.XML_VERSION));
        out.attribute(null, android.service.notification.ZenModeConfig.ZEN_ATT_USER, java.lang.Integer.toString(user));
        out.startTag(null, android.service.notification.ZenModeConfig.ALLOW_TAG);
        out.attribute(null, android.service.notification.ZenModeConfig.ALLOW_ATT_CALLS, java.lang.Boolean.toString(allowCalls));
        out.attribute(null, android.service.notification.ZenModeConfig.ALLOW_ATT_REPEAT_CALLERS, java.lang.Boolean.toString(allowRepeatCallers));
        out.attribute(null, android.service.notification.ZenModeConfig.ALLOW_ATT_MESSAGES, java.lang.Boolean.toString(allowMessages));
        out.attribute(null, android.service.notification.ZenModeConfig.ALLOW_ATT_REMINDERS, java.lang.Boolean.toString(allowReminders));
        out.attribute(null, android.service.notification.ZenModeConfig.ALLOW_ATT_EVENTS, java.lang.Boolean.toString(allowEvents));
        out.attribute(null, android.service.notification.ZenModeConfig.ALLOW_ATT_CALLS_FROM, java.lang.Integer.toString(allowCallsFrom));
        out.attribute(null, android.service.notification.ZenModeConfig.ALLOW_ATT_MESSAGES_FROM, java.lang.Integer.toString(allowMessagesFrom));
        out.attribute(null, android.service.notification.ZenModeConfig.ALLOW_ATT_SCREEN_OFF, java.lang.Boolean.toString(allowWhenScreenOff));
        out.attribute(null, android.service.notification.ZenModeConfig.ALLOW_ATT_SCREEN_ON, java.lang.Boolean.toString(allowWhenScreenOn));
        out.endTag(null, android.service.notification.ZenModeConfig.ALLOW_TAG);
        if (manualRule != null) {
            out.startTag(null, android.service.notification.ZenModeConfig.MANUAL_TAG);
            android.service.notification.ZenModeConfig.writeRuleXml(manualRule, out);
            out.endTag(null, android.service.notification.ZenModeConfig.MANUAL_TAG);
        }
        final int N = automaticRules.size();
        for (int i = 0; i < N; i++) {
            final java.lang.String id = automaticRules.keyAt(i);
            final android.service.notification.ZenModeConfig.ZenRule automaticRule = automaticRules.valueAt(i);
            out.startTag(null, android.service.notification.ZenModeConfig.AUTOMATIC_TAG);
            out.attribute(null, android.service.notification.ZenModeConfig.RULE_ATT_ID, id);
            android.service.notification.ZenModeConfig.writeRuleXml(automaticRule, out);
            out.endTag(null, android.service.notification.ZenModeConfig.AUTOMATIC_TAG);
        }
        out.endTag(null, android.service.notification.ZenModeConfig.ZEN_TAG);
    }

    public static android.service.notification.ZenModeConfig.ZenRule readRuleXml(org.xmlpull.v1.XmlPullParser parser) {
        final android.service.notification.ZenModeConfig.ZenRule rt = new android.service.notification.ZenModeConfig.ZenRule();
        rt.enabled = android.service.notification.ZenModeConfig.safeBoolean(parser, android.service.notification.ZenModeConfig.RULE_ATT_ENABLED, true);
        rt.snoozing = android.service.notification.ZenModeConfig.safeBoolean(parser, android.service.notification.ZenModeConfig.RULE_ATT_SNOOZING, false);
        rt.name = parser.getAttributeValue(null, android.service.notification.ZenModeConfig.RULE_ATT_NAME);
        final java.lang.String zen = parser.getAttributeValue(null, android.service.notification.ZenModeConfig.RULE_ATT_ZEN);
        rt.zenMode = android.service.notification.ZenModeConfig.tryParseZenMode(zen, -1);
        if (rt.zenMode == (-1)) {
            android.util.Slog.w(android.service.notification.ZenModeConfig.TAG, "Bad zen mode in rule xml:" + zen);
            return null;
        }
        rt.conditionId = android.service.notification.ZenModeConfig.safeUri(parser, android.service.notification.ZenModeConfig.RULE_ATT_CONDITION_ID);
        rt.component = android.service.notification.ZenModeConfig.safeComponentName(parser, android.service.notification.ZenModeConfig.RULE_ATT_COMPONENT);
        rt.creationTime = android.service.notification.ZenModeConfig.safeLong(parser, android.service.notification.ZenModeConfig.RULE_ATT_CREATION_TIME, 0);
        rt.enabler = parser.getAttributeValue(null, android.service.notification.ZenModeConfig.RULE_ATT_ENABLER);
        rt.condition = android.service.notification.ZenModeConfig.readConditionXml(parser);
        return rt;
    }

    public static void writeRuleXml(android.service.notification.ZenModeConfig.ZenRule rule, org.xmlpull.v1.XmlSerializer out) throws java.io.IOException {
        out.attribute(null, android.service.notification.ZenModeConfig.RULE_ATT_ENABLED, java.lang.Boolean.toString(rule.enabled));
        out.attribute(null, android.service.notification.ZenModeConfig.RULE_ATT_SNOOZING, java.lang.Boolean.toString(rule.snoozing));
        if (rule.name != null) {
            out.attribute(null, android.service.notification.ZenModeConfig.RULE_ATT_NAME, rule.name);
        }
        out.attribute(null, android.service.notification.ZenModeConfig.RULE_ATT_ZEN, java.lang.Integer.toString(rule.zenMode));
        if (rule.component != null) {
            out.attribute(null, android.service.notification.ZenModeConfig.RULE_ATT_COMPONENT, rule.component.flattenToString());
        }
        if (rule.conditionId != null) {
            out.attribute(null, android.service.notification.ZenModeConfig.RULE_ATT_CONDITION_ID, rule.conditionId.toString());
        }
        out.attribute(null, android.service.notification.ZenModeConfig.RULE_ATT_CREATION_TIME, java.lang.Long.toString(rule.creationTime));
        if (rule.enabler != null) {
            out.attribute(null, android.service.notification.ZenModeConfig.RULE_ATT_ENABLER, rule.enabler);
        }
        if (rule.condition != null) {
            android.service.notification.ZenModeConfig.writeConditionXml(rule.condition, out);
        }
    }

    public static android.service.notification.Condition readConditionXml(org.xmlpull.v1.XmlPullParser parser) {
        final android.net.Uri id = android.service.notification.ZenModeConfig.safeUri(parser, android.service.notification.ZenModeConfig.CONDITION_ATT_ID);
        if (id == null)
            return null;

        final java.lang.String summary = parser.getAttributeValue(null, android.service.notification.ZenModeConfig.CONDITION_ATT_SUMMARY);
        final java.lang.String line1 = parser.getAttributeValue(null, android.service.notification.ZenModeConfig.CONDITION_ATT_LINE1);
        final java.lang.String line2 = parser.getAttributeValue(null, android.service.notification.ZenModeConfig.CONDITION_ATT_LINE2);
        final int icon = android.service.notification.ZenModeConfig.safeInt(parser, android.service.notification.ZenModeConfig.CONDITION_ATT_ICON, -1);
        final int state = android.service.notification.ZenModeConfig.safeInt(parser, android.service.notification.ZenModeConfig.CONDITION_ATT_STATE, -1);
        final int flags = android.service.notification.ZenModeConfig.safeInt(parser, android.service.notification.ZenModeConfig.CONDITION_ATT_FLAGS, -1);
        try {
            return new android.service.notification.Condition(id, summary, line1, line2, icon, state, flags);
        } catch (java.lang.IllegalArgumentException e) {
            android.util.Slog.w(android.service.notification.ZenModeConfig.TAG, "Unable to read condition xml", e);
            return null;
        }
    }

    public static void writeConditionXml(android.service.notification.Condition c, org.xmlpull.v1.XmlSerializer out) throws java.io.IOException {
        out.attribute(null, android.service.notification.ZenModeConfig.CONDITION_ATT_ID, c.id.toString());
        out.attribute(null, android.service.notification.ZenModeConfig.CONDITION_ATT_SUMMARY, c.summary);
        out.attribute(null, android.service.notification.ZenModeConfig.CONDITION_ATT_LINE1, c.line1);
        out.attribute(null, android.service.notification.ZenModeConfig.CONDITION_ATT_LINE2, c.line2);
        out.attribute(null, android.service.notification.ZenModeConfig.CONDITION_ATT_ICON, java.lang.Integer.toString(c.icon));
        out.attribute(null, android.service.notification.ZenModeConfig.CONDITION_ATT_STATE, java.lang.Integer.toString(c.state));
        out.attribute(null, android.service.notification.ZenModeConfig.CONDITION_ATT_FLAGS, java.lang.Integer.toString(c.flags));
    }

    public static boolean isValidHour(int val) {
        return (val >= 0) && (val < 24);
    }

    public static boolean isValidMinute(int val) {
        return (val >= 0) && (val < 60);
    }

    private static boolean isValidSource(int source) {
        return (source >= android.service.notification.ZenModeConfig.SOURCE_ANYONE) && (source <= android.service.notification.ZenModeConfig.MAX_SOURCE);
    }

    private static boolean safeBoolean(org.xmlpull.v1.XmlPullParser parser, java.lang.String att, boolean defValue) {
        final java.lang.String val = parser.getAttributeValue(null, att);
        return android.service.notification.ZenModeConfig.safeBoolean(val, defValue);
    }

    private static boolean safeBoolean(java.lang.String val, boolean defValue) {
        if (android.text.TextUtils.isEmpty(val))
            return defValue;

        return java.lang.Boolean.valueOf(val);
    }

    private static int safeInt(org.xmlpull.v1.XmlPullParser parser, java.lang.String att, int defValue) {
        final java.lang.String val = parser.getAttributeValue(null, att);
        return android.service.notification.ZenModeConfig.tryParseInt(val, defValue);
    }

    private static android.content.ComponentName safeComponentName(org.xmlpull.v1.XmlPullParser parser, java.lang.String att) {
        final java.lang.String val = parser.getAttributeValue(null, att);
        if (android.text.TextUtils.isEmpty(val))
            return null;

        return android.content.ComponentName.unflattenFromString(val);
    }

    private static android.net.Uri safeUri(org.xmlpull.v1.XmlPullParser parser, java.lang.String att) {
        final java.lang.String val = parser.getAttributeValue(null, att);
        if (android.text.TextUtils.isEmpty(val))
            return null;

        return android.net.Uri.parse(val);
    }

    private static long safeLong(org.xmlpull.v1.XmlPullParser parser, java.lang.String att, long defValue) {
        final java.lang.String val = parser.getAttributeValue(null, att);
        return android.service.notification.ZenModeConfig.tryParseLong(val, defValue);
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    public android.service.notification.ZenModeConfig copy() {
        final android.os.Parcel parcel = android.os.Parcel.obtain();
        try {
            writeToParcel(parcel, 0);
            parcel.setDataPosition(0);
            return new android.service.notification.ZenModeConfig(parcel);
        } finally {
            parcel.recycle();
        }
    }

    public static final android.os.Parcelable.Creator<android.service.notification.ZenModeConfig> CREATOR = new android.os.Parcelable.Creator<android.service.notification.ZenModeConfig>() {
        @java.lang.Override
        public android.service.notification.ZenModeConfig createFromParcel(android.os.Parcel source) {
            return new android.service.notification.ZenModeConfig(source);
        }

        @java.lang.Override
        public android.service.notification.ZenModeConfig[] newArray(int size) {
            return new android.service.notification.ZenModeConfig[size];
        }
    };

    public android.app.NotificationManager.Policy toNotificationPolicy() {
        int priorityCategories = 0;
        int priorityCallSenders = android.app.NotificationManager.Policy.PRIORITY_SENDERS_CONTACTS;
        int priorityMessageSenders = android.app.NotificationManager.Policy.PRIORITY_SENDERS_CONTACTS;
        if (allowCalls) {
            priorityCategories |= android.app.NotificationManager.Policy.PRIORITY_CATEGORY_CALLS;
        }
        if (allowMessages) {
            priorityCategories |= android.app.NotificationManager.Policy.PRIORITY_CATEGORY_MESSAGES;
        }
        if (allowEvents) {
            priorityCategories |= android.app.NotificationManager.Policy.PRIORITY_CATEGORY_EVENTS;
        }
        if (allowReminders) {
            priorityCategories |= android.app.NotificationManager.Policy.PRIORITY_CATEGORY_REMINDERS;
        }
        if (allowRepeatCallers) {
            priorityCategories |= android.app.NotificationManager.Policy.PRIORITY_CATEGORY_REPEAT_CALLERS;
        }
        int suppressedVisualEffects = 0;
        if (!allowWhenScreenOff) {
            suppressedVisualEffects |= android.app.NotificationManager.Policy.SUPPRESSED_EFFECT_SCREEN_OFF;
        }
        if (!allowWhenScreenOn) {
            suppressedVisualEffects |= android.app.NotificationManager.Policy.SUPPRESSED_EFFECT_SCREEN_ON;
        }
        priorityCallSenders = android.service.notification.ZenModeConfig.sourceToPrioritySenders(allowCallsFrom, priorityCallSenders);
        priorityMessageSenders = android.service.notification.ZenModeConfig.sourceToPrioritySenders(allowMessagesFrom, priorityMessageSenders);
        return new android.app.NotificationManager.Policy(priorityCategories, priorityCallSenders, priorityMessageSenders, suppressedVisualEffects);
    }

    private static int sourceToPrioritySenders(int source, int def) {
        switch (source) {
            case android.service.notification.ZenModeConfig.SOURCE_ANYONE :
                return android.app.NotificationManager.Policy.PRIORITY_SENDERS_ANY;
            case android.service.notification.ZenModeConfig.SOURCE_CONTACT :
                return android.app.NotificationManager.Policy.PRIORITY_SENDERS_CONTACTS;
            case android.service.notification.ZenModeConfig.SOURCE_STAR :
                return android.app.NotificationManager.Policy.PRIORITY_SENDERS_STARRED;
            default :
                return def;
        }
    }

    private static int prioritySendersToSource(int prioritySenders, int def) {
        switch (prioritySenders) {
            case android.app.NotificationManager.Policy.PRIORITY_SENDERS_CONTACTS :
                return android.service.notification.ZenModeConfig.SOURCE_CONTACT;
            case android.app.NotificationManager.Policy.PRIORITY_SENDERS_STARRED :
                return android.service.notification.ZenModeConfig.SOURCE_STAR;
            case android.app.NotificationManager.Policy.PRIORITY_SENDERS_ANY :
                return android.service.notification.ZenModeConfig.SOURCE_ANYONE;
            default :
                return def;
        }
    }

    public void applyNotificationPolicy(android.app.NotificationManager.Policy policy) {
        if (policy == null)
            return;

        allowCalls = (policy.priorityCategories & android.app.NotificationManager.Policy.PRIORITY_CATEGORY_CALLS) != 0;
        allowMessages = (policy.priorityCategories & android.app.NotificationManager.Policy.PRIORITY_CATEGORY_MESSAGES) != 0;
        allowEvents = (policy.priorityCategories & android.app.NotificationManager.Policy.PRIORITY_CATEGORY_EVENTS) != 0;
        allowReminders = (policy.priorityCategories & android.app.NotificationManager.Policy.PRIORITY_CATEGORY_REMINDERS) != 0;
        allowRepeatCallers = (policy.priorityCategories & android.app.NotificationManager.Policy.PRIORITY_CATEGORY_REPEAT_CALLERS) != 0;
        allowCallsFrom = android.service.notification.ZenModeConfig.prioritySendersToSource(policy.priorityCallSenders, allowCallsFrom);
        allowMessagesFrom = android.service.notification.ZenModeConfig.prioritySendersToSource(policy.priorityMessageSenders, allowMessagesFrom);
        if (policy.suppressedVisualEffects != android.app.NotificationManager.Policy.SUPPRESSED_EFFECTS_UNSET) {
            allowWhenScreenOff = (policy.suppressedVisualEffects & android.app.NotificationManager.Policy.SUPPRESSED_EFFECT_SCREEN_OFF) == 0;
            allowWhenScreenOn = (policy.suppressedVisualEffects & android.app.NotificationManager.Policy.SUPPRESSED_EFFECT_SCREEN_ON) == 0;
        }
    }

    public static android.service.notification.Condition toTimeCondition(android.content.Context context, int minutesFromNow, int userHandle) {
        return /* shortVersion */
        android.service.notification.ZenModeConfig.toTimeCondition(context, minutesFromNow, userHandle, false);
    }

    public static android.service.notification.Condition toTimeCondition(android.content.Context context, int minutesFromNow, int userHandle, boolean shortVersion) {
        final long now = java.lang.System.currentTimeMillis();
        final long millis = (minutesFromNow == 0) ? android.service.notification.ZenModeConfig.ZERO_VALUE_MS : minutesFromNow * android.service.notification.ZenModeConfig.MINUTES_MS;
        return android.service.notification.ZenModeConfig.toTimeCondition(context, now + millis, minutesFromNow, userHandle, shortVersion);
    }

    public static android.service.notification.Condition toTimeCondition(android.content.Context context, long time, int minutes, int userHandle, boolean shortVersion) {
        final int num;
        java.lang.String summary;
        java.lang.String line1;
        java.lang.String line2;
        final java.lang.CharSequence formattedTime = android.service.notification.ZenModeConfig.getFormattedTime(context, time, userHandle);
        final android.content.res.Resources res = context.getResources();
        if (minutes < 60) {
            // display as minutes
            num = minutes;
            int summaryResId = (shortVersion) ? R.plurals.zen_mode_duration_minutes_summary_short : R.plurals.zen_mode_duration_minutes_summary;
            summary = res.getQuantityString(summaryResId, num, num, formattedTime);
            int line1ResId = (shortVersion) ? R.plurals.zen_mode_duration_minutes_short : R.plurals.zen_mode_duration_minutes;
            line1 = res.getQuantityString(line1ResId, num, num, formattedTime);
            line2 = res.getString(R.string.zen_mode_until, formattedTime);
        } else
            if (minutes < android.service.notification.ZenModeConfig.DAY_MINUTES) {
                // display as hours
                num = java.lang.Math.round(minutes / 60.0F);
                int summaryResId = (shortVersion) ? R.plurals.zen_mode_duration_hours_summary_short : R.plurals.zen_mode_duration_hours_summary;
                summary = res.getQuantityString(summaryResId, num, num, formattedTime);
                int line1ResId = (shortVersion) ? R.plurals.zen_mode_duration_hours_short : R.plurals.zen_mode_duration_hours;
                line1 = res.getQuantityString(line1ResId, num, num, formattedTime);
                line2 = res.getString(R.string.zen_mode_until, formattedTime);
            } else {
                // display as day/time
                summary = line1 = line2 = res.getString(R.string.zen_mode_until, formattedTime);
            }

        final android.net.Uri id = android.service.notification.ZenModeConfig.toCountdownConditionId(time);
        return new android.service.notification.Condition(id, summary, line1, line2, 0, android.service.notification.Condition.STATE_TRUE, android.service.notification.Condition.FLAG_RELEVANT_NOW);
    }

    public static android.service.notification.Condition toNextAlarmCondition(android.content.Context context, long now, long alarm, int userHandle) {
        final java.lang.CharSequence formattedTime = android.service.notification.ZenModeConfig.getFormattedTime(context, alarm, userHandle);
        final android.content.res.Resources res = context.getResources();
        final java.lang.String line1 = res.getString(R.string.zen_mode_alarm, formattedTime);
        final android.net.Uri id = android.service.notification.ZenModeConfig.toCountdownConditionId(alarm);
        return new android.service.notification.Condition(id, "", line1, "", 0, android.service.notification.Condition.STATE_TRUE, android.service.notification.Condition.FLAG_RELEVANT_NOW);
    }

    private static java.lang.CharSequence getFormattedTime(android.content.Context context, long time, int userHandle) {
        java.lang.String skeleton = "EEE " + (android.text.format.DateFormat.is24HourFormat(context, userHandle) ? "Hm" : "hma");
        java.util.GregorianCalendar now = new java.util.GregorianCalendar();
        java.util.GregorianCalendar endTime = new java.util.GregorianCalendar();
        endTime.setTimeInMillis(time);
        if (((now.get(java.util.Calendar.YEAR) == endTime.get(java.util.Calendar.YEAR)) && (now.get(java.util.Calendar.MONTH) == endTime.get(java.util.Calendar.MONTH))) && (now.get(java.util.Calendar.DATE) == endTime.get(java.util.Calendar.DATE))) {
            skeleton = (android.text.format.DateFormat.is24HourFormat(context, userHandle)) ? "Hm" : "hma";
        }
        final java.lang.String pattern = android.text.format.DateFormat.getBestDateTimePattern(java.util.Locale.getDefault(), skeleton);
        return android.text.format.DateFormat.format(pattern, time);
    }

    // ==== Built-in system conditions ====
    public static final java.lang.String SYSTEM_AUTHORITY = "android";

    // ==== Built-in system condition: countdown ====
    public static final java.lang.String COUNTDOWN_PATH = "countdown";

    public static android.net.Uri toCountdownConditionId(long time) {
        return new android.net.Uri.Builder().scheme(android.service.notification.Condition.SCHEME).authority(android.service.notification.ZenModeConfig.SYSTEM_AUTHORITY).appendPath(android.service.notification.ZenModeConfig.COUNTDOWN_PATH).appendPath(java.lang.Long.toString(time)).build();
    }

    public static long tryParseCountdownConditionId(android.net.Uri conditionId) {
        if (!android.service.notification.Condition.isValidId(conditionId, android.service.notification.ZenModeConfig.SYSTEM_AUTHORITY))
            return 0;

        if ((conditionId.getPathSegments().size() != 2) || (!android.service.notification.ZenModeConfig.COUNTDOWN_PATH.equals(conditionId.getPathSegments().get(0))))
            return 0;

        try {
            return java.lang.Long.parseLong(conditionId.getPathSegments().get(1));
        } catch (java.lang.RuntimeException e) {
            android.util.Slog.w(android.service.notification.ZenModeConfig.TAG, "Error parsing countdown condition: " + conditionId, e);
            return 0;
        }
    }

    public static boolean isValidCountdownConditionId(android.net.Uri conditionId) {
        return android.service.notification.ZenModeConfig.tryParseCountdownConditionId(conditionId) != 0;
    }

    // ==== Built-in system condition: schedule ====
    public static final java.lang.String SCHEDULE_PATH = "schedule";

    public static android.net.Uri toScheduleConditionId(android.service.notification.ZenModeConfig.ScheduleInfo schedule) {
        return new android.net.Uri.Builder().scheme(android.service.notification.Condition.SCHEME).authority(android.service.notification.ZenModeConfig.SYSTEM_AUTHORITY).appendPath(android.service.notification.ZenModeConfig.SCHEDULE_PATH).appendQueryParameter("days", android.service.notification.ZenModeConfig.toDayList(schedule.days)).appendQueryParameter("start", (schedule.startHour + ".") + schedule.startMinute).appendQueryParameter("end", (schedule.endHour + ".") + schedule.endMinute).appendQueryParameter("exitAtAlarm", java.lang.String.valueOf(schedule.exitAtAlarm)).build();
    }

    public static boolean isValidScheduleConditionId(android.net.Uri conditionId) {
        return android.service.notification.ZenModeConfig.tryParseScheduleConditionId(conditionId) != null;
    }

    public static android.service.notification.ZenModeConfig.ScheduleInfo tryParseScheduleConditionId(android.net.Uri conditionId) {
        final boolean isSchedule = ((((conditionId != null) && conditionId.getScheme().equals(android.service.notification.Condition.SCHEME)) && conditionId.getAuthority().equals(android.service.notification.ZenModeConfig.SYSTEM_AUTHORITY)) && (conditionId.getPathSegments().size() == 1)) && conditionId.getPathSegments().get(0).equals(android.service.notification.ZenModeConfig.SCHEDULE_PATH);
        if (!isSchedule)
            return null;

        final int[] start = android.service.notification.ZenModeConfig.tryParseHourAndMinute(conditionId.getQueryParameter("start"));
        final int[] end = android.service.notification.ZenModeConfig.tryParseHourAndMinute(conditionId.getQueryParameter("end"));
        if ((start == null) || (end == null))
            return null;

        final android.service.notification.ZenModeConfig.ScheduleInfo rt = new android.service.notification.ZenModeConfig.ScheduleInfo();
        rt.days = android.service.notification.ZenModeConfig.tryParseDayList(conditionId.getQueryParameter("days"), "\\.");
        rt.startHour = start[0];
        rt.startMinute = start[1];
        rt.endHour = end[0];
        rt.endMinute = end[1];
        rt.exitAtAlarm = android.service.notification.ZenModeConfig.safeBoolean(conditionId.getQueryParameter("exitAtAlarm"), false);
        return rt;
    }

    public static android.content.ComponentName getScheduleConditionProvider() {
        return new android.content.ComponentName(android.service.notification.ZenModeConfig.SYSTEM_AUTHORITY, "ScheduleConditionProvider");
    }

    public static class ScheduleInfo {
        public int[] days;

        public int startHour;

        public int startMinute;

        public int endHour;

        public int endMinute;

        public boolean exitAtAlarm;

        public long nextAlarm;

        @java.lang.Override
        public int hashCode() {
            return 0;
        }

        @java.lang.Override
        public boolean equals(java.lang.Object o) {
            if (!(o instanceof android.service.notification.ZenModeConfig.ScheduleInfo))
                return false;

            final android.service.notification.ZenModeConfig.ScheduleInfo other = ((android.service.notification.ZenModeConfig.ScheduleInfo) (o));
            return ((((android.service.notification.ZenModeConfig.toDayList(days).equals(android.service.notification.ZenModeConfig.toDayList(other.days)) && (startHour == other.startHour)) && (startMinute == other.startMinute)) && (endHour == other.endHour)) && (endMinute == other.endMinute)) && (exitAtAlarm == other.exitAtAlarm);
        }

        public android.service.notification.ZenModeConfig.ScheduleInfo copy() {
            final android.service.notification.ZenModeConfig.ScheduleInfo rt = new android.service.notification.ZenModeConfig.ScheduleInfo();
            if (days != null) {
                rt.days = new int[days.length];
                java.lang.System.arraycopy(days, 0, rt.days, 0, days.length);
            }
            rt.startHour = startHour;
            rt.startMinute = startMinute;
            rt.endHour = endHour;
            rt.endMinute = endMinute;
            rt.exitAtAlarm = exitAtAlarm;
            rt.nextAlarm = nextAlarm;
            return rt;
        }

        @java.lang.Override
        public java.lang.String toString() {
            return (((((((((((((("ScheduleInfo{" + "days=") + java.util.Arrays.toString(days)) + ", startHour=") + startHour) + ", startMinute=") + startMinute) + ", endHour=") + endHour) + ", endMinute=") + endMinute) + ", exitAtAlarm=") + exitAtAlarm) + ", nextAlarm=") + android.service.notification.ZenModeConfig.ScheduleInfo.ts(nextAlarm)) + '}';
        }

        protected static java.lang.String ts(long time) {
            return ((new java.util.Date(time) + " (") + time) + ")";
        }
    }

    // ==== Built-in system condition: event ====
    public static final java.lang.String EVENT_PATH = "event";

    public static android.net.Uri toEventConditionId(android.service.notification.ZenModeConfig.EventInfo event) {
        return new android.net.Uri.Builder().scheme(android.service.notification.Condition.SCHEME).authority(android.service.notification.ZenModeConfig.SYSTEM_AUTHORITY).appendPath(android.service.notification.ZenModeConfig.EVENT_PATH).appendQueryParameter("userId", java.lang.Long.toString(event.userId)).appendQueryParameter("calendar", event.calendar != null ? event.calendar : "").appendQueryParameter("reply", java.lang.Integer.toString(event.reply)).build();
    }

    public static boolean isValidEventConditionId(android.net.Uri conditionId) {
        return android.service.notification.ZenModeConfig.tryParseEventConditionId(conditionId) != null;
    }

    public static android.service.notification.ZenModeConfig.EventInfo tryParseEventConditionId(android.net.Uri conditionId) {
        final boolean isEvent = ((((conditionId != null) && conditionId.getScheme().equals(android.service.notification.Condition.SCHEME)) && conditionId.getAuthority().equals(android.service.notification.ZenModeConfig.SYSTEM_AUTHORITY)) && (conditionId.getPathSegments().size() == 1)) && conditionId.getPathSegments().get(0).equals(android.service.notification.ZenModeConfig.EVENT_PATH);
        if (!isEvent)
            return null;

        final android.service.notification.ZenModeConfig.EventInfo rt = new android.service.notification.ZenModeConfig.EventInfo();
        rt.userId = android.service.notification.ZenModeConfig.tryParseInt(conditionId.getQueryParameter("userId"), android.os.UserHandle.USER_NULL);
        rt.calendar = conditionId.getQueryParameter("calendar");
        if (android.text.TextUtils.isEmpty(rt.calendar) || (android.service.notification.ZenModeConfig.tryParseLong(rt.calendar, -1L) != (-1L))) {
            rt.calendar = null;
        }
        rt.reply = android.service.notification.ZenModeConfig.tryParseInt(conditionId.getQueryParameter("reply"), 0);
        return rt;
    }

    public static android.content.ComponentName getEventConditionProvider() {
        return new android.content.ComponentName(android.service.notification.ZenModeConfig.SYSTEM_AUTHORITY, "EventConditionProvider");
    }

    public static class EventInfo {
        public static final int REPLY_ANY_EXCEPT_NO = 0;

        public static final int REPLY_YES_OR_MAYBE = 1;

        public static final int REPLY_YES = 2;

        public int userId = android.os.UserHandle.USER_NULL;// USER_NULL = unspecified - use current user


        public java.lang.String calendar;// CalendarContract.Calendars.OWNER_ACCOUNT, or null for any


        public int reply;

        @java.lang.Override
        public int hashCode() {
            return 0;
        }

        @java.lang.Override
        public boolean equals(java.lang.Object o) {
            if (!(o instanceof android.service.notification.ZenModeConfig.EventInfo))
                return false;

            final android.service.notification.ZenModeConfig.EventInfo other = ((android.service.notification.ZenModeConfig.EventInfo) (o));
            return ((userId == other.userId) && java.util.Objects.equals(calendar, other.calendar)) && (reply == other.reply);
        }

        public android.service.notification.ZenModeConfig.EventInfo copy() {
            final android.service.notification.ZenModeConfig.EventInfo rt = new android.service.notification.ZenModeConfig.EventInfo();
            rt.userId = userId;
            rt.calendar = calendar;
            rt.reply = reply;
            return rt;
        }

        public static int resolveUserId(int userId) {
            return userId == android.os.UserHandle.USER_NULL ? android.app.ActivityManager.getCurrentUser() : userId;
        }
    }

    // ==== End built-in system conditions ====
    private static int[] tryParseHourAndMinute(java.lang.String value) {
        if (android.text.TextUtils.isEmpty(value))
            return null;

        final int i = value.indexOf('.');
        if ((i < 1) || (i >= (value.length() - 1)))
            return null;

        final int hour = android.service.notification.ZenModeConfig.tryParseInt(value.substring(0, i), -1);
        final int minute = android.service.notification.ZenModeConfig.tryParseInt(value.substring(i + 1), -1);
        return android.service.notification.ZenModeConfig.isValidHour(hour) && android.service.notification.ZenModeConfig.isValidMinute(minute) ? new int[]{ hour, minute } : null;
    }

    private static int tryParseZenMode(java.lang.String value, int defValue) {
        final int rt = android.service.notification.ZenModeConfig.tryParseInt(value, defValue);
        return android.provider.Settings.Global.isValidZenMode(rt) ? rt : defValue;
    }

    public static java.lang.String newRuleId() {
        return java.util.UUID.randomUUID().toString().replace("-", "");
    }

    private static java.lang.String getOwnerCaption(android.content.Context context, java.lang.String owner) {
        final android.content.pm.PackageManager pm = context.getPackageManager();
        try {
            final android.content.pm.ApplicationInfo info = pm.getApplicationInfo(owner, 0);
            if (info != null) {
                final java.lang.CharSequence seq = info.loadLabel(pm);
                if (seq != null) {
                    final java.lang.String str = seq.toString().trim();
                    if (str.length() > 0) {
                        return str;
                    }
                }
            }
        } catch (java.lang.Throwable e) {
            android.util.Slog.w(android.service.notification.ZenModeConfig.TAG, "Error loading owner caption", e);
        }
        return "";
    }

    public static java.lang.String getConditionSummary(android.content.Context context, android.service.notification.ZenModeConfig config, int userHandle, boolean shortVersion) {
        return /* useLine1 */
        android.service.notification.ZenModeConfig.getConditionLine(context, config, userHandle, false, shortVersion);
    }

    private static java.lang.String getConditionLine(android.content.Context context, android.service.notification.ZenModeConfig config, int userHandle, boolean useLine1, boolean shortVersion) {
        if (config == null)
            return "";

        java.lang.String summary = "";
        if (config.manualRule != null) {
            final android.net.Uri id = config.manualRule.conditionId;
            if (config.manualRule.enabler != null) {
                summary = android.service.notification.ZenModeConfig.getOwnerCaption(context, config.manualRule.enabler);
            } else {
                if (id == null) {
                    summary = context.getString(com.android.internal.R.string.zen_mode_forever);
                } else {
                    final long time = android.service.notification.ZenModeConfig.tryParseCountdownConditionId(id);
                    android.service.notification.Condition c = config.manualRule.condition;
                    if (time > 0) {
                        final long now = java.lang.System.currentTimeMillis();
                        final long span = time - now;
                        c = android.service.notification.ZenModeConfig.toTimeCondition(context, time, java.lang.Math.round(span / ((float) (android.service.notification.ZenModeConfig.MINUTES_MS))), userHandle, shortVersion);
                    }
                    final java.lang.String rt = (c == null) ? "" : useLine1 ? c.line1 : c.summary;
                    summary = (android.text.TextUtils.isEmpty(rt)) ? "" : rt;
                }
            }
        }
        for (android.service.notification.ZenModeConfig.ZenRule automaticRule : config.automaticRules.values()) {
            if (automaticRule.isAutomaticActive()) {
                if (summary.isEmpty()) {
                    summary = automaticRule.name;
                } else {
                    summary = context.getResources().getString(R.string.zen_mode_rule_name_combination, summary, automaticRule.name);
                }
            }
        }
        return summary;
    }

    public static class ZenRule implements android.os.Parcelable {
        public boolean enabled;

        public boolean snoozing;// user manually disabled this instance


        public java.lang.String name;// required for automatic


        public int zenMode;

        public android.net.Uri conditionId;// required for automatic


        public android.service.notification.Condition condition;// optional


        public android.content.ComponentName component;// optional


        public java.lang.String id;// required for automatic (unique)


        public long creationTime;// required for automatic


        public java.lang.String enabler;// package name, only used for manual rules.


        public ZenRule() {
        }

        public ZenRule(android.os.Parcel source) {
            enabled = source.readInt() == 1;
            snoozing = source.readInt() == 1;
            if (source.readInt() == 1) {
                name = source.readString();
            }
            zenMode = source.readInt();
            conditionId = source.readParcelable(null);
            condition = source.readParcelable(null);
            component = source.readParcelable(null);
            if (source.readInt() == 1) {
                id = source.readString();
            }
            creationTime = source.readLong();
            if (source.readInt() == 1) {
                enabler = source.readString();
            }
        }

        @java.lang.Override
        public int describeContents() {
            return 0;
        }

        @java.lang.Override
        public void writeToParcel(android.os.Parcel dest, int flags) {
            dest.writeInt(enabled ? 1 : 0);
            dest.writeInt(snoozing ? 1 : 0);
            if (name != null) {
                dest.writeInt(1);
                dest.writeString(name);
            } else {
                dest.writeInt(0);
            }
            dest.writeInt(zenMode);
            dest.writeParcelable(conditionId, 0);
            dest.writeParcelable(condition, 0);
            dest.writeParcelable(component, 0);
            if (id != null) {
                dest.writeInt(1);
                dest.writeString(id);
            } else {
                dest.writeInt(0);
            }
            dest.writeLong(creationTime);
            if (enabler != null) {
                dest.writeInt(1);
                dest.writeString(enabler);
            } else {
                dest.writeInt(0);
            }
        }

        @java.lang.Override
        public java.lang.String toString() {
            return new java.lang.StringBuilder(android.service.notification.ZenModeConfig.ZenRule.class.getSimpleName()).append('[').append("enabled=").append(enabled).append(",snoozing=").append(snoozing).append(",name=").append(name).append(",zenMode=").append(android.provider.Settings.Global.zenModeToString(zenMode)).append(",conditionId=").append(conditionId).append(",condition=").append(condition).append(",component=").append(component).append(",id=").append(id).append(",creationTime=").append(creationTime).append(",enabler=").append(enabler).append(']').toString();
        }

        private static void appendDiff(android.service.notification.ZenModeConfig.Diff d, java.lang.String item, android.service.notification.ZenModeConfig.ZenRule from, android.service.notification.ZenModeConfig.ZenRule to) {
            if (d == null)
                return;

            if (from == null) {
                if (to != null) {
                    d.addLine(item, "insert");
                }
                return;
            }
            from.appendDiff(d, item, to);
        }

        private void appendDiff(android.service.notification.ZenModeConfig.Diff d, java.lang.String item, android.service.notification.ZenModeConfig.ZenRule to) {
            if (to == null) {
                d.addLine(item, "delete");
                return;
            }
            if (enabled != to.enabled) {
                d.addLine(item, "enabled", enabled, to.enabled);
            }
            if (snoozing != to.snoozing) {
                d.addLine(item, "snoozing", snoozing, to.snoozing);
            }
            if (!java.util.Objects.equals(name, to.name)) {
                d.addLine(item, "name", name, to.name);
            }
            if (zenMode != to.zenMode) {
                d.addLine(item, "zenMode", zenMode, to.zenMode);
            }
            if (!java.util.Objects.equals(conditionId, to.conditionId)) {
                d.addLine(item, "conditionId", conditionId, to.conditionId);
            }
            if (!java.util.Objects.equals(condition, to.condition)) {
                d.addLine(item, "condition", condition, to.condition);
            }
            if (!java.util.Objects.equals(component, to.component)) {
                d.addLine(item, "component", component, to.component);
            }
            if (!java.util.Objects.equals(id, to.id)) {
                d.addLine(item, "id", id, to.id);
            }
            if (creationTime != to.creationTime) {
                d.addLine(item, "creationTime", creationTime, to.creationTime);
            }
            if (enabler != to.enabler) {
                d.addLine(item, "enabler", enabler, to.enabler);
            }
        }

        @java.lang.Override
        public boolean equals(java.lang.Object o) {
            if (!(o instanceof android.service.notification.ZenModeConfig.ZenRule))
                return false;

            if (o == this)
                return true;

            final android.service.notification.ZenModeConfig.ZenRule other = ((android.service.notification.ZenModeConfig.ZenRule) (o));
            return (((((((((other.enabled == enabled) && (other.snoozing == snoozing)) && java.util.Objects.equals(other.name, name)) && (other.zenMode == zenMode)) && java.util.Objects.equals(other.conditionId, conditionId)) && java.util.Objects.equals(other.condition, condition)) && java.util.Objects.equals(other.component, component)) && java.util.Objects.equals(other.id, id)) && (other.creationTime == creationTime)) && java.util.Objects.equals(other.enabler, enabler);
        }

        @java.lang.Override
        public int hashCode() {
            return java.util.Objects.hash(enabled, snoozing, name, zenMode, conditionId, condition, component, id, creationTime, enabler);
        }

        public boolean isAutomaticActive() {
            return ((enabled && (!snoozing)) && (component != null)) && isTrueOrUnknown();
        }

        public boolean isTrueOrUnknown() {
            return (condition != null) && ((condition.state == android.service.notification.Condition.STATE_TRUE) || (condition.state == android.service.notification.Condition.STATE_UNKNOWN));
        }

        public static final android.os.Parcelable.Creator<android.service.notification.ZenModeConfig.ZenRule> CREATOR = new android.os.Parcelable.Creator<android.service.notification.ZenModeConfig.ZenRule>() {
            @java.lang.Override
            public android.service.notification.ZenModeConfig.ZenRule createFromParcel(android.os.Parcel source) {
                return new android.service.notification.ZenModeConfig.ZenRule(source);
            }

            @java.lang.Override
            public android.service.notification.ZenModeConfig.ZenRule[] newArray(int size) {
                return new android.service.notification.ZenModeConfig.ZenRule[size];
            }
        };
    }

    // Legacy config
    public static final class XmlV1 {
        public static final java.lang.String SLEEP_MODE_NIGHTS = "nights";

        public static final java.lang.String SLEEP_MODE_WEEKNIGHTS = "weeknights";

        public static final java.lang.String SLEEP_MODE_DAYS_PREFIX = "days:";

        private static final java.lang.String EXIT_CONDITION_TAG = "exitCondition";

        private static final java.lang.String EXIT_CONDITION_ATT_COMPONENT = "component";

        private static final java.lang.String SLEEP_TAG = "sleep";

        private static final java.lang.String SLEEP_ATT_MODE = "mode";

        private static final java.lang.String SLEEP_ATT_NONE = "none";

        private static final java.lang.String SLEEP_ATT_START_HR = "startHour";

        private static final java.lang.String SLEEP_ATT_START_MIN = "startMin";

        private static final java.lang.String SLEEP_ATT_END_HR = "endHour";

        private static final java.lang.String SLEEP_ATT_END_MIN = "endMin";

        public boolean allowCalls;

        public boolean allowMessages;

        public boolean allowReminders = android.service.notification.ZenModeConfig.DEFAULT_ALLOW_REMINDERS;

        public boolean allowEvents = android.service.notification.ZenModeConfig.DEFAULT_ALLOW_EVENTS;

        public int allowFrom = android.service.notification.ZenModeConfig.SOURCE_ANYONE;

        public java.lang.String sleepMode;// nights, weeknights, days:1,2,3  Calendar.days


        public int sleepStartHour;// 0-23


        public int sleepStartMinute;// 0-59


        public int sleepEndHour;

        public int sleepEndMinute;

        public boolean sleepNone;// false = priority, true = none


        public android.content.ComponentName[] conditionComponents;

        public android.net.Uri[] conditionIds;

        public android.service.notification.Condition exitCondition;// manual exit condition


        public android.content.ComponentName exitConditionComponent;// manual exit condition component


        private static boolean isValidSleepMode(java.lang.String sleepMode) {
            return (((sleepMode == null) || sleepMode.equals(android.service.notification.ZenModeConfig.XmlV1.SLEEP_MODE_NIGHTS)) || sleepMode.equals(android.service.notification.ZenModeConfig.XmlV1.SLEEP_MODE_WEEKNIGHTS)) || (android.service.notification.ZenModeConfig.XmlV1.tryParseDays(sleepMode) != null);
        }

        public static int[] tryParseDays(java.lang.String sleepMode) {
            if (sleepMode == null)
                return null;

            sleepMode = sleepMode.trim();
            if (android.service.notification.ZenModeConfig.XmlV1.SLEEP_MODE_NIGHTS.equals(sleepMode))
                return android.service.notification.ZenModeConfig.ALL_DAYS;

            if (android.service.notification.ZenModeConfig.XmlV1.SLEEP_MODE_WEEKNIGHTS.equals(sleepMode))
                return android.service.notification.ZenModeConfig.WEEKNIGHT_DAYS;

            if (!sleepMode.startsWith(android.service.notification.ZenModeConfig.XmlV1.SLEEP_MODE_DAYS_PREFIX))
                return null;

            if (sleepMode.equals(android.service.notification.ZenModeConfig.XmlV1.SLEEP_MODE_DAYS_PREFIX))
                return null;

            return android.service.notification.ZenModeConfig.tryParseDayList(sleepMode.substring(android.service.notification.ZenModeConfig.XmlV1.SLEEP_MODE_DAYS_PREFIX.length()), ",");
        }

        public static android.service.notification.ZenModeConfig.XmlV1 readXml(org.xmlpull.v1.XmlPullParser parser) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
            int type;
            java.lang.String tag;
            android.service.notification.ZenModeConfig.XmlV1 rt = new android.service.notification.ZenModeConfig.XmlV1();
            final java.util.ArrayList<android.content.ComponentName> conditionComponents = new java.util.ArrayList<android.content.ComponentName>();
            final java.util.ArrayList<android.net.Uri> conditionIds = new java.util.ArrayList<android.net.Uri>();
            while ((type = parser.next()) != org.xmlpull.v1.XmlPullParser.END_DOCUMENT) {
                tag = parser.getName();
                if ((type == org.xmlpull.v1.XmlPullParser.END_TAG) && android.service.notification.ZenModeConfig.ZEN_TAG.equals(tag)) {
                    if (!conditionComponents.isEmpty()) {
                        rt.conditionComponents = conditionComponents.toArray(new android.content.ComponentName[conditionComponents.size()]);
                        rt.conditionIds = conditionIds.toArray(new android.net.Uri[conditionIds.size()]);
                    }
                    return rt;
                }
                if (type == org.xmlpull.v1.XmlPullParser.START_TAG) {
                    if (android.service.notification.ZenModeConfig.ALLOW_TAG.equals(tag)) {
                        rt.allowCalls = android.service.notification.ZenModeConfig.safeBoolean(parser, android.service.notification.ZenModeConfig.ALLOW_ATT_CALLS, false);
                        rt.allowMessages = android.service.notification.ZenModeConfig.safeBoolean(parser, android.service.notification.ZenModeConfig.ALLOW_ATT_MESSAGES, false);
                        rt.allowReminders = android.service.notification.ZenModeConfig.safeBoolean(parser, android.service.notification.ZenModeConfig.ALLOW_ATT_REMINDERS, android.service.notification.ZenModeConfig.DEFAULT_ALLOW_REMINDERS);
                        rt.allowEvents = android.service.notification.ZenModeConfig.safeBoolean(parser, android.service.notification.ZenModeConfig.ALLOW_ATT_EVENTS, android.service.notification.ZenModeConfig.DEFAULT_ALLOW_EVENTS);
                        rt.allowFrom = android.service.notification.ZenModeConfig.safeInt(parser, android.service.notification.ZenModeConfig.ALLOW_ATT_FROM, android.service.notification.ZenModeConfig.SOURCE_ANYONE);
                        if ((rt.allowFrom < android.service.notification.ZenModeConfig.SOURCE_ANYONE) || (rt.allowFrom > android.service.notification.ZenModeConfig.MAX_SOURCE)) {
                            throw new java.lang.IndexOutOfBoundsException("bad source in config:" + rt.allowFrom);
                        }
                    } else
                        if (android.service.notification.ZenModeConfig.XmlV1.SLEEP_TAG.equals(tag)) {
                            final java.lang.String mode = parser.getAttributeValue(null, android.service.notification.ZenModeConfig.XmlV1.SLEEP_ATT_MODE);
                            rt.sleepMode = (android.service.notification.ZenModeConfig.XmlV1.isValidSleepMode(mode)) ? mode : null;
                            rt.sleepNone = android.service.notification.ZenModeConfig.safeBoolean(parser, android.service.notification.ZenModeConfig.XmlV1.SLEEP_ATT_NONE, false);
                            final int startHour = android.service.notification.ZenModeConfig.safeInt(parser, android.service.notification.ZenModeConfig.XmlV1.SLEEP_ATT_START_HR, 0);
                            final int startMinute = android.service.notification.ZenModeConfig.safeInt(parser, android.service.notification.ZenModeConfig.XmlV1.SLEEP_ATT_START_MIN, 0);
                            final int endHour = android.service.notification.ZenModeConfig.safeInt(parser, android.service.notification.ZenModeConfig.XmlV1.SLEEP_ATT_END_HR, 0);
                            final int endMinute = android.service.notification.ZenModeConfig.safeInt(parser, android.service.notification.ZenModeConfig.XmlV1.SLEEP_ATT_END_MIN, 0);
                            rt.sleepStartHour = (android.service.notification.ZenModeConfig.isValidHour(startHour)) ? startHour : 0;
                            rt.sleepStartMinute = (android.service.notification.ZenModeConfig.isValidMinute(startMinute)) ? startMinute : 0;
                            rt.sleepEndHour = (android.service.notification.ZenModeConfig.isValidHour(endHour)) ? endHour : 0;
                            rt.sleepEndMinute = (android.service.notification.ZenModeConfig.isValidMinute(endMinute)) ? endMinute : 0;
                        } else
                            if (android.service.notification.ZenModeConfig.CONDITION_TAG.equals(tag)) {
                                final android.content.ComponentName component = android.service.notification.ZenModeConfig.safeComponentName(parser, android.service.notification.ZenModeConfig.CONDITION_ATT_COMPONENT);
                                final android.net.Uri conditionId = android.service.notification.ZenModeConfig.safeUri(parser, android.service.notification.ZenModeConfig.CONDITION_ATT_ID);
                                if ((component != null) && (conditionId != null)) {
                                    conditionComponents.add(component);
                                    conditionIds.add(conditionId);
                                }
                            } else
                                if (android.service.notification.ZenModeConfig.XmlV1.EXIT_CONDITION_TAG.equals(tag)) {
                                    rt.exitCondition = android.service.notification.ZenModeConfig.readConditionXml(parser);
                                    if (rt.exitCondition != null) {
                                        rt.exitConditionComponent = android.service.notification.ZenModeConfig.safeComponentName(parser, android.service.notification.ZenModeConfig.XmlV1.EXIT_CONDITION_ATT_COMPONENT);
                                    }
                                }



                }
            } 
            throw new java.lang.IllegalStateException("Failed to reach END_DOCUMENT");
        }
    }

    public interface Migration {
        android.service.notification.ZenModeConfig migrate(android.service.notification.ZenModeConfig.XmlV1 v1);
    }

    public static class Diff {
        private final java.util.ArrayList<java.lang.String> lines = new java.util.ArrayList<>();

        @java.lang.Override
        public java.lang.String toString() {
            final java.lang.StringBuilder sb = new java.lang.StringBuilder("Diff[");
            final int N = lines.size();
            for (int i = 0; i < N; i++) {
                if (i > 0) {
                    sb.append(',');
                }
                sb.append(lines.get(i));
            }
            return sb.append(']').toString();
        }

        private android.service.notification.ZenModeConfig.Diff addLine(java.lang.String item, java.lang.String action) {
            lines.add((item + ":") + action);
            return this;
        }

        public android.service.notification.ZenModeConfig.Diff addLine(java.lang.String item, java.lang.String subitem, java.lang.Object from, java.lang.Object to) {
            return addLine((item + ".") + subitem, from, to);
        }

        public android.service.notification.ZenModeConfig.Diff addLine(java.lang.String item, java.lang.Object from, java.lang.Object to) {
            return addLine(item, (from + "->") + to);
        }
    }
}

