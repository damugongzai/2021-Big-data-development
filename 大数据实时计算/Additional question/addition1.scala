import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time

object addition1 {
  val target="b"
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val text = env.socketTextStream("localhost", 9999)

    val counts = text.flatMap(_.toLowerCase.split("\\W+").filter(_.contains(target)))
      .map((_, 1))
      .keyBy(0)
      .timeWindow(Time.seconds(60))
      .sum(1)

    counts.print()

    env.execute("Window Stream WordCount")
  }

}
