import java.util.Scanner;
import java.util.TreeSet;
import java.util.TreeMap;
import java.awt.geom.Point2D;
import java.util.GregorianCalendar;
import java.util.ArrayList;
import GeoCachingExceptions.*;
import java.util.InputMismatchException;
import java.io.*;
import java.util.Random;

public class GeoCachingTeste1 implements Serializable
{
    public GeoCachingTeste1(){}
    
    private void iniciarDados(GeoCaching app){
       try{
           app.registarUser("alex","pass", "Alexandre", 'M', "ruaA",1995,12,20);
           app.registarUser("kiko","pass", "Francisco", 'M', "ruaA",1995,6,22);
           app.registarUser("rafa","pass", "Rafael", 'M', "ruaA",1995,1,23);
       }
       catch(DadosInvalidosException e){
           System.out.println("Dados inválidos.\n");
           return;
       }
       catch(AdminJaExisteException e){
           System.out.println("Admin já existe.\n");
           return;
       }
       catch(UserJaExisteException e){
           System.out.println("User já existe.\n");
           return;
       }
       catch(PassIncorretaException e){}
    }
    
    private int menuInicial(GeoCaching app){ /* retorna 0 caso queira sair, 1 se logou um user, 2 se logou um admin */
        int escolha = -1;
        Scanner input = new Scanner(System.in), inputInt = new Scanner(System.in), inputString = new Scanner(System.in);
        String email, pass, nome, morada;
        int gen, dia, mes, ano, logado;
        char genero='X';
        
        while(escolha != 0){
            System.out.println("Entrar -> 1\nRegistar -> 2\nSair -> 0");
            try{
                escolha = inputInt.nextInt();
            }catch(InputMismatchException e){
                System.out.println("Indique um inteiro!\n");
                escolha=3;
                inputInt.nextLine();
            }
            switch(escolha){
                case 1: /* login */
                        System.out.println("\nInsira o seu e-email:");
                        email = input.next();
                        System.out.println("\nIntroduza a sua palavra-passe:");
                        pass = input.next();
                        
                        try{
                            logado = app.logIn(email,pass);
                        }
                        catch (UserJaLogadoException e){
                            System.out.println("User já logado: " + e.getMessage());
                            return 0;
                        }
                        catch (AdminJaLogadoException e){
                            System.out.println("Admin já logado: " + e.getMessage());
                            return 0;
                        }
                        
                        if(logado!=0){
                            System.out.println("\nEntrou com sucesso");
                            return logado;
                        }
                        else System.out.println("\nDados incorretos!");
                    break;
                case 2: /* registar user */
                    System.out.println("Insira o seu e-email:");
                    email = input.next();
                    System.out.println("Introduza o seu nome:");
                    nome = inputString.nextLine();
                    System.out.println("Introduza a sua palavra-passe:");
                    pass = input.next();
                    System.out.println("Introduza a sua morada:");
                    morada = inputString.nextLine();
                    System.out.println("Genero: '1' -> Masculino '2' -> Feminino");
                    try{
                        gen = inputInt.nextInt(); 
                    }catch(InputMismatchException e){
                        System.out.println("Indique um inteiro para género");
                        break;
                    }
                    if(gen == 1) genero = 'M';
                    else if(gen == 2) genero = 'F'; 
                    System.out.println("Introduza o seu ano de nascimento:");
                    try{
                        ano = inputInt.nextInt(); 
                    }catch(InputMismatchException e){
                        System.out.println("Indique um inteiro para ano");
                        break;
                    }
                    System.out.println("Introduza o seu mes de nascimento:");
                    try{
                        mes = inputInt.nextInt(); 
                    }catch(InputMismatchException e){
                        System.out.println("Indique um inteiro para mes");
                        break;
                    };
                    System.out.println("Introduza o seu dia de nascimento:");
                    try{
                        dia = inputInt.nextInt(); 
                    }catch(InputMismatchException e){
                        System.out.println("Indique um inteiro para dia");
                        break;
                    }
                   
                    try{
                       app.registarUser(email,pass,nome,genero,morada,ano,mes,dia);
                    }
                    catch (DadosInvalidosException e){
                        System.out.println(e.getMessage());
                        break;
                    }
                    catch (AdminJaExisteException e){
                        System.out.println(e.getMessage());
                        break;
                    }
                    catch (UserJaExisteException e){
                        System.out.println(e.getMessage());
                        break;
                    }
                    catch(PassIncorretaException e){
                        System.out.println(e.getMessage());
                        break;
                    }
                    break;
                case 3:
                    break;
            }
        }
        return 0;
    }
    
