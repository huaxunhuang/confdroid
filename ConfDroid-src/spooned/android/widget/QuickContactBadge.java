/**
 * Copyright (C) 2009 The Android Open Source Project
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
package android.widget;


/**
 * Widget used to show an image with the standard QuickContact badge
 * and on-click behavior.
 */
public class QuickContactBadge extends android.widget.ImageView implements android.view.View.OnClickListener {
    private android.net.Uri mContactUri;

    private java.lang.String mContactEmail;

    private java.lang.String mContactPhone;

    @android.annotation.UnsupportedAppUsage
    private android.graphics.drawable.Drawable mOverlay;

    private android.widget.QuickContactBadge.QueryHandler mQueryHandler;

    private android.graphics.drawable.Drawable mDefaultAvatar;

    private android.os.Bundle mExtras = null;

    private java.lang.String mPrioritizedMimeType;

    protected java.lang.String[] mExcludeMimes = null;

    private static final int TOKEN_EMAIL_LOOKUP = 0;

    private static final int TOKEN_PHONE_LOOKUP = 1;

    private static final int TOKEN_EMAIL_LOOKUP_AND_TRIGGER = 2;

    private static final int TOKEN_PHONE_LOOKUP_AND_TRIGGER = 3;

    private static final java.lang.String EXTRA_URI_CONTENT = "uri_content";

    static final java.lang.String[] EMAIL_LOOKUP_PROJECTION = new java.lang.String[]{ android.provider.ContactsContract.RawContacts.CONTACT_ID, android.provider.ContactsContract.Contacts.LOOKUP_KEY };

    static final int EMAIL_ID_COLUMN_INDEX = 0;

    static final int EMAIL_LOOKUP_STRING_COLUMN_INDEX = 1;

    static final java.lang.String[] PHONE_LOOKUP_PROJECTION = new java.lang.String[]{ android.provider.ContactsContract.PhoneLookup._ID, android.provider.ContactsContract.PhoneLookup.LOOKUP_KEY };

    static final int PHONE_ID_COLUMN_INDEX = 0;

    static final int PHONE_LOOKUP_STRING_COLUMN_INDEX = 1;

    public QuickContactBadge(android.content.Context context) {
        this(context, null);
    }

    public QuickContactBadge(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QuickContactBadge(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public QuickContactBadge(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        android.content.res.TypedArray styledAttributes = mContext.obtainStyledAttributes(R.styleable.Theme);
        mOverlay = styledAttributes.getDrawable(com.android.internal.R.styleable.Theme_quickContactBadgeOverlay);
        styledAttributes.recycle();
        setOnClickListener(this);
    }

    @java.lang.Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isInEditMode()) {
            mQueryHandler = new android.widget.QuickContactBadge.QueryHandler(mContext.getContentResolver());
        }
    }

