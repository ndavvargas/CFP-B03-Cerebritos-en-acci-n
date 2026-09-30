import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Segunda clase con metodo main del proyecto. Procesa los archivos generados
 * por {@link GenerateInfoFiles} (catalogo de dispositivos, catalogo de
 * Access Points y registros de conexion por Access Point) y produce los dos
 * reportes exigidos por la rubrica del proyecto (puntos 3 y 4), adaptados al
 * dominio de Redes Inalambricas:
 *
 * <ul>
 *   <li><b>reporte_access_points.csv</b> (equivalente al reporte de
 *   vendedores): cada Access Point con el total de ancho de banda
 *   consumido por todas sus conexiones, ordenado de mayor a menor
 *   (el "dinero recaudado" de este dominio).</li>
 *   <li><b>reporte_dispositivos.csv</b> (equivalente al reporte de
 *   productos): tipo de dispositivo y su ancho de banda, ordenados
 *   -sin mostrarla- por la cantidad total de conexiones que ese tipo
 *   registro en toda la red, de mayor a menor.</li>
 * </ul>
 *
 * <p>El programa no solicita ningun dato al usuario. Si una linea de
 * entrada tiene formato incorrecto, referencia un dispositivo inexistente,
 * o falta el archivo de conexiones de algun Access Point, se registra un
 * aviso por consola y se continua con el resto (no se detiene el
 * procesamiento completo por un solo dato invalido).</p>
 *
 * @author Sonia Cruz Reyes
 * @version 1.0 (Entrega 2 - version preliminar del proyecto completo)
 */
public class main {

    /** Carpeta donde estan los archivos de entrada y donde se dejan los reportes. */
    private static final String DATA_FOLDER = "datos_generados";

    /** Nombre del archivo con el catalogo de dispositivos. */
    private static final String DEVICES_FILE = "dispositivos.csv";

    /** Nombre del archivo con el catalogo de Access Points. */
    private static final String ACCESS_POINTS_FILE = "aps.csv";

    /** Nombre del reporte final de Access Points (equivalente a vendedores). */
    private static final String ACCESS_POINTS_REPORT = "reporte_access_points.csv";

    /** Nombre del reporte final de dispositivos (equivalente a productos). */
    private static final String DEVICES_REPORT = "reporte_dispositivos.csv";

    /**
     * Punto de entrada del programa. Lee los catalogos y los archivos de
     * conexion generados por {@code GenerateInfoFiles}, calcula los reportes
     * y los escribe en disco. Muestra un mensaje de finalizacion exitosa o
     * de error, sin solicitar ningun dato al usuario.
     *
     * @param args argumentos de linea de comandos (no utilizados).
     */
    public static void main(String[] args) {
        try {
            Map<String, DeviceType> devices = readDeviceTypes();
            List<AccessPointInfo> accessPoints = readAccessPoints();

            Map<String, Double> bandwidthByAccessPoint = new HashMap<String, Double>();
            Map<String, Integer> connectionsByDeviceType = new HashMap<String, Integer>();

            for (AccessPointInfo ap : accessPoints) {
                double totalBandwidth = processConnectionsFile(ap.getId(), devices, connectionsByDeviceType);
                bandwidthByAccessPoint.put(ap.getId(), totalBandwidth);
            }

            writeAccessPointsReport(accessPoints, bandwidthByAccessPoint);
            writeDevicesReport(devices, connectionsByDeviceType);

            System.out.println("==================================================");
            System.out.println("PROCESAMIENTO DE REPORTES FINALIZADO EXITOSAMENTE");
            System.out.println("Reportes generados en: " + DATA_FOLDER);
            System.out.println("==================================================");
        } catch (Exception error) {
            System.err.println("ERROR: No fue posible generar los reportes.");
            System.err.println("Detalle: " + error.getMessage());
        }
    }

    /**
     * Lee el catalogo de dispositivos ({@code dispositivos.csv}).
     * Formato de cada linea: {@code IDDispositivo;TipoDispositivo;AnchoBandaMbps;PrioridadQoS}.
     *
     * @return un mapa de IDDispositivo a su informacion completa.
     * @throws IOException si el archivo no existe o no puede leerse.
     */
    private static Map<String, DeviceType> readDeviceTypes() throws IOException {
        Map<String, DeviceType> devices = new HashMap<String, DeviceType>();
        File file = new File(DATA_FOLDER, DEVICES_FILE);

        if (!file.exists()) {
            throw new IOException("No se encontro el archivo " + DEVICES_FILE
                    + ". Ejecute primero GenerateInfoFiles.");
        }

        BufferedReader reader = new BufferedReader(new FileReader(file));
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] fields = line.split(";");
                if (fields.length < 4) {
                    System.err.println("AVISO: linea con formato incorrecto en " + DEVICES_FILE + ": " + line);
                    continue;
                }

