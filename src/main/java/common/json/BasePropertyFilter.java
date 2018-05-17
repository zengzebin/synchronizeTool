package common.json;

 

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.alibaba.fastjson.serializer.PropertyFilter;

public class BasePropertyFilter implements PropertyFilter {  
	private Collection<String> excludeList;
	private Collection<String> includeList;
    //@Override  
    public boolean apply(Object source, String name, Object value) {  
        boolean exclude=false;
        boolean include=true;
    	if(excludeList!=null){
    		exclude=isInCollection(name, excludeList);
    	}
    	if(includeList!=null){
    		include=isInCollection(name, includeList);
    	}          
        return include&&!exclude;  
    }  
    private static boolean isInCollection(String name, Collection<String> fields){
//        for(String field : fields) {          	
//            if(field.equals(name)){  
//                return true;  
//            }  
//        }
//        return false;
    	return fields.contains(name);
    }
	public BasePropertyFilter( Collection<String> includeList, Collection<String> excludeList) {
		super();
		this.excludeList = excludeList;
		this.includeList = includeList;
	}
      
    
}  