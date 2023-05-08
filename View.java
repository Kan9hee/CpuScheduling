import java.awt.*;
import javax.swing.*;

public class View {

    public View(){
        Dimension dim=new Dimension(400, 400);
        JFrame frame=new JFrame("Test");
        GanttChart ganttChart = new GanttChart();
        frame.setLocation(200,200);
        frame.setPreferredSize(dim);
        frame.add(ganttChart);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}

class GanttChart extends JPanel{

    SchedulingFunction sFunction=new SchedulingFunction();

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        int nextWidth = 35;

        try {
            sFunction.SRT();
            for(int step=0;step<sFunction.timeLine.size();step++){
                switch(sFunction.timeLine.get(step).getProcessName()){
                    case"P1":g.setColor(Color.BLUE);
                            break;
                    case"P2":g.setColor(Color.GREEN);
                            break;
                    case"P3":g.setColor(Color.ORANGE);
                            break;
                    case"P4":g.setColor(Color.MAGENTA);
                            break;
                    case"P5":g.setColor(Color.RED);
                            break;
                }
                g.fillRect(nextWidth, 100, sFunction.timeLine.get(step).getExecutionTime()*5, 20);
                nextWidth+=sFunction.timeLine.get(step).getExecutionTime()*5;
                g.setColor(Color.BLACK);
                g.drawString(Integer.toString(sFunction.timeLine.get(step).getTurnaroundTime()), nextWidth-5, 140);
            }
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }
}
