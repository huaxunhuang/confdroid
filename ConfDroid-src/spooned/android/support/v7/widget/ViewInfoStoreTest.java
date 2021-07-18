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
package android.support.v7.widget;


@java.lang.SuppressWarnings("ConstantConditions")
@org.junit.runner.RunWith(org.junit.runners.JUnit4.class)
@android.test.suitebuilder.annotation.SmallTest
public class ViewInfoStoreTest extends junit.framework.TestCase {
    android.support.v7.widget.ViewInfoStore mStore;

    android.support.v7.widget.ViewInfoStoreTest.LoggingProcessCallback mCallback;

    @org.junit.Before
    public void prepare() {
        mStore = new android.support.v7.widget.ViewInfoStore();
        mCallback = new android.support.v7.widget.ViewInfoStoreTest.LoggingProcessCallback();
    }

    @org.junit.Test
    public void addOverridePre() {
        android.support.v7.widget.RecyclerView.ViewHolder vh = new android.support.v7.widget.ViewInfoStoreTest.MockViewHolder();
        android.support.v7.widget.ViewInfoStoreTest.MockInfo info = new android.support.v7.widget.ViewInfoStoreTest.MockInfo();
        mStore.addToPreLayout(vh, info);
        android.support.v7.widget.ViewInfoStoreTest.MockInfo info2 = new android.support.v7.widget.ViewInfoStoreTest.MockInfo();
        mStore.addToPreLayout(vh, info2);
        junit.framework.TestCase.assertSame(info2, find(vh, android.support.v7.widget.ViewInfoStore.InfoRecord.FLAG_PRE));
    }

    @org.junit.Test
    public void addOverridePost() {
        android.support.v7.widget.RecyclerView.ViewHolder vh = new android.support.v7.widget.ViewInfoStoreTest.MockViewHolder();
        android.support.v7.widget.ViewInfoStoreTest.MockInfo info = new android.support.v7.widget.ViewInfoStoreTest.MockInfo();
        mStore.addToPostLayout(vh, info);
        android.support.v7.widget.ViewInfoStoreTest.MockInfo info2 = new android.support.v7.widget.ViewInfoStoreTest.MockInfo();
        mStore.addToPostLayout(vh, info2);
        junit.framework.TestCase.assertSame(info2, find(vh, android.support.v7.widget.ViewInfoStore.InfoRecord.FLAG_POST));
    }

    @org.junit.Test
    public void addRemoveAndReAdd() {
        android.support.v7.widget.RecyclerView.ViewHolder vh = new android.support.v7.widget.ViewInfoStoreTest.MockViewHolder();
        android.support.v7.widget.ViewInfoStoreTest.MockInfo pre = new android.support.v7.widget.ViewInfoStoreTest.MockInfo();
        mStore.addToPreLayout(vh, pre);
        android.support.v7.widget.ViewInfoStoreTest.MockInfo post1 = new android.support.v7.widget.ViewInfoStoreTest.MockInfo();
        mStore.addToPostLayout(vh, post1);
        mStore.onViewDetached(vh);
        mStore.addToDisappearedInLayout(vh);
    }

    @org.junit.Test
    public void addToPreLayout() {
        android.support.v7.widget.RecyclerView.ViewHolder vh = new android.support.v7.widget.ViewInfoStoreTest.MockViewHolder();
        android.support.v7.widget.ViewInfoStoreTest.MockInfo info = new android.support.v7.widget.ViewInfoStoreTest.MockInfo();
        mStore.addToPreLayout(vh, info);
        junit.framework.TestCase.assertSame(info, find(vh, android.support.v7.widget.ViewInfoStore.InfoRecord.FLAG_PRE));
        junit.framework.TestCase.assertTrue(mStore.isInPreLayout(vh));
        mStore.removeViewHolder(vh);
        junit.framework.TestCase.assertFalse(mStore.isInPreLayout(vh));
    }

