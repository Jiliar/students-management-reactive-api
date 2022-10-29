package com.jsolutions.repository;

import com.jsolutions.model.User;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface IUserRepository extends IGenericRepository<User, String>{

    Mono<User> findOneByUsername(String username);
    Mono<User> findOneById(String id);

}
