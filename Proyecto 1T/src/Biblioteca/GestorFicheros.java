package Biblioteca;

import java.io.*;
import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class GestorFicheros {
	// Declaracion de constantes pra rutas de carpetas
	public static final String CARPETA_PRINCIPAL = "C:/Biblioteca";
	public static final String CARPETA_XML = CARPETA_PRINCIPAL + "/XML";
	private static final String CARPETA_SEGURIDAD = CARPETA_PRINCIPAL + "/Seguridad";

	// Declaracion de constantes pra validar años
	public static final int MIN_ANIO_PUBLICACION = 2000;
	public static final int MAX_ANIO_PUBLICACION = Year.now().getValue();
	public static final int MIN_ANIO_NACIMIENTO = 1940;
	public static final int MAX_ANIO_NACIMIENTO = 2005;

	// Metodo para crear carpeta de ficheros automaticamente
	public void crearCarpetaFicheros() {

		try {
			// Crear la carpeta principal
			File carpetaFicheros = new File(CARPETA_PRINCIPAL);
			if (!carpetaFicheros.exists()) {
				carpetaFicheros.mkdirs();
				// Crear los archivos en la carpeta principal
				new File(carpetaFicheros, "libros.bin").createNewFile();
				new File(carpetaFicheros, "autores.bin").createNewFile();
				new File(carpetaFicheros, "prestamos.txt").createNewFile();
			}

			// Crear la subcarpeta "xml"
			File xml = new File(CARPETA_XML);
			if (!xml.exists()) {
				xml.mkdirs();
				// Crear los archivos XML en la subcarpeta "XML"
				new File(xml, "libros.xml").createNewFile();
				new File(xml, "autores.xml").createNewFile();
			}

			System.out.println("Archivos creados correctamente en el directorio " + CARPETA_PRINCIPAL);

		} catch (IOException e) {
			System.err.println("¡ERROR! Hubo un problema al crear los ficheros.");
            e.printStackTrace();
		}
		
	}

	// Metodo para guardar libros en el fichero
	public void escribirLibrosBinario(List<Libro> libros, String filename) {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
			oos.writeObject(libros);
		} catch (IOException e) {
			e.getStackTrace();
		}
	}

	// Metodo para leer libros en el fichero
	public List<Libro> leerLibrosBinario(String filename) {
		List<Libro> listaLibros = new ArrayList<>();

		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
			listaLibros = (ArrayList<Libro>) ois.readObject();
		} catch (ClassNotFoundException | IOException e) {
			e.getStackTrace();
		}

		return listaLibros;
	}

	// Metodo para modificar libros en el fichero
	public void modificarLibroBinario(String filename, int id) {
		List<Libro> listaLibros = leerLibrosBinario(filename);
		Scanner scLibros = new Scanner(System.in);
		boolean encontrado = false;
		int anioModificado;

		for (Libro libroMostrar : listaLibros) {
			if (libroMostrar.getIdLibro() == id) {
				encontrado = true;

				// Mostramos el libro que cumple la condicion
				System.out.println("Libro con ID: " + id);
				System.out.println("----------------------------------------");
				System.out.println("Id: " + libroMostrar.getIdLibro());
				System.out.println("Titulo: " + libroMostrar.getTitulo());
				System.out.println("Autor: " + libroMostrar.getAutor());
				System.out.println("Año Publicación: " + libroMostrar.getAnioPublicacion());
				System.out.println("Género: " + libroMostrar.getGenero());
				System.out.println("Estado: " + libroMostrar.isEstado());
				System.out.println("----------------------------------------");

				System.out.println("Datos a modificar del libro: ");

				// Pedimos al usuario los datos del libro
				System.out.printf("Se deben añadir libros cuyo año de publicación esté entre (%d-%d) %n", MIN_ANIO_PUBLICACION, MAX_ANIO_PUBLICACION);
				System.out.println("Titulo:");
				String tituloModificado = scLibros.nextLine();

				System.out.println("Autor:");
				String autorModificado = scLibros.nextLine();

				do {
					System.out.printf("Año Publicación (%d-%d): %n", MIN_ANIO_PUBLICACION, MAX_ANIO_PUBLICACION);
					anioModificado = scLibros.nextInt();
				} while (!esAnioPubValido(anioModificado));

				scLibros.nextLine();
				
				System.out.println("Género:");
				String generoModificado = scLibros.nextLine();

				// Modificamos el libro con los datos añadidos por el usuario
				libroMostrar.setTitulo(tituloModificado);
				libroMostrar.setAutor(autorModificado);
				libroMostrar.setAnioPublicacion(anioModificado);
				libroMostrar.setGenero(generoModificado);

				listaLibros.set(listaLibros.indexOf(libroMostrar), libroMostrar);
				escribirLibrosBinario(listaLibros, filename); // Escribimos el libro en el fichero

			}
		}

		if (!encontrado) {
			System.err.println("¡ERROR! No se encontró ningún libro con la ID " + id + " en el archivo.");
		} else {
			System.out.println("¡Libro con ID "+id+" modificado con éxito!");
		}

	}

	// Metodo para eliminar libros en el fichero
	public void eliminarLibroBinario(String filename, int id) {
		List<Libro> listaLibros = leerLibrosBinario(filename);
		boolean encontrado = false;

		for (Libro libro : listaLibros) {
			if (libro.getIdLibro() == id) {
				encontrado = true;

				System.out.println("Libro con ID: " + id);
				System.out.println("----------------------------------------");
				System.out.println("Id: " + libro.getIdLibro());
				System.out.println("Titulo: " + libro.getTitulo());
				System.out.println("Autor: " + libro.getAutor());
				System.out.println("Año Publicación: " + libro.getAnioPublicacion());
				System.out.println("Género: " + libro.getGenero());
				System.out.println("----------------------------------------");

				// Utilizamos la funcion "remove" para eliminar el libro en la posicion (id) dada por el usuario
				listaLibros.remove(listaLibros.indexOf(libro));
				escribirLibrosBinario(listaLibros, filename);
				break;
			}
		}

		if (!encontrado) {
			System.err.println("¡ERROR! No se encontró ningún libro con la ID " + id + " en el archivo.");
		} else {
			System.out.println("¡Libro con ID "+id+" eliminado con éxito!");
		}
	}

	// Metodo para guardar autores en el fichero
	public void escribirAutoresBinario(List<Autor> autores, String filename) {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
			oos.writeObject(autores);
		} catch (IOException e) {
			e.getStackTrace();
		}
	}

	// Metodo para leer autores en el fichero
	public List<Autor> leerAutoresBinario(String filename) {
		List<Autor> listaAutores = new ArrayList<>();

		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
			listaAutores = (ArrayList<Autor>) ois.readObject();
		} catch (ClassNotFoundException | IOException e) {
			e.getStackTrace();
		}

		return listaAutores;
	}

	// Metodo para modificar autores en el fichero
	public void modificarAutorBinario(String filename, int id) {
		List<Autor> listaAutores = leerAutoresBinario(filename);
		Scanner scAutores = new Scanner(System.in);
		boolean encontrado = false;
		int anioModificado;

		for (Autor autor : listaAutores) {
			if (autor.getIdAutor() == id) {
				encontrado = true;

				System.out.println("Autor con ID: " + id);
				System.out.println("-------------------------------------");
				System.out.println("Id: " + autor.getIdAutor());
				System.out.println("Nombre: " + autor.getNombre());
				System.out.println("Nacionalidad: " + autor.getNacionalidad());
				System.out.println("Año nacimiento: " + autor.getAnioNacimiento());
				System.out.println("-------------------------------------");

				System.out.println("Datos a modificar del autor: ");

				// Pedimos al usuario los datos del autor
				System.out.printf("Se deben añadir autores cuyo año de nacimiento sea (%d-%d) %n", MIN_ANIO_NACIMIENTO, MAX_ANIO_NACIMIENTO);
				System.out.println("Nombre:");
				String nombreModificado = scAutores.nextLine();

				System.out.println("Nacionalidad:");
				String nacionalidadModificada = scAutores.nextLine();
				
				do {
					System.out.printf("Año Nacimiento (%d-%d): %n", MIN_ANIO_NACIMIENTO, MAX_ANIO_NACIMIENTO);
					anioModificado = scAutores.nextInt();
				} while (!esAnioNacValido(anioModificado));

				// Modificamos el autor con los datos añadidos por el usuario
				autor.setNombre(nombreModificado);
				autor.setNacionalidad(nacionalidadModificada);
				autor.setAnioNacimiento(anioModificado);

				// Utilizamos la funcion "set" para modificar el libro en la posicion (id) dada
				// por el usuario por el nuevo libro creado
				// listaLibros.indexOf(libro) Esta funcion obtiene la posicion en la que el
				// libro cumple la condicion
				listaAutores.set(listaAutores.indexOf(autor), autor);
				escribirAutoresBinario(listaAutores, filename);
			}
		}

		if (!encontrado) {
			System.err.println("¡ERROR! No se encontró ningún autor con la ID " + id + " en el archivo.");
		} else {
			System.out.println("¡Autor con ID "+id+" modificado con éxito!");
		}

	}

	// Metodo para eliminar autores en el fichero
	public void eliminarAutorBinario(String filename, int id) {
		List<Autor> listaAutores = leerAutoresBinario(filename);
		boolean encontrado = false;

		for (Autor autor : listaAutores) {
			if (autor.getIdAutor() == id) {
				encontrado = true;

				// Mostramos el autor que cumple la condicion
				System.out.println("Autor con ID: " + id);
				System.out.println("-------------------------------------");
				System.out.println("Id: " + autor.getIdAutor());
				System.out.println("Nombre: " + autor.getNombre());
				System.out.println("Nacionalidad: " + autor.getNacionalidad());
				System.out.println("Año nacimiento: " + autor.getAnioNacimiento());
				System.out.println("-------------------------------------");

				// Utilizamos la funcion "remove" para eliminar el libro en la posicion (id) dada por el usuario
				listaAutores.remove(listaAutores.indexOf(autor));
				escribirAutoresBinario(listaAutores, filename);
				break;
			}
		}

		if (!encontrado) {
			System.err.println("¡ERROR! No se encontró ningún autor con la ID " + id + " en el archivo.");
		} else {
			System.out.println("¡Autor con ID "+id+" eliminado con éxito!");
		}
	}

	// Metodo para leer prestamos en el fichero
	public ArrayList<String> leerPrestamos(String filename) {
		ArrayList<String> listaPrestamos = new ArrayList<>();

		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filename))) {
			String linea;
			while ((linea = bufferedReader.readLine()) != null) {
				listaPrestamos.add(linea);
			}
		} catch (IOException e) {
			System.err.println("ERROR: Fallo en lectura de prestamos");
		}

		return listaPrestamos;

	}

	// Metodo para guardar prestamos en el fichero
	public void escribirPrestamos(ArrayList<String> listaPrestamos, String filename) {
		try (FileWriter fw = new FileWriter(filename)) {
			for (String prestamo : listaPrestamos) {
				fw.write(prestamo.toString() + "\n");
			}
		} catch (IOException e) {
			System.err.println("ERROR: Fallo en escritura de prestamos");
		}
	}

	// Metodo para añadir prestamos al fichero
	public void crearPrestamos(String filePrestamos, String fileLibros, List<Libro> listaLibros, List<String> listaPrestamos) {

		Scanner scPrestamos = new Scanner(System.in);
		listaLibros = leerLibrosBinario(fileLibros);
		listaPrestamos = leerPrestamos(filePrestamos);
		boolean encontrado = false;

		LocalDate fechaActual = LocalDate.now();
		LocalDate fechaMasPlazo = fechaActual.plusMonths(3);

		DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy", new Locale("es", "ES"));
		String fechaEntrega = fechaActual.format(formatoFecha);
		String fechaDevolucion = fechaMasPlazo.format(formatoFecha);

		System.out.println("Nombre y Apellidos Usuario:");
		String nombre = scPrestamos.nextLine();

		for (Libro libroMostrar : listaLibros) {
			System.out.println("----------------------------------------");
			System.out.println("Id: " + libroMostrar.getIdLibro());
			System.out.println("Titulo: " + libroMostrar.getTitulo());
			System.out.println("Autor: " + libroMostrar.getAutor());
			System.out.println("Año: " + libroMostrar.getAnioPublicacion());
			System.out.println("Género: " + libroMostrar.getGenero());
			System.out.println("Estado: " + (libroMostrar.isEstado() ? "Prestado" : "No Prestado"));
			System.out.println("----------------------------------------");
		}

		System.out.println("Que libro desea añadir al préstamo (ID): ");
		int id = scPrestamos.nextInt();

		for (Libro libroMostrar : listaLibros) {
			// Verificar si el libro tiene el ID correcto y no está prestado
			if (id == libroMostrar.getIdLibro() && !libroMostrar.isEstado()) {
				encontrado = true;

				// Crear un objeto Prestamo
				Prestamo prestamo = new Prestamo(nombre, fechaEntrega, fechaDevolucion, id, libroMostrar.getTitulo());

				listaPrestamos = leerPrestamos(filePrestamos);

				// Añadir el nuevo préstamo a la lista
				listaPrestamos.add(prestamo.toString());

				escribirPrestamos((ArrayList<String>) listaPrestamos, filePrestamos);

				// Actualizar el estado del libro a prestado
				libroMostrar.setEstado(true);

				escribirLibrosBinario(listaLibros, fileLibros);
				System.out.println("Prestamos añadido con exito");

			} else if (id == libroMostrar.getIdLibro() && libroMostrar.isEstado()) {
				System.out.println("El libro con ID "+id+" ya está prestado");
				encontrado = true;
			}
		}

		if (!encontrado) {
			System.err.println("ERROR: La ID no se encuentra en el fichero");
		}
	}

	// Método para crear copia de seguridad de los archivos de libros y autores
	public void crearCopiaSeguridad(String fileLibros, String fileAutores) {

		try {

			// Creamos la carpeta de seguridad junto a la copia de los ficheros
			File carpetaSeguridad = new File(CARPETA_SEGURIDAD);
			File ficheroLibros = new File(carpetaSeguridad, "libros (copia).bin");
			File ficheroAutores = new File(carpetaSeguridad, "autores (copia).bin");

			// Condicion para comprobar si el directorio "Seguridad" y los ficheros "libros
			// (copia).bin" y "autores (copia).bin" existen
			if (!ficheroLibros.exists() && !ficheroAutores.exists() && !carpetaSeguridad.exists()) {
				carpetaSeguridad.mkdir();
				ficheroLibros.createNewFile();
				ficheroAutores.createNewFile();

			} else { // Si estos existen añade el contenido de los ficheros "libros.bin" y "autores.bin" a las copias creadas anteriormente

				List<Libro> listaLibros = leerLibrosBinario(fileLibros);
				List<Autor> listaAutores = leerAutoresBinario(fileAutores);

				escribirAutoresBinario(listaAutores, CARPETA_SEGURIDAD + "/autores (copia).bin");
				escribirLibrosBinario(listaLibros, CARPETA_SEGURIDAD + "/libros (copia).bin");
				
			}

		} catch (IOException e) {
			System.err.println("No se han podido guardar los ficheros: " + fileLibros + " , " + fileAutores);
		}

	}

	public void copiaSeguridad() {

		// Leemos la lista de libros del archivo de la copia de seguridad
		List<Libro> listaLibros = leerLibrosBinario(CARPETA_SEGURIDAD + "/libros (copia).bin");
		System.out.println("Fichero Libros:");
		if (!listaLibros.isEmpty()) {

			// Mostramos la lista de libros
			for (Libro libroMostrar : listaLibros) {
				System.out.println("-------------------------------------");
				System.out.println("Id: " + libroMostrar.getIdLibro());
				System.out.println("Titulo: " + libroMostrar.getTitulo());
				System.out.println("Autor: " + libroMostrar.getAutor());
				System.out.println("Año Publicado: " + libroMostrar.getAnioPublicacion());
				System.out.println("Género: " + libroMostrar.getGenero());
				System.out.println("-------------------------------------");
			}

		} else {
			System.out.println("No se ha añadido ningun libro");
		}

		// Leemos la lista de autores del archivo de la copia de seguridad
		List<Autor> listaAutores = leerAutoresBinario(CARPETA_SEGURIDAD + "/autores (copia).bin");
		System.out.println("Fichero Autores:");
		if (!listaAutores.isEmpty()) {

			// Mostramos la lista de autores
			for (Autor autorMostrar : listaAutores) {
				System.out.println("-------------------------------------");
				System.out.println("Id: " + autorMostrar.getIdAutor());
				System.out.println("Nombre: " + autorMostrar.getNombre());
				System.out.println("Nacionalidad: " + autorMostrar.getNacionalidad());
				System.out.println("Año nacimiento: " + autorMostrar.getAnioNacimiento());
				System.out.println("-------------------------------------");
			}

		} else {
			System.out.println("No se ha añadido ningun autor");
		}
	}

	// Metodo para exportar fichero libros
	public void exportarLibros(String fileLibros, String XmlLibros) {
		try {
			List<Libro> listaLibros = leerLibrosBinario(fileLibros);

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();

			// Configurar el Transformer para la salida formateada
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

			Document documentLibros = builder.newDocument();
			Element libreria = documentLibros.createElement("libreria");
			documentLibros.appendChild(libreria);

			// Comprobamos que la lista no esta vacia
			if (!listaLibros.isEmpty()) {
				for (Libro libroExp : listaLibros) {
					Element libros = documentLibros.createElement("libro");
					libros.setAttribute("id", String.valueOf(libroExp.getIdLibro()));

					Element titulo = documentLibros.createElement("titulo");
					titulo.appendChild(documentLibros.createTextNode(libroExp.getTitulo()));
					libros.appendChild(titulo);

					Element autor = documentLibros.createElement("autor");
					autor.appendChild(documentLibros.createTextNode(libroExp.getAutor()));
					libros.appendChild(autor);

					Element anioPublicacion = documentLibros.createElement("anioPublicacion");
					anioPublicacion
							.appendChild(documentLibros.createTextNode(String.valueOf(libroExp.getAnioPublicacion())));
					libros.appendChild(anioPublicacion);

					Element genero = documentLibros.createElement("genero");
					genero.appendChild(documentLibros.createTextNode(libroExp.getGenero()));
					libros.appendChild(genero);

					libreria.appendChild(libros);
				}
				System.out.println("Libros exportados con éxito");
			} else {
				System.out.println("Antes de exportar debe añadir un libro");
			}

			// Guardar los cambios en el archivo XML
			DOMSource sourceLibros = new DOMSource(documentLibros);
			StreamResult resultLibros = new StreamResult(new File(XmlLibros));
			transformer.transform(sourceLibros, resultLibros);

		} catch (ParserConfigurationException | TransformerException e) {
			e.printStackTrace();
		}
	}

	// Metodo para exportar fichero autores
	public void exportarAutores(String fileAutores, String XmlAutores) {
		try {
			List<Autor> listaAutores = leerAutoresBinario(fileAutores);

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();

			// Configurar el Transformer para la salida formateada
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

			Document documentAutores = builder.newDocument();

			Element autoria = documentAutores.createElement("autoria");
			documentAutores.appendChild(autoria);

			// Comprobamos que la lista no esta vacia
			if (!listaAutores.isEmpty()) {
				for (Autor autorExp : listaAutores) {
					Element autores = documentAutores.createElement("autor");
					autores.setAttribute("id", String.valueOf(autorExp.getIdAutor()));

					Element nombre = documentAutores.createElement("nombre");
					nombre.appendChild(documentAutores.createTextNode(autorExp.getNombre()));
					autores.appendChild(nombre);

					Element nacionalidad = documentAutores.createElement("nacionalidad");
					nacionalidad.appendChild(documentAutores.createTextNode(autorExp.getNacionalidad()));
					autores.appendChild(nacionalidad);

					Element anioNacimiento = documentAutores.createElement("anioNacimiento");
					anioNacimiento.appendChild(documentAutores.createTextNode(String.valueOf(autorExp.getAnioNacimiento())));
					autores.appendChild(anioNacimiento);

					autoria.appendChild(autores);
				}
				System.out.println("Autores exportados con éxito");
			} else {
				System.out.println("Antes de exportar debe añadir un autor");
			}

			DOMSource sourceAutores = new DOMSource(documentAutores);
			StreamResult resultAutores = new StreamResult(new File(XmlAutores));
			transformer.transform(sourceAutores, resultAutores);

		} catch (ParserConfigurationException | TransformerException e) {
			e.printStackTrace();
		}
	}

	// Metodo para importar fichero libros
	public void importarLibros(String XmlLibros, String fileLibros) {
		try {
			List<Libro> listaLibros = new ArrayList<>();

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new File(XmlLibros));
			NodeList librosXML = document.getElementsByTagName("libro");

			for (int i = 0; i < librosXML.getLength(); i++) {
				Element elementoLibro = (Element) librosXML.item(i);
				NodeList titulos = elementoLibro.getElementsByTagName("titulo");
				NodeList autores = elementoLibro.getElementsByTagName("autor");
				NodeList aniosPublicacion = elementoLibro.getElementsByTagName("anioPublicacion");
				NodeList generos = elementoLibro.getElementsByTagName("genero");

				// Comprobamos que los campos del xml no estan vacios
				if (titulos.getLength() > 0 && autores.getLength() > 0 && aniosPublicacion.getLength() > 0
						&& generos.getLength() > 0) {
					Element titulo = (Element) titulos.item(0);
					Element autor = (Element) autores.item(0);
					Element anioPublicacion = (Element) aniosPublicacion.item(0);
					Element genero = (Element) generos.item(0);

					// Crear un nuevo libro con la información del XML
					Libro libro = new Libro(titulo.getTextContent(), autor.getTextContent(),
							Integer.parseInt(anioPublicacion.getTextContent()), genero.getTextContent());
					libro.setIdLibro(Integer.parseInt(elementoLibro.getAttribute("id")));

					// Agregar el nuevo libro a la lista
					listaLibros.add(libro);
				}
			}

			// Leer la lista actual de libros del archivo binario
			List<Libro> listaLibrosActual = leerLibrosBinario(fileLibros);
			listaLibrosActual.clear();
			// Agregar todos los nuevos libros a la lista existente
			listaLibrosActual.addAll(listaLibros);

			// Guardar la lista actualizada en el fichero binario
			escribirLibrosBinario(listaLibrosActual, fileLibros);
			
			System.out.println("Libros importados con éxito");

		} catch (SAXException | IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}

	// Metodo para exportar fichero autores
	public void importarAutores(String XmlAutores, String fileAutores) {
		try {
			List<Autor> listaAutores = new ArrayList<>();

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new File(XmlAutores));
			NodeList autoresXML = document.getElementsByTagName("autor");

			for (int i = 0; i < autoresXML.getLength(); i++) {
				Element elementoAutor = (Element) autoresXML.item(i);
				NodeList nombres = elementoAutor.getElementsByTagName("nombre");
				NodeList nacionalidades = elementoAutor.getElementsByTagName("nacionalidad");
				NodeList aniosNacimiento = elementoAutor.getElementsByTagName("anioNacimiento");

				// Comprobamos que los campos del xml no estan vacios
				if (nombres.getLength() > 0 && nacionalidades.getLength() > 0 && aniosNacimiento.getLength() > 0) {
					Element nombre = (Element) nombres.item(0);
					Element nacionalidad = (Element) nacionalidades.item(0);
					Element anioNacimiento = (Element) aniosNacimiento.item(0);

					// Crear un nuevo libro con la información del XML
					Autor autor = new Autor(nombre.getTextContent(), nacionalidad.getTextContent(),
							Integer.parseInt(anioNacimiento.getTextContent()));
					autor.setIdAutor(Integer.parseInt(elementoAutor.getAttribute("id")));

					// Agregar el nuevo autor a la lista
					listaAutores.add(autor);
				}
			}

			// Leer la lista actual de libros del archivo binario
			List<Autor> listaAutoresActual = leerAutoresBinario(fileAutores);
			listaAutoresActual.clear();
			// Agregar todos los nuevos libros a la lista existente
			listaAutoresActual.addAll(listaAutores);

			// Guardar la lista actualizada en el fichero binario
			escribirAutoresBinario(listaAutoresActual, fileAutores);
			
			System.out.println("Autores importados con éxito");

		} catch (SAXException | IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}

	// Método para validar el año de publicación
	public static boolean esAnioPubValido(int anio) {
	    return anio >= MIN_ANIO_PUBLICACION && anio <= MAX_ANIO_PUBLICACION;
	}

	// Método para validar el año de nacimiento
	public static boolean esAnioNacValido(int anio) {
	    return anio >= MIN_ANIO_NACIMIENTO && anio <= MAX_ANIO_NACIMIENTO;
	}
}
