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
