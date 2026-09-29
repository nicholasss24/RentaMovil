import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Coordina la flota y los cobros; no lee del teclado ni imprime. */
public class RentaMovil {
    private final Map<String, Vehiculo> flota;
    private BigDecimal ingresos;

    public RentaMovil() {
        flota = new LinkedHashMap<>();
        ingresos = new BigDecimal("0.00");
    }

    public void registrar(Vehiculo vehiculo) {
        if (vehiculo == null) {
            throw new IllegalArgumentException("Debe proporcionar un vehiculo.");
        }
        if (flota.containsKey(vehiculo.getPlaca())) {
            throw new IllegalArgumentException("Ya existe un vehiculo con esa placa.");
        }
        if (!vehiculo.isDisponible()) {
            throw new IllegalArgumentException("Un vehiculo nuevo debe estar disponible.");
        }
        flota.put(vehiculo.getPlaca(), vehiculo);
    }

    public Vehiculo buscar(String placa) {
        Vehiculo vehiculo = flota.get(Vehiculo.normalizarPlaca(placa));
        if (vehiculo == null) {
            throw new IllegalArgumentException("No existe un vehiculo con esa placa.");
        }
        return vehiculo;
    }

    public List<Vehiculo> getFlota() {
        return Collections.unmodifiableList(new ArrayList<>(flota.values()));
    }

    public BigDecimal getIngresos() { return ingresos; }

    public BigDecimal cotizar(String placa, int dias) {
        return buscar(placa).calcularAlquiler(dias);
    }

    /** Retorna el cobro; si se cancela, retorna Q0.00 y no cambia el estado. */
    public BigDecimal alquilar(String placa, int dias, boolean confirmar) {
        Vehiculo vehiculo = buscar(placa);
        if (!vehiculo.isDisponible()) {
            throw new IllegalStateException("El vehiculo ya esta alquilado.");
        }
        BigDecimal total = vehiculo.calcularAlquiler(dias);
        if (!confirmar) {
            return new BigDecimal("0.00");
        }
        // Todas las validaciones se completan antes de mutar el estado.
        BigDecimal nuevosIngresos = ingresos.add(total);
        vehiculo.ocupar();
        ingresos = nuevosIngresos;
        return total;
    }

    public void devolver(String placa) { buscar(placa).devolver(); }

    public String generarReporte() {
        Map<String, int[]> conteos = new LinkedHashMap<>();
        int disponibles = 0;
        for (Vehiculo vehiculo : flota.values()) {
            int[] fila = conteos.computeIfAbsent(vehiculo.getCategoria(), clave -> new int[3]);
            fila[0]++;
            if (vehiculo.isDisponible()) {
                fila[1]++;
                disponibles++;
            } else {
                fila[2]++;
            }
        }
        StringBuilder reporte = new StringBuilder("Categoria | Total | Disponibles | Alquilados\n");
        for (Map.Entry<String, int[]> entrada : conteos.entrySet()) {
            int[] fila = entrada.getValue();
            reporte.append(entrada.getKey()).append(" | ").append(fila[0]).append(" | ")
                    .append(fila[1]).append(" | ").append(fila[2]).append('\n');
        }
        return reporte.append("TOTAL | ").append(flota.size()).append(" | ")
                .append(disponibles).append(" | ").append(flota.size() - disponibles)
                .append("\nIngresos acumulados: Q").append(ingresos.toPlainString()).toString();
    }
}
