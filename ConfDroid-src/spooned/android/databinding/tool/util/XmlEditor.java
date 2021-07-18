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
package android.databinding.tool.util;


/**
 * Ugly inefficient class to strip unwanted tags from XML.
 * Band-aid solution to unblock development
 */
public class XmlEditor {
    public static java.lang.String strip(java.io.File f, java.lang.String newTag, java.lang.String encoding) throws java.io.IOException {
        java.io.FileInputStream fin = new java.io.FileInputStream(f);
        java.io.InputStreamReader reader = new java.io.InputStreamReader(fin, encoding);
        org.antlr.v4.runtime.ANTLRInputStream inputStream = new org.antlr.v4.runtime.ANTLRInputStream(reader);
        android.databinding.parser.XMLLexer lexer = new android.databinding.parser.XMLLexer(inputStream);
        org.antlr.v4.runtime.CommonTokenStream tokenStream = new org.antlr.v4.runtime.CommonTokenStream(lexer);
        android.databinding.parser.XMLParser parser = new android.databinding.parser.XMLParser(tokenStream);
        android.databinding.parser.XMLParser.DocumentContext expr = parser.document();
        android.databinding.parser.XMLParser.ElementContext root = expr.element();
        if ((root == null) || (!"layout".equals(android.databinding.tool.util.XmlEditor.nodeName(root)))) {
            return null;// not a binding layout

        }
        java.util.List<? extends android.databinding.parser.XMLParser.ElementContext> childrenOfRoot = android.databinding.tool.util.XmlEditor.elements(root);
        java.util.List<? extends android.databinding.parser.XMLParser.ElementContext> dataNodes = android.databinding.tool.util.XmlEditor.filterNodesByName("data", childrenOfRoot);
        if (dataNodes.size() > 1) {
            android.databinding.tool.util.L.e("Multiple binding data tags in %s. Expecting a maximum of one.", f.getAbsolutePath());
        }
        java.util.ArrayList<java.lang.String> lines = new java.util.ArrayList<java.lang.String>();
        lines.addAll(org.apache.commons.io.FileUtils.readLines(f, "utf-8"));
        for (android.databinding.parser.XMLParser.ElementContext it : dataNodes) {
            android.databinding.tool.util.XmlEditor.replace(lines, android.databinding.tool.util.XmlEditor.toPosition(it.getStart()), android.databinding.tool.util.XmlEditor.toEndPosition(it.getStop()), "");
        }
        java.util.List<? extends android.databinding.parser.XMLParser.ElementContext> layoutNodes = android.databinding.tool.util.XmlEditor.excludeNodesByName("data", childrenOfRoot);
        if (layoutNodes.size() != 1) {
            android.databinding.tool.util.L.e("Only one layout element and one data element are allowed. %s has %d", f.getAbsolutePath(), layoutNodes.size());
        }
        final android.databinding.parser.XMLParser.ElementContext layoutNode = layoutNodes.get(0);
        java.util.ArrayList<android.databinding.tool.util.XmlEditor.TagAndContext> noTag = new java.util.ArrayList<android.databinding.tool.util.XmlEditor.TagAndContext>();
        android.databinding.tool.util.XmlEditor.recurseReplace(layoutNode, lines, noTag, newTag, 0);
        // Remove the <layout>
        android.databinding.tool.util.XmlEditor.Position rootStartTag = android.databinding.tool.util.XmlEditor.toPosition(root.getStart());
        android.databinding.tool.util.XmlEditor.Position rootEndTag = android.databinding.tool.util.XmlEditor.toPosition(root.content().getStart());
        android.databinding.tool.util.XmlEditor.replace(lines, rootStartTag, rootEndTag, "");
        // Remove the </layout>
        android.databinding.tool.util.XmlEditor.PositionPair endLayoutPositions = android.databinding.tool.util.XmlEditor.findTerminalPositions(root, lines);
        android.databinding.tool.util.XmlEditor.replace(lines, endLayoutPositions.left, endLayoutPositions.right, "");
        java.lang.StringBuilder rootAttributes = new java.lang.StringBuilder();
        for (android.databinding.parser.XMLParser.AttributeContext attr : android.databinding.tool.util.XmlEditor.attributes(root)) {
            rootAttributes.append(' ').append(attr.getText());
        }
        android.databinding.tool.util.XmlEditor.TagAndContext noTagRoot = null;
        for (android.databinding.tool.util.XmlEditor.TagAndContext tagAndContext : noTag) {
            if (tagAndContext.getContext() == layoutNode) {
                noTagRoot = tagAndContext;
                break;
            }
        }
        if (noTagRoot != null) {
            android.databinding.tool.util.XmlEditor.TagAndContext newRootTag = new android.databinding.tool.util.XmlEditor.TagAndContext(noTagRoot.getTag() + rootAttributes.toString(), layoutNode);
            int index = noTag.indexOf(noTagRoot);
            noTag.set(index, newRootTag);
        } else {
            android.databinding.tool.util.XmlEditor.TagAndContext newRootTag = new android.databinding.tool.util.XmlEditor.TagAndContext(rootAttributes.toString(), layoutNode);
            noTag.add(newRootTag);
        }
        // noinspection NullableProblems
        java.util.Collections.sort(noTag, new java.util.Comparator<android.databinding.tool.util.XmlEditor.TagAndContext>() {
            @java.lang.Override
            public int compare(android.databinding.tool.util.XmlEditor.TagAndContext o1, android.databinding.tool.util.XmlEditor.TagAndContext o2) {
                android.databinding.tool.util.XmlEditor.Position start1 = android.databinding.tool.util.XmlEditor.toPosition(o1.getContext().getStart());
                android.databinding.tool.util.XmlEditor.Position start2 = android.databinding.tool.util.XmlEditor.toPosition(o2.getContext().getStart());
                int lineCmp = start2.line - start1.line;
                if (lineCmp != 0) {
                    return lineCmp;
                }
                return start2.charIndex - start1.charIndex;
            }
        });
        for (android.databinding.tool.util.XmlEditor.TagAndContext it : noTag) {
            android.databinding.parser.XMLParser.ElementContext element = it.getContext();
            java.lang.String tag = it.getTag();
            android.databinding.tool.util.XmlEditor.Position endTagPosition = android.databinding.tool.util.XmlEditor.endTagPosition(element);
            android.databinding.tool.util.XmlEditor.fixPosition(lines, endTagPosition);
            java.lang.String line = lines.get(endTagPosition.line);
            java.lang.String newLine = ((line.substring(0, endTagPosition.charIndex) + " ") + tag) + line.substring(endTagPosition.charIndex);
            lines.set(endTagPosition.line, newLine);
        }
        return com.google.common.base.Joiner.on(android.databinding.tool.util.StringUtils.LINE_SEPARATOR).join(lines);
    }

