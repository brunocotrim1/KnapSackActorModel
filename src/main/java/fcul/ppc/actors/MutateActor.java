package fcul.ppc.actors;

import akka.Main;
import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import fcul.ppc.message.PopulationMessage;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static fcul.ppc.utils.Utils.POP_SIZE;
import static fcul.ppc.utils.Utils.PROB_MUTATION;

public class MutateActor extends AbstractActor {
    public static ActorRef mainActor;

    @Override
    public AbstractActor.Receive createReceive() {
        return receiveBuilder().match(PopulationMessage.class, message -> {
            // Step4 - Mutate
            Random r = ThreadLocalRandom.current();
            for (int i = 1; i < POP_SIZE; i++) {
                if (r.nextDouble() < PROB_MUTATION) {
                    message.getPopulation()[i].mutate(r);
                }
            }
            mainActor.tell(message, getSelf());

        }).build();
    }
}
