package android.media;


/**
 * A simple TTML parser (http://www.w3.org/TR/ttaf1-dfxp/) which supports DFXP
 * presentation profile.
 * <p>
 * Supported features in this parser are:
 * <ul>
 * <li>content
 * <li>core
 * <li>presentation
 * <li>profile
 * <li>structure
 * <li>time-offset
 * <li>timing
 * <li>tickRate
 * <li>time-clock-with-frames
 * <li>time-clock
 * <li>time-offset-with-frames
 * <li>time-offset-with-ticks
 * </ul>
 * </p>
 *
 * @unknown 
 */
class TtmlParser {
    static final java.lang.String TAG = "TtmlParser";

    // TODO: read and apply the following attributes if specified.
    private static final int DEFAULT_FRAMERATE = 30;

    private static final int DEFAULT_SUBFRAMERATE = 1;

    private static final int DEFAULT_TICKRATE = 1;

    private org.xmlpull.v1.XmlPullParser mParser;

    private final android.media.TtmlNodeListener mListener;

    private long mCurrentRunId;

    public TtmlParser(android.media.TtmlNodeListener listener) {
        mListener = listener;
    }

    /**
     * Parse TTML data. Once this is called, all the previous data are
     * reset and it starts parsing for the given text.
     *
     * @param ttmlText
     * 		TTML text to parse.
     * @throws XmlPullParserException
     * 		
     * @throws IOException
     * 		
     */
    public void parse(java.lang.String ttmlText, long runId) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        mParser = null;
        mCurrentRunId = runId;
        loadParser(ttmlText);
        parseTtml();
    }

    private void loadParser(java.lang.String ttmlFragment) throws org.xmlpull.v1.XmlPullParserException {
        org.xmlpull.v1.XmlPullParserFactory factory = org.xmlpull.v1.XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(false);
        mParser = factory.newPullParser();
        java.io.StringReader in = new java.io.StringReader(ttmlFragment);
        mParser.setInput(in);
    }

    private void extractAttribute(org.xmlpull.v1.XmlPullParser parser, int i, java.lang.StringBuilder out) {
        out.append(" ");
        out.append(parser.getAttributeName(i));
        out.append("=\"");
        out.append(parser.getAttributeValue(i));
        out.append("\"");
    }

    private void parseTtml() throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        java.util.LinkedList<android.media.TtmlNode> nodeStack = new java.util.LinkedList<android.media.TtmlNode>();
        int depthInUnsupportedTag = 0;
        boolean active = true;
        while (!isEndOfDoc()) {
            int eventType = mParser.getEventType();
            android.media.TtmlNode parent = nodeStack.peekLast();
            if (active) {
                if (eventType == org.xmlpull.v1.XmlPullParser.START_TAG) {
                    if (!android.media.TtmlParser.isSupportedTag(mParser.getName())) {
                        android.util.Log.w(android.media.TtmlParser.TAG, ("Unsupported tag " + mParser.getName()) + " is ignored.");
                        depthInUnsupportedTag++;
                        active = false;
                    } else {
                        android.media.TtmlNode node = parseNode(parent);
                        nodeStack.addLast(node);
                        if (parent != null) {
                            parent.mChildren.add(node);
                        }
                    }
                } else
                    if (eventType == org.xmlpull.v1.XmlPullParser.TEXT) {
                        java.lang.String text = android.media.TtmlUtils.applyDefaultSpacePolicy(mParser.getText());
                        if (!android.text.TextUtils.isEmpty(text)) {
                            parent.mChildren.add(new android.media.TtmlNode(android.media.TtmlUtils.PCDATA, "", text, 0, android.media.TtmlUtils.INVALID_TIMESTAMP, parent, mCurrentRunId));
                        }
                    } else
                        if (eventType == org.xmlpull.v1.XmlPullParser.END_TAG) {
                            if (mParser.getName().equals(android.media.TtmlUtils.TAG_P)) {
                                mListener.onTtmlNodeParsed(nodeStack.getLast());
                            } else
                                if (mParser.getName().equals(android.media.TtmlUtils.TAG_TT)) {
                                    mListener.onRootNodeParsed(nodeStack.getLast());
                                }

                            nodeStack.removeLast();
                        }


            } else {
                if (eventType == org.xmlpull.v1.XmlPullParser.START_TAG) {
                    depthInUnsupportedTag++;
                } else
                    if (eventType == org.xmlpull.v1.XmlPullParser.END_TAG) {
                        depthInUnsupportedTag--;
                        if (depthInUnsupportedTag == 0) {
                            active = true;
                        }
                    }

            }
            mParser.next();
        } 
    }

    private android.media.TtmlNode parseNode(android.media.TtmlNode parent) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        int eventType = mParser.getEventType();
        if (!(eventType == org.xmlpull.v1.XmlPullParser.START_TAG)) {
            return null;
        }
        java.lang.StringBuilder attrStr = new java.lang.StringBuilder();
        long start = 0;
        long end = android.media.TtmlUtils.INVALID_TIMESTAMP;
        long dur = 0;
        for (int i = 0; i < mParser.getAttributeCount(); ++i) {
            java.lang.String attr = mParser.getAttributeName(i);
            java.lang.String value = mParser.getAttributeValue(i);
            // TODO: check if it's safe to ignore the namespace of attributes as follows.
            attr = attr.replaceFirst("^.*:", "");
            if (attr.equals(android.media.TtmlUtils.ATTR_BEGIN)) {
                start = android.media.TtmlUtils.parseTimeExpression(value, android.media.TtmlParser.DEFAULT_FRAMERATE, android.media.TtmlParser.DEFAULT_SUBFRAMERATE, android.media.TtmlParser.DEFAULT_TICKRATE);
            } else
                if (attr.equals(android.media.TtmlUtils.ATTR_END)) {
                    end = android.media.TtmlUtils.parseTimeExpression(value, android.media.TtmlParser.DEFAULT_FRAMERATE, android.media.TtmlParser.DEFAULT_SUBFRAMERATE, android.media.TtmlParser.DEFAULT_TICKRATE);
                } else
                    if (attr.equals(android.media.TtmlUtils.ATTR_DURATION)) {
                        dur = android.media.TtmlUtils.parseTimeExpression(value, android.media.TtmlParser.DEFAULT_FRAMERATE, android.media.TtmlParser.DEFAULT_SUBFRAMERATE, android.media.TtmlParser.DEFAULT_TICKRATE);
                    } else {
                        extractAttribute(mParser, i, attrStr);
                    }


        }
        if (parent != null) {
            start += parent.mStartTimeMs;
            if (end != android.media.TtmlUtils.INVALID_TIMESTAMP) {
                end += parent.mStartTimeMs;
            }
        }
        if (dur > 0) {
            if (end != android.media.TtmlUtils.INVALID_TIMESTAMP) {
                android.util.Log.e(android.media.TtmlParser.TAG, "'dur' and 'end' attributes are defined at the same time." + "'end' value is ignored.");
            }
            end = start + dur;
        }
        if (parent != null) {
            // If the end time remains unspecified, then the end point is
            // interpreted as the end point of the external time interval.
            if (((end == android.media.TtmlUtils.INVALID_TIMESTAMP) && (parent.mEndTimeMs != android.media.TtmlUtils.INVALID_TIMESTAMP)) && (end > parent.mEndTimeMs)) {
                end = parent.mEndTimeMs;
            }
        }
        android.media.TtmlNode node = new android.media.TtmlNode(mParser.getName(), attrStr.toString(), null, start, end, parent, mCurrentRunId);
        return node;
    }

    private boolean isEndOfDoc() throws org.xmlpull.v1.XmlPullParserException {
        return mParser.getEventType() == org.xmlpull.v1.XmlPullParser.END_DOCUMENT;
    }

    private static boolean isSupportedTag(java.lang.String tag) {
        if ((((((((((((((tag.equals(android.media.TtmlUtils.TAG_TT) || tag.equals(android.media.TtmlUtils.TAG_HEAD)) || tag.equals(android.media.TtmlUtils.TAG_BODY)) || tag.equals(android.media.TtmlUtils.TAG_DIV)) || tag.equals(android.media.TtmlUtils.TAG_P)) || tag.equals(android.media.TtmlUtils.TAG_SPAN)) || tag.equals(android.media.TtmlUtils.TAG_BR)) || tag.equals(android.media.TtmlUtils.TAG_STYLE)) || tag.equals(android.media.TtmlUtils.TAG_STYLING)) || tag.equals(android.media.TtmlUtils.TAG_LAYOUT)) || tag.equals(android.media.TtmlUtils.TAG_REGION)) || tag.equals(android.media.TtmlUtils.TAG_METADATA)) || tag.equals(android.media.TtmlUtils.TAG_SMPTE_IMAGE)) || tag.equals(android.media.TtmlUtils.TAG_SMPTE_DATA)) || tag.equals(android.media.TtmlUtils.TAG_SMPTE_INFORMATION)) {
            return true;
        }
        return false;
    }
}

