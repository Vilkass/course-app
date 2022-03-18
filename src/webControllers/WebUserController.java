package webControllers;

import com.google.gson.Gson;
import model.Administrator;
import model.Student;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import utils.DatabaseOperations;

import java.util.ArrayList;
import java.util.Properties;

@Controller
@RequestMapping(value = "/users")
public class WebUserController {

    //READ
    @RequestMapping(value = "/getAllAdmins", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String getAllAdmins() {
        ArrayList<Administrator> allAdmins = new ArrayList<>();
        Gson parser = new Gson();
        try {
            allAdmins = DatabaseOperations.getAllAdmins();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parser.toJson(allAdmins);
    }

    @RequestMapping(value = "/getAllStudents", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String getAllStudents() {
        ArrayList<Student> allStudents = new ArrayList<>();
        Gson parser = new Gson();
        try {
            allStudents = DatabaseOperations.getAllStudents();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parser.toJson(allStudents);
    }

    @RequestMapping(value = "/getStudent/{login}", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String getStudent(@PathVariable("login") String login) {
        Gson parser = new Gson();
        Student stud = null;
        try {
            stud = DatabaseOperations.getStudentByLogin(login);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parser.toJson(stud);
    }

    @RequestMapping(value = "/getAdmin/{login}", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String getAdmin(@PathVariable("login") String login) {
        Gson parser = new Gson();
        Administrator admin = null;
        try {
            admin = DatabaseOperations.getAdminByLogin(login);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parser.toJson(admin);
    }



    //Authorization
    @RequestMapping(value = "/adminLogin", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String loginAdmin(@RequestBody String request) {
        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String login = data.getProperty("login");
        String pass = data.getProperty("pass");
        Administrator admin = null;
        try {
            admin = DatabaseOperations.authenticateAdmin(login, pass);
        } catch (Exception e) {
            return "Error";
        }
        if (admin == null) {
            return "Wrong credentials";
        }
        return Integer.toString(admin.getId());
    }

    @RequestMapping(value = "/studentLogin", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String loginStudent(@RequestBody String request) {
        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String login = data.getProperty("login");
        String pass = data.getProperty("pass");
        Student stud = null;
        try {
            stud = DatabaseOperations.authenticateStudent(login, pass);
        } catch (Exception e) {
            return "Error";
        }
        if (stud == null) {
            return "Wrong credentials";
        }
        return Integer.toString(stud.getId());
    }



    //INSERT
    @RequestMapping(value = "/insertAdmin", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String insertAdministrator(@RequestBody String request) {
        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String login = data.getProperty("login");
        String pass = data.getProperty("pass");
        String name = data.getProperty("name");
        String lastName = data.getProperty("lastName");
        String email = data.getProperty("email");
        String phoneNum = data.getProperty("phoneNum");
        try {
            DatabaseOperations.insertAdmin(login, pass, name, lastName, email, phoneNum);
            return parser.toJson(DatabaseOperations.getAdminByLogin(login));
        } catch (Exception e) {
            return "There were errors during insert operation";
        }
    }

    @RequestMapping(value = "/insertStudent", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String insertStudent(@RequestBody String request) {
        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String login = data.getProperty("login");
        String pass = data.getProperty("pass");
        String name = data.getProperty("name");
        String lastName = data.getProperty("lastName");
        String email = data.getProperty("email");
        try {
            DatabaseOperations.insertStudent(login, pass, name, lastName, email);
            return parser.toJson(DatabaseOperations.getStudentByLogin(login));
        } catch (Exception e) {
            return "There were errors during insert operation";
        }
    }

    //UPDATE
    @RequestMapping(value = "/updAdmin", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String updateAdmin(@RequestBody String request) {
        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String login = data.getProperty("login");
        String pass = data.getProperty("pass");
        String name = data.getProperty("name");
        String lastName = data.getProperty("lastName");
        String email = data.getProperty("email");
        String phoneNum = data.getProperty("phoneNum");
        try {
            DatabaseOperations.updateAdmin(login, "PASS", pass);
            DatabaseOperations.updateAdmin(login, "NAME", name);
            DatabaseOperations.updateAdmin(login, "LAST_NAME", lastName);
            DatabaseOperations.updateAdmin(login, "EMAIL", email);
            DatabaseOperations.updateAdmin(login, "PHONE_NUMBER", phoneNum);
            return parser.toJson(DatabaseOperations.getAdminByLogin(login));
        } catch (Exception e) {
            return "There were errors during update operation";
        }
    }

    @RequestMapping(value = "/updStudent", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String updateStudent(@RequestBody String request) {
        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String login = data.getProperty("login");
        String pass = data.getProperty("pass");
        String name = data.getProperty("name");
        String lastName = data.getProperty("lastName");
        String email = data.getProperty("email");
        int cardId = Integer.parseInt(data.getProperty("cardId"));
        try {
            DatabaseOperations.updateStudent(login, "PASS", pass);
            DatabaseOperations.updateStudent(login, "NAME", name);
            DatabaseOperations.updateStudent(login, "LAST_NAME", lastName);
            DatabaseOperations.updateStudent(login, "EMAIL", email);
            DatabaseOperations.updateStudent(login, "CREDIT_CARD_ID", String.valueOf(cardId));
            return parser.toJson(DatabaseOperations.getStudentByLogin(login));
        } catch (Exception e) {
            return "There were errors during update operation";
        }
    }


    //DELETE
    @RequestMapping(value = "/delAdmin", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String deleteAdmin(@RequestBody String request) {

        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String login = data.getProperty("login");
        String pass = data.getProperty("pass");
        try {
            DatabaseOperations.deleteAdmin(login, pass);
            return "Record deleted";
        } catch (Exception e) {
            return "There were errors during delete operation";
        }
    }

    @RequestMapping(value = "/delStudent", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String deleteStudent(@RequestBody String request) {
        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String login = data.getProperty("login");
        String pass = data.getProperty("pass");
        try {
            DatabaseOperations.deleteStudent(login, pass);
            return "Record deleted";
        } catch (Exception e) {
            return "There were errors during delete operation";
        }
    }

}
