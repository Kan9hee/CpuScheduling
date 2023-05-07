import java.io.*;
import java.util.*;

public class Function {

	public List<ProcessData> timeLine;

	private Queue<ProcessData> dataList = new PriorityQueue<>(Comparator.comparing(ProcessData::getArrivalTime));
	private Queue<ProcessData> copyList;
	private List<ProcessData> calculatedDataList;
	private ProcessData firstCome, tempData;
	private int currentTime = 0, waitingSum = 0, responseSum = 0, turnaroundSum = 0;
	private double awt, art, att, highPriority;

	public Function() {
		File processList = new File("dataSet.txt");
		String readLine;
		String[] splitData;

		try {
			processList.createNewFile();
			FileInputStream inputStream = new FileInputStream(processList);
			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "MS949"));
			while ((readLine = br.readLine()) != null) {
				splitData = readLine.split("\t");
				dataList.add(new ProcessData(splitData));
			}
			br.close();
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void FCFS() throws CloneNotSupportedException {
		init(null);
		int loop = copyList.size();

		for (int i = 0; i <= loop; i++) {
			if (i == 0)
				tempData = new ProcessData(firstCome);
			else
				tempData = new ProcessData(copyList.poll());
			calculateTime(tempData);
			timeLine.add(tempData);
		}

		averageResult();
	}

	public void SJF() throws CloneNotSupportedException {
		init(Comparator.comparing(ProcessData::getExecutionTime));
		int loop = copyList.size();

		for (int i = 0; i <= loop; i++) {
			if (i == 0)
				tempData = new ProcessData(firstCome);
			else
				tempData = new ProcessData(copyList.poll());
			calculateTime(tempData);
			timeLine.add(tempData);
		}

		averageResult();
	}

	public void HRN() throws CloneNotSupportedException {
		init(Comparator.comparing(ProcessData::getPriority, Comparator.reverseOrder()));
		int loop = copyList.size();
		double HRNPriority = 0;

		for (int i = 0; i <= loop; i++) {
			if (i == 0)
				tempData = new ProcessData(firstCome);
			else
				tempData = new ProcessData(copyList.poll());
			calculateTime(tempData);
			timeLine.add(tempData);
			if (i < loop - 1) {
				List<ProcessData> local = new ArrayList<>();
				for (int t = 0; t < loop - i; t++) {
					tempData = new ProcessData(copyList.poll());
					tempData.setWaitingTime(currentTime - tempData.getArrivalTime());
					HRNPriority = (tempData.getWaitingTime() + tempData.getExecutionTime())
							/ (double) tempData.getExecutionTime();
					tempData.setPriority(HRNPriority);
					local.add(tempData);
				}
				copyList.clear();
				copyList.addAll(local);
			}
		}

		averageResult();
	}

	public void NonPreemptivePriority() throws CloneNotSupportedException {
		init(Comparator.comparing(ProcessData::getPriority).thenComparing(ProcessData::getExecutionTime));
		int loop = copyList.size();

		for (int i = 0; i <= loop; i++) {
			if (i == 0)
				tempData = new ProcessData(firstCome);
			else
				tempData = new ProcessData(copyList.poll());
			calculateTime(tempData);
			timeLine.add(tempData);
		}

		averageResult();
	}

	public void PreemptivePriority() throws CloneNotSupportedException {
		init(Comparator.comparing(ProcessData::getWaitingTime).thenComparing(ProcessData::getArrivalTime)
				.thenComparing(ProcessData::getPriority));

		for (int i = 0;; i++) {
			if (copyList.isEmpty())
				break;
			if (i == 0)
				tempData = new ProcessData(relocationAccordingPriority(firstCome));
			else
				tempData = new ProcessData(relocationAccordingPriority(copyList.poll()));
			calculateTime(tempData);
			timeLine.add(tempData);
			if (tempData.getPriority() == highPriority)
				break;
		}

		if (!copyList.isEmpty()) {
			PriorityQueue<ProcessData> afterList = new PriorityQueue<>(
					Comparator.comparing(ProcessData::getPriority).thenComparing(ProcessData::getExecutionTime,
							Comparator.reverseOrder()));
			afterList.addAll(copyList);
			while (!afterList.isEmpty()) {
				tempData = new ProcessData(afterList.poll());
				if(tempData.getTurnaroundTime()==0)
					tempData.setTurnaroundTime(tempData.getArrivalTime()+1);
				else
					tempData.setTurnaroundTime(+tempData.getTurnaroundTime()+1);
				calculateTime(tempData);
				timeLine.add(tempData);
			}
		}

		averageResult();
	}

	public void RR() throws CloneNotSupportedException {
		init(null);

		for (int i = 0;; i++) {
			if (copyList.isEmpty())
				break;
			if (i == 0)
				tempData = new ProcessData(relocationRR(firstCome));
			else
				tempData = new ProcessData(relocationRR(copyList.poll()));
			calculateTime(tempData);
			timeLine.add(tempData);
		}

		averageResult();
	}