    private void criarCache(GeoCaching app){
        Scanner input = new Scanner(System.in), inputInt = new Scanner(System.in), inputString = new Scanner(System.in), inputDouble = new Scanner(System.in);
        String informacoesEsconderijo, nome;
        int dificuldade, tipoCache;
        double latitude, longitude;
        System.out.println("Indique latitude:");
        try{
            latitude = inputDouble.nextDouble();
        }
        catch(InputMismatchException e){
            System.out.println("Indique um double para latitude!\n");
            return;
        }
        System.out.println("Indique longitude:");
        try{
            longitude = inputDouble.nextDouble();
        }
        catch(InputMismatchException e){
            System.out.println("Indique um Double para longitude");
            return;
        }
        System.out.println("Indique informações do esconderijo:");
        informacoesEsconderijo = inputString.nextLine();
        System.out.println("Indique qual o nome:");
        nome = inputString.nextLine();
        System.out.println("Indique a dificuldade(1-10):");
        try{
            dificuldade = inputInt.nextInt();
            if(dificuldade <0 || dificuldade > 10){
                System.out.println("Dificuldade tem que ser entre 1-10");
                return;
            }
        }catch(InputMismatchException e){
            System.out.println("Indique um int para Dificuldade");
            return;
        }
        System.out.println("Diga qual o tipo de Cache:");
        System.out.println("1. MultiCache");
        System.out.println("2. Cache Mistério");
        System.out.println("3. Cache Evento");
        System.out.println("4. Virtual");
        System.out.println("5. MicroCache");
        System.out.println("6. Cache tradicional");
        System.out.println("0. Sair\n");
        try{
            tipoCache = inputInt.nextInt();
        }catch(InputMismatchException e){
            System.out.println("Indique um inteiro!\n");
            tipoCache=7;
            inputInt.nextLine();
        }
        switch(tipoCache){
            case 1:/* Escolheu Multi Cache*/
                int nrPontosAnteriores, i;
                double latitude2, longitude2;
                System.out.println("Indique o número de pontos(caches) que faltam:");
                nrPontosAnteriores = inputInt.nextInt();
                ArrayList<Point2D.Double> cjPontos = new ArrayList<Point2D.Double>();
                
                for(i=0;i<nrPontosAnteriores;i++){
                    System.out.println("Indique latitude:");
                    try{
                        latitude2 = inputDouble.nextDouble();
                    }catch(InputMismatchException e){
                        System.out.println("Indique um Double!\n");
                        return;
                    }
                    System.out.println("Indique longitude:");
                    try{
                        longitude2 = inputDouble.nextDouble();
                    }catch(InputMismatchException e){
                        System.out.println("Indique um Double!\n");
                        return;
                    }
                    cjPontos.add(new Point2D.Double(latitude2,longitude2));
                }
                MultiCache mc = new MultiCache(latitude,longitude,informacoesEsconderijo,nome,dificuldade,"",cjPontos);
                try{
                    app.criarCache(mc);
                }
                catch(NinguemLogadoException e){
                    System.out.println("Ninguém logado.\n");
                    return;
                }
                catch(UserNaoLogadoException e){
                    System.out.println("User não logado.\n");
                    return;
                }
                catch(CacheJaExisteException e){
                    System.out.println("A Cache já existe.\n");
                    return;
                }
                catch(UserNaoExisteException e){
                    System.out.println("O User não existe.\n");
                    return;
                }
                System.out.println("\nMultiCache criada com sucesso!\n");
                break;
            case 2: /*Escolheu Cache Misterio*/
                String puzzle, resposta;
                System.out.println("Indique o puzzle:");
                puzzle = inputString.nextLine();
                System.out.println("Indique a resposta:");
                resposta = inputString.nextLine();
                CacheMisterio cm = new CacheMisterio(latitude,longitude,informacoesEsconderijo,nome,dificuldade,"",puzzle,resposta);
                try{
                    app.criarCache(cm);
                }
                catch(NinguemLogadoException e){
                    System.out.println("Ninguém logado.\n");
                    return;
                }
                catch(UserNaoLogadoException e){
                    System.out.println("User não logado.\n");
                    return;
                }
                catch(CacheJaExisteException e){
                    System.out.println("A Cache já existe.\n");
                    return;
                }
                catch(UserNaoExisteException e){
                    System.out.println("O User não existe.\n");
                    return;
                }
                System.out.println("Cache Mistério criada com sucesso.\n");
                break; 
            case 3: /*Escolheu Cache evento*/
                int dia, mes, ano;
                System.out.println("Indique dia:");
                try{
                    dia = inputInt.nextInt();
                }catch(InputMismatchException e){
                    System.out.println("Indique um Int!\n");
                    return;
                }
                System.out.println("Indique mês:");
                try{
                    mes = inputInt.nextInt();
                }catch(InputMismatchException e){
                    System.out.println("Indique um Int!\n");
                    return;
                }
                System.out.println("Indique ano:");
                try{
                    ano = inputInt.nextInt();
                }catch(InputMismatchException e){
                    System.out.println("Indique um Int!\n");
                    return;
                }
                GregorianCalendar dataCacheEvento = new GregorianCalendar(ano,mes-1,dia);
                if(dataCacheEvento.compareTo(new GregorianCalendar()) < 0){
                    System.out.println("Essa data já passou!");
                    break;
                }
                CacheEvento ce = new CacheEvento(latitude,longitude,informacoesEsconderijo,nome,dificuldade,"",dataCacheEvento);
                try{
                    app.criarCache(ce);
                }
                catch(NinguemLogadoException e){
                    System.out.println("Ninguém logado.\n");
                    return;
                }
                catch(UserNaoLogadoException e){
                    System.out.println("User não logado.\n");
                    return;
                }
                catch(CacheJaExisteException e){
                    System.out.println("A Cache já existe.\n");
                    return;
                }
                catch(UserNaoExisteException e){
                    System.out.println("O User não existe.\n");
                    return;
                }
                System.out.println("Cache Evento criada com sucesso.\n");
                break;
            case 4:/*Escolheu Virtual*/
                String pergunta;
                String prova;
                System.out.println("Insira a pergunta:");
                pergunta = inputString.nextLine();
                System.out.println("Insira a prova:");
                prova = inputString.nextLine();
                Virtual cv = new Virtual(latitude,longitude,informacoesEsconderijo,nome,dificuldade,"",pergunta,prova);
                try{
                    app.criarCache(cv);
                }
                catch(NinguemLogadoException e){
                    System.out.println("Ninguém logado.\n");
                    return;
                }
                catch(UserNaoLogadoException e){
                    System.out.println("User não logado.\n");
                    return;
                }
                catch(CacheJaExisteException e){
                    System.out.println("A Cache já existe.\n");
                    return;
                }
                catch(UserNaoExisteException e){
                    System.out.println("O User não existe.\n");
                    return;
                }
                System.out.println("Cache Virtual criada com sucesso.\n");
                break;
            case 5:/*Escolheu MicroCache*/
                MicroCache microC = new MicroCache(latitude,longitude,informacoesEsconderijo,nome,dificuldade,"");
                try{
                    app.criarCache(microC);
                }
                catch(NinguemLogadoException e){
                    System.out.println("Ninguém logado.\n");
                    return;
                }
                catch(UserNaoLogadoException e){
                    System.out.println("User não logado.\n");
                    return;
                }
                catch(CacheJaExisteException e){
                    System.out.println("A Cache já existe.\n");
                    return;
                }
                catch(UserNaoExisteException e){
                    System.out.println("O User não existe.\n");
                    return;
                }
                System.out.println("Micro Cache criada com sucesso.\n");
                break;
            case 6:/*Escolheu Cache Tradicional*/
                int nrObjetos = 0;
                String objeto;
                System.out.println("Insira o número de objetos:");
                try{
                    nrObjetos = inputInt.nextInt();
                }catch(InputMismatchException e){
                    System.out.println("Indique um Int!\n");
                    return;
                }
                ArrayList<String> conjObjetos = new ArrayList<String>(nrObjetos);
                for(i = 0; i < nrObjetos; i++){
                    System.out.println("Insira o objeto que pretende adicionar:");
                    objeto = inputString.nextLine();
                    conjObjetos.add(objeto);
                }
                CacheTradicional ct = new CacheTradicional(latitude,longitude,informacoesEsconderijo,nome,dificuldade,"",conjObjetos);
                try{
                    app.criarCache(ct);
                }
                catch(NinguemLogadoException e){
                    System.out.println("Ninguém logado.\n");
                    return;
                }
                catch(UserNaoLogadoException e){
                    System.out.println("User não logado.\n");
                    return;
                }
                catch(CacheJaExisteException e){
                    System.out.println("A Cache já existe.\n");
                    return;
                }
                catch(UserNaoExisteException e){
                    System.out.println("O User não existe.\n");
                    return;
                }
                System.out.println("Cache Tradicional criada com sucesso.\n");
                break;
            case 7:
                break;
            case 0:
                break;
        }
        
    }
    
