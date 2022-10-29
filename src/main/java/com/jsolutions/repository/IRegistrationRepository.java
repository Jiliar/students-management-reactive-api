package com.jsolutions.repository;

import com.jsolutions.model.Registration;
import org.springframework.stereotype.Repository;

@Repository
public interface IRegistrationRepository extends IGenericRepository<Registration, String>{
}
