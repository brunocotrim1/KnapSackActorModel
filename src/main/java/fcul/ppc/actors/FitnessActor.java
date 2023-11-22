package fcul.ppc.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import fcul.ppc.message.PopulationMessage;

import static fcul.ppc.utils.Utils.POP_SIZE;

public class FitnessActor extends AbstractActor {
    private ActorRef bestIndividualActor;

    public FitnessActor(ActorRef bestIndividualActor) {
        this.bestIndividualActor = bestIndividualActor;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(PopulationMessage.class, message -> {
                    for (int i = 0; i < POP_SIZE; i++) {
                        message.getPopulation()[i].measureFitness();
                    }
                    bestIndividualActor.tell(message, getSelf());

                })
                .build();
    }
}
