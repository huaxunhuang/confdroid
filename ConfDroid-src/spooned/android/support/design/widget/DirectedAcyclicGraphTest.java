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
package android.support.design.widget;


@org.junit.runner.RunWith(org.junit.runners.JUnit4.class)
@android.test.suitebuilder.annotation.SmallTest
public class DirectedAcyclicGraphTest {
    private android.support.design.widget.DirectedAcyclicGraph<android.support.design.widget.DirectedAcyclicGraphTest.TestNode> mGraph;

    @org.junit.Before
    public void setup() {
        mGraph = new android.support.design.widget.DirectedAcyclicGraph<>();
    }

    @org.junit.Test
    public void test_addNode() {
        final android.support.design.widget.DirectedAcyclicGraphTest.TestNode node = new android.support.design.widget.DirectedAcyclicGraphTest.TestNode("node");
        mGraph.addNode(node);
        org.junit.Assert.assertEquals(1, mGraph.size());
        org.junit.Assert.assertTrue(mGraph.contains(node));
    }

    @org.junit.Test
    public void test_addNodeAgain() {
        final android.support.design.widget.DirectedAcyclicGraphTest.TestNode node = new android.support.design.widget.DirectedAcyclicGraphTest.TestNode("node");
        mGraph.addNode(node);
        mGraph.addNode(node);
        org.junit.Assert.assertEquals(1, mGraph.size());
        org.junit.Assert.assertTrue(mGraph.contains(node));
    }

    @org.junit.Test
    public void test_addEdge() {
        final android.support.design.widget.DirectedAcyclicGraphTest.TestNode node = new android.support.design.widget.DirectedAcyclicGraphTest.TestNode("node");
        final android.support.design.widget.DirectedAcyclicGraphTest.TestNode edge = new android.support.design.widget.DirectedAcyclicGraphTest.TestNode("edge");
        mGraph.addNode(node);
        mGraph.addNode(edge);
        mGraph.addEdge(node, edge);
    }

    @org.junit.Test(expected = java.lang.IllegalArgumentException.class)
    public void test_addEdgeWithNotAddedEdgeNode() {
        final android.support.design.widget.DirectedAcyclicGraphTest.TestNode node = new android.support.design.widget.DirectedAcyclicGraphTest.TestNode("node");
        final android.support.design.widget.DirectedAcyclicGraphTest.TestNode edge = new android.support.design.widget.DirectedAcyclicGraphTest.TestNode("edge");
        // Add the node, but not the edge node
        mGraph.addNode(node);
        // Now add the link
        mGraph.addEdge(node, edge);
    }

    @org.junit.Test
    public void test_getIncomingEdges() {
        final android.support.design.widget.DirectedAcyclicGraphTest.TestNode node = new android.support.design.widget.DirectedAcyclicGraphTest.TestNode("node");
        final android.support.design.widget.DirectedAcyclicGraphTest.TestNode edge = new android.support.design.widget.DirectedAcyclicGraphTest.TestNode("edge");
        mGraph.addNode(node);
        mGraph.addNode(edge);
        mGraph.addEdge(node, edge);
        final java.util.List<android.support.design.widget.DirectedAcyclicGraphTest.TestNode> incomingEdges = mGraph.getIncomingEdges(node);
        org.junit.Assert.assertNotNull(incomingEdges);
        org.junit.Assert.assertEquals(1, incomingEdges.size());
        org.junit.Assert.assertEquals(edge, incomingEdges.get(0));
    }

    @org.junit.Test
    public void test_getOutgoingEdges() {
        final android.support.design.widget.DirectedAcyclicGraphTest.TestNode node = new android.support.design.widget.DirectedAcyclicGraphTest.TestNode("node");
        final android.support.design.widget.DirectedAcyclicGraphTest.TestNode edge = new android.support.design.widget.DirectedAcyclicGraphTest.TestNode("edge");
        mGraph.addNode(node);
        mGraph.addNode(edge);
        mGraph.addEdge(node, edge);
        // Now assert the getOutgoingEdges returns a list which has one element (node)
        final java.util.List<android.support.design.widget.DirectedAcyclicGraphTest.TestNode> outgoingEdges = mGraph.getOutgoingEdges(edge);
        org.junit.Assert.assertNotNull(outgoingEdges);
        org.junit.Assert.assertEquals(1, outgoingEdges.size());
        org.junit.Assert.assertTrue(outgoingEdges.contains(node));
    }

