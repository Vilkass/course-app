package webControllers;

import com.google.gson.Gson;
import model.Course;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import utils.DatabaseOperations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Properties;

@Controller
@RequestMapping("/courses")
public class WebCourseController {

    // READ
    @RequestMapping(value = "/getAllCourses", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String getAllCourses() {
        ArrayList<Course> allCourses = new ArrayList<>();
        Gson parser = new Gson();
        try {
            allCourses = DatabaseOperations.getAllCourses();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return parser.toJson(allCourses);
    }

    @RequestMapping(value = "/getCourse/{name}", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String getCourse(@PathVariable("name") String name) {
        Gson parser = new Gson();
        try {
            return parser.toJson(DatabaseOperations.getCourseByName(name));
        } catch (Exception e) {
            e.printStackTrace();
            return "Error selecting";
        }

    }

    //INSERT
    @RequestMapping(value = "/insertCourse", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String insertCourse(@RequestBody String request) {
        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String name = data.getProperty("name");
        LocalDate startDate = LocalDate.parse(data.getProperty("start"));
        LocalDate endDate = LocalDate.parse(data.getProperty("end"));
        int adminId = Integer.parseInt(data.getProperty("adminId"));
        Double price = Double.parseDouble(data.getProperty("price"));
        try {
            DatabaseOperations.insertCourse(name, startDate, endDate, adminId, price);
            return parser.toJson(DatabaseOperations.getCourseByName(name));
        } catch (Exception e) {
            return "There were errors during insert operation";
        }
    }

    //UPDATE
    @RequestMapping(value = "/updCourse", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String updateCourse(@RequestBody String request) {
        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        int courseId = Integer.parseInt(data.getProperty("id"));
        String name = data.getProperty("name");
        Double price = Double.parseDouble(data.getProperty("price"));
        try {
            DatabaseOperations.updateCourse(courseId, "NAME", name);
            DatabaseOperations.updateCourse(courseId, "PRICE", String.valueOf(price));
            return parser.toJson(DatabaseOperations.getCourseByName(name));
        } catch (Exception e) {
            return "There were errors during update operation";
        }
    }

    //DELETE
    @RequestMapping(value = "/delCourseName", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String deleteCourseName(@RequestParam("name") String name) {
        try {
            DatabaseOperations.deleteRecord("course", "NAME", name);
            return "Record deleted";
        } catch (Exception e) {
            return "There were errors during delete operation";
        }
    }

    @RequestMapping(value = "/delCourseId/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String deleteCourseId(@PathVariable("id") int id) {
        try {
            DatabaseOperations.deleteRecord("course", "ID", String.valueOf(id));
            return "Record deleted";
        } catch (Exception e) {
            return "There were errors during delete operation";
        }
    }
}
