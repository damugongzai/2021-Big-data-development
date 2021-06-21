import java.util
import java.util.{Map, Properties, Scanner, UUID, ArrayList, HashMap}

import com.bingocloud.util.json.JSONObject
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.runtime.client.JobExecutionException
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.core.JsonParseException
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.node.ObjectNode
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer010
import org.apache.flink.streaming.util.serialization.JSONKeyValueDeserializationSchema

object addition5 {
  val bootstrapServers = "bigdata35.depts.bingosoft.net:29035,bigdata36.depts.bingosoft.net:29036,bigdata37.depts.bingosoft.net:29037"
  var Name_data_pairs:Map[String, ArrayList[String]] = new HashMap[String, ArrayList[String]]()
  val inputTopics = "12345";

  class name_data extends Thread{
    val scanner = new Scanner(System.in)
    override def run(): Unit = {
      super.run()
      while(true) {
        val inputs = scanner.next();
        var all = Name_data_pairs.get(inputs)

        if (all == null)  {println("无此人数据！")}
        else
            for(i<-0 to all.size()-1) {
              println("共有",all.size(),"条数据")
              println(all.get(i))}
      }
    }
  }

  def main(args: Array[String]): Unit = {
    var s = new name_data()
    s.start()

    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)
    val kafkaProperties = new Properties()
    kafkaProperties.put("bootstrap.servers", bootstrapServers)
    kafkaProperties.put("group.id", UUID.randomUUID().toString)
    kafkaProperties.put("auto.offset.reset", "earliest")
    kafkaProperties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    kafkaProperties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")

    val kafkaConsumer = new FlinkKafkaConsumer010[String](inputTopics, new SimpleStringSchema, kafkaProperties)
    kafkaConsumer.setCommitOffsetsOnCheckpoints(true)
    val inputKafkaStream = env.addSource(kafkaConsumer)

    inputKafkaStream.map(x => {
      if (x != null) {
        println(x)
        var json_x = new JSONObject(x)
        var name = json_x.get("username").toString()
        if (name != null) {
          if (Name_data_pairs.get(name) == null) {
            Name_data_pairs.put(name, new util.ArrayList[String])
          }
          else Name_data_pairs.get(name).add(x)
        }
      }
    })
    env.execute()
  }
}
