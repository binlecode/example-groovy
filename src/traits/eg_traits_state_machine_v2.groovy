/**
 * in v2, we adds advanced transition logic in state transfer
 */

trait ActsAsStateMachineWithGuard {
    private def _stateMachine
    abstract def defineStateMachine()

    def transition(event) {
        if (!_stateMachine) {
            _stateMachine = defineStateMachine()
        }

        def state = _stateMachine[this.state]
        def transition = state.find {
            it.event == event && (!it.guard || it.guard(this))
        }

        if (transition) {
            if (transition.beforeAction) {
                transition.beforeAction(this)
            }

            this.state = transition.to

            if (transition.afterAction) {
                transition.afterAction(this)
            }

        } else {
            throw new RuntimeException("invalid event ${event} ignored for state ${this.state} on subject ${this}")
        }
    }
}

class BarSm implements ActsAsStateMachineWithGuard {
    @Override
    def defineStateMachine() {
        [
                "State_A" : [
                        [event: "event_a", to: "State_A", guard: {it.isReadyForStateA()}],
                        [event: "event_b", to: "State_B", afterAction: { println "A to B - after action" }]
                ],
                "State_B" : [
                        [event: "event_a", to: "State_A", beforeAction: {it.doSomethingBeforeStateA()}]
                ]
        ]
    }

    def state

    boolean isReadyForStateA() {
        println 'check is ready for state A'
        true
    }

    def doSomethingBeforeStateA() {
        println '>> doSomethingBeforeStateA'
        return null
    }

}


foo = new BarSm(state: 'State_A')

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

