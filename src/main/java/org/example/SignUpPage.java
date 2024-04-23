package org.example;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.* ;

public class SignUpPage {

    static Connection con;

    public SignUpPage(){

        try{
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/trial","root","@shreya123");
        }
        catch(Exception ex){
            System.out.print(ex);
        }

        JFrame f = new JFrame("NEW USER REGISTER");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(1300,900);
        f.setLocationRelativeTo(null);
        f.getContentPane().setBackground(new Color(10,50,60));
        f.setLayout(null);

        Color c=new Color(32,178,170);
        Font ff = new Font("Serif",Font.BOLD, 30);

        JPanel heading = new JPanel();
        heading.setBackground(c);
        heading.setBounds(0,0,1300,100);
        f.add(heading);

        JLabel name = new JLabel("NEW USER REGISTER");
        name.setForeground(Color.WHITE);
        name.setBounds(120,50,400,50);
        name.setFont(ff);
        heading.add(name);

        Font  font  = new Font(Font.DIALOG_INPUT,  Font.BOLD|Font.ITALIC, 30);
        Font font1 = new Font(Font.SERIF,Font.BOLD,35);
        Font font2 = new Font(Font.SANS_SERIF,Font.PLAIN,30);

        JLabel usernameLabel = new JLabel("UserName :");
        usernameLabel.setBounds(350,300,300,50);
        usernameLabel.setFont(font);
        usernameLabel.setForeground(Color.WHITE);
        f.add(usernameLabel);

        JTextField usernameEntryField = new JTextField(20);
        usernameEntryField.setBounds(510,300,250,50);
        usernameEntryField.setFont(font2);
        f.add(usernameEntryField);

        JLabel passwordLabel = new JLabel("Password :");
        passwordLabel.setBounds(350,400 , 250, 50);
        passwordLabel.setFont(font);
        passwordLabel.setForeground(Color.WHITE);
        f.add(passwordLabel);

        JPasswordField passwordEntryField = new JPasswordField(20);
        passwordEntryField.setBounds(510, 400, 250, 50);
        passwordEntryField.setFont(font2);
        f.add(passwordEntryField);

        JCheckBox showPwd = new JCheckBox("Show Password");
        showPwd.setBounds(800, 400, 280, 30);
        showPwd.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD | Font.ITALIC, 30));
        f.add(showPwd);


        JButton signupButton = new JButton("Sign Up");
        signupButton.setBounds(400, 520, 200,50);
        signupButton.setFont(font1);
        f.add(signupButton);

        JButton back  = new JButton("Back");
        back.setBounds(660,520,200,50);
        back.setFont(font1);
        f.add(back);



        // Add ActionListener to showPwd checkbox
        showPwd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (showPwd.isSelected()) {
                    passwordEntryField.setEchoChar((char) 0);
                } else {
                    passwordEntryField.setEchoChar('*');
                }
            }
        });

        signupButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                //String user = t1.getText();
                String pwd = passwordEntryField.getText();
                String uname = usernameEntryField.getText();
                if(pwd.length() < 5) {
                    JOptionPane.showMessageDialog(f, "Atleast 5 characters of password required");
                    return ;
                }
                else if (pwd.contains(" ")) {
                    JOptionPane.showMessageDialog(f, "Password should not contain any spaces");
                    return ;
                }
                try {
                    PreparedStatement check = con.prepareStatement("select * from userDetails where username=?");
                    check.setString(1,uname);
                    ResultSet checkrs = check.executeQuery();
                    if(checkrs.next()){
                        JOptionPane.showMessageDialog(f,"This Username already exist.Please choose another one");
                    }else{
                        PreparedStatement pst = con.prepareStatement("INSERT INTO userDetails (username,password) VALUES (?,?)");
                        pst.setString(1,uname);
                        pst.setString(2,pwd);

                        int result=pst.executeUpdate();
                        if(result>0){
                            JOptionPane.showMessageDialog(f,"User Successfully Registered");
                        }
                    }

                } catch(Exception exc) {
                    System.out.print(exc);

                }
            }
        });



        back.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                f.setVisible(false);
                new LoginPage();
            }
        });



        f.setVisible(true);
    }
    public static void main(String []args){
        new SignUpPage();
    }
}
