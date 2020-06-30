package au.edu.qut.test.rv.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
public class InventoryItem {

  @Id private UUID id;

  @Basic(optional = false)
  @Column(nullable = false)
  private String name;

  @Basic(optional = false)
  @Column(nullable = false)
  private OffsetDateTime releaseDate;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "manufacturer_id")
  private Manufacturer manufacturer;

  public InventoryItem id(UUID id) {
    this.id = id;
    return this;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public InventoryItem name(String name) {
    this.name = name;
    return this;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public InventoryItem releaseDate(OffsetDateTime releaseDate) {
    this.releaseDate = releaseDate;
    return this;
  }

  public OffsetDateTime getReleaseDate() {
    return releaseDate;
  }

  public void setReleaseDate(OffsetDateTime releaseDate) {
    this.releaseDate = releaseDate;
  }

  public InventoryItem manufacturer(Manufacturer manufacturer) {
    this.manufacturer = manufacturer;
    return this;
  }

  public Manufacturer getManufacturer() {
    return manufacturer;
  }

  public void setManufacturer(Manufacturer manufacturer) {
    this.manufacturer = manufacturer;
  }

  public au.edu.qut.test.rv.apiModel.InventoryItem toApi() {
    return new au.edu.qut.test.rv.apiModel.InventoryItem()
        .id(id)
        .name(name)
        .releaseDate(releaseDate)
        .manufacturer(manufacturer.toApi());
  }

  public static InventoryItem valueOf(au.edu.qut.test.rv.apiModel.InventoryItem item) {
    InventoryItem inventoryItem = new InventoryItem();
    inventoryItem.setId(item.getId());
    inventoryItem.setName(item.getName());
    inventoryItem.setReleaseDate(item.getReleaseDate());
    inventoryItem.setManufacturer(Manufacturer.valueOf(item.getManufacturer()));
    return inventoryItem;
  }
}
