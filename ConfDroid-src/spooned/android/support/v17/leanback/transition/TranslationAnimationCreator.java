package android.support.v17.leanback.transition;


/**
 * This class is used by Slide and Explode to create an animator that goes from the start
 * position to the end position. It takes into account the canceled position so that it
 * will not blink out or shift suddenly when the transition is interrupted.
 *
 * @unknown 
 */
@android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
class TranslationAnimationCreator {
    /**
     * Creates an animator that can be used for x and/or y translations. When interrupted,
     * it sets a tag to keep track of the position so that it may be continued from position.
     *
     * @param view
     * 		The view being moved. This may be in the overlay for onDisappear.
     * @param values
     * 		The values containing the view in the view hierarchy.
     * @param viewPosX
     * 		The x screen coordinate of view
     * @param viewPosY
     * 		The y screen coordinate of view
     * @param startX
     * 		The start translation x of view
     * @param startY
     * 		The start translation y of view
     * @param endX
     * 		The end translation x of view
     * @param endY
     * 		The end translation y of view
     * @param interpolator
     * 		The interpolator to use with this animator.
     * @return An animator that moves from (startX, startY) to (endX, endY) unless there was
    a previous interruption, in which case it moves from the current position to (endX, endY).
     */
    static android.animation.Animator createAnimation(android.view.View view, android.transition.TransitionValues values, int viewPosX, int viewPosY, float startX, float startY, float endX, float endY, android.animation.TimeInterpolator interpolator, android.transition.Transition transition) {
        float terminalX = view.getTranslationX();
        float terminalY = view.getTranslationY();
        int[] startPosition = ((int[]) (values.view.getTag(R.id.transitionPosition)));
        if (startPosition != null) {
            startX = (startPosition[0] - viewPosX) + terminalX;
            startY = (startPosition[1] - viewPosY) + terminalY;
        }
        // Initial position is at translation startX, startY, so position is offset by that amount
        int startPosX = viewPosX + java.lang.Math.round(startX - terminalX);
        int startPosY = viewPosY + java.lang.Math.round(startY - terminalY);
        view.setTranslationX(startX);
        view.setTranslationY(startY);
        if ((startX == endX) && (startY == endY)) {
            return null;
        }
        android.graphics.Path path = new android.graphics.Path();
        path.moveTo(startX, startY);
        path.lineTo(endX, endY);
        android.animation.ObjectAnimator anim = android.animation.ObjectAnimator.ofFloat(view, android.view.View.TRANSLATION_X, android.view.View.TRANSLATION_Y, path);
        android.support.v17.leanback.transition.TranslationAnimationCreator.TransitionPositionListener listener = new android.support.v17.leanback.transition.TranslationAnimationCreator.TransitionPositionListener(view, values.view, startPosX, startPosY, terminalX, terminalY);
        transition.addListener(listener);
        anim.addListener(listener);
        anim.addPauseListener(listener);
        anim.setInterpolator(interpolator);
        return anim;
    }

    private static class TransitionPositionListener extends android.animation.AnimatorListenerAdapter implements android.transition.Transition.TransitionListener {
        private final android.view.View mViewInHierarchy;

        private final android.view.View mMovingView;

        private final int mStartX;

        private final int mStartY;

        private int[] mTransitionPosition;

        private float mPausedX;

        private float mPausedY;

        private final float mTerminalX;

        private final float mTerminalY;

        TransitionPositionListener(android.view.View movingView, android.view.View viewInHierarchy, int startX, int startY, float terminalX, float terminalY) {
            mMovingView = movingView;
            mViewInHierarchy = viewInHierarchy;
            mStartX = startX - java.lang.Math.round(mMovingView.getTranslationX());
            mStartY = startY - java.lang.Math.round(mMovingView.getTranslationY());
            mTerminalX = terminalX;
            mTerminalY = terminalY;
            mTransitionPosition = ((int[]) (mViewInHierarchy.getTag(R.id.transitionPosition)));
            if (mTransitionPosition != null) {
                mViewInHierarchy.setTag(R.id.transitionPosition, null);
            }
        }

        @java.lang.Override
        public void onAnimationCancel(android.animation.Animator animation) {
            if (mTransitionPosition == null) {
                mTransitionPosition = new int[2];
            }
            mTransitionPosition[0] = java.lang.Math.round(mStartX + mMovingView.getTranslationX());
            mTransitionPosition[1] = java.lang.Math.round(mStartY + mMovingView.getTranslationY());
            mViewInHierarchy.setTag(R.id.transitionPosition, mTransitionPosition);
        }

        @java.lang.Override
        public void onAnimationEnd(android.animation.Animator animator) {
        }

        @java.lang.Override
        public void onAnimationPause(android.animation.Animator animator) {
            mPausedX = mMovingView.getTranslationX();
            mPausedY = mMovingView.getTranslationY();
            mMovingView.setTranslationX(mTerminalX);
            mMovingView.setTranslationY(mTerminalY);
        }

        @java.lang.Override
        public void onAnimationResume(android.animation.Animator animator) {
            mMovingView.setTranslationX(mPausedX);
            mMovingView.setTranslationY(mPausedY);
        }

        @java.lang.Override
        public void onTransitionStart(android.transition.Transition transition) {
        }

        @java.lang.Override
        public void onTransitionEnd(android.transition.Transition transition) {
            mMovingView.setTranslationX(mTerminalX);
            mMovingView.setTranslationY(mTerminalY);
        }

        @java.lang.Override
        public void onTransitionCancel(android.transition.Transition transition) {
        }

        @java.lang.Override
        public void onTransitionPause(android.transition.Transition transition) {
        }

        @java.lang.Override
        public void onTransitionResume(android.transition.Transition transition) {
        }
    }
}

