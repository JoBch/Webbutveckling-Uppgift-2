package Servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet("/Courses")
public class AddCourseServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
            //Anslut till databasen och hämta data
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3307/gritacademy";

            HttpSession session = request.getSession(false);
            String user = (String) session.getAttribute("username");
            String password = (String) session.getAttribute("password");
            Connection connection = DriverManager.getConnection(url, user, password);

            out.println("<link rel=\"stylesheet\" href=\"/style.css\">");
            out.println("<html><body>");

            out.println("<header class=\"headerLinks\">"
                    + "<div>"
                    + "<a href=\"Attendance\">Click here to check who is studying what course.</a>"
                    + "</div>"
                    + "<div>"
                    + "<a href=\"Students\">Click here to add a student to the DB.</a>"
                    + "</div>"
                    + "<div>"
                    + "<a href=\"Enroll\">Click here to enroll a student in a new course.</a>"
                    + "</div>"
                    + "<div>"
                    + "<a href=\"index.html\">Home Page</a>"
                    + "</div>"
                    + "</header>");

            //Skriv ut resultatet till webbläsaren
            out.println("<h1>Hello " + user + ". Insert info of course below to add it to the DB.</h1>");

            out.println("<form action=\"/Courses\" method =\"post\" >"
                    + "Course name: <input type=\"text\" name=\"name\" required><br>"
                    + "YHP: <input type=\"number\" name=\"YHP\" required><br>"
                    + "Description: <textarea name=\"description\" rows=\"5\" cols=\"20\" placeholder=\"Write a short description of the course\"" +
                    " required></textarea><br><br>"
                    + "<input type=\"submit\" value=\"Submit\"><br>"
                    + "</form>");

            out.println("</body></html>");

        } catch (Exception e) {
            //redirection back to index.html (the login page), in a perfect world maybe check the inputs before this
            response.sendRedirect("/index.html");
            System.out.println(e.getMessage());
            out.println("<p><span style= \"background-color:red\">Error: " + e.getMessage() + "</span></p>");
        } finally {
            out.close();
        }
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        resp.setContentType("text/html;charset=UTF-8");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3307/gritacademy";

            HttpSession session = req.getSession(false);
            String user = (String) session.getAttribute("username");
            String password = (String) session.getAttribute("password");
            Connection connection = DriverManager.getConnection(url, user, password);

            out.println("<link rel=\"stylesheet\" href=\"/style.css\">");
            out.println("<html><body>");
            out.println("<header class=\"headerLinks\">"
                    + "<div>"
                    + "<a href=\"Attendance\">Click here to check who is studying what course.</a>"
                    + "</div>"
                    + "<div>"
                    + "<a href=\"Students\">Click here to add a student to the DB.</a>"
                    + "</div>"
                    + "<div>"
                    + "<a href=\"Enroll\">Click here to enroll a student in a new course.</a>"
                    + "</div>"
                    + "<div>"
                    + "<a href=\"index.html\">Home Page</a>"
                    + "</div>"
                    + "</header>"
            );
            out.println("<form action=\"/Courses\" method =\"post\" >"
                    + "Course name: <input type=\"text\" name=\"name\" required><br>"
                    + "YHP: <input type=\"number\" name=\"YHP\" required><br>"
                    + "Description: <textarea name=\"description\" rows=\"5\" cols=\"20\" placeholder=\"Write a short description of the course\"" +
                    " required></textarea><br><br>"
                    + "<input type=\"submit\" value=\"Submit\"><br>"
                    + "</form>"
                    + "<form action=\"/Courses\" method =\"get\" >"
                    + "<input type=\"submit\" value=\"Display Students and Courses\"><br>"
                    + "</form>");

            String sql = "INSERT INTO `courses` (`id`, `name`, `YHP`, `description`) VALUES (NULL, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, req.getParameter("name"));
            statement.setInt(2, Integer.parseInt(req.getParameter("YHP")));
            statement.setString(3, req.getParameter("description"));

            int resultRows = statement.executeUpdate();

            if (resultRows > 0) {
                out.println("<p><span style= \"background-color:green\">Student was added to the DB!</span></p>");
            } else {
                out.println("<p><span style= \"background-color:red\">No student was added to the DB!</span></p>");
            }

            String sql1 = "SELECT name, YHP, Description FROM Courses";
            PreparedStatement statement1 = connection.prepareStatement(sql1);
            ResultSet resultSet = statement1.executeQuery();
            out.println("<table class=\"table\">");

            while (resultSet.next()) {
                out.println("<tr>");
                out.println("<td>" + resultSet.getString("name") + "</td>");
                out.println("<td>" + resultSet.getString("YHP") + "</td>");
                out.println("<td>" + resultSet.getString("description") + "</td>");
                out.println("</tr>");
            }

            out.println("</table>");
            out.println("</body></html>");

        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
            out.println("<p><span style= \"background-color:red\">Error: " + e.getMessage() + "</span></p>");
        } finally {
            out.close();
        }
    }
}
