package com.jsolutions.service.impl;

import com.jsolutions.model.Registration;
import com.jsolutions.repository.IGenericRepository;
import com.jsolutions.repository.IRegistrationRepository;
import com.jsolutions.service.IRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegistrationServiceImpl extends CRUDImpl<Registration, String> implements IRegistrationService {

    @Autowired
    private IRegistrationRepository RegistrationRepository;

    @Override
    protected IGenericRepository<Registration, String> getRepository() {
        return RegistrationRepository;
    }
}
