
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet("/servlet/Manager")
public class Manager extends HttpServlet {
    private static final String jdbcUrl = "jdbc:mysql://localhost/project2";
    private static final String dbUser = "your_db_user";
    private static final String dbPassword = "your_db_password";
    

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.sendRedirect("Manager.html");
	}


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword)) {
            if ("addTenant".equals(action)) {
                addTenant(request, connection);
                response.sendRedirect("tenantAdded.jsp");
            } else if ("moveTenant".equals(action)) {
                moveTenant(request, connection);
                response.sendRedirect("tenantMoved.jsp");
            } else if ("deleteTenant".equals(action)) {
                deleteTenant(request, connection);
                response.sendRedirect("tenantDeleted.jsp");
            } else {
                response.getWriter().append("Invalid action.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().append("Database error: " + e.getMessage());
        }
    }

    private void addTenant(HttpServletRequest request, Connection connection) throws SQLException {
        String name = request.getParameter("name");
        String phoneNumber = request.getParameter("phoneNumber");
        String email = request.getParameter("email");
        String checkInDate = request.getParameter("checkInDate");
        String checkOutDate = request.getParameter("checkOutDate");
        String apartmentNumber = request.getParameter("apartmentNumber");

        String insertSQL = "INSERT INTO tenants (name, phone_number, email, check_in_date, check_out_date, apartment_number) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, phoneNumber);
            preparedStatement.setString(3, email);
            preparedStatement.setString(4, checkInDate);
            preparedStatement.setString(5, checkOutDate);
            preparedStatement.setString(6, apartmentNumber);
            preparedStatement.executeUpdate();
        }
    }

    private void moveTenant(HttpServletRequest request, Connection connection) throws SQLException {
        String tenantId = request.getParameter("tenantId");
        String newApartmentNumber = request.getParameter("newApartmentNumber");

        String updateSQL = "UPDATE tenants SET apartment_number = ? WHERE tenant_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {
            preparedStatement.setString(1, newApartmentNumber);
            preparedStatement.setString(2, tenantId);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Tenant not found.");
            }
        }
    }

    private void deleteTenant(HttpServletRequest request, Connection connection) throws SQLException {
        String tenantId = request.getParameter("tenantId");

        String deleteSQL = "DELETE FROM tenants WHERE tenant_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {
            preparedStatement.setString(1, tenantId);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Tenant not found.");
            }
        }
    }
}


