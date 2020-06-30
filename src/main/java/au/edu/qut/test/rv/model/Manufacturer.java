package au.edu.qut.test.rv.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Manufacturer {

  @Id @GeneratedValue private long id;

  @Basic(optional = false)
  @Column(nullable = false)
  private String name;

  private String homePage;

  private String phone;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public Manufacturer name(String name) {
    this.name = name;
    return this;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Manufacturer homePage(String homePage) {
    this.homePage = homePage;
    return this;
  }

  public String getHomePage() {
    return homePage;
  }

  public void setHomePage(String homePage) {
    this.homePage = homePage;
  }

  public Manufacturer phone(String phone) {
    this.phone = phone;
    return this;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public au.edu.qut.test.rv.apiModel.Manufacturer toApi() {
    return new au.edu.qut.test.rv.apiModel.Manufacturer()
        .name(name)
        .homePage(homePage)
        .phone(phone);
  }

  public static Manufacturer valueOf(au.edu.qut.test.rv.apiModel.Manufacturer apiManufacturer) {
    Manufacturer manufacturer = new Manufacturer();
    manufacturer.setName(apiManufacturer.getName());
    manufacturer.setHomePage(apiManufacturer.getHomePage());
    manufacturer.setPhone(apiManufacturer.getPhone());
    return manufacturer;
  }
}
