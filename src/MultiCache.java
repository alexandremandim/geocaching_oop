import java.util.ArrayList;
import java.awt.geom.Point2D;
import GeoCachingExceptions.*;
import java.lang.Class;
import java.io.*;

public class MultiCache extends Cache implements Serializable
{
    private ArrayList<Point2D.Double> todasCaches;
    
    /* construtores */
    public MultiCache(){
        super();
        this.todasCaches = new ArrayList<Point2D.Double>();
    }
    public MultiCache(double latitude, double longitude, String informacoesEsconderijo, String nome, int dificuldade, String criador, ArrayList<Point2D.Double> conjuntoCaches){
         super(latitude,longitude,informacoesEsconderijo,nome,dificuldade,criador);
         this.todasCaches = new ArrayList<Point2D.Double>();
         for(Point2D.Double p: conjuntoCaches){
             this.todasCaches.add(new Point2D.Double(p.getX(),p.getY()));
         }
    }
    public MultiCache(MultiCache multiC){
        super(multiC);
        this.todasCaches = multiC.gettodasCaches();
    }
    
    /* gets e sets */
    public ArrayList<Point2D.Double> gettodasCaches(){
        ArrayList<Point2D.Double> res = new ArrayList<Point2D.Double>();
        
        for(Point2D.Double p: todasCaches){
             res.add(new Point2D.Double(p.getX(),p.getY()));
         }
        return res;
    }
    
    public void setConjPontos(ArrayList<Point2D.Double> conjuntoCaches){
        
        for(Point2D.Double c: conjuntoCaches){
            this.todasCaches.add(new Point2D.Double(c.getX(),c.getY()));
        }
    }
    
    public void addPonto(Point2D.Double p) throws PontoJaExisteException{
       if(this.todasCaches.add(new Point2D.Double(p.getX(),p.getY())) == false){
           throw new PontoJaExisteException();
       }
    }
    
    public void validarPontos(ArrayList<Point2D.Double> conjuntoPontos) throws NrPtsDiferenteException, PontosErradosException{
        int valida = 0;
        
        if(this.todasCaches.size() != conjuntoPontos.size()){
            throw new NrPtsDiferenteException();
        }
        
        for(Point2D.Double p : this.todasCaches){
            for(Point2D.Double p1: conjuntoPontos){
                if(p.equals(p1))
                    valida++;
                
            }
        }
        if(valida != this.todasCaches.size()){
            throw new PontosErradosException();
        }
    }
    
    /* outros metodos */
    /* equals */
    public boolean equals(Object umamultiC){
        if(this==umamultiC){
            return true;
        }
        if((umamultiC==null) || (this.getClass()!=umamultiC.getClass())){
            return false;
        }
        else{
            MultiCache c = (MultiCache) umamultiC;
            if(this.todasCaches.size() != c.gettodasCaches().size()){
                return false;
            }
            for(Point2D.Double c1: c.gettodasCaches()){
                if(!(this.todasCaches.contains(c1))) return false;
            }
            return (super.equals(umamultiC));
        }
    }
    /* toString */
    public String toString(){
       StringBuilder sb = new StringBuilder();
       sb.append("-----------------------------------------");
       sb.append("\nMULTI-CACHE:\n\nNome: "+ super.getNome() +"\nPontos: " + this.todasCaches.size() + "\n");
       sb.append("Coordenadas dos pontos (exceto ponto final):\n");
       for(Point2D.Double p: todasCaches){
           sb.append("(" + p.getX() + " - " + p.getY() + ")   ;   ");
       }
       sb.append("\nInformações: " + super.getInformacoesEsconderijo() + "\nDificuldade: " + super.getDificuldade() + "\nCriador: "+super.getCriador());
       sb.append("\n-----------------------------------------");
       return sb.toString();
    }
    /* clone */
    public MultiCache clone(){
        return new MultiCache(this);
    }

}
