package Biblioteca;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Principal {

	// Declaración de objetos y rutas de archivos
	private static Scanner sc = new Scanner(System.in);
	private static GestorFicheros gestorFicheros = new GestorFicheros();
	private final static String RUTA_LIBROS = gestorFicheros.CARPETA_PRINCIPAL + "libros.bin";
	private final static String RUTA_AUTORES = gestorFicheros.CARPETA_PRINCIPAL + "autores.bin";
	private final static String RUTA_PRESTAMOS = gestorFicheros.CARPETA_PRINCIPAL + "prestamos.txt";
	private final static String RUTA_XML_LIBROS = gestorFicheros.CARPETA_PRINCIPAL + "libros.xml";
	private final static String RUTA_XML_AUTORES = gestorFicheros.CARPETA_PRINCIPAL + "autores.xml";

	public static void main(String[] args) {
		boolean salir = false;
		gestorFicheros.crearCarpetaFicheros();

		while (!salir) {

			// Crear copia de seguridad al inicio de cada iteración
			gestorFicheros.crearCopiaSeguridad(RUTA_LIBROS, RUTA_AUTORES);

			mostrarMenu();
			int opcion = sc.nextInt();

			switch (opcion) {
			case 1:
				gestionarLibros();
				break;
			case 2:
				gestionarAutores();
				break;
			case 3:
				gestionarPrestamos();
				break;
			case 4:
				gestionarExportImportXML();
				break;
			case 5:
				gestorFicheros.copiaSeguridad();
				break;
			case 6:
				System.out.println("Saliendo de programa ...");
				salir = true;
				break;
			default:
				System.out.println("Opción no válida. Por favor, intente de nuevo.");
			}
		}
	}

	private static void mostrarMenu() {
		System.out.println("Bienvenido al Sistema de Gestión de Biblioteca");
		System.out.println("1. Gestionar Libros");
		System.out.println("2. Gestionar Autores");
		System.out.println("3. Gestionar Préstamos");
		System.out.println("4. Exportar/Importar Datos (XML)");
		System.out.println("5. Ver Copia Seguridad");
		System.out.println("6. Salir");
		System.out.print("Seleccione una opción: ");
	}

	// Método para gestionar la funcionalidad de libros
	private static void gestionarLibros() {
		List<Libro> listaLibros = new ArrayList<>();
		boolean salir = false;
		int anio;

		while (!salir) {
			System.out.println("Bienvenido al Sistema de Gestión de Libros");
			System.out.println("1. Añadir Libro");
			System.out.println("2. Ver Libros");
			System.out.println("3. Modificar Libro");
			System.out.println("4. Eliminar Libro");
			System.out.println("5. Salir");
			System.out.print("Seleccione una opción: ");
			int opcion = sc.nextInt();

			switch (opcion) {
			case 1:

				// Pedimos al usuario los datos del libro
				System.out.printf("Se deben añadir libros cuyo año de publicación esté entre (%d-%d) %n", gestorFicheros.MIN_ANIO_PUBLICACION, gestorFicheros.MAX_ANIO_PUBLICACION);
				System.out.println("Título:");
				sc.nextLine();
				String titulo = sc.nextLine();

				System.out.println("Autor:");
				String autor = sc.nextLine();
				
				// Validamos al entrada del año de publicacion del usuario
				do {
					System.out.printf("Año Publicación (%d-%d): %n",  gestorFicheros.MIN_ANIO_PUBLICACION,  gestorFicheros.MAX_ANIO_PUBLICACION);
					anio = sc.nextInt();
				} while (!gestorFicheros.validarAnioPublicacion(anio));

				sc.nextLine();

				System.out.println("Genero:");
				String genero = sc.nextLine();

				// Creamos el libro
				Libro libro = new Libro(titulo, autor, anio, genero);

				// Leemos la lista actual de libros del archivo
				listaLibros = gestorFicheros.leerLibrosBinario(RUTA_LIBROS);

				// Añadimos el nuevo libro a la lista
				listaLibros.add(libro);

				// Guardamos la lista actualizada en el fichero
				gestorFicheros.escribirLibrosBinario(listaLibros, RUTA_LIBROS);

				break;

			case 2:

				// Leemos la lista de libros del archivo
				listaLibros = gestorFicheros.leerLibrosBinario(RUTA_LIBROS);

				// Mostramos la lista de libros
				for (Libro libroMostrar : listaLibros) {
					System.out.println("-------------------------------------");
					System.out.println("Id: " + libroMostrar.getIdLibro());
					System.out.println("Título: " + libroMostrar.getTitulo());
					System.out.println("Autor: " + libroMostrar.getAutor());
					System.out.println("Año: " + libroMostrar.getAnioPublicacion());
					System.out.println("Género: " + libroMostrar.getGenero());
					System.out.println("Estado: " + (libroMostrar.isEstado() ? "Prestado" : "No Prestado"));
					System.out.println("-------------------------------------");
				}

				break;

			case 3:

				// Leemos la lista de libros del archivo
				listaLibros = gestorFicheros.leerLibrosBinario(RUTA_LIBROS);

				// Pedimos al usuario la id a modificar
				System.out.println("Id a modificar: ");
				int idLibroModificado = sc.nextInt();
				
				// Modificamos el libro con la id introducida
				gestorFicheros.modificarLibroBinario(RUTA_LIBROS, idLibroModificado);

				break;

			case 4:

				// Leemos la lista de libros del archivo
				listaLibros = gestorFicheros.leerLibrosBinario(RUTA_LIBROS);

				System.out.println("Id a eliminar: ");
				int idLibroElminado = sc.nextInt();

				// Eliminamos el libro con la id introducida
				gestorFicheros.eliminarLibroBinario(RUTA_LIBROS, idLibroElminado);

				break;

			case 5:
				System.out.println("Saliendo de Gestion de Libros ...");
				salir = true;
				break;

			default:
				System.out.println("Opción no válida. Por favor, intente de nuevo.");
				break;
			}
		}
	}

	// Método para gestionar la funcionalidad de autores
	private static void gestionarAutores() {
		List<Autor> listaAutores = new ArrayList<>();
		boolean salir = false;

		while (!salir) {
			System.out.println("Bienvenido al Sistema de Gestión de Autores");
			System.out.println("1. Añadir Autor");
			System.out.println("2. Ver Autores");
			System.out.println("3. Modificar Autor");
			System.out.println("4. Eliminar Autor");
			System.out.println("5. Salir");
			System.out.print("Seleccione una opción: ");
			int opcion = sc.nextInt();
			switch (opcion) {
			case 1:

				// Pedimos al usuario los datos del autor
				System.out.printf("Se deben añadir autores cuyo año de nacimiento sea (%d-%d) %n",  gestorFicheros.MIN_ANIO_NACIMIENTO,  gestorFicheros.MAX_ANIO_NACIMIENTO);
				System.out.println("Nombre:");
				sc.nextLine();
				String nombre = sc.nextLine();

				System.out.println("Nacionalidad:");
				String nacionalidad = sc.nextLine();

				int anio;
				do {
					System.out.printf("Año Nacimiento (%d-%d): %n",  gestorFicheros.MIN_ANIO_NACIMIENTO,  gestorFicheros.MAX_ANIO_NACIMIENTO);
					anio = sc.nextInt();
				} while (!gestorFicheros.validarAnioNacimiento(anio));

				// Creamos el autor
				Autor autor = new Autor(nombre, nacionalidad, anio);

				// Leemos la lista actual de libros del archivo
				listaAutores = gestorFicheros.leerAutoresBinario(RUTA_AUTORES);

				// Añadimos el nuevo autor a la lista
				listaAutores.add(autor);

				// Guardamos la lista actualizada en el archivo
				gestorFicheros.escribirAutoresBinario(listaAutores, RUTA_AUTORES);

				break;

			case 2:

				// Leemos la lista de autores del archivo
				listaAutores = gestorFicheros.leerAutoresBinario(RUTA_AUTORES);

				// Mostramos la lista de autores
				for (Autor autorMostrar : listaAutores) {
					System.out.println("-------------------------------------");
					System.out.println("Id: " + autorMostrar.getIdAutor());
					System.out.println("Nombre: " + autorMostrar.getNombre());
					System.out.println("Nacionalidad: " + autorMostrar.getNacionalidad());
					System.out.println("Año nacimiento: " + autorMostrar.getAnioNacimiento());
					System.out.println("-------------------------------------");
				}

				break;

			case 3:

				// Leemos la lista de autores del fichero
				listaAutores = gestorFicheros.leerAutoresBinario(RUTA_AUTORES);

				System.out.println("Id a modificar: ");
				int idAutorModificado = sc.nextInt();

				// Modificamos el autor en el fichero
				gestorFicheros.modificarAutorBinario(RUTA_AUTORES, idAutorModificado);

				break;

			case 4:

				// Leemos la lista de autores del archivo
				listaAutores = gestorFicheros.leerAutoresBinario(RUTA_AUTORES);

				System.out.println("Id a eliminar: ");
				int idAutorElminado = sc.nextInt();

				// Eliminamos el autor con la id introducida
				gestorFicheros.eliminarAutorBinario(RUTA_AUTORES, idAutorElminado);

				break;

			case 5:
				System.out.println("Saliendo de Gestion de Autores ...");
				salir = true;
				break;

			default:
				System.out.println("Opción no válida. Por favor, intente de nuevo.");
				break;
			}
		}

	}

	// Método para gestionar la funcionalidad de préstamos
	private static void gestionarPrestamos() {
		boolean salir = false;

		while (!salir) {
			System.out.println("Bienvenido al Sistema de Gestión de Prestamos");
			System.out.println("1. Añadir Prestamo");
			System.out.println("2. Ver Prestamos");
			System.out.println("3. Salir");
			System.out.print("Seleccione una opción: ");
			int opcion = sc.nextInt();
			switch (opcion) {
			case 1:

				// Añadimos los datos de los ficheros a cada lista correspondiente
				List<Libro> listaLibros = gestorFicheros.leerLibrosBinario(RUTA_LIBROS);
				ArrayList<String> listaPrestamos = gestorFicheros.leerPrestamos(RUTA_PRESTAMOS);

				// Mostramos los libros del fichero para que los vea el usuario
				if (listaLibros.isEmpty()) {
					System.out.println("Para añadir un prestamo es necesario añadir un libro.");
					salir = true;

				} else {

					gestorFicheros.crearPrestamos(RUTA_PRESTAMOS, RUTA_LIBROS, listaLibros, listaPrestamos);
				}

				break;

			case 2:

				listaPrestamos = gestorFicheros.leerPrestamos(RUTA_PRESTAMOS);
				for (String prestamo : listaPrestamos) {
					System.out.println(prestamo.toString());
				}

				break;

			case 3:
				System.out.println("Saliendo de Gestion de Prestamos ...");
				salir = true;
				break;

			default:
				System.out.println("Opción no válida. Por favor, intente de nuevo.");
				break;
			}
		}
	}

	// Método para gestionar la exportación e importación de datos en formato XML
	private static void gestionarExportImportXML() {
		boolean salir = false;

		while (!salir) {
			System.out.println("Bienvenido al Sistema de Gestión de XML");
			System.out.println("1. Exportar");
			System.out.println("2. Importar");
			System.out.println("3. Salir");
			System.out.print("Seleccione una opción: ");
			int opcion = sc.nextInt();
			switch (opcion) {
			case 1:

				System.out.println("1. Exportar Libros");
				System.out.println("2. Exportar Autores");
				System.out.println("3. Salir");
				System.out.print("Seleccione una opción: ");
				int opcionExportar = sc.nextInt();
				switch (opcionExportar) {
				case 1:
					gestorFicheros.exportarLibros(RUTA_LIBROS, RUTA_XML_LIBROS);
					break;
				case 2:
					gestorFicheros.exportarAutores(RUTA_AUTORES, RUTA_XML_AUTORES);
					break;
				case 3:
					salir = true;
					break;

				default:
					System.out.println("Debes elegir un fichero a exportar");
					break;
				}

				break;

			case 2:

				System.out.println("1. Importar Libros");
				System.out.println("2. Importar Autores");
				System.out.println("3. Salir");
				System.out.print("Seleccione una opción: ");
				int opcionImportar = sc.nextInt();
				switch (opcionImportar) {
				case 1:
					gestorFicheros.importarLibros(RUTA_XML_LIBROS, RUTA_LIBROS);
					break;
				case 2:
					gestorFicheros.importarAutores(RUTA_XML_AUTORES, RUTA_AUTORES);
					break;
				case 3:
					salir = true;
					break;
				default:
					System.out.println("Debes elegir un fichero a importar");
					break;
				}

				break;

			case 3:
				System.out.println("Saliendo de Gestion de XML ...");
				salir = true;
				break;

			default:
				System.out.println("Opción no válida. Por favor, intente de nuevo.");
				break;
			}
		}
	}

}
