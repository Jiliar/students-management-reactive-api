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

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "users")
public class User {

    @EqualsAndHashCode.Include
    @Id
    private String id;
    @NotNull
    @Field(name = "username")
    private String username;
    @NotNull
    @Min(8)
    @Field(name = "password")
    private String password;
    @Email
    @Field(name = "email")
    private String email;
    @Field(name = "status")
    private Boolean status;
    @Field(name = "roles")
    private List<Role> roles;

}
