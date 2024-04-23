package org.example;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

public class EventScheduler extends JFrame {
    private final List<EventPanel> eventPanels;
    private JTextField eventNameField;
    private JTextField eventDescriptionField;
    private JButton scheduleButton;
    private JButton back;
    private JComboBox<String> categoryComboBox; // Declaring categoryComboBox as a class-level variable
    static Connection con;
    public EventScheduler() {

        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/trial", "root", "@shreya123");
        } catch (Exception ex) {
            System.out.print(ex);
        }
        eventPanels = new ArrayList<>();
        initializeFrame();
        initializeComponents();
        addListeners();

        setTitle("Event Scheduler");
        setSize(1300, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void initializeFrame() {
        getContentPane().setBackground(new Color(56, 4, 14));
    }

    private void initializeComponents() {
        eventNameField = new JTextField("Enter Company name");
        eventNameField.setPreferredSize(new Dimension(400, 20));
        eventNameField.setFont(new Font(Font.SERIF, Font.BOLD | Font.ITALIC, 16));

        eventDescriptionField = new JTextField("Enter Job description");
        eventDescriptionField.setPreferredSize(new Dimension(400, 20));
        eventDescriptionField.setFont(new Font(Font.SERIF, Font.BOLD | Font.ITALIC, 16));

        String[] arr = {"Web Development", "Data Science", "Cyber Security", "Cloud Computing", "Software Development"};
        categoryComboBox = new JComboBox<>(arr);
        categoryComboBox.setPreferredSize(new Dimension(200, 20));
        categoryComboBox.setFont(new Font(Font.SERIF, Font.BOLD | Font.ITALIC, 16));

        scheduleButton = new JButton("Schedule");
        scheduleButton.setPreferredSize(new Dimension(100, 30));
        scheduleButton.setFont(new Font(Font.SERIF, Font.BOLD | Font.ITALIC, 16));



        back=new JButton("Back");
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LoginPage();
            }
        });

        setLayout(new FlowLayout());
        add(eventNameField);
        add(eventDescriptionField);
        add(categoryComboBox);
        add(scheduleButton);
        add(back);
        JPanel eventsPanel = new JPanel();
        eventsPanel.setLayout(new BoxLayout(eventsPanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(eventsPanel);
        scrollPane.setPreferredSize(new Dimension(1200, 600));
        add(scrollPane);
    }

    private void addListeners() {
        scheduleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                scheduleEvent();
            }
        });
    }

    private void scheduleEvent() {
//        System.out.println("Got here");
        String eventName = eventNameField.getText();
        String eventDescription = eventDescriptionField.getText();
        String selectedCategory = (String) categoryComboBox.getSelectedItem();
        try{
            PreparedStatement pt= con.prepareStatement("insert into company (com_name,com_desp,com_req) values(?,?,?)");
            pt.setString(1,eventName);
            pt.setString(2,eventDescription);
            pt.setString(3,selectedCategory);
            pt.executeUpdate();
        }catch(Exception e){
            e.printStackTrace();
        }
        EventPanel eventPanel = new EventPanel(eventName, eventDescription, selectedCategory);
        eventPanels.add(eventPanel);

        JPanel eventsPanel = (JPanel) ((JViewport) ((JScrollPane) getContentPane().getComponent(4)).getComponent(0)).getComponent(0);
        eventsPanel.add(eventPanel);

        revalidate();
        repaint();
    }

    private void updateEventPanels() {
        Container contentPane = getContentPane();
        Component[] components = contentPane.getComponents();

        if (components.length >= 5 && components[4] instanceof JScrollPane) {
            JScrollPane scrollPane = (JScrollPane) components[4];
            JViewport viewport = scrollPane.getViewport();
            if (viewport != null && viewport.getView() instanceof JPanel) {
                JPanel eventsPanel = (JPanel) viewport.getView();
                eventsPanel.removeAll();

                for (EventPanel eventPanel : eventPanels) {
                    eventsPanel.add(eventPanel);
                }

                revalidate();
                repaint();
            }
        }
    }

    private void deleteEventPanel(EventPanel eventPanel) {
        eventPanels.remove(eventPanel);
        updateEventPanels();
    }

    private class EventPanel extends JPanel {
        private JLabel nameLabel;
        private JLabel descriptionLabel;
        private JLabel categoryLabel;
        private JButton deleteButton;

        public EventPanel(String eventName, String eventDescription, String selectedCategory) {
            setLayout(new FlowLayout());
            setBorder(BorderFactory.createLineBorder(Color.BLACK));
            setBackground(Color.WHITE);

            nameLabel = new JLabel("Company: " + eventName);
            descriptionLabel = new JLabel("Description: " + eventDescription);
            categoryLabel = new JLabel("Category: " + selectedCategory);

            deleteButton = new JButton("Delete");
            deleteButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    deleteEventPanel(EventPanel.this);
                }
            });

            add(nameLabel);
            add(descriptionLabel);
            add(categoryLabel);
            add(deleteButton);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(EventScheduler::new);
    }
}

