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
package android.content.pm.split;


/**
 * A helper class that implements the dependency tree traversal for splits. Callbacks
 * are implemented by subclasses to notify whether a split has already been constructed
 * and is cached, and to actually create the split requested.
 *
 * This helper is meant to be subclassed so as to reduce the number of allocations
 * needed to make use of it.
 *
 * All inputs and outputs are assumed to be indices into an array of splits.
 *
 * @unknown 
 */
public abstract class SplitDependencyLoader<E extends java.lang.Exception> {
    @android.annotation.NonNull
    private final android.util.SparseArray<int[]> mDependencies;

    /**
     * Construct a new SplitDependencyLoader. Meant to be called from the
     * subclass constructor.
     *
     * @param dependencies
     * 		The dependency tree of splits.
     */
    protected SplitDependencyLoader(@android.annotation.NonNull
    android.util.SparseArray<int[]> dependencies) {
        mDependencies = dependencies;
    }

    /**
     * Traverses the dependency tree and constructs any splits that are not already
     * cached. This routine short-circuits and skips the creation of splits closer to the
     * root if they are cached, as reported by the subclass implementation of
     * {@link #isSplitCached(int)}. The construction of splits is delegated to the subclass
     * implementation of {@link #constructSplit(int, int[], int)}.
     *
     * @param splitIdx
     * 		The index of the split to load. 0 represents the base Application.
     */
    protected void loadDependenciesForSplit(@android.annotation.IntRange(from = 0)
    int splitIdx) throws E {
        // Quick check before any allocations are done.
        if (isSplitCached(splitIdx)) {
            return;
        }
        // Special case the base, since it has no dependencies.
        if (splitIdx == 0) {
            final int[] configSplitIndices = collectConfigSplitIndices(0);
            constructSplit(0, configSplitIndices, -1);
            return;
        }
        // Build up the dependency hierarchy.
        final android.util.IntArray linearDependencies = new android.util.IntArray();
        linearDependencies.add(splitIdx);
        // Collect all the dependencies that need to be constructed.
        // They will be listed from leaf to root.
        while (true) {
            // Only follow the first index into the array. The others are config splits and
            // get loaded with the split.
            final int[] deps = mDependencies.get(splitIdx);
            if ((deps != null) && (deps.length > 0)) {
                splitIdx = deps[0];
            } else {
                splitIdx = -1;
            }
            if ((splitIdx < 0) || isSplitCached(splitIdx)) {
                break;
            }
            linearDependencies.add(splitIdx);
        } 
        // Visit each index, from right to left (root to leaf).
        int parentIdx = splitIdx;
        for (int i = linearDependencies.size() - 1; i >= 0; i--) {
            final int idx = linearDependencies.get(i);
            final int[] configSplitIndices = collectConfigSplitIndices(idx);
            constructSplit(idx, configSplitIndices, parentIdx);
            parentIdx = idx;
        }
    }

    @android.annotation.NonNull
    private int[] collectConfigSplitIndices(int splitIdx) {
        // The config splits appear after the first element.
        final int[] deps = mDependencies.get(splitIdx);
        if ((deps == null) || (deps.length <= 1)) {
            return libcore.util.EmptyArray.INT;
        }
        return java.util.Arrays.copyOfRange(deps, 1, deps.length);
    }

    /**
     * Subclass to report whether the split at `splitIdx` is cached and need not be constructed.
     * It is assumed that if `splitIdx` is cached, any parent of `splitIdx` is also cached.
     *
     * @param splitIdx
     * 		The index of the split to check for in the cache.
     * @return true if the split is cached and does not need to be constructed.
     */
    protected abstract boolean isSplitCached(@android.annotation.IntRange(from = 0)
    int splitIdx);

    /**
     * Subclass to construct a split at index `splitIdx` with parent split `parentSplitIdx`.
     * The result is expected to be cached by the subclass in its own structures.
     *
     * @param splitIdx
     * 		The index of the split to construct. 0 represents the base Application.
     * @param configSplitIndices
     * 		The array of configuration splits to load along with this split.
     * 		May be empty (length == 0) but never null.
     * @param parentSplitIdx
     * 		The index of the parent split. -1 if there is no parent.
     * @throws E
     * 		Subclass defined exception representing failure to construct a split.
     */
    protected abstract void constructSplit(@android.annotation.IntRange(from = 0)
    int splitIdx, @android.annotation.NonNull
    @android.annotation.IntRange(from = 1)
    int[] configSplitIndices, @android.annotation.IntRange(from = -1)
    int parentSplitIdx) throws E;

