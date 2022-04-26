import GeoCachingExceptions.*;
import java.util.TreeSet;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Random;
import java.util.Calendar;
import java.awt.geom.Point2D;
import java.util.TreeMap;
import java.io.*;
import java.util.Collections;

public class GeoCaching implements Serializable
{
   private String loggedUser, loggedAdmin;
   
   private UsersGroup utilizadores;
   private Caches caches;
   private Administracao administracao;
   private TreeMap<String,Evento> eventos;
   
    /* Construtores */
    public GeoCaching()
    {
        loggedUser = "";
        loggedAdmin = "";
        utilizadores = new UsersGroup();
        administracao = new Administracao();
        caches = new Caches();
        eventos = new TreeMap<String,Evento>();
    }
    
    public GeoCaching(GeoCaching g){
        loggedUser = g.getloggedUser();
        loggedAdmin = g.getloggedAdmin();
        utilizadores = g.getutilizadores();
        caches = g.gettodasAsCaches();
        administracao = g.getadministracao();
        eventos = g.getEventos();
    }
    
    /* gets */
    public String getloggedUser(){return this.loggedUser;}
    public String getloggedAdmin(){return this.loggedAdmin;}
    public UsersGroup getutilizadores(){return this.utilizadores.clone();}
    public ArrayList<String> getutilizadoresString(){ return this.utilizadores.getUsersString();}
    public Caches gettodasAsCaches(){return this.caches.clone();}
    public ArrayList<String> gettodasAsCachesString(){return this.caches.getTodasCacheString();}
    public Administracao getadministracao(){return this.administracao.clone(); }
    public TreeMap<String,Evento> getEventos(){
        TreeMap<String,Evento> newEventos = new TreeMap<String,Evento>();
        for(String s: this.eventos.keySet()){
            newEventos.put(s,this.eventos.get(s));
        }
        return newEventos;
    }
    /* sets */
    public void setutilizadores(UsersGroup users){this.utilizadores =  users.clone();}
    public void settodasAsCaches(Caches caches){this.caches = caches.clone();}
    
    /* restante API */
    /* metodos para os users */
    
