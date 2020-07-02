package au.edu.qut.test.rv.services;

import au.edu.qut.test.rv.model.InventoryItem;
import au.edu.qut.test.rv.model.Manufacturer;
import au.edu.qut.test.rv.repositories.InventoryItemRepository;
import au.edu.qut.test.rv.repositories.ManufacturerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(properties = {"au.edu.qut.user=test", "au.edu.qut.password=test"})
class InventoryServiceTest {

  @Autowired private InventoryItemRepository itemRepository;
  @Autowired private ManufacturerRepository manufacturerRepository;
  @Autowired private InventoryService service;

  @BeforeEach
  void setUp() {
    Manufacturer manufacturer;
    InventoryItem inventoryItem;

    manufacturer = new Manufacturer();
    manufacturer.setName("Awesome manufacturer #1");
    manufacturer.setPhone("0414700000");
    manufacturer.setHomePage("www.myawesomepage.com");
    manufacturerRepository.save(manufacturer);

    inventoryItem = new InventoryItem();
    inventoryItem.setId(UUID.fromString("d290f1ee-6c54-4b01-90e6-d701748f0851"));
    inventoryItem.setName("Awesome item #1");
    inventoryItem.setReleaseDate(OffsetDateTime.now());
    inventoryItem.setManufacturer(manufacturer);
    itemRepository.save(inventoryItem);

    manufacturer = new Manufacturer();
    manufacturer.setName("Awesome manufacturer #2");
    manufacturerRepository.save(manufacturer);

    inventoryItem = new InventoryItem();
    inventoryItem.setId(UUID.randomUUID());
    inventoryItem.setName("Awesome item #2");
    inventoryItem.setReleaseDate(OffsetDateTime.now());
    inventoryItem.setManufacturer(manufacturer);
    itemRepository.save(inventoryItem);

    manufacturer = new Manufacturer();
    manufacturer.setName("Awesome manufacturer #3");
    manufacturerRepository.save(manufacturer);

    inventoryItem = new InventoryItem();
    inventoryItem.setId(UUID.randomUUID());
    inventoryItem.setName("Awesome item #3");
    inventoryItem.setReleaseDate(OffsetDateTime.now());
    inventoryItem.setManufacturer(manufacturer);
    itemRepository.save(inventoryItem);

    manufacturer = new Manufacturer();
    manufacturer.setName("Awesome manufacturer #4");
    manufacturerRepository.save(manufacturer);

    inventoryItem = new InventoryItem();
    inventoryItem.setId(UUID.randomUUID());
    inventoryItem.setName("Awesome item #4");
    inventoryItem.setReleaseDate(OffsetDateTime.now());
    inventoryItem.setManufacturer(manufacturer);
    itemRepository.save(inventoryItem);

    manufacturer = new Manufacturer();
    manufacturer.setName("Awesome manufacturer #5");
    manufacturerRepository.save(manufacturer);

    inventoryItem = new InventoryItem();
    inventoryItem.setId(UUID.randomUUID());
    inventoryItem.setName("Awesome item #5");
    inventoryItem.setReleaseDate(OffsetDateTime.now());
    inventoryItem.setManufacturer(manufacturer);
    itemRepository.save(inventoryItem);
  }

  @AfterEach
  void tearDown() {
    itemRepository.deleteAll();
    manufacturerRepository.deleteAll();
  }

  @Test
  void bringAllData() {

    List<InventoryItem> items = service.findInventoryItems(null, null);

    assertNotNull(items);
    assertEquals(5, items.size());
    assertNotNull(items.get(0).getId());
    assertEquals("d290f1ee-6c54-4b01-90e6-d701748f0851", items.get(0).getId().toString());
    assertEquals("Awesome item #1", items.get(0).getName());
    assertNotNull(items.get(0).getManufacturer());
    assertEquals("Awesome manufacturer #1", items.get(0).getManufacturer().getName());
    assertEquals("0414700000", items.get(0).getManufacturer().getPhone());
    assertEquals("www.myawesomepage.com", items.get(0).getManufacturer().getHomePage());
  }

  @Test
  void bringThirdAndFourth() {

    List<InventoryItem> items = service.findInventoryItems(2, 2);

    assertNotNull(items);
    assertEquals(2, items.size());
    assertNotNull(items.get(0).getId());
    assertEquals("Awesome item #3", items.get(0).getName());
    assertNotNull(items.get(0).getManufacturer());
    assertEquals("Awesome manufacturer #3", items.get(0).getManufacturer().getName());
    assertEquals("Awesome item #4", items.get(1).getName());
    assertNotNull(items.get(1).getManufacturer());
    assertEquals("Awesome manufacturer #4", items.get(1).getManufacturer().getName());
  }

  @Test
  void findFirstInventoryItem() {

    Optional<InventoryItem> optItem =
        service.findInventoryItem(UUID.fromString("d290f1ee-6c54-4b01-90e6-d701748f0851"));

    assertTrue(optItem.isPresent());

    InventoryItem item = optItem.get();
    assertEquals("d290f1ee-6c54-4b01-90e6-d701748f0851", item.getId().toString());
    assertEquals("Awesome item #1", item.getName());
    assertNotNull(item.getManufacturer());
    assertEquals("Awesome manufacturer #1", item.getManufacturer().getName());
    assertEquals("0414700000", item.getManufacturer().getPhone());
    assertEquals("www.myawesomepage.com", item.getManufacturer().getHomePage());
  }

  @Test
  void addInventoryItem() {

    Manufacturer manufacturer;
    InventoryItem inventoryItem;

    manufacturer = new Manufacturer();
    manufacturer.setName("Awesome manufacturer #6");
    manufacturer.setPhone("0414700006");
    manufacturer.setHomePage("www.myawesomepage6.com");
    inventoryItem = new InventoryItem();
    inventoryItem.setId(UUID.fromString("d290f1ee-6c54-4b01-90e6-d701748f0856"));
    inventoryItem.setName("Awesome item #6");
    inventoryItem.setReleaseDate(OffsetDateTime.now());
    inventoryItem.setManufacturer(manufacturer);

    service.addInventoryItem(inventoryItem);

    Optional<InventoryItem> optItem =
            service.findInventoryItem(UUID.fromString("d290f1ee-6c54-4b01-90e6-d701748f0856"));

    assertTrue(optItem.isPresent());

    InventoryItem item = optItem.get();
    assertEquals("d290f1ee-6c54-4b01-90e6-d701748f0856", item.getId().toString());
    assertEquals("Awesome item #6", item.getName());
    assertNotNull(item.getManufacturer());
    assertEquals("Awesome manufacturer #6", item.getManufacturer().getName());
    assertEquals("0414700006", item.getManufacturer().getPhone());
    assertEquals("www.myawesomepage6.com", item.getManufacturer().getHomePage());

  }
}
