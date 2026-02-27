package hub.com.api_store.entity;

import hub.com.api_store.nums.NotificationType;
import hub.com.api_store.util.auditing.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Notification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private NotificationType type;

    @Column(nullable = false)
    private String message;

    private Long inventoryId;
    private String productName;
    private String lot;
    private LocalDateTime expirationDate;
    private Long daysUntilExpiration;
    private boolean read;
    private LocalDateTime sentAt;
}