
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class SwingAppMain implements ActionListener {
	
	EtchedBorder border = new EtchedBorder(EtchedBorder.RAISED);
	int boxsize = 35;
	int para = 12;
	
    JLabel port_id = new JLabel("port_id");
    JTextField tport_id = new JTextField(boxsize);
	JLabel dl_src = new JLabel("dl_src");
    JTextField tdl_src = new JTextField(boxsize);
    JLabel dl_dst = new JLabel("dl_dst");
    JTextField tdl_dst = new JTextField(boxsize);
	JLabel dl_vlan = new JLabel("dl_vlan");
    JTextField tdl_vlan = new JTextField(boxsize);
    JLabel dl_vlan_pcp = new JLabel("dl_vlan_pcp");
    JTextField tdl_vlan_pcp = new JTextField(boxsize);
    JLabel dl_type = new JLabel("dl_type");
    JTextField tdl_type = new JTextField(boxsize);
    JLabel nw_src = new JLabel("nw_src");
    JTextField tnw_src = new JTextField(boxsize);
    JLabel nw_dst = new JLabel("nw_dst");
    JTextField tnw_dst = new JTextField(boxsize);
    JLabel nw_proto = new JLabel("nw_proto");
    JTextField tnw_proto = new JTextField(boxsize);
    JLabel nw_tos = new JLabel("nw_tos");
    JTextField tnw_tos = new JTextField(boxsize);
    JLabel tp_src = new JLabel("tp_src");
    JTextField ttp_src = new JTextField(boxsize);
    JLabel tp_dst = new JLabel("tp_dst");
    JTextField ttp_dst = new JTextField(boxsize);
    
	JLabel idle_timeout = new JLabel("idle_timeout");
    JTextField sidle_timeout = new JTextField(boxsize);
    JLabel cookie = new JLabel("cookie");
    JTextField scookie = new JTextField(boxsize);
	JLabel hard_timeout = new JLabel("hard_timeout");
    JTextField shard_timeout = new JTextField(boxsize);
    JLabel priority = new JLabel("priority");
    JTextField spriority = new JTextField(boxsize);
    JLabel buffer_id = new JLabel("buffer_id");
    JTextField sbuffer_id = new JTextField(boxsize);
    JLabel out_port = new JLabel("out_port");
    JTextField sout_port = new JTextField(boxsize);
    JLabel send_flow_rem = new JLabel("send_flow_rem");
    JTextField ssend_flow_rem = new JTextField(boxsize);
    JLabel check_overlap = new JLabel("check_overlap");
    JTextField scheck_overlap = new JTextField(boxsize);
    JLabel emerg = new JLabel("emerg");
    JTextField semerg = new JTextField(boxsize);
    JLabel Forward = new JLabel("Forward");
    JTextField sForward = new JTextField(boxsize);
    JLabel ModifyField = new JLabel("Modify-Field");
    JTextField sModifyField = new JTextField(boxsize);
 //   JLabel Drop = new JLabel("Drop");
 //   JTextField sDrop = new JTextField(boxsize);
    JLabel route = new JLabel("route");
    JTextField troute = new JTextField(boxsize);
    
    JLabel or = new JLabel();
    JButton button = new JButton("make");
 //   JLabel route = new JLabel("route");
    JButton button2 = new JButton("delete");
  //  JTextArea troute = new JTextArea(5, 15);
    
    
        public JPanel SwingMain(JPanel p){ 
    	    p.setLayout(new BorderLayout());
    	    p.setPreferredSize(new Dimension(500, 600));
    	    
        	JPanel ppright = new JPanel();
    	    ppright.setPreferredSize(new Dimension(240, 340));
    	    GridLayout gll = new GridLayout(para, 2);
    	    gll.setVgap(2);
    	    ppright.setLayout(gll);
    	    
    	    JPanel ppup = new JPanel();
    	    ppup.setPreferredSize(new Dimension(240, 340));
    	    GridLayout gl = new GridLayout(para, 2);
    	    gl.setVgap(2);
    	    ppup.setLayout(gl);
    	    
    	    ppup.add(port_id);
    	    ppup.add(tport_id);    	    
    	    ppup.add(dl_src);
    	    ppup.add(tdl_src);
    	    ppup.add(dl_dst);
    	    ppup.add(tdl_dst);
    	    ppup.add(dl_vlan);
    	    ppup.add(tdl_vlan);
    	    ppup.add(dl_vlan_pcp);
    	    ppup.add(tdl_vlan_pcp);
    	    ppup.add(dl_type);
    	    ppup.add(tdl_type);
    	    ppup.add(nw_tos);
    	    ppup.add(tnw_tos);
    	    ppup.add(nw_proto);
    	    ppup.add(tnw_proto);
    	    ppup.add(nw_src);
    	    ppup.add(tnw_src);
    	    ppup.add(nw_dst);
    	    ppup.add(tnw_dst);
    	    ppup.add(tp_src);
    	    ppup.add(ttp_src);
    	    ppup.add(tp_dst);
    	    ppup.add(ttp_dst);
    	    
	    
    	    ppright.add(idle_timeout);
    	    ppright.add(sidle_timeout);
    	    ppright.add(cookie);
    	    ppright.add(scookie);
    	    ppright.add(hard_timeout);
    	    ppright.add(shard_timeout);
    	    ppright.add(priority);
    	    ppright.add(spriority);
    	    ppright.add(buffer_id);
    	    ppright.add(sbuffer_id);
    	    ppright.add(out_port);
    	    ppright.add(sout_port);
    	    ppright.add(send_flow_rem);
    	    ppright.add(ssend_flow_rem);
    	    ppright.add(check_overlap);
    	    ppright.add(scheck_overlap);
    	    ppright.add(emerg);
    	    ppright.add(semerg);
    	    ppright.add(Forward);
    	    ppright.add(sForward);
    	    ppright.add(ModifyField);
    	    ppright.add(sModifyField);
//    	    ppright.add(Drop);
 //   	    ppright.add(sDrop);
    	    ppright.add(route);
    	    ppright.add(troute);
    	    
    	    JPanel ppdown = new JPanel();
    	    ppdown.setPreferredSize(new Dimension(10, 50));
    	    ppdown.add(button);
    	    ppdown.add(button2);
    	    ppdown.add(or);
    	    button.addActionListener(this);
    	    button2.addActionListener(this);
    	    
    	    JPanel ppmid = new JPanel();
    	    ppmid.setPreferredSize(new Dimension(40, 50));
    	    troute.setBorder(border);
 //   	    ppdown.add(route);
  //  	    ppdown.add(troute);
    	    
    	    p.add(ppup, BorderLayout.WEST);
    	    p.add(ppright, BorderLayout.EAST);
 //   	    p.add(ppmid, BorderLayout.CENTER);	
    	    p.add(ppdown, BorderLayout.SOUTH);


    	    return p;
        }
       
        
        public void actionPerformed(ActionEvent event){
        	String[] routedata = new String[26];
        	
        	routedata[0] = tdl_src.getText();//MAC
        	routedata[1] = tdl_dst.getText();//MAC
        	routedata[2] = tdl_type.getText();//number
        	routedata[3] = tnw_src.getText();//IP
        	routedata[4] = tnw_dst.getText();//IP
        	routedata[5] = tnw_proto.getText();//number
        	routedata[6] = tnw_tos.getText();//number
        	routedata[7] = ttp_src.getText();//number
        	routedata[8] = ttp_dst.getText();//number
        	routedata[9] = troute.getText();//option
        	
        	routedata[10] = tport_id.getText();
        	routedata[11] = tdl_vlan.getText();
        	routedata[12] = tdl_vlan_pcp.getText();
        	
        	routedata[13] = sidle_timeout.getText();
        	routedata[14] = scookie.getText();
        	routedata[15] = shard_timeout.getText();
        	routedata[16] = spriority.getText();
        	routedata[17] = sbuffer_id.getText();
        	routedata[18] = sout_port.getText();
        	routedata[19] = ssend_flow_rem.getText();//true or false
        	routedata[20] = scheck_overlap.getText();//true or false
        	routedata[21] = semerg.getText();//true or false
        	routedata[22] = sForward.getText();//number or option
        	routedata[23] = sModifyField.getText();//dlsrc(00:00:00:00:00:00:))
        	String make = "0";
        	if(event.getSource()  ==  button){
        		make = "1";
        	}
        	routedata[24] = make;
        	routedata[25] = ",";
        	int sendor = 0;
        	int forw = 0;
        	for(int i=0; routedata.length-1>i; i++){
        		if(routedata[i].length() == 0){
        			if(i==routedata.length-1) sendor = 1;
        			routedata[i] = "none";
        		}
        		else if(i==0 || i==1){
        			//11:22:33:44:55:66
        			if(routedata[i].indexOf(":") != -1){
        				String[] mac = routedata[i].split(":", 0);
        				if(mac.length==6){
        					for(int f=0;f<6;f++){
        						if(mac[f].length()==2){
        		        	        if(!mac[f].matches("([0-9]*[A-F]*)*")) {
        		        	            sendor = 1;
        		        	            System.out.println(i+"aMACno");
        		        	            break;
        		        	        }
        						}else{
        							sendor = 1; 
        							System.out.println(i+"bMACno"); 
        							break;
        						}
        					}
        				}else{
        					sendor = 1; 
       					System.out.println(i +"+"+ mac.length+"cMACno");
        				}
        			}else{
        				sendor = 1; 
        				System.out.println(i+"dMACno");
        			}
        		}
        		
        		else if(i==3 || i==4){
        			//240.0.0.0-255.255.255.255
        			if(routedata[i].indexOf(".") != -1){
        				String[] ip = routedata[i].split("\\.", 0);
        				if(ip.length==4){
        					for(int f=1;f<4;f++){
        						if(ip[f].length()<4 && ip[f].length()>0){
        		        	        if(ip[f].matches("[0-9]*")){
        		        	        	int value = Integer.valueOf(ip[f]);
        		        	        	if(value>256 && value<0){
        		        	        		sendor = 1;
//        		        	        		System.out.println(i+"aMACno");
        		        	        		break;
        		        	        	}
        		        	        } else {
        		        	        	sendor = 1;
 //       		        	        	System.out.println(i+"bMACno");
        		        	        	break;
        		        	        }
        						}else{
        							sendor = 1; 
//        							System.out.println(i+"cMACno"); 
        							break;
        						}
        					}
        				}else{
        					sendor = 1; 
 //       					System.out.println(i +"+"+ ip.length+"dMACno");
        				}
        			}else{
        				sendor = 1; 
//        				System.out.println(i+"MACno");
        			}
        		}else if(i==9){
        			if(routedata[i].indexOf("-") != -1){
        				String[] route = routedata[i].split("-", 0);
        				for(int f=0;f<route.length;f++){
        					if(route[f].length() > 0){
        						if(!route[f].matches("[0-9]*")){
        							sendor = 1;
        						}
        						forw = 1;
        					}else{
        						sendor = 1;
        					}
        				}
        			}else if(routedata[i].indexOf(",") != -1){
        				String[] route = routedata[i].split(",", 0);
        				for(int f=0;f<route.length;f++){
        					if(route[f].length() > 0){
        						if(!route[f].matches("[0-9]*")){
        							sendor = 1;
        						}
        					}else{
        						sendor = 1;
        					}
        				}
        			}else if(routedata[i].indexOf("*") != -1){
        				String[] route = routedata[i].split("\\*", 0);
        				for(int f=0;f<route.length;f++){
        					if(route[f].length() > 0){
        						if(!route[f].matches("[0-9]*")){
        							System.out.println("1");
        							sendor = 1;
        						}else{
        							forw = 1;
        						}
        					}else{
        						System.out.println("2");
        						sendor = 1;
        					}
        				}
        			}else if(!routedata[i].matches("[0-9]*")){
        				System.out.println("3");
        				sendor = 1;
        			}
        		}
        		else if(i==19 || i==20 || i==21){
        			if(routedata[i] == "true" || routedata[i] == "false"){
        				
        			}
        			else{
        				sendor = 1;
        			}
        		}
        		else if(i==22){
        		//	System.out.println("aaaaa");
        			if(routedata[i].equals("IN_PORT") || routedata[i].equals("ALL") || routedata[i].equals("FLOOD") ||
        					routedata[i].equals("CONTROLLER") || routedata[i].equals("LOCAL") || routedata[i].matches("[0-9]*")){
        				if(forw == 1){
        					routedata[i] = "none";
        				}
        			}
        			else{
        			//	System.out.println("aragagega");
        				sendor = 1;
        			}
        		}
        		else if(i==23){
        			int tes = 0;
        			int next = 1;
        			while(next == 1){
        			if(routedata[i].indexOf("dlsrc(") != -1 || routedata[i].indexOf("dldst(") != -1){
        				int kkk = 0; 
        				if(routedata[i].indexOf("dlsrc(") != -1){
        					kkk = 1;
        				}
        				if(routedata[i].indexOf(")",tes+6) != -1){
        					String datachange = routedata[i].substring(tes+6,routedata[i].indexOf(")",tes+6));
 //       					System.out.println(datachange);
	            			if(datachange.indexOf(":") != -1){
	            				String[] mac = datachange.split(":", 0);
	            				if(mac.length==6){
	            					for(int f=0;f<6;f++){
//	            						System.out.println(mac[f]);
	            						if(mac[f].length()==2){
	            		        	        if(!mac[f].matches("([0-9]*[A-F]*)*")) {
	            		        	            sendor = 1;
	            		        	            System.out.println(i+"aMACno");
	            		        	            break;
	            		        	        }
	            						}else{
	            							sendor = 1; 
	            							System.out.println(f+"bMACno"); 
	            							break;
	            						}
	            					}
	            				}else{
	            					sendor = 1; 
	           					System.out.println(i +"+"+ mac.length+"cMACno");
	            				}
	            			}else{
	            				sendor = 1; 
	            				System.out.println(i+"dMACno");
	            			}
        				}else{
        					sendor = 1;
        				}
        				if(sendor == 0){
        					tes = routedata[i].indexOf(")",tes+6)+1;
        					if(kkk == 1){
        						routedata[25] = routedata[25] + ",dlsrc";
        					}else{
        						routedata[25] = routedata[25] + ",dldst";
        					}
        				}
        			}else if(routedata[i].indexOf("nwsrc(") != -1 || routedata[i].indexOf("nwdldst(") != -1){
        				int kkk = 0; 
        				if(routedata[i].indexOf("nwsrc(") != -1){
        					kkk = 1;
        				}
        				if(routedata[i].indexOf(")",tes+6) != -1){
        					String datachange = routedata[i].substring(tes+6,routedata[i].indexOf(")",tes+6));
	            			if(datachange.indexOf(".") != -1){
	            				String[] ip = datachange.split("\\.", 0);
	            				if(ip.length==4){
	            					for(int f=1;f<4;f++){
	            						if(ip[f].length()<4 && ip[f].length()>0){
	            		        	        if(ip[f].matches("[0-9]*")){
	            		        	        	int value = Integer.valueOf(ip[f]);
	            		        	        	if(value>256 && value<0){
	            		        	        		sendor = 1;
	//            		        	        		System.out.println(i+"aMACno");
	            		        	        		break;
	            		        	        	}
	            		        	        } else {
	            		        	        	sendor = 1;
	     //       		        	        	System.out.println(i+"bMACno");
	            		        	        	break;
	            		        	        }
	            						}else{
	            							sendor = 1; 
	//            							System.out.println(i+"cMACno"); 
	            							break;
	            						}
	            					}
	            				}else{
	            					sendor = 1; 
	     //       					System.out.println(i +"+"+ ip.length+"dMACno");
	            				}
	            			}else{
	            				sendor = 1; 
	//            				System.out.println(i+"MACno");
	            			}
        				}else{
        					sendor = 1;
        				}
        				if(sendor == 0){
        					tes = routedata[i].indexOf(")",tes+6)+1;
        					if(kkk == 1){
        						routedata[25] = routedata[25] + ",nwsrc";
        					}else{
        						routedata[25] = routedata[25] + ",nwdst";
        					}
        				}
        			}else if(routedata[i].indexOf("tpsrc(") != -1 || routedata[i].indexOf("tpdst(") != -1){
        				int kkk = 0; 
        				if(routedata[i].indexOf("tpsrc(") != -1){
        					kkk = 1;
        				}
        				if(routedata[i].indexOf(")",tes+6) != -1){
        					String datachange = routedata[i].substring(tes+6,routedata[i].indexOf(")",tes+6));
                	        if(datachange.matches("[0-9]*")) {
                	        } else {
                	            sendor = 1;
                	        }
        				}
        				if(sendor == 0){
        					tes = routedata[i].indexOf(")",tes+6)+1;
        					if(kkk == 1){
        						routedata[25] = routedata[25] + ",tpsrc";
        					}else{
        						routedata[25] = routedata[25] + ",tpdst";
        					}
        				}
        			}else{
        				sendor = 1;
        			}
        			if (routedata[i].length() > tes){
        				next = 1;
        			}else{
        				next = 0;
        			}
        		}
        		}
        		else{
        	        if(routedata[i].matches("[0-9]*")) {
//        	            System.out.println(i+"yes");
        	        } else {
//        	            System.out.println(i+"no");
        	            sendor = 1;
        	        }
        		}
        	}

        	
        	tdl_src.setText(null);
        	tdl_dst.setText(null);
        	tdl_type.setText(null);
        	tnw_src.setText(null);
        	tnw_dst.setText(null);
        	tnw_proto.setText(null);
        	tnw_tos.setText(null);
        	ttp_src.setText(null);
        	ttp_dst.setText(null);
        	troute.setText(null);
        	
        	tdl_vlan.setText(null);
        	tdl_vlan_pcp.setText(null);
        	tport_id.setText(null);
        	sidle_timeout.setText(null);
        	scookie.setText(null);
        	shard_timeout.setText(null);
        	spriority.setText(null);
        	sbuffer_id.setText(null);
        	sout_port.setText(null);
        	ssend_flow_rem.setText(null);
        	scheck_overlap.setText(null);
        	semerg.setText(null);
        	sForward.setText(null);
        	sModifyField.setText(null);

        	if(sendor==1){
        		System.out.println("yes");
        		or.setText("error");
        	}else{        	
	        	SH_XmlRpcClient gdgd = new SH_XmlRpcClient();
	        	
	        	int changeor = 0;
	        	try {
					changeor = gdgd.send_route_change(routedata);
		        	if(changeor == 0){
		        		or.setText("change");
		        	}else{
		        		or.setText("error");
		        	}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
        }
}