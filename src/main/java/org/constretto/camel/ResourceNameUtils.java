package org.constretto.camel;

import org.apache.commons.lang3.StringUtils;

/**
 * @author sondre
 */
public class ResourceNameUtils {

    public static String extractFileNameFromUri(final String uri) {
        if(uri == null) {
            return null;
        }
        final String cleanUri = (uri.contains("/") ? StringUtils.substringAfterLast(uri, "/") : StringUtils.substringAfter(uri, ":"));
        return StringUtils.substringBeforeLast(cleanUri, ".");
    }
}
