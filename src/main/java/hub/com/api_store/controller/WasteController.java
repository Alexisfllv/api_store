package hub.com.api_store.controller;

import hub.com.api_store.dto.waste.WasteDTORequest;
import hub.com.api_store.dto.waste.WasteDTOResponse;
import hub.com.api_store.nums.WasteReason;
import hub.com.api_store.service.WasteService;
import hub.com.api_store.util.page.PageResponse;
import hub.com.api_store.util.response.GenericResponse;
import hub.com.api_store.util.response.StatusApi;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wastes")
public class WasteController {
    private final WasteService wasteService;

    // GET
    @GetMapping("/page")
    public ResponseEntity<GenericResponse<PageResponse<WasteDTOResponse>>> findAllPageGet(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String prop
    ) {
        PageResponse<WasteDTOResponse> response = wasteService.findAllPage(page, size, prop);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new GenericResponse<>(StatusApi.SUCCESS, response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenericResponse<WasteDTOResponse>> findByIdWasteGet(@PathVariable Long id){
        WasteDTOResponse response = wasteService.findByIdWaste(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new GenericResponse<>(StatusApi.SUCCESS,response));
    }

    @GetMapping("/by-reason")
    public ResponseEntity<GenericResponse<List<WasteDTOResponse>>> findAllByReasonGet(
            @RequestParam WasteReason reason
            ){
        List<WasteDTOResponse> res = wasteService.findAllWasteByReason(reason);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new GenericResponse<>(StatusApi.SUCCESS, res));
    }

    // Post
    @PostMapping
    public ResponseEntity<GenericResponse<WasteDTOResponse>> createWastePost(@Valid @RequestBody WasteDTORequest wasteDTORequest){
        WasteDTOResponse response = wasteService.createWaste(wasteDTORequest);
        return ResponseEntity.status(HttpStatus.CREATED).body
                (new GenericResponse<>(StatusApi.CREATED,response
        ));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWasteDelete(@PathVariable Long id){
        wasteService.deleteWaste(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
