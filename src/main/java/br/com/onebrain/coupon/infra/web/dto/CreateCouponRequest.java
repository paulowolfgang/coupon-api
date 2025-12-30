package br.com.onebrain.coupon.infra.web.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateCouponRequest(
        @NotBlank String code,
        @NotBlank String description,
        @NotNull @DecimalMin(value = "0.50") BigDecimal discountValue,
        @NotNull LocalDate expirationDate,
        boolean published
){}
