package randomRequest;

import tools.MyRandom;

public class RequestFromElevator extends RequestInput {
    int elevator_id;
    int request_floor;

    public RequestFromElevator(int elevator_quantity, int floor_quantity) {
        elevator_id = MyRandom.getInt(0, elevator_quantity-1);
        request_floor = MyRandom.getInt(0, floor_quantity-1);
    }

    @Override
    public String get_text() {
        return "RequestStop " + request_floor + " " + elevator_id;
    }
}
