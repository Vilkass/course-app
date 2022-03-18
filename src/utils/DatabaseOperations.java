package utils;

import model.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class DatabaseOperations {

    private static Connection con;
    private static PreparedStatement st;
    private static ResultSet rs;
    private static int status;

    public static ArrayList<Course> getAllCourses() throws SQLException {
        ArrayList<Course> allCourses = new ArrayList<>();
        try{
            con = DatabaseConnection.connect();
            st = con.prepareStatement("SELECT * FROM course");
            rs = st.executeQuery();
        }catch (Exception e){
            e.printStackTrace();
        }

        while (rs.next()) {
            allCourses.add(new Course(rs.getInt(1), rs.getString(2), LocalDate.parse(rs.getString(3)), LocalDate.parse(rs.getString(4)), rs.getDouble(6)));
        }
        DatabaseConnection.disconect(con, st);
        return allCourses;
        // GOOD
    }


    public static Course getCourseByName(String name) throws SQLException {

        try{
            con = DatabaseConnection.connect();
            st = con.prepareStatement("SELECT * FROM course WHERE NAME = ?");
            st.setString(1, name);
            rs = st.executeQuery();
        }catch (Exception e){
            e.printStackTrace();
        }
        if(rs.next()){
            return new Course(rs.getInt(1), rs.getString(2), LocalDate.parse(rs.getString(3)), LocalDate.parse(rs.getString(4)), rs.getDouble(6));
        }
        return null;
        // GOOD
    }

    public static void deleteRecord(String TABLE_NAME, String ARGUMENT, String argumentValue) throws SQLException, Exception{
        try{
            con = DatabaseConnection.connect();
            st = con.prepareStatement("DELETE FROM " + TABLE_NAME + " WHERE " + ARGUMENT + " = ?");
            st.setString(1, argumentValue);
            status = st.executeUpdate();

        }catch (Exception e){
            e.printStackTrace();
        }
        DatabaseConnection.disconect(con, st);

        if(status == 0){
            throw new Exception();
        }

    }

    public static ArrayList<Administrator> getAllAdmins() throws SQLException {
        ArrayList<Administrator> allAdmins = new ArrayList<>();
        try{
            con = DatabaseConnection.connect();
            st = con.prepareStatement("SELECT * FROM admins");
            rs = st.executeQuery();
        }catch (Exception e){
            e.printStackTrace();
        }

        while (rs.next()) {
            allAdmins.add(new Administrator(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7)));
        }
        DatabaseConnection.disconect(con, st);
        return allAdmins;
        // GOOD
    }

    public static ArrayList<Student> getAllStudents() throws SQLException {
        ArrayList<Student> allStudents = new ArrayList<>();
        try{
            con = DatabaseConnection.connect();
            st = con.prepareStatement("SELECT * FROM students");
            rs = st.executeQuery();
        }catch (Exception e){
            e.printStackTrace();
        }

        while (rs.next()) {
            allStudents.add(new Student(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6)));
        }
        DatabaseConnection.disconect(con, st);
        return allStudents;
        // GOOD
    }

    public static ArrayList<Folder> getAllFolders(int courseId) throws SQLException {
        ArrayList<Folder> allFolders = new ArrayList<>();
        try{
            con = DatabaseConnection.connect();
            st = con.prepareStatement("SELECT f.* FROM courseAndFolders as cf, folder as f WHERE cf.COURSE_ID = ? AND cf.FOLDER_ID = f.ID");
            st.setInt(1,courseId);
            rs = st.executeQuery();
        }catch (Exception e){
            e.printStackTrace();
        }

        while (rs.next()) {
            allFolders.add(new Folder(rs.getInt(1), rs.getString(2), LocalDate.parse(rs.getString(3))));
        }
        DatabaseConnection.disconect(con, st);
        return allFolders;
        // GOOD
    }

    public static ArrayList<File> getAllFiles(int folderId) throws SQLException {
        ArrayList<File> allFiles = new ArrayList<>();
        try{
            con = DatabaseConnection.connect();
            st = con.prepareStatement("SELECT f.* FROM folderAndFiles as ff, file as f WHERE ff.FOLDER_ID = ? AND ff.FILE_ID = f.ID");
            st.setInt(1,folderId);
            rs = st.executeQuery();
        }catch (Exception e){
            e.printStackTrace();
        }

        while (rs.next()) {
            allFiles.add(new File(rs.getInt(1), rs.getString(2), LocalDate.parse(rs.getString(3)), rs.getString(4)));
        }
        DatabaseConnection.disconect(con, st);
        return allFiles;
        // GOOD
    }



    public static void insertCourse(String name, LocalDate startDate, LocalDate endDate, int adminId, double price) throws Exception{
        try{
            con = DatabaseConnection.connect();
            st = con.prepareStatement("INSERT INTO course VALUES(?, ?, ?, ?, ?, ?)");
            st.setInt(1,0);
            st.setString(2, name);
            st.setString(3, startDate.toString());
            st.setString(4, endDate.toString());
            st.setDouble(5, price);
            st.setInt(6, adminId);
            status = st.executeUpdate();

            st = con.prepareStatement("SELECT ID FROM course ORDER BY ID DESC LIMIT 1");
            rs = st.executeQuery();
            int index = 0;
            if(rs.next()){
                index = rs.getInt(1);
            }

            st = con.prepareStatement("INSERT INTO createdCourses VALUES(?, ?)");
            st.setInt(1, adminId);
            st.setInt(2, index);
            status = st.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }

        if(status == 0){
            throw new Exception();
        }
        DatabaseConnection.disconect(con, st);
    }


    public static void updateCourse(int id, String colName, String newValue) throws Exception {
        if (!newValue.equals("")) {
            try{
                con = DatabaseConnection.connect();
                st = con.prepareStatement("UPDATE course SET " + colName + "  = ? WHERE id = ?");
                st.setString(1, newValue);
                st.setInt(2, id);
                status = st.executeUpdate();
            }catch (Exception e){
                e.printStackTrace();
            }

            if(status == 0){
                throw new Exception();
            }

            DatabaseConnection.disconect(con, st);
        }
    }

    public static void deleteFile(int id) throws SQLException {
        try{
            con = DatabaseConnection.connect();
            st = con.prepareStatement("DELETE FROM file WHERE ID = ?");
            st.setInt(1, id);
            status = st.executeUpdate();

            st = con.prepareStatement("DELETE FROM foldersAndFiles WHERE FILE_ID = ?");
            st.setInt(1, id);
            status = st.executeUpdate();

        }catch (Exception e){
            e.printStackTrace();
        }

        DatabaseConnection.disconect(con, st);

    }

    public static File selectFileByName(String name) throws SQLException {
        try{
            con = DatabaseConnection.connect();
            st = con.prepareStatement("SELECT * FROM file WHERE NAME = ?");
            st.setString(1, name);
            rs = st.executeQuery();

            if(rs.next()){
                return new File(rs.getInt(1), rs.getString(2), LocalDate.parse(rs.getString(3)), rs.getString(4));
            }


        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public static File selectFile(int id) throws SQLException {
        try{
            con = DatabaseConnection.connect();
            st = con.prepareStatement("SELECT * FROM file WHERE ID = ?");
            st.setInt(1, id);
            rs = st.executeQuery();
            if(rs.next()){
                return new File(rs.getInt(1), rs.getString(2), LocalDate.parse(rs.getString(3)), rs.getString(4));
            }


        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public static void updateFile(int id, String colName, String newValue) throws Exception {
        if (!newValue.equals("")) {
            try{
                con = DatabaseConnection.connect();
                st = con.prepareStatement("UPDATE file SET " + colName + "  = ? WHERE id = ?");
                st.setString(1, newValue);
                st.setInt(2, id);
                status = st.executeUpdate();
            }catch (Exception e){
                e.printStackTrace();
            }

            if(status == 0){
                throw new Exception();
            }

            DatabaseConnection.disconect(con, st);
        }
    }


    public static void insertFile(String name, String resource, int folderId) throws Exception{
        try{
            con = DatabaseConnection.connect();
            st = con.prepareStatement("INSERT INTO file VALUES(?, ?, ?, ?)");
            st.setInt(1,0);
            st.setString(2, name);
            st.setString(3, LocalDate.now().toString());
            st.setString(4, resource);
            status = st.executeUpdate();

            int fileId = 0;
            st = con.prepareStatement("SELECT ID FROM file ORDER BY ID DESC LIMIT 1");
            rs = st.executeQuery();
            if(rs.next()){
                fileId = rs.getInt(1);
            }

            st = con.prepareStatement("INSERT INTO folderAndFiles VALUES(?, ?)");
            st.setInt(1,folderId);
            st.setInt(2,fileId);
            status = st.executeUpdate();

        }catch (Exception e){
            e.printStackTrace();
        }

        if(status == 0){
            throw new Exception();
        }
        DatabaseConnection.disconect(con, st);
    }

    public static Folder selectFolder(int courseId, String folderName) {

        try{
            con = DatabaseConnection.connect();
            st = con.prepareStatement("SELECT f.* FROM courseAndFolders as cf, folder as f WHERE cf.COURSE_ID = ? AND cf.folder_ID = f.ID AND f.NAME = ?");
            st.setInt(1, courseId);
            st.setString(2, folderName);
            rs = st.executeQuery();
            if(rs.next()){
                return new Folder(rs.getInt(1), rs.getString(2), LocalDate.parse(rs.getString(3)));
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }


    public static Folder selectFolderById(int id) {

        try{
            con = DatabaseConnection.connect();
            st = con.prepareStatement("SELECT * FROM folder WHERE ID = ?");
            st.setInt(1, id);
            rs = st.executeQuery();
            if(rs.next()){
                return new Folder(rs.getInt(1), rs.getString(2), LocalDate.parse(rs.getString(3)));
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public static void updateFolder(int id, String colName, String newValue) throws Exception {
        if (!newValue.equals("")) {
            try{
                con = DatabaseConnection.connect();
                st = con.prepareStatement("UPDATE folder SET " + colName + "  = ? WHERE ID = ?");
                st.setString(1, newValue);
                st.setInt(2, id);
                status = st.executeUpdate();
            }catch (Exception e){
                e.printStackTrace();
            }
            if(status == 0){
                throw new Exception();
            }

            DatabaseConnection.disconect(con, st);
        }
    }


    public static int insertFolder(String name, int courseId) throws Exception {
        int folderId = 0;
        try{
            con = DatabaseConnection.connect();
            st = con.prepareStatement("INSERT INTO folder VALUES(?, ?, ?)");
            st.setInt(1,0);
            st.setString(2, name);
            st.setString(3, LocalDate.now().toString());
            status = st.executeUpdate();

            st = con.prepareStatement("SELECT ID FROM folder ORDER BY ID DESC LIMIT 1");
            rs = st.executeQuery();
            if(rs.next()){
                folderId = rs.getInt(1);
            }

            st = con.prepareStatement("INSERT INTO courseAndFolders VALUES(?, ?)");
            st.setInt(1,courseId);
            st.setInt(2,folderId);
            status = st.executeUpdate();

        }catch (Exception e){
            e.printStackTrace();
        }

        if(status == 0){
            throw new Exception();
        }
        DatabaseConnection.disconect(con, st);

        return folderId;
    }

    public static void deleteFolder(int folderId) throws Exception {

        try{
            con = DatabaseConnection.connect();
            st = con.prepareStatement("DELETE FROM folder WHERE ID = ?");
            st.setInt(1, folderId);
            status = st.executeUpdate();

            st = con.prepareStatement("DELETE FROM courseAndFolders WHERE FOLDER_ID = ?");
            st.setInt(1, folderId);
            status = st.executeUpdate();

            st = con.prepareStatement("SELECT FILE_ID FROM folderAndFiles WHERE FOLDER_ID = ?");
            st.setInt(1, folderId);
            rs = st.executeQuery();
            while(rs.next()){
                st = con.prepareStatement("DELETE FROM file WHERE ID = ?");
                st.setInt(1, rs.getInt(1));
                status = st.executeUpdate();
            }

            st = con.prepareStatement("DELETE FROM folderAndFiles WHERE FOLDER_ID = ?");
            st.setInt(1, folderId);
            status = st.executeUpdate();

        }catch (Exception e){
            e.printStackTrace();
        }

        DatabaseConnection.disconect(con, st);
    }


    public static void deleteAdmin(String login, String pass) throws Exception {
        try{
            con = DatabaseConnection.connect();
            st = con.prepareStatement("DELETE FROM admins WHERE LOGIN = ? AND PASS = ?");
            st.setString(1, login);
            st.setString(2, pass);
            status = st.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }
        DatabaseConnection.disconect(con, st);
        if(status == 0){
            throw new Exception();
        }

    }


    public static void deleteStudent(String login, String pass) throws Exception {
        try{
            con = DatabaseConnection.connect();
            st = con.prepareStatement("DELETE FROM students WHERE LOGIN = ? AND PASS = ?");
            st.setString(1, login);
            st.setString(2, pass);
            status = st.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }
        DatabaseConnection.disconect(con, st);
        if(status == 0){
            throw new Exception();
        }

    }

    public static void insertAdmin(String login, String pass, String name, String lastName, String email, String phone) throws Exception {
        try{
            con = DatabaseConnection.connect();
            st = con.prepareStatement("INSERT INTO admins VALUES(?,?,?,?,?,?,?,?)");
            st.setInt(1, 0);
            st.setString(2, login);
            st.setString(3, pass);
            st.setString(4, name);
            st.setString(5, lastName);
            st.setString(6, email);
            st.setString(7, phone);
            st.setString(8, null);
            status = st.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }
        DatabaseConnection.disconect(con, st);
        if(status == 0){
            throw new Exception();
        }

    }

    public static void insertStudent(String login, String pass, String name, String lastName, String email) throws Exception {
        try{
            con = DatabaseConnection.connect();
            st = con.prepareStatement("INSERT INTO students VALUES(?,?,?,?,?,?,?,?)");
            st.setInt(1, 0);
            st.setString(2, login);
            st.setString(3, pass);
            st.setString(4, name);
            st.setString(5, lastName);
            st.setString(6, email);
            st.setString(7, null);
            st.setString(8, null);
            status = st.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }
        DatabaseConnection.disconect(con, st);
        if(status == 0){
            throw new Exception();
        }

    }


    public static Administrator getAdminByLogin(String login) throws Exception{

        try{
            con = DatabaseConnection.connect();
            st = con.prepareStatement("SELECT * FROM admins WHERE LOGIN = ?");
            st.setString(1, login);
            rs = st.executeQuery();
            if(rs.next()){
                return new Administrator(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7));
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static Student getStudentByLogin(String login) throws Exception{

        try{
            con = DatabaseConnection.connect();
            st = con.prepareStatement("SELECT * FROM students WHERE LOGIN = ?");
            st.setString(1, login);
            rs = st.executeQuery();
            if(rs.next()){
                return new Student(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6));
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static Student authenticateStudent(String login, String pass){

        try{
            con = DatabaseConnection.connect();
            st = con.prepareStatement("SELECT * FROM students WHERE LOGIN = ? AND PASS = ?");
            st.setString(1, login);
            st.setString(2, pass);
            rs = st.executeQuery();

            if(rs.next()){
                return new Student(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6));
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static Administrator authenticateAdmin(String login, String pass){

        try{
            con = DatabaseConnection.connect();
            st = con.prepareStatement("SELECT * FROM admins WHERE LOGIN = ? AND PASS = ?");
            st.setString(1, login);
            st.setString(2, pass);
            rs = st.executeQuery();
            if(rs.next()){
                return new Administrator(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7));
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    public static void updateAdmin(String login, String colName, String newValue) throws Exception {
        if (!newValue.equals("")) {
            try{
                con = DatabaseConnection.connect();
                st = con.prepareStatement("UPDATE admins SET " + colName + "  = ? WHERE LOGIN = ?");
                st.setString(1, newValue);
                st.setString(2, login);
                status = st.executeUpdate();
            }catch (Exception e){
                e.printStackTrace();
            }
            if(status == 0){
                throw new Exception();
            }

            DatabaseConnection.disconect(con, st);
        }
    }

    public static void updateStudent(String login, String colName, String newValue) throws Exception {
        if (!newValue.equals("")) {
            try{
                con = DatabaseConnection.connect();
                st = con.prepareStatement("UPDATE students SET " + colName + "  = ? WHERE LOGIN = ?");
                st.setString(1, newValue);
                st.setString(2, login);
                status = st.executeUpdate();
            }catch (Exception e){
                e.printStackTrace();
            }
            if(status == 0){
                throw new Exception();
            }

            DatabaseConnection.disconect(con, st);
        }
    }



}
