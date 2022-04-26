import java.lang.Class;
import java.io.*;

public class CacheMisterio extends Cache implements Serializable{
    
    private String puzzle;
    private String resposta;
    
    public CacheMisterio(){
        super();
        this.puzzle = new String();
        this.resposta = "";
    }
    
    public CacheMisterio(double latitude, double longitude , String informacoesEsconderijo, String nome, int dificuldade, String criador, String puzzle, String resposta){
        super(latitude,longitude,informacoesEsconderijo,nome,dificuldade,criador);
        this.puzzle = puzzle;
        this.resposta = resposta;
    }
    
    public CacheMisterio(CacheMisterio cm){
        super(cm);
        this.puzzle = cm.getPuzzle();
        this.resposta = cm.getResposta();
    }
    
    public String getPuzzle(){
        return this.puzzle;
    }
    
    public String getResposta(){
        return this.resposta;
    }
    
    public void setPuzzle(String puzzle){
        this.puzzle = puzzle;
    }
    
    public boolean resolverMisterio(String resposta){
        return this.resposta.equals(resposta);
    }
    
    public boolean equals(Object umaCM){
        if(this==umaCM)
           return true;
        if((umaCM==null) || (this.getClass()!=umaCM.getClass()))
            return false;
        else{
            CacheMisterio c =(CacheMisterio) umaCM;
            return super.equals(umaCM) && this.puzzle.equals(c.getPuzzle());
        }
    }
    
    public String toString(){
       StringBuilder sb = new StringBuilder();
       sb.append("-----------------------------------------");
       sb.append("\nCACHE MISTÉRIO:\n\nNome: "+super.getNome()+ "\nO puzzle é: " + this.getPuzzle());
       sb.append("\nDificuldade: " + super.getDificuldade());
       sb.append("\nCriador: " + super.getCriador() + "\n");
       sb.append("-----------------------------------------");
       return sb.toString();
    }
    
    public CacheMisterio clone(){
        return new CacheMisterio(this);
    }
}
