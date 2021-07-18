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
 * limitations under the License
 */
package android.appwidget;


/**
 *
 *
 * @unknown 
 */
public class PendingHostUpdate implements android.os.Parcelable {
    static final int TYPE_VIEWS_UPDATE = 0;

    static final int TYPE_PROVIDER_CHANGED = 1;

    static final int TYPE_VIEW_DATA_CHANGED = 2;

    final int appWidgetId;

    final int type;

    android.widget.RemoteViews views;

    android.appwidget.AppWidgetProviderInfo widgetInfo;

    int viewId;

    public static android.appwidget.PendingHostUpdate updateAppWidget(int appWidgetId, android.widget.RemoteViews views) {
        android.appwidget.PendingHostUpdate update = new android.appwidget.PendingHostUpdate(appWidgetId, android.appwidget.PendingHostUpdate.TYPE_VIEWS_UPDATE);
        update.views = views;
        return update;
    }

    public static android.appwidget.PendingHostUpdate providerChanged(int appWidgetId, android.appwidget.AppWidgetProviderInfo info) {
        android.appwidget.PendingHostUpdate update = new android.appwidget.PendingHostUpdate(appWidgetId, android.appwidget.PendingHostUpdate.TYPE_PROVIDER_CHANGED);
        update.widgetInfo = info;
        return update;
    }

    public static android.appwidget.PendingHostUpdate viewDataChanged(int appWidgetId, int viewId) {
        android.appwidget.PendingHostUpdate update = new android.appwidget.PendingHostUpdate(appWidgetId, android.appwidget.PendingHostUpdate.TYPE_VIEW_DATA_CHANGED);
        update.viewId = viewId;
        return update;
    }

    private PendingHostUpdate(int appWidgetId, int type) {
        this.appWidgetId = appWidgetId;
        this.type = type;
    }

    private PendingHostUpdate(android.os.Parcel in) {
        appWidgetId = in.readInt();
        type = in.readInt();
        switch (type) {
            case android.appwidget.PendingHostUpdate.TYPE_VIEWS_UPDATE :
                if (0 != in.readInt()) {
                    views = new android.widget.RemoteViews(in);
                }
                break;
            case android.appwidget.PendingHostUpdate.TYPE_PROVIDER_CHANGED :
                if (0 != in.readInt()) {
                    widgetInfo = new android.appwidget.AppWidgetProviderInfo(in);
                }
                break;
            case android.appwidget.PendingHostUpdate.TYPE_VIEW_DATA_CHANGED :
                viewId = in.readInt();
        }
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeInt(appWidgetId);
        dest.writeInt(type);
        switch (type) {
            case android.appwidget.PendingHostUpdate.TYPE_VIEWS_UPDATE :
                writeNullParcelable(views, dest, flags);
                break;
            case android.appwidget.PendingHostUpdate.TYPE_PROVIDER_CHANGED :
                writeNullParcelable(widgetInfo, dest, flags);
                break;
            case android.appwidget.PendingHostUpdate.TYPE_VIEW_DATA_CHANGED :
                dest.writeInt(viewId);
                break;
        }
    }

    private void writeNullParcelable(android.os.Parcelable p, android.os.Parcel dest, int flags) {
        if (p != null) {
            dest.writeInt(1);
            p.writeToParcel(dest, flags);
        } else {
            dest.writeInt(0);
        }
    }

    /**
     * Parcelable.Creator that instantiates PendingHostUpdate objects
     */
    public static final android.os.Parcelable.Creator<android.appwidget.PendingHostUpdate> CREATOR = new android.os.Parcelable.Creator<android.appwidget.PendingHostUpdate>() {
        public android.appwidget.PendingHostUpdate createFromParcel(android.os.Parcel parcel) {
            return new android.appwidget.PendingHostUpdate(parcel);
        }

        public android.appwidget.PendingHostUpdate[] newArray(int size) {
            return new android.appwidget.PendingHostUpdate[size];
        }
    };
}

