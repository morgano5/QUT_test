package au.edu.qut.test.rv.repositories;

import au.edu.qut.test.rv.model.Manufacturer;
import org.springframework.data.repository.CrudRepository;

public interface ManufacturerRepository extends CrudRepository<Manufacturer, Long> {

  Manufacturer findByName(String name);
}
