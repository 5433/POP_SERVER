package pop.server;

public final class MainDomain{
	
	int i = 8888;
	String mes = "";
	KeywordSearchServer kss;
	//HashMap<Integer,KeywordSearchServer> serverCollector = new HashMap<Integer,KeywordSearchServer>();
	
	public MainDomain(){
		executeService();
	}
	
	public void executeService(){		
		
		Thread thread = new Thread(new NewRunThread());
		thread.start();
		
		while(true){			
			try{
				//System.out.println("In while loop");			
				if(KeywordSearchServer.newForum == true){					 
					System.out.println("In if statement");					
					//i++;
					//System.out.println("1");						
					//mes = kss.temp;			
					//System.out.println("2");			
					//System.out.println("before kes");
					KeywordSearchServer kes = new KeywordSearchServer(9999,"something");
					//serverCollector.put(i,kes);
				}		
			}catch(NullPointerException e){
				// do nothing
			}
		}		
	}
	
	class NewRunThread implements Runnable{
            @Override
            public void run(){
		kss = new KeywordSearchServer(i,"Main");
            }
	}
	
}