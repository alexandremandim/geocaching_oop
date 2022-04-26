import java.util.TreeMap;
import java.util.TreeSet;
import GeoCachingExceptions.*;
import java.io.*;

public class Administracao implements Serializable
{
    private TreeMap<String, Admin> grupoAdmins;
    private TreeSet<Cache> cachesReportadas;
    /* construtores */
    public Administracao()
    {
        this.grupoAdmins = new TreeMap<String, Admin>();
        this.cachesReportadas = new TreeSet<Cache>();
        this.grupoAdmins.put("admin",new Admin("admin", "pass"));
    }
    public Administracao(TreeMap<String,Admin> grupoAdmins, TreeSet<Cache> cachesReportadas){
        this.grupoAdmins = new TreeMap<String, Admin>();
        for(Admin a: grupoAdmins.values()){
            this.grupoAdmins.put(a.getEmail(), a.clone());
        }
        this.cachesReportadas = new TreeSet<Cache>();
        for(Cache c: cachesReportadas){
            this.cachesReportadas.add(c.clone());
        }
    }
    public Administracao(Administracao a){
        this.grupoAdmins = a.getgrupoAdmins();
        this.cachesReportadas = a.getcachesReportadas();
    }
    
    /* gets/sets */
    public TreeMap<String, Admin> getgrupoAdmins(){
        TreeMap<String, Admin> novo =  new TreeMap<String, Admin>();
        for(Admin a : this.grupoAdmins.values()){
            novo.put(a.getEmail(),a.clone());
        }
        return novo;
    }
    public TreeSet<Cache> getcachesReportadas(){
        TreeSet<Cache> novo = new TreeSet<Cache>();
        for(Cache c: this.cachesReportadas){
            novo.add(c.clone());
        }
        return novo;
    }
    public int size(){
        return grupoAdmins.size();
    }
    public int nrCachesReportadas(){
        return this.cachesReportadas.size();
    }
    public void reportarCache(Cache cache) throws CacheJaExisteException{
        for(Cache c: this.cachesReportadas){
            if(c.getCoordenadas().equals(cache.getCoordenadas())) throw new CacheJaExisteException(cache.getCoordenadas().toString());
        }
        this.cachesReportadas.add(cache.clone());
    }
    public void addAdmin(String email, String password) throws AdminJaExisteException{
        if(grupoAdmins.containsKey(email) == true){
            throw new AdminJaExisteException();
        }
        grupoAdmins.put(email, new Admin(email,password));
    }
    public void removeAdmin(String email) throws UmAdminException,AdminNaoExisteException{
        if(grupoAdmins.size() <= 1){
            throw new UmAdminException("Administracao tem que ter no minimo 1 admin.");
        }
        if(grupoAdmins.remove(email) == null){
            throw new AdminNaoExisteException(email);
        }
    }
    public boolean existeAdmin(String email, String pass){
        Admin a = grupoAdmins.get(email);
        if(a == null){  return false;}
        if(a.getPassword().equals(pass)){
           return true;
        }
        return false;
    }
    public boolean contains(String email){
        return this.grupoAdmins.containsKey(email);
    }
    public void cacheAbusadoraResolvida(Cache cache) throws CacheNaoExisteException{
        if(cache == null){
            throw new CacheNaoExisteException();
        }
        for(Cache c: cachesReportadas){
            if(c.getLatitude() == cache.getLatitude() && c.getLongitude() == cache.getLongitude()){
                if(cachesReportadas.remove(c) == false){
                    throw new CacheNaoExisteException();
                }
            }
        }
    }
    
    /* clone */
    public Administracao clone(){
        return new Administracao(this);
    }
    /* equals */
    public boolean equals(Object umaAdm){
        if(this==umaAdm)
            return true;
        if((umaAdm==null) || (this.getClass()!=umaAdm.getClass()))
            return false;
        else{
            Administracao a =(Administracao) umaAdm;
            if(a.getgrupoAdmins().size() != this.grupoAdmins.size() || a.getcachesReportadas().size() != this.cachesReportadas.size()) return false;
            for(Admin ad : a.getgrupoAdmins().values()){
                if(this.grupoAdmins.get(ad.getEmail())== null) return false;
            }
            for(Cache c: a.getcachesReportadas()){
                if(this.cachesReportadas.contains(c) == false) return false;
            }
            
            return true;
        }
    }
    /* toString */
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append ("Administracao com " + this.grupoAdmins.size() + " administradores.");
        return sb.toString();
    }
}
