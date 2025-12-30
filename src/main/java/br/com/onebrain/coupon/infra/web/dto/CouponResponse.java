package br.com.onebrain.coupon.infra.web.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record CouponResponse(
        UUID id,
        String code,
        String description,
        BigDecimal discountValue,
        LocalDate expirationDate,
        boolean published,
        boolean deleted
){}
