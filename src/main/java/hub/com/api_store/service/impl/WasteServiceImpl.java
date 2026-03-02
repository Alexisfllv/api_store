package hub.com.api_store.service.impl;

import hub.com.api_store.dto.waste.WasteDTORequest;
import hub.com.api_store.dto.waste.WasteDTOResponse;
import hub.com.api_store.entity.Inventory;
import hub.com.api_store.entity.Waste;
import hub.com.api_store.mapper.WasteMapper;
import hub.com.api_store.repo.InventoryRepo;
import hub.com.api_store.repo.WasteRepo;
import hub.com.api_store.service.WasteService;
import hub.com.api_store.service.domain.InventoryServiceDomain;
import hub.com.api_store.service.domain.WasteServiceDomain;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WasteServiceImpl implements WasteService {

    private final WasteMapper wasteMapper;
    private final WasteRepo wasteRepo;
    private final InventoryServiceDomain inventoryServiceDomain;
    private final WasteServiceDomain wasteServiceDomain;
    private final InventoryRepo inventoryRepo;
    private final Clock clock;

    // POST
    @Transactional
    @Override
    public WasteDTOResponse createWaste(WasteDTORequest wasteDTORequest) {
        // inventoryId
        Inventory inventory = inventoryServiceDomain.findById(wasteDTORequest.inventoryId());

        // validate Stock
        wasteServiceDomain.validateStock(inventory.getQuantity(), wasteDTORequest.quantity());

        // toWaste
        Waste waste = wasteMapper.toWaste(wasteDTORequest, inventory);
        waste.setWasteDate(LocalDateTime.now(clock));

        // inventory
        inventory.setQuantity(inventory.getQuantity().subtract(wasteDTORequest.quantity()));
        inventoryRepo.save(inventory);

        // toWasteDTOResponse
        Waste wasteSaved = wasteRepo.save(waste);
        return wasteMapper.toWasteDTOResponse(wasteSaved);
    }


}
