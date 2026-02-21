import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class LoginForm extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;

    public LoginForm() {
        setTitle("Login");
        setSize(300,200);
        setLayout(new GridLayout(3,2,5,5));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        add(new JLabel("Username:"));
        txtUsername = new JTextField();
        add(txtUsername);

        add(new JLabel("Password:"));
        txtPassword = new JPasswordField();
        add(txtPassword);

        JButton btnLogin = new JButton("Login");
        JButton btnRegister = new JButton("Register");

        add(btnLogin);
        add(btnRegister);

        btnLogin.addActionListener(e -> login());
        btnRegister.addActionListener(e -> new RegisterForm());

        setVisible(true);
    }

    private void login() {

        String sql = "SELECT * FROM Users WHERE username=? AND password=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, txtUsername.getText());
            ps.setString(2, new String(txtPassword.getPassword()));

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("OK");
                JOptionPane.showMessageDialog(this,"Login successful");
            } else {
                JOptionPane.showMessageDialog(this,"Invalid credentials");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}