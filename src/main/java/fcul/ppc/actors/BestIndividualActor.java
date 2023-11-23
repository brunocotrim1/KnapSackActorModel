package fcul.ppc.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import fcul.ppc.message.PopulationMessage;
import fcul.ppc.utils.Individual;

public class BestIndividualActor extends AbstractActor {

    private ActorRef crossOverActor;

    public BestIndividualActor(ActorRef crossOverActor) {
        this.crossOverActor = crossOverActor;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(PopulationMessage.class, message -> {
                    Individual best = bestOfPopulation(message.getPopulation());
                    System.out.println("Best at generation " + message.getGeneration() + " is " + best + " with "
                            + best.fitness + " for iteration " + message.getIteration());
                    PopulationMessage bestInvMessage = new PopulationMessage(
                            message.getPopulation(), message.getGeneration(), message.getIteration(),
                            message.getCreationTime(), best);
                    crossOverActor.tell(bestInvMessage, getSelf());
                })
                .build();
    }

    public static Individual bestOfPopulation(Individual[] population) {
        /*
         * Returns the best individual of the population.
         */
        Individual best = population[0];
        for (Individual other : population) {
            if (other.fitness > best.fitness) {
                best = other;
            }
        }
        return best;
    }

}
