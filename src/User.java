import java.util.GregorianCalendar;
import java.util.TreeSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.io.*;
import GeoCachingExceptions.*;
import java.util.Collections;

public class User implements Comparable, Serializable{
    private String email;
    private String password;
    private String nome;
    private char genero;
    private String morada;
    private int pontos;
    private GregorianCalendar data_de_nascimento;
    
    private ArrayList <Registo> registos;
    private ArrayList <Estatistica> estatisticas;
    private TreeSet<String> amigosEmail;
    
    /* Construtores */
    public User(){
        this.email = new String();
        this.password = new String();
        this.nome = new String();
        this.genero = 'N';
        this.morada = new String();
        this.pontos = 0;
        this.data_de_nascimento = new GregorianCalendar(0,0,0);
        
        this.registos = new ArrayList<Registo>();
        this.estatisticas = new ArrayList<Estatistica>();
        this.amigosEmail = new TreeSet<String>();
    }
    public User(User u){
        this.email = u.getEmail();
        this.password = "";
        this.nome = u.getNome();
        this.genero = u.getGenero();
        this.morada = u.getMorada();
        this.pontos = u.getPontos();
        this.data_de_nascimento = u.getDataDeNascimento();
        
        this.registos = u.getRegistos();
        this.estatisticas = u.getEstatisticas();
        this.amigosEmail = new TreeSet<String>();
    }
    /*(umGenero): Masculino -> M, Feminino -> F */
    public User(String umEmail, String umaPass, String umNome, char umGenero, String umaMorada, int pontos, GregorianCalendar umaData){
        this.email = umEmail;
        this.password = umaPass;
        this.nome = umNome;
        this.genero = umGenero;
        this.morada = umaMorada;
        this.pontos = pontos;
        this.data_de_nascimento = (new GregorianCalendar(umaData.get(Calendar.YEAR),umaData.get(Calendar.MONTH),umaData.get(Calendar.DAY_OF_MONTH)));
        
        this.registos = new ArrayList<Registo>();
        this.estatisticas = new ArrayList<Estatistica>();
        this.amigosEmail = new TreeSet<String>();
    }
    
    /* gets e sets */
    
