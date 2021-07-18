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
package android.nfc.cardemulation;


/**
 * The AidGroup class represents a group of Application Identifiers (AIDs).
 *
 * <p>The format of AIDs is defined in the ISO/IEC 7816-4 specification. This class
 * requires the AIDs to be input as a hexadecimal string, with an even amount of
 * hexadecimal characters, e.g. "F014811481".
 *
 * @unknown 
 */
public final class AidGroup implements android.os.Parcelable {
    /**
     * The maximum number of AIDs that can be present in any one group.
     */
    public static final int MAX_NUM_AIDS = 256;

    static final java.lang.String TAG = "AidGroup";

    final java.util.List<java.lang.String> aids;

    final java.lang.String category;

    final java.lang.String description;

    /**
     * Creates a new AidGroup object.
     *
     * @param aids
     * 		The list of AIDs present in the group
     * @param category
     * 		The category of this group, e.g. {@link CardEmulation#CATEGORY_PAYMENT}
     */
    public AidGroup(java.util.List<java.lang.String> aids, java.lang.String category) {
        if ((aids == null) || (aids.size() == 0)) {
            throw new java.lang.IllegalArgumentException("No AIDS in AID group.");
        }
        if (aids.size() > android.nfc.cardemulation.AidGroup.MAX_NUM_AIDS) {
            throw new java.lang.IllegalArgumentException("Too many AIDs in AID group.");
        }
        for (java.lang.String aid : aids) {
            if (!android.nfc.cardemulation.CardEmulation.isValidAid(aid)) {
                throw new java.lang.IllegalArgumentException(("AID " + aid) + " is not a valid AID.");
            }
        }
        if (android.nfc.cardemulation.AidGroup.isValidCategory(category)) {
            this.category = category;
        } else {
            this.category = android.nfc.cardemulation.CardEmulation.CATEGORY_OTHER;
        }
        this.aids = new java.util.ArrayList<java.lang.String>(aids.size());
        for (java.lang.String aid : aids) {
            this.aids.add(aid.toUpperCase());
        }
        this.description = null;
    }

    AidGroup(java.lang.String category, java.lang.String description) {
        this.aids = new java.util.ArrayList<java.lang.String>();
        this.category = category;
        this.description = description;
    }

    /**
     *
     *
     * @return the category of this AID group
     */
    public java.lang.String getCategory() {
        return category;
    }

    /**
     *
     *
     * @return the list of AIDs in this group
     */
    public java.util.List<java.lang.String> getAids() {
        return aids;
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder out = new java.lang.StringBuilder(("Category: " + category) + ", AIDs:");
        for (java.lang.String aid : aids) {
            out.append(aid);
            out.append(", ");
        }
        return out.toString();
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeString(category);
        dest.writeInt(aids.size());
        if (aids.size() > 0) {
            dest.writeStringList(aids);
        }
    }

    public static final android.os.Parcelable.Creator<android.nfc.cardemulation.AidGroup> CREATOR = new android.os.Parcelable.Creator<android.nfc.cardemulation.AidGroup>() {
        @java.lang.Override
        public android.nfc.cardemulation.AidGroup createFromParcel(android.os.Parcel source) {
            java.lang.String category = source.readString();
            int listSize = source.readInt();
            java.util.ArrayList<java.lang.String> aidList = new java.util.ArrayList<java.lang.String>();
            if (listSize > 0) {
                source.readStringList(aidList);
            }
            return new android.nfc.cardemulation.AidGroup(aidList, category);
        }

        @java.lang.Override
        public android.nfc.cardemulation.AidGroup[] newArray(int size) {
            return new android.nfc.cardemulation.AidGroup[size];
        }
    };

    public static android.nfc.cardemulation.AidGroup createFromXml(org.xmlpull.v1.XmlPullParser parser) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        java.lang.String category = null;
        java.util.ArrayList<java.lang.String> aids = new java.util.ArrayList<java.lang.String>();
        android.nfc.cardemulation.AidGroup group = null;
        boolean inGroup = false;
        int eventType = parser.getEventType();
        int minDepth = parser.getDepth();
        while ((eventType != org.xmlpull.v1.XmlPullParser.END_DOCUMENT) && (parser.getDepth() >= minDepth)) {
            java.lang.String tagName = parser.getName();
            if (eventType == org.xmlpull.v1.XmlPullParser.START_TAG) {
                if (tagName.equals("aid")) {
                    if (inGroup) {
                        java.lang.String aid = parser.getAttributeValue(null, "value");
                        if (aid != null) {
                            aids.add(aid.toUpperCase());
                        }
                    } else {
                        android.util.Log.d(android.nfc.cardemulation.AidGroup.TAG, "Ignoring <aid> tag while not in group");
                    }
                } else
                    if (tagName.equals("aid-group")) {
                        category = parser.getAttributeValue(null, "category");
                        if (category == null) {
                            android.util.Log.e(android.nfc.cardemulation.AidGroup.TAG, "<aid-group> tag without valid category");
                            return null;
                        }
                        inGroup = true;
                    } else {
                        android.util.Log.d(android.nfc.cardemulation.AidGroup.TAG, "Ignoring unexpected tag: " + tagName);
                    }

            } else
                if (eventType == org.xmlpull.v1.XmlPullParser.END_TAG) {
                    if ((tagName.equals("aid-group") && inGroup) && (aids.size() > 0)) {
                        group = new android.nfc.cardemulation.AidGroup(aids, category);
                        break;
                    }
                }

            eventType = parser.next();
        } 
        return group;
    }

    public void writeAsXml(org.xmlpull.v1.XmlSerializer out) throws java.io.IOException {
        out.startTag(null, "aid-group");
        out.attribute(null, "category", category);
        for (java.lang.String aid : aids) {
            out.startTag(null, "aid");
            out.attribute(null, "value", aid);
            out.endTag(null, "aid");
        }
        out.endTag(null, "aid-group");
    }

    static boolean isValidCategory(java.lang.String category) {
        return android.nfc.cardemulation.CardEmulation.CATEGORY_PAYMENT.equals(category) || android.nfc.cardemulation.CardEmulation.CATEGORY_OTHER.equals(category);
    }
}

