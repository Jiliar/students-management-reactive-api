package com.jsolutions.controller;

import com.jsolutions.model.Menu;
import com.jsolutions.service.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Links;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.reactive.WebFluxLinkBuilder.linkTo;
import static org.springframework.hateoas.server.reactive.WebFluxLinkBuilder.methodOn;
import static reactor.function.TupleUtils.function;


@RestController
@RequestMapping("/menus")
public class MenuController {

    @Autowired
    private IMenuService service;

    @GetMapping("/userauth")
    public Mono<ResponseEntity<Flux<Menu>>> listAll(){
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getAuthorities)
                .map(roles->{
                    String rolesString = roles.stream().map(Object::toString).collect(Collectors.joining(","));
                    System.out.println(rolesString);
                    String[] strings = rolesString.split(",");
                    return service.getMenus(strings);
                })
                .flatMap(menus -> Mono.just(ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(menus)));
    }

    @GetMapping
    public Mono<ResponseEntity<Flux<Menu>>>findAll() {
        Flux<Menu> fx = service.findAll();
        return Mono.just(ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(fx));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Menu>> findById(@PathVariable("id") String id){
        return service.findById(id)
                .map(e->ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(e))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<Menu>> save(@RequestBody Menu Menu, final ServerHttpRequest request){
        return service.save(Menu)
                .map(e->ResponseEntity
                        .created(URI.create(request.getURI()
                                .toString()
                                .concat("/")
                                .concat(e.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(e));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Menu>> update(@PathVariable("id") String id, @RequestBody Menu Menu){

        Mono<Menu> monoBody = Mono.just(Menu);
        Mono<Menu> monoDB = service.findById(id);

        return monoDB.zipWith(monoBody, (bd, d)->{
                    bd.setId(id);
                    bd.setItems(d.getItems());
                    bd.setNombre(d.getNombre());
                    bd.setItems(d.getItems());
                    bd.setRoles(d.getRoles());
                    return bd;
                })
                .flatMap(e->service.update(e)) //service::update
                .map(e -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(e))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable("id") String id){
        return service.findById(id)
                .flatMap(e->service.delete(e.getId())
                                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)))
                        //.thenReturn(new ResponseEntity<Void>(HttpStatus.NO_CONTENT))
                )
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    //private Menu MenuHateoas;

    @GetMapping("/hateoas/{id}")
    public Mono<EntityModel<Menu>> getHateoasById(@PathVariable("id") String id){
        Mono<Link> link1 = linkTo(methodOn(MenuController.class).findById(id)).withSelfRel().toMono();
        Mono<Link> link2 = linkTo(methodOn(MenuController.class).findAll()).withSelfRel().toMono();
        return link1
                .zipWith(link2)
                .map(function((lk1, lk2) -> Links.of(lk1, lk2)))
                .zipWith(service.findById(id), (lk3, Menu) -> EntityModel.of(Menu, lk3));

    }


}
