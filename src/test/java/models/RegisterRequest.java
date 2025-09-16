package models;

public class RegisterRequest {

    private String first_name;
    private String last_name;
    private String email;
    private String password;

    public String getfirst_name(){
        return first_name;
    }

    public String getlast_name(){
        return last_name;
    }

    public String getEmail(){
        return email;
    }

    public String getPassword(){
        return password;
    }

    public void setFirstName(String first_name) {
        this.first_name = first_name;
    }

    public void setLastName(String last_name) {
        this.last_name = last_name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}