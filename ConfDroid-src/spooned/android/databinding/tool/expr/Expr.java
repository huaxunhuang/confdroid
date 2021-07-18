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


public abstract class Expr implements android.databinding.tool.expr.VersionProvider , android.databinding.tool.processing.scopes.LocationScopeProvider {
    public static final int NO_ID = -1;

    protected java.util.List<android.databinding.tool.expr.Expr> mChildren = new java.util.ArrayList<android.databinding.tool.expr.Expr>();

    // any expression that refers to this. Useful if this expr is duplicate and being replaced
    private java.util.List<android.databinding.tool.expr.Expr> mParents = new java.util.ArrayList<android.databinding.tool.expr.Expr>();

    private java.lang.Boolean mIsDynamic;

    private android.databinding.tool.reflection.ModelClass mResolvedType;

    private java.lang.String mUniqueKey;

    private java.util.List<android.databinding.tool.expr.Dependency> mDependencies;

    private java.util.List<android.databinding.tool.expr.Dependency> mDependants = new java.util.ArrayList<android.databinding.tool.expr.Dependency>();

    private int mId = android.databinding.tool.expr.Expr.NO_ID;

    private int mRequirementId = android.databinding.tool.expr.Expr.NO_ID;

    private int mVersion = 0;

    // means this expression can directly be invalidated by the user
    private boolean mCanBeInvalidated = false;

    @org.antlr.v4.runtime.misc.Nullable
    private java.util.List<android.databinding.tool.store.Location> mLocations = new java.util.ArrayList<android.databinding.tool.store.Location>();

    /**
     * This set denotes the times when this expression is invalid.
     * If it is an Identifier expression, it is its index
     * If it is a composite expression, it is the union of invalid flags of its descendants
     */
    private java.util.BitSet mInvalidFlags;

    /**
     * Set when this expression is registered to a model
     */
    private android.databinding.tool.expr.ExprModel mModel;

    /**
     * This set denotes the times when this expression must be read.
     *
     * It is the union of invalidation flags of all of its non-conditional dependants.
     */
    java.util.BitSet mShouldReadFlags;

    java.util.BitSet mReadSoFar = new java.util.BitSet();// i've read this variable for these flags


    /**
     * calculated on initialization, assuming all conditionals are true
     */
    java.util.BitSet mShouldReadWithConditionals;

    private boolean mIsBindingExpression;

    /**
     * Used by generators when this expression is resolved.
     */
    private boolean mRead;

    private boolean mIsUsed = false;

    private boolean mIsTwoWay = false;

    Expr(java.lang.Iterable<android.databinding.tool.expr.Expr> children) {
        for (android.databinding.tool.expr.Expr expr : children) {
            mChildren.add(expr);
        }
        addParents();
    }

    Expr(android.databinding.tool.expr.Expr... children) {
        java.util.Collections.addAll(mChildren, children);
        addParents();
    }

    public int getId() {
        android.databinding.tool.util.Preconditions.check(mId != android.databinding.tool.expr.Expr.NO_ID, "if getId is called on an expression, it should have" + " an id: %s", this);
        return mId;
    }

    public void setId(int id) {
        android.databinding.tool.util.Preconditions.check(mId == android.databinding.tool.expr.Expr.NO_ID, "ID is already set on %s", this);
        mId = id;
    }

    public void addLocation(android.databinding.tool.store.Location location) {
        mLocations.add(location);
    }

    public java.util.List<android.databinding.tool.store.Location> getLocations() {
        return mLocations;
    }

    public android.databinding.tool.expr.ExprModel getModel() {
        return mModel;
    }

    public java.util.BitSet getInvalidFlags() {
        if (mInvalidFlags == null) {
            mInvalidFlags = resolveInvalidFlags();
        }
        return mInvalidFlags;
    }

    private java.util.BitSet resolveInvalidFlags() {
        java.util.BitSet bitSet = ((java.util.BitSet) (mModel.getInvalidateAnyBitSet().clone()));
        if (mCanBeInvalidated) {
            bitSet.set(getId(), true);
        }
        for (android.databinding.tool.expr.Dependency dependency : getDependencies()) {
            // TODO optional optimization: do not invalidate for conditional flags
            bitSet.or(dependency.getOther().getInvalidFlags());
        }
        return bitSet;
    }

