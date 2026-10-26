/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package EDD;

/**
 * Representa una estación de la red de transporte multimodal.
 * Cada estación tiene un identificador único, nombre, línea, zona y capacidad.
 * @version 1.0
 */
public class Estacion {
    private String id;
    private String nombre;
    private String linea;
    private String zona;
    private int capacidad;
    private Lista<Arista> aristas;
    private boolean activa;
    
    /**
     * Constructor de la clase Estacion.
     * 
     * @param id Identificador único
     * @param nombre Nombre de la estación
     * @param linea Línea de transporte
     * @param zona Zona geográfica
     * @param capacidad Capacidad de pasajeros por hora
     */
    public Estacion(String id, String nombre, String linea, String zona, int capacidad) {
        this.id = id;
        this.nombre = nombre;
        this.linea = linea;
        this.zona = zona;
        this.capacidad = capacidad;
        this.aristas = new Lista<>();
        this.activa = true;
    }
    
    /**
     * Obtiene el identificador de la estación.
     * @return ID de la estación
     */
    public String getId() { 
        return id; 
    }
    
    /**
     * Obtiene el nombre de la estación.
     * @return Nombre de la estación
     */
    public String getNombre() { 
        return nombre; 
    }
    
    /**
     * Obtiene la línea de transporte.
     * @return Línea de la estación
     */
    public String getLinea() { 
        return linea; 
    }
    
    /**
     * Obtiene la zona geográfica.
     * @return Zona de la estación
     */
    public String getZona() { 
        return zona; 
    }
    
    /**
     * Obtiene la capacidad de pasajeros.
     * @return Capacidad por hora
     */
    public int getCapacidad() { 
        return capacidad; 
    }
    
    /**
     * Obtiene la lista de aristas de la estación.
     * @return Lista de aristas
     */
    public Lista<Arista> getAristas() { 
        return aristas; 
    }
    
    /**
     * Verifica si la estación está activa.
     * @return true si está activa
     */
    public boolean isActiva() { 
        return activa; 
    }
    
    /**
     * Establece el estado de la estación.
     * @param activa Nuevo estado
     */
    public void setActiva(boolean activa) { 
        this.activa = activa; 
    }
    
    /**
     * Agrega una arista a la estación.
     * @param a Arista a agregar
     */
    public void agregarArista(Arista a) {
        if (!aristas.contiene(a)) aristas.agregar(a);
    }
    
    /**
     * Elimina una arista de la estación.
     * @param a Arista a eliminar
     */
    public void eliminarArista(Arista a) {
        aristas.eliminar(a);
    }
    
    /**
     * Obtiene el grado de la estación (número de conexiones).
     * @return Grado de la estación
     */
    public int getGrado() { 
        return aristas.getTamaño(); 
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Estacion e = (Estacion) obj;
        return id.equals(e.id);
    }
    
    @Override
    public int hashCode() { 
        return id.hashCode(); 
    }
    
    @Override
    public String toString() {
        return id + " - " + nombre + " (" + linea + ")";
    }
}
