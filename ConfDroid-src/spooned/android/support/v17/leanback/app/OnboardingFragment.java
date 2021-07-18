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
package android.support.v17.leanback.app;


/**
 * An OnboardingFragment provides a common and simple way to build onboarding screen for
 * applications.
 * <p>
 * <h3>Building the screen</h3>
 * The view structure of onboarding screen is composed of the common parts and custom parts. The
 * common parts are composed of title, description and page navigator and the custom parts are
 * composed of background, contents and foreground.
 * <p>
 * To build the screen views, the inherited class should override:
 * <ul>
 * <li>{@link #onCreateBackgroundView} to provide the background view. Background view has the same
 * size as the screen and the lowest z-order.</li>
 * <li>{@link #onCreateContentView} to provide the contents view. The content view is located in
 * the content area at the center of the screen.</li>
 * <li>{@link #onCreateForegroundView} to provide the foreground view. Foreground view has the same
 * size as the screen and the highest z-order</li>
 * </ul>
 * <p>
 * Each of these methods can return {@code null} if the application doesn't want to provide it.
 * <p>
 * <h3>Page information</h3>
 * The onboarding screen may have several pages which explain the functionality of the application.
 * The inherited class should provide the page information by overriding the methods:
 * <p>
 * <ul>
 * <li>{@link #getPageCount} to provide the number of pages.</li>
 * <li>{@link #getPageTitle} to provide the title of the page.</li>
 * <li>{@link #getPageDescription} to provide the description of the page.</li>
 * </ul>
 * <p>
 * Note that the information is used in {@link #onCreateView}, so should be initialized before
 * calling {@code super.onCreateView}.
 * <p>
 * <h3>Animation</h3>
 * Onboarding screen has three kinds of animations:
 * <p>
 * <h4>Logo Splash Animation</a></h4>
 * When onboarding screen appears, the logo splash animation is played by default. The animation
 * fades in the logo image, pauses in a few seconds and fades it out.
 * <p>
 * In most cases, the logo animation needs to be customized because the logo images of applications
 * are different from each other, or some applications may want to show their own animations.
 * <p>
 * The logo animation can be customized in two ways:
 * <ul>
 * <li>The simplest way is to provide the logo image by calling {@link #setLogoResourceId} to show
 * the default logo animation. This method should be called in {@link Fragment#onCreateView}.</li>
 * <li>If the logo animation is complex, then override {@link #onCreateLogoAnimation} and return the
 * {@link Animator} object to run.</li>
 * </ul>
 * <p>
 * If the inherited class provides neither the logo image nor the animation, the logo animation will
 * be omitted.
 * <h4>Page enter animation</h4>
 * After logo animation finishes, page enter animation starts. The application can provide the
 * animations of custom views by overriding {@link #onCreateEnterAnimation}.
 * <h4>Page change animation</h4>
 * When the page changes, the default animations of the title and description are played. The
 * inherited class can override {@link #onPageChanged} to start the custom animations.
 * <p>
 * <h3>Finishing the screen</h3>
 * <p>
 * If the user finishes the onboarding screen after navigating all the pages,
 * {@link #onFinishFragment} is called. The inherited class can override this method to show another
 * fragment or activity, or just remove this fragment.
 * <p>
 * <h3>Theming</h3>
 * <p>
 * OnboardingFragment must have access to an appropriate theme. Specifically, the fragment must
 * receive  {@link R.style#Theme_Leanback_Onboarding}, or a theme whose parent is set to that theme.
 * Themes can be provided in one of three ways:
 * <ul>
 * <li>The simplest way is to set the theme for the host Activity to the Onboarding theme or a theme
 * that derives from it.</li>
 * <li>If the Activity already has a theme and setting its parent theme is inconvenient, the
 * existing Activity theme can have an entry added for the attribute
 * {@link R.styleable#LeanbackOnboardingTheme_onboardingTheme}. If present, this theme will be used
 * by OnboardingFragment as an overlay to the Activity's theme.</li>
 * <li>Finally, custom subclasses of OnboardingFragment may provide a theme through the
 * {@link #onProvideTheme} method. This can be useful if a subclass is used across multiple
 * Activities.</li>
 * </ul>
 * <p>
 * If the theme is provided in multiple ways, the onProvideTheme override has priority, followed by
 * the Activity's theme. (Themes whose parent theme is already set to the onboarding theme do not
 * need to set the onboardingTheme attribute; if set, it will be ignored.)
 *
 * @unknown ref R.styleable#LeanbackOnboardingTheme_onboardingTheme
 * @unknown ref R.styleable#LeanbackOnboardingTheme_onboardingHeaderStyle
 * @unknown ref R.styleable#LeanbackOnboardingTheme_onboardingTitleStyle
 * @unknown ref R.styleable#LeanbackOnboardingTheme_onboardingDescriptionStyle
 * @unknown ref R.styleable#LeanbackOnboardingTheme_onboardingNavigatorContainerStyle
 * @unknown ref R.styleable#LeanbackOnboardingTheme_onboardingPageIndicatorStyle
 * @unknown ref R.styleable#LeanbackOnboardingTheme_onboardingStartButtonStyle
 * @unknown ref R.styleable#LeanbackOnboardingTheme_onboardingLogoStyle
 */
