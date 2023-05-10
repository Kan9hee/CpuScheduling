import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class View {

    public GanttChart ganttChart;

    public View(){
        Dimension dim=new Dimension(650, 400);
        JFrame frame=new JFrame("Test");
        SchedulingFunction sFunction=new SchedulingFunction();
        GanttChart ganttChart=new GanttChart(sFunction);
        OrderPanel orderPanel=new OrderPanel(ganttChart,sFunction);
        frame.setLocation(200,200);
        frame.setPreferredSize(dim);
        frame.add(orderPanel,BorderLayout.NORTH);
        frame.add(ganttChart,BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}

class OrderPanel extends JPanel{
    private GridLayout grid;
    private JButton FCFS_B=new JButton("FCFS");
	private JButton SJF_B=new JButton("SJF");
	private JButton HRN_B=new JButton("HRN");
	private JButton NPP_B=new JButton("NonPreemptivePriority");
	private JButton PP_B=new JButton("PreemptivePriority");
    private JButton RR_B=new JButton("RR");
    private JButton SRT_B=new JButton("SRT");
    private SchedulingFunction sFunction;
    private GanttChart ganttChart;
    

    public OrderPanel(GanttChart ganttChart,SchedulingFunction sFunction){
        grid=new GridLayout(1,7);
		grid.setHgap(5);
		setLayout(new FlowLayout());
        this.sFunction=sFunction;
        this.ganttChart=ganttChart;

        FCFS_B.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    sFunction.FCFS();
                    ganttChart.repaint();
                } catch (CloneNotSupportedException e1) {
                    e1.printStackTrace();
                }
            }
        });
        SJF_B.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    sFunction.SJF();
                    ganttChart.repaint();
                } catch (CloneNotSupportedException e1) {
                    e1.printStackTrace();
                }
            }
        });
        HRN_B.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    sFunction.HRN();
                    ganttChart.repaint();
                } catch (CloneNotSupportedException e1) {
                    e1.printStackTrace();
                }
            }
        });
        NPP_B.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    sFunction.NonPreemptivePriority();
                    ganttChart.repaint();
                } catch (CloneNotSupportedException e1) {
                    e1.printStackTrace();
                }
            }
        });
        PP_B.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    sFunction.PreemptivePriority();
                    ganttChart.repaint();
                } catch (CloneNotSupportedException e1) {
                    e1.printStackTrace();
                }
            }
        });
        RR_B.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    sFunction.RR();
                    ganttChart.repaint();
                } catch (CloneNotSupportedException e1) {
                    e1.printStackTrace();
                }
            }
        });
        SRT_B.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    sFunction.SRT();
                    ganttChart.repaint();
                } catch (CloneNotSupportedException e1) {
                    e1.printStackTrace();
                }
            }
        });
        add(FCFS_B);
        add(SJF_B);
        add(HRN_B);
        add(NPP_B);
        add(PP_B);
        add(RR_B);
        add(SRT_B);
    }
}

class GanttChart extends JPanel{
    SchedulingFunction sFunction;
    public GanttChart(SchedulingFunction sFunction){
        this.sFunction=sFunction;
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        int nextWidth = 35;
        int processInterval = 200;
        int dataInterval = 80;

        if (sFunction != null && sFunction.timeLine != null){
            for(ProcessData data:sFunction.timeLine){
                setProcessColor(g,data);
                g.fillRect(nextWidth, 50, data.getExecutionTime()*9, 20);
                nextWidth+=data.getExecutionTime()*9;
                g.setColor(Color.BLACK);
                g.drawString(Integer.toString(data.getTurnaroundTime()), nextWidth-5, 80);
            }

            for(ProcessData data:sFunction.calculatedDataList){
                setProcessColor(g,data);
                g.drawString(data.getProcessName(),processInterval,dataInterval+=40);
                g.drawString(Integer.toString(data.getWaitingTime()),processInterval,dataInterval+=40);
                g.drawString(Integer.toString(data.getResponseTime()),processInterval,dataInterval+=40);
                g.drawString(Integer.toString(data.getTurnaroundTime()),processInterval,dataInterval+=40);
                dataInterval=80;
                processInterval+=35;
            }

            g.setColor(Color.BLACK);
            g.drawString("AWT: " + Double.toString(sFunction.awt),200,dataInterval+=100);
            g.drawString("ART: " + Double.toString(sFunction.art),200,dataInterval+=40);
            g.drawString("ATT: " + Double.toString(sFunction.att),200,dataInterval+=40);
        }
    }
    private void setProcessColor(Graphics g, ProcessData data){
        switch(data.getProcessName()){
            case"P1":g.setColor(Color.BLUE);
                    break;
            case"P2":g.setColor(Color.decode("#3CB371"));
                    break;
            case"P3":g.setColor(Color.ORANGE);
                    break;
            case"P4":g.setColor(Color.MAGENTA);
                    break;
            case"P5":g.setColor(Color.RED);
                    break;
        }
    }
}