    public String getEmail(){
        return this.email;
    }
    public String getNome(){
        return this.nome;
    }
    public char getGenero(){
        return this.genero;
    }
    public String getMorada(){
        return this.morada;
    }
    public int getPontos(){
        return this.pontos;
    }
    public GregorianCalendar getDataDeNascimento(){
        return (new GregorianCalendar(data_de_nascimento.get(Calendar.YEAR),data_de_nascimento.get(Calendar.MONTH),data_de_nascimento.get(Calendar.DAY_OF_MONTH)));
    }
    public ArrayList<Registo> getRegistos(){
        ArrayList<Registo> reg = new ArrayList<Registo>(this.registos.size());  /* encapsulamento */
        
        for(Registo r: registos){
            reg.add(r.clone());
        }
        return reg;
    }
    public ArrayList<Estatistica> getEstatisticas(){
        ArrayList<Estatistica> newestatistica= new ArrayList<Estatistica>(this.estatisticas.size());
        
        for(Estatistica e: estatisticas){
            newestatistica.add(e.clone());
        }
        return newestatistica;
    }
    public TreeSet<String> getAmigos(){
        TreeSet<String> newAmigos = new TreeSet<String>();
        
        for(String u: amigosEmail){
            newAmigos.add(u);
        }
        return newAmigos;
    }
    public void setRegistos(ArrayList<Registo> reg){
        
        for(Registo r: reg){
            this.registos.add(r.clone());
        }
    }
    public void setAmigos(TreeSet<String> newAmigos){
        
        for(String u: newAmigos){
            this.amigosEmail.add(u);
        }
    }
    /* outros métodos de instância */
    public void mudarPass(String passAntiga, String passNova) throws PassIncorretaException{
        if(this.password.equals(passAntiga) == true){   
            this.password = passNova;
        }
        else{ throw new PassIncorretaException();}
    }
    public boolean validarPass(String pass){
        return this.password.equals(pass);
    }
    public void editarPerfil(String pass, String nome, char genero, String morada, GregorianCalendar novaData) throws PassIncorretaException{
        if(!this.password.equals(pass)){ throw new PassIncorretaException();}
        this.nome = nome;
        this.genero = genero;
        this.morada = morada;
        this.data_de_nascimento = new GregorianCalendar(novaData.get(Calendar.YEAR), novaData.get(Calendar.MONTH)-1, novaData.get(Calendar.DAY_OF_MONTH));
    }
     public void adicionarAmigo(String newAmigo) throws AdicionarProprioException, AmigoJaExisteException{
        if(this.email.equals(newAmigo)){ throw new AdicionarProprioException();}
        if(amigosEmail.add(newAmigo) == false){ throw new AmigoJaExisteException(newAmigo);};
    }
    public void removerAmigo(String amigo) throws AmigoNaoExisteException{
        if(amigosEmail.remove(amigo) == false){throw new AmigoNaoExisteException(amigo);}
    }
    public void criarCache(Cache c){
        GregorianCalendar novoCalendario = new GregorianCalendar();
        
        Registo reg = new Registo(novoCalendario, c, false, true, 0);
        this.registos.add(reg);
        
        for(Estatistica e : estatisticas){
            /* Existe estatistica este mes */
            if(e.getData().get(Calendar.YEAR) == novoCalendario.get(Calendar.YEAR) && 
            e.getData().get(Calendar.MONTH) == novoCalendario.get(Calendar.MONTH)){
                e.add(reg);
                return;
            }
        }
        
        /* Ainda nao existe objeto estatistica para este mês */
        Estatistica novaEstatistica = new Estatistica();
        novaEstatistica.add(reg);
        estatisticas.add(novaEstatistica);
    }
    public void registarDescobertaCache(int condMeteo, Cache cache) throws CacheJaExisteException{
        
        for(Registo r : registos){
            if(r.getCache().equals(cache)){throw new CacheJaExisteException("Cache já foi criada/descoberta por " + this.email);} /* caso a cache já exista nao deixa adicionar */
        }
        
        GregorianCalendar novoCalendario = new GregorianCalendar();
        Registo reg = new Registo(novoCalendario, cache, true, false, condMeteo);
        this.registos.add(reg);
        pontos += (condMeteo + cache.getDificuldade());
       
        for(Estatistica e : estatisticas){
            /* Existe estatistica este mes */
            if(e.getData().get(Calendar.YEAR) == novoCalendario.get(Calendar.YEAR) && 
            e.getData().get(Calendar.MONTH) == novoCalendario.get(Calendar.MONTH)){
                e.add(reg);
                return;
            }
        }
        
        /* Ainda nao existe objeto estatistica para este mês */
        Estatistica novaEstatistica = new Estatistica();
        novaEstatistica.add(reg);
        estatisticas.add(novaEstatistica);
    }
    public void registarEvento(Evento e) throws EventoJaRegistadoException{
        for(Registo r : registos){  /* verifica se este evento ja n está registado */
            Evento eventoRegisto = r.getEvento();
            if(eventoRegisto != null && eventoRegisto == e) throw new EventoJaRegistadoException(e.getNome());
        }
        
        GregorianCalendar novoCalendario = new GregorianCalendar();
        Registo reg = new Registo(e, e.getcondicoesMeteo());
        this.registos.add(reg);
    }
    public void eliminaDescoberta(Cache c){
        int pontosRemover = 0;
        GregorianCalendar data;
        Registo reg = null;
        
        for(Registo r : registos){
            if(r.getCache().equals(c)==true && r.getEncontrado() == true){
                pontosRemover = r.getcondicoesMeteorologicas() + c.getDificuldade();
                pontos -= pontosRemover;    /* Retira os pontos */
                reg = r;
                break;
            }
        }
        data = (reg.getDataAtividade());
        
        /* Apaga nas estatisticas os registos relativos aquela cache */
        for(Estatistica e : estatisticas){
            if(e.getData().equals(data)){
                e.removerRegistoEncontrado(reg);
            }
        }
        
        registos.remove(reg);     /* Remove nos registos */
    }
    public ArrayList<Registo> get10Registos() throws SemRegistosException{
        if(this.registos.size() == 0) throw new SemRegistosException();
        
        ArrayList<Registo> reg = new ArrayList<Registo>(this.registos.size());  /* encapsulamento */
        Iterator it = this.registos.iterator();
        int i = 0;
        
        while(it.hasNext() && i<10){
            Registo r = (Registo)it.next();
            reg.add(r.clone());
            i++;
        }
        
        Collections.sort(reg,new ComparatorRegistoTimeLine());
        return reg;
    }
    public void eliminarCacheAbusadora(double latitude, double longitude){
        /* eliminar nos registos */
        Iterator it = registos.iterator();
        while(it.hasNext()){
            Registo r = (Registo)it.next();
            if(r.getCache().getLatitude() == latitude && r.getCache().getLongitude() == longitude){
                it.remove();
            }
        }
        /* eliminar nas estatisticas */
        for(Estatistica e: estatisticas){
            e.removerRegisto(latitude,longitude);
        }
    }
    public int qtsEncontradasTipo(String tipo) throws ClassNotFoundException{
        int resultado = 0;
        for(Estatistica e : this.estatisticas) {
            resultado += e.qtsEncontradaTipo(tipo);
        }
        return resultado;
    }
    public double mediaDificuldadeTipoCacheEncontrada(String tipo) throws ClassNotFoundException{
        double media = 0;
        int total = this.estatisticas.size();
        if(total== 0) return 0;
        
        for(Estatistica e : this.estatisticas) {
            media += e.qtsEncontradaTipo(tipo);
        }
        return media = media/total;
    }
    
