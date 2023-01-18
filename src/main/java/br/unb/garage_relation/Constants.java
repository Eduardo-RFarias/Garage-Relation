package br.unb.garage_relation;

import org.springframework.http.MediaType;

public class Constants {
    public static final String APPLICATION_YAML = "application/x-yaml";
    public static final String APPLICATION_XML = MediaType.APPLICATION_XML_VALUE;
    public static final String APPLICATION_JSON = MediaType.APPLICATION_JSON_VALUE;
    public static final MediaType MEDIA_TYPE_APPLICATION_YAML = MediaType.valueOf("application/x-yaml");
    public static final int TEST_SERVER_PORT = 8888;
    public static final String ACCESS_TOKEN_COOKIE_NAME = "garage_relation_access_token";
    public static final String REFRESH_TOKEN_COOKIE_NAME = "garage_relation_refresh_token";
    public static final String AUTHORIZATION_HEADER = "Authorization";
}