    @org.junit.Test
    public void addToPostLayout() {
        android.support.v7.widget.RecyclerView.ViewHolder vh = new android.support.v7.widget.ViewInfoStoreTest.MockViewHolder();
        android.support.v7.widget.ViewInfoStoreTest.MockInfo info = new android.support.v7.widget.ViewInfoStoreTest.MockInfo();
        mStore.addToPostLayout(vh, info);
        junit.framework.TestCase.assertSame(info, find(vh, android.support.v7.widget.ViewInfoStore.InfoRecord.FLAG_POST));
        mStore.removeViewHolder(vh);
        junit.framework.TestCase.assertNull(find(vh, android.support.v7.widget.ViewInfoStore.InfoRecord.FLAG_POST));
    }

    @org.junit.Test
    public void popFromPreLayout() {
        junit.framework.TestCase.assertEquals(0, sizeOf(android.support.v7.widget.ViewInfoStore.InfoRecord.FLAG_PRE));
        android.support.v7.widget.RecyclerView.ViewHolder vh = new android.support.v7.widget.ViewInfoStoreTest.MockViewHolder();
        android.support.v7.widget.ViewInfoStoreTest.MockInfo info = new android.support.v7.widget.ViewInfoStoreTest.MockInfo();
        mStore.addToPreLayout(vh, info);
        junit.framework.TestCase.assertSame(info, mStore.popFromPreLayout(vh));
        junit.framework.TestCase.assertNull(mStore.popFromPreLayout(vh));
    }

    @org.junit.Test
    public void addToOldChangeHolders() {
        android.support.v7.widget.RecyclerView.ViewHolder vh = new android.support.v7.widget.ViewInfoStoreTest.MockViewHolder();
        mStore.addToOldChangeHolders(1, vh);
        junit.framework.TestCase.assertSame(vh, mStore.getFromOldChangeHolders(1));
        mStore.removeViewHolder(vh);
        junit.framework.TestCase.assertNull(mStore.getFromOldChangeHolders(1));
    }

    @org.junit.Test
    public void appearListTests() {
        android.support.v7.widget.RecyclerView.ViewHolder vh = new android.support.v7.widget.ViewInfoStoreTest.MockViewHolder();
        android.support.v7.widget.RecyclerView.ItemAnimator.ItemHolderInfo info = new android.support.v7.widget.ViewInfoStoreTest.MockInfo();
        mStore.addToAppearedInPreLayoutHolders(vh, info);
        junit.framework.TestCase.assertEquals(1, sizeOf(android.support.v7.widget.ViewInfoStore.InfoRecord.FLAG_APPEAR));
        android.support.v7.widget.RecyclerView.ViewHolder vh2 = new android.support.v7.widget.ViewInfoStoreTest.MockViewHolder();
        mStore.addToAppearedInPreLayoutHolders(vh2, info);
        junit.framework.TestCase.assertEquals(2, sizeOf(android.support.v7.widget.ViewInfoStore.InfoRecord.FLAG_APPEAR));
        mStore.removeViewHolder(vh2);
        junit.framework.TestCase.assertEquals(1, sizeOf(android.support.v7.widget.ViewInfoStore.InfoRecord.FLAG_APPEAR));
    }