    private Cache criarCacheEmEvento(){
        int escolha = 1, dificuldade;
        double latitude, longitude;
        String informacoesEsconderijo, nome;
        Scanner input = new Scanner(System.in), inputInt = new Scanner(System.in), inputString = new Scanner(System.in), inputDouble = new Scanner(System.in);
        System.out.println("Caches do Evento:\n");
        System.out.println("Insira latitude:");
        try{
            latitude = inputDouble.nextDouble();
        }
        catch(InputMismatchException e){
            System.out.println("Indique um double para latitude!");
            return null;
        }
        System.out.println("Insira longitude:\n");
        try{
            longitude = inputDouble.nextDouble();
        }
        catch(InputMismatchException e){
            System.out.println("Indique um double para longitude!");
            return null;
        }
        System.out.println("Indique informações do esconderijo:");
        informacoesEsconderijo = inputString.nextLine();
        System.out.println("Indique qual o nome:");
        nome = inputString.nextLine();
        System.out.println("Indique a dificuldade(1-10):");
        try{
            dificuldade = inputInt.nextInt();
        }catch(InputMismatchException e){
            System.out.println("Indique um Double para Dificuldade");
            return null;
        }
        while(escolha!=0){
            System.out.println("Diga qual o tipo de Cache:");
            System.out.println("1. MultiCache");
            System.out.println("2. Cache Mistério");
            System.out.println("3. Virtual");
            System.out.println("4. MicroCache");
            System.out.println("5. Cache tradicional");
            System.out.println("0. Sair\n");
            try{
                escolha = inputInt.nextInt();
            }catch(InputMismatchException e){
                System.out.println("Indique um inteiro!");
                escolha=0;
                inputInt.nextLine();
            }
            switch(escolha){
                case 0:
                    break;
                case 1: /*Escolheu MultiCache*/
                    int nrPontosAnteriores, i;
                    double latitude2, longitude2;
                    System.out.println("Indique o número de pontos(caches) que faltam:");
                    nrPontosAnteriores = inputInt.nextInt();
                    ArrayList<Point2D.Double> cjPontos = new ArrayList<Point2D.Double>();
                    
                    for(i=0;i<nrPontosAnteriores;i++){
                        System.out.println("Indique latitude:");
                        try{
                            latitude2 = inputDouble.nextDouble();
                        }catch(InputMismatchException e){
                            System.out.println("Indique um Double!\n");
                            return null;
                        }
                        System.out.println("Indique longitude:");
                        try{
                            longitude2 = inputDouble.nextDouble();
                        }catch(InputMismatchException e){
                            System.out.println("Indique um Double!\n");
                            return null;
                        }
                        cjPontos.add(new Point2D.Double(latitude2,longitude2));
                    }
                    MultiCache multiC = new MultiCache(latitude,longitude,informacoesEsconderijo,nome,dificuldade,"",cjPontos);
                    System.out.println("Multi Cache criada com sucesso.\n");
                    return multiC;
                case 2: /*Escolheu Cache Misterio*/
                    String puzzle, resposta;
                    System.out.println("Indique o puzzle:");
                    puzzle = inputString.nextLine();
                    System.out.println("Indique a resposta:");
                    resposta = inputString.nextLine();
                    CacheMisterio cm = new CacheMisterio(latitude,longitude,informacoesEsconderijo,nome,dificuldade,"",puzzle,resposta);
                    System.out.println("Cache Mistério criada com sucesso.\n");
                    return cm;
                case 3:/*Escolheu Virtual*/
                    String pergunta;
                    String prova;
                    System.out.println("Insira a pergunta:");
                    pergunta = inputString.nextLine();
                    System.out.println("Insira a prova:");
                    prova = inputString.nextLine();
                    Virtual cv = new Virtual(latitude,longitude,informacoesEsconderijo,nome,dificuldade,"",pergunta,prova);
                    System.out.println("Cache Virtual criada com sucesso.\n");
                    return cv;
                case 4:/*Escolheu MicroCache*/
                    MicroCache microC = new MicroCache(latitude,longitude,informacoesEsconderijo,nome,dificuldade,"");
                    System.out.println("Micro Cache criada com sucesso.\n");
                    return microC;
                case 5:/*Escolheu Cache Tradicional*/
                    int nrObjetos = 0;
                    String objeto;
                    System.out.println("Insira o número de objetos:");
                    try{
                        nrObjetos = inputInt.nextInt();
                    }catch(InputMismatchException e){
                        System.out.println("Indique um Int!\n");
                        break;
                    }
                    ArrayList<String> conjObjetos = new ArrayList<String>(nrObjetos);
                    for(i = 0; i < nrObjetos; i++){
                        System.out.println("Insira o objeto que pretende adicionar:");
                        objeto = inputString.nextLine();
                        conjObjetos.add(objeto);
                    }
                    CacheTradicional ct = new CacheTradicional(latitude,longitude,informacoesEsconderijo,nome,dificuldade,"",conjObjetos);
                    System.out.println("Cache Tradicional criada com sucesso.\n");
                    return ct;
            }
        }
        return null;
    }
    
    
    private void removerCache(GeoCaching app){
        Scanner inputString = new Scanner(System.in);
        String nome;
        System.out.println("Insira o nome da cache: ");
        nome = inputString.nextLine();
        try{
            app.removerCache(nome);
        }
        catch(CacheNaoExisteException e){
            System.out.println("A cache não existe.\n");
            return;
        }
        catch(NaoECriadorException e){
            System.out.println("Não é o criador desta cache.\n");
            return;
        }
        System.out.println("Cache removida com sucesso.\n");
    }
    
    
    public void menuUtilizadorEvento(GeoCaching app, String nomeEvento){
        int escolha = -1;
        Scanner input = new Scanner(System.in), inputInt = new Scanner(System.in), inputString = new Scanner(System.in), inputDouble = new Scanner(System.in);
        String nome;
    
        while(escolha != 0){
            System.out.println("1 -> Registar descoberta.\n0 -> Sair");
            try{
                escolha = inputInt.nextInt();
            }catch(InputMismatchException e){
                System.out.println("Indique um inteiro!");
                escolha=0;
                inputInt.nextLine();
            }
            switch(escolha){
                case 1:
                    int escolha2 = 1;
                        System.out.println("Insira o nome da cache: ");
                        nome = inputString.nextLine();
                        
                        System.out.println("Diga qual o tipo de Cache que encontrou:");
                        System.out.println("1. MultiCache");
                        System.out.println("2. Cache Mistério");
                        System.out.println("3. Virtual");
                        System.out.println("4. MicroCache");
                        System.out.println("5. Cache tradicional");
                        System.out.println("0. Sair\n");
                        try{
                            escolha2 = inputInt.nextInt();
                        }catch(InputMismatchException e){
                            System.out.println("Indique um inteiro!\n");
                            escolha2=7;
                            inputInt.nextLine();
                        }
                        
                        switch(escolha2){
                         case 1: /* multicache */
                               int nrPontos, i;
                               double latitude2, longitude2;
                               System.out.println("Número de pontos passados antes de chegar à cache: \n");
                               try{
                                    nrPontos = inputInt.nextInt();
                               }catch(InputMismatchException e){
                                    System.out.println("Indique um inteiro!\n");
                                    break;
                               }
                               ArrayList<Point2D.Double> pontos = new ArrayList<Point2D.Double>(); 
                               for(i=0; i<nrPontos;i++){
                                    System.out.println("Latitude: \n");
                                    try{
                                        latitude2 = inputDouble.nextDouble();
                                    }catch(InputMismatchException e){
                                        System.out.println("Indique um Double!\n");
                                        break;
                                    }
                                    System.out.println("Longitude: \n");
                                    try{
                                        longitude2 = inputDouble.nextDouble();
                                    }catch(InputMismatchException e){
                                        System.out.println("Indique um Double!\n");
                                        break;
                                    }
                                    pontos.add(new Point2D.Double(latitude2,longitude2));
                               }
                               try{
                                    app.registarDescobertaCacheDeUmEvento(nomeEvento, "MultiCache", nome, null, "", null,null);
                               }
                               catch(UserNaoLogadoException e){
                                    System.out.println("User não logado.\n");
                                    break;
                               }
                               catch(CacheNaoExisteException e){
                                    System.out.println("Cache não existe.\n");
                                    break;
                               }
                               catch(TipoDeCacheErradoException e){
                                    System.out.println("Tipo de cache errado.\n");
                                    break;
                               }
                               catch(PontosErradosException e){
                                    System.out.println("Pontos errados.\n");
                                    break;
                               }
                               catch(NrPtsDiferenteException e){
                                    System.out.println("Número de pontos errados.\n");
                                    break;
                               }
                               catch(UserNaoExisteException e){
                                    System.out.println("User não existe.\n");
                                    break;
                               }
                               catch(CacheJaExisteException e){
                                    System.out.println(e.getMessage());
                                    break;
                               }catch(EventoNaoExisteException e){
                                    System.out.println("Evento não existe\n");
                                    break;
                               }catch(EventoTerminadoException e){
                                    System.out.println("Evento acabou!\n");
                                    break;
                               }catch(NaoExisteTempoDesseTipoCacheException e){
                                    System.out.println("Não existe tempo\n");
                                    break;
                               }catch(EstasAtrasadoException e){
                                    System.out.println("Atraso !");
                                    break;
                               }catch(RespostaErradaException e){
                                    System.out.println("Resposta errada !\n");
                                    break;
                               }catch(ObjetoNaoExisteException e){
                                    System.out.println("Objeto não existe\n");
                                    break;
                               }
                               System.out.println("Registado com sucesso!\n");
                               break;
                        case 2: /* cacheMisterio */
                                String resposta;
                                System.out.println("Introduza a resposta: ");
                                resposta = inputString.nextLine();
                                try{
                                    app.registarDescobertaCacheDeUmEvento(nomeEvento, "CacheMisterio", nome, null, resposta, null,null);
                                }
                                catch(UserNaoLogadoException e){
                                    System.out.println("User não logado.\n");
                                    break;
                                }
                                catch(CacheNaoExisteException e){
                                    System.out.println("Cache não existe.\n");
                                    break;
                                }
                                catch(TipoDeCacheErradoException e){
                                    System.out.println("Tipo de cache errado.\n");
                                    break;
                                }
                                catch(RespostaErradaException e){
                                    System.out.println("Resposta errada.\n");
                                    break;
                                }
                                catch(UserNaoExisteException e){
                                    System.out.println("User não existe.\n");
                                    break;
                                }
                                catch(CacheJaExisteException e){
                                    System.out.println("Cache já existe.\n");
                                    break;
                                }
                                catch(EventoNaoExisteException e){
                                    System.out.println("Evento não existe\n");
                                    break;
                                }
                                catch(EventoTerminadoException e){
                                    System.out.println("O evento foi terminado!");
                                    break;
                                }
                                catch(NaoExisteTempoDesseTipoCacheException e){
                                    System.out.println("Não existe tempo");
                                    break;
                                }
                                catch(EstasAtrasadoException e){
                                    System.out.println("Estas atrasado");
                                    break;
                                }
                                catch(NrPtsDiferenteException e){
                                    System.out.println("Numero pontos diferentes");
                                    break;
                                }
                                catch(PontosErradosException e){
                                    System.out.println("Pontos Errados");
                                    break;
                                }
                                catch(ObjetoNaoExisteException e){
                                    System.out.println("Objeto nao existe");
                                    break;
                                }
                                System.out.println("Registado com sucesso!\n");
                                break;
                        case 3: /* cacheVirtual */
                                String resposta2;
                                System.out.println("Insira a resposta: \n");
                                resposta2 = inputString.nextLine();
                                try{
                                    app.registarDescobertaCacheDeUmEvento(nomeEvento,"Virtual", nome, null, resposta2,null,null);
                                
                                }
                                catch(UserNaoLogadoException e){
                                    System.out.println("User não logado.\n");
                                    break;
                                }
                                catch(CacheNaoExisteException e){
                                    System.out.println("Cache não existe.\n");
                                    break;
                                }
                                catch(TipoDeCacheErradoException e){
                                    System.out.println("Tipo de cache errado.\n");
                                    break;
                                }
                                catch(RespostaErradaException e){
                                    System.out.println("Resposta errada.\n");
                                    break;
                                }
                                catch(UserNaoExisteException e){
                                    System.out.println("User não existe.\n");
                                    break;
                                }
                                catch(CacheJaExisteException e){
                                    System.out.println("Cache já existe.\n");
                                    break;
                                }
                                catch(EventoNaoExisteException e){
                                    System.out.println("Evento não existe\n");
                                    break;
                                }
                                catch(EventoTerminadoException e){
                                    System.out.println("O evento foi terminado!");
                                    break;
                                }
                                catch(NaoExisteTempoDesseTipoCacheException e){
                                    System.out.println("Não existe tempo");
                                    break;
                                }
                                catch(EstasAtrasadoException e){
                                    System.out.println("Estas atrasado");
                                    break;
                                }
                                catch(NrPtsDiferenteException e){
                                    System.out.println("Numero pontos diferentes");
                                    break;
                                }
                                catch(PontosErradosException e){
                                    System.out.println("Pontos Errados");
                                    break;
                                }
                                catch(ObjetoNaoExisteException e){
                                    System.out.println("Objeto nao existe");
                                    break;
                                }
                                System.out.println("Registado com sucesso!\n");
                                break;
                        case 4: /* microCache */
                                try{
                                    app.registarDescobertaCacheDeUmEvento(nomeEvento,"MicroCache",nome,null,"",null,null);
                                }
                                catch(UserNaoLogadoException e){
                                    System.out.println("User não logado.\n");
                                    break;
                                }
                                catch(CacheNaoExisteException e){
                                    System.out.println("Cache não existe.\n");
                                    break;
                                }
                                catch(TipoDeCacheErradoException e){
                                    System.out.println("Tipo de cache errado.\n");
                                    break;
                                }
                                catch(RespostaErradaException e){
                                    System.out.println("Resposta errada.\n");
                                    break;
                                }
                                catch(UserNaoExisteException e){
                                    System.out.println("User não existe.\n");
                                    break;
                                }
                                catch(CacheJaExisteException e){
                                    System.out.println("Cache já existe.\n");
                                    break;
                                }
                                catch(EventoNaoExisteException e){
                                    System.out.println("Evento não existe\n");
                                    break;
                                }
                                catch(EventoTerminadoException e){
                                    System.out.println("O evento foi terminado!");
                                    break;
                                }
                                catch(NaoExisteTempoDesseTipoCacheException e){
                                    System.out.println("Não existe tempo");
                                    break;
                                }
                                catch(EstasAtrasadoException e){
                                    System.out.println("Estas atrasado");
                                    break;
                                }
                                catch(NrPtsDiferenteException e){
                                    System.out.println("Numero pontos diferentes");
                                    break;
                                }
                                catch(PontosErradosException e){
                                    System.out.println("Pontos Errados");
                                    break;
                                }
                                catch(ObjetoNaoExisteException e){
                                    System.out.println("Objeto nao existe");
                                    break;
                                }
                                System.out.println("Registado com sucesso!\n");
                                break;
                        case 5: /* cacheTradicional */
                                int objRetirados, objColocados;
                                System.out.println("Número de objetos retirados da cache: \n");
                                try{
                                    objRetirados = inputInt.nextInt();
                                }catch(InputMismatchException e){
                                    System.out.println("Indique um inteiro!\n");
                                    break;
                                }
                                System.out.println("Número de objetos colocados da cache: \n");
                                try{
                                    objColocados = inputInt.nextInt();
                                }catch(InputMismatchException e){
                                    System.out.println("Indique um inteiro!\n");
                                    break;
                                }
                        
                                ArrayList<String> objCol = new ArrayList<String>(objColocados);
                                ArrayList<String> objRet = new ArrayList<String>(objRetirados);
                                
                                for(i=0; i<objRetirados;i++){
                                    System.out.println("Objeto retirado " + i+1 + ": \n");
                                    objRet.add(inputString.nextLine());
                                }
                                for(i=0; i<objColocados;i++){
                                    System.out.println("Objeto colocado " + i+1 + ": \n");
                                    objRet.add(inputString.nextLine());
                                }
                                try{
                                    app.registarDescobertaCacheDeUmEvento(nomeEvento, "CacheTradicional", nome, null, "", objRet,objRet);
                                }
                                catch(UserNaoLogadoException e){
                                    System.out.println("User não logado.\n");
                                    break;
                                }
                                catch(CacheNaoExisteException e){
                                    System.out.println("Cache não existe.\n");
                                    break;
                                }
                                catch(TipoDeCacheErradoException e){
                                    System.out.println("Tipo de cache errado.\n");
                                    break;
                                }
                                catch(RespostaErradaException e){
                                    System.out.println("Resposta errada.\n");
                                    break;
                                }
                                catch(UserNaoExisteException e){
                                    System.out.println("User não existe.\n");
                                    break;
                                }
                                catch(CacheJaExisteException e){
                                    System.out.println("Cache já existe.\n");
                                    break;
                                }
                                catch(EventoNaoExisteException e){
                                    System.out.println("Evento não existe\n");
                                    break;
                                }
                                catch(EventoTerminadoException e){
                                    System.out.println("O evento foi terminado!");
                                    break;
                                }
                                catch(NaoExisteTempoDesseTipoCacheException e){
                                    System.out.println("Não existe tempo");
                                    break;
                                }
                                catch(EstasAtrasadoException e){
                                    System.out.println("Estas atrasado");
                                    break;
                                }
                                catch(NrPtsDiferenteException e){
                                    System.out.println("Numero pontos diferentes");
                                    break;
                                }
                                catch(PontosErradosException e){
                                    System.out.println("Pontos Errados");
                                    break;
                                }
                                catch(ObjetoNaoExisteException e){
                                    System.out.println("Objeto nao existe");
                                    break;
                                }
                                System.out.println("Registado com sucesso!\n");
                                break;
                        case 0:
                            break;
                       }
                 case 2:
                    break;
            }
        }
    }

    
    private void menuUtilizador(GeoCaching app){
        Scanner input = new Scanner(System.in), inputInt = new Scanner(System.in), inputString = new Scanner(System.in), inputDouble = new Scanner(System.in);
        int escolha = 1;
        double latitude, longitude;
        String nome = new String();
        while(escolha != 0){
           System.out.println("\n1. Criar uma cache;" +"\n"+ "2. Remover uma cache;" +"\n"+"3. Registar descoberta de uma cache;" 
           +"\n"+"4. Remover uma atividade de descoberta;" +"\n"+"5. Ver as suas últimas 10 atividades (ver detalhadamente uma atividade);" +"\n"+"6. Ver lista de amigos;" +"\n"
           +"7. Ver 10 últimas atividades dos amigos (ver detalhadamente uma atividade);" +"\n"+"8. Ver o perfil;" +"\n"+"9. Editar o perfil;" +"\n"+
           "10. Ver estatística mensalmente ou anualmente;" +"\n" + "11. Adicionar um utilizador aos amigos\n" + "12. Remover um utilizador dos amigos\n" + "13. Ver todos os users\n" + 
           "14. Ver todas as caches\n" + "15. Reportar abuso de cache\n" + "16. Mudar palavra pass\n17. Ver eventos\n18. Entrar num evento\n19. Registar num evento\n20. Guardar estado\n" + "0. LogOut\n");
            
            try{
                escolha = inputInt.nextInt();
            }catch(InputMismatchException e){
                System.out.println("Indique um inteiro!\n");
                escolha=21;
                inputInt.nextLine();
            }
            switch(escolha){
                case 1: /* Criar cache */
                       criarCache(app);
                    break;
                case 2: /* Invalidar cache */
                       removerCache(app);
                    break;
                case 3: /* Registar descoberta de cache */
                    int escolha2 = 1;
                    System.out.println("Insira o nome da cache: ");
                    nome = inputString.nextLine();
                    
                    System.out.println("Diga qual o tipo de Cache que encontrou:");
                    System.out.println("1. MultiCache");
                    System.out.println("2. Cache Mistério");
                    System.out.println("3. Cache Evento");
                    System.out.println("4. Virtual");
                    System.out.println("5. MicroCache");
                    System.out.println("6. Cache tradicional");
                    System.out.println("0. Sair\n");
                    try{
                        escolha2 = inputInt.nextInt();
                    }catch(InputMismatchException e){
                        System.out.println("Indique um inteiro!\n");
                        escolha2=7;
                        inputInt.nextLine();
                    }
                    
                    switch(escolha2){
                    case 1: /* multicache */
                           int nrPontos, i;
                           double latitude2, longitude2;
                           System.out.println("Número de pontos passados antes de chegar à cache: \n");
                           try{
                                nrPontos = inputInt.nextInt();
                           }catch(InputMismatchException e){
                                System.out.println("Indique um inteiro!\n");
                                break;
                           }
                           ArrayList<Point2D.Double> pontos = new ArrayList<Point2D.Double>(); 
                           for(i=0; i<nrPontos;i++){
                                System.out.println("Latitude: \n");
                                try{
                                    latitude2 = inputDouble.nextDouble();
                                }catch(InputMismatchException e){
                                    System.out.println("Indique um Double!\n");
                                    break;
                                }
                                System.out.println("Longitude: \n");
                                try{
                                    longitude2 = inputDouble.nextDouble();
                                }catch(InputMismatchException e){
                                    System.out.println("Indique um Double!\n");
                                    break;
                                }
                                pontos.add(new Point2D.Double(latitude2,longitude2));
                           }
                            try{
                                app.encontreiMultiCache(nome, pontos);
                            }
                            catch(UserNaoLogadoException e){
                                System.out.println("User não logado.\n");
                                break;
                            }
                            catch(CacheNaoExisteException e){
                                System.out.println("Cache não existe.\n");
                                break;
                            }
                            catch(TipoDeCacheErradoException e){
                                System.out.println("Tipo de cache errado.\n");
                                break;
                            }
                            catch(PontosErradosException e){
                                System.out.println("Pontos errados.\n");
                                break;
                            }
                            catch(NrPtsDiferenteException e){
                                System.out.println("Número de pontos errados.\n");
                                break;
                            }
                            catch(UserNaoExisteException e){
                                System.out.println("User não existe.\n");
                                break;
                            }
                            catch(CacheJaExisteException e){
                                System.out.println("Ja encontrou essa cache ou és o criador.");
                                break;
                            }
                            catch(UserJaExisteException e){
                                System.out.println("User já existe.\n");
                                break;
                            }
                            System.out.println("Registado com sucesso!\n");
                            break;
                    case 2: /* cacheMisterio */
                            String resposta;
                            System.out.println("Introduza a resposta: ");
                            resposta = inputString.nextLine();
                            try{
                                app.encontreiCacheMisterio(nome,resposta);
                            }
                            catch(UserNaoLogadoException e){
                                System.out.println("User não logado.\n");
                                break;
                            }
                            catch(CacheNaoExisteException e){
                                System.out.println("Cache não existe.\n");
                                break;
                            }
                            catch(TipoDeCacheErradoException e){
                                System.out.println("Tipo de cache errado.\n");
                                break;
                            }
                            catch(RespostaErradaException e){
                                System.out.println("Resposta errada.\n");
                                break;
                            }
                            catch(UserNaoExisteException e){
                                System.out.println("User não existe.\n");
                                break;
                            }
                            catch(CacheJaExisteException e){
                                System.out.println("Ja encontrou essa cache ou és o criador.");
                                break;
                            }
                            catch(UserJaExisteException e){
                                System.out.println("User já existe.\n");
                                break;
                            }
                            System.out.println("Registado com sucesso!\n");
                            break;
                    case 3: /* cacheEvento */
                            try{
                                app.encontreiCacheEvento(nome);
                            }
                            catch(UserNaoLogadoException e){
                                System.out.println("User não logado.\n");
                                break;
                            }
                            catch(CacheNaoExisteException e){
                                System.out.println("Cache não existe.\n");
                                break;
                            }
                            catch(TipoDeCacheErradoException e){
                                System.out.println("Tipo de cache errado.\n");
                                break;
                            }
                            catch(UserNaoExisteException e){
                                System.out.println("User não existe.\n");
                                break;
                            }
                            catch(CacheJaExisteException e){
                                System.out.println("Ja encontrou essa cache ou és o criador.");
                                break;
                            }
                            catch(UserJaExisteException e){
                                System.out.println("User já existe.\n");
                                break;
                            }
                            System.out.println("Registado com sucesso!\n");
                            break;
                    case 4: /* cacheVirtual */
                            String resposta2;
                            System.out.println("Insira a resposta: \n");
                            resposta2 = inputString.nextLine();
                            try{
                                app.encontreiCacheVirtual(nome,resposta2);
                            }
                            catch(UserNaoLogadoException e){
                                System.out.println("User não logado.\n");
                                break;
                            }
                            catch(CacheNaoExisteException e){
                                System.out.println("Cache não existe.\n");
                                break;
                            }
                            catch(TipoDeCacheErradoException e){
                                System.out.println("Tipo de cache errado.\n");
                                break;
                            }
                            catch(RespostaErradaException e){
                                System.out.println("Resposta Errada.\n");
                                break;
                            }
                            catch(UserNaoExisteException e){
                                System.out.println("User não existe.\n");
                                break;
                            }
                            catch(CacheJaExisteException e){
                                System.out.println("Ja encontrou essa cache ou és o criador.");
                                break;
                            }
                            catch(UserJaExisteException e){
                                System.out.println("User já existe.\n");
                                break;
                            }
                            System.out.println("Registado com sucesso!\n");
                            break;
                    case 5: /* microCache */
                            try{
                                app.encontreiMicroCache(nome);
                            }
                            catch(UserNaoLogadoException e){
                                System.out.println("User não logado.\n");
                                break;
                            }
                            catch(CacheNaoExisteException e){
                                System.out.println("Cache não existe.\n");
                                break;
                            }
                            catch(TipoDeCacheErradoException e){
                                System.out.println("Tipo de cache errado.\n");
                                break;
                            }
                            catch(UserNaoExisteException e){
                                System.out.println("User não existe.\n");
                                break;
                            }
                            catch(CacheJaExisteException e){
                                System.out.println("Ja encontrou essa cache ou és o criador.");
                                break;
                            }
                            catch(UserJaExisteException e){
                                System.out.println("User já existe.\n");
                                break;
                            }
                            System.out.println("Registado com sucesso!\n");
                            break;
                    case 6: /* cacheTradicional */
                            int objRetirados, objColocados;
                            System.out.println("Número de objetos retirados da cache: \n");
                            try{
                                objRetirados = inputInt.nextInt();
                            }catch(InputMismatchException e){
                                System.out.println("Indique um inteiro!\n");
                                break;
                            }
                            System.out.println("Número de objetos colocados da cache: \n");
                            try{
                                objColocados = inputInt.nextInt();
                            }catch(InputMismatchException e){
                                System.out.println("Indique um inteiro!\n");
                                break;
                            }
                    
                            ArrayList<String> objCol = new ArrayList<String>(objColocados);
                            ArrayList<String> objRet = new ArrayList<String>(objRetirados);
                            
                            for(i=0; i<objRetirados;i++){
                                System.out.println("Objeto retirado " + (i+1) + ": \n");
                                objRet.add(inputString.nextLine());
                            }
                            for(i=0; i<objColocados;i++){
                                System.out.println("Objeto colocado " + (i+1) + ": \n");
                                objCol.add(inputString.nextLine());
                            }
                            try{
                                app.encontreiCacheTradicional(nome,objRet,objCol);
                            }
                            catch(UserNaoLogadoException e){
                                System.out.println("User não logado.\n");
                                break;
                            }
                            catch(CacheNaoExisteException e){
                                System.out.println("Cache não existe.\n");
                                break;
                            }
                            catch(TipoDeCacheErradoException e){
                                System.out.println("Tipo de cache errado.\n");
                                break;
                            }
                            catch(UserNaoExisteException e){
                                System.out.println("User não existe.\n");
                                break;
                            }
                            catch(CacheJaExisteException e){
                                System.out.println("Ja encontrou essa cache ou és o criador.");
                                break;
                            }
                            catch(ObjetoNaoExisteException e){
                                System.out.println("Objeto não existe.\n");
                                break;
                            }
                            catch(UserJaExisteException e){
                                System.out.println("User já existe.\n");
                                break;
                            }
                            System.out.println("Registado com sucesso!\n");
                            break;
                    case 7:
                        break;
                    case 0:
                        break;
                    }
                    break;
                case 4: /* Remover 1 atividade de descoberta */
                    System.out.println("Insira o nome da cache que pretende remover dos registos:\n");
                    nome = inputString.nextLine();
                    try{
                        app.removerEncontroCache(nome);
                    }
                    catch (ECriadorException e){
                        System.out.println("Voce é o criador desta cache, logo não a descobriu.\n");
                        break;
                    }
                    catch(UserNaoLogadoException e){
                        System.out.println("User não logado.\n");
                        return;
                    }
                    catch(CacheNaoExisteException e){
                        System.out.println("Cache não existe.\n");
                        break;
                    }
                    catch(UserNaoExisteException e){
                        System.out.println("User não existe.\n");
                        break;
                    }
                    System.out.println("Removido com sucesso!\n");
                    break;
                case 5: /* Ver nossas ultimas 10 atividades */
                    ArrayList<String> minhasAtividades;
                    try{
                        minhasAtividades = app.ultimas10Atividades();
                    }
                    catch(UserNaoLogadoException e){
                        System.out.println("User não logado.\n");
                        return;
                    }
                    catch(SemRegistosException e){
                        System.out.println("Sem registos.\n");
                        break;
                    }
                    catch(UserNaoExisteException e){
                        System.out.println("User não existe.\n");
                        return;
                    }
                    for(String s : minhasAtividades){
                        System.out.println(s);
                    }
                    System.out.println("\n1. Ver alguma atividade detalhadamente\n2. Sair\n");
                    try{
                        escolha2 = inputInt.nextInt();
                    }catch(InputMismatchException e){
                        System.out.println("Indique um Inteiro!\n");
                        break;
                    }
                    if(escolha2 == 1){
                        System.out.println("\nID do registo: \n");
                        try{
                            escolha2 = inputInt.nextInt();
                        }catch(InputMismatchException e){
                            System.out.println("Indique um Inteiro!\n");
                            break;
                        }
                        try{
                            System.out.println(app.verAtividade(escolha2,app.getloggedUser()));
                        }
                        catch(UserNaoLogadoException e){
                            System.out.print("User não logado.\n");
                            return;
                        }
                        catch(UserNaoExisteException e){
                            System.out.println("User não existe.\n");
                            return;
                        }
                    }
                    System.out.println("========================================\n");
                    break;
                case 6: /* Ver amigos */
                    ArrayList<String> meusAmigos;
                    try{
                        meusAmigos = app.meusAmigos();
                    }
                    catch(UserNaoLogadoException e){
                        System.out.print("User não logado.\n");
                        return;
                    }
                    catch(UserNaoExisteException e){
                        System.out.println("User não existe.\n");
                        return;
                    }
                    for(String s : meusAmigos){
                        System.out.println(s);
                    }
                    break;
                case 7: /* Ver 10 últimas atividades dos amigos (ver detalhadamente uma atividade) */
                    String email;
                    ArrayList<String> minhasAtividadesAmigos;
                    try{
                        minhasAtividadesAmigos = app.ultimas10AtividadesAmigos();
                    }
                    catch(UserNaoLogadoException e){
                        System.out.println("User nao logado.\n");
                        return;
                    }
                    catch(UserNaoExisteException e){
                        System.out.println("User nao existe.\n");
                        return;
                    }
                    catch(SemRegistosException e){
                        System.out.println("Sem registos.\n");
                        break;
                    }
                    for(String s : minhasAtividadesAmigos){
                        System.out.println(s);
                    }
                    System.out.println("\n1. Ver alguma atividade detalhadamente\n2. Sair\n");
                    try{
                        escolha2 = inputInt.nextInt();
                    }catch(InputMismatchException e){
                        System.out.println("Indique um Inteiro!\n");
                        break;
                    }
                    if(escolha2 == 1){
                        System.out.println("\nID do registo:");
                        try{
                            escolha2 = inputInt.nextInt();
                        }catch(InputMismatchException e){
                            System.out.println("Indique um Inteiro!\n");
                            break;
                        }
                        try{
                            System.out.println(app.verAtividade(escolha2,"ola"));
                        }
                        catch(UserNaoLogadoException e){
                            System.out.println("User não logado.\n");
                            return;
                        }
                        catch(UserNaoExisteException e){
                            System.out.println("User nao existe.\n");
                            return;
                        }
                    }
                    break;
                case 8: /* Ver o perfil */
                    try{
                        System.out.println(app.meuPerfil());
                    }
                    catch(UserNaoLogadoException e){
                        System.out.println("User nao logado.\n");
                        return;
                    }
                    catch(UserNaoExisteException e){
                        System.out.println("User nao existe.\n");
                        return;
                    }
                    break;
                case 9: /* Editar Perfil */
                    String pass, morada;
                    int gen, ano, mes, dia;
                    char genero = 'X';
                    System.out.println("Introduza o seu nome:");
                    nome = inputString.nextLine();
                    System.out.println("Introduza antiga palavra-passe(para validação):");
                    pass = input.next();
                    System.out.println("Introduza a sua morada:");
                    morada = inputString.nextLine();
                    System.out.println("Genero: '1' -> Masculino '2' -> Feminino");
                    try{
                        gen = inputInt.nextInt();
                    }catch(InputMismatchException e){
                        System.out.println("Indique um Inteiro!\n");
                        break;
                    }
                    if(gen == 1) genero = 'M';
                    else if(gen == 2) genero = 'F'; 
                    System.out.println("Introduza o seu ano de nascimento:");
                    try{
                        ano = inputInt.nextInt();
                    }catch(InputMismatchException e){
                        System.out.println("Indique um Inteiro!\n");
                        break;
                    }
                    System.out.println("Introduza o seu mes de nascimento:");
                    try{
                        mes = inputInt.nextInt();
                    }catch(InputMismatchException e){
                        System.out.println("Indique um Inteiro!\n");
                        break;
                    }
                    System.out.println("Introduza o seu dia de nascimento:");
                    try{
                        dia = inputInt.nextInt();
                    }catch(InputMismatchException e){
                        System.out.println("Indique um Inteiro!\n");
                        break;
                    }
                    
                    GregorianCalendar data = new GregorianCalendar(ano,mes,dia);
                    try{
                        app.editarPerfil(pass, nome, genero, morada, data);
                    }
                    catch(UserNaoLogadoException e){
                        System.out.println("User nao logado.\n");
                        return;
                    }
                    catch(UserNaoExisteException e){
                        System.out.println("User nao existe.\n");
                        return;
                    }
                    catch(PassIncorretaException e){
                        System.out.println("Pass incorreta.\n");
                        break;
                    }
                    System.out.println("Editado com sucesso!\n");
                    break;
                case 10:/* Ver estatisticas mensalmente ou anualmente (Diferentes tipos de cache) */
                    int escolhaEstat=1, anoEstat=1, mesEstat=1;
                    System.out.println("1. Mensais\n2. Anuais\n");
                    try{
                        escolhaEstat = inputInt.nextInt();
                    }catch(InputMismatchException e){
                        System.out.println("Indique um inteiro!\n");
                        escolhaEstat=3;
                        inputInt.nextLine();
                    }
                    switch(escolhaEstat){
                        case 1: /* Mensal */
                           System.out.println("Indique Ano:\n");
                           try{
                                anoEstat=inputInt.nextInt();
                           }catch(InputMismatchException e){
                                System.out.println("Indique um inteiro!\n");
                                break;
                           }  
                           System.out.println("Indique Mes:\n");
                           try{
                                mesEstat=inputInt.nextInt();
                           }catch(InputMismatchException e){
                                System.out.println("Indique um inteiro!\n");
                                break;
                           } 
                           try{
                                System.out.println(app.estatisticaMes(anoEstat,mesEstat));
                           }catch(UserNaoLogadoException e){
                                System.out.println("User nao logado!\n");
                                break;
                           }catch(UserNaoExisteException e){
                                System.out.println("User nao existe!\n");
                                break;
                           }catch(ClassNotFoundException e){
                               System.out.println("Class não encontrada!\n");
                               break;
                           }
                           break;
                        case 2: /*Anualmente*/
                            System.out.println("Indique Ano:\n");
                            try{
                                anoEstat=inputInt.nextInt();
                            }catch(InputMismatchException e){
                                System.out.println("Indique um inteiro!\n");
                                break;
                            }
                            try{
                                System.out.println(app.estatisticaAno(anoEstat));
                            }catch(UserNaoLogadoException e){
                                System.out.println("User nao logado!\n");
                                break;
                            }catch(UserNaoExisteException e){
                                System.out.println("User nao existe!\n");
                                break;
                            }catch(ClassNotFoundException e){
                                System.out.println("Class não encontrada!\n");
                                break;
                            }
                            break;
                         case 3:
                            break;
                    }
                    break;
                case 11:/* Adicionar um utilizador aos amigos*/
                    System.out.println("Insira o e-email do seu amigo:");
                    email = input.next();
                    try{
                        app.adicionarAmigo(email);
                    }
                    catch(UserNaoLogadoException e){
                        System.out.println("User nao logado.\n");
                        return;
                    }
                    catch(UserNaoExisteException e){
                        System.out.println("User nao existe.\n");
                        return;
                    }
                    catch(AmigoNaoExisteException e){
                        System.out.println("Amigo não existe.\n");
                        break;
                    }
                    catch(AdicionarProprioException e){
                        System.out.println("Não se pode adicionar a si próprio aos seus amigos.\n");
                        break;
                    }
                    catch(AmigoJaExisteException e){
                        System.out.println("Amigo já existe.\n");
                        break;
                    }
                    System.out.println("Amigo adicionado com sucesso!\n");
                    break;
                case 12:/* Remover amigo */
                    System.out.println("Insira o e-email do seu amigo:");
                    email = input.next();
                    try{
                        app.removerAmigo(email);
                    }
                    catch(UserNaoLogadoException e){
                        System.out.println("User nao logado.\n");
                        return;
                    }
                    catch(UserNaoExisteException e){
                        System.out.println("User nao existe.\n");
                        return;
                    }
                    catch(AmigoNaoExisteException e){
                        System.out.println("Amigo não existe.\n");
                        break;
                    }
                    System.out.println("Amigo removido com sucesso!\n");
                    break;
                case 13:/* Ver todos utilizadores */
                    ArrayList<String> todosUtilizadores = app.getutilizadoresString();
                    for(String s : todosUtilizadores){
                        System.out.println(s);
                    }
                    break;
                case 14:/* Ver todas as caches */
                    ArrayList<String> todasCaches = app.gettodasAsCachesString();
                    for(String s : todasCaches){
                        System.out.println(s);
                    }
                    break;
                case 15: /* Reportar abuso cache */
                    System.out.println("Insira o nome da cache: ");
                    nome = inputString.nextLine();
                    try{
                        app.reportarAbusoCache(nome);
                    }
                    catch(UserNaoLogadoException e){
                        System.out.print("User não logado.\n");
                        return;
                    }
                    catch(CacheNaoExisteException e){
                        System.out.println("Cache não existe.\n");
                        break;
                    }
                    catch(CacheJaExisteException e){
                        System.out.println("Cache já existe.\n");
                        break;
                    }
                    System.out.println("Cache reportada com sucesso!\n");
                    break;
                case 16:/*mudar pass*/
                    String passNova;
                    System.out.println("Insira palavra pass antiga: ");
                    pass = inputString.nextLine();
                    System.out.println("Insira nova palavra pass: ");
                    passNova = inputString.nextLine();
                    try{
                        app.trocaPass(pass,passNova);
                    }catch(UserNaoLogadoException e){
                        System.out.println("User nao logado.\n");
                        break;
                    }catch(PassIncorretaException e){
                        System.out.println("Pass incorreta.\n");
                        break;
                    }catch(UserNaoExisteException e){
                        System.out.println("User não existe.\n");
                        break;
                    }
                    System.out.println("Palavra pass alterada com sucesso!");
                    break;
                case 17:/*Ver eventos*/
                    ArrayList<String> eventos;
                    eventos = app.verEventos();
                    System.out.println("Eventos:");
                    for(String s: eventos){
                        System.out.println("-> "+s);
                    }
                    break;
                case 18:/* Entrar num Evento*/
                    String nomeEvento;
                    System.out.println("Indique o nome do evento: ");
                    nomeEvento = inputString.nextLine();
                
                    try{
                        app.entrarEvento(nomeEvento);
                    }catch(UserNaoLogadoException e){
                        System.out.println("User nao logado.");break;
                    }catch(EventoNaoExisteException e){
                        System.out.println("Evento nao existente.");break;
                    }catch(UserNaoRegistadoException e){
                        System.out.println("Ainda nao se registou nesse evento.");break;
                    }catch(EventoTerminadoException e){
                        System.out.println("Evento já acabou.");break;
                    }catch(EventoNaoComecouException e){
                        System.out.println("O evento ainda nao comecou.");break;
                    }    
                    
                    menuUtilizadorEvento(app, nomeEvento);
                    break;

                case 19: /*Registar num evento*/
                    String nomeEvento2;
                    System.out.println("Indique o nome do evento: \n");
                    nomeEvento2 = inputString.nextLine();
                    try{
                        app.registarNumEvento(nomeEvento2);
                    }catch(EventoCheioException e){
                        System.out.println("Evento cheio\n");
                        break;
                    }catch(UserNaoLogadoException e){
                        System.out.println("User não logado\n");
                        break;
                    }catch(UserNaoExisteException e){
                        System.out.println("User não existe\n");
                        break;
                    }catch(TempoInscricaoPassouException e){
                        System.out.println("Tempo de inscricao esgotou\n");
                        break;
                    }catch(UserJaExisteException e){
                        System.out.println("User já existe!\n");
                        break;
                    }catch(ClassNotFoundException e){
                        System.out.println("Classe nao existe!\n");
                        break;
                    }catch(EventoNaoExisteException e){
                        System.out.println("Evento nao existe!\n");
                        break;
                    }
                    System.out.println("Registado com sucesso.");
                    break;
                case 20:
                    try{
                        gravarObjeto(app);
                    }catch(FileNotFoundException e){
                        System.out.println("Erro abrir ficheiro!");
                    }catch(IOException e){
                        System.out.println("Erro IO!");
                    }
                    break;
                case 21:
                    break;
                case 0:/* logout */
                    try{
                        app.logOut();
                    }
                    catch(NinguemLogadoException e){
                        System.out.println("Não está ninguém logado.\n");
                        return;
                    }
                    return;
            }
        }
    }
    
