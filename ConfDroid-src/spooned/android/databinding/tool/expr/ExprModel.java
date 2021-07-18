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


public class ExprModel {
    java.util.Map<java.lang.String, android.databinding.tool.expr.Expr> mExprMap = new java.util.HashMap<java.lang.String, android.databinding.tool.expr.Expr>();

    java.util.List<android.databinding.tool.expr.Expr> mBindingExpressions = new java.util.ArrayList<android.databinding.tool.expr.Expr>();

    private int mInvalidateableFieldLimit = 0;

    private int mRequirementIdCount = 0;

    // each arg list receives a unique id even if it is the same arguments and method.
    private int mArgListIdCounter = 0;

    private static final java.lang.String TRUE_KEY_SUFFIX = "== true";

    private static final java.lang.String FALSE_KEY_SUFFIX = "== false";

    /**
     * Any expression can be invalidated by invalidating this flag.
     */
    private java.util.BitSet mInvalidateAnyFlags;

    private int mInvalidateAnyFlagIndex;

    /**
     * Used by code generation. Keeps the list of expressions that are waiting to be evaluated.
     */
    private java.util.List<android.databinding.tool.expr.Expr> mPendingExpressions;

    /**
     * Used for converting flags into identifiers while debugging.
     */
    private java.lang.String[] mFlagMapping;

    private java.util.BitSet mInvalidateableFlags;

    private java.util.BitSet mConditionalFlags;

    private int mFlagBucketCount;// how many buckets we use to identify flags


    private java.util.List<android.databinding.tool.expr.Expr> mObservables;

    private boolean mSealed = false;

    private java.util.Map<java.lang.String, java.lang.String> mImports = new java.util.HashMap<java.lang.String, java.lang.String>();

    private org.antlr.v4.runtime.ParserRuleContext mCurrentParserContext;

    private android.databinding.tool.store.Location mCurrentLocationInFile;

    /**
     * Adds the expression to the list of expressions and returns it.
     * If it already exists, returns existing one.
     *
     * @param expr
     * 		The new parsed expression
     * @return The expression itself or another one if the same thing was parsed before
     */
    public <T extends android.databinding.tool.expr.Expr> T register(T expr) {
        android.databinding.tool.util.Preconditions.check(!mSealed, "Cannot add expressions to a model after it is sealed");
        android.databinding.tool.store.Location location = null;
        if (mCurrentParserContext != null) {
            location = new android.databinding.tool.store.Location(mCurrentParserContext);
            location.setParentLocation(mCurrentLocationInFile);
        }
        T existing = ((T) (mExprMap.get(expr.getUniqueKey())));
        if (existing != null) {
            android.databinding.tool.util.Preconditions.check(expr.getParents().isEmpty(), "If an expression already exists, it should've never been added to a parent," + ("if thats the case, somewhere we are creating an expression w/o" + "calling expression model"));
            // tell the expr that it is being swapped so that if it was added to some other expr
            // as a parent, those can swap their references
            expr.onSwappedWith(existing);
            if (location != null) {
                existing.addLocation(location);
            }
            return existing;
        }
        mExprMap.put(expr.getUniqueKey(), expr);
        expr.setModel(this);
        if (location != null) {
            expr.addLocation(location);
        }
        return expr;
    }

    public void setCurrentParserContext(org.antlr.v4.runtime.ParserRuleContext currentParserContext) {
        mCurrentParserContext = currentParserContext;
    }

    public java.util.Map<java.lang.String, android.databinding.tool.expr.Expr> getExprMap() {
        return mExprMap;
    }

    public int size() {
        return mExprMap.size();
    }

    public android.databinding.tool.expr.ComparisonExpr comparison(java.lang.String op, android.databinding.tool.expr.Expr left, android.databinding.tool.expr.Expr right) {
        return register(new android.databinding.tool.expr.ComparisonExpr(op, left, right));
    }

