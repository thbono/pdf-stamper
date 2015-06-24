package com.thbono.pdfstamper;

import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import static junit.framework.Assert.assertEquals;

public class PdfStampHelperTest {

    private PdfStampHelper helper;

    @Before
    public void setUp() {
        helper = new PdfStampHelper();
    }

    @Test(expected = NullPointerException.class)
    public void should_not_set_stamp_content_when_stamp_content_is_null() {
        helper.setStampContent(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_not_set_stamp_content_when_stamp_content_is_empty() {
        helper.setStampContent("");
    }

    @Test
    public void should_set_stamp_content() {
        helper.setStampContent("  TEST  ");

        assertEquals("TEST", helper.getStampContent());
    }

    @Test(expected = NullPointerException.class)
    public void should_not_set_stamp_color_when_stamp_color_is_null() {
        helper.setStampColor(null);
    }

    @Test
    public void should_set_stamp_color() {
        helper.setStampColor(Color.GREEN);

        assertEquals(Color.GREEN, helper.getStampColor());
    }

    @Test(expected = NullPointerException.class)
    public void should_not_include_stamp_in_all_document_pages_when_document_content_stream_is_null() throws Exception {
        helper.includeStampInAllDocumentPages(null);
    }

    @Test
    public void should_include_stamp_in_all_document_pages() throws Exception {
        final InputStream pdfFileInputStream = getClass().getResourceAsStream("/javaee7-whitepaper.pdf");

        ByteArrayOutputStream result = helper.includeStampInAllDocumentPages(pdfFileInputStream);

        FileOutputStream out = new FileOutputStream("javaee7-whitepaper-with-stamp.pdf", false);

        result.writeTo(out);

        out.flush();
        out.close();
    }

}
