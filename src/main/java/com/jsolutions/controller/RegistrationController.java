package com.jsolutions.controller;

import com.jsolutions.model.Registration;
import com.jsolutions.model.Role;
import com.jsolutions.pagination.PageSupport;
import com.jsolutions.service.IRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Links;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.springframework.hateoas.server.reactive.WebFluxLinkBuilder.linkTo;
import static org.springframework.hateoas.server.reactive.WebFluxLinkBuilder.methodOn;
import static reactor.function.TupleUtils.function;


@RestController
@RequestMapping("/registrations")
public class RegistrationController {

    @Autowired
    private IRegistrationService service;

    @GetMapping
    public Mono<ResponseEntity<Flux<Registration>>>findAll() {
        Flux<Registration> fx = service.findAll();
        return Mono.just(ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(fx));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Registration>> findById(@PathVariable("id") String id){
        return service.findById(id)
                .map(e->ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(e))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<Registration>> save(@RequestBody Registration registration, final ServerHttpRequest request){
        return service.save(registration)
                .map(e->ResponseEntity
                        .created(URI.create(request.getURI()
                                .toString()
                                .concat("/")
                                .concat(e.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(e));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Registration>> update(@PathVariable("id") String id, @RequestBody Registration registration){

        Mono<Registration> monoBody = Mono.just(registration);
        Mono<Registration> monoDB = service.findById(id);

        return monoDB.zipWith(monoBody, (bd, d)->{
                    bd.setId(id);
                    bd.setCursos(d.getCursos());
                    bd.setEstudiante(d.getEstudiante());
                    bd.setFecha_matricula(d.getFecha_matricula());
                    bd.setEstado(d.getEstado());
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

    //private Registration RegistrationHateoas;

    @GetMapping("/hateoas/{id}")
    public Mono<EntityModel<Registration>> getHateoasById(@PathVariable("id") String id){
        Mono<Link> link1 = linkTo(methodOn(RegistrationController.class).findById(id)).withSelfRel().toMono();
        Mono<Link> link2 = linkTo(methodOn(RegistrationController.class).findAll()).withSelfRel().toMono();
        return link1
                .zipWith(link2)
                .map(function((lk1, lk2) -> Links.of(lk1, lk2)))
                .zipWith(service.findById(id), (lk3, Registration) -> EntityModel.of(Registration, lk3));

    }

    @GetMapping("/pageable")
    public Mono<ResponseEntity<PageSupport<Registration>>> getPage(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size
    ){
        return service.getPage(PageRequest.of(page, size))
                .map(pag -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(pag))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }


}
