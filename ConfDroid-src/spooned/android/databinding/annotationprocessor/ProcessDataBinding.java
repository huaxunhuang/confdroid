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
package android.databinding.annotationprocessor;


/**
 * Parent annotation processor that dispatches sub steps to ensure execution order.
 * Use initProcessingSteps to add a new step.
 */
@javax.annotation.processing.SupportedAnnotationTypes({ "android.databinding.BindingAdapter", "android.databinding.Untaggable", "android.databinding.BindingMethods", "android.databinding.BindingConversion", "android.databinding.BindingBuildInfo" })
public class ProcessDataBinding extends javax.annotation.processing.AbstractProcessor {
    private java.util.List<android.databinding.annotationprocessor.ProcessDataBinding.ProcessingStep> mProcessingSteps;

    @java.lang.Override
    public boolean process(java.util.Set<? extends javax.lang.model.element.TypeElement> annotations, javax.annotation.processing.RoundEnvironment roundEnv) {
        if (mProcessingSteps == null) {
            initProcessingSteps();
        }
        final android.databinding.BindingBuildInfo buildInfo = android.databinding.annotationprocessor.BuildInfoUtil.load(roundEnv);
        if (buildInfo == null) {
            return false;
        }
        boolean done = true;
        for (android.databinding.annotationprocessor.ProcessDataBinding.ProcessingStep step : mProcessingSteps) {
            try {
                done = step.runStep(roundEnv, processingEnv, buildInfo) && done;
            } catch (javax.xml.bind.JAXBException e) {
                android.databinding.tool.util.L.e(e, "Exception while handling step %s", step);
            }
        }
        if (roundEnv.processingOver()) {
            for (android.databinding.annotationprocessor.ProcessDataBinding.ProcessingStep step : mProcessingSteps) {
                step.onProcessingOver(roundEnv, processingEnv, buildInfo);
            }
        }
        android.databinding.tool.processing.Scope.assertNoError();
        return done;
    }

    @java.lang.Override
    public javax.lang.model.SourceVersion getSupportedSourceVersion() {
        return javax.lang.model.SourceVersion.latest();
    }

    private void initProcessingSteps() {
        final android.databinding.annotationprocessor.ProcessBindable processBindable = new android.databinding.annotationprocessor.ProcessBindable();
        mProcessingSteps = java.util.Arrays.asList(new android.databinding.annotationprocessor.ProcessMethodAdapters(), new android.databinding.annotationprocessor.ProcessExpressions(), processBindable);
        android.databinding.annotationprocessor.ProcessDataBinding.Callback dataBinderWriterCallback = new android.databinding.annotationprocessor.ProcessDataBinding.Callback() {
            android.databinding.tool.CompilerChef mChef;

            android.databinding.tool.writer.BRWriter mBRWriter;

            boolean mLibraryProject;

            int mMinSdk;

            @java.lang.Override
            public void onChefReady(android.databinding.tool.CompilerChef chef, boolean libraryProject, int minSdk) {
                android.databinding.tool.util.Preconditions.checkNull(mChef, "Cannot set compiler chef twice");
                chef.addBRVariables(processBindable);
                mChef = chef;
                mLibraryProject = libraryProject;
                mMinSdk = minSdk;
                considerWritingMapper();
                mChef.writeDynamicUtil();
            }

            private void considerWritingMapper() {
                if ((mLibraryProject || (mChef == null)) || (mBRWriter == null)) {
                    return;
                }
                mChef.writeDataBinderMapper(mMinSdk, mBRWriter);
            }

            @java.lang.Override
            public void onBrWriterReady(android.databinding.tool.writer.BRWriter brWriter) {
                android.databinding.tool.util.Preconditions.checkNull(mBRWriter, "Cannot set br writer twice");
                mBRWriter = brWriter;
                considerWritingMapper();
            }
        };
        android.databinding.tool.writer.AnnotationJavaFileWriter javaFileWriter = new android.databinding.tool.writer.AnnotationJavaFileWriter(processingEnv);
        for (android.databinding.annotationprocessor.ProcessDataBinding.ProcessingStep step : mProcessingSteps) {
            step.mJavaFileWriter = javaFileWriter;
            step.mCallback = dataBinderWriterCallback;
        }
    }

    @java.lang.Override
    public synchronized void init(javax.annotation.processing.ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        android.databinding.tool.reflection.ModelAnalyzer.setProcessingEnvironment(processingEnv);
    }

    /**
     * To ensure execution order and binding build information, we use processing steps.
     */
    public static abstract class ProcessingStep {
        private boolean mDone;

        private android.databinding.tool.writer.JavaFileWriter mJavaFileWriter;

        protected android.databinding.annotationprocessor.ProcessDataBinding.Callback mCallback;

        protected android.databinding.tool.writer.JavaFileWriter getWriter() {
            return mJavaFileWriter;
        }

        private boolean runStep(javax.annotation.processing.RoundEnvironment roundEnvironment, javax.annotation.processing.ProcessingEnvironment processingEnvironment, android.databinding.BindingBuildInfo buildInfo) throws javax.xml.bind.JAXBException {
            if (mDone) {
                return true;
            }
            mDone = onHandleStep(roundEnvironment, processingEnvironment, buildInfo);
            return mDone;
        }

        /**
         * Invoked in each annotation processing step.
         *
         * @return True if it is done and should never be invoked again.
         */
        public abstract boolean onHandleStep(javax.annotation.processing.RoundEnvironment roundEnvironment, javax.annotation.processing.ProcessingEnvironment processingEnvironment, android.databinding.BindingBuildInfo buildInfo) throws javax.xml.bind.JAXBException;

        /**
         * Invoked when processing is done. A good place to generate the output if the
         * processor requires multiple steps.
         */
        public abstract void onProcessingOver(javax.annotation.processing.RoundEnvironment roundEnvironment, javax.annotation.processing.ProcessingEnvironment processingEnvironment, android.databinding.BindingBuildInfo buildInfo);
    }

    interface Callback {
        void onChefReady(android.databinding.tool.CompilerChef chef, boolean libraryProject, int minSdk);

        void onBrWriterReady(android.databinding.tool.writer.BRWriter brWriter);
    }
}

