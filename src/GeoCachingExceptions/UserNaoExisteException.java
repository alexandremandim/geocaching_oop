package GeoCachingExceptions;

public class UserNaoExisteException extends Exception
{
    public UserNaoExisteException(){ super();}
    public UserNaoExisteException(String msg){ super(msg); }
}
