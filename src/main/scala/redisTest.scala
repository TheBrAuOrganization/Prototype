import com.redis._

object redisTest extends App {
  val r = new RedisClient("localhost", 6379)

  println(r.get("27936"))
}