    private static <T extends android.databinding.parser.XMLParser.ElementContext> java.util.List<T> filterNodesByName(java.lang.String name, java.lang.Iterable<T> items) {
        java.util.List<T> result = new java.util.ArrayList<T>();
        for (T item : items) {
            if (name.equals(android.databinding.tool.util.XmlEditor.nodeName(item))) {
                result.add(item);
            }
        }
        return result;
    }

    private static <T extends android.databinding.parser.XMLParser.ElementContext> java.util.List<T> excludeNodesByName(java.lang.String name, java.lang.Iterable<T> items) {
        java.util.List<T> result = new java.util.ArrayList<T>();
        for (T item : items) {
            if (!name.equals(android.databinding.tool.util.XmlEditor.nodeName(item))) {
                result.add(item);
            }
        }
        return result;
    }

    private static android.databinding.tool.util.XmlEditor.Position toPosition(org.antlr.v4.runtime.Token token) {
        return new android.databinding.tool.util.XmlEditor.Position(token.getLine() - 1, token.getCharPositionInLine());
    }

    private static android.databinding.tool.util.XmlEditor.Position toEndPosition(org.antlr.v4.runtime.Token token) {
        return new android.databinding.tool.util.XmlEditor.Position(token.getLine() - 1, token.getCharPositionInLine() + token.getText().length());
    }

    public static java.lang.String nodeName(android.databinding.parser.XMLParser.ElementContext elementContext) {
        return elementContext.elmName.getText();
    }

    public static java.util.List<? extends android.databinding.parser.XMLParser.AttributeContext> attributes(android.databinding.parser.XMLParser.ElementContext elementContext) {
        if (elementContext.attribute() == null)
            return new java.util.ArrayList<android.databinding.parser.XMLParser.AttributeContext>();
        else {
            return elementContext.attribute();
        }
    }

