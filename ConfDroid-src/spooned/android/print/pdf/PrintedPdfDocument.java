/**
 * Copyright (C) 2013 The Android Open Source Project
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
package android.print.pdf;


/**
 * This class is a helper for creating a PDF file for given print attributes. It is useful for
 * implementing printing via the native Android graphics APIs.
 * <p>
 * This class computes the page width, page height, and content rectangle from the provided print
 * attributes and these precomputed values can be accessed via {@link #getPageWidth()},
 * {@link #getPageHeight()}, and {@link #getPageContentRect()}, respectively. The
 * {@link #startPage(int)} methods creates pages whose
 * {@link android.graphics.pdf.PdfDocument.PageInfo PageInfo} is initialized with the precomputed
 * values for width, height, and content rectangle.
 * <p>
 * A typical use of the APIs looks like this:
 * </p>
 * <pre>
 * // open a new document
 * PrintedPdfDocument document = new PrintedPdfDocument(context,
 *         printAttributes);
 *
 * // start a page
 * Page page = document.startPage(0);
 *
 * // draw something on the page
 * View content = getContentView();
 * content.draw(page.getCanvas());
 *
 * // finish the page
 * document.finishPage(page);
 * . . .
 * // add more pages
 * . . .
 * // write the document content
 * document.writeTo(getOutputStream());
 *
 * //close the document
 * document.close();
 * </pre>
 */
public class PrintedPdfDocument extends android.graphics.pdf.PdfDocument {
    private static final int MILS_PER_INCH = 1000;

    private static final int POINTS_IN_INCH = 72;

    private final int mPageWidth;

    private final int mPageHeight;

    private final android.graphics.Rect mContentRect;

    /**
     * Creates a new document.
     * <p>
     * <strong>Note:</strong> You must close the document after you are
     * done by calling {@link #close()}.
     * </p>
     *
     * @param context
     * 		Context instance for accessing resources.
     * @param attributes
     * 		The print attributes.
     */
    public PrintedPdfDocument(@android.annotation.NonNull
    android.content.Context context, @android.annotation.NonNull
    android.print.PrintAttributes attributes) {
        android.print.PrintAttributes.MediaSize mediaSize = attributes.getMediaSize();
        // Compute the size of the target canvas from the attributes.
        mPageWidth = ((int) ((((float) (mediaSize.getWidthMils())) / android.print.pdf.PrintedPdfDocument.MILS_PER_INCH) * android.print.pdf.PrintedPdfDocument.POINTS_IN_INCH));
        mPageHeight = ((int) ((((float) (mediaSize.getHeightMils())) / android.print.pdf.PrintedPdfDocument.MILS_PER_INCH) * android.print.pdf.PrintedPdfDocument.POINTS_IN_INCH));
        // Compute the content size from the attributes.
        android.print.PrintAttributes.Margins minMargins = attributes.getMinMargins();
        final int marginLeft = ((int) ((((float) (minMargins.getLeftMils())) / android.print.pdf.PrintedPdfDocument.MILS_PER_INCH) * android.print.pdf.PrintedPdfDocument.POINTS_IN_INCH));
        final int marginTop = ((int) ((((float) (minMargins.getTopMils())) / android.print.pdf.PrintedPdfDocument.MILS_PER_INCH) * android.print.pdf.PrintedPdfDocument.POINTS_IN_INCH));
        final int marginRight = ((int) ((((float) (minMargins.getRightMils())) / android.print.pdf.PrintedPdfDocument.MILS_PER_INCH) * android.print.pdf.PrintedPdfDocument.POINTS_IN_INCH));
        final int marginBottom = ((int) ((((float) (minMargins.getBottomMils())) / android.print.pdf.PrintedPdfDocument.MILS_PER_INCH) * android.print.pdf.PrintedPdfDocument.POINTS_IN_INCH));
        mContentRect = new android.graphics.Rect(marginLeft, marginTop, mPageWidth - marginRight, mPageHeight - marginBottom);
    }

    /**
     * Starts a new page. The page is created using width, height and content rectangle computed
     * from the print attributes passed in the constructor and the given page number to create an
     * appropriate {@link android.graphics.pdf.PdfDocument.PageInfo PageInfo}.
     * <p>
     * After the page is created you can draw arbitrary content on the page's canvas which you can
     * get by calling {@link android.graphics.pdf.PdfDocument.Page#getCanvas() Page.getCanvas()}.
     * After you are done drawing the content you should finish the page by calling
     * {@link #finishPage(Page)}. After the page is finished you should no longer access the page or
     * its canvas.
     * </p>
     * <p>
     * <strong>Note:</strong> Do not call this method after {@link #close()}. Also do not call this
     * method if the last page returned by this method is not finished by calling
     * {@link #finishPage(Page)}.
     * </p>
     *
     * @param pageNumber
     * 		The page number. Must be a non negative.
     * @return A blank page.
     * @see #finishPage(Page)
     */
    @android.annotation.NonNull
    public android.graphics.pdf.PdfDocument.Page startPage(@android.annotation.IntRange(from = 0)
    int pageNumber) {
        android.graphics.pdf.PdfDocument.PageInfo pageInfo = new android.graphics.pdf.PdfDocument.PageInfo.Builder(mPageWidth, mPageHeight, pageNumber).setContentRect(mContentRect).create();
        return startPage(pageInfo);
    }

    /**
     * Gets the page width.
     *
     * @return The page width in PostScript points (1/72th of an inch).
     */
    @android.annotation.IntRange(from = 0)
    public int getPageWidth() {
        return mPageWidth;
    }

    /**
     * Gets the page height.
     *
     * @return The page height in PostScript points (1/72th of an inch).
     */
    @android.annotation.IntRange(from = 0)
    public int getPageHeight() {
        return mPageHeight;
    }

    /**
     * Gets the content rectangle. This is the area of the page that
     * contains printed data and is relative to the page top left.
     *
     * @return The content rectangle.
     */
    @android.annotation.NonNull
    public android.graphics.Rect getPageContentRect() {
        return mContentRect;
    }
}

