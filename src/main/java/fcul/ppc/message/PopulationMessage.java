package fcul.ppc.message;

import fcul.ppc.utils.Individual;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class PopulationMessage extends Message{
    public PopulationMessage(Individual[] population, int i) {
        this.setPopulation(population);
        this.setGeneration(i);
    }
}
