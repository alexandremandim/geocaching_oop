import java.util.TreeMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Calendar;
import GeoCachingExceptions.*;
import java.io.*;

public class UsersGroup implements Serializable
{
    private TreeMap <String, User> treeUsers;

    /* Construtores */
    public UsersGroup()
    {
        treeUsers = new TreeMap <String, User>();
    }
    public UsersGroup(TreeMap<String,User> usersList)
    {
        treeUsers = new TreeMap <String, User>();
        for(User u: usersList.values()){
            treeUsers.put(u.getEmail(), u.clone());
        }
    }
    public UsersGroup(UsersGroup ug){
        this.treeUsers = ug.getUsers();
    }

    /* gets */
    public TreeMap<String,User> getUsers()
    {
        TreeMap <String, User> res = new TreeMap <String, User>();
        
        for(User u: this.treeUsers.values()){
            treeUsers.put(u.getEmail(), u.clone());
        }
        return res;
    }
    public ArrayList<String> getUsersString(){
          ArrayList<String> novo = new ArrayList<String>();
          for(User u: this.treeUsers.values()){
              novo.add(u.getEmail());
          }
          return novo;
    }
    
    public void insertUser(User u, String pass) throws UserJaExisteException, PassIncorretaException{
        if(treeUsers.containsKey(u.getEmail()) == true) throw new UserJaExisteException(u.getEmail());
        User inserir =  u.clone();
        inserir.mudarPass("",pass);
        treeUsers.put(u.getEmail(),inserir);
        
    }
    /* Devolve o utilizador com o mail passado como parametro caso exista, se nao retorna null */
    public User get(String mail) throws UserNaoExisteException{
        if(!treeUsers.containsKey(mail)) throw new UserNaoExisteException(mail);
        return treeUsers.get(mail);
    }
    /* Dado um nome dum utilizador ve se existe */
    public boolean contains(String nomeUtilizador){
        for(User u : treeUsers.values()){
            if(u.getNome().equals(nomeUtilizador)){
                return true;
            }
        }
        return false;
    }
    
    public boolean containsMail(String mailUser){
        return treeUsers.containsKey(mailUser);
    }
    public boolean existeUser(String email, String pass){
        if(treeUsers.containsKey(email) == false) return false;
        return treeUsers.get(email).validarPass(pass);
    }
    public ArrayList<Registo> ultimas10Atividades(String user) throws SemRegistosException,UserNaoExisteException{
        StringBuilder sb = new StringBuilder();
        if(treeUsers.containsKey(user) == false) throw new UserNaoExisteException(user);
        
        ArrayList<Registo> ultimas10 = treeUsers.get(user).get10Registos();
        
        return ultimas10;
    }
    public ArrayList<String> amigos(String user) throws UserNaoExisteException{
        ArrayList<String> amigos = new ArrayList<String>();
        
        User res = treeUsers.get(user);
        if(res == null) throw new UserNaoExisteException(user);
        else{
            amigos.addAll(res.getAmigos());
            return amigos;
        }
    }
    public void removerEncontroCacheUser(String user, Cache c) throws UserNaoExisteException{
        if(treeUsers.containsKey(user) == false){throw new UserNaoExisteException(user);}
        treeUsers.get(user).eliminaDescoberta(c);
    }
    /* Retorna número de utilizadores total */
    public int size(){
        return treeUsers.size();
    }
    /* Editar um user, retorna o novo caso tenha alterado com sucesso, caso contrario retorna null */
    public void editUser(String email, String password, String nome, char genero, String morada, GregorianCalendar dataNascimento) throws UserNaoExisteException, PassIncorretaException{
        User u = treeUsers.get(email);
        if(u == null) {throw new UserNaoExisteException(email);}
        u.editarPerfil(password, nome, genero, morada, dataNascimento);
    }
    /* Muda pass */
    public void mudarPass(String passAntiga, String passNova, String email) throws UserNaoExisteException, PassIncorretaException{
        User u = treeUsers.get(email);
        if(u == null) {throw new UserNaoExisteException(email);}
        u.mudarPass(passAntiga,passNova);
    }
    /* registar descoberta de uma cache relativamente a um user */
    public void registarDescoberta(int condMeteo, Cache c, String u) throws UserNaoExisteException, CacheJaExisteException{
        User eu = treeUsers.get(u);
        if(eu== null) {throw new UserNaoExisteException(u);}
        eu.registarDescobertaCache(condMeteo,c);
    }
    public void registarEvento(String u, Evento e) throws UserNaoExisteException, EventoJaRegistadoException{
        User user = treeUsers.get(u);
        if(user== null) {throw new UserNaoExisteException(u);}
        user.registarEvento(e);
    }
    public void eliminarDescoberta(Cache c, String u) throws UserNaoExisteException{
        if(!treeUsers.containsKey(u)) {throw new UserNaoExisteException(u);}
        treeUsers.get(u).eliminaDescoberta(c);
    }
    /* ver perfil de um user */
    public String verUser(String u) throws UserNaoExisteException{
        if(treeUsers.containsKey(u) == false) throw new UserNaoExisteException(u);
        return treeUsers.get(u).toString();
    }
    public void adicionarAmigo(String user, String emailAmigo) throws UserNaoExisteException, AmigoNaoExisteException, AdicionarProprioException, AmigoJaExisteException{
        if(treeUsers.containsKey(user) == false) throw new UserNaoExisteException(user);
        if(treeUsers.containsKey(emailAmigo) == false) throw new AmigoNaoExisteException(emailAmigo);

        treeUsers.get(user).adicionarAmigo(emailAmigo);
    }
    public void removerAmigo(String user, String emailAmigo) throws UserNaoExisteException, AmigoNaoExisteException{
         if(treeUsers.containsKey(user) == false) throw new UserNaoExisteException(user);
         if(treeUsers.containsKey(emailAmigo) == false) throw new AmigoNaoExisteException(emailAmigo);
         
         treeUsers.get(user).removerAmigo(emailAmigo);
    }
    public ArrayList<Estatistica> estatisticaUser(String user) throws UserNaoExisteException{
        ArrayList<Estatistica> res = new ArrayList<Estatistica>();
        if(treeUsers.containsKey(user) == false) throw new UserNaoExisteException(user);
        
        return(treeUsers.get(user).getEstatisticas());
    }
    public void criarCache(String user, Cache c) throws UserNaoExisteException{
        if(treeUsers.containsKey(user) == false) throw new UserNaoExisteException(user);
        
        treeUsers.get(user).criarCache(c);
    }
    public void eliminarCacheAbusadora(double latitude, double longitude){
        for(User u: treeUsers.values()){
            u.eliminarCacheAbusadora(latitude, longitude);
        }
    }
    /*Equals*/
    public boolean equals(Object umUG){
        if(this == umUG){
            return true;
        }
        if((umUG == null) || (this.getClass() != umUG.getClass())){
            return false;
        }
        else{
            UsersGroup ug = (UsersGroup) umUG;
            for(User u: treeUsers.values()){
                if(!(ug.treeUsers.get(u.getEmail()).equals(u))) return false;
            }
            return true;
        }
    }
    /*Clone*/
    public UsersGroup clone(){
        return new UsersGroup(this);
    }
    /* toString */
    public String toString(){
        StringBuilder sb = new StringBuilder();
        for(User u: treeUsers.values()){
            sb.append(u.toString());
        }
        return sb.toString();
    }
}
