/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package EDD;

/**
 * Grafo no dirigido ponderado que representa la red de transporte multimodal.
 * Utiliza una Tabla Hash para indexar estaciones y una lista de adyacencia para las conexiones.
 * @version 1.0
 */
public class Grafo {
    private TablaHash<String, Estacion> estaciones;
    private Lista<Arista> aristas;
    
    /**
     * Constructor del grafo vacío.
     */
    public Grafo() {
        this.estaciones = new TablaHash<>();
        this.aristas = new Lista<>();
    }
    
    
    /**
     * Agrega una nueva estación al grafo.
     * 
     * @param id Identificador único
     * @param nombre Nombre de la estación
     * @param linea Línea de transporte
     * @param zona Zona geográfica
     * @param capacidad Capacidad de pasajeros
     * @return true si se agregó correctamente
     */
    public boolean agregarEstacion(String id, String nombre, String linea, String zona, int capacidad) {
        if (!estaciones.contiene(id)) {
            Estacion e = new Estacion(id, nombre, linea, zona, capacidad);
            estaciones.put(id, e);
            return true;
        }
        return false;
    }
    
    /**
     * Elimina una estación y todas sus conexiones del grafo.
     * 
     * @param id ID de la estación a eliminar
     * @return true si se eliminó correctamente
     */
    public boolean eliminarEstacion(String id) {
        Estacion e = estaciones.get(id);
        if (e == null) return false;
        
        Lista<Arista> aEliminar = new Lista<>();
        for (int i = 0; i < aristas.getTamaño(); i++) {
            Arista a = aristas.obtener(i);
            if (a.getOrigen().equals(e) || a.getDestino().equals(e)) {
                aEliminar.agregar(a);
            }
        }
        for (int i = 0; i < aEliminar.getTamaño(); i++) {
            eliminarArista(aEliminar.obtener(i));
        }
        
        estaciones.remove(id);
        return true;
    }
    
    /**
     * Obtiene una estación por su ID.
     * @param id ID de la estación
     * @return La estación, o null si no existe
     */
    public Estacion getEstacion(String id) { 
        return estaciones.get(id); 
    }
    
    /**
     * Verifica si existe una estación.
     * @param id ID de la estación
     * @return true si existe
     */
    public boolean existeEstacion(String id) { 
        return estaciones.contiene(id); 
    }
    
    /**
     * Obtiene la tabla hash de estaciones.
     * @return Tabla hash
     */
    public TablaHash<String, Estacion> getEstaciones() { 
        return estaciones; 
    }
    
    /**
     * Obtiene todas las estaciones del grafo.
     * @return Lista de estaciones
     */
    public Lista<Estacion> getTodasEstaciones() {
        return estaciones.getValores();
    }
        
    /**
     * Agrega una conexión entre dos estaciones.
     * 
     * @param idOrigen ID de la estación origen
     * @param idDestino ID de la estación destino
     * @param tiempo Tiempo de traslado en minutos
     * @return true si se agregó correctamente
     */
    public boolean agregarArista(String idOrigen, String idDestino, double tiempo) {
        Estacion origen = estaciones.get(idOrigen);
        Estacion destino = estaciones.get(idDestino);
        
        if (origen == null || destino == null) return false;
        if (origen.equals(destino)) return false;
        
        for (int i = 0; i < aristas.getTamaño(); i++) {
            Arista a = aristas.obtener(i);
            if ((a.getOrigen().equals(origen) && a.getDestino().equals(destino)) ||
                (a.getOrigen().equals(destino) && a.getDestino().equals(origen))) {
                return false;
            }
        }
        
        Arista nueva = new Arista(origen, destino, tiempo);
        aristas.agregar(nueva);
        origen.agregarArista(nueva);
        destino.agregarArista(nueva);
        return true;
    }
    
    /**
     * Elimina una arista del grafo.
     * 
     * @param a Arista a eliminar
     * @return true si se eliminó
     */
    public boolean eliminarArista(Arista a) {
        if (aristas.eliminar(a)) {
            a.getOrigen().eliminarArista(a);
            a.getDestino().eliminarArista(a);
            return true;
        }
        return false;
    }
    
