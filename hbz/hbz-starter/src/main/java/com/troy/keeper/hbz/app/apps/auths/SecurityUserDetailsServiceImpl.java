package com.troy.keeper.hbz.app.apps.auths;

import com.troy.keeper.core.base.entity.Account;
import com.troy.keeper.hbz.app.security.SecurityUserDetailsService;
import com.troy.keeper.hbz.po.HbzAuth;
import com.troy.keeper.hbz.po.HbzUser;
import com.troy.keeper.hbz.repository.HbzUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by leecheng on 2017/10/11.
 */
@Component("defultDetailService")
public class SecurityUserDetailsServiceImpl implements SecurityUserDetailsService {

    @Autowired
    private HbzUserRepository hbzUserRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        HbzUser user = hbzUserRepository.findByLogin(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        } else if (user.getActivated()) {
            List<GrantedAuthority> auths = user.getRoles().stream().flatMap(role -> role.getAuths().stream()).map(HbzAuth::getAuthName).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
            Account account = new Account(user.getLogin(), user.getPassword(), auths, user.getId());
            return account;
        } else {
            throw new UsernameNotFoundException("用户冻结");
        }
    }
}
