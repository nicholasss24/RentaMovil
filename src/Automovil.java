import java.math.BigDecimal;

public class Automovil extends Vehiculo {
    private final int pasajeros;
    private final boolean automatico;

    public Automovil(String placa, String marca, String modelo, BigDecimal tarifaDiaria,
                     int pasajeros, boolean automatico) {
        super(placa, marca, modelo, tarifaDiaria);
        if (pasajeros <= 0) {
            throw new IllegalArgumentException("Los pasajeros deben ser mayores que cero.");
        }
        this.pasajeros = pasajeros;
        this.automatico = automatico;
    }

    @Override public String getCategoria() { return "Automovil"; }
    @Override protected String detalles() {
        return "Pasajeros: " + pasajeros + " | Transmision: "
                + (automatico ? "automatica" : "manual");
    }
    @Override protected BigDecimal recargo(int dias) {
        return automatico ? new BigDecimal("50").multiply(BigDecimal.valueOf(dias))
                : BigDecimal.ZERO;
    }
}
