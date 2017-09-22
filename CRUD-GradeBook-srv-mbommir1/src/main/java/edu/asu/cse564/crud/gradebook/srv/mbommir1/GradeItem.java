/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.asu.cse564.crud.gradebook.srv.mbommir1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author mahenderreddyb
 */
public final class GradeItem {
    private static final Logger LOG = LoggerFactory.getLogger(Student.class);
    
    private static int serialNumber = 1;
    
    private String id;
    private String name;
    private String student_id;
    private String grade;
    private String remarks;
    
    public String getID() {
        return this.id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getStudentID() {
        return this.student_id;
    }
    
    public String getGrade() {
        return this.grade;
    }
    
    public String getRemarks() {
        return this.remarks;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setStudentID(String id) {
        this.student_id = id;
    }
    
    public void setGrade(String grade) {
        this.grade = grade;
    }
    
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    
    public void setID(String id) {
        this.id = id;
    }
    
    public GradeItem() {}
    
    public GradeItem(String name, String grade, String id, String remarks) {
        Integer newID = GradeItem.serialNumber;
        GradeItem.serialNumber++;
        this.setID(newID.toString());
        this.setName(name);
        this.setGrade(grade);
        this.setRemarks(remarks);
        this.setStudentID(id);
    }
    
    public String convertToResponse() {
        StringBuilder responseString = new StringBuilder();
        responseString.append(this.name).append(" , ").append(this.student_id).append(" , ").append(this.grade).append(" , ").append(this.remarks);
        return responseString.toString();
    }
}
