package hub.com.api_store.entity;


import hub.com.api_store.nums.GlobalUnit;
import hub.com.api_store.nums.PurchaseStatus;
import hub.com.api_store.util.auditing.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "purchases", indexes = {
        @Index(name = "idx_purchase_product", columnList = "product_id"),
        @Index(name = "idx_purchase_supplier", columnList = "supplier_id"),
        @Index(name = "idx_purchase_date", columnList = "purchase_date")
})
public class Purchase extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, precision = 10, scale = 3)
    private BigDecimal quantity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private GlobalUnit unit;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal costUnit;

    @Column(precision = 19, scale = 4)
    private BigDecimal totalCost;

    @Column(nullable = false)
    private LocalDateTime purchaseDate;

    @Column(length = 50)
    private String lot;

    private LocalDateTime expirationDate;

    @Column(length = 100)
    private String warehouseLocation;

    private LocalDateTime arrivalDate;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private PurchaseStatus status;

    @Column(length = 100)
    private String invoiceNumber;

    @Column(length = 500)
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_purchase_product"))
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_purchase_supplier"))
    private Supplier supplier;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_id",
            foreignKey = @ForeignKey(name = "fk_purchase_inventory"))
    private Inventory inventory;
}
