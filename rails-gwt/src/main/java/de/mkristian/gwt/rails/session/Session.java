package de.mkristian.gwt.rails.session;

import java.util.Set;


public class Session<T> {

    public int idle_session_timeout;
    
    public T user;
    
    public Set<Permission> permissions;
    
//    //TODO ignore this
//    protected Map<String, Set<String>> map;
//   
//    public boolean isAllowed(String resource, RestfulAction action){
//        Map<String, Set<String>> map = map();
//        if(map.containsKey(resource)){
//            return map.get(resource).contains(action.name());
//        }
//        return false;
//    }
//    
//    private Map<String, Set<String>> map(){
//        if(map == null){
//            map = new HashMap<String, Set<String>>();
//            for(Permission p: permissions){
//                Set<String> actions = new TreeSet<String>();
//                for(Action a: p.actions){
//                    actions.add(a.name);
//                }
//                map.put(p.resource, actions);
//            }
//        }
//        return map;
//    }
}
