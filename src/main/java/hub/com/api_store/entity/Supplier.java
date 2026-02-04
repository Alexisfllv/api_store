package hub.com.api_store.entity;


import hub.com.api_store.nums.CategoryStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "suppliers", indexes = {
        @Index(name = "idx_supplier_name", columnList = "name"),
        @Index(name = "idx_supplier_status", columnList = "status")
})
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 12,unique = true)
    private String phone;

    @Column(nullable = false, length = 100,unique = true)
    private String email;

    @Column(nullable = false, length = 200)
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoryStatus status;
}
