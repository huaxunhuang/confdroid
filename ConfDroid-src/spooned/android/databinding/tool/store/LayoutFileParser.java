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
package android.databinding.tool.store;


/**
 * Gets the list of XML files and creates a list of
 * {@link android.databinding.tool.store.ResourceBundle} that can be persistent or converted to
 * LayoutBinder.
 */
public class LayoutFileParser {
    private static final java.lang.String XPATH_BINDING_LAYOUT = "/layout";

    private static final java.lang.String LAYOUT_PREFIX = "@layout/";

    public android.databinding.tool.store.ResourceBundle.LayoutFileBundle parseXml(final java.io.File inputFile, final java.io.File outputFile, java.lang.String pkg, final android.databinding.tool.LayoutXmlProcessor.OriginalFileLookup originalFileLookup) throws java.io.IOException, javax.xml.parsers.ParserConfigurationException, javax.xml.xpath.XPathExpressionException, org.xml.sax.SAXException {
        java.io.File originalFileFor = originalFileLookup.getOriginalFileFor(inputFile);
        final java.lang.String originalFilePath = originalFileFor.getAbsolutePath();
        try {
            android.databinding.tool.processing.Scope.enter(new android.databinding.tool.processing.scopes.FileScopeProvider() {
                @java.lang.Override
                public java.lang.String provideScopeFilePath() {
                    return originalFilePath;
                }
            });
            final java.lang.String encoding = android.databinding.tool.store.LayoutFileParser.findEncoding(inputFile);
            stripFile(inputFile, outputFile, encoding, originalFileLookup);
            return parseOriginalXml(originalFileFor, pkg, encoding);
        } finally {
            android.databinding.tool.processing.Scope.exit();
        }
    }

    private android.databinding.tool.store.ResourceBundle.LayoutFileBundle parseOriginalXml(final java.io.File original, java.lang.String pkg, java.lang.String encoding) throws java.io.IOException {
        try {
            android.databinding.tool.processing.Scope.enter(new android.databinding.tool.processing.scopes.FileScopeProvider() {
                @java.lang.Override
                public java.lang.String provideScopeFilePath() {
                    return original.getAbsolutePath();
                }
            });
            final java.lang.String xmlNoExtension = android.databinding.tool.util.ParserHelper.stripExtension(original.getName());
            java.io.FileInputStream fin = new java.io.FileInputStream(original);
            java.io.InputStreamReader reader = new java.io.InputStreamReader(fin, encoding);
            org.antlr.v4.runtime.ANTLRInputStream inputStream = new org.antlr.v4.runtime.ANTLRInputStream(reader);
            android.databinding.parser.XMLLexer lexer = new android.databinding.parser.XMLLexer(inputStream);
            org.antlr.v4.runtime.CommonTokenStream tokenStream = new org.antlr.v4.runtime.CommonTokenStream(lexer);
            android.databinding.parser.XMLParser parser = new android.databinding.parser.XMLParser(tokenStream);
            android.databinding.parser.XMLParser.DocumentContext expr = parser.document();
            android.databinding.parser.XMLParser.ElementContext root = expr.element();
            if (!"layout".equals(root.elmName.getText())) {
                return null;
            }
            android.databinding.parser.XMLParser.ElementContext data = getDataNode(root);
            android.databinding.parser.XMLParser.ElementContext rootView = getViewNode(original, root);
            if (hasMergeInclude(rootView)) {
                android.databinding.tool.util.L.e(android.databinding.tool.processing.ErrorMessages.INCLUDE_INSIDE_MERGE);
                return null;
            }
            boolean isMerge = "merge".equals(rootView.elmName.getText());
            android.databinding.tool.store.ResourceBundle.LayoutFileBundle bundle = new android.databinding.tool.store.ResourceBundle.LayoutFileBundle(original, xmlNoExtension, original.getParentFile().getName(), pkg, isMerge);
            final java.lang.String newTag = (original.getParentFile().getName() + '/') + xmlNoExtension;
            parseData(original, data, bundle);
            parseExpressions(newTag, rootView, isMerge, bundle);
            return bundle;
        } finally {
            android.databinding.tool.processing.Scope.exit();
        }
    }

