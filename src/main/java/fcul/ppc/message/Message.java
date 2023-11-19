package fcul.ppc.message;

import fcul.ppc.utils.Individual;
import lombok.Data;

import java.time.Instant;

@Data
public abstract class Message {
    private Individual[] population;
    private int generation;
    private int iteration;
    private long creationTime;
    private long finishTime;
}
