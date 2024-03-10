package achievers.ieps.backend.security;

import java.util.HashSet;
import java.util.Set;

import achievers.ieps.backend.model.UserModel;
import achievers.ieps.backend.repository.UserDb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{
    @Autowired
    private UserDb userDb;

    //Maksudnya email
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
        UserModel user = userDb.findByEmail(email);
        if (user == null){
            return null;
        }
        Set<GrantedAuthority> grantedAuthoritySet = new HashSet<>();
        grantedAuthoritySet.add(new SimpleGrantedAuthority(user.getRole().getRole()));
        return new User(user.getEmail(), user.getPassword(), grantedAuthoritySet);
    }
}
