package alex.elevator.control.system;

import java.util.List;

import alex.elevator.control.system.enums.LiftState;
import io.vavr.Tuple;
import io.vavr.control.Option;
import junit.framework.TestCase;

/**
 * Unit test for simple World.
 */
public class LiftTest extends TestCase {

	private Lift lift = null;
	
	public void test_going_up_from_the_first_floor () {

		//[start floor, end floor, current floor, then go to, lift state]
		lift = new Lift(1, 10, 1, 10, LiftState.IDLE);

		lift.setRunPlan(Option.of(List.of(
    			Tuple.of(1, 10), Tuple.of(2,9))));

		System.out.println("***** test_going_up_from_the_first_floor");
		System.out.println(lift);
		System.out.println(lift.printRunPlan());
		
		assertFalse(lift.isFinished());
		assertEquals(LiftState.IDLE, lift.getState());
		assertTrue(lift.isReachedStartFloor());

		lift.run();
		assertEquals(LiftState.UP, lift.getState());

		for (int i=0;i<10;i++)
			lift.run();

		assertTrue(lift.isFinished());
		assertEquals(Option.none(), lift.getRunPlan());
		assertEquals(LiftState.IDLE, lift.getState());
		
		System.out.println("***** traveled floors: " + lift.getFloorsTraveled());
	}
	
	public void test_going_up_from_the_first_floor_AFTER_DONE_return_to_first_floor () {

		//[start floor, end floor, current floor, then go to, lift state]
		lift = new Lift(1, 10, 1, 1, LiftState.IDLE);

		lift.setRunPlan(Option.of(List.of(
    			Tuple.of(1, 10), Tuple.of(2,9))));

		System.out.println("***** test_going_up_from_the_first_floor_AFTER_DONE_return_to_first_floor");
		System.out.println(lift);
		System.out.println(lift.printRunPlan());
		
		assertFalse(lift.isFinished());
		assertEquals(LiftState.IDLE, lift.getState());
		assertTrue(lift.isReachedStartFloor());

		lift.run();
		assertEquals(LiftState.UP, lift.getState());

		for (int i=0;i<20;i++)
			lift.run();

		assertEquals(Option.none(), lift.getRunPlan());
		assertEquals(LiftState.IDLE, lift.getState());
		
		System.out.println("***** traveled floors: " + lift.getFloorsTraveled());
	}
	
	public void test_going_down_from_8th_floor () {

		//[start floor, end floor, current floor, then go to, lift state]
		lift = new Lift(8, 1, 8, 1, LiftState.IDLE);
		lift.setRunPlan(Option.of(List.of(
    			Tuple.of(5, 1), Tuple.of(3,2))));

		System.out.println("***** test_going_down_from_8th_floor");
		System.out.println(lift);
		System.out.println(lift.printRunPlan());
		
		assertFalse(lift.isFinished());
		assertEquals(LiftState.IDLE, lift.getState());
		assertTrue(lift.isReachedStartFloor());

		lift.run();
		assertEquals(LiftState.DOWN, lift.getState());

		for (int i=0;i<10;i++)
			lift.run();

		assertTrue(lift.isFinished());
		assertEquals(Option.none(), lift.getRunPlan());
		assertEquals(LiftState.IDLE, lift.getState());
		
		System.out.println("***** traveled floors: " + lift.getFloorsTraveled());
	}
	
	public void test_picking_passengers_downward_being_on_the_1st_floor () {

		//[start floor, end floor, current floor, then go to, lift state]
		lift = new Lift(8, 1, 1, 1, LiftState.IDLE);
		lift.setRunPlan(Option.of(List.of(
    			Tuple.of(5, 1), Tuple.of(3,2))));

		System.out.println("***** test_picking_passengers_downward_being_on_the_1st_floor");
		System.out.println(lift);
		System.out.println(lift.printRunPlan());
    	
		for (int i=0;i<15;i++)
			lift.run();

		assertEquals(LiftState.IDLE, lift.getState());
		assertTrue(lift.isFinished());
		assertEquals(Option.none(), lift.getRunPlan());
		
		System.out.println("---------- traveled floors: " + lift.getFloorsTraveled() + " ----------");
	}
}
