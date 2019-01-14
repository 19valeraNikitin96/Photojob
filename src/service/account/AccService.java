package service.account;

import common.Validated;
import entity.user.User;
import java.awt.Component;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class AccService {

    public boolean registration(JPanel regPanel) {
        User u = userBuffer(regPanel);
        File users = new File("users.txt");
        if (!users.exists()) {
            try {
                users.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(AccService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(users));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AccService.class.getName()).log(Level.SEVERE, null, ex);
        }
        String data = "";
        try {
            while ((data = br.readLine()) != null) {
                if (checkUser(data, u)) {
                    return false;
                }
            }
            br.close();
        } catch (IOException ex) {
            Logger.getLogger(AccService.class.getName()).log(Level.SEVERE, null, ex);
        }
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(users));
        } catch (IOException ex) {
            Logger.getLogger(AccService.class.getName()).log(Level.SEVERE, null, ex);
        }
        StringBuilder record = new StringBuilder()
                .append(UUID.randomUUID().toString())
                .append(" ")
                .append(u.getName())
                .append(" ")
                .append(u.getSurname())
                .append(" ")
                .append(u.getLogin())
                .append(" ")
                .append(u.getPassword())
                .append(" ")
                .append(u.getAge());
        System.out.println(record.toString());
        try {
            bw.write(record.toString());
            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(AccService.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        return true;
    }

    public boolean checkFields(JPanel regPanel) {
        Component[] components = regPanel.getComponents();
        for (Component c : components) {
            if (!c.getClass().equals(JTextField.class)) {
                continue;
            }
            if (((JTextField) c).getText().equals("")) {
                return false;
            }
        }
        return true;
    }

    private boolean checkUser(String data, User u) {
        Validated<User> v = new Validated<>(u);
        if(!v.validated()) return false;
        return data.contains(u.getLogin()) || data.contains(u.getEmail());
    }
    
    public Boolean authentication(JPanel authPanel){
        User u = userBuffer(authPanel);
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(new File("users.txt")));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AccService.class.getName()).log(Level.SEVERE, null, ex);
        }
        String data;
        try {
            while((data = br.readLine())!= null){
                if(data.contains(u.getLogin()) && data.contains(u.getPassword())){
                    return true;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(AccService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    private User userBuffer(JPanel panel){
        User u = new User();
        for (Component c : panel.getComponents()) {
            if (!c.getClass().equals(JTextField.class)) {
                continue;
            }
            for (Field f : u.getClass().getDeclaredFields()) {
                if (!f.getName().equals(c.getName())) {
                    continue;
                }
                f.setAccessible(true);
                if(c.getName().equals("age")) {
                    try {
                        f.set(u, Integer.parseInt(((JTextField) c).getText()));
                    } catch (IllegalArgumentException | IllegalAccessException ex) {
                        Logger.getLogger(AccService.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                }
                try {
                    f.set(u, ((JTextField) c).getText());
                } catch (IllegalArgumentException | IllegalAccessException ex) {
                    Logger.getLogger(AccService.class.getName()).log(Level.SEVERE, null, ex);
                }
                f.setAccessible(false);
                break;
            }
        }
        return u;
    }
}