    /**
     * Elimina una conexión entre dos estaciones.
     * 
     * @param idOrigen ID de la estación origen
     * @param idDestino ID de la estación destino
     * @return true si se eliminó
     */
    public boolean eliminarArista(String idOrigen, String idDestino) {
        for (int i = 0; i < aristas.getTamaño(); i++) {
            Arista a = aristas.obtener(i);
            if ((a.getOrigen().getId().equals(idOrigen) && a.getDestino().getId().equals(idDestino)) ||
                (a.getOrigen().getId().equals(idDestino) && a.getDestino().getId().equals(idOrigen))) {
                return eliminarArista(a);
            }
        }
        return false;
    }
    
    /**
     * Obtiene todas las aristas del grafo.
     * @return Lista de aristas
     */
    public Lista<Arista> getAristas() { 
        return aristas; 
    }
    
    
    /**
     * Encuentra las subredes (componentes conexos) usando BFS.
     * 
     * @return Lista de subredes, donde cada subred es una lista de estaciones
     */
    public Lista<Lista<Estacion>> encontrarSubredes() {
        Lista<Lista<Estacion>> subredes = new Lista<>();
        Lista<Estacion> visitadas = new Lista<>();
        
        Lista<Estacion> todas = getTodasEstaciones();
        for (int i = 0; i < todas.getTamaño(); i++) {
            Estacion e = todas.obtener(i);
            if (!visitadas.contiene(e) && e.isActiva()) {
                Lista<Estacion> subred = new Lista<>();
                Lista<Estacion> cola = new Lista<>();
                
                cola.agregar(e);
                visitadas.agregar(e);
                
                while (!cola.estaVacia()) {
                    Estacion actual = cola.getPrimero();
                    cola.eliminarPorIndice(0);
                    subred.agregar(actual);
                    
                    Lista<Arista> aristasActual = actual.getAristas();
                    for (int j = 0; j < aristasActual.getTamaño(); j++) {
                        Arista a = aristasActual.obtener(j);
                        if (a.isActiva()) {
                            Estacion vecino = a.getOtroNodo(actual);
                            if (vecino != null && vecino.isActiva() && !visitadas.contiene(vecino)) {
                                visitadas.agregar(vecino);
                                cola.agregar(vecino);
                            }
                        }
                    }
                }
                
                if (subred.getTamaño() > 1) {
                    subredes.agregar(subred);
                }
            }
        }
        return subredes;
    }
    
    /**
     * Encuentra la ruta más corta entre dos estaciones usando Dijkstra.
     * 
     * @param idOrigen ID de la estación origen
     * @param idDestino ID de la estación destino
     * @return ResultadoDijkstra con distancia y ruta
     */
    public ResultadoDijkstra rutaMasCorta(String idOrigen, String idDestino) {
        Estacion origen = estaciones.get(idOrigen);
        Estacion destino = estaciones.get(idDestino);
        
        if (origen == null || destino == null) {
            return new ResultadoDijkstra(Double.MAX_VALUE, new Lista<>());
        }
        
        Lista<Estacion> todos = getTodasEstaciones();
        int n = todos.getTamaño();
        Estacion[] nodos = new Estacion[n];
        double[] dist = new double[n];
        int[] prev = new int[n];
        boolean[] visitado = new boolean[n];
        
        for (int i = 0; i < n; i++) {
            nodos[i] = todos.obtener(i);
            dist[i] = Double.MAX_VALUE;
            prev[i] = -1;
            visitado[i] = false;
            if (nodos[i].equals(origen)) dist[i] = 0;
        }
        
        for (int count = 0; count < n - 1; count++) {
            double min = Double.MAX_VALUE;
            int u = -1;
            for (int i = 0; i < n; i++) {
                if (!visitado[i] && dist[i] < min && nodos[i].isActiva()) {
                    min = dist[i];
                    u = i;
                }
            }
            if (u == -1) break;
            visitado[u] = true;
            
            Lista<Arista> aristasU = nodos[u].getAristas();
            for (int i = 0; i < aristasU.getTamaño(); i++) {
                Arista a = aristasU.obtener(i);
                if (!a.isActiva()) continue;
                Estacion vecino = a.getOtroNodo(nodos[u]);
                if (vecino == null || !vecino.isActiva()) continue;
                
                int v = -1;
                for (int j = 0; j < n; j++) {
                    if (nodos[j].equals(vecino)) { v = j; break; }
                }
                
                if (v != -1 && !visitado[v] && dist[u] + a.getTiempo() < dist[v]) {
                    dist[v] = dist[u] + a.getTiempo();
                    prev[v] = u;
                }
            }
        }
        
        int idxDest = -1;
        for (int i = 0; i < n; i++) {
            if (nodos[i].equals(destino)) { idxDest = i; break; }
        }
        
        if (idxDest == -1 || dist[idxDest] == Double.MAX_VALUE) {
            return new ResultadoDijkstra(Double.MAX_VALUE, new Lista<>());
        }
        
        Lista<Estacion> ruta = new Lista<>();
        int actual = idxDest;
        while (actual != -1) {
            ruta.agregarAlInicio(nodos[actual]);
            actual = prev[actual];
        }
        
        return new ResultadoDijkstra(dist[idxDest], ruta);
    }
    