    private static boolean isProcessedElement(java.lang.String name) {
        if (com.google.common.base.Strings.isNullOrEmpty(name)) {
            return false;
        }
        if (("view".equals(name) || "include".equals(name)) || (name.indexOf('.') >= 0)) {
            return true;
        }
        return !name.toLowerCase().equals(name);
    }

    private void parseExpressions(java.lang.String newTag, final android.databinding.parser.XMLParser.ElementContext rootView, final boolean isMerge, android.databinding.tool.store.ResourceBundle.LayoutFileBundle bundle) {
        final java.util.List<android.databinding.parser.XMLParser.ElementContext> bindingElements = new java.util.ArrayList<android.databinding.parser.XMLParser.ElementContext>();
        final java.util.List<android.databinding.parser.XMLParser.ElementContext> otherElementsWithIds = new java.util.ArrayList<android.databinding.parser.XMLParser.ElementContext>();
        rootView.accept(new android.databinding.parser.XMLParserBaseVisitor<java.lang.Void>() {
            @java.lang.Override
            public java.lang.Void visitElement(@org.antlr.v4.runtime.misc.NotNull
            android.databinding.parser.XMLParser.ElementContext ctx) {
                if (filter(ctx)) {
                    bindingElements.add(ctx);
                } else {
                    java.lang.String name = ctx.elmName.getText();
                    if (android.databinding.tool.store.LayoutFileParser.isProcessedElement(name) && android.databinding.tool.store.LayoutFileParser.attributeMap(ctx).containsKey("android:id")) {
                        otherElementsWithIds.add(ctx);
                    }
                }
                visitChildren(ctx);
                return null;
            }

            private boolean filter(android.databinding.parser.XMLParser.ElementContext ctx) {
                if (isMerge) {
                    // account for XMLParser.ContentContext
                    if (ctx.getParent().getParent() == rootView) {
                        return true;
                    }
                } else
                    if (ctx == rootView) {
                        return true;
                    }

                return (hasIncludeChild(ctx) || android.databinding.tool.util.XmlEditor.hasExpressionAttributes(ctx)) || "include".equals(ctx.elmName.getText());
            }

            private boolean hasIncludeChild(android.databinding.parser.XMLParser.ElementContext ctx) {
                for (android.databinding.parser.XMLParser.ElementContext child : android.databinding.tool.util.XmlEditor.elements(ctx)) {
                    if ("include".equals(child.elmName.getText())) {
                        return true;
                    }
                }
                return false;
            }
        });
        final java.util.HashMap<android.databinding.parser.XMLParser.ElementContext, java.lang.String> nodeTagMap = new java.util.HashMap<android.databinding.parser.XMLParser.ElementContext, java.lang.String>();
        android.databinding.tool.util.L.d("number of binding nodes %d", bindingElements.size());
        int tagNumber = 0;
        for (android.databinding.parser.XMLParser.ElementContext parent : bindingElements) {
            final java.util.Map<java.lang.String, java.lang.String> attributes = android.databinding.tool.store.LayoutFileParser.attributeMap(parent);
            java.lang.String nodeName = parent.elmName.getText();
            java.lang.String viewName = null;
            java.lang.String includedLayoutName = null;
            final java.lang.String id = attributes.get("android:id");
            final java.lang.String tag;
            final java.lang.String originalTag = attributes.get("android:tag");
            if ("include".equals(nodeName)) {
                // get the layout attribute
                final java.lang.String includeValue = attributes.get("layout");
                if (com.google.common.base.Strings.isNullOrEmpty(includeValue)) {
                    android.databinding.tool.util.L.e("%s must include a layout", parent);
                }
                if (!includeValue.startsWith(android.databinding.tool.store.LayoutFileParser.LAYOUT_PREFIX)) {
                    android.databinding.tool.util.L.e("included value (%s) must start with %s.", includeValue, android.databinding.tool.store.LayoutFileParser.LAYOUT_PREFIX);
                }
                // if user is binding something there, there MUST be a layout file to be
                // generated.
                includedLayoutName = includeValue.substring(android.databinding.tool.store.LayoutFileParser.LAYOUT_PREFIX.length());
                final org.antlr.v4.runtime.ParserRuleContext myParentContent = parent.getParent();
                android.databinding.tool.util.Preconditions.check(myParentContent instanceof android.databinding.parser.XMLParser.ContentContext, "parent of an include tag must be a content context but it is %s", myParentContent.getClass().getCanonicalName());
                final org.antlr.v4.runtime.ParserRuleContext grandParent = myParentContent.getParent();
                android.databinding.tool.util.Preconditions.check(grandParent instanceof android.databinding.parser.XMLParser.ElementContext, "grandparent of an include tag must be an element context but it is %s", grandParent.getClass().getCanonicalName());
                // noinspection SuspiciousMethodCalls
                tag = nodeTagMap.get(grandParent);
            } else
                if ("fragment".equals(nodeName)) {
                    if (android.databinding.tool.util.XmlEditor.hasExpressionAttributes(parent)) {
                        android.databinding.tool.util.L.e("fragments do not support data binding expressions.");
                    }
                    continue;
                } else {
                    viewName = getViewName(parent);
                    // account for XMLParser.ContentContext
                    if ((rootView == parent) || (isMerge && (parent.getParent().getParent() == rootView))) {
                        tag = (newTag + "_") + tagNumber;
                    } else {
                        tag = "binding_" + tagNumber;
                    }
                    tagNumber++;
                }

            final android.databinding.tool.store.ResourceBundle.BindingTargetBundle bindingTargetBundle = bundle.createBindingTarget(id, viewName, true, tag, originalTag, new android.databinding.tool.store.Location(parent));
            nodeTagMap.put(parent, tag);
            bindingTargetBundle.setIncludedLayout(includedLayoutName);
            for (android.databinding.parser.XMLParser.AttributeContext attr : android.databinding.tool.util.XmlEditor.expressionAttributes(parent)) {
                java.lang.String value = android.databinding.tool.store.LayoutFileParser.escapeQuotes(attr.attrValue.getText(), true);
                final boolean isOneWay = value.startsWith("@{");
                final boolean isTwoWay = value.startsWith("@={");
                if (isOneWay || isTwoWay) {
                    if (value.charAt(value.length() - 1) != '}') {
                        android.databinding.tool.util.L.e("Expecting '}' in expression '%s'", attr.attrValue.getText());
                    }
                    final int startIndex = (isTwoWay) ? 3 : 2;
                    final int endIndex = value.length() - 1;
                    final java.lang.String strippedValue = value.substring(startIndex, endIndex);
                    android.databinding.tool.store.Location attrLocation = new android.databinding.tool.store.Location(attr);
                    android.databinding.tool.store.Location valueLocation = new android.databinding.tool.store.Location();
                    // offset to 0 based
                    valueLocation.startLine = attr.attrValue.getLine() - 1;
                    valueLocation.startOffset = attr.attrValue.getCharPositionInLine() + attr.attrValue.getText().indexOf(strippedValue);
                    valueLocation.endLine = attrLocation.endLine;
                    valueLocation.endOffset = attrLocation.endOffset - 2;// account for: "}

                    bindingTargetBundle.addBinding(android.databinding.tool.store.LayoutFileParser.escapeQuotes(attr.attrName.getText(), false), strippedValue, isTwoWay, attrLocation, valueLocation);
                }
            }
        }
        for (android.databinding.parser.XMLParser.ElementContext elm : otherElementsWithIds) {
            final java.lang.String id = android.databinding.tool.store.LayoutFileParser.attributeMap(elm).get("android:id");
            final java.lang.String className = getViewName(elm);
            bundle.createBindingTarget(id, className, true, null, null, new android.databinding.tool.store.Location(elm));
        }
    }

