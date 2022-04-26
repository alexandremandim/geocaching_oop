package GeoCachingExceptions;

public class UserNaoRegistadoException extends Exception
{
    public UserNaoRegistadoException(){ super();}
    public UserNaoRegistadoException(String msg){ super(msg); }
}
