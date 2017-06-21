package file;

import java.io.Serializable;
import java.util.ArrayList;

public class block implements Serializable {       //继承于Serializable 将Java对象序列化为二进制文件，用于模拟文件系统在磁盘上的保存
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private char [] FAT=new char[128];              //FAT表，该文件系统共128个块区
	private char [][] Blocks=new char[128][512];   
	private int nowBlock;          //当前所在块
	private int currentNum;        //当前所在块的第几位
	
	public block(){                 //对内存块的初始化
		for(int i=0;i<128;++i){
			FAT[i]=0;                       //初始每个块都默认为空的
		}	
		FAT[0]=2;
		for (int i=0;i<512;i++)
		{
			Blocks[0][i]=' ';
		}                                         //前16位是文件名，17位是文件类型，18位是文件大小
		Blocks[0][0]='.';
		Blocks[0][16]=0;
		Blocks[0][18]=0;
		
		Blocks[0][32]='.';
		Blocks[0][33]='.';
		Blocks[0][48]=2;
	}
	public char[] retDirectory(int currentBlock,int num){                        //获取目录信息
		char [] directory=new char[32];
		for(int i=0;i<32;++i){
			directory[i]=Blocks[currentBlock][num*32+i];
		}
		return directory;
	}
	
	public String retDirectoryName(int currentBlock, int num){               //获取文件名
		char[] directory=retDirectory(currentBlock,num);
		String DirectoryName="";
		for(int i=0;i<16;++i){
			DirectoryName=DirectoryName+directory[i];
		}
		return DirectoryName.trim();	
	}
	
	public void getDirectoryLocation(int currentBlock, String name){                //获取文件的位置
		int i;
		for(i=0;i<16;i++){
			if(name.equals(retDirectoryName(currentBlock,i))){
				break;
			}
		}
		nowBlock=currentBlock;
		currentNum=i;
	}
	
	public String getText(int num){                            //将块区中的内容读出
		String text="";
		do{
			for(int i=0;i<256;i++){
				text=text+Blocks[num][i];
			}
			num=FAT[num];
		}while (num!=6666);
		/*while(num!=6666){
			for(int i=0;i<256;++i){
				text=text+Blocks[num][i];
			}
			num=FAT[num];
		}*/
		return text.trim();
	}
	public String retFileContent(int currentBlock, String fileName){                     //获取文件中的数据（内容）
		int i;
		for(i=0;i<16;i++){
			if(fileName.equals(retDirectoryName(currentBlock,i))&& Blocks[currentBlock][i*32+16]==1){
				break;
			}
		}
		if(i!=16){
			int blockNum=(int)Blocks[currentBlock][i*32+18];
			return getText(blockNum);
		}
		return "";
	}
	
	public int enterDirectory(int currentBlock,String fileName,ArrayList<String> CDR,ArrayList<String> FL){       //进入文件夹
		getDirectoryLocation(currentBlock,fileName);
		int newBloc=Blocks[nowBlock][currentNum*32+18];
		CDR.clear();
		FL.clear();
		int offset=64;
		int i;
		for(i=2;i<16;i++){
			retDirectory(newBloc,i);
			String name=retDirectoryName(newBloc,i);
			if(name !=""&&Blocks[newBloc][offset+16]==0){                     //每当进入一个文件夹后，都要对该文件夹下的内容进行更新
				CDR.add(name);
			}
			else if(name!=""&&Blocks[newBloc][offset+16]==1){
				FL.add(name);
			}
			offset=offset+32;
		}
		return newBloc;
	}
	 
	public int exitDirectory(int currentBlock,ArrayList<String> CDR,ArrayList<String> FL){                  //返回上一级
		
		if (Blocks[currentBlock][48] == 0) {                                        
			int newCurBlock = Blocks[currentBlock][50];
			CDR.clear();
			FL.clear();
			int i;
			int offset = 64;
			for (i = 2; i < 16; i++) {
				retDirectory(newCurBlock, i);
				String name = retDirectoryName(newCurBlock, i);
				if (Blocks[newCurBlock][offset + 16] == 0) {               //每当退出一个文件夹后，都要对当前位置的文件夹的内容进行更新
					CDR.add(name);
				} else if (Blocks[newCurBlock][offset + 16] == 1) {
					FL.add(name);
				}
//				System.out.println((int) disc[newCurBlock][offset + 16]);
				offset += 32;
			}
			return newCurBlock;
		} else
			return -1;
	}
	
	public boolean saveFile(int currentBlock,String fileName,String Text){            //保存文件
		int i;
		for( i=0;i<16;i++){
			if(fileName.equals(retDirectoryName(currentBlock,i))){
				break;
			}
		}
		//		if(i != 16)
		//		{
		Blocks[currentBlock][i * 32+ 17] = (char)Text.length();
		int TxtBloNum = (int) Blocks[currentBlock][i * 32 + 18];
		removeBlock(TxtBloNum);
		for (int j = 0; j < Text.length(); j++) {            //将文件内容保存进内存块
			Blocks[TxtBloNum][j] = Text.charAt(j);
		}
		
		return true;	
		
	}
	
