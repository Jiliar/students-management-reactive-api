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
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "menus")
public class Menu {

    @EqualsAndHashCode.Include
    @Id
    private String id;
    @NotNull
    @Field(name = "name")
    private String nombre;
    @Field(name = "items")
    private List<MenuItem> items;
    @Field(name = "roles")
    private List<String> roles;

}
