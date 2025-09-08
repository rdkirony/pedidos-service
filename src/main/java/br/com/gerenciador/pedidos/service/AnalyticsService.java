package br.com.gerenciador.pedidos.service;

import br.com.gerenciador.pedidos.core.records.output.AvgTicketOutput;
import br.com.gerenciador.pedidos.core.records.output.MonthlyRevenueOutput;
import br.com.gerenciador.pedidos.core.records.output.TopBuyerOutput;
import br.com.gerenciador.pedidos.enums.OrderStatus;
import br.com.gerenciador.pedidos.repositories.OrderRepository;
import br.com.gerenciador.pedidos.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final OrderRepository orderRepository;

    @Transactional(readOnly = true)
    public List<TopBuyerOutput> topBuyers(int limit) {
        return orderRepository.topBuyersRaw(OrderStatus.PAID, PageRequest.of(0, limit)).stream()
                .map(r -> new TopBuyerOutput(
                        (String) r[0],
                        (BigDecimal) r[1]
                ))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AvgTicketOutput> avgTicketPerUser() {
        return orderRepository.avgTicketPerUser(OrderStatus.PAID).stream()
                .map(r -> {
                    final var userId = (String) r[0];
                    final var total = (BigDecimal) r[1];
                    long numOrders = (Long) r[2];
                    final var avg = total.divide(BigDecimal.valueOf(numOrders), 2, RoundingMode.HALF_UP);
                    return new AvgTicketOutput(userId, avg);
                }).toList();
    }

    @Transactional(readOnly = true)
    public MonthlyRevenueOutput monthRevenue(int year, int month) {
        LocalDate first = LocalDate.of(year, month, 1);
        LocalDateTime start = first.atStartOfDay();
        LocalDateTime end = first.plusMonths(1).atStartOfDay();

        BigDecimal revenue = orderRepository.monthlyRevenue(OrderStatus.PAID, start, end);
        return new MonthlyRevenueOutput(year, month, revenue == null ? BigDecimal.ZERO : revenue);
    }
}