    /**
     * Identifica los hubs (estaciones con más conexiones).
     * 
     * @param topN Número de hubs a retornar
     * @return Lista de estaciones ordenadas por grado
     */
    public Lista<Estacion> identificarHubs(int topN) {
        Lista<Estacion> todas = getTodasEstaciones();
        int n = todas.getTamaño();
        Estacion[] nodos = new Estacion[n];
        int[] grados = new int[n];
        
        for (int i = 0; i < n; i++) {
            nodos[i] = todas.obtener(i);
            grados[i] = nodos[i].getGrado();
        }
        
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (grados[j] < grados[j + 1]) {
                    int tempG = grados[j]; grados[j] = grados[j + 1]; grados[j + 1] = tempG;
                    Estacion tempN = nodos[j]; nodos[j] = nodos[j + 1]; nodos[j + 1] = tempN;
                }
            }
        }
        
        Lista<Estacion> hubs = new Lista<>();
        int limite = Math.min(topN, n);
        for (int i = 0; i < limite; i++) {
            hubs.agregar(nodos[i]);
        }
        return hubs;
    }
    
    /**
     * Elimina todas las estaciones y aristas del grafo.
     */
    public void vaciar() {
        estaciones.limpiar();
        aristas.vaciar();
    }
    
    /**
     * Obtiene el número de estaciones.
     * @return Cantidad de estaciones
     */
    public int getNumEstaciones() { 
        return estaciones.getTamanio(); 
    }
    
    /**
     * Obtiene el número de aristas.
     * @return Cantidad de conexiones
     */
    public int getNumAristas() { 
        return aristas.getTamaño(); 
    }
    
    /**
     * Clase para encapsular el resultado del algoritmo de Dijkstra.
     */
    public static class ResultadoDijkstra {
        private double distancia;
        private Lista<Estacion> ruta;
        
        /**
         * Constructor del resultado.
         * @param distancia Distancia total
         * @param ruta Lista de estaciones en la ruta
         */
        public ResultadoDijkstra(double distancia, Lista<Estacion> ruta) {
            this.distancia = distancia;
            this.ruta = ruta;
        }
        
        /**
         * Obtiene la distancia total.
         * @return Distancia
         */
        public double getDistancia() { 
            return distancia; 
        }
        
        /**
         * Obtiene la ruta.
         * @return Lista de estaciones
         */
        public Lista<Estacion> getRuta() { 
            return ruta; 
        }
        
        /**
         * Verifica si hay ruta disponible.
         * @return true si hay ruta
         */
        public boolean hayRuta() { 
            return distancia < Double.MAX_VALUE && ruta.getTamaño() > 0; 
        }
    }
}