    public android.databinding.tool.expr.InstanceOfExpr instanceOfOp(android.databinding.tool.expr.Expr expr, java.lang.String type) {
        return register(new android.databinding.tool.expr.InstanceOfExpr(expr, type));
    }

    public android.databinding.tool.expr.FieldAccessExpr field(android.databinding.tool.expr.Expr parent, java.lang.String name) {
        return register(new android.databinding.tool.expr.FieldAccessExpr(parent, name));
    }

    public android.databinding.tool.expr.FieldAccessExpr observableField(android.databinding.tool.expr.Expr parent, java.lang.String name) {
        return register(new android.databinding.tool.expr.FieldAccessExpr(parent, name, true));
    }

    public android.databinding.tool.expr.SymbolExpr symbol(java.lang.String text, java.lang.Class type) {
        return register(new android.databinding.tool.expr.SymbolExpr(text, type));
    }

    public android.databinding.tool.expr.TernaryExpr ternary(android.databinding.tool.expr.Expr pred, android.databinding.tool.expr.Expr ifTrue, android.databinding.tool.expr.Expr ifFalse) {
        return register(new android.databinding.tool.expr.TernaryExpr(pred, ifTrue, ifFalse));
    }

    public android.databinding.tool.expr.IdentifierExpr identifier(java.lang.String name) {
        return register(new android.databinding.tool.expr.IdentifierExpr(name));
    }

    public android.databinding.tool.expr.StaticIdentifierExpr staticIdentifier(java.lang.String name) {
        return register(new android.databinding.tool.expr.StaticIdentifierExpr(name));
    }

    public android.databinding.tool.expr.BuiltInVariableExpr builtInVariable(java.lang.String name, java.lang.String type, java.lang.String accessCode) {
        return register(new android.databinding.tool.expr.BuiltInVariableExpr(name, type, accessCode));
    }

    public android.databinding.tool.expr.ViewFieldExpr viewFieldExpr(android.databinding.tool.BindingTarget bindingTarget) {
        return register(new android.databinding.tool.expr.ViewFieldExpr(bindingTarget));
    }

    /**
     * Creates a static identifier for the given class or returns the existing one.
     */
    public android.databinding.tool.expr.StaticIdentifierExpr staticIdentifierFor(final android.databinding.tool.reflection.ModelClass modelClass) {
        final java.lang.String type = modelClass.getCanonicalName();
        // check for existing
        for (android.databinding.tool.expr.Expr expr : mExprMap.values()) {
            if (expr instanceof android.databinding.tool.expr.StaticIdentifierExpr) {
                android.databinding.tool.expr.StaticIdentifierExpr id = ((android.databinding.tool.expr.StaticIdentifierExpr) (expr));
                if (id.getUserDefinedType().equals(type)) {
                    return id;
                }
            }
        }
        // does not exist. Find a name for it.
        int cnt = 0;
        int dotIndex = type.lastIndexOf(".");
        java.lang.String baseName;
        android.databinding.tool.util.Preconditions.check(dotIndex < (type.length() - 1), "Invalid type %s", type);
        if (dotIndex == (-1)) {
            baseName = type;
        } else {
            baseName = type.substring(dotIndex + 1);
        }
        while (true) {
            java.lang.String candidate = (cnt == 0) ? baseName : baseName + cnt;
            if (!mImports.containsKey(candidate)) {
                return addImport(candidate, type, null);
            }
            cnt++;
            android.databinding.tool.util.Preconditions.check(cnt < 100, "Failed to create an import for " + type);
        } 
    }

    public android.databinding.tool.expr.MethodCallExpr methodCall(android.databinding.tool.expr.Expr target, java.lang.String name, java.util.List<android.databinding.tool.expr.Expr> args) {
        return register(new android.databinding.tool.expr.MethodCallExpr(target, name, args));
    }

