public class MyEdge {
    String label;
    int co;
    int bytes;
    int flow_or;
    int flow_n;
    String front;
    String rear;
    
    public MyEdge(String label) {
        this.label = label;
        this.co = 0;
        this.bytes = 0;
        this.flow_or = 0;
    }
    
    public MyEdge(String label, String front, String rear,  int a) {
        this.label = label;
        this.front = front;
        this.rear = rear;
        this.bytes = 0;
        this.flow_or = 1;
        this.flow_n = a;
    }
    
    public void col(int c){
    	this.co = c;
    }
    
    public void by(int b){
    	this.bytes += b;
    	if(this.bytes !=0) this.co = 1;
    	if(this.bytes >200 ) this.co = 4;
    }
    
    @Override
    public String toString() {
        return label;
    }
}