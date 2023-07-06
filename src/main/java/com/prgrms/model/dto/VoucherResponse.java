package com.prgrms.model.dto;

import com.prgrms.model.voucher.discount.Discount;
import com.prgrms.model.voucher.Voucher;
import com.prgrms.model.voucher.VoucherType;

import java.util.Objects;


public class VoucherResponse {
    private final VoucherType voucherType;
    private final Discount discount;

    public VoucherResponse ( Voucher voucher) {
        this.voucherType = voucher.getVoucherType();
        this.discount = voucher.getVoucherDiscount();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.voucherType).append(" : ")
                .append(this.discount.getValue());

        return sb.toString();
    }

    public VoucherType getVoucherType() {
        return voucherType;
    }

    public Discount getDiscount() {
        return discount;
    }

}
