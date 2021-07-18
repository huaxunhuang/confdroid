/**
 * Copyright (C) 2007 The Android Open Source Project
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
package android.widget;


public class ListViewTest extends android.test.InstrumentationTestCase {
    /**
     * If a view in a ListView requests a layout it should be remeasured.
     */
    @android.test.suitebuilder.annotation.MediumTest
    public void testRequestLayout() throws java.lang.Exception {
        android.test.mock.MockContext context = new android.widget.ListViewTest.MockContext2();
        android.widget.ListView listView = new android.widget.ListView(context);
        java.util.List<java.lang.String> items = com.google.android.collect.Lists.newArrayList("hello");
        android.widget.ListViewTest.Adapter<java.lang.String> adapter = new android.widget.ListViewTest.Adapter<java.lang.String>(context, 0, items);
        listView.setAdapter(adapter);
        int measureSpec = android.view.View.MeasureSpec.makeMeasureSpec(100, android.view.View.MeasureSpec.EXACTLY);
        adapter.notifyDataSetChanged();
        listView.measure(measureSpec, measureSpec);
        listView.layout(0, 0, 100, 100);
        android.widget.ListViewTest.MockView childView = ((android.widget.ListViewTest.MockView) (listView.getChildAt(0)));
        childView.requestLayout();
        childView.onMeasureCalled = false;
        listView.measure(measureSpec, measureSpec);
        listView.layout(0, 0, 100, 100);
        junit.framework.Assert.assertTrue(childView.onMeasureCalled);
    }

    /**
     * The list view should handle the disappearance of the only selected item, even when that item
     * was selected before its disappearance.
     */
    @android.test.suitebuilder.annotation.MediumTest
    public void testNoSelectableItems() throws java.lang.Exception {
        android.test.mock.MockContext context = new android.widget.ListViewTest.MockContext2();
        android.widget.ListView listView = new android.widget.ListView(context);
        // We use a header as the unselectable item to remain after the selectable one is removed.
        listView.addHeaderView(new android.view.View(context), null, false);
        java.util.List<java.lang.String> items = com.google.android.collect.Lists.newArrayList("hello");
        android.widget.ListViewTest.Adapter<java.lang.String> adapter = new android.widget.ListViewTest.Adapter<java.lang.String>(context, 0, items);
        listView.setAdapter(adapter);
        listView.setSelection(1);
        int measureSpec = android.view.View.MeasureSpec.makeMeasureSpec(100, android.view.View.MeasureSpec.EXACTLY);
        adapter.notifyDataSetChanged();
        listView.measure(measureSpec, measureSpec);
        listView.layout(0, 0, 100, 100);
        items.remove(0);
        adapter.notifyDataSetChanged();
        listView.measure(measureSpec, measureSpec);
        listView.layout(0, 0, 100, 100);
    }

    private class MockContext2 extends android.test.mock.MockContext {
        @java.lang.Override
        public android.content.res.Resources getResources() {
            return getResources();
        }

        @java.lang.Override
        public android.content.res.Resources.Theme getTheme() {
            return getInstrumentation().getTargetContext().getTheme();
        }

        @java.lang.Override
        public java.lang.Object getSystemService(java.lang.String name) {
            if (android.content.Context.LAYOUT_INFLATER_SERVICE.equals(name)) {
                return getSystemService(name);
            }
            return getSystemService(name);
        }
    }

    private class MockView extends android.view.View {
        public boolean onMeasureCalled = false;

        public MockView(android.content.Context context) {
            super(context);
        }

        @java.lang.Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            onMeasureCalled = true;
        }
    }

    private class Adapter<T> extends android.widget.ArrayAdapter<T> {
        public Adapter(android.content.Context context, int resource, java.util.List<T> objects) {
            super(context, resource, objects);
        }

        @java.lang.Override
        public android.view.View getView(int position, android.view.View convertView, android.view.ViewGroup parent) {
            return new android.widget.ListViewTest.MockView(getContext());
        }
    }
}

