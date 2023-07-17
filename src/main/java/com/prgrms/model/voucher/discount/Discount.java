package com.prgrms.model.voucher.discount;

import com.prgrms.model.order.Price;
import com.prgrms.presentation.message.ErrorMessage;
import java.util.Objects;

public abstract class Discount {

    private final double discountAmount;

    public Discount(double discountAmount) {
        validPositiveDiscount(discountAmount);
        validLimit(discountAmount);
        this.discountAmount = discountAmount;
    }

    private void validPositiveDiscount(double value) {
        if (value < 0) {
            throw new IllegalArgumentException(ErrorMessage.NEGATIVE_ARGUMENT.getMessage());
        }
    }

    protected abstract void validLimit(double value);

    public double getDiscountAmount() {
        return discountAmount;
    }

    public abstract Price sale(Price originalPrice);

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Discount discount = (Discount) o;
        return Double.compare(discount.discountAmount, discountAmount) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(discountAmount);
    }

}