    @org.junit.Test
    public void disappearListTest() {
        android.support.v7.widget.RecyclerView.ViewHolder vh = new android.support.v7.widget.ViewInfoStoreTest.MockViewHolder();
        mStore.addToDisappearedInLayout(vh);
        junit.framework.TestCase.assertEquals(1, sizeOf(android.support.v7.widget.ViewInfoStore.InfoRecord.FLAG_DISAPPEARED));
        mStore.addToDisappearedInLayout(vh);
        junit.framework.TestCase.assertEquals(1, sizeOf(android.support.v7.widget.ViewInfoStore.InfoRecord.FLAG_DISAPPEARED));
        android.support.v7.widget.RecyclerView.ViewHolder vh2 = new android.support.v7.widget.ViewInfoStoreTest.MockViewHolder();
        mStore.addToDisappearedInLayout(vh2);
        junit.framework.TestCase.assertEquals(2, sizeOf(android.support.v7.widget.ViewInfoStore.InfoRecord.FLAG_DISAPPEARED));
        mStore.removeViewHolder(vh2);
        junit.framework.TestCase.assertEquals(1, sizeOf(android.support.v7.widget.ViewInfoStore.InfoRecord.FLAG_DISAPPEARED));
        mStore.removeFromDisappearedInLayout(vh);
        junit.framework.TestCase.assertEquals(0, sizeOf(android.support.v7.widget.ViewInfoStore.InfoRecord.FLAG_DISAPPEARED));
    }

    @org.junit.Test
    public void processAppear() {
        android.support.v7.widget.RecyclerView.ViewHolder vh = new android.support.v7.widget.ViewInfoStoreTest.MockViewHolder();
        android.support.v7.widget.ViewInfoStoreTest.MockInfo info = new android.support.v7.widget.ViewInfoStoreTest.MockInfo();
        mStore.addToPostLayout(vh, info);
        mStore.process(mCallback);
        junit.framework.TestCase.assertEquals(new android.support.v4.util.Pair<>(null, info), mCallback.appeared.get(vh));
        junit.framework.TestCase.assertTrue(mCallback.disappeared.isEmpty());
        junit.framework.TestCase.assertTrue(mCallback.unused.isEmpty());
        junit.framework.TestCase.assertTrue(mCallback.persistent.isEmpty());
    }

    @org.junit.Test
    public void processDisappearNormal() {
        android.support.v7.widget.RecyclerView.ViewHolder vh = new android.support.v7.widget.ViewInfoStoreTest.MockViewHolder();
        android.support.v7.widget.ViewInfoStoreTest.MockInfo info = new android.support.v7.widget.ViewInfoStoreTest.MockInfo();
        mStore.addToPreLayout(vh, info);
        mStore.process(mCallback);
        junit.framework.TestCase.assertEquals(new android.support.v4.util.Pair<>(info, null), mCallback.disappeared.get(vh));
        junit.framework.TestCase.assertTrue(mCallback.appeared.isEmpty());
        junit.framework.TestCase.assertTrue(mCallback.unused.isEmpty());
        junit.framework.TestCase.assertTrue(mCallback.persistent.isEmpty());
    }

    @org.junit.Test
    public void processDisappearMissingLayout() {
        android.support.v7.widget.RecyclerView.ViewHolder vh = new android.support.v7.widget.ViewInfoStoreTest.MockViewHolder();
        android.support.v7.widget.ViewInfoStoreTest.MockInfo info = new android.support.v7.widget.ViewInfoStoreTest.MockInfo();
        mStore.addToPreLayout(vh, info);
        mStore.addToDisappearedInLayout(vh);
        mStore.process(mCallback);
        junit.framework.TestCase.assertEquals(new android.support.v4.util.Pair<>(info, null), mCallback.disappeared.get(vh));
        junit.framework.TestCase.assertTrue(mCallback.appeared.isEmpty());
        junit.framework.TestCase.assertTrue(mCallback.unused.isEmpty());
        junit.framework.TestCase.assertTrue(mCallback.persistent.isEmpty());
    }

