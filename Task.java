package 内存;

public class Task {                        //任务类 模内存占用的任务
	
		private int id;                       //任务的序号
		private boolean allocation;           //记录该任务是否分配了内存，true代表分配内存，false代表释放内存，便于taskList表的维护
		private int size=0;					  //任务所占内存的大小
		
		Task(int number,boolean allocation, int size){
			id=number;
			this.allocation=allocation;
			this.size=size;
		}
		Task(boolean allocation,int number){
			this.allocation=allocation;
			id=number;
		}
		
		public int retId(){
			return id;
		}
		public int retSize(){
			return size;
		}
		public boolean retAllocation(){
			return allocation;
		}
	}

