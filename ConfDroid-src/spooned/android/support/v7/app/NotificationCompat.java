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
package android.support.v7.app;


/**
 * An extension of {@link android.support.v4.app.NotificationCompat} which supports
 * {@link android.support.v7.app.NotificationCompat.MediaStyle},
 * {@link android.support.v7.app.NotificationCompat.DecoratedCustomViewStyle},
 * and {@link android.support.v7.app.NotificationCompat.DecoratedMediaCustomViewStyle}.
 * You should start using this variant if you need support any of these styles.
 */
public class NotificationCompat extends android.support.v4.app.NotificationCompat {
    /**
     * Extracts a {@link MediaSessionCompat.Token} from the extra values
     * in the {@link MediaStyle} {@link android.app.Notification notification}.
     *
     * @param notification
     * 		The notification to extract a {@link MediaSessionCompat.Token} from.
     * @return The {@link MediaSessionCompat.Token} in the {@code notification} if it contains,
    null otherwise.
     */
    public static android.support.v4.media.session.MediaSessionCompat.Token getMediaSession(android.app.Notification notification) {
        android.os.Bundle extras = android.support.v4.app.NotificationCompat.getExtras(notification);
        if (extras != null) {
            if (android.os.Build.VERSION.SDK_INT >= 21) {
                java.lang.Object tokenInner = extras.getParcelable(android.support.v4.app.NotificationCompat.EXTRA_MEDIA_SESSION);
                if (tokenInner != null) {
                    return android.support.v4.media.session.MediaSessionCompat.Token.fromToken(tokenInner);
                }
            } else {
                android.os.IBinder tokenInner = android.support.v4.app.BundleCompat.getBinder(extras, android.support.v4.app.NotificationCompat.EXTRA_MEDIA_SESSION);
                if (tokenInner != null) {
                    android.os.Parcel p = android.os.Parcel.obtain();
                    p.writeStrongBinder(tokenInner);
                    p.setDataPosition(0);
                    android.support.v4.media.session.MediaSessionCompat.Token token = android.support.v4.media.session.MediaSessionCompat.Token.CREATOR.createFromParcel(p);
                    p.recycle();
                    return token;
                }
            }
        }
        return null;
    }

    private static void addStyleToBuilderApi24(android.support.v4.app.NotificationBuilderWithBuilderAccessor builder, android.support.v4.app.NotificationCompat.Builder b) {
        if (b.mStyle instanceof android.support.v7.app.NotificationCompat.DecoratedCustomViewStyle) {
            android.support.v7.app.NotificationCompatImpl24.addDecoratedCustomViewStyle(builder);
        } else
            if (b.mStyle instanceof android.support.v7.app.NotificationCompat.DecoratedMediaCustomViewStyle) {
                android.support.v7.app.NotificationCompatImpl24.addDecoratedMediaCustomViewStyle(builder);
            } else
                if (!(b.mStyle instanceof android.support.v4.app.NotificationCompat.MessagingStyle)) {
                    android.support.v7.app.NotificationCompat.addStyleGetContentViewLollipop(builder, b);
                }


    }