public abstract class OnboardingFragment extends android.app.Fragment {
    private static final java.lang.String TAG = "OnboardingFragment";

    private static final boolean DEBUG = false;

    private static final long LOGO_SPLASH_PAUSE_DURATION_MS = 1333;

    private static final long START_DELAY_TITLE_MS = 33;

    private static final long START_DELAY_DESCRIPTION_MS = 33;

    private static final long HEADER_ANIMATION_DURATION_MS = 417;

    private static final long DESCRIPTION_START_DELAY_MS = 33;

    private static final long HEADER_APPEAR_DELAY_MS = 500;

    private static final int SLIDE_DISTANCE = 60;

    private static int sSlideDistance;

    private static final android.animation.TimeInterpolator HEADER_APPEAR_INTERPOLATOR = new android.view.animation.DecelerateInterpolator();

    private static final android.animation.TimeInterpolator HEADER_DISAPPEAR_INTERPOLATOR = new android.view.animation.AccelerateInterpolator();

    // Keys used to save and restore the states.
    private static final java.lang.String KEY_CURRENT_PAGE_INDEX = "leanback.onboarding.current_page_index";

    private android.view.ContextThemeWrapper mThemeWrapper;

    android.support.v17.leanback.widget.PagingIndicator mPageIndicator;

    android.view.View mStartButton;

    private android.widget.ImageView mLogoView;

    android.widget.TextView mTitleView;

    android.widget.TextView mDescriptionView;

    boolean mIsLtr;

    // No need to save/restore the logo resource ID, because the logo animation will not appear when
    // the fragment is restored.
    private int mLogoResourceId;

    boolean mEnterTransitionFinished;

    int mCurrentPageIndex;

    private android.animation.AnimatorSet mAnimator;

    private final android.view.View.OnClickListener mOnClickListener = new android.view.View.OnClickListener() {
        @java.lang.Override
        public void onClick(android.view.View view) {
            if (!mEnterTransitionFinished) {
                // Do not change page until the enter transition finishes.
                return;
            }
            if (mCurrentPageIndex == (getPageCount() - 1)) {
                onFinishFragment();
            } else {
                moveToNextPage();
            }
        }
    };

    private final android.view.View.OnKeyListener mOnKeyListener = new android.view.View.OnKeyListener() {
        @java.lang.Override
        public boolean onKey(android.view.View v, int keyCode, android.view.KeyEvent event) {
            if (!mEnterTransitionFinished) {
                // Ignore key event until the enter transition finishes.
                return keyCode != android.view.KeyEvent.KEYCODE_BACK;
            }
            if (event.getAction() == android.view.KeyEvent.ACTION_DOWN) {
                return false;
            }
            switch (keyCode) {
                case android.view.KeyEvent.KEYCODE_BACK :
                    if (mCurrentPageIndex == 0) {
                        return false;
                    }
                    moveToPreviousPage();
                    return true;
                case android.view.KeyEvent.KEYCODE_DPAD_LEFT :
                    if (mIsLtr) {
                        moveToPreviousPage();
                    } else {
                        moveToNextPage();
                    }
                    return true;
                case android.view.KeyEvent.KEYCODE_DPAD_RIGHT :
                    if (mIsLtr) {
                        moveToNextPage();
                    } else {
                        moveToPreviousPage();
                    }
                    return true;
            }
            return false;
        }
    };

