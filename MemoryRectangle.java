package 内存;

public class MemoryRectangle {               //任务的图形显示类
	
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


		public void setUpperLevel(int u){   //设置任务矩形的上底
			upperLevel=u;
		}
		
		public void setBelowLevel(int b){  //设置任务矩形的下底
			belowLevel=b;
		}
		
		public int retUpperLevel(){ 
			return upperLevel;
		}
		
		public int retBelowLevel(){
			return belowLevel;
		}
		
		public int retLength(){              //返回任务矩形的大小
			return upperLevel-belowLevel+1;
		}
		
		public int retId(){
			return id;
		}
	
}
