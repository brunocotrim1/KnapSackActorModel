package fcul.ppc.message;

import fcul.ppc.utils.Individual;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class PopulationMessage extends Message{
    private Individual bestIndividual;

    public PopulationMessage(Individual[] population, int i, int iteration, long creationTime,Individual bestIndividual) {
        this.setBestIndividual(bestIndividual);
        this.setPopulation(population);
        this.setGeneration(i);
        this.setIteration(iteration);
        setCreationTime(creationTime);
    }
}
