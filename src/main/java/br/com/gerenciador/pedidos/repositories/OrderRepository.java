package br.com.gerenciador.pedidos.repositories;

import br.com.gerenciador.pedidos.entitys.Order;
import br.com.gerenciador.pedidos.entitys.Product;
import br.com.gerenciador.pedidos.entitys.User;
import br.com.gerenciador.pedidos.enums.OrderStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, String> {

    @Query("select o.id from Order o where o.user.id = :userId")
    Page<String> findByUserId(String userId, Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select o from Order o where o.id = :id")
    Optional<Order> lockById(@Param("id") String id);

    @Query("""
                select distinct o
                from Order o
                join o.items i
                where o.status = :status
                  and i.product.id = :productId
            """)
    List<Order> findByStatusAndProductId(
            @Param("status") OrderStatus status,
            @Param("productId") String productId);

    @Query("""
              select  o.user.id as userId,
                     sum(i.quantity * i.price) as totalSpent
              from Order o
              join o.items i
              where o.status = :status
              group by o.user.id
              order by sum(i.quantity * i.price) desc
            """)
    List<Object[]> topBuyersRaw(@Param("status") OrderStatus status, Pageable pageable);


    @Query("""
              select o.user.id,
                     sum(i.price * i.quantity),
                     count(distinct o.id)
              from Order o
                join o.items i
              where o.status = :status
              group by o.user.id
            """)
    List<Object[]> avgTicketPerUser(@Param("status") OrderStatus status);


    @Transactional(readOnly = true)
    @Query("""
              select coalesce(sum(i.price * i.quantity), 0)
              from Order o
                join o.items i
              where o.status = :status
                and o.completedDate >= :start
                and o.completedDate < :end
            """)
    BigDecimal monthlyRevenue(@Param("status") OrderStatus status,
                              @Param("start") LocalDateTime start,
                              @Param("end") LocalDateTime end);
}