    void moveToPreviousPage() {
        if (mCurrentPageIndex > 0) {
            --mCurrentPageIndex;
            onPageChangedInternal(mCurrentPageIndex + 1);
        }
    }

    void moveToNextPage() {
        if (mCurrentPageIndex < (getPageCount() - 1)) {
            ++mCurrentPageIndex;
            onPageChangedInternal(mCurrentPageIndex - 1);
        }
    }

    @android.support.annotation.Nullable
    @java.lang.Override
    public android.view.View onCreateView(android.view.LayoutInflater inflater, final android.view.ViewGroup container, android.os.Bundle savedInstanceState) {
        resolveTheme();
        android.view.LayoutInflater localInflater = getThemeInflater(inflater);
        final android.view.ViewGroup view = ((android.view.ViewGroup) (localInflater.inflate(R.layout.lb_onboarding_fragment, container, false)));
        mIsLtr = getResources().getConfiguration().getLayoutDirection() == android.view.View.LAYOUT_DIRECTION_LTR;
        mPageIndicator = ((android.support.v17.leanback.widget.PagingIndicator) (view.findViewById(R.id.page_indicator)));
        mPageIndicator.setOnClickListener(mOnClickListener);
        mPageIndicator.setOnKeyListener(mOnKeyListener);
        mStartButton = view.findViewById(R.id.button_start);
        mStartButton.setOnClickListener(mOnClickListener);
        mStartButton.setOnKeyListener(mOnKeyListener);
        mLogoView = ((android.widget.ImageView) (view.findViewById(R.id.logo)));
        mTitleView = ((android.widget.TextView) (view.findViewById(R.id.title)));
        mDescriptionView = ((android.widget.TextView) (view.findViewById(R.id.description)));
        if (android.support.v17.leanback.app.OnboardingFragment.sSlideDistance == 0) {
            android.support.v17.leanback.app.OnboardingFragment.sSlideDistance = ((int) (android.support.v17.leanback.app.OnboardingFragment.SLIDE_DISTANCE * getActivity().getResources().getDisplayMetrics().scaledDensity));
        }
        if (savedInstanceState == null) {
            mCurrentPageIndex = 0;
            mEnterTransitionFinished = false;
            mPageIndicator.onPageSelected(0, false);
            view.getViewTreeObserver().addOnPreDrawListener(new android.view.ViewTreeObserver.OnPreDrawListener() {
                @java.lang.Override
                public boolean onPreDraw() {
                    view.getViewTreeObserver().removeOnPreDrawListener(this);
                    if (!startLogoAnimation()) {
                        startEnterAnimation();
                    }
                    return true;
                }
            });
        } else {
            mEnterTransitionFinished = true;
            mCurrentPageIndex = savedInstanceState.getInt(android.support.v17.leanback.app.OnboardingFragment.KEY_CURRENT_PAGE_INDEX);
            initializeViews(view);
        }
        view.requestFocus();
        return view;
    }

    @java.lang.Override
    public void onSaveInstanceState(android.os.Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(android.support.v17.leanback.app.OnboardingFragment.KEY_CURRENT_PAGE_INDEX, mCurrentPageIndex);
    }

    /**
     * Returns the theme used for styling the fragment. The default returns -1, indicating that the
     * host Activity's theme should be used.
     *
     * @return The theme resource ID of the theme to use in this fragment, or -1 to use the host
    Activity's theme.
     */
    public int onProvideTheme() {
        return -1;
    }

    private void resolveTheme() {
        android.app.Activity activity = getActivity();
        int theme = onProvideTheme();
        if (theme == (-1)) {
            // Look up the onboardingTheme in the activity's currently specified theme. If it
            // exists, wrap the theme with its value.
            int resId = R.attr.onboardingTheme;
            android.util.TypedValue typedValue = new android.util.TypedValue();
            boolean found = activity.getTheme().resolveAttribute(resId, typedValue, true);
            if (android.support.v17.leanback.app.OnboardingFragment.DEBUG)
                android.util.Log.v(android.support.v17.leanback.app.OnboardingFragment.TAG, "Found onboarding theme reference? " + found);

            if (found) {
                mThemeWrapper = new android.view.ContextThemeWrapper(activity, typedValue.resourceId);
            }
        } else {
            mThemeWrapper = new android.view.ContextThemeWrapper(activity, theme);
        }
    }

