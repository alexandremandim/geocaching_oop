package GeoCachingExceptions;

public class CacheNaoExisteException extends Exception
{
    public CacheNaoExisteException(){ super();}
    public CacheNaoExisteException(String msg){ super(msg); }
}
