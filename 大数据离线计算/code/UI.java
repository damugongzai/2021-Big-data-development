import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;


public class UI extends JFrame {
    public UI() throws SQLException {
        initComponents();
    }

    public class MyCloseActionHandler implements ActionListener {

        public void actionPerformed(ActionEvent evt) {

            Component selected = tabbedPane1.getSelectedComponent();
            if (selected != null) {
                tabbedPane1.remove(selected);
            }

        }
    }

    private void button1MouseClicked(MouseEvent e) {
        // TODO add your code here
        execute all_panel = new execute();
        JPanel whole_panel = all_panel.get_panel();
        tabbedPane1.addTab("新建查詢"+num, whole_panel);

        int index = tabbedPane1.indexOfTab("新建查詢"+num);
        JPanel pnlTab = new JPanel(new GridBagLayout());
        pnlTab.setOpaque(false);
        JLabel lblTitle = new JLabel("新建查詢"+num);
        JButton btnClose = new JButton("x");
        //btnClose.setBounds(100,100,110,40);
        btnClose.setContentAreaFilled(false);//设置按钮透明
        btnClose.setBorder(null);//取消边框
        btnClose.setFont(new Font("黑体",Font.LAYOUT_LEFT_TO_RIGHT,18));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;

        pnlTab.add(lblTitle, gbc);

        gbc.gridx++;
        gbc.weightx = 0;
        pnlTab.add(btnClose, gbc);

        tabbedPane1.setTabComponentAt(index, pnlTab);
        MyCloseActionHandler myCloseActionHandler = new MyCloseActionHandler();
        btnClose.addActionListener(myCloseActionHandler);
        num++;
    }

    private void button2MouseClicked(MouseEvent e) {
        // TODO add your code here
    }

    private void initComponents() throws SQLException {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        splitPane1 = new JSplitPane();
        scrollPane1 = new JScrollPane();
        tree1 = new JTree();
        splitPane2 = new JSplitPane();
        splitPane3 = new JSplitPane();
        button1 = new JButton();
        panel3 = new JPanel();
        tabbedPane1 = new JTabbedPane();
        panel1 = new JPanel();
        splitPane4 = new JSplitPane();
        splitPane5 = new JSplitPane();
        button2 = new JButton();
        splitPane7 = new JSplitPane();
        button3 = new JButton();
        panel4 = new JPanel();
        splitPane6 = new JSplitPane();
        scrollPane2 = new JScrollPane();
        textArea1 = new JTextArea();
        scrollPane3 = new JScrollPane();
        table1 = new JTable();

        //======== this ========
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== splitPane1 ========
        {
            splitPane1.setDividerSize(5);
            splitPane1.setDividerLocation(200);

            //======== scrollPane1 ========
            {
                //---- tree1 ----
                tree tree_ = new tree();
                //tree tree_ = new tree();
                tree_.setTree();
                tree1 = new JTree(tree_.sort);
                tree1.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
                scrollPane1.setViewportView(tree1);
            }
            splitPane1.setLeftComponent(scrollPane1);

            //======== splitPane2 ========
            {
                splitPane2.setOrientation(JSplitPane.VERTICAL_SPLIT);
                splitPane2.setEnabled(false);
                splitPane2.setDividerSize(2);

                //======== splitPane3 ========
                {
                    splitPane3.setDividerSize(0);

                    //---- button1 ----
                    button1.setText("\u65b0\u5efa\u67e5\u8be2");
                    button1.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 14));
                    button1.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            button1MouseClicked(e);
                        }
                    });
                    splitPane3.setLeftComponent(button1);

                    //======== panel3 ========
                    {
                        panel3.setLayout(new BorderLayout());
                    }
                    splitPane3.setRightComponent(panel3);
                }
                splitPane2.setTopComponent(splitPane3);
                splitPane2.setBottomComponent(tabbedPane1);
            }
            splitPane1.setRightComponent(splitPane2);
        }
        contentPane.add(splitPane1, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());

        //======== panel1 ========
        {
            panel1.setLayout(new BorderLayout());

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
                    button2.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 14));
                    button2.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            button2MouseClicked(e);
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
                    splitPane6.setDividerSize(5);

                    //======== scrollPane2 ========
                    {
                        scrollPane2.setViewportView(textArea1);
                    }
                    splitPane6.setTopComponent(scrollPane2);

                    //======== scrollPane3 ========
                    {
                        scrollPane3.setViewportView(table1);
                    }
                    splitPane6.setBottomComponent(scrollPane3);
                }
                splitPane4.setBottomComponent(splitPane6);
            }
            panel1.add(splitPane4, BorderLayout.CENTER);
        }
        setTitle("Spark SQL查询分析器");
        setBackground(Color.black);
        setLocationRelativeTo(getOwner());
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle bounds = new Rectangle(screenSize);
        setBounds(bounds);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JSplitPane splitPane1;
    private JScrollPane scrollPane1;
    private JTree tree1;
    private JSplitPane splitPane2;
    private JSplitPane splitPane3;
    private JButton button1;
    private JPanel panel3;
    private JTabbedPane tabbedPane1;
    private JPanel panel1;
    private JSplitPane splitPane4;
    private JSplitPane splitPane5;
    private JButton button2;
    private JSplitPane splitPane7;
    private JButton button3;
    private JPanel panel4;
    private JSplitPane splitPane6;
    private JScrollPane scrollPane2;
    private JTextArea textArea1;
    private JScrollPane scrollPane3;
    private JTable table1;
    private int num=0;
    public static void main(String[]args) throws SQLException {
        UI a = new UI();
    }
}
