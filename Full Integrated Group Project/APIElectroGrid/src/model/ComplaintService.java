package model;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

public class ComplaintService {

	private static Connection connect() {
		Connection con = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			// Database Connection Details
			con = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/grideletro?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",
					"root", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return con;
	}

	
	public static Response insertComplaint(Complaint complaint) {
		//String output = "";
		try {
			Connection con = connect();
			if (con == null) {
				return Response
						.status(Response.Status.INTERNAL_SERVER_ERROR)
						.entity("DataBase connectivity Error")
						.build();

			}
			// create a prepared statement
			String query = " insert into complaint(`compID`,`compAccNO`,`compName`,`compArea`,`compReason`,`compPhone`)" 
			+ " values (?, ?, ?, ?, ?, ?)";
			PreparedStatement preparedStmt = con.prepareStatement(query);
			// binding values
			preparedStmt.setInt(1, 0);
			preparedStmt.setString(2, complaint.getAccNo());
			preparedStmt.setString(3, complaint.getName());
			preparedStmt.setString(4, complaint.getArea());
			preparedStmt.setString(5, complaint.getReason());
			preparedStmt.setString(6, complaint.getPhone());
			// execute the statement
			preparedStmt.execute();
			con.close();
			//output = "Inserted successfully";
		} catch (Exception e) {
			return Response
					.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(e)
					.build();
		}
		return Response
				.status(Response.Status.CREATED)
				.entity(complaint)
				.build();
	}

	
	public Response readComplaints() {
		List<Complaint> complaints = new ArrayList<Complaint> ();

		try {
			Connection con = connect();
			if (con == null) return Response
					.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity("Error while connecting to the database for reading.")
					.build();

			String query = "select * from complaint";
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);

			while (rs.next()) {
				int compID = rs.getInt("compID");
				String compAccNO = rs.getString("compAccNO");
				String compName = rs.getString("compName");
				String compArea = rs.getString("compArea");
				String compReason = rs.getString("compReason");
				String compPhone = rs.getString("compPhone");
				Complaint complaint = new Complaint(compAccNO, compName, compArea, compReason, compPhone);
				complaint.setId(compID);
				complaints.add(complaint);

			}
			con.close();

		} catch (Exception e) {
			return Response
					.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(e)
					.build();
		}

		return Response
				.status(Response.Status.OK)
				.entity(complaints)
				.build();
	}

	
	public static Response updateComplaint(Complaint complaint) {

		try {
			Connection con = connect();

			if (con == null) {
				return Response
						.status(Response.Status.INTERNAL_SERVER_ERROR)
						.entity("DataBase connectivity Error")
						.build();
			}

			// create a prepared statement
			String query = "UPDATE complaint SET compAccNO=?,compName=?,compArea=?,compReason=?,compPhone=?" + "WHERE compID=?";
			
			System.out.println(complaint.getId());
			System.out.println(complaint.getReason());

			PreparedStatement preparedStmt = con.prepareStatement(query);

			// binding values
			preparedStmt.setString(1, complaint.getAccNo());
			preparedStmt.setString(2, complaint.getName());
			preparedStmt.setString(3, complaint.getArea());
			preparedStmt.setString(4, complaint.getReason());
			preparedStmt.setString(5, complaint.getPhone());
			preparedStmt.setInt(6, complaint.getId());

			// execute the statement
			preparedStmt.execute();
			con.close();

			}
			catch (Exception e)
			{
				return Response
						.status(Response.Status.INTERNAL_SERVER_ERROR)
						.entity("Error while updating the complaint")
						.build();
			}

			return Response
					.status(Response.Status.CREATED)
					.entity(complaint)
					.build();
		
	}

	public static Response deleteComplaint(int compID) {

		try {
			Connection con = connect();

			if (con == null) {
				return Response
						.status(Response.Status.INTERNAL_SERVER_ERROR)
						.entity("DataBase connectivity Error")
						.build();
			}

			// create a prepared statement
			String query = "delete from complaint where compID =?";

			PreparedStatement preparedStmt = con.prepareStatement(query);

			// binding values
			preparedStmt.setInt(1, compID);

			// execute the statement
			preparedStmt.execute();
			con.close();


		} catch (Exception e) {
			return Response
					.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(e)
					.build();
		}

		return Response
				.status(Response.Status.OK)
				.entity("Complaint deleted successfully")
				.build();
	}

}
