/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package EDD;

/**
 * Implementación de una Tabla Hash con encadenamiento separado.
 * Permite almacenar e indexar objetos por clave con complejidad O(1) promedio.
 * @param <K> Tipo de la clave
 * @param <V> Tipo del valor
 * @version 1.0
 */
public class TablaHash<K, V> {
    private Lista<Entrada<K, V>>[] tabla;
    private int tamanio;
    private int capacidad;
    private static final int CAPACIDAD_INICIAL = 16;
    private static final double FACTOR_CARGA = 0.75;
    
    /**
     * Clase interna que representa una entrada clave-valor.
     * 
     * @param <K> Tipo de clave
     * @param <V> Tipo de valor
     */
    private static class Entrada<K, V> {
        K clave;
        V valor;
        
        Entrada(K clave, V valor) {
            this.clave = clave;
            this.valor = valor;
        }
    }
    
    /**
     * Constructor que crea una tabla hash con capacidad inicial.
     */
    @SuppressWarnings("unchecked")
    public TablaHash() {
        this.capacidad = CAPACIDAD_INICIAL;
        this.tabla = new Lista[capacidad];
        for (int i = 0; i < capacidad; i++) {
            tabla[i] = new Lista<>();
        }
        this.tamanio = 0;
    }
    
    /**
     * Calcula el índice hash de una clave.
     * 
     * @param clave Clave a hashear
     * @return Índice en la tabla
     */
    private int hash(K clave) {
        return Math.abs(clave.hashCode()) % capacidad;
    }
    
    /**
     * Inserta o actualiza un par clave-valor.
     * 
     * @param clave Clave del elemento
     * @param valor Valor asociado
     */
    public void put(K clave, V valor) {
        if ((double) tamanio / capacidad >= FACTOR_CARGA) {
            rehash();
        }
        
        int indice = hash(clave);
        Lista<Entrada<K, V>> lista = tabla[indice];
        
        for (int i = 0; i < lista.getTamaño(); i++) {
            Entrada<K, V> e = lista.obtener(i);
            if (e.clave.equals(clave)) {
                e.valor = valor;
                return;
            }
        }
        
        lista.agregar(new Entrada<>(clave, valor));
        tamanio++;
    }
    
    /**
     * Obtiene el valor asociado a una clave.
     * 
     * @param clave Clave a buscar
     * @return El valor asociado, o null si no existe
     */
    public V get(K clave) {
        int indice = hash(clave);
        Lista<Entrada<K, V>> lista = tabla[indice];
        
        for (int i = 0; i < lista.getTamaño(); i++) {
            Entrada<K, V> e = lista.obtener(i);
            if (e.clave.equals(clave)) {
                return e.valor;
            }
        }
        return null;
    }
    
    /**
     * Elimina una entrada por su clave.
     * 
     * @param clave Clave a eliminar
     * @return El valor eliminado, o null si no existe
     */
    public V remove(K clave) {
        int indice = hash(clave);
        Lista<Entrada<K, V>> lista = tabla[indice];
        
        for (int i = 0; i < lista.getTamaño(); i++) {
            Entrada<K, V> e = lista.obtener(i);
            if (e.clave.equals(clave)) {
                lista.eliminarPorIndice(i);
                tamanio--;
                return e.valor;
            }
        }
        return null;
    }
    
    /**
     * Verifica si existe una clave en la tabla.
     * 
     * @param clave Clave a verificar
     * @return true si existe
     */
    public boolean contiene(K clave) {
        return get(clave) != null;
    }
    
    /**
     * Expande la tabla cuando se alcanza el factor de carga.
     */
    @SuppressWarnings("unchecked")
    private void rehash() {
        Lista<Entrada<K, V>>[] old = tabla;
        capacidad *= 2;
        tabla = new Lista[capacidad];
        for (int i = 0; i < capacidad; i++) {
            tabla[i] = new Lista<>();
        }
        tamanio = 0;
        
        for (Lista<Entrada<K, V>> lista : old) {
            for (int i = 0; i < lista.getTamaño(); i++) {
                Entrada<K, V> e = lista.obtener(i);
                put(e.clave, e.valor);
            }
        }
    }
    
    /**
     * Obtiene todas las claves de la tabla.
     * 
     * @return Lista con todas las claves
     */
    public Lista<K> getClaves() {
        Lista<K> claves = new Lista<>();
        for (int i = 0; i < capacidad; i++) {
            Lista<Entrada<K, V>> lista = tabla[i];
            for (int j = 0; j < lista.getTamaño(); j++) {
                claves.agregar(lista.obtener(j).clave);
            }
        }
        return claves;
    }
    
    /**
     * Obtiene todos los valores de la tabla.
     * 
     * @return Lista con todos los valores
     */
    public Lista<V> getValores() {
        Lista<V> valores = new Lista<>();
        for (int i = 0; i < capacidad; i++) {
            Lista<Entrada<K, V>> lista = tabla[i];
            for (int j = 0; j < lista.getTamaño(); j++) {
                valores.agregar(lista.obtener(j).valor);
            }
        }
        return valores;
    }
    
    /**
     * Obtiene el número de elementos en la tabla.
     * 
     * @return Cantidad de elementos
     */
    public int getTamanio() { 
        return tamanio; 
    }
    
    /**
     * Verifica si la tabla está vacía.
     * 
     * @return true si está vacía
     */
    public boolean estaVacia() { 
        return tamanio == 0; 
    }
    
    /**
     * Limpia la tabla, eliminando todos los elementos.
     */
    @SuppressWarnings("unchecked")
    public void limpiar() {
        tabla = new Lista[capacidad];
        for (int i = 0; i < capacidad; i++) {
            tabla[i] = new Lista<>();
        }
        tamanio = 0;
    }
}
