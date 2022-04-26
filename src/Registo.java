import java.util.GregorianCalendar;
import java.util.Calendar;
import java.lang.Class;
import java.io.*;

public class Registo implements Comparable, Serializable
{
   private static int contador;
   
   private int idRegisto;
   private GregorianCalendar dataAtividade;
   private Cache cache;
   private Evento evento;
   private boolean encontrado, criado;
   private int condicoesMeteorologicas;
   
   public static int getContador(){return contador;}
   public static void setContador(int cont){contador = cont;}
   /* Construtores */
    public Registo(GregorianCalendar dataAtividade, Cache cache, boolean encontrado, boolean criado, int condicoesMeteorologicas){
        idRegisto = ++contador;
        this.dataAtividade = new GregorianCalendar(dataAtividade.get(Calendar.YEAR),dataAtividade.get(Calendar.MONTH),dataAtividade.get(Calendar.DAY_OF_MONTH),dataAtividade.get(Calendar.HOUR_OF_DAY),dataAtividade.get(Calendar.MINUTE),dataAtividade.get(Calendar.SECOND));
        this.cache = cache;
        this.evento = null;
        this.encontrado = encontrado;
        this.criado = criado;
        this.condicoesMeteorologicas = condicoesMeteorologicas;
    }
    public Registo(Cache cache, boolean encontrado, boolean criado, int condicoesMeteorologicas){
        idRegisto = ++contador;
        this.dataAtividade = new GregorianCalendar();
        this.cache = cache;
        this.evento = null;
        this.encontrado = encontrado;
        this.criado = criado;
        this.condicoesMeteorologicas = condicoesMeteorologicas;
    }
    public Registo(Evento evento, int condicoesMeteorologicas){
        idRegisto = ++contador;
        this.dataAtividade = new GregorianCalendar();
        this.encontrado =  true;
        this.evento = evento;
        this.condicoesMeteorologicas = condicoesMeteorologicas;
    }
    public Registo(Registo r){
        idRegisto = r.getIdRegisto();
        this.dataAtividade = r.getDataAtividade();
        this.cache = r.getCache();
        this.encontrado = r.getEncontrado();
        this.criado = r.getCriado();
        this.condicoesMeteorologicas = r.getcondicoesMeteorologicas();
        this.evento = r.getEvento();
    }
    
    /* Gets/Sets */
    public Evento getEvento(){
        return this.evento;
    }
    public GregorianCalendar getDataAtividade() {
        return (new GregorianCalendar(dataAtividade.get(Calendar.YEAR),dataAtividade.get(Calendar.MONTH),dataAtividade.get(Calendar.DAY_OF_MONTH),dataAtividade.get(Calendar.HOUR_OF_DAY),dataAtividade.get(Calendar.MINUTE),dataAtividade.get(Calendar.SECOND)));
    }
    public void setDataAtividade(GregorianCalendar dataAtividade) {
        this.dataAtividade = (new GregorianCalendar(dataAtividade.get(Calendar.YEAR),dataAtividade.get(Calendar.MONTH),dataAtividade.get(Calendar.DAY_OF_MONTH),dataAtividade.get(Calendar.HOUR_OF_DAY),dataAtividade.get(Calendar.MINUTE),dataAtividade.get(Calendar.SECOND)));
    }
    public Cache getCache() {
        return cache;
    }
    public void setCache(Cache cache) {
        this.cache = cache.clone();
    }
    public boolean getEncontrado() {
        return this.encontrado;
    }
    public void setEncontrado(boolean encontrado) {
        this.encontrado = encontrado;
    }
    public boolean getCriado() {
        return this.criado;
    }
    public void setCriado(boolean criado) {
        this.criado = criado;
    }
    public int getcondicoesMeteorologicas() {
        return this.condicoesMeteorologicas;
    }
    public void setcondicoesMeteorologicas(int condicoesMeteorologicas) {
        this.condicoesMeteorologicas = condicoesMeteorologicas;
    }
    public int getIdRegisto(){
        return (this.idRegisto);
    }
    /*toString mais reduzido*/   
    public String toStringReduzido(){
        String res = new String();
        if(encontrado == true)  res = "\nID Registo: "+ this.idRegisto + "\nEncontrado\n"+ "\nData: " + dataAtividade.get(Calendar.YEAR) + "/" + (dataAtividade.get(Calendar.MONTH)+1) + "/" + dataAtividade.get(Calendar.DAY_OF_MONTH) + "\nNome da Cache:" + cache.getNome() +"\nTipo de Cache: " + cache.getClass().getSimpleName();
        if(criado == true)  res = "\nID Registo: "+ this.idRegisto + "\nCriado\n"+ "\nData: " + dataAtividade.get(Calendar.YEAR) + "/" + (dataAtividade.get(Calendar.MONTH)+1) + "/" + dataAtividade.get(Calendar.DAY_OF_MONTH) + "\nNome da Cache:" + cache.getNome() +"\nTipo de Cache: " + cache.getClass().getSimpleName();
        
        return res;
    }
    
    /* toString */
    public String toString(){
        String res = new String();
        if(this.evento != null) res = ("\nParticipou no evento " + this.evento.getNome() + " .");
        else{
            if(encontrado == true)  res = "\nID Registo: "+ this.idRegisto + "\nData: " + dataAtividade.get(Calendar.YEAR) + "/" + (dataAtividade.get(Calendar.MONTH)+1) + "/" + dataAtividade.get(Calendar.DAY_OF_MONTH) + " foi encontrada a seguinte cache:\n" + cache.toString() ;
            if(criado == true)  res = "\nID Registo: "+ this.idRegisto + "\nData: " + dataAtividade.get(Calendar.YEAR) + "/" + (dataAtividade.get(Calendar.MONTH)+1) + "/" + dataAtividade.get(Calendar.DAY_OF_MONTH) + " foi criada a seguinte cache:\n" + cache.toString() ;
        }
        return res;
    }
    /* clone */
    public Registo clone(){
        return new Registo(this);
    }
    /* Equals */
    public boolean equals(Object umRegisto){
        if(this == umRegisto){
            return true;
        }
        
        if((umRegisto == null) || (this.getClass() != umRegisto.getClass())){
            return false;
        }
        
        Registo reg = (Registo) umRegisto;
        
        return(this.evento == reg.getEvento() && (this.dataAtividade.equals(reg.dataAtividade)) && 
        (this.cache.equals(reg.cache)) && (this.encontrado == reg.encontrado) && 
        (this.criado == reg.criado) && (this.condicoesMeteorologicas == reg.condicoesMeteorologicas) && this.idRegisto == reg.getIdRegisto());
    }
    /* compareTo */
    public int compareTo(Object obj){
        Registo r = (Registo)obj;
        
        return(dataAtividade.compareTo(r.getDataAtividade()));
        
    }
}
