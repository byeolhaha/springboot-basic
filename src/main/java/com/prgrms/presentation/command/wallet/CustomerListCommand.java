package com.prgrms.presentation.command.wallet;

import com.prgrms.custoemer.dto.CustomerResponse;
import com.prgrms.presentation.Power;
import com.prgrms.presentation.command.Command;
import com.prgrms.presentation.message.GuideMessage;
import com.prgrms.presentation.view.Input;
import com.prgrms.presentation.view.Output;
import com.prgrms.wallet.service.WalletService;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class CustomerListCommand implements Command {

    private final Output output;
    private final Input input;
    private final WalletService walletService;

    public CustomerListCommand(Output output, Input input, WalletService walletService) {
        this.output = output;
        this.input = input;
        this.walletService = walletService;
    }

    @Override
    public Power execute() {
        output.write(GuideMessage.CUSTOMER_LIST.toString());
        String voucherId = input.enterID();

        List<CustomerResponse> customers = walletService.customerList(voucherId);

        customers.forEach(v -> output.write(v.toString()));

        return Power.ON;
    }

}
