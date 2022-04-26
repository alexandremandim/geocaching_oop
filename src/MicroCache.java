import java.lang.Class;
import java.io.*;

public class MicroCache extends Cache implements Serializable
{
    
    public MicroCache(){
        super();
    }
    
     public MicroCache(double latitude, double longitude , String informacoesEsconderijo, String nome, int dificuldade, String criador){
        super(latitude,longitude,informacoesEsconderijo,nome,dificuldade,criador);
    }
    
    public MicroCache(MicroCache mc){
        super(mc);
    }
    
    public boolean equals(Object umaMC){
         if(this==umaMC)
            return true;
        if((umaMC==null) || (this.getClass()!=umaMC.getClass()))
            return false;
        else{
            MicroCache c =(MicroCache) umaMC;
            return super.equals(umaMC);
        }
    }
    
    public String toString(){
       StringBuilder sb = new StringBuilder();
       sb.append("-----------------------------------------\nMICRO-CACHE\n");
       sb.append(super.toString());
       sb.append("-----------------------------------------");
       return sb.toString();
    }
    
    public MicroCache clone(){
        return new MicroCache(this);
    }
}
