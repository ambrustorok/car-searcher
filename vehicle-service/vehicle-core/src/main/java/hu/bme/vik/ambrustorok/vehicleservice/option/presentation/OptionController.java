package hu.bme.vik.ambrustorok.vehicleservice.option.presentation;

import hu.bme.vik.ambrustorok.vehicleservice.dto.option.OptionRequest;
import hu.bme.vik.ambrustorok.vehicleservice.dto.option.OptionResponse;
import hu.bme.vik.ambrustorok.vehicleservice.dto.option.OptionServiceClient;
import hu.bme.vik.ambrustorok.vehicleservice.option.data.OptionEntity;
import hu.bme.vik.ambrustorok.vehicleservice.option.service.OptionServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/option")
@AllArgsConstructor
public class OptionController implements OptionServiceClient {

    private final OptionServiceImpl service;
    private final OptionMapper mapper;

    @GetMapping
    public ResponseEntity<Collection<OptionResponse>> findAll() {
        return ResponseEntity.ok(service.findAll().stream().map(mapper::EntityToDTO).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OptionResponse> findOne(@PathVariable UUID id) {
        return service.findOne(id).map(engineEntity -> ResponseEntity.ok(mapper.EntityToDTO(engineEntity))).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<OptionResponse> create(@RequestBody OptionRequest dto, UriComponentsBuilder b) {
        try {
            OptionEntity result = service.create(dto);

            UriComponents uriComponents = b.path("/investor/{id}").buildAndExpand(result.getId());
            return ResponseEntity.created(uriComponents.toUri()).body(mapper.EntityToDTO(result));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<OptionResponse> modify(@PathVariable UUID id, @RequestBody OptionResponse dto) {

        try {
            OptionEntity result = service.update(id, dto);

            return ResponseEntity.ok(mapper.EntityToDTO(result));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable UUID id) {

        boolean isRemoved = service.delete(id);

        if (!isRemoved) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().build();

    }
}