    @org.junit.Test
    public void processDisappearMoveOut() {
        android.support.v7.widget.RecyclerView.ViewHolder vh = new android.support.v7.widget.ViewInfoStoreTest.MockViewHolder();
        android.support.v7.widget.ViewInfoStoreTest.MockInfo pre = new android.support.v7.widget.ViewInfoStoreTest.MockInfo();
        android.support.v7.widget.ViewInfoStoreTest.MockInfo post = new android.support.v7.widget.ViewInfoStoreTest.MockInfo();
        mStore.addToPreLayout(vh, pre);
        mStore.addToDisappearedInLayout(vh);
        mStore.addToPostLayout(vh, post);
        mStore.process(mCallback);
        junit.framework.TestCase.assertEquals(new android.support.v4.util.Pair<>(pre, post), mCallback.disappeared.get(vh));
        junit.framework.TestCase.assertTrue(mCallback.appeared.isEmpty());
        junit.framework.TestCase.assertTrue(mCallback.unused.isEmpty());
        junit.framework.TestCase.assertTrue(mCallback.persistent.isEmpty());
    }

    @org.junit.Test
    public void processDisappearAppear() {
        android.support.v7.widget.RecyclerView.ViewHolder vh = new android.support.v7.widget.ViewInfoStoreTest.MockViewHolder();
        android.support.v7.widget.ViewInfoStoreTest.MockInfo pre = new android.support.v7.widget.ViewInfoStoreTest.MockInfo();
        android.support.v7.widget.ViewInfoStoreTest.MockInfo post = new android.support.v7.widget.ViewInfoStoreTest.MockInfo();
        mStore.addToPreLayout(vh, pre);
        mStore.addToDisappearedInLayout(vh);
        mStore.addToPostLayout(vh, post);
        mStore.removeFromDisappearedInLayout(vh);
        mStore.process(mCallback);
        junit.framework.TestCase.assertTrue(mCallback.disappeared.isEmpty());
        junit.framework.TestCase.assertTrue(mCallback.appeared.isEmpty());
        junit.framework.TestCase.assertTrue(mCallback.unused.isEmpty());
        junit.framework.TestCase.assertEquals(mCallback.persistent.get(vh), new android.support.v4.util.Pair<>(pre, post));
    }

    @org.junit.Test
    public void processAppearAndDisappearInPostLayout() {
        android.support.v7.widget.RecyclerView.ViewHolder vh = new android.support.v7.widget.ViewInfoStoreTest.MockViewHolder();
        android.support.v7.widget.ViewInfoStoreTest.MockInfo info1 = new android.support.v7.widget.ViewInfoStoreTest.MockInfo();
        mStore.addToPostLayout(vh, info1);
        mStore.addToDisappearedInLayout(vh);
        mStore.process(mCallback);
        junit.framework.TestCase.assertTrue(mCallback.disappeared.isEmpty());
        junit.framework.TestCase.assertTrue(mCallback.appeared.isEmpty());
        junit.framework.TestCase.assertTrue(mCallback.persistent.isEmpty());
        junit.framework.TestCase.assertSame(mCallback.unused.get(0), vh);
    }

