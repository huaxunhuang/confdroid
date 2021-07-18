/**
 * Copyright (C) 2016 The Android Open Source Project
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
package android.content.res;


/**
 * Class that provides access to the {@link GradientColor#createFromXmlInner(Resources,
 * XmlPullParser, AttributeSet, Theme)} and {@link ColorStateList#createFromXmlInner(Resources,
 * XmlPullParser, AttributeSet, Theme)} methods
 */
public class ComplexColor_Accessor {
    public static android.content.res.GradientColor createGradientColorFromXmlInner(@android.annotation.NonNull
    android.content.res.Resources r, @android.annotation.NonNull
    org.xmlpull.v1.XmlPullParser parser, @android.annotation.NonNull
    android.util.AttributeSet attrs, @android.annotation.Nullable
    android.content.res.Resources.Theme theme) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        return android.content.res.GradientColor.createFromXmlInner(r, parser, attrs, theme);
    }

    public static android.content.res.ColorStateList createColorStateListFromXmlInner(@android.annotation.NonNull
    android.content.res.Resources r, @android.annotation.NonNull
    org.xmlpull.v1.XmlPullParser parser, @android.annotation.NonNull
    android.util.AttributeSet attrs, @android.annotation.Nullable
    android.content.res.Resources.Theme theme) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        return android.content.res.ColorStateList.createFromXmlInner(r, parser, attrs, theme);
    }
}

