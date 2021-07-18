/**
 * Copyright (C) 2013 The Android Open Source Project
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
package android.transition;


/**
 * This class inflates scenes and transitions from resource files.
 *
 * Information on XML resource descriptions for transitions can be found for
 * {@link android.R.styleable#Transition}, {@link android.R.styleable#TransitionSet},
 * {@link android.R.styleable#TransitionTarget}, {@link android.R.styleable#Fade},
 * and {@link android.R.styleable#TransitionManager}.
 */
public class TransitionInflater {
    private static final java.lang.Class<?>[] sConstructorSignature = new java.lang.Class[]{ android.content.Context.class, android.util.AttributeSet.class };

    private static final android.util.ArrayMap<java.lang.String, java.lang.reflect.Constructor> sConstructors = new android.util.ArrayMap<java.lang.String, java.lang.reflect.Constructor>();

    private android.content.Context mContext;

    private TransitionInflater(android.content.Context context) {
        mContext = context;
    }

    /**
     * Obtains the TransitionInflater from the given context.
     */
    public static android.transition.TransitionInflater from(android.content.Context context) {
        return new android.transition.TransitionInflater(context);
    }

    /**
     * Loads a {@link Transition} object from a resource
     *
     * @param resource
     * 		The resource id of the transition to load
     * @return The loaded Transition object
     * @throws android.content.res.Resources.NotFoundException
     * 		when the
     * 		transition cannot be loaded
     */
    public android.transition.Transition inflateTransition(@android.annotation.TransitionRes
    int resource) {
        // noinspection ResourceType
        android.content.res.XmlResourceParser parser = mContext.getResources().getXml(resource);
        try {
            return createTransitionFromXml(parser, android.util.Xml.asAttributeSet(parser), null);
        } catch (org.xmlpull.v1.XmlPullParserException e) {
            android.view.InflateException ex = new android.view.InflateException(e.getMessage());
            ex.initCause(e);
            throw ex;
        } catch (java.io.IOException e) {
            android.view.InflateException ex = new android.view.InflateException((parser.getPositionDescription() + ": ") + e.getMessage());
            ex.initCause(e);
            throw ex;
        } finally {
            parser.close();
        }
    }

    /**
     * Loads a {@link TransitionManager} object from a resource
     *
     * @param resource
     * 		The resource id of the transition manager to load
     * @return The loaded TransitionManager object
     * @throws android.content.res.Resources.NotFoundException
     * 		when the
     * 		transition manager cannot be loaded
     */
    public android.transition.TransitionManager inflateTransitionManager(@android.annotation.TransitionRes
    int resource, android.view.ViewGroup sceneRoot) {
        // noinspection ResourceType
        android.content.res.XmlResourceParser parser = mContext.getResources().getXml(resource);
        try {
            return createTransitionManagerFromXml(parser, android.util.Xml.asAttributeSet(parser), sceneRoot);
        } catch (org.xmlpull.v1.XmlPullParserException e) {
            android.view.InflateException ex = new android.view.InflateException(e.getMessage());
            ex.initCause(e);
            throw ex;
        } catch (java.io.IOException e) {
            android.view.InflateException ex = new android.view.InflateException((parser.getPositionDescription() + ": ") + e.getMessage());
            ex.initCause(e);
            throw ex;
        } finally {
            parser.close();
        }
    }

