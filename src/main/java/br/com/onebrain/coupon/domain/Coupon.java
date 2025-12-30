package br.com.onebrain.coupon.domain;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;
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
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "code", nullable = false, length = 6)
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
        this.description = requireNonBlank(description, "description");
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
        Objects.requireNonNull(now, "Now must not be null.");
        return new Coupon(code, description, discountValue, expirationDate, published, now);
    }

    public void publish(Instant now)
    {
        ensureNotDeleted();
        Objects.requireNonNull(now, "Now must not be null.");
        this.published = true;
        touch(now);
    }

    public void delete(Instant now)
    {
        ensureNotDeleted();
        Objects.requireNonNull(now, "Now must not be null.");
        this.deleted = true;
        this.deletedAt = now;
        touch(now);
    }

    private void ensureNotDeleted()
    {
        if (this.deleted)
        {
            throw new IllegalStateException("Coupon already deleted.");
        }
    }

    private void touch(Instant now)
    {
        this.updatedAt = now;
    }

    private static String normalizeAndValidateCode(String raw)
    {
        String validate = requireNonBlank(raw, "code");

        validate = validate.replaceAll("[^A-Za-z0-9]", "");

        if (validate.length() != 6)
        {
            throw new IllegalArgumentException("Code must have exactly 6 alphanumeric characters after normalization.");
        }

        return validate.toUpperCase();
    }

    private static BigDecimal validateDiscount(BigDecimal validate)
    {
        Objects.requireNonNull(validate, "Discount value must not be null.");

        if (validate.compareTo(new BigDecimal("0.50")) < 0)
        {
            throw new IllegalArgumentException("Discount value must be >= 0.50");
        }

        return validate;
    }

    private static LocalDate validateExpiration(LocalDate date)
    {
        Objects.requireNonNull(date, "Expiration date must not be null.");

        if (date.isBefore(LocalDate.now()))
        {
            throw new IllegalArgumentException("Expiration date must not be in the past.");
        }

        return date;
    }

    private static String requireNonBlank(String value, String field)
    {
        if (value == null || value.trim().isEmpty())
        {
            throw new IllegalArgumentException(field + " must not be blank.");
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
