package app

import io.micrometer.core.instrument.{Clock, Counter, Timer}
import io.micrometer.statsd.{StatsdConfig, StatsdFlavor, StatsdMeterRegistry}

object MinimalApplication extends cask.MainRoutes {
  override def port: Int    = 8081
  override def host: String = "0.0.0.0"

  private val statsdRegistry    = util.initStatsDRegistry()
  given requestCounter: Counter = statsdRegistry.counter("request.count")
  given requestTimer: Timer     = statsdRegistry.timer("request.time")

  @cask.get("/")
  def hello() = util.withInstrumentation {
    "Hello, world!"
  }

  @cask.get("/reviews/:limit")
  def getReviews(limit: Int) = util.withInstrumentation {
    ujson.Arr.from(Mongo.findReviews(limit).map(d => ujson.read(d.toJson)))
  }

  initialize()
  println("ready to serve")
}

object util {
  def initStatsDRegistry() = new StatsdMeterRegistry(
    {
      case "statsd.host"   => "telegraf" // Use the Docker service name if running in Docker
      case "statsd.port"   => "8125"
      case "statsd.flavor" => StatsdFlavor.TELEGRAF.name()
      case _               => null
    },
    Clock.SYSTEM
  )

  def withInstrumentation[T](f: => T)(using c: Counter, t: Timer): T = {
    val startTime = System.nanoTime()
    val result    = f
    val endTime   = System.nanoTime()
    val duration  = endTime - startTime
    c.increment()
    t.record(duration, java.util.concurrent.TimeUnit.NANOSECONDS)
    result
  }
}
