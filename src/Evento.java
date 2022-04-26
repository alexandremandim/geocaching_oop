import java.util.GregorianCalendar;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TreeMap;
import java.awt.geom.Point2D;
import GeoCachingExceptions.*;
import java.io.*;

public class Evento implements Serializable
{
    private String nome;
    private int nrUsersMax, condicoesMeteo; /* 0 <= condicoesMeteo <= 5 */
    private double tempoMedioEncontrarCache;
    private GregorianCalendar datalimiteInscricao, dataInicioEvento;
    private boolean terminado;
    private Point2D.Double localizacaoEvento;
    TreeMap <String, Cache> cachesEvento;
    TreeMap <String, UserEvento> utilizadoresRegistados;

    /* construtores */
    public Evento(String nome, double tempoMedioEncontrarCache, int NrUsersMax, int condicoesMeteo, Point2D.Double localizacaoEvento, GregorianCalendar datalimiteInscricao, GregorianCalendar dataInicio, TreeMap <String, Cache> caches){
        this.nome = nome;
        this.nrUsersMax = NrUsersMax;
        this.condicoesMeteo = condicoesMeteo;
        this.localizacaoEvento = new Point2D.Double(localizacaoEvento.getX(),  localizacaoEvento.getY());
        this.datalimiteInscricao = new GregorianCalendar(datalimiteInscricao.get(Calendar.YEAR), datalimiteInscricao.get(Calendar.MONTH), datalimiteInscricao.get(Calendar.DAY_OF_MONTH));
        this.dataInicioEvento = new GregorianCalendar(dataInicio.get(Calendar.YEAR), dataInicio.get(Calendar.MONTH), dataInicio.get(Calendar.DAY_OF_MONTH));
        this.terminado = false;
        this.tempoMedioEncontrarCache = tempoMedioEncontrarCache;
        this.cachesEvento = new TreeMap <String, Cache>();
        
        for(Cache c : caches.values()){
            this.cachesEvento.put(c.getNome(), c);
        }
        
        this.utilizadoresRegistados = new TreeMap<String,UserEvento>();
    }
    public Evento(Evento evento){
        this.nome = evento.getNome();
        this.nrUsersMax = evento.getNrUsersMax();
        this.condicoesMeteo = evento.getcondicoesMeteo();
        this.datalimiteInscricao = evento.getdatalimiteInscricao();
        this.dataInicioEvento = evento.getdataInicioEvento();
        this.utilizadoresRegistados = evento.getutilizadoresRegistados();
        this.cachesEvento = evento.getcachesEvento();
        this.localizacaoEvento = evento.getlocalizacaoEvento();
        this.tempoMedioEncontrarCache = evento.gettempoMedioEncontrarCache();
    }

    /* gets/sets */
    public boolean getterminado(){ return this.terminado;}
    public double gettempoMedioEncontrarCache(){return this.tempoMedioEncontrarCache;}
    public int getNrUsersMax(){return this.nrUsersMax;}   
    public String getNome(){ return this.nome;}
    public int getcondicoesMeteo(){return this.condicoesMeteo;}
    public TreeMap <String, UserEvento> getutilizadoresRegistados(){
        TreeMap <String, UserEvento> new_users = new TreeMap <String, UserEvento>();
        
        for(UserEvento u: utilizadoresRegistados.values()){
            new_users.put(u.getEmail(), u);   
        }
        return new_users;
    }
    public TreeMap <String, Cache> getcachesEvento(){
        TreeMap <String, Cache> new_caches = new TreeMap <String, Cache>();
        
        for(Cache c: this.cachesEvento.values()){
            new_caches.put(c.getNome(), c);  
        }
        return new_caches;
    }
    public GregorianCalendar getdatalimiteInscricao(){return (new GregorianCalendar(this.datalimiteInscricao.get(Calendar.YEAR),this.datalimiteInscricao.get(Calendar.MONTH),this.datalimiteInscricao.get(Calendar.DAY_OF_MONTH)));}
    public GregorianCalendar getdataInicioEvento(){return (new GregorianCalendar(this.dataInicioEvento.get(Calendar.YEAR),this.dataInicioEvento.get(Calendar.MONTH),this.dataInicioEvento.get(Calendar.DAY_OF_MONTH)));}
    public Point2D.Double getlocalizacaoEvento(){return new Point2D.Double(localizacaoEvento.getX(), localizacaoEvento.getY());}
    
