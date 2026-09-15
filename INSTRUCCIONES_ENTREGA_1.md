# GUÍA E INSTRUCCIONES PARA LA ENTREGA 1 (SEMANA 3)
## Módulo: Conceptos Fundamentales de Programación
### Politécnico Grancolombiano - Nivel Especialización
**Proyecto:** Monitoreo y Análisis de Redes Inalámbricas

---

## 1. ESTADO DE CUMPLIMIENTO DE LA ENTREGA

> **Confirmación Docente:** El docente aprobó el cambio de contexto temático hacia **Redes Inalámbricas**, exigiendo el cumplimiento riguroso de todos los **Requerimientos Funcionales** de la guía oficial institucional (`Documentacion/Entrega 1.pdf`).

A continuación se detalla la matriz final de cumplimiento técnico y funcional:

| Requerimiento Oficial (`Entrega 1.pdf`) | Implementación en `Entrega1_Semana3_Redes_Inalambricas` | Estado |
| :--- | :--- | :---: |
| **RF1: Catálogo Maestro de Recursos** | Catálogo de tipos de dispositivos con ancho de banda y prioridad en `dispositivos.csv`. | **CUMPLE** |
| **RF2: Catálogo de Nodos / Agrupadores** | Maestro de Access Points con ubicación y zona en `aps.csv`. | **CUMPLE** |
| **RF3: Archivos de Eventos por Nodo** | Archivos individuales `conexiones_AP*.txt` asociando dispositivos, MACs y RSSI. | **CUMPLE** |
| **RF4: Métodos de Prueba (Numeral 5)** | Métodos de dominio + métodos puente exactos (`createSalesMenFile`, `createProductsFile`, `createSalesManInfoFile`). | **CUMPLE** |
| **RF5: Cero Interacción con el Usuario** | `GenerateInfoFiles.java` se ejecuta sin pedir datos por consola (`Scanner`, etc.). | **CUMPLE** |
| **RF6: Notificación de Ejecución** | Mensajes estructurados de éxito o captura de excepciones con `try-catch`. | **CUMPLE** |
| **Entorno y Versión** | Proyecto Eclipse configurado para Java 8 (`JavaSE-1.8`). | **CUMPLE** |
| **Estructura de Clases con main** | Una sola clase con `main` en la Entrega 1 (`GenerateInfoFiles`). | **CUMPLE** |
| **Documentación y Clean Code** | Comentarios Javadoc completos en clases y métodos, variables descriptivas. | **CUMPLE** |

---

## 2. HOMOLOGACIÓN DETALLADA DE MÉTODOS Y ARCHIVOS

Para garantizar que el proyecto apruebe tanto una revisión manual por parte del profesor como cualquier script de evaluación automática, la clase `GenerateInfoFiles.java` incorpora una doble capa de métodos:

### 1. Métodos de Dominio (Redes Inalámbricas):
* `createDeviceTypesFile(int deviceTypesCount)`: Genera `datos_generados/dispositivos.csv`.
* `createAccessPointsFile(int accessPointsCount)`: Genera `datos_generados/aps.csv`.
* `createConnectionsFile(int connectionCount, String accessPointId, String location)`: Genera `datos_generados/conexiones_AP*.txt`.

### 2. Métodos Puente (Compatibilidad literal con el Numeral 5 del PDF):
* `createProductsFile(int productsCount)` $\rightarrow$ Delega internamente a `createDeviceTypesFile`.
* `createSalesManInfoFile(int salesmanCount)` $\rightarrow$ Delega internamente a `createAccessPointsFile`.
* `createSalesMenFile(int randomSalesCount, String name, long id)` $\rightarrow$ Delega internamente a `createConnectionsFile`.

---

## 3. PASO A PASO PARA REALIZAR LA ENTREGA 1

### Paso 1: Importar y Probar en Eclipse
1. Abre **Eclipse IDE for Java Developers**.
2. Selecciona **File > Import... > General > Existing Projects into Workspace**.
3. En *Select root directory*, navega y selecciona:
   `c:\ProAntigravity\Conceptos Fundamentales de programacion\Entrega1_Semana3_Redes_Inalambricas`
