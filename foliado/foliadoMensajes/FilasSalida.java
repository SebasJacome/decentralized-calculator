package foliadoMensajes;
import java.util.concurrent.ConcurrentLinkedQueue;
import protocolosComunicacion.*;


public class FilasSalida {
    private ConcurrentLinkedQueue<MensajeOperacion> filaSuma = new ConcurrentLinkedQueue<>();
    private ConcurrentLinkedQueue<MensajeOperacion> filaResta = new ConcurrentLinkedQueue<>();
    private ConcurrentLinkedQueue<MensajeOperacion> filaMultiplicacion = new ConcurrentLinkedQueue<>();
    private ConcurrentLinkedQueue<MensajeOperacion> filaDivision = new ConcurrentLinkedQueue<>();

    public void addMessage(MensajeOperacion mensaje, int cola){
        switch(cola){
            case 1:
                filaSuma.add(mensaje);
            case 2:
                filaResta.add(mensaje);
            case 3:
                filaMultiplicacion.add(mensaje);
            case 4: 
                filaMultiplicacion.add(mensaje);
            default:
                System.err.println("Esa cola a la que intentas acceder no existe, intente de nuevo.");
        }
    }
    public MensajeOperacion knowMessage(int cola){
        switch(cola){
            case 1:
                return filaSuma.peek();
            case 2:
                return filaResta.peek();
            case 3:
                return filaMultiplicacion.peek();
            case 4: 
                return filaDivision.peek();
            default:
                System.err.println("Esa cola a la que intentas acceder no existe, intente de nuevo.");
                return new MensajeOperacion((short)5, "Error", 0, 0);
        }
    }
    public MensajeOperacion getMessage(int cola){
        switch(cola){
            case 1:
                return filaSuma.poll();
            case 2:
                return filaResta.poll();
            case 3:
                return filaMultiplicacion.poll();
            case 4: 
                return filaMultiplicacion.poll();
            default:
                System.err.println("Esa cola a la que intentas acceder no existe, intente de nuevo.");
                return new MensajeOperacion((short)5, "Error", 0, 0);
        }
    }

}
