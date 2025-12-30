package br.com.onebrain.coupon.app.usecase;

import br.com.onebrain.coupon.app.port.CouponRepositoryPort;
import br.com.onebrain.coupon.domain.Coupon;
import br.com.onebrain.coupon.domain.CouponMessages;
import br.com.onebrain.coupon.domain.DomainException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.*;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DeleteCouponUseCaseTest
{
    private CouponRepositoryPort repository;
    private Clock clock;
    private DeleteCouponUseCase useCase;

    @BeforeEach
    void setup()
    {
        repository = mock(CouponRepositoryPort.class);
        clock = Clock.fixed(Instant.parse("2025-12-30T15:00:00Z"), ZoneOffset.UTC);
        useCase = new DeleteCouponUseCase(repository, clock);
    }

    @Test
    void shouldSoftDeleteAndSave()
    {
        UUID id = UUID.randomUUID();

        Coupon coupon = Coupon.create(
                "aa11bb",
                "Desc",
                new BigDecimal("1.00"),
                LocalDate.now().plusDays(1),
                false,
                Instant.parse("2025-12-30T10:00:00Z")
        );

        when(repository.findActiveById(id)).thenReturn(Optional.of(coupon));
        when(repository.save(any(Coupon.class))).thenAnswer(inv -> inv.getArgument(0));

        useCase.execute(id);

        assertTrue(coupon.isDeleted());
        assertEquals(Instant.parse("2025-12-30T15:00:00Z"), coupon.getDeletedAt());
        assertEquals(Instant.parse("2025-12-30T15:00:00Z"), coupon.getUpdatedAt());

        verify(repository).findActiveById(id);
        verify(repository).save(coupon);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void shouldThrowWhenCouponNotFoundOrDeleted()
    {
        UUID id = UUID.randomUUID();

        when(repository.findActiveById(id)).thenReturn(Optional.empty());

        DomainException ex = assertThrows(DomainException.class, () -> useCase.execute(id));
        assertEquals(CouponMessages.NOT_FOUND_OR_DELETED, ex.getMessageKey());

        verify(repository).findActiveById(id);
        verify(repository, never()).save(any());
        verifyNoMoreInteractions(repository);
    }

    @Test
    void shouldThrowNullPointerWhenIdIsNull()
    {
        assertThrows(NullPointerException.class, () -> useCase.execute(null));
        verifyNoInteractions(repository);
    }
}