    public void setBindingExpression(boolean isBindingExpression) {
        mIsBindingExpression = isBindingExpression;
    }

    public boolean isBindingExpression() {
        return mIsBindingExpression;
    }

    public boolean canBeEvaluatedToAVariable() {
        return true;// anything except arg expr can be evaluated to a variable

    }

    public boolean isObservable() {
        return getResolvedType().isObservable();
    }

    public android.databinding.tool.expr.Expr resolveListeners(android.databinding.tool.reflection.ModelClass valueType, android.databinding.tool.expr.Expr parent) {
        for (int i = mChildren.size() - 1; i >= 0; i--) {
            android.databinding.tool.expr.Expr child = mChildren.get(i);
            child.resolveListeners(valueType, this);
        }
        resetResolvedType();
        return this;
    }

    public android.databinding.tool.expr.Expr resolveTwoWayExpressions(android.databinding.tool.expr.Expr parent) {
        for (int i = mChildren.size() - 1; i >= 0; i--) {
            final android.databinding.tool.expr.Expr child = mChildren.get(i);
            child.resolveTwoWayExpressions(this);
        }
        return this;
    }

    protected void resetResolvedType() {
        mResolvedType = null;
    }

    public java.util.BitSet getShouldReadFlags() {
        if (mShouldReadFlags == null) {
            getShouldReadFlagsWithConditionals();
            mShouldReadFlags = resolveShouldReadFlags();
        }
        return mShouldReadFlags;
    }

    public java.util.BitSet getShouldReadFlagsWithConditionals() {
        if (mShouldReadWithConditionals == null) {
            mShouldReadWithConditionals = resolveShouldReadWithConditionals();
        }
        return mShouldReadWithConditionals;
    }

    public void setModel(android.databinding.tool.expr.ExprModel model) {
        mModel = model;
    }

    public void setTwoWay(boolean isTwoWay) {
        mIsTwoWay = isTwoWay;
    }

    public boolean isTwoWay() {
        return mIsTwoWay;
    }

    protected java.lang.String addTwoWay(java.lang.String uniqueKey) {
        if (mIsTwoWay) {
            return ("twoWay(" + uniqueKey) + ")";
        } else {
            return ("oneWay(" + uniqueKey) + ")";
        }
    }

    private java.util.BitSet resolveShouldReadWithConditionals() {
        // ensure we have invalid flags
        java.util.BitSet bitSet = new java.util.BitSet();
        // if i'm invalid, that DOES NOT mean i should be read :/.
        if (mIsBindingExpression) {
            bitSet.or(getInvalidFlags());
        }
        for (android.databinding.tool.expr.Dependency dependency : getDependants()) {
            if (dependency.getCondition() == null) {
                bitSet.or(dependency.getDependant().getShouldReadFlagsWithConditionals());
            } else {
                bitSet.set(dependency.getDependant().getRequirementFlagIndex(dependency.getExpectedOutput()));
            }
        }
        return bitSet;
    }

    private java.util.BitSet resolveShouldReadFlags() {
        // ensure we have invalid flags
        java.util.BitSet bitSet = new java.util.BitSet();
        if (isRead()) {
            return bitSet;
        }
        if (mIsBindingExpression) {
            bitSet.or(getInvalidFlags());
        }
        for (android.databinding.tool.expr.Dependency dependency : getDependants()) {
            final boolean isUnreadElevated = android.databinding.tool.expr.Expr.isUnreadElevated(dependency);
            if (dependency.isConditional()) {
                continue;// will be resolved later when conditional is elevated

            }
            if (isUnreadElevated) {
                bitSet.set(dependency.getDependant().getRequirementFlagIndex(dependency.getExpectedOutput()));
            } else {
                bitSet.or(dependency.getDependant().getShouldReadFlags());
            }
        }
        bitSet.and(mShouldReadWithConditionals);
        bitSet.andNot(mReadSoFar);
        return bitSet;
    }

    private static boolean isUnreadElevated(android.databinding.tool.expr.Dependency input) {
        return input.isElevated() && (!input.getDependant().isRead());
    }

