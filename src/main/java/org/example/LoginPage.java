package org.example;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;

public class LoginPage {

    static Connection con;
    static Point loginPageLocation;
    static JFrame loginFrame;


    public LoginPage() {

        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/trial", "root", "@shreya123");
        } catch (Exception ex) {
            System.out.print(ex);
        }

        loginFrame = new JFrame("LOG IN PAGE");
        loginFrame.setSize(1300, 900);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setLocationRelativeTo(null);
        loginFrame.getContentPane().setBackground(new Color(56, 4, 14));
        loginFrame.setLayout(null);

        Color col = new Color(96, 61, 69);

        Font ff = new Font("Serif", Font.BOLD, 30);
        Font font2 = new Font(Font.SANS_SERIF,Font.PLAIN,20);
        Font font3 = new Font(Font.SANS_SERIF,Font.PLAIN,30);

        JPanel heading = new JPanel();
        heading.setBackground(col);
        heading.setBounds(0, 0, 1300, 100);
        loginFrame.add(heading);

        JLabel name = new JLabel("Log In Page");
        name.setForeground(Color.WHITE);
        name.setBounds(120, 50, 400, 50);
        name.setFont(ff);
        heading.add(name);

        Font font = new Font(Font.DIALOG_INPUT, Font.BOLD | Font.ITALIC, 40);

        JTextField t1 = new JTextField();
        t1.setBounds(560, 200, 200, 50);
        t1.setFont(font3);
        loginFrame.add(t1);

        JPasswordField p1 = new JPasswordField();
        p1.setBounds(560, 300, 200, 50);
        p1.setFont(font3);
        loginFrame.add(p1);

        JButton loginButton = new JButton("LOGIN");
        loginButton.setBounds(400, 450, 150, 50);
        loginFrame.add(loginButton);



        JButton signupButton = new JButton("SIGN UP");
        signupButton.setBounds(650, 450, 150, 50);
        loginFrame.add(signupButton);

        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setBounds(300, 200, 200, 50);
        usernameLabel.setFont(font);
        usernameLabel.setForeground(Color.WHITE);
        loginFrame.add(usernameLabel);

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(300, 280, 200, 100);
        passwordLabel.setFont(font);
        passwordLabel.setForeground(Color.WHITE);
        loginFrame.add(passwordLabel);

        JCheckBox showPwd = new JCheckBox("Show Password");
        showPwd.setBounds(800, 310, 280, 30);
        showPwd.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD | Font.ITALIC, 30));
        loginFrame.add(showPwd);

        JRadioButton resumeEntryRadio = new JRadioButton("Open Resume Entry Page");
        resumeEntryRadio.setBounds(1000,600,300,50);
        resumeEntryRadio.setBackground(new Color(10,50,60))    ;
        resumeEntryRadio.setForeground(Color.WHITE);
        resumeEntryRadio.setFont(font2);
        loginFrame.add(resumeEntryRadio);

        JRadioButton resumeFetchRadio = new JRadioButton("Open Applicant Filter Page",true);
        resumeFetchRadio.setBounds(1000,650,300,50);
        resumeFetchRadio.setBackground(new Color(10,50,60))        ;
        resumeFetchRadio.setForeground(Color.WHITE);
        resumeFetchRadio.setFont(font2);
        loginFrame.add(resumeFetchRadio);

        //creating Button group for radioButtons to open one of main two pages;
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(resumeEntryRadio);
        buttonGroup.add(resumeFetchRadio);

        //add the Button button tto login page
        JButton Button = new JButton("Profiles");
        Button.setBounds(550, 550, 150, 50);



        JButton Button1 = new JButton("JOBS");
        Button1.setBounds(550, 550, 150, 50);
        // Assuming resumeEntryRadio is a JRadioButton
        resumeFetchRadio.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    loginFrame.add(Button1);
                } else {
                    loginFrame.add(Button);
                }
                loginFrame.revalidate();
                loginFrame.repaint();
            }
        });
        //Add the actionlisner to Button
        Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new EventScheduler();
            }
        });


        Button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AllJobs();
            }
        });
        // Add ActionListener to showPwd checkbox
        showPwd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (showPwd.isSelected()) {
                    p1.setEchoChar((char) 0);
                } else {
                    p1.setEchoChar('*');
                }
            }
        });


        //add action listener to login button loginButton
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                String user = t1.getText();
                String pwd = p1.getText();
                try {

                    //fetching value of provided username

                    PreparedStatement ps = con.prepareStatement("select * from userDetails where username = ?");
                    ps.setString(1, user);

                    ResultSet rs = ps.executeQuery();
                    String msg = null;

                    boolean popupFlag = true; //flag check whether to show pop up
                    if (rs.next()) {
                        String pass = rs.getString("password");         // fetching value from column 'password'
                        String username = rs.getString("username");     // fetching value from column 'password'

                        if (pwd.equals(pass)){
                            loginFrame.setVisible(false);
                            if(resumeEntryRadio.isSelected()){
                                new ResumeEntry();
                            }else{
                                new ShowResume();
                            }
                            popupFlag = false;
                        }
                        else
                            msg = "Incorrect Password";
                    } else {
                        msg = "User ID does not Exist";
                    }

                    if(popupFlag)JOptionPane.showMessageDialog(loginFrame, msg);
                } catch (Exception exc) {
                    System.out.println(exc);
                }
            }
        });


        //add action listener to signup button
        signupButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae){
                loginFrame.setVisible(false);
                new SignUpPage();
            }
        });

        loginFrame.setVisible(true);
    }

}