    public static java.util.List<? extends android.databinding.parser.XMLParser.AttributeContext> expressionAttributes(android.databinding.parser.XMLParser.ElementContext elementContext) {
        java.util.List<android.databinding.parser.XMLParser.AttributeContext> result = new java.util.ArrayList<android.databinding.parser.XMLParser.AttributeContext>();
        for (android.databinding.parser.XMLParser.AttributeContext input : android.databinding.tool.util.XmlEditor.attributes(elementContext)) {
            java.lang.String attrName = input.attrName.getText();
            boolean isExpression = attrName.equals("android:tag");
            if (!isExpression) {
                final java.lang.String value = input.attrValue.getText();
                isExpression = android.databinding.tool.util.XmlEditor.isExpressionText(input.attrValue.getText());
            }
            if (isExpression) {
                result.add(input);
            }
        }
        return result;
    }

    private static boolean isExpressionText(java.lang.String value) {
        // Check if the expression ends with "}" and starts with "@{" or "@={", ignoring
        // the surrounding quotes.
        return ((value.length() > 5) && (value.charAt(value.length() - 2) == '}')) && ("@{".equals(value.substring(1, 3)) || "@={".equals(value.substring(1, 4)));
    }

    private static android.databinding.tool.util.XmlEditor.Position endTagPosition(android.databinding.parser.XMLParser.ElementContext context) {
        if (context.content() == null) {
            // no content, so just choose the start of the "/>"
            android.databinding.tool.util.XmlEditor.Position endTag = android.databinding.tool.util.XmlEditor.toPosition(context.getStop());
            if (endTag.charIndex <= 0) {
                android.databinding.tool.util.L.e("invalid input in %s", context);
            }
            return endTag;
        } else {
            // tag with no attributes, but with content
            android.databinding.tool.util.XmlEditor.Position position = android.databinding.tool.util.XmlEditor.toPosition(context.content().getStart());
            if (position.charIndex <= 0) {
                android.databinding.tool.util.L.e("invalid input in %s", context);
            }
            position.charIndex--;
            return position;
        }
    }

    public static java.util.List<? extends android.databinding.parser.XMLParser.ElementContext> elements(android.databinding.parser.XMLParser.ElementContext context) {
        if ((context.content() != null) && (context.content().element() != null)) {
            return context.content().element();
        }
        return new java.util.ArrayList<android.databinding.parser.XMLParser.ElementContext>();
    }

    private static boolean replace(java.util.ArrayList<java.lang.String> lines, android.databinding.tool.util.XmlEditor.Position start, android.databinding.tool.util.XmlEditor.Position end, java.lang.String text) {
        android.databinding.tool.util.XmlEditor.fixPosition(lines, start);
        android.databinding.tool.util.XmlEditor.fixPosition(lines, end);
        if (start.line != end.line) {
            java.lang.String startLine = lines.get(start.line);
            java.lang.String newStartLine = startLine.substring(0, start.charIndex) + text;
            lines.set(start.line, newStartLine);
            for (int i = start.line + 1; i < end.line; i++) {
                java.lang.String line = lines.get(i);
                lines.set(i, android.databinding.tool.util.XmlEditor.replaceWithSpaces(line, 0, line.length() - 1));
            }
            java.lang.String endLine = lines.get(end.line);
            java.lang.String newEndLine = android.databinding.tool.util.XmlEditor.replaceWithSpaces(endLine, 0, end.charIndex - 1);
            lines.set(end.line, newEndLine);
            return true;
        } else
            if ((end.charIndex - start.charIndex) >= text.length()) {
                java.lang.String line = lines.get(start.line);
                int endTextIndex = start.charIndex + text.length();
                java.lang.String replacedText = android.databinding.tool.util.XmlEditor.replaceRange(line, start.charIndex, endTextIndex, text);
                java.lang.String spacedText = android.databinding.tool.util.XmlEditor.replaceWithSpaces(replacedText, endTextIndex, end.charIndex - 1);
                lines.set(start.line, spacedText);
                return true;
            } else {
                java.lang.String line = lines.get(start.line);
                java.lang.String newLine = android.databinding.tool.util.XmlEditor.replaceWithSpaces(line, start.charIndex, end.charIndex - 1);
                lines.set(start.line, newLine);
                return false;
            }

    }

