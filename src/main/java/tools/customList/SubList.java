package tools.customList;

import java.util.ArrayList;

public class SubList<T> {
    ArrayList<T> list;
    String thread_name;

    public SubList (String thread_name){
        list = new ArrayList<>();
        this.thread_name = thread_name;
    }

    public void add(T new_obj) {
        list.add(new_obj);
    }
}
