package fcul.ppc.message;

public class StartMessage extends Message{
    public StartMessage(int iteration){
        this.setIteration(iteration);
    }
}
