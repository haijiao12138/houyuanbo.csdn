package tech.niua.auth.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Wangzhen
 * createAt: 2020/5/29
 */
@Data
@AllArgsConstructor
public class ResponseUserToken {
    private String token;
    private UserDetail userDetail;
}
