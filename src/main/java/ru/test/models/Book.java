package ru.test.models;

import jakarta.validation.constraints.NotEmpty;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Book {
    private int book_id;

    @NotNull(message = "Name cannot be empty")
    private String book_name;

    @NotNull(message = "Name cannot be empty")
    private String author;

    @Range(min = 1000,max = 3000,message = "year in range 1000 to 3000")
    private int year_of_publication;

    private Person person;
}
