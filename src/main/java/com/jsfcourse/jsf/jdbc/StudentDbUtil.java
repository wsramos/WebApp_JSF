package com.jsfcourse.jsf.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class StudentDbUtil {

	private static StudentDbUtil instance;
	private DataSource dataSource;
	private String jndiName = "java:comp/env/jdbc/JSF_WebApp";

	public static StudentDbUtil getInstance() throws Exception {
		if (instance == null) {
			instance = new StudentDbUtil();
		}

		return instance;
	}

	private StudentDbUtil() throws Exception {
		dataSource = getDataSource();
	}

	private DataSource getDataSource() throws NamingException {
		Context context = new InitialContext();

		DataSource theDataSource = (DataSource) context.lookup(jndiName);

		return theDataSource;
	}

	public List<Student> getStudents() throws Exception {

		List<Student> students = new ArrayList<>();

		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;

		try {
			myConn = dataSource.getConnection();

			String sql = "select * from student;";

			myStmt = myConn.createStatement();

			myRs = myStmt.executeQuery(sql);

			while (myRs.next()) {
				Integer id = myRs.getInt("id");
				String firstName = myRs.getString("first_name");
				String lastName = myRs.getString("last_name");
				String email = myRs.getString("email");

				Student tempStudent = new Student();
				tempStudent.setId(id);
				tempStudent.setFirstName(firstName);
				tempStudent.setLastName(lastName);
				tempStudent.setEmail(email);

				students.add(tempStudent);
			}

			return students;
		} finally {
			close(myConn, myStmt, myRs);
		}

	}

	public void addStudent(Student student) {
		System.out.println("chegou");
		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;

		try {
			myConn = dataSource.getConnection();
			
			myStmt = myConn.prepareStatement("INSERT INTO student\r\n" + 
							"(id, first_name, last_name, email)\r\n" + 
							"VALUES\r\n" + 
							"(?, ?, ?, ?)",
							Statement.RETURN_GENERATED_KEYS);
			myStmt.setString(1, null);
			myStmt.setString(2, student.getFirstName());
			myStmt.setString(3, student.getLastName());
			myStmt.setString(4, student.getEmail());
			
			int rowsAffected = myStmt.executeUpdate();	
			
			if(rowsAffected > 0) {
				myRs = myStmt.getGeneratedKeys();
				while (myRs.next()) {
					int id = myRs.getInt(1);
					System.out.println("Done! ID = " + id);
				}
			}else {
				System.out.println("No rows affected");
			}

			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void close(Connection myConn, Statement myStmt, ResultSet myRs) {
		try {
			myConn.close();
			myStmt.close();
			myRs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
