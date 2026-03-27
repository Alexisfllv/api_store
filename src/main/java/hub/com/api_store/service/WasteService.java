package hub.com.api_store.service;

import hub.com.api_store.dto.waste.WasteDTORequest;
import hub.com.api_store.dto.waste.WasteDTOResponse;
import hub.com.api_store.dto.waste.WasteSummaryDTOResponse;
import hub.com.api_store.nums.WasteReason;
import hub.com.api_store.util.page.PageResponse;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;

public interface WasteService {

    // GET
    PageResponse<WasteDTOResponse> findAllPage(int page, int size, String prop);

    WasteDTOResponse findByIdWaste(Long id);

    List<WasteDTOResponse> findAllWasteByReason(WasteReason reason);

    List<WasteDTOResponse> findAllWasteByWasteDateBetween(LocalDateTime start, LocalDateTime end);

    WasteSummaryDTOResponse getSumaryWaste();

    // POST
    WasteDTOResponse createWaste(WasteDTORequest wasteDTORequest);

    // DELETE
    void deleteWaste(Long id);
}
