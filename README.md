# Entrega 2 - Semana 5

## Módulo: Conceptos Fundamentales de Programación

Proyecto: Redes Inalámbricas (Generación y Clasificación de Datos)

# CFP-B03 Cerebritos en Acción

## Integrantes

### CRISTIAN AGUIRRE RAMÍREZ
### ANGIE LEGUIZAMÓN BUITRAGO
### YENNIFER OFELIA MALAGUERA VASCO
### NELSON DAVID VARGAS LÓPEZ
### SONIA LILIANA CRUZ REYES

---

### 1. Descripción General

El proyecto adapta el problema general de generación y clasificación de datos al dominio de **Monitoreo de Redes Inalámbricas**.

Esta entrega contiene una **versión preliminar del proyecto completo**: generación de archivos, lectura de datos y elaboración de reportes. Las partes pendientes se describen en [pendientes_entrega2.txt](pendientes_entrega2.txt).

---

### 2. Correspondencia de Requisitos Funcionales

| Requisito | Implementación en Redes Inalámbricas | Archivo o salida | Método asociado |
| --- | --- | --- | --- |
| **RF1: Catálogo Maestro de Artículos** | Catálogo de tipos de dispositivos y ancho de banda estimado | `dispositivos.csv` | `createDeviceTypesFile` / `createProductsFile` |
| **RF2: Catálogo de Nodos / Agrupadores** | Maestro de puntos de acceso (AP) con ubicación y zona física | `aps.csv` | `createAccessPointsFile` / `createSalesManInfoFile` |
| **RF3: Archivos de Transacciones por Nodo** | Archivos de conexiones con MAC, RSSI y tipo de dispositivo | `conexiones_AP*.txt` | `createConnectionsFile` / `createSalesMenFile` |
| **RF4: Métodos de Prueba (Numeral 5)** | Métodos del dominio de redes y métodos de compatibilidad | Código Java | `createProductsFile`, `createSalesManInfoFile`, `createSalesMenFile` |
| **RF5: Cero Interacción** | Ejecución autónoma sin solicitar datos al usuario | Consola desatendida | `main(String[] args)` en ambas clases |
| **RF6: Notificación** | Mensajes descriptivos de éxito y avisos de error | Salida estándar / error | `try-catch` |

---

### 3. Archivos Generados por `GenerateInfoFiles.java`

Al ejecutar el programa, se crea la carpeta `datos_generados/` con los siguientes archivos:

#### 1. `dispositivos.csv` (Catálogo maestro)

Registra los siete tipos de dispositivos autorizados en la red universitaria con su perfil de consumo y prioridad:

```text
DEV01;Laptop_Docente;60;Alta
DEV02;Smartphone_Estudiante;25;Media
DEV03;Tablet_Laboratorio;20;Media
DEV04;SmartTV_Auditorio;80;Alta
DEV05;Sensor_IoT_Ambiente;5;Baja
DEV06;Terminal_Impresion;10;Baja
DEV07;Laptop_Estudiante;45;Media
```

#### 2. `aps.csv` (Maestro de Puntos de Acceso)

Registra los puntos de acceso desplegados en el campus:

```text
AP;AP001;Biblioteca_Central;Bloque_A
AP;AP002;Cafeteria_Principal;Bloque_B
AP;AP003;Laboratorio_Sistemas;Bloque_C
AP;AP004;Auditorio_Mayor;Bloque_A
AP;AP005;Salas_de_Estudio;Bloque_B
```

#### 3. `conexiones_AP*.txt` (Registros de conexión por AP)

Un archivo independiente por cada punto de acceso.

- **Primera línea:** encabezado con la identificación del AP (`AP;ID_AP;Ubicacion`).
- **Líneas siguientes:** eventos de conexión (`IDDispositivo;DireccionMAC;ValorRSSI;`).

Ejemplo ilustrativo de `conexiones_AP001.txt`:

```text
AP;AP001;Biblioteca_Central
DEV02;A4:5E:60:12:34:01;-45;
DEV01;18:65:90:AB:32:02;-68;
DEV02;A4:5E:60:12:34:01;-52;
DEV03;C2:44:11:99:FF:30;-71;
```

> **Nota técnica:** La reutilización intencional de direcciones MAC busca simular reconexiones de un mismo usuario. Los registros generados son aleatorios.

---

### 4. Reportes de la Entrega 2

La clase `main.java` procesa los archivos de entrada y escribe:

- **`reporte_access_points.csv`:** identificador y ubicación del AP, junto con la suma de los valores nominales de ancho de banda de sus eventos de conexión, ordenada de mayor a menor.
- **`reporte_dispositivos.csv`:** nombre del tipo y ancho de banda nominal, ordenados por cantidad de conexiones de mayor a menor; la cantidad no se muestra en el archivo.

La suma por eventos es una estimación acumulada; no representa una medición de consumo simultáneo ni una comprobación de saturación del AP.

### 5. Ejecución en Eclipse

1. Descargar y descomprimir el repositorio.
2. Importar la carpeta mediante **File > Import > Existing Projects into Workspace**.
3. Configurar el proyecto con Java 8 o un JDK compatible.
4. Ejecutar `src/GenerateInfoFiles.java` con **Run As > Java Application**.
5. Ejecutar `src/main.java` con **Run As > Java Application**.
6. Actualizar el proyecto con **Refresh** y revisar `datos_generados/`.

Ejecutar desde la carpeta raíz del proyecto. Los archivos de entrada y los reportes se crean al ejecutar las clases; no se incluyen resultados de prueba en esta versión.

### 6. Estado y pendientes

Consultar [pendientes_entrega2.txt](pendientes_entrega2.txt), documento adjunto de la versión preliminar. El código Java y ese documento se conservan tal como fueron recibidos en el ZIP de la Entrega 2.

Repositorio de referencia del grupo: [CFP-B03-Cerebritos-en-acci-n](https://github.com/ndavvargas/CFP-B03-Cerebritos-en-acci-n).
