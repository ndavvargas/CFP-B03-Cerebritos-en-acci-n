import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Generador de archivos planos para simulación y monitoreo de redes inalámbricas.
 * Crea catálogos maestros de dispositivos, puntos de acceso y registros de conexiones.
 */
public class GenerateInfoFiles {

    private static final String OUTPUT_FOLDER = "datos_generados";

    private static final String[] LOCATIONS = {
        "Biblioteca_Central;Bloque_A",
        "Cafeteria_Principal;Bloque_B",
        "Laboratorio_Sistemas;Bloque_C",
        "Auditorio_Mayor;Bloque_A",
        "Salas_de_Estudio;Bloque_B",
        "Edificio_Administrativo;Bloque_D",
        "Plaza_Central;Zona_Abierta"
    };

    private static final String[][] DEVICE_TYPES = {
        {"DEV01", "Laptop_Docente", "60", "Alta"},
        {"DEV02", "Smartphone_Estudiante", "25", "Media"},
        {"DEV03", "Tablet_Laboratorio", "20", "Media"},
        {"DEV04", "SmartTV_Auditorio", "80", "Alta"},
        {"DEV05", "Sensor_IoT_Ambiente", "5", "Baja"},
        {"DEV06", "Terminal_Impresion", "10", "Baja"},
        {"DEV07", "Laptop_Estudiante", "45", "Media"}
    };

    private static final Random RANDOM = new Random();

    public static void main(String[] args) {
        try {
            createOutputFolder();

            int deviceTypesCount = 5;
            int accessPointsCount = 5;

            createDeviceTypesFile(deviceTypesCount);
            createAccessPointsFile(accessPointsCount);

            for (int i = 1; i <= accessPointsCount; i++) {
                String apId = String.format("AP%03d", i);
                String locationInfo = LOCATIONS[(i - 1) % LOCATIONS.length].split(";")[0];
                int connectionCount = 25 + RANDOM.nextInt(26);

                createConnectionsFile(connectionCount, apId, locationInfo);
            }

            System.out.println("Generación de archivos finalizada exitosamente.");
            System.out.println("Archivos generados en: " + OUTPUT_FOLDER);

        } catch (Exception e) {
            System.err.println("Error durante la generación de archivos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Genera el catálogo de tipos de dispositivos.
     * Formato: IDDispositivo;TipoDispositivo;AnchoBandaMbps;PrioridadQoS
     */
    public static void createDeviceTypesFile(int deviceTypesCount) throws Exception {
        File file = new File(OUTPUT_FOLDER, "dispositivos.csv");

        try (PrintWriter writer = new PrintWriter(file, "UTF-8")) {
            int countToGenerate = Math.min(deviceTypesCount, DEVICE_TYPES.length);

            for (int i = 0; i < countToGenerate; i++) {
                String[] dev = DEVICE_TYPES[i];
                writer.println(dev[0] + ";" + dev[1] + ";" + dev[2] + ";" + dev[3]);
            }
        }
    }

    public static void createProductsFile(int productsCount) throws Exception {
        createDeviceTypesFile(productsCount);
    }

    /**
     * Genera el catálogo de Access Points.
     * Formato: TipoID;ID_AP;Ubicacion;BloqueZona
     */
    public static void createAccessPointsFile(int accessPointsCount) throws Exception {
        File file = new File(OUTPUT_FOLDER, "aps.csv");

        try (PrintWriter writer = new PrintWriter(file, "UTF-8")) {
            for (int i = 0; i < accessPointsCount; i++) {
                String apId = String.format("AP%03d", i + 1);
                String[] loc = LOCATIONS[i % LOCATIONS.length].split(";");
                writer.println("AP;" + apId + ";" + loc[0] + ";" + loc[1]);
            }
        }
    }

    public static void createSalesManInfoFile(int salesmanCount) throws Exception {
        createAccessPointsFile(salesmanCount);
    }

    /**
     * Genera el archivo de conexiones de un Access Point.
     * Formato:
     * AP;ID_AP;Ubicacion
     * IDDispositivo;DireccionMAC;ValorRSSI;
     */
    public static void createConnectionsFile(int connectionCount, String accessPointId, String location) throws Exception {
        File file = new File(OUTPUT_FOLDER, "conexiones_" + accessPointId + ".txt");
        List<String> registeredMacs = new ArrayList<String>();

        try (PrintWriter writer = new PrintWriter(file, "UTF-8")) {
            writer.println("AP;" + accessPointId + ";" + location);

            for (int i = 0; i < connectionCount; i++) {
                String deviceId = DEVICE_TYPES[RANDOM.nextInt(DEVICE_TYPES.length)][0];

                // Reutiliza MAC en ~25% de los registros para simular reconexiones
                boolean repeatMac = !registeredMacs.isEmpty() && RANDOM.nextInt(100) < 25;
                String macAddress;

                if (repeatMac) {
                    macAddress = registeredMacs.get(RANDOM.nextInt(registeredMacs.size()));
                } else {
                    macAddress = generateMacAddress();
                    registeredMacs.add(macAddress);
                }

                int rssi = -(30 + RANDOM.nextInt(61));
                writer.println(deviceId + ";" + macAddress + ";" + rssi + ";");
            }
        }
    }

    public static void createSalesMenFile(int randomSalesCount, String name, long id) throws Exception {
        createConnectionsFile(randomSalesCount, String.format("AP%03d", id), name);
    }

    private static String generateMacAddress() {
        StringBuilder mac = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            if (i > 0) {
                mac.append(":");
            }
            mac.append(String.format("%02X", RANDOM.nextInt(256)));
        }

        return mac.toString();
    }

    private static void createOutputFolder() throws Exception {
        File folder = new File(OUTPUT_FOLDER);

        if (!folder.exists() && !folder.mkdirs()) {
            throw new Exception("No fue posible crear la carpeta: " + OUTPUT_FOLDER);
        }
    }
}
