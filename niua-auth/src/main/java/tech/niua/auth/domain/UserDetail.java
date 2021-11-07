package tech.niua.auth.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @author : Wangzhen
 * createAt: 2020/5/29
 */
public class UserDetail implements UserDetails {
    private long id;
    private String username;
    private String password;
    private List<SysResource> resources;
    private Date lastPasswordResetDate;

    public UserDetail(
            long id,
            String username,
            List<SysResource> resources,
//            Date lastPasswordResetDate,
        String password) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.resources = resources;
//        this.lastPasswordResetDate = lastPasswordResetDate;
    }

    public UserDetail(String username, String password, List<SysResource> resources) {
        this.username = username;
        this.password = password;
        this.resources = resources;
    }

    public UserDetail(long id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    //返回分配给用户的资源列表
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        this.getResources().stream().forEach(resource -> {
            if(resource != null){
                String permission = resource.getPermission();
                authorities.add(new SimpleGrantedAuthority(permission));
            }
        });

        return authorities;
    }

    public long getId() {
        return id;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    /**
     * 账户是否未过期
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     *  账户是否未锁定
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    /**
     * 密码是否未过期
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /** 账户是否激活
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
    public Date getLastPasswordResetDate() {
        return lastPasswordResetDate;
    }

    public List<SysResource> getResources() {
        return resources;
    }

    public void setResources(List<SysResource> resources) {
        this.resources = resources;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setLastPasswordResetDate(Date lastPasswordResetDate) {
        this.lastPasswordResetDate = lastPasswordResetDate;
    }
}
