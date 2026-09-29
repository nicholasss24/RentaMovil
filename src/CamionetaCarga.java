import java.math.BigDecimal;

public class CamionetaCarga extends Vehiculo {
    private final BigDecimal capacidadToneladas;

    public CamionetaCarga(String placa, String marca, String modelo, BigDecimal tarifaDiaria,
                         BigDecimal capacidadToneladas) {
        super(placa, marca, modelo, tarifaDiaria);
        if (capacidadToneladas == null || capacidadToneladas.signum() <= 0) {
            throw new IllegalArgumentException("La capacidad debe ser mayor que cero.");
        }
        this.capacidadToneladas = capacidadToneladas;
    }

    @Override public String getCategoria() { return "Camioneta de carga"; }
    @Override protected String detalles() {
        return "Capacidad maxima: " + capacidadToneladas.toPlainString() + " toneladas";
    }
    @Override protected BigDecimal recargo(int dias) {
        return new BigDecimal("100").multiply(capacidadToneladas)
                .multiply(BigDecimal.valueOf(dias));
    }
}
