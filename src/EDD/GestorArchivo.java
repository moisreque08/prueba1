/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package EDD;

import EDD.*;
import javax.swing.*;
import java.io.*;

/**
 * Controlador para la carga y guardado de archivos CSV.
 * Utiliza JFileChooser para la selección de archivos.
 * @version 1.0
 */
public class GestorArchivo {
    private static File ultimoArchivoEstaciones;
    private static File ultimoArchivoConexiones;
    private static boolean cambiosPendientes = false;
    
    /**
     * Carga estaciones desde un archivo CSV.
     * Formato: ID,Nombre,Linea,Zona,Capacidad
     * 
     * @param grafo Grafo donde cargar
     * @param parent Ventana padre
     * @return true si se cargó correctamente
     */
    public static boolean cargarEstaciones(Grafo grafo, JFrame parent) {
        JFileChooser fc = new JFileChooser(".");
        fc.setDialogTitle("Seleccionar archivo de estaciones (CSV)");
        fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("CSV", "csv"));
        
        if (fc.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
            File archivo = fc.getSelectedFile();
            ultimoArchivoEstaciones = archivo;
            return procesarEstaciones(archivo, grafo, parent);
        }
        return false;
    }
    
    /**
     * Procesa el archivo de estaciones.
     */
    private static boolean procesarEstaciones(File archivo, Grafo grafo, JFrame parent) {
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            int lineas = 0, agregadas = 0;
            
            while ((linea = br.readLine()) != null) {
                lineas++;
                if (linea.trim().isEmpty()) continue;
                if (lineas == 1 && (linea.contains("ID") || linea.contains("Nombre"))) continue;
                
                String[] p = linea.split(",");
                if (p.length >= 5) {
                    try {
                        String id = p[0].trim();
                        String nombre = p[1].trim();
                        String lineaT = p[2].trim();
                        String zona = p[3].trim();
                        int capacidad = Integer.parseInt(p[4].trim());
                        
                        if (grafo.agregarEstacion(id, nombre, lineaT, zona, capacidad)) {
                            agregadas++;
                        }
                    } catch (NumberFormatException e) {
                        
                    }
                }
            }
            
            JOptionPane.showMessageDialog(parent,
                "Estaciones cargadas:\n" +
                "Archivo: " + archivo.getName() + "\n" +
                "Agregadas: " + agregadas + "\n" +
                "Total: " + grafo.getNumEstaciones(),
                "Carga exitosa", JOptionPane.INFORMATION_MESSAGE);
            
            cambiosPendientes = false;
            return true;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(parent, "Error: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    /**
     * Carga conexiones desde un archivo CSV.
     * Formato: ID_Origen,ID_Destino,Tiempo
     * 
     * @param grafo Grafo donde cargar
     * @param parent Ventana padre
     * @return true si se cargó correctamente
     */
    public static boolean cargarConexiones(Grafo grafo, JFrame parent) {
        JFileChooser fc = new JFileChooser(".");
        fc.setDialogTitle("Seleccionar archivo de conexiones (CSV)");
        fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("CSV", "csv"));
        
        if (fc.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
            File archivo = fc.getSelectedFile();
            ultimoArchivoConexiones = archivo;
            return procesarConexiones(archivo, grafo, parent);
        }
        return false;
    }
    
    /**
     * Procesa el archivo de conexiones.
     */
    private static boolean procesarConexiones(File archivo, Grafo grafo, JFrame parent) {
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            int lineas = 0, agregadas = 0, fallidas = 0;
            
            while ((linea = br.readLine()) != null) {
                lineas++;
                if (linea.trim().isEmpty()) continue;
                if (lineas == 1 && (linea.contains("Estacion") || linea.contains("Origen"))) continue;
                
                String[] p = linea.split(",");
                if (p.length >= 3) {
                    try {
                        String origen = p[0].trim();
                        String destino = p[1].trim();
                        double tiempo = Double.parseDouble(p[2].trim());
                        
                        if (grafo.agregarArista(origen, destino, tiempo)) {
                            agregadas++;
                        } else {
                            fallidas++;
                        }
                    } catch (NumberFormatException e) {
                        fallidas++;
                    }
                }
            }
            
            JOptionPane.showMessageDialog(parent,
                "Conexiones cargadas:\n" +
                "Archivo: " + archivo.getName() + "\n" +
                "Agregadas: " + agregadas + "\n" +
                "Fallidas: " + fallidas + "\n" +
                "Total aristas: " + grafo.getNumAristas(),
                "Carga exitosa", JOptionPane.INFORMATION_MESSAGE);
            
            cambiosPendientes = false;
            return true;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(parent, "Error: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    /**
     * Guarda las estaciones en un archivo CSV.
     * 
     * @param grafo Grafo a guardar
     * @param parent Ventana padre
     * @return true si se guardó correctamente
     */
    public static boolean guardarEstaciones(Grafo grafo, JFrame parent) {
        JFileChooser fc = new JFileChooser(".");
        fc.setDialogTitle("Guardar estaciones");
        fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("CSV", "csv"));
        if (ultimoArchivoEstaciones != null) fc.setSelectedFile(ultimoArchivoEstaciones);
        
        if (fc.showSaveDialog(parent) == JFileChooser.APPROVE_OPTION) {
            File f = fc.getSelectedFile();
            if (!f.getName().toLowerCase().endsWith(".csv"))
                f = new File(f.getAbsolutePath() + ".csv");
            
            try (PrintWriter pw = new PrintWriter(new FileWriter(f))) {
                pw.println("ID_Estacion,Nombre,Linea,Zona,Capacidad_Pasajeros_Hora");
                Lista<Estacion> estaciones = grafo.getTodasEstaciones();
                for (int i = 0; i < estaciones.getTamaño(); i++) {
                    Estacion e = estaciones.obtener(i);
                    pw.println(e.getId() + "," + e.getNombre() + "," + e.getLinea() + 
                              "," + e.getZona() + "," + e.getCapacidad());
                }
                ultimoArchivoEstaciones = f;
                cambiosPendientes = false;
                JOptionPane.showMessageDialog(parent, "Estaciones guardadas: " + f.getName(),
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
                return true;
            } catch (IOException e) {
                JOptionPane.showMessageDialog(parent, "Error: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        return false;
    }
    
    /**
     * Guarda las conexiones en un archivo CSV.
     * 
     * @param grafo Grafo a guardar
     * @param parent Ventana padre
     * @return true si se guardó correctamente
     */
    public static boolean guardarConexiones(Grafo grafo, JFrame parent) {
        JFileChooser fc = new JFileChooser(".");
        fc.setDialogTitle("Guardar conexiones");
        fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("CSV", "csv"));
        if (ultimoArchivoConexiones != null) fc.setSelectedFile(ultimoArchivoConexiones);
        
        if (fc.showSaveDialog(parent) == JFileChooser.APPROVE_OPTION) {
            File f = fc.getSelectedFile();
            if (!f.getName().toLowerCase().endsWith(".csv"))
                f = new File(f.getAbsolutePath() + ".csv");
            
            try (PrintWriter pw = new PrintWriter(new FileWriter(f))) {
                pw.println("Estacion_Origen,Estacion_Destino,Tiempo_Minutos");
                Lista<Arista> aristas = grafo.getAristas();
                for (int i = 0; i < aristas.getTamaño(); i++) {
                    Arista a = aristas.obtener(i);
                    pw.println(a.getOrigen().getId() + "," + a.getDestino().getId() + "," + a.getTiempo());
                }
                ultimoArchivoConexiones = f;
                cambiosPendientes = false;
                JOptionPane.showMessageDialog(parent, "Conexiones guardadas: " + f.getName(),
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
                return true;
            } catch (IOException e) {
                JOptionPane.showMessageDialog(parent, "Error: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        return false;
    }
    
    /**
     * Carga los archivos por defecto si existen.
     * 
     * @param grafo Grafo donde cargar
     * @param parent Ventana padre
     * @return true si se cargó al menos un archivo
     */
    public static boolean cargarArchivosPorDefecto(Grafo grafo, JFrame parent) {
        File fEst = new File("diccionario.csv");
        File fCon = new File("grafo.csv");
        boolean ok = false;
        if (fEst.exists()) {
            ultimoArchivoEstaciones = fEst;
            procesarEstaciones(fEst, grafo, parent);
            ok = true;
        }
        if (fCon.exists()) {
            ultimoArchivoConexiones = fCon;
            procesarConexiones(fCon, grafo, parent);
            ok = true;
        }
        return ok;
    }
    
    /**
     * Marca que hay cambios pendientes.
     */
    public static void marcarCambios() { 
        cambiosPendientes = true; 
    }
    
    /**
     * Verifica si hay cambios pendientes.
     * @return true si hay cambios sin guardar
     */
    public static boolean hayCambiosPendientes() { 
        return cambiosPendientes; 
    }
    
    /**
     * Obtiene el nombre del último archivo de estaciones.
     * @return Nombre del archivo o "Ninguno"
     */
    public static String getNombreArchivoEstaciones() {
        return ultimoArchivoEstaciones != null ? ultimoArchivoEstaciones.getName() : "Ninguno";
    }
    
    /**
     * Obtiene el nombre del último archivo de conexiones.
     * @return Nombre del archivo o "Ninguno"
     */
    public static String getNombreArchivoConexiones() {
        return ultimoArchivoConexiones != null ? ultimoArchivoConexiones.getName() : "Ninguno";
    }
}

