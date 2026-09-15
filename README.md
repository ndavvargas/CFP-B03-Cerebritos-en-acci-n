# Entrega 1 - Semana 3
## Módulo: Conceptos Fundamentales de Programación
Proyecto: Redes Inalámbricas (Generación y Clasificación de Datos)

# CFP-B03 Cerebritos en Accion
## Integrantes

### CRISTIAN AGUIRRE RAMIREZ
### ANGIE LEGUIZAMÓN BUITRAGO
### YENNIFER OFELIA MALAGUERA VASCO
### NELSON DAVID VARGAS LOPEZ
### SONIA LILIANA CRUZ REYES



---

### 1. Descripción General

El proyecto adapta el problema general de generación y clasificación de datos al dominio de **Monitoreo de Redes Inalámbricas**.

---

### 2. Correspondencia de Requerimientos Funcionales

| Implementación en Redes Inalámbricas | Archivo Generado | Método Asociado |

| **RF1: Catálogo Maestro de Items** | Catálogo de tipos de dispositivos y ancho de banda estimado | `dispositivos.csv` | `createDeviceTypesFile` / `createProductsFile` |
| **RF2: Catálogo de Nodos / Agrupadores** | Maestro de Access Points (APs) con ubicación y zona física | `aps.csv` | `createAccessPointsFile` / `createSalesManInfoFile` |
| **RF3: Archivos de Transacciones por Nodo** | Archivos de conexiones con MAC, RSSI y tipo de dispositivo | `conexiones_AP*.txt` | `createConnectionsFile` / `createSalesMenFile` |
| **RF4: Métodos de Prueba (Numeral 5)** | Métodos con firmas de Redes + Métodos puente de la rúbrica | Código Java | Sobrecargas y delegaciones exactas |
| **RF5: Cero Interacción** | Ejecución autónoma sin solicitar datos al usuario | Consola desatendida | `main(String[] args)` |
| **RF6: Notificación** | Mensajes descriptivos de éxito o captura de errores | Salida estándar / error | `try-catch` |

---

### 3. Archivos Generados por `GenerateInfoFiles.java`

Al ejecutar el programa, se crea la carpeta `datos_generados/` con los siguientes archivos:

#### 1. `dispositivos.csv` (Catálogo maestro)
Registra los tipos de dispositivos autorizados en la red universitaria con su perfil de consumo y prioridad:
```text
DEV01;Laptop_Docente;60;Alta
DEV02;Smartphone_Estudiante;25;Media
DEV03;Tablet_Laboratorio;20;Media
DEV04;SmartTV_Auditorio;80;Alta
DEV05;Sensor_IoT_Ambiente;5;Baja
```

#### 2. `aps.csv` (Maestro de Access Points)
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
- **Primera línea:** Encabezado con la identificación del AP (`AP;ID_AP;Ubicacion`).
- **Líneas siguientes:** Eventos de conexión (`IDDispositivo;DireccionMAC;ValorRSSI;`).

Ejemplo de `conexiones_AP001.txt`:
```text
AP;AP001;Biblioteca_Central
DEV02;A4:5E:60:12:34:01;-45;
DEV01;18:65:90:AB:32:02;-68;
DEV02;A4:5E:60:12:34:01;-52;
DEV03;C2:44:11:99:FF:30;-71;
```
> **Nota técnica:** La reutilización intencional de direcciones MAC simula reconexiones de un mismo usuario.


