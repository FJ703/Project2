
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet("/BrowseServlet")
public class BrowseServlet extends HttpServlet {
    private static final String jdbcUrl = "jdbc:mysql://localhost/project2";
    private static final String dbUser = "your_db_user";
    private static final String dbPassword = "your_db_password";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String apartmentNumber = request.getParameter("apartmentNumber");
        String area = request.getParameter("area");
        String status = request.getParameter("status");
        String dateFrom = request.getParameter("dateFrom");
        String dateTo = request.getParameter("dateTo");

        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM maintenance_requests WHERE 1=1");
        
        if (apartmentNumber != null && !apartmentNumber.isEmpty()) {
            queryBuilder.append(" AND apartment_number = ?");
        }
        if (area != null && !area.isEmpty()) {
            queryBuilder.append(" AND area_of_problem = ?");
        }
        if (status != null && !status.isEmpty()) {
            queryBuilder.append(" AND status = ?");
        }
        if (dateFrom != null && !dateFrom.isEmpty() && dateTo != null && !dateTo.isEmpty()) {
            queryBuilder.append(" AND date_time BETWEEN ? AND ?");
        }

        try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword);
             PreparedStatement preparedStatement = connection.prepareStatement(queryBuilder.toString())) {

            int paramIndex = 1;
            if (apartmentNumber != null && !apartmentNumber.isEmpty()) {
                preparedStatement.setString(paramIndex++, apartmentNumber);
            }
            if (area != null && !area.isEmpty()) {
                preparedStatement.setString(paramIndex++, area);
            }
            if (status != null && !status.isEmpty()) {
                preparedStatement.setString(paramIndex++, status);
            }
            if (dateFrom != null && !dateFrom.isEmpty() && dateTo != null && !dateTo.isEmpty()) {
                preparedStatement.setString(paramIndex++, dateFrom);
                preparedStatement.setString(paramIndex++, dateTo);
            }

            ResultSet resultSet = preparedStatement.executeQuery();
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<h2>Maintenance Requests</h2>");
            out.println("<table border='1'><tr><th>Request ID</th><th>Apartment Number</th><th>Area</th><th>Description</th><th>Date/Time</th><th>Status</th></tr>");

            while (resultSet.next()) {
                out.println("<tr>");
                out.println("<td>" + resultSet.getInt("request_id") + "</td>");
                out.println("<td>" + resultSet.getString("apartment_number") + "</td>");
                out.println("<td>" + resultSet.getString("area_of_problem") + "</td>");
                out.println("<td>" + resultSet.getString("description") + "</td>");
                out.println("<td>" + resultSet.getTimestamp("date_time") + "</td>");
                out.println("<td>" + resultSet.getString("status") + "</td>");
                out.println("</tr>");
            }
            out.println("</table>");

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().append("Error: " + e.getMessage());
        }
    }
}


