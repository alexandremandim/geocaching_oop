import java.awt.geom.Point2D.Double;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.ArrayList;
import GeoCachingExceptions.*;
import java.lang.Class;
import java.io.*;

public class Cache implements Comparable, Serializable
{
    private Point2D.Double coordenadas;
    private String informacoesEsconderijo;
    private String nome;
    private int dificuldade;    /* 1-10*/
    private ArrayList <String> livroRegisto;
    private String criador;
    
    public Cache(){
        this.coordenadas = new Point2D.Double(0.0,0.0);
        this.informacoesEsconderijo = new String("");
        this.nome = new String("");
        this.dificuldade = 0;
        this.livroRegisto = new ArrayList<String>();
        this.criador = "";
    }
    
    public Cache(double latitude, double longitude , String informacoesEsconderijo, String nome, int dificuldade, String criador){
        this.coordenadas = new Point2D.Double(latitude,longitude);
        this.informacoesEsconderijo = informacoesEsconderijo;
        this.nome = nome;
        this.dificuldade = dificuldade;
        this.livroRegisto = new ArrayList<String>();
        this.criador = criador;
    }
    
    public Cache(Cache c){
        this.coordenadas = c.getCoordenadas();
        this.informacoesEsconderijo = c.getInformacoesEsconderijo();
        this.nome = c.getNome();
        this.dificuldade = c.getDificuldade();
        this.livroRegisto = c.getLivroRegisto();
        this.criador = c.getCriador();
    }
    
    /* Gets e Sets */
   
    public Point2D.Double getCoordenadas() {
        return (Point2D.Double)coordenadas.clone();
    }
    public double getLatitude() {
        return coordenadas.getX();
    }
    public double getLongitude() {
        return coordenadas.getY();
    }
    public void setCoordenadas(Point2D.Double coordenadas) {
        this.coordenadas = (Point2D.Double)coordenadas.clone();
    }
    public String getInformacoesEsconderijo() {
        return informacoesEsconderijo;
    }
    public void setInformacoesEsconderijo(String informacoesEsconderijo) {
        this.informacoesEsconderijo = informacoesEsconderijo;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public int getDificuldade() {
        return dificuldade;
    }
    public void setDificuldade(int dificuldade) {
        this.dificuldade = dificuldade;
    }
    public ArrayList<String> getLivroRegisto(){
        ArrayList<String> new_livroRegistos = new ArrayList <String>(this.livroRegisto.size());
        
        for(String u: livroRegisto){
            new_livroRegistos.add(u);   
        }
        return new_livroRegistos;
    }
    public void setLivroRegisto(List<String> users){
        
        livroRegisto = new ArrayList <String>();
        for(String u: users){
            livroRegisto.add(u);
        }
    }
    public String getCriador(){
        return criador;
    }
    public void setCriador(String criador){
        this.criador = criador;
    }
    /* outros métodos de instancia */
    
    public void addUserLivroRegisto(String email) throws UserJaExisteException{
        if(livroRegisto.add(email) == false){
            throw new UserJaExisteException();
        }
    }
    public void rmvUserLivroRegisto(String email) throws UserNaoExisteException{
        if(livroRegisto.remove(email) == false){
            throw new UserNaoExisteException();
        }
    }
    /*equals */
    public boolean equals(Object umaCache){
        if(this==umaCache)
            return true;
        if((umaCache==null) || (this.getClass()!=umaCache.getClass()))
            return false;
        else{
            Cache c =(Cache) umaCache;
            return((this.coordenadas.equals(c.getCoordenadas())) && (this.informacoesEsconderijo.equals(c.getInformacoesEsconderijo())) && 
            (this.nome.equals(c.getNome())) && (this.dificuldade==c.getDificuldade()) && (this.livroRegisto.equals(c.getLivroRegisto())) &&(this.criador.equals(c.getCriador())));
        }
    }  
    /* toString */
    public String toString(){
        StringBuilder sb = new StringBuilder();

        sb.append ("\nNome: " + nome + "\nCoordenadas: (" + coordenadas.getX() + " , " + coordenadas.getY() + ").\nInformação Adicional : " + informacoesEsconderijo +"\nDificuldade: " + this.dificuldade + "\n"+ 
        "Criada por: "+ criador +".\n");
        
        return sb.toString();
    }
    /* Clone */
    public Cache clone(){
        return new Cache(this);
    }
    /* compareTo */
    public int compareTo(Object obj){
       Cache c = (Cache)obj;
       return (nome.compareTo(c.getNome()));
    }
}
