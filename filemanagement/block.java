package file;

import java.io.Serializable;
import java.util.ArrayList;

public class block implements Serializable {       //�̳���Serializable ��Java�������л�Ϊ�������ļ�������ģ���ļ�ϵͳ�ڴ����ϵı���
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private char [] FAT=new char[128];              //FAT�����ļ�ϵͳ��128������
	private char [][] Blocks=new char[128][512];   
	private int nowBlock;          //��ǰ���ڿ�
	private int currentNum;        //��ǰ���ڿ�ĵڼ�λ
	
	public block(){                 //���ڴ��ĳ�ʼ��
		for(int i=0;i<128;++i){
			FAT[i]=0;                       //��ʼÿ���鶼Ĭ��Ϊ�յ�
		}	
		FAT[0]=2;
		for (int i=0;i<512;i++)
		{
			Blocks[0][i]=' ';
		}                                         //ǰ16λ���ļ�����17λ���ļ����ͣ�18λ���ļ���С
		Blocks[0][0]='.';
		Blocks[0][16]=0;
		Blocks[0][18]=0;
		
		Blocks[0][32]='.';
		Blocks[0][33]='.';
		Blocks[0][48]=2;
	}
	public char[] retDirectory(int currentBlock,int num){                        //��ȡĿ¼��Ϣ
		char [] directory=new char[32];
		for(int i=0;i<32;++i){
			directory[i]=Blocks[currentBlock][num*32+i];
		}
		return directory;
	}
	
	public String retDirectoryName(int currentBlock, int num){               //��ȡ�ļ���
		char[] directory=retDirectory(currentBlock,num);
		String DirectoryName="";
		for(int i=0;i<16;++i){
			DirectoryName=DirectoryName+directory[i];
		}
		return DirectoryName.trim();	
	}
	
	public void getDirectoryLocation(int currentBlock, String name){                //��ȡ�ļ���λ��
		int i;
		for(i=0;i<16;i++){
			if(name.equals(retDirectoryName(currentBlock,i))){
				break;
			}
		}
		nowBlock=currentBlock;
		currentNum=i;
	}
	
	public String getText(int num){                            //�������е����ݶ���
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
	public String retFileContent(int currentBlock, String fileName){                     //��ȡ�ļ��е����ݣ����ݣ�
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
	
	public int enterDirectory(int currentBlock,String fileName,ArrayList<String> CDR,ArrayList<String> FL){       //�����ļ���
		getDirectoryLocation(currentBlock,fileName);
		int newBloc=Blocks[nowBlock][currentNum*32+18];
		CDR.clear();
		FL.clear();
		int offset=64;
		int i;
		for(i=2;i<16;i++){
			retDirectory(newBloc,i);
			String name=retDirectoryName(newBloc,i);
			if(name !=""&&Blocks[newBloc][offset+16]==0){                     //ÿ������һ���ļ��к󣬶�Ҫ�Ը��ļ����µ����ݽ��и���
				CDR.add(name);
			}
			else if(name!=""&&Blocks[newBloc][offset+16]==1){
				FL.add(name);
			}
			offset=offset+32;
		}
		return newBloc;
	}
	 
	public int exitDirectory(int currentBlock,ArrayList<String> CDR,ArrayList<String> FL){                  //������һ��
		
		if (Blocks[currentBlock][48] == 0) {                                        
			int newCurBlock = Blocks[currentBlock][50];
			CDR.clear();
			FL.clear();
			int i;
			int offset = 64;
			for (i = 2; i < 16; i++) {
				retDirectory(newCurBlock, i);
				String name = retDirectoryName(newCurBlock, i);
				if (Blocks[newCurBlock][offset + 16] == 0) {               //ÿ���˳�һ���ļ��к󣬶�Ҫ�Ե�ǰλ�õ��ļ��е����ݽ��и���
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
	
	public boolean saveFile(int currentBlock,String fileName,String Text){            //�����ļ�
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
		for (int j = 0; j < Text.length(); j++) {            //���ļ����ݱ�����ڴ��
			Blocks[TxtBloNum][j] = Text.charAt(j);
		}
		
		return true;	
		
	}
	
	public void removeBlock(int BlockNum){                         //�Ƴ��ڴ��
		if(FAT[BlockNum]!=0&&FAT[BlockNum]!=6666){
			remove((int)FAT[BlockNum]);
		}
	}
	
	public void remove(int BlockNum){                   //�Ƴ��ڴ�飬������FAT��
		if(FAT[BlockNum]!=0&&FAT[BlockNum]!=6666){
			remove((int)FAT[BlockNum]);
		}
		FAT[BlockNum]=0;
		
	}
	public boolean addDirectory(int currentBlock,String name){           //�����ļ���
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
	public boolean addFile(int currentBlock,String name){                //�����ļ�
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
	
	public int distribute(){                                 //��������ռ�
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
	
	public  void initial(int currentBlock,int freeBlock){             //��ʼ���ڴ��
		Blocks[currentBlock][0]='.';
		Blocks[currentBlock][16]='0';
		Blocks[currentBlock][18]=(char)currentBlock;
		Blocks[currentBlock][32]='.';
		Blocks[currentBlock][33]='.';
		Blocks[currentBlock][48]=0;
		Blocks[currentBlock][50]=(char) freeBlock;
		
		
	}
	
	public void clearDirectory(int currentBlock,int num){                  //���ڴ����
		for(int i=0;i<31;i++){
			Blocks[currentBlock][num*32+i]=' ';
		}
	}
	public void removeFile(int currentBlock,int num){                    //ɾ���ļ�
		remove((int)Blocks[currentBlock][num*32+18]);
		clearDirectory(currentBlock,num);
	}
	public void removeFile(int currentBlock,String name ){
		getDirectoryLocation(currentBlock,name);
		removeFile(nowBlock,currentNum);
	}
	
	public void removeDirectory(int Block){                     //ɾ���ļ���
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
	public int getDirectoryLoc(int currentBlock,String fileName){                     //��ȡ�ļ�λ��
		getDirectoryLocation(currentBlock,fileName);
		return (int)(Blocks[currentBlock][currentNum*32+18]*256);
		
	}
	
	public int getDirectorySize(int currentBlock,String fileName){                    //��ȡ�ļ���С
		getDirectoryLocation(currentBlock,fileName); 
		return (int)(Blocks[currentBlock][currentNum*32+17]);
		
	}
	
	/*public int getDirectoryTime(int currentBlock,String fileName){
		getDirectoryLocation(currentBlock,fileName);
		
	}*/
	
	public void load(ArrayList<String> currentDirectory, ArrayList<String> currentFile){                      //�������ڴ��̵��ļ�������װ��
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