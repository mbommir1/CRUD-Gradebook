package edu.asu.cse564.crud.gradebook.srv.mbommir1;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Root resource (exposed at "gradebook" path)
 */
@Path("gradebook")
public class GradeBookResource {
    
    private static final Logger LOG = LoggerFactory.getLogger(GradeBookResource.class);
    
    private static final HashMap<String, GradeItem> gradeBookItems = new HashMap<>();
    
    public GradeBookResource() {
        LOG.info("Creating an GradeBook Resource");
    }
    
    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createGradeItem(String jsonObject) throws IOException {
        
        Response response = null;
        ObjectMapper mapper = new ObjectMapper();
        
        if (jsonObject == null) {
            response = Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(generateInvalidJSONMessage("Invalid JSON passed")).build();
            return response;
        }
        
        JsonNode root = mapper.readTree(jsonObject);
        
        JsonNode nodeName = root.path("grade_item_name");
        
        if (nodeName.isMissingNode()) {
            response = Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(generateInvalidJSONMessage("Name of the grade book entry is missing")).build();
            return response;
        }
        
        String name = nodeName.asText();
        
        nodeName = root.path("grade");
        
        if (nodeName.isMissingNode()) {
            response = Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(generateInvalidJSONMessage("Grade in the grade book entry is missing")).build();
            return response;
        }
        
        String grade = nodeName.asText();
        
        nodeName = root.path("remarks");
        
        String remarks = "";
        if (!nodeName.isMissingNode()) {
            remarks = nodeName.asText();
        }
                
        nodeName = root.path("student_id");
        
        if (nodeName.isMissingNode()) {
            response = Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(generateInvalidJSONMessage("Student ID in the grade book entry is missing")).build();
            return response;
        }
        
        String student_id = nodeName.asText();
        
        
        if (name.isEmpty() || student_id.isEmpty() || grade.isEmpty()) {
            response = Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(generateInvalidJSONMessage("One of the node name is empty")).build();
            return response;
        }
        
        Boolean canAdd = checkIfItemDoesNotExist(name, student_id);
        
        if (canAdd) {
            GradeItem gradeItem = new GradeItem(name, grade, student_id, remarks);
        
            String id = gradeItem.getID();
        
            gradeBookItems.put(id, gradeItem);
            
            response = Response.status(Response.Status.CREATED).type(MediaType.APPLICATION_JSON).entity(convertToJSONFromGradeItemObject(gradeItem)).build();
        } else {
            response = Response.status(Response.Status.CONFLICT).type(MediaType.APPLICATION_JSON).entity(generateInvalidJSONMessage("Grade Book entry already present")).build();
        }
        
        
        
        return response;
    }
    
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getGradeItem(@PathParam("id") String id) {
        LOG.info("Retrieving the Grade Book Item {}");
        LOG.debug("GET request");
        LOG.debug("PathParam id = {}", id);
        
        Response response;
        
        if (id == null) {
            response = Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(generateInvalidJSONMessage("Invalid Grade book entry ID")).build();
        }
        
        if (gradeBookItems.isEmpty()) {
            response = Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(generateInvalidJSONMessage("Grade book is empty")).build();
            return response;
        }
        
        GradeItem gradeItem = gradeBookItems.get(id);
        if (gradeItem == null) {
            response = Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON).entity(generateInvalidJSONMessage("Grade book entry not found")).build();
            return response;
        }
        
        response = Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity(convertToJSONFromGradeItemObject(gradeItem)).build();
        
