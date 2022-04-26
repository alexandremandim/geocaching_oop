import java.util.ArrayList;
import java.util.TreeMap;
import java.util.GregorianCalendar;
import GeoCachingExceptions.*;
import java.io.*;

public class UserEvento implements Serializable
{
    private String email;
    private TreeMap<String,Double> tempoMinimo;
    private ArrayList<Registo> registos;
    private int pontos;
    private boolean terminado;
    
    /* construtores*/
    public UserEvento(String umEmail){
        this.email = umEmail;   
        this.terminado =  false;
        this.tempoMinimo = new TreeMap<String,Double>();
        this.registos = new ArrayList<Registo>();
        this.pontos = 0;
    }
    public UserEvento(UserEvento us){
        this.email = us.getEmail();
        this.tempoMinimo = us.getTempoMinimo();
        this.registos = us.getRegistos();
        this.pontos = us.getPontos();
        this.terminado = us.getTerminado();
    }
    
    /* gets */
    public boolean getTerminado(){
        return this.terminado;
    }
    public String getEmail(){
        return this.email;
    }
    public TreeMap<String,Double> getTempoMinimo(){
        TreeMap<String,Double> novoTempoMinimo = new TreeMap<String,Double>();
        for(String s: this.tempoMinimo.keySet()){
            novoTempoMinimo.put(s,tempoMinimo.get(s));
        }
        return novoTempoMinimo;
    }
    public ArrayList<Registo> getRegistos(){
        ArrayList<Registo> reg = new ArrayList<Registo>(this.registos.size());  /* encapsulamento */
        
        for(Registo r: registos){
            reg.add(r.clone());
        }
        return reg;
    }
    public int getPontos(){
        return this.pontos;
    }
    public void setTerminado(boolean terminado){this.terminado = terminado;}
    
    /* outros metodos */
    public void atualizarTempos(TreeMap<String,Double> tempos){
        for(String s : tempos.keySet()){
            this.tempoMinimo.put(s,tempos.get(s));
        }
    }
    public boolean isPossivelRegistarDescoberta(){
        if(this.terminado == true) return false;
        GregorianCalendar ultimaAtividade = registos.get(registos.size()-1).getDataAtividade();
        GregorianCalendar agora = new GregorianCalendar();
        long miliSegundosPassadosDesdeUltimaDescoberta = ultimaAtividade.getTimeInMillis() - agora.getTimeInMillis();
        double segsPassadosDesdeUltimaDescoberta = miliSegundosPassadosDesdeUltimaDescoberta/1000;  /* segs que passaram desde a ultima descoberta*/
        double segsCacheMaisTempo = 0;
        for(Double b : this.tempoMinimo.values()){
            if(segsCacheMaisTempo < b.doubleValue()) segsCacheMaisTempo = b.doubleValue();
        }
        segsCacheMaisTempo = segsCacheMaisTempo * 60; /* segs da Cache com mais tempo pra descobrir */
        
        if(segsPassadosDesdeUltimaDescoberta > segsCacheMaisTempo ){ terminado = true; return false; }
        else return true;
    }
    public void registarDescoberta(Cache c, int condMeteo) throws NaoExisteTempoDesseTipoCacheException, EstasAtrasadoException,CacheJaExisteException {
        // verificar se nao a encontramos ja
        for(Registo r : registos){
            if(r.getCache().equals(c)){throw new CacheJaExisteException("Cache já foi criada/descoberta por " + this.email);} /* caso a cache já exista nao deixa adicionar */
        }
        
        // verificar se n passou o tempo
        Class c1 = c.getClass();
        String classe = c1.getSimpleName();
        Double t = tempoMinimo.get(classe); /* verificar o tempo minimo pra esse tipo de cache */
        if(t==null) throw new NaoExisteTempoDesseTipoCacheException(classe);
        if(terminado == true) throw new NaoExisteTempoDesseTipoCacheException(classe);
        
        if(!registos.isEmpty()){ /* existem descobertas */
            GregorianCalendar ultimaAtividade = registos.get(registos.size()-1).getDataAtividade();
            GregorianCalendar agora = new GregorianCalendar();
            long diferenca = ultimaAtividade.getTimeInMillis() - agora.getTimeInMillis();
            double tempoMinutos = t.doubleValue();
            double segundosARespeitar = (tempoMinutos*60);
            double segundosPassados = diferenca/1000;
            
            if(segundosPassados > segundosARespeitar) throw new EstasAtrasadoException();
        }
        
        // atualizar pontos
        pontos += (condMeteo + c.getDificuldade());
        // adicionar cache registos
        GregorianCalendar novoCalendario = new GregorianCalendar();
        Registo reg = new Registo(novoCalendario, c, true, false, condMeteo);
        this.registos.add(reg);
    }
    
    
    /* equals, tostring, clone */
    public boolean equals(Object umUserEvento){
        if(this == umUserEvento){
            return true;
        }
        if((umUserEvento == null) || (this.getClass() != umUserEvento.getClass())){
            return false;
        }

        UserEvento u = (UserEvento) umUserEvento;
        
        if(u.getTempoMinimo().size() != this.tempoMinimo.size()) return false;
        for(String s: this.tempoMinimo.keySet()){
            if(this.tempoMinimo.get(s) == u.getTempoMinimo().get(s)) return false;
        }
        if(u.getRegistos().size() !=this.registos.size()) return false;
        for(Registo reg: u.getRegistos()){
            if(registos.contains(reg)==false) return false;
        }
                        
        return((this.email.equals(u.getEmail())) && (this.pontos==u.getPontos()) );
    }
    
    public UserEvento clone(){
        return new UserEvento(this);
    }
    
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("O user " + email + " tem " + pontos + " pontos\n");
        sb.append("Tem " + registos.size() + " atividades de descoberta.\n");
        return sb.toString();
    }
}
