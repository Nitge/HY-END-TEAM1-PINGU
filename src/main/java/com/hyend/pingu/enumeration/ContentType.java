package com.hyend.pingu.enumeration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public enum ContentType {
    JPEG("image/jpeg", "jpg", "jpeg"),
    PNG("image/png", "png"),
    GIF("image/gif", "gif"),
    BMP("image/bmp", "bmp"),
    WEBP("image/webp", "webp"),
    SVG("image/svg+xml", "svg", "svgz"),
    TIFF("image/tiff", "tif", "tiff"),
    ICO("image/x-icon", "ico"),
    HEIC("image/heic", "heic"),
    HEIF("image/heif", "heif"),
    AVIF("image/avif", "avif");

    private final String mimeType;
    private final String[] extensions;

    ContentType(String mimeType, String... extensions) {
        this.mimeType = mimeType;
        this.extensions = extensions;
    }

    private static final Map<String, ContentType> EXT_MAP = new HashMap<>();

    static {
        for (ContentType ct : values()) {
            for (String ext : ct.extensions) {
                EXT_MAP.put(ext.toLowerCase(), ct);
            }
        }
    }

    public static ContentType fromExtension(String ext) {
        if (ext == null) return null;
        return EXT_MAP.get(ext.toLowerCase());
    }

    @Override
    public String toString() {
        return this.mimeType;
    }
}
