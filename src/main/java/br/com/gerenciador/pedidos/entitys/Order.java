package br.com.gerenciador.pedidos.entitys;

import br.com.gerenciador.pedidos.core.converters.OrderStatusConverter;
import br.com.gerenciador.pedidos.enums.OrderStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "ORDERS")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    @Id
    @Column(name = "ID")
    private String id;
    @Convert(converter = OrderStatusConverter.class)
    @Column(name = "STATUS")
    private OrderStatus status;
    @Column(name = "CREATION_DATE")
    private LocalDateTime creationDate;
    @Column(name = "COMPLETED_DATE")
    private LocalDateTime completedDate;
    @Version
    @Column(name = "VERSION")
    private Long version;
    @Column(name = "PAYMENT_IDEM_KEY", length = 100)
    private String paymentIdemKey;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();
    @Column(name = "DETAILS")
    private String details;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "USER_ID")
    private User user;

    @PrePersist
    public void prePersist() {
        this.id = UUID.randomUUID().toString();
        this.creationDate = Objects.isNull(this.creationDate) ? LocalDateTime.now() : this.creationDate;
        this.status = this.status == null ? OrderStatus.PENDING : this.status;
        this.details = StringUtils.isBlank(this.details) ? "Pedido criado com sucesso." : this.details;
    }

    public void markPaid() {
        if (status != OrderStatus.PENDING) {
            throw new IllegalStateException("Pedido não está autorizado para pagamento");
        }
        this.status = OrderStatus.PAID;
        this.completedDate = LocalDateTime.now();
        this.details = "Pagamento realizado com sucesso";
    }

    public void cancel(String msg) {
        if (status == OrderStatus.PAID) throw new IllegalStateException("Pedido já pago; não pode ser cancelado");
        if (status == OrderStatus.CANCELED) return;
        this.status = OrderStatus.CANCELED;
        this.completedDate = LocalDateTime.now();
        this.details = msg;
    }

    public void ensurePaymentIdemKey() {
        if (StringUtils.isBlank(this.paymentIdemKey)) {
            this.paymentIdemKey = UUID.randomUUID().toString();
        }
    }

}