    private java.lang.String getViewName(android.databinding.parser.XMLParser.ElementContext elm) {
        java.lang.String viewName = elm.elmName.getText();
        if ("view".equals(viewName)) {
            java.lang.String classNode = android.databinding.tool.store.LayoutFileParser.attributeMap(elm).get("class");
            if (com.google.common.base.Strings.isNullOrEmpty(classNode)) {
                android.databinding.tool.util.L.e("No class attribute for 'view' node");
            }
            viewName = classNode;
        } else
            if ("include".equals(viewName) && (!android.databinding.tool.util.XmlEditor.hasExpressionAttributes(elm))) {
                viewName = "android.view.View";
            }

        return viewName;
    }

    private void parseData(java.io.File xml, android.databinding.parser.XMLParser.ElementContext data, android.databinding.tool.store.ResourceBundle.LayoutFileBundle bundle) {
        if (data == null) {
            return;
        }
        for (android.databinding.parser.XMLParser.ElementContext imp : filter(data, "import")) {
            final java.util.Map<java.lang.String, java.lang.String> attrMap = android.databinding.tool.store.LayoutFileParser.attributeMap(imp);
            java.lang.String type = attrMap.get("type");
            java.lang.String alias = attrMap.get("alias");
            android.databinding.tool.util.Preconditions.check(android.databinding.tool.util.StringUtils.isNotBlank(type), "Type of an import cannot be empty." + " %s in %s", imp.toStringTree(), xml);
            if (com.google.common.base.Strings.isNullOrEmpty(alias)) {
                alias = type.substring(type.lastIndexOf('.') + 1);
            }
            bundle.addImport(alias, type, new android.databinding.tool.store.Location(imp));
        }
        for (android.databinding.parser.XMLParser.ElementContext variable : filter(data, "variable")) {
            final java.util.Map<java.lang.String, java.lang.String> attrMap = android.databinding.tool.store.LayoutFileParser.attributeMap(variable);
            java.lang.String type = attrMap.get("type");
            java.lang.String name = attrMap.get("name");
            android.databinding.tool.util.Preconditions.checkNotNull(type, "variable must have a type definition %s in %s", variable.toStringTree(), xml);
            android.databinding.tool.util.Preconditions.checkNotNull(name, "variable must have a name %s in %s", variable.toStringTree(), xml);
            bundle.addVariable(name, type, new android.databinding.tool.store.Location(variable), true);
        }
        final android.databinding.parser.XMLParser.AttributeContext className = android.databinding.tool.store.LayoutFileParser.findAttribute(data, "class");
        if (className != null) {
            final java.lang.String name = android.databinding.tool.store.LayoutFileParser.escapeQuotes(className.attrValue.getText(), true);
            if (android.databinding.tool.util.StringUtils.isNotBlank(name)) {
                android.databinding.tool.store.Location location = new android.databinding.tool.store.Location(className.attrValue.getLine() - 1, className.attrValue.getCharPositionInLine() + 1, className.attrValue.getLine() - 1, className.attrValue.getCharPositionInLine() + name.length());
                bundle.setBindingClass(name, location);
            }
        }
    }

