package br.com.onebrain.coupon.app.port;

import br.com.onebrain.coupon.domain.Coupon;

import java.util.Optional;
import java.util.UUID;

public interface CouponRepositoryPort
{
    Coupon save(Coupon coupon);
    Optional<Coupon> findActiveById(UUID id);
    boolean existsActiveByCode(String normalizedCode);
}
