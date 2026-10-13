import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Segunda clase con metodo main del proyecto. Procesa los catalogos y registros
 * de conexiones generados para producir los reportes consolidados y ordenados.
 *
 * @author Cristian Aguirre Ramirez
 * @author Angie Leguizamon Buitrago
 * @author Yennifer Ofelia Malaguera Vasco
 * @author Nelson David Vargas Lopez
 * @author Sonia Liliana Cruz Reyes
 * @version 3.0 (Entrega final)
 */
public class main {

    private static final String DATA_FOLDER = "datos_generados";
    private static final String DEVICES_FILE = "dispositivos.csv";
    private static final String ACCESS_POINTS_FILE = "aps.csv";
    private static final String ACCESS_POINTS_REPORT = "reporte_access_points.csv";
    private static final String DEVICES_REPORT = "reporte_dispositivos.csv";

    private static final int MIN_RSSI = -120;
    private static final int MAX_RSSI = 0;
    private static final Pattern MAC_PATTERN =
            Pattern.compile("^([0-9A-Fa-f]{2}:){5}[0-9A-Fa-f]{2}$");

    private static final DecimalFormat NUMBER_FORMAT =
            new DecimalFormat("0.##", new DecimalFormatSymbols(Locale.US));

    private main() {
    }

    /**
     * Punto de entrada principal. Lee catalogos, totaliza consumos y genera reportes.
     *
     * @param args argumentos de linea de comandos (no utilizados).
     */
    public static void main(String[] args) {
        try {
            Map<String, DeviceType> devices = readDeviceTypes();
            List<AccessPoint> accessPoints = readAccessPoints();

            Map<String, Double> bandwidthByAccessPoint = new HashMap<String, Double>();
            Map<String, Integer> connectionsByDeviceId = new HashMap<String, Integer>();

            for (AccessPoint accessPoint : accessPoints) {
                double totalBandwidth = processAccessPoint(accessPoint.getId(), devices, connectionsByDeviceId);
                bandwidthByAccessPoint.put(accessPoint.getId(), totalBandwidth);
            }

            writeAccessPointsReport(accessPoints, bandwidthByAccessPoint);
            writeDevicesReport(devices, connectionsByDeviceId);

            System.out.println("============================================");
            System.out.println("REPORTES GENERADOS EXITOSAMENTE");
            System.out.println("Archivos creados en la carpeta: " + DATA_FOLDER);
            System.out.println("============================================");
        } catch (IOException error) {
            System.err.println("ERROR: No fue posible generar los reportes.");
            System.err.println("Detalle: " + error.getMessage());
        }
    }

    /**
     * Carga el catalogo de dispositivos desde {@code dispositivos.csv}.
     *
     * @return mapa de ID de dispositivo a su objeto {@link DeviceType}.
     * @throws IOException si ocurre un error de lectura.
     */
    private static Map<String, DeviceType> readDeviceTypes() throws IOException {
        Map<String, DeviceType> devices = new HashMap<String, DeviceType>();
        File file = requireFile(DEVICES_FILE);

        BufferedReader reader = new BufferedReader(new FileReader(file));
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] fields = line.split(";");
                if (fields.length < 4) {
                    warn("linea con formato incorrecto en " + DEVICES_FILE + ": " + line);
                    continue;
                }

                String id = fields[0].trim();
                String type = fields[1].trim();
                double bandwidth = parseBandwidth(fields[2].trim(), line);

