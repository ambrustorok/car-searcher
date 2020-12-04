package hu.bme.vik.ambrustorok.searchservice.searchdata.data;

import hu.bme.vik.ambrustorok.vehicleservice.common.EStyle;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
public class SearchEntity {

    @Id
    private UUID id = UUID.randomUUID();
    @Column()
    private double lengthMin;
    @Column()
    private double lengthMax;
    @Column()
    private String manufacturer;
    @Column()
    private String model;
    @Column()
    private int numberOfDoorsMin;
    @Column()
    private int numberOfDoorsMax;
    @Column()
    private double priceMin;
    @Column()
    private double priceMax;
    @Column()
    private EStyle style;
    @Column()
    private String username;
    @Column()
    private int warrantyMin;
    @Column()
    private int warrantyMax;
    @Column()
    private double weightMin;
    @Column()
    private double weightMax;
    @Column()
    private double widthMin;
    @Column()
    private double widthMax;
    @OneToMany(mappedBy = "search")
    private Set<OptionSearchEntity> options;
    @OneToMany(mappedBy = "search")
    private Set<EngineSearchEntity> engines;

}
