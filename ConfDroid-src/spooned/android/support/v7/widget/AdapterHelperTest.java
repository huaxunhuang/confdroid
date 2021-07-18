/**
 * Copyright (C) 2014 The Android Open Source Project
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


@org.junit.runner.RunWith(org.junit.runners.JUnit4.class)
@android.test.suitebuilder.annotation.SmallTest
public class AdapterHelperTest extends android.test.AndroidTestCase {
    private static final boolean DEBUG = false;

    private boolean mCollectLogs = false;

    private static final java.lang.String TAG = "AHT";

    java.util.List<android.support.v7.widget.AdapterHelperTest.MockViewHolder> mViewHolders;

    android.support.v7.widget.AdapterHelper mAdapterHelper;

    java.util.List<android.support.v7.widget.AdapterHelper.UpdateOp> mFirstPassUpdates;

    java.util.List<android.support.v7.widget.AdapterHelper.UpdateOp> mSecondPassUpdates;

    android.support.v7.widget.AdapterHelperTest.TestAdapter mTestAdapter;

    android.support.v7.widget.AdapterHelperTest.TestAdapter mPreProcessClone;// we clone adapter pre-process to run operations to see result


    private java.util.List<android.support.v7.widget.AdapterHelperTest.TestAdapter.Item> mPreLayoutItems;

    private java.lang.StringBuilder mLog = new java.lang.StringBuilder();

    @java.lang.Override
    public void run(junit.framework.TestResult result) {
        super.run(result);
        if (!result.wasSuccessful()) {
            result.addFailure(this, new junit.framework.AssertionFailedError(mLog.toString()));
        }
    }

    @org.junit.Before
    public void cleanState() {
        mLog.setLength(0);
        mPreLayoutItems = new java.util.ArrayList<android.support.v7.widget.AdapterHelperTest.TestAdapter.Item>();
        mViewHolders = new java.util.ArrayList<android.support.v7.widget.AdapterHelperTest.MockViewHolder>();
        mFirstPassUpdates = new java.util.ArrayList<android.support.v7.widget.AdapterHelper.UpdateOp>();
        mSecondPassUpdates = new java.util.ArrayList<android.support.v7.widget.AdapterHelper.UpdateOp>();
        mPreProcessClone = null;
        mAdapterHelper = new android.support.v7.widget.AdapterHelper(new android.support.v7.widget.AdapterHelper.Callback() {
            @java.lang.Override
            public android.support.v7.widget.RecyclerView.ViewHolder findViewHolder(int position) {
                for (android.support.v7.widget.RecyclerView.ViewHolder vh : mViewHolders) {
                    if ((vh.mPosition == position) && (!vh.isRemoved())) {
                        return vh;
                    }
                }
                return null;
            }

            @java.lang.Override
            public void offsetPositionsForRemovingInvisible(int positionStart, int itemCount) {
                final int positionEnd = positionStart + itemCount;
                for (android.support.v7.widget.RecyclerView.ViewHolder holder : mViewHolders) {
                    if (holder.mPosition >= positionEnd) {
                        holder.offsetPosition(-itemCount, true);
                    } else
                        if (holder.mPosition >= positionStart) {
                            holder.flagRemovedAndOffsetPosition(positionStart - 1, -itemCount, true);
                        }

                }
            }

            @java.lang.Override
            public void offsetPositionsForRemovingLaidOutOrNewView(int positionStart, int itemCount) {
                final int positionEnd = positionStart + itemCount;
                for (android.support.v7.widget.RecyclerView.ViewHolder holder : mViewHolders) {
                    if (holder.mPosition >= positionEnd) {
                        holder.offsetPosition(-itemCount, false);
                    } else
                        if (holder.mPosition >= positionStart) {
                            holder.flagRemovedAndOffsetPosition(positionStart - 1, -itemCount, false);
                        }

                }
            }

            @java.lang.Override
            public void markViewHoldersUpdated(int positionStart, int itemCount, java.lang.Object payload) {
                final int positionEnd = positionStart + itemCount;
                for (android.support.v7.widget.RecyclerView.ViewHolder holder : mViewHolders) {
                    if ((holder.mPosition >= positionStart) && (holder.mPosition < positionEnd)) {
                        holder.addFlags(android.support.v7.widget.RecyclerView.ViewHolder.FLAG_UPDATE);
                        holder.addChangePayload(payload);
                    }
                }
            }

            @java.lang.Override
            public void onDispatchFirstPass(android.support.v7.widget.AdapterHelper.UpdateOp updateOp) {
                if (android.support.v7.widget.AdapterHelperTest.DEBUG) {
                    log("first pass:" + updateOp.toString());
                }
                for (android.support.v7.widget.RecyclerView.ViewHolder viewHolder : mViewHolders) {
                    for (int i = 0; i < updateOp.itemCount; i++) {
                        // events are dispatched before view holders are updated for consistency
                        junit.framework.TestCase.assertFalse("update op should not match any existing view holders", viewHolder.getLayoutPosition() == (updateOp.positionStart + i));
                    }
                }
                mFirstPassUpdates.add(updateOp);
            }

            @java.lang.Override
            public void onDispatchSecondPass(android.support.v7.widget.AdapterHelper.UpdateOp updateOp) {
                if (android.support.v7.widget.AdapterHelperTest.DEBUG) {
                    log("second pass:" + updateOp.toString());
                }
                mSecondPassUpdates.add(updateOp);
            }

            @java.lang.Override
            public void offsetPositionsForAdd(int positionStart, int itemCount) {
                for (android.support.v7.widget.RecyclerView.ViewHolder holder : mViewHolders) {
                    if ((holder != null) && (holder.mPosition >= positionStart)) {
                        holder.offsetPosition(itemCount, false);
                    }
                }
            }

            @java.lang.Override
            public void offsetPositionsForMove(int from, int to) {
                final int start;
                final int end;
                final int inBetweenOffset;
                if (from < to) {
                    start = from;
                    end = to;
                    inBetweenOffset = -1;
                } else {
                    start = to;
                    end = from;
                    inBetweenOffset = 1;
                }
                for (android.support.v7.widget.RecyclerView.ViewHolder holder : mViewHolders) {
                    if (((holder == null) || (holder.mPosition < start)) || (holder.mPosition > end)) {
                        continue;
                    }
                    if (holder.mPosition == from) {
                        holder.offsetPosition(to - from, false);
                    } else {
                        holder.offsetPosition(inBetweenOffset, false);
                    }
                }
            }
        }, true);
    }

    void log(java.lang.String msg) {
        if (mCollectLogs) {
            mLog.append(msg).append("\n");
        } else {
            android.util.Log.d(android.support.v7.widget.AdapterHelperTest.TAG, msg);
        }
    }

    void setupBasic(int count, int visibleStart, int visibleCount) {
        if (android.support.v7.widget.AdapterHelperTest.DEBUG) {
            log(((((("setupBasic(" + count) + ",") + visibleStart) + ",") + visibleCount) + ");");
        }
        mTestAdapter = new android.support.v7.widget.AdapterHelperTest.TestAdapter(count, mAdapterHelper);
        for (int i = 0; i < visibleCount; i++) {
            addViewHolder(visibleStart + i);
        }
        mPreProcessClone = mTestAdapter.createCopy();
    }

    private void addViewHolder(int position) {
        android.support.v7.widget.AdapterHelperTest.MockViewHolder viewHolder = new android.support.v7.widget.AdapterHelperTest.MockViewHolder(new android.widget.TextView(getContext()));
        viewHolder.mPosition = position;
        viewHolder.mItem = mTestAdapter.mItems.get(position);
        mViewHolders.add(viewHolder);
    }

    @org.junit.Test
    public void testChangeAll() throws java.lang.Exception {
        try {
            setupBasic(5, 0, 3);
            up(0, 5);
            mAdapterHelper.preProcess();
        } catch (java.lang.Throwable t) {
            throw new java.lang.Exception(mLog.toString());
        }
    }

    @org.junit.Test
    public void testFindPositionOffsetInPreLayout() {
        setupBasic(50, 25, 10);
        rm(24, 5);
        mAdapterHelper.preProcess();
        // since 25 is invisible, we offset by one while checking
        junit.framework.TestCase.assertEquals("find position for view 23", 23, mAdapterHelper.findPositionOffset(23));
        junit.framework.TestCase.assertEquals("find position for view 24", -1, mAdapterHelper.findPositionOffset(24));
        junit.framework.TestCase.assertEquals("find position for view 25", -1, mAdapterHelper.findPositionOffset(25));
        junit.framework.TestCase.assertEquals("find position for view 26", -1, mAdapterHelper.findPositionOffset(26));
        junit.framework.TestCase.assertEquals("find position for view 27", -1, mAdapterHelper.findPositionOffset(27));
        junit.framework.TestCase.assertEquals("find position for view 28", 24, mAdapterHelper.findPositionOffset(28));
        junit.framework.TestCase.assertEquals("find position for view 29", 25, mAdapterHelper.findPositionOffset(29));
    }

    @org.junit.Test
    public void testNotifyAfterPre() {
        setupBasic(10, 2, 3);
        add(2, 1);
        mAdapterHelper.preProcess();
        add(3, 1);
        mAdapterHelper.consumeUpdatesInOnePass();
        mPreProcessClone.applyOps(mFirstPassUpdates, mTestAdapter);
        mPreProcessClone.applyOps(mSecondPassUpdates, mTestAdapter);
        assertAdaptersEqual(mTestAdapter, mPreProcessClone);
    }

    @org.junit.Test
    public void testSinglePass() {
        setupBasic(10, 2, 3);
        add(2, 1);
        rm(1, 2);
        add(1, 5);
        mAdapterHelper.consumeUpdatesInOnePass();
        assertDispatch(0, 3);
    }

    @org.junit.Test
    public void testDeleteVisible() {
        setupBasic(10, 2, 3);
        rm(2, 1);
        preProcess();
        assertDispatch(0, 1);
    }

    @org.junit.Test
    public void testDeleteInvisible() {
        setupBasic(10, 3, 4);
        rm(2, 1);
        preProcess();
        assertDispatch(1, 0);
    }

    @org.junit.Test
    public void testAddCount() {
        setupBasic(0, 0, 0);
        add(0, 1);
        junit.framework.TestCase.assertEquals(1, mAdapterHelper.mPendingUpdates.size());
    }

    @org.junit.Test
    public void testDeleteCount() {
        setupBasic(1, 0, 0);
        rm(0, 1);
        junit.framework.TestCase.assertEquals(1, mAdapterHelper.mPendingUpdates.size());
    }

    @org.junit.Test
    public void testAddProcess() {
        setupBasic(0, 0, 0);
        add(0, 1);
        preProcess();
        junit.framework.TestCase.assertEquals(0, mAdapterHelper.mPendingUpdates.size());
    }

    @org.junit.Test
    public void testAddRemoveSeparate() {
        setupBasic(10, 2, 2);
        add(6, 1);
        rm(5, 1);
        preProcess();
        assertDispatch(1, 1);
    }

    @org.junit.Test
    public void testScenario1() {
        setupBasic(10, 3, 2);
        rm(4, 1);
        rm(3, 1);
        rm(3, 1);
        preProcess();
        assertDispatch(1, 2);
    }

    @org.junit.Test
    public void testDivideDelete() {
        setupBasic(10, 3, 4);
        rm(2, 2);
        preProcess();
        assertDispatch(1, 1);
    }

    @org.junit.Test
    public void testScenario2() {
        setupBasic(10, 3, 3);// 3-4-5

        add(4, 2);// 3 a b 4 5

        rm(0, 1);// (0) 3(2) a(3) b(4) 4(3) 5(4)

        rm(1, 3);// (1,2) (x) a(1) b(2) 4(3)

        preProcess();
        assertDispatch(2, 2);
    }

    @org.junit.Test
    public void testScenario3() {
        setupBasic(10, 2, 2);
        rm(0, 5);
        preProcess();
        assertDispatch(2, 1);
        assertOps(mFirstPassUpdates, rmOp(0, 2), rmOp(2, 1));
        assertOps(mSecondPassUpdates, rmOp(0, 2));
    }

    // TODO test MOVE then remove items in between.
    // TODO test MOVE then remove it, make sure it is not dispatched
    @org.junit.Test
    public void testScenario4() {
        setupBasic(5, 0, 5);
        // 0 1 2 3 4
        // 0 1 2 a b 3 4
        // 0 2 a b 3 4
        // 0 c d 2 a b 3 4
        // 0 c d 2 a 4
        // c d 2 a 4
        // pre: 0 1 2 3 4
        add(3, 2);
        rm(1, 1);
        add(1, 2);
        rm(5, 2);
        rm(0, 1);
        preProcess();
    }

    @org.junit.Test
    public void testScenario5() {
        setupBasic(5, 0, 5);
        // 0 1 2 3 4
        // 0 1 2 a b 3 4
        // 0 1 b 3 4
        // pre: 0 1 2 3 4
        // pre w/ adap: 0 1 2 b 3 4
        add(3, 2);
        rm(2, 2);
        preProcess();
    }

    @org.junit.Test
    public void testScenario6() {
        // setupBasic(47, 19, 24);
        // mv(11, 12);
        // add(24, 16);
        // rm(9, 3);
        setupBasic(10, 5, 3);
        mv(2, 3);
        add(6, 4);
        rm(4, 1);
        preProcess();
    }

    @org.junit.Test
    public void testScenario8() {
        setupBasic(68, 51, 13);
        mv(22, 11);
        mv(22, 52);
        rm(37, 19);
        add(12, 38);
        preProcess();
    }

    @org.junit.Test
    public void testScenario9() {
        setupBasic(44, 3, 7);
        add(7, 21);
        rm(31, 3);
        rm(32, 11);
        mv(29, 5);
        mv(30, 32);
        add(25, 32);
        rm(15, 66);
        preProcess();
    }

    @org.junit.Test
    public void testScenario10() {
        setupBasic(14, 10, 3);
        rm(4, 4);
        add(5, 11);
        mv(5, 18);
        rm(2, 9);
        preProcess();
    }

    @org.junit.Test
    public void testScenario11() {
        setupBasic(78, 3, 64);
        mv(34, 28);
        add(1, 11);
        rm(9, 74);
        preProcess();
    }

    @org.junit.Test
    public void testScenario12() {
        setupBasic(38, 9, 7);
        rm(26, 3);
        mv(29, 15);
        rm(30, 1);
        preProcess();
    }

    @org.junit.Test
    public void testScenario13() {
        setupBasic(49, 41, 3);
        rm(30, 13);
        add(4, 10);
        mv(3, 38);
        mv(20, 17);
        rm(18, 23);
        preProcess();
    }

    @org.junit.Test
    public void testScenario14() {
        setupBasic(24, 3, 11);
        rm(2, 15);
        mv(2, 1);
        add(2, 34);
        add(11, 3);
        rm(10, 25);
        rm(13, 6);
        rm(4, 4);
        rm(6, 4);
        preProcess();
    }

    @org.junit.Test
    public void testScenario15() {
        setupBasic(10, 8, 1);
        mv(6, 1);
        mv(1, 4);
        rm(3, 1);
        preProcess();
    }

    @org.junit.Test
    public void testScenario16() {
        setupBasic(10, 3, 3);
        rm(2, 1);
        rm(1, 7);
        rm(0, 1);
        preProcess();
    }

    @org.junit.Test
    public void testScenario17() {
        setupBasic(10, 8, 1);
        mv(1, 0);
        mv(5, 1);
        rm(1, 7);
        preProcess();
    }

    @org.junit.Test
    public void testScenario18() throws java.lang.InterruptedException {
        setupBasic(10, 1, 4);
        add(2, 11);
        rm(16, 1);
        add(3, 1);
        rm(9, 10);
        preProcess();
    }

    @org.junit.Test
    public void testScenario19() {
        setupBasic(10, 8, 1);
        mv(9, 7);
        mv(9, 3);
        rm(5, 4);
        preProcess();
    }

    @org.junit.Test
    public void testScenario20() {
        setupBasic(10, 7, 1);
        mv(9, 1);
        mv(3, 9);
        rm(7, 2);
        preProcess();
    }

    @org.junit.Test
    public void testScenario21() {
        setupBasic(10, 5, 2);
        mv(1, 0);
        mv(9, 1);
        rm(2, 3);
        preProcess();
    }

    @org.junit.Test
    public void testScenario22() {
        setupBasic(10, 7, 2);
        add(2, 16);
        mv(20, 9);
        rm(17, 6);
        preProcess();
    }

    @org.junit.Test
    public void testScenario23() {
        setupBasic(10, 5, 3);
        mv(9, 6);
        add(4, 15);
        rm(21, 3);
        preProcess();
    }

    @org.junit.Test
    public void testScenario24() {
        setupBasic(10, 1, 6);
        add(6, 5);
        mv(14, 6);
        rm(7, 6);
        preProcess();
    }

    @org.junit.Test
    public void testScenario25() {
        setupBasic(10, 3, 4);
        mv(3, 9);
        rm(5, 4);
        preProcess();
    }

    @org.junit.Test
    public void testScenario25a() {
        setupBasic(10, 3, 4);
        rm(6, 4);
        mv(3, 5);
        preProcess();
    }

    @org.junit.Test
    public void testScenario26() {
        setupBasic(10, 4, 4);
        rm(3, 5);
        mv(2, 0);
        mv(1, 0);
        rm(1, 1);
        mv(0, 2);
        preProcess();
    }

    @org.junit.Test
    public void testScenario27() {
        setupBasic(10, 0, 3);
        mv(9, 4);
        mv(8, 4);
        add(7, 6);
        rm(5, 5);
        preProcess();
    }

    @org.junit.Test
    public void testScenerio28() {
        setupBasic(10, 4, 1);
        mv(8, 6);
        rm(8, 1);
        mv(7, 5);
        rm(3, 3);
        rm(1, 4);
        preProcess();
    }

    @org.junit.Test
    public void testScenerio29() {
        setupBasic(10, 6, 3);
        mv(3, 6);
        up(6, 2);
        add(5, 5);
    }

    @org.junit.Test
    public void testScenerio30() throws java.lang.InterruptedException {
        mCollectLogs = true;
        setupBasic(10, 3, 1);
        rm(3, 2);
        rm(2, 5);
        preProcess();
    }

    @org.junit.Test
    public void testScenerio31() throws java.lang.InterruptedException {
        mCollectLogs = true;
        setupBasic(10, 3, 1);
        rm(3, 1);
        rm(2, 3);
        preProcess();
    }

    @org.junit.Test
    public void testScenerio32() {
        setupBasic(10, 8, 1);
        add(9, 2);
        add(7, 39);
        up(0, 39);
        mv(36, 20);
        add(1, 48);
        mv(22, 98);
        mv(96, 29);
        up(36, 29);
        add(60, 36);
        add(127, 34);
        rm(142, 22);
        up(12, 69);
        up(116, 13);
        up(118, 19);
        mv(94, 69);
        up(98, 21);
        add(89, 18);
        rm(94, 70);
        up(71, 8);
        rm(54, 26);
        add(2, 20);
        mv(78, 84);
        mv(56, 2);
        mv(1, 79);
        rm(76, 7);
        rm(57, 12);
        rm(30, 27);
        add(24, 13);
        add(21, 5);
        rm(11, 27);
        rm(32, 1);
        up(0, 5);
        mv(14, 9);
        rm(15, 12);
        up(19, 1);
        rm(7, 1);
        mv(10, 4);
        up(4, 3);
        rm(16, 1);
        up(13, 5);
        up(2, 8);
        add(10, 19);
        add(15, 42);
        preProcess();
    }

    @org.junit.Test
    public void testScenerio33() throws java.lang.Throwable {
        try {
            mCollectLogs = true;
            setupBasic(10, 7, 1);
            mv(0, 6);
            up(0, 7);
            preProcess();
        } catch (java.lang.Throwable t) {
            throw new java.lang.Throwable((t.getMessage() + "\n") + mLog.toString());
        }
    }

    @org.junit.Test
    public void testScenerio34() {
        setupBasic(10, 6, 1);
        mv(9, 7);
        rm(5, 2);
        up(4, 3);
        preProcess();
    }

    @org.junit.Test
    public void testScenerio35() {
        setupBasic(10, 4, 4);
        mv(1, 4);
        up(2, 7);
        up(0, 1);
        preProcess();
    }

    @org.junit.Test
    public void testScenerio36() {
        setupBasic(10, 7, 2);
        rm(4, 1);
        mv(1, 6);
        up(4, 4);
        preProcess();
    }

    @org.junit.Test
    public void testScenerio37() throws java.lang.Throwable {
        try {
            mCollectLogs = true;
            setupBasic(10, 5, 2);
            mv(3, 6);
            rm(4, 4);
            rm(3, 2);
            preProcess();
        } catch (java.lang.Throwable t) {
            throw new java.lang.Throwable((t.getMessage() + "\n") + mLog.toString());
        }
    }

    @org.junit.Test
    public void testScenerio38() {
        setupBasic(10, 2, 2);
        add(0, 24);
        rm(26, 4);
        rm(1, 24);
        preProcess();
    }

    @org.junit.Test
    public void testScenerio39() {
        setupBasic(10, 7, 1);
        mv(0, 2);
        rm(8, 1);
        rm(2, 6);
        preProcess();
    }

    @org.junit.Test
    public void testScenerio40() {
        setupBasic(10, 5, 3);
        rm(5, 4);
        mv(0, 5);
        rm(2, 3);
        preProcess();
    }

    @org.junit.Test
    public void testScenerio41() {
        setupBasic(10, 7, 2);
        mv(4, 9);
        rm(0, 6);
        rm(0, 1);
        preProcess();
    }

    @org.junit.Test
    public void testScenerio42() {
        setupBasic(10, 6, 2);
        mv(5, 9);
        rm(5, 1);
        rm(2, 6);
        preProcess();
    }

    @org.junit.Test
    public void testScenerio43() {
        setupBasic(10, 1, 6);
        mv(6, 8);
        rm(3, 5);
        up(3, 1);
        preProcess();
    }

    @org.junit.Test
    public void testScenerio44() {
        setupBasic(10, 5, 2);
        mv(6, 4);
        mv(4, 1);
        rm(5, 3);
        preProcess();
    }

    @org.junit.Test
    public void testScenerio45() {
        setupBasic(10, 4, 2);
        rm(1, 4);
        preProcess();
    }

    @org.junit.Test
    public void testScenerio46() {
        setupBasic(10, 4, 3);
        up(6, 1);
        mv(8, 0);
        rm(2, 7);
        preProcess();
    }

    @org.junit.Test
    public void testMoveAdded() {
        setupBasic(10, 2, 2);
        add(3, 5);
        mv(4, 2);
        preProcess();
    }

    @org.junit.Test
    public void testPayloads() {
        setupBasic(10, 2, 2);
        up(3, 3, "payload");
        preProcess();
        assertOps(mFirstPassUpdates, upOp(4, 2, "payload"));
        assertOps(mSecondPassUpdates, upOp(3, 1, "payload"));
    }

    @org.junit.Test
    public void testRandom() throws java.lang.Throwable {
        mCollectLogs = true;
        java.util.Random random = new java.util.Random(java.lang.System.nanoTime());
        for (int i = 0; i < 100; i++) {
            try {
                android.util.Log.d(android.support.v7.widget.AdapterHelperTest.TAG, "running random test " + i);
                randomTest(random, java.lang.Math.max(40, 10 + nextInt(random, i)));
            } catch (java.lang.Throwable t) {
                throw new java.lang.Throwable((((("failure at random test " + i) + "\n") + t.getMessage()) + "\n") + mLog.toString(), t);
            }
        }
    }

    public void randomTest(java.util.Random random, int opCount) {
        cleanState();
        if (android.support.v7.widget.AdapterHelperTest.DEBUG) {
            log("randomTest");
        }
        final int count = 10;// + nextInt(random,100);

        final int start = nextInt(random, count - 1);
        final int layoutCount = java.lang.Math.max(1, nextInt(random, count - start));
        setupBasic(count, start, layoutCount);
        while ((opCount--) > 0) {
            final int op = nextInt(random, 5);
            switch (op) {
                case 0 :
                    if (mTestAdapter.mItems.size() > 1) {
                        int s = nextInt(random, mTestAdapter.mItems.size() - 1);
                        int len = java.lang.Math.max(1, nextInt(random, mTestAdapter.mItems.size() - s));
                        rm(s, len);
                    }
                    break;
                case 1 :
                    int s = (mTestAdapter.mItems.size() == 0) ? 0 : nextInt(random, mTestAdapter.mItems.size());
                    add(s, nextInt(random, 50));
                    break;
                case 2 :
                    if (mTestAdapter.mItems.size() >= 2) {
                        int from = nextInt(random, mTestAdapter.mItems.size());
                        int to;
                        do {
                            to = nextInt(random, mTestAdapter.mItems.size());
                        } while (to == from );
                        mv(from, to);
                    }
                    break;
                case 3 :
                    if (mTestAdapter.mItems.size() > 1) {
                        s = nextInt(random, mTestAdapter.mItems.size() - 1);
                        int len = java.lang.Math.max(1, nextInt(random, mTestAdapter.mItems.size() - s));
                        up(s, len);
                    }
                    break;
                case 4 :
                    if (mTestAdapter.mItems.size() > 1) {
                        s = nextInt(random, mTestAdapter.mItems.size() - 1);
                        int len = java.lang.Math.max(1, nextInt(random, mTestAdapter.mItems.size() - s));
                        up(s, len, java.lang.Integer.toString(s));
                    }
                    break;
            }
        } 
        preProcess();
    }

    int nextInt(java.util.Random random, int n) {
        if (n == 0) {
            return 0;
        }
        return random.nextInt(n);
    }

    public void assertOps(java.util.List<android.support.v7.widget.AdapterHelper.UpdateOp> actual, android.support.v7.widget.AdapterHelper.UpdateOp... expected) {
        junit.framework.TestCase.assertEquals(expected.length, actual.size());
        for (int i = 0; i < expected.length; i++) {
            junit.framework.TestCase.assertEquals(expected[i], actual.get(i));
        }
    }

    void assertDispatch(int firstPass, int secondPass) {
        junit.framework.TestCase.assertEquals(firstPass, mFirstPassUpdates.size());
        junit.framework.TestCase.assertEquals(secondPass, mSecondPassUpdates.size());
    }

    void preProcess() {
        for (android.support.v7.widget.AdapterHelperTest.MockViewHolder vh : mViewHolders) {
            final int ind = mTestAdapter.mItems.indexOf(vh.mItem);
            junit.framework.TestCase.assertEquals("actual adapter position should match", ind, mAdapterHelper.applyPendingUpdatesToPosition(vh.mPosition));
        }
        mAdapterHelper.preProcess();
        for (int i = 0; i < mPreProcessClone.mItems.size(); i++) {
            android.support.v7.widget.AdapterHelperTest.TestAdapter.Item item = mPreProcessClone.mItems.get(i);
            final int preLayoutIndex = mPreLayoutItems.indexOf(item);
            final int endIndex = mTestAdapter.mItems.indexOf(item);
            if (preLayoutIndex != (-1)) {
                junit.framework.TestCase.assertEquals((((("find position offset should work properly for existing elements" + i) + " at pre layout position ") + preLayoutIndex) + " and post layout position ") + endIndex, endIndex, mAdapterHelper.findPositionOffset(preLayoutIndex));
            }
        }
        // make sure visible view holders still have continuous positions
        final java.lang.StringBuilder vhLogBuilder = new java.lang.StringBuilder();
        for (android.support.v7.widget.RecyclerView.ViewHolder vh : mViewHolders) {
            vhLogBuilder.append("\n").append(vh.toString());
        }
        if (mViewHolders.size() > 0) {
            final java.lang.String vhLog = vhLogBuilder.toString();
            final int start = mViewHolders.get(0).getLayoutPosition();
            for (int i = 1; i < mViewHolders.size(); i++) {
                junit.framework.TestCase.assertEquals("view holder positions should be continious in pre-layout" + vhLog, start + i, mViewHolders.get(i).getLayoutPosition());
            }
        }
        mAdapterHelper.consumePostponedUpdates();
        // now assert these two adapters have identical data.
        mPreProcessClone.applyOps(mFirstPassUpdates, mTestAdapter);
        mPreProcessClone.applyOps(mSecondPassUpdates, mTestAdapter);
        assertAdaptersEqual(mTestAdapter, mPreProcessClone);
    }

    private void assertAdaptersEqual(android.support.v7.widget.AdapterHelperTest.TestAdapter a1, android.support.v7.widget.AdapterHelperTest.TestAdapter a2) {
        junit.framework.TestCase.assertEquals(a1.mItems.size(), a2.mItems.size());
        for (int i = 0; i < a1.mItems.size(); i++) {
            android.support.v7.widget.AdapterHelperTest.TestAdapter.Item item = a1.mItems.get(i);
            junit.framework.TestCase.assertSame(item, a2.mItems.get(i));
            junit.framework.TestCase.assertEquals(0, item.getUpdateCount());
        }
        junit.framework.TestCase.assertEquals(0, a1.mPendingAdded.size());
        junit.framework.TestCase.assertEquals(0, a2.mPendingAdded.size());
    }

    android.support.v7.widget.AdapterHelper.UpdateOp op(int cmd, int start, int count) {
        return new android.support.v7.widget.AdapterHelper.UpdateOp(cmd, start, count, null);
    }

    android.support.v7.widget.AdapterHelper.UpdateOp op(int cmd, int start, int count, java.lang.Object payload) {
        return new android.support.v7.widget.AdapterHelper.UpdateOp(cmd, start, count, payload);
    }

    android.support.v7.widget.AdapterHelper.UpdateOp addOp(int start, int count) {
        return op(android.support.v7.widget.AdapterHelper.UpdateOp.ADD, start, count);
    }

    android.support.v7.widget.AdapterHelper.UpdateOp rmOp(int start, int count) {
        return op(android.support.v7.widget.AdapterHelper.UpdateOp.REMOVE, start, count);
    }

    android.support.v7.widget.AdapterHelper.UpdateOp upOp(int start, int count, java.lang.Object payload) {
        return op(android.support.v7.widget.AdapterHelper.UpdateOp.UPDATE, start, count, payload);
    }

    void add(int start, int count) {
        if (android.support.v7.widget.AdapterHelperTest.DEBUG) {
            log(((("add(" + start) + ",") + count) + ");");
        }
        mTestAdapter.add(start, count);
    }

    boolean isItemLaidOut(int pos) {
        for (android.support.v7.widget.RecyclerView.ViewHolder viewHolder : mViewHolders) {
            if (viewHolder.mOldPosition == pos) {
                return true;
            }
        }
        return false;
    }

    private void mv(int from, int to) {
        if (android.support.v7.widget.AdapterHelperTest.DEBUG) {
            log(((("mv(" + from) + ",") + to) + ");");
        }
        mTestAdapter.move(from, to);
    }

    void rm(int start, int count) {
        if (android.support.v7.widget.AdapterHelperTest.DEBUG) {
            log(((("rm(" + start) + ",") + count) + ");");
        }
        for (int i = start; i < (start + count); i++) {
            if (!isItemLaidOut(i)) {
                android.support.v7.widget.AdapterHelperTest.TestAdapter.Item item = mTestAdapter.mItems.get(i);
                mPreLayoutItems.remove(item);
            }
        }
        mTestAdapter.remove(start, count);
    }

    void up(int start, int count) {
        if (android.support.v7.widget.AdapterHelperTest.DEBUG) {
            log(((("up(" + start) + ",") + count) + ");");
        }
        mTestAdapter.update(start, count);
    }

    void up(int start, int count, java.lang.Object payload) {
        if (android.support.v7.widget.AdapterHelperTest.DEBUG) {
            log(((((("up(" + start) + ",") + count) + ",") + payload) + ");");
        }
        mTestAdapter.update(start, count, payload);
    }

    static class TestAdapter {
        java.util.List<android.support.v7.widget.AdapterHelperTest.TestAdapter.Item> mItems;

        final android.support.v7.widget.AdapterHelper mAdapterHelper;

        java.util.Queue<android.support.v7.widget.AdapterHelperTest.TestAdapter.Item> mPendingAdded;

        public TestAdapter(int initialCount, android.support.v7.widget.AdapterHelper container) {
            mItems = new java.util.ArrayList<android.support.v7.widget.AdapterHelperTest.TestAdapter.Item>();
            mAdapterHelper = container;
            mPendingAdded = new java.util.LinkedList<android.support.v7.widget.AdapterHelperTest.TestAdapter.Item>();
            for (int i = 0; i < initialCount; i++) {
                mItems.add(new android.support.v7.widget.AdapterHelperTest.TestAdapter.Item());
            }
        }

        public void add(int index, int count) {
            for (int i = 0; i < count; i++) {
                android.support.v7.widget.AdapterHelperTest.TestAdapter.Item item = new android.support.v7.widget.AdapterHelperTest.TestAdapter.Item();
                mPendingAdded.add(item);
                mItems.add(index + i, item);
            }
            mAdapterHelper.addUpdateOp(new android.support.v7.widget.AdapterHelper.UpdateOp(android.support.v7.widget.AdapterHelper.UpdateOp.ADD, index, count, null));
        }

        public void move(int from, int to) {
            mItems.add(to, mItems.remove(from));
            mAdapterHelper.addUpdateOp(new android.support.v7.widget.AdapterHelper.UpdateOp(android.support.v7.widget.AdapterHelper.UpdateOp.MOVE, from, to, null));
        }

        public void remove(int index, int count) {
            for (int i = 0; i < count; i++) {
                mItems.remove(index);
            }
            mAdapterHelper.addUpdateOp(new android.support.v7.widget.AdapterHelper.UpdateOp(android.support.v7.widget.AdapterHelper.UpdateOp.REMOVE, index, count, null));
        }

        public void update(int index, int count) {
            update(index, count, null);
        }

        public void update(int index, int count, java.lang.Object payload) {
            for (int i = 0; i < count; i++) {
                mItems.get(index + i).update(payload);
            }
            mAdapterHelper.addUpdateOp(new android.support.v7.widget.AdapterHelper.UpdateOp(android.support.v7.widget.AdapterHelper.UpdateOp.UPDATE, index, count, payload));
        }

        protected android.support.v7.widget.AdapterHelperTest.TestAdapter createCopy() {
            android.support.v7.widget.AdapterHelperTest.TestAdapter adapter = new android.support.v7.widget.AdapterHelperTest.TestAdapter(0, mAdapterHelper);
            for (android.support.v7.widget.AdapterHelperTest.TestAdapter.Item item : mItems) {
                adapter.mItems.add(item);
            }
            return adapter;
        }

        public void applyOps(java.util.List<android.support.v7.widget.AdapterHelper.UpdateOp> updates, android.support.v7.widget.AdapterHelperTest.TestAdapter dataSource) {
            for (android.support.v7.widget.AdapterHelper.UpdateOp op : updates) {
                switch (op.cmd) {
                    case android.support.v7.widget.AdapterHelper.UpdateOp.ADD :
                        for (int i = 0; i < op.itemCount; i++) {
                            mItems.add(op.positionStart + i, dataSource.consumeNextAdded());
                        }
                        break;
                    case android.support.v7.widget.AdapterHelper.UpdateOp.REMOVE :
                        for (int i = 0; i < op.itemCount; i++) {
                            mItems.remove(op.positionStart);
                        }
                        break;
                    case android.support.v7.widget.AdapterHelper.UpdateOp.UPDATE :
                        for (int i = 0; i < op.itemCount; i++) {
                            mItems.get(op.positionStart + i).handleUpdate(op.payload);
                        }
                        break;
                    case android.support.v7.widget.AdapterHelper.UpdateOp.MOVE :
                        mItems.add(op.itemCount, mItems.remove(op.positionStart));
                        break;
                }
            }
        }

        private android.support.v7.widget.AdapterHelperTest.TestAdapter.Item consumeNextAdded() {
            return mPendingAdded.remove();
        }

        public void createFakeItemAt(int fakeAddedItemIndex) {
            android.support.v7.widget.AdapterHelperTest.TestAdapter.Item fakeItem = new android.support.v7.widget.AdapterHelperTest.TestAdapter.Item();
            ((java.util.LinkedList<android.support.v7.widget.AdapterHelperTest.TestAdapter.Item>) (mPendingAdded)).add(fakeAddedItemIndex, fakeItem);
        }

        public static class Item {
            private static java.util.concurrent.atomic.AtomicInteger itemCounter = new java.util.concurrent.atomic.AtomicInteger();

            private final int id;

            private int mVersionCount = 0;

            private java.util.ArrayList<java.lang.Object> mPayloads = new java.util.ArrayList<java.lang.Object>();

            public Item() {
                id = android.support.v7.widget.AdapterHelperTest.TestAdapter.Item.itemCounter.incrementAndGet();
            }

            public void update(java.lang.Object payload) {
                mPayloads.add(payload);
                mVersionCount++;
            }

            public void handleUpdate(java.lang.Object payload) {
                junit.framework.TestCase.assertSame(payload, mPayloads.get(0));
                mPayloads.remove(0);
                mVersionCount--;
            }

            public int getUpdateCount() {
                return mVersionCount;
            }
        }
    }

    void waitForDebugger() {
        android.os.Debug.waitForDebugger();
    }

    static class MockViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
        public java.lang.Object mItem;

        public MockViewHolder(android.view.View itemView) {
            super(itemView);
        }
    }
}