    // 
    // Transition loading
    // 
    private android.transition.Transition createTransitionFromXml(org.xmlpull.v1.XmlPullParser parser, android.util.AttributeSet attrs, android.transition.Transition parent) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        android.transition.Transition transition = null;
        // Make sure we are on a start tag.
        int type;
        int depth = parser.getDepth();
        android.transition.TransitionSet transitionSet = (parent instanceof android.transition.TransitionSet) ? ((android.transition.TransitionSet) (parent)) : null;
        while ((((type = parser.next()) != org.xmlpull.v1.XmlPullParser.END_TAG) || (parser.getDepth() > depth)) && (type != org.xmlpull.v1.XmlPullParser.END_DOCUMENT)) {
            if (type != org.xmlpull.v1.XmlPullParser.START_TAG) {
                continue;
            }
            java.lang.String name = parser.getName();
            if ("fade".equals(name)) {
                transition = new android.transition.Fade(mContext, attrs);
            } else
                if ("changeBounds".equals(name)) {
                    transition = new android.transition.ChangeBounds(mContext, attrs);
                } else
                    if ("slide".equals(name)) {
                        transition = new android.transition.Slide(mContext, attrs);
                    } else
                        if ("explode".equals(name)) {
                            transition = new android.transition.Explode(mContext, attrs);
                        } else
                            if ("changeImageTransform".equals(name)) {
                                transition = new android.transition.ChangeImageTransform(mContext, attrs);
                            } else
                                if ("changeTransform".equals(name)) {
                                    transition = new android.transition.ChangeTransform(mContext, attrs);
                                } else
                                    if ("changeClipBounds".equals(name)) {
                                        transition = new android.transition.ChangeClipBounds(mContext, attrs);
                                    } else
                                        if ("autoTransition".equals(name)) {
                                            transition = new android.transition.AutoTransition(mContext, attrs);
                                        } else
                                            if ("recolor".equals(name)) {
                                                transition = new android.transition.Recolor(mContext, attrs);
                                            } else
                                                if ("changeScroll".equals(name)) {
                                                    transition = new android.transition.ChangeScroll(mContext, attrs);
                                                } else
                                                    if ("transitionSet".equals(name)) {
                                                        transition = new android.transition.TransitionSet(mContext, attrs);
                                                    } else
                                                        if ("transition".equals(name)) {
                                                            transition = ((android.transition.Transition) (createCustom(attrs, android.transition.Transition.class, "transition")));
                                                        } else
                                                            if ("targets".equals(name)) {
                                                                getTargetIds(parser, attrs, parent);
                                                            } else
                                                                if ("arcMotion".equals(name)) {
                                                                    parent.setPathMotion(new android.transition.ArcMotion(mContext, attrs));
                                                                } else
                                                                    if ("pathMotion".equals(name)) {
                                                                        parent.setPathMotion(((android.transition.PathMotion) (createCustom(attrs, android.transition.PathMotion.class, "pathMotion"))));
                                                                    } else
                                                                        if ("patternPathMotion".equals(name)) {
                                                                            parent.setPathMotion(new android.transition.PatternPathMotion(mContext, attrs));
                                                                        } else {
                                                                            throw new java.lang.RuntimeException("Unknown scene name: " + parser.getName());
                                                                        }















            if (transition != null) {
                if (!parser.isEmptyElementTag()) {
                    createTransitionFromXml(parser, attrs, transition);
                }
                if (transitionSet != null) {
                    transitionSet.addTransition(transition);
                    transition = null;
                } else
                    if (parent != null) {
                        throw new android.view.InflateException("Could not add transition to another transition.");
                    }

            }
        } 
        return transition;
    }

    private java.lang.Object createCustom(android.util.AttributeSet attrs, java.lang.Class expectedType, java.lang.String tag) {
        java.lang.String className = attrs.getAttributeValue(null, "class");
        if (className == null) {
            throw new android.view.InflateException(tag + " tag must have a 'class' attribute");
        }
        try {
            synchronized(android.transition.TransitionInflater.sConstructors) {
                java.lang.reflect.Constructor constructor = android.transition.TransitionInflater.sConstructors.get(className);
                if (constructor == null) {
                    java.lang.Class c = mContext.getClassLoader().loadClass(className).asSubclass(expectedType);
                    if (c != null) {
                        constructor = c.getConstructor(android.transition.TransitionInflater.sConstructorSignature);
                        constructor.setAccessible(true);
                        android.transition.TransitionInflater.sConstructors.put(className, constructor);
                    }
                }
                return constructor.newInstance(mContext, attrs);
            }
        } catch (java.lang.InstantiationException e) {
            throw new android.view.InflateException((("Could not instantiate " + expectedType) + " class ") + className, e);
        } catch (java.lang.ClassNotFoundException e) {
            throw new android.view.InflateException((("Could not instantiate " + expectedType) + " class ") + className, e);
        } catch (java.lang.reflect.InvocationTargetException e) {
            throw new android.view.InflateException((("Could not instantiate " + expectedType) + " class ") + className, e);
        } catch (java.lang.NoSuchMethodException e) {
            throw new android.view.InflateException((("Could not instantiate " + expectedType) + " class ") + className, e);
        } catch (java.lang.IllegalAccessException e) {
            throw new android.view.InflateException((("Could not instantiate " + expectedType) + " class ") + className, e);
        }
    }

    private void getTargetIds(org.xmlpull.v1.XmlPullParser parser, android.util.AttributeSet attrs, android.transition.Transition transition) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        // Make sure we are on a start tag.
        int type;
        int depth = parser.getDepth();
        while ((((type = parser.next()) != org.xmlpull.v1.XmlPullParser.END_TAG) || (parser.getDepth() > depth)) && (type != org.xmlpull.v1.XmlPullParser.END_DOCUMENT)) {
            if (type != org.xmlpull.v1.XmlPullParser.START_TAG) {
                continue;
            }
            java.lang.String name = parser.getName();
            if (name.equals("target")) {
                android.content.res.TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.TransitionTarget);
                int id = a.getResourceId(R.styleable.TransitionTarget_targetId, 0);
                java.lang.String transitionName;
                if (id != 0) {
                    transition.addTarget(id);
                } else
                    if ((id = a.getResourceId(R.styleable.TransitionTarget_excludeId, 0)) != 0) {
                        transition.excludeTarget(id, true);
                    } else
                        if ((transitionName = a.getString(R.styleable.TransitionTarget_targetName)) != null) {
                            transition.addTarget(transitionName);
                        } else
                            if ((transitionName = a.getString(R.styleable.TransitionTarget_excludeName)) != null) {
                                transition.excludeTarget(transitionName, true);
                            } else {
                                java.lang.String className = a.getString(R.styleable.TransitionTarget_excludeClass);
                                try {
                                    if (className != null) {
                                        java.lang.Class clazz = java.lang.Class.forName(className);
                                        transition.excludeTarget(clazz, true);
                                    } else
                                        if ((className = a.getString(R.styleable.TransitionTarget_targetClass)) != null) {
                                            java.lang.Class clazz = java.lang.Class.forName(className);
                                            transition.addTarget(clazz);
                                        }

                                } catch (java.lang.ClassNotFoundException e) {
                                    a.recycle();
                                    throw new java.lang.RuntimeException("Could not create " + className, e);
                                }
                            }



                a.recycle();
            } else {
                throw new java.lang.RuntimeException("Unknown scene name: " + parser.getName());
            }
        } 
    }

    // 
    // TransitionManager loading
    // 
    private android.transition.TransitionManager createTransitionManagerFromXml(org.xmlpull.v1.XmlPullParser parser, android.util.AttributeSet attrs, android.view.ViewGroup sceneRoot) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        // Make sure we are on a start tag.
        int type;
        int depth = parser.getDepth();
        android.transition.TransitionManager transitionManager = null;
        while ((((type = parser.next()) != org.xmlpull.v1.XmlPullParser.END_TAG) || (parser.getDepth() > depth)) && (type != org.xmlpull.v1.XmlPullParser.END_DOCUMENT)) {
            if (type != org.xmlpull.v1.XmlPullParser.START_TAG) {
                continue;
            }
            java.lang.String name = parser.getName();
            if (name.equals("transitionManager")) {
                transitionManager = new android.transition.TransitionManager();
            } else
                if (name.equals("transition") && (transitionManager != null)) {
                    loadTransition(attrs, sceneRoot, transitionManager);
                } else {
                    throw new java.lang.RuntimeException("Unknown scene name: " + parser.getName());
                }

        } 
        return transitionManager;
    }

    private void loadTransition(android.util.AttributeSet attrs, android.view.ViewGroup sceneRoot, android.transition.TransitionManager transitionManager) throws android.content.res.Resources.NotFoundException {
        android.content.res.TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.TransitionManager);
        int transitionId = a.getResourceId(R.styleable.TransitionManager_transition, -1);
        int fromId = a.getResourceId(R.styleable.TransitionManager_fromScene, -1);
        android.transition.Scene fromScene = (fromId < 0) ? null : android.transition.Scene.getSceneForLayout(sceneRoot, fromId, mContext);
        int toId = a.getResourceId(R.styleable.TransitionManager_toScene, -1);
        android.transition.Scene toScene = (toId < 0) ? null : android.transition.Scene.getSceneForLayout(sceneRoot, toId, mContext);
        if (transitionId >= 0) {
            android.transition.Transition transition = inflateTransition(transitionId);
            if (transition != null) {
                if (toScene == null) {
                    throw new java.lang.RuntimeException("No toScene for transition ID " + transitionId);
                }
                if (fromScene == null) {
                    transitionManager.setTransition(toScene, transition);
                } else {
                    transitionManager.setTransition(fromScene, toScene, transition);
                }
            }
        }
        a.recycle();
    }
}

