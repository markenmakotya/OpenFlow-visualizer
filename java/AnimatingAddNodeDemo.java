import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.algorithms.layout.util.Relaxer;
import edu.uci.ics.jung.algorithms.layout.util.VisRunner;
import edu.uci.ics.jung.algorithms.util.IterativeContext;
import edu.uci.ics.jung.graph.SparseGraph;
//import edu.uci.ics.jung.graph.UndirectedSparseGraph;
//import edu.uci.ics.jung.graph.UndirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.ObservableGraph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.event.GraphEvent;
import edu.uci.ics.jung.graph.event.GraphEventListener;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.graph.util.Graphs;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.layout.LayoutTransition;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import edu.uci.ics.jung.visualization.util.Animator;

import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.functors.ConstantTransformer;
 
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.TexturePaint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class AnimatingAddNodeDemo extends javax.swing.JApplet {
	private static final long serialVersionUID = -5345319851341875800L;
	private Graph<MyNode,MyEdge> g = null;
	private VisualizationViewer<MyNode,MyEdge> vv = null;
	private AbstractLayout<MyNode,MyEdge> layout = null;
	Timer timer;
	boolean done;
	public static final int EDGE_LENGTH = 100;
    List<MyNode> n = new ArrayList<MyNode>();
    List<MyEdge> e = new ArrayList<MyEdge>();
    List<MyEdge> e_flow = new ArrayList<MyEdge>();
    ArrayList matchlist = new ArrayList();
    ArrayList ppmatchlist = new ArrayList();
    
    class MyTableModel extends DefaultTableModel{
        MyTableModel(String[] columnNames, int rowNum){
          super(columnNames, rowNum);
        }

        public Class getColumnClass(int col){
          return getValueAt(0, col).getClass();
        }
      }

    String[] columnNames = {"dl_src","dl_dst","dl_vlan","dl_vlan_pcp","dl_type","nw_tos","nw_proto","nw_src","nw_dst","tp_src","tp_dst"};
	DefaultTableModel tableModel = new MyTableModel(columnNames, 0);
	JTable table = new JTable(tableModel);
    JScrollPane sp = new JScrollPane(table);
    


	@Override
	public void init() {
		//グラフ作成
//		Graph<MyNode,MyEdge> ig = Graphs.<MyNode,MyEdge>synchronizedUndirectedGraph(new UndirectedSparseMultigraph<MyNode,MyEdge>());
		Graph<MyNode,MyEdge> ig = new SparseMultigraph();
		ObservableGraph<MyNode,MyEdge> og = new ObservableGraph<MyNode,MyEdge>(ig);
		
		/*
		og.addGraphEventListener(new GraphEventListener<Number,Number>() {
				public void handleGraphEvent(GraphEvent<Number, Number> evt) {
					System.err.println("got "+evt);
				}
			}
		);
		*/
		this.g = og;
		//グラフ描写作成
		layout = new FRLayout<MyNode,MyEdge>(g);
		layout.setSize(new Dimension(600,600));
		//実行スレッド作成？
		Relaxer relaxer = new VisRunner((IterativeContext)layout);
		relaxer.stop();
		relaxer.prerelax();
		
		Layout<MyNode,MyEdge> staticLayout =new StaticLayout<MyNode,MyEdge>(g, layout);
		
		vv = new VisualizationViewer<MyNode,MyEdge>(staticLayout, new Dimension(600,500));
		
		JRootPane rp = this.getRootPane();
		rp.putClientProperty("defeatSystemEventQueueCheck", Boolean.TRUE);
		
		//画面の設定
		EtchedBorder border = new EtchedBorder(EtchedBorder.RAISED);
		
	    JPanel p = new JPanel();
	    SwingAppMain SAM = new SwingAppMain();
	    p = SAM.SwingMain(p);
	    
	    JPanel pp = new JPanel();
	    pp.setLayout(new BorderLayout());
	    pp.setPreferredSize(new Dimension(800, 200));

//	    JTable table = new JTable(tabledata, columnNames);


	    
	    sp.setPreferredSize(new Dimension(10, 10));

	    DefaultTableColumnModel columnModel = (DefaultTableColumnModel)table.getColumnModel();
	    TableColumn column = null;
//	    column = columnModel.getColumn(0);
//	    column.setPreferredWidth(100);
	    column = columnModel.getColumn(0);
	    column.setPreferredWidth(170);
	    column = columnModel.getColumn(1);
	    column.setPreferredWidth(170);
	    column = columnModel.getColumn(7);
	    column.setPreferredWidth(100);
	    column = columnModel.getColumn(8);
	    column.setPreferredWidth(100);
	    pp.add(sp);
	    
	    
		getContentPane().setLayout(new BorderLayout());
		getContentPane().setBackground(java.awt.Color.BLACK);
		getContentPane().setFont(new Font("Serif", Font.PLAIN, 12));

		vv.setGraphMouse(new DefaultModalGraphMouse<Number,Number>());
		vv.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.CNTR);
		vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<MyNode>());
		vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller<MyEdge>());