    static class MockViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
        public MockViewHolder() {
            super(new android.view.View(null));
        }
    }

    static class MockInfo extends android.support.v7.widget.RecyclerView.ItemAnimator.ItemHolderInfo {}

    private int sizeOf(int flags) {
        int cnt = 0;
        final int size = mStore.mLayoutHolderMap.size();
        for (int i = 0; i < size; i++) {
            android.support.v7.widget.ViewInfoStore.InfoRecord record = mStore.mLayoutHolderMap.valueAt(i);
            if ((record.flags & flags) != 0) {
                cnt++;
            }
        }
        return cnt;
    }

    private android.support.v7.widget.RecyclerView.ItemAnimator.ItemHolderInfo find(android.support.v7.widget.RecyclerView.ViewHolder viewHolder, int flags) {
        final int size = mStore.mLayoutHolderMap.size();
        for (int i = 0; i < size; i++) {
            android.support.v7.widget.ViewInfoStore.InfoRecord record = mStore.mLayoutHolderMap.valueAt(i);
            android.support.v7.widget.RecyclerView.ViewHolder holder = mStore.mLayoutHolderMap.keyAt(i);
            if (((record.flags & flags) != 0) && (holder == viewHolder)) {
                if ((flags == android.support.v7.widget.ViewInfoStore.InfoRecord.FLAG_PRE) || (flags == android.support.v7.widget.ViewInfoStore.InfoRecord.FLAG_APPEAR)) {
                    return record.preInfo;
                } else
                    if (flags == android.support.v7.widget.ViewInfoStore.InfoRecord.FLAG_POST) {
                        return record.postInfo;
                    }

                throw new java.lang.UnsupportedOperationException("don't know this flag");
            }
        }
        return null;
    }

    private static class LoggingProcessCallback implements android.support.v7.widget.ViewInfoStore.ProcessCallback {
        final java.util.Map<android.support.v7.widget.RecyclerView.ViewHolder, android.support.v4.util.Pair<android.support.v7.widget.RecyclerView.ItemAnimator.ItemHolderInfo, android.support.v7.widget.RecyclerView.ItemAnimator.ItemHolderInfo>> disappeared = new java.util.HashMap<>();

        final java.util.Map<android.support.v7.widget.RecyclerView.ViewHolder, android.support.v4.util.Pair<android.support.v7.widget.RecyclerView.ItemAnimator.ItemHolderInfo, android.support.v7.widget.RecyclerView.ItemAnimator.ItemHolderInfo>> appeared = new java.util.HashMap<>();

        final java.util.Map<android.support.v7.widget.RecyclerView.ViewHolder, android.support.v4.util.Pair<android.support.v7.widget.RecyclerView.ItemAnimator.ItemHolderInfo, android.support.v7.widget.RecyclerView.ItemAnimator.ItemHolderInfo>> persistent = new java.util.HashMap<>();

        final java.util.List<android.support.v7.widget.RecyclerView.ViewHolder> unused = new java.util.ArrayList<>();

        @java.lang.Override
        public void processDisappeared(android.support.v7.widget.RecyclerView.ViewHolder viewHolder, android.support.v7.widget.RecyclerView.ItemAnimator.ItemHolderInfo preInfo, @android.support.annotation.Nullable
        android.support.v7.widget.RecyclerView.ItemAnimator.ItemHolderInfo postInfo) {
            junit.framework.TestCase.assertNotNull(preInfo);
            junit.framework.TestCase.assertFalse(disappeared.containsKey(viewHolder));
            disappeared.put(viewHolder, new android.support.v4.util.Pair<>(preInfo, postInfo));
        }

        @java.lang.Override
        public void processAppeared(android.support.v7.widget.RecyclerView.ViewHolder viewHolder, @android.support.annotation.Nullable
        android.support.v7.widget.RecyclerView.ItemAnimator.ItemHolderInfo preInfo, @android.support.annotation.NonNull
        android.support.v7.widget.RecyclerView.ItemAnimator.ItemHolderInfo info) {
            junit.framework.TestCase.assertNotNull(info);
            junit.framework.TestCase.assertFalse(appeared.containsKey(viewHolder));
            appeared.put(viewHolder, new android.support.v4.util.Pair<>(preInfo, info));
        }

        @java.lang.Override
        public void processPersistent(android.support.v7.widget.RecyclerView.ViewHolder viewHolder, @android.support.annotation.NonNull
        android.support.v7.widget.RecyclerView.ItemAnimator.ItemHolderInfo preInfo, @android.support.annotation.NonNull
        android.support.v7.widget.RecyclerView.ItemAnimator.ItemHolderInfo postInfo) {
            junit.framework.TestCase.assertFalse(persistent.containsKey(viewHolder));
            junit.framework.TestCase.assertNotNull(preInfo);
            junit.framework.TestCase.assertNotNull(postInfo);
            persistent.put(viewHolder, new android.support.v4.util.Pair<>(preInfo, postInfo));
        }

        @java.lang.Override
        public void unused(android.support.v7.widget.RecyclerView.ViewHolder holder) {
            unused.add(holder);
        }
    }
}

