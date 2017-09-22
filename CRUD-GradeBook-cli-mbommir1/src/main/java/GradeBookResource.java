
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import java.util.HashMap;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author mahenderreddyb
 */
public class GradeBookResource {
    private static final Logger LOG = LoggerFactory.getLogger(GradeBookResource.class);
    
    private WebResource webResource;
    private Client client;
    private static final String BASE_URI = "http://localhost:8080/CRUD-GradeBook-srv-mbommir1/webapi";
    
    public GradeBookResource() {        
        LOG.info("Creating a GradeBook REST client");

        ClientConfig config = new DefaultClientConfig();
        client = Client.create(config);
        
    }
    
    public ClientResponse createGradeItem(String requestEntity) throws UniformInterfaceException {
        LOG.info("Initiating a Create request");
        
        WebResource resource  = client.resource(BASE_URI).path("gradebook");
        
        return resource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, requestEntity);
    }
    
    public ClientResponse deleteGradeItem(String id) throws UniformInterfaceException {
        LOG.info("Initiating a Delete request");
        LOG.debug("Id = {}", id);
        
        WebResource resource  = client.resource(BASE_URI).path("gradebook");
        
        return resource.path(id).delete(ClientResponse.class);
    }

    public ClientResponse updateGradeItem(Object requestEntity, String id) throws UniformInterfaceException {
        LOG.info("Initiating an Update request");
        LOG.debug("Id = {}", id);
        
        WebResource resource  = client.resource(BASE_URI).path("gradebook");
        
        return resource.path(id).type(MediaType.APPLICATION_JSON).put(ClientResponse.class, requestEntity);
    }

    public <T> T retrieveGradeItems(Class<T> responseType, HashMap<String, String> queryParamsMap) throws UniformInterfaceException {
        LOG.info("Initiating a Retrieve request");
        LOG.debug("responseType = {}", responseType.getClass());
        
        WebResource resource = client.resource(BASE_URI).path("gradebook");
        
        if (queryParamsMap.containsKey("grade_item_id")) {
            resource = resource.queryParam("grade_item_id", queryParamsMap.get("grade_item_id"));
        }
        
        if (queryParamsMap.containsKey("student_id")) {
            resource = resource.queryParam("student_id", queryParamsMap.get("student_id"));
        }
        
        if (queryParamsMap.containsKey("grade_item_name")) {
            resource = resource.queryParam("grade_item_name", queryParamsMap.get("grade_item_name"));
        }
        
        if (queryParamsMap.containsKey("grade")) {
            resource = resource.queryParam("grade", queryParamsMap.get("grade"));
        }
        
        return resource.accept(MediaType.APPLICATION_JSON).get(responseType);
    }
    
    public <T> T retrieveGradeItem(Class<T> responseType, String id) throws UniformInterfaceException {
        LOG.info("Initiating a Retrieve request");
        LOG.debug("responseType = {}", responseType.getClass());
        LOG.debug("Id = {}", id);
        WebResource resource = client.resource(BASE_URI).path("gradebook");
        
        return resource.path(id).accept(MediaType.APPLICATION_JSON).get(responseType);
    }

    public void close() {
        LOG.info("Closing the REST Client");
        
        client.destroy();
    }
    
}
