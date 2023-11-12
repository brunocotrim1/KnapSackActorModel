package fcul.ppc.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import fcul.ppc.message.BestInvMessage;
import fcul.ppc.message.PopulationMessage;
import fcul.ppc.utils.Individual;
import fcul.ppc.utils.Utils;

public class BestIndividualActor extends AbstractActor {

    private ActorRef crossOverActor;

    public BestIndividualActor(ActorRef crossOverActor) {
        this.crossOverActor = crossOverActor;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(PopulationMessage.class, message -> {
                    Individual best = Utils.bestOfPopulation(message.getPopulation());
                    System.out.println("Best at generation " + message.getGeneration() + " is " + best + " with "
                            + best.fitness);
                    BestInvMessage bestInvMessage = new BestInvMessage(best,
                            message.getPopulation(), message.getGeneration());
                    crossOverActor.tell(bestInvMessage, getSelf());
                })
                .build();
    }


}
