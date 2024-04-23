package org.example;

import java.io.*;
import java.sql.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;


public class ResumeEntry{
    static Connection con;

    public ResumeEntry(){

        JFrame entryFrame = new JFrame("Resume Upload");
        entryFrame.setSize(1300,900);
        entryFrame.setLocationRelativeTo(null);
        entryFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        Font font1 = new Font(Font.SERIF,Font.BOLD,40);
        Font font2 = new Font(Font.SANS_SERIF,Font.PLAIN,30);


        JPanel entryPanel  = new JPanel();
        entryPanel.setLayout(null);
        entryPanel.setBackground(new Color(10,50,60));
        entryFrame.add(entryPanel);


        JLabel name = new JLabel("Name : ");        // Creating Label for name
        name.setFont(font1);
        name.setBounds(300,100,300,75);
        name.setForeground(Color.WHITE);
        entryPanel.add(name);

        JTextField tf1 = new JTextField(10);   // Text field for name as tf1
        tf1.setFont(font2);
        tf1.setBounds(600,100,350,55);
        entryPanel.add(tf1);

        JLabel filePath = new JLabel("File Path : ");    // Creating Label for file path
        filePath.setFont(font1);
        filePath.setBounds(300,200,300,75);
        filePath.setForeground(Color.WHITE);
        entryPanel.add(filePath);

        JTextField tf2 = new JTextField();   // Text field filepath as tf2
        tf2.setFont(font2);
        tf2.setBounds(600,200,350,55);
        entryPanel.add(tf2);


        JLabel exp = new JLabel("Experience : ");      // Creating Label for experience
        exp.setFont(font1);
        exp.setBounds(300,300,300,75);
        exp.setForeground(Color.WHITE);
        entryPanel.add(exp);

        JTextField tf3 = new JTextField();   // Text field for e as tf3
        tf3.setFont(font2);
        tf3.setBounds(600,300,350,55);
        entryPanel.add(tf3);


        JLabel special = new JLabel("Specialization : ");      //creating label for Specialization
        special.setFont(font1);
        special.setBounds(300,400,300,75);
        special.setForeground(Color.WHITE);
        entryPanel.add(special);

        String boxList[] = {"Web Development","Data Science","Machine Learning","Cyber Security","Cloud Computing","Internet of Things"};
        JComboBox specialBox = new JComboBox(boxList);
        specialBox.setBounds(600,400,350,55);
        entryPanel.add(specialBox);

        JButton submit = new JButton("Submit");
        submit.setFont(font1);
        submit.setBounds(350,600,200,50);
        entryPanel.add(submit);

        JButton back = new JButton("Back");
        back.setFont(font1);
        back.setBounds(650,600,200,50);
        entryPanel.add(back);

        try{
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/trial", "root","@shreya123");
        }catch(Exception e){System.out.println(e);}

        submit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae){
                String uname = tf1.getText();                           //taking  of valuee of ame,path and specialization from
                String fpath = tf2.getText();
                String sp = (String)specialBox.getSelectedItem();

                int uexp;
                try{
                    uexp =Integer.parseInt(tf3.getText());         // taking value of Experience form textfield tf3 and parsing it.
                }catch(NumberFormatException nfe){
                    System.out.println("Pleae provid experience in interger values as months");
                    uexp = 0;
                }
                System.out.println(uname+" "+fpath+" "+sp+ " "+ uexp);

                try{
                    //Making sure that all values are not empty or null
                    if(!(uname.equals("") || fpath.equals("") || sp.equals(""))){

                        File file = new File(fpath);
                        if(file.isFile()){  // to check if path is valid

                            //SQL query to enter all values in database trial table applicantDetails

                            PreparedStatement ps = con.prepareStatement("insert into applicantDetails (name,path,exp,specialization) values (?,?,?,?)");
                            ps.setString(1,uname);
                            ps.setString(2,fpath);
                            ps.setInt(3,uexp);
                            ps.setString(4,sp);


                            int ret_val = ps.executeUpdate();
                            if(ret_val == 1){
                                JOptionPane.showMessageDialog(entryPanel,"Data Enterd in tab");
                                //clearing all text field values for the next entry
                                tf1.setText("");tf2.setText("");tf3.setText("");
                            }
                        }else{
                            JOptionPane.showMessageDialog(entryPanel,"Invlaid Path");
                        }
                    }else {
                        JOptionPane.showMessageDialog(entryPanel,"Provide all Values");
                    }
                }catch(Exception e){System.out.println(e);}
            }
        });

        back.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae){
                entryFrame.setVisible(false);
                new LoginPage();
            }

        });
        entryFrame.setVisible(true);
    }


    public static void main(String []args){
        new ResumeEntry();
    }
}

