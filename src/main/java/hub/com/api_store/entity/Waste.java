package hub.com.api_store.entity;

import hub.com.api_store.nums.GlobalUnit;
import hub.com.api_store.nums.WasteReason;
import hub.com.api_store.util.auditing.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "wastes")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Waste extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, precision = 10, scale = 3)
    private BigDecimal quantity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private GlobalUnit unit;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private WasteReason reason;

    @Column(length = 500)
    private String notes;

    private LocalDateTime wasteDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_waste_inventory"))
    private Inventory inventory;
}