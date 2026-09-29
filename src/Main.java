import java.math.BigDecimal;
import java.util.NoSuchElementException;
import java.util.Scanner;

/** Interfaz de consola y punto de entrada de la aplicacion. */
public class Main {
    private final Scanner entrada;
    private final RentaMovil empresa;

    public Main() {
        entrada = new Scanner(System.in);
        empresa = new RentaMovil();
        cargarDatosDemo();
    }

    public static void main(String[] args) { new Main().ejecutar(); }

    private void cargarDatosDemo() {
        empresa.registrar(new Automovil("P001AAA", "Toyota", "Corolla", new BigDecimal("200"), 5, true));
        empresa.registrar(new Automovil("P002AAA", "Kia", "Rio", new BigDecimal("180"), 5, false));
        empresa.registrar(new Motocicleta("M001AAA", "Honda", "CB250", new BigDecimal("100"), 250));
        empresa.registrar(new Motocicleta("M002AAA", "Yamaha", "MT03", new BigDecimal("150"), 321));
        empresa.registrar(new CamionetaCarga("C001AAA", "Hyundai", "H100", new BigDecimal("200"), new BigDecimal("1.5")));
        empresa.registrar(new CamionetaCarga("C002AAA", "Isuzu", "NPR", new BigDecimal("300"), new BigDecimal("3")));
    }

    private void ejecutar() {
        boolean continuar = true;
        while (continuar) {
            System.out.println("\nRENTAMOVIL\n1. Registrar vehiculo\n2. Consultar flota"
                    + "\n3. Cotizar\n4. Alquilar\n5. Devolver\n6. Reporte general\n0. Salir");
            try {
                int opcion = leerEntero("Opcion: ", 0, 6);
                switch (opcion) {
                    case 1: registrarVehiculo(); break;
                    case 2: mostrarFlota(); break;
                    case 3: cotizarVehiculo(); break;
                    case 4: alquilarVehiculo(); break;
                    case 5:
                        empresa.devolver(leerTexto("Placa: "));
                        System.out.println("Devolucion registrada. No se genero otro cobro.");
                        break;
                    case 6: System.out.println(empresa.generarReporte()); break;
                    case 0: continuar = false; break;
                    default: break;
                }
            } catch (IllegalArgumentException | IllegalStateException ex) {
                System.out.println("Operacion rechazada: " + ex.getMessage());
            } catch (NoSuchElementException ex) {
                System.out.println("Fin de la entrada. Se cierra el programa.");
                continuar = false;
            }
        }
        System.out.println("Hasta pronto.");
    }

    private void registrarVehiculo() {
        int tipo = leerEntero("1. Automovil  2. Motocicleta  3. Camioneta de carga: ", 1, 3);
        String placa = leerTexto("Placa: ");
        String marca = leerTexto("Marca: ");
        String modelo = leerTexto("Modelo: ");
        BigDecimal tarifa = leerDecimal("Tarifa diaria (Q): ");
        Vehiculo vehiculo;
        if (tipo == 1) {
            int pasajeros = leerEntero("Pasajeros: ", 1, Integer.MAX_VALUE);
            boolean automatico = leerSiNo("Transmision automatica (s/n): ");
            vehiculo = new Automovil(placa, marca, modelo, tarifa, pasajeros, automatico);
        } else if (tipo == 2) {
            int cilindraje = leerEntero("Cilindraje (cc): ", 1, Integer.MAX_VALUE);
            vehiculo = new Motocicleta(placa, marca, modelo, tarifa, cilindraje);
        } else {
            vehiculo = new CamionetaCarga(placa, marca, modelo, tarifa,
                    leerDecimal("Capacidad maxima (toneladas): "));
        }
        empresa.registrar(vehiculo);
        System.out.println("Vehiculo registrado y disponible.");
    }

    private void mostrarFlota() {
        if (empresa.getFlota().isEmpty()) {
            System.out.println("No hay vehiculos registrados.");
        }
        for (Vehiculo vehiculo : empresa.getFlota()) {
            System.out.println(vehiculo);
        }
    }

    private void cotizarVehiculo() {
        String placa = leerTexto("Placa: ");
        empresa.buscar(placa);
        int dias = leerEntero("Dias: ", 1, Integer.MAX_VALUE);
        mostrarCotizacion(placa, dias);
    }

    private void mostrarCotizacion(String placa, int dias) {
        System.out.println(empresa.buscar(placa));
        System.out.println("Dias: " + dias + " | Total: Q" + empresa.cotizar(placa, dias).toPlainString());
        System.out.println("La cotizacion no registra un alquiler ni un ingreso.");
    }

    private void alquilarVehiculo() {
        String placa = leerTexto("Placa: ");
        if (!empresa.buscar(placa).isDisponible()) {
            throw new IllegalStateException("El vehiculo ya esta alquilado.");
        }
        int dias = leerEntero("Dias: ", 1, Integer.MAX_VALUE);
        mostrarCotizacion(placa, dias);
        boolean confirmar = leerSiNo("Confirmar alquiler y cobro completo (s/n): ");
        BigDecimal cobro = empresa.alquilar(placa, dias, confirmar);
        System.out.println(confirmar ? "Alquiler confirmado. Cobrado: Q" + cobro.toPlainString()
                : "Alquiler cancelado. No se modificaron disponibilidad ni ingresos.");
    }

    private String leerTexto(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String valor = entrada.nextLine().trim();
            if (!valor.isEmpty()) { return valor; }
            System.out.println("El dato no puede estar vacio.");
        }
    }

    private int leerEntero(String mensaje, int minimo, int maximo) {
        while (true) {
            try {
                int valor = Integer.parseInt(leerTexto(mensaje));
                if (valor >= minimo && valor <= maximo) { return valor; }
            } catch (NumberFormatException ex) {
                // Mostrar el mismo mensaje y volver a pedir el dato.
            }
            System.out.println("Ingrese un entero entre " + minimo + " y " + maximo + ".");
        }
    }

    private BigDecimal leerDecimal(String mensaje) {
        while (true) {
            String texto = leerTexto(mensaje).replace(',', '.');
            if (texto.matches("[0-9]+(\\.[0-9]+)?")) {
                BigDecimal valor = new BigDecimal(texto);
                if (valor.signum() > 0) { return valor; }
            }
            System.out.println("Ingrese un decimal mayor que cero, sin separadores de miles.");
        }
    }

    private boolean leerSiNo(String mensaje) {
        while (true) {
            String valor = leerTexto(mensaje);
            if (valor.equalsIgnoreCase("s")) { return true; }
            if (valor.equalsIgnoreCase("n")) { return false; }
            System.out.println("Responda s o n.");
        }
    }
}
