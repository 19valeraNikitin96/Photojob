
package common;

import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Validated<T> {
    
    private T data;
    
    public Boolean validated(){
        if(data == null) return false;
        for(Field f : data.getClass().getDeclaredFields()){
            f.setAccessible(true);
            try {
                if(f.get(data) == null){
                    return false;
                }
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                Logger.getLogger(Validated.class.getName()).log(Level.SEVERE, null, ex);
            }
            f.setAccessible(false);
        }
        return true;
    }
    
    public T get(){
        return this.data;
    }
    
    public Validated(T data){
        this.data = data;
    }
}
