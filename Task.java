package �ڴ�;

public class Task {                        //������ ģ�ڴ�ռ�õ�����
	
		private int id;                       //��������
		private boolean allocation;           //��¼�������Ƿ�������ڴ棬true��������ڴ棬false�����ͷ��ڴ棬����taskList���ά��
		private int size=0;					  //������ռ�ڴ�Ĵ�С
		
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

