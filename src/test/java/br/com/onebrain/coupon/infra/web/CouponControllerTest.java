package br.com.onebrain.coupon.infra.web;

import br.com.onebrain.coupon.app.usecase.*;
import br.com.onebrain.coupon.domain.Coupon;
import br.com.onebrain.coupon.infra.web.dto.CouponResponse;
import br.com.onebrain.coupon.infra.web.dto.CreateCouponRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CouponControllerTest
{
    private CreateCouponUseCase createUseCase;
    private DeleteCouponUseCase deleteUseCase;

    private CouponController controller;

    @BeforeEach
    void setup()
    {
        createUseCase = mock(CreateCouponUseCase.class);
        deleteUseCase = mock(DeleteCouponUseCase.class);

        controller = new CouponController(createUseCase, deleteUseCase);
    }

    @Test
    void createShouldCallUseCaseAndReturnResponse()
    {
        CreateCouponRequest request = new CreateCouponRequest(
                "ab-12!3@4",
                "Desc",
                new BigDecimal("10.00"),
                LocalDate.now().plusDays(1),
                true
        );

        Coupon created = Coupon.create(
                "ab-12!3@4",
                "Desc",
                new BigDecimal("10.00"),
                LocalDate.now().plusDays(1),
                true,
                Instant.parse("2025-12-30T12:00:00Z")
        );

        when(createUseCase.execute(any(CreateCouponCommand.class))).thenReturn(created);

        CouponResponse response = controller.create(request);

        assertNotNull(response);
        assertEquals(created.getCode(), response.code());
        assertEquals(created.getDescription(), response.description());
        assertEquals(created.getDiscountValue(), response.discountValue());
        assertEquals(created.getExpirationDate(), response.expirationDate());
        assertEquals(created.isPublished(), response.published());
        assertEquals(created.isDeleted(), response.deleted());

        verify(createUseCase).execute(any(CreateCouponCommand.class));
        verifyNoMoreInteractions(createUseCase, deleteUseCase);
    }

    @Test
    void deleteShouldCallUseCase()
    {
        UUID id = UUID.randomUUID();

        controller.delete(id);

        verify(deleteUseCase).execute(id);
        verifyNoMoreInteractions(createUseCase, deleteUseCase);
    }
}
