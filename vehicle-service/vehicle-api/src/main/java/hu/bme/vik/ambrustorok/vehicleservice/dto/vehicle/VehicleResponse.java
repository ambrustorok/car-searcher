package hu.bme.vik.ambrustorok.vehicleservice.dto.vehicle;

import hu.bme.vik.ambrustorok.vehicleservice.common.EStyle;
import hu.bme.vik.ambrustorok.vehicleservice.dto.engine.EngineInVehicleResponse;
import hu.bme.vik.ambrustorok.vehicleservice.dto.option.OptionInVehicleResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleResponse {

    @NotNull
    @NotEmpty
    Collection<EngineInVehicleResponse> engines;
    @NotNull
    @NotEmpty
    Collection<OptionInVehicleResponse> options;
    @NotNull
    private UUID id;
    @NotNull
    @NotEmpty
    private double price;
    @NotNull
    @NotEmpty
    private int numberOfDoors;
    @NotNull
    @NotEmpty
    private double length;
    @NotNull
    @NotEmpty
    private String manufacturer;
    @NotNull
    @NotEmpty
    private String model;
    @NotNull
    @NotEmpty
    private EStyle style;
    @NotNull
    @NotEmpty
    private double weight;
    @NotNull
    @NotEmpty
    private double width;
    @NotNull
    @NotEmpty
    private int warranty;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VehicleResponse that = (VehicleResponse) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