    private static android.widget.RemoteViews addStyleGetContentViewLollipop(android.support.v4.app.NotificationBuilderWithBuilderAccessor builder, android.support.v4.app.NotificationCompat.Builder b) {
        if (b.mStyle instanceof android.support.v7.app.NotificationCompat.MediaStyle) {
            android.support.v7.app.NotificationCompat.MediaStyle mediaStyle = ((android.support.v7.app.NotificationCompat.MediaStyle) (b.mStyle));
            android.support.v7.app.NotificationCompatImpl21.addMediaStyle(builder, mediaStyle.mActionsToShowInCompact, mediaStyle.mToken != null ? mediaStyle.mToken.getToken() : null);
            boolean hasContentView = b.getContentView() != null;
            // If we are on L/M the media notification will only be colored if the expanded version
            // is of media style, so we have to create a custom view for the collapsed version as
            // well in that case.
            boolean isMorL = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) && (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.M);
            boolean createCustomContent = hasContentView || (isMorL && (b.getBigContentView() != null));
            if ((b.mStyle instanceof android.support.v7.app.NotificationCompat.DecoratedMediaCustomViewStyle) && createCustomContent) {
                android.widget.RemoteViews contentViewMedia = /* no cancel button on L */
                /* cancelButtonIntent */
                /* isDecoratedCustomView */
                android.support.v7.app.NotificationCompatImplBase.overrideContentViewMedia(builder, b.mContext, b.mContentTitle, b.mContentText, b.mContentInfo, b.mNumber, b.mLargeIcon, b.mSubText, b.mUseChronometer, b.getWhenIfShowing(), b.getPriority(), b.mActions, mediaStyle.mActionsToShowInCompact, false, null, hasContentView);
                if (hasContentView) {
                    android.support.v7.app.NotificationCompatImplBase.buildIntoRemoteViews(b.mContext, contentViewMedia, b.getContentView());
                }
                android.support.v7.app.NotificationCompat.setBackgroundColor(b.mContext, contentViewMedia, b.getColor());
                return contentViewMedia;
            }
            return null;
        } else
            if (b.mStyle instanceof android.support.v7.app.NotificationCompat.DecoratedCustomViewStyle) {
                return android.support.v7.app.NotificationCompat.getDecoratedContentView(b);
            }

