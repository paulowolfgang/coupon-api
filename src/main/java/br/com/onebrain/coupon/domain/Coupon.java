package br.com.onebrain.coupon.domain;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(
        name = "coupons",
        indexes = {
                @Index(name = "idx_coupons_code", columnList = "code"),
                @Index(name = "idx_coupons_deleted", columnList = "deleted")
        }
)
public class Coupon
{
    public static final int MAXIMUM_CHARACTER_SIZE = 6;
    public static final int MINIMUM_DISCOUNT_BALANCE = 0;

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "code", nullable = false, length = MAXIMUM_CHARACTER_SIZE)
    private String code;

    @Column(name = "description", nullable = false, length = 255)
    private String description;

    @Column(name = "discount_value", nullable = false, precision = 11, scale = 2)
    private BigDecimal discountValue;

    @Column(name = "expiration_date", nullable = false)
    private LocalDate expirationDate;

    @Column(name = "published", nullable = false)
    private boolean published;

    @Column(name = "deleted", nullable = false)
    private boolean deleted;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected Coupon() {}

    private Coupon(String code,
                   String description,
                   BigDecimal discountValue,
                   LocalDate expirationDate,
                   boolean published,
                   Instant now)
    {
        this.code = normalizeAndValidateCode(code);
        this.description = requireNonBlank(description, CouponMessages.DESCRIPTION_REQUIRED);
        this.discountValue = validateDiscount(discountValue);
        this.expirationDate = validateExpiration(expirationDate);
        this.published = published;

        this.deleted = false;
        this.deletedAt = null;

        this.createdAt = now;
        this.updatedAt = now;
    }

    public static Coupon create(String code,
                                String description,
                                BigDecimal discountValue,
                                LocalDate expirationDate,
                                boolean published,
                                Instant now)
    {
        requireNow(now);
        return new Coupon(code, description, discountValue, expirationDate, published, now);
    }

    public void publish(Instant now)
    {
        ensureNotDeleted();
        requireNow(now);
        this.published = true;
        touch(now);
    }

    public void delete(Instant now)
    {
        ensureNotDeleted();
        requireNow(now);
        this.deleted = true;
        this.deletedAt = now;
        touch(now);
    }

    private void ensureNotDeleted()
    {
        if (this.deleted)
        {
            throw new DomainException(CouponMessages.ALREADY_DELETED);
        }
    }

    private void touch(Instant now)
    {
        this.updatedAt = now;
    }

    private static void requireNow(Instant now)
    {
        if (now == null)
        {
            throw new DomainException(CouponMessages.NOW_REQUIRED);
        }
    }

    private static String normalizeAndValidateCode(String raw)
    {
        String validate = requireNonBlank(raw, CouponMessages.CODE_REQUIRED);

        validate = validate.replaceAll("[^A-Za-z0-9]", "");

        if (validate.length() != MAXIMUM_CHARACTER_SIZE)
        {
            throw new DomainException(CouponMessages.CODE_INVALID_LENGTH, MAXIMUM_CHARACTER_SIZE);
        }

        return validate.toUpperCase();
    }

    private static BigDecimal validateDiscount(BigDecimal validate)
    {
        if (validate == null)
        {
            throw new DomainException(CouponMessages.DISCOUNT_REQUIRED);
        }

        if (validate.compareTo(new BigDecimal("0.50")) < MINIMUM_DISCOUNT_BALANCE)
        {
            throw new DomainException(CouponMessages.DISCOUNT_MIN, "0.50");
        }

        return validate;
    }

    private static LocalDate validateExpiration(LocalDate date)
    {
        if (date == null)
        {
            throw new DomainException(CouponMessages.EXPIRATION_REQUIRED);
        }

        if (date.isBefore(LocalDate.now()))
        {
            throw new DomainException(CouponMessages.EXPIRATION_PAST);
        }

        return date;
    }

    private static String requireNonBlank(String value, String messageKey)
    {
        if (value == null || value.trim().isEmpty())
        {
            throw new DomainException(messageKey);
        }

        return value.trim();
    }

    public UUID getId() { return id; }
    public String getCode() { return code; }
    public String getDescription() { return description; }
    public BigDecimal getDiscountValue() { return discountValue; }
    public LocalDate getExpirationDate() { return expirationDate; }
    public boolean isPublished() { return published; }
    public boolean isDeleted() { return deleted; }
    public Instant getDeletedAt() { return deletedAt; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}