    private void addParents() {
        for (android.databinding.tool.expr.Expr expr : mChildren) {
            expr.mParents.add(this);
        }
    }

    public void onSwappedWith(android.databinding.tool.expr.Expr existing) {
        for (android.databinding.tool.expr.Expr child : mChildren) {
            child.onParentSwapped(this, existing);
        }
    }

    private void onParentSwapped(android.databinding.tool.expr.Expr oldParent, android.databinding.tool.expr.Expr newParent) {
        android.databinding.tool.util.Preconditions.check(mParents.remove(oldParent), "trying to remove non-existent parent %s" + " from %s", oldParent, mParents);
        mParents.add(newParent);
    }

    public java.util.List<android.databinding.tool.expr.Expr> getChildren() {
        return mChildren;
    }

    public java.util.List<android.databinding.tool.expr.Expr> getParents() {
        return mParents;
    }

    /**
     * Whether the result of this expression can change or not.
     *
     * For example, 3 + 5 can not change vs 3 + x may change.
     *
     * Default implementations checks children and returns true if any of them returns true
     *
     * @return True if the result of this expression may change due to variables
     */
    public boolean isDynamic() {
        if (mIsDynamic == null) {
            mIsDynamic = isAnyChildDynamic();
        }
        return mIsDynamic;
    }

    private boolean isAnyChildDynamic() {
        for (android.databinding.tool.expr.Expr expr : mChildren) {
            if (expr.isDynamic()) {
                return true;
            }
        }
        return false;
    }

    public android.databinding.tool.reflection.ModelClass getResolvedType() {
        if (mResolvedType == null) {
            // TODO not get instance
            try {
                android.databinding.tool.processing.Scope.enter(this);
                mResolvedType = resolveType(android.databinding.tool.reflection.ModelAnalyzer.getInstance());
                if (mResolvedType == null) {
                    android.databinding.tool.util.L.e(android.databinding.tool.processing.ErrorMessages.CANNOT_RESOLVE_TYPE, this);
                }
            } finally {
                android.databinding.tool.processing.Scope.exit();
            }
        }
        return mResolvedType;
    }

    protected abstract android.databinding.tool.reflection.ModelClass resolveType(android.databinding.tool.reflection.ModelAnalyzer modelAnalyzer);

    protected abstract java.util.List<android.databinding.tool.expr.Dependency> constructDependencies();

    /**
     * Creates a dependency for each dynamic child. Should work for any expression besides
     * conditionals.
     */
    protected java.util.List<android.databinding.tool.expr.Dependency> constructDynamicChildrenDependencies() {
        java.util.List<android.databinding.tool.expr.Dependency> dependencies = new java.util.ArrayList<android.databinding.tool.expr.Dependency>();
        for (android.databinding.tool.expr.Expr node : mChildren) {
            if (!node.isDynamic()) {
                continue;
            }
            dependencies.add(new android.databinding.tool.expr.Dependency(this, node));
        }
        return dependencies;
    }

    public final java.util.List<android.databinding.tool.expr.Dependency> getDependencies() {
        if (mDependencies == null) {
            mDependencies = constructDependencies();
        }
        return mDependencies;
    }

    void addDependant(android.databinding.tool.expr.Dependency dependency) {
        mDependants.add(dependency);
    }

    public java.util.List<android.databinding.tool.expr.Dependency> getDependants() {
        return mDependants;
    }

    protected static final java.lang.String KEY_JOIN = "~";

    /**
     * Returns a unique string key that can identify this expression.
     *
     * It must take into account any dependencies
     *
     * @return A unique identifier for this expression
     */
    public final java.lang.String getUniqueKey() {
        if (mUniqueKey == null) {
            mUniqueKey = computeUniqueKey();
            android.databinding.tool.util.Preconditions.checkNotNull(mUniqueKey, "if there are no children, you must override computeUniqueKey");
            android.databinding.tool.util.Preconditions.check(!mUniqueKey.trim().equals(""), "if there are no children, you must override computeUniqueKey");
        }
        return mUniqueKey;
    }

    protected java.lang.String computeUniqueKey() {
        return computeChildrenKey();
    }

    protected final java.lang.String computeChildrenKey() {
        return android.databinding.tool.expr.Expr.join(mChildren);
    }

