package �ڴ�;

public class MemoryRectangle {               //�����ͼ����ʾ��
	
		private int upperLevel;
		private int belowLevel;
		private int id=0;
		
		MemoryRectangle(int number,int u,int b){
			id=number;
			upperLevel=u;
			belowLevel=b;
		}
		MemoryRectangle(int b,int u){
			
			belowLevel=b;
			upperLevel=u;
		}


		public void setUpperLevel(int u){   //����������ε��ϵ�
			upperLevel=u;
		}
		
		public void setBelowLevel(int b){  //����������ε��µ�
			belowLevel=b;
		}
		
		public int retUpperLevel(){ 
			return upperLevel;
		}
		
		public int retBelowLevel(){
			return belowLevel;
		}
		
		public int retLength(){              //����������εĴ�С
			return upperLevel-belowLevel+1;
		}
		
		public int retId(){
			return id;
		}
	
}