    private void menuAdmin(GeoCaching app){
        Scanner input = new Scanner(System.in), inputInt = new Scanner(System.in), inputString = new Scanner(System.in), inputDouble = new Scanner(System.in);
        int escolha = 1;
        double latitude, longitude;
        TreeMap<String,Cache> treeCachesEvento = new TreeMap<String,Cache>();
        try{
            System.out.println("\nExiste(m) " + app.nrCachesReportadasPorResolver() + " cache(s) reportada(s) como abusadora(s)!\n");
        }
        catch(AdminNaoLogadoException e){
            System.out.println("Admin não logado.\n");
            return;
        }
        while(escolha != 0){
            System.out.println("\n1. Criar uma cache;\n"+ "2. Remover uma cache;\n" + "3. Registar administrador\n"+ "4. Eliminar administrador\n"+
            "5. Ver caches reportadas \n" + "6. Tratar cache abusadora\n" + "7. Criar Evento\n8. Finalizar evento\n" +"9. Ver eventos\n"+ "0. LogOut;");
            try{
                escolha = inputInt.nextInt();
            }catch(InputMismatchException e){
                System.out.println("Indique um inteiro!\n");
                escolha=8;
                inputInt.nextLine();
            }
            switch(escolha){
                case 1: /*Criar case*/
                    criarCache(app);
                    break;
                case 2: /*Remover cache*/
                    removerCache(app);
                    break;
                case 3: /*Registar Admin*/
                    String email, pass;
                    System.out.println("Insira o seu e-email:");
                    email = input.next();
                    System.out.println("Introduza a sua pass:");
                    pass = input.next();
                    try{
                        app.registarAdmin(email,pass);
                    }
                    catch(AdminNaoLogadoException e){
                        System.out.println("Admin não logado.\n");
                        return;
                    }
                    catch(AdminJaExisteException e){
                        System.out.println("Admin já existe.\n");
                        break;
                    }
                    System.out.println("\nAdmin registado com sucesso!\n");
                    break;
                case 4: /*Eliminar Admin*/
                    System.out.println("Insira o e-email do admin:");
                    email = input.next();
                    try{
                        app.eliminarAdmin(email);
                    }
                    catch(AdminNaoLogadoException e){
                        System.out.println("Admin não logado.\n");
                        return;
                    }
                    catch(UmAdminException e){
                        System.out.println("Só há um admin.\n");
                        break;
                    }
                    catch(AdminNaoExisteException e){
                        System.out.println("Admin não existe.\n");
                        break;
                    }
                    System.out.println("Admin removido com sucesso!\n");
                    break;
                case 5: /*Ver caches abusadoras*/
                    for(String s: app.cachesReportadas()){
                        System.out.println(s);
                    }
                    break;
                case 6: /* Tratar caches abusadoras */
                    String nome = new String();
                    int escolha2;
                    System.out.println("Insira o nome da cache: ");
                    nome = inputString.nextLine();
                    System.out.println("Insira a latitude da cache: ");
                    try{
                        latitude = inputDouble.nextDouble();
                    }catch(InputMismatchException e){
                        System.out.println("Indique um double!\n");
                        break;
                    }
                    System.out.println("Insira a longitude da cache: ");
                    try{
                        longitude = inputDouble.nextDouble();
                    }catch(InputMismatchException e){
                        System.out.println("Indique um double!\n");
                        break;
                    }
                    System.out.println("1. Abusadora\n2. Nao abusadora");
                    try{
                        escolha2 = inputInt.nextInt();
                    }catch(InputMismatchException e){
                        System.out.println("Indique um Inteiro!\n");
                        break;
                    }
                    if(escolha2==1){
                        try{
                            app.eliminarCacheAbusadora(nome, latitude,longitude,true);
                        }
                        catch(AdminNaoLogadoException e){
                            System.out.println("Admin não logado.\n");
                            return;
                        }
                        catch(CacheNaoExisteException e){
                            System.out.println("Cache não existe.\n");
                            break;
                        }catch(CoordenadasInvalidasException e){
                            System.out.println("Coordenadas inválidas.\n");
                            break;
                        }
                    }
                    else{
                        try{
                            app.eliminarCacheAbusadora(nome, latitude,longitude,false);
                        }
                        catch(AdminNaoLogadoException e){
                            System.out.println("Admin não logado.\n");
                            return;
                        }
                        catch(CacheNaoExisteException e){
                            System.out.println("Cache não existe.\n");
                            break;
                        }catch(CoordenadasInvalidasException e){
                            System.out.println("Coordenadas inválidas.\n");
                            break;
                        }
                    }
                    break;
                case 7: /*Criar Evento*/
                    System.out.println("Insira o nome do evento:");
                    String nomeEvento;
                    double tempoMedio;
                    int nrMaxUsers, condMeteo, diaFim, mesFim, anoFim, diaInicio, mesInicio, anoInicio, escolha3 = 1;
                    Point2D.Double pontoLocalizacao;
                    nomeEvento = inputString.nextLine();
                    System.out.println("Tempo médio para encontrar caches:");
                    try{
                        tempoMedio = inputDouble.nextDouble();
                    }catch(InputMismatchException e){
                        System.out.println("Indique um Double!");
                        break;
                    }
                    System.out.println("Número máximo de utilizadores.");
                    try{
                        nrMaxUsers = inputInt.nextInt();
                    }catch(InputMismatchException e){
                        System.out.println("Indique um inteiro!");
                        break;
                    }
                    Random rand = new Random();
                    condMeteo = rand.nextInt(10) + 1;
                    System.out.println("Localização do evento.\nLatitude:");
                    try{
                        latitude = inputDouble.nextDouble();
                    }
                    catch(InputMismatchException e){
                        System.out.println("Indique um double para latitude!\n");
                        break;
                    }
                    System.out.println("Longitude:");
                    try{
                        longitude = inputDouble.nextDouble();
                    }
                    catch(InputMismatchException e){
                        System.out.println("Indique um double para longitude!\n");
                        break;
                    }
                    System.out.println("Data limite de inscrições.\nAno:");
                    try{
                        anoFim = inputInt.nextInt();
                    }catch(InputMismatchException e){
                        System.out.println("Indique um inteiro!\n");
                        break;
                    }
                    System.out.println("Mês:");
                    try{
                        mesFim = inputInt.nextInt();
                    }catch(InputMismatchException e){
                        System.out.println("Indique um inteiro!\n");
                        break;
                    }
                    System.out.println("Dia:");
                    try{
                        diaFim = inputInt.nextInt();
                    }catch(InputMismatchException e){
                        System.out.println("Indique um inteiro!\n");
                        break;
                    }
                    System.out.println("Data de início do evento.\nAno:");
                    try{
                        anoInicio = inputInt.nextInt();
                    }catch(InputMismatchException e){
                        System.out.println("Indique um inteiro!\n");
                        break;
                    }
                    System.out.println("Mês:");
                    try{
                        mesInicio = inputInt.nextInt();
                    }catch(InputMismatchException e){
                        System.out.println("Indique um inteiro!\n");
                        break;
                    }
                    System.out.println("Dia:");
                    try{
                        diaInicio = inputInt.nextInt();
                    }catch(InputMismatchException e){
                        System.out.println("Indique um inteiro!\n");
                        break;
                    }
                    while(escolha3 != 2){
                        System.out.println("Deseja criar uma cache?\n1. Sim\n2. Não");
                        try{
                            escolha3 = inputInt.nextInt();
                        }catch(InputMismatchException e){
                            System.out.println("Indique um inteiro!\n");
                            break;
                        }
                        if(escolha3 == 1){
                            Cache cache = criarCacheEmEvento();
                            if(cache == null){
                                System.out.println("Erro na criação da cache.\n");
                            }
                            else{
                                treeCachesEvento.put(cache.getNome(),cache);
                            }
                        }
                        else break;
                    }
                    try{
                        GregorianCalendar agora = new GregorianCalendar();
                        GregorianCalendar dataLimiteInscricoes = new GregorianCalendar(anoFim,mesFim,diaFim); 
                        GregorianCalendar dataInicio = new GregorianCalendar(anoInicio,mesInicio-1,diaInicio);
                        if(agora.compareTo(dataInicio) > 0 || agora.compareTo(dataLimiteInscricoes) > 0){System.out.println("Datas têm que ser depois de hoje!\n");break;}
                        if(dataLimiteInscricoes.compareTo(dataInicio) < 0 ){System.out.println("Data final de inscricoes superior á data de inicio de evento!\n");break;}
                        app.criarEvento(nomeEvento, tempoMedio, nrMaxUsers, condMeteo, new Point2D.Double(latitude,longitude), dataLimiteInscricoes 
                        ,dataInicio , treeCachesEvento);
                    }
                    catch(AdminNaoLogadoException e){
                        System.out.println("Não está um Admin logado.\n");
                        break;
                    }catch(EventoJaRegistadoException e){
                        System.out.println("Este evento já está registado.\n");
                        break;
                    }
                    System.out.println("Evento criado com sucesso.");
                    break;
                case 8: /* finalizar evento */
                    String nomeEvento2 = new String();
                    String vencedorEvento = new String();
                    System.out.println("Indique o nome do evento: ");
                    nomeEvento2 = inputString.nextLine();
                    try{
                        vencedorEvento = app.finalizarEvento(nomeEvento2);
                    }catch(AdminNaoLogadoException e){
                        System.out.println("Admin nao logado.");
                        break;
                    }catch(EventoNaoComecouException e){
                        System.out.println("Impossivel finalizar evento que ainda nao começou.");
                        break;
                    }catch(EventoNaoExisteException e){
                        System.out.println("Evento nao existe.");
                        break;
                    }
                    System.out.println("Evento finalizado com sucesso.\n" + vencedorEvento);
                    break;
                case 9:
                    ArrayList<String> eventos;
                    eventos = app.verEventos();
                    System.out.println("Eventos:");
                    for(String s: eventos){
                        System.out.println("-> " + s);
                    }
                    break;
                case 0:/* logout */
                    try{
                        app.logOut();
                    }
                    catch(NinguemLogadoException e){
                        System.out.println("Ninguém logado.\n");
                        return;
                    }
                    break;
            }
        }
    } 
    
