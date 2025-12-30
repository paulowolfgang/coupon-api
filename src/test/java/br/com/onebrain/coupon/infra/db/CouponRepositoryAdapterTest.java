package br.com.onebrain.coupon.infra.db;

import br.com.onebrain.coupon.domain.Coupon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CouponRepositoryAdapterTest
{
    private SpringDataCouponRepository springRepo;
    private CouponRepositoryAdapter adapter;

    @BeforeEach
    void setup()
    {
        springRepo = mock(SpringDataCouponRepository.class);
        adapter = new CouponRepositoryAdapter(springRepo);
    }

    @Test
    void saveShouldDelegateToSpringRepo()
    {
        Coupon coupon = Coupon.create(
                "aa11bb",
                "Desc",
                new BigDecimal("1.00"),
                LocalDate.now().plusDays(1),
                false,
                Instant.parse("2025-12-30T12:00:00Z")
        );

        when(springRepo.save(coupon)).thenReturn(coupon);

        Coupon saved = adapter.save(coupon);

        assertSame(coupon, saved);
        verify(springRepo).save(coupon);
        verifyNoMoreInteractions(springRepo);
    }

    @Test
    void findActiveByIdShouldDelegateToSpringRepo()
    {
        UUID id = UUID.randomUUID();

        Coupon coupon = Coupon.create(
                "aa11bb",
                "Desc",
                new BigDecimal("1.00"),
                LocalDate.now().plusDays(1),
                false,
                Instant.parse("2025-12-30T12:00:00Z")
        );

        when(springRepo.findByIdAndDeletedFalse(id)).thenReturn(Optional.of(coupon));

        Optional<Coupon> result = adapter.findActiveById(id);

        assertTrue(result.isPresent());
        assertSame(coupon, result.get());

        verify(springRepo).findByIdAndDeletedFalse(id);
        verifyNoMoreInteractions(springRepo);
    }

    @Test
    void existsActiveByCodeShouldDelegateToSpringRepo()
    {
        String code = "AB1234";

        when(springRepo.existsByCodeAndDeletedFalse(code)).thenReturn(true);

        boolean exists = adapter.existsActiveByCode(code);

        assertTrue(exists);

        verify(springRepo).existsByCodeAndDeletedFalse(code);
        verifyNoMoreInteractions(springRepo);
    }
}
