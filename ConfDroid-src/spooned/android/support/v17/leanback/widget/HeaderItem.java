/**
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package android.support.v17.leanback.widget;


/**
 * A header item describes the metadata of a {@link Row}, such as a category
 * of media items.  May be subclassed to add more information.
 */
public class HeaderItem {
    private final long mId;

    private final java.lang.String mName;

    private java.lang.CharSequence mContentDescription;

    /**
     * Create a header item.  All fields are optional.
     */
    public HeaderItem(long id, java.lang.String name) {
        mId = id;
        mName = name;
    }

    /**
     * Create a header item.
     */
    public HeaderItem(java.lang.String name) {
        this(android.support.v17.leanback.widget.ObjectAdapter.NO_ID, name);
    }

    /**
     * Returns a unique identifier for this item.
     */
    public final long getId() {
        return mId;
    }

    /**
     * Returns the name of this header item.
     */
    public final java.lang.String getName() {
        return mName;
    }

    /**
     * Returns optional content description for the HeaderItem.  When it is null, {@link #getName()}
     * should be used for the content description.
     *
     * @return Content description for the HeaderItem.
     */
    public java.lang.CharSequence getContentDescription() {
        return mContentDescription;
    }

    /**
     * Sets optional content description for the HeaderItem.
     *
     * @param contentDescription
     * 		Content description sets on the HeaderItem.
     */
    public void setContentDescription(java.lang.CharSequence contentDescription) {
        mContentDescription = contentDescription;
    }
}

