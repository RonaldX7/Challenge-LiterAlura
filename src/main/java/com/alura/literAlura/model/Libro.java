package com.alura.literAlura.model;

import jakarta.persistence.*;

import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String titulo;

    @Enumerated(EnumType.STRING)
    private Idioma idioma;

    private Integer numeroDeDescargas;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Autor autor;

    public Libro(){}

    public Libro(DatosLibro datosLibro){
        this.titulo = datosLibro.titulo();
        this.idioma = Idioma.fromString(datosLibro.idioma().toString().split(",")[0].trim());
        Optional<DatosAutor> autor = datosLibro.autor().stream()
                .findFirst();
        if (autor.isPresent()) {
            this.autor = new Autor(autor.get());
        } else {
            System.out.println("No se a encontrado el autor");
        }
        try{
            this.numeroDeDescargas = datosLibro.numeroDeDescargas();
        }catch(NumberFormatException e){
            this.numeroDeDescargas = 0;
        }


    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Idioma getIdioma() {
        return idioma;
    }

    public void setIdioma(Idioma idioma) {
        this.idioma = idioma;
    }

    public Integer getNumeroDeDescargas() {
        return numeroDeDescargas;
    }

    public void setNumeroDeDescargas(Integer numeroDeDescargas) {
        this.numeroDeDescargas = numeroDeDescargas;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    @Override
    public String toString() {
        return "\n************* Libro " + id + " *************\n"+
                "Titulo = " + titulo + '\n' +
                "Autor = " + autor.getNombre() + '\n' +
                "Idioma = " + idioma + '\n' +
                "NumeroDeDescargas = " + numeroDeDescargas;
    }
}
