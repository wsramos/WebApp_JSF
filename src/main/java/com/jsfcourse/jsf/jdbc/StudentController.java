package com.jsfcourse.jsf.jdbc;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;


@ManagedBean
@SessionScoped
public class StudentController {
	
	private List<Student> students;
	private StudentDbUtil studentDbUtil;
	private Logger logger = Logger.getLogger(getClass().getName());
	
	public StudentController() throws Exception {
		students = new ArrayList<>();
		
		studentDbUtil = StudentDbUtil.getInstance();
	}
	
	public List<Student> getStudents(){
		return students;
	}
	
	public void loadStudents() {
		logger.info("Loading students");
		
		students.clear();
		
		try {
			students = studentDbUtil.getStudents();
		}
		catch (Exception e) {
			logger.log(Level.SEVERE, "Error loading students", e);
			
			addErrorMessage(e);
		}
	}
	
	public String addStudent(Student student) {
		studentDbUtil.addStudent(student);
		return "databaseTest";
	}

	private void addErrorMessage(Exception e) {
		FacesMessage message = new FacesMessage("Erros: " + e.getMessage());
		FacesContext.getCurrentInstance().addMessage(null, message);
		
	}

}