    /* outros metodos de instância */
    public void addUserEvento(User u) throws TempoInscricaoPassouException, UserJaExisteException, ClassNotFoundException, EventoCheioException{
        if(this.datalimiteInscricao.compareTo(new GregorianCalendar()) <= 0 )throw new TempoInscricaoPassouException(); 
        if(utilizadoresRegistados.containsKey(u.getEmail())) throw new UserJaExisteException(u.getEmail());
        if(nrUsersMax <=this.utilizadoresRegistados.size()) throw new EventoCheioException();
        UserEvento user = new UserEvento(u.getEmail());
        double multicache, tradicional, micro, virtual, misterio, evento;   /* tempos */
        TreeMap<String,Double> tempos = new TreeMap<String,Double>();
        
        multicache = u.mediaDificuldadeTipoCacheEncontrada("MultiCache"); 
        if(multicache == 0) multicache = this.tempoMedioEncontrarCache;
        else {multicache =  (double)tempoMedioEncontrarCache*((15-multicache)/10);multicache =  (double)multicache*(1+(this.condicoesMeteo/10));}
        tempos.put("MultiCache", multicache);
        
        tradicional = u.mediaDificuldadeTipoCacheEncontrada("CacheTradicional"); 
        if(tradicional == 0)tradicional = this.tempoMedioEncontrarCache;
        else {tradicional = tempoMedioEncontrarCache*((15-tradicional)/10);tradicional = tradicional*(1+(this.condicoesMeteo/10));}
        tempos.put("CacheTradicional",tradicional);
        
        micro = u.mediaDificuldadeTipoCacheEncontrada("MicroCache"); 
        if(micro== 0)micro = this.tempoMedioEncontrarCache;
        else {micro = tempoMedioEncontrarCache*((15-micro)/10);micro = micro*(1+(this.condicoesMeteo/10));}
        tempos.put("MicroCache",micro);
        
        virtual= u.mediaDificuldadeTipoCacheEncontrada("Virtual");       
        if(virtual == 0)virtual = this.tempoMedioEncontrarCache;
        else {virtual = tempoMedioEncontrarCache*((15-virtual)/10);virtual = virtual*(1+(this.condicoesMeteo/10));}
        tempos.put("Virtual",virtual);
        
        misterio= u.mediaDificuldadeTipoCacheEncontrada("CacheMisterio"); 
        if(misterio == 0)misterio = this.tempoMedioEncontrarCache;
        else {misterio = tempoMedioEncontrarCache*((15-misterio)/10);misterio = misterio*(1+(this.condicoesMeteo/10));}
        tempos.put("CacheMisterio",misterio);
        
        evento = u.mediaDificuldadeTipoCacheEncontrada("CacheEvento"); 
        if(evento == 0)evento = this.tempoMedioEncontrarCache;
        else {evento = tempoMedioEncontrarCache*((15-evento)/10);evento = evento*(1+(this.condicoesMeteo/10));}
        tempos.put("CacheEvento",evento);
        
        this.utilizadoresRegistados.put(u.getEmail(),user);
        user.atualizarTempos(tempos);
    }
    public boolean userRegistado(String emailUser){
       return this.utilizadoresRegistados.containsKey(emailUser);
    }
    public void registarDescoberta(String emailUser, String nomeCache) throws UserNaoExisteException, CacheNaoExisteException, NaoExisteTempoDesseTipoCacheException
    ,EstasAtrasadoException, CacheJaExisteException, EventoTerminadoException{
        if(this.terminado){throw new EventoTerminadoException(this.nome);}
        UserEvento u = utilizadoresRegistados.get(emailUser);
        if(u == null) throw new UserNaoExisteException(emailUser);
        Cache c = cachesEvento.get(nomeCache);
        if(c==null) throw new CacheNaoExisteException();
        
        
        u.registarDescoberta(c, condicoesMeteo);
    }  
    public String determinarVencedor(){
        int pontosMax = 0;
        UserEvento user = null;
        
        for(UserEvento utilizador: this.utilizadoresRegistados.values()){
                if(utilizador.getPontos()>pontosMax){
                    pontosMax = utilizador.getPontos();
                    user = utilizador;
                }
        }
        if(user!= null)    return ("O " + user.getEmail() + " ganhou o evento " + this.nome + " com " + pontosMax + " pontos.");
        else return "";
    }
    public void finalizarEvento() throws EventoNaoComecouException{
        if(this.dataInicioEvento.compareTo(new GregorianCalendar()) < 0){
            this.terminado = true;
        }
        else{throw new EventoNaoComecouException(this.nome);}
    }
    public Cache getCache(String nomeCache){
        return this.cachesEvento.get(nomeCache);
    }
    
