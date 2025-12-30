package br.com.onebrain.coupon.infra.db;

import br.com.onebrain.coupon.domain.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataCouponRepository extends JpaRepository<Coupon, UUID>
{
    Optional<Coupon> findByIdAndDeletedFalse(UUID id);
    boolean existsByCodeAndDeletedFalse(String code);
}
