import com.bingocloud.ClientConfiguration;
import com.bingocloud.Protocol;
import com.bingocloud.auth.BasicAWSCredentials;
import com.bingocloud.services.s3.AmazonS3Client;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class addition3 {
    private List table_list = new ArrayList();
    private List column_list = new ArrayList();
    private List data_list = new ArrayList();
    private ResultSet resultSet;
    private Statement statement;
    final String accessKey = "F1C89FE5FFD52C4614EE";
    final String secretKey = "Wzk3MUE2MTU5NUQ3Nzg1ODBENDdDMDNDNzE0NTA3";
    final String endpoint = "http://scut.depts.bingosoft.net:29997";
    final String bucket = "new1";
    final String key = "a.txt";
    final String topic = "lbdsb";
    final String bootstrapServers = "bigdata35.depts.bingosoft.net:29035,bigdata36.depts.bingosoft.net:29036,bigdata37.depts.bingosoft.net:29037";
    AmazonS3Client amazonS3;

    addition3() throws SQLException {
        String url = "jdbc:mysql://bigdata130.depts.bingosoft.net:24306/user35_db";
        Properties properties = new Properties();
        properties.setProperty("driverClassName", "com.mysql.jdbc.Driver");
        properties.setProperty("user", "user35");
        properties.setProperty("password", "pass@bingo35");
        Connection connection = DriverManager.getConnection(url, properties);
        statement = connection.createStatement();
    }

    public void upload(){
        BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        ClientConfiguration clientConfig = new ClientConfiguration();
        clientConfig.setProtocol(Protocol.HTTP);
        amazonS3 = new AmazonS3Client(credentials, clientConfig);
        amazonS3.setEndpoint(endpoint);
        File file = new File(key);
        amazonS3.putObject(bucket, key, file);
    }

    public void produceToKafka(String s3Content){
        //kafka属性配置
        Properties props = new Properties();
        props.put("bootstrap.servers", bootstrapServers);
        props.put("acks", "all");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        KafkaProducer producer = new KafkaProducer<String, String>(props) ;
        ProducerRecord record = new ProducerRecord<String, String>(topic, null, s3Content);
        System.out.println("开始生产数据：" + s3Content);
        producer.send(record);
        producer.flush();
        producer.close();
    }

    public void get_data() throws SQLException{
        resultSet = statement.executeQuery("show tables");
        while (resultSet.next()) {
            String tableName = resultSet.getString(1);
            table_list.add(tableName);
        }
        Object[] table_array = table_list.toArray();

        for(int i=0;i<table_array.length;i++){
            //获取表列名
            column_list = new ArrayList();
            resultSet = statement.executeQuery("show columns from "+table_array[i]);
            while (resultSet.next()) {
                String columnName = resultSet.getString(1);
                column_list.add(columnName);
            }

            resultSet = statement.executeQuery("select * from "+table_array[i]);
            Object[] column_array = column_list.toArray();
            while (resultSet.next()) {
                List<String> datas = new ArrayList<String>();
                String strings = "{";
                for (int j = 1; j <= column_array.length; j++) {
                    datas.add(resultSet.getString(j));
                    strings = strings + "\"" + column_array[j - 1].toString() + "\"" + ":" + "\"" + resultSet.getString(j) + "\"";
                    if (j != column_array.length)
                        strings += ",";
                }
                strings += "}";
                produceToKafka(strings);
            }
        }
    }

    public static void main(String args[]) throws SQLException, IOException {
        addition3 add = new addition3();
        add.get_data();
    }
}