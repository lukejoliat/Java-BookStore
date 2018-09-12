package com.pluralsight;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class BookDAO {
	private Connection jdbcConnection;

	public BookDAO(Connection connection) {
		jdbcConnection = connection;
	}

	public Book getBook(int id) {
		Book book = null;
		String sql = "SELECT * FROM book WHERE id = ?";

		try (PreparedStatement statement = jdbcConnection.prepareStatement(sql)) {
			statement.setInt(1, id);

			try (ResultSet resultSet = statement.executeQuery()) {

				if (resultSet.next()) {
					String title = resultSet.getString("title");
					String author = resultSet.getString("author");
					float price = resultSet.getFloat("price");

					book = new Book(id, title, author, price);
				} else {
					book = null;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return book;
	}

	public ArrayList<Book> listAllBooks() {
		ArrayList<Book> listBook = new ArrayList<>();

		String sql = "SELECT * FROM book";

		try (Statement statement = jdbcConnection.createStatement()) {

			try (ResultSet resultSet = statement.executeQuery(sql)) {

				while (resultSet.next()) {
					int id = resultSet.getInt("id");
					String title = resultSet.getString("title");
					String author = resultSet.getString("author");
					float price = resultSet.getFloat("price");

					Book book = new Book(id, title, author, price);
					listBook.add(book);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listBook;
	}

	public boolean insertBook(Book book) throws SQLException {
		String sql = "INSERT INTO book (title, author, price) VALUES (?, ?, ?)";

		try (PreparedStatement statement = jdbcConnection.prepareStatement(sql)) {
			statement.setString(1, book.getTitle());
			statement.setString(2, book.getAuthor());
			statement.setFloat(3, book.getPrice());

			boolean rowInserted = statement.executeUpdate() > 0;
			return rowInserted;
		}

		// return false;
	}

	public void deleteBook(int id) throws SQLException {
		String SQL = "DELETE FROM book WHERE id = ?";
		try (PreparedStatement statement = jdbcConnection.prepareStatement(SQL)) {
			statement.setInt(1, id);
			statement.executeUpdate();
		}
	}

	public void updateBook(Book book) throws SQLException {
		String SQL = "UPDATE book SET title = ?, author = ?, price = ? " + "WHERE id = ?";
		try (PreparedStatement statement = jdbcConnection.prepareStatement(SQL)) {
			statement.setString(1, book.getTitle());
			statement.setString(2, book.getAuthor());
			statement.setFloat(3, book.getPrice());
			statement.setInt(4, book.getId());
			statement.executeUpdate();
		}
	}
}
