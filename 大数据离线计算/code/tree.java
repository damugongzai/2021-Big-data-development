import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.sql.SQLException;

public class tree {
    public static DefaultMutableTreeNode sort;
    tree(){
        sort= new DefaultMutableTreeNode("All databases");
    }

    public void setTree() throws SQLException {
        connect f = new connect();
        Object[] db_list = f.get_db_list();

        for(int i=0;i<db_list.length;i++){
            DefaultMutableTreeNode db_node = new DefaultMutableTreeNode(db_list[i].toString());
            sort.add(db_node);
            Object[] table_list = f.get_table_list(db_list[i].toString());
            if(!db_list[i].toString().equals("user35_db")) {
                for (int j = 0; j < table_list.length; j++) {
                    DefaultMutableTreeNode table_node = new DefaultMutableTreeNode(table_list[j].toString());
                    db_node.add(table_node);
                }
            }
            else{
                for (int j=0;j<table_list.length;j++){
                    DefaultMutableTreeNode table_node = new DefaultMutableTreeNode(table_list[j].toString());
                    db_node.add(table_node);
                    Object[] column_list = f.get_column_list(table_list[j].toString());
                    for(int m=0;m<column_list.length;m++){
                        DefaultMutableTreeNode column_node = new DefaultMutableTreeNode(column_list[m].toString());
                        table_node.add(column_node);
                    }
                }
            }
        }

    }
}