    public android.databinding.tool.expr.MathExpr math(android.databinding.tool.expr.Expr left, java.lang.String op, android.databinding.tool.expr.Expr right) {
        return register(new android.databinding.tool.expr.MathExpr(left, op, right));
    }

    public android.databinding.tool.expr.TernaryExpr logical(android.databinding.tool.expr.Expr left, java.lang.String op, android.databinding.tool.expr.Expr right) {
        if ("&&".equals(op)) {
            // left && right
            // left ? right : false
            return register(new android.databinding.tool.expr.TernaryExpr(left, right, symbol("false", boolean.class)));
        } else {
            // left || right
            // left ? true : right
            return register(new android.databinding.tool.expr.TernaryExpr(left, symbol("true", boolean.class), right));
        }
    }

    public android.databinding.tool.expr.BitShiftExpr bitshift(android.databinding.tool.expr.Expr left, java.lang.String op, android.databinding.tool.expr.Expr right) {
        return register(new android.databinding.tool.expr.BitShiftExpr(left, op, right));
    }

    public android.databinding.tool.expr.UnaryExpr unary(java.lang.String op, android.databinding.tool.expr.Expr expr) {
        return register(new android.databinding.tool.expr.UnaryExpr(op, expr));
    }

    public android.databinding.tool.expr.Expr group(android.databinding.tool.expr.Expr grouped) {
        return register(new android.databinding.tool.expr.GroupExpr(grouped));
    }

    public android.databinding.tool.expr.Expr resourceExpr(java.lang.String packageName, java.lang.String resourceType, java.lang.String resourceName, java.util.List<android.databinding.tool.expr.Expr> args) {
        return register(new android.databinding.tool.expr.ResourceExpr(packageName, resourceType, resourceName, args));
    }

    public android.databinding.tool.expr.Expr bracketExpr(android.databinding.tool.expr.Expr variableExpr, android.databinding.tool.expr.Expr argExpr) {
        return register(new android.databinding.tool.expr.BracketExpr(variableExpr, argExpr));
    }

    public android.databinding.tool.expr.Expr castExpr(java.lang.String type, android.databinding.tool.expr.Expr expr) {
        return register(new android.databinding.tool.expr.CastExpr(type, expr));
    }

    public android.databinding.tool.expr.TwoWayListenerExpr twoWayListenerExpr(android.databinding.tool.InverseBinding inverseBinding) {
        return register(new android.databinding.tool.expr.TwoWayListenerExpr(inverseBinding));
    }

    public java.util.List<android.databinding.tool.expr.Expr> getBindingExpressions() {
        return mBindingExpressions;
    }

    public android.databinding.tool.expr.StaticIdentifierExpr addImport(java.lang.String alias, java.lang.String type, android.databinding.tool.store.Location location) {
        android.databinding.tool.util.Preconditions.check(!mImports.containsKey(alias), "%s has already been defined as %s", alias, type);
        final android.databinding.tool.expr.StaticIdentifierExpr id = staticIdentifier(alias);
        android.databinding.tool.util.L.d("adding import %s as %s klass: %s", type, alias, id.getClass().getSimpleName());
        id.setUserDefinedType(type);
        if (location != null) {
            id.addLocation(location);
        }
        mImports.put(alias, type);
        return id;
    }

    public java.util.Map<java.lang.String, java.lang.String> getImports() {
        return mImports;
    }

    /**
     * The actual thingy that is set on the binding target.
     *
     * Input must be already registered
     */
    public android.databinding.tool.expr.Expr bindingExpr(android.databinding.tool.expr.Expr bindingExpr) {
        android.databinding.tool.util.Preconditions.check(mExprMap.containsKey(bindingExpr.getUniqueKey()), "Main expression should already be registered");
        if (!mBindingExpressions.contains(bindingExpr)) {
            mBindingExpressions.add(bindingExpr);
        }
        return bindingExpr;
    }

