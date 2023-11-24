package ru.test.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.test.models.Book;
import ru.test.models.Person;
import ru.test.util.ConnectService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class PersonDao {


    ConnectService connectService;

    @Autowired
    public PersonDao(ConnectService connectService) {
        this.connectService = connectService;
    }

    public List<Person> getAllPersons(){
        List<Person> list = new ArrayList<>();
        try(Connection connection = connectService.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("select * from Person");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                list.add(Person.builder()
                        .person_id(resultSet.getInt("person_id"))
                        .FIO(resultSet.getString("FIO"))
                        .birthday(resultSet.getInt("birthday"))
                        .build());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public void save(Person person){
        try ( Connection connection = connectService.getConnection()){
            PreparedStatement statement = connection.prepareStatement("INSERT INTO person(FIO, BIRTHDAY) VALUES (?,?)");
            statement.setString(1, person.getFIO());
            statement.setInt(2,person.getBirthday());
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Person get(int id){
        Person person = new Person();

        try ( Connection connection = connectService.getConnection()){
            PreparedStatement statement = connection.prepareStatement("select * from person where person_id = ?");
            statement.setInt(1,id);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            person.setPerson_id(resultSet.getInt("person_id"));
            person.setFIO(resultSet.getString("FIO"));
            person.setBirthday(resultSet.getInt("birthday"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return person;
    }

    public Optional<Person> get(String fio){
        Person person = new Person();

        try ( Connection connection = connectService.getConnection()){
            PreparedStatement statement = connection.prepareStatement("select * from person where fio = ?");
            statement.setString(1,fio);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            person.setPerson_id(resultSet.getInt("person_id"));
            person.setFIO(resultSet.getString("FIO"));
            person.setBirthday(resultSet.getInt("birthday"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.of(person);
    }

    public void personEditById(int id, Person person){
        try ( Connection connection = connectService.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE Person set FIO = ?, birthday = ? where person_id = ?");
            statement.setString(1, person.getFIO());
            statement.setInt(2, person.getBirthday());
            statement.setInt(3,id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(int id){
        try(Connection connection = connectService.getConnection()){
            PreparedStatement statement = connection.prepareStatement(
                    "delete from person where person_id = ?");
            statement.setInt(1,id);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Book> getPersonsBook(int id){
        List<Book> books = new ArrayList<>();
        try(Connection connection = connectService.getConnection()){
            PreparedStatement statement = connection.prepareStatement(
                    "select * from person join book using(person_id) where person_id = ?"
            );
            statement.setInt(1,id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                books.add(Book.builder()
                                .book_id(resultSet.getInt("book_id"))
                                .book_name(resultSet.getString("book_name"))
                                .author(resultSet.getString("author"))
                                .year_of_publication(resultSet.getInt("year_of_publication"))
                        .build());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return books;
    }
}
