package com.prgrms.wallet.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.samePropertyValuesAs;

import com.prgrms.custoemer.model.Customer;
import com.prgrms.custoemer.model.Name;
import com.prgrms.voucher.model.voucher.FixedAmountVoucher;
import com.prgrms.voucher.model.voucher.Voucher;
import com.prgrms.voucher.model.VoucherCreator;
import com.prgrms.voucher.model.VoucherType;
import com.prgrms.voucher.model.Vouchers;
import com.prgrms.voucher.model.discount.DiscountCreator;
import com.prgrms.voucher.model.discount.FixedDiscount;
import com.prgrms.wallet.model.Wallet;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({JdbcWalletRepository.class, DiscountCreator.class, VoucherCreator.class})
class JdbcWalletRepositoryTest {

    private final String walletId = "10";
    private final String voucherId = "10";
    private final String customerId = "10";
    private final Name testUserName = new Name("test-user");
    private final String testUserEmail = "test1-user@gmail.com";

    @Autowired
    private JdbcWalletRepository jdbcWalletRepository;
    private Wallet newWallet;
    private Customer newCustomer;
    private Voucher newFixVoucher;

    @BeforeEach
    void clean() {
        newWallet = new Wallet(walletId, customerId, voucherId);
        newCustomer = new Customer(customerId, testUserName, testUserEmail, LocalDateTime.now());
        newCustomer.login(LocalDateTime.now());
        newFixVoucher = new FixedAmountVoucher(voucherId, new FixedDiscount(20),
                VoucherType.FIXED_AMOUNT_VOUCHER, LocalDateTime.now());

        jdbcWalletRepository.insert(newWallet);
    }

    @Test
    @DisplayName("바우처 지갑을 추가한 결과 반환하는 지갑에 저장된 지갑 아이디, 고객의 아이디, 바우처 아이디는 추가한 고객과 같다.")
    void insert_CustomerId_EqualsNewCustomerId() {
        //when
        Optional<Wallet> wallet = jdbcWalletRepository.findById(walletId);

        //then
        assertThat(wallet.isEmpty(), is(false));
        assertThat(wallet.get().getWalletId(), samePropertyValuesAs(newWallet.getWalletId()));
        assertThat(wallet.get().getCustomerId(), samePropertyValuesAs(newWallet.getCustomerId()));
        assertThat(wallet.get().getVoucherId(), samePropertyValuesAs(newWallet.getVoucherId()));
    }

    @Test
    @DisplayName("데이터베이스에 몇 개의 데이터를 저장한 후 전체 지갑을 조회한 결과는 빈 값을 반환하지 않는다.")
    void findAllWallet_Customer_NotEmpty() {
        //when
        List<Wallet> wallets = jdbcWalletRepository.findAllWallet();

        //then
        assertThat(wallets.isEmpty(), is(false));
    }

    @Test
    @DisplayName("바우처 아이디로 지갑에 등록된 고객의 정보를 가져올 수 있다.")
    void findAllCustomersByVoucher_VoucherId_SameValue() {
        // when
        List<Customer> customers = jdbcWalletRepository.findAllCustomersByVoucher(voucherId);

        // then
        assertThat(customers.isEmpty(), is(true));
    }

    @Test
    @DisplayName("고객 아이디로 지갑에 등록된 바우처의 정보를 가져올 수 있다.")
    void findAllVouchersByCustomer_CustomerId_SameValue() {
        // when
        Vouchers vouchers = jdbcWalletRepository.findAllVouchersByCustomer(customerId);

        // then
        assertThat(vouchers.vouchers().isEmpty(), is(true));
    }

    @Test
    @DisplayName("고객의 자신이 보유한 바우처를 삭제하고자 한다면 지갑의 삭제 여부를 true로 바꾼다.")
    void deleteWithVoucherIdAndCustomerId_Name_EqualsExistingCustomerName() {
        //when
        Wallet wallet = jdbcWalletRepository.deleteWithVoucherIdAndCustomerId(voucherId,
                customerId);

        //then
        assertThat(wallet.isDeleted(), is(true));
    }
}

