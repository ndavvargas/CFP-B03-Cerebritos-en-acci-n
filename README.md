# Entrega Final

## Módulo: Conceptos Fundamentales de Programación (Politécnico Grancolombiano)
Proyecto: Redes Inalámbricas (Generación y Clasificación de Datos)

# CFP-B03 Cerebritos en Acción

## Integrantes
- CRISTIAN AGUIRRE RAMÍREZ
- ANGIE LEGUIZAMÓN BUITRAGO
- YENNIFER OFELIA MALAGUERA VASCO
- NELSON DAVID VARGAS LÓPEZ
- SONIA LILIANA CRUZ REYES

---

## Descripción General

Genera datos de prueba de una red inalámbrica y produce dos reportes ordenados. Java 8, Eclipse.

## Clases (solo dos tienen metodo `main`)

| Clase | Que hace |
|---|---|
| `GenerateInfoFiles` | Genera en `datos_generados/`: `dispositivos.csv`, `aps.csv` y de 1 a 3 archivos `conexiones_<AP>.txt` por Access Point. |
| `main` | Lee esos archivos y genera `reporte_access_points.csv` y `reporte_dispositivos.csv`. |

Ninguna de las dos pide datos al usuario; ambas muestran un mensaje de
finalizacion exitosa o de error.

## Equivalencia con la rubrica

| Rubrica (ventas) | Este proyecto |
|---|---|
| Vendedores | Access Points |
| Productos | Tipos de dispositivo |
| Ventas | Conexiones (MAC y RSSI) |
| Punto 3: vendedores por dinero recaudado | `reporte_access_points.csv`: `ID_AP;Ubicacion;TotalAnchoBandaMbps`, de mayor a menor |
| Punto 4: productos por cantidad vendida (se muestra nombre y precio) | `reporte_dispositivos.csv`: `TipoDispositivo;AnchoBandaMbps`, ordenado por numero de conexiones (no se muestra) |
| `createSalesMenFile`, `createProductsFile`, `createSalesManInfoFile` | Existen con la firma pedida y delegan en los metodos del dominio |

## Extras de la rubrica

- **(a) Mas de un archivo por vendedor**: implementado. Un Access Point puede
  tener `conexiones_AP001.txt`, `conexiones_AP001_2.txt`, ...; `main` los suma.
- **(b) Archivos serializados**: no implementado.
- **(c) Deteccion de datos erroneos**: implementado. Se avisa y se continua ante
  lineas mal formadas, dispositivos inexistentes, MAC o RSSI invalidos, anchos
  de banda negativos y archivos faltantes.

## Como ejecutar en Eclipse

1. `File > Import > Existing Projects into Workspace` y elegir esta carpeta.
2. Verificar que el JRE del proyecto sea Java 8.
3. Ejecutar `GenerateInfoFiles.java` como *Java Application*.
4. Ejecutar `main.java` como *Java Application*.
5. Revisar la carpeta `datos_generados`.

La carpeta `datos_generados` ya trae un ejemplo de ejecucion; se sobrescribe
cada vez que se ejecuta `GenerateInfoFiles`.

# Entrega 2 - Semana 5

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

# Entrega 1 - Semana 3
## Módulo: Conceptos Fundamentales de Programación
### Proyecto: Redes Inalámbricas (Generación y Clasificación de Datos)

---

### 1. Descripción General
Este proyecto corresponde a la **Entrega 1 (Semana 3)** del módulo *Conceptos Fundamentales de Programación* (Nivel Especialización - Politécnico Grancolombiano). 

El proyecto adapta el problema general de generación y clasificación de datos al dominio de **Monitoreo de Redes Inalámbricas**, manteniendo una correspondencia estricta del 100% con todos los **Requerimientos Funcionales** de la guía institucional.

---

### 2. Correspondencia de Requerimientos Funcionales

| Requerimiento Oficial (`Entrega 1.pdf`) | Implementación en Redes Inalámbricas | Archivo Generado | Método Asociado |
| :--- | :--- | :--- | :--- |
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
> **Nota técnica:** La reutilización intencional de direcciones MAC simula reconexiones de un mismo usuario, lo que permitirá en las Entregas 2 y 3 validar el conteo de dispositivos únicos vs. conexiones totales.

---

### 4. Requisitos y Compatibilidad
- **Entorno:** Eclipse IDE for Java Developers.
- **Java:** Java 8 (`JavaSE-1.8`).
- **Interacción:** El programa no requiere ninguna interacción por teclado (`Scanner`, diálogos, etc.).
- **Métodos requeridos del PDF:** Incluye tanto los métodos de dominio como los métodos puente (`createSalesMenFile`, `createProductsFile`, `createSalesManInfoFile`) garantizando compatibilidad con cualquier rúbrica de corrección automática.

---

### 5. Cómo Ejecutar en Eclipse
1. Importar el proyecto: **File > Import... > Existing Projects into Workspace**.
2. Seleccionar la carpeta `Entrega1_Semana3_Redes_Inalambricas`.
3. Abrir `src/GenerateInfoFiles.java`.
4. Ejecutar con **Run As > Java Application**.
5. Presionar **F5** en la carpeta del proyecto para ver los archivos actualizados en `datos_generados/`.


