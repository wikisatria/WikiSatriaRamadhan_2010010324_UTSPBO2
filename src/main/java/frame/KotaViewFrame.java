package frame;
import helpers.Koneksi;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.sql.*;

public class KotaViewFrame extends JFrame{
    private JPanel mainPanel;
    private JPanel buttonPanel;
    private JTable viewTable;
    private JButton createButton;
    private JButton deleteButton;
    private JButton updateButton;
    private JButton readButton;

    public KotaViewFrame(){
        createButton.addActionListener(e -> {
            KotaInputFrame inputFrame = new KotaInputFrame();
            inputFrame.setVisible(true);
        });
        deleteButton.addActionListener(e -> {
            int barisTerpilih = viewTable.getSelectedRow();
            if (barisTerpilih < 0) {
                JOptionPane.showMessageDialog(null, "Pilih data dulu");
                return;
            }
            int pilihan = JOptionPane.showConfirmDialog(null,"Yakin mau hapus?", "Konfirmasi hapus",JOptionPane.YES_NO_OPTION);
            if (pilihan == 0){
                TableModel tm = viewTable.getModel();
                int id = Integer.parseInt(tm.getValueAt(barisTerpilih,0).toString());
                Connection c = Koneksi.getConnection();
                String deleteSQL = "DELETE FROM kota WHERE id = ?";
                try {
                    PreparedStatement ps = c.prepareStatement(deleteSQL);
                    ps.setInt(1,id);
                    ps.executeUpdate();
                }catch (SQLException ex){
                    throw new RuntimeException(ex);
                }
            }
        });
        updateButton.addActionListener(e -> {
            int barisTerpilih = viewTable.getSelectedRow();
            if(barisTerpilih < 0){
                JOptionPane.showMessageDialog(null,"Pilih Data Dulu");
                return;
            }
            TableModel tm = viewTable.getModel();
            int id = Integer.parseInt(tm.getValueAt(barisTerpilih,0).toString());
            KotaInputFrame inputFrame = new KotaInputFrame();
            inputFrame.setId(id);
            inputFrame.isiKomponen();
            inputFrame.setVisible(true);

        });
        readButton.addActionListener(e -> {
            isiTable();
        });
        isiTable();
        init();

    }

    public void init(){
        setContentPane(mainPanel);
        setTitle("Data Kota");
        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }
    public void isiTable(){
        Connection c = Koneksi.getConnection();
        String selecSQL = "SELECT * FROM kota";
        try {
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery(selecSQL);
            String header[] = {"id","Nama Kota","Luas"};
            DefaultTableModel dtm = new DefaultTableModel(header,0);
            viewTable.setModel(dtm);
            viewTable.getColumnModel().getColumn(0).setMaxWidth(32);
            Object[] row = new Object[3];
            while (rs.next()){
                row [0] = rs.getInt("id");
                row [1] = rs.getString("nama_kota");
                row [2] = rs.getString("luas");
                dtm.addRow(row);
            }

        }catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
