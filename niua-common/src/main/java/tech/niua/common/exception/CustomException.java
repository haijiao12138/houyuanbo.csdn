package tech.niua.common.exception;

import lombok.Getter;
import tech.niua.common.model.ResultJson;

/**
 * @author Wangzhen
 * createAt: 2020/5/29
 */
@Getter
public class CustomException extends RuntimeException{
    private ResultJson resultJson;

    public CustomException(ResultJson resultJson) {
        this.resultJson = resultJson;
    }
}
