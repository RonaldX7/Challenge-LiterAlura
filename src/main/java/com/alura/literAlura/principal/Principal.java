package com.alura.literAlura.principal;

import com.alura.literAlura.model.*;
import com.alura.literAlura.repository.AutorRepository;
import com.alura.literAlura.repository.LibroRepository;
import com.alura.literAlura.service.ConsumoAPI;
import com.alura.literAlura.service.ConvierteDatos;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private final String URL_BASE = "https://gutendex.com/books/?search=";
    private ConvierteDatos conversor = new ConvierteDatos();
    private List<Libro> libros;
    private List<Autor> autores;
    private AutorRepository Arepositorio;
    private LibroRepository Lrepositorio;

    public Principal(LibroRepository lRepository, AutorRepository aRepository){
        this.Lrepositorio = lRepository;
        this.Arepositorio = aRepository;
    }

    public void muestraElMenu(){
        var opcion = -1;
        while(opcion != 0){
            var menu = """
                    \n********* BIENVENIDO A LITERALURA *********
                    1 - Buscar libros por titulo
                    2 - Listar libros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos en un determinado año
                    5 - listar libros por idioma
                    
                    0 - Salir
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch(opcion){
                case 1:
                    buscarLibro();
                    break;
                case 2:
                    listarLibros();
                    break;
                case 3:
                    listarAutores();
                    break;
                case 4:
                    listarAutoresVivos();
                    break;
                case 5:
                    listarLibrosPorIdioma();
                    break;
                case 0:
                    System.out.println("Cerrando el programa...");
                    break;
                default:
                    System.out.println("Opción no valida.");
            }
        }
    }

    private void buscarLibro() {
        System.out.println("Ingrese el titulo del libro: ");
        var nombreLibro = teclado.nextLine();
        var json = consumoApi.obtenerDatos(URL_BASE + nombreLibro.replace(" ", "+"));
        List<DatosLibro> datos = conversor.obtenerDatos(json, Datos.class).resultado();
        Optional<DatosLibro> libroBuscado = datos.stream()
                .filter(l -> l.titulo().toLowerCase().contains(nombreLibro.toLowerCase()))
                .findFirst();

        if (libroBuscado.isPresent()) {
            var libroEncontrado = libroBuscado.get();

            Libro libroExistente = Lrepositorio.findByTituloContainsIgnoreCase(libroEncontrado.titulo());

            if (libroExistente != null){
                System.out.println("El libro ya esta registrado");
            }else {
                var libro = new Libro(libroEncontrado);
                Lrepositorio.save(libro);
                System.out.println(libro);
                System.out.println("\nLibro registrado correctamente");
            }
        } else {
            System.out.println("Libro no encontrado");
        }
    }

    private void listarLibros() {
        libros = Lrepositorio.findAll();
        libros.forEach(System.out::println);
    }

    private void listarAutores() {
        autores = Arepositorio.findAll();
        autores.forEach(System.out::println);
    }

    private void listarAutoresVivos() {
        System.out.println("Ingrese el año que desea:");
        var fechaAutor = teclado.nextLine();

        List<Autor> autores = Arepositorio.autoresPorAnio(Integer.valueOf(fechaAutor));

        if (autores.isEmpty()){
            System.out.println("\nAutores no encontrados");
        }else{
            autores.forEach(System.out::println);
        }

    }

    private void listarLibrosPorIdioma() {
        System.out.println("Ingrese el idioma que desea buscar:");
        var idioma = teclado.nextLine();
        var LibroIdioma = Idioma.fromEspanol(idioma);

        List<Libro> librosPorIdioma = Lrepositorio.findByIdioma(LibroIdioma);

        if (librosPorIdioma.isEmpty()){
            System.out.println("\nLibros no encontrados en el idioma: " + idioma);
        }else {
            System.out.println("\nLos libros encontrados en el idioma " + idioma + " son:");
            librosPorIdioma.forEach(System.out::println);
        }
    }

}
