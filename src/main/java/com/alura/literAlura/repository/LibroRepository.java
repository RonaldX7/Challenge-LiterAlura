package com.alura.literAlura.repository;

import com.alura.literAlura.model.Idioma;
import com.alura.literAlura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LibroRepository extends JpaRepository<Libro, Long> {

    Libro findByTituloContainsIgnoreCase(String titulo);

    List<Libro> findByIdioma(Idioma idiomaLibro);
}
