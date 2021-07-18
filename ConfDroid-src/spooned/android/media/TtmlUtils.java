package android.media;


/**
 * A class which provides utillity methods for TTML parsing.
 *
 * @unknown 
 */
final class TtmlUtils {
    public static final java.lang.String TAG_TT = "tt";

    public static final java.lang.String TAG_HEAD = "head";

    public static final java.lang.String TAG_BODY = "body";

    public static final java.lang.String TAG_DIV = "div";

    public static final java.lang.String TAG_P = "p";

    public static final java.lang.String TAG_SPAN = "span";

    public static final java.lang.String TAG_BR = "br";

    public static final java.lang.String TAG_STYLE = "style";

    public static final java.lang.String TAG_STYLING = "styling";

    public static final java.lang.String TAG_LAYOUT = "layout";

    public static final java.lang.String TAG_REGION = "region";

    public static final java.lang.String TAG_METADATA = "metadata";

    public static final java.lang.String TAG_SMPTE_IMAGE = "smpte:image";

    public static final java.lang.String TAG_SMPTE_DATA = "smpte:data";

    public static final java.lang.String TAG_SMPTE_INFORMATION = "smpte:information";

    public static final java.lang.String PCDATA = "#pcdata";

    public static final java.lang.String ATTR_BEGIN = "begin";

    public static final java.lang.String ATTR_DURATION = "dur";

    public static final java.lang.String ATTR_END = "end";

    public static final long INVALID_TIMESTAMP = java.lang.Long.MAX_VALUE;

    /**
     * Time expression RE according to the spec:
     * http://www.w3.org/TR/ttaf1-dfxp/#timing-value-timeExpression
     */
    private static final java.util.regex.Pattern CLOCK_TIME = java.util.regex.Pattern.compile("^([0-9][0-9]+):([0-9][0-9]):([0-9][0-9])" + "(?:(\\.[0-9]+)|:([0-9][0-9])(?:\\.([0-9]+))?)?$");

    private static final java.util.regex.Pattern OFFSET_TIME = java.util.regex.Pattern.compile("^([0-9]+(?:\\.[0-9]+)?)(h|m|s|ms|f|t)$");

    private TtmlUtils() {
    }

    /**
     * Parses the given time expression and returns a timestamp in millisecond.
     * <p>
     * For the format of the time expression, please refer <a href=
     * "http://www.w3.org/TR/ttaf1-dfxp/#timing-value-timeExpression">timeExpression</a>
     *
     * @param time
     * 		A string which includes time expression.
     * @param frameRate
     * 		the framerate of the stream.
     * @param subframeRate
     * 		the sub-framerate of the stream
     * @param tickRate
     * 		the tick rate of the stream.
     * @return the parsed timestamp in micro-second.
     * @throws NumberFormatException
     * 		if the given string does not match to the
     * 		format.
     */
    public static long parseTimeExpression(java.lang.String time, int frameRate, int subframeRate, int tickRate) throws java.lang.NumberFormatException {
        java.util.regex.Matcher matcher = android.media.TtmlUtils.CLOCK_TIME.matcher(time);
        if (matcher.matches()) {
            java.lang.String hours = matcher.group(1);
            double durationSeconds = java.lang.Long.parseLong(hours) * 3600;
            java.lang.String minutes = matcher.group(2);
            durationSeconds += java.lang.Long.parseLong(minutes) * 60;
            java.lang.String seconds = matcher.group(3);
            durationSeconds += java.lang.Long.parseLong(seconds);
            java.lang.String fraction = matcher.group(4);
            durationSeconds += (fraction != null) ? java.lang.Double.parseDouble(fraction) : 0;
            java.lang.String frames = matcher.group(5);
            durationSeconds += (frames != null) ? ((double) (java.lang.Long.parseLong(frames))) / frameRate : 0;
            java.lang.String subframes = matcher.group(6);
            durationSeconds += (subframes != null) ? (((double) (java.lang.Long.parseLong(subframes))) / subframeRate) / frameRate : 0;
            return ((long) (durationSeconds * 1000));
        }
        matcher = android.media.TtmlUtils.OFFSET_TIME.matcher(time);
        if (matcher.matches()) {
            java.lang.String timeValue = matcher.group(1);
            double value = java.lang.Double.parseDouble(timeValue);
            java.lang.String unit = matcher.group(2);
            if (unit.equals("h")) {
                value *= 3600L * 1000000L;
            } else
                if (unit.equals("m")) {
                    value *= 60 * 1000000;
                } else
                    if (unit.equals("s")) {
                        value *= 1000000;
                    } else
                        if (unit.equals("ms")) {
                            value *= 1000;
                        } else
                            if (unit.equals("f")) {
                                value = (value / frameRate) * 1000000;
                            } else
                                if (unit.equals("t")) {
                                    value = (value / tickRate) * 1000000;
                                }





            return ((long) (value));
        }
        throw new java.lang.NumberFormatException("Malformed time expression : " + time);
    }

