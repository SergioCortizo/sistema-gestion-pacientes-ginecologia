package es.udc.fic.ginecologia.common.exception;

@SuppressWarnings("serial")
public class InstanceNotFoundException extends InstanceException {
    
    public InstanceNotFoundException(String name, Object key) {
    	super(name, key); 	
    }

}

