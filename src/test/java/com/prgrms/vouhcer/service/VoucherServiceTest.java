package com.prgrms.vouhcer.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.prgrms.config.TestConfig;
import com.prgrms.voucher.repository.VoucherRepository;
import com.prgrms.voucher.service.VoucherService;
import com.prgrms.voucher.service.dto.VoucherServiceCreateRequest;
import com.prgrms.voucher.service.dto.VoucherServiceListRequest;
import com.prgrms.voucher.service.dto.VoucherServiceResponse;
import com.prgrms.voucher.model.voucher.FixedAmountVoucher;
import com.prgrms.voucher.model.voucher.PercentDiscountVoucher;
import com.prgrms.voucher.model.voucher.Voucher;
import com.prgrms.voucher.model.VoucherType;
import com.prgrms.voucher.model.discount.Discount;
import com.prgrms.voucher.model.discount.FixedDiscount;
import com.prgrms.voucher.model.discount.PercentDiscount;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
@Import({TestConfig.class})
class VoucherServiceTest {

    final static String FIXED_ID = "22";
    final static String PERCENT_ID = "40";
    final static double DISCOUNT_AMOUNT = 20;

    @Autowired
    VoucherService voucherService;

    @Autowired
    VoucherRepository voucherRepository;

    @Test
    @DisplayName("만들고자 하는 바우처를 createVoucher()로 만들었을 때 기대값과 같은 바우처를 반환한다.")
    void createVoucher_RepositoryInsertVoucher_Equals() {
        //given
        VoucherType voucherType = VoucherType.FIXED_AMOUNT_VOUCHER;
        Discount discount = new FixedDiscount(DISCOUNT_AMOUNT);
        VoucherServiceCreateRequest serviceCreateRequest = new VoucherServiceCreateRequest(voucherType,DISCOUNT_AMOUNT);

        //when
        VoucherServiceResponse result = voucherService.createVoucher(serviceCreateRequest);

        //then
        assertThat(result.voucherId())
                .isNotNull()
                .isEqualTo("22");
    }

    @ParameterizedTest
    @DisplayName("몇 개의 바우처 정책을 List로 만들어 저장소에 저장한 결과와 getAllVocherList의 결과는 같다.")
    @MethodSource("voucherProvider")
    void getAllVoucherList_RepositoryListVoucherList_Equals(List<Voucher> list) {
        // Given
        Voucher voucher1=  voucherRepository.insert(list.get(0));
        Voucher voucher2 = voucherRepository.insert(list.get(1));
        VoucherServiceResponse voucherServiceResponse1 = new VoucherServiceResponse(voucher1);
        VoucherServiceResponse voucherServiceResponse2 = new VoucherServiceResponse(voucher2);
        VoucherServiceListRequest voucherServiceListRequest = new VoucherServiceListRequest(null, null);

        // When
        List<VoucherServiceResponse> result = voucherService.getAllVoucherList(voucherServiceListRequest);

        // Then
        assertThat(result)
                .containsOnly(voucherServiceResponse1, voucherServiceResponse2);

    }

    private static Stream<List<Voucher>> voucherProvider() {
        Voucher createdVoucher1 = new FixedAmountVoucher(FIXED_ID, new FixedDiscount(20),
                VoucherType.FIXED_AMOUNT_VOUCHER, LocalDateTime.now());
        Voucher createdVoucher2 = new PercentDiscountVoucher(PERCENT_ID, new PercentDiscount(20),
                VoucherType.PERCENT_DISCOUNT_VOUCHER, LocalDateTime.now());
        return Stream.of(
                List.of(createdVoucher1, createdVoucher2)
        );
    }

}
