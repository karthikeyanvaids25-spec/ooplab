import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

public class StudentManagementApp {
    private final List<Student> students = new ArrayList<>();
    private final JTextField idField = new JTextField();
    private final JTextField nameField = new JTextField();
    private final JTextField ageField = new JTextField();
    private final JTextField courseField = new JTextField();
    private final JTextArea displayArea = new JTextArea();
    private final JPanel root = new JPanel(new BorderLayout(10, 10));
    private int nextId = 1;

    public static void main(String[] args) {
        if (args.length > 0 && "--capture".equals(args[0])) {
            new StudentManagementApp().captureWorkflow();
        } else {
            SwingUtilities.invokeLater(() -> new StudentManagementApp().showWindow());
        }
    }

    private StudentManagementApp() {
        root.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        root.setBackground(Color.WHITE);
        displayArea.setEditable(false);
        displayArea.setLineWrap(true);
        displayArea.setWrapStyleWord(true);
        displayArea.setFont(new Font("Arial", Font.PLAIN, 14));
    }

    private void showWindow() {
        JFrame frame = new JFrame("Student Management");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        buildUi();
        frame.setContentPane(root);
        frame.pack();
        frame.setMinimumSize(new Dimension(460, 500));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void buildUi() {
        JPanel fields = new JPanel(new GridLayout(4, 2, 8, 8));
        fields.setBackground(Color.WHITE);
        addField(fields, "ID (for Update/Delete)", idField);
        addField(fields, "Name", nameField);
        addField(fields, "Age", ageField);
        addField(fields, "Course", courseField);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
        buttons.setBackground(Color.WHITE);
        buttons.add(button("Create", new Color(76, 175, 80), e -> createStudent()));
        buttons.add(button("Display", new Color(33, 150, 243), e -> readStudents()));
        buttons.add(button("Update", new Color(255, 152, 0), e -> updateStudent()));
        buttons.add(button("Delete", new Color(244, 67, 54), e -> deleteStudent()));

        JPanel top = new JPanel(new BorderLayout(0, 10));
        top.setBackground(Color.WHITE);
        top.add(fields, BorderLayout.NORTH);
        top.add(buttons, BorderLayout.SOUTH);
        root.add(top, BorderLayout.NORTH);
        root.add(new JScrollPane(displayArea), BorderLayout.CENTER);
    }

    private void addField(JPanel panel, String label, JTextField field) {
        panel.add(new JLabel(label + ":"));
        panel.add(field);
    }

    private JButton button(String text, Color color, java.awt.event.ActionListener action) {
        JButton button = new JButton(text);
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.addActionListener(action);
        return button;
    }

    private void createStudent() {
        Integer age = parseAge();
        if (age == null || nameField.getText().isBlank() || courseField.getText().isBlank()) {
            displayArea.setText("Please enter a name, numeric age, and course.");
            return;
        }
        students.add(new Student(nextId++, nameField.getText().trim(), age, courseField.getText().trim()));
        displayArea.setText("Student created successfully.");
    }

    private void readStudents() {
        StringBuilder output = new StringBuilder();
        for (Student student : students) {
            output.append("ID: ").append(student.id)
                    .append(", Name: ").append(student.name)
                    .append(", Age: ").append(student.age)
                    .append(", Course: ").append(student.course).append('\n');
        }
        displayArea.setText(output.length() == 0 ? "No students found." : output.toString());
    }

    private void updateStudent() {
        Integer id = parseId();
        Integer age = parseAge();
        if (id == null || age == null) {
            displayArea.setText("Please enter a numeric ID and age.");
            return;
        }
        for (Student student : students) {
            if (student.id == id) {
                student.name = nameField.getText().trim();
                student.age = age;
                student.course = courseField.getText().trim();
                displayArea.setText("Student updated successfully.");
                return;
            }
        }
        displayArea.setText("Student not found.");
    }

    private void deleteStudent() {
        Integer id = parseId();
        if (id == null) {
            displayArea.setText("Please enter a numeric ID.");
            return;
        }
        boolean removed = students.removeIf(student -> student.id == id);
        displayArea.setText(removed ? "Student deleted successfully." : "Student not found.");
    }

    private Integer parseId() {
        try {
            return Integer.parseInt(idField.getText().trim());
        } catch (NumberFormatException exception) {
            return null;
        }
    }

    private Integer parseAge() {
        try {
            return Integer.parseInt(ageField.getText().trim());
        } catch (NumberFormatException exception) {
            return null;
        }
    }

    private void captureWorkflow() {
        new File("output").mkdirs();
        buildUi();
        nameField.setText("Alice");
        ageField.setText("20");
        courseField.setText("Computer Science");
        createStudent();
        capture("output/01-create.png");
        readStudents();
        capture("output/02-display.png");
        idField.setText("1");
        nameField.setText("Alice Johnson");
        ageField.setText("21");
        courseField.setText("Software Engineering");
        updateStudent();
        capture("output/03-update.png");
        deleteStudent();
        capture("output/04-delete.png");
        System.out.println("Generated output/01-create.png through output/04-delete.png");
    }

    private void capture(String filename) {
        root.setSize(460, 500);
        root.validate();
        layoutComponents(root);
        BufferedImage image = new BufferedImage(root.getWidth(), root.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        root.paint(graphics);
        graphics.dispose();
        try {
            ImageIO.write(image, "png", new File(filename));
        } catch (IOException exception) {
            throw new IllegalStateException("Could not write " + filename, exception);
        }
    }

    private void layoutComponents(Container container) {
        container.doLayout();
        for (java.awt.Component component : container.getComponents()) {
            if (component instanceof Container child) {
                layoutComponents(child);
            }
        }
    }

    private static final class Student {
        private final int id;
        private String name;
        private int age;
        private String course;

        private Student(int id, String name, int age, String course) {
            this.id = id;
            this.name = name;
            this.age = age;
            this.course = course;
        }
    }
}
