import java.util.ArrayList;
import java.util.PriorityQueue;

/**
 * Models a FirstComeFirstServed class to 
 * @author yen_my_huynh
 *
 */
public class FirstComeFirstServed {
	protected PriorityQueue<ProcessSimulator> processQueue;
	protected ArrayList<ProcessSimulator> processQueueTrack;
	private ArrayList<String> outputListing;
	private int quanta;
	
	/**
	 * Constructs objects for First-come first-served class.
	 * @param processQueues the priority queue list of processes (size: 100)
	 */
	public FirstComeFirstServed(PriorityQueue<ProcessSimulator> processQueue){
		this.processQueue = processQueue;
		this.processQueueTrack = new ArrayList<>();
		this.outputListing = new ArrayList<>();
		this.quanta = 0;
	}
	
	/**
	 * Runs FCFS scheduling algorithm.
	 */
	public void runFCFS(){
		boolean processFinished = false;
		boolean firstProcess =  true;
		ProcessSimulator currentProcess;
		
		// runs when all processes are not all finished and is less than 100 quantum.
		while (!processFinished && quanta < 100){
			currentProcess = processQueue.poll();
			
			// runs and waits until the process arrives and increases quanta during the wait.
			while (currentProcess.getArrivalTime() > quanta){
				quanta++;
			}
			
			// Runs the current process until the end of expected run time.
			// if 5s is given, then runs until reaches 5s.
			int runTime = (int) currentProcess.getExpectedRunTime();
			int i = 0;
			while (i != runTime){
				i++;
				if (i == runTime){
					currentProcess.setProcessCompleted(false);
					processQueueTrack.add(currentProcess);
				}
			}
			runTime = 0;
			
			// if it's the first process, then run. After that, sets it to false, so other processes 
			// after it can run.
			if (firstProcess == true){ 
				currentProcess.setFinishedTime(currentProcess.getArrivalTime(), currentProcess.getExpectedRunTime());
				currentProcess.setTurnAroundTime(currentProcess.getFinishedTime(), currentProcess.getArrivalTime());
				currentProcess.setWaitingTime(currentProcess.getTurnAroundTime(), currentProcess.getExpectedRunTime());
				currentProcess.setResponseTime(currentProcess.getWaitingTime());
				firstProcess = false;
			}	
			// processes after the first one
			else {
				currentProcess.setFinishedTime(quanta, currentProcess.getExpectedRunTime());
				currentProcess.setTurnAroundTime(currentProcess.getFinishedTime(), currentProcess.getArrivalTime());
				currentProcess.setWaitingTime(currentProcess.getTurnAroundTime(), currentProcess.getExpectedRunTime());
				currentProcess.setResponseTime(currentProcess.getWaitingTime());
			}
			
			// sets quanta as the new arrival time for the next process.
			quanta = (int) currentProcess.getFinishedTime();
			
			// loops through the PriorityQueue list to check if they're done or not.
			// Not all processes will run since the simulator only has 100 quantum
			for (ProcessSimulator p : processQueue) {
				processFinished = p.isProcessCompleted();
			}
		}
		printStatistics(processQueueTrack);
	}
	
	/**
	 * Prints out the FCFS scheduling algorithm statistics: average turn around time,
	 * waiting time, and response time.
	 * @param processQueueTrack the track of all processes that ran successfully
	 */
	public void printStatistics(ArrayList<ProcessSimulator> processQueueTrack){
		float turnAroundTimeTotal = 0;
		float waitingTimeTotal = 0;
		float responseTimeTotal = 0;
		String timeChart = "";
		int count = 0;
		
		for (ProcessSimulator p : processQueueTrack){
			turnAroundTimeTotal += p.getTurnAroundTime();
			waitingTimeTotal += p.getWaitingTime();
			responseTimeTotal += p.getResponseTime();
			count++;
			
			if (count == 10){
				timeChart += "\n";
				count = 0;
			}
		
			outputListing.add(p.toString());
		}
		// gathers up all the statistics
		float averageTurnAroundTime = turnAroundTimeTotal/ processQueue.size();
		float averageWaitingTime = waitingTimeTotal/ processQueue.size();
		float averageResponseTime = responseTimeTotal/ processQueue.size();
		// casts throughtput to avoid truncating
		float throughput = (float) processQueueTrack.size()/ 100;
		String timeChartDisplay = "\n" + "Time Chart:" + timeChart;
		outputListing.add(timeChartDisplay);
		timeChartDisplay = "Average Turnaround Time: " + averageTurnAroundTime + "\tAverage Waiting Time: "
				+ averageWaitingTime + "\tAverage Response Time: " + averageResponseTime + "\tThroughput: "
				+ throughput + "\n";
		outputListing.add(timeChartDisplay);
	}
	
	/**
	 * Gets the output listing.
	 * @return the output listing
	 */
	public ArrayList<String> getOutputListing(){
		return outputListing;
	}
}
