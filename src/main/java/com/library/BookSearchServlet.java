package com.library;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * BookSearchServlet
 * Handles GET requests to /search?query=...
 * Searches a hardcoded in-memory library catalog by title or author
 * (case-insensitive, partial match) and returns the results as JSON.
 */
@WebServlet("/search")
public class BookSearchServlet extends HttpServlet {

    private static final List<Book> CATALOG = Arrays.asList(
            new Book("Clean Code", "Robert C. Martin", 2008, "Programming"),
            new Book("The Pragmatic Programmer", "Andrew Hunt", 1999, "Programming"),
            new Book("Introduction to Algorithms", "Thomas H. Cormen", 2009, "Computer Science"),
            new Book("Design Patterns", "Erich Gamma", 1994, "Programming"),
            new Book("The Hobbit", "J.R.R. Tolkien", 1937, "Fiction"),
            new Book("1984", "George Orwell", 1949, "Fiction"),
            new Book("A Brief History of Time", "Stephen Hawking", 1988, "Science"),
            new Book("Sapiens", "Yuval Noah Harari", 2011, "History"),
            new Book("The Selfish Gene", "Richard Dawkins", 1976, "Science"),
            new Book("Atomic Habits", "James Clear", 2018, "Self-Help")
    );

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String query = request.getParameter("query");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        List<Book> results = new ArrayList<>();

        if (query != null && !query.trim().isEmpty()) {
            String normalized = query.trim().toLowerCase();
            for (Book book : CATALOG) {
                if (book.getTitle().toLowerCase().contains(normalized)
                        || book.getAuthor().toLowerCase().contains(normalized)) {
                    results.add(book);
                }
            }
        }

        try (PrintWriter out = response.getWriter()) {
            out.print(toJsonArray(results));
        }
    }

    /** Builds a JSON array string from a list of books without any external library. */
    private String toJsonArray(List<Book> books) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < books.size(); i++) {
            Book b = books.get(i);
            sb.append("{")
              .append("\"title\":\"").append(escape(b.getTitle())).append("\",")
              .append("\"author\":\"").append(escape(b.getAuthor())).append("\",")
              .append("\"year\":").append(b.getYear()).append(",")
              .append("\"genre\":\"").append(escape(b.getGenre())).append("\"")
              .append("}");
            if (i < books.size() - 1) {
                sb.append(",");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    private String escape(String value) {
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    /** Simple immutable book record. */
    private static class Book {
        private final String title;
        private final String author;
        private final int year;
        private final String genre;

        Book(String title, String author, int year, String genre) {
            this.title = title;
            this.author = author;
            this.year = year;
            this.genre = genre;
        }

        String getTitle() { return title; }
        String getAuthor() { return author; }
        int getYear() { return year; }
        String getGenre() { return genre; }
    }
}
