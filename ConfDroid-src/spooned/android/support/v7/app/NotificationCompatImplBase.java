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
 * limitations under the License
 */
package android.support.v7.app;


/**
 * Helper class to generate MediaStyle notifications for pre-Lollipop platforms. Overrides
 * contentView and bigContentView of the notification.
 */
class NotificationCompatImplBase {
    static final int MAX_MEDIA_BUTTONS_IN_COMPACT = 3;

    static final int MAX_MEDIA_BUTTONS = 5;

    private static final int MAX_ACTION_BUTTONS = 3;

    public static <T extends android.support.v4.app.NotificationCompatBase.Action> android.widget.RemoteViews overrideContentViewMedia(android.support.v4.app.NotificationBuilderWithBuilderAccessor builder, android.content.Context context, java.lang.CharSequence contentTitle, java.lang.CharSequence contentText, java.lang.CharSequence contentInfo, int number, android.graphics.Bitmap largeIcon, java.lang.CharSequence subText, boolean useChronometer, long when, int priority, java.util.List<T> actions, int[] actionsToShowInCompact, boolean showCancelButton, android.app.PendingIntent cancelButtonIntent, boolean isDecoratedCustomView) {
        android.widget.RemoteViews views = android.support.v7.app.NotificationCompatImplBase.generateContentViewMedia(context, contentTitle, contentText, contentInfo, number, largeIcon, subText, useChronometer, when, priority, actions, actionsToShowInCompact, showCancelButton, cancelButtonIntent, isDecoratedCustomView);
        builder.getBuilder().setContent(views);
        if (showCancelButton) {
            builder.getBuilder().setOngoing(true);
        }
        return views;
    }

    private static <T extends android.support.v4.app.NotificationCompatBase.Action> android.widget.RemoteViews generateContentViewMedia(android.content.Context context, java.lang.CharSequence contentTitle, java.lang.CharSequence contentText, java.lang.CharSequence contentInfo, int number, android.graphics.Bitmap largeIcon, java.lang.CharSequence subText, boolean useChronometer, long when, int priority, java.util.List<T> actions, int[] actionsToShowInCompact, boolean showCancelButton, android.app.PendingIntent cancelButtonIntent, boolean isDecoratedCustomView) {
        android.widget.RemoteViews view = /* smallIcon */
        /* color is unused on media */
        /* fitIn1U */
        android.support.v7.app.NotificationCompatImplBase.applyStandardTemplate(context, contentTitle, contentText, contentInfo, number, 0, largeIcon, subText, useChronometer, when, priority, 0, isDecoratedCustomView ? R.layout.notification_template_media_custom : R.layout.notification_template_media, true);
        final int numActions = actions.size();
        final int N = (actionsToShowInCompact == null) ? 0 : java.lang.Math.min(actionsToShowInCompact.length, android.support.v7.app.NotificationCompatImplBase.MAX_MEDIA_BUTTONS_IN_COMPACT);
        view.removeAllViews(R.id.media_actions);
        if (N > 0) {
            for (int i = 0; i < N; i++) {
                if (i >= numActions) {
                    throw new java.lang.IllegalArgumentException(java.lang.String.format("setShowActionsInCompactView: action %d out of bounds (max %d)", i, numActions - 1));
                }
                final android.support.v4.app.NotificationCompatBase.Action action = actions.get(actionsToShowInCompact[i]);
                final android.widget.RemoteViews button = android.support.v7.app.NotificationCompatImplBase.generateMediaActionButton(context, action);
                view.addView(R.id.media_actions, button);
            }
        }
        if (showCancelButton) {
            view.setViewVisibility(R.id.end_padder, android.view.View.GONE);
            view.setViewVisibility(R.id.cancel_action, android.view.View.VISIBLE);
            view.setOnClickPendingIntent(R.id.cancel_action, cancelButtonIntent);
            view.setInt(R.id.cancel_action, "setAlpha", context.getResources().getInteger(R.integer.cancel_button_image_alpha));
        } else {
            view.setViewVisibility(R.id.end_padder, android.view.View.VISIBLE);
            view.setViewVisibility(R.id.cancel_action, android.view.View.GONE);
        }
        return view;
    }

