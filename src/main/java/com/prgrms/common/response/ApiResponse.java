package com.prgrms.common.response;

import com.prgrms.common.codes.SuccessCode;
import lombok.Getter;

@Getter
public class ApiResponse<T> {

    private T result;
    private int resultCode;
    private String resultMsg;

    public ApiResponse(final T result, SuccessCode successCode) {
        this.result = result;
        this.resultCode = successCode.getStatus();
        this.resultMsg = successCode.getMessage();
    }

}
