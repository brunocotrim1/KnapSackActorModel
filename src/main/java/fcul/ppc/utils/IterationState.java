package fcul.ppc.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IterationState {
    private int generation;
    private Individual[] population;
}