    public static class IllegalDependencyException extends java.lang.Exception {
        private IllegalDependencyException(java.lang.String message) {
            super(message);
        }
    }

    private static int[] append(int[] src, int elem) {
        if (src == null) {
            return new int[]{ elem };
        }
        int[] dst = java.util.Arrays.copyOf(src, src.length + 1);
        dst[src.length] = elem;
        return dst;
    }

    @android.annotation.NonNull
    public static android.util.SparseArray<int[]> createDependenciesFromPackage(android.content.pm.PackageParser.PackageLite pkg) throws android.content.pm.split.SplitDependencyLoader.IllegalDependencyException {
        // The data structure that holds the dependencies. In PackageParser, splits are stored
        // in their own array, separate from the base. We treat all paths as equals, so
        // we need to insert the base as index 0, and shift all other splits.
        final android.util.SparseArray<int[]> splitDependencies = new android.util.SparseArray();
        // The base depends on nothing.
        splitDependencies.put(0, new int[]{ -1 });
        // First write out the <uses-split> dependencies. These must appear first in the
        // array of ints, as is convention in this class.
        for (int splitIdx = 0; splitIdx < pkg.splitNames.length; splitIdx++) {
            if (!pkg.isFeatureSplits[splitIdx]) {
                // Non-feature splits don't have dependencies.
                continue;
            }
            // Implicit dependency on the base.
            final int targetIdx;
            final java.lang.String splitDependency = pkg.usesSplitNames[splitIdx];
            if (splitDependency != null) {
                final int depIdx = java.util.Arrays.binarySearch(pkg.splitNames, splitDependency);
                if (depIdx < 0) {
                    throw new android.content.pm.split.SplitDependencyLoader.IllegalDependencyException(((("Split '" + pkg.splitNames[splitIdx]) + "' requires split '") + splitDependency) + "', which is missing.");
                }
                targetIdx = depIdx + 1;
            } else {
                // Implicitly depend on the base.
                targetIdx = 0;
            }
            splitDependencies.put(splitIdx + 1, new int[]{ targetIdx });
        }
        // Write out the configForSplit reverse-dependencies. These appear after the <uses-split>
        // dependencies and are considered leaves.
        // 
        // At this point, all splits in splitDependencies have the first element in their array set.
        for (int splitIdx = 0; splitIdx < pkg.splitNames.length; splitIdx++) {
            if (pkg.isFeatureSplits[splitIdx]) {
                // Feature splits are not configForSplits.
                continue;
            }
            // Implicit feature for the base.
            final int targetSplitIdx;
            final java.lang.String configForSplit = pkg.configForSplit[splitIdx];
            if (configForSplit != null) {
                final int depIdx = java.util.Arrays.binarySearch(pkg.splitNames, configForSplit);
                if (depIdx < 0) {
                    throw new android.content.pm.split.SplitDependencyLoader.IllegalDependencyException(((("Split '" + pkg.splitNames[splitIdx]) + "' targets split '") + configForSplit) + "', which is missing.");
                }
                if (!pkg.isFeatureSplits[depIdx]) {
                    throw new android.content.pm.split.SplitDependencyLoader.IllegalDependencyException(((("Split '" + pkg.splitNames[splitIdx]) + "' declares itself as configuration split for a non-feature split '") + pkg.splitNames[depIdx]) + "'");
                }
                targetSplitIdx = depIdx + 1;
            } else {
                targetSplitIdx = 0;
            }
            splitDependencies.put(targetSplitIdx, android.content.pm.split.SplitDependencyLoader.append(splitDependencies.get(targetSplitIdx), splitIdx + 1));
        }
        // Verify that there are no cycles.
        final java.util.BitSet bitset = new java.util.BitSet();
        for (int i = 0, size = splitDependencies.size(); i < size; i++) {
            int splitIdx = splitDependencies.keyAt(i);
            bitset.clear();
            while (splitIdx != (-1)) {
                // Check if this split has been visited yet.
                if (bitset.get(splitIdx)) {
                    throw new android.content.pm.split.SplitDependencyLoader.IllegalDependencyException("Cycle detected in split dependencies.");
                }
                // Mark the split so that if we visit it again, we no there is a cycle.
                bitset.set(splitIdx);
                // Follow the first dependency only, the others are leaves by definition.
                final int[] deps = splitDependencies.get(splitIdx);
                splitIdx = (deps != null) ? deps[0] : -1;
            } 
        }
        return splitDependencies;
    }
}

