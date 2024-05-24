package randomRequest;

import tools.MyRandom;

public class RequestFromFloor extends RequestInput {
    int floor_id;
    boolean isUp; // 1 for up and -1 for down

    public RequestFromFloor(int floor_quantity) {
        floor_id = MyRandom.getInt(0, floor_quantity-1);
        isUp = MyRandom.getBoolean();
    }

    @Override
    public String get_text() {
        return "Request" + ((isUp)? "Up":"Down") + " " + floor_id;
    }
}
