package hub.com.api_store.controller;

import hub.com.api_store.dto.waste.WasteDTORequest;
import hub.com.api_store.dto.waste.WasteDTOResponse;
import hub.com.api_store.service.WasteService;
import hub.com.api_store.util.response.GenericResponse;
import hub.com.api_store.util.response.StatusApi;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wastes")
public class WasteController {
    private final WasteService wasteService;

    // Post
    @PostMapping
    public ResponseEntity<GenericResponse<WasteDTOResponse>> createWastePost(@Valid @RequestBody WasteDTORequest wasteDTORequest){
        WasteDTOResponse response = wasteService.createWaste(wasteDTORequest);
        return ResponseEntity.status(HttpStatus.CREATED).body
                (new GenericResponse<>(StatusApi.CREATED,response
        ));
    }

}