    private android.databinding.parser.XMLParser.ElementContext getDataNode(android.databinding.parser.XMLParser.ElementContext root) {
        final java.util.List<android.databinding.parser.XMLParser.ElementContext> data = filter(root, "data");
        if (data.size() == 0) {
            return null;
        }
        android.databinding.tool.util.Preconditions.check(data.size() == 1, "XML layout can have only 1 data tag");
        return data.get(0);
    }

    private android.databinding.parser.XMLParser.ElementContext getViewNode(java.io.File xml, android.databinding.parser.XMLParser.ElementContext root) {
        final java.util.List<android.databinding.parser.XMLParser.ElementContext> view = filterNot(root, "data");
        android.databinding.tool.util.Preconditions.check(view.size() == 1, "XML layout %s must have 1 view but has %s. root" + " children count %s", xml, view.size(), root.getChildCount());
        return view.get(0);
    }

    private java.util.List<android.databinding.parser.XMLParser.ElementContext> filter(android.databinding.parser.XMLParser.ElementContext root, java.lang.String name) {
        java.util.List<android.databinding.parser.XMLParser.ElementContext> result = new java.util.ArrayList<android.databinding.parser.XMLParser.ElementContext>();
        if (root == null) {
            return result;
        }
        final android.databinding.parser.XMLParser.ContentContext content = root.content();
        if (content == null) {
            return result;
        }
        for (android.databinding.parser.XMLParser.ElementContext child : android.databinding.tool.util.XmlEditor.elements(root)) {
            if (name.equals(child.elmName.getText())) {
                result.add(child);
            }
        }
        return result;
    }