    public static <T extends android.support.v4.app.NotificationCompatBase.Action> void overrideMediaBigContentView(android.app.Notification n, android.content.Context context, java.lang.CharSequence contentTitle, java.lang.CharSequence contentText, java.lang.CharSequence contentInfo, int number, android.graphics.Bitmap largeIcon, java.lang.CharSequence subText, boolean useChronometer, long when, int priority, int color, java.util.List<T> actions, boolean showCancelButton, android.app.PendingIntent cancelButtonIntent, boolean decoratedCustomView) {
        n.bigContentView = android.support.v7.app.NotificationCompatImplBase.generateMediaBigView(context, contentTitle, contentText, contentInfo, number, largeIcon, subText, useChronometer, when, priority, color, actions, showCancelButton, cancelButtonIntent, decoratedCustomView);
        if (showCancelButton) {
            n.flags |= android.app.Notification.FLAG_ONGOING_EVENT;
        }
    }

    public static <T extends android.support.v4.app.NotificationCompatBase.Action> android.widget.RemoteViews generateMediaBigView(android.content.Context context, java.lang.CharSequence contentTitle, java.lang.CharSequence contentText, java.lang.CharSequence contentInfo, int number, android.graphics.Bitmap largeIcon, java.lang.CharSequence subText, boolean useChronometer, long when, int priority, int color, java.util.List<T> actions, boolean showCancelButton, android.app.PendingIntent cancelButtonIntent, boolean decoratedCustomView) {
        final int actionCount = java.lang.Math.min(actions.size(), android.support.v7.app.NotificationCompatImplBase.MAX_MEDIA_BUTTONS);
        android.widget.RemoteViews big = /* smallIcon */
        /* fitIn1U */
        android.support.v7.app.NotificationCompatImplBase.applyStandardTemplate(context, contentTitle, contentText, contentInfo, number, 0, largeIcon, subText, useChronometer, when, priority, color, android.support.v7.app.NotificationCompatImplBase.getBigMediaLayoutResource(decoratedCustomView, actionCount), false);
        big.removeAllViews(R.id.media_actions);
        if (actionCount > 0) {
            for (int i = 0; i < actionCount; i++) {
                final android.widget.RemoteViews button = android.support.v7.app.NotificationCompatImplBase.generateMediaActionButton(context, actions.get(i));
                big.addView(R.id.media_actions, button);
            }
        }
        if (showCancelButton) {
            big.setViewVisibility(R.id.cancel_action, android.view.View.VISIBLE);
            big.setInt(R.id.cancel_action, "setAlpha", context.getResources().getInteger(R.integer.cancel_button_image_alpha));
            big.setOnClickPendingIntent(R.id.cancel_action, cancelButtonIntent);
        } else {
            big.setViewVisibility(R.id.cancel_action, android.view.View.GONE);
        }
        return big;
    }

    private static android.widget.RemoteViews generateMediaActionButton(android.content.Context context, android.support.v4.app.NotificationCompatBase.Action action) {
        final boolean tombstone = action.getActionIntent() == null;
        android.widget.RemoteViews button = new android.widget.RemoteViews(context.getPackageName(), R.layout.notification_media_action);
        button.setImageViewResource(R.id.action0, action.getIcon());
        if (!tombstone) {
            button.setOnClickPendingIntent(R.id.action0, action.getActionIntent());
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            button.setContentDescription(R.id.action0, action.getTitle());
        }
        return button;
    }

    private static int getBigMediaLayoutResource(boolean decoratedCustomView, int actionCount) {
        if (actionCount <= 3) {
            return decoratedCustomView ? R.layout.notification_template_big_media_narrow_custom : R.layout.notification_template_big_media_narrow;
        } else {
            return decoratedCustomView ? R.layout.notification_template_big_media_custom : R.layout.notification_template_big_media;
        }
    }

