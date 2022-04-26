import java.io.*;

public class Admin implements Comparable, Serializable
{
    private String email;
    private String password;

    public Admin(){
        this.email = new String("");
        this.password = new String("");
    }
    
    public Admin(String Email, String Password){
        this.email = Email;
        this.password = Password;
    }
    
    public Admin(Admin a){
        this.email = a.getEmail();
        this.password = a.getPassword();
    }
    
    /* gets*/
    public String getEmail(){
        return this.email;
    }
    public String getPassword(){
        return this.password;
    }
    
    /* sets*/
    public void setEmail(String Email){
        this.email=Email;
    }
    public void setPassword(String Password){
        this.password=Password;
    }
    
    /* Equals */
    public boolean equals(Object umaAdm){
        if(this==umaAdm)
            return true;
        if((umaAdm==null) || (this.getClass()!=umaAdm.getClass()))
            return false;
        else{
            Admin a =(Admin) umaAdm;
            return((this.email.equals(a.getEmail())) && (this.password.equals(a.getPassword())));
        }
    }
    /* toString */
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append ("Email: ");
        sb.append (this.email +"\n");
        sb.append ("Password: ");
        sb.append (this.password +"\n");
        return sb.toString();
    }
    /* Clone */
    public Admin clone(){
        return new Admin(this);
    }
    /* compareTo */
    public int compareTo(Object obj){
        Admin a = (Admin)obj;
        
        return (email.compareTo(a.getEmail()));
    }
    
}
