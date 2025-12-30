package br.com.onebrain.coupon.domain;

public final class CouponMessages
{
    private CouponMessages() {}

    public static final String NOW_REQUIRED = "coupon.now.required";
    public static final String CODE_REQUIRED = "coupon.code.required";
    public static final String CODE_INVALID_LENGTH = "coupon.code.invalid_length";
    public static final String DESCRIPTION_REQUIRED = "coupon.description.required";
    public static final String DISCOUNT_REQUIRED = "coupon.discount.required";
    public static final String DISCOUNT_MIN = "coupon.discount.min";
    public static final String EXPIRATION_REQUIRED = "coupon.expiration.required";
    public static final String EXPIRATION_PAST = "coupon.expiration.past";
    public static final String ALREADY_DELETED = "coupon.already_deleted";
    public static final String NOT_FOUND_OR_DELETED = "coupon.not_found_or_deleted";
    public static final String CODE_ALREADY_EXISTS = "coupon.code.already_exists";
}