    /* Insere um user caso este nao exista na lista de utilizadores nem exista um admin com esse email */
    public void registarUser(String email, String pass, String nome, char genero, String morada, int anoNascimento, int mesNascimento, int diaNascimento) throws DadosInvalidosException,
    AdminJaExisteException, UserJaExisteException, PassIncorretaException{
        if(mesNascimento < 1 || mesNascimento > 12 || diaNascimento < 1 || diaNascimento > 31) throw new DadosInvalidosException("Data de nascimento inválida.");
        if(genero != 'F' && genero != 'M') throw new DadosInvalidosException("Género diferente de F ou M.");
        if(email.equals("")) throw new DadosInvalidosException("Email vazio.");
        
        User u = new User(email,pass,nome,genero,morada,0,new GregorianCalendar(anoNascimento,mesNascimento - 1,diaNascimento));
        if(administracao.contains(email)) throw new AdminJaExisteException(email);
        
        utilizadores.insertUser(u, pass);
    }
    public void encontreiMultiCache(String nomeCache, ArrayList<Point2D.Double> pontos) throws UserNaoLogadoException, 
    CacheNaoExisteException, TipoDeCacheErradoException, PontosErradosException, NrPtsDiferenteException, UserNaoExisteException, CacheJaExisteException, UserJaExisteException{
        if(loggedUser.equals("")) throw new UserNaoLogadoException();
        
        /* se a cache existe vou adicionar o registo ao user e o user ao livroregistos da cache */
        Cache c = caches.getCache(nomeCache);
        if(c==null) throw new CacheNaoExisteException();
        
        Class c1 = c.getClass();
        if(!c1.getSimpleName().equals("MultiCache")) throw new TipoDeCacheErradoException(); /* verifica se é uma multicache */
        else{
            ((MultiCache)c).validarPontos(pontos);/* verifica se o pontos estão certos */
            Random rand = new Random();
            int condMeteo = rand.nextInt(10)+1; /* A condicao meteorologica varia entre 1 e 10 */
            
            utilizadores.registarDescoberta(condMeteo, c ,loggedUser);
            caches.addUserLivroRegisto(nomeCache, loggedUser);/* Adicionar user ao livro registo da cache */
        }
    }
    public void encontreiCacheMisterio(String nomeCache, String resposta) throws UserNaoLogadoException, 
    CacheNaoExisteException, TipoDeCacheErradoException, RespostaErradaException, UserNaoExisteException, CacheJaExisteException, UserJaExisteException{
        if(loggedUser.equals("")) throw new UserNaoLogadoException();
        Cache c = caches.getCache(nomeCache);
        
        if(c==null) throw new CacheNaoExisteException();
        
        Class c1 = c.getClass();
        if(!c1.getSimpleName().equals("CacheMisterio")) throw new TipoDeCacheErradoException();  /* verifica se é uma Cache Misterio*/
        else{
            if(((CacheMisterio)c).resolverMisterio(resposta) == false){
                throw new RespostaErradaException();
            }   
            Random rand = new Random();
            int condMeteo = rand.nextInt(10)+1; /* A condicao meteorologica varia entre 1 e 10 */
            
            utilizadores.registarDescoberta(condMeteo, c ,loggedUser);
            caches.addUserLivroRegisto(nomeCache, loggedUser);/* Adicionar user ao livro registo da cache */
        }
    }
    public void encontreiCacheEvento(String nomeCache) throws UserNaoLogadoException, CacheNaoExisteException, 
    TipoDeCacheErradoException, UserNaoExisteException, CacheJaExisteException, UserJaExisteException{
        if(loggedUser.equals("")) throw new UserNaoLogadoException();
        
        Cache c = caches.getCache(nomeCache);
        if(c==null) throw new CacheNaoExisteException();
        
        Class c1 = c.getClass();
        if(!c1.getSimpleName().equals("CacheEvento")) throw new TipoDeCacheErradoException(); /* verifica se é uma Cache evento */
        else{
            Random rand = new Random();
            int condMeteo = rand.nextInt(10)+1; /* A condicao meteorologica varia entre 1 e 10 */
            
            utilizadores.registarDescoberta(condMeteo, c ,loggedUser);
            caches.addUserLivroRegisto(nomeCache, loggedUser);/* Adicionar user ao livro registo da cache */
        }
    }
    public void encontreiCacheVirtual(String nomeCache, String resposta) throws UserNaoLogadoException, 
    CacheNaoExisteException, TipoDeCacheErradoException, RespostaErradaException, UserNaoExisteException, CacheJaExisteException, UserJaExisteException{
        if(loggedUser.equals("")) throw new UserNaoLogadoException();
        
        Cache c = caches.getCache(nomeCache);
        if(c==null) throw new CacheNaoExisteException();
        
        Class c1 = c.getClass();
        if(!c1.getSimpleName().equals("Virtual")) throw new TipoDeCacheErradoException();  /* verifica se é uma Cache Virtual */
        else{
            if(((Virtual)c).verificaProva(resposta) == false) throw new RespostaErradaException(); /* verifica se o pontos estão certos */
            Random rand = new Random();
            int condMeteo = rand.nextInt(10)+1; /* A condicao meteorologica varia entre 1 e 10 */
            
            utilizadores.registarDescoberta(condMeteo, c ,loggedUser);
            caches.addUserLivroRegisto(nomeCache, loggedUser);/* Adicionar user ao livro registo da cache */
        }
    }
    public void encontreiMicroCache(String nomeCache) throws UserNaoLogadoException, 
    CacheNaoExisteException, TipoDeCacheErradoException, UserNaoExisteException, CacheJaExisteException, UserJaExisteException{
        if(loggedUser.equals("")) throw new UserNaoLogadoException();
        
        Cache c = caches.getCache(nomeCache);
        if(c==null) throw new CacheNaoExisteException();
        
        
        Class c1 = c.getClass();
        if(!c1.getSimpleName().equals("MicroCache")) throw new TipoDeCacheErradoException(); /* verifica se é uma Micro Cache */
        else{
            Random rand = new Random();
            int condMeteo = rand.nextInt(10)+1; /* A condicao meteorologica varia entre 1 e 10 */
            
            utilizadores.registarDescoberta(condMeteo, c ,loggedUser);
            caches.addUserLivroRegisto(nomeCache, loggedUser);/* Adicionar user ao livro registo da cache */
        }
    }
    public void encontreiCacheTradicional(String nomeCache, ArrayList<String> objetosRetirados, ArrayList<String> objetosInseridos) throws UserNaoLogadoException, 
    CacheNaoExisteException, TipoDeCacheErradoException, UserNaoExisteException, CacheJaExisteException, ObjetoNaoExisteException, UserJaExisteException{
        if(loggedUser.equals("")) throw new UserNaoLogadoException();
        
        Cache c = caches.getCache(nomeCache);
        if(c==null) throw new CacheNaoExisteException();
        
        Class c1 = c.getClass();
        if(!c1.getSimpleName().equals("CacheTradicional")) throw new TipoDeCacheErradoException(); /* verifica se é uma Cache Tradicional */
        else{
            Random rand = new Random();
            int condMeteo = rand.nextInt(10)+1; /* A condicao meteorologica varia entre 1 e 10 */
            
            ((CacheTradicional)c).setObjetos(objetosInseridos);
            ((CacheTradicional)c).tirarObjetos(objetosRetirados);
            utilizadores.registarDescoberta(condMeteo, c ,loggedUser);
            caches.addUserLivroRegisto(nomeCache, loggedUser);/* Adicionar user ao livro registo da cache */
        }
    }
    public void removerEncontroCache(String nomeCache) throws ECriadorException, UserNaoLogadoException, CacheNaoExisteException, UserNaoExisteException{ /* remover o registo no user e tirar do livroRegistos */
        if(loggedUser.equals("")) throw new UserNaoLogadoException();
        
        Cache c = caches.getCache(nomeCache);
        if(c==null) throw new CacheNaoExisteException();
        if(c.getCriador().equals(loggedUser)) throw new ECriadorException();
        c.rmvUserLivroRegisto(loggedUser);  /* removi no livro de registos */
        utilizadores.eliminarDescoberta(c,loggedUser);    /* removi os registos e estatisticas de encontro dessa cache */
    }
    public ArrayList<String> ultimas10Atividades() throws UserNaoLogadoException, SemRegistosException, UserNaoExisteException{
        ArrayList<String> ultimas10 = new ArrayList<String>();
        
        if(loggedUser.equals("")) throw new UserNaoLogadoException();
        
        for(Registo r: utilizadores.ultimas10Atividades(loggedUser)){
            ultimas10.add(r.toStringReduzido());
        }
        
        return ultimas10;
    }
    public String verAtividade(int idRegisto, String email) throws UserNaoLogadoException, UserNaoExisteException{
        if(loggedUser.equals("")) throw new UserNaoLogadoException();
        String atividade = "";
        
        for(String u : utilizadores.getUsersString()){
            for(Registo r : utilizadores.get(u).getRegistos()){
                if(r.getIdRegisto() == idRegisto) return "Atividade de :" + u + "\n" + r.toString();
            }
        }
        return atividade;
    }
    public ArrayList<String> meusAmigos() throws UserNaoLogadoException,UserNaoExisteException{
        ArrayList<String> retornoAmigos = new ArrayList<String>();
    
        if(loggedUser.equals("")) throw new UserNaoLogadoException();
        
        return utilizadores.amigos(loggedUser);
    }
    public ArrayList<String> ultimas10AtividadesAmigos() throws UserNaoLogadoException,UserNaoExisteException, SemRegistosException{
        
        /* problema a resolver, ordenaçao!!  time line */
        int i = 0;
        ArrayList<String> ultimas10 = new ArrayList<String>();
        if(loggedUser.equals("")) throw new UserNaoLogadoException();
        
        ArrayList<Registo> regAmigos = new ArrayList<Registo>();
        for(String u: utilizadores.amigos(loggedUser)){
            for(Registo r: utilizadores.ultimas10Atividades(u)){
                regAmigos.add(r);
            }
        }
        Collections.sort(regAmigos, new ComparatorRegistoTimeLine());
        for(Registo r: regAmigos){
            ultimas10.add(r.toStringReduzido());
            i++;
            if(i>=10){
                break;
            }
        }
        return ultimas10;
    }
    public String meuPerfil() throws UserNaoLogadoException, UserNaoExisteException{
        if(loggedUser.equals("")) throw new UserNaoLogadoException();
        return(utilizadores.verUser(loggedUser) );
    }
    public void editarPerfil(String password, String nome, char genero, String morada, GregorianCalendar dataNascimento) throws UserNaoLogadoException, UserNaoExisteException, PassIncorretaException{
        if(loggedUser.equals("")) throw new UserNaoLogadoException();
        utilizadores.editUser(loggedUser, password, nome, genero, morada, dataNascimento);
    }
    public void trocaPass(String passAntiga, String passNova) throws UserNaoLogadoException, PassIncorretaException, UserNaoExisteException{
        if(loggedUser.equals("")) throw new UserNaoLogadoException();
        utilizadores.mudarPass(passAntiga, passNova, loggedUser);
    }
    public void adicionarAmigo(String email) throws UserNaoLogadoException, UserNaoExisteException, AmigoNaoExisteException, AdicionarProprioException, AmigoJaExisteException{
        if(loggedUser.equals("")) throw new UserNaoLogadoException();
        utilizadores.adicionarAmigo(loggedUser,email);
    }
    public void removerAmigo(String email) throws UserNaoLogadoException, UserNaoExisteException, AmigoNaoExisteException{
        if(loggedUser.equals("")) throw new UserNaoLogadoException();
        utilizadores.removerAmigo(loggedUser,email);
    }
    public String estatisticaMes(int ano, int mes) throws UserNaoLogadoException, UserNaoExisteException, ClassNotFoundException{
        if(loggedUser.equals("")) throw new UserNaoLogadoException();
        mes = mes--;
        
        StringBuilder sb = new StringBuilder();
        for(Estatistica e : utilizadores.estatisticaUser(loggedUser)){
            if(e.getData().get(Calendar.YEAR) == ano && e.getData().get(Calendar.MONTH) == mes-1){
                sb.append("Em "+ ano + "/" + mes + " foram criadas " + e.qtsCriadasTotal() + " caches. Das quais :\n");
                sb.append("Multi-Cache : " + e.qtsCriadasTipo("MultiCache") + "\n");
                sb.append("Cache Misterio : " + e.qtsCriadasTipo("CacheMisterio") + "\n");
                sb.append("Cache Evento : " + e.qtsCriadasTipo("CacheEvento") + "\n");
                sb.append("Cache Virtual : " + e.qtsCriadasTipo("Virtual") + "\n");
                sb.append("Micro Cache: " + e.qtsCriadasTipo("MicroCache") + "\n");
                sb.append("Cache Tradicional : " + e.qtsCriadasTipo("CacheTradicional") + "\n");
                sb.append("\nE foram encontradas " + e.qtsEncontradasTotal() + " caches. Das quais :\n");
                sb.append("Multi-Cache : " + e.qtsEncontradaTipo("MultiCache") + "\n");
                sb.append("Cache Misterio : " + e.qtsEncontradaTipo("CacheMisterio") + "\n");
                sb.append("Cache Evento : " + e.qtsEncontradaTipo("CacheEvento") + "\n");
                sb.append("Cache Virtual : " + e.qtsEncontradaTipo("Virtual") + "\n");
                sb.append("Micro Cache: " + e.qtsEncontradaTipo("MicroCache") + "\n");
                sb.append("Cache Tradicional : " + e.qtsEncontradaTipo("CacheTradicional") + "\n");
            }
        }
        
        return sb.toString();
    }
    public String estatisticaAno(int ano) throws UserNaoLogadoException, UserNaoExisteException, ClassNotFoundException{
        if(loggedUser.equals("")) throw new UserNaoLogadoException();
        
        int criadasTotal = 0, encontradasTotal = 0;
        int cMulti = 0, cMisterio = 0, cEvento = 0, cVirtual = 0, cMicro = 0,  cTradicional = 0, eMulti = 0, eMisterio = 0, eEvento = 0, eVirtual = 0, eMicro = 0,  eTradicional = 0;
        StringBuilder sb = new StringBuilder();
        
        for(Estatistica e :  utilizadores.estatisticaUser(loggedUser)){
            if(e.getData().get(Calendar.YEAR) == ano){
                criadasTotal += e.qtsCriadasTotal();
                cMulti += e.qtsCriadasTipo("MultiCache");
                cMisterio += e.qtsCriadasTipo("CacheMisterio");
                cEvento += e.qtsCriadasTipo("CacheEvento");
                cVirtual+= e.qtsCriadasTipo("Virtual");
                cMicro+= e.qtsCriadasTipo("MicroCache");
                cTradicional += e.qtsCriadasTipo("CacheTradicional");
                
                encontradasTotal += e.qtsEncontradasTotal();
                eMulti += e.qtsEncontradaTipo("MultiCache");
                eMisterio += e.qtsEncontradaTipo("CacheMisterio");
                eEvento += e.qtsEncontradaTipo("CacheEvento");
                eVirtual+= e.qtsEncontradaTipo("Virtual");
                eMicro+= e.qtsEncontradaTipo("MicroCache");
                eTradicional += e.qtsEncontradaTipo("CacheTradicional");
            }
        }
        
        sb.append("No ano "+ ano + " foram criadas " + criadasTotal + " caches. Das quais :\n");
        sb.append("Multi-Cache : " + cMulti + "\n");
        sb.append("Cache Misterio : " + cMisterio + "\n");
        sb.append("Cache Evento : " + cEvento + "\n");
        sb.append("Cache Virtual : " + cVirtual + "\n");
        sb.append("Micro Cache: " + cMicro + "\n");
        sb.append("Cache Tradicional : " + cTradicional + "\n");
        
        sb.append("\nE foram encontradas " + encontradasTotal + " caches. Das quais :\n");
        sb.append("Multi-Cache : " + eMulti + "\n");
        sb.append("Cache Misterio : " + eMisterio + "\n");
        sb.append("Cache Evento : " + eEvento + "\n");
        sb.append("Cache Virtual : " + eVirtual + "\n");
        sb.append("Micro Cache: " + eMicro + "\n");
        sb.append("Cache Tradicional : " + eTradicional + "\n");
        
        return sb.toString();
    }
    public void reportarAbusoCache(String nomeCache) throws UserNaoLogadoException, CacheNaoExisteException, CacheJaExisteException{
        if(loggedUser.equals("")) throw new UserNaoLogadoException();
        
        Cache c = caches.getCache(nomeCache);
        if(c==null) throw new CacheNaoExisteException();
        administracao.reportarCache(c);
    }
    public void registarNumEvento(String nomeEvento) throws EventoCheioException, UserNaoLogadoException, UserNaoExisteException, TempoInscricaoPassouException, UserJaExisteException, ClassNotFoundException, EventoNaoExisteException{
        if(loggedUser.equals("")) throw new UserNaoLogadoException();
        Evento aux = this.eventos.get(nomeEvento);
        if(aux == null){throw new EventoNaoExisteException();}
        else{
            UserEvento userEvento = new UserEvento(loggedUser);
            aux.addUserEvento(utilizadores.get(loggedUser));
        }
    }
    public void entrarEvento(String evento) throws UserNaoLogadoException, EventoNaoExisteException, UserNaoRegistadoException, EventoTerminadoException, EventoNaoComecouException{
        if(loggedUser.equals("")) throw new UserNaoLogadoException();
        if(!this.eventos.containsKey(evento)) throw new EventoNaoExisteException();
        Evento e = this.eventos.get(evento);
        if(e.getdataInicioEvento().compareTo(new GregorianCalendar()) < 0) throw new EventoTerminadoException();
        else if(e.getdataInicioEvento().compareTo(new GregorianCalendar()) > 0) throw new EventoNaoComecouException();
        if(e.userRegistado(loggedUser) == false) throw new UserNaoRegistadoException(loggedUser);
    }
    
