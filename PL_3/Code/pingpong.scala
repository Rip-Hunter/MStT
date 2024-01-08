import akka.actor._

case object PingMessage
case object PongMessage
case object StartMessage
case object StopMessage

/**
 * An Akka Actor example written by Alvin Alexander of
 * http://alvinalexander.com
 *
 * Shared here under the terms of the Creative Commons
 * Attribution Share-Alike License: http://creativecommons.org/licenses/by-sa/2.5/
 * 
 * more akka info: http://doc.akka.io/docs/akka/snapshot/scala/actors.html
 */
class Ping(pong: ActorRef) extends Actor {
  var count = 0
  def incrementAndPrint { count += 1; println("ping") }
  def receive = {
    case StartMessage =>
        incrementAndPrint
        pong ! PingMessage
    case PongMessage => 
        incrementAndPrint
        if (count > 99) {
          sender ! StopMessage
          println("ping stopped")
          context.stop(self)
        } else {
          sender ! PingMessage
        }
  }
}

class Pong extends Actor {
  def receive = {
    case PingMessage =>
        println("  pong")
        sender ! PongMessage
    case StopMessage =>
        println("pong stopped")
        context.stop(self)
  }
}

object PingPongTest extends App {
  val system = ActorSystem("PingPongSystem")
  val pong = system.actorOf(Props[Pong], name = "pong")
  val ping = system.actorOf(Props(new Ping(pong)), name = "ping")
  // start them going
  ping ! StartMessage
}

// import scala.actors.Actor
// import scala.actors.Actor._

// class Ping(count: int, pong: Actor) extends Actor {
//   def act() {
//     var pingsLeft = count - 1
//     pong ! Ping
//     while (true) {
//       receive {
//         case Pong =>
//           if (pingsLeft % 1000 == 0)
//             Console.println("Ping: pong")
//           if (pingsLeft > 0) {
//             pong ! Ping
//             pingsLeft -= 1
//           } else {
//             Console.println("Ping: stop")
//             pong ! Stop
//             exit()
//           }
//       }
//     }
//   }
// }

// class Pong extends Actor {
//   def act() {
//     var pongCount = 0
//     while (true) {
//       receive {
//         case Ping =>
//           if (pongCount % 1000 == 0)
//             Console.println("Pong: ping "+pongCount)
//           sender ! Pong
//           pongCount = pongCount + 1
//         case Stop =>
//           Console.println("Pong: stop")
//           exit()
//       }
//     }
//   }
// }