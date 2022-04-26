import java.util.TreeSet;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Random;

public class GeoCachingPOO
{
   private static User logged = null;
    
   private static boolean login(UsersGroup utilizadores){
        String nome = new String();
        String pass = new String();
        Scanner input = new Scanner(System.in);
        
        System.out.println("Insira o seu e-mail:");
        nome = input.next();
        System.out.println("Introduza a sua palavra-passe:");
        pass = input.next();
        
        logged = utilizadores.get(nome);
        if(logged != null){
            if(logged.getPassword().equals(pass) == true){
                System.out.println("Bem-vindo " + logged.getNome()+" .");
                return true;
            }
            else{
                System.out.println("Palavra-passe incorreta.");
                logged = null;
                return true;
            }
        }
        else{
            System.out.println("E-mail incorreto.");
            return false;
        }
    }
    
    
    private static boolean registerUser(UsersGroup utilizadores){
        User newUser;
        String email = new String(), password = new String(), morada = new String(), nome = new String();
        char genero='N';
        GregorianCalendar dataNascimento;
        int gen,erro = 1, ano=0, mes=0 ,dia=0;
        Scanner input = new Scanner(System.in);
        
        while(erro ==1){
            erro = 0;
            System.out.println("Insira o seu e-mail:");
            email = input.next();
            System.out.println("Introduza a sua palavra-passe:");
            password = input.next();
            System.out.println("Introduza a sua morada:");
            morada = input.next();
            System.out.println("Introduza o seu nome:");
            nome = input.next();
            System.out.println("Genero: '1' -> Masculino '2' -> Feminino");
            gen = input.nextInt();
            if(gen == 1) genero = 'M';
            if(gen == 2) genero = 'F';
            else erro = 1;
            System.out.println("Introduza o seu ano de nascimento:");
            ano = input.nextInt();
            System.out.println("Introduza o seu mes de nascimento:");
            mes = input.nextInt();
            System.out.println("Introduza o seu dia de nascimento:");
            dia = input.nextInt();
            if(ano < 1800 || mes < 1 || mes > 12 || dia < 1 || dia > 31) erro = 1;
        }
        dataNascimento =  new GregorianCalendar(ano,mes,dia);
        newUser = new User(email, password, nome, genero, morada, 0, dataNascimento);
        return(utilizadores.insertUser(newUser));
        
    }
    
    private static boolean createCache(TreeSet <Cache> caches){
        double latitude = 0,longitude = 0;
        String informacoesAux = new String(), nome = new String();
        int erro = 1, dificuldade = 0, tipoCache = 0, nrPoints = 0;
        Cache novaCache = null;
        boolean adicionado;
        Scanner input = new Scanner(System.in);
        
        while(erro==1){
            erro = 0;
            System.out.println("Diga qual o tipo de Cache:\n");
            System.out.println("1 - MultiCache\n");
            System.out.println("2 - Cache Mistério\n");
            System.out.println("3 - Cache Evento\n");
            System.out.println("4 - Virtual\n");
            System.out.println("5 - MicroCache\n");
            tipoCache = input.nextInt();
            if (tipoCache == 1){
                System.out.println("Insira o número de pontos: ");
                nrPoints = input.nextInt();
            }
            System.out.println("Insira a latitude: ");
            latitude = input.nextDouble();
            System.out.println("Insira a longitude: ");
            longitude = input.nextDouble();
            System.out.println("Informações auxiliares:");
            informacoesAux = input.next();
            System.out.println("Nome da cache:");
            nome = input.next();
            System.out.println("Introduza a dificuldade (1-10):");
            dificuldade = input.nextInt();
            if(dificuldade <1 || dificuldade >10 || tipoCache < 1 || tipoCache > 5) erro = 1;
        }
        for(Cache c: caches){   /* Verificar se a cache com estas coords ja não existe ou já existe com esse nome */
            if(c.getLatitude() == latitude && c.getLongitude() == longitude || c.getNome().equals(nome)) return false;
        }
        if(tipoCache == 1){
            novaCache = new MultiCache(latitude,longitude,informacoesAux,nome,dificuldade,nrPoints);
        }
        else if(tipoCache == 2){
            novaCache = new CacheMisterio(latitude,longitude,informacoesAux,nome,dificuldade);
        }
        else if(tipoCache == 3){
            novaCache = new CacheEvento(latitude,longitude,informacoesAux,nome,dificuldade);
        }
        else if(tipoCache == 4){
            novaCache = new Virtual(latitude,longitude,informacoesAux,nome,dificuldade);
        }
        else if(tipoCache == 5){
            novaCache = new MicroCache(latitude,longitude,informacoesAux,nome,dificuldade);
        }
        adicionado =  caches.add(novaCache);
        
        if(adicionado){
            logged.addRegisto(0,new GregorianCalendar(), novaCache, true, false);
        }
        return(adicionado);
    }
    
