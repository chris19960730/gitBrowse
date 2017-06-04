package 内存;


import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import 内存.MemoryRectangle;

public class ContentPane extends JPanel {

	private int MEMORY=640;           //嫩村总大小640k
	private boolean firstAdapt=true;  //true代表首次适应算法 false代表最佳适应算法 用以之后Radiobutton的区分
	private JButton addButton;        //添加任务按钮
	private JButton removeButton;     //释放内存按钮
	private ButtonGroup group=new ButtonGroup();   //用于管理选择不同算法的按钮的组
	private final int LENGTH=640;         //矩形的长
	private final int WIDTH=150; 		  //矩形的宽
	private ArrayList<Task> taskList;      //任务表
	private ArrayList<MemoryRectangle> usedList;  //占用内存的表
	private ArrayList<MemoryRectangle> freeList;  //空闲内存的表
	private JTextField addText;                  // 添加任务的任务大小输入框
	private JComboBox runningTask;               //删除任务的下拉选择菜单
	private runtask rt;                          //用于管理整个运行的线程
	private int taskNum=0;                       //记录运行过的任务数量
	private JScrollPane sPane; 					 //存放输出文本的容器
	private JTextArea result;                    //输出文本
	private JButton clearAll;                    //重置按钮
	ContentPane(){                               //初始化各个表和图形界面
		this.setLayout(null);
		
		taskList=new ArrayList();
		usedList=new ArrayList();
		freeList=new ArrayList();
		clearAll=new JButton("reset");
		clearAll.addActionListener(new Clear());
		clearAll.setBounds(500,575, 100, 50);
		addButton=new JButton("add");
		addButton.addActionListener(new add());
		addText=new JTextField(15);
		removeButton=new JButton("remove");
		removeButton.addActionListener(new remove());
		runningTask=new JComboBox();
		freeList.add(new MemoryRectangle(0,MEMORY));                 
		JRadioButton firstButton=new JRadioButton("首次适应算法",true);
		JRadioButton secondButton=new JRadioButton("最佳适应算法",false);
		firstButton.setBounds(300,200,150,50);
		secondButton.setBounds(300,250,150,50);
		
		firstButton.addActionListener(new chooseAL(true));
		secondButton.addActionListener(new chooseAL(false));
		this.add(firstButton);
		this.add(secondButton);
		group.add(firstButton);
		group.add(secondButton);
		
		addButton.setBounds(500,500,100,50);
		addText.setBounds(650,500,100,50);
		removeButton.setBounds(500,650,100,50);
		runningTask.setBounds(650,650,100,50);
		result=new JTextArea("测试结果\n");
		result.setLineWrap(true);
		
		result.setBounds(500, 200, 220, 200);
			
		sPane=new JScrollPane(result);
		sPane.setBounds(500, 200, 220, 200);
		result.setEditable(false);
		this.add(sPane);
		//this.add(result);
		this.add(addButton);
		this.add(addText);
		this.add(removeButton);
		this.add(runningTask);
		this.add(clearAll);
		rt=new runtask();
		rt.start();
	}
	class Clear implements ActionListener{                      //重置按钮的监听事件 将当前所有信息清零，恢复初始状态
		public void actionPerformed (ActionEvent e){
			freeList.clear();
			freeList.add(new MemoryRectangle(0, MEMORY));
			repaint();
			result.append("All tasks have been removed!\n");
			usedList.clear();
			taskList.clear();
			taskNum = 0;
		}
	}
	class chooseAL implements ActionListener{  //对两个算法的按钮的初始化
		private boolean status;
		 chooseAL(boolean a ){
			status =a;
		}
		public void actionPerformed(ActionEvent e){
			firstAdapt=status;
			
		}
	}
	class add implements ActionListener{         //添加任务
		public void actionPerformed(ActionEvent e){
			
			try{int count=0;
				int size=Integer.parseInt(addText.getText().trim());
				for(int i=0;i<freeList.size();i++){
					if(size<=freeList.get(i).retLength())
					{
						count++;
					}
				}
				if (count==0){
					result.append("输入过大，无法放置\n");
					return;
				}
				count=0;
				taskList.add(new Task(++taskNum,true,size));
				//System.out.print(taskList.get(0).retSize());
				addText.setText(null);
			}catch(Exception aa){}
		}
	}
	
	class remove implements ActionListener{                     //释放指定任务的内存
		public void actionPerformed(ActionEvent e){
			try{
				//addButton.setVisible(false);
				int id=Integer.parseInt(((String) runningTask.getSelectedItem()).trim());
				//System.out.println(id);
					taskList.add(new Task(false,id));
					
			}catch(Exception aa){}
		}
	}
	class runtask extends Thread{                 //执行整个程序的线程
		public void run(){
			while(true){
				if(taskList.isEmpty()==false){
					Task temp=taskList.get(0);
					//System.out.println(temp.retSize());
					
					if(temp.retAllocation()==true){          // 通过判断任务表中的每个任务的分配状态选择添加任务操作或者释放内存
						setNewTask(temp.retId(),temp.retSize());
					}
					else{
							freeTask(temp.retId());
					} 	
					repaint();
					taskList.remove(0);
					runningTask.removeAllItems();
					for(int i=0;i<usedList.size();i++){
						runningTask.addItem(""+((usedList.get(i)).retId()));   //将新添加的任务的标号存放进下拉菜单中
					}
				}
				try{
					sleep(1000);
				}catch(Exception e){}
			}
		}
	}

