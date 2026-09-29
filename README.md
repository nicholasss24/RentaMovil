# RentaMovil - Ejercicio 4: Herencia

Aplicación de consola en Java para registrar automóviles, motocicletas y camionetas de carga; consultar, cotizar, confirmar alquileres, devolver vehículos y reportar la flota y los ingresos.

## Requisitos y ejecución

Se necesita un **JDK 17 o posterior** (incluye `java` y `javac`). No usa Maven, Gradle ni bibliotecas externas. Los datos viven en memoria y se reinician al salir.

Abre una terminal en la carpeta `RentaMovil`, donde está este README:

```sh
mkdir out
javac -encoding UTF-8 -d out src/*.java
java -cp out Main
```

Si `out` ya existe, omite `mkdir out`. Los mismos comandos sirven en macOS, Linux y Windows con el JDK disponible en PATH.

## Menú

| Opción | Acción |
|---|---|
| 1 | Registrar un vehículo de cualquiera de las tres categorías |
| 2 | Ver la flota, características, tarifa y disponibilidad |
| 3 | Cotizar por placa y días, incluso si el vehículo está alquilado |
| 4 | Ver el total y confirmar con `s` o cancelar con `n` |
| 5 | Registrar una devolución sin nuevos cobros |
| 6 | Consultar totales por categoría e ingresos acumulados |
| 0 | Salir |

Los días, pasajeros y cilindraje son enteros positivos. Tarifa y capacidad son decimales positivos. Se admite punto o coma decimal, sin separadores de miles. Las placas se recortan y convierten a mayúsculas; `p001aaa` y ` P001AAA ` identifican al mismo vehículo. Marca y modelo tampoco pueden estar vacíos.

El dinero usa `BigDecimal`: al registrar, la tarifa se redondea a centavos con `HALF_UP` y debe seguir siendo positiva. La capacidad conserva sus decimales. El total se redondea una vez a dos decimales, y ese mismo importe se cobra y acumula. Esta es una decisión explícita de implementación.

## Datos iniciales

Todos están disponibles y los ingresos empiezan en Q0.00.

| Placa | Tipo | Tarifa diaria | Característica |
|---|---|---:|---|
| P001AAA | Automóvil | Q200.00 | 5 pasajeros, automático |
| P002AAA | Automóvil | Q180.00 | 5 pasajeros, manual |
| M001AAA | Motocicleta | Q100.00 | 250 cc |
| M002AAA | Motocicleta | Q150.00 | 321 cc |
| C001AAA | Camioneta | Q200.00 | 1.5 toneladas |
| C002AAA | Camioneta | Q300.00 | 3 toneladas |

Para `d` días y tarifa diaria `t`:

- Automóvil manual: `t * d`.
- Automóvil automático: `(t + 50) * d`.
- Moto de hasta 250 cc: `t * d`.
- Moto de más de 250 cc: `t * d + 75` (una sola vez).
- Camioneta de capacidad `c`: `(t + 100 * c) * d`.

## Organización y herencia

- `Vehiculo`: clase abstracta, identidad y datos comunes, disponibilidad, validación de días y cálculo base.
- `Automovil`, `Motocicleta`, `CamionetaCarga`: datos propios y sobrescritura del recargo y las descripciones.
- `RentaMovil`: flota por placa, unicidad, búsquedas, alquileres, devoluciones, ingresos y reporte.
- `Main`: punto de entrada, datos iniciales, lectura validada y menú.

El cálculo llama al método polimórfico `recargo`. Cotizar no cambia el estado. `alquilar` comprueba las condiciones antes de modificar disponibilidad e ingresos. Si recibe `confirmar = false`, retorna Q0.00 sin cambios. El reporte calcula los conteos a partir de la flota y agrupa por categoría, sin almacenar contadores duplicados. No se necesita una clase Cliente, Reserva o Fecha para el alcance solicitado.

## Pruebas reproducibles

Después de compilar, se pueden ejecutar 34 pruebas automáticas de la consola. **Solo para estas pruebas** se requiere Python 3:

```sh
python3 pruebas/probar_consola.py
```

En Windows, si corresponde, usa `python pruebas/probar_consola.py` o `py pruebas/probar_consola.py`.

Resultado de la ejecución incluida: **34/34 PASS**, compilación con OpenJDK 17.0.20 y `-Xlint:all`, sin advertencias. El script verifica textos, totales, estados, salida normal y ausencia de errores en stderr. Cada caso comienza con un proceso nuevo; algunos realizan varias operaciones para comprobar acumulación y consistencia.

- `pruebas/resumen.txt`: resumen de resultados de la ejecución realizada.
- `pruebas/resultados.json`: expectativas y verificaciones de cada caso.
- `pruebas/transcripciones.txt`: entradas y salidas completas reales.
- `docs/RentaMovil_Informe.pdf`: análisis, diccionario de clases, UML incluido y evidencia.
- `docs/diagrama.puml`: fuente editable del diagrama UML, sin dependencias para ejecutar la aplicación.

### Demostración breve manual

1. Opción 6: comprobar 6 disponibles e ingresos Q0.00.
2. Opción 3, `C001AAA`, 3 días: total Q1050.00. Opción 6: ingresos siguen Q0.00.
3. Opción 4, `P001AAA`, 3 días, `n`: cancelado; siguen Q0.00.
4. Repetir el alquiler y responder `s`: cobra Q750.00; quedan 5 disponibles y 1 alquilado.
5. Cotizar `P001AAA` por 2 días: Q500.00; ingresos siguen Q750.00.
6. Intentar alquilar `P001AAA`: rechazo; ingresos siguen Q750.00.
7. Opción 5, `P001AAA`: vuelve a estar disponible; ingresos siguen Q750.00.
8. Repetir la devolución: rechazo; ingresos siguen Q750.00.

## Subir a GitHub sin conectar ChatGPT

1. Inicia sesión en tu cuenta de GitHub y crea un repositorio llamado, por ejemplo, `rentamovil-herencia` con visibilidad **Public**.
2. Descomprime el ZIP. Sube el **contenido** de `RentaMovil` para que `README.md` y `src` queden en la raíz del repositorio. Puedes usar la opción de subir archivos del sitio y arrastrar las carpetas. No subas solo el ZIP.
3. Incluye todos los `.java`, el README y, si deseas, `docs` y `pruebas`. No es necesario subir `out` ni archivos `.class`.
4. Confirma la carga y abre el repositorio en una ventana privada para verificar que sea público.
5. Copia la URL real que aparece en el navegador. En Canvas entrega esa URL junto con `docs/RentaMovil_Informe.pdf`.

El PDF no lleva nombre ni carné: añade los datos del estudiante si tu curso los solicita. Revisa el diseño y ejecuta el programa antes de presentarlo para poder explicar cómo funciona.