    public void removeExpr(android.databinding.tool.expr.Expr expr) {
        android.databinding.tool.util.Preconditions.check(!mSealed, "Can't modify the expression list after sealing the model.");
        mBindingExpressions.remove(expr);
        mExprMap.remove(expr.computeUniqueKey());
    }

    public java.util.List<android.databinding.tool.expr.Expr> getObservables() {
        return mObservables;
    }

    /**
     * Give id to each expression. Will be useful if we serialize.
     */
    public void seal() {
        android.databinding.tool.util.L.d("sealing model");
        java.util.List<android.databinding.tool.expr.Expr> notifiableExpressions = new java.util.ArrayList<android.databinding.tool.expr.Expr>();
        // ensure class analyzer. We need to know observables at this point
        final android.databinding.tool.reflection.ModelAnalyzer modelAnalyzer = android.databinding.tool.reflection.ModelAnalyzer.getInstance();
        updateExpressions(modelAnalyzer);
        int counter = 0;
        final java.lang.Iterable<android.databinding.tool.expr.Expr> observables = filterObservables(modelAnalyzer);
        java.util.List<java.lang.String> flagMapping = new java.util.ArrayList<java.lang.String>();
        mObservables = new java.util.ArrayList<android.databinding.tool.expr.Expr>();
        for (android.databinding.tool.expr.Expr expr : observables) {
            // observables gets initial ids
            flagMapping.add(expr.getUniqueKey());
            expr.setId(counter++);
            mObservables.add(expr);
            notifiableExpressions.add(expr);
            android.databinding.tool.util.L.d("observable %s", expr.getUniqueKey());
        }
        // non-observable identifiers gets next ids
        final java.lang.Iterable<android.databinding.tool.expr.Expr> nonObservableIds = filterNonObservableIds(modelAnalyzer);
        for (android.databinding.tool.expr.Expr expr : nonObservableIds) {
            flagMapping.add(expr.getUniqueKey());
            expr.setId(counter++);
            notifiableExpressions.add(expr);
            android.databinding.tool.util.L.d("non-observable %s", expr.getUniqueKey());
        }
        // descendants of observables gets following ids
        for (android.databinding.tool.expr.Expr expr : observables) {
            for (android.databinding.tool.expr.Expr parent : expr.getParents()) {
                if (parent.hasId()) {
                    continue;// already has some id, means observable

                }
                // only fields earn an id
                if (parent instanceof android.databinding.tool.expr.FieldAccessExpr) {
                    android.databinding.tool.expr.FieldAccessExpr fae = ((android.databinding.tool.expr.FieldAccessExpr) (parent));
                    android.databinding.tool.util.L.d("checking field access expr %s. getter: %s", fae, fae.getGetter());
                    if (fae.isDynamic() && fae.getGetter().canBeInvalidated()) {
                        flagMapping.add(parent.getUniqueKey());
                        parent.setId(counter++);
                        notifiableExpressions.add(parent);
                        android.databinding.tool.util.L.d("notifiable field %s : %s for %s : %s", parent.getUniqueKey(), java.lang.Integer.toHexString(java.lang.System.identityHashCode(parent)), expr.getUniqueKey(), java.lang.Integer.toHexString(java.lang.System.identityHashCode(expr)));
                    }
                }
            }
        }
        // now all 2-way bound view fields
        for (android.databinding.tool.expr.Expr expr : mExprMap.values()) {
            if (expr instanceof android.databinding.tool.expr.FieldAccessExpr) {
                android.databinding.tool.expr.FieldAccessExpr fieldAccessExpr = ((android.databinding.tool.expr.FieldAccessExpr) (expr));
                if (fieldAccessExpr.getChild() instanceof android.databinding.tool.expr.ViewFieldExpr) {
                    flagMapping.add(fieldAccessExpr.getUniqueKey());
                    fieldAccessExpr.setId(counter++);
                }
            }
        }
        // non-dynamic binding expressions receive some ids so that they can be invalidated
        android.databinding.tool.util.L.d("list of binding expressions");
        for (int i = 0; i < mBindingExpressions.size(); i++) {
            android.databinding.tool.util.L.d("[%d] %s", i, mBindingExpressions.get(i));
        }
        // we don't assign ids to constant binding expressions because now invalidateAll has its own
        // flag.
        for (android.databinding.tool.expr.Expr expr : notifiableExpressions) {
            expr.enableDirectInvalidation();
        }
        // make sure all dependencies are resolved to avoid future race conditions
        for (android.databinding.tool.expr.Expr expr : mExprMap.values()) {
            expr.getDependencies();
        }
        mInvalidateAnyFlagIndex = counter++;
        flagMapping.add("INVALIDATE ANY");
        mInvalidateableFieldLimit = counter;
        mInvalidateableFlags = new java.util.BitSet();
        for (int i = 0; i < mInvalidateableFieldLimit; i++) {
            mInvalidateableFlags.set(i, true);
        }
        // make sure all dependencies are resolved to avoid future race conditions
        for (android.databinding.tool.expr.Expr expr : mExprMap.values()) {
            if (expr.isConditional()) {
                android.databinding.tool.util.L.d("requirement id for %s is %d", expr, counter);
                expr.setRequirementId(counter);
                flagMapping.add(expr.getUniqueKey() + android.databinding.tool.expr.ExprModel.FALSE_KEY_SUFFIX);
                flagMapping.add(expr.getUniqueKey() + android.databinding.tool.expr.ExprModel.TRUE_KEY_SUFFIX);
                counter += 2;
            }
        }
        mConditionalFlags = new java.util.BitSet();
        for (int i = mInvalidateableFieldLimit; i < counter; i++) {
            mConditionalFlags.set(i, true);
        }
        mRequirementIdCount = (counter - mInvalidateableFieldLimit) / 2;
        // everybody gets an id
        for (java.util.Map.Entry<java.lang.String, android.databinding.tool.expr.Expr> entry : mExprMap.entrySet()) {
            final android.databinding.tool.expr.Expr value = entry.getValue();
            if (!value.hasId()) {
                value.setId(counter++);
            }
        }
        mFlagMapping = new java.lang.String[flagMapping.size()];
        flagMapping.toArray(mFlagMapping);
        mFlagBucketCount = 1 + (getTotalFlagCount() / android.databinding.tool.writer.FlagSet.sBucketSize);
        mInvalidateAnyFlags = new java.util.BitSet();
        mInvalidateAnyFlags.set(mInvalidateAnyFlagIndex, true);
        for (android.databinding.tool.expr.Expr expr : mExprMap.values()) {
            expr.getShouldReadFlagsWithConditionals();
        }
        for (android.databinding.tool.expr.Expr expr : mExprMap.values()) {
            // ensure all types are calculated
            expr.getResolvedType();
        }
        mSealed = true;
    }

