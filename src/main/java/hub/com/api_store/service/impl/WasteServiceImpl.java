package hub.com.api_store.service.impl;

import hub.com.api_store.dto.waste.WasteDTORequest;
import hub.com.api_store.dto.waste.WasteDTOResponse;
import hub.com.api_store.entity.Inventory;
import hub.com.api_store.entity.Waste;
import hub.com.api_store.mapper.WasteMapper;
import hub.com.api_store.nums.WasteReason;
import hub.com.api_store.repo.InventoryRepo;
import hub.com.api_store.repo.WasteRepo;
import hub.com.api_store.service.WasteService;
import hub.com.api_store.service.domain.InventoryServiceDomain;
import hub.com.api_store.service.domain.WasteServiceDomain;
import hub.com.api_store.util.page.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

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

    // GET
    @Override
    public PageResponse<WasteDTOResponse> findAllPage(int page, int size, String prop) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, prop));
        Page<Waste> wastePage = wasteRepo.findAll(pageable);
        return new PageResponse<>(
                wastePage.getContent().stream().map(wasteMapper::toWasteDTOResponse).toList(),
                wastePage.getNumber(),
                wastePage.getSize(),
                wastePage.getTotalElements(),
                wastePage.getTotalPages()
        );
    }

    @Override
    public WasteDTOResponse findByIdWaste(Long id) {
        Waste waste = wasteServiceDomain.findById(id);
        return wasteMapper.toWasteDTOResponse(waste);
    }

    @Override
    public List<WasteDTOResponse> findAllWasteByReason(WasteReason reason) {
        List<Waste> wasteList = wasteRepo.findByReason(reason);
        return wasteList.stream()
                .map(wasteMapper::toWasteDTOResponse)
                .toList();
    }

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

    @Transactional
    @Override
    public void deleteWaste(Long id) {
        Waste waste = wasteServiceDomain.findById(id);
        Inventory inventory = waste.getInventory();
        inventory.setQuantity(inventory.getQuantity().add(waste.getQuantity()));
        inventoryRepo.save(inventory);
        wasteRepo.delete(waste);
    }


}
