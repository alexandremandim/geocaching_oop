package GeoCachingExceptions;

public class EventoNaoExisteException extends Exception
{
    public EventoNaoExisteException(){ super();}
    public EventoNaoExisteException(String msg){ super(msg); }
}
