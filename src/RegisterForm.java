import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.sql.*;
import java.util.Scanner;

public class RegisterForm extends JFrame {

    private JTextField txtUsername, txtFirst, txtLast, txtEmail, txtDOB;
    private JPasswordField txtPassword;
    private JLabel lblPhoto;

    private byte[] photoBytes;

    public RegisterForm() {

        setTitle("Registration");
        setSize(400,500);
        setLayout(new GridLayout(9,2,5,5));
        setLocationRelativeTo(null);

        txtUsername = new JTextField();
        txtPassword = new JPasswordField();
        txtFirst = new JTextField();
        txtLast = new JTextField();
        txtEmail = new JTextField();
        txtDOB = new JTextField();

        lblPhoto = new JLabel("No Photo");

        JButton btnChoosePhoto = new JButton("Choose Photo");
        JButton btnInsert = new JButton("Insert");
        JButton btnUpdate = new JButton("Update");
        JButton btnDelete = new JButton("Delete");
        JButton btnSelect = new JButton("Select");

        add(new JLabel("Username")); add(txtUsername);
        add(new JLabel("Password")); add(txtPassword);
        add(new JLabel("First Name")); add(txtFirst);
        add(new JLabel("Last Name")); add(txtLast);
        add(new JLabel("Email")); add(txtEmail);
        add(new JLabel("Date (YYYY-MM-DD)")); add(txtDOB);
        add(btnChoosePhoto); add(lblPhoto);
        add(btnInsert); add(btnUpdate);
        add(btnDelete); add(btnSelect);

        btnChoosePhoto.addActionListener(e -> choosePhoto());
        btnInsert.addActionListener(e -> insertUser());
        btnUpdate.addActionListener(e -> updateUser());
        btnDelete.addActionListener(e -> deleteUser());
        btnSelect.addActionListener(e -> selectUser());

        setVisible(true);
    }

    private void choosePhoto() {
        try {
            JFileChooser chooser = new JFileChooser();
            chooser.showOpenDialog(null);
            File file = chooser.getSelectedFile();
            FileInputStream fis = new FileInputStream(file);
            photoBytes = fis.readAllBytes();
            lblPhoto.setText(file.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void insertUser() {
        String sql = "INSERT INTO Users(username,password,first_name,last_name,email,date_of_birth,photo) VALUES(?,?,?,?,?,?,?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, txtUsername.getText());
            ps.setString(2, new String(txtPassword.getPassword()));
            ps.setString(3, txtFirst.getText());
            ps.setString(4, txtLast.getText());
            ps.setString(5, txtEmail.getText());
            ps.setString(6, txtDOB.getText());
            ps.setBytes(7, photoBytes);

            ps.executeUpdate();
            JOptionPane.showMessageDialog(this,"User inserted");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean checkAdminPassword() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter admin password: ");
        String pass = sc.nextLine();
        return pass.equals("admin123");
    }

    private void updateUser() {
        if (!checkAdminPassword()) {
            JOptionPane.showMessageDialog(this,"Wrong password");
            return;
        }

        String sql = "UPDATE Users SET first_name=?, last_name=?, email=? WHERE username=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, txtFirst.getText());
            ps.setString(2, txtLast.getText());
            ps.setString(3, txtEmail.getText());
            ps.setString(4, txtUsername.getText());

            ps.executeUpdate();
            JOptionPane.showMessageDialog(this,"User updated");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteUser() {
        if (!checkAdminPassword()) {
            JOptionPane.showMessageDialog(this,"Wrong password");
            return;
        }

        String sql = "DELETE FROM Users WHERE username=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, txtUsername.getText());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this,"User deleted");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void selectUser() {
        String sql = "SELECT * FROM Users WHERE username=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, txtUsername.getText());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                txtFirst.setText(rs.getString("first_name"));
                txtLast.setText(rs.getString("last_name"));
                txtEmail.setText(rs.getString("email"));
                txtDOB.setText(rs.getString("date_of_birth"));
                JOptionPane.showMessageDialog(this,"User found");
            } else {
                JOptionPane.showMessageDialog(this,"User not found");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}