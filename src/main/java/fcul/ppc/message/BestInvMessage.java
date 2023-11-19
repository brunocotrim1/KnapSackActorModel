package fcul.ppc.message;

import fcul.ppc.utils.Individual;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class BestInvMessage extends Message{
    private Individual bestIndividual;

    public BestInvMessage(Individual bestIndividual, Individual[] population, int i, int iteration, long creationTime) {
        this.setBestIndividual(bestIndividual);
        this.setPopulation(population);
        this.setGeneration(i);
        this.setIteration(iteration);
        setCreationTime(creationTime);
    }
}
