package au.edu.qut.test.rv.api;

import au.edu.qut.test.rv.apiModel.InventoryItem;
import au.edu.qut.test.rv.services.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
public class MainInventoryApi implements InventoryApi {

  @Autowired private InventoryService inventoryService;

  @Override
  public ResponseEntity<List<InventoryItem>> inventoryGet(Integer skip, Integer limit) {
    return ResponseEntity.ok(
        inventoryService.findInventoryItems(skip, limit).stream()
            .map(au.edu.qut.test.rv.model.InventoryItem::toApi)
            .collect(Collectors.toList()));
  }

  @Override
  public ResponseEntity<InventoryItem> inventoryIdGet(UUID id) {
    return inventoryService
        .findInventoryItem(id)
        .map(au.edu.qut.test.rv.model.InventoryItem::toApi)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @Override
  public ResponseEntity<Void> inventoryPost(InventoryItem inventoryItem) {
    inventoryService.addInventoryItem(
        au.edu.qut.test.rv.model.InventoryItem.valueOf(inventoryItem));
    return ResponseEntity.ok().build();
  }
}
