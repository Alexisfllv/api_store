package hub.com.api_store.service.domain;

import hub.com.api_store.entity.Waste;
import hub.com.api_store.exception.InsufficientStockException;
import hub.com.api_store.exception.ResourceNotFoundException;
import hub.com.api_store.nums.ExceptionMessages;
import hub.com.api_store.nums.GlobalStatus;
import hub.com.api_store.repo.WasteRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class WasteServiceDomain {

    private final WasteRepo wasteRepo;

    public void validateStock(BigDecimal quantityActual , BigDecimal quantityRequest){
        if (quantityActual.compareTo(quantityRequest)<0){
            throw new InsufficientStockException(ExceptionMessages.INSUFFICIENT_STOCK.message()+
                    quantityActual +
                    " requested: " +
                    quantityRequest);
        }
    }

    // id exist
    public Waste findById(Long id){
        return wasteRepo.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException(ExceptionMessages.RESOURCE_NOT_FOUND_ERROR.message()+id));
    }
}
