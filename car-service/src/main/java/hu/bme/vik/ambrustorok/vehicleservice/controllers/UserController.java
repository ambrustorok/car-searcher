package hu.bme.vik.ambrustorok.vehicleservice.controllers;

import hu.bme.vik.ambrustorok.vehicleservice.model.Vehicle;
import hu.bme.vik.ambrustorok.vehicleservice.payload.request.SearchRequest;
import hu.bme.vik.ambrustorok.vehicleservice.repository.EngineRepository;
import hu.bme.vik.ambrustorok.vehicleservice.repository.VehicleRepository;
import hu.bme.vik.ambrustorok.vehicleservice.services.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/guest")
public class UserController {
    @Autowired
    VehicleService vehicleService;

    @GetMapping("/manufacturers")
    @ResponseBody
    public List<String> getAllManufacturers() {
        return vehicleService.getAllManufacturers();
    }

    @GetMapping("/manufacturers/{manufacturer}")
    @ResponseBody
    public List<String> getAllModelsOfManufacturer(@RequestParam String manufacturer) {
        return vehicleService.getAllModels(manufacturer);
    }

    @PostMapping("/search")
    @ResponseBody
    public List<String> search(@RequestBody SearchRequest searchRequest) {
        //TODO save in searchDB
        return vehicleService.search(searchRequest);
    }

    @Autowired
    EngineRepository engineRepository;

    @Autowired
    VehicleRepository vehicleRepository;

    @GetMapping("/vehicles")
    public List<Vehicle> vehicles() {
        return vehicleRepository.findAll();
    }


}