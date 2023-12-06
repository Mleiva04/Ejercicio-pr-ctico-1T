package Biblioteca;

import java.io.Serializable;

public class Libro implements Serializable {
	
	private int idLibro;
	private String titulo;
	private String autor;
	private int anioPublicacion;
	private String genero;
	private static int contador = 0;
	private boolean estado = false;
	
	// CONTRUCTOR
	public Libro(String titulo, String autor, int anioPublicacion, String genero) {
		contador++;
		this.idLibro = contador;
		this.titulo = titulo;
		this.autor = autor;
		this.anioPublicacion = anioPublicacion;
		this.genero = genero;
		this.estado = false;
	}
	
	// GETTERS AND SETTERS
	public int getIdLibro() {
		return idLibro;
	}

	public void setIdLibro(int idLibro) {
		this.idLibro = idLibro;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getAutor() {
		return autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}

	public int getAnioPublicacion() {
		return anioPublicacion;
	}

	public void setAnioPublicacion(int anioPublicacion) {
		this.anioPublicacion = anioPublicacion;
	}

	public String getGenero() {
		return genero;
	}

	public void setGenero(String genero) {
		this.genero = genero;
	}

	public boolean isEstado() {
		return estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	
}