	public void paintComponent(Graphics g){	   //绘制图形显示
		super.paintComponent(g);
		Graphics2D g2D = (Graphics2D) g;
		/*Font f = new Font("SansSerif", Font.BOLD, 15);
		g2D.setFont(f);*/
		Rectangle2D rec = new Rectangle2D.Double(100, 50, WIDTH, LENGTH);
		g2D.setColor(Color.WHITE); 
		g2D.fill(rec);
		g2D.setColor(Color.RED);
		
		for (int i = 0; i < usedList.size(); i++)//找到新添加任务矩形的上底和下底，并绘制任务矩形
		{
			MemoryRectangle temp =  usedList.get(i);
			int b = temp.retBelowLevel();
			int u = temp.retUpperLevel();
			/*int x = LENGTH * b / MEMORY;
			int y = LENGTH * u / MEMORY;*/
			g2D.fillRect(100, 50 + b, WIDTH, u - b);
			//g2D.setColor(Color.red);
		}
		g2D.setColor(Color.BLACK);
		g2D.draw(rec);

		for (int i = 0; i < usedList.size(); i++)    //绘制边界线 
		{
			MemoryRectangle temp =  usedList.get(i);
			int b = temp.retBelowLevel();
			int u = temp.retUpperLevel();
			/*int x = LENGTH * b / MEMORY;
			int y = LENGTH * u / MEMORY;*/
			g2D.drawLine(100, 50 + u, 100 + WIDTH, 50 + u);
			g2D.drawLine(100, 50 + b, 100 + WIDTH, 50 + b);
			g2D.drawString("Task" + temp.retId(), 170, 50 + b + (u - b) / 2);
		}
			
	}
	
	public void setNewTask(int id,int size){       //添加新任务
		
		int formerBelow=0;
		//int currentUpper=0;
		boolean flag=false;
		for(int i=0;i<freeList.size();i++){
			MemoryRectangle temp= freeList.get(i);
			int currentSize=temp.retLength();
			if(currentSize>=size){           //只要空余空间不小于新加入的内存的空间，即可进行分配
				 formerBelow=temp.retBelowLevel();
				 if(currentSize>size){
					 temp.setBelowLevel(formerBelow+size);
					 if(!firstAdapt){          //第二种算法，对空余内存空间进行重组
						 sortFree(i);
					 }
				 }
				 else{
					 freeList.remove(i);
				 }
				 usedList.add(new MemoryRectangle(id,formerBelow+size-1,formerBelow));
				 flag=true;
				// currentUpper=formerBelow+size-1;
				 break;
				 
			}
		}
		
		if(flag==true){
			result.append("加入任务 "+id+" ,任务大小为： "+size+'\n');
		}
	}
	
	public void freeTask(int id){          //移除任务
		boolean flag=false;
		for(int i=0;i<usedList.size();i++){	
			MemoryRectangle usedRectangle=usedList.get(i);
			if(usedRectangle.retId()==id){                            //找到要释放的任务矩形
				for(int j=0;j<freeList.size();j++){
					MemoryRectangle freeRectangle=(MemoryRectangle) freeList.get(j);
					int b=freeRectangle.retBelowLevel();
					int u=freeRectangle.retUpperLevel();
					int usedB=usedRectangle.retBelowLevel();
					int usedU=usedRectangle.retUpperLevel();
					if(usedB==u+1){                                            //通过比较临近空余空间矩形和待释放的任务矩形 对新的空余矩形进行重设置边界
						freeRectangle.setUpperLevel(usedU);
						flag=true;
					}
					if(usedU==b-1){
						freeRectangle.setBelowLevel(usedB);
						flag=true;
					}
					if(usedU<b-1){
						freeList.add(j,new MemoryRectangle(usedRectangle.retId(),usedRectangle.retUpperLevel(),usedRectangle.retBelowLevel()));
						flag=true;
					}
					if(flag){
						System.out.println("移除 "+id+" "+usedU+"--"+usedB+"Size= "+(usedB-usedU+1));
						usedList.remove(i);
					
					if(!firstAdapt){
						sortFree(j);
					}
						result.append("释放任务 "+id+" 大小为： "+(usedU-usedB+1)+'\n');
						return;
					}
					
				}	
				freeList.add(new MemoryRectangle(usedRectangle.retId(),usedRectangle.retUpperLevel(),usedRectangle.retBelowLevel()));
				if(!firstAdapt){
				//sortFree(j);
					sortFree(freeList.size()-1);
				usedList.remove(i);
				return;
				}
				
			}
		}
	}
	
	public void sortFree(int number){           //找到最小的合适空间
		MemoryRectangle temp=freeList.get(number);
		freeList.remove(number);
		for(int i=0;i<freeList.size();i++){
			if(temp.retLength()<((freeList.get(i)).retLength())){
				freeList.add(i, temp);
				return;
			}
			
		}
		freeList.add(temp);
	}
	}
	

