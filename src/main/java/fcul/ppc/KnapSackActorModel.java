package fcul.ppc;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.Terminated;
import akka.routing.BalancingPool;
import fcul.ppc.actors.*;
import fcul.ppc.message.StartMessage;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import static fcul.ppc.utils.Utils.max_iterations;

public class KnapSackActorModel {
    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("KnapSackActorModel");
        ActorRef mutateActor = system.actorOf(Props.create(MutateActor.class), "mutateActor");
        ActorRef crossOverRouter = system.actorOf(new BalancingPool(2)
                .props(Props.create(CrossoverActor.class,mutateActor)), "crossOverRouter");
        ActorRef bestIndividualActor = system.actorOf(Props.create(BestIndividualActor.class,crossOverRouter), "bestIndividualActor");
        ActorRef fitnessRouter = system.actorOf(new BalancingPool(2)
                .props(Props.create(FitnessActor.class,bestIndividualActor)), "fitnessRouter");
        ActorRef mainActor = system.actorOf(Props.create(MainActor.class,fitnessRouter), "mainActor");
        MutateActor.mainActor = mainActor;
        long startTime = System.nanoTime();
        for (int i = 0; i <= max_iterations; i++) {
            mainActor.tell(new StartMessage(i), ActorRef.noSender());
        }

        Future<Terminated> terminationFuture = system.whenTerminated();
        try {
            Object response = Await.result(terminationFuture, Duration.Inf());
            long endTime = System.nanoTime();
            System.out.println("Received response: " + response);
            System.out.println("Time taken: " + (endTime - startTime) / 1E9 + " seconds");
        } catch (Exception e) {
            System.err.println("Failed to receive a response: " + e.getMessage());
        } finally {
            system.terminate();
        }
    }
}
