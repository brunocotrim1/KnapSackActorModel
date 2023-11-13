package fcul.ppc.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import fcul.ppc.message.PopulationMessage;
import fcul.ppc.message.StartMessage;
import fcul.ppc.utils.Individual;
import fcul.ppc.utils.IterationState;
import fcul.ppc.utils.Utils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

import static fcul.ppc.utils.Utils.POP_SIZE;

public class MainActor extends AbstractActor {

    private ActorRef fitnessActor;

    public MainActor(ActorRef fitnessActor) {
        this.fitnessActor = fitnessActor;
    }

    private Individual[] randomPopulation() {
        Individual[] population = new Individual[POP_SIZE];
        for (int i = 0; i < POP_SIZE; i++) {
            population[i] = Individual.createRandom(ThreadLocalRandom.current());
        }
        return population;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().match(StartMessage.class, message -> {
            System.out.println("Starting Iteration " + message.getIteration());


            PopulationMessage populationMessage = new PopulationMessage(randomPopulation(), 0, message.getIteration());
            fitnessActor.tell(populationMessage, getSelf());
        }).match(PopulationMessage.class, message -> {


            if (message.getGeneration() == Utils.N_GENERATIONS) {
                System.out.println("Finished iteration " + message.getIteration()
                        + " with best individual " + Utils.bestOfPopulation(message.getPopulation()));
                return;
            }
            System.out.println("Starting generation " + message.getGeneration()
                    + " of iteration " + message.getIteration());
            PopulationMessage populationMessage = new PopulationMessage(message.getPopulation(),
                    message.getGeneration() + 1, message.getIteration());
            fitnessActor.tell(populationMessage, getSelf());
        }).build();
    }


}