    public static android.widget.RemoteViews applyStandardTemplateWithActions(android.content.Context context, java.lang.CharSequence contentTitle, java.lang.CharSequence contentText, java.lang.CharSequence contentInfo, int number, int smallIcon, android.graphics.Bitmap largeIcon, java.lang.CharSequence subText, boolean useChronometer, long when, int priority, int color, int resId, boolean fitIn1U, java.util.ArrayList<android.support.v4.app.NotificationCompat.Action> actions) {
        android.widget.RemoteViews remoteViews = android.support.v7.app.NotificationCompatImplBase.applyStandardTemplate(context, contentTitle, contentText, contentInfo, number, smallIcon, largeIcon, subText, useChronometer, when, priority, color, resId, fitIn1U);
        remoteViews.removeAllViews(R.id.actions);
        boolean actionsVisible = false;
        if (actions != null) {
            int N = actions.size();
            if (N > 0) {
                actionsVisible = true;
                if (N > android.support.v7.app.NotificationCompatImplBase.MAX_ACTION_BUTTONS)
                    N = android.support.v7.app.NotificationCompatImplBase.MAX_ACTION_BUTTONS;

                for (int i = 0; i < N; i++) {
                    final android.widget.RemoteViews button = android.support.v7.app.NotificationCompatImplBase.generateActionButton(context, actions.get(i));
                    remoteViews.addView(R.id.actions, button);
                }
            }
        }
        int actionVisibility = (actionsVisible) ? android.view.View.VISIBLE : android.view.View.GONE;
        remoteViews.setViewVisibility(R.id.actions, actionVisibility);
        remoteViews.setViewVisibility(R.id.action_divider, actionVisibility);
        return remoteViews;
    }

    private static android.widget.RemoteViews generateActionButton(android.content.Context context, android.support.v4.app.NotificationCompat.Action action) {
        final boolean tombstone = action.actionIntent == null;
        android.widget.RemoteViews button = new android.widget.RemoteViews(context.getPackageName(), tombstone ? android.support.v7.app.NotificationCompatImplBase.getActionTombstoneLayoutResource() : android.support.v7.app.NotificationCompatImplBase.getActionLayoutResource());
        button.setImageViewBitmap(R.id.action_image, android.support.v7.app.NotificationCompatImplBase.createColoredBitmap(context, action.getIcon(), context.getResources().getColor(R.color.notification_action_color_filter)));
        button.setTextViewText(R.id.action_text, action.title);
        if (!tombstone) {
            button.setOnClickPendingIntent(R.id.action_container, action.actionIntent);
        }
        button.setContentDescription(R.id.action_container, action.title);
        return button;
    }

    private static android.graphics.Bitmap createColoredBitmap(android.content.Context context, int iconId, int color) {
        return android.support.v7.app.NotificationCompatImplBase.createColoredBitmap(context, iconId, color, 0);
    }

    private static android.graphics.Bitmap createColoredBitmap(android.content.Context context, int iconId, int color, int size) {
        android.graphics.drawable.Drawable drawable = context.getResources().getDrawable(iconId);
        int width = (size == 0) ? drawable.getIntrinsicWidth() : size;
        int height = (size == 0) ? drawable.getIntrinsicHeight() : size;
        android.graphics.Bitmap resultBitmap = android.graphics.Bitmap.createBitmap(width, height, android.graphics.Bitmap.Config.ARGB_8888);
        drawable.setBounds(0, 0, width, height);
        if (color != 0) {
            drawable.mutate().setColorFilter(new android.graphics.PorterDuffColorFilter(color, android.graphics.PorterDuff.Mode.SRC_IN));
        }
        android.graphics.Canvas canvas = new android.graphics.Canvas(resultBitmap);
        drawable.draw(canvas);
        return resultBitmap;
    }

    private static int getActionLayoutResource() {
        return R.layout.notification_action;
    }

    private static int getActionTombstoneLayoutResource() {
        return R.layout.notification_action_tombstone;
    }