                String id = fields[0].trim();
                String type = fields[1].trim();
                double bandwidth = parseBandwidth(fields[2].trim(), line);
                String priority = fields[3].trim();

                devices.put(id, new DeviceType(id, type, bandwidth, priority));
            }
        } finally {
            reader.close();
        }

        return devices;
    }

    /**
     * Convierte el texto de ancho de banda a un numero, validando que no
     * sea negativo ni tenga formato invalido (deteccion de informacion
     * incoherente).
     *
     * @param rawValue     el texto leido del archivo.
     * @param originalLine la linea completa, usada solo para el mensaje de aviso.
     * @return el valor numerico, o 0 si el dato era invalido o negativo.
     */
    private static double parseBandwidth(String rawValue, String originalLine) {
        try {
            double value = Double.parseDouble(rawValue);

            if (value < 0) {
                System.err.println("AVISO: ancho de banda negativo, se ajusta a 0 en: " + originalLine);
                return 0;
            }

            return value;
        } catch (NumberFormatException error) {
            System.err.println("AVISO: ancho de banda con formato invalido, se asume 0 en: " + originalLine);
            return 0;
        }
    }

    /**
     * Lee el catalogo de Access Points ({@code aps.csv}).
     * Formato de cada linea: {@code TipoID;ID_AP;Ubicacion;BloqueZona}.
     *
     * @return la lista de Access Points encontrados.
     * @throws IOException si el archivo no existe o no puede leerse.
     */
    private static List<AccessPointInfo> readAccessPoints() throws IOException {
        List<AccessPointInfo> accessPoints = new ArrayList<AccessPointInfo>();
        File file = new File(DATA_FOLDER, ACCESS_POINTS_FILE);

        if (!file.exists()) {
            throw new IOException("No se encontro el archivo " + ACCESS_POINTS_FILE
                    + ". Ejecute primero GenerateInfoFiles.");
        }

        BufferedReader reader = new BufferedReader(new FileReader(file));
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] fields = line.split(";");
                if (fields.length < 4) {
                    System.err.println("AVISO: linea con formato incorrecto en " + ACCESS_POINTS_FILE + ": " + line);
                    continue;
                }

                String id = fields[1].trim();
                String location = fields[2].trim();
                String block = fields[3].trim();

                accessPoints.add(new AccessPointInfo(id, location, block));
            }
        } finally {
            reader.close();
        }

        return accessPoints;
    }

    /**
     * Procesa el archivo de conexiones de un Access Point especifico
     * ({@code conexiones_<accessPointId>.txt}): valida cada linea contra el
     * catalogo de dispositivos y acumula, por tipo de dispositivo, el total
     * de conexiones registradas en toda la red (parametro de salida
     * {@code connectionsByDeviceType}).
     *
     * @param accessPointId           id del Access Point a procesar.
     * @param devices                 catalogo de dispositivos ya cargado.
     * @param connectionsByDeviceType mapa acumulador (TipoDispositivo -&gt;
     *                                total de conexiones); se actualiza
     *                                dentro de este metodo.
     * @return la suma del ancho de banda (Mbps) de todas las conexiones
     *         validas registradas en este Access Point.
     */
    private static double processConnectionsFile(String accessPointId, Map<String, DeviceType> devices,
            Map<String, Integer> connectionsByDeviceType) {

        File file = new File(DATA_FOLDER, "conexiones_" + accessPointId + ".txt");
        double totalBandwidth = 0;

        if (!file.exists()) {
            System.err.println("AVISO: no se encontro el archivo de conexiones del AP " + accessPointId
                    + ". Se asumira 0 Mbps consumidos para ese Access Point.");
            return 0;
        }

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            reader.readLine(); // Primera linea: AP;ID_AP;Ubicacion (no se necesita en este calculo).

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] fields = line.split(";");
                if (fields.length < 3) {
                    System.err.println("AVISO: linea con formato incorrecto en conexiones_"
                            + accessPointId + ".txt: " + line);
                    continue;
                }

                String deviceId = fields[0].trim();
                DeviceType device = devices.get(deviceId);

                if (device == null) {
                    System.err.println("AVISO: id de dispositivo inexistente (" + deviceId
                            + ") en conexiones_" + accessPointId + ".txt");
                    continue;
                }

                totalBandwidth += device.getBandwidth();

                Integer currentTotal = connectionsByDeviceType.get(device.getType());
                int newTotal = (currentTotal == null ? 0 : currentTotal) + 1;
                connectionsByDeviceType.put(device.getType(), newTotal);
            }
        } catch (IOException error) {
            System.err.println("AVISO: error al leer conexiones_" + accessPointId + ".txt: " + error.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ignored) {
                    // No es critico si falla el cierre de un archivo que ya se termino de leer.
                }
            }
        }

        return totalBandwidth;
    }

    /**
     * Genera {@code reporte_access_points.csv}, equivalente al reporte de
     * vendedores exigido por la rubrica. Formato de cada linea:
     * {@code ID_AP;Ubicacion;TotalAnchoBandaConsumidoMbps}, ordenado de
     * mayor a menor ancho de banda consumido.
     *
     * @param accessPoints           lista de Access Points (se ordena in-place).
     * @param bandwidthByAccessPoint mapa con el total de Mbps por Access Point.
     * @throws IOException si ocurre un error al escribir el archivo.
     */
    private static void writeAccessPointsReport(List<AccessPointInfo> accessPoints,
            final Map<String, Double> bandwidthByAccessPoint) throws IOException {

        Collections.sort(accessPoints, new Comparator<AccessPointInfo>() {
            @Override
            public int compare(AccessPointInfo primero, AccessPointInfo segundo) {
                double totalPrimero = bandwidthByAccessPoint.get(primero.getId());
                double totalSegundo = bandwidthByAccessPoint.get(segundo.getId());
                return Double.compare(totalSegundo, totalPrimero);
            }
        });

        File outputFile = new File(DATA_FOLDER, ACCESS_POINTS_REPORT);
        PrintWriter writer = new PrintWriter(outputFile, "UTF-8");
        try {
            for (AccessPointInfo ap : accessPoints) {
                double total = bandwidthByAccessPoint.get(ap.getId());
                writer.println(ap.getId() + ";" + ap.getLocation() + ";" + total);
            }
        } finally {
            writer.close();
        }
    }

    /**
     * Genera {@code reporte_dispositivos.csv}, equivalente al reporte de
     * productos exigido por la rubrica. Formato de cada linea:
     * {@code TipoDispositivo;AnchoBandaMbps} (sin mostrar la cantidad de
     * conexiones, tal como pide la rubrica para el reporte de productos),
     * ordenado -por dentro- de mayor a menor cantidad de conexiones totales
     * registradas en la red para ese tipo de dispositivo.
     *
     * @param devices                 catalogo de dispositivos.
     * @param connectionsByDeviceType mapa con el total de conexiones por tipo de dispositivo.
     * @throws IOException si ocurre un error al escribir el archivo.
     */
    private static void writeDevicesReport(Map<String, DeviceType> devices,
            final Map<String, Integer> connectionsByDeviceType) throws IOException {

        List<DeviceType> orderedDevices = new ArrayList<DeviceType>(devices.values());

        Collections.sort(orderedDevices, new Comparator<DeviceType>() {
            @Override
            public int compare(DeviceType primero, DeviceType segundo) {
                int totalPrimero = getTotalConnections(primero.getType());
                int totalSegundo = getTotalConnections(segundo.getType());
                return Integer.compare(totalSegundo, totalPrimero);
            }

            private int getTotalConnections(String deviceType) {
                Integer total = connectionsByDeviceType.get(deviceType);
                return (total == null ? 0 : total);
            }
        });

        File outputFile = new File(DATA_FOLDER, DEVICES_REPORT);
        PrintWriter writer = new PrintWriter(outputFile, "UTF-8");
        try {
            for (DeviceType device : orderedDevices) {
                writer.println(device.getType() + ";" + device.getBandwidth());
            }
        } finally {
            writer.close();
        }
    }

    /**
     * Representa un tipo de dispositivo del catalogo.
     */
    private static class DeviceType {

        private final String id;
        private final String type;
        private final double bandwidth;
        private final String priority;

        DeviceType(String id, String type, double bandwidth, String priority) {
            this.id = id;
            this.type = type;
            this.bandwidth = bandwidth;
            this.priority = priority;
        }

        String getId() {
            return id;
        }

        String getType() {
            return type;
        }

        double getBandwidth() {
            return bandwidth;
        }

        String getPriority() {
            return priority;
        }
    }

    /**
     * Representa un Access Point del catalogo.
     */
    private static class AccessPointInfo {

        private final String id;
        private final String location;
        private final String block;

        AccessPointInfo(String id, String location, String block) {
            this.id = id;
            this.location = location;
            this.block = block;
        }

        String getId() {
            return id;
        }

        String getLocation() {
            return location;
        }

        String getBlock() {
            return block;
        }
    }
}
