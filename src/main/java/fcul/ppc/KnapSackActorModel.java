package fcul.ppc;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.Terminated;
import fcul.ppc.actors.*;
import fcul.ppc.message.StartMessage;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

public class KnapSackActorModel {
    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("KnapSackActorModel");
        // Create the actor that will receive the messages

        ActorRef mutateActor = system.actorOf(Props.create(MutateActor.class), "mutateActor");
        ActorRef crossOverActor = system.actorOf(Props.create(CrossoverActor.class,mutateActor), "crossOverActor");
        ActorRef bestIndividualActor = system.actorOf(Props.create(BestIndividualActor.class,crossOverActor), "bestIndividualActor");
        ActorRef fitnessActor = system.actorOf(Props.create(FitnessActor.class,bestIndividualActor), "fitnessActor");
        ActorRef mainActor = system.actorOf(Props.create(MainActor.class,fitnessActor), "mainActor");
        MutateActor.mainActor = mainActor;
        mainActor.tell(new StartMessage(0), ActorRef.noSender());
        mainActor.tell(new StartMessage(1), ActorRef.noSender());

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
