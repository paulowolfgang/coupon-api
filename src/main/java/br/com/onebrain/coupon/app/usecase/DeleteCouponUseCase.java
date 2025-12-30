package br.com.onebrain.coupon.app.usecase;

import br.com.onebrain.coupon.app.port.CouponRepositoryPort;
import br.com.onebrain.coupon.domain.Coupon;

import java.time.Clock;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class DeleteCouponUseCase
{
    private final CouponRepositoryPort repository;
    private final Clock clock;

    public DeleteCouponUseCase(CouponRepositoryPort repository, Clock clock)
    {
        this.repository = Objects.requireNonNull(repository);
        this.clock = Objects.requireNonNull(clock);
    }

    public void execute(UUID id)
    {
        Objects.requireNonNull(id, "Id must not be null!");

        Coupon coupon = repository
                .findActiveById(id)
                .orElseThrow(() -> new IllegalArgumentException("Coupon not found or already deleted."));

        coupon.delete(Instant.now(clock));
        repository.save(coupon);
    }
}