    private java.util.List<android.databinding.parser.XMLParser.ElementContext> filterNot(android.databinding.parser.XMLParser.ElementContext root, java.lang.String name) {
        java.util.List<android.databinding.parser.XMLParser.ElementContext> result = new java.util.ArrayList<android.databinding.parser.XMLParser.ElementContext>();
        if (root == null) {
            return result;
        }
        final android.databinding.parser.XMLParser.ContentContext content = root.content();
        if (content == null) {
            return result;
        }
        for (android.databinding.parser.XMLParser.ElementContext child : android.databinding.tool.util.XmlEditor.elements(root)) {
            if (!name.equals(child.elmName.getText())) {
                result.add(child);
            }
        }
        return result;
    }

    private boolean hasMergeInclude(android.databinding.parser.XMLParser.ElementContext rootView) {
        return "merge".equals(rootView.elmName.getText()) && (filter(rootView, "include").size() > 0);
    }

    private void stripFile(java.io.File xml, java.io.File out, java.lang.String encoding, android.databinding.tool.LayoutXmlProcessor.OriginalFileLookup originalFileLookup) throws java.io.IOException, javax.xml.parsers.ParserConfigurationException, javax.xml.xpath.XPathExpressionException, org.xml.sax.SAXException {
        javax.xml.parsers.DocumentBuilderFactory factory = javax.xml.parsers.DocumentBuilderFactory.newInstance();
        javax.xml.parsers.DocumentBuilder builder = factory.newDocumentBuilder();
        org.w3c.dom.Document doc = builder.parse(xml);
        javax.xml.xpath.XPathFactory xPathFactory = javax.xml.xpath.XPathFactory.newInstance();
        javax.xml.xpath.XPath xPath = xPathFactory.newXPath();
        java.io.File actualFile = (originalFileLookup == null) ? null : originalFileLookup.getOriginalFileFor(xml);
        // TODO get rid of original file lookup
        if (actualFile == null) {
            actualFile = xml;
        }
        // always create id from actual file when available. Gradle may duplicate files.
        java.lang.String noExt = android.databinding.tool.util.ParserHelper.stripExtension(actualFile.getName());
        java.lang.String binderId = (actualFile.getParentFile().getName() + '/') + noExt;
        // now if file has any binding expressions, find and delete them
        boolean changed = isBindingLayout(doc, xPath);
        if (changed) {
            stripBindingTags(xml, out, binderId, encoding);
        } else
            if (!xml.equals(out)) {
                org.apache.commons.io.FileUtils.copyFile(xml, out);
            }

    }

    private boolean isBindingLayout(org.w3c.dom.Document doc, javax.xml.xpath.XPath xPath) throws javax.xml.xpath.XPathExpressionException {
        return !get(doc, xPath, android.databinding.tool.store.LayoutFileParser.XPATH_BINDING_LAYOUT).isEmpty();
    }

