package br.com.onebrain.coupon.infra.web;

import br.com.onebrain.coupon.app.usecase.CreateCouponCommand;
import br.com.onebrain.coupon.app.usecase.CreateCouponUseCase;
import br.com.onebrain.coupon.app.usecase.DeleteCouponUseCase;
import br.com.onebrain.coupon.domain.Coupon;
import br.com.onebrain.coupon.infra.web.dto.CouponResponse;
import br.com.onebrain.coupon.infra.web.dto.CreateCouponRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/coupons")
public class CouponController
{
    private final CreateCouponUseCase createUseCase;
    private final DeleteCouponUseCase deleteUseCase;

    public CouponController(CreateCouponUseCase createUseCase, DeleteCouponUseCase deleteUseCase)
    {
        this.createUseCase = createUseCase;
        this.deleteUseCase = deleteUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CouponResponse create(@RequestBody @Valid CreateCouponRequest request)
    {
        Coupon created = createUseCase.execute(new CreateCouponCommand(
                request.code(),
                request.description(),
                request.discountValue(),
                request.expirationDate(),
                request.published()
        ));

        return toResponse(created);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id)
    {
        deleteUseCase.execute(id);
    }

    private static CouponResponse toResponse(Coupon c)
    {
        return new CouponResponse(
                c.getId(),
                c.getCode(),
                c.getDescription(),
                c.getDiscountValue(),
                c.getExpirationDate(),
                c.isPublished(),
                c.isDeleted()
        );
    }
}