//		vv.getRenderContext().setEdgeShapeTransformer(new EdgeShape.QuadCurve());
		vv.setForeground(Color.BLACK);
		vv.addComponentListener(new ComponentAdapter() {
					/**
					* @see java.awt.event.ComponentAdapter#componentResized(java.awt.event.ComponentEvent)
					*/
					@Override
					public void componentResized(ComponentEvent arg0) {
						super.componentResized(arg0);
						System.err.println("resized");
						layout.setSize(arg0.getComponent().getSize());
					}
				}
		);
	
//	    p.setBorder(border);
	    p.setBorder(BorderFactory.createTitledBorder("make flow entry"));
//	    vv.setBorder(border);
	    vv.setBorder(BorderFactory.createTitledBorder("network"));
	    getContentPane().add(pp, BorderLayout.SOUTH);	
		getContentPane().add(p, BorderLayout.EAST);	
		getContentPane().add(vv, BorderLayout.CENTER);
		timer = new Timer();
	}
	
	

	
	
	
	
	@Override
	public void start() {
		validate();
		//変更タイマーのセット(別プロセス？)
		timer.schedule(new RemindTask(), 500, 500);
		vv.repaint();
	}
			
		
		
	class RemindTask extends TimerTask {
		@Override
		public void run() {
			process();
//			if(done) cancel();
		}
	}
		
						
		
	Integer v_prev = null;
	public void process() {
//		vv.getRenderContext().getPickedVertexState().clear();
//		vv.getRenderContext().getPickedEdgeState().clear();		
		SH_XmlRpcClient gd = new SH_XmlRpcClient();
		String[] data = null;
		try {
			data = gd.get_topology_data();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		int repaint = 0;
	    String topology = data[0];
	    String linkdata = data[1];
	    String flowdata = data[2];
	    
        String node = "";
        String linkednode = "";
        String linkname = "";
        String match = "";
        
        int next=0;
        int a=0,b=0,c=0,d=0;

        edge_initialize();

        while(next > -1){
        	a = topology.indexOf("SW", next)+2;
        	b = topology.indexOf("-", a);
        	node = topology.substring(a, b);
	        if(numN(node)==-1){
		        n.add(new MyNode(node));
		        g.addVertex(n.get(n.size()-1));
	        	repaint=1;
	        }
 //       	System.out.println("node="+node);
        	
           	next = topology.indexOf("SW", b);
           	d = a;
           	while(((topology.indexOf(",", d+1) < next && topology.indexOf(",", d) != -1)) || (next == -1 && topology.indexOf(",", d) != -1)){
	           	c = topology.indexOf(",", d);
	           	d = topology.indexOf(",", c+1);
	           	if(d < 0) break;
	           	linkednode = topology.substring(c+1, d);
	           	linkname = node +"-"+ linkednode;
		        if(numN(linkednode)==-1){
			        n.add(new MyNode(linkednode));
			        g.addVertex(n.get(n.size()-1));
		        	repaint=1;
		        }
		        if(numE(linkednode+"-"+node)==-1 && numE(node+"-"+linkednode)==-1){
			        e.add(new MyEdge(linkednode+"-"+node));
			        g.addEdge(e.get(e.size()-1), n.get(numN(linkednode)), n.get(numN(node)), EdgeType.UNDIRECTED);
		        	repaint=1;
		        }
		        if(numE(linkednode+"-"+node)!=-1) e.get(numE(linkednode+"-"+node)).col(1);
		        if(numE(node+"-"+linkednode)!=-1) e.get(numE(node+"-"+linkednode)).col(1);
		        
//	           	System.out.println(node +"-"+ linkednode);
           	}
        }
        a=0;
        b=0;
        c=0;
        d=0;
        //read link bytes
        int nodebytes = 0;
        int k = 0;

        while(linkdata.indexOf("L",b) > -1){
        	a = linkdata.indexOf("L",b);
        	b = linkdata.indexOf("-",a+1);
        	c = linkdata.indexOf("-",b+1);
        	node = linkdata.substring(a+1,b);
        	linkednode = linkdata.substring(b+1,c);
 //       	System.out.println(node +"-"+ linkednode);
        	a = linkdata.indexOf(">",a);
        	b = linkdata.indexOf(">",a+1);
        	nodebytes = Integer.parseInt(linkdata.substring(a+1,b));
        	if(numE(linkednode+"-"+node)!=-1) e.get(numE(linkednode+"-"+node)).by(nodebytes);
        	if(numE(node+"-"+linkednode)!=-1) e.get(numE(node+"-"+linkednode)).by(nodebytes);
/*        	
        	if(nodebytes != 0){
	        	e_flow.add(new MyEdge(node+"-"+linkednode+"-"+String.valueOf(k), 1));
	        	g.addEdge(e_flow.get(e_flow.size()-1), n.get(numN(node)), n.get(numN(linkednode)), EdgeType.DIRECTED);
	        	e_flow.get(numEF(node+"-"+linkednode+"-"+String.valueOf(k))).col(1);
	        	e_flow.get(numEF(node+"-"+linkednode+"-"+String.valueOf(k))).by(nodebytes);
	        }
        	k++;
        	*/
        }

        
        a=0;
        b=0;
        c=0;
        d=0;
		if(e_flow.size()!=0){
			while(e_flow.isEmpty() != true){
		    	g.removeEdge(e_flow.get(0));
				e_flow.remove(0);
			}
		}
		for(int y = 0;ppmatchlist.size() > 0;y++){
			ppmatchlist.remove(0);
		}
		
        while(flowdata.indexOf("L",b) > -1){
        	a = flowdata.indexOf("L",b);
        	b = flowdata.indexOf("-",a+1);
        	c = flowdata.indexOf("-",b+1);
        	node = flowdata.substring(a+1,b);
        	linkednode = flowdata.substring(b+1,c);
        	
        	a = flowdata.indexOf("matchstart",a);
        	b = flowdata.indexOf("matchend",a);
        	match = flowdata.substring(a, b);

        	if(matchlist.contains(match) == false) matchlist.add(match);
//        	System.out.println(matchlist.indexOf(match));
//        	System.out.println(matchlist);
        	a = flowdata.indexOf(">",a);
        	b = flowdata.indexOf(">",a+1);
        	nodebytes = Integer.parseInt(flowdata.substring(a+1,b));
        	
        	if(nodebytes != 0){
	        	e_flow.add(new MyEdge(node+"-"+linkednode+"-"+ String.valueOf(matchlist.indexOf(match)), String.valueOf(node), String.valueOf(linkednode), matchlist.indexOf(match)));
	        	g.addEdge(e_flow.get(e_flow.size()-1), n.get(numN(node)), n.get(numN(linkednode)), EdgeType.DIRECTED);
	        	e_flow.get(numEF(node+"-"+linkednode+"-"+String.valueOf(matchlist.indexOf(match)))).by(nodebytes);
	        	
	        	if(ppmatchlist.contains(match) == false) ppmatchlist.add(match);

	        }
        }
        while(tableModel.getRowCount()>0){
        	tableModel.removeRow(0);
        }
        Collections.sort(ppmatchlist);
		for(int y = 0;ppmatchlist.size() > y;y++){
			String mat = (String) ppmatchlist.get(y);
			int mmm = matchlist.indexOf(mat);
			a = mat.indexOf(", dl_src = ");
			b = mat.indexOf(", dl_dst = ");
			String dl_src = mat.substring(a+11, b);
			a = mat.indexOf(", dl_vlan = ");
			String dl_dst = mat.substring(b+11, a);
			b = mat.indexOf(", dl_vlan_pcp = ");
			String dl_vlan = mat.substring(a+12, b);
			a = mat.indexOf(", dl_type = ");
			String dl_vlan_pcp = mat.substring(b+16, a);
			b = mat.indexOf(", nw_tos = ");
			String dl_type = mat.substring(a+12, b);
			a = mat.indexOf(", nw_proto = ");
			String nw_tos = mat.substring(b+11, a);
			b = mat.indexOf(", nw_src = ");
			String nw_proto = mat.substring(a+13, b);
			a = mat.indexOf(", nw_dst = ");
			String nw_src = mat.substring(b+11, a);
			b = mat.indexOf(", tp_src = ");
			String nw_dst = mat.substring(a+11, b);
			a = mat.indexOf(", tp_dst = ");
			String tp_src = mat.substring(b+11, a);
			b = mat.indexOf("]",a);
			String tp_dst = mat.substring(a+11, a+12);
	    	Object[] newtable = {dl_src,dl_dst,dl_vlan,dl_vlan_pcp,dl_type,nw_tos,nw_proto,nw_src,nw_dst,tp_src,tp_dst};
	    	tableModel.addRow(newtable);
		}
		


//		vv.getRenderContext().getPickedVertexState().pick(v1, true);
//		vv.getRenderContext().getPickedEdgeState().pick(edge, true);
        
        Transformer<MyEdge, Paint> edgeColor = new Transformer<MyEdge, Paint>() {
        	@Override
        	public Paint transform(MyEdge e) {
        		if(e.flow_or == 0){
	        		if(e.co == 0)
	        			return Color.WHITE;
	        		else if(e.co == 1 ||e.co == 4)
	        			return Color.BLACK;
	        		else
	        			return Color.red;
        		}
        		else{
        			return new Color(Color.HSBtoRGB(e.flow_n*5 / 23f, 0.8f, 1.0f));
        		}
        	}
        };
        
        vv.getRenderContext().setEdgeDrawPaintTransformer(edgeColor);
        
        Transformer<MyEdge,String> edgeLabeller = new Transformer<MyEdge, String>() {
        	@Override
        	public String transform(MyEdge e) {
        		return String.valueOf(e.bytes);
        	}
        };
        
        vv.getRenderContext().setEdgeLabelTransformer(edgeLabeller);
        
        final Stroke edgeStroke = new BasicStroke(1.8f);
        final Stroke edgeStrokebig = new BasicStroke(2.8f); 
        final Stroke edgeStrokebigbig = new BasicStroke(3.4f); 
        Transformer<MyEdge, Stroke> edgeStrokeTransformer = new Transformer<MyEdge, Stroke>() {
        	@Override
        	public Stroke transform(MyEdge e) {
        		if(e.co == 3){
        			return edgeStrokebig;
        		}
        		if(e.co == 4){
        			return edgeStrokebigbig;
        		}
        		else{
        			return edgeStroke;
        		}
        	}
        };        
        vv.getRenderContext().setEdgeStrokeTransformer(edgeStrokeTransformer);
        Transformer<MyNode, Paint> nodeFillColor = new Transformer<MyNode, Paint>() {
        	@Override
        	public Paint transform(MyNode n) {
        		if(n.label.length()>5){
        			return Color.BLUE;
        		}
        		else{
        			return Color.red;
        		}
        	}
        };
        vv.getRenderContext().setVertexFillPaintTransformer(nodeFillColor);
        Transformer<MyNode, Shape> nodeShapeTransformer = new Transformer<MyNode, Shape>() {
        	@Override
        	public Shape transform(MyNode n) {
        		if(n.label.length()>5){
        			return new Rectangle(-10, -10, 20, 20);
        		}
        		else{
        			return new Ellipse2D.Double(-10, -10, 25, 25);
        		}
        	}
        }; 
        vv.getRenderContext().setVertexShapeTransformer(nodeShapeTransformer);
        
		layout.initialize();
		if(repaint==1){
			Relaxer relaxer = new VisRunner((IterativeContext)layout);
			relaxer.stop();
			relaxer.prerelax();
								
			StaticLayout<MyNode,MyEdge> staticLayout =new StaticLayout<MyNode,MyEdge>(g, layout);
			LayoutTransition<MyNode,MyEdge> lt =new LayoutTransition<MyNode,MyEdge>(vv, vv.getGraphLayout(),staticLayout);
			Animator animator = new Animator(lt);
			animator.start();
		}
		vv.repaint();
	}
	
	
	
	
	
	public int numN(String name){
		if(n.size()==0)return -1;
		int num=-1;
		for(int i=0;i<n.size();i++){
			if(n.get(i).toString().equals(name)==true)
				num=i;
		}
		return num;
	}
	public int numE(String name){
		if(e.size()==0)return -1;
		int num=-1;
		for(int i=0;i<e.size();i++){
			if(e.get(i).toString().equals(name)==true)
				num=i;
		}
		return num;
	}
	public int numEF(String name){
		if(e_flow.size()==0)return -1;
		int num=-1;
		for(int i=0;i<e_flow.size();i++){
			if(e_flow.get(i).toString().equals(name)==true)
				num=i;
		}
		return num;
	}
	
	public void edge_initialize(){
		if(e.size()==0) return;
		for(int i=0;i<e.size();i++){
			e.get(i).col(0);
			e.get(i).bytes = 0;
		}
	}
	
	
	public static void main(String[] args) {
		AnimatingAddNodeDemo and = new AnimatingAddNodeDemo();
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
		frame.getContentPane().add(and);
		
		and.init();
		and.start();
		frame.pack();
		frame.setVisible(true);
	}
}