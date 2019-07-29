package alex.elevator.control.system;

import java.util.List;

import alex.elevator.control.system.enums.LiftState;
import alex.elevator.control.system.enums.TimeOfTheDay;
import io.vavr.Tuple;
import io.vavr.control.Option;
import junit.framework.TestCase;

public class ResourceIntensiveBuildingLiftControllerTest extends TestCase {

	public void test_basic_setup () {

		System.out.println("***** test_basic_setup");

		List<Lift> lifts = List.of(
				new Lift(null, null, 1, null, LiftState.IDLE),	//1st floor, available for pickup 
				new Lift(null, null, 1, null, LiftState.IDLE), 	//1st floor, available for pickup
				new Lift(null, null, 10, null, LiftState.IDLE),	//high floor, available for pickup
				new Lift(null, null, 1, null, LiftState.UP));	//busy, not available		

		ResourceIntensiveBuildingLiftScheduler scheduler = new ResourceIntensiveBuildingLiftScheduler(10,  
				List.of(Tuple.of(5, 1), Tuple.of(3,2), Tuple.of(1,10)), TimeOfTheDay.EVENING);
		
		scheduler.scheduleLifts(lifts);
		
		lifts.stream().forEach(v->{
			if(!v.getRunPlan().isEmpty()) {
				System.out.println(v);
				System.out.println(v.printRunPlan());
			}
		});

		//trying to move or not if done all assigned lifts N times
		for (int i=0;i<20;i++) {
			for (Lift lift : lifts) {
				if(lift.getStartFloor()!=null) {
					lift.run();
				}
			}
		}
		
		System.out.println("***** results:\n");
		
		for (Lift lift : lifts) {
				System.out.println(lift.hashCode() + " - " + lift + " went floors: " + lift.getFloorsTraveled() + "\n");
		}
		
		System.out.println("***** done");
	}
}
