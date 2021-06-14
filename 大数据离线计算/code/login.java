import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.plaf.*;

public class login extends JFrame {
    public login() {
        initComponents();
    }

    private void button1MouseClicked(MouseEvent e) throws SQLException {
        String admin=textField1.getText();
        char[] password=passwordField1.getPassword();
        String str=String.valueOf(password); //将char数组转化为string类型
        String url = textField3.getText();

        if(admin.equals("jdbc:hive2://bigdata129.depts.bingosoft.net:22129/user35_db")
                &&str.equals("user35")&&url.equals("bingo35"))
        {
            UI ml=new UI();//为跳转的界面
            dispose();//销毁当前界面
        }
        else {
            count++;
            System.out.println("error");
            if(count==3){
                dispose();
            }
        }
    }

    private void initComponents() {
        label1 = new JLabel();
        label2 = new JLabel();
        label3 = new JLabel();
        textField3 = new JTextField();
        button1 = new JButton();
        passwordField1 = new JPasswordField();
        textField1 = new JTextField();

        //======== this ========
        setResizable(false);
        setMinimumSize(new Dimension(450, 300));
        Container contentPane = getContentPane();
        contentPane.setLayout(null);

        //---- label1 ----
        label1.setText("User");
        label1.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 14));
        contentPane.add(label1);
        label1.setBounds(new Rectangle(new Point(85, 70), label1.getPreferredSize()));

        //---- label2 ----
        label2.setText("Password");
        label2.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 14));
        contentPane.add(label2);
        label2.setBounds(new Rectangle(new Point(55, 105), label2.getPreferredSize()));

        //---- label3 ----
        label3.setText("Url");
        label3.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 14));
        contentPane.add(label3);
        label3.setBounds(90, 145, 24, label3.getPreferredSize().height);

        //---- textField3 ----
        textField3.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
        contentPane.add(textField3);
        textField3.setBounds(135, 140, 215, textField3.getPreferredSize().height);

        //---- button1 ----
        button1.setText("Login");
        button1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    button1MouseClicked(e);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
        contentPane.add(button1);
        button1.setBounds(new Rectangle(new Point(165, 200), button1.getPreferredSize()));

        //---- passwordField1 ----
        passwordField1.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 16));
        contentPane.add(passwordField1);
        passwordField1.setBounds(135, 100, 215, passwordField1.getPreferredSize().height);

        //---- textField1 ----
        textField1.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
        contentPane.add(textField1);
        textField1.setBounds(135, 65, 215, textField1.getPreferredSize().height);

        {
            // compute preferred size
            Dimension preferredSize = new Dimension();
            for(int i = 0; i < contentPane.getComponentCount(); i++) {
                Rectangle bounds = contentPane.getComponent(i).getBounds();
                preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
            }
            Insets insets = contentPane.getInsets();
            preferredSize.width += insets.right;
            preferredSize.height += insets.bottom;
            contentPane.setMinimumSize(preferredSize);
            contentPane.setPreferredSize(preferredSize);
        }
        setTitle("登录");
        pack();
        setLocationRelativeTo(getOwner());
        setVisible(true);
    }

    private int count=0;
    private JLabel label1;
    private JLabel label2;
    private JLabel label3;
    private JTextField textField3;
    private JButton button1;
    private JPasswordField passwordField1;
    private JTextField textField1;
    public static void main(String[]args) throws SQLException {
        login a = new login();
    }
}
