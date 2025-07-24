package com.LucasRibasCardoso.api_rest_with_spring_boot.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Classe de configuração do Jackson para personalizar a serialização e deserialização de datas e a
 * aplicar a função trim() aos campos string.
 */
@Configuration
public class JacksonConfig {

  private static final String DATE_FORMAT = "dd/MM/yyyy";

  @Bean
  public Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
    JavaTimeModule javaTimeModule = new JavaTimeModule();
    javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(formatter));
    javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(formatter));

    return new Jackson2ObjectMapperBuilder()
        .modules(javaTimeModule, new StringTrimModule())
        .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        .simpleDateFormat(DATE_FORMAT);
  }

  @Bean
  public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
    return builder.build();
  }
}