    /**
     * Run updateExpr on each binding expression until no new expressions are added.
     * <p>
     * Some expressions (e.g. field access) may replace themselves and add/remove new dependencies
     * so we need to make sure each expression's update is called at least once.
     */
    private void updateExpressions(android.databinding.tool.reflection.ModelAnalyzer modelAnalyzer) {
        int startSize = -1;
        while (startSize != mExprMap.size()) {
            startSize = mExprMap.size();
            java.util.ArrayList<android.databinding.tool.expr.Expr> exprs = new java.util.ArrayList<android.databinding.tool.expr.Expr>(mBindingExpressions);
            for (android.databinding.tool.expr.Expr expr : exprs) {
                expr.updateExpr(modelAnalyzer);
            }
        } 
    }

    public int getFlagBucketCount() {
        return mFlagBucketCount;
    }

    public int getTotalFlagCount() {
        return (mRequirementIdCount * 2) + mInvalidateableFieldLimit;
    }

    public int getInvalidateableFieldLimit() {
        return mInvalidateableFieldLimit;
    }

    public java.lang.String[] getFlagMapping() {
        return mFlagMapping;
    }

    public java.lang.String getFlag(int id) {
        return mFlagMapping[id];
    }

    private java.util.List<android.databinding.tool.expr.Expr> filterNonObservableIds(final android.databinding.tool.reflection.ModelAnalyzer modelAnalyzer) {
        java.util.List<android.databinding.tool.expr.Expr> result = new java.util.ArrayList<android.databinding.tool.expr.Expr>();
        for (android.databinding.tool.expr.Expr input : mExprMap.values()) {
            if ((((input instanceof android.databinding.tool.expr.IdentifierExpr) && (!input.hasId())) && (!input.isObservable())) && input.isDynamic()) {
                result.add(input);
            }
        }
        return result;
    }

