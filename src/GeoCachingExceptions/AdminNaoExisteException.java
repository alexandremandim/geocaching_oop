package GeoCachingExceptions;

public class AdminNaoExisteException extends Exception
{
    public AdminNaoExisteException(){ super();}
    public AdminNaoExisteException(String msg){ super(msg); }
}
