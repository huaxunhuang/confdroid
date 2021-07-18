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
package android.databinding.tool.store;


/**
 * Identifies the range of a code block inside a file or a string.
 * Note that, unlike antlr4 tokens, the line positions start from 0 (to be compatible with Studio).
 * <p>
 * Both start and end line/column indices are inclusive.
 */
@javax.xml.bind.annotation.XmlAccessorType(javax.xml.bind.annotation.XmlAccessType.NONE)
public class Location {
    public static final int NaN = -1;

    @javax.xml.bind.annotation.XmlAttribute(name = "startLine")
    public int startLine;

    @javax.xml.bind.annotation.XmlAttribute(name = "startOffset")
    public int startOffset;

    @javax.xml.bind.annotation.XmlAttribute(name = "endLine")
    public int endLine;

    @javax.xml.bind.annotation.XmlAttribute(name = "endOffset")
    public int endOffset;

    @javax.xml.bind.annotation.XmlElement(name = "parentLocation")
    public android.databinding.tool.store.Location parentLocation;

    // for XML unmarshalling
    public Location() {
        startOffset = endOffset = startLine = endLine = android.databinding.tool.store.Location.NaN;
    }

    public Location(android.databinding.tool.store.Location other) {
        startOffset = other.startOffset;
        endOffset = other.endOffset;
        startLine = other.startLine;
        endLine = other.endLine;
    }

    public Location(org.antlr.v4.runtime.Token start, org.antlr.v4.runtime.Token end) {
        if (start == null) {
            startLine = startOffset = android.databinding.tool.store.Location.NaN;
        } else {
            startLine = start.getLine() - 1;// token lines start from 1

            startOffset = start.getCharPositionInLine();
        }
        if (end == null) {
            endLine = endOffset = android.databinding.tool.store.Location.NaN;
        } else {
            endLine = end.getLine() - 1;// token lines start from 1

            java.lang.String endText = end.getText();
            int lastLineStart = endText.lastIndexOf(android.databinding.tool.util.StringUtils.LINE_SEPARATOR);
            java.lang.String lastLine = (lastLineStart < 0) ? endText : endText.substring(lastLineStart + 1);
            endOffset = (end.getCharPositionInLine() + lastLine.length()) - 1;// end is inclusive

        }
    }

    public Location(org.antlr.v4.runtime.ParserRuleContext context) {
        this(context == null ? null : context.getStart(), context == null ? null : context.getStop());
    }

    public Location(int startLine, int startOffset, int endLine, int endOffset) {
        this.startOffset = startOffset;
        this.startLine = startLine;
        this.endLine = endLine;
        this.endOffset = endOffset;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return (((((((((("Location{" + "startLine=") + startLine) + ", startOffset=") + startOffset) + ", endLine=") + endLine) + ", endOffset=") + endOffset) + ", parentLocation=") + parentLocation) + '}';
    }

    public void setParentLocation(android.databinding.tool.store.Location parentLocation) {
        this.parentLocation = parentLocation;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if ((o == null) || (getClass() != o.getClass())) {
            return false;
        }
        android.databinding.tool.store.Location location = ((android.databinding.tool.store.Location) (o));
        if (endLine != location.endLine) {
            return false;
        }
        if (endOffset != location.endOffset) {
            return false;
        }
        if (startLine != location.startLine) {
            return false;
        }
        if (startOffset != location.startOffset) {
            return false;
        }
        return !(parentLocation != null ? !parentLocation.equals(location.parentLocation) : location.parentLocation != null);
    }

    @java.lang.Override
    public int hashCode() {
        int result = startLine;
        result = (31 * result) + startOffset;
        result = (31 * result) + endLine;
        result = (31 * result) + endOffset;
        return result;
    }

    public boolean isValid() {
        return (((startLine != android.databinding.tool.store.Location.NaN) && (endLine != android.databinding.tool.store.Location.NaN)) && (startOffset != android.databinding.tool.store.Location.NaN)) && (endOffset != android.databinding.tool.store.Location.NaN);
    }

    public boolean contains(android.databinding.tool.store.Location other) {
        if (startLine > other.startLine) {
            return false;
        }
        if ((startLine == other.startLine) && (startOffset > other.startOffset)) {
            return false;
        }
        if (endLine < other.endLine) {
            return false;
        }
        return !((endLine == other.endLine) && (endOffset < other.endOffset));
    }

    private android.databinding.tool.store.Location getValidParentAbsoluteLocation() {
        if (parentLocation == null) {
            return null;
        }
        if (parentLocation.isValid()) {
            return parentLocation.toAbsoluteLocation();
        }
        return parentLocation.getValidParentAbsoluteLocation();
    }

    public android.databinding.tool.store.Location toAbsoluteLocation() {
        android.databinding.tool.store.Location absoluteParent = getValidParentAbsoluteLocation();
        if (absoluteParent == null) {
            return this;
        }
        android.databinding.tool.store.Location copy = new android.databinding.tool.store.Location(this);
        boolean sameLine = copy.startLine == copy.endLine;
        if (copy.startLine == 0) {
            copy.startOffset += absoluteParent.startOffset;
        }
        if (sameLine) {
            copy.endOffset += absoluteParent.startOffset;
        }
        copy.startLine += absoluteParent.startLine;
        copy.endLine += absoluteParent.startLine;
        return copy;
    }

    public java.lang.String toUserReadableString() {
        return (((((startLine + ":") + startOffset) + " - ") + endLine) + ":") + endOffset;
    }

    public static android.databinding.tool.store.Location fromUserReadableString(java.lang.String str) {
        int glue = str.indexOf('-');
        if (glue == (-1)) {
            return new android.databinding.tool.store.Location();
        }
        java.lang.String start = str.substring(0, glue);
        java.lang.String end = str.substring(glue + 1);
        int[] point = new int[]{ -1, -1 };
        android.databinding.tool.store.Location location = new android.databinding.tool.store.Location();
        android.databinding.tool.store.Location.parsePoint(start, point);
        location.startLine = point[0];
        location.startOffset = point[1];
        point[0] = point[1] = -1;
        android.databinding.tool.store.Location.parsePoint(end, point);
        location.endLine = point[0];
        location.endOffset = point[1];
        return location;
    }

    private static boolean parsePoint(java.lang.String content, int[] into) {
        int index = content.indexOf(':');
        if (index == (-1)) {
            return false;
        }
        into[0] = java.lang.Integer.parseInt(content.substring(0, index).trim());
        into[1] = java.lang.Integer.parseInt(content.substring(index + 1).trim());
        return true;
    }

    public android.databinding.tool.processing.scopes.LocationScopeProvider createScope() {
        return new android.databinding.tool.processing.scopes.LocationScopeProvider() {
            @java.lang.Override
            public java.util.List<android.databinding.tool.store.Location> provideScopeLocation() {
                return java.util.Collections.singletonList(android.databinding.tool.store.Location.this);
            }
        };
    }
}

