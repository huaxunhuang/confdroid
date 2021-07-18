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
package android.databinding.tool.expr;


public class ResourceExpr extends android.databinding.tool.expr.Expr {
    private static final java.util.Map<java.lang.String, java.lang.String> RESOURCE_TYPE_TO_R_OBJECT;

    static {
        RESOURCE_TYPE_TO_R_OBJECT = new java.util.HashMap<java.lang.String, java.lang.String>();
        android.databinding.tool.expr.ResourceExpr.RESOURCE_TYPE_TO_R_OBJECT.put("colorStateList", "color  ");
        android.databinding.tool.expr.ResourceExpr.RESOURCE_TYPE_TO_R_OBJECT.put("dimenOffset", "dimen  ");
        android.databinding.tool.expr.ResourceExpr.RESOURCE_TYPE_TO_R_OBJECT.put("dimenSize", "dimen  ");
        android.databinding.tool.expr.ResourceExpr.RESOURCE_TYPE_TO_R_OBJECT.put("intArray", "array  ");
        android.databinding.tool.expr.ResourceExpr.RESOURCE_TYPE_TO_R_OBJECT.put("stateListAnimator", "animator  ");
        android.databinding.tool.expr.ResourceExpr.RESOURCE_TYPE_TO_R_OBJECT.put("stringArray", "array  ");
        android.databinding.tool.expr.ResourceExpr.RESOURCE_TYPE_TO_R_OBJECT.put("typedArray", "array");
    }

    // lazily initialized
    private java.util.Map<java.lang.String, android.databinding.tool.reflection.ModelClass> mResourceToTypeMapping;

    protected final java.lang.String mPackage;

    protected final java.lang.String mResourceType;

    protected final java.lang.String mResourceId;

    public ResourceExpr(java.lang.String packageName, java.lang.String resourceType, java.lang.String resourceName, java.util.List<android.databinding.tool.expr.Expr> args) {
        super(args);
        if ("android".equals(packageName)) {
            mPackage = "android.";
        } else {
            mPackage = "";
        }
        mResourceType = resourceType;
        mResourceId = resourceName;
    }

    private java.util.Map<java.lang.String, android.databinding.tool.reflection.ModelClass> getResourceToTypeMapping(android.databinding.tool.reflection.ModelAnalyzer modelAnalyzer) {
        if (mResourceToTypeMapping == null) {
            final java.util.Map<java.lang.String, java.lang.String> imports = getModel().getImports();
            mResourceToTypeMapping = new java.util.HashMap<java.lang.String, android.databinding.tool.reflection.ModelClass>();
            mResourceToTypeMapping.put("anim", modelAnalyzer.findClass("android.view.animation.Animation", imports));
            mResourceToTypeMapping.put("animator", modelAnalyzer.findClass("android.animation.Animator", imports));
            mResourceToTypeMapping.put("colorStateList", modelAnalyzer.findClass("android.content.res.ColorStateList", imports));
            mResourceToTypeMapping.put("drawable", modelAnalyzer.findClass("android.graphics.drawable.Drawable", imports));
            mResourceToTypeMapping.put("stateListAnimator", modelAnalyzer.findClass("android.animation.StateListAnimator", imports));
            mResourceToTypeMapping.put("transition", modelAnalyzer.findClass("android.transition.Transition", imports));
            mResourceToTypeMapping.put("typedArray", modelAnalyzer.findClass("android.content.res.TypedArray", imports));
            mResourceToTypeMapping.put("interpolator", modelAnalyzer.findClass("android.view.animation.Interpolator", imports));
            mResourceToTypeMapping.put("bool", modelAnalyzer.findClass(boolean.class));
            mResourceToTypeMapping.put("color", modelAnalyzer.findClass(int.class));
            mResourceToTypeMapping.put("dimenOffset", modelAnalyzer.findClass(int.class));
            mResourceToTypeMapping.put("dimenSize", modelAnalyzer.findClass(int.class));
            mResourceToTypeMapping.put("id", modelAnalyzer.findClass(int.class));
            mResourceToTypeMapping.put("integer", modelAnalyzer.findClass(int.class));
            mResourceToTypeMapping.put("layout", modelAnalyzer.findClass(int.class));
            mResourceToTypeMapping.put("dimen", modelAnalyzer.findClass(float.class));
            mResourceToTypeMapping.put("fraction", modelAnalyzer.findClass(float.class));
            mResourceToTypeMapping.put("intArray", modelAnalyzer.findClass(int[].class));
            mResourceToTypeMapping.put("string", modelAnalyzer.findClass(java.lang.String.class));
            mResourceToTypeMapping.put("stringArray", modelAnalyzer.findClass(java.lang.String[].class));
        }
        return mResourceToTypeMapping;
    }

    @java.lang.Override
    protected android.databinding.tool.reflection.ModelClass resolveType(android.databinding.tool.reflection.ModelAnalyzer modelAnalyzer) {
        final java.util.Map<java.lang.String, android.databinding.tool.reflection.ModelClass> mapping = getResourceToTypeMapping(modelAnalyzer);
        final android.databinding.tool.reflection.ModelClass modelClass = mapping.get(mResourceType);
        if (modelClass != null) {
            return modelClass;
        }
        if ("plurals".equals(mResourceType)) {
            if (getChildren().isEmpty()) {
                return modelAnalyzer.findClass(int.class);
            } else {
                return modelAnalyzer.findClass(java.lang.String.class);
            }
        }
        return modelAnalyzer.findClass(mResourceType, getModel().getImports());
    }

