package com.prgrms.repository.voucher;

import com.prgrms.model.voucher.Voucher;
import com.prgrms.model.voucher.Vouchers;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

@Component
public class MemoryVoucherRepository implements VoucherRepository {
    private final Map<Integer, Voucher> storage = new TreeMap<>();

    @Override
    public Optional<Voucher> findById(int voucherId) {
        return Optional.ofNullable(storage.get(voucherId));
    }

    @Override
    public Voucher insert(Voucher voucher) {
        storage.put(voucher.getVoucherId(), voucher);
        return voucher;
    }
    public Vouchers getAllVoucher() {
        return new Vouchers(storage.values()
                .stream()
                .toList());
    }
}
