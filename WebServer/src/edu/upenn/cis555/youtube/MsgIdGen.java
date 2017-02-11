package edu.upenn.cis555.youtube;

public class MsgIdGen {
   int id;
	
	MsgIdGen (){
	id = 0;	
	}
	
	int getNextId(){
		id = id+1;
		return id;
	}
}
