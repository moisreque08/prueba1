/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package EDD;
import EDD.Estacion;

/**
 * Representa una conexión (arista) entre dos estaciones de la red de transporte.
 * Cada arista tiene un origen, un destino y un tiempo de traslado en minutos.
 * @version 1.0
 */
public class Arista {
    private Estacion origen;
    private Estacion destino;
    private double tiempo;
    private boolean activa;
    
    /**
     * Constructor de la clase Arista.
     * 
     * @param origen Estación de origen
     * @param destino Estación de destino
     * @param tiempo Tiempo de traslado en minutos
     */
    public Arista(Estacion origen, Estacion destino, double tiempo) {
        this.origen = origen;
        this.destino = destino;
        this.tiempo = tiempo;
        this.activa = true;
    }
    
    /**
     * Obtiene la estación de origen.
     * @return Estación origen
     */
    public Estacion getOrigen() { 
        return origen; 
    }
    
    /**
     * Obtiene la estación de destino.
     * @return Estación destino
     */
    public Estacion getDestino() { 
        return destino; 
    }
    
    /**
     * Obtiene el tiempo de traslado.
     * @return Tiempo en minutos
     */
    public double getTiempo() { 
        return tiempo; 
    }
    
    /**
     * Verifica si la arista está activa.
     * @return true si está activa
     */
    public boolean isActiva() { 
        return activa; 
    }
    
    /**
     * Establece el estado de la arista.
     * @param activa Nuevo estado
     */
    public void setActiva(boolean activa) { 
        this.activa = activa; 
    }
    
    /**
     * Obtiene el nodo opuesto al dado.
     * 
     * @param e Estación de referencia
     * @return La estación opuesta, o null si no pertenece a la arista
     */
    public Estacion getOtroNodo(Estacion e) {
        if (origen.equals(e)) return destino;
        if (destino.equals(e)) return origen;
        return null;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Arista a = (Arista) obj;
        return (origen.equals(a.origen) && destino.equals(a.destino)) ||
               (origen.equals(a.destino) && destino.equals(a.origen));
    }
    
    @Override
    public String toString() {
        return origen.getId() + " - " + destino.getId() + " (" + tiempo + " min)";
    }
}
