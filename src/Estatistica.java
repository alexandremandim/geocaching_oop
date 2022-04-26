import java.util.GregorianCalendar;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.io.*;

public class Estatistica implements Serializable{
    private GregorianCalendar data;
    private ArrayList<Registo> registoMes;
    
    public Estatistica(){
        this.data = new GregorianCalendar();
        this.registoMes = new ArrayList<Registo>();
    }
    
    public Estatistica(int ano, int mes){
        this.data = new GregorianCalendar(ano,mes,0);
        this.registoMes = new ArrayList<Registo>();
    }
    
    public Estatistica(Estatistica e){
       this.registoMes = e.getRegistosMes();
       this.data = e.getData();
    }
    
    /*gets e sets*/
    public GregorianCalendar getData(){
        return (new GregorianCalendar(data.get(Calendar.YEAR),data.get(Calendar.MONTH),data.get(Calendar.DAY_OF_MONTH)));
    }
    public ArrayList<Registo> getRegistosMes(){
        ArrayList<Registo> new_registoMes = new ArrayList <Registo>(this.registoMes.size());
        
        for(Registo r: registoMes){
            new_registoMes.add(r.clone());   
        }
        return new_registoMes;
    }
    
    /* outros métodos de instância*/
    public void removerRegisto(double latitude, double longitude) {
        
        Iterator it = registoMes.iterator();
        while(it.hasNext()){
            Registo r = (Registo)it.next();
            if(r.getCache().getLatitude() == latitude && r.getCache().getLongitude() == longitude){
                it.remove();
            }
        }
    }
    
    /* adiciona um registo */
    public void add(Registo r){
        registoMes.add(r);
    }
    /* remove um registo do tipo Encontrado */
    public void removerRegistoEncontrado(Registo r){
        registoMes.remove(r);
    }
    
    public int qtsCriadasTotal(){
        int total = 0;
        for(Registo r: registoMes){
            if(r.getCriado() == true){
                total++;
            }
        }
        return total;
    }
    
    public int qtsCriadasTipo(String tipo) throws ClassNotFoundException{
        Class <?> c = Class.forName(tipo);
        int total = 0;
        Cache aux;
        for(Registo r : registoMes){
            aux = r.getCache();
            if(c.isInstance(aux) && r.getCriado() == true){
                 total++;
            }
        }
         return total;
    }
    
     public int qtsEncontradasTotal(){
        int total = 0;
        for(Registo r: registoMes){
            if(r.getEncontrado() == true){
                total++;
            }
        }
        return total;
    }
    
    public int qtsEncontradaTipo(String tipo) throws ClassNotFoundException{
        Class <?> c = Class.forName(tipo);
        int total = 0;
         Cache aux;
         for(Registo r : registoMes){
             aux = r.getCache();
             if(c.isInstance(aux) && r.getEncontrado() == true){
                 total++;
            }
         }
         return total;
    }
    
    public double dificuldadeMediaEncontradaTipo(String tipo)throws ClassNotFoundException{
        double resultado=0;
        int total = this.qtsEncontradaTipo(tipo);
        if(total==0) return 0;
        
        for(Registo r: this.registoMes){
            resultado = r.getCache().getDificuldade();
        }
        
        resultado = resultado/total;
        return resultado;
    }
    
     /* equals */
    public boolean equals(Object umaEstatistica){
        if(this==umaEstatistica)
            return true;
        if((umaEstatistica==null) || (this.getClass()!=umaEstatistica.getClass()))
            return false;
        Estatistica e = (Estatistica) umaEstatistica;
        
        if(e.getRegistosMes().size() != this.registoMes.size()) return false;
        
        for(Registo c : e.getRegistosMes()){
            if(registoMes.contains(c) == false) return false;
        }
        return (this.data == e.getData());
    }
    /* toString */
    public String toString(){
        StringBuilder sb = new StringBuilder();

        sb.append ("Em " + data.get(Calendar.YEAR) + "/" + data.get(Calendar.MONTH)+1 +  "existem " + registoMes.size() +" registos.");
        
        return sb.toString();
    }
    /* clone */
    public Estatistica clone(){
        return new Estatistica(this);
    }
    
}
