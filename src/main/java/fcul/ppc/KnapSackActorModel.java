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
        // Create the actor that will receive the messages

        // ActorRef mutateActor = system.actorOf(Props.create(MutateActor.class), "mutateActor");
        ActorRef mutateRouter = system.actorOf(new BalancingPool(2)
                .props(Props.create(MutateActor.class)), "mutateRouter");
        ActorRef crossOverActor = system.actorOf(Props.create(CrossoverActor.class,mutateRouter), "crossOverActor");
        ActorRef bestIndividualActor = system.actorOf(Props.create(BestIndividualActor.class,crossOverActor), "bestIndividualActor");

        //ActorRef fitnessActor = system.actorOf(Props.create(FitnessActor.class,bestIndividualActor), "fitnessActor");
        ActorRef fitnessRouter = system.actorOf(new BalancingPool(2)
                .props(Props.create(FitnessActor.class,bestIndividualActor)), "fitnessRouter");
        ActorRef mainActor = system.actorOf(Props.create(MainActor.class,fitnessRouter), "mainActor");
        MutateActor.mainActor = mainActor;
        for (int i = 0; i <= max_iterations; i++) {
            mainActor.tell(new StartMessage(i), ActorRef.noSender());
        }



        // Block and wait for the termination of the actor system
        Future<Terminated> terminationFuture = system.whenTerminated();
        try {
            // Block and wait for the result synchronously
            Object response = Await.result(terminationFuture, Duration.Inf());
            System.out.println("Received response: " + response);
        } catch (Exception e) {
            System.err.println("Failed to receive a response: " + e.getMessage());
        } finally {
            // Terminate the actor system
            system.terminate();
        }
    }
}