    /* metodos para os admins */
    public void registarAdmin(String email, String pass) throws AdminNaoLogadoException, AdminJaExisteException{
        if(!(loggedAdmin.equals(""))){
            administracao.addAdmin(email,pass);
        }
        else throw new AdminNaoLogadoException();
    }
    public void eliminarAdmin(String email) throws AdminNaoLogadoException, UmAdminException, AdminNaoExisteException{
        if(!(loggedAdmin.equals(""))){
            administracao.removeAdmin(email);
        }
        else throw new AdminNaoLogadoException();
    }
    public int nrCachesReportadasPorResolver() throws AdminNaoLogadoException{
        if(loggedAdmin.equals("")) throw new AdminNaoLogadoException();
        return administracao.nrCachesReportadas();
    }
    public ArrayList<String> cachesReportadas(){
        ArrayList<String> novo = new ArrayList<String>();
        if(loggedAdmin.equals("")) return novo;
        for(Cache c: administracao.getcachesReportadas()){
            novo.add("Nome: "+ c.getNome() +"\nLatitude: " + c.getLatitude() + "\nLongitude: " + c.getLongitude());
        }
        return novo;
    }
    public void eliminarCacheAbusadora(String nomeCache, double latitude, double longitude, boolean isAbusadora) throws AdminNaoLogadoException, 
    CacheNaoExisteException, CoordenadasInvalidasException{
        if(loggedAdmin.equals("")) throw new AdminNaoLogadoException();
        
        Cache c = caches.getCache(nomeCache);
        if(c==null) throw new CacheNaoExisteException();
        if((!c.getCoordenadas().equals(new Point2D.Double(latitude,longitude)))) throw new CoordenadasInvalidasException();
        
        administracao.cacheAbusadoraResolvida(c);
        if(isAbusadora == true){
            utilizadores.eliminarCacheAbusadora(latitude,longitude);    /* apaga os dados em todos os users */
            caches.removerCacheAbusadora(c);
        }
    }
    
