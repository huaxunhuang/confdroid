/**
 * Copyright (C) 2016 The Android Open Source Project
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
 * The view displaying 3 animated peak meters next to each other when a media item is playing.
 *
 * @unknown 
 */
@android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
public class MediaNowPlayingView extends android.widget.LinearLayout {
    private final android.widget.ImageView mImage1;

    private final android.widget.ImageView mImage2;

    private final android.widget.ImageView mImage3;

    private final android.animation.ObjectAnimator mObjectAnimator1;

    private final android.animation.ObjectAnimator mObjectAnimator2;

    private final android.animation.ObjectAnimator mObjectAnimator3;

    protected final android.view.animation.LinearInterpolator mLinearInterpolator = new android.view.animation.LinearInterpolator();

    public MediaNowPlayingView(android.content.Context context, android.util.AttributeSet attrs) {
        super(context, attrs);
        android.view.LayoutInflater.from(context).inflate(R.layout.lb_playback_now_playing_bars, this, true);
        mImage1 = ((android.widget.ImageView) (findViewById(R.id.bar1)));
        mImage2 = ((android.widget.ImageView) (findViewById(R.id.bar2)));
        mImage3 = ((android.widget.ImageView) (findViewById(R.id.bar3)));
        mImage1.setPivotY(mImage1.getDrawable().getIntrinsicHeight());
        mImage2.setPivotY(mImage2.getDrawable().getIntrinsicHeight());
        mImage3.setPivotY(mImage3.getDrawable().getIntrinsicHeight());
        android.support.v17.leanback.widget.MediaNowPlayingView.setDropScale(mImage1);
        android.support.v17.leanback.widget.MediaNowPlayingView.setDropScale(mImage2);
        android.support.v17.leanback.widget.MediaNowPlayingView.setDropScale(mImage3);
        mObjectAnimator1 = android.animation.ObjectAnimator.ofFloat(mImage1, "scaleY", 5.0F / 12.0F, 3.0F / 12.0F, 5.0F / 12.0F, 7.0F / 12.0F, 9.0F / 12.0F, 10.0F / 12.0F, 11.0F / 12.0F, 12.0F / 12.0F, 11.0F / 12.0F, 12.0F / 12.0F, 10.0F / 12.0F, 8.0F / 12.0F, 6.0F / 12.0F, 4.0F / 12.0F, 2.0F / 12.0F, 4.0F / 12.0F, 6.0F / 12.0F, 7.0F / 12.0F, 9.0F / 12.0F, 11.0F / 12.0F, 9.0F / 12.0F, 7.0F / 12.0F, 5.0F / 12.0F, 3.0F / 12.0F, 5.0F / 12.0F, 8.0F / 12.0F, 5.0F / 12.0F, 3.0F / 12.0F, 4.0F / 12.0F, 5.0F / 12.0F);
        mObjectAnimator1.setRepeatCount(android.animation.ValueAnimator.INFINITE);
        mObjectAnimator1.setDuration(2320);
        mObjectAnimator1.setInterpolator(mLinearInterpolator);
        mObjectAnimator2 = android.animation.ObjectAnimator.ofFloat(mImage2, "scaleY", 12.0F / 12.0F, 11.0F / 12.0F, 10.0F / 12.0F, 11.0F / 12.0F, 12.0F / 12.0F, 11.0F / 12.0F, 9.0F / 12.0F, 7.0F / 12.0F, 9.0F / 12.0F, 11.0F / 12.0F, 12.0F / 12.0F, 10.0F / 12.0F, 8.0F / 12.0F, 10.0F / 12.0F, 12.0F / 12.0F, 11.0F / 12.0F, 9.0F / 12.0F, 5.0F / 12.0F, 3.0F / 12.0F, 5.0F / 12.0F, 8.0F / 12.0F, 10.0F / 12.0F, 12.0F / 12.0F, 10.0F / 12.0F, 9.0F / 12.0F, 8.0F / 12.0F, 12.0F / 12.0F);
        mObjectAnimator2.setRepeatCount(android.animation.ValueAnimator.INFINITE);
        mObjectAnimator2.setDuration(2080);
        mObjectAnimator2.setInterpolator(mLinearInterpolator);
        mObjectAnimator3 = android.animation.ObjectAnimator.ofFloat(mImage3, "scaleY", 8.0F / 12.0F, 9.0F / 12.0F, 10.0F / 12.0F, 12.0F / 12.0F, 11.0F / 12.0F, 9.0F / 12.0F, 7.0F / 12.0F, 5.0F / 12.0F, 7.0F / 12.0F, 8.0F / 12.0F, 9.0F / 12.0F, 12.0F / 12.0F, 11.0F / 12.0F, 12.0F / 12.0F, 9.0F / 12.0F, 7.0F / 12.0F, 9.0F / 12.0F, 11.0F / 12.0F, 12.0F / 12.0F, 10.0F / 12.0F, 8.0F / 12.0F, 9.0F / 12.0F, 7.0F / 12.0F, 5.0F / 12.0F, 3.0F / 12.0F, 8.0F / 12.0F);
        mObjectAnimator3.setRepeatCount(android.animation.ValueAnimator.INFINITE);
        mObjectAnimator3.setDuration(2000);
        mObjectAnimator3.setInterpolator(mLinearInterpolator);
    }

    static void setDropScale(android.view.View view) {
        view.setScaleY(1.0F / 12.0F);
    }

    @java.lang.Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (visibility == android.view.View.GONE) {
            stopAnimation();
        } else {
            startAnimation();
        }
    }

    @java.lang.Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (getVisibility() == android.view.View.VISIBLE)
            startAnimation();

    }

    @java.lang.Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAnimation();
    }

    private void startAnimation() {
        startAnimation(mObjectAnimator1);
        startAnimation(mObjectAnimator2);
        startAnimation(mObjectAnimator3);
        mImage1.setVisibility(android.view.View.VISIBLE);
        mImage2.setVisibility(android.view.View.VISIBLE);
        mImage3.setVisibility(android.view.View.VISIBLE);
    }

    private void stopAnimation() {
        stopAnimation(mObjectAnimator1, mImage1);
        stopAnimation(mObjectAnimator2, mImage2);
        stopAnimation(mObjectAnimator3, mImage3);
        mImage1.setVisibility(android.view.View.GONE);
        mImage2.setVisibility(android.view.View.GONE);
        mImage3.setVisibility(android.view.View.GONE);
    }

    private void startAnimation(android.animation.Animator animator) {
        if (!animator.isStarted()) {
            animator.start();
        }
    }

    private void stopAnimation(android.animation.Animator animator, android.view.View view) {
        if (animator.isStarted()) {
            animator.cancel();
            android.support.v17.leanback.widget.MediaNowPlayingView.setDropScale(view);
        }
    }
}

