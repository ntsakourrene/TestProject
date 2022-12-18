package com.backend.test.service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import java.util.Iterator;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import com.backend.test.model.User;

@Service("userService")
public class UserService {


    private static final AtomicLong counter = new AtomicLong();

    private static List<User> users;

    private File initialFile = new File("test_file.txt");

    public List<User> findAllUsers() {
        return users;
    }

    public User findById(long id) {
        try {

            String data = findUserByIdFromFile(id);
            User u = null;
            if (data != null && !data.trim().isEmpty()) {
                ObjectMapper Obj = new ObjectMapper();
                u = Obj.readValue(data, User.class);
            }
            return u;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String findUserByIdFromFile(long id)
            throws IOException {
        File f = new File("test_file.txt");
        if (f.exists() && !f.isDirectory()) {

            InputStream targetStream = new FileInputStream(initialFile);
            StringBuilder resultStringBuilder = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(targetStream))) {
                String line;
                while ((line = br.readLine()) != null) {
                    ObjectMapper Obj = new ObjectMapper();
                    User u = Obj.readValue(line, User.class);
                    if (u.getId() == id) {
                        resultStringBuilder.append(line).append("\n");
                        //break
                    }
                }
            }
            return resultStringBuilder.toString();
        } else {
            return null;
        }
    }

    private List<String> findAllUsersFromFile(long id) throws IOException {
        File initialFile = new File("test_file.txt");
        InputStream inputStream = new FileInputStream(initialFile);

        List<String> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                ObjectMapper Obj = new ObjectMapper();
                User u = Obj.readValue(line, User.class);
                if (u.getId() != id) {
                    list.add(line);
                }
            }
        }
        return list;
    }

    public User findByName(String name) {
        for (User user : users) {
            if (user.getFirstname().equalsIgnoreCase(name)) {
                return user;
            }
        }
        return null;
    }

    public void saveUser(User user) {
        try {
            ObjectMapper Obj = new ObjectMapper();
            String jsonStr = Obj.writeValueAsString(user)+"\n";

            File f = new File("test_file.txt");
            if (f.exists() && !f.isDirectory()) {
                Files.write(Paths.get("test_file.txt"), jsonStr.getBytes(), StandardOpenOption.APPEND);
            } else {
                BufferedWriter writer = new BufferedWriter(new FileWriter("test_file.txt"));
                writer.write(jsonStr);
                writer.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateUser(User user) {
        try {
            List<String> allUsers = findAllUsersFromFile(user.getId());
            deleteAllDataFromFile();
            saveUser(user);
            for (String userJson : allUsers) {
                ObjectMapper Obj = new ObjectMapper();
                User u = Obj.readValue(userJson, User.class);
                saveUser(u);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteUserById(long id) {
        try {
            List<String> allUsers = findAllUsersFromFile(id);
            deleteAllDataFromFile();
            for (String userJson : allUsers) {
                ObjectMapper Obj = new ObjectMapper();
                User u = Obj.readValue(userJson, User.class);
                saveUser(u);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteAllDataFromFile() throws FileNotFoundException {
        PrintWriter writer = new PrintWriter("test_file.txt");
        writer.print("");
        writer.close();
    }

    public boolean isUserExist(User user) {
        try {
            String data = findUserByIdFromFile(user.getId());
            return (data != null && !data.trim().isEmpty());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public void deleteAllUsers() {
        users.clear();
    }
}
