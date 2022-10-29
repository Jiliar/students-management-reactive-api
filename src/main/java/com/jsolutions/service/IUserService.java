package com.jsolutions.service;
import com.jsolutions.model.User;
import com.jsolutions.security.AuthUser;
import reactor.core.publisher.Mono;


public interface IUserService extends ICRUD<User, String>{

    Mono<User> saveHash(User user);
    Mono<AuthUser> searchByUserName(String user);
    Mono<User> searchById(String IS);

}