    /* equals, tostring, clone */
    public boolean equals(Object umUser){
        if(this == umUser){
            return true;
        }
        if((umUser == null) || (this.getClass() != umUser.getClass())){
            return false;
        }

        User u = (User) umUser;
        
        if(u.getRegistos().size() !=this.registos.size()) return false;
        for(Registo reg: u.getRegistos()){
            if(registos.contains(reg)==false) return false;
        }
        
        if(u.getEstatisticas().size() !=this.estatisticas.size()) return false;
        for(Estatistica esta: u.getEstatisticas()){
            if(estatisticas.contains(esta)==false) return false;
        }
        
        if(u.getAmigos().size() !=this.amigosEmail.size()) return false;
        for(String us: u.getAmigos()){
            if(amigosEmail.contains(us)==false) return false;
        }
        
        return((this.email.equals(u.getEmail())) && (this.nome.equals(u.getNome())) && 
        (this.genero == u.getGenero()) && (this.morada.equals(u.getMorada())) && (this.pontos == u.getPontos()) && (this.data_de_nascimento.equals(u.getDataDeNascimento())));
    }
    
    public User clone(){
        return new User(this);
    }
    
    public String toString(){
        StringBuilder sb = new StringBuilder();
        
        sb.append("\nNome: " + nome + "\nEmail: " + email + "\nPassword: " +password+ "\nGenero: " + genero + "\nPontos: " + pontos + "\nMorada: " 
        +morada+ "\nData de Nascimento: "+ data_de_nascimento.get(Calendar.YEAR) + "/" +  (data_de_nascimento.get(Calendar.MONTH) + 1) + "/" + data_de_nascimento.get(Calendar.DAY_OF_MONTH) +"\n" );
        
        return sb.toString();
    }
 
    public int compareTo(Object obj){
        User u = (User)obj;
        
         return (email.compareTo(u.getEmail()));
    }
}