    public void registarDescobertaCacheDeUmEvento(String nomeEvento, String tipoCache,String nomeCache, ArrayList<Point2D.Double> pontos, String resposta, 
    ArrayList<String> objetosRetirados, ArrayList<String> objetosInseridos) 
    throws EventoNaoExisteException, EventoTerminadoException, UserNaoLogadoException, UserNaoExisteException, CacheNaoExisteException, 
    NaoExisteTempoDesseTipoCacheException, EstasAtrasadoException, CacheJaExisteException, TipoDeCacheErradoException,NrPtsDiferenteException,
    PontosErradosException,RespostaErradaException, ObjetoNaoExisteException{
        if(loggedUser.equals("")) throw new UserNaoLogadoException();
        Evento aux = this.eventos.get(nomeEvento);
        if(aux == null){ throw new EventoNaoExisteException(); }
        Cache c = aux.getCache(nomeCache);Class c1;
        if(c==null) throw new CacheNaoExisteException();
        
        switch(tipoCache){
            case "MultiCache":
                c1 = c.getClass();
                if(!c1.getSimpleName().equals("MultiCache")) throw new TipoDeCacheErradoException(); /* verifica se é uma multicache */
                else{
                    ((MultiCache)c).validarPontos(pontos);/* verifica se o pontos estão certos */
                    aux.registarDescoberta(loggedUser,nomeCache);
                    return;
                }
            case "CacheMisterio":
                c1 = c.getClass();
                if(!c1.getSimpleName().equals("CacheMisterio")) throw new TipoDeCacheErradoException();  /* verifica se é uma Cache Misterio*/
                else{
                    if(((CacheMisterio)c).resolverMisterio(resposta) == false) throw new RespostaErradaException();    /* verifica se o pontos estão certos */
                    aux.registarDescoberta(loggedUser,nomeCache);
                    return;
                }
            case "CacheTradicional":
                c1 = c.getClass();
                if(!c1.getSimpleName().equals("CacheTradicional")) throw new TipoDeCacheErradoException(); /* verifica se é uma Cache Tradicional */
                else{
                    ((CacheTradicional)c).setObjetos(objetosInseridos);
                    ((CacheTradicional)c).tirarObjetos(objetosRetirados);
                    aux.registarDescoberta(loggedUser,nomeCache);
                    return;
                }
            case "Virtual":
                c1 = c.getClass();
                if(!c1.getSimpleName().equals("Virtual")) throw new TipoDeCacheErradoException();  /* verifica se é uma Cache Virtual */
                else{
                    if(((Virtual)c).verificaProva(resposta) == false) throw new RespostaErradaException(); /* verifica se o pontos estão certos */
                    aux.registarDescoberta(loggedUser,nomeCache);
                    return;
                }
            case "MicroCache":
                c1 = c.getClass();
                if(!c1.getSimpleName().equals("MicroCache")) throw new TipoDeCacheErradoException(); /* verifica se é uma Micro Cache */
                else{
                    aux.registarDescoberta(loggedUser,nomeCache);
                    return;
                }
        }
    }
    
