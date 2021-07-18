/**
 * Copyright (C) 2011 The Android Open Source Project
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
package android.support.v4.widget;


/**
 * Helper for accessing {@link android.widget.EdgeEffect} introduced after
 * API level 4 in a backwards compatible fashion.
 *
 * This class is used to access {@link android.widget.EdgeEffect} on platform versions
 * that support it. When running on older platforms it will result in no-ops. It should
 * be used by views that wish to use the standard Android visual effects at the edges
 * of scrolling containers.
 */
public final class EdgeEffectCompat {
    private java.lang.Object mEdgeEffect;

    private static final android.support.v4.widget.EdgeEffectCompat.EdgeEffectImpl IMPL;

    static {
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            IMPL = new android.support.v4.widget.EdgeEffectCompat.EdgeEffectLollipopImpl();// Lollipop

        } else
            if (android.os.Build.VERSION.SDK_INT >= 14) {
                // ICS
                IMPL = new android.support.v4.widget.EdgeEffectCompat.EdgeEffectIcsImpl();
            } else {
                IMPL = new android.support.v4.widget.EdgeEffectCompat.BaseEdgeEffectImpl();
            }

    }

    interface EdgeEffectImpl {
        public java.lang.Object newEdgeEffect(android.content.Context context);

        public void setSize(java.lang.Object edgeEffect, int width, int height);

        public boolean isFinished(java.lang.Object edgeEffect);

        public void finish(java.lang.Object edgeEffect);

        public boolean onPull(java.lang.Object edgeEffect, float deltaDistance);

        public boolean onRelease(java.lang.Object edgeEffect);

        public boolean onAbsorb(java.lang.Object edgeEffect, int velocity);

        public boolean draw(java.lang.Object edgeEffect, android.graphics.Canvas canvas);

        public boolean onPull(java.lang.Object edgeEffect, float deltaDistance, float displacement);
    }

    /**
     * Null implementation to use pre-ICS
     */
    static class BaseEdgeEffectImpl implements android.support.v4.widget.EdgeEffectCompat.EdgeEffectImpl {
        @java.lang.Override
        public java.lang.Object newEdgeEffect(android.content.Context context) {
            return null;
        }

        @java.lang.Override
        public void setSize(java.lang.Object edgeEffect, int width, int height) {
        }

        @java.lang.Override
        public boolean isFinished(java.lang.Object edgeEffect) {
            return true;
        }

        @java.lang.Override
        public void finish(java.lang.Object edgeEffect) {
        }

        @java.lang.Override
        public boolean onPull(java.lang.Object edgeEffect, float deltaDistance) {
            return false;
        }

        @java.lang.Override
        public boolean onRelease(java.lang.Object edgeEffect) {
            return false;
        }

        @java.lang.Override
        public boolean onAbsorb(java.lang.Object edgeEffect, int velocity) {
            return false;
        }

        @java.lang.Override
        public boolean draw(java.lang.Object edgeEffect, android.graphics.Canvas canvas) {
            return false;
        }

        @java.lang.Override
        public boolean onPull(java.lang.Object edgeEffect, float deltaDistance, float displacement) {
            return false;
        }
    }

    static class EdgeEffectIcsImpl implements android.support.v4.widget.EdgeEffectCompat.EdgeEffectImpl {
        @java.lang.Override
        public java.lang.Object newEdgeEffect(android.content.Context context) {
            return android.support.v4.widget.EdgeEffectCompatIcs.newEdgeEffect(context);
        }

        @java.lang.Override
        public void setSize(java.lang.Object edgeEffect, int width, int height) {
            android.support.v4.widget.EdgeEffectCompatIcs.setSize(edgeEffect, width, height);
        }

        @java.lang.Override
        public boolean isFinished(java.lang.Object edgeEffect) {
            return android.support.v4.widget.EdgeEffectCompatIcs.isFinished(edgeEffect);
        }

        @java.lang.Override
        public void finish(java.lang.Object edgeEffect) {
            android.support.v4.widget.EdgeEffectCompatIcs.finish(edgeEffect);
        }

        @java.lang.Override
        public boolean onPull(java.lang.Object edgeEffect, float deltaDistance) {
            return android.support.v4.widget.EdgeEffectCompatIcs.onPull(edgeEffect, deltaDistance);
        }

        @java.lang.Override
        public boolean onRelease(java.lang.Object edgeEffect) {
            return android.support.v4.widget.EdgeEffectCompatIcs.onRelease(edgeEffect);
        }

        @java.lang.Override
        public boolean onAbsorb(java.lang.Object edgeEffect, int velocity) {
            return android.support.v4.widget.EdgeEffectCompatIcs.onAbsorb(edgeEffect, velocity);
        }

        @java.lang.Override
        public boolean draw(java.lang.Object edgeEffect, android.graphics.Canvas canvas) {
            return android.support.v4.widget.EdgeEffectCompatIcs.draw(edgeEffect, canvas);
        }

        @java.lang.Override
        public boolean onPull(java.lang.Object edgeEffect, float deltaDistance, float displacement) {
            return android.support.v4.widget.EdgeEffectCompatIcs.onPull(edgeEffect, deltaDistance);
        }
    }

    static class EdgeEffectLollipopImpl extends android.support.v4.widget.EdgeEffectCompat.EdgeEffectIcsImpl {
        @java.lang.Override
        public boolean onPull(java.lang.Object edgeEffect, float deltaDistance, float displacement) {
            return android.support.v4.widget.EdgeEffectCompatLollipop.onPull(edgeEffect, deltaDistance, displacement);
        }
    }

    /**
     * Construct a new EdgeEffect themed using the given context.
     *
     * <p>Note: On platform versions that do not support EdgeEffect, all operations
     * on the newly constructed object will be mocked/no-ops.</p>
     *
     * @param context
     * 		Context to use for theming the effect
     */
    public EdgeEffectCompat(android.content.Context context) {
        mEdgeEffect = android.support.v4.widget.EdgeEffectCompat.IMPL.newEdgeEffect(context);
    }

    /**
     * Set the size of this edge effect in pixels.
     *
     * @param width
     * 		Effect width in pixels
     * @param height
     * 		Effect height in pixels
     */
    public void setSize(int width, int height) {
        android.support.v4.widget.EdgeEffectCompat.IMPL.setSize(mEdgeEffect, width, height);
    }

    /**
     * Reports if this EdgeEffectCompat's animation is finished. If this method returns false
     * after a call to {@link #draw(Canvas)} the host widget should schedule another
     * drawing pass to continue the animation.
     *
     * @return true if animation is finished, false if drawing should continue on the next frame.
     */
    public boolean isFinished() {
        return android.support.v4.widget.EdgeEffectCompat.IMPL.isFinished(mEdgeEffect);
    }

    /**
     * Immediately finish the current animation.
     * After this call {@link #isFinished()} will return true.
     */
    public void finish() {
        android.support.v4.widget.EdgeEffectCompat.IMPL.finish(mEdgeEffect);
    }

    /**
     * A view should call this when content is pulled away from an edge by the user.
     * This will update the state of the current visual effect and its associated animation.
     * The host view should always {@link android.view.View#invalidate()} if this method
     * returns true and draw the results accordingly.
     *
     * @param deltaDistance
     * 		Change in distance since the last call. Values may be 0 (no change) to
     * 		1.f (full length of the view) or negative values to express change
     * 		back toward the edge reached to initiate the effect.
     * @return true if the host view should call invalidate, false if it should not.
     * @deprecated use {@link #onPull(float, float)}
     */
    @java.lang.Deprecated
    public boolean onPull(float deltaDistance) {
        return android.support.v4.widget.EdgeEffectCompat.IMPL.onPull(mEdgeEffect, deltaDistance);
    }

    /**
     * A view should call this when content is pulled away from an edge by the user.
     * This will update the state of the current visual effect and its associated animation.
     * The host view should always {@link android.view.View#invalidate()} if this method
     * returns true and draw the results accordingly.
     *
     * @param deltaDistance
     * 		Change in distance since the last call. Values may be 0 (no change) to
     * 		1.f (full length of the view) or negative values to express change
     * 		back toward the edge reached to initiate the effect.
     * @param displacement
     * 		The displacement from the starting side of the effect of the point
     * 		initiating the pull. In the case of touch this is the finger position.
     * 		Values may be from 0-1.
     * @return true if the host view should call invalidate, false if it should not.
     */
    public boolean onPull(float deltaDistance, float displacement) {
        return android.support.v4.widget.EdgeEffectCompat.IMPL.onPull(mEdgeEffect, deltaDistance, displacement);
    }

    /**
     * Call when the object is released after being pulled.
     * This will begin the "decay" phase of the effect. After calling this method
     * the host view should {@link android.view.View#invalidate()} if this method
     * returns true and thereby draw the results accordingly.
     *
     * @return true if the host view should invalidate, false if it should not.
     */
    public boolean onRelease() {
        return android.support.v4.widget.EdgeEffectCompat.IMPL.onRelease(mEdgeEffect);
    }

    /**
     * Call when the effect absorbs an impact at the given velocity.
     * Used when a fling reaches the scroll boundary.
     *
     * <p>When using a {@link android.widget.Scroller} or {@link android.widget.OverScroller},
     * the method <code>getCurrVelocity</code> will provide a reasonable approximation
     * to use here.</p>
     *
     * @param velocity
     * 		Velocity at impact in pixels per second.
     * @return true if the host view should invalidate, false if it should not.
     */
    public boolean onAbsorb(int velocity) {
        return android.support.v4.widget.EdgeEffectCompat.IMPL.onAbsorb(mEdgeEffect, velocity);
    }

    /**
     * Draw into the provided canvas. Assumes that the canvas has been rotated
     * accordingly and the size has been set. The effect will be drawn the full
     * width of X=0 to X=width, beginning from Y=0 and extending to some factor <
     * 1.f of height.
     *
     * @param canvas
     * 		Canvas to draw into
     * @return true if drawing should continue beyond this frame to continue the
    animation
     */
    public boolean draw(android.graphics.Canvas canvas) {
        return android.support.v4.widget.EdgeEffectCompat.IMPL.draw(mEdgeEffect, canvas);
    }
}

