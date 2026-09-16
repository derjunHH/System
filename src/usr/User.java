package usr;

class User{
    String name;
    String passwd;

    User(String name,String passwd){
        this.name=name;
        this.passwd=passwd;
    }

    boolean login(String name,String passwd){
        if(this.name.equals(name)&&this.passwd.equals(passwd)){
            return true;
        }
        return false;
    }

    boolean register(String name,String passwd){
        try{
            
        }
    }
}