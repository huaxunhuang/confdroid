/**
 * *
 * * Copyright 2007, The Android Open Source Project
 * *
 * * Licensed under the Apache License, Version 2.0 (the "License");
 * * you may not use this file except in compliance with the License.
 * * You may obtain a copy of the License at
 * *
 * *     http://www.apache.org/licenses/LICENSE-2.0
 * *
 * * Unless required by applicable law or agreed to in writing, software
 * * distributed under the License is distributed on an "AS IS" BASIS,
 * * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * * See the License for the specific language governing permissions and
 * * limitations under the License.
 */
package android.widget;


/**
 * Allows the device admin to show certain dialogs. Should be integrated into settings.
 *
 * @deprecated {@hide }
 */
@java.lang.Deprecated
public class AppSecurityPermissions {
    /**
     * Utility to retrieve a view displaying a single permission.  This provides
     * the old UI layout for permissions; it is only here for the device admin
     * settings to continue to use.
     */
    public static android.view.View getPermissionItemView(android.content.Context context, java.lang.CharSequence grpName, java.lang.CharSequence description, boolean dangerous) {
        android.view.LayoutInflater inflater = ((android.view.LayoutInflater) (context.getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE)));
        android.graphics.drawable.Drawable icon = context.getDrawable(dangerous ? R.drawable.ic_bullet_key_permission : R.drawable.ic_text_dot);
        return android.widget.AppSecurityPermissions.getPermissionItemViewOld(context, inflater, grpName, description, dangerous, icon);
    }

    private static android.view.View getPermissionItemViewOld(android.content.Context context, android.view.LayoutInflater inflater, java.lang.CharSequence grpName, java.lang.CharSequence permList, boolean dangerous, android.graphics.drawable.Drawable icon) {
        android.view.View permView = inflater.inflate(R.layout.app_permission_item_old, null);
        android.widget.TextView permGrpView = permView.findViewById(R.id.permission_group);
        android.widget.TextView permDescView = permView.findViewById(R.id.permission_list);
        android.widget.ImageView imgView = ((android.widget.ImageView) (permView.findViewById(R.id.perm_icon)));
        imgView.setImageDrawable(icon);
        if (grpName != null) {
            permGrpView.setText(grpName);
            permDescView.setText(permList);
        } else {
            permGrpView.setText(permList);
            permDescView.setVisibility(android.view.View.GONE);
        }
        return permView;
    }
}

