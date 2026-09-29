import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;

/** Datos comunes y calculo base. Los datos de identificacion no cambian. */
public abstract class Vehiculo {
    private final String placa;
    private final String marca;
    private final String modelo;
    private final BigDecimal tarifaDiaria;
    private boolean disponible;

    protected Vehiculo(String placa, String marca, String modelo, BigDecimal tarifaDiaria) {
        this.placa = normalizarPlaca(placa);
        if (marca == null || marca.trim().isEmpty()
                || modelo == null || modelo.trim().isEmpty()) {
            throw new IllegalArgumentException("La marca y el modelo no pueden estar vacios.");
        }
        if (tarifaDiaria == null || tarifaDiaria.signum() <= 0) {
            throw new IllegalArgumentException("La tarifa debe ser mayor que cero.");
        }
        this.tarifaDiaria = tarifaDiaria.setScale(2, RoundingMode.HALF_UP);
        if (this.tarifaDiaria.signum() <= 0) {
            throw new IllegalArgumentException("La tarifa redondeada debe ser al menos Q0.01.");
        }
        this.marca = marca.trim();
        this.modelo = modelo.trim();
        this.disponible = true;
    }

    public static String normalizarPlaca(String placa) {
        if (placa == null || placa.trim().isEmpty()) {
            throw new IllegalArgumentException("La placa no puede estar vacia.");
        }
        return placa.trim().toUpperCase(Locale.ROOT);
    }

    public final String getPlaca() { return placa; }
    public final boolean isDisponible() { return disponible; }
    public abstract String getCategoria();
    protected abstract String detalles();
    protected abstract BigDecimal recargo(int dias);

    /** Cotizar no modifica disponibilidad ni ingresos. */
    public final BigDecimal calcularAlquiler(int dias) {
        if (dias <= 0) {
            throw new IllegalArgumentException("Los dias deben ser enteros positivos.");
        }
        return tarifaDiaria.multiply(BigDecimal.valueOf(dias))
                .add(recargo(dias)).setScale(2, RoundingMode.HALF_UP);
    }

    // Acceso de paquete: RentaMovil coordina los cambios de estado.
    final void ocupar() {
        if (!disponible) {
            throw new IllegalStateException("El vehiculo ya esta alquilado.");
        }
        disponible = false;
    }

    final void devolver() {
        if (disponible) {
            throw new IllegalStateException("El vehiculo ya esta disponible.");
        }
        disponible = true;
    }

    @Override
    public final String toString() {
        return getCategoria() + " | " + placa + " | " + marca + " " + modelo
                + " | Tarifa diaria: Q" + tarifaDiaria.toPlainString()
                + " | " + detalles() + " | " + (disponible ? "Disponible" : "Alquilado");
    }
}
