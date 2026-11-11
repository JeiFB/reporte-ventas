# Reporte de Ventas

Aplicacion Java (Maven) que lee un archivo `ventas.csv`, calcula indicadores de
ventas, genera una grafica, y prepara un resumen estructurado con un prompt para
que una IA redacte un reporte ejecutivo. Se empaqueta como un JAR ejecutable.

Caso de Estudio 2 - Tecnicas de Programacion y Laboratorio (Unidad 6).

## Requisitos

- Java 17 o superior
- Maven 3.8 o superior

## Estructura

```
reporte-ventas/
├── pom.xml
├── README.md
├── datos/
│   └── ventas.csv
├── salida/                     (se genera al ejecutar)
│   ├── ventas_por_producto.png
│   ├── prompt_ia.txt
│   └── respuesta_ia.txt
├── src/main/java/
│   ├── Main.java
│   ├── modelo/                 Venta, Indicadores
│   ├── servicio/               LectorVentas, CalculadoraIndicadores
│   └── reporte/                GeneradorGrafica, GeneradorResumenIA
└── target/
    └── reporte-ventas.jar
```

## Compilar y empaquetar

```
mvn clean package
```

Esto genera el JAR en `target/reporte-ventas.jar`.

## Ejecutar (fuera de IntelliJ, desde la terminal)

Desde la carpeta del proyecto:

```
java -jar target/reporte-ventas.jar
```

La aplicacion imprime los indicadores, genera la grafica en
`salida/ventas_por_producto.png` y el prompt en `salida/prompt_ia.txt`.

## Indicadores que calcula Java

- Valor total de ventas
- Cantidad total vendida
- Producto con mayor valor vendido
- Valor promedio por registro

## Analisis de licencias

| Libreria | Uso en el proyecto | Licencia | Conclusion de uso |
|---|---|---|---|
| Apache Commons CSV | Leer y procesar el archivo `ventas.csv` | Apache License 2.0 | Licencia permisiva: permite usar, modificar y distribuir el software (incluso en proyectos comerciales) con la unica condicion de conservar el aviso de copyright y licencia. No obliga a liberar el codigo propio. Totalmente compatible con un proyecto academico. |
| XChart | Generar la grafica de ventas por producto | Apache License 2.0 | Misma licencia permisiva, sin clausulas copyleft. No impone restricciones sobre el codigo del proyecto. Compatible con uso academico. |

> La herramienta de empaquetado `maven-shade-plugin` tambien usa Apache License 2.0.

**Por que importa:** una dependencia tecnica no es "gratis de usar sin condiciones".
Cada libreria tiene una licencia que define como se puede usar y distribuir. En
este proyecto ambas librerias son Apache 2.0 (permisivas), por lo que no hay
restricciones para un trabajo academico. Con una licencia copyleft fuerte (por
ejemplo GPL) habria que tener mas cuidado, porque podria obligar a liberar el
codigo del proyecto.

## Parte de IA

- Java calcula los indicadores (deterministas) y construye el **prompt**.
- La IA solo **interpreta y redacta** el reporte ejecutivo; no recalcula cifras.
- El prompt generado queda en `salida/prompt_ia.txt` y la respuesta obtenida en
  `salida/respuesta_ia.txt`.
- No se usan API keys en el codigo. Si se usara una API de IA, las credenciales
  se manejarian por variables de entorno.

## Preguntas de sustentacion

**1. ¿Que problema resolvio cada libreria?**
Apache Commons CSV se encarga de leer y separar correctamente las filas y
columnas del `ventas.csv`, sin tener que escribir un parser a mano. XChart genera
la grafica de barras con titulo y ejes, sin dibujarla manualmente.

**2. ¿Que licencia tiene cada dependencia y por que es importante revisarla?**
Ambas son Apache License 2.0 (permisiva). Es importante revisarla porque una
dependencia impone condiciones de uso y distribucion; una licencia copyleft
podria obligar a publicar el codigo propio. Apache 2.0 no tiene ese problema.

**3. ¿Cual es la diferencia entre empaquetar y desplegar?**
Empaquetar es generar el artefacto (el JAR con todo lo necesario para correr).
Desplegar es ponerlo a ejecutar en un entorno real, en este caso correrlo desde
la terminal con `java -jar` fuera de IntelliJ.

**4. ¿Que funcion cumple Maven en el proyecto?**
Gestiona las dependencias (las descarga solo), compila el codigo, y empaqueta la
aplicacion en el JAR ejecutable (con el maven-shade-plugin). Estandariza todo el
proceso de construccion.

**5. ¿Que parte del reporte calcula Java y que parte realiza la IA?**
Java calcula los indicadores (valor total, cantidad total, producto mayor y
promedio) y arma el resumen y el prompt. La IA solo redacta el reporte ejecutivo
en lenguaje natural a partir de esos datos.

**6. ¿Que cambiaria si la aplicacion debiera ejecutarse en un servidor?**
La ruta del CSV y de las salidas deberian ser configurables (no fijas); se
ejecutaria sin interfaz grafica (modo headless); las credenciales irian en
variables de entorno; se agregarian registros (logs) y posiblemente se correria
de forma programada o como servicio. El JAR con dependencias ya facilita ese
despliegue.

## Integrantes

- Jeifher Fuentes
- Andres Perez
