## What is Redis?

Redis is a highly performant key-value store that provides a lot of features such as sorted sets and even pubsub support: http://code.google.com/p/redis/

## Experimenting with Rediscala

- Get sbt from http://code.google.com/p/simple-build-tool/ (if you don't already have it).
- On the command line run "sbt update console" to bootstrap into the Scala REPL with the scalaredis code available to you.
- Get a connection and start experimenting:
    import com.github.rediscala._
    val connection = new RedisConnection()
    connection.set("TEST", "cake")
    connection.get("TEST")        
    connection.get("TEST1")