    public static android.widget.RemoteViews applyStandardTemplate(android.content.Context context, java.lang.CharSequence contentTitle, java.lang.CharSequence contentText, java.lang.CharSequence contentInfo, int number, int smallIcon, android.graphics.Bitmap largeIcon, java.lang.CharSequence subText, boolean useChronometer, long when, int priority, int color, int resId, boolean fitIn1U) {
        android.content.res.Resources res = context.getResources();
        android.widget.RemoteViews contentView = new android.widget.RemoteViews(context.getPackageName(), resId);
        boolean showLine3 = false;
        boolean showLine2 = false;
        boolean minPriority = priority < android.support.v4.app.NotificationCompat.PRIORITY_LOW;
        boolean afterJellyBean = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN;
        boolean afterLollipop = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
        if (afterJellyBean && (!afterLollipop)) {
            // lets color the backgrounds
            if (minPriority) {
                contentView.setInt(R.id.notification_background, "setBackgroundResource", R.drawable.notification_bg_low);
                contentView.setInt(R.id.icon, "setBackgroundResource", R.drawable.notification_template_icon_low_bg);
            } else {
                contentView.setInt(R.id.notification_background, "setBackgroundResource", R.drawable.notification_bg);
                contentView.setInt(R.id.icon, "setBackgroundResource", R.drawable.notification_template_icon_bg);
            }
        }
        if (largeIcon != null) {
            // On versions before Jellybean, the large icon was shown by SystemUI, so we need to hide
            // it here.
            if (afterJellyBean) {
                contentView.setViewVisibility(R.id.icon, android.view.View.VISIBLE);
                contentView.setImageViewBitmap(R.id.icon, largeIcon);
            } else {
                contentView.setViewVisibility(R.id.icon, android.view.View.GONE);
            }
            if (smallIcon != 0) {
                int backgroundSize = res.getDimensionPixelSize(R.dimen.notification_right_icon_size);
                int iconSize = backgroundSize - (res.getDimensionPixelSize(R.dimen.notification_small_icon_background_padding) * 2);
                if (afterLollipop) {
                    android.graphics.Bitmap smallBit = android.support.v7.app.NotificationCompatImplBase.createIconWithBackground(context, smallIcon, backgroundSize, iconSize, color);
                    contentView.setImageViewBitmap(R.id.right_icon, smallBit);
                } else {
                    contentView.setImageViewBitmap(R.id.right_icon, android.support.v7.app.NotificationCompatImplBase.createColoredBitmap(context, smallIcon, android.graphics.Color.WHITE));
                }
                contentView.setViewVisibility(R.id.right_icon, android.view.View.VISIBLE);
            }
        } else
            if (smallIcon != 0) {
                // small icon at left
                contentView.setViewVisibility(R.id.icon, android.view.View.VISIBLE);
                if (afterLollipop) {
                    int backgroundSize = res.getDimensionPixelSize(R.dimen.notification_large_icon_width) - res.getDimensionPixelSize(R.dimen.notification_big_circle_margin);
                    int iconSize = res.getDimensionPixelSize(R.dimen.notification_small_icon_size_as_large);
                    android.graphics.Bitmap smallBit = android.support.v7.app.NotificationCompatImplBase.createIconWithBackground(context, smallIcon, backgroundSize, iconSize, color);
                    contentView.setImageViewBitmap(R.id.icon, smallBit);
                } else {
                    contentView.setImageViewBitmap(R.id.icon, android.support.v7.app.NotificationCompatImplBase.createColoredBitmap(context, smallIcon, android.graphics.Color.WHITE));
                }
            }

        if (contentTitle != null) {
            contentView.setTextViewText(R.id.title, contentTitle);
        }
        if (contentText != null) {
            contentView.setTextViewText(R.id.text, contentText);
            showLine3 = true;
        }
        // If there is a large icon we have a right side
        boolean hasRightSide = (!afterLollipop) && (largeIcon != null);
        if (contentInfo != null) {
            contentView.setTextViewText(R.id.info, contentInfo);
            contentView.setViewVisibility(R.id.info, android.view.View.VISIBLE);
            showLine3 = true;
            hasRightSide = true;
        } else
            if (number > 0) {
                final int tooBig = res.getInteger(R.integer.status_bar_notification_info_maxnum);
                if (number > tooBig) {
                    contentView.setTextViewText(R.id.info, ((android.content.res.Resources) (res)).getString(R.string.status_bar_notification_info_overflow));
                } else {
                    java.text.NumberFormat f = java.text.NumberFormat.getIntegerInstance();
                    contentView.setTextViewText(R.id.info, f.format(number));
                }
                contentView.setViewVisibility(R.id.info, android.view.View.VISIBLE);
                showLine3 = true;
                hasRightSide = true;
            } else {
                contentView.setViewVisibility(R.id.info, android.view.View.GONE);
            }

        // Need to show three lines? Only allow on Jellybean+
        if ((subText != null) && afterJellyBean) {
            contentView.setTextViewText(R.id.text, subText);
            if (contentText != null) {
                contentView.setTextViewText(R.id.text2, contentText);
                contentView.setViewVisibility(R.id.text2, android.view.View.VISIBLE);
                showLine2 = true;
            } else {
                contentView.setViewVisibility(R.id.text2, android.view.View.GONE);
            }
        }
        // RemoteViews.setViewPadding and RemoteViews.setTextViewTextSize is not available on ICS-
        if (showLine2 && afterJellyBean) {
            if (fitIn1U) {
                // need to shrink all the type to make sure everything fits
                final float subTextSize = res.getDimensionPixelSize(R.dimen.notification_subtext_size);
                contentView.setTextViewTextSize(R.id.text, android.util.TypedValue.COMPLEX_UNIT_PX, subTextSize);
            }
            // vertical centering
            contentView.setViewPadding(R.id.line1, 0, 0, 0, 0);
        }
        if (when != 0) {
            if (useChronometer && afterJellyBean) {
                contentView.setViewVisibility(R.id.chronometer, android.view.View.VISIBLE);
                contentView.setLong(R.id.chronometer, "setBase", when + (android.os.SystemClock.elapsedRealtime() - java.lang.System.currentTimeMillis()));
                contentView.setBoolean(R.id.chronometer, "setStarted", true);
            } else {
                contentView.setViewVisibility(R.id.time, android.view.View.VISIBLE);
                contentView.setLong(R.id.time, "setTime", when);
            }
            hasRightSide = true;
        }
        contentView.setViewVisibility(R.id.right_side, hasRightSide ? android.view.View.VISIBLE : android.view.View.GONE);
        contentView.setViewVisibility(R.id.line3, showLine3 ? android.view.View.VISIBLE : android.view.View.GONE);
        return contentView;
    }

