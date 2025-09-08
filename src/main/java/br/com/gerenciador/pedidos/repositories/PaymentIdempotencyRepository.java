package br.com.gerenciador.pedidos.repositories;

import br.com.gerenciador.pedidos.entitys.PaymentIdempotency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentIdempotencyRepository extends JpaRepository<PaymentIdempotency, String> {
}
