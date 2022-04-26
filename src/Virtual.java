import java.lang.Class;
import java.io.*;

public class Virtual extends Cache implements Serializable
{
    private String pergunta;
    private String prova;
    
    /*Construtores*/
    public Virtual(){
        super();
        this.pergunta = new String();
        this.prova = new String();
    }
    
    public Virtual(double latitude, double longitude , String informacoesEsconderijo, String nome, int dificuldade, String criador, String pergunta, String prova){
        super(latitude,longitude,informacoesEsconderijo,nome,dificuldade,criador);
        this.pergunta = pergunta;
        this.prova = prova;
    }
    
    public Virtual(Virtual vc){
        super(vc);
        this.pergunta = vc.getPergunta();
        this.prova = vc.getProva();
    }
    
    public String getProva(){
        return this.prova;
    }
    
    public String getPergunta(){
        return this.pergunta;
    }
    
    public void setProva(String prova){
        this.prova = prova;
    }
    
    public void setPergunta(String pergunta){
        this.pergunta = pergunta;
    }
    
    /* outros metodos de instancia */
    public boolean verificaProva(String prova){
        return prova.equals(this.prova);
    }
    
    public boolean equals(Object umaVC){
        if(this==umaVC)
           return true;
        if((umaVC==null) || (this.getClass()!=umaVC.getClass()))
            return false;
        else{
            Virtual c =(Virtual) umaVC;
            return super.equals(umaVC) && this.pergunta.equals(c.getPergunta()) && this.prova.equals(c.getProva());
        }
    }
    
    public String toString(){
       StringBuilder sb = new StringBuilder();
       sb.append("-----------------------------------------");
       sb.append("\nCACHE VIRTUAL:\n\nA pergunta é: " + this.getPergunta() + "\n");
       sb.append(super.toString());
       sb.append("-----------------------------------------");
       return sb.toString();
    }
    
    public Virtual clone(){
        return new Virtual(this);
    }
}
