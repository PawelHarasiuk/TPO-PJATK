package com.prog.tpo6_hp_s24628;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet(name = "bookServlet", value = "")
public class BooksServlet extends HttpServlet {
    String host = "db4free.net";
    String user = "s24628";
    String password = "newpassword";
    String database = "pjatk_database";
    String url = "jdbc:mysql://" + host + "/" + database;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, user, password);
            System.out.println(connection);

            Statement statement = connection.createStatement();

            PrintWriter out = response.getWriter();

            // Display the search form
            out.println("<html><head><title>Book Search</title>");
            out.println("<style>");
            out.println("body { font-family: Arial, sans-serif; background-color: #F2F2F2; margin: 0; padding: 0; }");
            out.println(".container { max-width: 800px; margin: 0 auto; padding: 20px; background-color: #FFFFFF; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); }");
            out.println("h1 { text-align: center; color: #333333; }");
            out.println("form { text-align: center; margin-top: 20px; }");
            out.println("table { margin: 0 auto; border-collapse: collapse; width: 100%; }");
            out.println("th, td { padding: 10px; text-align: left; }");
            out.println("th { background-color: #333333; color: #FFFFFF; }");
            out.println("tr:nth-child(even) { background-color: #F2F2F2; }");
            out.println("tr:hover { background-color: #DDDDDD; }");
            out.println("</style>");
            out.println("</head><body>");
            out.println("<div class=\"container\">");
            out.println("<h1>Book Search</h1>");

            out.println("</div>");

            out.println("<form method=\"get\">");
            out.println("<input type=\"text\" name=\"title\" placeholder=\"Enter title\">");
            out.println("<input type=\"text\" name=\"author\" placeholder=\"Enter author\">");
            out.println("<input type=\"text\" name=\"category\" placeholder=\"Enter category\">");

            // Sort options
            out.println("<select name=\"sort\">");
            out.println("<option value=\"\">Sort By</option>");
            out.println("<option value=\"price_asc\">Price: Low to High</option>");
            out.println("<option value=\"price_desc\">Price: High to Low</option>");
            out.println("<option value=\"title_asc\">Title: A-Z</option>");
            out.println("<option value=\"title_desc\">Title: Z-A</option>");
            out.println("</select>");

            out.println("<button type=\"submit\">Search</button>");
            out.println("</form>");

            // Search
            String searchTitle = request.getParameter("title");
            String searchAuthor = request.getParameter("author");
            String searchCategory = request.getParameter("category");
            String sortOption = request.getParameter("sort");

            StringBuilder queryBuilder = new StringBuilder("SELECT * FROM Books WHERE 1=1");

            if (searchTitle != null && !searchTitle.isEmpty()) {
                queryBuilder.append(" AND title LIKE '%").append(searchTitle).append("%'");
            }

            if (searchAuthor != null && !searchAuthor.isEmpty()) {
                queryBuilder.append(" AND Author LIKE '%").append(searchAuthor).append("%'");
            }

            if (searchCategory != null && !searchCategory.isEmpty()) {
                queryBuilder.append(" AND category LIKE '%").append(searchCategory).append("%'");
            }

            if (sortOption != null && !sortOption.isEmpty()) {
                switch (sortOption) {
                    case "price_asc":
                        queryBuilder.append(" ORDER BY price ASC");
                        break;
                    case "price_desc":
                        queryBuilder.append(" ORDER BY price DESC");
                        break;
                    case "title_asc":
                        queryBuilder.append(" ORDER BY title ASC");
                        break;
                    case "title_desc":
                        queryBuilder.append(" ORDER BY title DESC");
                        break;
                }
            }

            String searchQuery = queryBuilder.toString();
            ResultSet resultSet = statement.executeQuery(searchQuery);

            out.println("<h2>Search Results:</h2>");
            out.println("<table>");
            out.println("<tr><th>Title</th><th>Author</th><th>Price</th><th>Category</th>");
            out.println("</tr>");

            while (resultSet.next()) {
                out.println("<tr>");
                out.println("<td>" + resultSet.getString("title") + "</td>");
                out.println("<td>" + resultSet.getString("Author") + "</td>");
                out.println("<td>" + resultSet.getDouble("price") + "</td>");
                out.println("<td>" + resultSet.getString("category") + "</td>");
                out.println("</tr>");
            }

            out.println("</table>");
            resultSet.close();
            statement.close();
            connection.close();

            out.println("</div></body></html>");
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}