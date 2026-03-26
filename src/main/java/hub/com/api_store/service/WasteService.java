package hub.com.api_store.service;

import hub.com.api_store.dto.waste.WasteDTORequest;
import hub.com.api_store.dto.waste.WasteDTOResponse;
import hub.com.api_store.util.page.PageResponse;
import org.springframework.data.domain.PageRequest;

public interface WasteService {

    // GET
    PageResponse<WasteDTOResponse> findAllPage(int page, int size, String prop);

    WasteDTOResponse findByIdWaste(Long id);


    // POST
    WasteDTOResponse createWaste(WasteDTORequest wasteDTORequest);

    // DELETE
    void deleteWaste(Long id);
}
