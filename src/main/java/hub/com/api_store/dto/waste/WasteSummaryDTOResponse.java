package hub.com.api_store.dto.waste;

import hub.com.api_store.nums.WasteReason;

import java.math.BigDecimal;
import java.util.Map;

public record WasteSummaryDTOResponse(
        Long totalWastes,
        BigDecimal totalQuantityLost,
        Map<WasteReason,BigDecimal> byReason
) {}
