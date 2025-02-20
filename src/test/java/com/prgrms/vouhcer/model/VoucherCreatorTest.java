package com.prgrms.vouhcer.model;

import static org.assertj.core.api.Assertions.assertThat;

import com.prgrms.voucher.model.voucher.FixedAmountVoucher;
import com.prgrms.voucher.model.voucher.PercentDiscountVoucher;
import com.prgrms.voucher.model.voucher.Voucher;
import com.prgrms.voucher.model.VoucherCreator;
import com.prgrms.voucher.model.VoucherType;
import com.prgrms.voucher.model.discount.Discount;
import com.prgrms.voucher.model.discount.FixedDiscount;
import com.prgrms.voucher.model.discount.PercentDiscount;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class VoucherCreatorTest {

    final String voucherId = "1";
    final double discountAmount = 20;
    final Discount fixedDiscount = new FixedDiscount(discountAmount);
    final Discount percentDiscount = new PercentDiscount(discountAmount);

    @Autowired
    private VoucherCreator voucherCreator;


    @Test
    @DisplayName("고정 금액 바우처 요청을 통해 바우처를 만들었을 때 요청과 같은 바우처를 반환한다.")
    void createVoucher_FixedVoucher_Equal() {
        //given
        Voucher fixedVoucher = new FixedAmountVoucher(voucherId, fixedDiscount,
                VoucherType.FIXED_AMOUNT_VOUCHER, LocalDateTime.now());

        // When
        Voucher result = voucherCreator.createVoucher(voucherId, VoucherType.FIXED_AMOUNT_VOUCHER,
                fixedDiscount, LocalDateTime.now());

        // Then
        assertThat(result).usingRecursiveComparison().isEqualTo(fixedVoucher);
    }

    @Test
    @DisplayName("할인율 바우처 요청을 통해 바우처를 만들었을 때 요청과 같은 바우처를 반환한다.")
    void createVoucher_PercentVoucher_Equal() {
        //given
        Voucher perecntVoucher = new PercentDiscountVoucher(voucherId, percentDiscount,
                VoucherType.PERCENT_DISCOUNT_VOUCHER, LocalDateTime.now());

        // When
        Voucher result = voucherCreator.createVoucher(voucherId,
                VoucherType.PERCENT_DISCOUNT_VOUCHER,
                percentDiscount, LocalDateTime.now());

        // Then
        assertThat(result).usingRecursiveComparison().isEqualTo(perecntVoucher);
    }

}
