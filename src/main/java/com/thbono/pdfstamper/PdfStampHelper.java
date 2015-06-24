package com.thbono.pdfstamper;

import com.google.common.base.Preconditions;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Include stamp in PDF documents
 *
 * @author Tiago Bono
 * @since 2015-06-24
 */
public class PdfStampHelper {

    private static final String DEFAULT_STAMP_CONTENT = "TOP SECRET";
    private static final int FONT_SIZE = 20;

    private String stampContent;

    private final Font stampFont;

    /**
     * Default constructor
     */
    public PdfStampHelper() {
        stampContent = DEFAULT_STAMP_CONTENT;
        stampFont = new Font(Font.HELVETICA, FONT_SIZE, Font.BOLD, Color.RED);
    }

    /**
     * Set the stamp content
     *
     * @param stampContent Stamp content
     * @throws NullPointerException     If stamp content is null
     * @throws IllegalArgumentException If stamp content is empty
     */
    public void setStampContent(final String stampContent) {
        Preconditions.checkNotNull(stampContent, "Stamp content cannot be null");
        Preconditions.checkArgument(!stampContent.trim().isEmpty(), "Stamp content cannot be empty");

        this.stampContent = stampContent.trim();
    }

    /**
     * @return Stamp content
     */
    public String getStampContent() {
        return stampContent;
    }

    /**
     * Set the stamp color
     *
     * @param stampColor Stamp color
     * @throws NullPointerException If stamp color is null
     */
    public void setStampColor(final Color stampColor) {
        stampFont.setColor(Preconditions.checkNotNull(stampColor, "Stamp color cannot be null"));
    }

    /**
     * @return Stamp color
     */
    public Color getStampColor() {
        return stampFont.getColor();
    }

    /**
     * Include the stamp in all document pages
     *
     * @param documentContentStream Stream to document content
     * @return Stream to document with stamp in all pages
     * @throws NullPointerException     If stream to document content is null
     * @throws IOException
     * @throws DocumentException
     */
    public ByteArrayOutputStream includeStampInAllDocumentPages(final InputStream documentContentStream) throws IOException, DocumentException {
        final PdfReader pdfReader = new PdfReader(Preconditions.checkNotNull(documentContentStream, "Document content stream cannot be null"));
        final int numberOfPages = pdfReader.getNumberOfPages();
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final PdfStamper pdfStamper = new PdfStamper(pdfReader, out);
        final Phrase phrase = new Phrase(stampContent, stampFont);

        for (int i = 1; i <= numberOfPages; i++) {
            final PdfContentByte page = pdfStamper.getOverContent(i);
            final Rectangle drawArea = pdfReader.getPageSize(i);

            ColumnText.showTextAligned(
                    page,
                    Element.ALIGN_RIGHT,
                    phrase,
                    drawArea.getWidth() * 0.99f,
                    drawArea.getHeight() * 0.01f,
                    0);
        }

        pdfStamper.close();
        out.flush();
        out.close();

        return out;
    }

}