        return response;
    }
    
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getGradeItems(@QueryParam("grade_item_id") String grade_item_id, @QueryParam("grade_item_name") String name, @QueryParam("student_id") String student_id, @QueryParam("grade") String grade) throws IOException {
        LOG.info("Retrieving the Grade Book Items {}");
        LOG.debug("GET request");
        
        Response response;
        
        ArrayList<GradeItem> gradeItems = new ArrayList<>();
        
        if (grade_item_id != null && !grade_item_id.isEmpty()) {
            GradeItem retrievedItem = gradeBookItems.get(grade_item_id);
            if (retrievedItem != null) {
                gradeItems.add(retrievedItem);
            }
            response = Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity(convertToJSONFromArrayList(gradeItems)).build();    
            return response;
        }
        
        gradeItems = GradeBookUtils.getAllGradeItems(gradeBookItems);
                
        
        if (student_id != null && !student_id.isEmpty()) {
            gradeItems = GradeBookUtils.filterGradeItemsByStudent(gradeItems, student_id);
        }
        
        if (grade != null && !grade.isEmpty()) {
            gradeItems = GradeBookUtils.filterGradeItemsByGrade(gradeItems, grade);
        }
                
        if (name != null && !name.isEmpty()) {
            gradeItems = GradeBookUtils.filterGradeItemsByName(gradeItems, name);
        }
        
        response = Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity(convertToJSONFromArrayList(gradeItems)).build();
        
        return response;
    }
    
    
    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateGradeItem(@PathParam("id") String id, String jsonObject) throws IOException {
        
        Response response;
        
        ObjectMapper mapper = new ObjectMapper();
        
        if (jsonObject == null) {
            response = Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(generateInvalidJSONMessage("Invalid JSON passed")).build();
            return response;
        }
        
        GradeItem toBeUpdatedItem = gradeBookItems.get(id);
        
        if (toBeUpdatedItem == null) {
            response = Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON).entity(generateInvalidJSONMessage("Grade book entry not found")).build();
            return response;
        }
        
        JsonNode root = mapper.readTree(jsonObject);
        
        JsonNode nodeName = root.path("grade_item_name");
        
        GradeItem gradeItem = new GradeItem();
        gradeItem.setID(toBeUpdatedItem.getID());
        gradeItem.setName(toBeUpdatedItem.getName());
        gradeItem.setGrade(toBeUpdatedItem.getGrade());
        gradeItem.setStudentID(toBeUpdatedItem.getStudentID());
        gradeItem.setRemarks(toBeUpdatedItem.getRemarks());
        
        
        if (!nodeName.isMissingNode() && nodeName.asText().isEmpty()) {
            response = Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(generateInvalidJSONMessage("Name in the grade book entry cannot be empty")).build();
            return response;
        }
        if (!nodeName.isMissingNode()) {
            gradeItem.setName(nodeName.asText());
        }
        
        nodeName = root.path("grade");
        
        if (!nodeName.isMissingNode() && nodeName.asText().isEmpty()) {
            response = Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(generateInvalidJSONMessage("Grade in the grade book entry cannot be empty")).build();
            return response;
        }
        if (!nodeName.isMissingNode()) {
            gradeItem.setGrade(nodeName.asText());
        }
                
        nodeName = root.path("student_id");
        
        if (!nodeName.isMissingNode() && nodeName.asText().isEmpty()) {
            response = Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(generateInvalidJSONMessage("Student ID in the grade book entry cannot be empty")).build();
            return response;
        }
        if (!nodeName.isMissingNode()) {
            gradeItem.setStudentID(nodeName.asText());
        }
        
        nodeName = root.path("remarks");
        
        if (!nodeName.isMissingNode()) {
           gradeItem.setRemarks(nodeName.asText());
        }
        
        gradeBookItems.put(id, gradeItem);
        
        response = Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity(convertToJSONFromGradeItemObject(gradeItem)).build();
        
        return response;
    }
    
    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteGradeItem(@PathParam("id") String id) {
        Response response;
        
        if (id == null) {
            response = Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(generateInvalidJSONMessage("Invalid Grade book entry ID")).build();
        }
        
        if (gradeBookItems.isEmpty()) {
            response = Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON).entity(generateInvalidJSONMessage("Grade Book is empty")).build();
            return response;
        }
        
        GradeItem gradeItem = gradeBookItems.get(id);
        if (gradeItem == null) {
            response = Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON).entity(generateInvalidJSONMessage("Grade book entry not found")).build();
            return response;
        }
        
        gradeBookItems.remove(id);
        
        response = Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity(generateInvalidJSONMessage("Grade book entry deleted")).build();
        
        return response;
    }
    
    public String convertToJSONFromArrayList(ArrayList<GradeItem> items) {
        ObjectMapper mapper = new ObjectMapper();
        
        JsonNode root = mapper.createObjectNode();
        
        ArrayNode itemsNode = mapper.createArrayNode();
        
        for (GradeItem item : items) {
            ObjectNode itemNode = mapper.createObjectNode();
            itemNode.put("grade_item_id", item.getID());
            itemNode.put("grade_item_name", item.getName());
            itemNode.put("student_id", item.getStudentID());
            itemNode.put("grade", item.getGrade());
            itemNode.put("remarks", item.getRemarks());
            itemsNode.add(itemNode);
        }
        ((ObjectNode) root).set("grade_items", itemsNode);
        return root.toString();
    }
    
    public String convertToJSONFromGradeItemObject(GradeItem item) {
        ObjectMapper mapper = new ObjectMapper();
        
        ObjectNode itemNode = mapper.createObjectNode();

        itemNode.put("grade_item_id", item.getID());
        itemNode.put("grade_item_name", item.getName());
        itemNode.put("student_id", item.getStudentID());
        itemNode.put("grade", item.getGrade());
        itemNode.put("remarks", item.getRemarks());
        
        return itemNode.toString();
    }
    
    public String generateInvalidJSONMessage(String message) {
        ObjectMapper mapper = new ObjectMapper();
        
        ObjectNode itemNode = mapper.createObjectNode();

        itemNode.put("message", message);
        
        return itemNode.toString();
    }
    
    public Boolean checkIfItemDoesNotExist(String name, String student_id) {
        for (GradeItem item : GradeBookUtils.getAllGradeItems(gradeBookItems)) {
            if (name.equalsIgnoreCase(item.getName()) && student_id.equalsIgnoreCase(item.getStudentID()))
                return false;
        }
        return true;
    }
}
