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
public class Student {
    private static final Logger LOG = LoggerFactory.getLogger(Student.class);
    
    private String name;
    private String id;

    public Student() {
        LOG.info("Creating an Student object");
    }
    
    public String getID() {
        return this.id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
}
