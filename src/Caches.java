import java.util.TreeMap;
import java.awt.geom.Point2D.Double;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import GeoCachingExceptions.*;
import java.io.*;

public class Caches implements Serializable
{
    private TreeMap<String,Cache> todasCaches, cachesEliminadas;

    /* construtores */
    
    public Caches()
    {
        todasCaches = new TreeMap<String,Cache>();
        cachesEliminadas = new TreeMap<String,Cache>();
    }
    public Caches(Caches cs){
        this.todasCaches = cs.gettodasCaches();
        this.cachesEliminadas = cs.getcachesEliminadas();
    }
    
    /* gets */
    public TreeMap<String ,Cache> gettodasCaches(){
        
        TreeMap<String,Cache> res = new TreeMap<String,Cache> ();
        for(Cache c: this.todasCaches.values()){
            res.put(c.getNome(),c.clone());
        }
        return res;
    }
    public TreeMap<String,Cache> getcachesEliminadas(){
        TreeMap<String,Cache> res = new TreeMap<String,Cache> ();
        for(Cache c: this.cachesEliminadas.values()){
            res.put(c.getNome(),c.clone());
        }
        return res;
    }
    public ArrayList<String> getTodasCacheString(){
        ArrayList<String> novo = new ArrayList<String>();
        for(Cache c: todasCaches.values()){
            novo.add(c.toString());
        }
        return novo;
    }
    public void settodasCaches(TreeMap<String,Cache> todasCaches){
        for(Cache c: todasCaches.values()){
            this.todasCaches.put(c.getNome(),c.clone());
        }
    }
    
    /* outros metodos */
    /* Adiciona uma cache */
    public Cache addCache(Cache c) throws CacheJaExisteException{
        Point2D novo = new Point2D.Double(c.getLatitude(),c.getLongitude());
        for(Cache cache: todasCaches.values()){
            if(cache.getCoordenadas().equals(novo)){
                throw new CacheJaExisteException();
            }
        }
        if(todasCaches.containsKey(c.getNome()) == true){
            throw new CacheJaExisteException();
        }
        this.todasCaches.put(c.getNome(), c.clone());
        return this.todasCaches.get(c.getNome());
    }
    /* Remove uma cache */
    public void removerCache(Cache c) throws CacheNaoExisteException{/* retira a cache das caches e mete-a nas eliminadas */
        
        if(todasCaches.containsKey(c.getNome()) == false){
            throw new CacheNaoExisteException();
        }
        this.cachesEliminadas.put(c.getNome(), todasCaches.get(c.getNome())); 
        this.todasCaches.remove(c.getNome());
    }
    public void removerCacheAbusadora(Cache c) throws CacheNaoExisteException{
        
        if(todasCaches.containsKey(c.getNome()) == false){
            throw new CacheNaoExisteException();
        }
        this.todasCaches.remove(c.getNome());
    }
    /* Dado uma String retorna uma copia da cache com essas coords */
    public Cache getCache(String nomeCache){
        return this.todasCaches.get(nomeCache);
    }
    public void addUserLivroRegisto(String nomeCache, String email) throws UserJaExisteException, CacheNaoExisteException{
        Cache c = todasCaches.get(nomeCache);
        if(c==null){
            throw new CacheNaoExisteException();
        }
        c.addUserLivroRegisto(email);
    }
    public int size(){
        return todasCaches.size();
    }
    /*equals */
    public boolean equals(Object umasCaches){
        if(this==umasCaches)
            return true;
        if((umasCaches==null) || (this.getClass()!=umasCaches.getClass()))
            return false;
        else{
            Caches cs =(Caches) umasCaches;
            if(this.todasCaches.size()!=cs.gettodasCaches().size()) return false;
            if(this.cachesEliminadas.size()!=cs.getcachesEliminadas().size()) return false;
            for(Cache tc: cs.gettodasCaches().values()){
                if(this.todasCaches.containsValue(tc) ==false)return false;
            }
            for(Cache ce: cs.getcachesEliminadas().values()){
                if(this.cachesEliminadas.containsValue(ce) ==false)return false;
            }
        }
        return true;
    }
    /* clone */
    public Caches clone(){
        return new Caches(this);
    }
    /* toString */
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Existem " + this.todasCaches.size() + " caches que podem ser descobertas.\n");
        
        return sb.toString();
    }
}
