package br.com.onebrain.coupon.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class CouponTest
{
    private static final Instant NOW = Instant.parse("2025-12-30T12:00:00Z");

    @Test
    void shouldCreateCouponWithNormalizedCodeAndPublishedTrue()
    {
        Coupon coupon = Coupon.create(
                "ab-12!3@4",
                "Cupom teste",
                new BigDecimal("10.00"),
                LocalDate.now().plusDays(1),
                true,
                NOW
        );

        assertNull(coupon.getId());
        assertEquals("AB1234", coupon.getCode());
        assertEquals("Cupom teste", coupon.getDescription());
        assertEquals(new BigDecimal("10.00"), coupon.getDiscountValue());
        assertEquals(LocalDate.now().plusDays(1), coupon.getExpirationDate());
        assertTrue(coupon.isPublished());
        assertFalse(coupon.isDeleted());
        assertNull(coupon.getDeletedAt());
        assertEquals(NOW, coupon.getCreatedAt());
        assertEquals(NOW, coupon.getUpdatedAt());
    }

    @Test
    void shouldCreateCouponWithPublishedFalse()
    {
        Coupon coupon = Coupon.create(
                "aa11bb",
                "Descrição",
                new BigDecimal("0.50"),
                LocalDate.now(),
                false,
                NOW
        );

        assertFalse(coupon.isPublished());
    }

    @Test
    void shouldThrowWhenNowIsNullOnCreate()
    {
        DomainException example = assertThrows(DomainException.class, () ->
                Coupon.create(
                        "aa11bb",
                        "Descrição",
                        new BigDecimal("1.00"),
                        LocalDate.now().plusDays(1),
                        false,
                        null
                )
        );

        assertEquals(CouponMessages.NOW_REQUIRED, example.getMessageKey());
    }

    @Test
    void shouldThrowWhenCodeIsBlank()
    {
        DomainException example = assertThrows(DomainException.class, () ->
                Coupon.create(
                        "   ",
                        "Descrição",
                        new BigDecimal("1.00"),
                        LocalDate.now().plusDays(1),
                        false,
                        NOW
                )
        );

        assertEquals(CouponMessages.CODE_REQUIRED, example.getMessageKey());
    }

    @Test
    void shouldThrowWhenCodeAfterNormalizationIsNotLengthSix()
    {
        DomainException example = assertThrows(DomainException.class, () ->
                Coupon.create(
                        "AB-12!",
                        "Descrição",
                        new BigDecimal("1.00"),
                        LocalDate.now().plusDays(1),
                        false,
                        NOW
                )
        );

        assertEquals(CouponMessages.CODE_INVALID_LENGTH, example.getMessageKey());
        assertArrayEquals(new Object[]{ Coupon.MAXIMUM_CHARACTER_SIZE }, example.getArgs());
    }

    @Test
    void shouldThrowWhenDescriptionIsBlank()
    {
        DomainException example = assertThrows(DomainException.class, () ->
                Coupon.create(
                        "AA11BB",
                        "   ",
                        new BigDecimal("1.00"),
                        LocalDate.now().plusDays(1),
                        false,
                        NOW
                )
        );

        assertEquals(CouponMessages.DESCRIPTION_REQUIRED, example.getMessageKey());
    }

    @Test
    void shouldThrowWhenDiscountIsNull()
    {
        DomainException example = assertThrows(DomainException.class, () ->
                Coupon.create(
                        "AA11BB",
                        "Descrição",
                        null,
                        LocalDate.now().plusDays(1),
                        false,
                        NOW
                )
        );

        assertEquals(CouponMessages.DISCOUNT_REQUIRED, example.getMessageKey());
    }

    @Test
    void shouldThrowWhenDiscountIsLessThan050()
    {
        DomainException example = assertThrows(DomainException.class, () ->
                Coupon.create(
                        "AA11BB",
                        "Descrição",
                        new BigDecimal("0.49"),
                        LocalDate.now().plusDays(1),
                        false,
                        NOW
                )
        );

        assertEquals(CouponMessages.DISCOUNT_MIN, example.getMessageKey());
        assertArrayEquals(new Object[]{ "0.50" }, example.getArgs());
    }

    @Test
    void shouldAcceptDiscountEqualTo050()
    {
        Coupon coupon = Coupon.create(
                "AA11BB",
                "Descrição",
                new BigDecimal("0.50"),
                LocalDate.now().plusDays(1),
                false,
                NOW
        );

        assertEquals(new BigDecimal("0.50"), coupon.getDiscountValue());
    }

    @Test
    void shouldThrowWhenExpirationDateIsNull()
    {
        DomainException example = assertThrows(DomainException.class, () ->
                Coupon.create(
                        "AA11BB",
                        "Descrição",
                        new BigDecimal("1.00"),
                        null,
                        false,
                        NOW
                )
        );

        assertEquals(CouponMessages.EXPIRATION_REQUIRED, example.getMessageKey());
    }

    @Test
    void shouldThrowWhenExpirationDateIsInThePast()
    {
        DomainException example = assertThrows(DomainException.class, () ->
                Coupon.create(
                        "AA11BB",
                        "Descrição",
                        new BigDecimal("1.00"),
                        LocalDate.now().minusDays(1),
                        false,
                        NOW
                )
        );

        assertEquals(CouponMessages.EXPIRATION_PAST, example.getMessageKey());
    }

    @Test
    void shouldSoftDeleteCoupon()
    {
        Coupon coupon = Coupon.create(
                "AA11BB",
                "Descrição",
                new BigDecimal("1.00"),
                LocalDate.now().plusDays(1),
                false,
                NOW
        );

        Instant deleteNow = Instant.parse("2025-12-30T13:00:00Z");
        coupon.delete(deleteNow);

        assertTrue(coupon.isDeleted());
        assertEquals(deleteNow, coupon.getDeletedAt());
        assertEquals(deleteNow, coupon.getUpdatedAt());
    }

    @Test
    void shouldNotAllowDeleteTwice()
    {
        Coupon coupon = Coupon.create(
                "AA11BB",
                "Descrição",
                new BigDecimal("1.00"),
                LocalDate.now().plusDays(1),
                false,
                NOW
        );

        coupon.delete(Instant.parse("2025-12-30T13:00:00Z"));

        DomainException example = assertThrows(DomainException.class, () ->
                coupon.delete(Instant.parse("2025-12-30T14:00:00Z"))
        );

        assertEquals(CouponMessages.ALREADY_DELETED, example.getMessageKey());
    }

    @Test
    void shouldNotAllowPublishIfDeleted()
    {
        Coupon coupon = Coupon.create(
                "AA11BB",
                "Descrição",
                new BigDecimal("1.00"),
                LocalDate.now().plusDays(1),
                false,
                NOW
        );

        coupon.delete(Instant.parse("2025-12-30T13:00:00Z"));

        DomainException example = assertThrows(DomainException.class, () ->
                coupon.publish(Instant.parse("2025-12-30T14:00:00Z"))
        );

        assertEquals(CouponMessages.ALREADY_DELETED, example.getMessageKey());
    }

    @Test
    void shouldThrowWhenPublishNowIsNull()
    {
        Coupon coupon = Coupon.create(
                "AA11BB",
                "Descrição",
                new BigDecimal("1.00"),
                LocalDate.now().plusDays(1),
                false,
                NOW
        );

        DomainException example = assertThrows(DomainException.class, () -> coupon.publish(null));
        assertEquals(CouponMessages.NOW_REQUIRED, example.getMessageKey());
    }

    @Test
    void shouldThrowWhenDeleteNowIsNull()
    {
        Coupon coupon = Coupon.create(
                "AA11BB",
                "Descrição",
                new BigDecimal("1.00"),
                LocalDate.now().plusDays(1),
                false,
                NOW
        );

        DomainException example = assertThrows(DomainException.class, () -> coupon.delete(null));
        assertEquals(CouponMessages.NOW_REQUIRED, example.getMessageKey());
    }
}
