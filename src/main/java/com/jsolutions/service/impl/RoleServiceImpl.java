package com.jsolutions.service.impl;

import com.jsolutions.model.Role;
import com.jsolutions.repository.IGenericRepository;
import com.jsolutions.repository.IRoleRepository;
import com.jsolutions.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl extends CRUDImpl<Role, String> implements IRoleService {

    @Autowired
    private IRoleRepository roleRepository;

    @Override
    protected IGenericRepository<Role, String> getRepository() {
        return roleRepository;
    }
}