    public void criarEvento(String nome, double tempoMedioEncontrarCache, int nrUsersMax, int condicoesMeteo, Point2D.Double localizacaoEvento, GregorianCalendar datalimiteInscricao, 
    GregorianCalendar dataInicio, TreeMap <String, Cache> caches) throws AdminNaoLogadoException, EventoJaRegistadoException{
        if(loggedAdmin.equals("")) throw new AdminNaoLogadoException();
        Evento aux = this.eventos.get(nome);
        if(aux!= null){throw new EventoJaRegistadoException();} 
        else{
            TreeMap <String, Cache> novasCaches = new TreeMap <String, Cache>();
            for(String p : caches.keySet()){
                novasCaches.put(p, (caches.get(p)).clone());
            }
            
            Evento e = new Evento(nome, tempoMedioEncontrarCache, nrUsersMax, condicoesMeteo, (Point2D.Double)localizacaoEvento.clone(), new GregorianCalendar(datalimiteInscricao.get(Calendar.YEAR),
            datalimiteInscricao.get(Calendar.MONTH),datalimiteInscricao.get(Calendar.DAY_OF_MONTH)), new GregorianCalendar(dataInicio.get(Calendar.YEAR),
            dataInicio.get(Calendar.MONTH),dataInicio.get(Calendar.DAY_OF_MONTH)), novasCaches);
            this.eventos.put(e.getNome(),e);
        }                        
    }
    public String finalizarEvento(String nome) throws AdminNaoLogadoException, EventoNaoComecouException, EventoNaoExisteException{
        if(loggedAdmin.equals("")) throw new AdminNaoLogadoException();
        Evento aux = this.eventos.get(nome);
        if(aux == null){throw new EventoNaoExisteException();}
        else{
            aux.finalizarEvento();
        }
        if(aux.getterminado()){
            return aux.determinarVencedor();
        }
        else{
            return "O evento não foi terminado.\n";
        }
    }
        
    
    /* metodos para ambos */
    /* Faz login caso ainda ninguem esteja logado */
    /* retorna 1 caso seja user, 2 caso seja admin, 0 erro */
    public int logIn(String email, String password) throws UserJaLogadoException, AdminJaLogadoException{
        if(!(loggedUser.equals(""))) throw new UserJaLogadoException(loggedUser);
        if(!(loggedAdmin.equals(""))) throw new AdminJaLogadoException(loggedAdmin);
        else{
            /* User */
            if(utilizadores.existeUser(email,password) == true){
                loggedUser = email;
                return 1;
            }
            /* Admin */
            if(administracao.existeAdmin(email,password) == true){
                loggedAdmin = email;
                return 2;
            }
            return 0;
        }
    }
    public void logOut() throws NinguemLogadoException{
        if(loggedUser.equals("") && loggedAdmin.equals("")){
            throw new NinguemLogadoException();
        }
        else{
            loggedUser = "";
            loggedAdmin = "";
        }
    }
    public void criarCache(Cache c) throws NinguemLogadoException, UserNaoLogadoException, CacheJaExisteException, UserNaoExisteException{
        if(loggedUser.equals("") && loggedAdmin.equals("")) throw new NinguemLogadoException();
        if(!(loggedUser.equals(""))) c.setCriador(loggedUser);
        else c.setCriador("Administração");
        
        Cache c1 = caches.addCache(c);
        if(!(loggedUser.equals(""))){
            utilizadores.criarCache(loggedUser,c1);
        }
    }
    public void removerCache(String nomeCache) throws CacheNaoExisteException, NaoECriadorException{
        Cache c = caches.getCache(nomeCache);
        if(c==null) throw new CacheNaoExisteException();
        
        if(!(loggedAdmin.equals(""))){
            caches.removerCache(c);
        }
        else if(!(loggedUser.equals(""))){
            if(c.getCriador().equals(loggedUser) == true){
                caches.removerCache(c);
            }
            else{
                throw new NaoECriadorException();
            }
        }
    }
    public ArrayList<String> verEventos(){
        ArrayList<String> eventos = new ArrayList<String>();
        for(String nome : this.eventos.keySet()){
            eventos.add((this.eventos.get(nome)).toString());
        }
        return eventos;
    }
    
