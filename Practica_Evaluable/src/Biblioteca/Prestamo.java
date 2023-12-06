package Biblioteca;

public class Prestamo {
	
	private int idPrestamo;
	private int idLibro;
	private String nombreLibroPrestado;
	private String nombreUsuario;
	private String fechaPrestamo;
	private String fechaDevolucion;
	private static int contador = 0;
	
	// CONSTRUCTOR
	public Prestamo(String nameUser, String fPrest, String fDevol, int idLibro, String nomLibPrest) {
		contador++;
		this.idPrestamo = contador;
		this.nombreUsuario = nameUser;
		this.fechaPrestamo = fPrest;
		this.fechaDevolucion = fDevol;
		this.idLibro = idLibro;
		this.nombreLibroPrestado = nomLibPrest;
	}
	
	// GETTERS AND SETTERS
	public int getIdPrestamo() {
		return idPrestamo;
	}
	
	public String getNombreUsuario() {
		return nombreUsuario;
	}
	
	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	public String getFechaPrestamo() {
		return fechaPrestamo;
	}

	public void setFechaPrestamo(String fechaPrestamo) {
		this.fechaPrestamo = fechaPrestamo;
	}

	public String getFechaDevolucion() {
		return fechaDevolucion;
	}

	public void setFechaDevolucion(String fechaDevolucion) {
		this.fechaDevolucion = fechaDevolucion;
	}

	public int getIdLibro() {
		return idLibro;
	}

	public void setIdLibro(int idLibro) {
		this.idLibro = idLibro;
	}

	@Override
	public String toString() {
		return String.format("Id = %s, Id libro prestado = %s, Titulo Libro = %s, Nombre Usuario = %s, Fecha Inicio = %s, Fecha Devolucion = %s",
				idPrestamo, idLibro, nombreLibroPrestado ,nombreUsuario, fechaPrestamo, fechaDevolucion);
	}
	
	
	
}