    /**
     * Applies <a href
     * src="http://www.w3.org/TR/ttaf1-dfxp/#content-attribute-space">the
     * default space policy</a> to the given string.
     *
     * @param in
     * 		A string to apply the policy.
     */
    public static java.lang.String applyDefaultSpacePolicy(java.lang.String in) {
        return android.media.TtmlUtils.applySpacePolicy(in, true);
    }

    /**
     * Applies the space policy to the given string. This applies <a href
     * src="http://www.w3.org/TR/ttaf1-dfxp/#content-attribute-space">the
     * default space policy</a> with linefeed-treatment as treat-as-space
     * or preserve.
     *
     * @param in
     * 		A string to apply the policy.
     * @param treatLfAsSpace
     * 		Whether convert line feeds to spaces or not.
     */
    public static java.lang.String applySpacePolicy(java.lang.String in, boolean treatLfAsSpace) {
        // Removes CR followed by LF. ref:
        // http://www.w3.org/TR/xml/#sec-line-ends
        java.lang.String crRemoved = in.replaceAll("\r\n", "\n");
        // Apply suppress-at-line-break="auto" and
        // white-space-treatment="ignore-if-surrounding-linefeed"
        java.lang.String spacesNeighboringLfRemoved = crRemoved.replaceAll(" *\n *", "\n");
        // Apply linefeed-treatment="treat-as-space"
        java.lang.String lfToSpace = (treatLfAsSpace) ? spacesNeighboringLfRemoved.replaceAll("\n", " ") : spacesNeighboringLfRemoved;
        // Apply white-space-collapse="true"
        java.lang.String spacesCollapsed = lfToSpace.replaceAll("[ \t\\x0B\f\r]+", " ");
        return spacesCollapsed;
    }

    /**
     * Returns the timed text for the given time period.
     *
     * @param root
     * 		The root node of the TTML document.
     * @param startUs
     * 		The start time of the time period in microsecond.
     * @param endUs
     * 		The end time of the time period in microsecond.
     */
    public static java.lang.String extractText(android.media.TtmlNode root, long startUs, long endUs) {
        java.lang.StringBuilder text = new java.lang.StringBuilder();
        android.media.TtmlUtils.extractText(root, startUs, endUs, text, false);
        return text.toString().replaceAll("\n$", "");
    }

    private static void extractText(android.media.TtmlNode node, long startUs, long endUs, java.lang.StringBuilder out, boolean inPTag) {
        if (node.mName.equals(android.media.TtmlUtils.PCDATA) && inPTag) {
            out.append(node.mText);
        } else
            if (node.mName.equals(android.media.TtmlUtils.TAG_BR) && inPTag) {
                out.append("\n");
            } else
                if (node.mName.equals(android.media.TtmlUtils.TAG_METADATA)) {
                    // do nothing.
                } else
                    if (node.isActive(startUs, endUs)) {
                        boolean pTag = node.mName.equals(android.media.TtmlUtils.TAG_P);
                        int length = out.length();
                        for (int i = 0; i < node.mChildren.size(); ++i) {
                            android.media.TtmlUtils.extractText(node.mChildren.get(i), startUs, endUs, out, pTag || inPTag);
                        }
                        if (pTag && (length != out.length())) {
                            out.append("\n");
                        }
                    }



    }

    /**
     * Returns a TTML fragment string for the given time period.
     *
     * @param root
     * 		The root node of the TTML document.
     * @param startUs
     * 		The start time of the time period in microsecond.
     * @param endUs
     * 		The end time of the time period in microsecond.
     */
    public static java.lang.String extractTtmlFragment(android.media.TtmlNode root, long startUs, long endUs) {
        java.lang.StringBuilder fragment = new java.lang.StringBuilder();
        android.media.TtmlUtils.extractTtmlFragment(root, startUs, endUs, fragment);
        return fragment.toString();
    }

    private static void extractTtmlFragment(android.media.TtmlNode node, long startUs, long endUs, java.lang.StringBuilder out) {
        if (node.mName.equals(android.media.TtmlUtils.PCDATA)) {
            out.append(node.mText);
        } else
            if (node.mName.equals(android.media.TtmlUtils.TAG_BR)) {
                out.append("<br/>");
            } else
                if (node.isActive(startUs, endUs)) {
                    out.append("<");
                    out.append(node.mName);
                    out.append(node.mAttributes);
                    out.append(">");
                    for (int i = 0; i < node.mChildren.size(); ++i) {
                        android.media.TtmlUtils.extractTtmlFragment(node.mChildren.get(i), startUs, endUs, out);
                    }
                    out.append("</");
                    out.append(node.mName);
                    out.append(">");
                }


    }
}