	public void removeBlock(int BlockNum){                         //移除内存块
		if(FAT[BlockNum]!=0&&FAT[BlockNum]!=6666){
			remove((int)FAT[BlockNum]);
		}
	}
	
	public void remove(int BlockNum){                   //移除内存块，并更新FAT表
		if(FAT[BlockNum]!=0&&FAT[BlockNum]!=6666){
			remove((int)FAT[BlockNum]);
		}
		FAT[BlockNum]=0;
		
	}
	public boolean addDirectory(int currentBlock,String name){           //创建文件夹
		int block=distribute();
		if(block==-1){
			return false;
		}	
		int i;
		for(i=0;i<16;i++){
			if(Blocks[currentBlock][i*32]==' '){
				break;
			}
		}
		if(i!=16){
			int offset=i*32;
			for(int j=0;j<name.length();j++){
				Blocks[currentBlock][offset+j]=name.charAt(j);
			}
			Blocks[currentBlock][offset+16]=0;
			Blocks[currentBlock][offset+17]=0;
			Blocks[currentBlock][offset+18]=(char)block;
			initial(block,currentBlock);
			return true;
		}
		else{
			return false;
		}
		
	}
	public boolean addFile(int currentBlock,String name){                //创建文件
		int i;
		for(i=0;i<16;i++){
			if(Blocks[currentBlock][i*32]==' '){
				break;
			}
		}
		if(i!=16){
			int offset=i*32;
			for(int j=0;j<name.length();j++){
				Blocks[currentBlock][offset+j]=name.charAt(j);
			}
			Blocks[currentBlock][offset+16]=1;
			Blocks[currentBlock][offset+17]=0;
			Blocks[currentBlock][offset+18]=(char)distribute();
			return true;			
		}
		else {
			return false;
		}
	}
	
	public int distribute(){                                 //分配块区空间
		for (int i=0;i<128;i++){
			if(FAT[i]==0){
				FAT[i]=6666;
				for(int j=0;j<512;j++){
					Blocks[i][j]=' ';
				}
				return i;
			}
		}
		return -1;
	}
	
	public  void initial(int currentBlock,int freeBlock){             //初始化内存块
		Blocks[currentBlock][0]='.';
		Blocks[currentBlock][16]='0';
		Blocks[currentBlock][18]=(char)currentBlock;
		Blocks[currentBlock][32]='.';
		Blocks[currentBlock][33]='.';
		Blocks[currentBlock][48]=0;
		Blocks[currentBlock][50]=(char) freeBlock;
		
		
	}
	
	public void clearDirectory(int currentBlock,int num){                  //块内存清空
		for(int i=0;i<31;i++){
			Blocks[currentBlock][num*32+i]=' ';
		}
	}
	public void removeFile(int currentBlock,int num){                    //删除文件
		remove((int)Blocks[currentBlock][num*32+18]);
		clearDirectory(currentBlock,num);
	}
	public void removeFile(int currentBlock,String name ){
		getDirectoryLocation(currentBlock,name);
		removeFile(nowBlock,currentNum);
	}
	
	public void removeDirectory(int Block){                     //删除文件夹
		for(int i=2;i<16;++i){
			if(Blocks[Block][i*32]==' '){
				continue;
			}
			if(Blocks[Block][i*32+16]==1){
				removeFile(Block,i);
			}
			if(Blocks[Block][i*32+16]==0){
				removeDirectory(Blocks[Block][i*32+18]);
			}
		}
		FAT[Block]=0;
	}
	public void removeDirectory(int currentBlock,String dName){
		getDirectoryLocation(currentBlock,dName);
		int num=currentNum;
		int Block=Blocks[nowBlock][num*32+18];
		removeDirectory(Block);
		clearDirectory(currentBlock,num);
	}
	public int getDirectoryLoc(int currentBlock,String fileName){                     //获取文件位置
		getDirectoryLocation(currentBlock,fileName);
		return (int)(Blocks[currentBlock][currentNum*32+18]*256);
		
	}
	
	public int getDirectorySize(int currentBlock,String fileName){                    //获取文件大小
		getDirectoryLocation(currentBlock,fileName); 
		return (int)(Blocks[currentBlock][currentNum*32+17]);
		
	}
	
	/*public int getDirectoryTime(int currentBlock,String fileName){
		getDirectoryLocation(currentBlock,fileName);
		
	}*/
	
	public void load(ArrayList<String> currentDirectory, ArrayList<String> currentFile){                      //将保存在磁盘的文件解析并装载
		currentDirectory.clear();
		currentFile.clear();
		for(int i=2;i<16;i++){
			if(Blocks[0][i*32]!=' '){
				if(Blocks[0][i*32+16]==0){
					currentDirectory.add(retDirectoryName(0,i));
				}
				else if(Blocks[0][i*32+16]==1){
					currentFile.add(retDirectoryName(0,i));
				}
			}
		}
	}
}