    /* outros metodos */
    public GeoCaching clone(){
        return new GeoCaching(this);
    }
    public String toString(){
        StringBuilder sb = new StringBuilder();

        if ((loggedUser==null)&&(loggedAdmin==null))
            sb.append("\nNinguém está logado\n");
        else if(loggedUser!=null && loggedAdmin==null){   
            sb.append("\nEmail da pessoa que está logado é: " + loggedUser+"\nNúmero de utilizadores: "+ utilizadores.size() 
        + "\nNúmero de Caches: " +caches.size()+ "\nNúmero de Administradores: " +administracao.size()+"\n");
            }
            else if(loggedAdmin!=null && loggedUser==null){
            sb.append("\nEmail da pessoa que está logado é: " + loggedAdmin+"\nNúmero de utilizadores: "+ utilizadores.size() 
        + "\nNúmero de Caches: " +caches.size()+ "\nNúmero de Administradores: " +administracao.size()+"\n");
            }

        return sb.toString();

    }
    public boolean equals(Object umGeoCaching){
        if(this == umGeoCaching){
            return true;
        }
        if((umGeoCaching == null) || (this.getClass() != umGeoCaching.getClass())){
            return false;
        }

        GeoCaching geo = (GeoCaching) umGeoCaching;
        
        return(this.caches.equals(geo.gettodasAsCaches()) &&this.administracao.equals(geo.getadministracao()) && (this.loggedUser.equals(geo.getloggedUser())) && (this.loggedAdmin.equals(geo.getloggedAdmin())) && 
            (this.utilizadores.equals(geo.getutilizadores())));
    }
}