    private android.view.LayoutInflater getThemeInflater(android.view.LayoutInflater inflater) {
        return mThemeWrapper == null ? inflater : inflater.cloneInContext(mThemeWrapper);
    }

    /**
     * Sets the resource ID of the splash logo image. If the logo resource id set, the default logo
     * splash animation will be played.
     *
     * @param id
     * 		The resource ID of the logo image.
     */
    public final void setLogoResourceId(int id) {
        mLogoResourceId = id;
    }

    /**
     * Returns the resource ID of the splash logo image.
     *
     * @return The resource ID of the splash logo image.
     */
    public final int getLogoResourceId() {
        return mLogoResourceId;
    }

    /**
     * Called to have the inherited class create its own logo animation.
     * <p>
     * This is called only if the logo image resource ID is not set by {@link #setLogoResourceId}.
     * If this returns {@code null}, the logo animation is skipped.
     *
     * @return The {@link Animator} object which runs the logo animation.
     */
    @android.support.annotation.Nullable
    protected android.animation.Animator onCreateLogoAnimation() {
        return null;
    }

    boolean startLogoAnimation() {
        android.animation.Animator animator = null;
        if (mLogoResourceId != 0) {
            mLogoView.setVisibility(android.view.View.VISIBLE);
            mLogoView.setImageResource(mLogoResourceId);
            android.animation.Animator inAnimator = android.animation.AnimatorInflater.loadAnimator(getActivity(), R.animator.lb_onboarding_logo_enter);
            android.animation.Animator outAnimator = android.animation.AnimatorInflater.loadAnimator(getActivity(), R.animator.lb_onboarding_logo_exit);
            outAnimator.setStartDelay(android.support.v17.leanback.app.OnboardingFragment.LOGO_SPLASH_PAUSE_DURATION_MS);
            android.animation.AnimatorSet logoAnimator = new android.animation.AnimatorSet();
            logoAnimator.playSequentially(inAnimator, outAnimator);
            logoAnimator.setTarget(mLogoView);
            animator = logoAnimator;
        } else {
            animator = onCreateLogoAnimation();
        }
        if (animator != null) {
            animator.addListener(new android.animation.AnimatorListenerAdapter() {
                @java.lang.Override
                public void onAnimationEnd(android.animation.Animator animation) {
                    if (getActivity() != null) {
                        startEnterAnimation();
                    }
                }
            });
            animator.start();
            return true;
        }
        return false;
    }

    /**
     * Called to have the inherited class create its enter animation. The start animation runs after
     * logo animation ends.
     *
     * @return The {@link Animator} object which runs the page enter animation.
     */
    @android.support.annotation.Nullable
    protected android.animation.Animator onCreateEnterAnimation() {
        return null;
    }

    private void initializeViews(android.view.View container) {
        mLogoView.setVisibility(android.view.View.GONE);
        // Create custom views.
        android.view.LayoutInflater inflater = getThemeInflater(android.view.LayoutInflater.from(getActivity()));
        android.view.ViewGroup backgroundContainer = ((android.view.ViewGroup) (container.findViewById(R.id.background_container)));
        android.view.View background = onCreateBackgroundView(inflater, backgroundContainer);
        if (background != null) {
            backgroundContainer.setVisibility(android.view.View.VISIBLE);
            backgroundContainer.addView(background);
        }
        android.view.ViewGroup contentContainer = ((android.view.ViewGroup) (container.findViewById(R.id.content_container)));
        android.view.View content = onCreateContentView(inflater, contentContainer);
        if (content != null) {
            contentContainer.setVisibility(android.view.View.VISIBLE);
            contentContainer.addView(content);
        }
        android.view.ViewGroup foregroundContainer = ((android.view.ViewGroup) (container.findViewById(R.id.foreground_container)));
        android.view.View foreground = onCreateForegroundView(inflater, foregroundContainer);
        if (foreground != null) {
            foregroundContainer.setVisibility(android.view.View.VISIBLE);
            foregroundContainer.addView(foreground);
        }
        // Make views visible which were invisible while logo animation is running.
        container.findViewById(R.id.page_container).setVisibility(android.view.View.VISIBLE);
        container.findViewById(R.id.content_container).setVisibility(android.view.View.VISIBLE);
        if (getPageCount() > 1) {
            mPageIndicator.setPageCount(getPageCount());
            mPageIndicator.onPageSelected(mCurrentPageIndex, false);
        }
        if (mCurrentPageIndex == (getPageCount() - 1)) {
            mStartButton.setVisibility(android.view.View.VISIBLE);
        } else {
            mPageIndicator.setVisibility(android.view.View.VISIBLE);
        }
        // Header views.
        mTitleView.setText(getPageTitle(mCurrentPageIndex));
        mDescriptionView.setText(getPageDescription(mCurrentPageIndex));
    }

