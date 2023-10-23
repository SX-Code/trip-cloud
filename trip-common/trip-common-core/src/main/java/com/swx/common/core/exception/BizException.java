package com.swx.common.core.exception;

import com.swx.common.core.utils.R;
import lombok.Getter;

/**
 * 自定义的业务异常
 */
@Getter
public class BizException extends RuntimeException {

    private Integer code = R.CODE_ERROR;

    public BizException() {
        super(R.MSG_ERROR);
    }

    public BizException(String message) {
        super(message);
    }

    public BizException(Integer code, String message) {
        super(message);
        this.code = code;
    }
}
