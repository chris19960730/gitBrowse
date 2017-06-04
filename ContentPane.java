package �ڴ�;


import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import �ڴ�.MemoryRectangle;

public class ContentPane extends JPanel {

	private int MEMORY=640;           //�۴��ܴ�С640k
	private boolean firstAdapt=true;  //true�����״���Ӧ�㷨 false���������Ӧ�㷨 ����֮��Radiobutton������
	private JButton addButton;        //�������ť
	private JButton removeButton;     //�ͷ��ڴ水ť
	private ButtonGroup group=new ButtonGroup();   //���ڹ���ѡ��ͬ�㷨�İ�ť����
	private final int LENGTH=640;         //���εĳ�
	private final int WIDTH=150; 		  //���εĿ�
	private ArrayList<Task> taskList;      //�����
	private ArrayList<MemoryRectangle> usedList;  //ռ���ڴ�ı�
	private ArrayList<MemoryRectangle> freeList;  //�����ڴ�ı�
	private JTextField addText;                  // �������������С�����
	private JComboBox runningTask;               //ɾ�����������ѡ��˵�
	private runtask rt;                          //���ڹ����������е��߳�
	private int taskNum=0;                       //��¼���й�����������
	private JScrollPane sPane; 					 //�������ı�������
	private JTextArea result;                    //����ı�
	private JButton clearAll;                    //���ð�ť
	ContentPane(){                               //��ʼ���������ͼ�ν���
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
		JRadioButton firstButton=new JRadioButton("�״���Ӧ�㷨",true);
		JRadioButton secondButton=new JRadioButton("�����Ӧ�㷨",false);
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
		result=new JTextArea("���Խ��\n");
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
	class Clear implements ActionListener{                      //���ð�ť�ļ����¼� ����ǰ������Ϣ���㣬�ָ���ʼ״̬
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
	class chooseAL implements ActionListener{  //�������㷨�İ�ť�ĳ�ʼ��
		private boolean status;
		 chooseAL(boolean a ){
			status =a;
		}
		public void actionPerformed(ActionEvent e){
			firstAdapt=status;
			
		}
	}
	class add implements ActionListener{         //�������
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
					result.append("��������޷�����\n");
					return;
				}
				count=0;
				taskList.add(new Task(++taskNum,true,size));
				//System.out.print(taskList.get(0).retSize());
				addText.setText(null);
			}catch(Exception aa){}
		}
	}
	
	class remove implements ActionListener{                     //�ͷ�ָ��������ڴ�
		public void actionPerformed(ActionEvent e){
			try{
				//addButton.setVisible(false);
				int id=Integer.parseInt(((String) runningTask.getSelectedItem()).trim());
				//System.out.println(id);
					taskList.add(new Task(false,id));
					
			}catch(Exception aa){}
		}
	}
	class runtask extends Thread{                 //ִ������������߳�
		public void run(){
			while(true){
				if(taskList.isEmpty()==false){
					Task temp=taskList.get(0);
					//System.out.println(temp.retSize());
					
					if(temp.retAllocation()==true){          // ͨ���ж�������е�ÿ������ķ���״̬ѡ�����������������ͷ��ڴ�
						setNewTask(temp.retId(),temp.retSize());
					}
					else{
							freeTask(temp.retId());
					} 	
					repaint();
					taskList.remove(0);
					runningTask.removeAllItems();
					for(int i=0;i<usedList.size();i++){
						runningTask.addItem(""+((usedList.get(i)).retId()));   //������ӵ�����ı�Ŵ�Ž������˵���
					}
				}
				try{
					sleep(1000);
				}catch(Exception e){}
			}
		}
	}

	public void paintComponent(Graphics g){	   //����ͼ����ʾ
		super.paintComponent(g);
		Graphics2D g2D = (Graphics2D) g;
		/*Font f = new Font("SansSerif", Font.BOLD, 15);
		g2D.setFont(f);*/
		Rectangle2D rec = new Rectangle2D.Double(100, 50, WIDTH, LENGTH);
		g2D.setColor(Color.WHITE); 
		g2D.fill(rec);
		g2D.setColor(Color.RED);
		
		for (int i = 0; i < usedList.size(); i++)//�ҵ������������ε��ϵ׺��µף��������������
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

		for (int i = 0; i < usedList.size(); i++)    //���Ʊ߽��� 
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
	
	public void setNewTask(int id,int size){       //���������
		
		int formerBelow=0;
		//int currentUpper=0;
		boolean flag=false;
		for(int i=0;i<freeList.size();i++){
			MemoryRectangle temp= freeList.get(i);
			int currentSize=temp.retLength();
			if(currentSize>=size){           //ֻҪ����ռ䲻С���¼�����ڴ�Ŀռ䣬���ɽ��з���
				 formerBelow=temp.retBelowLevel();
				 if(currentSize>size){
					 temp.setBelowLevel(formerBelow+size);
					 if(!firstAdapt){          //�ڶ����㷨���Կ����ڴ�ռ��������
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
			result.append("�������� "+id+" ,�����СΪ�� "+size+'\n');
		}
	}
	
	public void freeTask(int id){          //�Ƴ�����
		boolean flag=false;
		for(int i=0;i<usedList.size();i++){	
			MemoryRectangle usedRectangle=usedList.get(i);
			if(usedRectangle.retId()==id){                            //�ҵ�Ҫ�ͷŵ��������
				for(int j=0;j<freeList.size();j++){
					MemoryRectangle freeRectangle=(MemoryRectangle) freeList.get(j);
					int b=freeRectangle.retBelowLevel();
					int u=freeRectangle.retUpperLevel();
					int usedB=usedRectangle.retBelowLevel();
					int usedU=usedRectangle.retUpperLevel();
					if(usedB==u+1){                                            //ͨ���Ƚ��ٽ�����ռ���κʹ��ͷŵ�������� ���µĿ�����ν��������ñ߽�
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
						System.out.println("�Ƴ� "+id+" "+usedU+"--"+usedB+"Size= "+(usedB-usedU+1));
						usedList.remove(i);
					
					if(!firstAdapt){
						sortFree(j);
					}
						result.append("�ͷ����� "+id+" ��СΪ�� "+(usedU-usedB+1)+'\n');
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
	
	public void sortFree(int number){           //�ҵ���С�ĺ��ʿռ�
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
	

