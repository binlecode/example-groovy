@Grab(group='org.codehaus.gpars', module='gpars', version='1.2.1')


import groovyx.gpars.actor.Actors
import groovyx.gpars.actor.DefaultActor

/**
 * This example shows "Rock, Paper, Scissors" game implemented with GPars using a Coordinator actor
 * and two Player actors. To make it fun, we have two players one for prod one for engr.
 * If prod wins: its a bug, if engr wins: its a feature, else: 'escalate to manager'
 */

enum Move {
    ROCK,
    PAPER,
    SCISSORS

    static size = 3

    static Move winner(Move m1, Move m2) {
        final rivals = [m1, m2]
        if (rivals.contains(ROCK) && rivals.contains(PAPER)) {
            return PAPER
        } else if (rivals.contains(PAPER) && rivals.contains(SCISSORS)) {
            return SCISSORS
        } else if (rivals.contains(SCISSORS) && rivals.contains(ROCK)) {
            return ROCK
        } else {  // they are the same, return null to flag a draw
            return null
        }
    }
}

class Player extends DefaultActor {
    String name
    def random = new Random()

    @Override
    void act() {
        loop {
            react { msg ->
                if (msg == 'game over') {
                    stop()
                } else {
                    def move = Move.values()[random.nextInt(Move.size)]
                    println "${this.name} shows $move"
                    reply ([player: this.name, move: move])
                }
            }
        }
    }
}

/**
 * We have two players: prod and dev team
 */
final String NAME_PROD = 'Prod'
final String NAME_DEV = 'Dev'
Player player1 = new Player(name: NAME_PROD).start()
Player player2 = new Player(name: NAME_DEV).start()

/**
 * Utility closure to announce the winner
 */
Closure announce = { String playerName ->
    if (playerName == NAME_PROD) {
        println "=> Prod wins: its a BUG!  :("
    } else if (playerName == NAME_DEV) {
        println "=> Dev wins: its a FEATURE!  :)"
    } else {
        println '=> No wins: escalate to management ...'
    }
}

//todo: create a console interactive version that:
//todo: - ask user to pick either prod or dev side, then code pick the other side
//todo: - ask user for a move, then code will show a move too (random), then announce winner
//todo: - ask user if continue with another round

/**
 * This is the coordinator actor that calls two players to show their moves and announce the winner.
 */
def coordinator = Actors.actor {
    Player p1 = player1
    Player p2 = player2
    int games = 0

    p1.send 'show your move'
    p2.send 'show your move'

    loop {
        react { msgA ->
            react { msgB ->
                Move winnerMove = Move.winner(msgA.move, msgB.move)
                if (!winnerMove) {
                    announce(null)
                } else {
                    String winnerPlayerName = (msgA.move == winnerMove ? msgA.player : msgB.player)
                    announce(winnerPlayerName)
                }

                if (games++ > 10) {
                    p1.send 'game over'
                    p2.send 'game over'

                    println '... enough scrum today, done ...'
                    stop()
                } else {
                    p1.send 'show your next move'
                    p2.send 'show your next move'
                }
            }
        }
    }
}

// wait until all the actors are stopped and their threads are joined
player1.join()
player2.join()
coordinator.join()