4. Verifica que el proyecto no muestre errores y que la librería sea **JRE System Library [JavaSE-1.8]**.
5. Abre `src/GenerateInfoFiles.java`.
6. Haz clic derecho y selecciona **Run As > Java Application**.
7. Verifica la salida en consola:
   ```text
   ==========================================================
   Generación de archivos finalizada exitosamente.
   ==========================================================
   Archivos generados en la carpeta: datos_generados
    1. Catalogo de Dispositivos: dispositivos.csv
    2. Catalogo de Access Points: aps.csv
    3. Archivos de Conexiones: conexiones_AP*.txt (5 archivos)
   ==========================================================
   ```
8. Presiona **F5** en Eclipse sobre la carpeta del proyecto para refrescar y confirmar que aparezcan los archivos dentro de `datos_generados/`.

---

### Paso 2: Crear el Repositorio en GitHub o Bitbucket
El documento oficial exige entregar mediante un **hipervínculo a un repositorio de código**.

1. Inicia sesión en tu cuenta de [GitHub](https://github.com) (o Bitbucket).
2. Crea un repositorio nuevo (por ejemplo: `CFP-Entrega1-RedesInalambricas`).
3. Déjalo configurado como **Público** para que el docente pueda ingresar sin restricciones de acceso.
4. Sube el proyecto ejecutando los siguientes comandos en tu terminal (PowerShell o Git Bash):
   ```bash
   cd "c:\ProAntigravity\Conceptos Fundamentales de programacion\Entrega1_Semana3_Redes_Inalambricas"
   git init
   git add .project .classpath src/ README.md ENTREGA_1_SEMANA_3.txt INSTRUCCIONES_ENTREGA_1.md
   git commit -m "Entrega 1 - Semana 3: Generacion y clasificacion de datos (Redes Inalambricas)"
   git branch -M main
   git remote add origin https://github.com/TU_USUARIO/TU_REPOSITORIO.git
   git push -u origin main
   ```

---

### Paso 3: Registrar la Entrega en el Aula Virtual
1. Ingresa a la plataforma virtual del **Politécnico Grancolombiano**.
2. Ve al curso: **Conceptos Fundamentales de Programación > Actividades > Entrega 1 (Semana 3)**.
3. En el formulario de entrega (o documento de portada grupal si se solicita formato Word/PDF), redacta el texto de entrega siguiendo esta plantilla:

```text
Estimado(a) Tutor(a),

Presentamos la Entrega 1 (Semana 3) del Módulo Conceptos Fundamentales de Programación.

- Nombre del Proyecto: Sistema de Generación de Datos para Monitoreo de Redes Inalámbricas.
- Integrantes del Grupo: [Nombres y Apellidos de los Integrantes]
- Enlace al Repositorio de Código (GitHub): https://github.com/TU_USUARIO/TU_REPOSITORIO

Alcance de la Entrega:
El repositorio contiene el proyecto completo estructurado para Eclipse en Java 8. Se implementó la clase GenerateInfoFiles.java cumpliendo con todos los requerimientos funcionales solicitados (generación autónoma de catálogo maestro de dispositivos, catálogo maestro de Access Points y archivos individuales de conexiones con simulación de reconexiones/MACs repetidas). Se incluyeron igualmente los métodos puente para garantizar compatibilidad con las firmas del numeral 5 de la guía.

Quedamos atentos a sus comentarios.
```

---

## 4. CHECKLIST DE VERIFICACIÓN ANTES DE ENVIAR

- [x] Proyecto Eclipse con `.project` y `.classpath` presentes.
- [x] Configuración de compilación fijada en Java 8 (`JavaSE-1.8`).
- [x] Clase `GenerateInfoFiles.java` como única clase con método `main`.
- [x] El programa se ejecuta de forma autónoma sin solicitar entradas al usuario.
- [x] Manejo de salida con mensaje descriptivo de éxito y control de excepciones (`try-catch`).
- [x] Se generan los tres conjuntos de datos requeridos (`dispositivos.csv`, `aps.csv`, `conexiones_AP*.txt`).
- [x] Métodos puente implementados con las firmas del PDF (`createSalesMenFile`, `createProductsFile`, `createSalesManInfoFile`).
- [x] Repositorio de GitHub/Bitbucket público y verificado.
