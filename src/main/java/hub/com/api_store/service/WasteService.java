package hub.com.api_store.service;

import hub.com.api_store.dto.waste.WasteDTORequest;
import hub.com.api_store.dto.waste.WasteDTOResponse;

public interface WasteService {

    // POST
    WasteDTOResponse createWaste(WasteDTORequest wasteDTORequest);
}