    private static java.lang.String replaceRange(java.lang.String line, int start, int end, java.lang.String newText) {
        return (line.substring(0, start) + newText) + line.substring(end);
    }

    public static boolean hasExpressionAttributes(android.databinding.parser.XMLParser.ElementContext context) {
        java.util.List<? extends android.databinding.parser.XMLParser.AttributeContext> expressions = android.databinding.tool.util.XmlEditor.expressionAttributes(context);
        int size = expressions.size();
        if (size == 0) {
            return false;
        } else
            if (size > 1) {
                return true;
            } else {
                // android:tag is included, regardless, so we must only count as an expression
                // if android:tag has a binding expression.
                return android.databinding.tool.util.XmlEditor.isExpressionText(expressions.get(0).attrValue.getText());
            }

    }

    private static int recurseReplace(android.databinding.parser.XMLParser.ElementContext node, java.util.ArrayList<java.lang.String> lines, java.util.ArrayList<android.databinding.tool.util.XmlEditor.TagAndContext> noTag, java.lang.String newTag, int bindingIndex) {
        int nextBindingIndex = bindingIndex;
        boolean isMerge = "merge".equals(android.databinding.tool.util.XmlEditor.nodeName(node));
        final boolean containsInclude = android.databinding.tool.util.XmlEditor.filterNodesByName("include", android.databinding.tool.util.XmlEditor.elements(node)).size() > 0;
        if ((!isMerge) && ((android.databinding.tool.util.XmlEditor.hasExpressionAttributes(node) || (newTag != null)) || containsInclude)) {
            java.lang.String tag = "";
            if (newTag != null) {
                tag = ((("android:tag=\"" + newTag) + "_") + bindingIndex) + "\"";
                nextBindingIndex++;
            } else
                if (!"include".equals(android.databinding.tool.util.XmlEditor.nodeName(node))) {
                    tag = ("android:tag=\"binding_" + bindingIndex) + "\"";
                    nextBindingIndex++;
                }

            for (android.databinding.parser.XMLParser.AttributeContext it : android.databinding.tool.util.XmlEditor.expressionAttributes(node)) {
                android.databinding.tool.util.XmlEditor.Position start = android.databinding.tool.util.XmlEditor.toPosition(it.getStart());
                android.databinding.tool.util.XmlEditor.Position end = android.databinding.tool.util.XmlEditor.toEndPosition(it.getStop());
                java.lang.String defaultVal = android.databinding.tool.util.XmlEditor.defaultReplacement(it);
                if (defaultVal != null) {
                    android.databinding.tool.util.XmlEditor.replace(lines, start, end, ((it.attrName.getText() + "=\"") + defaultVal) + "\"");
                } else
                    if (android.databinding.tool.util.XmlEditor.replace(lines, start, end, tag)) {
                        tag = "";
                    }

            }
            if (tag.length() != 0) {
                noTag.add(new android.databinding.tool.util.XmlEditor.TagAndContext(tag, node));
            }
        }
        java.lang.String nextTag;
        if ((bindingIndex == 0) && isMerge) {
            nextTag = newTag;
        } else {
            nextTag = null;
        }
        for (android.databinding.parser.XMLParser.ElementContext it : android.databinding.tool.util.XmlEditor.elements(node)) {
            nextBindingIndex = android.databinding.tool.util.XmlEditor.recurseReplace(it, lines, noTag, nextTag, nextBindingIndex);
        }
        return nextBindingIndex;
    }