    @org.junit.Test
    public void test_getOutgoingEdgesMultiple() {
        final android.support.design.widget.DirectedAcyclicGraphTest.TestNode node1 = new android.support.design.widget.DirectedAcyclicGraphTest.TestNode("1");
        final android.support.design.widget.DirectedAcyclicGraphTest.TestNode node2 = new android.support.design.widget.DirectedAcyclicGraphTest.TestNode("2");
        final android.support.design.widget.DirectedAcyclicGraphTest.TestNode edge = new android.support.design.widget.DirectedAcyclicGraphTest.TestNode("edge");
        mGraph.addNode(node1);
        mGraph.addNode(node2);
        mGraph.addNode(edge);
        mGraph.addEdge(node1, edge);
        mGraph.addEdge(node2, edge);
        // Now assert the getOutgoingEdges returns a list which has 2 elements (node1 & node2)
        final java.util.List<android.support.design.widget.DirectedAcyclicGraphTest.TestNode> outgoingEdges = mGraph.getOutgoingEdges(edge);
        org.junit.Assert.assertNotNull(outgoingEdges);
        org.junit.Assert.assertEquals(2, outgoingEdges.size());
        org.junit.Assert.assertTrue(outgoingEdges.contains(node1));
        org.junit.Assert.assertTrue(outgoingEdges.contains(node2));
    }

    @org.junit.Test
    public void test_hasOutgoingEdges() {
        final android.support.design.widget.DirectedAcyclicGraphTest.TestNode node = new android.support.design.widget.DirectedAcyclicGraphTest.TestNode("node");
        final android.support.design.widget.DirectedAcyclicGraphTest.TestNode edge = new android.support.design.widget.DirectedAcyclicGraphTest.TestNode("edge");
        mGraph.addNode(node);
        mGraph.addNode(edge);
        // There is no edge currently and assert that fact
        org.junit.Assert.assertFalse(mGraph.hasOutgoingEdges(edge));
        // Now add the edge
        mGraph.addEdge(node, edge);
        // and assert that the methods returns true;
        org.junit.Assert.assertTrue(mGraph.hasOutgoingEdges(edge));
    }

    @org.junit.Test
    public void test_clear() {
        final android.support.design.widget.DirectedAcyclicGraphTest.TestNode node1 = new android.support.design.widget.DirectedAcyclicGraphTest.TestNode("1");
        final android.support.design.widget.DirectedAcyclicGraphTest.TestNode node2 = new android.support.design.widget.DirectedAcyclicGraphTest.TestNode("2");
        final android.support.design.widget.DirectedAcyclicGraphTest.TestNode edge = new android.support.design.widget.DirectedAcyclicGraphTest.TestNode("edge");
        mGraph.addNode(node1);
        mGraph.addNode(node2);
        mGraph.addNode(edge);
        // Now clear the graph
        mGraph.clear();
        // Now assert the graph is empty and that contains returns false
        org.junit.Assert.assertEquals(0, mGraph.size());
        org.junit.Assert.assertFalse(mGraph.contains(node1));
        org.junit.Assert.assertFalse(mGraph.contains(node2));
        org.junit.Assert.assertFalse(mGraph.contains(edge));
    }

    @org.junit.Test
    public void test_getSortedList() {
        final android.support.design.widget.DirectedAcyclicGraphTest.TestNode node1 = new android.support.design.widget.DirectedAcyclicGraphTest.TestNode("A");
        final android.support.design.widget.DirectedAcyclicGraphTest.TestNode node2 = new android.support.design.widget.DirectedAcyclicGraphTest.TestNode("B");
        final android.support.design.widget.DirectedAcyclicGraphTest.TestNode node3 = new android.support.design.widget.DirectedAcyclicGraphTest.TestNode("C");
        final android.support.design.widget.DirectedAcyclicGraphTest.TestNode node4 = new android.support.design.widget.DirectedAcyclicGraphTest.TestNode("D");
        // Now we'll add the nodes
        mGraph.addNode(node1);
        mGraph.addNode(node2);
        mGraph.addNode(node3);
        mGraph.addNode(node4);
        // Now we'll add edges so that 4 <- 2, 2 <- 3, 3 <- 1  (where <- denotes a dependency)
        mGraph.addEdge(node4, node2);
        mGraph.addEdge(node2, node3);
        mGraph.addEdge(node3, node1);
        final java.util.List<android.support.design.widget.DirectedAcyclicGraphTest.TestNode> sorted = mGraph.getSortedList();
        // Assert that it is the correct size
        org.junit.Assert.assertEquals(4, sorted.size());
        // Assert that all of the nodes are present and in their sorted order
        org.junit.Assert.assertEquals(node1, sorted.get(0));
        org.junit.Assert.assertEquals(node3, sorted.get(1));
        org.junit.Assert.assertEquals(node2, sorted.get(2));
        org.junit.Assert.assertEquals(node4, sorted.get(3));
    }

    private static class TestNode {
        private final java.lang.String mLabel;

        TestNode(@android.support.annotation.NonNull
        java.lang.String label) {
            mLabel = label;
        }

        @java.lang.Override
        public java.lang.String toString() {
            return "TestNode: " + mLabel;
        }
    }
}

