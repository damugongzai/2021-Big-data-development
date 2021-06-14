import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class execute {
    private JPanel panel2 = new JPanel();
    private JSplitPane splitPane4 = new JSplitPane();
    private JSplitPane splitPane5 = new JSplitPane();
    private JSplitPane splitPane6 = new JSplitPane();
    private JSplitPane splitPane7 = new JSplitPane();
    private JPanel panel4 = new JPanel();
    private JScrollPane scrollPane2 = new JScrollPane();
    private JScrollPane scrollPane3 = new JScrollPane();
    private JTextArea textArea1 = new JTextArea();
    private JButton button2 = new JButton();
    private JButton button3 = new JButton();
    private JTable table1 = new JTable();
    private static String SQL_state;

    private void button2MouseClicked(MouseEvent e) throws SQLException {
        SQL_state = textArea1.getText();
        connect f = new connect();
        Object[] temp = f.get_column_name_list(SQL_state);
        ResultSet resultSet = f.get_data_resultset(SQL_state);

        DefaultTableModel table2 = new DefaultTableModel(null, temp);

        while (resultSet.next()) {
            List data_ = new ArrayList();
            for(int i = 1; i <= temp.length; i++){
                data_.add(resultSet.getString(i));
            }
            table2.addRow(data_.toArray());
        }

        table1.setModel(table2);
    }

    public JPanel get_panel(){

        panel2.setLayout(new BorderLayout());
        //======== splitPane4 ========
        {
            splitPane4.setOrientation(JSplitPane.VERTICAL_SPLIT);
            splitPane4.setDividerSize(5);
            splitPane4.setEnabled(false);

            //======== splitPane5 ========
            {
                splitPane5.setDividerSize(0);
                splitPane5.setEnabled(false);

                //---- button2 ----
                button2.setText("\u8fd0\u884c");
                button2.setOpaque(true);
                button2.setBackground(Color.blue);
                button2.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 14));
                button2.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        try {
                            button2MouseClicked(e);
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                    }
                });
                splitPane5.setLeftComponent(button2);

                //======== splitPane7 ========
                {
                    splitPane7.setEnabled(false);
                    splitPane7.setDividerSize(0);

                    //---- button3 ----
                    button3.setText("\u4e2d\u6b62");
                    button3.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 14));
                    splitPane7.setLeftComponent(button3);

                    //======== panel4 ========
                    {
                        panel4.setLayout(new BorderLayout());
                    }
                    splitPane7.setRightComponent(panel4);
                }
                splitPane5.setRightComponent(splitPane7);
            }
            splitPane4.setTopComponent(splitPane5);

            //======== splitPane6 ========
            {
                splitPane6.setOrientation(JSplitPane.VERTICAL_SPLIT);
                splitPane6.setDividerLocation(700);
                splitPane6.setDividerSize(4);

                //======== scrollPane2 ========
                {
                    textArea1.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 18));
                    scrollPane2.setViewportView(textArea1);
                }
                splitPane6.setTopComponent(scrollPane2);

                //======== scrollPane3 ========
                {
                    table1.setPreferredScrollableViewportSize(null);
                    table1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                    scrollPane3.setViewportView(table1);
                }
                splitPane6.setBottomComponent(scrollPane3);
            }
            splitPane4.setBottomComponent(splitPane6);
        }
        panel2.add(splitPane4, BorderLayout.CENTER);
        return panel2;
    }


}
