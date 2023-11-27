package foliadoMensajes;
import java.util.concurrent.ConcurrentLinkedQueue;
import protocolosComunicacion.MensajeOperacion;

public class FilaEntrada {
    private ConcurrentLinkedQueue<MensajeOperacion> recibido = new ConcurrentLinkedQueue<>();

    public void addMessage(MensajeOperacion mensaje){
        recibido.add(mensaje);
    }

    public MensajeOperacion knowMessage(){
        return recibido.peek();
    }

    public MensajeOperacion getMessage(){
        return recibido.poll();
    }
}