    private java.lang.Iterable<android.databinding.tool.expr.Expr> filterObservables(final android.databinding.tool.reflection.ModelAnalyzer modelAnalyzer) {
        java.util.List<android.databinding.tool.expr.Expr> result = new java.util.ArrayList<android.databinding.tool.expr.Expr>();
        for (android.databinding.tool.expr.Expr input : mExprMap.values()) {
            if (input.isObservable()) {
                result.add(input);
            }
        }
        return result;
    }

    public java.util.List<android.databinding.tool.expr.Expr> getPendingExpressions() {
        if (mPendingExpressions == null) {
            mPendingExpressions = new java.util.ArrayList<android.databinding.tool.expr.Expr>();
            for (android.databinding.tool.expr.Expr expr : mExprMap.values()) {
                // if an expression is NOT dynanic but has conditional dependants, still return it
                // so that conditional flags can be set
                if ((!expr.isRead()) && (expr.isDynamic() || expr.hasConditionalDependant())) {
                    mPendingExpressions.add(expr);
                }
            }
        }
        return mPendingExpressions;
    }

    public boolean markBitsRead() {
        // each has should read flags, we set them back on them
        java.util.List<android.databinding.tool.expr.Expr> markedSomeFlagsRead = new java.util.ArrayList<android.databinding.tool.expr.Expr>();
        for (android.databinding.tool.expr.Expr expr : android.databinding.tool.expr.ExprModel.filterShouldRead(getPendingExpressions())) {
            expr.markFlagsAsRead(expr.getShouldReadFlags());
            markedSomeFlagsRead.add(expr);
        }
        return pruneDone(markedSomeFlagsRead);
    }

    private boolean pruneDone(java.util.List<android.databinding.tool.expr.Expr> markedSomeFlagsAsRead) {
        boolean marked = true;
        java.util.List<android.databinding.tool.expr.Expr> markedAsReadList = new java.util.ArrayList<android.databinding.tool.expr.Expr>();
        while (marked) {
            marked = false;
            for (android.databinding.tool.expr.Expr expr : mExprMap.values()) {
                if (expr.isRead()) {
                    continue;
                }
                if (expr.markAsReadIfDone()) {
                    android.databinding.tool.util.L.d("marked %s as read ", expr.getUniqueKey());
                    marked = true;
                    markedAsReadList.add(expr);
                    markedSomeFlagsAsRead.remove(expr);
                }
            }
        } 
        boolean elevated = false;
        for (android.databinding.tool.expr.Expr markedAsRead : markedAsReadList) {
            for (android.databinding.tool.expr.Dependency dependency : markedAsRead.getDependants()) {
                if (dependency.getDependant().considerElevatingConditionals(markedAsRead)) {
                    elevated = true;
                }
            }
        }
        for (android.databinding.tool.expr.Expr partialRead : markedSomeFlagsAsRead) {
            // even if all paths are not satisfied, we can elevate certain conditional dependencies
            // if all of their paths are satisfied.
            for (android.databinding.tool.expr.Dependency dependency : partialRead.getDependants()) {
                android.databinding.tool.expr.Expr dependant = dependency.getDependant();
                if (dependant.isConditional() && dependant.getAllCalculationPaths().areAllPathsSatisfied(partialRead.mReadSoFar)) {
                    if (dependant.considerElevatingConditionals(partialRead)) {
                        elevated = true;
                    }
                }
            }
        }
        if (elevated) {
            // some conditionals are elevated. We should re-calculate flags
            for (android.databinding.tool.expr.Expr expr : getPendingExpressions()) {
                if (!expr.isRead()) {
                    expr.invalidateReadFlags();
                }
            }
            mPendingExpressions = null;
        }
        return elevated;
    }