    public static android.graphics.Bitmap createIconWithBackground(android.content.Context ctx, int iconId, int size, int iconSize, int color) {
        android.graphics.Bitmap coloredBitmap = android.support.v7.app.NotificationCompatImplBase.createColoredBitmap(ctx, R.drawable.notification_icon_background, color == android.support.v4.app.NotificationCompat.COLOR_DEFAULT ? 0 : color, size);
        android.graphics.Canvas canvas = new android.graphics.Canvas(coloredBitmap);
        android.graphics.drawable.Drawable icon = ctx.getResources().getDrawable(iconId).mutate();
        icon.setFilterBitmap(true);
        int inset = (size - iconSize) / 2;
        icon.setBounds(inset, inset, iconSize + inset, iconSize + inset);
        icon.setColorFilter(new android.graphics.PorterDuffColorFilter(android.graphics.Color.WHITE, android.graphics.PorterDuff.Mode.SRC_ATOP));
        icon.draw(canvas);
        return coloredBitmap;
    }

    public static void buildIntoRemoteViews(android.content.Context ctx, android.widget.RemoteViews outerView, android.widget.RemoteViews innerView) {
        // this needs to be done fore the other calls, since otherwise we might hide the wrong
        // things if our ids collide.
        android.support.v7.app.NotificationCompatImplBase.hideNormalContent(outerView);
        outerView.removeAllViews(R.id.notification_main_column);
        outerView.addView(R.id.notification_main_column, innerView.clone());
        outerView.setViewVisibility(R.id.notification_main_column, android.view.View.VISIBLE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            // Adjust padding depending on font size.
            outerView.setViewPadding(R.id.notification_main_column_container, 0, android.support.v7.app.NotificationCompatImplBase.calculateTopPadding(ctx), 0, 0);
        }
    }

    private static void hideNormalContent(android.widget.RemoteViews outerView) {
        outerView.setViewVisibility(R.id.title, android.view.View.GONE);
        outerView.setViewVisibility(R.id.text2, android.view.View.GONE);
        outerView.setViewVisibility(R.id.text, android.view.View.GONE);
    }

    public static int calculateTopPadding(android.content.Context ctx) {
        int padding = ctx.getResources().getDimensionPixelSize(R.dimen.notification_top_pad);
        int largePadding = ctx.getResources().getDimensionPixelSize(R.dimen.notification_top_pad_large_text);
        float fontScale = ctx.getResources().getConfiguration().fontScale;
        float largeFactor = (android.support.v7.app.NotificationCompatImplBase.constrain(fontScale, 1.0F, 1.3F) - 1.0F) / (1.3F - 1.0F);
        // Linearly interpolate the padding between large and normal with the font scale ranging
        // from 1f to LARGE_TEXT_SCALE
        return java.lang.Math.round(((1 - largeFactor) * padding) + (largeFactor * largePadding));
    }

    public static float constrain(float amount, float low, float high) {
        return amount < low ? low : amount > high ? high : amount;
    }
}

