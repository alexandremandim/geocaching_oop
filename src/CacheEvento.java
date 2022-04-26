import java.util.GregorianCalendar;
import java.util.Calendar;
import java.lang.Class;
import java.io.*;

public class CacheEvento extends Cache implements Serializable{
    
    private GregorianCalendar data;
    
    public CacheEvento(){
        super();
        this.data = new GregorianCalendar(0,0,0);
    }
    
    public CacheEvento(double latitude, double longitude, String informacoesEsconderijo, String nome, int dificuldade, String criador, GregorianCalendar umaData){
        super(latitude,longitude,informacoesEsconderijo,nome,dificuldade,criador);
        this.data = new GregorianCalendar(umaData.get(Calendar.YEAR), umaData.get(Calendar.MONTH), umaData.get(Calendar.DAY_OF_MONTH));
    }
    
    public CacheEvento(CacheEvento ce){
        super(ce);
        this.data = ce.getData();
    }
    
    public GregorianCalendar getData(){
        return (new GregorianCalendar(data.get(Calendar.YEAR),data.get(Calendar.MONTH),data.get(Calendar.DAY_OF_MONTH)));
    }
    public void setData(GregorianCalendar data) {
        this.data = (new GregorianCalendar(data.get(Calendar.YEAR),data.get(Calendar.MONTH),data.get(Calendar.DAY_OF_MONTH)));
    }
    
    public boolean equals(Object umaCE){
        if(this==umaCE)
           return true;
        if((umaCE==null) || (this.getClass()!=umaCE.getClass()))
            return false;
        else{
            CacheEvento c = (CacheEvento) umaCE;
            
            return (super.equals(umaCE) && (this.data.equals(c.getData())));
        }
    }
    
    public String toString(){
       StringBuilder sb = new StringBuilder();
       sb.append("-----------------------------------------");
       sb.append("\nCACHE EVENTO:\n"+"\nData: " +data.get(Calendar.YEAR) + "/" +  (data.get(Calendar.MONTH)+1) + "/" + data.get(Calendar.DAY_OF_MONTH));
       sb.append(super.toString());
       sb.append("Participaram nesta evento: " +super.getLivroRegisto().size()+" utilizadores.\n");
       sb.append("-----------------------------------------");
       return sb.toString();
    }
    
    public CacheEvento clone(){
        return new CacheEvento(this);
    }
}
