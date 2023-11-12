package fcul.ppc.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import fcul.ppc.message.PopulationMessage;

public class FitnessActor extends AbstractActor {
    private ActorRef bestIndividualActor;

    public FitnessActor(ActorRef bestIndividualActor) {
        this.bestIndividualActor = bestIndividualActor;
    }


    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(PopulationMessage.class, message -> {

                    for (int i = 0; i < message.getPopulation().length; i++) {
                        message.getPopulation()[i].measureFitness();
                    }
                    bestIndividualActor.tell(message, getSelf());

                })
                .build();
    }
}
