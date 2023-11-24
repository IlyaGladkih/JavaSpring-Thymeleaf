package ru.test.models;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Person {

    private int person_id;

    @Pattern(regexp = "[a-zA-Z0-9]+ [a-zA-Z0-9]+ [a-zA-Z0-9]+", message = "FIO shoud look like: falily name otchestvo")
    @NotNull(message = "FIO shoud be not empty")
    private String FIO;

    @Range(min = 1900,max = 2020,message = "birthday in range 1900 to 2020")
    private int birthday;

    private List<Book> books;
}
