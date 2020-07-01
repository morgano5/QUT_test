package au.edu.qut.test.rv.api;

import au.edu.qut.test.rv.model.InventoryItem;
import au.edu.qut.test.rv.model.Manufacturer;
import au.edu.qut.test.rv.repositories.InventoryItemRepository;
import au.edu.qut.test.rv.repositories.ManufacturerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {"au.edu.qut.user=test", "au.edu.qut.password=test"})
@AutoConfigureMockMvc
public class MainInventoryApiTest {

  @Autowired private MockMvc mockMvc;
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
  void dontAllowUnauthenticated() throws Exception {

    mockMvc.perform(get("/inventory")).andExpect(status().isUnauthorized());
  }

  @Test
  void dontAllowWrongPassword() throws Exception {

    mockMvc
        .perform(get("/inventory").header("Authorization", "Basic dGVzdDp3cm9uZ1Bhc3N3b3Jk"))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void bringAllData() throws Exception {

    mockMvc
        .perform(get("/inventory").header("Authorization", "Basic dGVzdDp0ZXN0"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$", hasSize(5)))
        .andExpect(jsonPath("$[0].name", equalTo("Awesome item #1")))
        .andExpect(jsonPath("$[0].id", equalTo("d290f1ee-6c54-4b01-90e6-d701748f0851")))
        .andExpect(jsonPath("$[0].manufacturer.name", equalTo("Awesome manufacturer #1")))
        .andExpect(jsonPath("$[0].manufacturer.phone", equalTo("0414700000")))
        .andExpect(jsonPath("$[0].manufacturer.homePage", equalTo("www.myawesomepage.com")))
        .andExpect(jsonPath("$[1].name", equalTo("Awesome item #2")))
        .andExpect(jsonPath("$[1].manufacturer.name", equalTo("Awesome manufacturer #2")))
        .andExpect(jsonPath("$[2].name", equalTo("Awesome item #3")))
        .andExpect(jsonPath("$[2].manufacturer.name", equalTo("Awesome manufacturer #3")))
        .andExpect(jsonPath("$[3].name", equalTo("Awesome item #4")))
        .andExpect(jsonPath("$[3].manufacturer.name", equalTo("Awesome manufacturer #4")))
        .andExpect(jsonPath("$[4].name", equalTo("Awesome item #5")))
        .andExpect(jsonPath("$[4].manufacturer.name", equalTo("Awesome manufacturer #5")));
  }

  @Test
  void bringThirdAndFourth() throws Exception {

    mockMvc
        .perform(
            get("/inventory")
                .queryParam("skip", "2")
                .queryParam("limit", "2")
                .header("Authorization", "Basic dGVzdDp0ZXN0"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].name", equalTo("Awesome item #3")))
        .andExpect(jsonPath("$[0].manufacturer.name", equalTo("Awesome manufacturer #3")))
        .andExpect(jsonPath("$[1].name", equalTo("Awesome item #4")))
        .andExpect(jsonPath("$[1].manufacturer.name", equalTo("Awesome manufacturer #4")));
  }

  @Test
  void dontAllowInvalidSkipValue() throws Exception {

    mockMvc
        .perform(
            get("/inventory")
                .queryParam("skip", "-1")
                .queryParam("limit", "2")
                .header("Authorization", "Basic dGVzdDp0ZXN0"))
        .andExpect(status().isBadRequest())
        .andExpect(
            jsonPath(
                "$.description", equalTo("_inventoryGet.skip must be greater than or equal to 0")));
  }

  @Test
  void bringExistingItem() throws Exception {
    mockMvc
        .perform(
            get("/inventory/d290f1ee-6c54-4b01-90e6-d701748f0851")
                .header("Authorization", "Basic dGVzdDp0ZXN0"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name", equalTo("Awesome item #1")))
        .andExpect(jsonPath("$.manufacturer.name", equalTo("Awesome manufacturer #1")));
  }

  @Test
  void returnNotFoundForNonExistingItem() throws Exception {

    mockMvc
        .perform(
            get("/inventory/d290f1ee-6c54-4b01-90e6-d701748f0850")
                .header("Authorization", "Basic dGVzdDp0ZXN0"))
        .andExpect(status().isNotFound());
  }

  @Test
  void addNewItem() throws Exception {

    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());

    Manufacturer manufacturer = new Manufacturer();
    manufacturer.setName("Awesome manufacturer #6");
    manufacturer.setPhone("0414700000");
    manufacturer.setHomePage("www.myawesomepage.com");

    InventoryItem inventoryItem = new InventoryItem();
    inventoryItem.setId(UUID.fromString("d290f1ee-6c54-4b01-90e6-d701748f0860"));
    inventoryItem.setName("Awesome item #6");
    inventoryItem.setReleaseDate(OffsetDateTime.now());
    inventoryItem.setManufacturer(manufacturer);

    mockMvc
        .perform(
            post("/inventory")
                .header("Authorization", "Basic dGVzdDp0ZXN0")
                .contentType("application/json")
                .content(mapper.writeValueAsBytes(inventoryItem)))
        .andExpect(status().isOk());

    mockMvc
        .perform(
            get("/inventory")
                .queryParam("skip", "5")
                .queryParam("limit", "1")
                .header("Authorization", "Basic dGVzdDp0ZXN0"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].name", equalTo("Awesome item #6")));
  }
}
