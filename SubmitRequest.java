
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Timestamp;

@WebServlet("/SubmitTenantRequest")
public class SubmitRequest extends HttpServlet {
    private static final String jdbcUrl = "jdbc:mysql://localhost:3306/project2";
    private static final String dbUser = "your_db_user";
    private static final String dbPassword = "your_db_password";
    

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.sendRedirect("submitRequest.html");
	}


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String apartmentNumber = request.getParameter("apartmentNumber");
        String areaOfProblem = request.getParameter("areaOfProblem");
        String description = request.getParameter("description");
        String photo = request.getParameter("photo"); // Handle file upload if necessary
        Timestamp dateTime = new Timestamp(System.currentTimeMillis());
        String status = "pending";

        String insertSQL = "INSERT INTO maintenance_requests (apartment_number, area_of_problem, description, date_time, photo, status) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword);
             PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {

            preparedStatement.setString(1, apartmentNumber);
            preparedStatement.setString(2, areaOfProblem);
            preparedStatement.setString(3, description);
            preparedStatement.setTimestamp(4, dateTime);
            preparedStatement.setString(5, photo);
            preparedStatement.setString(6, status);

            preparedStatement.executeUpdate();
            response.sendRedirect("success.jsp"); // Redirect to a success page

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().append("Error: " + e.getMessage());
        }
    }
}



