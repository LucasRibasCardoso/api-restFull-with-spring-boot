package com.LucasRibasCardoso.api_rest_with_spring_boot.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;

/**
 * Classe responsável por criar um módulo Jackson que remove espaços em branco no início e no final
 * de strings
 */
public class StringTrimModule extends SimpleModule {

  public StringTrimModule() {
    super("StringTrimModule");
    addDeserializer(
        String.class,
        new StdDeserializer<String>(String.class) {
          @Override
          public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String value = p.getValueAsString();
            return value != null ? value.trim() : null;
          }
        });
  }
}