                devices.put(id, new DeviceType(id, type, bandwidth));
            }
        } finally {
            reader.close();
        }

        return devices;
    }

    /**
     * Carga el catalogo de Access Points desde {@code aps.csv}.
     *
     * @return lista de objetos {@link AccessPoint}.
     * @throws IOException si ocurre un error de lectura.
     */
    private static List<AccessPoint> readAccessPoints() throws IOException {
        List<AccessPoint> accessPoints = new ArrayList<AccessPoint>();
        File file = requireFile(ACCESS_POINTS_FILE);

        BufferedReader reader = new BufferedReader(new FileReader(file));
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] fields = line.split(";");
                if (fields.length < 4) {
                    warn("linea con formato incorrecto en " + ACCESS_POINTS_FILE + ": " + line);
                    continue;
                }

                accessPoints.add(new AccessPoint(fields[1].trim(), fields[2].trim()));
            }
        } finally {
            reader.close();
        }

        return accessPoints;
    }

    /**
     * Procesa todos los archivos de conexiones asociados a un Access Point.
     *
     * @param accessPointId identificador del Access Point.
     * @param devices catalogo de dispositivos.
     * @param connectionsByDeviceId acumulador de conexiones por dispositivo.
     * @return total de ancho de banda consumido en Mbps.
     */
    private static double processAccessPoint(final String accessPointId, Map<String, DeviceType> devices,
            Map<String, Integer> connectionsByDeviceId) {

        File[] files = new File(DATA_FOLDER).listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                String exactName = "conexiones_" + accessPointId + ".txt";
                String prefix = "conexiones_" + accessPointId + "_";
                return name.equals(exactName) || (name.startsWith(prefix) && name.endsWith(".txt"));
            }
        });

        if (files == null || files.length == 0) {
            warn("no se encontro ningun archivo de conexiones del AP " + accessPointId
                    + ". Se asumira 0 Mbps consumidos.");
            return 0;
        }

        Arrays.sort(files);

        double totalBandwidth = 0;
        for (File file : files) {
            totalBandwidth += processConnectionsFile(file, devices, connectionsByDeviceId);
        }

        return totalBandwidth;
    }

    /**
     * Lee y valida un archivo de conexiones, acumulando consumo y frecuencia de uso.
     */
    private static double processConnectionsFile(File file, Map<String, DeviceType> devices,
            Map<String, Integer> connectionsByDeviceId) {

        double totalBandwidth = 0;
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(file));
            reader.readLine(); // Encabezado: AP;ID_AP;Ubicacion

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] fields = line.split(";");
                if (fields.length < 3) {
                    warn("linea con formato incorrecto en " + file.getName() + ": " + line);
                    continue;
                }

                String deviceId = fields[0].trim();
                DeviceType device = devices.get(deviceId);
                if (device == null) {
                    warn("dispositivo inexistente (" + deviceId + ") en " + file.getName() + ": " + line);
                    continue;
                }

                if (!MAC_PATTERN.matcher(fields[1].trim()).matches()) {
                    warn("direccion MAC invalida en " + file.getName() + ": " + line);
                    continue;
                }

                if (!isValidRssi(fields[2].trim())) {
                    warn("valor RSSI invalido en " + file.getName() + ": " + line);
                    continue;
                }

                totalBandwidth += device.getBandwidth();

                Integer current = connectionsByDeviceId.get(deviceId);
                connectionsByDeviceId.put(deviceId, (current == null ? 0 : current) + 1);
            }
        } catch (IOException error) {
            warn("error al leer " + file.getName() + ": " + error.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ignored) {
                }
            }
        }

        return totalBandwidth;
    }

    /**
     * Genera {@code reporte_access_points.csv} ordenado descendentemente por consumo total.
     */
    private static void writeAccessPointsReport(List<AccessPoint> accessPoints,
            final Map<String, Double> bandwidthByAccessPoint) throws IOException {

        Collections.sort(accessPoints, new Comparator<AccessPoint>() {
            @Override
            public int compare(AccessPoint first, AccessPoint second) {
                int byBandwidth = Double.compare(bandwidthByAccessPoint.get(second.getId()),
                        bandwidthByAccessPoint.get(first.getId()));
                return (byBandwidth != 0) ? byBandwidth : first.getId().compareTo(second.getId());
            }
        });

        PrintWriter writer = new PrintWriter(new File(DATA_FOLDER, ACCESS_POINTS_REPORT), "UTF-8");
        try {
            for (AccessPoint accessPoint : accessPoints) {
                writer.println(accessPoint.getId() + ";" + accessPoint.getLocation() + ";"
                        + NUMBER_FORMAT.format(bandwidthByAccessPoint.get(accessPoint.getId())));
            }
        } finally {
            writer.close();
        }
    }

    /**
     * Genera {@code reporte_dispositivos.csv} ordenado por cantidad de conexiones.
     */
    private static void writeDevicesReport(Map<String, DeviceType> devices,
            final Map<String, Integer> connectionsByDeviceId) throws IOException {

        List<DeviceType> orderedDevices = new ArrayList<DeviceType>(devices.values());

        Collections.sort(orderedDevices, new Comparator<DeviceType>() {
            @Override
            public int compare(DeviceType first, DeviceType second) {
                int byConnections = Integer.compare(getConnections(second.getId()), getConnections(first.getId()));
                return (byConnections != 0) ? byConnections : first.getType().compareTo(second.getType());
            }

            private int getConnections(String deviceId) {
                Integer total = connectionsByDeviceId.get(deviceId);
                return (total == null) ? 0 : total;
            }
        });

        PrintWriter writer = new PrintWriter(new File(DATA_FOLDER, DEVICES_REPORT), "UTF-8");
        try {
            for (DeviceType device : orderedDevices) {
                writer.println(device.getType() + ";" + NUMBER_FORMAT.format(device.getBandwidth()));
            }
        } finally {
            writer.close();
        }
    }

    private static File requireFile(String fileName) throws IOException {
        File file = new File(DATA_FOLDER, fileName);
        if (!file.exists()) {
            throw new IOException("No se encontro el archivo " + fileName
                    + " en la carpeta " + DATA_FOLDER + ". Ejecute primero GenerateInfoFiles.");
        }
        return file;
    }

    private static double parseBandwidth(String rawValue, String originalLine) {
        try {
            double value = Double.parseDouble(rawValue);
            if (value < 0) {
                warn("ancho de banda negativo, se asume 0 en: " + originalLine);
                return 0;
            }
            return value;
        } catch (NumberFormatException error) {
            warn("ancho de banda invalido, se asume 0 en: " + originalLine);
            return 0;
        }
    }

    private static boolean isValidRssi(String rawValue) {
        try {
            int value = Integer.parseInt(rawValue);
            return value >= MIN_RSSI && value <= MAX_RSSI;
        } catch (NumberFormatException error) {
            return false;
        }
    }

    private static void warn(String message) {
        System.err.println("AVISO: " + message);
    }

    private static class DeviceType {
        private final String id;
        private final String type;
        private final double bandwidth;

        DeviceType(String id, String type, double bandwidth) {
            this.id = id;
            this.type = type;
            this.bandwidth = bandwidth;
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
    }

    private static class AccessPoint {
        private final String id;
        private final String location;

        AccessPoint(String id, String location) {
            this.id = id;
            this.location = location;
        }

        String getId() {
            return id;
        }

        String getLocation() {
            return location;
        }
    }
}