    void startEnterAnimation() {
        mEnterTransitionFinished = true;
        initializeViews(getView());
        java.util.List<android.animation.Animator> animators = new java.util.ArrayList<>();
        android.animation.Animator animator = android.animation.AnimatorInflater.loadAnimator(getActivity(), R.animator.lb_onboarding_page_indicator_enter);
        animator.setTarget(getPageCount() <= 1 ? mStartButton : mPageIndicator);
        animators.add(animator);
        // Header title
        android.view.View view = getActivity().findViewById(R.id.title);
        view.setAlpha(0);
        animator = android.animation.AnimatorInflater.loadAnimator(getActivity(), R.animator.lb_onboarding_title_enter);
        animator.setStartDelay(android.support.v17.leanback.app.OnboardingFragment.START_DELAY_TITLE_MS);
        animator.setTarget(view);
        animators.add(animator);
        // Header description
        view = getActivity().findViewById(R.id.description);
        view.setAlpha(0);
        animator = android.animation.AnimatorInflater.loadAnimator(getActivity(), R.animator.lb_onboarding_description_enter);
        animator.setStartDelay(android.support.v17.leanback.app.OnboardingFragment.START_DELAY_DESCRIPTION_MS);
        animator.setTarget(view);
        animators.add(animator);
        // Customized animation by the inherited class.
        android.animation.Animator customAnimator = onCreateEnterAnimation();
        if (customAnimator != null) {
            animators.add(customAnimator);
        }
        mAnimator = new android.animation.AnimatorSet();
        mAnimator.playTogether(animators);
        mAnimator.start();
        // Search focus and give the focus to the appropriate child which has become visible.
        getView().requestFocus();
    }

    /**
     * Returns the page count.
     *
     * @return The page count.
     */
    protected abstract int getPageCount();

    /**
     * Returns the title of the given page.
     *
     * @param pageIndex
     * 		The page index.
     * @return The title of the page.
     */
    protected abstract java.lang.CharSequence getPageTitle(int pageIndex);

    /**
     * Returns the description of the given page.
     *
     * @param pageIndex
     * 		The page index.
     * @return The description of the page.
     */
    protected abstract java.lang.CharSequence getPageDescription(int pageIndex);

    /**
     * Returns the index of the current page.
     *
     * @return The index of the current page.
     */
    protected final int getCurrentPageIndex() {
        return mCurrentPageIndex;
    }

    /**
     * Called to have the inherited class create background view. This is optional and the fragment
     * which doesn't have the background view can return {@code null}. This is called inside
     * {@link #onCreateView}.
     *
     * @param inflater
     * 		The LayoutInflater object that can be used to inflate the views,
     * @param container
     * 		The parent view that the additional views are attached to.The fragment
     * 		should not add the view by itself.
     * @return The background view for the onboarding screen, or {@code null}.
     */
    @android.support.annotation.Nullable
    protected abstract android.view.View onCreateBackgroundView(android.view.LayoutInflater inflater, android.view.ViewGroup container);

    /**
     * Called to have the inherited class create content view. This is optional and the fragment
     * which doesn't have the content view can return {@code null}. This is called inside
     * {@link #onCreateView}.
     *
     * <p>The content view would be located at the center of the screen.
     *
     * @param inflater
     * 		The LayoutInflater object that can be used to inflate the views,
     * @param container
     * 		The parent view that the additional views are attached to.The fragment
     * 		should not add the view by itself.
     * @return The content view for the onboarding screen, or {@code null}.
     */
    @android.support.annotation.Nullable
    protected abstract android.view.View onCreateContentView(android.view.LayoutInflater inflater, android.view.ViewGroup container);

