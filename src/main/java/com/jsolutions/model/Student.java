package com.jsolutions.model;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "students")
public class Student {

    @EqualsAndHashCode.Include
    @Id
    private String id;
    @NotNull
    @Field(name = "names")
    private String nombres;
    @NotNull
    @Field(name = "lastnames")
    private String apellidos;
    @NotNull
    @Field(name = "dni")
    private String dni;
    @Field(name = "age")
    private Integer edad;

}
