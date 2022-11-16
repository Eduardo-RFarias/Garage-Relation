package br.unb.garage_relation.serialization.converter;

import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;

import static br.unb.garage_relation.Constants.APPLICATION_YAML;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

public class YamlJackson2HttpMessageConverter extends AbstractJackson2HttpMessageConverter {
    public YamlJackson2HttpMessageConverter() {
        super(
                new YAMLMapper().setSerializationInclusion(NON_NULL),
                MediaType.parseMediaType(APPLICATION_YAML)
        );
    }
}
