package com.jsolutions.service;

import com.jsolutions.model.Menu;
import reactor.core.publisher.Flux;

public interface IMenuService extends ICRUD<Menu, String> {
    Flux<Menu> getMenus(String[] roles);
}