    private static java.lang.String defaultReplacement(android.databinding.parser.XMLParser.AttributeContext attr) {
        java.lang.String textWithQuotes = attr.attrValue.getText();
        java.lang.String escapedText = textWithQuotes.substring(1, textWithQuotes.length() - 1);
        final boolean isTwoWay = escapedText.startsWith("@={");
        final boolean isOneWay = escapedText.startsWith("@{");
        if (((!isTwoWay) && (!isOneWay)) || (!escapedText.endsWith("}"))) {
            return null;
        }
        final int startIndex = (isTwoWay) ? 3 : 2;
        final int endIndex = escapedText.length() - 1;
        java.lang.String text = android.databinding.tool.util.StringUtils.unescapeXml(escapedText.substring(startIndex, endIndex));
        org.antlr.v4.runtime.ANTLRInputStream inputStream = new org.antlr.v4.runtime.ANTLRInputStream(text);
        android.databinding.parser.BindingExpressionLexer lexer = new android.databinding.parser.BindingExpressionLexer(inputStream);
        org.antlr.v4.runtime.CommonTokenStream tokenStream = new org.antlr.v4.runtime.CommonTokenStream(lexer);
        android.databinding.parser.BindingExpressionParser parser = new android.databinding.parser.BindingExpressionParser(tokenStream);
        android.databinding.parser.BindingExpressionParser.BindingSyntaxContext root = parser.bindingSyntax();
        android.databinding.parser.BindingExpressionParser.DefaultsContext defaults = root.defaults();
        if (defaults != null) {
            android.databinding.parser.BindingExpressionParser.ConstantValueContext constantValue = defaults.constantValue();
            android.databinding.parser.BindingExpressionParser.LiteralContext literal = constantValue.literal();
            if (literal != null) {
                android.databinding.parser.BindingExpressionParser.StringLiteralContext stringLiteral = literal.stringLiteral();
                if (stringLiteral != null) {
                    org.antlr.v4.runtime.tree.TerminalNode doubleQuote = stringLiteral.DoubleQuoteString();
                    if (doubleQuote != null) {
                        java.lang.String quotedStr = doubleQuote.getText();
                        java.lang.String unquoted = quotedStr.substring(1, quotedStr.length() - 1);
                        return com.google.common.xml.XmlEscapers.xmlAttributeEscaper().escape(unquoted);
                    } else {
                        java.lang.String quotedStr = stringLiteral.SingleQuoteString().getText();
                        java.lang.String unquoted = quotedStr.substring(1, quotedStr.length() - 1);
                        java.lang.String unescaped = unquoted.replace("\"", "\\\"").replace("\\`", "`");
                        return com.google.common.xml.XmlEscapers.xmlAttributeEscaper().escape(unescaped);
                    }
                }
            }
            return constantValue.getText();
        }
        return null;
    }

    private static android.databinding.tool.util.XmlEditor.PositionPair findTerminalPositions(android.databinding.parser.XMLParser.ElementContext node, java.util.ArrayList<java.lang.String> lines) {
        android.databinding.tool.util.XmlEditor.Position endPosition = android.databinding.tool.util.XmlEditor.toEndPosition(node.getStop());
        android.databinding.tool.util.XmlEditor.Position startPosition = android.databinding.tool.util.XmlEditor.toPosition(node.getStop());
        int index;
        do {
            index = lines.get(startPosition.line).lastIndexOf("</");
            startPosition.line--;
        } while (index < 0 );
        startPosition.line++;
        startPosition.charIndex = index;
        // noinspection unchecked
        return new android.databinding.tool.util.XmlEditor.PositionPair(startPosition, endPosition);
    }

    private static java.lang.String replaceWithSpaces(java.lang.String line, int start, int end) {
        java.lang.StringBuilder lineBuilder = new java.lang.StringBuilder(line);
        for (int i = start; i <= end; i++) {
            lineBuilder.setCharAt(i, ' ');
        }
        return lineBuilder.toString();
    }

    private static void fixPosition(java.util.ArrayList<java.lang.String> lines, android.databinding.tool.util.XmlEditor.Position pos) {
        java.lang.String line = lines.get(pos.line);
        while (pos.charIndex > line.length()) {
            pos.charIndex--;
        } 
    }

    private static class Position {
        int line;

        int charIndex;

        public Position(int line, int charIndex) {
            this.line = line;
            this.charIndex = charIndex;
        }
    }

    private static class TagAndContext {
        private final java.lang.String mTag;

        private final android.databinding.parser.XMLParser.ElementContext mElementContext;

        private TagAndContext(java.lang.String tag, android.databinding.parser.XMLParser.ElementContext elementContext) {
            mTag = tag;
            mElementContext = elementContext;
        }

        private android.databinding.parser.XMLParser.ElementContext getContext() {
            return mElementContext;
        }

        private java.lang.String getTag() {
            return mTag;
        }
    }

    private static class PositionPair {
        private final android.databinding.tool.util.XmlEditor.Position left;

        private final android.databinding.tool.util.XmlEditor.Position right;

        private PositionPair(android.databinding.tool.util.XmlEditor.Position left, android.databinding.tool.util.XmlEditor.Position right) {
            this.left = left;
            this.right = right;
        }
    }
}

