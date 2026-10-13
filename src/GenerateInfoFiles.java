import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Generador de archivos planos pseudoaleatorios de entrada para el sistema
 * de monitoreo de redes inalambricas.
 *
 * @author Cristian Aguirre Ramirez
 * @author Angie Leguizamon Buitrago
 * @author Yennifer Ofelia Malaguera Vasco
 * @author Nelson David Vargas Lopez
 * @author Sonia Liliana Cruz Reyes
 * @version 3.0 (Entrega final)
 */
public class GenerateInfoFiles {

    private static final String OUTPUT_FOLDER = "datos_generados";
    private static final String DEVICES_FILE = "dispositivos.csv";
    private static final String ACCESS_POINTS_FILE = "aps.csv";

    private static final int DEFAULT_ACCESS_POINTS_COUNT = 5;
    private static final int MAX_FILES_PER_ACCESS_POINT = 3;
    private static final int MIN_CONNECTIONS_PER_FILE = 15;
    private static final int MAX_CONNECTIONS_PER_FILE = 35;
    private static final int MAC_REUSE_PERCENTAGE = 25;

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
        {"Laptop_Docente", "60", "Alta"},
        {"Smartphone_Estudiante", "25", "Media"},
        {"Tablet_Laboratorio", "20", "Media"},
        {"SmartTV_Auditorio", "80", "Alta"},
        {"Sensor_IoT_Ambiente", "5", "Baja"},
        {"Terminal_Impresion", "10", "Baja"},
        {"Laptop_Estudiante", "45", "Media"}
    };

    private static final Random RANDOM = new Random();
    private static final List<String> DEVICE_IDS = new ArrayList<String>();

    private GenerateInfoFiles() {
    }

    /**
     * Punto de entrada principal. Genera catalogos y archivos de conexiones.
     *
     * @param args argumentos de linea de comandos (no utilizados).
     */
    public static void main(String[] args) {
        try {
            createOutputFolder();
            removePreviousConnectionFiles();

            createDeviceTypesFile(DEVICE_TYPES.length);
            createAccessPointsFile(DEFAULT_ACCESS_POINTS_COUNT);

            for (int index = 1; index <= DEFAULT_ACCESS_POINTS_COUNT; index++) {
                String accessPointId = buildAccessPointId(index);
                String location = getLocationName(index);
                int filesCount = 1 + RANDOM.nextInt(MAX_FILES_PER_ACCESS_POINT);

                for (int fileNumber = 1; fileNumber <= filesCount; fileNumber++) {
                    int connectionCount = MIN_CONNECTIONS_PER_FILE
                            + RANDOM.nextInt(MAX_CONNECTIONS_PER_FILE - MIN_CONNECTIONS_PER_FILE + 1);

                    createConnectionsFile(connectionCount, accessPointId, location, fileNumber);
                }
            }

            System.out.println("============================================");
            System.out.println("GENERACION FINALIZADA EXITOSAMENTE");
            System.out.println("Archivos creados en la carpeta: " + OUTPUT_FOLDER);
            System.out.println("============================================");
        } catch (IOException | IllegalArgumentException | IllegalStateException error) {
            System.err.println("ERROR: No fue posible generar los archivos.");
            System.err.println("Detalle: " + error.getMessage());
        }
    }

    /**
     * Genera el archivo {@code dispositivos.csv} con el catalogo de dispositivos.
     *
     * @param deviceTypesCount cantidad de tipos de dispositivo a generar.
     * @throws IOException si ocurre un error de escritura.
     */
    public static void createDeviceTypesFile(int deviceTypesCount) throws IOException {
        if (deviceTypesCount < 1) {
            throw new IllegalArgumentException("La cantidad de tipos de dispositivo debe ser mayor que cero.");
        }

        createOutputFolder();
        DEVICE_IDS.clear();

        File file = new File(OUTPUT_FOLDER, DEVICES_FILE);
        PrintWriter writer = new PrintWriter(file, "UTF-8");
        try {
            for (int i = 0; i < deviceTypesCount; i++) {
                String[] baseType = DEVICE_TYPES[i % DEVICE_TYPES.length];
                int round = i / DEVICE_TYPES.length;

                String deviceId = String.format("DEV%02d", i + 1);
                String typeName = (round == 0) ? baseType[0] : baseType[0] + "_" + (round + 1);

                DEVICE_IDS.add(deviceId);
                writer.println(deviceId + ";" + typeName + ";" + baseType[1] + ";" + baseType[2]);
            }
        } finally {
            writer.close();
        }
    }

    /**
     * Metodo de compatibilidad con la guia: genera el catalogo de productos/dispositivos.
     *
     * @param productsCount cantidad de elementos a generar.
     * @throws IOException si ocurre un error de escritura.
     */
    public static void createProductsFile(int productsCount) throws IOException {
        createDeviceTypesFile(productsCount);
    }

    /**
     * Genera el archivo {@code aps.csv} con el catalogo de Access Points.
     *
     * @param accessPointsCount cantidad de Access Points a generar.
     * @throws IOException si ocurre un error de escritura.
     */
    public static void createAccessPointsFile(int accessPointsCount) throws IOException {
        if (accessPointsCount < 1) {
            throw new IllegalArgumentException("La cantidad de Access Points debe ser mayor que cero.");
        }

        createOutputFolder();

        File file = new File(OUTPUT_FOLDER, ACCESS_POINTS_FILE);
        PrintWriter writer = new PrintWriter(file, "UTF-8");
        try {
            for (int index = 1; index <= accessPointsCount; index++) {
                String block = LOCATIONS[(index - 1) % LOCATIONS.length].split(";")[1];
                writer.println("AP;" + buildAccessPointId(index) + ";" + getLocationName(index) + ";" + block);
            }
        } finally {
            writer.close();
        }
    }

    /**
     * Metodo de compatibilidad con la guia: genera informacion de vendedores/Access Points.
     *
     * @param salesmanCount cantidad de Access Points a generar.
     * @throws IOException si ocurre un error de escritura.
     */
    public static void createSalesManInfoFile(int salesmanCount) throws IOException {
        createAccessPointsFile(salesmanCount);
    }

    /**
     * Genera el primer archivo de conexiones de un Access Point.
     *
     * @param connectionCount cantidad de conexiones a registrar.
     * @param accessPointId identificador del Access Point.
     * @param location ubicacion del Access Point.
     * @throws IOException si ocurre un error de escritura.
     */
    public static void createConnectionsFile(int connectionCount, String accessPointId, String location)
            throws IOException {
        createConnectionsFile(connectionCount, accessPointId, location, 1);
    }

    /**
     * Genera un archivo de conexiones para un Access Point especifico.
     *
     * @param connectionCount cantidad de conexiones a registrar.
     * @param accessPointId identificador del Access Point.
     * @param location ubicacion del Access Point.
     * @param fileNumber numero de archivo para permitir multiples archivos por nodo.
     * @throws IOException si ocurre un error de escritura.
     */
    public static void createConnectionsFile(int connectionCount, String accessPointId,
            String location, int fileNumber) throws IOException {

        if (connectionCount < 1) {
            throw new IllegalArgumentException("La cantidad de conexiones debe ser mayor que cero.");
        }
        if (accessPointId == null || accessPointId.trim().isEmpty()) {
            throw new IllegalArgumentException("El identificador del Access Point no puede estar vacio.");
        }
        if (location == null || location.trim().isEmpty()) {
            throw new IllegalArgumentException("La ubicacion del Access Point no puede estar vacia.");
        }
        if (fileNumber < 1) {
            throw new IllegalArgumentException("El numero de archivo debe ser mayor o igual a uno.");
        }
        if (DEVICE_IDS.isEmpty()) {
            throw new IllegalStateException("Primero debe generarse el catalogo de dispositivos.");
        }

        createOutputFolder();

        String fileName = "conexiones_" + accessPointId.trim()
                + (fileNumber == 1 ? "" : "_" + fileNumber) + ".txt";
        File file = new File(OUTPUT_FOLDER, fileName);

        List<String> registeredMacs = new ArrayList<String>();
        PrintWriter writer = new PrintWriter(file, "UTF-8");
        try {
            writer.println("AP;" + accessPointId.trim() + ";" + location.trim());

            for (int i = 0; i < connectionCount; i++) {
                String deviceId = DEVICE_IDS.get(RANDOM.nextInt(DEVICE_IDS.size()));
                String macAddress = pickMacAddress(registeredMacs);
                int rssi = -(30 + RANDOM.nextInt(61));

                writer.println(deviceId + ";" + macAddress + ";" + rssi + ";");
            }
        } finally {
            writer.close();
        }
    }

    /**
     * Metodo de compatibilidad con la guia: genera ventas/conexiones de un nodo.
     *
     * @param randomSalesCount cantidad de conexiones a registrar.
     * @param name ubicacion del Access Point.
     * @param id identificador numerico del Access Point.
     * @throws IOException si ocurre un error de escritura.
     */
    public static void createSalesMenFile(int randomSalesCount, String name, long id) throws IOException {
        createConnectionsFile(randomSalesCount, String.format("AP%03d", id), name);
    }

    private static String pickMacAddress(List<String> registeredMacs) {
        boolean reuseMac = !registeredMacs.isEmpty() && RANDOM.nextInt(100) < MAC_REUSE_PERCENTAGE;
        if (reuseMac) {
            return registeredMacs.get(RANDOM.nextInt(registeredMacs.size()));
        }

        String newMac = generateMacAddress();
        registeredMacs.add(newMac);
        return newMac;
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

    private static String buildAccessPointId(int index) {
        return String.format("AP%03d", index);
    }

    private static String getLocationName(int index) {
        String baseName = LOCATIONS[(index - 1) % LOCATIONS.length].split(";")[0];
        int round = (index - 1) / LOCATIONS.length;
        return (round == 0) ? baseName : baseName + "_" + (round + 1);
    }

    /**
     * Elimina registros de conexiones de ejecuciones anteriores para evitar datos huerfanos.
     *
     * @throws IOException si no es posible listar o borrar los archivos.
     */
    private static void removePreviousConnectionFiles() throws IOException {
        File folder = new File(OUTPUT_FOLDER);
        File[] previous = folder.listFiles();
        if (previous == null) {
            throw new IOException("No fue posible consultar la carpeta: " + OUTPUT_FOLDER);
        }
        for (File file : previous) {
            if (file.isFile() && file.getName().matches("conexiones_AP[0-9]{3}(?:_[0-9]+)?\\.txt")
                    && !file.delete()) {
                throw new IOException("No fue posible eliminar archivo anterior: " + file.getName());
            }
        }
    }

    private static void createOutputFolder() throws IOException {
        File folder = new File(OUTPUT_FOLDER);
        if (!folder.exists() && !folder.mkdirs()) {
            throw new IOException("No fue posible crear la carpeta: " + OUTPUT_FOLDER);
        }
    }
}