	public void SRT() throws CloneNotSupportedException {
		init(Comparator.comparing(ProcessData::getExecutionTime));

		for (int i = 0;; i++) {
			if (copyList.isEmpty())
				break;
			if (i == 0)
				tempData = new ProcessData(relocationSRT(firstCome));
			else
				tempData = new ProcessData(relocationSRT(copyList.poll()));
			calculateTime(tempData);
			timeLine.add(tempData);
		}

		averageResult();
	}

	private ProcessData relocationRR(ProcessData data) {
		ProcessData temp = new ProcessData(data);

		if (data.getExecutionTime() > 10) {
			data.setExecutionTime(data.getExecutionTime() - 10);
			temp.setExecutionTime(10);
			copyList.add(data);
		}

		return temp;
	}

	private ProcessData relocationSRT(ProcessData data) {
		ProcessData temp = new ProcessData(data);

		if(!copyList.isEmpty()){
			if (data.getExecutionTime() > 10 && copyList.peek().getExecutionTime()<data.getExecutionTime()-10) {
				data.setExecutionTime(data.getExecutionTime() - 10);
				temp.setExecutionTime(10);
				copyList.add(data);
			}
		}

		return temp;
	}

	private ProcessData relocationAccordingPriority(ProcessData data) {
		List<ProcessData> tempList = new ArrayList<>();
		ProcessData temp = new ProcessData(data);

		while (!copyList.isEmpty()) {
			if (copyList.peek().getPriority() < data.getPriority())
				break;
			else
				tempList.add(copyList.poll());
		}

		if (copyList.isEmpty()) {
			copyList.addAll(tempList);
			return temp;
		}

		data.setExecutionTime(data.getExecutionTime() + data.getArrivalTime() - copyList.peek().getArrivalTime());
		temp.setExecutionTime(temp.getExecutionTime() - data.getExecutionTime());
		tempList.add(data);

		for (int i = 0; i < tempList.size(); i++)
			tempList.get(i).setWaitingTime(tempList.get(i).getWaitingTime()+1);
		copyList.addAll(tempList);
		
		return temp;
	}

	private void calculateTime(ProcessData tempData) {
		boolean foundName = false;

		if (tempData.getArrivalTime() > currentTime)
			currentTime = tempData.getArrivalTime();
		
		if(tempData.getWaitingTime()==0)
			tempData.setWaitingTime(currentTime - tempData.getArrivalTime());
		else
			tempData.setWaitingTime(currentTime - tempData.getTurnaroundTime());

		if (tempData.getReactionPoint() > tempData.getExecutionTime())
			tempData.setReactionPoint(tempData.getReactionPoint() - tempData.getExecutionTime());
		else
			tempData.setResponseTime(currentTime + tempData.getReactionPoint());

		currentTime += tempData.getExecutionTime();
		tempData.setTurnaroundTime(currentTime);

		for (ProcessData obj : calculatedDataList) {
			if (obj.getProcessName().equals(tempData.getProcessName())) {
				foundName = true;
				obj.setWaitingTime(obj.getWaitingTime()+tempData.getWaitingTime());
				obj.setResponseTime(tempData.getResponseTime());
				obj.setTurnaroundTime(tempData.getTurnaroundTime());
				break;
			}
		}

		if (!foundName)
			calculatedDataList.add(tempData);
	}

	private void averageResult() {
		calculatedDataList.sort(Comparator.comparing(ProcessData::getProcessName));

		for (ProcessData obj : calculatedDataList) {
			waitingSum += obj.getWaitingTime();
			responseSum += obj.getResponseTime();
			turnaroundSum += obj.getTurnaroundTime();
		}

		awt = waitingSum / (double) dataList.size();
		art = responseSum / (double) dataList.size();
		att = turnaroundSum / (double) dataList.size();

		for (ProcessData obj : calculatedDataList)
			System.out.println(obj.getProcessName()+" waiting time: "+obj.getWaitingTime());
		System.out.println("AWT: " + awt);

		for (ProcessData obj : calculatedDataList)
			System.out.println(obj.getProcessName()+" response time: "+obj.getResponseTime());
		System.out.println("ART: " + art);
		
		for (ProcessData obj : calculatedDataList)
			System.out.println(obj.getProcessName()+" turnaround time: "+obj.getTurnaroundTime());
		System.out.println("ATT: " + att);
	}

	private void init(Comparator condition) throws CloneNotSupportedException {
		timeLine = new ArrayList<>();
		calculatedDataList = new ArrayList<>();
		calculatedDataList.addAll(dataList);
		firstCome = calculatedDataList.get(0);
		calculatedDataList.remove(0);

		if (condition != null)
			copyList = new PriorityQueue<>(condition);
		else
			copyList=new LinkedList<>();

		copyList.addAll(calculatedDataList);

		currentTime = waitingSum = responseSum = turnaroundSum = 0;
		awt = art = att = 0.0;

		highPriority = Collections.min(dataList, new PriorityComparator()).getPriority();
	}
}

class PriorityComparator implements Comparator<ProcessData> {
	@Override
	public int compare(ProcessData p1, ProcessData p2) {
		return Double.compare(p1.getPriority(), p2.getPriority());
	}
}