    public void enableDirectInvalidation() {
        mCanBeInvalidated = true;
    }

    public boolean canBeInvalidated() {
        return mCanBeInvalidated;
    }

    public void trimShouldReadFlags(java.util.BitSet bitSet) {
        mShouldReadFlags.andNot(bitSet);
    }

    public boolean isConditional() {
        return false;
    }

    public int getRequirementId() {
        return mRequirementId;
    }

    public void setRequirementId(int requirementId) {
        mRequirementId = requirementId;
    }

    /**
     * This is called w/ a dependency of mine.
     * Base method should thr
     */
    public int getRequirementFlagIndex(boolean expectedOutput) {
        android.databinding.tool.util.Preconditions.check(mRequirementId != android.databinding.tool.expr.Expr.NO_ID, "If this is an expression w/ conditional" + " dependencies, it must be assigned a requirement ID. %s", this);
        return expectedOutput ? mRequirementId + 1 : mRequirementId;
    }

    public boolean hasId() {
        return mId != android.databinding.tool.expr.Expr.NO_ID;
    }

    public void markFlagsAsRead(java.util.BitSet flags) {
        mReadSoFar.or(flags);
    }

    public boolean isRead() {
        return mRead;
    }

    public boolean considerElevatingConditionals(android.databinding.tool.expr.Expr justRead) {
        boolean elevated = false;
        for (android.databinding.tool.expr.Dependency dependency : mDependencies) {
            if (dependency.isConditional() && (dependency.getCondition() == justRead)) {
                dependency.elevate();
                elevated = true;
            }
        }
        return elevated;
    }

    public void invalidateReadFlags() {
        mShouldReadFlags = null;
        mVersion++;
    }

    @java.lang.Override
    public int getVersion() {
        return mVersion;
    }

    public boolean hasNestedCannotRead() {
        if (isRead()) {
            return false;
        }
        if (getShouldReadFlags().isEmpty()) {
            return true;
        }
        for (android.databinding.tool.expr.Dependency dependency : getDependencies()) {
            if (android.databinding.tool.expr.Expr.hasNestedCannotRead(dependency)) {
                return true;
            }
        }
        return false;
    }

    private static boolean hasNestedCannotRead(android.databinding.tool.expr.Dependency input) {
        return input.isConditional() || input.getOther().hasNestedCannotRead();
    }

    public boolean markAsReadIfDone() {
        if (mRead) {
            return false;
        }
        // TODO avoid clone, we can calculate this iteratively
        java.util.BitSet clone = ((java.util.BitSet) (mShouldReadWithConditionals.clone()));
        clone.andNot(mReadSoFar);
        mRead = clone.isEmpty();
        if ((!mRead) && (!mReadSoFar.isEmpty())) {
            // check if remaining dependencies can be satisfied w/ existing values
            // for predicate flags, this expr may already be calculated to get the predicate
            // to detect them, traverse them later on, see which flags should be calculated to calculate
            // them. If any of them is completely covered w/ our non-conditional flags, no reason
            // to add them to the list since we'll already be calculated due to our non-conditional
            // flags
            boolean allCovered = true;
            for (int i = clone.nextSetBit(0); i != (-1); i = clone.nextSetBit(i + 1)) {
                final android.databinding.tool.expr.Expr expr = mModel.findFlagExpression(i);
                if (expr == null) {
                    continue;
                }
                if (!expr.isConditional()) {
                    allCovered = false;
                    break;
                }
                final java.util.BitSet readForConditional = ((java.util.BitSet) (expr.findConditionalFlags().clone()));
                // FIXME: this does not do full traversal so misses some cases
                // to calculate that conditional, i should've read /readForConditional/ flags
                // if my read-so-far bits cover that; that means i would've already
                // read myself
                readForConditional.andNot(mReadSoFar);
                if (!readForConditional.isEmpty()) {
                    allCovered = false;
                    break;
                }
            }
            mRead = allCovered;
        }
        if (mRead) {
            mShouldReadFlags = null;// if we've been marked as read, clear should read flags

        }
        return mRead;
    }

    java.util.BitSet mConditionalFlags;