    @java.lang.Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        final android.graphics.drawable.Drawable overlay = mOverlay;
        if (((overlay != null) && overlay.isStateful()) && overlay.setState(getDrawableState())) {
            invalidateDrawable(overlay);
        }
    }

    @java.lang.Override
    public void drawableHotspotChanged(float x, float y) {
        super.drawableHotspotChanged(x, y);
        if (mOverlay != null) {
            mOverlay.setHotspot(x, y);
        }
    }

    /**
     * This call has no effect anymore, as there is only one QuickContact mode
     */
    @java.lang.SuppressWarnings("unused")
    public void setMode(int size) {
    }

    /**
     * Set which mimetype should be prioritized in the QuickContacts UI. For example, passing the
     * value {@link Email#CONTENT_ITEM_TYPE} can cause emails to be displayed more prominently in
     * QuickContacts.
     */
    public void setPrioritizedMimeType(java.lang.String prioritizedMimeType) {
        mPrioritizedMimeType = prioritizedMimeType;
    }

    @java.lang.Override
    protected void onDraw(android.graphics.Canvas canvas) {
        super.onDraw(canvas);
        if (!isEnabled()) {
            // not clickable? don't show triangle
            return;
        }
        if (((mOverlay == null) || (mOverlay.getIntrinsicWidth() == 0)) || (mOverlay.getIntrinsicHeight() == 0)) {
            // nothing to draw
            return;
        }
        mOverlay.setBounds(0, 0, getWidth(), getHeight());
        if ((mPaddingTop == 0) && (mPaddingLeft == 0)) {
            mOverlay.draw(canvas);
        } else {
            int saveCount = canvas.getSaveCount();
            canvas.save();
            canvas.translate(mPaddingLeft, mPaddingTop);
            mOverlay.draw(canvas);
            canvas.restoreToCount(saveCount);
        }
    }

    /**
     * True if a contact, an email address or a phone number has been assigned
     */
    private boolean isAssigned() {
        return ((mContactUri != null) || (mContactEmail != null)) || (mContactPhone != null);
    }

    /**
     * Resets the contact photo to the default state.
     */
    public void setImageToDefault() {
        if (mDefaultAvatar == null) {
            mDefaultAvatar = mContext.getDrawable(R.drawable.ic_contact_picture);
        }
        setImageDrawable(mDefaultAvatar);
    }

    /**
     * Assign the contact uri that this QuickContactBadge should be associated
     * with. Note that this is only used for displaying the QuickContact window and
     * won't bind the contact's photo for you. Call {@link #setImageDrawable(Drawable)} to set the
     * photo.
     *
     * @param contactUri
     * 		Either a {@link Contacts#CONTENT_URI} or
     * 		{@link Contacts#CONTENT_LOOKUP_URI} style URI.
     */
    public void assignContactUri(android.net.Uri contactUri) {
        mContactUri = contactUri;
        mContactEmail = null;
        mContactPhone = null;
        onContactUriChanged();
    }

    /**
     * Assign a contact based on an email address. This should only be used when
     * the contact's URI is not available, as an extra query will have to be
     * performed to lookup the URI based on the email.
     *
     * @param emailAddress
     * 		The email address of the contact.
     * @param lazyLookup
     * 		If this is true, the lookup query will not be performed
     * 		until this view is clicked.
     */
    public void assignContactFromEmail(java.lang.String emailAddress, boolean lazyLookup) {
        assignContactFromEmail(emailAddress, lazyLookup, null);
    }

    /**
     * Assign a contact based on an email address. This should only be used when
     * the contact's URI is not available, as an extra query will have to be
     * performed to lookup the URI based on the email.
     *
     * @param emailAddress
     * 		The email address of the contact.
     * @param lazyLookup
     * 		If this is true, the lookup query will not be performed
     * 		until this view is clicked.
     * @param extras
     * 		A bundle of extras to populate the contact edit page with if the contact
     * 		is not found and the user chooses to add the email address to an existing contact or
     * 		create a new contact. Uses the same string constants as those found in
     * 		{@link android.provider.ContactsContract.Intents.Insert}
     */
    public void assignContactFromEmail(java.lang.String emailAddress, boolean lazyLookup, android.os.Bundle extras) {
        mContactEmail = emailAddress;
        mExtras = extras;
        if ((!lazyLookup) && (mQueryHandler != null)) {
            mQueryHandler.startQuery(android.widget.QuickContactBadge.TOKEN_EMAIL_LOOKUP, null, android.net.Uri.withAppendedPath(Email.CONTENT_LOOKUP_URI, android.net.Uri.encode(mContactEmail)), android.widget.QuickContactBadge.EMAIL_LOOKUP_PROJECTION, null, null, null);
        } else {
            mContactUri = null;
            onContactUriChanged();
        }
    }

    /**
     * Assign a contact based on a phone number. This should only be used when
     * the contact's URI is not available, as an extra query will have to be
     * performed to lookup the URI based on the phone number.
     *
     * @param phoneNumber
     * 		The phone number of the contact.
     * @param lazyLookup
     * 		If this is true, the lookup query will not be performed
     * 		until this view is clicked.
     */
    public void assignContactFromPhone(java.lang.String phoneNumber, boolean lazyLookup) {
        assignContactFromPhone(phoneNumber, lazyLookup, new android.os.Bundle());
    }

    /**
     * Assign a contact based on a phone number. This should only be used when
     * the contact's URI is not available, as an extra query will have to be
     * performed to lookup the URI based on the phone number.
     *
     * @param phoneNumber
     * 		The phone number of the contact.
     * @param lazyLookup
     * 		If this is true, the lookup query will not be performed
     * 		until this view is clicked.
     * @param extras
     * 		A bundle of extras to populate the contact edit page with if the contact
     * 		is not found and the user chooses to add the phone number to an existing contact or
     * 		create a new contact. Uses the same string constants as those found in
     * 		{@link android.provider.ContactsContract.Intents.Insert}
     */
    public void assignContactFromPhone(java.lang.String phoneNumber, boolean lazyLookup, android.os.Bundle extras) {
        mContactPhone = phoneNumber;
        mExtras = extras;
        if ((!lazyLookup) && (mQueryHandler != null)) {
            mQueryHandler.startQuery(android.widget.QuickContactBadge.TOKEN_PHONE_LOOKUP, null, android.net.Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, mContactPhone), android.widget.QuickContactBadge.PHONE_LOOKUP_PROJECTION, null, null, null);
        } else {
            mContactUri = null;
            onContactUriChanged();
        }
    }

    /**
     * Assigns the drawable that is to be drawn on top of the assigned contact photo.
     *
     * @param overlay
     * 		Drawable to be drawn over the assigned contact photo. Must have a non-zero
     * 		instrinsic width and height.
     */
    public void setOverlay(android.graphics.drawable.Drawable overlay) {
        mOverlay = overlay;
    }

    private void onContactUriChanged() {
        setEnabled(isAssigned());
    }

    @java.lang.Override
    public void onClick(android.view.View v) {
        // If contact has been assigned, mExtras should no longer be null, but do a null check
        // anyway just in case assignContactFromPhone or Email was called with a null bundle or
        // wasn't assigned previously.
        final android.os.Bundle extras = (mExtras == null) ? new android.os.Bundle() : mExtras;
        if (mContactUri != null) {
            android.provider.ContactsContract.QuickContact.showQuickContact(getContext(), this, mContactUri, mExcludeMimes, mPrioritizedMimeType);
        } else
            if ((mContactEmail != null) && (mQueryHandler != null)) {
                extras.putString(android.widget.QuickContactBadge.EXTRA_URI_CONTENT, mContactEmail);
                mQueryHandler.startQuery(android.widget.QuickContactBadge.TOKEN_EMAIL_LOOKUP_AND_TRIGGER, extras, android.net.Uri.withAppendedPath(Email.CONTENT_LOOKUP_URI, android.net.Uri.encode(mContactEmail)), android.widget.QuickContactBadge.EMAIL_LOOKUP_PROJECTION, null, null, null);
            } else
                if ((mContactPhone != null) && (mQueryHandler != null)) {
                    extras.putString(android.widget.QuickContactBadge.EXTRA_URI_CONTENT, mContactPhone);
                    mQueryHandler.startQuery(android.widget.QuickContactBadge.TOKEN_PHONE_LOOKUP_AND_TRIGGER, extras, android.net.Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, mContactPhone), android.widget.QuickContactBadge.PHONE_LOOKUP_PROJECTION, null, null, null);
                } else {
                    // If a contact hasn't been assigned, don't react to click.
                    return;
                }


    }

    @java.lang.Override
    public java.lang.CharSequence getAccessibilityClassName() {
        return android.widget.QuickContactBadge.class.getName();
    }

    /**
     * Set a list of specific MIME-types to exclude and not display. For
     * example, this can be used to hide the {@link Contacts#CONTENT_ITEM_TYPE}
     * profile icon.
     */
    public void setExcludeMimes(java.lang.String[] excludeMimes) {
        mExcludeMimes = excludeMimes;
    }

    private class QueryHandler extends android.content.AsyncQueryHandler {
        public QueryHandler(android.content.ContentResolver cr) {
            super(cr);
        }

        @java.lang.Override
        protected void onQueryComplete(int token, java.lang.Object cookie, android.database.Cursor cursor) {
            android.net.Uri lookupUri = null;
            android.net.Uri createUri = null;
            boolean trigger = false;
            android.os.Bundle extras = (cookie != null) ? ((android.os.Bundle) (cookie)) : new android.os.Bundle();
            try {
                switch (token) {
                    case android.widget.QuickContactBadge.TOKEN_PHONE_LOOKUP_AND_TRIGGER :
                        trigger = true;
                        createUri = android.net.Uri.fromParts("tel", extras.getString(android.widget.QuickContactBadge.EXTRA_URI_CONTENT), null);
                        // $FALL-THROUGH$
                    case android.widget.QuickContactBadge.TOKEN_PHONE_LOOKUP :
                        {
                            if ((cursor != null) && cursor.moveToFirst()) {
                                long contactId = cursor.getLong(android.widget.QuickContactBadge.PHONE_ID_COLUMN_INDEX);
                                java.lang.String lookupKey = cursor.getString(android.widget.QuickContactBadge.PHONE_LOOKUP_STRING_COLUMN_INDEX);
                                lookupUri = android.provider.ContactsContract.Contacts.getLookupUri(contactId, lookupKey);
                            }
                            break;
                        }
                    case android.widget.QuickContactBadge.TOKEN_EMAIL_LOOKUP_AND_TRIGGER :
                        trigger = true;
                        createUri = android.net.Uri.fromParts("mailto", extras.getString(android.widget.QuickContactBadge.EXTRA_URI_CONTENT), null);
                        // $FALL-THROUGH$
                    case android.widget.QuickContactBadge.TOKEN_EMAIL_LOOKUP :
                        {
                            if ((cursor != null) && cursor.moveToFirst()) {
                                long contactId = cursor.getLong(android.widget.QuickContactBadge.EMAIL_ID_COLUMN_INDEX);
                                java.lang.String lookupKey = cursor.getString(android.widget.QuickContactBadge.EMAIL_LOOKUP_STRING_COLUMN_INDEX);
                                lookupUri = android.provider.ContactsContract.Contacts.getLookupUri(contactId, lookupKey);
                            }
                            break;
                        }
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
            mContactUri = lookupUri;
            onContactUriChanged();
            if (trigger && (mContactUri != null)) {
                // Found contact, so trigger QuickContact
                android.provider.ContactsContract.QuickContact.showQuickContact(getContext(), android.widget.QuickContactBadge.this, mContactUri, mExcludeMimes, mPrioritizedMimeType);
            } else
                if (createUri != null) {
                    // Prompt user to add this person to contacts
                    final android.content.Intent intent = new android.content.Intent(android.provider.ContactsContract.Intents.SHOW_OR_CREATE_CONTACT, createUri);
                    if (extras != null) {
                        extras.remove(android.widget.QuickContactBadge.EXTRA_URI_CONTENT);
                        intent.putExtras(extras);
                    }
                    getContext().startActivity(intent);
                }

        }
    }
}