        return android.support.v7.app.NotificationCompat.addStyleGetContentViewJellybean(builder, b);
    }

    private static android.widget.RemoteViews addStyleGetContentViewJellybean(android.support.v4.app.NotificationBuilderWithBuilderAccessor builder, android.support.v4.app.NotificationCompat.Builder b) {
        if (b.mStyle instanceof android.support.v4.app.NotificationCompat.MessagingStyle) {
            android.support.v7.app.NotificationCompat.addMessagingFallBackStyle(((android.support.v4.app.NotificationCompat.MessagingStyle) (b.mStyle)), builder, b);
        }
        return android.support.v7.app.NotificationCompat.addStyleGetContentViewIcs(builder, b);
    }

    private static android.support.v4.app.NotificationCompat.MessagingStyle.Message findLatestIncomingMessage(android.support.v4.app.NotificationCompat.MessagingStyle style) {
        java.util.List<android.support.v4.app.NotificationCompat.MessagingStyle.Message> messages = style.getMessages();
        for (int i = messages.size() - 1; i >= 0; i--) {
            android.support.v4.app.NotificationCompat.MessagingStyle.Message m = messages.get(i);
            // Incoming messages have a non-empty sender.
            if (!android.text.TextUtils.isEmpty(m.getSender())) {
                return m;
            }
        }
        if (!messages.isEmpty()) {
            // No incoming messages, fall back to outgoing message
            return messages.get(messages.size() - 1);
        }
        return null;
    }

    private static java.lang.CharSequence makeMessageLine(android.support.v4.app.NotificationCompat.Builder b, android.support.v4.app.NotificationCompat.MessagingStyle style, android.support.v4.app.NotificationCompat.MessagingStyle.Message m) {
        android.support.v4.text.BidiFormatter bidi = android.support.v4.text.BidiFormatter.getInstance();
        android.text.SpannableStringBuilder sb = new android.text.SpannableStringBuilder();
        boolean afterLollipop = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
        int color = (afterLollipop || (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.GINGERBREAD_MR1)) ? android.graphics.Color.BLACK : android.graphics.Color.WHITE;
        java.lang.CharSequence replyName = m.getSender();
        if (android.text.TextUtils.isEmpty(m.getSender())) {
            replyName = (style.getUserDisplayName() == null) ? "" : style.getUserDisplayName();
            color = (afterLollipop && (b.getColor() != android.support.v7.app.NotificationCompat.COLOR_DEFAULT)) ? b.getColor() : color;
        }
        java.lang.CharSequence senderText = bidi.unicodeWrap(replyName);
        sb.append(senderText);
        /* flags */
        sb.setSpan(android.support.v7.app.NotificationCompat.makeFontColorSpan(color), sb.length() - senderText.length(), sb.length(), android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        java.lang.CharSequence text = (m.getText() == null) ? "" : m.getText();
        sb.append("  ").append(bidi.unicodeWrap(text));
        return sb;
    }

    private static android.text.style.TextAppearanceSpan makeFontColorSpan(int color) {
        return new android.text.style.TextAppearanceSpan(null, 0, 0, android.content.res.ColorStateList.valueOf(color), null);
    }

    private static void addMessagingFallBackStyle(android.support.v4.app.NotificationCompat.MessagingStyle style, android.support.v4.app.NotificationBuilderWithBuilderAccessor builder, android.support.v4.app.NotificationCompat.Builder b) {
        android.text.SpannableStringBuilder completeMessage = new android.text.SpannableStringBuilder();
        java.util.List<android.support.v4.app.NotificationCompat.MessagingStyle.Message> messages = style.getMessages();
        boolean showNames = (style.getConversationTitle() != null) || android.support.v7.app.NotificationCompat.hasMessagesWithoutSender(style.getMessages());
        for (int i = messages.size() - 1; i >= 0; i--) {
            android.support.v4.app.NotificationCompat.MessagingStyle.Message m = messages.get(i);
            java.lang.CharSequence line;
            line = (showNames) ? android.support.v7.app.NotificationCompat.makeMessageLine(b, style, m) : m.getText();
            if (i != (messages.size() - 1)) {
                completeMessage.insert(0, "\n");
            }
            completeMessage.insert(0, line);
        }
        android.support.v7.app.NotificationCompatImplJellybean.addBigTextStyle(builder, completeMessage);
    }

    private static boolean hasMessagesWithoutSender(java.util.List<android.support.v4.app.NotificationCompat.MessagingStyle.Message> messages) {
        for (int i = messages.size() - 1; i >= 0; i--) {
            android.support.v4.app.NotificationCompat.MessagingStyle.Message m = messages.get(i);
            if (m.getSender() == null) {
                return true;
            }
        }
        return false;
    }

    private static android.widget.RemoteViews addStyleGetContentViewIcs(android.support.v4.app.NotificationBuilderWithBuilderAccessor builder, android.support.v4.app.NotificationCompat.Builder b) {
        if (b.mStyle instanceof android.support.v7.app.NotificationCompat.MediaStyle) {
            android.support.v7.app.NotificationCompat.MediaStyle mediaStyle = ((android.support.v7.app.NotificationCompat.MediaStyle) (b.mStyle));
            boolean isDecorated = (b.mStyle instanceof android.support.v7.app.NotificationCompat.DecoratedMediaCustomViewStyle) && (b.getContentView() != null);
            android.widget.RemoteViews contentViewMedia = android.support.v7.app.NotificationCompatImplBase.overrideContentViewMedia(builder, b.mContext, b.mContentTitle, b.mContentText, b.mContentInfo, b.mNumber, b.mLargeIcon, b.mSubText, b.mUseChronometer, b.getWhenIfShowing(), b.getPriority(), b.mActions, mediaStyle.mActionsToShowInCompact, mediaStyle.mShowCancelButton, mediaStyle.mCancelButtonIntent, isDecorated);
            if (isDecorated) {
                android.support.v7.app.NotificationCompatImplBase.buildIntoRemoteViews(b.mContext, contentViewMedia, b.getContentView());
                return contentViewMedia;
            }
        } else
            if (b.mStyle instanceof android.support.v7.app.NotificationCompat.DecoratedCustomViewStyle) {
                return android.support.v7.app.NotificationCompat.getDecoratedContentView(b);
            }

        return null;
    }

    private static void addBigStyleToBuilderJellybean(android.app.Notification n, android.support.v4.app.NotificationCompat.Builder b) {
        if (b.mStyle instanceof android.support.v7.app.NotificationCompat.MediaStyle) {
            android.support.v7.app.NotificationCompat.MediaStyle mediaStyle = ((android.support.v7.app.NotificationCompat.MediaStyle) (b.mStyle));
            android.widget.RemoteViews innerView = (b.getBigContentView() != null) ? b.getBigContentView() : b.getContentView();
            boolean isDecorated = (b.mStyle instanceof android.support.v7.app.NotificationCompat.DecoratedMediaCustomViewStyle) && (innerView != null);
            android.support.v7.app.NotificationCompatImplBase.overrideMediaBigContentView(n, b.mContext, b.mContentTitle, b.mContentText, b.mContentInfo, b.mNumber, b.mLargeIcon, b.mSubText, b.mUseChronometer, b.getWhenIfShowing(), b.getPriority(), 0, b.mActions, mediaStyle.mShowCancelButton, mediaStyle.mCancelButtonIntent, isDecorated);
            if (isDecorated) {
                android.support.v7.app.NotificationCompatImplBase.buildIntoRemoteViews(b.mContext, n.bigContentView, innerView);
            }
        } else
            if (b.mStyle instanceof android.support.v7.app.NotificationCompat.DecoratedCustomViewStyle) {
                android.support.v7.app.NotificationCompat.addDecoratedBigStyleToBuilder(n, b);
            }

    }

    private static android.widget.RemoteViews getDecoratedContentView(android.support.v4.app.NotificationCompat.Builder b) {
        if (b.getContentView() == null) {
            // No special content view
            return null;
        }
        android.widget.RemoteViews remoteViews = /* fitIn1U */
        /* actions */
        android.support.v7.app.NotificationCompatImplBase.applyStandardTemplateWithActions(b.mContext, b.mContentTitle, b.mContentText, b.mContentInfo, b.mNumber, b.mNotification.icon, b.mLargeIcon, b.mSubText, b.mUseChronometer, b.getWhenIfShowing(), b.getPriority(), b.getColor(), R.layout.notification_template_custom_big, false, null);
        android.support.v7.app.NotificationCompatImplBase.buildIntoRemoteViews(b.mContext, remoteViews, b.getContentView());
        return remoteViews;
    }

    private static void addDecoratedBigStyleToBuilder(android.app.Notification n, android.support.v4.app.NotificationCompat.Builder b) {
        android.widget.RemoteViews bigContentView = b.getBigContentView();
        android.widget.RemoteViews innerView = (bigContentView != null) ? bigContentView : b.getContentView();
        if (innerView == null) {
            // No expandable notification
            return;
        }
        android.widget.RemoteViews remoteViews = /* fitIn1U */
        android.support.v7.app.NotificationCompatImplBase.applyStandardTemplateWithActions(b.mContext, b.mContentTitle, b.mContentText, b.mContentInfo, b.mNumber, n.icon, b.mLargeIcon, b.mSubText, b.mUseChronometer, b.getWhenIfShowing(), b.getPriority(), b.getColor(), R.layout.notification_template_custom_big, false, b.mActions);
        android.support.v7.app.NotificationCompatImplBase.buildIntoRemoteViews(b.mContext, remoteViews, innerView);
        n.bigContentView = remoteViews;
    }

    private static void addDecoratedHeadsUpToBuilder(android.app.Notification n, android.support.v4.app.NotificationCompat.Builder b) {
        android.widget.RemoteViews headsUp = b.getHeadsUpContentView();
        android.widget.RemoteViews innerView = (headsUp != null) ? headsUp : b.getContentView();
        if (headsUp == null) {
            // No expandable notification
            return;
        }
        android.widget.RemoteViews remoteViews = /* fitIn1U */
        android.support.v7.app.NotificationCompatImplBase.applyStandardTemplateWithActions(b.mContext, b.mContentTitle, b.mContentText, b.mContentInfo, b.mNumber, n.icon, b.mLargeIcon, b.mSubText, b.mUseChronometer, b.getWhenIfShowing(), b.getPriority(), b.getColor(), R.layout.notification_template_custom_big, false, b.mActions);
        android.support.v7.app.NotificationCompatImplBase.buildIntoRemoteViews(b.mContext, remoteViews, innerView);
        n.headsUpContentView = remoteViews;
    }

    private static void addBigStyleToBuilderLollipop(android.app.Notification n, android.support.v4.app.NotificationCompat.Builder b) {
        android.widget.RemoteViews innerView = (b.getBigContentView() != null) ? b.getBigContentView() : b.getContentView();
        if ((b.mStyle instanceof android.support.v7.app.NotificationCompat.DecoratedMediaCustomViewStyle) && (innerView != null)) {
            /* showCancelButton */
            /* cancelButtonIntent */
            /* decoratedCustomView */
            android.support.v7.app.NotificationCompatImplBase.overrideMediaBigContentView(n, b.mContext, b.mContentTitle, b.mContentText, b.mContentInfo, b.mNumber, b.mLargeIcon, b.mSubText, b.mUseChronometer, b.getWhenIfShowing(), b.getPriority(), 0, b.mActions, false, null, true);
            android.support.v7.app.NotificationCompatImplBase.buildIntoRemoteViews(b.mContext, n.bigContentView, innerView);
            android.support.v7.app.NotificationCompat.setBackgroundColor(b.mContext, n.bigContentView, b.getColor());
        } else
            if (b.mStyle instanceof android.support.v7.app.NotificationCompat.DecoratedCustomViewStyle) {
                android.support.v7.app.NotificationCompat.addDecoratedBigStyleToBuilder(n, b);
            }

    }

    private static void setBackgroundColor(android.content.Context context, android.widget.RemoteViews views, int color) {
        if (color == android.support.v4.app.NotificationCompat.COLOR_DEFAULT) {
            color = context.getResources().getColor(R.color.notification_material_background_media_default_color);
        }
        views.setInt(R.id.status_bar_latest_event_content, "setBackgroundColor", color);
    }

    private static void addHeadsUpToBuilderLollipop(android.app.Notification n, android.support.v4.app.NotificationCompat.Builder b) {
        android.widget.RemoteViews innerView = (b.getHeadsUpContentView() != null) ? b.getHeadsUpContentView() : b.getContentView();
        if ((b.mStyle instanceof android.support.v7.app.NotificationCompat.DecoratedMediaCustomViewStyle) && (innerView != null)) {
            n.headsUpContentView = /* showCancelButton */
            /* cancelButtonIntent */
            /* decoratedCustomView */
            android.support.v7.app.NotificationCompatImplBase.generateMediaBigView(b.mContext, b.mContentTitle, b.mContentText, b.mContentInfo, b.mNumber, b.mLargeIcon, b.mSubText, b.mUseChronometer, b.getWhenIfShowing(), b.getPriority(), 0, b.mActions, false, null, true);
            android.support.v7.app.NotificationCompatImplBase.buildIntoRemoteViews(b.mContext, n.headsUpContentView, innerView);
            android.support.v7.app.NotificationCompat.setBackgroundColor(b.mContext, n.headsUpContentView, b.getColor());
        } else
            if (b.mStyle instanceof android.support.v7.app.NotificationCompat.DecoratedCustomViewStyle) {
                android.support.v7.app.NotificationCompat.addDecoratedHeadsUpToBuilder(n, b);
            }

    }

    /**
     * See {@link android.support.v4.app.NotificationCompat}. In addition to the builder in v4, this
     * builder also supports {@link MediaStyle}.
     */
    public static class Builder extends android.support.v4.app.NotificationCompat.Builder {
        /**
         *
         *
         * @unknown 
         */
        public Builder(android.content.Context context) {
            super(context);
        }

        /**
         *
         *
         * @return the text of the notification
         * @unknown 
         */
        @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
        @java.lang.Override
        protected java.lang.CharSequence resolveText() {
            if (mStyle instanceof android.support.v4.app.NotificationCompat.MessagingStyle) {
                android.support.v4.app.NotificationCompat.MessagingStyle style = ((android.support.v4.app.NotificationCompat.MessagingStyle) (mStyle));
                android.support.v4.app.NotificationCompat.MessagingStyle.Message m = android.support.v7.app.NotificationCompat.findLatestIncomingMessage(style);
                java.lang.CharSequence conversationTitle = style.getConversationTitle();
                if (m != null) {
                    return conversationTitle != null ? android.support.v7.app.NotificationCompat.makeMessageLine(this, style, m) : m.getText();
                }
            }
            return super.resolveText();
        }

        /**
         *
         *
         * @return the title of the notification
         * @unknown 
         */
        @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
        @java.lang.Override
        protected java.lang.CharSequence resolveTitle() {
            if (mStyle instanceof android.support.v4.app.NotificationCompat.MessagingStyle) {
                android.support.v4.app.NotificationCompat.MessagingStyle style = ((android.support.v4.app.NotificationCompat.MessagingStyle) (mStyle));
                android.support.v4.app.NotificationCompat.MessagingStyle.Message m = android.support.v7.app.NotificationCompat.findLatestIncomingMessage(style);
                java.lang.CharSequence conversationTitle = style.getConversationTitle();
                if ((conversationTitle != null) || (m != null)) {
                    return conversationTitle != null ? conversationTitle : m.getSender();
                }
            }
            return super.resolveTitle();
        }

        /**
         *
         *
         * @unknown 
         */
        @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
        @java.lang.Override
        protected android.support.v4.app.NotificationCompat.BuilderExtender getExtender() {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                return new android.support.v7.app.NotificationCompat.Api24Extender();
            } else
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    return new android.support.v7.app.NotificationCompat.LollipopExtender();
                } else
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        return new android.support.v7.app.NotificationCompat.JellybeanExtender();
                    } else
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                            return new android.support.v7.app.NotificationCompat.IceCreamSandwichExtender();
                        } else {
                            return super.getExtender();
                        }



        }
    }

    private static class IceCreamSandwichExtender extends android.support.v4.app.NotificationCompat.BuilderExtender {
        IceCreamSandwichExtender() {
        }

        @java.lang.Override
        public android.app.Notification build(android.support.v4.app.NotificationCompat.Builder b, android.support.v4.app.NotificationBuilderWithBuilderAccessor builder) {
            android.widget.RemoteViews contentView = android.support.v7.app.NotificationCompat.addStyleGetContentViewIcs(builder, b);
            android.app.Notification n = builder.build();
            // The above call might override decorated content views again, let's make sure it
            // sticks.
            if (contentView != null) {
                n.contentView = contentView;
            } else
                if (b.getContentView() != null) {
                    n.contentView = b.getContentView();
                }

            return n;
        }
    }

    private static class JellybeanExtender extends android.support.v4.app.NotificationCompat.BuilderExtender {
        JellybeanExtender() {
        }

        @java.lang.Override
        public android.app.Notification build(android.support.v4.app.NotificationCompat.Builder b, android.support.v4.app.NotificationBuilderWithBuilderAccessor builder) {
            android.widget.RemoteViews contentView = android.support.v7.app.NotificationCompat.addStyleGetContentViewJellybean(builder, b);
            android.app.Notification n = builder.build();
            // The above call might override decorated content views again, let's make sure it
            // sticks.
            if (contentView != null) {
                n.contentView = contentView;
            }
            android.support.v7.app.NotificationCompat.addBigStyleToBuilderJellybean(n, b);
            return n;
        }
    }

    private static class LollipopExtender extends android.support.v4.app.NotificationCompat.BuilderExtender {
        LollipopExtender() {
        }

        @java.lang.Override
        public android.app.Notification build(android.support.v4.app.NotificationCompat.Builder b, android.support.v4.app.NotificationBuilderWithBuilderAccessor builder) {
            android.widget.RemoteViews contentView = android.support.v7.app.NotificationCompat.addStyleGetContentViewLollipop(builder, b);
            android.app.Notification n = builder.build();
            // The above call might override decorated content views again, let's make sure it
            // sticks.
            if (contentView != null) {
                n.contentView = contentView;
            }
            android.support.v7.app.NotificationCompat.addBigStyleToBuilderLollipop(n, b);
            android.support.v7.app.NotificationCompat.addHeadsUpToBuilderLollipop(n, b);
            return n;
        }
    }

    private static class Api24Extender extends android.support.v4.app.NotificationCompat.BuilderExtender {
        @java.lang.Override
        public android.app.Notification build(android.support.v4.app.NotificationCompat.Builder b, android.support.v4.app.NotificationBuilderWithBuilderAccessor builder) {
            android.support.v7.app.NotificationCompat.addStyleToBuilderApi24(builder, b);
            return builder.build();
        }
    }

    /**
     * Notification style for media playback notifications.
     *
     * In the expanded form, {@link Notification#bigContentView}, up to 5
     * {@link android.support.v4.app.NotificationCompat.Action}s specified with
     * {@link NotificationCompat.Builder#addAction(int, CharSequence, PendingIntent) addAction} will
     * be shown as icon-only pushbuttons, suitable for transport controls. The Bitmap given to
     * {@link NotificationCompat.Builder#setLargeIcon(android.graphics.Bitmap) setLargeIcon()} will
     * be treated as album artwork.
     *
     * Unlike the other styles provided here, MediaStyle can also modify the standard-size
     * {@link Notification#contentView}; by providing action indices to
     * {@link #setShowActionsInCompactView(int...)} you can promote up to 3 actions to be displayed
     * in the standard view alongside the usual content.
     *
     * Notifications created with MediaStyle will have their category set to
     * {@link Notification#CATEGORY_TRANSPORT CATEGORY_TRANSPORT} unless you set a different
     * category using {@link NotificationCompat.Builder#setCategory(String) setCategory()}.
     *
     * Finally, if you attach a {@link android.media.session.MediaSession.Token} using
     * {@link android.support.v7.app.NotificationCompat.MediaStyle#setMediaSession}, the System UI
     * can identify this as a notification representing an active media session and respond
     * accordingly (by showing album artwork in the lockscreen, for example).
     *
     * To use this style with your Notification, feed it to
     * {@link NotificationCompat.Builder#setStyle} like so:
     * <pre class="prettyprint">
     * Notification noti = new NotificationCompat.Builder()
     *     .setSmallIcon(R.drawable.ic_stat_player)
     *     .setContentTitle(&quot;Track title&quot;)
     *     .setContentText(&quot;Artist - Album&quot;)
     *     .setLargeIcon(albumArtBitmap))
     *     .setStyle(<b>new NotificationCompat.MediaStyle()</b>
     *         .setMediaSession(mySession))
     *     .build();
     * </pre>
     *
     * @see Notification#bigContentView
     */
    public static class MediaStyle extends android.support.v4.app.NotificationCompat.Style {
        int[] mActionsToShowInCompact = null;

        android.support.v4.media.session.MediaSessionCompat.Token mToken;

        boolean mShowCancelButton;

        android.app.PendingIntent mCancelButtonIntent;

        public MediaStyle() {
        }

        public MediaStyle(android.support.v4.app.NotificationCompat.Builder builder) {
            setBuilder(builder);
        }

        /**
         * Requests up to 3 actions (by index in the order of addition) to be shown in the compact
         * notification view.
         *
         * @param actions
         * 		the indices of the actions to show in the compact notification view
         */
        public android.support.v7.app.NotificationCompat.MediaStyle setShowActionsInCompactView(int... actions) {
            mActionsToShowInCompact = actions;
            return this;
        }

        /**
         * Attaches a {@link MediaSessionCompat.Token} to this Notification
         * to provide additional playback information and control to the SystemUI.
         */
        public android.support.v7.app.NotificationCompat.MediaStyle setMediaSession(android.support.v4.media.session.MediaSessionCompat.Token token) {
            mToken = token;
            return this;
        }

        /**
         * Sets whether a cancel button at the top right should be shown in the notification on
         * platforms before Lollipop.
         *
         * <p>Prior to Lollipop, there was a bug in the framework which prevented the developer to
         * make a notification dismissable again after having used the same notification as the
         * ongoing notification for a foreground service. When the notification was posted by
         * {@link android.app.Service#startForeground}, but then the service exited foreground mode
         * via {@link android.app.Service#stopForeground}, without removing the notification, the
         * notification stayed ongoing, and thus not dismissable.
         *
         * <p>This is a common scenario for media notifications, as this is exactly the service
         * lifecycle that happens when playing/pausing media. Thus, a workaround is provided by the
         * support library: Instead of making the notification ongoing depending on the playback
         * state, the support library provides the ability to add an explicit cancel button to the
         * notification.
         *
         * <p>Note that the notification is enforced to be ongoing if a cancel button is shown to
         * provide a consistent user experience.
         *
         * <p>Also note that this method is a no-op when running on Lollipop and later.
         *
         * @param show
         * 		whether to show a cancel button
         */
        public android.support.v7.app.NotificationCompat.MediaStyle setShowCancelButton(boolean show) {
            mShowCancelButton = show;
            return this;
        }

        /**
         * Sets the pending intent to be sent when the cancel button is pressed. See {@link #setShowCancelButton}.
         *
         * @param pendingIntent
         * 		the intent to be sent when the cancel button is pressed
         */
        public android.support.v7.app.NotificationCompat.MediaStyle setCancelButtonIntent(android.app.PendingIntent pendingIntent) {
            mCancelButtonIntent = pendingIntent;
            return this;
        }
    }

    /**
     * Notification style for custom views that are decorated by the system.
     *
     * <p>Instead of providing a notification that is completely custom, a developer can set this
     * style and still obtain system decorations like the notification header with the expand
     * affordance and actions.
     *
     * <p>Use {@link android.app.Notification.Builder#setCustomContentView(RemoteViews)},
     * {@link android.app.Notification.Builder#setCustomBigContentView(RemoteViews)} and
     * {@link android.app.Notification.Builder#setCustomHeadsUpContentView(RemoteViews)} to set the
     * corresponding custom views to display.
     *
     * <p>To use this style with your Notification, feed it to
     * {@link NotificationCompat.Builder#setStyle(Style)} like so:
     * <pre class="prettyprint">
     * Notification noti = new NotificationCompat.Builder()
     *     .setSmallIcon(R.drawable.ic_stat_player)
     *     .setLargeIcon(albumArtBitmap))
     *     .setCustomContentView(contentView);
     *     .setStyle(<b>new NotificationCompat.DecoratedCustomViewStyle()</b>)
     *     .build();
     * </pre>
     *
     * <p>If you are using this style, consider using the corresponding styles like
     * {@link android.support.v7.appcompat.R.style#TextAppearance_AppCompat_Notification} or
     * {@link android.support.v7.appcompat.R.style#TextAppearance_AppCompat_Notification_Title} in
     * your custom views in order to get the correct styling on each platform version.
     */
    public static class DecoratedCustomViewStyle extends android.support.v4.app.NotificationCompat.Style {
        public DecoratedCustomViewStyle() {
        }
    }

    /**
     * Notification style for media custom views that are decorated by the system.
     *
     * <p>Instead of providing a media notification that is completely custom, a developer can set
     * this style and still obtain system decorations like the notification header with the expand
     * affordance and actions.
     *
     * <p>Use {@link android.app.Notification.Builder#setCustomContentView(RemoteViews)},
     * {@link android.app.Notification.Builder#setCustomBigContentView(RemoteViews)} and
     * {@link android.app.Notification.Builder#setCustomHeadsUpContentView(RemoteViews)} to set the
     * corresponding custom views to display.
     *
     * <p>To use this style with your Notification, feed it to
     * {@link NotificationCompat.Builder#setStyle(Style)} like so:
     * <pre class="prettyprint">
     * Notification noti = new Notification.Builder()
     *     .setSmallIcon(R.drawable.ic_stat_player)
     *     .setLargeIcon(albumArtBitmap))
     *     .setCustomContentView(contentView);
     *     .setStyle(<b>new NotificationCompat.DecoratedMediaCustomViewStyle()</b>
     *          .setMediaSession(mySession))
     *     .build();
     * </pre>
     *
     * <p>If you are using this style, consider using the corresponding styles like
     * {@link android.support.v7.appcompat.R.style#TextAppearance_AppCompat_Notification_Media} or
     * {@link android.support.v7.appcompat.R.style#TextAppearance_AppCompat_Notification_Title_Media} in
     * your custom views in order to get the correct styling on each platform version.
     *
     * @see DecoratedCustomViewStyle
     * @see MediaStyle
     */
    public static class DecoratedMediaCustomViewStyle extends android.support.v7.app.NotificationCompat.MediaStyle {
        public DecoratedMediaCustomViewStyle() {
        }
    }
}

