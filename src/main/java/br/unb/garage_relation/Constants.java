package br.unb.garage_relation;

import org.springframework.http.MediaType;

public class Constants {
    public static final String APPLICATION_YAML = "application/x-yaml";
    public static final String APPLICATION_XML = MediaType.APPLICATION_XML_VALUE;
    public static final String APPLICATION_JSON = MediaType.APPLICATION_JSON_VALUE;
    public static final MediaType MEDIA_TYPE_APPLICATION_YAML = MediaType.valueOf("application/x-yaml");
    public static final int TEST_SERVER_PORT = 8888;
}
