package fcul.ppc.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import fcul.ppc.message.PopulationMessage;
import fcul.ppc.message.StartMessage;
import fcul.ppc.utils.Individual;
import fcul.ppc.utils.Utils;

import java.util.concurrent.ThreadLocalRandom;

import static fcul.ppc.utils.Utils.POP_SIZE;
import static fcul.ppc.utils.Utils.MAX_ITERATIONS;

public class MainActor extends AbstractActor {

    private ActorRef fitnessActor;
    private static int iteration = 0;

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

            PopulationMessage populationMessage = new PopulationMessage(randomPopulation(), 0,
                    message.getIteration(), System.nanoTime(),null);
            fitnessActor.tell(populationMessage, getSelf());

        }).match(PopulationMessage.class, message -> {

            if (message.getGeneration() == Utils.N_GENERATIONS) {
                iteration++;
                message.setFinishTime(System.nanoTime());
                double timeTaken = (message.getFinishTime() - message.getCreationTime()) / 1E9;
                System.out.println("Finished iteration " + message.getIteration() + " with best individual " +
                        message.getBestIndividual() + "in " + timeTaken + " seconds");
                if (iteration == MAX_ITERATIONS) {
                    System.out.println("Finished all iterations");
                    getContext().getSystem().terminate();
                }
                return;
            }

            System.out.println("Starting generation " + message.getGeneration() + " of iteration " + message.getIteration());
            PopulationMessage populationMessage = new PopulationMessage(message.getPopulation(),
                    message.getGeneration() + 1, message.getIteration(), message.getCreationTime(),null);
            fitnessActor.tell(populationMessage, getSelf());

        }).build();
    }


}
