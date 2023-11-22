package fcul.ppc.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import fcul.ppc.message.PopulationMessage;
import fcul.ppc.utils.Individual;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static fcul.ppc.utils.Utils.POP_SIZE;
import static fcul.ppc.utils.Utils.TOURNAMENT_SIZE;

public class CrossoverActor extends AbstractActor {

    private ActorRef mutateActor;

    public CrossoverActor(ActorRef mutateActor) {
        this.mutateActor = mutateActor;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().match(PopulationMessage.class, message -> {
            Individual[] newPopulation = new Individual[POP_SIZE];
            newPopulation[0] = message.getBestIndividual(); // The best individual remains
            for (int i = 1; i < POP_SIZE; i++) {
                // We select two parents, using a tournament.
                Individual parent1 = tournament(TOURNAMENT_SIZE, ThreadLocalRandom.current(), message.getPopulation());
                Individual parent2 = tournament(TOURNAMENT_SIZE, ThreadLocalRandom.current(), message.getPopulation());

                newPopulation[i] = parent1.crossoverWith(parent2, ThreadLocalRandom.current());
            }
            PopulationMessage crossoverMessage = new PopulationMessage(newPopulation, message.getGeneration()
                    , message.getIteration(), message.getCreationTime(), message.getBestIndividual());
            mutateActor.tell(crossoverMessage, getSelf());
        }).build();
    }

    public static Individual tournament(int tournamentSize, Random r, Individual[] population) {
        Individual best = population[r.nextInt(POP_SIZE)];
        for (int i = 0; i < tournamentSize; i++) {
            Individual other = population[r.nextInt(POP_SIZE)];
            if (other.fitness > best.fitness) {
                best = other;
            }
        }
        return best;
    }
}
