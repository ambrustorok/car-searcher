package hu.bme.vik.ambrustorok.vehicleservice.vehicle;

import hu.bme.vik.ambrustorok.vehicleservice.common.EStyle;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleRegisterDTO {

    private double price;

    private int numberOfDoors;

    private double length;

    private String manufacturer;

    private String model;

    private EStyle style;

    private double weight;

    private double width;

    private int warranty;
}
