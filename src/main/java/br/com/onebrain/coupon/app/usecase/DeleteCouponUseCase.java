package br.com.onebrain.coupon.app.usecase;

import br.com.onebrain.coupon.app.port.CouponRepositoryPort;
import br.com.onebrain.coupon.domain.Coupon;
import br.com.onebrain.coupon.domain.CouponMessages;
import br.com.onebrain.coupon.domain.DomainException;

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
                .orElseThrow(() -> new DomainException(CouponMessages.NOT_FOUND_OR_DELETED));

        coupon.delete(Instant.now(clock));
        repository.save(coupon);
    }
}
