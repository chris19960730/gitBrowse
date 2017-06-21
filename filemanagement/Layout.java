package file;

import java.awt.Container;
import java.awt.Font;
//import java.awt.Graphics;
//import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.*;

public class Layout {

}
	class ContentPane extends JPanel{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private block Blo;          //模拟块区
		private String route=new String("$$");    
		private int currentBlock;       //当前所在块区的位置
		private JTextField currentDirectory;   //显示目录的文本框
		private JComboBox<String> directory;           //目录列表
		private JComboBox<String> file;				   // 文件列表
		private JTextArea directoryWrite;	   //目录内容
		private JTextArea fileWrite;		   //文件内容
		private JButton createDirectory;       //创建目录
		private JButton deleteDirectory;		//删除目录
		private JButton accessDirectory;		//进入目录
		private JButton exitDirectory;			//返回上一级
		private JButton createFile;  		    //新建文件
		private JButton deleteFile;				//删除文件
		private JButton saveFile;				//保存文件
		private JButton clearFile;				//清空内容
		private JButton accessFile;				//打开文件
		private JButton closeFile;				//关闭文件
		private ArrayList<String> totalDir=new ArrayList<String>();//当前目录下的所有文件夹名
		private ArrayList<String> totalFil=new ArrayList<String>();//当前目录下的所有文件名
		private JTextField dirName;                //目录名字输入框
		private JTextField filName;					//文件名字输入框
		/* ImageIcon icon;  
		 Image img;  
		   
		    public void paintComponent(Graphics g) {     //增加了背景，仅为美观
		        super.paintComponent(g);  
		        //背景图片可以跟随窗口自行调整大小 
		        g.drawImage(img, 0, 0,this.getWidth(), this.getHeight(), this);  
		    }  */
	   
		public ContentPane(){     //界面容器的初始化
			
		        
		       /* icon=new ImageIcon(getClass().getResource("image/test.jpg"));  
		        img=icon.getImage();  
		   */
			this.setLayout(null);
			this.setBackground(getBackground());
			JLabel lb1=new JLabel("目录区");
			lb1.setFont(new Font("黑体",Font.BOLD, 20));
			JLabel lb2=new JLabel("文件区");
			lb2.setFont(new Font("黑体",Font.BOLD, 20));
			
			lb1.setSize(200,50);
			lb1.setLocation(50,0);
			this.add(lb1);
			
			lb2.setSize(200,50);
			lb2.setLocation(700,0);
			this.add(lb2);
			
			JLabel headline=new JLabel("当前所在目录");
			headline.setSize(200,30);
			headline.setLocation(10,500);
			this.add(headline);
			currentDirectory=new JTextField(route,50);
			currentDirectory.setSize(200,20);
			currentDirectory.setLocation(10,550);
			currentDirectory.setEditable(false);
			this.add(currentDirectory);
			
			JButton exit=new JButton("退出并保存");
			exit.addActionListener(new ExitEvent());
			exit.setSize(100,40);
			exit.setLocation(1000,550);
			this.add(exit);
			
			JLabel DirectorySign=new JLabel("文件夹");
			DirectorySign.setSize(100,20);
			DirectorySign.setLocation(10,110);
			this.add(DirectorySign);
			
			directory=new JComboBox<String>();
			directory.addActionListener(new SelectDirectory());
			directory.setSize(200,20);
			directory.setLocation(10,130);
			this.add(directory);
			
			JLabel fileSign=new JLabel("文件名");
			fileSign.setSize(100,20);
			fileSign.setLocation(600,110);
			this.add(fileSign);
			
			dirName=new JTextField(20);
			dirName.setSize(200,30);
			dirName.setLocation(110,70);
			this.add(dirName);
			
			filName=new JTextField(20);
			filName.setSize(200,30);
			filName.setLocation(830,70);
			this.add(filName);
			file=new JComboBox<String>();
			file.addActionListener(new SelectFile());
			file.setSize(200,20);
			file.setLocation(700,130);
			this.add(file);
			
			createDirectory=new JButton("新建目录");
			createDirectory.addActionListener(new CreateDirectory());
			createDirectory.setSize(100,30);
			createDirectory.setLocation(0,70);
			this.add(createDirectory);
			
			deleteDirectory=new JButton("删除目录");
			deleteDirectory.addActionListener(new RemoveDirectory());
			deleteDirectory.setSize(100,30);
			deleteDirectory.setLocation(250,125);
			this.add(deleteDirectory);
			
			accessDirectory=new JButton("进入目录");
			accessDirectory.addActionListener(new AcessDirectory());
			accessDirectory.setSize(100,30);
			accessDirectory.setLocation(360,125);
			this.add(accessDirectory);
			
			exitDirectory=new JButton("返回上级");
			exitDirectory.addActionListener(new ExitDirectory());
			exitDirectory.setSize(100,30);
			exitDirectory.setLocation(470,125);
			this.add(exitDirectory);
			
			createFile=new JButton("新建文件");
			createFile.addActionListener(new CreateFile());
			createFile.setSize(100,30);
			createFile.setLocation(700,70);
			this.add(createFile);
			
			deleteFile=new JButton("删除文件");
			deleteFile.addActionListener(new RemoveFile());
			deleteFile.setSize(100,30);
			deleteFile.setLocation(1280,125);
			this.add(deleteFile);
			
			accessFile=new JButton("打开文件");
			accessFile.addActionListener(new accessFile());
			accessFile.setSize(100,30);
			accessFile.setLocation(950,125);
			this.add(accessFile);
			
			closeFile=new JButton("关闭文件");
			closeFile.addActionListener(new closeFile());
			closeFile.setSize(100,30);
			closeFile.setLocation(1060,125);
			this.add(closeFile);
			
			saveFile=new JButton("保存文件");
			saveFile.addActionListener(new SaveFile());
			saveFile.setSize(100,30);
			saveFile.setLocation(1170,125);
			this.add(saveFile);
			
			clearFile=new JButton("清空内容");
			clearFile.addActionListener(new ClearFile());
			clearFile.setSize(100,30);
			clearFile.setLocation(1390,125);
			this.add(clearFile);
			
			directoryWrite=new JTextArea(8,40);
			directoryWrite.setLineWrap(true);
			directoryWrite.setEditable(false);
			directoryWrite.setSize(550,250);
			directoryWrite.setLocation(10,180);
			this.add(directoryWrite);
			
			fileWrite=new JTextArea(8,40);
			fileWrite.setLineWrap(true);
			fileWrite.setSize(790,250);
			fileWrite.setLocation(700,180);
			this.add(fileWrite);
			
			
			                        //用于将保存在磁盘中的内容重新载入
			try{
				ObjectInputStream in=new ObjectInputStream(new FileInputStream("backup.YHC"));
				Blo=(block) in.readObject();
				in.close();
				Blo.load(totalDir, totalFil);
				for(int i=0;i<totalDir.size();i++){
					directory.addItem(totalDir.get(i));
				}
				
				for(int i=0;i<totalFil.size();i++){
					file.addItem(totalFil.get(i));
				}
				
				if(Blo==null){
					Blo=new block();
					
				}
			}catch (IOException e){
				Blo=new block();
			}catch (ClassNotFoundException ee){
				
			}
		}
		class CreateDirectory implements ActionListener{          //新建目录的callback Function
			public void actionPerformed(ActionEvent e){
				String dName=dirName.getText();             //获取输入目录的名字
				System.out.print(dName);
				dirName.setText(null);
				if(dName==null){
					return;
				}
				else{
					dName=dName.trim();
					if(dName.length()>16){
						JOptionPane.showMessageDialog(null,"目录名不可超过16个字符");
						return;
					}
				}
				if(dName.equals("")){
					JOptionPane.showMessageDialog(null, "目录名不能为空");
					return;
				}
				else{
					for(int i=0;i<totalDir.size();++i){
						if(dName.equals(totalDir.get(i))){
							JOptionPane.showMessageDialog(null, "该目录名已存在");
							return;
						}
					}
					if(Blo.addDirectory(currentBlock, dName)){        //判断是否可以开辟新的文件空间，可以则创建他
						JOptionPane.showMessageDialog(null, "成功创建文件夹"+dName);
						totalDir.add(dName);
						directory.addItem(dName);
					}
				}
				
			}
		}
		
		class CreateFile implements ActionListener{                      //新建文件
			public void actionPerformed(ActionEvent e){
				String fName=filName.getText();
				filName.setText(null);
				if(fName==null){
					return;
				}
				else{
					fName=fName.trim();
					if(fName.length()>16){
						JOptionPane.showMessageDialog(null,"文件名不可超过16个字符");
						return;
					}
				}
				if(fName.equals("")){
					JOptionPane.showMessageDialog(null, "文件名不能为空");
					return;
				}
				else{
					for(int i=0;i<totalFil.size();++i){
						if(fName.equals(totalFil.get(i))){
							JOptionPane.showMessageDialog(null, "该文件名已存在");
							return;
						}
					}
					if(Blo.addFile(currentBlock, fName)){
					JOptionPane.showMessageDialog(null,"成功创建文件"+fName+"    \n请在下方空白区域输入文件内容");
					//System.out.println(txt);
					//txt=txt.trim();
					//fileWrite.setText(txt);
						file.addItem(fName);
						totalFil.add(fName);
					}
				}
			}
		}
		
		class SelectDirectory implements ActionListener{                          //在下拉菜单选择某个目录
			public void actionPerformed(ActionEvent e){
				String dName=(String) directory.getSelectedItem();
				//int temp=currentBlock;
				if(dName!=null){                                 //显示出该文件夹的位置
					
					
					//currentBlock=Blo.enterDirectory(currentBlock, dName, totalDir, totalFil);
						String txt="***************\n";
					
					txt=txt+"文件夹所在位置为：";
					txt=txt+Blo.getDirectoryLoc(currentBlock, dName)+"\n"+"****************";
					/*Iterator it1=totalFil.iterator();
					while(it1.hasNext()){
						System.out.println(it1.next());
					}*/
					//System.out.println(totalFil.get(0));
					directoryWrite.setText(txt);
					//currentBlock=temp;
				
				}
			
			}
		}
		
		class SelectFile implements ActionListener{                            //在下拉菜单选择某个文件
			public void actionPerformed(ActionEvent e){
				String fName=(String)file.getSelectedItem();
				if(fName!=null){                                                  //显示文件的位置，大小					
					/*String text=Blo.retFileContent(currentBlock, fName);
					fileWrite.setText(text);*/
					String txt="******************\n"+"文件位置： "+Blo.getDirectoryLoc(currentBlock, fName)
								+"\n"+"文件大小: "+Blo.getDirectorySize(currentBlock, fName)+"\n"+"***************";
					directoryWrite.setText(txt);
				
				
				}
			}
		}
		
		class accessFile implements ActionListener{                                 //打开文件
			public void actionPerformed(ActionEvent e){
				String fName=(String)file.getSelectedItem();
				if(fName!=null){
				
				if(JOptionPane.showConfirmDialog(null, "是否要打开"+fName+"文件？")==0){
					String text=Blo.retFileContent(currentBlock, fName);
					fileWrite.setText(text);
				}
			}
			}
		}
		
		class closeFile implements ActionListener{                                  //关闭文件
			public void actionPerformed(ActionEvent e){
				String fName=(String)file.getSelectedItem();
				if(fName!=null){
					if(JOptionPane.showConfirmDialog(null, "您确认要退出该文件吗？")==0);
					fileWrite.setText(null);
				}
				
			}
		}
		class SaveFile implements ActionListener{                          //保存文件
			public void actionPerformed (ActionEvent e){
				String name=(String)file.getSelectedItem();
				if(name!=null){
					SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String date=df.format(new Date());
				String txt=fileWrite.getText().trim();
				Blo.saveFile(currentBlock, name, txt);
				
				String fileInformation="***********************\n"+"文件位置："+Blo.getDirectoryLoc(currentBlock, name)+"\n"+"文件大小为： "+Blo.getDirectorySize(currentBlock, name)+"\n"+"修改日期："+date+'\n'+"*******************\n";
				directoryWrite.setText(fileInformation);
				fileWrite.setText(null);
				JOptionPane.showMessageDialog(null, name+"保存成功！");
			}
			}
		}
		class AcessDirectory implements ActionListener{                       //进入文件夹
			public void actionPerformed(ActionEvent e){
				String dName=(String) directory.getSelectedItem();
				if(dName==null|| dName.equals("")){
					JOptionPane.showMessageDialog(null, "请选择文件夹");
					return;
				}
				if(JOptionPane.showConfirmDialog(null, "是否要进入"+dName+"文件夹？")==0){
				route=route +"\\"+dName;       
				currentBlock=Blo.enterDirectory(currentBlock, dName, totalDir, totalFil);
				currentDirectory.setText(route);
				directory.removeAllItems();
				for(int i=0;i<totalDir.size();i++){
					directory.addItem((String)totalDir.get(i));
				}
				file.removeAllItems();
				for(int i=0;i<totalFil.size();i++){
					file.addItem((String)totalFil.get(i));
					
				}
				directoryWrite.setText("");
				fileWrite.setText("");
				}
				/*String DirName = (String) directory.getSelectedItem();
				if (DirName == null || DirName.equals("")) {
					JOptionPane.showMessageDialog(null, "aa");
					return;
				}
				route = route + "\\" + DirName;
				currentBlock =
					Blo.enterDirectory(currentBlock, DirName, totalDir, totalFil);
				currentDirectory.setText(route);
				directory.removeAllItems();
				for (int i = 0; i < totalDir.size(); i++) {
					directory.addItem((String) totalDir.get(i));
				}
				file.removeAllItems();
				for (int i = 0; i < totalFil.size(); i++) {
					file.addItem((String) totalFil.get(i));
				}
				directoryWrite.setText("");
				fileWrite.setText("");
			}*/
			}
		}
		
		class ExitDirectory implements ActionListener{                         //返回上一级文件夹
			public void actionPerformed(ActionEvent e){
				int block=Blo.exitDirectory(currentBlock, totalDir, totalFil);
				if(block==-1){
					return;
				}
				if(JOptionPane.showConfirmDialog(null, "是否要返回上一级文件夹?")==0){
				currentBlock=block;
				int i=route.lastIndexOf("\\");
				route=route.substring(0,i);
				currentDirectory.setText(route);
				directory.removeAllItems();
				directoryWrite.setText("");
				for(int j=0;j<totalDir.size();j++){
					directory.addItem((String)totalDir.get(j));
				}
				file.removeAllItems();
				for(int j=0;j<totalFil.size();j++){
					file.addItem((String)totalFil.get(j));
					//System.out.println(totalFil.get(i));
					
				}
				fileWrite.setText("");
				directoryWrite.setText("");
			}
			}
		}
		
		class RemoveFile implements ActionListener{                         //删除文件
			public void actionPerformed(ActionEvent e){
				String name=(String)file.getSelectedItem();
				if(name!=null&&!name.equals("")){
					if(JOptionPane.showConfirmDialog(null, "是否要删除该文件？")==0){
					fileWrite.setText(null);
					Blo.removeFile(currentBlock, name);
					totalFil.remove(name);
					file.removeItem(name);
				}
				}
			}
		}
		
		class RemoveDirectory implements ActionListener{               //删除文件夹
			public void actionPerformed(ActionEvent e){
				String name=(String)directory.getSelectedItem();
				if(name!=null&&!name.equals("")){
					if(JOptionPane.showConfirmDialog(null, "确认删除该目录及其中的所有文件吗？")==0){
					directoryWrite.setText(null);
					Blo.removeDirectory(currentBlock, name);
					directoryWrite.setText(null);
					fileWrite.setText(null);
					totalDir.remove(name);
					directory.removeItem(name);
					}
				}
			}
		}
		
		class ClearFile implements ActionListener{                 //清空文件里的内容
			public void actionPerformed(ActionEvent e){
				fileWrite.setText(null);
				JOptionPane.showMessageDialog(null, "内容已被清空");
			}
		}
		
		class ExitEvent implements ActionListener{                  //关闭程序并保存
			public void actionPerformed(ActionEvent e){
				if(JOptionPane.showConfirmDialog(null, "退出本系统后，您的数据会保存在backup.YHC中."+"\n                 谢谢使用！")==0){
				try{
					ObjectOutputStream out= new ObjectOutputStream (new FileOutputStream ("backup.YHC"));
					out.writeObject(Blo);
					out.close();
					System.exit(0);
				} catch (IOException ee){
					System.out.print("出错了");
				}
			}
		}
		}
		
	}
	class ShowFrame extends JFrame{                              //窗口初始化
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public ShowFrame(){
			this.setSize(500,600);
			ContentPane cp=new ContentPane();
			Container c=getContentPane();
			c.add(cp);
			this.setTitle("File Management");
				
		}
	}
	
	
	
	
 

