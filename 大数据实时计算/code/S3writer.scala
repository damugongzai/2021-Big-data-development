import java.io.{File, FileWriter}
import java.util.{Timer, TimerTask}
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.node.ObjectNode
import com.bingocloud.auth.BasicAWSCredentials
import com.bingocloud.{ClientConfiguration, Protocol}
import com.bingocloud.services.s3.AmazonS3Client
import org.apache.flink.api.common.io.OutputFormat
import org.apache.flink.configuration.Configuration

class S3writer(accessKey: String, secretKey: String, endpoint: String, bucket: String,
               keyPrefix: String, period: Int) extends OutputFormat[ObjectNode] {
  var timer: Timer = _
  var file: File = _
  var fileWriter: FileWriter = _
  var length = 0L
  var amazonS3: AmazonS3Client = _

  def upload: Unit = {
    this.synchronized {
      if (length > 0) {
        fileWriter.close()
        val targetKey = keyPrefix + file.getName
        amazonS3.putObject(bucket, targetKey, file)
        println("开始上传文件：%s 至 %s 桶的 %s 目录下".format(file.getAbsoluteFile, bucket, targetKey))
        file = null
        fileWriter = null
        length = 0L
      }
    }
  }

  override def configure(configuration: Configuration): Unit = {
    timer = new Timer("S3Writer")
    timer.schedule(new TimerTask() {
      def run(): Unit = {
        upload
      }
    }, 10, period)
    val credentials = new BasicAWSCredentials(accessKey, secretKey)
    val clientConfig = new ClientConfiguration()
    clientConfig.setProtocol(Protocol.HTTP)
    amazonS3 = new AmazonS3Client(credentials, clientConfig)
    amazonS3.setEndpoint(endpoint)

  }

  override def open(taskNumber: Int, numTasks: Int): Unit = {

  }

  override def writeRecord(it: ObjectNode): Unit = {
    this.synchronized {
      val time = it.get("value").get("buy_time").asText("").substring(0,7)
      file = new File("summary/"+ time + ".txt")
      fileWriter = new FileWriter(file, true)
      fileWriter.append(it + "\n")
      length += it.toString.length
      fileWriter.flush()
    }
  }

  override def close(): Unit = {
    fileWriter.flush()
    fileWriter.close()
    timer.cancel()
  }
}
