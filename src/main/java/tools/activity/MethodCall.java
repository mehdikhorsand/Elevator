package tools.activity;

public class MethodCall extends Action {
    public MethodCall(StackTraceElement element) {
        super(element);
    }

    @Override
    public String toString(){
        return method.toString();
    }

    @Override
    public boolean can_be_characteristic() {
        return true;
    }
}
