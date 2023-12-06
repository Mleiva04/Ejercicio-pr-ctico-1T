package Biblioteca;

import java.io.Serializable;

public class Autor implements Serializable {
	
	private int idAutor;
	private String nombre;
	private String nacionalidad;
	private int anioNacimiento;
	private static int contador = 0;
	
	// CONSTRUCTOR
	public Autor(String nombre, String nacionalidad, int anioNacimiento) {
		contador++;
		this.idAutor = contador;
		this.nombre = nombre;
		this.nacionalidad = nacionalidad;
		this.anioNacimiento = anioNacimiento;
	}
	
	// GETTERS AND SETTERS
	public int getIdAutor() {
		return idAutor;
	}

	public void setIdAutor(int idAutor) {
		this.idAutor = idAutor;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getNacionalidad() {
		return nacionalidad;
	}

	public void setNacionalidad(String nacionalidad) {
		this.nacionalidad = nacionalidad;
	}

	public int getAnioNacimiento() {
		return anioNacimiento;
	}

	public void setAnioNacimiento(int anioNacimiento) {
		this.anioNacimiento = anioNacimiento;
	}
	
	
	
	
}
