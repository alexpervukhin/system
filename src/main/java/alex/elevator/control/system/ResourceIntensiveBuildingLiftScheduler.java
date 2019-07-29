package alex.elevator.control.system;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import alex.elevator.control.system.enums.LiftState;
import alex.elevator.control.system.enums.TimeOfTheDay;
import io.vavr.Tuple2;
import io.vavr.control.Option;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourceIntensiveBuildingLiftScheduler implements IBuildingLiftScheduler {

	public static final Integer FIRST_FLOOR = 1;

	public ResourceIntensiveBuildingLiftScheduler(int floors, List<Tuple2<Integer, Integer>> workQueue,
			TimeOfTheDay timeOfTheDay) {
		this.floors = floors;
		this.workQueue = workQueue;
		this.timeOfTheDay = timeOfTheDay;

	}

	private int floors;
	private TimeOfTheDay timeOfTheDay;
	private List<Tuple2<Integer, Integer>> workQueue;

	Integer lastUpwardLiftAssigned = null;
	Integer lastDownwardLiftAssigned = null;

	private Predicate<Tuple2<Integer, Integer>> goingUp = task -> (task._1 < task._2);

	private Predicate<Tuple2<Integer, Integer>> goingDown = task -> (task._1 > task._2);

	private Predicate<Lift> onTheFirstFloor = lift -> (lift.getCurrentFloor() == FIRST_FLOOR);

	private Predicate<Lift> onTheLastFloor = lift -> (lift.getCurrentFloor() == floors);

	private List<Lift> getAvailableUpwardLifts(List<Lift> lifts) {
		return lifts.stream().filter(v -> (v.getState() == LiftState.IDLE && onTheFirstFloor.test(v)))
				.collect(Collectors.toList());
	}

	private List<Lift> getAvailableDownwardLifts(List<Lift> lifts) {
		return lifts.stream().filter(v -> (v.getState() == LiftState.IDLE && onTheLastFloor.test(v)))
				.collect(Collectors.toList());
	}

	Lift findNextEligibleUpwardLift(List<Lift> availableUpwardLifts) {
		if(lastUpwardLiftAssigned == null) 
			lastUpwardLiftAssigned = 0;
		else {
			lastUpwardLiftAssigned++;
			if(lastUpwardLiftAssigned == availableUpwardLifts.size())
				lastUpwardLiftAssigned = 0;
		}
		return availableUpwardLifts.get(lastUpwardLiftAssigned);
	}
			
	Lift findNextEligibleDownwardLift(List<Lift> availableDownwardLifts) {
		if(lastDownwardLiftAssigned == null) 
			lastDownwardLiftAssigned = 0;
		else {
			lastDownwardLiftAssigned++;
			if(lastDownwardLiftAssigned == availableDownwardLifts.size())
				lastDownwardLiftAssigned = 0;
		}
		return availableDownwardLifts.get(lastDownwardLiftAssigned);
	}
	
	public void scheduleLifts(List<Lift> lifts) {

		List<Lift> availableUpwardLifts = getAvailableUpwardLifts(lifts);
		List<Lift> availableDownwardLifts = getAvailableDownwardLifts(lifts);

		for (Tuple2<Integer, Integer> task : workQueue) {
			if(goingUp.test(task) && !availableUpwardLifts.isEmpty())
				assignTaskAndDestinationFloor(findNextEligibleUpwardLift(availableUpwardLifts), task);
			else
				assignTaskAndDestinationFloor(findNextEligibleDownwardLift(availableDownwardLifts), task);
		}
		
		adjustForTrafficHour(lifts);
	}

	private void assignTaskAndDestinationFloor(Lift lift, Tuple2<Integer, Integer> task) {
		if(lift.getRunPlan().isEmpty())
			lift.setRunPlan(Option.of(new ArrayList<Tuple2<Integer, Integer>>()));
		lift.getRunPlan().get().add(task);
		
		if(goingUp.test(task)) {
			lift.setStartFloor(FIRST_FLOOR);
			lift.setEndFloor(floors);
		}else {
			lift.setStartFloor(floors);
			lift.setEndFloor(FIRST_FLOOR);
		}
		
		System.out.println("assigned task " + task + " to " + lift);	
	}
	
	private void adjustForTrafficHour(List<Lift> lifts) {
		for (Lift lift : lifts) {
			if(!lift.getRunPlan().isEmpty()) {
				if(timeOfTheDay == TimeOfTheDay.MORNING)
					lift.setAfterDoneGoToFloor(FIRST_FLOOR);
				else
					if(timeOfTheDay == TimeOfTheDay.EVENING)
						lift.setAfterDoneGoToFloor(floors);
			}
		}
	}
}