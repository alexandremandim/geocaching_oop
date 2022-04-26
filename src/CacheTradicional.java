import java.util.ArrayList;
import GeoCachingExceptions.*;
import java.lang.Class;
import java.io.*;

public class CacheTradicional extends Cache implements Serializable{
    
    private ArrayList<String> objetos;
    
    /* construtores */
    public CacheTradicional(){
        super();
        this.objetos = new ArrayList<String>();
    }
    public CacheTradicional(double latitude, double longitude , String informacoesEsconderijo, String nome, int dificuldade, String criador, ArrayList<String> objetos){
        super(latitude,longitude,informacoesEsconderijo,nome,dificuldade,criador);
        this.objetos = new ArrayList<String>();
        for(String obj: objetos){
            this.objetos.add(obj);
        }
    }
    public CacheTradicional(CacheTradicional ct){
        super(ct);
        this.objetos = ct.getObjetos();
    }
    
    /* gets e sets */
    public ArrayList<String> getObjetos(){
        ArrayList<String> new_objetos = new ArrayList<String>();
        for(String obj: this.objetos){
            new_objetos.add(obj);
        }
        return new_objetos;
    }
    
    public void setObjetos(ArrayList<String> obj){
        for(String s: obj){
            this.objetos.add(s);
        }
    }
    
    public void tirarObjetos(ArrayList<String> obj) throws ObjetoNaoExisteException{
        for(String o: obj){
            if(objetos.contains(o) ==false){
                throw new ObjetoNaoExisteException(o);
            }
        }
        for(String s: obj){
            objetos.remove(s);
        }
    }
    
    /* equals */
    public boolean equals(Object umaCT){
        if(this==umaCT)
           return true;
        if((umaCT==null) || (this.getClass()!=umaCT.getClass()))
            return false;
        else{
            CacheTradicional c = (CacheTradicional) umaCT;
            if(this.objetos.size() != c.getObjetos().size()) return false;
            for(String obj: c.getObjetos()){
                if(!(this.objetos.contains(obj))) return false;
            }
            return super.equals(umaCT);
        }
    }
    /* toString */
    public String toString(){
       StringBuilder sb = new StringBuilder();
       sb.append("-----------------------------------------");
       sb.append("\nCACHE TRADICIONAL:\n\nExistem: " + this.getObjetos().size() + " objetos:\n");
       for(String obj: this.getObjetos()){
           sb.append("  -"+obj + "; \n");
       }
       sb.append(super.toString());
       sb.append("-----------------------------------------");
       return sb.toString();
    }
    /* clone */
    public CacheTradicional clone(){
        return new CacheTradicional(this);
    }
}
