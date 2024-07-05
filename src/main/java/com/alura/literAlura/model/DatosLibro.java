package com.alura.literAlura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosLibro(
       @JsonAlias("title") String titulo,
       @JsonAlias("languages") String idioma,
       @JsonAlias("download_count") Integer numeroDeDescargas
) {
}