    /**
     * Called to have the inherited class create foreground view. This is optional and the fragment
     * which doesn't need the foreground view can return {@code null}. This is called inside
     * {@link #onCreateView}.
     *
     * <p>This foreground view would have the highest z-order.
     *
     * @param inflater
     * 		The LayoutInflater object that can be used to inflate the views,
     * @param container
     * 		The parent view that the additional views are attached to.The fragment
     * 		should not add the view by itself.
     * @return The foreground view for the onboarding screen, or {@code null}.
     */
    @android.support.annotation.Nullable
    protected abstract android.view.View onCreateForegroundView(android.view.LayoutInflater inflater, android.view.ViewGroup container);

    /**
     * Called when the onboarding flow finishes.
     */
    protected void onFinishFragment() {
    }

    /**
     * Called when the page changes.
     */
    private void onPageChangedInternal(int previousPage) {
        if (mAnimator != null) {
            mAnimator.end();
        }
        mPageIndicator.onPageSelected(mCurrentPageIndex, true);
        java.util.List<android.animation.Animator> animators = new java.util.ArrayList<>();
        // Header animation
        android.animation.Animator fadeAnimator = null;
        if (previousPage < getCurrentPageIndex()) {
            // sliding to left
            animators.add(createAnimator(mTitleView, false, android.view.Gravity.START, 0));
            animators.add(fadeAnimator = createAnimator(mDescriptionView, false, android.view.Gravity.START, android.support.v17.leanback.app.OnboardingFragment.DESCRIPTION_START_DELAY_MS));
            animators.add(createAnimator(mTitleView, true, android.view.Gravity.END, android.support.v17.leanback.app.OnboardingFragment.HEADER_APPEAR_DELAY_MS));
            animators.add(createAnimator(mDescriptionView, true, android.view.Gravity.END, android.support.v17.leanback.app.OnboardingFragment.HEADER_APPEAR_DELAY_MS + android.support.v17.leanback.app.OnboardingFragment.DESCRIPTION_START_DELAY_MS));
        } else {
            // sliding to right
            animators.add(createAnimator(mTitleView, false, android.view.Gravity.END, 0));
            animators.add(fadeAnimator = createAnimator(mDescriptionView, false, android.view.Gravity.END, android.support.v17.leanback.app.OnboardingFragment.DESCRIPTION_START_DELAY_MS));
            animators.add(createAnimator(mTitleView, true, android.view.Gravity.START, android.support.v17.leanback.app.OnboardingFragment.HEADER_APPEAR_DELAY_MS));
            animators.add(createAnimator(mDescriptionView, true, android.view.Gravity.START, android.support.v17.leanback.app.OnboardingFragment.HEADER_APPEAR_DELAY_MS + android.support.v17.leanback.app.OnboardingFragment.DESCRIPTION_START_DELAY_MS));
        }
        final int currentPageIndex = getCurrentPageIndex();
        fadeAnimator.addListener(new android.animation.AnimatorListenerAdapter() {
            @java.lang.Override
            public void onAnimationEnd(android.animation.Animator animation) {
                mTitleView.setText(getPageTitle(currentPageIndex));
                mDescriptionView.setText(getPageDescription(currentPageIndex));
            }
        });
        // Animator for switching between page indicator and button.
        if (getCurrentPageIndex() == (getPageCount() - 1)) {
            mStartButton.setVisibility(android.view.View.VISIBLE);
            android.animation.Animator navigatorFadeOutAnimator = android.animation.AnimatorInflater.loadAnimator(getActivity(), R.animator.lb_onboarding_page_indicator_fade_out);
            navigatorFadeOutAnimator.setTarget(mPageIndicator);
            navigatorFadeOutAnimator.addListener(new android.animation.AnimatorListenerAdapter() {
                @java.lang.Override
                public void onAnimationEnd(android.animation.Animator animation) {
                    mPageIndicator.setVisibility(android.view.View.GONE);
                }
            });
            animators.add(navigatorFadeOutAnimator);
            android.animation.Animator buttonFadeInAnimator = android.animation.AnimatorInflater.loadAnimator(getActivity(), R.animator.lb_onboarding_start_button_fade_in);
            buttonFadeInAnimator.setTarget(mStartButton);
            animators.add(buttonFadeInAnimator);
        } else
            if (previousPage == (getPageCount() - 1)) {
                mPageIndicator.setVisibility(android.view.View.VISIBLE);
                android.animation.Animator navigatorFadeInAnimator = android.animation.AnimatorInflater.loadAnimator(getActivity(), R.animator.lb_onboarding_page_indicator_fade_in);
                navigatorFadeInAnimator.setTarget(mPageIndicator);
                animators.add(navigatorFadeInAnimator);
                android.animation.Animator buttonFadeOutAnimator = android.animation.AnimatorInflater.loadAnimator(getActivity(), R.animator.lb_onboarding_start_button_fade_out);
                buttonFadeOutAnimator.setTarget(mStartButton);
                buttonFadeOutAnimator.addListener(new android.animation.AnimatorListenerAdapter() {
                    @java.lang.Override
                    public void onAnimationEnd(android.animation.Animator animation) {
                        mStartButton.setVisibility(android.view.View.GONE);
                    }
                });
                animators.add(buttonFadeOutAnimator);
            }

        mAnimator = new android.animation.AnimatorSet();
        mAnimator.playTogether(animators);
        mAnimator.start();
        onPageChanged(mCurrentPageIndex, previousPage);
    }

