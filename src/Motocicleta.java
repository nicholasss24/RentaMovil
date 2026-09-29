import java.math.BigDecimal;

public class Motocicleta extends Vehiculo {
    private final int cilindraje;

    public Motocicleta(String placa, String marca, String modelo, BigDecimal tarifaDiaria,
                       int cilindraje) {
        super(placa, marca, modelo, tarifaDiaria);
        if (cilindraje <= 0) {
            throw new IllegalArgumentException("El cilindraje debe ser mayor que cero.");
        }
        this.cilindraje = cilindraje;
    }

    @Override public String getCategoria() { return "Motocicleta"; }
    @Override protected String detalles() { return "Cilindraje: " + cilindraje + " cc"; }
    @Override protected BigDecimal recargo(int dias) {
        return cilindraje > 250 ? new BigDecimal("75") : BigDecimal.ZERO;
    }
}
