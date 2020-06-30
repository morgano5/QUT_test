package au.edu.qut.test.rv.repositories;

import au.edu.qut.test.rv.model.InventoryItem;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface InventoryItemRepository extends PagingAndSortingRepository<InventoryItem, UUID> {}