    private java.util.BitSet findConditionalFlags() {
        android.databinding.tool.util.Preconditions.check(isConditional(), "should not call this on a non-conditional expr");
        if (mConditionalFlags == null) {
            mConditionalFlags = new java.util.BitSet();
            resolveConditionalFlags(mConditionalFlags);
        }
        return mConditionalFlags;
    }

    private void resolveConditionalFlags(java.util.BitSet flags) {
        flags.or(getPredicateInvalidFlags());
        // if i have only 1 dependency which is conditional, traverse it as well
        if (getDependants().size() == 1) {
            final android.databinding.tool.expr.Dependency dependency = getDependants().get(0);
            if (dependency.getCondition() != null) {
                flags.or(dependency.getDependant().findConditionalFlags());
                flags.set(dependency.getDependant().getRequirementFlagIndex(dependency.getExpectedOutput()));
            }
        }
    }

    @java.lang.Override
    public java.lang.String toString() {
        return getUniqueKey();
    }

    public java.util.BitSet getReadSoFar() {
        return mReadSoFar;
    }

    private android.databinding.tool.expr.Expr.Node mCalculationPaths = null;

    /**
     * All flag paths that will result in calculation of this expression.
     */
    protected android.databinding.tool.expr.Expr.Node getAllCalculationPaths() {
        if (mCalculationPaths == null) {
            android.databinding.tool.expr.Expr.Node node = new android.databinding.tool.expr.Expr.Node();
            if (isConditional()) {
                node.mBitSet.or(getPredicateInvalidFlags());
            } else {
                node.mBitSet.or(getInvalidFlags());
            }
            for (android.databinding.tool.expr.Dependency dependency : getDependants()) {
                final android.databinding.tool.expr.Expr dependant = dependency.getDependant();
                if (dependency.getCondition() != null) {
                    android.databinding.tool.expr.Expr.Node cond = new android.databinding.tool.expr.Expr.Node();
                    cond.setConditionFlag(dependant.getRequirementFlagIndex(dependency.getExpectedOutput()));
                    cond.mParents.add(dependant.getAllCalculationPaths());
                    node.mParents.add(cond);
                } else {
                    node.mParents.add(dependant.getAllCalculationPaths());
                }
            }
            mCalculationPaths = node;
        }
        return mCalculationPaths;
    }

    public java.lang.String getDefaultValue() {
        return android.databinding.tool.reflection.ModelAnalyzer.getInstance().getDefaultValue(getResolvedType().toJavaCode());
    }

    protected java.util.BitSet getPredicateInvalidFlags() {
        throw new java.lang.IllegalStateException("must override getPredicateInvalidFlags in " + getClass().getSimpleName());
    }

    /**
     * Used by code generation
     */
    public boolean shouldReadNow(final java.util.List<android.databinding.tool.expr.Expr> justRead) {
        if (getShouldReadFlags().isEmpty()) {
            return false;
        }
        for (android.databinding.tool.expr.Dependency input : getDependencies()) {
            boolean dependencyReady = input.getOther().isRead() || ((justRead != null) && justRead.contains(input.getOther()));
            if (!dependencyReady) {
                return false;
            }
        }
        return true;
    }

    public boolean isEqualityCheck() {
        return false;
    }

    public void setIsUsed(boolean isUsed) {
        mIsUsed = isUsed;
        for (android.databinding.tool.expr.Expr child : getChildren()) {
            child.setIsUsed(isUsed);
        }
    }

    public boolean isUsed() {
        return mIsUsed;
    }

    public void updateExpr(android.databinding.tool.reflection.ModelAnalyzer modelAnalyzer) {
        final java.util.Map<java.lang.String, android.databinding.tool.expr.Expr> exprMap = mModel.getExprMap();
        for (int i = mParents.size() - 1; i >= 0; i--) {
            final android.databinding.tool.expr.Expr parent = mParents.get(i);
            if (exprMap.get(parent.getUniqueKey()) != parent) {
                mParents.remove(i);
            }
        }
        for (android.databinding.tool.expr.Expr child : mChildren) {
            child.updateExpr(modelAnalyzer);
        }
    }

