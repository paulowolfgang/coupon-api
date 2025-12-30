package br.com.onebrain.coupon.infra.config;

import br.com.onebrain.coupon.app.port.CouponRepositoryPort;
import br.com.onebrain.coupon.app.usecase.CreateCouponUseCase;
import br.com.onebrain.coupon.app.usecase.DeleteCouponUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class ApplicationBeansConfig
{
    @Bean
    public Clock clock()
    {
        return Clock.systemUTC();
    }

    @Bean
    public CreateCouponUseCase createCouponUseCase(CouponRepositoryPort repo, Clock clock)
    {
        return new CreateCouponUseCase(repo, clock);
    }

    @Bean
    public DeleteCouponUseCase deleteCouponUseCase(CouponRepositoryPort repo, Clock clock)
    {
        return new DeleteCouponUseCase(repo, clock);
    }
}
