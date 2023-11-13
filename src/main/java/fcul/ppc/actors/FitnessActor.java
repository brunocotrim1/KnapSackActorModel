package fcul.ppc.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import fcul.ppc.message.PopulationMessage;
import fcul.ppc.parallelization.ParallelFW;
import fcul.ppc.utils.Individual;

import static fcul.ppc.utils.Utils.POP_SIZE;

public class FitnessActor extends AbstractActor {
    private ActorRef bestIndividualActor;

    public FitnessActor(ActorRef bestIndividualActor) {
        this.bestIndividualActor = bestIndividualActor;
    }

    private void ParallelMeasureFitness(Individual[] population) {
        ParallelFW.doInParallel(((startIndex, endIndex) -> {
            for (int i = startIndex; i < endIndex; i++) {
                population[i].measureFitness();
            }
        }), POP_SIZE);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(PopulationMessage.class, message -> {

                    ParallelMeasureFitness(message.getPopulation());
                    bestIndividualActor.tell(message, getSelf());

                })
                .build();
    }
}
