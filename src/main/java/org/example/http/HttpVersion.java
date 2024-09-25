package org.example.http;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum HttpVersion {
    //using regex

    HTTP_1_1("HTTP/1.1", 1, 1);

    public final String LITERAL;
    public final int MAJOR;
    public final int MINOR;

    HttpVersion(String LITERAL, int MAJOR, int MINOR) {
        this.LITERAL = LITERAL;
        this.MAJOR = MAJOR;
        this.MINOR = MINOR;
    }

    private static final Pattern versionPattern = Pattern.compile("^HTTP/(?<major>\\d+).(?<minor>\\d+)");

    public static HttpVersion getHttp11(String literalVersion) throws BadHttpVersionException {
        Matcher matcher = versionPattern.matcher(literalVersion);

        if(!matcher.find() || matcher.groupCount() != 2) {
            throw new BadHttpVersionException();
        }

        int major = Integer.parseInt(matcher.group("major"));
        int minor = Integer.parseInt(matcher.group("minor"));

        HttpVersion tempCompatible = null;

        for(HttpVersion version : HttpVersion.values()) {
            if(version.LITERAL.equals(literalVersion)) {
                return version;
            } else {
                if(version.MAJOR == major) {
                    if(version.MINOR < minor) {
                        tempCompatible = version;
                    }
                }
            }
        }

        return tempCompatible;
    }

}