    private java.util.List<org.w3c.dom.Node> get(org.w3c.dom.Document doc, javax.xml.xpath.XPath xPath, java.lang.String pattern) throws javax.xml.xpath.XPathExpressionException {
        final javax.xml.xpath.XPathExpression expr = xPath.compile(pattern);
        return toList(((org.w3c.dom.NodeList) (expr.evaluate(doc, javax.xml.xpath.XPathConstants.NODESET))));
    }

    private java.util.List<org.w3c.dom.Node> toList(org.w3c.dom.NodeList nodeList) {
        java.util.List<org.w3c.dom.Node> result = new java.util.ArrayList<org.w3c.dom.Node>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            result.add(nodeList.item(i));
        }
        return result;
    }

    private void stripBindingTags(java.io.File xml, java.io.File output, java.lang.String newTag, java.lang.String encoding) throws java.io.IOException {
        java.lang.String res = android.databinding.tool.util.XmlEditor.strip(xml, newTag, encoding);
        android.databinding.tool.util.Preconditions.checkNotNull(res, "layout file should've changed %s", xml.getAbsolutePath());
        if (res != null) {
            android.databinding.tool.util.L.d("file %s has changed, overwriting %s", xml.getName(), xml.getAbsolutePath());
            org.apache.commons.io.FileUtils.writeStringToFile(output, res, encoding);
        }
    }

    private static java.lang.String findEncoding(java.io.File f) throws java.io.IOException {
        java.io.FileInputStream fin = new java.io.FileInputStream(f);
        try {
            org.mozilla.universalchardet.UniversalDetector universalDetector = new org.mozilla.universalchardet.UniversalDetector(null);
            byte[] buf = new byte[4096];
            int nread;
            while (((nread = fin.read(buf)) > 0) && (!universalDetector.isDone())) {
                universalDetector.handleData(buf, 0, nread);
            } 
            universalDetector.dataEnd();
            java.lang.String encoding = universalDetector.getDetectedCharset();
            if (encoding == null) {
                encoding = "utf-8";
            }
            return encoding;
        } finally {
            fin.close();
        }
    }

    private static java.util.Map<java.lang.String, java.lang.String> attributeMap(android.databinding.parser.XMLParser.ElementContext root) {
        final java.util.Map<java.lang.String, java.lang.String> result = new java.util.HashMap<java.lang.String, java.lang.String>();
        for (android.databinding.parser.XMLParser.AttributeContext attr : android.databinding.tool.util.XmlEditor.attributes(root)) {
            result.put(android.databinding.tool.store.LayoutFileParser.escapeQuotes(attr.attrName.getText(), false), android.databinding.tool.store.LayoutFileParser.escapeQuotes(attr.attrValue.getText(), true));
        }
        return result;
    }

    private static android.databinding.parser.XMLParser.AttributeContext findAttribute(android.databinding.parser.XMLParser.ElementContext element, java.lang.String name) {
        for (android.databinding.parser.XMLParser.AttributeContext attr : element.attribute()) {
            if (android.databinding.tool.store.LayoutFileParser.escapeQuotes(attr.attrName.getText(), false).equals(name)) {
                return attr;
            }
        }
        return null;
    }

    private static java.lang.String escapeQuotes(java.lang.String textWithQuotes, boolean unescapeValue) {
        char first = textWithQuotes.charAt(0);
        int start = 0;
        int end = textWithQuotes.length();
        if ((first == '"') || (first == '\'')) {
            start = 1;
        }
        char last = textWithQuotes.charAt(textWithQuotes.length() - 1);
        if ((last == '"') || (last == '\'')) {
            end -= 1;
        }
        java.lang.String val = textWithQuotes.substring(start, end);
        if (unescapeValue) {
            return android.databinding.tool.util.StringUtils.unescapeXml(val);
        } else {
            return val;
        }
    }
}

