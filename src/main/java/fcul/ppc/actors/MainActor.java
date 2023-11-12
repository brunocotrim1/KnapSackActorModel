package fcul.ppc.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import fcul.ppc.message.PopulationMessage;
import fcul.ppc.message.StartMessage;
import fcul.ppc.utils.Individual;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

import static fcul.ppc.utils.Utils.POP_SIZE;

public class MainActor extends AbstractActor {


    private Individual[] population = new Individual[POP_SIZE];
    private AtomicInteger generation = new AtomicInteger(0);
    private ActorRef fitnessActor;

    public MainActor(ActorRef fitnessActor) {
        for (int i = 0; i < POP_SIZE; i++) {
            population[i] = Individual.createRandom(ThreadLocalRandom.current());
        }
        this.fitnessActor = fitnessActor;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(StartMessage.class, message -> {
                    System.out.println("Starting generation " + generation.get());
                    PopulationMessage populationMessage = new PopulationMessage(population, generation.get());
                    fitnessActor.tell(populationMessage, getSelf());
                    generation.incrementAndGet();
                }).match(PopulationMessage.class, message -> {
                    System.out.println("Starting generation " + generation.get());
                    this.population = message.getPopulation();
                    PopulationMessage populationMessage = new PopulationMessage(population, generation.get());
                    fitnessActor.tell(populationMessage, getSelf());
                    generation.incrementAndGet();

                })
                .build();
    }


}