    protected static java.lang.String join(java.lang.String... items) {
        java.lang.StringBuilder result = new java.lang.StringBuilder();
        for (int i = 0; i < items.length; i++) {
            if (i > 0) {
                result.append(android.databinding.tool.expr.Expr.KEY_JOIN);
            }
            result.append(items[i]);
        }
        return result.toString();
    }

    protected static java.lang.String join(java.util.List<android.databinding.tool.expr.Expr> items) {
        java.lang.StringBuilder result = new java.lang.StringBuilder();
        for (int i = 0; i < items.size(); i++) {
            if (i > 0) {
                result.append(android.databinding.tool.expr.Expr.KEY_JOIN);
            }
            result.append(items.get(i).getUniqueKey());
        }
        return result.toString();
    }

    protected java.lang.String asPackage() {
        return null;
    }

    @java.lang.Override
    public java.util.List<android.databinding.tool.store.Location> provideScopeLocation() {
        return mLocations;
    }

    public android.databinding.tool.writer.KCode toCode() {
        return toCode(false);
    }

    protected android.databinding.tool.writer.KCode toCode(boolean expand) {
        if ((!expand) && isDynamic()) {
            return new android.databinding.tool.writer.KCode(android.databinding.tool.writer.LayoutBinderWriterKt.getExecutePendingLocalName(this));
        }
        return generateCode(expand);
    }

    public android.databinding.tool.writer.KCode toFullCode() {
        return generateCode(false);
    }

    protected abstract android.databinding.tool.writer.KCode generateCode(boolean expand);

    public android.databinding.tool.writer.KCode toInverseCode(android.databinding.tool.writer.KCode value) {
        throw new java.lang.IllegalStateException("expression does not support two-way binding");
    }

    public void assertIsInvertible() {
        final java.lang.String errorMessage = getInvertibleError();
        if (errorMessage != null) {
            android.databinding.tool.util.L.e(android.databinding.tool.processing.ErrorMessages.EXPRESSION_NOT_INVERTIBLE, toFullCode().generate(), errorMessage);
        }
    }

    /**
     *
     *
     * @return The reason the expression wasn't invertible or null if it was invertible.
     */
    protected abstract java.lang.String getInvertibleError();

    /**
     * This expression is the predicate for 1 or more ternary expressions.
     */
    public boolean hasConditionalDependant() {
        for (android.databinding.tool.expr.Dependency dependency : getDependants()) {
            android.databinding.tool.expr.Expr dependant = dependency.getDependant();
            if (dependant.isConditional() && (dependant instanceof android.databinding.tool.expr.TernaryExpr)) {
                android.databinding.tool.expr.TernaryExpr ternary = ((android.databinding.tool.expr.TernaryExpr) (dependant));
                return ternary.getPred() == this;
            }
        }
        return false;
    }

    static class Node {
        java.util.BitSet mBitSet = new java.util.BitSet();

        java.util.List<android.databinding.tool.expr.Expr.Node> mParents = new java.util.ArrayList<android.databinding.tool.expr.Expr.Node>();

        int mConditionFlag = -1;

        public boolean areAllPathsSatisfied(java.util.BitSet readSoFar) {
            if (mConditionFlag != (-1)) {
                return readSoFar.get(mConditionFlag) || mParents.get(0).areAllPathsSatisfied(readSoFar);
            } else {
                final java.util.BitSet myBitsClone = ((java.util.BitSet) (mBitSet.clone()));
                myBitsClone.andNot(readSoFar);
                if (!myBitsClone.isEmpty()) {
                    // read so far does not cover all of my invalidation. The only way I could be
                    // covered is that I only have 1 conditional dependent which is covered by this.
                    if ((mParents.size() == 1) && (mParents.get(0).mConditionFlag != (-1))) {
                        return mParents.get(0).areAllPathsSatisfied(readSoFar);
                    }
                    return false;
                }
                if (mParents.isEmpty()) {
                    return true;
                }
                for (android.databinding.tool.expr.Expr.Node parent : mParents) {
                    if (!parent.areAllPathsSatisfied(readSoFar)) {
                        return false;
                    }
                }
                return true;
            }
        }

        public void setConditionFlag(int requirementFlagIndex) {
            mConditionFlag = requirementFlagIndex;
        }
    }
}

