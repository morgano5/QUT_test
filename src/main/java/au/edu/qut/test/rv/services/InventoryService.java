package au.edu.qut.test.rv.services;

import au.edu.qut.test.rv.model.InventoryItem;
import au.edu.qut.test.rv.model.Manufacturer;
import au.edu.qut.test.rv.repositories.InventoryItemRepository;
import au.edu.qut.test.rv.repositories.ManufacturerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class InventoryService {

  private static final Integer MAX_LIMIT = 50;

  @Autowired private InventoryItemRepository itemRepository;

  @Autowired private ManufacturerRepository manufacturerRepository;

  public List<InventoryItem> findInventoryItems(Integer skip, Integer limit) {
    if (skip == null) {
      if (limit == null) {
        List<InventoryItem> list = new ArrayList<>();
        itemRepository.findAll().iterator().forEachRemaining(list::add);
        return list;
      } else {
        skip = 0;
      }
    }
    if (limit == null) {
      limit = MAX_LIMIT;
    }
    Pageable pageable = new OffsetPageRequest(skip, limit, Sort.by("name"));
    return itemRepository.findAll(pageable).getContent();
  }

  public Optional<InventoryItem> findInventoryItem(UUID id) {
    return itemRepository.findById(id);
  }

  public void addInventoryItem(InventoryItem inventoryItem) {
    itemRepository
        .findById(inventoryItem.getId())
        .ifPresent(item -> inventoryItem.getManufacturer().setId(item.getManufacturer().getId()));
    Manufacturer manufacturer = manufacturerRepository.save(inventoryItem.getManufacturer());
    inventoryItem.setManufacturer(manufacturer);
    itemRepository.save(inventoryItem);
  }
}
