package com.prgrms.repository.customer;

import com.prgrms.model.customer.Customer;
import com.prgrms.repository.DataRowMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;


@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({JdbcCustomerRepository.class, DataRowMapper.class})
class JdbcCustomerRepositoryTest {

    private final String TEST_USER_NAME = "test-user";
    private final String TEST_USER_EMAIL = "test1-user@gmail.com";
    private final int ID = 10;


    @Autowired
    private JdbcCustomerRepository jdbcCustomerRepository;
    @MockBean
    private DataRowMapper dataRowMapper;
    private Customer newCustomer;

    @BeforeEach
    void clean() {
        newCustomer = new Customer(ID, TEST_USER_NAME, TEST_USER_EMAIL, LocalDateTime.now());
        newCustomer.login(LocalDateTime.now());
        jdbcCustomerRepository.insert(newCustomer);
    }

    @Test
    @DisplayName("고객을 추가한 결과 반환하는 고객의 아이디와 추가한 고객의 아이디는 같다.")
    void insert_CustomerId_EqualsNewCustomerId() {
        // given
        when(dataRowMapper.getCustomerRowMapper()).thenReturn((ResultSet rs, int rowNum) -> newCustomer);

        //when
        Optional<Customer> retrievedCustomer = jdbcCustomerRepository.findById(newCustomer.getCustomerId());

        //then
        assertThat(retrievedCustomer.isEmpty(), is(false));
        assertThat(retrievedCustomer.get().getCustomerId(), samePropertyValuesAs(newCustomer.getCustomerId()));
    }

    @Test
    @DisplayName("데이터베이스에 몇 개의 데이터를 저장한 후 전체 고객을 조회한 결과는 빈 값을 반환하지 않는다.")
    void findAll_Customer_NotEmpty() {
        //given
        when(dataRowMapper.getCustomerRowMapper()).thenReturn((ResultSet rs, int rowNum) -> newCustomer);

        //when
        List<Customer> customers = jdbcCustomerRepository.findAll();

        //then
        assertThat(customers.isEmpty(), is(false));
    }

    @Test
    @DisplayName("데이터베이스에 존재하는 회원의 이름으로 검색했을 때 빈값을 반환하지 않는다.")
    void findByName_ExistingCustomer_NotEmpty() {
        // given
        when(dataRowMapper.getCustomerRowMapper()).thenReturn((ResultSet rs, int rowNum) -> newCustomer);

        // when
        Optional<Customer> customers = jdbcCustomerRepository.findByName(newCustomer.getName());
        Optional<Customer> unknownCustomers = jdbcCustomerRepository.findByName("테스터");

        // then
        assertThat(customers.isEmpty(), is(false));
        assertThat(unknownCustomers.isEmpty(), is(true));
    }

    @Test
    @DisplayName("데이터베이스에 존재하는 회원의 이메일로 검색했을 때 빈값을 반환하지 않는다.")
    void findByEmail_ExistingCustomerEmail_NotEmpty() {
        //given
        when(dataRowMapper.getCustomerRowMapper()).thenReturn((ResultSet rs, int rowNum) -> newCustomer);

        //when
        Optional<Customer> customers = jdbcCustomerRepository.findByEmail(newCustomer.getEmail());
        Optional<Customer> unknownCustomers = jdbcCustomerRepository.findByEmail("테스터");

        //then
        assertThat(customers.isEmpty(), is(false));
        assertThat(unknownCustomers.isEmpty(), is(true));
    }

    @Test
    @DisplayName("고객의 이름을 수정하고 데이터 베이스에 넣고 이를 다시 검색했을 때 수정된 이름으로 반환한다.")
    void update_Name_EqualsExistingCustomerName() {
        //given
        when(dataRowMapper.getCustomerRowMapper()).thenReturn((ResultSet rs, int rowNum) -> newCustomer);
        newCustomer.changeName("updated-user");

        //when
        jdbcCustomerRepository.update(newCustomer);
        List<Customer> all = jdbcCustomerRepository.findAll();

        //then
        Optional<Customer> retrievedCustomer = jdbcCustomerRepository.findById(newCustomer.getCustomerId());
        assertThat(retrievedCustomer.isEmpty(), is(false));
        assertThat(retrievedCustomer.get().getName(), samePropertyValuesAs(newCustomer.getName()));
    }
}
