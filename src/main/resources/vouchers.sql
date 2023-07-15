create table voucher_table
(
    voucher_id   BIGINT      not null,
    discount     BIGINT      not null,
    voucher_type varchar(20) not null,

    primary key (voucher_id)
);
INSERT INTO vouchers(voucher_id, discount, voucher_type)
VALUES (11, 20, 'FIXED_AMOUNT_VOUCHER');
INSERT INTO vouchers(voucher_id, discount, voucher_type)
VALUES (12, 30, 'FIXED_AMOUNT_VOUCHER');
INSERT INTO vouchers(voucher_id, discount, voucher_type)
VALUES (1000, 40, 'PERCENT_DISCOUNT_VOUCHER');
