package br.com.onebrain.coupon.infra.db;

import br.com.onebrain.coupon.app.port.CouponRepositoryPort;
import br.com.onebrain.coupon.domain.Coupon;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class CouponRepositoryAdapter implements CouponRepositoryPort
{
    private final SpringDataCouponRepository springRepo;

    public CouponRepositoryAdapter(SpringDataCouponRepository springRepo)
    {
        this.springRepo = springRepo;
    }

    @Override
    public Coupon save(Coupon coupon)
    {
        return springRepo.save(coupon);
    }

    @Override
    public Optional<Coupon> findActiveById(UUID id)
    {
        return springRepo.findByIdAndDeletedFalse(id);
    }

    @Override
    public boolean existsActiveByCode(String normalizedCode)
    {
        return springRepo.existsByCodeAndDeletedFalse(normalizedCode);
    }
}
