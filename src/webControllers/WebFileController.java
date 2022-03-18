package webControllers;

import com.google.gson.Gson;
import model.File;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import utils.DatabaseOperations;
import java.util.ArrayList;
import java.util.Properties;

@Controller
@RequestMapping(value = "/file")
public class WebFileController {

    // READ
    @RequestMapping(value = "/getAllFiles", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String getAllFiles(@RequestParam("folderId") int folderId) {
        ArrayList<File> allFiles = new ArrayList<>();
        Gson parser = new Gson();
        try {
            allFiles = DatabaseOperations.getAllFiles(folderId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parser.toJson(allFiles);
    }

    @RequestMapping(value = "/getFile/{id}", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String getFile(@PathVariable("id") int id) {
        Gson parser = new Gson();
        File file = null;
        try {
            file = DatabaseOperations.selectFile(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parser.toJson(file);
    }

    // INSERT
    @RequestMapping(value = "/insertFile", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String insertFile(@RequestBody String request) {
        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String name = data.getProperty("name");
        String resource = data.getProperty("resource");
        int folderId = Integer.parseInt(data.getProperty("folderId"));

        try {
            DatabaseOperations.insertFile(name, resource, folderId);
            return parser.toJson(DatabaseOperations.selectFileByName(name));
        } catch (Exception e) {
            return "There were errors during insert operation";
        }
    }

    // UPDATE
    @RequestMapping(value = "/updFile", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String updateCourse(@RequestBody String request) {
        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        int fileId = Integer.parseInt(data.getProperty("id"));
        String name = data.getProperty("name");
        String resource = data.getProperty("resource");
        try {
            DatabaseOperations.updateFile(fileId, "NAME", name);
            DatabaseOperations.updateFile(fileId, "LINK", resource);
            return parser.toJson(DatabaseOperations.selectFileByName(name));
        } catch (Exception e) {
            return "There were errors during update operation";
        }
    }

    // DELETE
    @RequestMapping(value = "/delFile", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String deleteFile(@RequestParam("fileId") String id) {
        try {
           DatabaseOperations.deleteFile(Integer.parseInt(id));
            return "Record deleted";
        } catch (Exception e) {
            return "There were errors during delete operation";
        }
    }


}
