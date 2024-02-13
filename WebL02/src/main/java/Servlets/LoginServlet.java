package Servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //Input credentials to check
        String user = request.getParameter("username");
        String password = request.getParameter("password");

        HttpSession session = request.getSession(true);
        session.setAttribute("username", user);
        session.setAttribute("password", password);

        response.sendRedirect("/login");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        HttpSession session = req.getSession(false);
        String user = (String) session.getAttribute("username");

        out.println("<link rel=\"stylesheet\" href=\"/style.css\">");
        out.println("<html><body>");
        out.println("<header class=\"headerLinks\">"
                + "<div>"
                + "<a href=\"Attendance\">Click here to check who is studying what course.</a>"
                + "</div>"
                + "<div>"
                + "<a href=\"Courses\">Click here to add a course to the DB.</a>"
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
        out.println("<h2>Hello " + user + " from Java Servlet!</h2>");
        out.println("</body></html>");
        out.close();
    }
}