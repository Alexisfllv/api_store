package hub.com.api_store.service.domain;

import hub.com.api_store.exception.InsufficientStockException;
import hub.com.api_store.nums.ExceptionMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class WasteServiceDomain {

    public void validateStock(BigDecimal quantityActual , BigDecimal quantityRequest){
        if (quantityActual.compareTo(quantityRequest)<0){
            throw new InsufficientStockException(ExceptionMessages.INSUFFICIENT_STOCK.message()+
                    quantityActual +
                    " requested: " +
                    quantityRequest);
        }
    }
}
