package au.edu.qut.test.rv;

import au.edu.qut.test.rv.api.ErrorResponse;
import au.edu.qut.test.rv.model.InventoryItem;
import au.edu.qut.test.rv.model.Manufacturer;
import au.edu.qut.test.rv.repositories.InventoryItemRepository;
import au.edu.qut.test.rv.repositories.ManufacturerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.ResolvableType;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = {"au.edu.qut.user=test", "au.edu.qut.password=test"})
class TestApplicationTests {

  @Autowired private TestRestTemplate restTemplate;
  @Autowired private InventoryItemRepository itemRepository;
  @Autowired protected ManufacturerRepository manufacturerRepository;

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
  void dontAllowWrongPassword() {
    ResponseEntity<?> response =
        restTemplate.exchange(
            RequestEntity.get(URI.create("/inventory"))
                .header("Authorization", "Basic dGVzdDp3cm9uZ1Bhc3N3b3Jk")
                .build(),
            Object.class);
    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
  }

  @Test
  void dontAllowUnauthenticated() {
    ResponseEntity<?> response =
        restTemplate.exchange(RequestEntity.get(URI.create("/inventory")).build(), Object.class);
    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
  }

  @Test
  void bringAllData() {
    ResponseEntity<List<InventoryItem>> response =
        restTemplate.exchange(
            RequestEntity.get(URI.create("/inventory"))
                .header("Authorization", "Basic dGVzdDp0ZXN0")
                .build(),
            ParameterizedTypeReference.forType(
                ResolvableType.forClassWithGenerics(List.class, InventoryItem.class).getType()));

    List<InventoryItem> data = response.getBody();

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(data);
    assertEquals(5, data.size());

    InventoryItem item = data.get(0);
    Manufacturer manufacturer = item.getManufacturer();

    assertEquals(UUID.fromString("d290f1ee-6c54-4b01-90e6-d701748f0851"), item.getId());
    assertEquals("Awesome item #1", item.getName());

    assertEquals("Awesome manufacturer #1", manufacturer.getName());
    assertEquals("0414700000", manufacturer.getPhone());
    assertEquals("www.myawesomepage.com", manufacturer.getHomePage());
  }

  @Test
  void bringThirdAndFourth() {
    ResponseEntity<List<InventoryItem>> response =
        restTemplate.exchange(
            RequestEntity.get(
                    UriComponentsBuilder.fromPath("/inventory")
                        .queryParam("skip", 2)
                        .queryParam("limit", 2)
                        .build()
                        .toUri())
                .header("Authorization", "Basic dGVzdDp0ZXN0")
                .build(),
            ParameterizedTypeReference.forType(
                ResolvableType.forClassWithGenerics(List.class, InventoryItem.class).getType()));

    List<InventoryItem> data = response.getBody();

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(data);
    assertEquals(2, data.size());
    assertEquals("Awesome item #3", data.get(0).getName());
    assertEquals("Awesome item #4", data.get(1).getName());
  }

  @Test
  void dontAllowInvalidSkipValue() {
    ResponseEntity<ErrorResponse> response =
        restTemplate.exchange(
            RequestEntity.get(
                    UriComponentsBuilder.fromPath("/inventory")
                        .queryParam("skip", -1)
                        .queryParam("limit", 2)
                        .build()
                        .toUri())
                .header("Authorization", "Basic dGVzdDp0ZXN0")
                .build(),
            ErrorResponse.class);

    ErrorResponse data = response.getBody();

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertNotNull(data);
    assertEquals("_inventoryGet.skip must be greater than or equal to 0", data.getDescription());
  }

  @Test
  void bringExistingItem() {
    ResponseEntity<InventoryItem> response =
        restTemplate.exchange(
            RequestEntity.get(URI.create("/inventory/d290f1ee-6c54-4b01-90e6-d701748f0851"))
                .header("Authorization", "Basic dGVzdDp0ZXN0")
                .build(),
            InventoryItem.class);

    InventoryItem data = response.getBody();

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(data);
    assertEquals("Awesome item #1", data.getName());
  }

  @Test
  void returnNotFoundForNonExistingItem() {
    ResponseEntity<?> response =
        restTemplate.exchange(
            RequestEntity.get(URI.create("/inventory/d290f1ee-6c54-4b01-90e6-d701748f0850"))
                .header("Authorization", "Basic dGVzdDp0ZXN0")
                .build(),
            Object.class);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  void addNewItem() {

    Manufacturer manufacturer = new Manufacturer();
    manufacturer.setName("Awesome manufacturer #6");
    manufacturer.setPhone("0414700000");
    manufacturer.setHomePage("www.myawesomepage.com");

    InventoryItem inventoryItem = new InventoryItem();
    inventoryItem.setId(UUID.fromString("d290f1ee-6c54-4b01-90e6-d701748f0860"));
    inventoryItem.setName("Awesome item #6");
    inventoryItem.setReleaseDate(OffsetDateTime.now());
    inventoryItem.setManufacturer(manufacturer);

    ResponseEntity<?> response =
        restTemplate.exchange(
            RequestEntity.post(URI.create("/inventory"))
                .header("Authorization", "Basic dGVzdDp0ZXN0")
                .header("Content-type", "application/json")
                .body(inventoryItem),
            Object.class);

    assertEquals(HttpStatus.OK, response.getStatusCode());

    ResponseEntity<List<InventoryItem>> responseForAll =
        restTemplate.exchange(
            RequestEntity.get(
                    UriComponentsBuilder.fromPath("/inventory")
                        .queryParam("skip", 5)
                        .queryParam("limit", 1)
                        .build()
                        .toUri())
                .header("Authorization", "Basic dGVzdDp0ZXN0")
                .build(),
            ParameterizedTypeReference.forType(
                ResolvableType.forClassWithGenerics(List.class, InventoryItem.class).getType()));

    List<InventoryItem> data = responseForAll.getBody();

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(data);
    assertEquals(1, data.size());
    assertEquals("Awesome item #6", data.get(0).getName());
  }
}
