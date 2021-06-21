import java.io.FileWriter
import java.sql.{Connection, DriverManager}
import java.util.{Properties, Timer, TimerTask, UUID}

import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.node.ObjectNode
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer010
import org.apache.flink.streaming.util.serialization.JSONKeyValueDeserializationSchema

import scala.collection.immutable.ListMap

object addition4 {
  val period = 5000
  val inputTopic = "lbsb"
  val bootstrapServers = "bigdata35.depts.bingosoft.net:29035,bigdata36.depts.bingosoft.net:29036,bigdata37.depts.bingosoft.net:29037"
  val path = "addition4.txt"
  var filewriter = new FileWriter(path,true)
  var change = false

  val url = "jdbc:mysql://bigdata130.depts.bingosoft.net:24306/user35_db"
  val properties = new Properties
  properties.setProperty("driverClassName", "com.mysql.jdbc.Driver")
  properties.setProperty("user", "user35")
  properties.setProperty("password", "pass@bingo35")
  val connection = DriverManager.getConnection(url, properties)
  val statement = connection.createStatement

  def main(args: Array[String]): Unit = {

    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)

    statement.execute("create table lbsb(sfzhm varchar(100), " +
      "xm varchar(100), asjbh varchar(100),ajmc varchar(100), aj_jyqk varchar(100))")

    val kafkaProperties = new Properties()
    kafkaProperties.put("bootstrap.servers", bootstrapServers)
    kafkaProperties.put("group.id", UUID.randomUUID().toString)
    kafkaProperties.put("auto.offset.reset", "earliest")
    kafkaProperties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    kafkaProperties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    val kafkaConsumer = new FlinkKafkaConsumer010[ObjectNode](inputTopic,
      new JSONKeyValueDeserializationSchema(true), kafkaProperties)
    kafkaConsumer.setCommitOffsetsOnCheckpoints(true)
    val inputKafkaStream = env.addSource(kafkaConsumer)

    inputKafkaStream.map(x=>{
      val sfzhm = x.get("value").get("sfzhm").toString
      val xm = x.get("value").get("xm").toString
      val asjbh = x.get("value").get("asjbh").toString
      val ajmc = x.get("value").get("ajmc").toString
      val aj_jyqk = x.get("value").get("aj_jyqk").toString

      statement.execute("INSERT INTO lbsb (sfzhm , xm , asjbh ,ajmc , aj_jyqk)"+
        "VALUES"+
        "( " + sfzhm + ","+ xm +"," + asjbh + "," + ajmc + "," + aj_jyqk+"); ")
    })
    env.execute()
  }
}
