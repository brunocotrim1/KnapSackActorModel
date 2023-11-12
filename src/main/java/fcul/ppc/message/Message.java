package fcul.ppc.message;

import fcul.ppc.utils.Individual;
import lombok.Data;
@Data
public abstract class Message {
    private Individual[] population;
    private int generation;
}
