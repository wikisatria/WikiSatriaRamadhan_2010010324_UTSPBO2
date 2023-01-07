package frame;

import helpers.Koneksi;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class KotaInputFrame extends JFrame {
    private JPanel mainPanel;
    private JTextField idTextField;
    private JTextField kotaTextField;
    private JTextField luasTextField;
    private JButton simpanButton;
    private JButton batalButton;
     private int id;

    public KotaInputFrame () {
        simpanButton.addActionListener(e -> {
            String nama = kotaTextField.getText();
            Double luas = Double.valueOf(luasTextField.getText());
            Connection c = Koneksi.getConnection();
            PreparedStatement ps;
            try {
                if (id == 0){
                    String insertSQL = "INSERT INTO kota VALUES (NULL, ?, ?)";
                    ps = c.prepareStatement(insertSQL);
                    ps.setString(1,nama);
                    ps.setDouble(2,luas);
                    ps.executeUpdate();
                    dispose();
                } else {
                    String updateSQL = "UPDATE kota SET nama_kota = ?," + "luas = ? WHERE id = ?";
                    ps = c.prepareStatement(updateSQL);
                    ps.setString(1,nama);
                    ps.setDouble(2,luas);
                    ps.setInt(3,id);
                    ps.executeUpdate();
                    dispose();
                }
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        init();
    }
    public void init(){
        setContentPane(mainPanel);
        setTitle("Data Kota");
        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public void setId(int id) {
        this.id = id;
    }

    public void isiKomponen() {
        Connection c = Koneksi.getConnection();
        String findSQL = "SELECT * FROM kota WHERE id = ?";
        PreparedStatement ps = null;
        try {
            ps = c.prepareStatement(findSQL);
            ps.setInt(1,id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                idTextField.setText(String.valueOf(rs.getInt("id")));
                kotaTextField.setText(String.valueOf(rs.getString("nama_kota")));
                luasTextField.setText(String.valueOf(rs.getDouble("luas")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
