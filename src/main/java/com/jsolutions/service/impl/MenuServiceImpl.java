package com.jsolutions.service.impl;

import com.jsolutions.model.Menu;
import com.jsolutions.repository.IGenericRepository;
import com.jsolutions.repository.IMenuRepository;
import com.jsolutions.service.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class MenuServiceImpl extends CRUDImpl<Menu, String> implements IMenuService {

    @Autowired
    private IMenuRepository menuRepository;

    @Override
    protected IGenericRepository<Menu, String> getRepository() {
        return menuRepository;
    }

    @Override
    public Flux<Menu> getMenus(String[] roles) {
        return menuRepository.getMenus(roles);
    }
}
