package tools.customList;

import java.util.ArrayList;
import java.util.Objects;

public class MultiList<T> {
    ArrayList<SubList<T>> list;

    public MultiList () {
        list = new ArrayList<>();
    }

    public void add(T new_obj) {
        String thread_name = Thread.currentThread().getName();
        SubList<T> related_sublist = null;
        for(SubList<T> sub_list : list) {
            if(Objects.equals(sub_list.thread_name, thread_name)) {
                related_sublist = sub_list;
                break;
            }
        }
        if(related_sublist == null) {
            related_sublist = new SubList<>(thread_name);
            list.add(related_sublist);
        }
        related_sublist.add(new_obj);
    }

    public ArrayList<T> read_list() {
        ArrayList<T> output = new ArrayList<>();
        for(SubList<T> sub_list : list) {
            output.addAll(new ArrayList<>(sub_list.list));
        }
        return output;
    }
}
