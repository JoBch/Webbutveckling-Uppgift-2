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

@WebServlet("/Attendance")
public class AttendanceServlet extends HttpServlet {

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
                    + "<a href=\"Students\">Click here to add a student to the DB.</a>"
                    + "</div>"
                    + "<div>"
                    + "<a href=\"Courses\">Click here to add a course to the DB.</a>"
                    + "</div>"
                    + "<div>"
                    + "<a href=\"Enroll\">Click here to enroll a student in a new course.</a>"
                    + "</div>"
                    + "<div>"
                    + "<a href=\"index.html\">Home Page</a>"
                    + "</div>"
                    + "</header>"
            );

            out.println("<h1>Hello " + user + ". Insert name of student below to see their current courses.</h1>");
            out.println("<form method =\"post\">"
                    + "First name: <input type=\"text\" name=\"fname\" required><br>"
                    + "Last name: <input type=\"text\" name=\"lname\" required><br><br>"
                    + "<input type=\"submit\" value=\"Submit\"><br>"
                    + "</form>");

            String sql = "SELECT fname, lname, town FROM Students";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            out.println("<table class=\"table\">");
            out.println("<tr><th>First Name</th><th>Last Name</th><th>Town</th></tr>");

            while (resultSet.next()) {
                out.println("<tr>");
                out.println("<td>" + resultSet.getString("fname") + "</td>");
                out.println("<td>" + resultSet.getString("lname") + "</td>");
                out.println("<td>" + resultSet.getString("town") + "</td>");
                out.println("</tr>");
            }

            out.println("</table>");
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
                    + "<a href=\"Students\">Click here to add a student to the DB.</a>"
                    + "</div>"
                    + "<div>"
                    + "<a href=\"Courses\">Click here to add a course to the DB.</a>"
                    + "</div>"
                    + "<div>"
                    + "<a href=\"Enroll\">Click here to enroll a student in a new course.</a>"
                    + "</div>"
                    + "<div>"
                    + "<a href=\"index.html\">Home Page</a>"
                    + "</div>"
                    + "</header>"
            );
            out.println("<h1>Hello " + user + ". Insert name of student below to see their current courses.</h1>");
            out.println("<form method =\"post\" >"
                    + "First name: <input type=\"text\" name=\"fname\" required><br>"
                    + "Last name: <input type=\"text\" name=\"lname\" required><br><br>"
                    + "<input type=\"submit\" value=\"Submit\"><br>"
                    + "</form>"
                    + "<form action=\"Attendance\" method =\"get\" >"
                    + "<input type=\"submit\" value=\"Display Students\"><br>"
                    + "</form>");

            String sql = "SELECT s.fname, s.lname, c.name AS coursename, c.YHP, c.description FROM students s JOIN attendance a ON a.studentsid = s.id JOIN courses c ON a.subjectsid = c.id WHERE s.fname = ? AND s.lname = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, req.getParameter("fname"));
            statement.setString(2, req.getParameter("lname"));
            ResultSet resultSet = statement.executeQuery();
            out.println("<table class=\"table\">");
            out.println("<tr><th>First Name</th><th>Last Name</th><th>Course Name</th><th>YHP</th><th>Description</th></tr>");

            while (resultSet.next()) {
                out.println("<tr>");
                out.println("<td>" + resultSet.getString(1) + "</td>");
                out.println("<td>" + resultSet.getString(2) + "</td>");
                out.println("<td>" + resultSet.getString(3) + "</td>");
                out.println("<td>" + resultSet.getInt(4) + "</td>");
                out.println("<td>" + resultSet.getString(5) + "</td>");
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
