import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.regex.*;
import javax.swing.JOptionPane;

import java.io.*;

public class notepad extends WindowAdapter implements ActionListener,KeyListener,TextListener{
    public static int n=1;
    Frame f,f2;
    TextArea t1;
    Label l1,l2;
    TextField tf1,tf2;
    MenuBar mb;
    Menu file,edit;
    Pattern p;
    Matcher m;
    String dir=null;
    MenuItem New,Open,Save,SaveAs,Exit,Find,FindandReplace;
    Button b1,b2,b3,b4;
    private boolean saved()
    {
        if(dir==null)
        return false;
        FileInputStream fis;
        try{
            fis=new FileInputStream(new File(dir+f.getTitle()));
        }
        catch(FileNotFoundException e)
        {
            return false;
        }
        long totalsize=new File(dir+f.getTitle()).length();
        String s1=t1.getText();
        if((long)s1.length()!=totalsize)
        return false;
        for(long i=0;i<totalsize;i++)
        {
            long save=0;
            try{
                save=fis.read();
            }
            catch(IOException e)
            {
                System.out.print(e.getMessage());
            }
            if(save!=s1.charAt((int)i))
            {
                try{

                    fis.close();
                }
                catch(IOException e)
                {
                    System.out.print(e.getMessage());
                }
                return false;
            }
        }
        return true;
    }
    public void textValueChanged(TextEvent e)
    {
        if(tf1==null || tf1.getText().equals(""))
        {
            p=null;
            return;
        }
        else
        {
            p=Pattern.compile(tf1.getText());
        }
        if(t1==null || t1.getText().equals(""))
        {
            m=null;
        }
        else
        {
            m=p.matcher(t1.getText());
        }
    }
    public void windowClosing(WindowEvent e)
    {
        boolean stop=(f2==null);
        if(stop)
        actionPerformed(new ActionEvent(f,ActionEvent.ACTION_PERFORMED,"Exit"));
        else
        actionPerformed(new ActionEvent(f2,ActionEvent.ACTION_PERFORMED,"Close"));
    }
    public void actionPerformed(ActionEvent e)
    {
        String str=e.getActionCommand();
        if(str.equals("New"))
        {
            new notepad();
        }
        else if(str.equals("Exit"))
        {
            if(dir==null && t1.getText().equals(""))
            {
                f.setVisible(false);
                f.dispose();
                return;
            }
            if(saved())
            {
                f.setVisible(false);
                f.dispose();
                return;
            }
            int a=JOptionPane.showConfirmDialog(f,"Do you want to save the file");
            if(a==0)
            {
                actionPerformed(new ActionEvent(f,ActionEvent.ACTION_PERFORMED,"Save"));
            }
            else if(a==2 || a==-1)
            {
                return;
            }
            if(f2!=null)
            {
                f2.setVisible(false);
                f2.dispose();
            }
            f.setVisible(false);
            f.dispose();
        }
        else if(str.equals("Open"))
        {
            FileDialog fd=new FileDialog(f,"Open Window",FileDialog.LOAD);
            fd.setVisible(true);
            if(fd.getFile()==null)
            return;
            File file=new File(fd.getDirectory()+fd.getFile());

            if(!file.exists())
            {
                fd.setVisible(false);
                fd.dispose();
                return;
            }
            FileInputStream fis=null;
            try{
                fis=new FileInputStream(file);
            }
            catch(FileNotFoundException fe)
            {
                System.out.println(fe.getMessage());
            }
            int i;

            notepad nw=new notepad();
            nw.dir=fd.getDirectory();
            if(nw.dir=="")
            nw.dir=".";
            nw.f.setTitle(fd.getFile());
            StringBuffer st=new StringBuffer();
            try{
                while((i=fis.read())!=-1)
                {
                    st.append((char)i+"");    
                }
            }
            catch(IOException ie)
            {
                System.out.println(ie.getMessage());
            }
            nw.t1.append(st.toString());
            try{

                fis.close();
            }
            catch(IOException ie)
            {
                System.out.println(ie.getMessage());
            }
        }
        else if(str.equals("Save As"))
        {
            FileDialog fd=new FileDialog(f, "Save As Window", FileDialog.SAVE);
            fd.setVisible(true);
            if(fd.getFile()==null)
            return;
            File file=new File(fd.getDirectory()+fd.getFile());
            dir=fd.getDirectory();
            if(dir==null)
            dir=".";
            file.delete();
            FileOutputStream fos=null;
            try{
                fos=new FileOutputStream(file);
            }
            catch(FileNotFoundException fe)
            {
                System.out.println(fe.getMessage());
            }
            try{
                String st=t1.getText();
                for(int i=0;i<st.length();i++)
                {
                    fos.write((int)st.charAt(i));
                }
            }
            catch(IOException ie)
            {
                System.out.println(ie.getMessage());
            }
            try{

                fos.close();
            }
            catch(IOException ie)
            {
                System.out.println(ie.getMessage());
            }
            f.setTitle(fd.getFile());
            fd.setVisible(false);
            fd.dispose();
        }
        else if(str.equals("Save"))
        {
            if(dir==null || dir=="")
            {
                actionPerformed(new ActionEvent(f,ActionEvent.ACTION_PERFORMED,"Save As"));
            }
            else
            {
                File fl=new File(dir+f.getTitle());
                if(fl.exists())
                {
                    fl.delete();
                }
                FileOutputStream fos=null;
                try{
                    fos=new FileOutputStream(fl, false);
                }
                catch(IOException ie)
                {
                    System.out.println(ie.getMessage());
                }
                try{
                String st=t1.getText();
                for(int i=0;i<st.length();i++)
                {
                    fos.write((int)st.charAt(i));
                }
                }
                catch(IOException ie)
                {
                    System.out.println(ie.getMessage());
                }
                try{

                    fos.close();
                }
                catch(IOException ie)
                {
                    System.out.println(ie.getMessage());
                }
            }
        }
        else if(str.equals("Find"))
        {
            if(f2!=null)
            {
                f2.setVisible(false);
                f2.dispose();
            }
            f2=new Frame("Find Window");
            f2.addWindowListener(this);
            f2.setSize(300,200);
            f2.setLocation(450,0);
            f2.setLayout(new GridBagLayout());
            f2.setVisible(true);
            GridBagConstraints gbc=new GridBagConstraints();
            gbc.gridx=gbc.gridy=0;
            gbc.fill=GridBagConstraints.HORIZONTAL;
            gbc.gridheight=1;
            gbc.gridwidth=2;
            gbc.insets=new Insets(20,0,20,0);
            f2.add(l1=new Label("Find"),gbc);
            gbc.gridx=2;
            f2.add(tf1=new TextField(),gbc);
            tf1.addTextListener(this);
            gbc.gridx=0;
            gbc.gridy=1;
            b1=new Button("Find Next");
            b2=new Button("Close");
            b1.setPreferredSize(new Dimension(100, 30));
            b2.setPreferredSize(new Dimension(100, 30));
            f2.add(b1,gbc);
            gbc.gridx+=2;
            f2.add(b2,gbc);
            b1.addActionListener(this);
            b2.addActionListener(this);
            f2.toFront();
            return;
        }
        else if(str.equals("Find And Replace"))
        {
            if(f2!=null)
            {
                f2.setVisible(false);
                f2.dispose();
            }
            f2=new Frame("Find And Replace Window");
            f2.toFront();
            f2.addWindowListener(this);
            f2.setSize(500,300);
            f2.setLocation(450,0);
            f2.setLayout(new GridBagLayout());
            f2.setVisible(true);
            GridBagConstraints gbc=new GridBagConstraints();
            gbc.gridx=gbc.gridy=0;
            gbc.fill=GridBagConstraints.HORIZONTAL;
            gbc.gridheight=1;
            gbc.gridwidth=2;
            gbc.insets=new Insets(20,20,20,20);
            f2.add(l1=new Label("Find"),gbc);
            gbc.gridx=2;
            f2.add(tf1=new TextField(20),gbc);
            tf1.addTextListener(this);
            gbc.gridx=0;
            gbc.gridy=1;
            f2.add(l2=new Label("Replace With"),gbc);
            gbc.gridx=2;
            f2.add(tf2=new TextField(20),gbc);
            gbc.gridx=0;
            gbc.gridy=2;
            b1=new Button("Find Next");
            b2=new Button("Replace");
            b3=new Button("Replace All");
            b4=new Button("Close");
            gbc.gridwidth=1;
            f2.add(b1,gbc);
            gbc.gridx++;
            f2.add(b2,gbc);
            gbc.gridx++;
            f2.add(b3,gbc);
            gbc.gridx++;
            f2.add(b4,gbc);
            b1.addActionListener(this);
            b2.addActionListener(this);
            b3.addActionListener(this);
            b4.addActionListener(this);
            f2.toFront();
            return;
        }
        if(f2!=null)
        {
            if(str.equals("Close"))
            {
                f2.setVisible(false);
                f2.dispose();
                f2=null;
            }
            else if(str.equals("Find Next"))
            {
                if(p==null)
                {
                    System.out.println("There is nothing to find, Fill the find text field before proceeding.");
                    return;
                }
                if(m==null)
                {
                    System.out.println("The Text File is empty. Add some text before getting started.");
                    return;
                }
                String sear=tf1.getText();
                String st2=t1.getText();
                // for(int i=0;i<st2.length();i++)
                // {
                //     System.out.print(i+": "+st2.charAt(i));
                // }
                int start=-1;
                if(t1.getSelectedText()==null || t1.getSelectedText()=="")
                {
                    start=t1.getCaretPosition();
                }
                else
                {
                    start=t1.getSelectionStart()+1;
                }
                // System.out.print("The start was: "+start+"\n");
                boolean found=false;
                int correction=0;
                for(int i=0;i<start;i++)
                {
                    if(st2.charAt(i)=='\n' || st2.charAt(i)=='\t')
                    correction++;
                }
                for(int i=start;i+sear.length()<st2.length()+1;i++)
                {
                    if(st2.charAt(i)=='\n')
                    {
                        correction++;
                        continue;
                    }
                    if(st2.substring(i,i+sear.length()).equals(sear))
                    {
                        t1.select(i-correction,i-correction+sear.length());
                        // System.out.print("Had to select the region: "+(i-correction)+" to "+(i-correction+sear.length()));
                        found=true;
                        break;
                    }
                }
                if(!found)
                {
                    System.out.println("The search is complete, and we could not find any other occurence of "+sear+" (rerouting to the beginning of the text).");
                    t1.setCaretPosition(0);
                }
            }
            else if(str.equals("Replace"))
            {
                String check=t1.getSelectedText();
                if(check!="")
                {
                    int start=t1.getSelectionStart();
                    int end=t1.getSelectionEnd();
                    if(start==end)
                    return;
                    String text=tf2.getText();
                    if(text==null)
                    {
                        System.out.println("The replace text field cannot be empty.");
                        return;
                    }
                    else
                    {
                        t1.setText(t1.getText().substring(0,start)+text+t1.getText().substring(end));
                        t1.select(start,start);
                    }
                }
                else
                {
                    System.out.println("There is nothing to replace with");
                }
                actionPerformed(new ActionEvent(f, ActionEvent.ACTION_PERFORMED, "Find Next"));
            }
            else if(str.equals("Replace All"))
            {
                if(tf2.getText().equals(""))
                {
                    System.out.println("Replace With Text Field is empty");
                    return;
                }
                String store=m.replaceAll(tf2.getText());
                t1.setText(store);
            }

            f.toFront();
        }
    }
    public void keyPressed(KeyEvent e)
    {
        if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_S) {
            actionPerformed(new ActionEvent(f, ActionEvent.ACTION_PERFORMED, "Save"));
        }
        System.out.print(e.getKeyChar()+","+e.getKeyCode());
    }
    public void keyTyped(KeyEvent e)
    {
    }
    public void keyReleased(KeyEvent e)
    {

    }
    public notepad()
    {
        f=new Frame(encoder());
        f.setSize(400,400);
        t1=new TextArea();
        f.add(t1);
        f.setVisible(true);
        mb=new MenuBar();
        file=new Menu("File");
        edit=new Menu("Edit");

        New=new MenuItem("New");
        Open=new MenuItem("Open");
        Save=new MenuItem("Save");
        SaveAs=new MenuItem("Save As");
        Exit=new MenuItem("Exit");
        Find=new MenuItem("Find");
        FindandReplace=new MenuItem("Find And Replace");

        New.addActionListener(this);
        Open.addActionListener(this);
        Save.addActionListener(this);
        SaveAs.addActionListener(this);
        Exit.addActionListener(this);
        Find.addActionListener(this);
        FindandReplace.addActionListener(this);
        f.addKeyListener(this);
        f.addWindowListener(this);
        t1.addTextListener(this);

        file.add(New);
        file.add(Open);
        file.add(Save);
        file.add(SaveAs);
        file.addSeparator();
        file.add(Exit);
        edit.add(Find);
        edit.add(FindandReplace);
        mb.add(file);
        mb.add(edit);
        f.setMenuBar(mb);

        
    }
    public static void main(String[] args)
    {
        new notepad();
    }
    private static String encoder()
    {
        return "Untitled"+(n++);
    }
}