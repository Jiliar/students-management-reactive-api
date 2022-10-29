package com.jsolutions.controller;

import com.jsolutions.model.Role;
import com.jsolutions.model.Student;
import com.jsolutions.pagination.PageSupport;
import com.jsolutions.service.IStudentService;
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
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private IStudentService service;

    @GetMapping
    public Mono<ResponseEntity<Flux<Student>>>findAll() {
        Flux<Student> fx = service.findAll();
        return Mono.just(ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(fx));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Student>> findById(@PathVariable("id") String id){
        return service.findById(id)
                .map(e->ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(e))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<Student>> save(@RequestBody Student student, final ServerHttpRequest request){
        return service.save(student)
                .map(e->ResponseEntity
                        .created(URI.create(request.getURI()
                                .toString()
                                .concat("/")
                                .concat(e.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(e));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Student>> update(@PathVariable("id") String id, @RequestBody Student Student){

        Mono<Student> monoBody = Mono.just(Student);
        Mono<Student> monoDB = service.findById(id);

        return monoDB.zipWith(monoBody, (bd, d)->{
                    bd.setId(id);
                    bd.setNombres(d.getNombres());
                    bd.setEdad(d.getEdad());
                    bd.setDni(d.getDni());
                    bd.setApellidos(d.getApellidos());
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

    //private Student StudentHateoas;

    @GetMapping("/hateoas/{id}")
    public Mono<EntityModel<Student>> getHateoasById(@PathVariable("id") String id){
        Mono<Link> link1 = linkTo(methodOn(StudentController.class).findById(id)).withSelfRel().toMono();
        Mono<Link> link2 = linkTo(methodOn(StudentController.class).findAll()).withSelfRel().toMono();
        return link1
                .zipWith(link2)
                .map(function((lk1, lk2) -> Links.of(lk1, lk2)))
                .zipWith(service.findById(id), (lk3, Student) -> EntityModel.of(Student, lk3));

    }

    @GetMapping("/pageable")
    public Mono<ResponseEntity<PageSupport<Student>>> getPage(
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
