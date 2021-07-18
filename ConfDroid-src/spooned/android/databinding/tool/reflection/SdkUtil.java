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
package android.databinding.tool.reflection;


/**
 * Class that is used for SDK related stuff.
 * <p>
 * Must be initialized with the sdk location to work properly
 */
public class SdkUtil {
    static android.databinding.tool.reflection.SdkUtil.ApiChecker sApiChecker;

    static int sMinSdk;

    public static void initialize(int minSdk, java.io.File sdkPath) {
        android.databinding.tool.reflection.SdkUtil.sMinSdk = minSdk;
        android.databinding.tool.reflection.SdkUtil.sApiChecker = new android.databinding.tool.reflection.SdkUtil.ApiChecker(new java.io.File(sdkPath.getAbsolutePath() + "/platform-tools/api/api-versions.xml"));
        android.databinding.tool.util.L.d("SdkUtil init, minSdk: %s", minSdk);
    }

    public static int getMinApi(android.databinding.tool.reflection.ModelClass modelClass) {
        return android.databinding.tool.reflection.SdkUtil.sApiChecker.getMinApi(modelClass.getJniDescription(), null);
    }

    public static int getMinApi(android.databinding.tool.reflection.ModelMethod modelMethod) {
        android.databinding.tool.reflection.ModelClass declaringClass = modelMethod.getDeclaringClass();
        android.databinding.tool.util.Preconditions.checkNotNull(android.databinding.tool.reflection.SdkUtil.sApiChecker, "should've initialized api checker");
        while (declaringClass != null) {
            java.lang.String classDesc = declaringClass.getJniDescription();
            java.lang.String methodDesc = modelMethod.getJniDescription();
            int result = android.databinding.tool.reflection.SdkUtil.sApiChecker.getMinApi(classDesc, methodDesc);
            android.databinding.tool.util.L.d("checking method api for %s, class:%s method:%s. result: %d", modelMethod.getName(), classDesc, methodDesc, result);
            if (result > 0) {
                return result;
            }
            declaringClass = declaringClass.getSuperclass();
        } 
        return 1;
    }

    static class ApiChecker {
        private java.util.Map<java.lang.String, java.lang.Integer> mFullLookup;

        private org.w3c.dom.Document mDoc;

        private javax.xml.xpath.XPath mXPath;

        public ApiChecker(java.io.File apiFile) {
            java.io.InputStream inputStream = null;
            try {
                if ((apiFile == null) || (!apiFile.exists())) {
                    inputStream = getClass().getClassLoader().getResourceAsStream("api-versions.xml");
                } else {
                    inputStream = org.apache.commons.io.FileUtils.openInputStream(apiFile);
                }
                javax.xml.parsers.DocumentBuilderFactory factory = javax.xml.parsers.DocumentBuilderFactory.newInstance();
                javax.xml.parsers.DocumentBuilder builder = factory.newDocumentBuilder();
                mDoc = builder.parse(inputStream);
                javax.xml.xpath.XPathFactory xPathFactory = javax.xml.xpath.XPathFactory.newInstance();
                mXPath = xPathFactory.newXPath();
                buildFullLookup();
            } catch (java.lang.Throwable t) {
                android.databinding.tool.util.L.e(t, "cannot load api descriptions from %s", apiFile);
            } finally {
                org.apache.commons.io.IOUtils.closeQuietly(inputStream);
            }
        }

        private void buildFullLookup() throws javax.xml.xpath.XPathExpressionException {
            org.w3c.dom.NodeList allClasses = mDoc.getChildNodes().item(0).getChildNodes();
            mFullLookup = new java.util.HashMap<java.lang.String, java.lang.Integer>(allClasses.getLength() * 4);
            for (int j = 0; j < allClasses.getLength(); j++) {
                org.w3c.dom.Node node = allClasses.item(j);
                if ((node.getNodeType() != org.w3c.dom.Node.ELEMENT_NODE) || (!"class".equals(node.getNodeName()))) {
                    continue;
                }
                // L.d("checking node %s", node.getAttributes().getNamedItem("name").getNodeValue());
                int classSince = android.databinding.tool.reflection.SdkUtil.ApiChecker.getSince(node);
                java.lang.String classDesc = node.getAttributes().getNamedItem("name").getNodeValue();
                final org.w3c.dom.NodeList childNodes = node.getChildNodes();
                for (int i = 0; i < childNodes.getLength(); i++) {
                    org.w3c.dom.Node child = childNodes.item(i);
                    if ((child.getNodeType() != org.w3c.dom.Node.ELEMENT_NODE) || (!"method".equals(child.getNodeName()))) {
                        continue;
                    }
                    int methodSince = android.databinding.tool.reflection.SdkUtil.ApiChecker.getSince(child);
                    int since = java.lang.Math.max(classSince, methodSince);
                    java.lang.String methodDesc = child.getAttributes().getNamedItem("name").getNodeValue();
                    java.lang.String key = android.databinding.tool.reflection.SdkUtil.ApiChecker.cacheKey(classDesc, methodDesc);
                    mFullLookup.put(key, since);
                }
            }
        }

        /**
         * Returns 0 if we cannot find the API level for the method.
         */
        public int getMinApi(java.lang.String classDesc, java.lang.String methodOrFieldDesc) {
            if ((mDoc == null) || (mXPath == null)) {
                return 1;
            }
            if ((classDesc == null) || classDesc.isEmpty()) {
                return 1;
            }
            final java.lang.String key = android.databinding.tool.reflection.SdkUtil.ApiChecker.cacheKey(classDesc, methodOrFieldDesc);
            java.lang.Integer since = mFullLookup.get(key);
            return since == null ? 0 : since;
        }

        private static java.lang.String cacheKey(java.lang.String classDesc, java.lang.String methodOrFieldDesc) {
            return (classDesc + "~") + methodOrFieldDesc;
        }

        private static int getSince(org.w3c.dom.Node node) {
            final org.w3c.dom.Node since = node.getAttributes().getNamedItem("since");
            if (since != null) {
                final java.lang.String nodeValue = since.getNodeValue();
                if ((nodeValue != null) && (!nodeValue.isEmpty())) {
                    try {
                        return java.lang.Integer.parseInt(nodeValue);
                    } catch (java.lang.Throwable t) {
                    }
                }
            }
            return 1;
        }
    }
}

