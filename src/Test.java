import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
//http://stackoverflow.com/questions/11300847/load-and-display-all-the-images-from-a-folder
public class Test {
	static JTextField tf;
 public  static String fl=null; 
	static JFrame frame;
	static JPanel panel1,panel2;
	static JButton jb;
	static JList list;
	static int y;
	static String[] images;
	public volatile int state=7;//gui invisible
	
	
    // File representing the folder that you select using a FileChooser
    static final File dir = new File("testcases/");

    // array of supported extensions (use a List if you prefer)
    static final String[] EXTENSIONS = new String[]{
        "gif", "png", "bmp" ,"jpg"// and other formats you need
    };
    // filter to identify images based on their extensions
    static final FilenameFilter IMAGE_FILTER = new FilenameFilter() {

        @Override
        public boolean accept(final File dir, final String name) {
            for (final String ext : EXTENSIONS) {
                if (name.endsWith("." + ext)) {
                    return (true);
                }
            }
            return (false);
        }
    };
    public int getState()
    {
    	return state;
    }
    public void setvisible()
    {
    	state=1;
    	frame.setVisible(true);
    }
  //  public static void main(String[] args) {
    public String getFile()
    {
    	return fl;
    }
public void selectFile(){
    	
 ArrayList<String> im1 = new ArrayList<String>();
 ArrayList<Integer> wd = new ArrayList<Integer>();
 ArrayList<Integer> ht = new ArrayList<Integer>();
frame = new JFrame();
    	int x=0;
        if (dir.isDirectory()) { // make sure it's a directory
            for (final File f : dir.listFiles(IMAGE_FILTER)) {
                BufferedImage img = null;

                try {
                   // img = ImageIO.read(f);

                    // you probably want something more involved here
                    // to display in your UI
                    System.out.println("image: " + f.getName());
                    im1.add(f.getName());
                   // wd.add(img.getWidth());
                    //ht.add(img.getWidth());

                    
                    //System.out.println(" width : " + img.getWidth());
                    //System.out.println(" height: " + img.getHeight());
                    //System.out.println(" size  : " + f.length());
                } catch (final Exception e) {
                    // handle errors here
                }
                
            }
            
          //  for(String s : im1){
            	
            //}
            images = new String[im1.size()+1];
            y=0;
            Iterator<String> it = im1.iterator();
            while(it.hasNext())
            {
            	images[y] = it.next();
            	y++;
            }
            Iterator<Integer> it1 = wd.iterator();
            x=0;
          /*  while(it1.hasNext())
            {
            	images[x] = images[x] +"          "+Integer.toString(it1.next());
            	x++;
            }
            x=0;
            Iterator<Integer> it2 = ht.iterator();

            while(it2.hasNext())
            {
            	images[x] = images[x] +"*"+Integer.toString(it2.next());
            	x++;
            }
            */
            
         list = new JList(images);   
            panel1=new JPanel();
            panel2=new JPanel();

            JScrollPane scroller = new JScrollPane(list);
            scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            scroller.setViewportView(list);

            tf = new JTextField(20);
            
            jb =new JButton("Open");
            jb.addActionListener(new openImg());
            panel2.add(jb);
            panel2.add(tf);
           
          //  jb.addActionListener(new openImg());
            frame.setTitle("Choose Sudoku Image File");
            list.setVisibleRowCount(4);
            //can select onlt one thing
            list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            list.addListSelectionListener(new ListSelectionListener() {
				
				@Override
				public void valueChanged(ListSelectionEvent lse) {
					// TODO Auto-generated method stub
					if(!lse.getValueIsAdjusting()){
			    		String selection = (String)list.getSelectedValue();
			    		System.out.print(selection);
			    		tf.setText(selection);
			    	}
					
				}
			});
          //  panel1.add(list);
           // panel1.add(scroller);
            frame.getContentPane().add(BorderLayout.CENTER,scroller);
            frame.getContentPane().add(BorderLayout.SOUTH,panel2);
          //  frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // Handle window closing
            frame.setSize(500,300);
             frame.setTitle("Sudoku");
             frame.setVisible(false);  
             state = 0;// gui invisible
             
        }
        
        
    }
    
    public void valueChanged(ListSelectionEvent lse){
    	if(!lse.getValueIsAdjusting()){
    		String selection = (String)list.getSelectedValue();
    		System.out.print(selection);
    		tf.setText(selection);
    	}
    	
    }
    
    public class openImg implements ActionListener{
    	public void actionPerformed(ActionEvent e){
    		
    		fl = tf.getText();
        frame.setVisible(false);
 		state=0;

         return;
}
    }
    
}