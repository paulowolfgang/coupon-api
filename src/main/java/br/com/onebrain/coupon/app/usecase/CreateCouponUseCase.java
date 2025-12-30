package br.com.onebrain.coupon.app.usecase;

import br.com.onebrain.coupon.app.port.CouponRepositoryPort;
import br.com.onebrain.coupon.domain.Coupon;
import br.com.onebrain.coupon.domain.CouponMessages;
import br.com.onebrain.coupon.domain.DomainException;

import java.time.Clock;
import java.time.Instant;
import java.util.Objects;

public class CreateCouponUseCase
{
    private final CouponRepositoryPort repository;
    private final Clock clock;

    public CreateCouponUseCase(CouponRepositoryPort repository, Clock clock)
    {
        this.repository = Objects.requireNonNull(repository);
        this.clock = Objects.requireNonNull(clock);
    }

    public Coupon execute(CreateCouponCommand cmd)
    {
        Objects.requireNonNull(cmd, "CMD must not be null!");

        Instant now = Instant.now(clock);

        Coupon coupon = Coupon.create(
                cmd.code(),
                cmd.description(),
                cmd.discountValue(),
                cmd.expirationDate(),
                cmd.published(),
                now
        );

        if (repository.existsActiveByCode(coupon.getCode()))
        {
            throw new DomainException(CouponMessages.CODE_ALREADY_EXISTS);
        }

        return repository.save(coupon);
    }
}