    /* equals */
    public boolean equals(Object UmEvento){
        if(this == UmEvento){
            return true;
        }
        if((UmEvento == null) || (this.getClass() != UmEvento.getClass())){
            return false;
        }
        
        Evento u = (Evento)UmEvento;
        
        if(cachesEvento.size() != u.getcachesEvento().size() || utilizadoresRegistados.size() != u.getutilizadoresRegistados().size()) return false;
        
        for(String p : u.getcachesEvento().keySet()){
            if(!this.cachesEvento.containsKey(p)) return false;
        }
        
        for(String s: u.getutilizadoresRegistados().keySet()){
            if(!this.utilizadoresRegistados.containsKey(s)) return false;
        }
        
        return (this.nrUsersMax == u.getNrUsersMax() && this.condicoesMeteo == u.getcondicoesMeteo() && this.tempoMedioEncontrarCache == u.gettempoMedioEncontrarCache() &&
                this.datalimiteInscricao.equals(u.getdatalimiteInscricao()) && this.dataInicioEvento == u.getdataInicioEvento() && this.terminado == u.getterminado() &&
                this.localizacaoEvento.equals(u.getlocalizacaoEvento()) && this.nome.equals(u.getNome()));
    }  
    /* toString */
    public String toString(){
        StringBuilder sb = new StringBuilder();

        if(terminado){
            sb.append("O evento " + this.nome +" decorreu em "+this.dataInicioEvento.get(Calendar.YEAR) + "/" + (this.dataInicioEvento.get(Calendar.MONTH)+1) + "/" + this.dataInicioEvento.get(Calendar.DAY_OF_MONTH)
            + " com as condicoes metereologicas de " + this.condicoesMeteo + " (1-10).\n");
            sb.append("Este evento contou com " + this.utilizadoresRegistados.size() + " jogadores\n");
            sb.append("Localizacao: " + "(" + this.localizacaoEvento.getX() + "," + this.localizacaoEvento.getY() + ")" + "\n");
            sb.append(this.determinarVencedor());
        
        }
        else{
            sb.append("O evento " + this.nome +" vai decorrer em "+this.dataInicioEvento.get(Calendar.YEAR) + "/" + (this.dataInicioEvento.get(Calendar.MONTH)+1) + "/" + this.dataInicioEvento.get(Calendar.DAY_OF_MONTH)
            + " e conta com um nr máx de users de  " + this.nrUsersMax + ".\n");
            sb.append("Neste evento já se encontram registados " + this.utilizadoresRegistados.size() + " jogadores.\n");
        }
        
        sb.append ("Evento : " + this.nome + "\nNúmero máximo de users: ");
        sb.append (this.nrUsersMax +"\n");
        
        return sb.toString();
    }
    /* clone */
    public Evento clone(){
        return new Evento(this);
    }
    
}

