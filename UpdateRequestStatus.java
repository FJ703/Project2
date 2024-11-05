
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

@WebServlet("/UpdateRequestStatus")
public class UpdateRequestStatus extends HttpServlet {
    private static final String jdbcUrl = "jdbc:mysql://localhost/project2";
    private static final String dbUser = "your_db_user";
    private static final String dbPassword = "your_db_password";
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html");
	}

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int requestId = Integer.parseInt(request.getParameter("requestId"));
        String newStatus = request.getParameter("status");

        String updateSQL = "UPDATE maintenance_requests SET status = ? WHERE request_id = ?";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword);
             PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {

            preparedStatement.setString(1, newStatus);
            preparedStatement.setInt(2, requestId);
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                response.sendRedirect("statusUpdated.jsp"); // Redirect to a success page
            } else {
                response.getWriter().append("Error: Request not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().append("Error: " + e.getMessage());
        }
    }
}




