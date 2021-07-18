/**
 * Copyright (C) 2015 The Android Open Source Project
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package android.databinding.tool.expr;


public class Dependency {
    final android.databinding.tool.expr.Expr mDependant;

    final android.databinding.tool.expr.Expr mOther;

    final android.databinding.tool.expr.Expr mCondition;

    final boolean mExpectedOutput;// !


    // set only if this is conditional. Means it has been resolved so that it can be used in
    // should get calculations
    boolean mElevated;

    // this means that trying to calculate the dependant expression w/o
    // will crash the app unless "Other" has a non-null value
    boolean mMandatory = false;

    public Dependency(android.databinding.tool.expr.Expr dependant, android.databinding.tool.expr.Expr other) {
        mDependant = dependant;
        mOther = other;
        mCondition = null;
        mOther.addDependant(this);
        mExpectedOutput = false;
    }

    public Dependency(android.databinding.tool.expr.Expr dependant, android.databinding.tool.expr.Expr other, android.databinding.tool.expr.Expr condition, boolean expectedOutput) {
        mDependant = dependant;
        mOther = other;
        mCondition = condition;
        mOther.addDependant(this);
        mExpectedOutput = expectedOutput;
    }

    public void setMandatory(boolean mandatory) {
        mMandatory = mandatory;
    }

    public boolean isMandatory() {
        return mMandatory;
    }

    public boolean isConditional() {
        return (mCondition != null) && (!mElevated);
    }

    public android.databinding.tool.expr.Expr getOther() {
        return mOther;
    }

    public android.databinding.tool.expr.Expr getDependant() {
        return mDependant;
    }

    public boolean getExpectedOutput() {
        return mExpectedOutput;
    }

    public android.databinding.tool.expr.Expr getCondition() {
        return mCondition;
    }

    public void elevate() {
        mElevated = true;
    }

    public boolean isElevated() {
        return mElevated;
    }
}

