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
 * A subclass of {@link BaseCardView} with an {@link ImageView} as its main region. The
 * {@link ImageCardView} is highly customizable and can be used for various use-cases by adjusting
 * the ImageViewCard's type to any combination of Title, Content, Badge or ImageOnly.
 * <p>
 * <h3>Styling</h3> There are two different ways to style the ImageCardView. <br>
 * No matter what way you use, all your styles applied to an ImageCardView have to extend the style
 * {@link R.style#Widget_Leanback_ImageCardViewStyle}.
 * <p>
 * <u>Example:</u><br>
 *
 * <pre>
 * {@code <style name="CustomImageCardViewStyle" parent="Widget.Leanback.ImageCardViewStyle">
 * <item name="cardBackground">#F0F</item>
 * <item name="lbImageCardViewType">Title|Content</item>
 * </style>
 * <style name="CustomImageCardTheme" parent="Theme.Leanback">
 * <item name="imageCardViewStyle">@style/CustomImageCardViewStyle</item>
 * <item name="imageCardViewInfoAreaStyle">@style/ImageCardViewColoredInfoArea</item>
 * <item name="imageCardViewTitleStyle">@style/ImageCardViewColoredTitle</item>
 * </style>}
 * </pre>
 * <p>
 * The first possibility is to set custom Styles in the Leanback Theme's attributes
 * <code>imageCardViewStyle</code>, <code>imageCardViewTitleStyle</code> etc. The styles set here,
 * is the default style for all ImageCardViews.
 * <p>
 * The second possibility allows you to style a particular ImageCardView. This is useful if you
 * want to create multiple types of cards. E.g. you might want to display a card with only a title
 * and another one with title and content. Thus you need to define two different
 * <code>ImageCardViewStyles</code> and two different themes and apply them to the ImageCardViews.
 * You can do this by using a the {@link #ImageCardView(Context)} constructor and passing a
 * ContextThemeWrapper with the custom ImageCardView theme id.
 * <p>
 * <u>Example (using constructor):</u><br>
 *
 * <pre>
 * {@code new ImageCardView(new ContextThemeWrapper(context, R.style.CustomImageCardTheme));}
 * </pre>
 *
 * <p>
 * You can style all ImageCardView's components such as the title, content, badge, infoArea and the
 * image itself by extending the corresponding style and overriding the specific attribute in your
 * custom ImageCardView theme.
 *
 * <h3>Components</h3> The ImageCardView contains three components which can be combined in any
 * combination:
 * <ul>
 * <li>Title: The card's title</li>
 * <li>Content: A short description</li>
 * <li>Badge: An icon which can be displayed on the right or left side of the card.</li>
 * </ul>
 * In order to choose the components you want to use in your ImageCardView, you have to specify them
 * in the <code>lbImageCardViewType</code> attribute of your custom <code>ImageCardViewStyle</code>.
 * You can combine the following values:
 * <code>Title, Content, IconOnRight, IconOnLeft, ImageOnly</code>.
 * <p>
 * <u>Examples:</u><br>
 *
 * <pre>
 * {@code <style name="CustomImageCardViewStyle" parent="Widget.Leanback.ImageCardViewStyle">
 * ...
 * <item name="lbImageCardViewType">Title|Content|IconOnLeft</item>
 * ...
 * </style>}
 * </pre>
 *
 * <pre>
 * {@code <style name="CustomImageCardViewStyle" parent="Widget.Leanback.ImageCardViewStyle">
 * ...
 * <item name="lbImageCardViewType">ImageOnly</item>
 * ...
 * </style>}
 * </pre>
 *
 * @unknown ref android.support.v17.leanback.R.styleable#LeanbackTheme_imageCardViewStyle
 * @unknown ref android.support.v17.leanback.R.styleable#lbImageCardView_lbImageCardViewType
 * @unknown ref android.support.v17.leanback.R.styleable#LeanbackTheme_imageCardViewTitleStyle
 * @unknown ref android.support.v17.leanback.R.styleable#LeanbackTheme_imageCardViewContentStyle
 * @unknown ref android.support.v17.leanback.R.styleable#LeanbackTheme_imageCardViewBadgeStyle
 * @unknown ref android.support.v17.leanback.R.styleable#LeanbackTheme_imageCardViewImageStyle
 * @unknown ref android.support.v17.leanback.R.styleable#LeanbackTheme_imageCardViewInfoAreaStyle
 */
public class ImageCardView extends android.support.v17.leanback.widget.BaseCardView {
    public static final int CARD_TYPE_FLAG_IMAGE_ONLY = 0;

    public static final int CARD_TYPE_FLAG_TITLE = 1;

    public static final int CARD_TYPE_FLAG_CONTENT = 2;

    public static final int CARD_TYPE_FLAG_ICON_RIGHT = 4;

    public static final int CARD_TYPE_FLAG_ICON_LEFT = 8;

    private android.widget.ImageView mImageView;

    private android.view.ViewGroup mInfoArea;

    private android.widget.TextView mTitleView;

    private android.widget.TextView mContentView;

    private android.widget.ImageView mBadgeImage;

    private boolean mAttachedToWindow;

    /**
     * Create an ImageCardView using a given theme for customization.
     *
     * @param context
     * 		The Context the view is running in, through which it can
     * 		access the current theme, resources, etc.
     * @param themeResId
     * 		The resourceId of the theme you want to apply to the ImageCardView. The theme
     * 		includes attributes "imageCardViewStyle", "imageCardViewTitleStyle",
     * 		"imageCardViewContentStyle" etc. to customize individual part of ImageCardView.
     * @deprecated Calling this constructor inefficiently creates one ContextThemeWrapper per card,
    you should share it in card Presenter: wrapper = new ContextThemeWrapper(context, themResId);
    return new ImageCardView(wrapper);
     */
    @java.lang.Deprecated
    public ImageCardView(android.content.Context context, int themeResId) {
        this(new android.view.ContextThemeWrapper(context, themeResId));
    }

    /**
     *
     *
     * @see #View(Context, AttributeSet, int)
     */
    public ImageCardView(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        buildImageCardView(attrs, defStyleAttr, R.style.Widget_Leanback_ImageCardView);
    }

    private void buildImageCardView(android.util.AttributeSet attrs, int defStyleAttr, int defStyle) {
        // Make sure the ImageCardView is focusable.
        setFocusable(true);
        setFocusableInTouchMode(true);
        android.view.LayoutInflater inflater = android.view.LayoutInflater.from(getContext());
        inflater.inflate(R.layout.lb_image_card_view, this);
        android.content.res.TypedArray cardAttrs = getContext().obtainStyledAttributes(attrs, R.styleable.lbImageCardView, defStyleAttr, defStyle);
        int cardType = cardAttrs.getInt(R.styleable.lbImageCardView_lbImageCardViewType, android.support.v17.leanback.widget.ImageCardView.CARD_TYPE_FLAG_IMAGE_ONLY);
        boolean hasImageOnly = cardType == android.support.v17.leanback.widget.ImageCardView.CARD_TYPE_FLAG_IMAGE_ONLY;
        boolean hasTitle = (cardType & android.support.v17.leanback.widget.ImageCardView.CARD_TYPE_FLAG_TITLE) == android.support.v17.leanback.widget.ImageCardView.CARD_TYPE_FLAG_TITLE;
        boolean hasContent = (cardType & android.support.v17.leanback.widget.ImageCardView.CARD_TYPE_FLAG_CONTENT) == android.support.v17.leanback.widget.ImageCardView.CARD_TYPE_FLAG_CONTENT;
        boolean hasIconRight = (cardType & android.support.v17.leanback.widget.ImageCardView.CARD_TYPE_FLAG_ICON_RIGHT) == android.support.v17.leanback.widget.ImageCardView.CARD_TYPE_FLAG_ICON_RIGHT;
        boolean hasIconLeft = (!hasIconRight) && ((cardType & android.support.v17.leanback.widget.ImageCardView.CARD_TYPE_FLAG_ICON_LEFT) == android.support.v17.leanback.widget.ImageCardView.CARD_TYPE_FLAG_ICON_LEFT);
        mImageView = ((android.widget.ImageView) (findViewById(R.id.main_image)));
        if (mImageView.getDrawable() == null) {
            mImageView.setVisibility(android.view.View.INVISIBLE);
        }
        mInfoArea = ((android.view.ViewGroup) (findViewById(R.id.info_field)));
        if (hasImageOnly) {
            removeView(mInfoArea);
            cardAttrs.recycle();
            return;
        }
        // Create children
        if (hasTitle) {
            mTitleView = ((android.widget.TextView) (inflater.inflate(R.layout.lb_image_card_view_themed_title, mInfoArea, false)));
            mInfoArea.addView(mTitleView);
        }
        if (hasContent) {
            mContentView = ((android.widget.TextView) (inflater.inflate(R.layout.lb_image_card_view_themed_content, mInfoArea, false)));
            mInfoArea.addView(mContentView);
        }
        if (hasIconRight || hasIconLeft) {
            int layoutId = R.layout.lb_image_card_view_themed_badge_right;
            if (hasIconLeft) {
                layoutId = R.layout.lb_image_card_view_themed_badge_left;
            }
            mBadgeImage = ((android.widget.ImageView) (inflater.inflate(layoutId, mInfoArea, false)));
            mInfoArea.addView(mBadgeImage);
        }
        // Set up LayoutParams for children
        if ((hasTitle && (!hasContent)) && (mBadgeImage != null)) {
            android.widget.RelativeLayout.LayoutParams relativeLayoutParams = ((android.widget.RelativeLayout.LayoutParams) (mTitleView.getLayoutParams()));
            // Adjust title TextView if there is an icon but no content
            if (hasIconLeft) {
                relativeLayoutParams.addRule(android.widget.RelativeLayout.END_OF, mBadgeImage.getId());
            } else {
                relativeLayoutParams.addRule(android.widget.RelativeLayout.START_OF, mBadgeImage.getId());
            }
            mTitleView.setLayoutParams(relativeLayoutParams);
        }
        // Set up LayoutParams for children
        if (hasContent) {
            android.widget.RelativeLayout.LayoutParams relativeLayoutParams = ((android.widget.RelativeLayout.LayoutParams) (mContentView.getLayoutParams()));
            if (!hasTitle) {
                relativeLayoutParams.addRule(android.widget.RelativeLayout.ALIGN_PARENT_TOP);
            }
            // Adjust content TextView if icon is on the left
            if (hasIconLeft) {
                relativeLayoutParams.removeRule(android.widget.RelativeLayout.START_OF);
                relativeLayoutParams.removeRule(android.widget.RelativeLayout.ALIGN_PARENT_START);
                relativeLayoutParams.addRule(android.widget.RelativeLayout.END_OF, mBadgeImage.getId());
            }
            mContentView.setLayoutParams(relativeLayoutParams);
        }
        if (mBadgeImage != null) {
            android.widget.RelativeLayout.LayoutParams relativeLayoutParams = ((android.widget.RelativeLayout.LayoutParams) (mBadgeImage.getLayoutParams()));
            if (hasContent) {
                relativeLayoutParams.addRule(android.widget.RelativeLayout.ALIGN_BOTTOM, mContentView.getId());
            } else
                if (hasTitle) {
                    relativeLayoutParams.addRule(android.widget.RelativeLayout.ALIGN_BOTTOM, mTitleView.getId());
                }

            mBadgeImage.setLayoutParams(relativeLayoutParams);
        }
        // Backward compatibility: Newly created ImageCardViews should change
        // the InfoArea's background color in XML using the corresponding style.
        // However, since older implementations might make use of the
        // 'infoAreaBackground' attribute, we have to make sure to support it.
        // If the user has set a specific value here, it will differ from null.
        // In this case, we do want to override the value set in the style.
        android.graphics.drawable.Drawable background = cardAttrs.getDrawable(R.styleable.lbImageCardView_infoAreaBackground);
        if (null != background) {
            setInfoAreaBackground(background);
        }
        // Backward compatibility: There has to be an icon in the default
        // version. If there is one, we have to set it's visibility to 'GONE'.
        // Disabling 'adjustIconVisibility' allows the user to set the icon's
        // visibility state in XML rather than code.
        if ((mBadgeImage != null) && (mBadgeImage.getDrawable() == null)) {
            mBadgeImage.setVisibility(android.view.View.GONE);
        }
        cardAttrs.recycle();
    }

    /**
     *
     *
     * @see #View(Context)
     */
    public ImageCardView(android.content.Context context) {
        this(context, null);
    }

    /**
     *
     *
     * @see #View(Context, AttributeSet)
     */
    public ImageCardView(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, R.attr.imageCardViewStyle);
    }

    /**
     * Returns the main image view.
     */
    public final android.widget.ImageView getMainImageView() {
        return mImageView;
    }

    /**
     * Enables or disables adjustment of view bounds on the main image.
     */
    public void setMainImageAdjustViewBounds(boolean adjustViewBounds) {
        if (mImageView != null) {
            mImageView.setAdjustViewBounds(adjustViewBounds);
        }
    }

    /**
     * Sets the ScaleType of the main image.
     */
    public void setMainImageScaleType(android.widget.ImageView.ScaleType scaleType) {
        if (mImageView != null) {
            mImageView.setScaleType(scaleType);
        }
    }

    /**
     * Sets the image drawable with fade-in animation.
     */
    public void setMainImage(android.graphics.drawable.Drawable drawable) {
        setMainImage(drawable, true);
    }

    /**
     * Sets the image drawable with optional fade-in animation.
     */
    public void setMainImage(android.graphics.drawable.Drawable drawable, boolean fade) {
        if (mImageView == null) {
            return;
        }
        mImageView.setImageDrawable(drawable);
        if (drawable == null) {
            mImageView.animate().cancel();
            mImageView.setAlpha(1.0F);
            mImageView.setVisibility(android.view.View.INVISIBLE);
        } else {
            mImageView.setVisibility(android.view.View.VISIBLE);
            if (fade) {
                fadeIn();
            } else {
                mImageView.animate().cancel();
                mImageView.setAlpha(1.0F);
            }
        }
    }

    /**
     * Sets the layout dimensions of the ImageView.
     */
    public void setMainImageDimensions(int width, int height) {
        android.view.ViewGroup.LayoutParams lp = mImageView.getLayoutParams();
        lp.width = width;
        lp.height = height;
        mImageView.setLayoutParams(lp);
    }

    /**
     * Returns the ImageView drawable.
     */
    public android.graphics.drawable.Drawable getMainImage() {
        if (mImageView == null) {
            return null;
        }
        return mImageView.getDrawable();
    }

    /**
     * Returns the info area background drawable.
     */
    public android.graphics.drawable.Drawable getInfoAreaBackground() {
        if (mInfoArea != null) {
            return mInfoArea.getBackground();
        }
        return null;
    }

    /**
     * Sets the info area background drawable.
     */
    public void setInfoAreaBackground(android.graphics.drawable.Drawable drawable) {
        if (mInfoArea != null) {
            mInfoArea.setBackground(drawable);
        }
    }

    /**
     * Sets the info area background color.
     */
    public void setInfoAreaBackgroundColor(@android.support.annotation.ColorInt
    int color) {
        if (mInfoArea != null) {
            mInfoArea.setBackgroundColor(color);
        }
    }

    /**
     * Sets the title text.
     */
    public void setTitleText(java.lang.CharSequence text) {
        if (mTitleView == null) {
            return;
        }
        mTitleView.setText(text);
    }

    /**
     * Returns the title text.
     */
    public java.lang.CharSequence getTitleText() {
        if (mTitleView == null) {
            return null;
        }
        return mTitleView.getText();
    }

    /**
     * Sets the content text.
     */
    public void setContentText(java.lang.CharSequence text) {
        if (mContentView == null) {
            return;
        }
        mContentView.setText(text);
    }

    /**
     * Returns the content text.
     */
    public java.lang.CharSequence getContentText() {
        if (mContentView == null) {
            return null;
        }
        return mContentView.getText();
    }

    /**
     * Sets the badge image drawable.
     */
    public void setBadgeImage(android.graphics.drawable.Drawable drawable) {
        if (mBadgeImage == null) {
            return;
        }
        mBadgeImage.setImageDrawable(drawable);
        if (drawable != null) {
            mBadgeImage.setVisibility(android.view.View.VISIBLE);
        } else {
            mBadgeImage.setVisibility(android.view.View.GONE);
        }
    }

    /**
     * Returns the badge image drawable.
     */
    public android.graphics.drawable.Drawable getBadgeImage() {
        if (mBadgeImage == null) {
            return null;
        }
        return mBadgeImage.getDrawable();
    }

    private void fadeIn() {
        mImageView.setAlpha(0.0F);
        if (mAttachedToWindow) {
            mImageView.animate().alpha(1.0F).setDuration(mImageView.getResources().getInteger(android.R.integer.config_shortAnimTime));
        }
    }

    @java.lang.Override
    public boolean hasOverlappingRendering() {
        return false;
    }

    @java.lang.Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mAttachedToWindow = true;
        if (mImageView.getAlpha() == 0) {
            fadeIn();
        }
    }

    @java.lang.Override
    protected void onDetachedFromWindow() {
        mAttachedToWindow = false;
        mImageView.animate().cancel();
        mImageView.setAlpha(1.0F);
        super.onDetachedFromWindow();
    }
}

