package hu.bme.vik.ambrustorok.vehicleservice.option.service;

import hu.bme.vik.ambrustorok.vehicleservice.option.OptionDTO;
import hu.bme.vik.ambrustorok.vehicleservice.option.OptionRegisterDTO;
import hu.bme.vik.ambrustorok.vehicleservice.option.data.OptionEntity;
import hu.bme.vik.ambrustorok.vehicleservice.option.data.OptionRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Optional;
import java.util.UUID;


@Service
@AllArgsConstructor
@Slf4j
public class OptionService {

    private OptionRepository repository;

    @PostConstruct
    public void mock() {
        OptionEntity entity1 = new OptionEntity();
        OptionEntity entity2 = new OptionEntity();

        entity1.setId(UUID.fromString("61638f8c-8e62-4a76-8fab-4e1650f9e1cb"));
        entity1.setName("AC");
        entity1.setValue("Automatic");
        entity1.setPrice(1500);

        entity2.setId(UUID.fromString("c885829c-8c4b-4413-96f6-34c3d9f70ab5"));
        entity2.setName("AC");
        entity2.setValue("Manual");
        entity2.setPrice(500);

        repository.save(entity1);
        repository.save(entity2);
    }

    @PreDestroy
    public void reset() {
        repository.deleteAll();
    }

    public Page<OptionEntity> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Optional<OptionEntity> findOne(UUID id) {
        return repository.findById(id);
    }

    public OptionEntity create(OptionRegisterDTO dto) {
        log.debug("Creating new Option {}", dto);

        OptionEntity entity = new OptionEntity();
        entity.setName((dto.getName()));
        entity.setValue(dto.getValue());
        entity.setPrice(dto.getPrice());

        return repository.save(entity);
    }

    public OptionEntity update(UUID id, OptionDTO dto) {
        OptionEntity entity = repository.getOne(id);
        entity.setName((dto.getName()));
        entity.setValue(dto.getValue());
        entity.setPrice(dto.getPrice());

        return repository.save(entity);

    }

    public boolean delete(UUID id) {
        boolean exists = repository.existsById(id);
        if (exists) {
            repository.deleteById(id);
        }
        return exists;
    }
}