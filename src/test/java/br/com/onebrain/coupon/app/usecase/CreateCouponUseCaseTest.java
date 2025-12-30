package br.com.onebrain.coupon.app.usecase;

import br.com.onebrain.coupon.app.port.CouponRepositoryPort;
import br.com.onebrain.coupon.domain.Coupon;
import br.com.onebrain.coupon.domain.CouponMessages;
import br.com.onebrain.coupon.domain.DomainException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateCouponUseCaseTest
{
    private CouponRepositoryPort repository;
    private Clock clock;
    private CreateCouponUseCase useCase;

    @BeforeEach
    void setup()
    {
        repository = mock(CouponRepositoryPort.class);
        clock = Clock.fixed(Instant.parse("2025-12-30T12:00:00Z"), ZoneOffset.UTC);
        useCase = new CreateCouponUseCase(repository, clock);
    }

    @Test
    void shouldCreateAndSaveCouponWhenCodeIsUnique()
    {
        CreateCouponCommand cmd = new CreateCouponCommand(
                "ab-12!3@4",
                "Desc",
                new BigDecimal("10.00"),
                LocalDate.now().plusDays(1),
                true
        );

        when(repository.existsActiveByCode("AB1234")).thenReturn(false);

        when(repository.save(any(Coupon.class))).thenAnswer(inv -> inv.getArgument(0));

        Coupon result = useCase.execute(cmd);

        assertNotNull(result);
        assertEquals("AB1234", result.getCode());
        assertEquals("Desc", result.getDescription());
        assertEquals(new BigDecimal("10.00"), result.getDiscountValue());
        assertTrue(result.isPublished());
        assertFalse(result.isDeleted());

        verify(repository).existsActiveByCode("AB1234");
        verify(repository).save(any(Coupon.class));
        verifyNoMoreInteractions(repository);
    }

    @Test
    void shouldThrowWhenCodeAlreadyExists()
    {
        CreateCouponCommand cmd = new CreateCouponCommand(
                "ab-12!3@4",
                "Desc",
                new BigDecimal("10.00"),
                LocalDate.now().plusDays(1),
                false
        );

        when(repository.existsActiveByCode("AB1234")).thenReturn(true);

        DomainException ex = assertThrows(DomainException.class, () -> useCase.execute(cmd));

        assertEquals(CouponMessages.CODE_ALREADY_EXISTS, ex.getMessageKey());

        verify(repository).existsActiveByCode("AB1234");
        verify(repository, never()).save(any());
        verifyNoMoreInteractions(repository);
    }

    @Test
    void shouldPassAClockBasedNowToDomain()
    {
        CreateCouponCommand cmd = new CreateCouponCommand(
                "aa11bb",
                "Desc",
                new BigDecimal("1.00"),
                LocalDate.now().plusDays(1),
                false
        );

        when(repository.existsActiveByCode("AA11BB")).thenReturn(false);
        when(repository.save(any(Coupon.class))).thenAnswer(inv -> inv.getArgument(0));

        Coupon result = useCase.execute(cmd);

        assertEquals(Instant.parse("2025-12-30T12:00:00Z"), result.getCreatedAt());
        assertEquals(Instant.parse("2025-12-30T12:00:00Z"), result.getUpdatedAt());
    }

    @Test
    void shouldThrowNullPointerWhenCmdIsNull()
    {
        assertThrows(NullPointerException.class, () -> useCase.execute(null));
        verifyNoInteractions(repository);
    }
}
