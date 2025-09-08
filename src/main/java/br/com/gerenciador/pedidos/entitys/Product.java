package br.com.gerenciador.pedidos.entitys;


import br.com.gerenciador.pedidos.core.records.input.ProductInput;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "PRODUCT")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @Column(name = "ID")
    private String id;
    @Column(name = "NAME")
    private String name;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "PRICE")
    private BigDecimal price;
    @Column(name = "CATEGORY")
    private String category;
    @Column(name = "QUANTITY")
    private Integer quantity;
    @Column(name = "CREATION_DATE")
    private LocalDateTime creationDate;
    @Column(name = "UPDATE_DATE")
    private LocalDateTime updateDate;
    @Version
    @Column(name = "VERSION")
    private Long version;

    @PrePersist
    public void prePersist() {
        this.id = UUID.randomUUID().toString();
        this.creationDate = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updateDate = LocalDateTime.now();
    }

    public Product(@Valid ProductInput request) {
        this.quantity = request.quantity();
        this.category = request.category();
        this.price = request.price();
        this.description = request.description();
        this.name = request.name();
    }

    public void setValuesFromInputRequest(Product product, ProductInput request) {
        product.setQuantity(request.quantity());
        product.setCategory(request.category());
        product.setPrice(request.price());
        product.setDescription(request.description());
        product.setName(request.name());
    }


}