    private void gravarObjeto(GeoCaching app) throws FileNotFoundException, IOException {
        FileOutputStream fops = new FileOutputStream("estado.db");
        ObjectOutputStream oout = new ObjectOutputStream(fops); 
        
        FileWriter fr = new FileWriter("contador");
        BufferedWriter bf = new BufferedWriter(fr);
        String aux = Integer.toString(Registo.getContador());
        bf.write(aux);
        bf.flush();bf.close();
        fr.close();
        
        try{
            oout.writeObject(app);
        }
        catch(FileNotFoundException e){
            System.out.println(e.getMessage());
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
        oout.flush();
        oout.close();
        fops.close();
    }
    
    private GeoCaching carregarDados(){
        GeoCaching app = null;
        try{
            ObjectInputStream oin = new ObjectInputStream(new FileInputStream("estado.db"));
            app = (GeoCaching)oin.readObject();
            oin.close();
        }catch(FileNotFoundException e){
            System.out.println("Erro abrir ficheiro!");
            app = new GeoCaching();
        }catch(IOException e){
            System.out.println("Erro IO!");
            app = new GeoCaching();
        }catch(ClassNotFoundException e){
            System.out.println("Classe nao encontrada!");
            app = new GeoCaching();
        }
        try{
            FileReader fr = new FileReader("contador");
            BufferedReader bf = new BufferedReader(fr);
            String x = bf.readLine();
            Registo.setContador(Integer.parseInt(x));
            bf.close();
            fr.close();
        }catch(FileNotFoundException e){
            System.out.println("Erro abrir ficheiro!");
        }catch(IOException e){
            System.out.println("Erro IO!");
        }
        
        return app;
    }

    private void meuMain() throws FileNotFoundException, IOException{
        GeoCaching app = carregarDados();
        boolean sair = false;
        int logado;
        
        iniciarDados(app);
        
        while(sair == false){
            logado = menuInicial(app);
            if(logado == 1){ /* user logado */
                menuUtilizador(app);
            }
            else if(logado == 2){ /* admin logado */
                menuAdmin(app);
            }
            else if(logado == 0){
                sair = true;
            }
        }
        gravarObjeto(app);
    }
    
    public static void main (String args[]) throws FileNotFoundException, IOException, ClassNotFoundException{
        GeoCachingTeste1 nova = new GeoCachingTeste1();
        nova.meuMain();
    }
}
