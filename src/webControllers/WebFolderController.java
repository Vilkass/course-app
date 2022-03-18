package webControllers;

import com.google.gson.Gson;
import model.Administrator;
import model.Folder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import utils.DatabaseOperations;

import java.util.ArrayList;
import java.util.Properties;

@Controller
@RequestMapping(value = "/folder")
public class WebFolderController {

    // READ
    @RequestMapping(value = "/getAllFolders", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String getAllFolders(@RequestParam("courseId") int courseId) {
        ArrayList<Folder> allFolders = new ArrayList<>();
        Gson parser = new Gson();
        try {
            allFolders = DatabaseOperations.getAllFolders(courseId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parser.toJson(allFolders);
    }

    @RequestMapping(value = "/getFolder", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String getFolder(@RequestParam("courseId") int courseId, @RequestParam("folderName") String folderName) {
        Gson parser = new Gson();
        Folder folder = null;
        try {
            folder = DatabaseOperations.selectFolder(courseId, folderName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parser.toJson(folder);
    }

    //INSERT
    @RequestMapping(value = "/insertFolder", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String insertFolder(@RequestBody String request) {
        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String name = data.getProperty("name");
        int courseId = Integer.parseInt(data.getProperty("courseId"));
        try {
            int folderId = DatabaseOperations.insertFolder(name, courseId);
            return parser.toJson(DatabaseOperations.selectFolderById(folderId));
        } catch (Exception e) {
            return "There were errors during insert operation";
        }
    }



    // UPDATE
    @RequestMapping(value = "/updFolder", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String updateFolder(@RequestBody String request) {
        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        int folderId = Integer.parseInt(data.getProperty("id"));
        String name = data.getProperty("name");
        try {
            DatabaseOperations.updateFolder(folderId, "NAME", name);
            return parser.toJson(DatabaseOperations.selectFolderById(folderId));
        } catch (Exception e) {
            return "There were errors during update operation";
        }
    }



    //DELETE
    @RequestMapping(value = "/delFolder", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String deleteFile(@RequestParam("folderId") String id) {
        try {
            DatabaseOperations.deleteFolder(Integer.parseInt(id));
            return "Record deleted";
        } catch (Exception e) {
            return "There were errors during delete operation";
        }
    }



}