    @java.lang.Override
    protected java.util.List<android.databinding.tool.expr.Dependency> constructDependencies() {
        return constructDynamicChildrenDependencies();
    }

    @java.lang.Override
    protected java.lang.String computeUniqueKey() {
        java.lang.String base;
        if (mPackage == null) {
            base = (("@" + mResourceType) + "/") + mResourceId;
        } else {
            base = ((("@" + "android:") + mResourceType) + "/") + mResourceId;
        }
        return android.databinding.tool.expr.Expr.join(base, computeChildrenKey());
    }

    @java.lang.Override
    protected android.databinding.tool.writer.KCode generateCode(boolean expand) {
        return new android.databinding.tool.writer.KCode(toJava());
    }

    public java.lang.String getResourceId() {
        return (((mPackage + "R.") + getResourceObject()) + ".") + mResourceId;
    }

    @java.lang.Override
    public java.lang.String getInvertibleError() {
        return "Resources may not be the target of a two-way binding expression: " + computeUniqueKey();
    }

    public java.lang.String toJava() {
        final java.lang.String context = "getRoot().getContext()";
        final java.lang.String resources = "getRoot().getResources()";
        final java.lang.String resourceName = (((mPackage + "R.") + getResourceObject()) + ".") + mResourceId;
        if ("anim".equals(mResourceType))
            return ((("android.view.animation.AnimationUtils.loadAnimation(" + context) + ", ") + resourceName) + ")";

        if ("animator".equals(mResourceType))
            return ((("android.animation.AnimatorInflater.loadAnimator(" + context) + ", ") + resourceName) + ")";

        if ("bool".equals(mResourceType))
            return ((resources + ".getBoolean(") + resourceName) + ")";

        if ("color".equals(mResourceType))
            return ("android.databinding.DynamicUtil.getColorFromResource(getRoot(), " + resourceName) + ")";

        if ("colorStateList".equals(mResourceType))
            return ("getColorStateListFromResource(" + resourceName) + ")";

        if ("dimen".equals(mResourceType))
            return ((resources + ".getDimension(") + resourceName) + ")";

        if ("dimenOffset".equals(mResourceType))
            return ((resources + ".getDimensionPixelOffset(") + resourceName) + ")";

        if ("dimenSize".equals(mResourceType))
            return ((resources + ".getDimensionPixelSize(") + resourceName) + ")";

        if ("drawable".equals(mResourceType))
            return ("getDrawableFromResource(" + resourceName) + ")";

        if ("fraction".equals(mResourceType)) {
            java.lang.String base = getChildCode(0, "1");
            java.lang.String pbase = getChildCode(1, "1");
            return ((((((resources + ".getFraction(") + resourceName) + ", ") + base) + ", ") + pbase) + ")";
        }
        if ("id".equals(mResourceType))
            return resourceName;

        if ("intArray".equals(mResourceType))
            return ((resources + ".getIntArray(") + resourceName) + ")";

        if ("integer".equals(mResourceType))
            return ((resources + ".getInteger(") + resourceName) + ")";

        if ("interpolator".equals(mResourceType))
            return ((("android.view.animation.AnimationUtils.loadInterpolator(" + context) + ", ") + resourceName) + ")";

        if ("layout".equals(mResourceType))
            return resourceName;

        if ("plurals".equals(mResourceType)) {
            if (getChildren().isEmpty()) {
                return resourceName;
            } else {
                return makeParameterCall(resourceName, "getQuantityString");
            }
        }
        if ("stateListAnimator".equals(mResourceType))
            return ((("android.animation.AnimatorInflater.loadStateListAnimator(" + context) + ", ") + resourceName) + ")";

        if ("string".equals(mResourceType))
            return makeParameterCall(resourceName, "getString");

        if ("stringArray".equals(mResourceType))
            return ((resources + ".getStringArray(") + resourceName) + ")";

        if ("transition".equals(mResourceType))
            return ((("android.transition.TransitionInflater.from(" + context) + ").inflateTransition(") + resourceName) + ")";

        if ("typedArray".equals(mResourceType))
            return ((resources + ".obtainTypedArray(") + resourceName) + ")";

        final java.lang.String property = java.lang.Character.toUpperCase(mResourceType.charAt(0)) + mResourceType.substring(1);
        return ((((resources + ".get") + property) + "(") + resourceName) + ")";
    }

    private java.lang.String getChildCode(int childIndex, java.lang.String defaultValue) {
        if (getChildren().size() <= childIndex) {
            return defaultValue;
        } else {
            return getChildren().get(childIndex).toCode().generate();
        }
    }

    private java.lang.String makeParameterCall(java.lang.String resourceName, java.lang.String methodCall) {
        java.lang.StringBuilder sb = new java.lang.StringBuilder("getRoot().getResources().");
        sb.append(methodCall).append("(").append(resourceName);
        for (android.databinding.tool.expr.Expr expr : getChildren()) {
            sb.append(", ").append(expr.toCode().generate());
        }
        sb.append(")");
        return sb.toString();
    }

    private java.lang.String getResourceObject() {
        java.lang.String rFileObject = android.databinding.tool.expr.ResourceExpr.RESOURCE_TYPE_TO_R_OBJECT.get(mResourceType);
        if (rFileObject == null) {
            rFileObject = mResourceType;
        }
        return rFileObject;
    }
}

