package edu.asu.cse564.crud.gradebook.srv.mbommir1;


import edu.asu.cse564.crud.gradebook.srv.mbommir1.GradeBookResource;
import edu.asu.cse564.crud.gradebook.srv.mbommir1.GradeItem;
import java.util.ArrayList;
import java.util.HashMap;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author mahenderreddyb
 */
public class GradeBookUtils {
    public static ArrayList<GradeItem> getAllGradeItems(HashMap<String, GradeItem> gradeBookItems) {

        ArrayList gradeItems = new ArrayList(gradeBookItems.values());
        
        return gradeItems;
        
    }
    
    public static ArrayList<GradeItem> filterGradeItemsByStudent(ArrayList<GradeItem> gradeBookItems, String student_id) {
        
        if (gradeBookItems.isEmpty() || student_id.isEmpty()) {
            return gradeBookItems;
        }
        
        ArrayList<GradeItem> resultList = new ArrayList<>();
        for (GradeItem gradeItem : gradeBookItems) {
            if (student_id.equals(gradeItem.getStudentID())) {
                resultList.add(gradeItem);
            }
        }
        return resultList;
    }
    
    public static ArrayList<GradeItem> filterGradeItemsByGrade(ArrayList<GradeItem> gradeBookItems, String grade) {
        
        if (gradeBookItems.isEmpty() || grade.isEmpty()) {
            return gradeBookItems;
        }
        
        ArrayList<GradeItem> resultList = new ArrayList<>();
        for (GradeItem gradeItem : gradeBookItems) {
            if (grade.equals(gradeItem.getGrade())) {
                resultList.add(gradeItem);
            }
        }
        return resultList;
    }
    
    public static ArrayList<GradeItem> filterGradeItemsByName(ArrayList<GradeItem> gradeBookItems, String name) {
        
        if (gradeBookItems.isEmpty() || name.isEmpty()) {
            return gradeBookItems;
        }
        
        ArrayList<GradeItem> resultList = new ArrayList<>();
        for (GradeItem gradeItem : gradeBookItems) {
            if (name.equals(gradeItem.getName())) {
                resultList.add(gradeItem);
            }
        }
        return resultList;
    }
    
}