    private static void encontreiCache(TreeSet <Cache> treeCaches){
        String nome = new String();
        Random rand = new Random();
        Scanner input = new Scanner(System.in);
        int condMeteo =  rand.nextInt(10) + 1;  /* A condicao meteorologica varia entre 1 e 10 */
        System.out.println("Nome da cache que encontrou:");
        nome = input.next();
        System.out.println("Condição metereológica de hoje: " + condMeteo + ".");
        
        for(Cache c: treeCaches){
            if(c.getNome().equals(nome) == true){
                logged.addRegisto(condMeteo, new GregorianCalendar(), c, false, true);
                return;
            }
        }
        
        System.out.println("A cache " + nome + " não existe.");
        
    }
    
    private static void removerEncontroDeCache(TreeSet <Cache> treeCaches){
        Scanner input = new Scanner(System.in);
        String nome =  new String();
        System.out.println("Nome da cache que pretende apagar o registo: ");
        nome = input.next();
        
        
        for(Cache c: treeCaches){
            if(c.getNome().equals(nome) == true){
                logged.eliminaDescoberta(c);
                return;
            }
        }
        
    }
    
    private static void editProfile(User user){
        int escolha = 1, gen=1, anoNovo = 0, mesNovo = 0, diaNovo = 0;
        String novoNome = new String(), mailNovo = new String(), passNova = new String(), moradaNova = new String();
        GregorianCalendar dataDeNascimentoNova;
        Scanner input = new Scanner(System.in);
        while(escolha != 0){
            System.out.println("1. Mudar nome " +"\n"+ "2. Mudar Genero" +"\n"+ "3. Mudar email" 
                +"\n"+ "4. Mudar password" +"\n"+ "5. Mudar morada" +"\n"+ "6. Mudar data de nascimento");
                
            escolha = input.nextInt();
            switch(escolha){
                case 1:
                    System.out.println("Indique novo nome de utilizador:");
                    novoNome = input.next();
                    user.setNome(novoNome);
                break;
                case 2:
                    System.out.println("Novo Genero: '1' -> Masculino '2' -> Feminino");
                    gen = input.nextInt();
                    if(gen == 1) {
                        user.setGenero('M');
                    }
                    if(gen == 2){
                        user.setGenero('F');
                    }
                break;
                case 3:
                    System.out.println("Indique novo email:");
                    mailNovo = input.next();
                    user.setEmail(mailNovo);
                    break;
                case 4:
                    System.out.println("Indique nova password:");
                    passNova = input.next();
                    user.setPassword(passNova);
                break;
                case 5:
                    System.out.println("Indique nova morada:");
                    moradaNova = input.next();
                    user.setMorada(moradaNova);
                break;
                case 6:
                    System.out.println("Introduza o seu ano de nascimento:");
                    anoNovo = input.nextInt();
                    System.out.println("Introduza o seu mes de nascimento:");
                    mesNovo = input.nextInt();
                    System.out.println("Introduza o seu dia de nascimento:");
                    diaNovo = input.nextInt();
                    if(anoNovo < 1800 || mesNovo < 1 || mesNovo > 12 || diaNovo < 1 || diaNovo > 31) {
                        System.out.println("Erro na introduçao da data");   
                        escolha = 0;
                    }
                    else {
                        dataDeNascimentoNova = new GregorianCalendar(anoNovo,mesNovo,diaNovo);
                        user.setDataDeNascimento(dataDeNascimentoNova);
                    }
                break;
            }
        }
    }
    
    private static void adicionarUtilizadorAmigos(UsersGroup users){/* Sabendo que existe esse utilizador */
        String user = new String();
        System.out.println("Indique o nome do seu novo amigo:");
        user =  intput.next();
        if(users.existeuser(user) == false){
            System.out.println("Esse utilizador nao existe:");
            return;
        };
        if(contains(user).logged.getAmigos() == true){
            System.out.println("O utilizador " + user.getNome()+ " ja é seu amigo");
            return;
        }
        else{
            this.logged.setAmigo(user);
            System.out.println("O utilizador " + user.getNome()+ " é agora seu amigo");
        }
    }
    