    private static boolean hasConditionalOrNestedCannotReadDependency(android.databinding.tool.expr.Expr expr) {
        for (android.databinding.tool.expr.Dependency dependency : expr.getDependencies()) {
            if (dependency.isConditional() || dependency.getOther().hasNestedCannotRead()) {
                return true;
            }
        }
        return false;
    }

    public static java.util.List<android.databinding.tool.expr.Expr> filterShouldRead(java.lang.Iterable<android.databinding.tool.expr.Expr> exprs) {
        java.util.List<android.databinding.tool.expr.Expr> result = new java.util.ArrayList<android.databinding.tool.expr.Expr>();
        for (android.databinding.tool.expr.Expr expr : exprs) {
            if ((!expr.getShouldReadFlags().isEmpty()) && (!android.databinding.tool.expr.ExprModel.hasConditionalOrNestedCannotReadDependency(expr))) {
                result.add(expr);
            }
        }
        return result;
    }

    /**
     * May return null if flag is equal to invalidate any flag.
     */
    public android.databinding.tool.expr.Expr findFlagExpression(int flag) {
        if (mInvalidateAnyFlags.get(flag)) {
            return null;
        }
        final java.lang.String key = mFlagMapping[flag];
        if (mExprMap.containsKey(key)) {
            return mExprMap.get(key);
        }
        int falseIndex = key.indexOf(android.databinding.tool.expr.ExprModel.FALSE_KEY_SUFFIX);
        if (falseIndex > (-1)) {
            final java.lang.String trimmed = key.substring(0, falseIndex);
            return mExprMap.get(trimmed);
        }
        int trueIndex = key.indexOf(android.databinding.tool.expr.ExprModel.TRUE_KEY_SUFFIX);
        if (trueIndex > (-1)) {
            final java.lang.String trimmed = key.substring(0, trueIndex);
            return mExprMap.get(trimmed);
        }
        // log everything we call
        java.lang.StringBuilder error = new java.lang.StringBuilder();
        error.append("cannot find flag:").append(flag).append("\n");
        error.append("invalidate any flag:").append(mInvalidateAnyFlags).append("\n");
        error.append("key:").append(key).append("\n");
        error.append("flag mapping:").append(java.util.Arrays.toString(mFlagMapping));
        android.databinding.tool.util.L.e(error.toString());
        return null;
    }

    public java.util.BitSet getInvalidateAnyBitSet() {
        return mInvalidateAnyFlags;
    }

    public int getInvalidateAnyFlagIndex() {
        return mInvalidateAnyFlagIndex;
    }

    public android.databinding.tool.expr.Expr argListExpr(java.lang.Iterable<android.databinding.tool.expr.Expr> expressions) {
        return register(new android.databinding.tool.expr.ArgListExpr(mArgListIdCounter++, expressions));
    }

    public void setCurrentLocationInFile(android.databinding.tool.store.Location location) {
        mCurrentLocationInFile = location;
    }

    public android.databinding.tool.expr.Expr listenerExpr(android.databinding.tool.expr.Expr expression, java.lang.String name, android.databinding.tool.reflection.ModelClass listenerType, android.databinding.tool.reflection.ModelMethod listenerMethod) {
        return register(new android.databinding.tool.expr.ListenerExpr(expression, name, listenerType, listenerMethod));
    }
}

