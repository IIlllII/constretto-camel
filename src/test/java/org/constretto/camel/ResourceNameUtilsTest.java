package org.constretto.camel;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author sondre
 */
public class ResourceNameUtilsTest {

    @Test
    public void testExtractFileNameFromUriNull() throws Exception {
       assertNull(ResourceNameUtils.extractFileNameFromUri(null));
    }

    @Test
    public void testExtractFileNameFromUriEmpty() throws Exception {
       assertEquals("", ResourceNameUtils.extractFileNameFromUri(""));
    }

    @Test
    public void testExtractFileNameFromUriWithoutSlash() throws Exception {
       assertEquals("test", ResourceNameUtils.extractFileNameFromUri("classpath:test.txt"));
    }

    @Test
    public void testExtractFileNameFromUriWithSlash() throws Exception {
       assertEquals("test", ResourceNameUtils.extractFileNameFromUri("classpath:myfolder/test.txt"));
    }

}
