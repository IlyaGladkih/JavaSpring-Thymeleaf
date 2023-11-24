package ru.test.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.test.models.Book;
import ru.test.models.Person;
import ru.test.util.ConnectService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class BookDao {


    ConnectService connectService;

    @Autowired
    public BookDao(ConnectService connectService) {
        this.connectService = connectService;
    }

    public List<Book> all(){
        List<Book> list = new ArrayList<>();
        try (Connection connection = connectService.getConnection()){
            PreparedStatement statement = connection.prepareStatement("select * from book");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                list.add(Book.builder()
                                .book_id(resultSet.getInt("book_id"))
                                .book_name(resultSet.getString("book_name"))
                                .year_of_publication(resultSet.getInt("year_of_publication"))
                                .author(resultSet.getString("author"))
                        .build());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public void save(Book book){
        try (Connection connection = connectService.getConnection()){
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO Book(book_name, author, year_of_publication) values (?,?,?)");
            statement.setString(1, book.getBook_name());
            statement.setString(2, book.getAuthor());
            statement.setInt(3,book.getYear_of_publication());
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Book get(int id){
        Book book = new Book();
        try(Connection connection = connectService.getConnection()){
            PreparedStatement statement = connection.prepareStatement("select * from book where book_id = ?");
            statement.setInt(1,id);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            book.setBook_id(resultSet.getInt("book_id"));
            book.setBook_name(resultSet.getString("book_name"));
            book.setAuthor(resultSet.getString("author"));
            book.setYear_of_publication(resultSet.getInt("year_of_publication"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return book;
    }

    public void edit(int id, Book book){
        try(Connection connection = connectService.getConnection()){
            PreparedStatement statement = connection.prepareStatement(
                    "update book set book_name = ?, author = ?, year_of_publication = ? where book_id = ?");
            statement.setString(1, book.getBook_name());
            statement.setString(2, book.getAuthor());
            statement.setInt(3,book.getYear_of_publication());
            statement.setInt(4,id);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(int id){
        try(Connection connection = connectService.getConnection()){
            PreparedStatement statement = connection.prepareStatement(
                    "delete from book where book_id = ?");
            statement.setInt(1,id);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Book getWithOwner(int id){
        Book book = null;
        try(Connection connection = connectService.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "select * from book left join person using(person_id) where book_id = ?");
            statement.setInt(1,id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                book = Book.builder()
                        .book_id(resultSet.getInt("book_id"))
                        .book_name(resultSet.getString("book_name"))
                        .author(resultSet.getString("author"))
                        .year_of_publication(resultSet.getInt("year_of_publication"))
                        .person(Person.builder()
                                .person_id(resultSet.getInt("person_id"))
                                .FIO(resultSet.getString("FIO"))
                                .birthday(resultSet.getInt("birthday"))
                                .build())
                        .build();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return book;
    }

    public void setPerson(int bookId, int personId){
        try(Connection connection = connectService.getConnection()){
            PreparedStatement statement = connection.prepareStatement(
                    "update book set person_id = ? where book_id = ?");
            statement.setInt(1, personId);
            statement.setInt(2, bookId);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void releaseBook(int id){
        try(Connection connection = connectService.getConnection()){
            PreparedStatement statement = connection.prepareStatement(
                    "update book set person_id = null where book_id = ?");
            statement.setInt(1, id);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
