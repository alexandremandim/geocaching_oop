import java.util.Comparator;
import java.io.Serializable;

public class ComparatorRegistoTimeLine implements Comparator<Registo>, Serializable
{
   public int compare(Registo r1, Registo r2){
       return (-(r1.getDataAtividade()).compareTo(r2.getDataAtividade()));
   }
}