    /**
     * Called when the page has been changed.
     *
     * @param newPage
     * 		The new page.
     * @param previousPage
     * 		The previous page.
     */
    protected void onPageChanged(int newPage, int previousPage) {
    }

    private android.animation.Animator createAnimator(android.view.View view, boolean fadeIn, int slideDirection, long startDelay) {
        boolean isLtr = getView().getLayoutDirection() == android.view.View.LAYOUT_DIRECTION_LTR;
        boolean slideRight = ((isLtr && (slideDirection == android.view.Gravity.END)) || ((!isLtr) && (slideDirection == android.view.Gravity.START))) || (slideDirection == android.view.Gravity.RIGHT);
        android.animation.Animator fadeAnimator;
        android.animation.Animator slideAnimator;
        if (fadeIn) {
            fadeAnimator = android.animation.ObjectAnimator.ofFloat(view, android.view.View.ALPHA, 0.0F, 1.0F);
            slideAnimator = android.animation.ObjectAnimator.ofFloat(view, android.view.View.TRANSLATION_X, slideRight ? android.support.v17.leanback.app.OnboardingFragment.sSlideDistance : -android.support.v17.leanback.app.OnboardingFragment.sSlideDistance, 0);
            fadeAnimator.setInterpolator(android.support.v17.leanback.app.OnboardingFragment.HEADER_APPEAR_INTERPOLATOR);
            slideAnimator.setInterpolator(android.support.v17.leanback.app.OnboardingFragment.HEADER_APPEAR_INTERPOLATOR);
        } else {
            fadeAnimator = android.animation.ObjectAnimator.ofFloat(view, android.view.View.ALPHA, 1.0F, 0.0F);
            slideAnimator = android.animation.ObjectAnimator.ofFloat(view, android.view.View.TRANSLATION_X, 0, slideRight ? android.support.v17.leanback.app.OnboardingFragment.sSlideDistance : -android.support.v17.leanback.app.OnboardingFragment.sSlideDistance);
            fadeAnimator.setInterpolator(android.support.v17.leanback.app.OnboardingFragment.HEADER_DISAPPEAR_INTERPOLATOR);
            slideAnimator.setInterpolator(android.support.v17.leanback.app.OnboardingFragment.HEADER_DISAPPEAR_INTERPOLATOR);
        }
        fadeAnimator.setDuration(android.support.v17.leanback.app.OnboardingFragment.HEADER_ANIMATION_DURATION_MS);
        fadeAnimator.setTarget(view);
        slideAnimator.setDuration(android.support.v17.leanback.app.OnboardingFragment.HEADER_ANIMATION_DURATION_MS);
        slideAnimator.setTarget(view);
        android.animation.AnimatorSet animator = new android.animation.AnimatorSet();
        animator.playTogether(fadeAnimator, slideAnimator);
        if (startDelay > 0) {
            animator.setStartDelay(startDelay);
        }
        return animator;
    }
}

