package com.jsolutions.service.impl;

import com.jsolutions.model.User;
import com.jsolutions.repository.IGenericRepository;
import com.jsolutions.repository.IRoleRepository;
import com.jsolutions.repository.IUserRepository;
import com.jsolutions.security.AuthUser;
import com.jsolutions.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl extends CRUDImpl<User, String> implements IUserService {


    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IRoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder bcrypt;

    @Override
    protected IGenericRepository<User, String> getRepository() {
        return userRepository;
    }

    @Override
    public Mono<User> saveHash(User user) {
        user.setPassword(bcrypt.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public Mono<AuthUser> searchByUserName(String username) {
        Mono<User> monoUser = userRepository.findOneByUsername(username);
        List<String> roles = new ArrayList<>();

        return monoUser.flatMap(u ->{
            return Flux.fromIterable(u.getRoles())
                    .flatMap(rol ->{
                        return roleRepository.findById(rol.getId())
                                .map(r->{
                                    roles.add(r.getNombre());
                                    return r;
                                });
                    }).collectList().flatMap(list ->{
                       u.setRoles(list);
                       return Mono.just(u);
                    });
        }).flatMap(u->{
            return  Mono.just(new AuthUser(u.getUsername(), u.getPassword(), u.getStatus(), roles));
        });
    }

    @Override
    public Mono<User> searchById(String id) {
        return userRepository.findOneById(id);
    }


}