    private static void testes(UsersGroup users, TreeSet <Cache> treeCaches){
        User novo = new User();
        Cache novaCache;
        novo.setEmail("alex");novo.setPassword("pass");users.insertUser(novo);
        novo.setEmail("rafa");novo.setPassword("pass");users.insertUser(novo);
        novo.setEmail("kiko");novo.setPassword("pass");users.insertUser(novo);
        
    }
    
    public static void main (String args[]){
        
        UsersGroup users = new UsersGroup();                                 /* Utilizadores */
        TreeSet <Cache> treeCaches = new TreeSet <Cache>();                  /* Caches */
        boolean isLogged = false, byebye = false;
        Scanner input = new Scanner(System.in);
        int choice = 0;
        testes(users,treeCaches);
        
        while(byebye == false && isLogged == false){
            
            System.out.println("Entrar -> 1\nRegistar -> 2\nSair -> 0");
            choice = input.nextInt();
            switch(choice){
                case 1:
                    isLogged = login(users);
                    break;
                case 2: 
                    if(registerUser(users) == true){
                        System.out.println("Registado com sucesso. Efectue o login.");
                    }
                    else{
                        System.out.println("Utilizador já registado com esse e-mail.");
                    }
                    
                    break;
                default:
                    byebye = true;
                    break; 
            }
        }
        
        if(isLogged == true){
            
            /* Menu User */
           while(choice != 0){
               System.out.println("1. Criar uma cache;" +"\n"+ "2. Invalidar uma cache;" +"\n"+"3. Registar descoberta de uma cache;" 
               +"\n"+"4. Remover uma atividade de descoberta;" +"\n"+"5. Ver as suas últimas 10 atividades (ver detalhadamente uma atividade);" +"\n"+"6. Ver lista de amigos;" +"\n"
               +"7. Ver 10 últimas atividades dos amigos (ver detalhadamente uma atividade);" +"\n"+"8. Ver o perfil;" +"\n"+"9. Editar o perfil;" +"\n"+
               "10. Ver estatística mensalmente ou anualmente;" +"\n"+ "11. Adicionar um utizilizador aos amigos" +"\n" + "0. Sair;");
                
                choice = input.nextInt();
                switch(choice){
                    case 1: /* Criar cache */
                        if(createCache(treeCaches) == true)
                            System.out.println("Criada com sucesso.");
                        else
                            System.out.println("Cache já existente.");
                        break;
                    case 2: /* Invalidar cache */
                        String nomeRemover;
                        Cache cacheRemover = null;
                        
                        System.out.println("Insira o nome da cache que pretende remover: ");
                        nomeRemover = input.next();
                        
                        for(Cache c: treeCaches){
                            if(c.getNome().equals(nomeRemover) == true) cacheRemover = c;
                        }
                        
                        if(cacheRemover == null){
                            System.out.println("Cache não existe.");
                        }
                        else{
                            for(User u: users.getUsers()){ /* Remover os dados dessa cache nos users */
                                u.removerCache(cacheRemover);
                            }
                            
                            treeCaches.remove(cacheRemover);
                            System.out.println("Cache removida com sucesso.");
                        }
                        break;
                    case 3: /* Registar descoberta de cache */
                        encontreiCache(treeCaches);
                        break;
                    case 4:
                        
                        break;
                    case 5: /* Ver nossas ultimas 10 atividades */
                        ArrayList <Registo> ultimasAtividades = logged.getRegistos();
                        for(Registo r: ultimasAtividades){
                            System.out.println(r.toString());
                        }
                        break;
                    case 6: /* Ver amigos */
                        TreeSet<User> listaAmigos = logged.getAmigos();
                        for(User u: listaAmigos)
                            u.toString();
                        break;
                    case 7:/*  Ver 10 últimas atividades dos amigos (ver detalhadamente uma atividade) */
                    
                        break;
                    case 8:/* Ver o perfil */
                        System.out.println(logged.toString());
                        break;
                    case 9: /* Editar Perfil */
                        editProfile(logged);
                        break;
                    case 10:/* Ver estatisticas mensalmente ou anualmente (Diferentes tipos de cache) */
                    
                        break;
                    case 11:/* Adicionar um utilizador aos amigos*/
                        adicionarUtilizadorAmigos(users);
                        break;
                   
                }
            }
            
        }
        
        else return;
            
    }
    
}
