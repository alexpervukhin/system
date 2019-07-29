package alex.elevator.control.system;

import java.util.List;

import alex.elevator.control.system.enums.LiftState;
import io.vavr.Tuple2;
import io.vavr.control.Option;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Lift {
	private Integer currentFloor;
	private LiftState state;

	private Integer startFloor;
	private Integer endFloor;
	private Integer afterDoneGoToFloor;
	
	private Integer floorsTraveled = 0;

	private boolean reachedStartFloor = false;

	private Option<List<Tuple2<Integer, Integer>>> runPlan = Option.none();

	// init Lift, set props
	public Lift(Integer startFloor, Integer endFloor, Integer currentFloor, Integer afterDoneGoToFloor, LiftState state) {
		this.startFloor = startFloor;
		this.endFloor = endFloor;
		this.state = state;
		this.currentFloor = currentFloor;
		this.afterDoneGoToFloor = afterDoneGoToFloor;

		isReachedStartFloor();
	}

	public void run() {

		if (runPlan.isEmpty() || isFinished() && !state.equals(LiftState.IDLE) && reachedStartFloor) {
			state = LiftState.IDLE;
			servicePassengers();
			runPlan = Option.none();
			
			//go to optimal floor for future requests
			if(currentFloor != afterDoneGoToFloor) {
				adjustPositionAccordingToTimeOfADay();
			}else
				System.out.println("... idling ...");

			return;
		}

		move();
	}

	private void move() {

		if (isReachedStartFloor()) {
			
			servicePassengers();

			if (currentFloor < endFloor) {
				currentFloor++;
				state = LiftState.UP;
				System.out.println("moved up one floor to " + currentFloor);
			} else {
				currentFloor--;
				state = LiftState.DOWN;
				System.out.println("moved down one floor to " + currentFloor);
			}
		}else{
			if (currentFloor < startFloor) {
				currentFloor++;
				state = LiftState.UP;
				System.out.println("moved up one floor to " + currentFloor);
			} else {
				currentFloor--;
				state = LiftState.DOWN;
				System.out.println("moved down one floor to " + currentFloor);
			}
		}
		floorsTraveled++;
	}

	public void adjustPositionAccordingToTimeOfADay(){
		if (currentFloor < afterDoneGoToFloor) {
			currentFloor++;
			state = LiftState.UP;
			System.out.println("after done adjusting to rush hour, moved up one floor to " + currentFloor);
		} else {
			currentFloor--;
			state = LiftState.DOWN;
			System.out.println("after done adjusting to rush hour, moved down one floor to " + currentFloor);
		}
		floorsTraveled++;
	}
	
	public boolean isReachedStartFloor() {
		if (!reachedStartFloor)
			reachedStartFloor = currentFloor == startFloor ? true : false;
		return reachedStartFloor;
	}

	public boolean isFinished() {
		return endFloor == currentFloor ? true : false;
	}

	private void servicePassengers() {
		if (!runPlan.isEmpty()) {
			for (Tuple2<Integer, Integer> floorTask : runPlan.get()) {
				if (floorTask._1 == currentFloor)
					System.out.println("collected passenger(s) at floor " + floorTask._1);
				if (floorTask._2 == currentFloor)
					System.out.println("off-loaded passenger(s) at floor " + floorTask._2);
			}
		}
	}
	
	public String toString() {
		return "Lift, currentFloor:" + currentFloor + ", startFloor:" + startFloor + ", endFloor:" + endFloor + ", moveToAfter: "  + 
				afterDoneGoToFloor + ", state=" + state; 
	}

	public String printRunPlan() {
		StringBuffer sb = new StringBuffer();
		sb.append("Task List: ");
		if(runPlan.isEmpty())
			sb.append("empty");
		else 
			for (Tuple2<Integer, Integer> task : runPlan.get())
				sb.append(task + " ");
		return sb.toString();
	}
}
