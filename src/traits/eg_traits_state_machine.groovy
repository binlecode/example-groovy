
trait ActsAsStateMachine {
    private def _stateMachine
    abstract def defineStateMachine()

    def transition(event) {
        if (!_stateMachine) {
            _stateMachine = defineStateMachine()
        }

        def state = _stateMachine[this.state]
        if (state[event]) {
            this.state = state[event]
        } else {
            throw new RuntimeException("invalid event ${event} ignored for state ${this.state} on subject ${this}")
        }
    }
}

class FooSm implements ActsAsStateMachine {
    @Override
    def defineStateMachine() {
        [
                "State_A" : [
                        "event_a" : "State_A",
                        "event_b" : "State_B"
                ],
                "State_B" : [
                        "event_a" : "State_A"
                ]
        ]
    }

    def state

}


foo = new FooSm(state: 'State_A')

foo.transition("event_b")
assert foo.state == 'State_B'
println foo.state

foo.transition('event_a')
assert foo.state == 'State_A'
println foo.state

try {
    foo.transition('event_c')
} catch (ex) {
    assert ex instanceof RuntimeException
    assert ex.message.startsWith('invalid event event_c')
    println ex.message
}

